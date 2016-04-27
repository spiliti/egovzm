/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.web.controller.lineestimate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.dao.EgwTypeOfWorkHibernateDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.eis.service.DesignationService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.pims.commons.Designation;
import org.egov.services.masters.SchemeService;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.entity.enums.Beneficiary;
import org.egov.works.lineestimate.entity.enums.ModeOfAllotment;
import org.egov.works.lineestimate.entity.enums.TypeOfSlum;
import org.egov.works.lineestimate.entity.enums.WorkCategory;
import org.egov.works.lineestimate.repository.LineEstimateDetailsRepository;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.master.services.NatureOfWorkService;
import org.egov.works.models.estimate.ProjectCode;
import org.egov.works.services.ProjectCodeService;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/lineestimate")
public class CreateSpillOverLineEstimateController {

    @Autowired
    private LineEstimateService lineEstimateService;

    @Autowired
    private FundHibernateDAO fundHibernateDAO;

    @Autowired
    private FunctionHibernateDAO functionHibernateDAO;

    @Autowired
    private BudgetGroupDAO budgetGroupDAO;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private NatureOfWorkService natureOfWorkService;

    @Autowired
    private EgwTypeOfWorkHibernateDAO egwTypeOfWorkHibernateDAO;

    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private LineEstimateDetailsRepository lineEstimateDetailsRepository;

    @Autowired
    private ProjectCodeService projectCodeService;

    @Autowired
    private BudgetDetailsDAO budgetDetailsDAO;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @RequestMapping(value = "/newspilloverform", method = RequestMethod.GET)
    public String showNewSpillOverLineEstimateForm(@ModelAttribute("lineEstimate") final LineEstimate lineEstimate,
            final Model model) throws ApplicationException {
        setDropDownValues(model);

        final List<Department> departments = lineEstimateService.getUserDepartments(securityUtils.getCurrentUser());
        if (departments != null && !departments.isEmpty())
            lineEstimate.setExecutingDepartment(departments.get(0));

        model.addAttribute("lineEstimate", lineEstimate);

        model.addAttribute("mode", null);

        return "spillOverLineEstimate-form";
    }

    @RequestMapping(value = "/create-spillover", method = RequestMethod.POST)
    public String create(@ModelAttribute("lineEstimate") final LineEstimate lineEstimate,
            final Model model, final BindingResult errors, @RequestParam("file") final MultipartFile[] files,
            final RedirectAttributes redirectAttributes, final HttpServletRequest request,
            final BindingResult resultBinder)
                    throws ApplicationException, IOException {

        validateLineEstimateDetails(lineEstimate, errors);
        validateAdminSanctionDetail(lineEstimate, errors);
        validateTechSanctionDetails(lineEstimate, errors);

        final List<AppConfigValues> values = appConfigValuesService.getConfigValuesByModuleAndKey(WorksConstants.EGF_MODULE_NAME,
                WorksConstants.APPCONFIG_KEY_BUDGETCHECK_REQUIRED);
        final AppConfigValues value = values.get(0);
        if (value.getValue().equalsIgnoreCase("Y"))
            validateBudgetAmount(lineEstimate, errors);

        if (errors.hasErrors()) {
            setDropDownValues(model);
            model.addAttribute("mode", null);
            model.addAttribute("designation", request.getParameter("designation"));
            return "spillOverLineEstimate-form";
        } else {
            final LineEstimate newLineEstimate = lineEstimateService.createSpillOver(lineEstimate, files);
            return "redirect:/lineestimate/spillover-lineestimate-success?lineEstimateNumber="
                    + newLineEstimate.getLineEstimateNumber();
        }
    }

    private void validateLineEstimateDetails(final LineEstimate lineEstimate, final BindingResult errors) {
        Integer index = 0;
        for (final LineEstimateDetails led : lineEstimate.getLineEstimateDetails()) {
            final LineEstimateDetails details = lineEstimateDetailsRepository
                    .findByEstimateNumberAndLineEstimate_Status_CodeNot(led.getEstimateNumber(), WorksConstants.CANCELLED_STATUS);
            final ProjectCode projectCode = projectCodeService.findByCode(led.getProjectCode().getCode().toUpperCase());
            if (details != null)
                errors.rejectValue("lineEstimateDetails[" + index + "].estimateNumber", "error.estimatenumber.unique");
            if (projectCode != null)
                errors.rejectValue("lineEstimateDetails[" + index + "].projectCode.code", "error.win.unique");
            index++;
        }
    }

    private void validateTechSanctionDetails(final LineEstimate lineEstimate, final BindingResult errors) {
        if (lineEstimate.getTechnicalSanctionDate() == null)
            errors.rejectValue("technicalSanctionDate", "error.techdate.notnull");
        if (lineEstimate.getTechnicalSanctionDate() != null
                && lineEstimate.getTechnicalSanctionDate().before(lineEstimate.getAdminSanctionDate()))
            errors.rejectValue("technicalSanctionDate", "error.technicalsanctiondate");
        if (lineEstimate.getTechnicalSanctionNumber() == null)
            errors.rejectValue("technicalSanctionNumber", "error.technumber.notnull");
        if (lineEstimate.getTechnicalSanctionNumber() != null) {
            final LineEstimate existingLineEstimate = lineEstimateService.getLineEstimateByTechnicalSanctionNumber(lineEstimate
                    .getTechnicalSanctionNumber());
            if (existingLineEstimate != null)
                errors.rejectValue("technicalSanctionNumber", "error.technumber.unique");
        }
    }

    private void validateAdminSanctionDetail(final LineEstimate lineEstimate, final BindingResult errors) {
        if (lineEstimate.getAdminSanctionDate() != null
                && lineEstimate.getAdminSanctionDate().before(lineEstimate.getLineEstimateDate()))
            errors.rejectValue("adminSanctionDate", "error.adminsanctiondate");
        if (lineEstimate.getCouncilResolutionDate() != null
                && lineEstimate.getCouncilResolutionDate().after(lineEstimate.getAdminSanctionDate()))
            errors.rejectValue("councilResolutionDate", "error.spillover.councilresolutiondate");
        if (StringUtils.isBlank(lineEstimate.getAdminSanctionNumber()))
            errors.rejectValue("adminSanctionNumber", "error.adminsanctionnumber.notnull");
        if (lineEstimate.getAdminSanctionNumber() != null) {
            final LineEstimate checkLineEstimate = lineEstimateService.getLineEstimateByAdminSanctionNumber(lineEstimate
                    .getAdminSanctionNumber());

            if (checkLineEstimate != null)
                errors.rejectValue("adminSanctionNumber", "error.adminsanctionnumber.unique");
        }
    }

    private void setDropDownValues(final Model model) {
        model.addAttribute("funds", fundHibernateDAO.findAllActiveFunds());
        model.addAttribute("functions", functionHibernateDAO.getAllActiveFunctions());
        model.addAttribute("budgetHeads", budgetGroupDAO.getBudgetGroupList());
        model.addAttribute("schemes", schemeService.findAll());
        model.addAttribute("departments", lineEstimateService.getUserDepartments(securityUtils.getCurrentUser()));
        model.addAttribute("workCategory", WorkCategory.values());
        model.addAttribute("typeOfSlum", TypeOfSlum.values());
        model.addAttribute("beneficiary", Beneficiary.values());
        model.addAttribute("modeOfAllotment", ModeOfAllotment.values());
        model.addAttribute("typeOfWork", egwTypeOfWorkHibernateDAO.getTypeOfWorkForPartyTypeContractor());
        model.addAttribute("natureOfWork", natureOfWorkService.findAll());

        final List<Designation> designations = new ArrayList<Designation>();

        final List<AppConfigValues> configValues = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_DESIGNATION_TECHSANCTION_AUTHORITY);

        for (final AppConfigValues value : configValues)
            designations.add(designationService.getDesignationByName(value.getValue()));
        model.addAttribute("designations", designations);
    }

    @RequestMapping(value = "/spillover-lineestimate-success", method = RequestMethod.GET)
    public ModelAndView successView(@RequestParam("lineEstimateNumber") final String lineEstimateNumber, final Model model) {
        final LineEstimate lineEstimate = lineEstimateService.getLineEstimateByLineEstimateNumber(lineEstimateNumber);

        model.addAttribute("message", messageSource.getMessage("msg.spillover.lineestimate.success",
                new String[] { lineEstimate.getLineEstimateNumber(), lineEstimate.getAdminSanctionNumber(),
                        lineEstimate.getTechnicalSanctionNumber() },
                null));

        return new ModelAndView("lineestimate-success");
    }

    private void validateBudgetAmount(final LineEstimate lineEstimate, final BindingResult errors) {
        final List<Long> budgetheadid = new ArrayList<Long>();
        budgetheadid.add(lineEstimate.getBudgetHead().getId());

        try {
            final BigDecimal budgetAvailable = budgetDetailsDAO
                    .getPlanningBudgetAvailable(
                            lineEstimateService.getCurrentFinancialYear(new Date()).getId(),
                            Integer.parseInt(lineEstimate
                                    .getExecutingDepartment().getId().toString()),
                            lineEstimate.getFunction().getId(),
                            null,
                            lineEstimate.getScheme() == null ? null : Integer.parseInt(lineEstimate.getScheme().getId()
                                    .toString()),
                            lineEstimate.getSubScheme() == null ? null : Integer.parseInt(lineEstimate.getSubScheme().getId()
                                    .toString()),
                            null, budgetheadid, Integer.parseInt(lineEstimate.getFund()
                                    .getId().toString()));

            BigDecimal totalAppropriationAmount = BigDecimal.ZERO;

            for (final LineEstimateDetails led : lineEstimate.getLineEstimateDetails())
                if (lineEstimate.isBillsCreated() && led.getGrossAmountBilled() != null)
                    totalAppropriationAmount = totalAppropriationAmount.add(led.getEstimateAmount().subtract(
                            led.getGrossAmountBilled()));
                else
                    totalAppropriationAmount = totalAppropriationAmount.add(led.getEstimateAmount());

            if (budgetAvailable.compareTo(totalAppropriationAmount) == -1)
                errors.reject("error.budgetappropriation.amount",
                        new String[] { budgetAvailable.toString(), totalAppropriationAmount.toString() }, null);
        } catch (final ValidationException e) {
            // TODO: Used ApplicationRuntimeException for time being since there is issue in session after
            // budgetDetailsDAO.getPlanningBudgetAvailable API call
            // TODO: needs to replace with errors.reject
            for (final ValidationError error : e.getErrors())
                throw new ApplicationRuntimeException(error.getKey());
            /*
             * for (final ValidationError error : e.getErrors()) errors.reject(error.getMessage());
             */
        }
    }
}