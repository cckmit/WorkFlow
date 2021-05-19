package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.DateHelper;
import java.util.Date;
import java.util.List;

public class PutLevelDeploymentDateMail extends MailMessage {
    private String targetSystem;
    private Date deploymentDate;
    private Boolean devFlag = false;
    private List<String> putLevelStatus;

    public String getTargetSystem() {
	return targetSystem;
    }

    public void setTargetSystem(String targetSystem) {
	this.targetSystem = targetSystem;
    }

    public Date getDeploymentDate() {
	return deploymentDate;
    }

    public void setDeploymentDate(Date deploymentDate) {
	this.deploymentDate = deploymentDate;
    }

    public Boolean getDevFlag() {
	return devFlag;
    }

    public void setDevFlag(Boolean devFlag) {
	this.devFlag = devFlag;
    }

    public List<String> getPutLevelStatus() {
	return putLevelStatus;
    }

    public void setPutLevelStatus(List<String> putLevelStatus) {
	this.putLevelStatus = putLevelStatus;
    }

    @Override
    public void processMessage() {
	StringBuilder message = new StringBuilder();
	String subject = " ";
	if (getDevFlag()) {
	    subject = "  Action required on zTPF level in  " + String.join(",", getPutLevelStatus()) + " state";
	    message.append("  The zTPF Level for  ").append(getTargetSystem()).append("  has reached its scheduled deployment date but the status is still in ").append(String.join(",", getPutLevelStatus())).append(" state .").append("Kindly take corrective action.");
	} else {
	    subject = "  Action required: New Production zTPF Level for  " + getTargetSystem();
	    message.append("  The zTPF Level for  ").append(getTargetSystem()).append(" has been deployed in the zTPF Production System effective ").append(DateHelper.convertGMTtoEST(getDeploymentDate())).append(".").append(" Kindly ensure the following applicable activities are initiated. ").append(" <br> 1. LC to update current production zTPF level details  <br> ").append("  2. Update base .cfg file  <br> ").append("  3. Permissions for the new /ztpfsys folders  <br> ")
		    .append("  4. Re-index Impact Analysis  <br> ").append("  5. Update symbolic links for all \"prod\" folders  <br> ").append("  6. Update environment variables in /etc/profile.d/ztpf.sh  <br> ").append("  7. Update maketpf rules files <br> ").append("  8. Populate the prod PDS(zOS) with new put level offline items  <br> ").append("  9. Populate the .mak files into /ztpfsys folders  <br> ").append("  10. Rename old versions as ~  <br> ")
		    .append("  11. Data migration to Git  <br> ");
	}
	this.setSubject(subject);
	this.setMessage(message.toString());
    }
}
