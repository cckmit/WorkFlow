/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.tsi.workflow.User;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Radha.Adhimoolam
 */
public class TosActionQueue implements Serializable {

    private static final long serialVersionUID = -3032034971814265315L;

    private User user;
    private String oldStatus;
    private Integer tosRecId;
    private String queueStatus = "TOPROCESS";
    private String tosResponseMessage;
    private String action;
    private String planId;
    private String systemName;

    public TosActionQueue() {
    }

    public TosActionQueue(User user, String oldStatus, Integer tosRecId) {
	this.user = user;
	this.oldStatus = oldStatus;
	this.tosRecId = tosRecId;
    }

    public TosActionQueue(User user, String oldStatus, Integer tosRecId, String queueStatus, String tosResponseMessage, String action) {
	this.user = user;
	this.oldStatus = oldStatus;
	this.tosRecId = tosRecId;
	this.queueStatus = queueStatus;
	this.tosResponseMessage = tosResponseMessage;
	this.action = action;
    }

    public TosActionQueue(User user, String oldStatus, Integer tosRecId, String queueStatus, String tosResponseMessage, String action, String planId) {
	this.user = user;
	this.oldStatus = oldStatus;
	this.tosRecId = tosRecId;
	this.queueStatus = queueStatus;
	this.tosResponseMessage = tosResponseMessage;
	this.action = action;
	this.planId = planId;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public String getOldStatus() {
	return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
	this.oldStatus = oldStatus;
    }

    public Integer getTosRecId() {
	return tosRecId;
    }

    public void setTosRecId(Integer tosRecId) {
	this.tosRecId = tosRecId;
    }

    public String getQueueStatus() {
	return queueStatus;
    }

    public void setQueueStatus(String queueStatus) {
	this.queueStatus = queueStatus;
    }

    public String getTosResponseMessage() {
	return tosResponseMessage;
    }

    public void setTosResponseMessage(String tosResponseMessage) {
	this.tosResponseMessage = tosResponseMessage;
    }

    public String getAction() {
	return action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public String getSystemName() {
	return systemName;
    }

    public void setSystemName(String systemName) {
	this.systemName = systemName;
    }

    @Override
    public int hashCode() {
	int hash = 3;
	hash = 83 * hash + Objects.hashCode(this.planId);
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	if (this.planId.equals(((TosActionQueue) obj).getPlanId())) {
	    return true;
	}

	final TosActionQueue other = (TosActionQueue) obj;
	return true;
    }

}
