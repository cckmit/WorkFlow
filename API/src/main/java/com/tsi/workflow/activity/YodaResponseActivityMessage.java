/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import com.tsi.workflow.base.ActivityLogMessage;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.ui.YodaResult;
import com.tsi.workflow.utils.Constants.YodaActivtiyMessage;
import java.text.MessageFormat;
import org.apache.log4j.Priority;

/**
 *
 * @author yeshwanth.shenoy
 */
public class YodaResponseActivityMessage extends ActivityLogMessage {

    Priority lPriority;
    YodaResult lYodaResult;
    YodaActivtiyMessage yodaActivity;
    String systemName;
    String vparName;
    boolean ftpFlag = false;

    public YodaResponseActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    public String getSystemName() {
	return systemName;
    }

    public void setSystemName(String systemName) {
	this.systemName = systemName;
    }

    public String getVparName() {
	return vparName;
    }

    public void setVparName(String vparName) {
	this.vparName = vparName;
    }

    public YodaResult getlYodaResult() {
	return lYodaResult;
    }

    public void setlYodaResult(YodaResult lYodaResult) {
	this.lYodaResult = lYodaResult;
    }

    public YodaActivtiyMessage getYodaActivity() {
	return yodaActivity;
    }

    public void setYodaActivity(YodaActivtiyMessage yodaActivity) {
	this.yodaActivity = yodaActivity;
    }

    public boolean isFtpFlag() {
	return ftpFlag;
    }

    public void setFtpFlag(boolean ftpFlag) {
	this.ftpFlag = ftpFlag;
    }

    @Override
    public String processMessage() {
	String yodaMessage = "";
	String lMessage = "";
	if (yodaActivity == YodaActivtiyMessage.IPADDRESS) {
	    if (lYodaResult.getRc() == 0) {
		lPriority = Priority.INFO;
		lMessage = MessageFormat.format("IP Address of {0} VPARS {1} has been successfully returned as {2}", systemName, vparName, lYodaResult.getIp());
	    } else {
		lPriority = Priority.ERROR;
		lMessage = MessageFormat.format("Unable to get IP Address of {0} VPARS {1}, RC: {2}", systemName, vparName, lYodaResult.getRc());
		yodaMessage = lYodaResult.getLogMessage();
	    }
	    return lMessage + yodaMessage;
	} else {
	    if (lYodaResult.getRc() == 0) {
		lPriority = Priority.INFO;
		lMessage = MessageFormat.format("Plan {0} {1} is Success", impPlan.getId(), yodaActivity.getDescription());
		if (isFtpFlag()) {
		    yodaMessage = lYodaResult.getLogMessage();
		}
	    } else {
		lPriority = Priority.ERROR;
		lMessage = MessageFormat.format("Plan {0} {1} is Failed", impPlan.getId(), yodaActivity.getDescription());
		yodaMessage = lYodaResult.getLogMessage();
	    }
	    lMessage += "RC: " + lYodaResult.getRc() + " Message: " + yodaMessage;
	    return lMessage;
	}
    }

    @Override
    public Priority getLogLevel() {
	return lPriority;
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

}
