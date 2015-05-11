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
package org.egov.works.web.actions.reports;

import java.math.BigDecimal;

public class WorkProgressAbstractBean {
    private String department;
    private Integer estimatesPrepared = 0;
    private BigDecimal estPreparedValue = new BigDecimal(0);
    private Integer adminSancEstimate = 0;
    private BigDecimal adminSancEstValue = new BigDecimal(0);
    private Integer estimateBalance = 0;
    private BigDecimal estBalanceValue = new BigDecimal(0);
    private Integer tenderCalledWP = 0;
    private Integer tenderCalledEst = 0;
    private Integer tenderFinalisedWP = 0;
    private Integer tenderFinalisedEst = 0;
    private Integer workOrderWP = 0;
    private Integer workOrderEst = 0;
    private BigDecimal workOrderAmt = new BigDecimal(0);
    private Integer workNotStartedEst = 0;
    private BigDecimal workNotStartedAmt = new BigDecimal(0);
    private Integer workStartedEst = 0;
    private BigDecimal workStartedAmt = new BigDecimal(0);
    private Integer inProgress25 = 0;
    private Integer inProgress50 = 0;
    private Integer inProgress75 = 0;
    private Integer inProgress99 = 0;
    private Integer worksCompleted = 0;
    private Integer worksNotCompleted = 0;
    private Integer voucherCount = 0;
    private Integer numberOfCompletedWorks = 0;
    private BigDecimal valueOfCompletedWorks = new BigDecimal(0);
    private Integer siteHandedOverAndWrkNtStrtdCnt = 0;
    private BigDecimal siteHandedOverAndWrkNtStrtdAmt = new BigDecimal(0);
    private BigDecimal paymentReleased = new BigDecimal(0);
    private BigDecimal budgetAmount = BigDecimal.ZERO;
    private BigDecimal spillOverWorkValue = BigDecimal.ZERO;
    private BigDecimal budgetAvailable = BigDecimal.ZERO;
    private Integer spillOverVoucherCount = 0;
    private BigDecimal spillOverPaymentReleased = BigDecimal.ZERO;
    private Integer totalVoucherCount = 0;
    private BigDecimal totalPaymentReleased = BigDecimal.ZERO;
    private Integer spilloverWorksEstimateCount = 0;
    private Integer tenderYetToBeCalledEstimateCount = 0;
    private Integer tenderYetToBeFinalizedWPCount = 0;
    private Integer tenderYetToBeFinalizedEstCount = 0;
    private Integer approvedMBCount = 0;
    private Integer approvedBillCount = 0;
    private Integer mbCoveredByBillsCount = 0;
    private Integer billsYetToBeCreatedCount = 0;
    private Integer approvedCJVCount = 0;
    private BigDecimal approvedCJVAmount = BigDecimal.ZERO;
    private Integer approvedBPVCount = 0;
    private BigDecimal approvedBPVAmount = BigDecimal.ZERO;
    private Integer concurrenceVoucherCount = 0;
    private BigDecimal concurrencePaymentAmount = BigDecimal.ZERO;
    private Integer approvedCJVForSpilloverCount = 0;
    private BigDecimal approvedCJVForSpilloverAmount = BigDecimal.ZERO;
    private Integer approvedBPVForSpilloverCount = 0;
    private BigDecimal approvedBPVForSpilloverAmount = BigDecimal.ZERO;
    private Integer concurrenceVoucherForSpilloverCount = 0;
    private BigDecimal concurrencePaymentForSpilloverAmount = BigDecimal.ZERO;
    private BigDecimal balanceBudget = BigDecimal.ZERO;
    private BigDecimal balanceBudgetForSpillover = BigDecimal.ZERO;
    private Integer woYetToBeGivenTNCount = 0;
    private Integer woYetToBeGivenEstimateCount = 0;
    private BigDecimal woYetToBeGivenEstimateValue = BigDecimal.ZERO;
    private BigDecimal billsYetToBeCreatedValue = BigDecimal.ZERO;
    private BigDecimal tenderYetToBeCalledEstValue = BigDecimal.ZERO;
    private BigDecimal tenderYetToBeFinalizedEstValue = BigDecimal.ZERO;
    private BigDecimal tenderCalledEstValue = BigDecimal.ZERO;
    private BigDecimal tenderFinalizedEstValue = BigDecimal.ZERO;
    private Integer rcEstimateCount = 0;
    private BigDecimal rcEstimateValue = BigDecimal.ZERO;
    private Integer rcWOCount = 0;
    // For Dashboard Report
    private BigDecimal totalPaymentReleasedForMonth = BigDecimal.ZERO;

    public String getDepartment() {
        return department;
    }

    public BigDecimal getEstPreparedValue() {
        return estPreparedValue;
    }

    public Integer getAdminSancEstimate() {
        return adminSancEstimate;
    }

    public BigDecimal getAdminSancEstValue() {
        return adminSancEstValue;
    }

    public Integer getEstimateBalance() {
        return estimateBalance;
    }

    public BigDecimal getEstBalanceValue() {
        return estBalanceValue;
    }

    public Integer getTenderCalledWP() {
        return tenderCalledWP;
    }

    public Integer getTenderCalledEst() {
        return tenderCalledEst;
    }

    public Integer getTenderFinalisedWP() {
        return tenderFinalisedWP;
    }

    public Integer getTenderFinalisedEst() {
        return tenderFinalisedEst;
    }

    public Integer getWorkOrderWP() {
        return workOrderWP;
    }

    public Integer getWorkOrderEst() {
        return workOrderEst;
    }

    public BigDecimal getWorkOrderAmt() {
        return workOrderAmt;
    }

    public Integer getWorkNotStartedEst() {
        return workNotStartedEst;
    }

    public BigDecimal getWorkNotStartedAmt() {
        return workNotStartedAmt;
    }

    public Integer getWorkStartedEst() {
        return workStartedEst;
    }

    public BigDecimal getWorkStartedAmt() {
        return workStartedAmt;
    }

    public Integer getInProgress25() {
        return inProgress25;
    }

    public Integer getInProgress50() {
        return inProgress50;
    }

    public Integer getInProgress75() {
        return inProgress75;
    }

    public Integer getInProgress99() {
        return inProgress99;
    }

    public Integer getWorksCompleted() {
        return worksCompleted;
    }

    public Integer getWorksNotCompleted() {
        return worksNotCompleted;
    }

    public BigDecimal getPaymentReleased() {
        return paymentReleased;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setEstPreparedValue(BigDecimal estPreparedValue) {
        this.estPreparedValue = estPreparedValue;
    }

    public void setAdminSancEstimate(Integer adminSancEstimate) {
        this.adminSancEstimate = adminSancEstimate;
    }

    public void setAdminSancEstValue(BigDecimal adminSancEstValue) {
        this.adminSancEstValue = adminSancEstValue;
    }

    public void setEstimateBalance(Integer estimateBalance) {
        this.estimateBalance = estimateBalance;
    }

    public void setEstBalanceValue(BigDecimal estBalanceValue) {
        this.estBalanceValue = estBalanceValue;
    }

    public void setTenderCalledWP(Integer tenderCalledWP) {
        this.tenderCalledWP = tenderCalledWP;
    }

    public void setTenderCalledEst(Integer tenderCalledEst) {
        this.tenderCalledEst = tenderCalledEst;
    }

    public void setTenderFinalisedWP(Integer tenderFinalisedWP) {
        this.tenderFinalisedWP = tenderFinalisedWP;
    }

    public void setTenderFinalisedEst(Integer tenderFinalisedEst) {
        this.tenderFinalisedEst = tenderFinalisedEst;
    }

    public void setWorkOrderWP(Integer workOrderWP) {
        this.workOrderWP = workOrderWP;
    }

    public void setWorkOrderEst(Integer workOrderEst) {
        this.workOrderEst = workOrderEst;
    }

    public void setWorkOrderAmt(BigDecimal workOrderAmt) {
        this.workOrderAmt = workOrderAmt;
    }

    public void setWorkNotStartedEst(Integer workNotStartedEst) {
        this.workNotStartedEst = workNotStartedEst;
    }

    public void setWorkNotStartedAmt(BigDecimal workNotStartedAmt) {
        this.workNotStartedAmt = workNotStartedAmt;
    }

    public void setWorkStartedEst(Integer workStartedEst) {
        this.workStartedEst = workStartedEst;
    }

    public void setWorkStartedAmt(BigDecimal workStartedAmt) {
        this.workStartedAmt = workStartedAmt;
    }

    public void setInProgress25(Integer inProgress25) {
        this.inProgress25 = inProgress25;
    }

    public void setInProgress50(Integer inProgress50) {
        this.inProgress50 = inProgress50;
    }

    public void setInProgress75(Integer inProgress75) {
        this.inProgress75 = inProgress75;
    }

    public void setInProgress99(Integer inProgress99) {
        this.inProgress99 = inProgress99;
    }

    public void setWorksCompleted(Integer worksCompleted) {
        this.worksCompleted = worksCompleted;
    }

    public void setWorksNotCompleted(Integer worksNotCompleted) {
        this.worksNotCompleted = worksNotCompleted;
    }

    public void setPaymentReleased(BigDecimal paymentReleased) {
        this.paymentReleased = paymentReleased;
    }

    public Integer getEstimatesPrepared() {
        return estimatesPrepared;
    }

    public void setEstimatesPrepared(Integer estimatesPrepared) {
        this.estimatesPrepared = estimatesPrepared;
    }

    public Integer getVoucherCount() {
        return voucherCount;
    }

    public void setVoucherCount(Integer voucherCount) {
        this.voucherCount = voucherCount;
    }

    public Integer getNumberOfCompletedWorks() {
        return numberOfCompletedWorks;
    }

    public void setNumberOfCompletedWorks(Integer numberOfCompletedWorks) {
        this.numberOfCompletedWorks = numberOfCompletedWorks;
    }

    public BigDecimal getValueOfCompletedWorks() {
        return valueOfCompletedWorks;
    }

    public void setValueOfCompletedWorks(BigDecimal valueOfCompletedWorks) {
        this.valueOfCompletedWorks = valueOfCompletedWorks;
    }

    public Integer getSiteHandedOverAndWrkNtStrtdCnt() {
        return siteHandedOverAndWrkNtStrtdCnt;
    }

    public void setSiteHandedOverAndWrkNtStrtdCnt(Integer siteHandedOverAndWrkNtStrtdCnt) {
        this.siteHandedOverAndWrkNtStrtdCnt = siteHandedOverAndWrkNtStrtdCnt;
    }

    public BigDecimal getSiteHandedOverAndWrkNtStrtdAmt() {
        return siteHandedOverAndWrkNtStrtdAmt;
    }

    public void setSiteHandedOverAndWrkNtStrtdAmt(BigDecimal siteHandedOverAndWrkNtStrtdAmt) {
        this.siteHandedOverAndWrkNtStrtdAmt = siteHandedOverAndWrkNtStrtdAmt;
    }

    public BigDecimal getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(BigDecimal budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public BigDecimal getSpillOverWorkValue() {
        return spillOverWorkValue;
    }

    public void setSpillOverWorkValue(BigDecimal spillOverWorkValue) {
        this.spillOverWorkValue = spillOverWorkValue;
    }

    public Integer getSpillOverVoucherCount() {
        return spillOverVoucherCount;
    }

    public void setSpillOverVoucherCount(Integer spillOverVoucherCount) {
        this.spillOverVoucherCount = spillOverVoucherCount;
    }

    public BigDecimal getSpillOverPaymentReleased() {
        return spillOverPaymentReleased;
    }

    public void setSpillOverPaymentReleased(BigDecimal spillOverPaymentReleased) {
        this.spillOverPaymentReleased = spillOverPaymentReleased;
    }

    public Integer getTotalVoucherCount() {
        return totalVoucherCount;
    }

    public void setTotalVoucherCount(Integer totalVoucherCount) {
        this.totalVoucherCount = totalVoucherCount;
    }

    public BigDecimal getTotalPaymentReleased() {
        return totalPaymentReleased;
    }

    public void setTotalPaymentReleased(BigDecimal totalPaymentReleased) {
        this.totalPaymentReleased = totalPaymentReleased;
    }

    public BigDecimal getBudgetAvailable() {
        return budgetAvailable;
    }

    public void setBudgetAvailable(BigDecimal budgetAvailable) {
        this.budgetAvailable = budgetAvailable;
    }

    public Integer getSpilloverWorksEstimateCount() {
        return spilloverWorksEstimateCount;
    }

    public void setSpilloverWorksEstimateCount(Integer spilloverWorksEstimateCount) {
        this.spilloverWorksEstimateCount = spilloverWorksEstimateCount;
    }

    public Integer getTenderYetToBeCalledEstimateCount() {
        return tenderYetToBeCalledEstimateCount;
    }

    public Integer getTenderYetToBeFinalizedWPCount() {
        return tenderYetToBeFinalizedWPCount;
    }

    public Integer getTenderYetToBeFinalizedEstCount() {
        return tenderYetToBeFinalizedEstCount;
    }

    public Integer getApprovedMBCount() {
        return approvedMBCount;
    }

    public Integer getApprovedBillCount() {
        return approvedBillCount;
    }

    public Integer getMbCoveredByBillsCount() {
        return mbCoveredByBillsCount;
    }

    public Integer getBillsYetToBeCreatedCount() {
        return billsYetToBeCreatedCount;
    }

    public Integer getApprovedCJVCount() {
        return approvedCJVCount;
    }

    public BigDecimal getApprovedCJVAmount() {
        return approvedCJVAmount;
    }

    public Integer getConcurrenceVoucherCount() {
        return concurrenceVoucherCount;
    }

    public BigDecimal getConcurrencePaymentAmount() {
        return concurrencePaymentAmount;
    }

    public Integer getApprovedCJVForSpilloverCount() {
        return approvedCJVForSpilloverCount;
    }

    public BigDecimal getApprovedCJVForSpilloverAmount() {
        return approvedCJVForSpilloverAmount;
    }

    public Integer getConcurrenceVoucherForSpilloverCount() {
        return concurrenceVoucherForSpilloverCount;
    }

    public BigDecimal getConcurrencePaymentForSpilloverAmount() {
        return concurrencePaymentForSpilloverAmount;
    }

    public BigDecimal getBalanceBudget() {
        return balanceBudget;
    }

    public BigDecimal getBalanceBudgetForSpillover() {
        return balanceBudgetForSpillover;
    }

    public void setTenderYetToBeCalledEstimateCount(Integer tenderYetToBeCalledEstimateCount) {
        this.tenderYetToBeCalledEstimateCount = tenderYetToBeCalledEstimateCount;
    }

    public void setTenderYetToBeFinalizedWPCount(Integer tenderYetToBeFinalizedWPCount) {
        this.tenderYetToBeFinalizedWPCount = tenderYetToBeFinalizedWPCount;
    }

    public void setTenderYetToBeFinalizedEstCount(Integer tenderYetToBeFinalizedEstCount) {
        this.tenderYetToBeFinalizedEstCount = tenderYetToBeFinalizedEstCount;
    }

    public void setApprovedMBCount(Integer approvedMBCount) {
        this.approvedMBCount = approvedMBCount;
    }

    public void setApprovedBillCount(Integer approvedBillCount) {
        this.approvedBillCount = approvedBillCount;
    }

    public void setMbCoveredByBillsCount(Integer mbCoveredByBillsCount) {
        this.mbCoveredByBillsCount = mbCoveredByBillsCount;
    }

    public void setBillsYetToBeCreatedCount(Integer billsYetToBeCreatedCount) {
        this.billsYetToBeCreatedCount = billsYetToBeCreatedCount;
    }

    public void setApprovedCJVCount(Integer approvedCJVCount) {
        this.approvedCJVCount = approvedCJVCount;
    }

    public void setApprovedCJVAmount(BigDecimal approvedCJVAmount) {
        this.approvedCJVAmount = approvedCJVAmount;
    }

    public void setConcurrenceVoucherCount(Integer concurrenceVoucherCount) {
        this.concurrenceVoucherCount = concurrenceVoucherCount;
    }

    public void setConcurrencePaymentAmount(BigDecimal concurrencePaymentAmount) {
        this.concurrencePaymentAmount = concurrencePaymentAmount;
    }

    public void setApprovedCJVForSpilloverCount(Integer approvedCJVForSpilloverCount) {
        this.approvedCJVForSpilloverCount = approvedCJVForSpilloverCount;
    }

    public void setApprovedCJVForSpilloverAmount(BigDecimal approvedCJVForSpilloverAmount) {
        this.approvedCJVForSpilloverAmount = approvedCJVForSpilloverAmount;
    }

    public void setConcurrenceVoucherForSpilloverCount(Integer concurrenceVoucherForSpilloverCount) {
        this.concurrenceVoucherForSpilloverCount = concurrenceVoucherForSpilloverCount;
    }

    public void setConcurrencePaymentForSpilloverAmount(BigDecimal concurrencePaymentForSpilloverAmount) {
        this.concurrencePaymentForSpilloverAmount = concurrencePaymentForSpilloverAmount;
    }

    public void setBalanceBudget(BigDecimal balanceBudget) {
        this.balanceBudget = balanceBudget;
    }

    public void setBalanceBudgetForSpillover(BigDecimal balanceBudgetForSpillover) {
        this.balanceBudgetForSpillover = balanceBudgetForSpillover;
    }

    public Integer getApprovedBPVCount() {
        return approvedBPVCount;
    }

    public void setApprovedBPVCount(Integer approvedBPVCount) {
        this.approvedBPVCount = approvedBPVCount;
    }

    public BigDecimal getApprovedBPVAmount() {
        return approvedBPVAmount;
    }

    public Integer getApprovedBPVForSpilloverCount() {
        return approvedBPVForSpilloverCount;
    }

    public BigDecimal getApprovedBPVForSpilloverAmount() {
        return approvedBPVForSpilloverAmount;
    }

    public void setApprovedBPVAmount(BigDecimal approvedBPVAmount) {
        this.approvedBPVAmount = approvedBPVAmount;
    }

    public void setApprovedBPVForSpilloverCount(Integer approvedBPVForSpilloverCount) {
        this.approvedBPVForSpilloverCount = approvedBPVForSpilloverCount;
    }

    public void setApprovedBPVForSpilloverAmount(BigDecimal approvedBPVForSpilloverAmount) {
        this.approvedBPVForSpilloverAmount = approvedBPVForSpilloverAmount;
    }

    public Integer getWoYetToBeGivenTNCount() {
        return woYetToBeGivenTNCount;
    }

    public void setWoYetToBeGivenTNCount(Integer woYetToBeGivenTNCount) {
        this.woYetToBeGivenTNCount = woYetToBeGivenTNCount;
    }

    public Integer getWoYetToBeGivenEstimateCount() {
        return woYetToBeGivenEstimateCount;
    }

    public void setWoYetToBeGivenEstimateCount(Integer woYetToBeGivenEstimateCount) {
        this.woYetToBeGivenEstimateCount = woYetToBeGivenEstimateCount;
    }

    public BigDecimal getWoYetToBeGivenEstimateValue() {
        return woYetToBeGivenEstimateValue;
    }

    public void setWoYetToBeGivenEstimateValue(BigDecimal woYetToBeGivenEstimateValue) {
        this.woYetToBeGivenEstimateValue = woYetToBeGivenEstimateValue;
    }

    public BigDecimal getBillsYetToBeCreatedValue() {
        return billsYetToBeCreatedValue;
    }

    public void setBillsYetToBeCreatedValue(BigDecimal billsYetToBeCreatedValue) {
        this.billsYetToBeCreatedValue = billsYetToBeCreatedValue;
    }

    public BigDecimal getTenderYetToBeCalledEstValue() {
        return tenderYetToBeCalledEstValue;
    }

    public BigDecimal getTenderYetToBeFinalizedEstValue() {
        return tenderYetToBeFinalizedEstValue;
    }

    public void setTenderYetToBeCalledEstValue(BigDecimal tenderYetToBeCalledEstValue) {
        this.tenderYetToBeCalledEstValue = tenderYetToBeCalledEstValue;
    }

    public void setTenderYetToBeFinalizedEstValue(BigDecimal tenderYetToBeFinalizedEstValue) {
        this.tenderYetToBeFinalizedEstValue = tenderYetToBeFinalizedEstValue;
    }

    public BigDecimal getTenderCalledEstValue() {
        return tenderCalledEstValue;
    }

    public void setTenderCalledEstValue(BigDecimal tenderCalledEstValue) {
        this.tenderCalledEstValue = tenderCalledEstValue;
    }

    public BigDecimal getTenderFinalizedEstValue() {
        return tenderFinalizedEstValue;
    }

    public void setTenderFinalizedEstValue(BigDecimal tenderFinalizedEstValue) {
        this.tenderFinalizedEstValue = tenderFinalizedEstValue;
    }

    public Integer getRcEstimateCount() {
        return rcEstimateCount;
    }

    public void setRcEstimateCount(Integer rcEstimateCount) {
        this.rcEstimateCount = rcEstimateCount;
    }

    public BigDecimal getRcEstimateValue() {
        return rcEstimateValue;
    }

    public void setRcEstimateValue(BigDecimal rcEstimateValue) {
        this.rcEstimateValue = rcEstimateValue;
    }

    public Integer getRcWOCount() {
        return rcWOCount;
    }

    public void setRcWOCount(Integer rcWOCount) {
        this.rcWOCount = rcWOCount;
    }

    public BigDecimal getTotalPaymentReleasedForMonth() {
        return totalPaymentReleasedForMonth;
    }

    public void setTotalPaymentReleasedForMonth(BigDecimal totalPaymentReleasedForMonth) {
        this.totalPaymentReleasedForMonth = totalPaymentReleasedForMonth;
    }

}