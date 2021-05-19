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
import com.tsi.workflow.beans.dao.SystemLoad;
import java.text.MessageFormat;
import org.apache.log4j.Priority;

/**
 *
 * @author USER
 */
public class ProdFTPActivityMessage extends ActivityLogMessage {

    SystemLoad lBuild;
    String ipAddress;
    String status;
    boolean fallback;
    String vparsName = "";

    public String getVparsName() {
	return vparsName;
    }

    public void setVparsName(String vparsName) {
	this.vparsName = vparsName;
    }

    public ProdFTPActivityMessage(ImpPlan impPlan, Implementation implementation, IBeans... beans) {
	super(impPlan, implementation, beans);
	status = "Started";
    }

    public boolean isFallback() {
	return fallback;
    }

    public void setFallback(boolean fallback) {
	this.fallback = fallback;
    }

    @Override
    public Priority getLogLevel() {
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
	lBuild = (SystemLoad) beans[0];
    }

    public String getIpAddress() {
	return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
	this.ipAddress = ipAddress;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    @Override
    public String processMessage() {
	StringBuilder text = new StringBuilder("");
	if (isFallback()) {
	    if (user.getCurrentDelegatedUser() != null) {
		text.append("Fallback Loadset for the plan {2} - {0} has uploaded {5} the loadset {1}, system {3} to the IP Address {4}");
		if (!vparsName.equals("")) {
		    text.append(" deployed in Vpars: {7}");
		}
		text.append(", on behalf of {6} ");
		return MessageFormat.format(text.toString(), user.getDisplayName(), isFallback() ? lBuild.getFallbackLoadSetName() : lBuild.getLoadSetName(), impPlan.getId(), lBuild.getSystemId().getName(), ipAddress, status, user.getCurrentDelegatedUser().getDisplayName(), vparsName);
	    }
	    text.append("Fallback Loadset for the plan {2} - {0} has uploaded {5} the loadset {1}, system {3} to the IP Address {4}");
	    if (!vparsName.equals("")) {
		text.append(" deployed in Vpars: {6}");
	    }
	    return MessageFormat.format(text.toString(), user.getDisplayName(), isFallback() ? lBuild.getFallbackLoadSetName() : lBuild.getLoadSetName(), impPlan.getId(), lBuild.getSystemId().getName(), ipAddress, status, vparsName);
	}
	if (user.getCurrentDelegatedUser() != null) {
	    text.append("{0} has uploaded {5} loadset {1} implementation plan {2}, system {3} to the IP Address {4} ");
	    if (!vparsName.equals("")) {
		text.append("deployed in Vpars: {7}");
	    }
	    text.append(", on behalf of {6} ");
	    return MessageFormat.format(text.toString(), user.getDisplayName(), isFallback() ? lBuild.getFallbackLoadSetName() : lBuild.getLoadSetName(), impPlan.getId(), lBuild.getSystemId().getName(), ipAddress, status, user.getCurrentDelegatedUser().getDisplayName(), vparsName);
	}

	text.append("{0} has uploaded {5} loadset {1} implementation plan {2}, system {3} to the IP Address {4} ");
	if (!vparsName.equals("")) {
	    text.append("deployed in Vpars: {6}");
	}
	return MessageFormat.format(text.toString(), user.getDisplayName(), isFallback() ? lBuild.getFallbackLoadSetName() : lBuild.getLoadSetName(), impPlan.getId(), lBuild.getSystemId().getName(), ipAddress, status, vparsName);
    }

}
