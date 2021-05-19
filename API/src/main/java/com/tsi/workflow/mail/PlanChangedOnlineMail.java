package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.DateHelper;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Vinoth
 */
public class PlanChangedOnlineMail extends MailMessage {

    private String planId;
    private Map<String, Date> loadDateTargetSys;
    private String tosServerId;

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public Map<String, Date> getLoadDateTargetSys() {
	return loadDateTargetSys;
    }

    public void setLoadDateTargetSys(Map<String, Date> loadDateTargetSys) {
	this.loadDateTargetSys = loadDateTargetSys;
    }

    public String getTosServerId() {
	return tosServerId;
    }

    public void setTosServerId(String tosServerId) {
	this.tosServerId = tosServerId;
    }

    @Override
    public void processMessage() {
	StringBuilder message = new StringBuilder();
	this.setSubject("JENKINS JOB FAILED ONLINE/FALLBACK FEEDBACK- " + getPlanId());
	message.append(" Team,Jenkins job failed  while doing online/fallback ").append(" feedback on GIT for the Plan ").append(getPlanId()).append("<br><br>");

	getLoadDateTargetSys().forEach((tarSystem, newLoadDate) -> {
	    if (newLoadDate != null) {
		message.append("System  : ").append(tarSystem).append(" Load Date : ").append(DateHelper.convertGMTtoEST(newLoadDate)).append(" <br>");
	    }
	});
	this.setMessage(message.toString());
    }
}
