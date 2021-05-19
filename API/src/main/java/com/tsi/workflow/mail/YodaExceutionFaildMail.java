package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vinoth
 */
public class YodaExceutionFaildMail extends MailMessage {

    private String planId;
    private List<String> vparList = new ArrayList<>();
    private String loadSetName;

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public List<String> getVparList() {
	return vparList;
    }

    public void setVparList(List<String> vparList) {
	this.vparList = vparList;
    }

    public String getLoadSetName() {
	return loadSetName;
    }

    public void setLoadSetName(String loadSetName) {
	this.loadSetName = loadSetName;
    }

    @Override
    public void processMessage() {
	StringBuilder message = new StringBuilder();

	this.setSubject("YODA DEACTIVATE/DELETE FAILED-  " + getPlanId());

	message.append(" Team,  While deactive and delete loadset following vpars ").append(getVparList()).append(" and LoadSet Name ").append(getLoadSetName()).append("  we are getting the exception .").append(getPlanId());

	this.setMessage(message.toString());
    }
}
