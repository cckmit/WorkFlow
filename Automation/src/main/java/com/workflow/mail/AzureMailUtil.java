/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workflow.mail;

import com.tsi.workflow.interfaces.IMailConfig;
import com.workflow.mail.core.Address;
import com.workflow.mail.core.Attachment;
import com.workflow.mail.core.ICalendar;
import com.workflow.mail.core.MailClient;
import com.workflow.mail.core.MailItem;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import retrofit2.Response;

/**
 *
 * @author USER
 */
public class AzureMailUtil {

    private static final Logger LOG = Logger.getLogger(AzureMailUtil.class);
    private final IMailConfig lConfig;

    String MessageTemplate;
    MailClient lClient;

    public AzureMailUtil(final IMailConfig pConfig) {
	this.lConfig = pConfig;
	int year = Calendar.getInstance().get(Calendar.YEAR);
	MessageTemplate = "<html> <body> <br/><br/>{1} <br/> <h6> <hr/> © " + year + " Travelport. All rights reserved. <br/> Travelport, the Travelport logo, Apollo, Galileo and Worldspan are trademarks of Travelport.<br/> <br/> This communication and the information in it are confidential and for internal use only, and may not be distributed externally. <br/> </h6> </body> </html>";
	try {
	    lClient = new MailClient.Builder().connect(lConfig.getAzureURL(), null).build();
	} catch (IOException ex) {
	    LOG.error("Error in Creating Mail Client", ex);
	}
    }

    public void sendNotification(Collection<Address> toAddressList, Collection<Address> cCAddressList, String subject, String message, List<Attachment> attachments) throws Exception {
	MailItem lMailItem = new MailItem(lConfig.getAzureId());
	lMailItem.setFrom(new Address(lConfig.getFromMailId(), lConfig.getSendingApplication()));
	lMailItem.setToContacts(toAddressList);
	lMailItem.setCcContacts(cCAddressList);
	lMailItem.setSubject(subject);
	if (!attachments.isEmpty()) {
	    lMailItem.setAttachments(attachments);
	}

	lMailItem.setBody(formatMessage(subject, message));
	lMailItem.setBodyType("text/html");

	Response<Void> execute = lClient.getMailAPI().sendMail(lMailItem, lConfig.getAzureKey()).execute();
	if (execute.isSuccessful()) {
	    LOG.info("Mail (" + subject + ") to " + String.join(",", toAddressList) + " sent successfully");
	} else {
	    LOG.info("Mail (" + subject + ") to " + String.join(",", toAddressList) + " failed");
	}
    }

    // public void sendCalendar(Collection<Address> toAddressList,
    // Collection<Address> cCAddressList, String subject, String message,
    // List<Attachment> attachments, Date pStartDate, int durationMins, String
    // pFileName) throws Exception {
    // MailItem lMailItem = new MailItem(lConfig.getAzureId());
    // lMailItem.setFrom(new Address(lConfig.getFromMailId(),
    // lMailItem.getSendingApplication()));
    // lMailItem.setToContacts(toAddressList);
    // lMailItem.setCcContacts(cCAddressList);
    // lMailItem.setSubject(subject);
    // if (!attachments.isEmpty()) {
    // lMailItem.setAttachments(attachments);
    // }
    // String lInviteContent = getCalendarContent(toAddressList, cCAddressList,
    // subject, message, pStartDate, durationMins, pFileName);
    // lMailItem.setBody(lInviteContent);
    // lMailItem.setBodyType("text/calendar");
    //
    // Response<Void> execute = lClient.getMailAPI().sendMail(lMailItem,
    // lConfig.getAzureKey()).execute();
    // if (execute.isSuccessful()) {
    // LOG.info("Mail (" + subject + ") to " + String.join(",", toAddressList) + "
    // sent successfully");
    // } else {
    // LOG.info("Mail (" + subject + ") to " + String.join(",", toAddressList) + "
    // failed");
    // }
    // }

    public String getCalendarContent(Collection<Address> toAddressList, Collection<Address> cCAddressList, String subject, String message, Date pStartDate, int durationMins, String pFileName) throws Exception {
	List<Address> attendees = new ArrayList<>();
	attendees.addAll(toAddressList);
	attendees.addAll(cCAddressList);

	ICalendar iCalendar = new ICalendar();
	if (pStartDate != null) {
	    iCalendar.setAttendees(attendees);
	    iCalendar.setOrganiser(lConfig.getFromMailId());
	    if (durationMins > 0) {
		return iCalendar.getCalendar(pFileName, pStartDate, durationMins, formatMessage(subject, message), subject);
	    } else {
		return iCalendar.getCalendar(pFileName, pStartDate, formatMessage(subject, message), subject);
	    }
	} else {
	    return iCalendar.cancelCalendar(pFileName);
	}
    }

    private String formatMessage(String subject, String message) {
	return MessageFormat.format(MessageTemplate, subject, message);
    }
}
