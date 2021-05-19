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
import java.util.concurrent.LinkedBlockingQueue;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.apache.log4j.Logger;

/**
 *
 * @author USER
 */
public class TOSIncomingIPMessageListener implements MessageListener {

    private static final Logger LOG = Logger.getLogger(TOSIncomingIPMessageListener.class.getName());
    TOSClientImpl lClientImpl;

    public TOSIncomingIPMessageListener(TOSClientImpl pClientImpl) {
	lClientImpl = pClientImpl;
    }

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
			lClient = lClientImpl.connectTOSClient(lRequest, true);
		    }

		    TOSResult lReqResult = lRequest.getResult(true);

		    LOG.error("Adding request  for " + lRequest.getSystem() + " " + lRequest.getCpuName() + ", " + lReqResult.getCommand() + "," + lReqResult.getLoadset());
		    if (TOSClientImpl.lIPMessages.get(lReqResult.hashSystemCPUCommand()) == null) {
			TOSClientImpl.lIPMessages.put(lReqResult.hashSystemCPUCommand(), new LinkedBlockingQueue<>());
		    }
		    TOSClientImpl.lIPMessages.get(lReqResult.hashSystemCPUCommand()).add(lReqResult);

		    int ret = lClient.sendMessage(lRequest.getCommand());
		    boolean validateCommands = lClient.validateCommands(ret, lReqResult);

		    if (!validateCommands) {
			lClientImpl.sendResult(lRequest.getSystem(), lReqResult);
		    }

		} else {
		    TOSResult lReqResult = lRequest.getResult(true, 8, "TOS Server Stopped");
		    lClientImpl.sendResult(lRequest.getSystem(), lReqResult);
		}
	    } catch (JMSException ex) {
		LOG.error("Error in parsing Message", ex);
	    }
	}
    }
}
