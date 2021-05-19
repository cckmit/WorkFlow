/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author User
 */
@Component
public class DateAuditCrossCheck {

    private static final Logger LOG = Logger.getLogger(DateAuditCrossCheck.class.getName());

    @Autowired
    GITConfig gITConfig;
    @Autowired
    CheckoutSegmentsDAO checkoutSegmentsDAO;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    SSHClientUtils sSHClientUtils;

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public CheckoutSegmentsDAO getCheckoutSegmentsDAO() {
	return checkoutSegmentsDAO;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public String dateAutditForMigration(User user, ImpPlan plan) {

	List<SystemLoad> lSystemLoads = plan.getSystemLoadList();

	if (lSystemLoads != null) {
	    for (SystemLoad lSystemLoad : lSystemLoads) {
		if (lSystemLoad.getActive() != null && lSystemLoad.getActive().equalsIgnoreCase("Y")) {
		    JSONResponse lCommandResponse = new JSONResponse();

		    if (lSystemLoad.getSystemId() != null && lSystemLoad.getLoadDateTime() == null) {
			LOG.error("Load Date Time for the System is NULL");
			throw new WorkflowException("Load Date Time is Null, Unable to Submit/Update Implementation Plan");
		    }

		    List<CheckoutSegments> lSegmentList = getCheckoutSegmentsDAO().findBySystemLoad(lSystemLoad.getId());
		    List<String> lSegments = new ArrayList();
		    for (CheckoutSegments lSegment : lSegmentList) {
			if (!lSegment.getProgramName().contains(".mak")) {
			    lSegments.add(lSegment.getProgramName());
			}
		    }

		    if (lSegments.isEmpty()) {
			continue;
		    }

		    if (lSystemLoad.getSystemId().getPlatformId().getNickName().equals(Constants.TRAVELPORT) && !lSystemLoad.getSystemId().getName().equals(Constants.getDSLLoadsetSystem().get(0))) {

			String lCommand = Constants.SystemScripts.DATE_AUDIT_CROSS_CHECK.getScript() + " " + lSystemLoad.getSystemId().getName().toLowerCase() + " " + Constants.REX_DATEFORMAT.get().format(lSystemLoad.getLoadDateTime()) + " " + StringUtils.join(lSegments, ",");
			lCommandResponse = getsSHClientUtils().executeCommand(lSystemLoad.getSystemId(), lCommand);
			if (!lCommandResponse.getStatus() && lCommandResponse.getErrorMessage() != null) {
			    try {
				LOG.error("Date Audit for Non Moderization Plan fails for system - " + lSystemLoad.getSystemId().getName() + " Plan Id " + plan.getId());
				String lString = (String) lCommandResponse.getErrorMessage();
				List<String> lLines = IOUtils.readLines(new StringReader(lString));
				// Temp code, need to replace with proper error format
				if (lLines.size() >= 2) {
				    lLines = lLines.subList(0, lLines.size() - 2);
				}
				StringBuilder message = new StringBuilder();
				for (String temp : lLines) {
				    message.append(temp).append(",");
				}
				if (message.length() > 0) {
				    message.setLength(message.length() - 1);
				}
				return message.toString().replace("Error Code: 8", "");
			    } catch (IOException ex) {
				LOG.error("Error in getting command response: " + ex);
				throw new WorkflowException("Error in getting command response", ex);
			    }
			}
		    }
		}

	    }
	}
	return "PASSED";
    }
}
