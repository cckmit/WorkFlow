/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos.model;

import com.tsi.workflow.User;
import com.tsi.workflow.utils.Constants;
import java.io.Serializable;
import java.util.Date;
import java.util.regex.Matcher;

/**
 *
 * @author USER
 */
public class TOSRequest implements Serializable {

    private static final long serialVersionUID = 7047457150062433154L;

    /*
     * Plan Details
     *
     */
    private int id;
    private String oldStatus;
    private String system;
    private String rejectReason;

    /*
     * Tos Details
     */
    private String primaryAddress;
    private String secondaryAddress;
    private String command;
    private String cpuName;
    private boolean coexist;

    /*
     * Internal
     */
    private String host;

    /*
     * User Details
     */
    private User user;

    public TOSRequest() {
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public String getRejectReason() {
	return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
	this.rejectReason = rejectReason;
    }

    public String getSystem() {
	return system;
    }

    public void setSystem(String system) {
	this.system = system.toUpperCase();
    }

    public String getOldStatus() {
	return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
	this.oldStatus = oldStatus;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getPrimaryAddress() {
	return primaryAddress;
    }

    public void setPrimaryAddress(String primaryAddress) {
	this.primaryAddress = primaryAddress;
    }

    public String getSecondaryAddress() {
	return secondaryAddress;
    }

    public void setSecondaryAddress(String secondaryAddress) {
	this.secondaryAddress = secondaryAddress;
    }

    public String getCommand() {
	return command;
    }

    public void setCommand(String command) {
	this.command = command;
    }

    public String getHost() {
	return host;
    }

    public void setHost(String host) {
	this.host = host;
    }

    public String getCpuName() {
	return cpuName;
    }

    public void setCpuName(String cpuName) {
	this.cpuName = cpuName;
    }

    public boolean isCoexist() {
	return coexist;
    }

    public void setCoexist(boolean coexist) {
	this.coexist = coexist;
    }

    // TOS Client Object map key
    // TOS Client IP map key
    public String hashSystemCPU() {
	return getSystem() + " | " + getCpuName();
    }

    // TOS Client IP map value
    public String hashIPAddress() {
	return getPrimaryAddress() + " | " + getSecondaryAddress();
    }

    public TOSResult getResult(boolean isIP) {
	return getResult(isIP, 8, null);
    }

    public TOSResult getResult(boolean isIP, int pRC, String pMessage) {
	TOSResult lTOSResult = new TOSResult();
	if (!isIP) {
	    Matcher matcher = Constants.IN_MESSAGE_PATTERN.matcher(command);
	    if (matcher.matches()) {
		lTOSResult.setCommand(matcher.group(1));
		lTOSResult.setLoadset(matcher.group(2));
	    }
	} else {
	    lTOSResult.setCommand("LOADFIP");
	}
	lTOSResult.setCreatedTime(new Date());
	if (pMessage != null) {
	    lTOSResult.setReturnValue(pRC);
	    lTOSResult.setMessage(pMessage);
	}
	lTOSResult.setFullCommand(command);
	lTOSResult.setId(id);
	lTOSResult.setUser(user);
	lTOSResult.setCoexist(coexist);
	lTOSResult.setHost(host);
	lTOSResult.setRejectReason(rejectReason);
	lTOSResult.setSystem(system);
	lTOSResult.setCpuName(cpuName);
	lTOSResult.setOldStatus(oldStatus);
	return lTOSResult;
    }
}
