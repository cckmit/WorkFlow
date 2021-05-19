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
 * @author gn.ebinezardharmaraj
 */
public class DivergedCheckoutSourceArtifactMail extends MailMessage {

    private String sourceArtifact;
    private String developer;
    private String targetSystemBefore;
    private String targetSystemAfter;
    private String checkedOutTargetSystem;

    public String getSourceArtifact() {
	return sourceArtifact;
    }

    public void setSourceArtifact(String sourceArtifact) {
	this.sourceArtifact = sourceArtifact;
    }

    public String getDeveloper() {
	return developer;
    }

    public void setDeveloper(String developer) {
	this.developer = developer;
    }

    public String getTargetSystemBefore() {
	return targetSystemBefore;
    }

    public void setTargetSystemBefore(String targetSystemBefore) {
	this.targetSystemBefore = targetSystemBefore;
    }

    public String getTargetSystemAfter() {
	return targetSystemAfter;
    }

    public void setTargetSystemAfter(String targetSystemAfter) {
	this.targetSystemAfter = targetSystemAfter;
    }

    public String getCheckedOutTargetSystem() {
	return checkedOutTargetSystem;
    }

    public void setCheckedOutTargetSystem(String checkedOutTargetSystem) {
	this.checkedOutTargetSystem = checkedOutTargetSystem;
    }

    @Override
    public void processMessage() {
	String message = "Source artifact {0} common to {1} has been diverged by {2}.";
	String subject = "";
	String checkedOutTargetSystemArr[] = checkedOutTargetSystem.split(",");
	if (checkedOutTargetSystemArr.length > 1) {

	    message = message + "It is now common for {3} and unique for {4}.";
	} else {
	    message = message + "It is now  unique for {3}.";
	}
	message = MessageFormat.format(message, sourceArtifact, targetSystemBefore, developer, checkedOutTargetSystem, targetSystemAfter);
	subject = MessageFormat.format("Source artifact {0} common to {1} has been diverged", sourceArtifact, targetSystemBefore);

	this.setMessage(message);
	this.setSubject(subject);

    }
}
