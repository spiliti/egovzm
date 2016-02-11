/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.ptis.web.controller.reports;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.config.properties.ApplicationProperties;
import org.egov.ptis.domain.entity.property.BillCollectorDailyCollectionReportResult;
import org.egov.ptis.domain.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.GsonBuilder;

/**
 * @author Pradeep
 */
@Controller
@RequestMapping(value = "/reports")
public class BillCollectorWiseDialyCollectionReportController {

    private static final String BILL_COLLECTOR_COLL_REPORT_FORM = "bcDailyCollectionReport-form";

    BillCollectorDailyCollectionReportResult bcDailyCollectionReportResult = new BillCollectorDailyCollectionReportResult();

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ReportService reportService;

    protected @Autowired FinancialYearDAO financialYearDAO;

    @ModelAttribute("previousFinancialYear")
    public CFinancialYear previousFinancialYear() {
        return financialYearDAO.getPreviousFinancialYearByDate(new Date());
    }

    @ModelAttribute("currentFinancialYear")
    public CFinancialYear currentFinancialYear() {
        return financialYearDAO.getFinancialYearByDate(new Date());
    }

    @ModelAttribute("bcDailyCollectionReportResult")
    public BillCollectorDailyCollectionReportResult bcDailyCollectionReportResultModel() {
        return bcDailyCollectionReportResult;
    }

    @RequestMapping(value = "/billcollectorDailyCollectionReport-form", method = RequestMethod.GET)
    public String searchForm(final Model model) {
        return BILL_COLLECTOR_COLL_REPORT_FORM;
    }

    @RequestMapping(value = "/billcollectorDailyCollectionReportList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void search(final HttpServletRequest request, final HttpServletResponse response,
            final Model model) throws IOException {
        IOUtils.write(
                "{ \"data\":"
                        + new GsonBuilder().setDateFormat(applicationProperties.defaultDatePattern()).create()
                                .toJson(reportService.getBillCollectorWiseDailyCollection(new Date())) + "}",
                response.getWriter());
    }

}