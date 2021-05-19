/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.dto;

import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author prabhu.prabhakaran
 */
public class PutLevelDTO {

    List<String> options = new ArrayList<>();
    Boolean canUpdateStatus = false;
    Boolean canUpdateOthers = false;
    Boolean canDelete = false;
    PutLevel putLevel;
    String userId;
    String role;

    public PutLevelDTO(PutLevel putLevel, String userId, String role) {
	this.putLevel = putLevel;
	this.userId = userId;
	this.role = role;
    }

    public List<String> getOptions() {
	return options;
    }

    public void setOptions(List<String> options) {
	this.options = options;
    }

    public Boolean getCanDelete() {
	return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
	this.canDelete = canDelete;
    }

    public Boolean getCanUpdateStatus() {
	return canUpdateStatus;
    }

    public void setCanUpdateStatus(Boolean canUpdateStatus) {
	this.canUpdateStatus = canUpdateStatus;
    }

    public PutLevel getPutLevel() {
	return putLevel;
    }

    public void setPutLevel(PutLevel putLevel) {
	this.putLevel = putLevel;
    }

    public Boolean getCanUpdateOthers() {
	return canUpdateOthers;
    }

    public void setCanUpdateOthers(Boolean canUpdateOthers) {
	this.canUpdateOthers = canUpdateOthers;
    }

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public String getRole() {
	return role;
    }

    public void setRole(String role) {
	this.role = role;
    }

    public PutLevelDTO populate() {

	if (putLevel.getStatus() == null) {
	    putLevel.setStatus(Constants.PUTLevelOptions.INITIAL.name());
	}

	if (putLevel.getStatus().equals(Constants.PUTLevelOptions.INITIAL.name())) {
	    options.add(Constants.PUTLevelOptions.INITIAL.name());
	    options.add(Constants.PUTLevelOptions.DEVELOPMENT.name());
	    options.add(Constants.PUTLevelOptions.INACTIVE.name());
	} else if (putLevel.getStatus().equals(Constants.PUTLevelOptions.DEVELOPMENT.name())) {
	    options.add(Constants.PUTLevelOptions.DEVELOPMENT.name());
	    options.add(Constants.PUTLevelOptions.PRE_PROD_CO_EXIST.name());
	    options.add(Constants.PUTLevelOptions.LOCKDOWN.name());
	    options.add(Constants.PUTLevelOptions.INACTIVE.name());
	} else if (putLevel.getStatus().equals(Constants.PUTLevelOptions.PRE_PROD_CO_EXIST.name())) {
	    options.add(Constants.PUTLevelOptions.PRE_PROD_CO_EXIST.name());
	    options.add(Constants.PUTLevelOptions.DEVELOPMENT.name());
	    options.add(Constants.PUTLevelOptions.LOCKDOWN.name());
	    // options.add(Constants.PUTLevelOptions.INACTIVE.name());
	} else if (putLevel.getStatus().equals(Constants.PUTLevelOptions.LOCKDOWN.name())) {
	    options.add(Constants.PUTLevelOptions.LOCKDOWN.name());
	    options.add(Constants.PUTLevelOptions.DEVELOPMENT.name());
	    if (role.equals("LoadsControl")) {
		options.add(Constants.PUTLevelOptions.PROD_CO_EXIST.name());
		options.add(Constants.PUTLevelOptions.PRODUCTION.name());
	    }
	    options.add(Constants.PUTLevelOptions.INACTIVE.name());
	} else if (putLevel.getStatus().equals(Constants.PUTLevelOptions.PROD_CO_EXIST.name())) {
	    options.add(Constants.PUTLevelOptions.PROD_CO_EXIST.name());
	    if (role.equals("LoadsControl")) {
		options.add(Constants.PUTLevelOptions.PRODUCTION.name());
		options.add(Constants.PUTLevelOptions.LOCKDOWN.name());
	    }
	    // options.add(Constants.PUTLevelOptions.INACTIVE.name());
	} else if (putLevel.getStatus().equals(Constants.PUTLevelOptions.BACKUP.name())) {
	    options.add(Constants.PUTLevelOptions.BACKUP.name());
	    if (role.equals("LoadsControl")) {
		options.add(Constants.PUTLevelOptions.PRODUCTION.name());
	    }
	} else if (putLevel.getStatus().equals(Constants.PUTLevelOptions.PRODUCTION.name())) {
	    options.add(Constants.PUTLevelOptions.PRODUCTION.name());
	} else if (putLevel.getStatus().equals(Constants.PUTLevelOptions.INACTIVE.name())) {
	    options.add(Constants.PUTLevelOptions.INACTIVE.name());
	    options.add(Constants.PUTLevelOptions.INITIAL.name());
	    options.add(Constants.PUTLevelOptions.DEVELOPMENT.name());
	    options.add(Constants.PUTLevelOptions.LOCKDOWN.name());
	}
	if (putLevel.getOwnerids() != null) {
	    if (putLevel.getOwnerids().contains(userId) || role.equals("LoadsControl")) {
		canUpdateStatus = true;
	    }
	} else {
	    canUpdateStatus = false;
	}
	if (role.equals("LoadsControl")) {
	    canUpdateStatus = true;
	    canUpdateOthers = true;
	    canDelete = true;
	}
	if (putLevel.getStatus().equals(Constants.PUTLevelOptions.PRODUCTION.name()) || putLevel.getStatus().equals(Constants.PUTLevelOptions.BACKUP.name())) {
	    canDelete = false;
	}
	if (putLevel.getStatus().equals(Constants.PUTLevelOptions.PRODUCTION.name()) || putLevel.getStatus().equals(Constants.PUTLevelOptions.ARCHIVE.name())) {
	    canUpdateOthers = false;
	    canUpdateStatus = false;
	    canDelete = false;
	}
	return this;
    }

}
