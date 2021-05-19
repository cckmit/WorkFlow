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
 * @author deepa.jayakumar
 */
public class UnsecuredCheckoutSourceContentMail extends MailMessage {

    private String sourceArtifact;
    private String otherPlanLoadType;
    private String otherPlanId;
    private String toPlanId;
    private boolean dependent = false;

    public void setDependent(boolean dependent) {
	this.dependent = dependent;
    }

    public void setOtherPlanLoadType(String otherPlanLoadType) {
	this.otherPlanLoadType = otherPlanLoadType;
    }

    public void setOtherPlanId(String otherPlanId) {
	this.otherPlanId = otherPlanId;
    }

    public void setSourceArtifact(String sourceArtifact) {
	this.sourceArtifact = sourceArtifact;
    }

    public void setToPlanId(String toPlanId) {
	this.toPlanId = toPlanId;
    }

    @Override
    public void processMessage() {
	String message = "";
	String subject = "";
	if (!dependent) {
	    message = MessageFormat.format("Source Artifacts {0} that was just checked out in Non-Secured {1} is already checked out in {2} . Please sync up your {1} source code with the latest in {2}", sourceArtifact, toPlanId, otherPlanId);
	    subject = MessageFormat.format("{0} : Resolve source contention with the Non-Secured {1} Load {2} ", toPlanId, otherPlanLoadType, otherPlanId);
	} else {
	    message = MessageFormat.format("Source Artifacts {0} that was just checked out in Non-Secured {1} is already checked out in {2} . Please sync up your {2} source code with the latest in {1}", sourceArtifact, toPlanId, otherPlanId);
	    subject = MessageFormat.format("{2} : Resolve source contention with the Non-Secured {1} Load {0} ", toPlanId, otherPlanLoadType, otherPlanId);
	}

	this.setMessage(message);
	this.setSubject(subject);
    }
}
