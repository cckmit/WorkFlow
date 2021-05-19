/**
 *
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.beans.ui.ProdPlanDetails;
import com.tsi.workflow.beans.ui.ProdSystemDetails;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.DateHelper;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author vinoth.ponnurangan
 *
 */
public class ProductionLoadDetailsMail extends MailMessage {

    private static final Logger LOG = Logger.getLogger(ProductionLoadDetailsMail.class.getName());

    Map<String, ProdPlanDetails> beforeDayDetails = new HashMap<>();
    Map<String, ProdPlanDetails> nextDayDetails = new HashMap<>();

    @Autowired
    private VelocityEngine velocityEngine;

    private String companyName;

    /**
     * @return the beforeDayDetails
     */
    public Map<String, ProdPlanDetails> getBeforeDayDetails() {
	return beforeDayDetails;
    }

    /**
     * @param beforeDayDetails
     *            the beforeDayDetails to set
     */
    public void setBeforeDayDetails(Map<String, ProdPlanDetails> beforeDayDetails) {
	this.beforeDayDetails = beforeDayDetails;
    }

    /**
     * @return the nextDayDetails
     */
    public Map<String, ProdPlanDetails> getNextDayDetails() {
	return nextDayDetails;
    }

    /**
     * @param nextDayDetails
     *            the nextDayDetails to set
     */
    public void setNextDayDetails(Map<String, ProdPlanDetails> nextDayDetails) {
	this.nextDayDetails = nextDayDetails;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
	return companyName;
    }

    /**
     * @param companyName
     *            the companyName to set
     */
    public void setCompanyName(String companyName) {
	this.companyName = companyName;
    }

    @Override
    public void processMessage() {
	String companyProfile = null;
	if (getCompanyName().equals("TravelPort")) {
	    companyProfile = "TravelPort";
	}
	if (getCompanyName().equals("Delta")) {
	    companyProfile = "Delta";
	}
	LocalDateTime ldt = LocalDateTime.now();
	this.setSubject("Production Load Summary " + DateTimeFormatter.ofPattern("MM-dd-yyyy", Locale.ENGLISH).format(ldt) + " for " + companyProfile);
	VelocityContext context = new VelocityContext();
	StringWriter stringWriter = new StringWriter();
	List<String> prodLoadSet = getLoadSetDetails();
	boolean isHeaderAdded = false;
	for (Map.Entry<String, ProdPlanDetails> entrySet : getBeforeDayDetails().entrySet()) {
	    Boolean colorCode = false;
	    String planid = null;
	    String background = null;
	    for (ProdSystemDetails lSystem : entrySet.getValue().getProdSystemDetails()) {
		if (prodLoadSet.contains(lSystem.getProdstatus())) {
		    colorCode = true;
		}
	    }
	    if (colorCode) {
		planid = "<font color=\"red\">" + entrySet.getValue().getPlanid() + "</font>";
	    } else {
		planid = entrySet.getValue().getPlanid();
	    }
	    if (Constants.PlanStatus.getPlanStatusBeforeProdLoad().containsKey(entrySet.getValue().getPlanstatus())) {
		background = "#B4C6E7";
	    }
	    if (Constants.PlanStatus.getPlanStatusAfterProdLoad().containsKey(entrySet.getValue().getPlanstatus())) {
		background = "#A8D08D";
	    }
	    if (Constants.PlanStatus.getPlanStatusAfterOnline().containsKey(entrySet.getValue().getPlanstatus())) {
		background = "#F7CAAC";
	    }

	    context.put("background", background);
	    context.put("plan", entrySet.getValue());
	    context.put("planId", planid);
	    if (!isHeaderAdded) {
		context.put("formBck", "#008000");
		context.put("message", "<font color=\"white\">" + "Production deployments carried out on  " + DateHelper.convertGMTtoESTDate(entrySet.getValue().getActivateddatetime()) + "</font>");
		isHeaderAdded = true;
	    } else {
		context.put("message", "NONE");
	    }
	    velocityEngine.mergeTemplate("/templates/email-template.vm", "UTF-8", context, stringWriter);
	}
	isHeaderAdded = false;
	for (Map.Entry<String, ProdPlanDetails> entrySet : getNextDayDetails().entrySet()) {
	    Boolean colorCode = false;
	    String planid = null;
	    String background = null;
	    for (ProdSystemDetails lSystem : entrySet.getValue().getProdSystemDetails()) {
		if (prodLoadSet.contains(lSystem.getProdstatus())) {
		    colorCode = true;
		}
	    }
	    if (colorCode) {
		planid = "<font color=\"red\">" + entrySet.getValue().getPlanid() + "</font>";
	    } else {
		planid = entrySet.getValue().getPlanid();
	    }
	    if (Constants.PlanStatus.getPlanStatusBeforeProdLoad().containsKey(entrySet.getValue().getPlanstatus())) {
		background = "#B4C6E7";
	    }
	    if (Constants.PlanStatus.getPlanStatusAfterProdLoad().containsKey(entrySet.getValue().getPlanstatus())) {
		background = "#A8D08D";
	    }
	    context.put("background", background);
	    context.put("planId", planid);
	    context.put("plan", entrySet.getValue());
	    if (!isHeaderAdded) {
		context.put("formBck", "#FFA500");
		context.put("message", "<font color=\"white\">" + "Production deployments planned for " + DateHelper.convertGMTtoESTDate(entrySet.getValue().getLoaddatetime()) + "</font>");
		isHeaderAdded = true;
	    } else {
		context.put("message", "NONE");
	    }
	    velocityEngine.mergeTemplate("/templates/email-template.vm", "UTF-8", context, stringWriter);
	}
	String lMailContent = stringWriter.toString();
	this.setMessage(lMailContent);

    }

    private List<String> getLoadSetDetails() {
	List<String> prodLoadSet = new ArrayList<>();
	prodLoadSet.add("DEACTIVATED");
	prodLoadSet.add("DELETED");
	prodLoadSet.add("FALLBACK_LOADED");
	prodLoadSet.add("FALLBACK_ACTIVATED");
	prodLoadSet.add("FALLBACK_ACCEPTED");
	return prodLoadSet;
    }

}
