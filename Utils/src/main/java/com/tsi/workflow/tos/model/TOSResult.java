/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos.model;

import com.tsi.workflow.User;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author USER
 */
public class TOSResult implements Serializable {

    private static final long serialVersionUID = -4164504289500511955L;

    /*
     * Plan Details
     */
    private Integer id;
    private String rejectReason;
    private String oldStatus;
    private String system;

    /*
     * Tos Details
     */
    private String command;
    private String loadset;
    private String cpuName;
    private boolean isPrimary;
    private boolean coexist;

    /*
     * Internal
     */
    private String host;
    private String fullCommand;
    private transient Date createdTime;

    /*
     * TOs Result
     */
    private int returnValue;
    private String message;
    private String ipAddress;
    private boolean last;
    /*
     * User Details
     */
    private User user;

    public TOSResult() {
    }

    public TOSResult(String command, String loadset, int returnValue, String message, String system, String cpuName, boolean isPrimary) {
	// Used in TOS Response (TOS Listener Thread), will be matched with (1)
	this.command = command;
	this.loadset = loadset;
	this.returnValue = returnValue;
	if (returnValue == 0) {
	    this.last = true;
	}
	this.message = message;
	this.system = system;
	this.cpuName = cpuName;
	this.isPrimary = isPrimary;
    }

    public TOSResult(String command, int returnValue, String message, String system, String cpuName, boolean isPrimary) {
	// Used in IP Response (TOS Listener Thread), will be matched with (2)
	this.command = command;
	this.returnValue = returnValue;
	if (returnValue == 0) {
	    this.last = true;
	}
	this.message = message;
	this.system = system;
	this.cpuName = cpuName;
	this.isPrimary = isPrimary;
    }

    public Date getCreatedTime() {
	return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
	this.createdTime = createdTime;
    }

    public String getSystem() {
	return system;
    }

    public void setSystem(String system) {
	this.system = system;
    }

    public String getRejectReason() {
	return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
	this.rejectReason = rejectReason;
    }

    public boolean isLast() {
	return last;
    }

    public void setLast(boolean last) {
	this.last = last;
    }

    public String getIpAddress() {
	return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
	this.ipAddress = ipAddress;
    }

    public String getOldStatus() {
	return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
	this.oldStatus = oldStatus;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public String getCommand() {
	return command;
    }

    public void setCommand(String command) {
	this.command = command;
    }

    public String getLoadset() {
	return loadset;
    }

    public void setLoadset(String loadset) {
	this.loadset = loadset;
    }

    public int getReturnValue() {
	return returnValue;
    }

    public void setReturnValue(int returnValue) {
	this.returnValue = returnValue;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
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

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof TOSResult) {
	    TOSResult result = (TOSResult) obj;
	    return (command != null ? this.command.equals(result.command) : true) && (loadset != null ? this.loadset.equals(result.loadset) : true);
	}
	return false;
    }

    @Override
    public int hashCode() {
	int hash = 3;
	hash = 97 * hash + (this.command != null ? this.command.hashCode() : 0);
	return hash;
    }

    public String getFullCommand() {
	return fullCommand;
    }

    public void setFullCommand(String fullCommand) {
	this.fullCommand = fullCommand;
    }

    public boolean isCoexist() {
	return coexist;
    }

    public void setCoexist(boolean coexist) {
	this.coexist = coexist;
    }

    public boolean isIsPrimary() {
	return isPrimary;
    }

    public void setIsPrimary(boolean isPrimary) {
	this.isPrimary = isPrimary;
    }

    // Messages Result track map
    public String hashSystemCommandLoadsetPrimary() {
	return getSystem() + " | " + getCommand() + " | " + getLoadset() + " | " + isIsPrimary();
    }

    // Mail
    public String hashSystemCPUCommandLoadet() {
	return getSystem() + " | " + getCpuName() + " | " + getCommand() + " | " + getLoadset();
    }

    // TOS Client Object map key (Retry)
    public String hashSystemCPU() {
	return getSystem() + " | " + getCpuName();
    }

    // IP Messages Result track map
    public String hashSystemCPUCommand() {
	return getSystem() + " | " + getCpuName() + " | " + getCommand();
    }
}
