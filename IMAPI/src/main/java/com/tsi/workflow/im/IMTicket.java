/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.im;

import com.mks.api.Command;
import com.mks.api.FileOption;
import com.mks.api.MultiValue;
import com.mks.api.Option;
import com.mks.api.Session;
import com.mks.api.response.Response;
import com.tsi.workflow.im.IMOptions.LoadType;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author USER
 */
public class IMTicket {

    private static final Logger LOG = Logger.getLogger(IMTicket.class.getName());

    public static String createIssue(Session lSession) throws Exception {
	return createIssue(lSession, LoadType.PRE_PROD, null);
    }

    public static String createIssue(Session lSession, String IMTicket) throws Exception {
	return createIssue(lSession, LoadType.PROD, IMTicket);
    }

    public static String createIssue(Session lSession, LoadType pLoadType, String IMTicket) throws Exception {
	String lIMTicket = "";

	Command lCreateIssueCommand = new Command(Command.IM, "createissue");
	lCreateIssueCommand.addOption(new Option("type", pLoadType.getName()));

	HashMap<String, String> lValues = new HashMap<>();
	if (pLoadType == LoadType.PROD) {
	    lValues = getProdParameterValues();
	} else {
	    lValues = getPreProdParameterValues();
	}

	for (Map.Entry<String, String> entrySet : lValues.entrySet()) {
	    String key = entrySet.getKey();
	    String value = entrySet.getValue();
	    MultiValue mv = new MultiValue("=");
	    mv.add(key);
	    mv.add(value);
	    lCreateIssueCommand.addOption(new Option("field", mv));
	}

	if (pLoadType == LoadType.PROD && IMTicket != null) {
	    MultiValue mv = new MultiValue(":");
	    mv.add("Backward Relationships");
	    mv.add(IMTicket);
	    lCreateIssueCommand.addOption(new Option("addRelationships", mv));
	}

	String[] toStringArray = lCreateIssueCommand.toStringArray();
	LOG.debug(String.join(" ", toStringArray));

	Response lCTResponse = lSession.createCmdRunner().execute(lCreateIssueCommand);

	if (lCTResponse.getExitCode() == 0) {
	    String lCTResponseMessage = lCTResponse.getResult().getMessage();
	    Matcher matcher = pLoadType.getResultPattern().matcher(lCTResponseMessage);
	    if (matcher.matches()) {
		lIMTicket = matcher.group(1);
	    }
	    LOG.info(pLoadType.name() + " IM Ticket created successfully " + lIMTicket);
	} else {
	    LOG.error(pLoadType.name() + " IM Ticket creation failed");
	}
	return lIMTicket;
    }

    public static boolean editIssue(Session lSession, LoadType pLoadType, String lIMTicket) throws Exception {
	File InstallESPScript;
	File FallbackESPScript;

	String lDir = IMOptions.getProperty(IMOptions.OtherKeys.FILEPATH);
	if (pLoadType == LoadType.PROD) {
	    InstallESPScript = new File(lDir, "ESPAutomation_PN_" + lIMTicket + ".txt");
	    FallbackESPScript = new File(lDir, "ESPAutomation_PNFB_" + lIMTicket + ".txt");
	} else {
	    InstallESPScript = new File(lDir, "ESPAutomation_PP_" + lIMTicket + ".txt");
	    FallbackESPScript = new File(lDir, "ESPAutomation_PPFB_" + lIMTicket + ".txt");
	}

	String content = getURL(false, pLoadType);
	FileUtils.write(InstallESPScript, content, "UTF-8", false);

	content = getURL(true, pLoadType);
	FileUtils.write(FallbackESPScript, content, "UTF-8", false);

	Command lEditIssueCommand = new Command(Command.IM, "editissue");
	lEditIssueCommand.addSelection(lIMTicket);
	lEditIssueCommand.addOption(new Option("batch"));
	lEditIssueCommand.addOption(new FileOption("addAttachment", InstallESPScript));
	lEditIssueCommand.addOption(new FileOption("addAttachment", FallbackESPScript));

	String[] toStringArray = lEditIssueCommand.toStringArray();
	LOG.debug(String.join(" ", toStringArray));

	Response lETResponse = lSession.createCmdRunner().execute(lEditIssueCommand);
	if (lETResponse.getExitCode() == 0) {
	    LOG.info("File attached successfully in the " + pLoadType.name() + " IM Ticket " + lIMTicket);
	    return true;
	} else {
	    LOG.error("File attachement failed in the " + pLoadType.name() + " IM Ticket " + lIMTicket);
	    return false;
	}
    }

    public static HashMap<String, String> getPreProdParameterValues() {
	HashMap<String, String> lMap = new HashMap<>();
	for (IMOptions.PreProdKeys lKey : IMOptions.PreProdKeys.values()) {
	    lMap.put(lKey.getName(), IMOptions.getProperty(lKey));
	}
	return lMap;
    }

    public static HashMap<String, String> getProdParameterValues() {
	HashMap<String, String> lMap = new HashMap<>();
	for (IMOptions.ProdKeys lKey : IMOptions.ProdKeys.values()) {
	    lMap.put(lKey.getName(), IMOptions.getProperty(lKey));
	}
	return lMap;
    }

    public static String getURL(boolean isFallBack, LoadType pLoadType) {
	StringBuilder lURL = new StringBuilder("https://vhldvztdt001.tvlport.net:8443/jenkins/job/Production/job/").append(IMOptions.getProperty(IMOptions.OtherKeys.JOBNAME)).append("_Deploy/buildWithParameters?").append("environment=").append(pLoadType.name()).append("&company=").append(IMOptions.getProperty(IMOptions.OtherKeys.COMPANY));

	if (isFallBack) {
	    lURL.append("&buildId=").append(IMOptions.getProperty(IMOptions.OtherKeys.FALLBACK_VERSION));
	} else {
	    lURL.append("&buildId=").append(IMOptions.getProperty(IMOptions.PreProdKeys.PRODUCT_VERSION));
	}

	if (pLoadType == LoadType.PROD) {
	    lURL.append("&linuxServers=").append(IMOptions.getProperty(IMOptions.OtherKeys.PROD_LINUX_SERVERS).replaceAll("\\[", "").replaceAll("\\]", "")).append("&zLinuxServers=").append(IMOptions.getProperty(IMOptions.OtherKeys.PROD_ZLINUX_SERVERS).replaceAll("\\[", "").replaceAll("\\]", "")).append("&f5Servers=").append(IMOptions.getProperty(IMOptions.OtherKeys.PROD_F5_SERVERS).replaceAll("\\[", "").replaceAll("\\]", "")).append("&TOSServers=")
		    .append(IMOptions.getProperty(IMOptions.OtherKeys.PROD_TOS_SERVERS).replaceAll("\\[", "").replaceAll("\\]", ""));
	} else {
	    lURL.append("&linuxServers=").append(IMOptions.getProperty(IMOptions.OtherKeys.PRE_PROD_LINUX_SERVERS).replaceAll("\\[", "").replaceAll("\\]", "")).append("&zLinuxServers=").append(IMOptions.getProperty(IMOptions.OtherKeys.PRE_PROD_ZLINUX_SERVERS).replaceAll("\\[", "").replaceAll("\\]", "")).append("&f5Servers=").append(IMOptions.getProperty(IMOptions.OtherKeys.PRE_PROD_F5_SERVERS).replaceAll("\\[", "").replaceAll("\\]", "")).append("&TOSServers=")
		    .append(IMOptions.getProperty(IMOptions.OtherKeys.PRE_PROD_TOS_SERVERS).replaceAll("\\[", "").replaceAll("\\]", ""));
	}

	LOG.debug(pLoadType.getName() + " " + (isFallBack ? "Fallback" : "Deploy") + " URL = " + lURL);

	return "LNXSCRIPT SCR='/usr/bin/curl' ARGS='--insecure --silent -X POST " + lURL.toString() + " --user mtpservice:2f32b35fb4dbc444b416e272f834206f'";
    }
}
