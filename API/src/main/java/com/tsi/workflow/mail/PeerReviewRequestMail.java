/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.Constants;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author User
 */
public class PeerReviewRequestMail extends MailMessage {

    private List<String> reviewerList;
    private String ticketUrl;
    private String developer;
    private String implementationId;
    private String projectName;
    private List<String> segementList;
    private String impDesc;
    private Set<String> problemTicketNum;
    private List<String> targetSystem;
    private Map<String, List<String>> programNameTargetSys;

    @Autowired
    LDAPAuthenticatorImpl authenticator;

    public List<String> getReviewerList() {
	return reviewerList;
    }

    public void setReviewerList(List<String> reviewerList) {
	this.reviewerList = reviewerList;
    }

    public String getDeveloper() {
	return developer;
    }

    public void setDeveloper(String developer) {
	this.developer = developer;
    }

    public String getTicketUrl() {
	return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
	this.ticketUrl = ticketUrl;
    }

    public String getImplementationId() {
	return implementationId;
    }

    public void setImplementationId(String implementationId) {
	this.implementationId = implementationId;
    }

    public LDAPAuthenticatorImpl getAuthenticator() {
	return authenticator;
    }

    public void setAuthenticator(LDAPAuthenticatorImpl authenticator) {
	this.authenticator = authenticator;
    }

    public String getProjectName() {
	return projectName;
    }

    public void setProjectName(String projectName) {
	this.projectName = projectName;
    }

    public String getImpDesc() {
	return impDesc;
    }

    public void setImpDesc(String impDesc) {
	this.impDesc = impDesc;
    }

    public Set<String> getProblemTicketNum() {
	return problemTicketNum;
    }

    public void setProblemTicketNum(Set<String> problemTicketNum) {
	this.problemTicketNum = problemTicketNum;
    }

    public List<String> getSegementList() {
	return segementList;
    }

    public void setSegementList(List<String> segementList) {
	this.segementList = segementList;
    }

    public List<String> getTargetSystem() {
	return targetSystem;
    }

    public void setTargetSystem(List<String> targetSystem) {
	this.targetSystem = targetSystem;
    }

    public Map<String, List<String>> getProgramNameTargetSys() {
	return programNameTargetSys;
    }

    public void setProgramNameTargetSys(Map<String, List<String>> programNameTargetSys) {
	this.programNameTargetSys = programNameTargetSys;
    }

    @Override
    public void processMessage() {
	StringBuilder message = new StringBuilder();
	for (String reviewer : reviewerList) {
	    this.addToAddressUserId(reviewer, Constants.MailSenderRole.PEER_REVIEWER);
	}
	String subject = "Request for Peer Review " + implementationId;
	message.append(developer).append(" has completed the code development and requested you to review the implementation -").append(implementationId).append("<br><br> Project/CSR  : ").append(getProjectName());
	if (!getProblemTicketNum().isEmpty() && getProblemTicketNum() != null) {
	    message.append("<br><br> Problem Ticket: ").append(getProblemTicketNum());
	}
	message.append("<br><br> Description of Change:  ").append(getImpDesc()).append("<br><br>");
	// setting Program Name and Segements
	getProgramNameTargetSys().forEach((segName, tarSys) -> {
	    message.append("Target System(s):  ").append(tarSys.stream().collect(Collectors.joining(","))).append(" Segment(s):   ").append(segName).append(" <br>");

	});

	message.append("<br><br> <b>Ticket URL :</b><br> ").append("<a href =" + ticketUrl + ">" + ticketUrl).append("<a/>");

	this.setSubject(subject);
	this.setMessage(message.toString());
    }

}
