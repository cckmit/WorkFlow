/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos.jms.listener;

import com.google.gson.Gson;
import com.tsi.workflow.tos.client.TOSClient;
import com.tsi.workflow.tos.client.TOSClientImpl;
import com.tsi.workflow.tos.model.TOSRequest;
import com.tsi.workflow.tos.model.TOSResult;
import com.tsi.workflow.tos.util.MailClient;
import com.tsi.workflow.utils.Constants;
import java.util.regex.Matcher;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.apache.log4j.Logger;

/**
 *
 * @author USER
 */
public class TOSIncomingMessageListener implements MessageListener {

    private static final Logger LOG = Logger.getLogger(TOSIncomingMessageListener.class.getName());
    TOSClientImpl lClientImpl;

    public TOSIncomingMessageListener(TOSClientImpl pClientImpl) {
	lClientImpl = pClientImpl;
    }

    String subject = "";
    String mailMessage = "";

    @Override
    public void onMessage(Message message) {
	if (message instanceof TextMessage) {
	    TextMessage lMessage = (TextMessage) message;
	    try {
		TOSRequest lRequest = new Gson().fromJson(lMessage.getText(), TOSRequest.class);
		if (!TOSClientImpl.exited) {
		    TOSClient lClient = null;
		    lClientImpl.checkIPaddress(lRequest);

		    lClient = TOSClientImpl.lMessageClients.get(lRequest.hashSystemCPU());
		    if (lClient == null || lClient.isClosed()) {
			lClient = lClientImpl.connectTOSClient(lRequest, false);
		    }

		    Matcher matcher = Constants.IN_MESSAGE_PATTERN.matcher(lRequest.getCommand());
		    TOSResult lReqResult = null;

		    if (matcher.matches()) {
			/*
			 * 2152 Check for Seconday message CPU Check - ? if true, send the message with
			 * CE hashmap wiht pri/sec
			 */
			lReqResult = lRequest.getResult(false);
			LOG.error("Adding Primary request for " + lRequest.getSystem() + ", " + lRequest.getCpuName() + ", " + lReqResult.getCommand() + ", " + lReqResult.getLoadset());
			lReqResult.setIsPrimary(true);
			TOSClientImpl.lMessages.put(lReqResult.hashSystemCommandLoadsetPrimary(), lReqResult);
			int ret = -1;

			if (lRequest.isCoexist()) {
			    lReqResult = lRequest.getResult(false);
			    LOG.error("Adding Secondary request for " + lRequest.getSystem() + ", " + lRequest.getCpuName() + ", " + lReqResult.getCommand() + ", " + lReqResult.getLoadset());
			    lReqResult.setIsPrimary(false);
			    TOSClientImpl.lMessages.put(lReqResult.hashSystemCommandLoadsetPrimary(), lReqResult);
			    ret = lClient.sendMessage(lRequest.getCommand() + Constants.COEXIST_NAME);
			} else {
			    ret = lClient.sendMessage(lRequest.getCommand());
			}

			boolean validateCommands = lClient.validateCommands(ret, lReqResult);

			if (!validateCommands) {
			    lClientImpl.sendResult(lRequest.getSystem(), lReqResult);
			    MailClient.sendFailMail(lReqResult, true, false);
			} else {
			    MailClient.sendSuccessMail(lReqResult, true, false);
			}
			try {
			    Thread.sleep(1000L);
			} catch (InterruptedException ex) {
			    LOG.error("Error in wait thread", ex);
			}
		    }
		} else {
		    Matcher matcher = Constants.IN_MESSAGE_PATTERN.matcher(lRequest.getCommand());
		    if (matcher.matches()) {
			{
			    TOSResult lReqResult = lRequest.getResult(false, 8, "TOS Server Stopped");
			    lReqResult.setIsPrimary(true);
			    lClientImpl.sendResult(lRequest.getSystem(), lReqResult);
			    MailClient.sendFailMail(lReqResult, false, false);
			}
			if (lRequest.isCoexist()) {
			    TOSResult lReqResult = lRequest.getResult(false, 8, "TOS Server Stopped");
			    lReqResult.setIsPrimary(false);
			    lClientImpl.sendResult(lRequest.getSystem(), lReqResult);
			    MailClient.sendFailMail(lReqResult, false, false);
			}
		    }
		}
	    } catch (JMSException ex) {
		LOG.error("Error in parsing Message", ex);
	    }
	}
    }
}
