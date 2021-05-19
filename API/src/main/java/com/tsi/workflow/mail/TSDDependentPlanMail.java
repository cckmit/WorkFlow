/**
 * 
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import java.util.Set;

/**
 * @author vinoth.ponnurangan
 *
 */
public class TSDDependentPlanMail extends MailMessage {

    private Set<String> dependentPlan;
    private String loadedPlan;

    public Set<String> getDependentPlan() {
	return dependentPlan;
    }

    public void setDependentPlan(Set<String> dependentPlan) {
	this.dependentPlan = dependentPlan;
    }

    public String getLoadedPlan() {
	return loadedPlan;
    }

    public void setLoadedPlan(String loadedPlan) {
	this.loadedPlan = loadedPlan;
    }

    @Override
    public void processMessage() {

	StringBuilder message = new StringBuilder();
	String subject = getLoadedPlan() + " loaded to Production having same source artifacts as your  " + getDependentPlan();

	message.append(getLoadedPlan()).append(" loaded to Production having same source artifacts as your ").append(getDependentPlan()).append(" Please take below actions: ");
	message.append(" <br><br>  1. Please make sure your plan has latest changes and update load date if you are still planning to go to PROD with all your changes. ").append(" <br><br>  2. If your plan is no longer valid then please delete Implementation(s)/Implementation Plan to avoid any dependency for others . ");

	this.setSubject(subject);
	this.setMessage(message.toString());

    }

}
