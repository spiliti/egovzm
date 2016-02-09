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
package org.egov.adtax.web.controller.hoarding;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Date;
import java.util.List;

import org.egov.adtax.entity.AdvertisementBatchDemandGenerate;
import org.egov.adtax.search.contract.HoardingSearch;
import org.egov.adtax.service.AdvertisementBatchDemandGenService;
import org.egov.adtax.service.AdvertisementRateService;
import org.egov.adtax.web.controller.GenericController;
import org.egov.commons.CFinancialYear;
import org.egov.commons.repository.CFinancialYearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hoarding")
public class generateDemandHoardingController extends GenericController {

    @Autowired
    private AdvertisementRateService advertisementRateService;

    private @Autowired CFinancialYearRepository cFinancialYearRepository;
    @Autowired
    private AdvertisementBatchDemandGenService advertisementBatchDemandGenService;

    public @ModelAttribute("financialYears") List<CFinancialYear> financialyear() {

        return advertisementRateService.getAllFinancialYears();
    }

    @RequestMapping(value = "/generate-search", method = GET)
    public String search(@ModelAttribute final HoardingSearch hoardingSearch) {
        return "hoarding-generate";
    }

    @RequestMapping(value = "generate-search", method = POST)
    public String searchHoarding(@ModelAttribute final HoardingSearch hoardingSearch,
            final RedirectAttributes redirectAttributes, final BindingResult resultBinder) {

        CFinancialYear cFinancialYear = null;

        if (hoardingSearch.getFinancialYear() != null) {
            cFinancialYear = cFinancialYearRepository.getOne(Long.valueOf(hoardingSearch.getFinancialYear()));
        } else {
            resultBinder.rejectValue("financialYear", "*");
            return "hoarding-generate";
        }

        if (cFinancialYear != null) {
            AdvertisementBatchDemandGenerate advBatchDmdGenerate = new AdvertisementBatchDemandGenerate();
            advBatchDmdGenerate.setActive(true);
            advBatchDmdGenerate.setFinancialYear(cFinancialYear);
            advBatchDmdGenerate.setJobName("Generate Demand For " + cFinancialYear.getFinYearRange() + new Date());
            advertisementBatchDemandGenService.createAdvertisementBatchDemandGenerate(advBatchDmdGenerate);
        }

        redirectAttributes.addFlashAttribute("message", "msg.demand.Scheduled");

        return "generateDemand-success";
    }

}
