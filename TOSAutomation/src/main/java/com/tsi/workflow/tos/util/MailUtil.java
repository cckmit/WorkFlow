/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos.util;

import com.google.gson.Gson;
import com.tsi.workflow.tos.TOSConfig;
import com.workflow.mail.core.Address;
import com.workflow.mail.core.MailItem;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author prabhu.prabhakaran
 */
public class MailUtil {

    private static final Logger LOG = Logger.getLogger(MailUtil.class.getName());
    private static final String TOS_CLIENT_STARTED_MESSAGE = "TOS Application has been Started on Server " + TOSConfig.getInstance().getTOSEnvName();
    private static final String TOS_CLIENT_STOPPED_MESSAGE = "TOS Application has been Stopped on Server " + TOSConfig.getInstance().getTOSEnvName();
    private static final String SUBJECT = "TOS Service Notification";
    private static MailUtil lMailUtil;

    HttpClient client;
    // String MessageTemplate;

    // private String subject;
    // private String message;
    public static MailUtil getInstance() {
	if (lMailUtil == null) {
	    lMailUtil = new MailUtil();
	}
	return lMailUtil;
    }

    private MailUtil() {
	client = new DefaultHttpClient();
    }

    // <editor-fold defaultstate="collapsed" desc="Start and Stop Mails">
    public void sendServerStarted() throws Exception {
	Set<Address> toAddressList = new HashSet<>();
	Set<Address> ccAddressList = new HashSet<>();
	toAddressList.add(new Address(TOSConfig.getInstance().getDevOpsMailId(), "DevOps Team"));
	sendNotification(toAddressList, ccAddressList, SUBJECT, TOS_CLIENT_STARTED_MESSAGE);
    }

    public void sendServerStopped() throws Exception {
	Set<Address> toAddressList = new HashSet<>();
	Set<Address> ccAddressList = new HashSet<>();
	toAddressList.add(new Address(TOSConfig.getInstance().getDevOpsMailId(), "DevOps Team"));
	sendNotification(toAddressList, ccAddressList, SUBJECT, TOS_CLIENT_STOPPED_MESSAGE);
    }
    // </editor-fold>

    public void sendTOSMail(String subject, String message, String level) throws Exception {
	Set<Address> toAddressList = new HashSet<>();
	Set<Address> ccAddressList = new HashSet<>();
	toAddressList.add(new Address(TOSConfig.getInstance().getDevOpsMailId(), "DevOps Team"));
	LOG.info("Mail : " + subject + " / " + message);
	String lMessage;
	if (level.equals(Level.INFO.toString())) {
	    lMessage = "<font color=\"green\"> " + message + "</font>";
	} else {
	    lMessage = "<font color=\"red\"> " + message + "</font>";
	}
	sendNotification(toAddressList, ccAddressList, subject, lMessage);
    }

    private void sendNotification(Collection<Address> toAddressList, Collection<Address> cCAddressList, String subject, String message) throws Exception {
	MailItem lMailItem = new MailItem(TOSConfig.getInstance().getAzureId());
	lMailItem.setFrom(new Address(TOSConfig.getInstance().getFromMailId(), TOSConfig.getInstance().getSendingApplication()));
	lMailItem.setToContacts(toAddressList);
	lMailItem.setCcContacts(cCAddressList);
	lMailItem.setSubject(subject);
	lMailItem.setBody(message);
	lMailItem.setBodyType("text/html");

	URIBuilder lBuilder = new URIBuilder(TOSConfig.getInstance().getAzureMailURL() + "ProcessEmailFunction");
	lBuilder.setParameter("code", TOSConfig.getInstance().getAzureMailKey());
	HttpPost lPost = new HttpPost(lBuilder.build());
	lPost.setEntity(new StringEntity(new Gson().toJson(lMailItem)));
	HttpResponse lResponse = client.execute(lPost);
	lResponse.getEntity().consumeContent();
	StatusLine statusLine = lResponse.getStatusLine();
	int statusCode = statusLine.getStatusCode();
	if (statusCode == 200) {
	    LOG.info("Mail (" + subject + ") to " + String.join(",", toAddressList) + " sent successfully");
	} else {
	    LOG.info("Mail (" + subject + ") to " + String.join(",", toAddressList) + " failed");
	}
    }

}
