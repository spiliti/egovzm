/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.lcms.transactions.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.hibernate.validator.constraints.Length;

/**
 * Appeal entity.
 *
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EGLC_APPEAL")
@SequenceGenerator(name = Appeal.SEQ_EGLC_APPEAL, sequenceName = Appeal.SEQ_EGLC_APPEAL, allocationSize = 1)
public class Appeal extends AbstractAuditable {

    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_EGLC_APPEAL = "SEQ_EGLC_APPEAL";

    @Id
    @GeneratedValue(generator = SEQ_EGLC_APPEAL, strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "JUDGMENTIMPL")
    private JudgmentImpl judgmentImpl;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "STATUS")
    private EgwStatus status;
    @Required(message = "srnumber.null")
    @Length(max = 50, message = "srnumber.length")
    @OptionalPattern(regex = LcmsConstants.alphaNumeric, message = "srnumber.alpha")
    private String srNumber;
    @Required(message = "appealfiledon.null")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.appeal.date")
    private Date appealFiledOn;
    @Required(message = "appealfiledby.null")
    @Length(max = 100, message = "appealfiledby.length")
    @OptionalPattern(regex = LcmsConstants.alphaNumeric, message = "appealfiledby.alpha")
    private String appealFiledBy;

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(final EgwStatus status) {
        this.status = status;
    }

    public String getSrNumber() {
        return srNumber;
    }

    public void setSrNumber(final String srNumber) {
        this.srNumber = srNumber;
    }

    public Date getAppealFiledOn() {
        return appealFiledOn;
    }

    public void setAppealFiledOn(final Date appealFiledOn) {
        this.appealFiledOn = appealFiledOn;
    }

    public String getAppealFiledBy() {
        return appealFiledBy;
    }

    public void setAppealFiledBy(final String appealFiledBy) {
        this.appealFiledBy = appealFiledBy;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        if (getAppealFiledOn() != null
                && !DateUtils.compareDates(getAppealFiledOn(), getJudgmentImpl().getJudgment().getOrderDate()))
            errors.add(new ValidationError("appealfiledon", "appealfiledon.less.orderDate"));
        return errors;
    }

    public JudgmentImpl getJudgmentImpl() {
        return judgmentImpl;
    }

    public void setJudgmentImpl(JudgmentImpl judgmentImpl) {
        this.judgmentImpl = judgmentImpl;
    }
}