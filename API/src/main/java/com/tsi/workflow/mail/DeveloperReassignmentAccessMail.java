/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import java.util.List;

/**
 *
 * @author Radha.Adhimoolam
 */
public class DeveloperReassignmentAccessMail extends MailMessage {

    private List<String> programName;
    private String repoName;
    private String impId;
    private String devId;

    public List<String> getProgramName() {
	return programName;
    }

    public void setProgramName(List<String> programName) {
	this.programName = programName;
    }

    public String getRepoName() {
	return repoName;
    }

    public void setRepoName(String repoName) {
	this.repoName = repoName;
    }

    public String getImpId() {
	return impId;
    }

    public void setImpId(String impId) {
	this.impId = impId;
    }

    public String getDevId() {
	return devId;
    }

    public void setDevId(String devId) {
	this.devId = devId;
    }

    @Override
    public void processMessage() {
	String lSubject = getRepoName() + " : Non member developer reassigned";
	String lMessage = "Non member developer " + getDevId() + " has been reassigned to implementation " + getImpId() + " that has source artifacts " + String.join(",", getProgramName()) + " from repository " + getRepoName();

	this.setSubject(lSubject);
	this.setMessage(lMessage);
    }

}
