/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.User;
import com.tsi.workflow.base.MailMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author User
 */
public class QaFunctionalTesterReassignmentMail extends MailMessage {

    private static final Logger LOG = Logger.getLogger(QaFunctionalTesterReassignmentMail.class.getName());
    boolean removed;
    boolean reAssigned;
    User lUser;
    List<String> qaFunTestersId = new ArrayList<>();
    List<String> addedQaFunTestersName = new ArrayList<>();
    List<String> removeQaFunTestersName = new ArrayList<>();
    private String projectCSRNum;
    private String projectName;
    private String planId;
    private Map<String, String> programNameTargetSys;

    public User getUser() {
	return lUser;
    }

    public void setUser(User lUser) {
	this.lUser = lUser;
    }

    public boolean isReAssigned() {
	return reAssigned;
    }

    public void setReAssigned(boolean reAssigned) {
	this.reAssigned = reAssigned;
    }

    public boolean isRemoved() {
	return removed;
    }

    public void setRemoved(boolean removed) {
	this.removed = removed;
    }

    public String getProjectName() {
	return projectName;
    }

    public void setProjectName(String projectName) {
	this.projectName = projectName;
    }

    public List<String> getQaFunTestersId() {
	return qaFunTestersId;
    }

    public void setQaFunTestersId(List<String> qaFunTestersId) {
	this.qaFunTestersId = qaFunTestersId;
    }

    public List<String> getAddedQaFunTestersName() {
	return addedQaFunTestersName;
    }

    public void setAddedQaFunTestersName(List<String> addedQaFunTestersName) {
	this.addedQaFunTestersName = addedQaFunTestersName;
    }

    public List<String> getRemoveQaFunTestersName() {
	return removeQaFunTestersName;
    }

    public void setRemoveQaFunTestersName(List<String> removeQaFunTestersName) {
	this.removeQaFunTestersName = removeQaFunTestersName;
    }

    public String getProjectCSRNum() {
	return projectCSRNum;
    }

    public void setProjectCSRNum(String projectCSRNum) {
	this.projectCSRNum = projectCSRNum;
    }

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
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
	if (removed) {
	    this.setSubject("QA Functional Tester removed from  - " + getPlanId());
	    message.append(String.join(",", getRemoveQaFunTestersName())).append(" have been removed as QA Functional Testers for the implementation plan ").append(getPlanId()).append(" . ").append("<br><br> Project name  : ").append(getProjectName()).append("<br><br> Project CSR : ").append(getProjectCSRNum()).append("<br><br>");

	    getProgramNameTargetSys().forEach((tarSys, qaIns) -> {
		message.append("Target System :  ").append(tarSys).append(" -  ").append(" Special instruction from Lead: ").append(qaIns).append(" <br>");

	    });
	} else if (reAssigned) {
	    this.setSubject("QA Functional Tester reassign for - " + getPlanId());

	    message.append(String.join(",", getRemoveQaFunTestersName())).append(" to ").append(String.join(",", getAddedQaFunTestersName())).append(" has re-assigned as QA Functional Testers for the implementation plan ").append(getPlanId()).append(" . ").append("<br><br> Project name  : ").append(getProjectName()).append("<br><br> Project CSR : ").append(getProjectCSRNum()).append("<br><br>");

	    getProgramNameTargetSys().forEach((tarSys, qaIns) -> {
		message.append("Target System :  ").append(tarSys).append(" -  ").append(" Special instruction from Lead: ").append(qaIns).append(" <br>");

	    });

	} else {
	    this.setSubject("QA Functional Tester added for  - " + getPlanId());

	    message.append(String.join(",", getAddedQaFunTestersName())).append(" have been added as QA Functional Testers for the implementation plan ").append(getPlanId()).append(" . ").append("<br><br> Project name  : ").append(getProjectName()).append("<br><br> Project CSR : ").append(getProjectCSRNum()).append("<br><br>");

	    getProgramNameTargetSys().forEach((tarSys, qaIns) -> {
		message.append("Target System :  ").append(tarSys).append(" -  ").append(" Special instruction from Lead: ").append(qaIns).append(" <br>");

	    });
	}
	this.setMessage(message.toString());
    }

}
