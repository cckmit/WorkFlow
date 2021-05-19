package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import java.util.List;

public class AccessPermissionMail extends MailMessage {

    private String repoName;
    private List<String> userList;
    private String repoPermission;
    private Boolean isAdded;
    private String devManager;
    private Boolean isUserMail;

    public String getRepoName() {
	return repoName;
    }

    public void setRepoName(String repoName) {
	this.repoName = repoName;
    }

    public List<String> getUserList() {
	return userList;
    }

    public void setUserList(List<String> userList) {
	this.userList = userList;
    }

    public String getRepoPermission() {
	return repoPermission;
    }

    public void setRepoPermission(String repoPermission) {
	this.repoPermission = repoPermission;
    }

    public Boolean getIsAdded() {
	return isAdded;
    }

    public void setIsAdded(Boolean isAdded) {
	this.isAdded = isAdded;
    }

    public String getDevManager() {
	return devManager;
    }

    public void setDevManager(String devManager) {
	this.devManager = devManager;
    }

    public Boolean getIsUserMail() {
	return isUserMail;
    }

    public void setIsUserMail(Boolean isUserMail) {
	this.isUserMail = isUserMail;
    }

    @Override
    public void processMessage() {
	StringBuilder message = new StringBuilder();
	String subject = "";
	if (getIsAdded() && !getIsUserMail()) {
	    subject = "Users added to repository -  " + repoName;
	    message.append("The following users has been added to Repository ").append(getRepoName()).append("  by dev manager ").append(getDevManager()).append(" with the below permissions:");
	    for (String user : getUserList()) {
		message.append("<br>").append(user).append(":").append(getRepoPermission());
	    }
	} else if (!(getIsAdded() && getIsUserMail())) {
	    subject = "Users updated with repository -  " + repoName;
	    message.append("The following users has been updated to Repository ").append(getRepoName()).append("  by dev manager ").append(getDevManager()).append(" with the below permissions:");
	    for (String user : getUserList()) {
		message.append("<br>").append(user).append(":").append(getRepoPermission());
	    }
	} else if (!getIsAdded() && getIsUserMail()) {
	    subject = "Users " + String.join(",", getUserList()) + " updated with repository -  " + repoName;
	    message.append("User ").append(String.join(",", getUserList())).append(" has been updated with Repository ").append(getRepoName()).append("  by dev manager ").append(getDevManager()).append(" with the permissions ").append(getRepoPermission());
	} else if (getIsAdded() && getIsUserMail()) {
	    subject = "Users " + String.join(",", getUserList()) + " added to repository -  " + repoName;
	    message.append("User ").append(String.join(",", getUserList())).append(" has been added to Repository").append(getRepoName()).append("  by dev manager ").append(getDevManager()).append(" with the permissions ").append(getRepoPermission());

	}

	this.setMessage(message.toString());
	this.setSubject(subject);

    }

}
