/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author vinoth
 */
public class RepoCheckoutMail extends MailMessage {

    private String repoName;
    private boolean isNewOwnerUpdate;
    private Collection<String> newOwnerList;
    private Collection<String> removedOwnerList;
    private List<String> newOwnerDisplayName;
    private List<String> removedOwnerDisplayName;
    private String description;
    private boolean isDescUpdate;

    public String getRepoName() {
	return repoName;
    }

    public void setRepoName(String repoName) {
	this.repoName = repoName;
    }

    public boolean getIsNewOwnerUpdate() {
	return isNewOwnerUpdate;
    }

    public void setIsNewOwnerUpdate(boolean isNewOwnerUpdate) {
	this.isNewOwnerUpdate = isNewOwnerUpdate;
    }

    public Collection<String> getNewOwnerList() {
	return newOwnerList;
    }

    public void setNewOwnerList(Collection<String> newOwnerList) {
	this.newOwnerList = newOwnerList;
    }

    public Collection<String> getRemovedOwnerList() {
	return removedOwnerList;
    }

    public void setRemovedOwnerList(Collection<String> removedOwnerList) {
	this.removedOwnerList = removedOwnerList;
    }

    public List<String> getNewOwnerDisplayName() {
	return newOwnerDisplayName;
    }

    public void setNewOwnerDisplayName(List<String> newOwnerDisplayName) {
	this.newOwnerDisplayName = newOwnerDisplayName;
    }

    public List<String> getRemovedOwnerDisplayName() {
	return removedOwnerDisplayName;
    }

    public void setRemovedOwnerDisplayName(List<String> removedOwnerDisplayName) {
	this.removedOwnerDisplayName = removedOwnerDisplayName;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public boolean isDescUpdate() {
	return isDescUpdate;
    }

    public void setDescUpdate(boolean isDescUpdate) {
	this.isDescUpdate = isDescUpdate;
    }

    @Override
    public void processMessage() {
	String message;
	String subject;
	if (getIsNewOwnerUpdate()) {
	    message = MessageFormat.format("{0}  has been added as a package owner for  {1} package.", String.join(",", newOwnerDisplayName), repoName);
	    subject = MessageFormat.format("{0}: Package owner updates", repoName);
	} else if (isDescUpdate()) {
	    message = MessageFormat.format("Description of package  {0} has been updated as {1}", repoName, description);
	    subject = MessageFormat.format("{0}: Package description updates", repoName);
	} else {
	    message = MessageFormat.format("{0}  has been removed as a package owner of  {1} package.", String.join(",", removedOwnerDisplayName), repoName);
	    subject = MessageFormat.format("{0}: Package owner updates", repoName);
	}

	this.setMessage(message);
	this.setSubject(subject);
    }

}
