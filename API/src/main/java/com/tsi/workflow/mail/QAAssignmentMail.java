/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.User;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessage;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Vinoth
 */
public class QAAssignmentMail extends MailMessage {

    private static final Logger LOG = Logger.getLogger(QAAssignmentMail.class.getName());

    private String planId;
    private boolean assignment;
    private Set<String> qaFunctionalTestersList;
    private String projectName;
    private String projectCSR;
    private Map<String, String> programNameTargetSys;

    @Autowired
    LDAPAuthenticatorImpl authenticator;

    User userDetails;

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public boolean isAssignment() {
	return assignment;
    }

    public void setAssignment(boolean assignment) {
	this.assignment = assignment;
    }

    public Set<String> getQaFunctionalTestersList() {
	return qaFunctionalTestersList;
    }

    public void setQaFunctionalTestersList(Set<String> qaFunctionalTestersList) {
	this.qaFunctionalTestersList = qaFunctionalTestersList;
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

    public Map<String, String> getProgramNameTargetSys() {
	return programNameTargetSys;
    }

    public void setProgramNameTargetSys(Map<String, String> programNameTargetSys) {
	this.programNameTargetSys = programNameTargetSys;
    }

    @Override
    public void processMessage() {
	StringBuilder message = new StringBuilder();
	if (isAssignment()) {
	    this.setSubject("QA Functional Tester assign for " + getPlanId());
	    message.append(String.join(",", getQaFunctionalTestersList())).append("   have been assigned as QA Functional Testers for the implementation plan  ").append(getPlanId()).append(".").append("<br><br> Project Name : ").append(getProjectName()).append(".").append("<br><br> Project CSR : ").append(getProjectCSR()).append(".").append("<br><br>");

	    getProgramNameTargetSys().forEach((tarSys, qaIns) -> {
		message.append("Target System :  ").append(tarSys).append(" -  ").append(" Special instruction from Lead: ").append(qaIns).append(" <br>");

	    });
	} else {
	    this.setSubject("Action required: " + getPlanId() + "  is ready for QA Functional Testing ");
	    message.append(getPlanId()).append("  is ready for QA Functional Testing.  ").append("<br><br> Project Name : ").append(getProjectName()).append(".").append("<br><br> Project CSR : ").append(getProjectCSR()).append(".").append("<br><br>");
	    getProgramNameTargetSys().forEach((tarSys, qaIns) -> {
		message.append("Target System :  ").append(tarSys).append(" -  ").append(" Special instruction from Lead: ").append(qaIns).append(" <br>");

	    });
	}
	this.setMessage(message.toString());

    }
}
