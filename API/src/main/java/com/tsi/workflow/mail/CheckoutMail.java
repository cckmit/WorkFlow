/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import java.text.MessageFormat;

/**
 *
 * @author
 */
public class CheckoutMail extends MailMessage {

    private String sourceArtifact;
    private String planId;
    private String impId;
    private String toImpId;
    private String toPlanId;

    public void setSourceArtifact(String sourceArtifact) {
	this.sourceArtifact = sourceArtifact;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public void setToPlanId(String toPlanId) {
	this.toPlanId = toPlanId;
    }

    public void setImpId(String impId) {
	this.impId = impId;
    }

    public void setToImpId(String toImpId) {
	this.toImpId = toImpId;
    }

    @Override
    public void processMessage() {
	String message = MessageFormat.format("Source Artifact - <b>{0}</b> has been checked-out for implementation Plan <b> {1} / {2}</b> ", sourceArtifact, planId, impId);
	String subject = MessageFormat.format("Implementation Plan - {0} / Implementation - {1}", toPlanId, toImpId);
	this.setMessage(message);
	this.setSubject(subject);
    }
}
