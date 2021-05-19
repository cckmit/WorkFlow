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
public class CheckoutSourceContentMail extends MailMessage {

    private String sourceArtifact;
    private String otherPlanLoadType;
    private String otherPlanId;
    private String toPlanId;
    private String otherPlanStatus;
    private boolean secured;

    public void setSecured(boolean secured) {
	this.secured = secured;
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

    public void setOtherPlanStatus(String otherPlanStatus) {
	this.otherPlanStatus = otherPlanStatus;
    }

    public void setToPlanId(String toPlanId) {
	this.toPlanId = toPlanId;
    }

    @Override
    public void processMessage() {
	String message = "";
	String subject = "";
	if (secured) {
	    message = MessageFormat.format("Source Artifacts {0} belonging to implementation plan {1} have been secured {2}. Please sync up your {3} source code with the latest in Secured {1}", sourceArtifact, otherPlanId, otherPlanStatus, toPlanId);

	    subject = MessageFormat.format("{0} : Resolve source contention with the Secured {1} Load {2}", toPlanId, otherPlanLoadType, otherPlanId);
	} else {
	    message = MessageFormat.format("Source Artifacts {0} that was just checked out in {1} is already checked out in {2} . Please sync up your {1} source code with the latest in Non-Secured {2}", sourceArtifact, toPlanId, otherPlanId);

	    subject = MessageFormat.format("{0} : Resolve source contention with the Non-Secured {1} Load {2}", toPlanId, otherPlanLoadType, otherPlanId);
	}

	this.setMessage(message);
	this.setSubject(subject);
    }
}
