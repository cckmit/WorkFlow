/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.base;

import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.dao.UserSettingsDAO;
import com.tsi.workflow.helper.DelegateHelper;
import com.tsi.workflow.utils.Constants.MailSenderRole;
import com.workflow.mail.AzureMailUtil;
import com.workflow.mail.core.Address;
import com.workflow.mail.core.Attachment;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import javax.activation.MimetypesFileTypeMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
public abstract class MailMessage {

    private static final Logger LOG = Logger.getLogger(MailMessage.class.getName());

    @Autowired
    AzureMailUtil azureMailUtil;

    @Autowired
    WFConfig wFConfig;

    @Autowired
    UserSettingsDAO userSettingsDAO;

    @Autowired
    LDAPAuthenticatorImpl authenticator;

    @Autowired
    DelegateHelper lDelegateHelper;

    private String subject;
    private String message;

    private Date startDate;
    private int durationMins;
    private String calendarFileName;

    private Map<String, List<MailSenderRole>> toAddressUserIds = new HashMap<>();
    private Map<String, List<MailSenderRole>> cCAddressUserIds = new HashMap<>();
    private Map<String, String> toAddressEmailIds = new HashMap<>();
    private Map<String, String> cCAddressEmailIds = new HashMap<>();
    private Collection<String> attachmentsFiles = new HashSet<>();

    private boolean addToDevCentre;
    private boolean addCcDevCentre;
    private boolean addCcDeltaFallbackCentre;
    private boolean addCcLoadsControlCentre;
    private boolean addToEaVmDevCentre;
    private boolean addToDevOpsCentre;
    private boolean addCcDevOpsCentre;
    private boolean addToProdLoadCentre;
    // private boolean sendAsCalendar;
    private boolean addToDlCoreChangeTeam;

    // public void setSendAsCalendar(boolean sendAsCalendar) {
    // this.sendAsCalendar = sendAsCalendar;
    // }

    public AzureMailUtil getAzureMailUtil() {
	return azureMailUtil;
    }

    public void addToDevOpsCentre(boolean addToDevOpsCentre) {
	this.addToDevOpsCentre = addToDevOpsCentre;
    }

    public void addCcDevOpsCentre(boolean addCcDevOpsCentre) {
	this.addCcDevOpsCentre = addCcDevOpsCentre;
    }

    public void addToDEVCentre() {
	addToDevCentre = true;
    }

    public void addCcDEVCentre() {
	addCcDevCentre = true;
    }

    public void addCcDeltaFallbackCentre() {
	addCcDeltaFallbackCentre = true;
    }

    public void addCcLoadsControlCentre() {
	addCcLoadsControlCentre = true;
    }

    public void addToEaVmDevCentre() {
	addToEaVmDevCentre = true;
    }

    public Map<String, String> getToAddressEmailIds() {
	return toAddressEmailIds;
    }

    public Map<String, String> getcCAddressEmailIds() {
	return cCAddressEmailIds;
    }

    public void addToAddressUserId(String toAddressUserId, MailSenderRole pRole) {
	if (this.toAddressUserIds.get(toAddressUserId) == null) {
	    this.toAddressUserIds.put(toAddressUserId, new ArrayList<>());
	}
	this.toAddressUserIds.get(toAddressUserId).add(pRole);
    }

    public void addcCAddressUserId(String ccAddressUserId, MailSenderRole pRole) {
	if (this.cCAddressUserIds.get(ccAddressUserId) == null) {
	    this.cCAddressUserIds.put(ccAddressUserId, new ArrayList<>());
	}
	this.cCAddressUserIds.get(ccAddressUserId).add(pRole);
    }

    public void addToAddressEmailId(String toAddressEmailId, String pName) {
	this.toAddressEmailIds.put(toAddressEmailId, pName);
    }

    public void addcCAddressEmailId(String ccAddressEmailId, String pName) {
	this.cCAddressEmailIds.put(ccAddressEmailId, pName);
    }

    public String getSubject() {
	return subject;
    }

    public void setSubject(String subject) {
	this.subject = subject;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public Collection<String> getAttachments() {
	return attachmentsFiles;
    }

    public void addAttachment(String fileName) {
	this.attachmentsFiles.add(fileName);
    }

    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    public int getDurationMins() {
	return durationMins;
    }

    public void setDurationMins(int durationMins) {
	this.durationMins = durationMins;
    }

    public String getCalendarFileName() {
	return calendarFileName;
    }

    public void setCalendarFileName(String calendarFileName) {
	this.calendarFileName = calendarFileName;
    }

    public void addToDlCoreChangeTeamMail() {
	addToDlCoreChangeTeam = true;
    }

    public void addToProdLoadCentre(boolean addToProdLoadCentre) {
	this.addToProdLoadCentre = addToProdLoadCentre;
    }

    public abstract void processMessage();

    @Transactional
    public void send() throws Exception {
	try {
	    Set<Address> toAddressList = new HashSet<>();
	    Set<Address> ccAddressList = new HashSet<>();
	    List<Attachment> attachments = new ArrayList<>();
	    /**
	     * Getting Mail Address of the Users
	     */
	    for (String userId : toAddressUserIds.keySet()) {
		User lUser = authenticator.getUserDetails(userId);
		toAddressList.add(new Address(lUser.getMailId(), lUser.getDisplayName()));
	    }
	    for (String userId : cCAddressUserIds.keySet()) {
		User lUser = authenticator.getUserDetails(userId);
		ccAddressList.add(new Address(lUser.getMailId(), lUser.getDisplayName()));
	    }
	    /**
	     * Add any Plain Email Id's (Not a User of Application)
	     */
	    for (Map.Entry<String, String> lToEmail : toAddressEmailIds.entrySet()) {
		toAddressList.add(new Address(lToEmail.getKey(), lToEmail.getValue()));
	    }
	    for (Map.Entry<String, String> lCcEmail : cCAddressEmailIds.entrySet()) {
		ccAddressList.add(new Address(lCcEmail.getKey(), lCcEmail.getValue()));
	    }
	    /**
	     * Add any configuration email
	     */
	    addGenrousMailIds(toAddressList, ccAddressList);
	    /**
	     * Attach any Attachments
	     */
	    for (String fileName : attachmentsFiles) {
		Attachment attachment = new Attachment();
		attachment.setFileName(FilenameUtils.getName(fileName));
		File file = new File(fileName);
		if (file.exists()) {
		    attachment.setContentType(new MimetypesFileTypeMap().getContentType(file));
		    attachment.setContent(Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath())));
		    attachments.add(attachment);
		} else {
		    LOG.warn(fileName + " is doesn't exist to Attach");
		}
	    }
	    /**
	     * Attach Calendar Invite
	     */
	    if (calendarFileName != null) {
		String lContent = azureMailUtil.getCalendarContent(toAddressList, ccAddressList, subject, message, startDate, durationMins, calendarFileName);
		LOG.info(lContent);
		Attachment attachment = new Attachment();
		attachment.setFileName(FilenameUtils.getName(calendarFileName));
		File file = new File(calendarFileName);
		if (file.exists()) {
		    // attachment.setContentType(new MimetypesFileTypeMap().getContentType(file));
		    attachment.setContentType("text/calendar; method=REQUEST");
		    attachment.setContent(Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath())));
		    attachments.add(attachment);
		} else {
		    LOG.warn(calendarFileName + " is doesn't exist to Attach");
		}
	    }

	    /**
	     * Adding Delegations
	     */
	    addDelegationMailIDs(toAddressUserIds, ccAddressList);
	    addDelegationMailIDs(cCAddressUserIds, ccAddressList);
	    /**
	     * Send Email
	     */
	    // if (sendAsCalendar) {
	    // sendAsCalendar(toAddressList, ccAddressList, attachments);
	    // } else {
	    sendAsEmailWithAttachment(toAddressList, ccAddressList, attachments);
	    // }
	} catch (Exception ex) {
	    throw ex;
	}
    }

    private void addGenrousMailIds(Set<Address> toAddressList, Set<Address> ccAddressList) {
	if (addToDevCentre) {
	    toAddressList.add(new Address(wFConfig.getDevCentreMailID(), "DEV Center"));
	}
	if (addCcDevCentre) {
	    ccAddressList.add(new Address(wFConfig.getDevCentreMailID(), "DEV Center"));
	}
	if (addCcDeltaFallbackCentre) {
	    ccAddressList.add(new Address(wFConfig.getDeltaFallbackOnloneMailId(), "Delta Fallback Center"));
	}
	if (addCcLoadsControlCentre) {
	    ccAddressList.add(new Address(wFConfig.getLoadsControlCentreMailId(), "LoadControl  Center"));
	}
	if (addToEaVmDevCentre) {
	    toAddressList.add(new Address(wFConfig.getEaVmDevCentreMailId(), "EA VM Center"));
	}
	if (addToDevOpsCentre) {
	    toAddressList.add(new Address(wFConfig.getDevOpsCentreMailId(), "DEVOPS Center"));
	}
	if (addCcDevOpsCentre) {
	    ccAddressList.add(new Address(wFConfig.getDevOpsCentreMailId(), "DEVOPS Center"));
	}
	/**
	 * ZTPFM-1795 Mail Id
	 */
	if (addToProdLoadCentre) {
	    toAddressList.add(new Address(wFConfig.getProdLoadsCentreMailId(), "PROD-LOAD Center"));
	}
	if (addToDlCoreChangeTeam) {
	    toAddressList.add(new Address(wFConfig.getDlCoreTeamMail(), "Dl Core Change Team"));
	}
    }

    private void sendAsEmailWithAttachment(Set<Address> toAddressList, Set<Address> ccAddressList, List<Attachment> attachments) throws Exception {
	azureMailUtil.sendNotification(toAddressList, ccAddressList, getSubject(), getMessage(), attachments);
    }

    // private void sendAsCalendar(Set<Address> toAddressList, Set<Address>
    // ccAddressList, List<Attachment> attachments) throws Exception {
    // azureMailUtil.sendCalendar(toAddressList, ccAddressList, getSubject(),
    // getMessage(), attachments, getStartDate(), getDurationMins(),
    // getCalendarFileName());
    // }

    private void addDelegationMailIDs(Map<String, List<MailSenderRole>> pToAddressUserIds, Set<Address> pCcAddressList) {
	if (!pToAddressUserIds.isEmpty()) {
	    for (Map.Entry<String, List<MailSenderRole>> entry : pToAddressUserIds.entrySet()) {
		String toAddressUserId = entry.getKey();
		List<MailSenderRole> value = entry.getValue();
		if (value.contains(MailSenderRole.DEV_MANAGER)) {
		    SortedSet<String> lDelegateIds = lDelegateHelper.getParentsCache(toAddressUserId);
		    for (String lDelegateId : lDelegateIds) {
			if (!this.toAddressUserIds.keySet().contains(lDelegateId) && !this.cCAddressUserIds.keySet().contains(lDelegateId)) {
			    User lUser = authenticator.getUserDetails(lDelegateId);
			    if (lUser != null && lUser.getMailId() != null) {
				pCcAddressList.add(new Address(lUser.getMailId(), lUser.getDisplayName()));
			    }
			}
		    }
		}
	    }
	}
    }

}
