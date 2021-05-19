/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import java.text.MessageFormat;
import java.util.List;

/**
 *
 * @author
 */
public class CheckoutDeleteMail extends MailMessage {

    private String sourceArtifact;
    private String otherPlanLoadType;
    private String otherPlanId;
    private String toPlanId;
    private String toImpId;
    private List<String> targetSystem;

    public void setToImpId(String toImpId) {
	this.toImpId = toImpId;
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

    public List<String> getTargetSystem() {
	return targetSystem;
    }

    public void setTargetSystem(List<String> targetSystem) {
	this.targetSystem = targetSystem;
    }

    @Override
    public void processMessage() {
	String message = MessageFormat.format("Source Artifacts {0} {1} deleted from implementation plan Non-Secured  {2}/{4}. Kindly remove the {2} changes from plan {3}, if any", sourceArtifact, targetSystem, otherPlanId, toPlanId, toImpId);

	String subject = MessageFormat.format("{0} : Resolve source contention with the Non-Secured {1} Load {2} ", toPlanId, otherPlanLoadType, otherPlanId);
	this.setMessage(message);
	this.setSubject(subject);
    }
}
