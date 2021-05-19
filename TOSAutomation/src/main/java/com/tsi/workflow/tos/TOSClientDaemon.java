/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos;

import com.tsi.workflow.tos.client.TOSClient;
import com.tsi.workflow.tos.client.TOSClientImpl;
import com.tsi.workflow.tos.model.TOSResult;
import com.tsi.workflow.tos.util.MailClient;
import com.tsi.workflow.tos.util.MailUtil;
import com.tsi.workflow.utils.DateHelper;
import java.io.InputStream;
import java.util.Map;
import javax.jms.JMSException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author USER
 */
class TOSClientDaemon {

    private static final Logger LOG = Logger.getLogger(TOSClientDaemon.class.getName());
    public static boolean wait = true;

    static void start(String[] args) throws Exception {
	InputStream lLogStream = null;
	try {
	    lLogStream = TOSClientImpl.class.getResourceAsStream("/log4j-tos.properties");
	    PropertyConfigurator.configure(lLogStream);
	    Runtime r = Runtime.getRuntime();
	    r.addShutdownHook(new Thread() {
		public void run() {
		    if (!TOSClientImpl.lMessages.isEmpty()) {
			LOG.info("Waiting for One Minute to Receive Responses from TOS");
			try {
			    Thread.sleep(60000L);
			} catch (InterruptedException ex) {
			    LOG.error("Error in Exit Waiting", ex);
			}
		    }
		    if (!TOSClientImpl.lMessageClients.isEmpty()) {
			for (Map.Entry<String, TOSClient> entry : TOSClientImpl.lMessageClients.entrySet()) {
			    String key = entry.getKey();
			    TOSClient tosClient = entry.getValue();
			    tosClient.close();
			}
		    }
		    TOSClientImpl.stopSend = true;
		    if (!TOSClientImpl.lMessages.isEmpty()) {
			StringBuilder lBuilder = new StringBuilder("Please find the list of loadsets are having pending responses from TOS <BR><BR>");
			boolean sendMail = false;
			for (Map.Entry<String, TOSResult> en : TOSClientImpl.lMessages.entrySet()) {
			    String key = en.getKey();
			    TOSResult lReqResult = en.getValue();
			    LOG.info("Pending Messages : " + key + " " + lReqResult.getLoadset() + " " + lReqResult.getId() + " | " + lReqResult.getFullCommand());
			    lBuilder.append("System : ").append(lReqResult.getSystem()).append("Loadset : ").append(lReqResult.getLoadset()).append("CPU : ").append(lReqResult.getCpuName()).append("Command : ").append(lReqResult.getFullCommand()).append("User : ").append(lReqResult.getUser().getDisplayName()).append("Issued At : ").append(DateHelper.convertGMTtoEST(lReqResult.getCreatedTime())).append("<BR>");
			    sendMail = true;
			}
			lBuilder.append("<BR><BR> TOS Application is about to STOP");
			if (sendMail) {
			    MailClient.sendPendingMessagesMail(lBuilder.toString());
			}
		    }
		    try {
			MailUtil.getInstance().sendServerStopped();
		    } catch (Exception e) {
			LOG.error("Error in sending mail", e);
		    }
		    LOG.info("Exiting Application");
		}
	    });
	    TOSClientImpl clientImpl = new TOSClientImpl();
	    clientImpl.configure();
	} catch (JMSException ex) {
	    LOG.error("Error in Application", ex);
	} finally {
	    IOUtils.closeQuietly(lLogStream);
	}
    }
}
