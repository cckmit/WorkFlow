/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workflow.mail.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.RandomStringUtils;

/**
 *
 * @author USER
 */
public class MailItem {

    private String MessageId;
    private String SendingApplication;
    private String SenderId;
    private Address From;
    private Address ReplyTo;
    private Collection<Address> ToContacts = new ArrayList<>();
    private Collection<Address> CcContacts = new ArrayList<>();
    private Collection<Address> BccContacts = new ArrayList<>();
    private String Subject;
    private String Body;
    private String BodyType;
    private List<Attachment> Attachments;

    public MailItem(String pSenderId) {
	this.SendingApplication = "zTPF DevOps ToolChain";
	this.SenderId = pSenderId;
	this.MessageId = "zTPFM_WAPI_" + RandomStringUtils.randomAlphanumeric(15);
    }

    public String getMessageId() {
	return MessageId;
    }

    public void setMessageId(String messageId) {
	this.MessageId = messageId;
    }

    public String getSendingApplication() {
	return SendingApplication;
    }

    public void setSendingApplication(String sendingApplication) {
	this.SendingApplication = sendingApplication;
    }

    public String getSenderId() {
	return SenderId;
    }

    public void setSenderId(String senderId) {
	this.SenderId = senderId;
    }

    public Address getFrom() {
	return From;
    }

    public void setFrom(Address from) {
	this.From = from;
    }

    public Address getReplyTo() {
	return ReplyTo;
    }

    public void setReplyTo(Address ReplyTo) {
	this.ReplyTo = ReplyTo;
    }

    public Collection<Address> getToContacts() {
	return ToContacts;
    }

    public void setToContacts(Collection<Address> toContacts) {
	this.ToContacts = toContacts;
    }

    public void addToContacts(Address toContact) {
	this.ToContacts.add(toContact);
    }

    public Collection<Address> getCcContacts() {
	return CcContacts;
    }

    public void setCcContacts(Collection<Address> ccContacts) {
	this.CcContacts = ccContacts;
    }

    public void addCcContacts(Address ccContact) {
	this.CcContacts.add(ccContact);
    }

    public Collection<Address> getBccContacts() {
	return BccContacts;
    }

    public void setBccContacts(Collection<Address> bccContacts) {
	this.BccContacts = bccContacts;
    }

    public void addBccContacts(Address bccContact) {
	this.BccContacts.add(bccContact);
    }

    public String getSubject() {
	return Subject;
    }

    public void setSubject(String subject) {
	this.Subject = subject;
    }

    public String getBody() {
	return Body;
    }

    public void setBody(String body) {
	this.Body = body;
    }

    public String getBodyType() {
	return BodyType;
    }

    public void setBodyType(String bodyType) {
	this.BodyType = bodyType;
    }

    public List<Attachment> getAttachments() {
	return Attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
	this.Attachments = attachments;
    }
}
