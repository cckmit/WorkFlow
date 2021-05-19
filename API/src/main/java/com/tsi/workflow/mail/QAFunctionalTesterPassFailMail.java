package com.tsi.workflow.mail;

import com.tsi.workflow.User;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.TagStatus;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Vinoth
 */
public class QAFunctionalTesterPassFailMail extends MailMessage {

    private String planId;
    private String projectName;
    private String projectCSR;
    private String currentUser;
    private String planStatus;
    private String currentRole;

    @Autowired
    LDAPAuthenticatorImpl authenticator;

    User userDetails;

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public User getUserDetails() {
	return userDetails;
    }

    public void setUserDetails(User userDetails) {
	this.userDetails = userDetails;
    }

    public String getProjectName() {
	return projectName;
    }

    public void setProjectName(String projectName) {
	this.projectName = projectName;
    }

    public String getProjectCSR() {
	return projectCSR;
    }

    public void setProjectCSR(String projectCSR) {
	this.projectCSR = projectCSR;
    }

    public String getCurrentUser() {
	return currentUser;
    }

    public void setCurrentUser(String currentUser) {
	this.currentUser = currentUser;
    }

    public String getPlanStatus() {
	return planStatus;
    }

    public void setPlanStatus(String planStatus) {
	this.planStatus = planStatus;
    }

    /**
     * @return the currentRole
     */
    public String getCurrentRole() {
	return currentRole;
    }

    /**
     * @param currentRole
     *            the currentRole to set
     */
    public void setCurrentRole(String currentRole) {
	this.currentRole = currentRole;
    }

    @Override
    public void processMessage() {
	StringBuilder message = new StringBuilder();
	if (getCurrentRole().equals(Constants.UserGroup.QADeployLead.name())) {
	    if (getPlanStatus().equals(Constants.PlanStatus.PASSED_FUNCTIONAL_TESTING.name())) {
		this.setSubject(getPlanId() + "  PASSED  " + "  QA Functional testing ");
		message.append(getCurrentUser() + " PASSED " + " QA function testing for implementation plan " + getPlanId());
	    } else if (getPlanStatus().equals(TagStatus.REJECTED.name())) {
		this.setSubject(getPlanId() + "  FAILED  " + "  QA Functional testing ");
		message.append(getCurrentUser() + " FAILED " + " QA function testing for implementation plan " + getPlanId());
	    }
	} else {
	    this.setSubject("QA Functional Tester " + getCurrentUser() + "  has tested  " + getPlanId());
	    if (getPlanStatus().equals(Constants.PlanStatus.PASSED_FUNCTIONAL_TESTING.name())) {
		message.append(" QA Functional Tester ").append(getCurrentUser()).append(" marked ").append(getPlanId()).append(" as ").append(getPlanStatus()).append("<br><br> Project Name : ").append(getProjectName()).append(".").append("<br><br> Project CSR : ").append(getProjectCSR()).append(".");
	    } else if (getPlanStatus().equals(TagStatus.REJECTED.name())) {
		message.append(" QA Functional Tester ").append(getCurrentUser()).append(getPlanStatus()).append(" ").append(getPlanId()).append("<br><br> Project Name : ").append(getProjectName()).append(".").append("<br><br> Project CSR : ").append(getProjectCSR()).append(".");
	    }
	}

	this.setMessage(message.toString());
    }

}
