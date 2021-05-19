/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.util;

import static com.tsi.workflow.Main.duplicateFiles;
import static com.tsi.workflow.Main.excludedRepos;
import static com.tsi.workflow.Main.includedRepos;
import static com.tsi.workflow.Main.lDBSchema;
import static com.tsi.workflow.Main.suspiciousFiles;

import com.google.gson.Gson;
import com.tsi.workflow.Main;
import com.tsi.workflow.config.AppConfig;
import com.workflow.mail.core.Address;
import com.workflow.mail.core.MailItem;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

/**
 *
 * @author prabhu.prabhakaran
 */
public class MailUtil {

    private static final Logger LOG = Logger.getLogger(MailUtil.class.getName());
    private static final String SUBJECT = "Recon Job Notification";
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

    public void sendMail(String message) throws Exception {
	Set<Address> toAddressList = new HashSet<>();
	Set<Address> ccAddressList = new HashSet<>();
	toAddressList.add(new Address(AppConfig.getInstance().getDevOpsMailId(), "DevOps Team"));
	toAddressList.add(new Address("prabhu.prabhakaran@travelport.com", "Prabhu Prabhakaran"));
	sendNotification(toAddressList, ccAddressList, SUBJECT, message);
    }

    private void sendNotification(Collection<Address> toAddressList, Collection<Address> cCAddressList, String subject, String message) throws Exception {
	MailItem lMailItem = new MailItem(AppConfig.getInstance().getAzureId());
	lMailItem.setFrom(new Address(AppConfig.getInstance().getFromMailId(), lMailItem.getSendingApplication()));
	lMailItem.setToContacts(toAddressList);
	lMailItem.setCcContacts(cCAddressList);
	lMailItem.setSubject(subject);
	lMailItem.setBody(message);
	lMailItem.setBodyType("text/html");

	URIBuilder lBuilder = new URIBuilder(AppConfig.getInstance().getAzureMailURL() + "ProcessEmailFunction");
	lBuilder.setParameter("code", AppConfig.getInstance().getAzureMailKey());
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

    public void sendCompletionMail() throws Exception {
	StringBuilder lBuilder = new StringBuilder();
	lBuilder.append("Data population completed for Environment ").append(AppConfig.getInstance().getSessionKey()).append("<BR>").append(" Duration : ").append(DurationFormatUtils.formatDuration(Main.duration, "HH:mm:ss:SS", true));

	Collections.sort(duplicateFiles);
	Collections.sort(suspiciousFiles);
	if (!suspiciousFiles.isEmpty()) {
	    lBuilder.append("<BR><BR>").append("Suspicious File(s) : <BR>").append(String.join("<BR>", suspiciousFiles));
	}
	if (!duplicateFiles.isEmpty()) {
	    lBuilder.append("<BR><BR>").append("Duplicate Files(s) : <BR>").append(String.join("<BR>", duplicateFiles));
	}
	sendMail(lBuilder.toString());
    }

    public void sendStartMail() throws Exception {
	StringBuilder lBuilder = new StringBuilder();
	lBuilder.append("Data population started for Environment ").append(AppConfig.getInstance().getSessionKey()).append("<BR>").append("DB : ").append(lDBSchema);
	if (!excludedRepos.isEmpty()) {
	    lBuilder.append("<BR>").append("Except Repo(s) : ").append(String.join(",", excludedRepos));
	}
	if (!includedRepos.isEmpty()) {
	    lBuilder.append("<BR>").append("Repo(s) : ").append(String.join(",", includedRepos));
	}
	sendMail(lBuilder.toString());
    }

}
