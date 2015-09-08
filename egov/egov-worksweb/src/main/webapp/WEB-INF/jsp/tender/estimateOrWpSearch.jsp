<!-- -------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency,
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It
# 	   is required that all modified versions of this material be marked in
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program
# 	   with regards to rights under trademark law for use of the trade names
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<script type="text/javascript">
   function viewSearchPage(val){
 
    if(val.value="wp"){
  	 document.forms[0].tenderForEst.checked=false;
  	  document.forms[0].tenderForWp.checked=true;
    }else if(val.value="estimate"){  
   	 document.forms[0].tenderForWp.checked=false;
   	 document.forms[0].tenderForEst.checked=true;
    }
     	
   	if(val !='' && val =='estimate'){  
 	window.open('${pageContext.request.contextPath}/estimate/searchEstimate.action?source=createNegotiationNew&option=menu','_self');
   	}
   	else
   	{
   		window.open('${pageContext.request.contextPath}/tender/searchWorksPackage.action?source=createNegotiationForWP&option=menu','_self');
   	}		
   }
 </script>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
 <tr>
    <td width="11%" class="whiteboxwk">Select Estimate:</td>
	<td width="21%" class="whitebox2wk">
	   <input name="tenderForEst" type="radio" id="tenderForEst" onClick="viewSearchPage('estimate');"/>
	</td>
	<td width="11%" class="whiteboxwk">Select  Works package:</td>
	<td width="21%" class="whitebox2wk">
	  <input name="tenderForWp" type="radio" id="tenderForWp" onClick="viewSearchPage('wp');"/>
	</td>  
 </tr>	
</table>
