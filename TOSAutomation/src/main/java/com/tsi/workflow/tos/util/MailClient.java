/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos.util;

import com.tsi.workflow.tos.TOSConfig;
import com.tsi.workflow.tos.model.TOSRequest;
import com.tsi.workflow.tos.model.TOSResult;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author prabhu.prabhakaran
 */
public class MailClient {

    private static final Logger LOG = Logger.getLogger(MailClient.class.getName());
    // <editor-fold defaultstate="collapsed" desc="Connection Establishment Mails">

    public static void sendConnectionSuccessMail(TOSRequest lRequest, String pThreadName) {
	String subject = TOSConfig.getInstance().getTOSEnvName() + " : Client Connection Mail for the " + lRequest.getSystem();
	String mailMessage = "TOS Client Connection Established for the " + lRequest.getSystem() + "" + "<BR>CPU : " + lRequest.getCpuName() + "<BR>IP : " + lRequest.hashIPAddress() + "<BR>Thread Name :" + pThreadName;
	try {
	    MailUtil.getInstance().sendTOSMail(subject, mailMessage, Level.INFO.toString());
	} catch (Exception e) {
	    LOG.info("Error in Sending Connection Success Mail", e);
	}
    }

    public static void sendConnectionFailMail(TOSRequest lRequest) {
	String subject = TOSConfig.getInstance().getTOSEnvName() + " : Client Connection Mail for the " + lRequest.getSystem();
	String mailMessage = "TOS Client Connection Failed to Establish for the " + lRequest.getSystem() + "<BR>CPU : " + lRequest.getCpuName() + "<BR>IP : " + lRequest.hashIPAddress();
	try {
	    MailUtil.getInstance().sendTOSMail(subject, mailMessage, Level.ERROR.toString());
	} catch (Exception e) {
	    LOG.info("Error in Sending Connection Failure Mail", e);
	}
    }

    public static void sendDisconnectionMail(String pSystem, String pCPU, String pThreadName) {
	String subject = TOSConfig.getInstance().getTOSEnvName() + " : Client Disconnection Mail for the " + pSystem;
	String mailMessage = "TOS Client Disconnected for the " + pSystem + "<BR>CPU : " + pCPU + "<BR>Thread Name :" + pThreadName;
	try {
	    MailUtil.getInstance().sendTOSMail(subject, mailMessage, Level.ERROR.toString());
	} catch (Exception e) {
	    LOG.info("Error in Sending Disconnection Mail", e);
	}
    }

    // </editor-fold>
    public static void sendPendingMessagesMail(String pContent) {
	String subject = TOSConfig.getInstance().getTOSEnvName() + " : Pending Messages for Response from TOS";
	try {
	    MailUtil.getInstance().sendTOSMail(subject, pContent, Level.INFO.toString());
	} catch (Exception e) {
	    LOG.info("Error in Sending Disconnection Mail", e);
	}
    }

    public static void sendSuccessMail(TOSResult pResponse, boolean isRequest, boolean isRetry) {
	String subject = TOSConfig.getInstance().getTOSEnvName() + " : TOS Notification Mail for " + pResponse.getLoadset();
	String mailMessage = "";
	if (isRequest) {
	    if (isRetry) {
		mailMessage = pResponse.getCommand() + " TOS Retry Request Sent Success from API to TOS Server for the " + pResponse.getSystem() + ", CPU " + pResponse.getCpuName() + ", Loadset " + pResponse.getLoadset();
	    } else {
		mailMessage = pResponse.getCommand() + " TOS Request Sent Success from API to TOS Server for the " + pResponse.getSystem() + ", CPU " + pResponse.getCpuName() + ", Loadset " + pResponse.getLoadset();
	    }
	} else {
	    if (pResponse.isCoexist() && pResponse.isIsPrimary()) {
		mailMessage = pResponse.getCommand() + " TOS Primary Response Sent Success from TOS Server to API for the " + pResponse.getSystem() + ", CPU " + pResponse.getCpuName() + ", Loadset " + pResponse.getLoadset() + ", RC " + pResponse.getReturnValue() + ", Message " + pResponse.getMessage();
	    } else if (pResponse.isCoexist() && !pResponse.isIsPrimary()) {
		mailMessage = pResponse.getCommand() + " TOS Secondary Response Sent Success from TOS Server to API for the " + pResponse.getSystem() + ", CPU " + pResponse.getCpuName() + ", Loadset " + pResponse.getLoadset() + ", RC " + pResponse.getReturnValue() + ", Message " + pResponse.getMessage();
	    } else {
		mailMessage = pResponse.getCommand() + " TOS Response Sent Success from TOS Server to API for the " + pResponse.getSystem() + ", CPU " + pResponse.getCpuName() + ", Loadset " + pResponse.getLoadset() + ", RC " + pResponse.getReturnValue() + ", Message " + pResponse.getMessage();
	    }
	}
	try {
	    MailUtil.getInstance().sendTOSMail(subject, mailMessage, Level.INFO.toString());
	} catch (Exception e) {
	    LOG.info("Error in sending mail", e);
	}
    }

    public static void sendFailMail(TOSResult pResponse, boolean isRequest, boolean isRetry) {
	String subject = TOSConfig.getInstance().getTOSEnvName() + " : TOS Notification Mail for " + pResponse.getLoadset();
	String mailMessage = "";
	if (isRequest) {
	    if (isRetry) {
		mailMessage = pResponse.getCommand() + " TOS Retry Request Sent Failed from API to TOS Server for the " + pResponse.getSystem() + ", CPU " + pResponse.getCpuName() + ", Loadset " + pResponse.getLoadset();
	    } else {
		mailMessage = pResponse.getCommand() + " TOS Request Sent Failed from API to TOS Server for the " + pResponse.getSystem() + ", CPU " + pResponse.getCpuName() + ", Loadset " + pResponse.getLoadset();
	    }
	} else {
	    if (pResponse.isCoexist() && pResponse.isIsPrimary()) {
		mailMessage = pResponse.getCommand() + " TOS Primary Response Sent Failed from TOS Server to API for the " + pResponse.getSystem() + ", CPU " + pResponse.getCpuName() + ", Loadset " + pResponse.getLoadset() + ", RC " + pResponse.getReturnValue() + ", Message " + pResponse.getMessage();
	    } else if (pResponse.isCoexist() && !pResponse.isIsPrimary()) {
		mailMessage = pResponse.getCommand() + " TOS Secondary Response Sent Failed from TOS Server to API for the " + pResponse.getSystem() + ", CPU " + pResponse.getCpuName() + ", Loadset " + pResponse.getLoadset() + ", RC " + pResponse.getReturnValue() + ", Message " + pResponse.getMessage();
	    } else {
		mailMessage = pResponse.getCommand() + " TOS Response Sent Failed from TOS Server to API for the " + pResponse.getSystem() + ", CPU " + pResponse.getCpuName() + ", Loadset " + pResponse.getLoadset() + ", RC " + pResponse.getReturnValue() + ", Message " + pResponse.getMessage();
	    }
	}
	try {
	    MailUtil.getInstance().sendTOSMail(subject, mailMessage, Level.ERROR.toString());
	} catch (Exception e) {
	    LOG.info("Error in sending mail", e);
	}
    }
}
