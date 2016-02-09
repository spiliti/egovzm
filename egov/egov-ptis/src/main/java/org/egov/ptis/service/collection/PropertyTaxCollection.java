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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.service.collection;

import static org.egov.ptis.constants.PropertyTaxConstants.CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DMD_STATUS_CHEQUE_BOUNCED;
import static org.egov.ptis.constants.PropertyTaxConstants.FIRST_REBATETAX_PERC;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODEMAP_FOR_ARREARTAX;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODEMAP_FOR_CURRENTTAX;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODES_FOR_ARREARTAX;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODES_FOR_CURRENTTAX;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODE_FOR_TAXREBATE;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.SECOND_REBATETAX_PERC;
import static org.egov.ptis.constants.PropertyTaxConstants.STR_FOR_CASH;
import static org.egov.ptis.constants.PropertyTaxConstants.STR_FOR_CASH_ADJUSTMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.STR_FOR_SUBMISSION;
import static org.egov.ptis.constants.PropertyTaxConstants.STR_INSTRUMENTTYPE_CHEQUE;
import static org.egov.ptis.constants.PropertyTaxConstants.STR_INSTRUMENTTYPE_DD;
import static org.egov.ptis.constants.PropertyTaxConstants.STR_REALIZATION;
import static org.egov.ptis.constants.PropertyTaxConstants.STR_WITH_AMOUNT;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.integration.models.ReceiptAccountInfo;
import org.egov.collection.integration.models.ReceiptAmountInfo;
import org.egov.collection.integration.models.ReceiptInstrumentInfo;
import org.egov.commons.Installment;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.integration.TaxCollection;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.messaging.MessagingService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.MoneyUtils;
import org.egov.ptis.client.service.CollectionApportioner;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is used to persist Collections .This is used for the integration
 * of Collections and Bills and property tax.
 */
@Transactional
public class PropertyTaxCollection extends TaxCollection {

    private static final Logger LOGGER = Logger.getLogger(PropertyTaxCollection.class);
    private PersistenceService persistenceService;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private Installment currInstallment = null;

    @Autowired
    private ModuleService moduleDao;

    @Autowired
    private EgBillDao egBillDAO;

    @Autowired
    private DemandGenericDao demandGenericDAO;

    @Autowired
    private PersistenceService<Property, Long> propertyImplService;

    @Autowired
    private UserService userService;

    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Autowired
    private MessagingService messagingService;

    @Override
    protected Module module() {
        return moduleDao.getModuleByName(PTMODULENAME);
    }

    @Override
    public void updateDemandDetails(BillReceiptInfo billRcptInfo) throws ApplicationRuntimeException {
        totalAmount = billRcptInfo.getTotalAmount();
        currInstallment = PropertyTaxUtil.getCurrentInstallment();
        LOGGER.debug("updateDemandDetails : Updating Demand Details Started, billRcptInfo : " + billRcptInfo);
        try {
            EgDemand demand = getCurrentDemand(Long.valueOf(billRcptInfo.getBillReferenceNum()));
            String assessmentNo = ((BillReceiptInfoImpl) billRcptInfo).getReceiptMisc().getReceiptHeader()
                    .getConsumerCode();
            LOGGER.info("updateDemandDetails : Demand before proceeding : " + demand);
            LOGGER.info("updateDemandDetails : collection back update started for property : " + assessmentNo
                    + " and receipt event is " + billRcptInfo.getEvent() + ". Total Receipt amount is." + totalAmount
                    + " with receipt no." + billRcptInfo.getReceiptNum());

            if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CREATED)) {
                updateCollForRcptCreate(demand, billRcptInfo);
                activateDemand(demand);
                buildSMS(demand, billRcptInfo);

            } else if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CANCELLED)) {
                updateCollForRcptCancel(demand, billRcptInfo);

            } else if (billRcptInfo.getEvent().equals(EVENT_INSTRUMENT_BOUNCED)) {
                updateCollForChequeBounce(demand, billRcptInfo);
            }
            LOGGER.info("updateDemandDetails : Demand after processed : " + demand);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationRuntimeException("Error occured during back update of DCB : " + e.getMessage(), e);
        }
        LOGGER.debug("updateDemandDetails : Updating Demand Details Finished...");
    }

    private void buildSMS(EgDemand demand, BillReceiptInfo billRcptInfo) {
        Property property = ((Ptdemand) demand).getEgptProperty();
        final User user = property.getBasicProperty().getPrimaryOwner();
        final String mobileNumber = user.getMobileNumber();
        StringBuilder smsMsg = new StringBuilder(100);
        String instNumber = "";
        List<String> instrumentType = new ArrayList<String>();

        for (ReceiptInstrumentInfo instrumentInfo : billRcptInfo.getInstrumentDetails()) {
            instrumentType.add(instrumentInfo.getInstrumentType());
            instNumber = instrumentInfo.getInstrumentNumber();
        }
        if (instrumentType.contains("cheque")) {
            smsMsg.append(STR_INSTRUMENTTYPE_CHEQUE).append(instNumber).append(STR_WITH_AMOUNT)
                    .append(billRcptInfo.getTotalAmount()).append(STR_FOR_SUBMISSION)
                    .append(((BillReceiptInfoImpl) billRcptInfo).getReceiptMisc().getReceiptHeader().getConsumerCode())
                    .append(STR_REALIZATION);
        } else if (instrumentType.contains("dd")) {
            smsMsg.append(STR_INSTRUMENTTYPE_DD).append(instNumber).append(STR_WITH_AMOUNT)
                    .append(billRcptInfo.getTotalAmount()).append(STR_FOR_SUBMISSION)
                    .append(((BillReceiptInfoImpl) billRcptInfo).getReceiptMisc().getReceiptHeader().getConsumerCode())
                    .append(STR_REALIZATION);
        } else if (instrumentType.contains("cash")) {
            smsMsg.append(STR_FOR_CASH)
                    .append(billRcptInfo.getTotalAmount())
                    .append(STR_FOR_CASH_ADJUSTMENT)
                    .append((((BillReceiptInfoImpl) billRcptInfo).getReceiptMisc().getReceiptHeader().getConsumerCode()));
        }

        if (mobileNumber != null)
            messagingService.sendSMS(mobileNumber, smsMsg.toString());

    }

    /**
     * This method is invoked from Collections end when an event related to
     * receipt in bill generation occurs.
     */
    @Override
    public void updateReceiptDetails(Set<BillReceiptInfo> billReceipts) {
        LOGGER.debug("updateReceiptDetails : Updating Receipt Details Started, billReceipts : " + billReceipts);
        Boolean status = false;
        if (billReceipts != null) {
            super.updateReceiptDetails(billReceipts);
        }
        LOGGER.debug("updateReceiptDetails : Updating Receipt Details Finished, status : " + status);
    }

    /**
     * Adds the collected amounts in the appropriate buckets.
     */
    private void updateCollForRcptCreate(EgDemand demand, BillReceiptInfo billRcptInfo) {
        LOGGER.debug("updateCollForRcptCreate : Updating Collection Started For Demand : " + demand
                + " with BillReceiptInfo - " + billRcptInfo);
        LOGGER.info("updateCollForRcptCreate : Total amount collected : " + totalAmount);
        demand.addCollected(totalAmount);
        try {
            if (demand.getMinAmtPayable() != null && demand.getMinAmtPayable().compareTo(BigDecimal.ZERO) > 0) {
                demand.setMinAmtPayable(BigDecimal.ZERO);
            }
            updateDemandDetailForReceiptCreate(billRcptInfo.getAccountDetails(), demand, billRcptInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationRuntimeException(
                    "Error occured during back update of DCB : updateCollForRcptCreate() " + e.getMessage(), e);
        }
        LOGGER.debug("updateCollForRcptCreate : Updating Demand For Collection finished...");
    }

    /**
     * Deducts the collected amounts as per the amount of the cancelled receipt.
     */
    private void updateCollForRcptCancel(EgDemand demand, BillReceiptInfo billRcptInfo) {
        LOGGER.debug("reconcileCollForRcptCancel : Updating Collection Started For Demand : " + demand
                + " with BillReceiptInfo - " + billRcptInfo);
        cancelBill(Long.valueOf(billRcptInfo.getBillReferenceNum()));

        if (demand.getAmtCollected() != null) {
            demand.setAmtCollected(demand.getAmtCollected().subtract(billRcptInfo.getTotalAmount()));
        }

        updateDmdDetForRcptCancel(demand, billRcptInfo);
        LOGGER.debug("reconcileCollForRcptCancel : Updating Collection finished For Demand : " + demand);
    }

    /**
     * Deducts the collected amounts as per the amount of the bounced cheque,
     * and also imposes a cheque-bounce penalty.
     */
    private void updateCollForChequeBounce(EgDemand demand, BillReceiptInfo billRcptInfo) {
        LOGGER.debug("reconcileCollForChequeBounce : Updating Collection Started For Demand : " + demand
                + " with BillReceiptInfo - " + billRcptInfo);
        BigDecimal totalCollChqBounced = getTotalChequeAmt(billRcptInfo);
        BigDecimal chqBouncePenalty = getChqBouncePenaltyAmt(totalCollChqBounced);
        cancelBill(Long.valueOf(billRcptInfo.getBillReferenceNum()));
        EgDemandDetails dmdDet = null;

        EgDemandDetails penaltyDmdDet = getDemandDetail(demand, currInstallment, DEMANDRSN_STR_CHQ_BOUNCE_PENALTY);
        if (penaltyDmdDet == null) {
            dmdDet = insertPenalty(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY, chqBouncePenalty, currInstallment);
        } else {
            BigDecimal existDmdDetAmt = penaltyDmdDet.getAmount();
            existDmdDetAmt = (existDmdDetAmt == null || existDmdDetAmt.equals(BigDecimal.ZERO)) ? existDmdDetAmt = BigDecimal.ZERO
                    : existDmdDetAmt;
            penaltyDmdDet.setAmount(existDmdDetAmt.add(chqBouncePenalty));
            dmdDet = penaltyDmdDet;
        }

        // setting this min amount into demand to check next payment should be
        // min of this amount with mode of payment cash or DD
        demand.setMinAmtPayable(totalCollChqBounced.add(chqBouncePenalty));
        demand.setAmtCollected(demand.getAmtCollected().subtract(billRcptInfo.getTotalAmount()));
        demand.setBaseDemand(demand.getBaseDemand().add(chqBouncePenalty));
        demand.setStatus(DMD_STATUS_CHEQUE_BOUNCED);
        demand.addEgDemandDetails(dmdDet);
        updateDmdDetForRcptCancel(demand, billRcptInfo);
        LOGGER.debug("reconcileCollForChequeBounce : Updating Collection finished For Demand : " + demand);
    }

    /**
     * Update the collection to respective account heads paid
     *
     * @param accountDetails
     * @param demand
     * @param billRcptInfo
     */
    @SuppressWarnings("unchecked")
    private void updateDemandDetailForReceiptCreate(Set<ReceiptAccountInfo> accountDetails, EgDemand demand,
            BillReceiptInfo billRcptInfo) {
        LOGGER.debug("Entering method saveCollectionDetails");

        BigDecimal rebateAmt = BigDecimal.ZERO;

        LOGGER.info("saveCollectionDetails : Start get demandDetailList");

        List<EgDemandDetails> demandDetailList = persistenceService.findAllBy(
                "select dmdet FROM EgDemandDetails dmdet " + "left join fetch dmdet.egDemandReason dmdRsn "
                        + "left join fetch dmdRsn.egDemandReasonMaster dmdRsnMstr "
                        + "left join fetch dmdRsn.egInstallmentMaster installment WHERE dmdet.egDemand = ?", demand);

        LOGGER.info("saveCollectionDetails : End get demandDetailList");

        Map<String, Map<String, EgDemandDetails>> installmentWiseDemandDetailsByReason = new HashMap<String, Map<String, EgDemandDetails>>();
        Map<String, EgDemandDetails> demandDetailByReason = new HashMap<String, EgDemandDetails>();

        EgDemandReason dmdRsn = null;
        String installmentDesc = null;

        for (EgDemandDetails dmdDtls : demandDetailList) {

            if (dmdDtls.getAmount().compareTo(BigDecimal.ZERO) > 0) {

                dmdRsn = dmdDtls.getEgDemandReason();
                installmentDesc = dmdRsn.getEgInstallmentMaster().getDescription();
                demandDetailByReason = new HashMap<String, EgDemandDetails>();

                if (installmentWiseDemandDetailsByReason.get(installmentDesc) == null) {
                    demandDetailByReason.put(dmdRsn.getEgDemandReasonMaster().getReasonMaster(), dmdDtls);
                    installmentWiseDemandDetailsByReason.put(installmentDesc, demandDetailByReason);
                } else {
                    installmentWiseDemandDetailsByReason.get(installmentDesc).put(
                            dmdRsn.getEgDemandReasonMaster().getReasonMaster(), dmdDtls);
                }
            } else {
                LOGGER.info("saveCollectionDetails - demand detail amount is zero " + dmdDtls);
            }
        }

        LOGGER.info("saveCollectionDetails - installment demandDetails size = "
                + installmentWiseDemandDetailsByReason.size());

        EgDemandDetails demandDetail = null;

        for (ReceiptAccountInfo rcptAccInfo : accountDetails) {
            if (rcptAccInfo.getDescription() != null && !rcptAccInfo.getDescription().isEmpty()) {
                if ((rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1)) {
                    String[] desc = rcptAccInfo.getDescription().split("-", 2);
                    String reason = desc[0].trim();
                    String instDesc = desc[1].trim();

                    demandDetail = installmentWiseDemandDetailsByReason.get(instDesc).get(reason);

                    if (rcptAccInfo.getGlCode().equalsIgnoreCase(
                    /* GLCODEMAP_FOR_CURRENTTAX.get( */PropertyTaxConstants.GLCODE_FOR_PENALTY/* ) */)) {

                        if (demandDetail == null) {
                            throw new ApplicationRuntimeException(" Penalty Demand Details is null ");
                        } else {
                            demandDetail.addCollected(rcptAccInfo.getCrAmount());
                        }

                    } else {
                        demandDetail.addCollectedWithOnePaisaTolerance(rcptAccInfo.getCrAmount());
                    }

                    persistCollectedReceipts(demandDetail, billRcptInfo.getReceiptNum(), totalAmount,
                            billRcptInfo.getReceiptDate(), demandDetail.getAmtCollected());
                    LOGGER.info("Persisted demand and receipt details for tax : " + reason + " installment : "
                            + instDesc + " with receipt No : " + billRcptInfo.getReceiptNum() + " for Rs. "
                            + rcptAccInfo.getCrAmount());
                }
            }
        }
        LOGGER.debug("Exiting method saveCollectionDetails");
    }

    /**
     * Return true if Arrear or Current General Tax GlCode or Advance GlCode
     * 
     * @param rcptAccInfo
     * @return true if Arrear or Current General Tax GlCode or Advance GlCode
     *         else false
     */
    private boolean isArrearOrCurrentGenTaxGlCode(String glCode) {
        return glCode.equalsIgnoreCase(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_GENERAL_TAX))
                || glCode.equalsIgnoreCase(GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_GENERAL_TAX));
    }

    /**
     * Reconciles the collection for respective account heads thats been paid
     * with given cancel receipt
     *
     * @param demand
     * @param billRcptInfo
     */
    private void updateDmdDetForRcptCancel(EgDemand demand, BillReceiptInfo billRcptInfo) {
        LOGGER.debug("Entering method updateDmdDetForRcptCancel");
        ReceiptAccountInfo rebateRcptAccInfo = null;

        Map<String, ReceiptAccountInfo> rebateReceiptAccInfoByInstallment = getRebteReceiptAccountInfosByInstallment(billRcptInfo);

        for (ReceiptAccountInfo rcptAccInfo : billRcptInfo.getAccountDetails()) {
            if ((rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1)
                    && !rcptAccInfo.getIsRevenueAccount()) {
                String[] desc = rcptAccInfo.getDescription().split("-", 2);
                String reason = desc[0].trim();
                String installment = desc[1].trim();
                EgDemandReasonMaster demandReasonMaster = null;

                rebateRcptAccInfo = rebateReceiptAccInfoByInstallment.get(installment);

                for (EgDemandDetails demandDetail : demand.getEgDemandDetails()) {

                    demandReasonMaster = demandDetail.getEgDemandReason().getEgDemandReasonMaster();

                    if (reason.equals(demandReasonMaster.getReasonMaster())) {
                        if (reason.equalsIgnoreCase(DEMANDRSN_CODE_ADVANCE)
                                || installment.equals(demandDetail.getEgDemandReason().getEgInstallmentMaster()
                                        .getDescription())) {

                            if (rebateRcptAccInfo != null) {
                                if (demandDetail.getAmtRebate().compareTo(BigDecimal.ZERO) > 0
                                        && (demandReasonMaster.getCode().equals(DEMANDRSN_CODE_GENERAL_TAX) || demandReasonMaster
                                                .getCode().equalsIgnoreCase(DEMANDRSN_CODE_ADVANCE))) {
                                    demandDetail.setAmtRebate(demandDetail.getAmtRebate().subtract(
                                            rebateRcptAccInfo.getDrAmount()));
                                }
                            }

                            if (demandDetail.getAmtCollected().compareTo(rcptAccInfo.getCrAmount()) < 0) {
                                throw new ApplicationRuntimeException(
                                        "updateDmdDetForRcptCancel : Exception while updating cancel receipt, "
                                                + "to be deducted amount " + rcptAccInfo.getCrAmount()
                                                + " is greater than the collected amount "
                                                + demandDetail.getAmtCollected() + " for demandDetail " + demandDetail);
                            }

                            demandDetail.setAmtCollected(demandDetail.getAmtCollected().subtract(
                                    rcptAccInfo.getCrAmount()));

                            LOGGER.info("Deducted Collected amount Rs." + rcptAccInfo.getCrAmount() + " for tax : "
                                    + reason + " and installment : " + installment);
                        }
                    }
                }
            }
        }
        updateReceiptStatusWhenCancelled(billRcptInfo.getReceiptNum());
        LOGGER.debug("Exiting method updateDmdDetForRcptCancel");
    }

    /**
     * Returns a map of Installment description and ReceiptAccountInfo
     * 
     * @param billRcptInfo
     * @return
     */
    private Map<String, ReceiptAccountInfo> getRebteReceiptAccountInfosByInstallment(BillReceiptInfo billRcptInfo) {
        Map<String, ReceiptAccountInfo> rebateReceiptAccInfoByInstallment = new HashMap<String, ReceiptAccountInfo>();

        for (ReceiptAccountInfo rcptAccInfo : billRcptInfo.getAccountDetails()) {
            if (rcptAccInfo.getGlCode().equalsIgnoreCase(GLCODE_FOR_TAXREBATE)
                    || rcptAccInfo.getGlCode().equalsIgnoreCase(PropertyTaxConstants.GLCODE_FOR_ADVANCE_REBATE)) {
                rebateReceiptAccInfoByInstallment
                        .put(rcptAccInfo.getDescription().split("-", 2)[1].trim(), rcptAccInfo);
            }
        }

        return rebateReceiptAccInfoByInstallment;
    }

    @Override
    public void apportionCollection(String billRefNo, BigDecimal amtPaid, List<ReceiptDetail> receiptDetails) {
        boolean isEligibleForCurrentRebate = false;
        boolean isEligibleForAdvanceRebate = false;

        if (isRebatePeriodActive()) {
            isEligibleForCurrentRebate = true;
        }

        CollectionApportioner apportioner = new CollectionApportioner(isEligibleForCurrentRebate,
                isEligibleForAdvanceRebate, BigDecimal.ZERO);
        Map<String, BigDecimal> instDemand = getInstDemand(receiptDetails);
        apportioner.apportion(amtPaid, receiptDetails, instDemand);
    }

    private EgDemand cancelBill(Long billId) {
        EgDemand egDemand = null;
        if (billId != null) {
            EgBill egBill = egBillDAO.findById(billId, false);
            egBill.setIs_Cancelled("Y");
        }
        return egDemand;
    }

    /**
     * Checks if we are within a rebate period.
     *
     * @return
     */
    public static boolean isRebatePeriodActive() {
        boolean isActive = false;
        Date today = new Date();
        Calendar dateWithRbtDays = Calendar.getInstance();
        int currMonth = dateWithRbtDays.get(Calendar.MONTH);
        if (currMonth <= 2) {
            dateWithRbtDays.set(Calendar.YEAR, dateWithRbtDays.get(Calendar.YEAR) - 1);
        }
        dateWithRbtDays.set(Calendar.DAY_OF_MONTH, 30);
        dateWithRbtDays.set(Calendar.MONTH, Calendar.NOVEMBER);
        dateWithRbtDays.set(Calendar.HOUR_OF_DAY, 23);
        dateWithRbtDays.set(Calendar.MINUTE, 59);
        dateWithRbtDays.set(Calendar.SECOND, 59);
        if (today.before(dateWithRbtDays.getTime())) {
            isActive = true;
        }
        return isActive;
    }

    /**
     * Calculates Early Payment Rebate for given Tax Amount
     *
     * @param rebateApplTaxAmt
     *            for which Rebate has to be calculated
     * @return rebate amount.
     */
    public BigDecimal calcEarlyPayRebate(BigDecimal instTaxAmount, BigDecimal rebateApplTaxAmt, BigDecimal collection) {
        BigDecimal rebate = BigDecimal.ZERO;
        Date today = new Date();
        Calendar firstRebateDate = Calendar.getInstance();
        BigDecimal halfYearTax = instTaxAmount.divide(new BigDecimal(2));
        LOGGER.debug("calcEarlyPayRebate instTaxAmount " + instTaxAmount + " halfYearTax " + halfYearTax
                + " rebateApplTaxAmt " + rebateApplTaxAmt + " collection " + collection);
        int currMonth = firstRebateDate.get(Calendar.MONTH);
        if (currMonth <= 2) {
            firstRebateDate.set(Calendar.YEAR, firstRebateDate.get(Calendar.YEAR) - 1);
        }
        firstRebateDate.set(Calendar.DAY_OF_MONTH, 31);
        firstRebateDate.set(Calendar.MONTH, Calendar.MAY);
        firstRebateDate.set(Calendar.HOUR_OF_DAY, 23);
        firstRebateDate.set(Calendar.MINUTE, 59);
        firstRebateDate.set(Calendar.SECOND, 59);

        Calendar secondRebateDate = Calendar.getInstance();
        if (currMonth <= 2) {
            secondRebateDate.set(Calendar.YEAR, secondRebateDate.get(Calendar.YEAR) - 1);
        }
        secondRebateDate.set(Calendar.DAY_OF_MONTH, 30);
        secondRebateDate.set(Calendar.MONTH, Calendar.NOVEMBER);
        secondRebateDate.set(Calendar.HOUR_OF_DAY, 23);
        secondRebateDate.set(Calendar.MINUTE, 59);
        secondRebateDate.set(Calendar.SECOND, 59);

        if (today.before(firstRebateDate.getTime()) || today.equals(firstRebateDate.getTime())) {
            if (collection.compareTo(BigDecimal.ZERO) == 1) {
                if (collection.compareTo(halfYearTax) <= 0) {
                    rebate = MoneyUtils.roundOff((rebateApplTaxAmt.multiply(SECOND_REBATETAX_PERC)).divide(BigDecimal
                            .valueOf(100)));
                } else {
                    rebate = BigDecimal.ZERO;
                }
            } else {
                rebate = MoneyUtils.roundOff((rebateApplTaxAmt.multiply(FIRST_REBATETAX_PERC)).divide(BigDecimal
                        .valueOf(100)));
            }
        } else if (today.before(secondRebateDate.getTime()) || today.equals(secondRebateDate.getTime())) {
            if (collection.compareTo(halfYearTax) <= 0) {
                rebate = MoneyUtils.roundOff((rebateApplTaxAmt.multiply(SECOND_REBATETAX_PERC)).divide(BigDecimal
                        .valueOf(100)));
            } else {
                rebate = BigDecimal.ZERO;
            }
        }
        LOGGER.debug("calcEarlyPayRebate rebate " + rebate);
        return rebate;
    }

    /**
     * Gives the tax amount of Account head for which Rebate applicable
     *
     * @param List
     *            of <code>ReceiptDetail</code>
     * @return rebate applicable tax amount.
     */
    public BigDecimal getRebateApplAmount(List<ReceiptDetail> receiptDetails) {
        BigDecimal taxAmount = BigDecimal.ZERO;
        for (ReceiptDetail rd : receiptDetails) {
            if (rd.getAccounthead().getGlcode().equals(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_GENERAL_TAX))) {
                /*
                 * getting rebate amount from getCramountToBePaid() because
                 * before receipt created CrAmount is Zero and it will updated
                 * as part of receipt creation.
                 */
                taxAmount = rd.getCramountToBePaid();
                break;
            }
        }
        return taxAmount;
    }

    public Map<String, BigDecimal> getInstDemand(List<ReceiptDetail> receiptDetails) {
        Map<String, BigDecimal> retMap = new HashMap<String, BigDecimal>();
        String installment = "";
        String[] desc;

        for (ReceiptDetail rd : receiptDetails) {
            String glCode = rd.getAccounthead().getGlcode();
            installment = "";
            desc = rd.getDescription().split("-", 2);
            installment = desc[1].trim();

            if (!glCode.equalsIgnoreCase(GLCODE_FOR_TAXREBATE)
                    && (GLCODEMAP_FOR_ARREARTAX.containsValue(glCode) || GLCODEMAP_FOR_CURRENTTAX.containsValue(glCode))) {

                if (retMap.get(installment) == null) {
                    retMap.put(installment, rd.getCramountToBePaid());
                } else {
                    retMap.put(installment, retMap.get(installment).add(rd.getCramountToBePaid()));
                }
            }
            if (GLCODES_FOR_CURRENTTAX.contains(glCode) || GLCODES_FOR_ARREARTAX.contains(glCode)) {
                prepareTaxMap(retMap, installment, rd, "FULLTAX");
            } else if (PropertyTaxConstants.GLCODE_FOR_ADVANCE.equalsIgnoreCase(glCode)) {
                prepareTaxMap(retMap, installment, rd, "ADVANCE");
            }
        }
        return retMap;
    }

    /**
     * @param retMap
     * @param installment
     * @param rd
     */
    private void prepareTaxMap(Map<String, BigDecimal> retMap, String installment, ReceiptDetail rd, String type) {
        if (retMap.get(installment + type) == null) {
            retMap.put(installment + type, rd.getCramountToBePaid());
        } else {
            retMap.put(installment + type, retMap.get(installment + type).add(rd.getCramountToBePaid()));
        }
    }

    /**
     * Method used to calculate the Total Cheque amount from he BillreceiptInfo
     * object which is received from Collections Module.
     *
     * @param billRcptInfo
     * @return Total Cheque amount
     * @exception ApplicationRuntimeException
     */

    @Override
    public BigDecimal getTotalChequeAmt(BillReceiptInfo billRcptInfo) {
        BigDecimal totalCollAmt = BigDecimal.ZERO;
        try {
            if (billRcptInfo != null) {
                for (ReceiptInstrumentInfo rctInst : billRcptInfo.getBouncedInstruments()) {
                    if (rctInst.getInstrumentAmount() != null) {
                        totalCollAmt = totalCollAmt.add(rctInst.getInstrumentAmount());
                    }
                }
            }
        } catch (ApplicationRuntimeException e) {
            throw new ApplicationRuntimeException("Exception in calculate Total Collected Amt" + e);
        }

        return totalCollAmt;
    }

    /**
     * Gives the Cheque bounce penalty charges for given cheque amount
     *
     * @param totalChqAmount
     * @return {@link BigDecimal}
     */
    public BigDecimal getChqBouncePenaltyAmt(BigDecimal totalChqAmount) {
        return CHQ_BOUNCE_PENALTY;
    }

    /**
     * Method used to insert penalty in EgDemandDetail table. Penalty Amount
     * will be calculated depending upon the cheque Amount.
     *
     * @see createDemandDetails() -- EgDemand Details are created
     * @see getPenaltyAmount() --Penalty Amount is calculated
     * @param chqBouncePenalty
     * @return New EgDemandDetails Object
     */
    public EgDemandDetails insertPenalty(String demandReason, BigDecimal penaltyAmount, Installment inst) {
        EgDemandDetails demandDetail = null;
        Module ptModule = null;

        if (penaltyAmount != null && penaltyAmount.compareTo(BigDecimal.ZERO) > 0) {

            ptModule = module();
            EgDemandReasonMaster egDemandReasonMaster = demandGenericDAO.getDemandReasonMasterByCode(demandReason,
                    ptModule);

            if (egDemandReasonMaster == null) {
                throw new ApplicationRuntimeException(" Penalty Demand reason Master is null in method  insertPenalty");
            }

            EgDemandReason egDemandReason = demandGenericDAO.getDmdReasonByDmdReasonMsterInstallAndMod(
                    egDemandReasonMaster, inst, ptModule);

            if (egDemandReason == null) {
                throw new ApplicationRuntimeException(" Penalty Demand reason is null in method  insertPenalty ");
            }

            demandDetail = createDemandDetails(egDemandReason, BigDecimal.ZERO, penaltyAmount);
        }
        return demandDetail;
    }

    /**
     * Method used to create new EgDemandDetail Object depending upon the
     * EgDemandReason , Collected amount and Demand amount(which are
     * compulsory),Other wise returns Empty EgDemandDetails Object.
     *
     * @param egDemandReason
     * @param amtCollected
     * @param dmdAmount
     * @return New EgDemandDetails Object
     */

    public EgDemandDetails createDemandDetails(EgDemandReason egDemandReason, BigDecimal amtCollected,
            BigDecimal dmdAmount) {
        return EgDemandDetails.fromReasonAndAmounts(dmdAmount, egDemandReason, amtCollected);
    }

    /**
     * Gives the Current EgDemand for billId
     *
     * @param upicNo
     * @return EgDemand
     */
    @SuppressWarnings("unchecked")
    public EgDemand getCurrentDemand(Long billId) {
        LOGGER.debug("Entered into getCurrentDemand");

        EgBill egBill = (EgBill) egBillDAO.findById(billId, false);

        String query = "SELECT ptd FROM Ptdemand ptd " + "WHERE ptd.egInstallmentMaster = ? "
                + "AND ptd.egptProperty.basicProperty.upicNo = ? "
                + "AND (ptd.egptProperty.status = 'I' OR ptd.egptProperty.status = 'A') "
                + "AND ptd.egptProperty.basicProperty.active = true";

        EgDemand egDemand = (EgDemand) persistenceService.find(query, currInstallment, egBill.getConsumerId());

        LOGGER.debug("Exiting from getCurrentDemand");
        return egDemand;
    }

    /**
     * Method used to insert advance collection in EgDemandDetail table.
     *
     * @see createDemandDetails() -- EgDemand Details are created
     * @return New EgDemandDetails Object
     */
    public EgDemandDetails insertAdvanceCollection(String demandReason, BigDecimal advanceCollectionAmount,
            Installment installment) {
        EgDemandDetails demandDetail = null;

        if (advanceCollectionAmount != null && advanceCollectionAmount.compareTo(BigDecimal.ZERO) > 0) {

            EgDemandReasonMaster egDemandReasonMaster = demandGenericDAO.getDemandReasonMasterByCode(demandReason,
                    module());

            if (egDemandReasonMaster == null) {
                throw new ApplicationRuntimeException(
                        " Advance Demand reason Master is null in method  insertAdvanceCollection");
            }

            EgDemandReason egDemandReason = demandGenericDAO.getDmdReasonByDmdReasonMsterInstallAndMod(
                    egDemandReasonMaster, installment, module());

            if (egDemandReason == null) {
                throw new ApplicationRuntimeException(
                        " Advance Demand reason is null in method  insertAdvanceCollection ");
            }

            demandDetail = createDemandDetails(egDemandReason, advanceCollectionAmount, BigDecimal.ZERO);
        }
        return demandDetail;
    }

    // Activating the demand on payment
    @Transactional
    private void activateDemand(EgDemand demand) {
        Property property = ((Ptdemand) demand).getEgptProperty();
        if (property.getStatus().equals(PropertyTaxConstants.STATUS_DEMAND_INACTIVE)) {
            property.setStatus(PropertyTaxConstants.STATUS_ISACTIVE);
            propertyImplService.persist(property);
        }
    }

    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public List<ReceiptDetail> reconstructReceiptDetail(String billReferenceNumber, BigDecimal actualAmountPaid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String constructAdditionalInfoForReceipt(BillReceiptInfo billReceiptInfo) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public ReceiptAmountInfo receiptAmountBifurcation(BillReceiptInfo billReceiptInfo) {
        return new ReceiptAmountInfo();
    }

}
