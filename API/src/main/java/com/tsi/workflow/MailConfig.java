/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import com.tsi.workflow.interfaces.IMailConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Deepa
 */
@Component
public class MailConfig implements IMailConfig {

    @Value("${mail.azure.from}")
    private String fromMailId;
    @Value("${mail.azure.url}")
    private String azureURL;
    @Value("${mail.azure.password}")
    private String azureKey;
    @Value("${mail.azure.senderid}")
    private String azureId;
    @Value("${mail.send.app.name}")
    private String sendingApplication;

    public String getAzureId() {
	return azureId;
    }

    public void setAzureId(String azureId) {
	this.azureId = azureId;
    }

    public String getAzureURL() {
	return azureURL;
    }

    public void setAzureURL(String azureURL) {
	this.azureURL = azureURL;
    }

    public String getAzureKey() {
	return azureKey;
    }

    public void setAzureKey(String azureKey) {
	this.azureKey = azureKey;
    }

    public String getFromMailId() {
	return fromMailId;
    }

    public void setFromMailId(String fromMailId) {
	this.fromMailId = fromMailId;
    }

    public String getSendingApplication() {
	return sendingApplication;
    }

    public void setSendingApplication(String sendingApplication) {
	this.sendingApplication = sendingApplication;
    }

}
