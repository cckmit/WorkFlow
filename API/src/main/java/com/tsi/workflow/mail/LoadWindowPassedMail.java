/**
 *
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.DateHelper;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author vinoth.ponnurangan
 *
 */
public class LoadWindowPassedMail extends MailMessage {

    private String impPlan;

    private Date loadDateTime;

    private Set<String> targetSystem = new HashSet<>();

    private String planDesc;

    public String getImpPlan() {
	return impPlan;
    }

    public void setImpPlan(String impPlan) {
	this.impPlan = impPlan;
    }

    public Date getLoadDateTime() {
	return loadDateTime;
    }

    public void setLoadDateTime(Date loadDateTime) {
	this.loadDateTime = loadDateTime;
    }

    public Set<String> getTargetSystem() {
	return targetSystem;
    }

    public void setTargetSystem(Set<String> targetSystem) {
	this.targetSystem = targetSystem;
    }

    public String getPlanDesc() {
	return planDesc;
    }

    public void setPlanDesc(String planDesc) {
	this.planDesc = planDesc;
    }

    @Override
    public void processMessage() {

	StringBuilder message = new StringBuilder();
	String subject = " Load date is due soon for  " + getImpPlan();

	message.append(getImpPlan()).append(" is due for production deployment soon.").append(" Please make sure all actions are performed ").append(" to avoid missing the load date. ");

	message.append("<br><br>").append(" Plan Description : ").append(getPlanDesc());

	message.append("<br><br>").append(String.join(",", getTargetSystem())).append("  : ").append(DateHelper.convertGMTtoEST(getLoadDateTime()));

	message.append("<br><br>").append(" Note: If load date/time will be missed then after 2 days  ").append(getImpPlan()).append("  will be auto rejected. ");

	this.setSubject(subject);
	this.setMessage(message.toString());

    }

}
