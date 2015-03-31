/*
 * @(#)WorkflowTypeService.java 3.0, 17 Jun, 2013 4:44:11 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infra.workflow.inbox;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.infra.workflow.entity.StateAware;

public interface InboxRenderService<T extends StateAware> {
	
	String WFTYPE = "wfType";
	String SENDER = "sender";
	String OWNER = "owner";
	String FROMDATE = "fromDate";
	String TODATE = "toDate";
	String WFSTATE = "wfState";
	String IDENTIFIER = "identifier";
	String SEARCH_OP = "searchOp";
	String INBOX_RENDER_SERVICE_SUFFIX = "InboxRenderService";
	Character RENDER_Y = Character.valueOf('Y');
	Character GROUP_Y = Character.valueOf('Y');
	    
	/**
	 * Returns a list of workflow items that are assigned to the given position where the workflow
	 * has not ended.
	 * @param owner the owner
	 * @param userId the user id
	 * @param order the order
	 * @return the assigned workflow items
	 */
	List<T> getAssignedWorkflowItems(Long owner, Long userId, String order);
	
	/**
	 * Returns a list of workflow items that are created by the given position and the state in
	 * 'NEW'.
	 * @param owner the owner
	 * @param userId the user id
	 * @param order the order
	 * @return the draft workflow items
	 */
	List<T> getDraftWorkflowItems(Long owner, Long userId, String order);
	
	/**
	 * Gets the filtered workflow items.
	 * @param owner the owner
	 * @param userId the user id
	 * @param sender the sender
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the filtered workflow items
	 */
	List<T> getFilteredWorkflowItems(Long owner, Long userId, Long sender, Date fromDate, Date toDate);
	
	
	/**
	 * Returns a list of workflow items for the given criteria
	 * @param criteria Map of criteria fields<br>
	 * Mandatory key in criteria <br>
	 * 1] wfType {@link InboxRenderService#WFTYPE} the StateAware simple class name (without package structure) <br> 
	 * Other non mandatory keys in criteria <br>
	 * 1] sender {@link InboxRenderService#SENDER}: List of Positions that have created the workflow items <br>
	 * 2] owner {@link InboxRenderService#OWNER}: List of Positions to whom the workflow items are currently assigned <br>
	 * 3] fromDate {@link InboxRenderService#FROMDATE}: From date for fetching the workflow items (based on creation time stamp) <br>
	 * 4] toDate {@link InboxRenderService#TODATE}: To date for fetching the workflow items (based on creation time stamp) <br>
	 * 5] wfState {@link InboxRenderService#WFSTATE}: StateAware current state value <br>
	 * @return the workflow items
	 */
	List<T> getWorkflowItems(Map<String,Object> criteria);
	
	/**
	 * Populate all Workflow Items using the myLinkedId value.
	 * @param myLinkId the StateAware myLinkId property
	 * @return all StateAware
	 */
	List<T> getWorkflowItems(String myLinkId);
	
}
