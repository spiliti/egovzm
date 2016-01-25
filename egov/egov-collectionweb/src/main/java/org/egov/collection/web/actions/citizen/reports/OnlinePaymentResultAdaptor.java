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
 *     it under the terms of the GnU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT AnY WARRAnTY; without even the implied warranty of
 *     MERCHAnTABILITY or FITnESS FOR A PARTICULAR PURPOSE.  See the
 *     GnU General Public License for more details.
 *
 *     You should have received a copy of the GnU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.collection.web.actions.citizen.reports;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import org.egov.collection.entity.OnlinePaymentResult;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class OnlinePaymentResultAdaptor implements JsonSerializer<OnlinePaymentResult> {

    @Override
    public JsonElement serialize(final OnlinePaymentResult OnlinePaymentResultObj, final Type type,
            final JsonSerializationContext jsc) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ulbname", OnlinePaymentResultObj.getUlbname());
        jsonObject.addProperty("districtname", OnlinePaymentResultObj.getDistrictname());
        jsonObject.addProperty("receiptdate", OnlinePaymentResultObj.getReceiptdate().toString());
        jsonObject.addProperty("referenceid", OnlinePaymentResultObj.getReferenceid());
        jsonObject.addProperty("receiptnumber", OnlinePaymentResultObj.getReceiptnumber() == null ? ""
                : OnlinePaymentResultObj.getReceiptnumber());
        jsonObject.addProperty("servicename", OnlinePaymentResultObj.getServicename());
        jsonObject.addProperty("totalamount", OnlinePaymentResultObj.getTotalamount());
        jsonObject.addProperty("transactionnumber", OnlinePaymentResultObj.getTransactionnumber() == null ? ""
                : OnlinePaymentResultObj.getTransactionnumber());
        jsonObject.addProperty("onlineservicename", OnlinePaymentResultObj.getOnlineservicename());
        jsonObject.addProperty(
                "transactiondate",
                OnlinePaymentResultObj.getTransactiondate() == null ? "" : sdf.format(OnlinePaymentResultObj
                        .getTransactiondate()));
        jsonObject.addProperty("status", OnlinePaymentResultObj.getStatus());
        jsonObject.addProperty("payeename",
                OnlinePaymentResultObj.getPayeename() == null ? "" : OnlinePaymentResultObj.getPayeename());
        return jsonObject;
    }

}