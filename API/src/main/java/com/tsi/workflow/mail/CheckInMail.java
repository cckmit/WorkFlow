/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.beans.dao.ImpPlan;
import java.text.MessageFormat;

/**
 *
 * @author
 */
public class CheckInMail extends MailMessage {

    private String sourceArtifacts;
    private ImpPlan dependentPlan;
    private ImpPlan sourcePlan;

    public ImpPlan getDependentPlan() {
	return dependentPlan;
    }

    public void setDependentPlan(ImpPlan dependentPlan) {
	this.dependentPlan = dependentPlan;
    }

    public ImpPlan getSourcePlan() {
	return sourcePlan;
    }

    public void setSourcePlan(ImpPlan sourcePlan) {
	this.sourcePlan = sourcePlan;
    }

    public String getSourceArtifacts() {
	return sourceArtifacts;
    }

    public void setSourceArtifacts(String sourceArtifacts) {
	this.sourceArtifacts = sourceArtifacts;
    }

    @Override
    public void processMessage() {

	String message = MessageFormat.format("Source artifacts {0} has been modified and checked-in in {1}. " + "Please sync up your {2} source code with the latest from Non-Secured {1}.", sourceArtifacts, sourcePlan.getId(), dependentPlan.getId());

	String subject = dependentPlan.getId() + " : Resolve source contention with the Non-Secured " + sourcePlan.getLoadType() + " Load " + sourcePlan.getId();
	this.setMessage(message);
	this.setSubject(subject);
    }

}
