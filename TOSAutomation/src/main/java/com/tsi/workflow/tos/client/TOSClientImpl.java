/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos.client;

import com.google.gson.Gson;
import com.tsi.workflow.tos.TOSConfig;
import com.tsi.workflow.tos.jms.listener.StopListener;
import com.tsi.workflow.tos.jms.listener.TOSIncomingIPMessageListener;
import com.tsi.workflow.tos.jms.listener.TOSIncomingMessageListener;
import com.tsi.workflow.tos.model.TOSRequest;
import com.tsi.workflow.tos.model.TOSResult;
import com.tsi.workflow.tos.util.MailClient;
import com.tsi.workflow.tos.util.MailUtil;
import com.tsi.workflow.utils.Constants;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author USER
 */
public class TOSClientImpl extends Thread {

    public static final ConcurrentHashMap<String, TOSClient> lMessageClients = new ConcurrentHashMap<String, TOSClient>();
    public static final ConcurrentHashMap<String, String> lMessageIPs = new ConcurrentHashMap<String, String>();
    public static final ConcurrentHashMap<String, LinkedBlockingQueue<TOSResult>> lIPMessages = new ConcurrentHashMap<String, LinkedBlockingQueue<TOSResult>>();
    public static final ConcurrentHashMap<String, TOSResult> lMessages = new ConcurrentHashMap<String, TOSResult>();
    public static final LinkedBlockingQueue<TOSResult> lResults = new LinkedBlockingQueue<TOSResult>();
    public static boolean exited = false;
    public static boolean stopSend = false;
    private static final Logger LOG = Logger.getLogger(TOSClientImpl.class.getName());

    Connection connection;
    Session session;
    MessageProducer lOutgoingQueueProducer;
    MessageProducer lOutgoingIPQueueProducer;
    String subject = "";
    String mailMessage = "";
    Date lastRan = new Date();

    public TOSClientImpl() {
    }

    public void configure() throws JMSException, Exception {
	MailUtil.getInstance();
	ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
	connection = connectionFactory.createConnection(TOSConfig.getInstance().getServiceUserID(), TOSConfig.getInstance().getServiceSecret());
	connection.start();
	session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	Queue lExitQueue = session.createQueue(Constants.TOS_QUEUE_EXIT);
	MessageConsumer lExitQueueConsumer = session.createConsumer(lExitQueue);
	lExitQueueConsumer.setMessageListener(new StopListener());

	Queue lIncomingQueue = session.createQueue(Constants.TOS_QUEUE_APP_TOS);
	MessageConsumer lIncomingQueueConsumer = session.createConsumer(lIncomingQueue);
	lIncomingQueueConsumer.setMessageListener(new TOSIncomingMessageListener(this));

	Queue lIncomingIPQueue = session.createQueue(Constants.TOS_QUEUE_IP_APP_TOS);
	MessageConsumer lIncomingIPQueueConsumer = session.createConsumer(lIncomingIPQueue);
	lIncomingIPQueueConsumer.setMessageListener(new TOSIncomingIPMessageListener(this));

	Topic lOutgoingQueue = session.createTopic(Constants.TOS_TOPIC_TOS_APP);
	lOutgoingQueueProducer = session.createProducer(lOutgoingQueue);

	Topic lOutgoingIPQueue = session.createTopic(Constants.TOS_TOPIC_IP_TOS_APP);
	lOutgoingIPQueueProducer = session.createProducer(lOutgoingIPQueue);
	this.start();
	MailUtil.getInstance().sendServerStarted();
    }

    /**
     * Only Immediate response will be sent direct
     */
    public void sendResult(String system, TOSResult pResult) {
	try {
	    LOG.error("Trying to send result for system " + system + "," + pResult.getCommand() + "," + pResult.getLoadset());
	    String lSearch = "";
	    String lSecSearch = "";
	    boolean isLoadfIP;
	    if (pResult.getCommand().equals("LOADFIP")) {
		lSearch = system + " | " + pResult.getCpuName() + " | " + pResult.getCommand();
		isLoadfIP = true;
	    } else if (pResult.getCommand().equals("LOADACT")) {
		lSearch = system + " | " + pResult.getCommand() + "0 | " + pResult.getLoadset() + " | " + pResult.isIsPrimary();
		lSecSearch = system + " | " + pResult.getCommand() + "0 | " + pResult.getLoadset() + " | " + (!pResult.isIsPrimary());
		isLoadfIP = false;
	    } else {
		lSearch = system + " | " + pResult.getCommand() + " | " + pResult.getLoadset() + " | " + pResult.isIsPrimary();
		lSecSearch = system + " | " + pResult.getCommand() + "0 | " + pResult.getLoadset() + " | " + (!pResult.isIsPrimary());
		isLoadfIP = false;
	    }
	    boolean containsKey = containsMessage(isLoadfIP, lSearch);
	    if (containsKey) {
		TOSResult lReqResult = null;
		/*
		 * 2152 get appropriate pri/sec result from the removeMessage getMessage
		 * containsMessage
		 */
		if ((pResult.getMessage().contains("LAST_STATE") || pResult.getReturnValue() == 0 || pResult.getReturnValue() > 4) && !pResult.getMessage().contains("SECOND INSTANCE FAILED")) {
		    // SUCCESS or FAIL Messages
		    lReqResult = removeMessage(isLoadfIP, lSearch);
		    lReqResult.setReturnValue(pResult.getReturnValue());
		    lReqResult.setLast(true);
		    lReqResult.setMessage(pResult.getMessage());
		    sendToClient(system, lReqResult);
		    if (!isLoadfIP) {
			MailClient.sendSuccessMail(lReqResult, false, false);
		    }

		    if ((!isLoadfIP) && pResult.isCoexist() && pResult.isIsPrimary() && pResult.getReturnValue() > 4 && containsMessage(isLoadfIP, lSecSearch)) {
			TOSResult lReqSecResult = removeMessage(isLoadfIP, lSecSearch);
			lReqSecResult.setReturnValue(pResult.getReturnValue());
			lReqSecResult.setLast(true);
			lReqSecResult.setMessage("Primary Failed : " + pResult.getMessage());
			sendToClient(system, lReqSecResult);
			MailClient.sendSuccessMail(lReqSecResult, false, false);
		    }
		} else if (pResult.getReturnValue() > 4 && pResult.getMessage().contains("SECOND INSTANCE FAILED")) {
		    // Retry Messages
		    lReqResult = getMessage(isLoadfIP, lSearch);
		    if (lReqResult != null) {
			try {
			    LOG.info("Retrying the command for system " + system + "," + pResult.getCpuName() + "," + pResult.getCommand() + "," + pResult.getLoadset());
			    Thread.sleep(1000L);
			} catch (InterruptedException ex) {
			    LOG.error("Error in Waiting Thread", ex);
			}
			TOSClient lClient = TOSClientImpl.lMessageClients.get(lReqResult.hashSystemCPU());
			int ret = -1;
			if (pResult.isCoexist()) {
			    ret = lClient.sendMessage(lReqResult.getFullCommand() + Constants.COEXIST_NAME);
			} else {
			    ret = lClient.sendMessage(lReqResult.getFullCommand());
			}
			boolean validateCommands = lClient.validateCommands(ret, lReqResult);
			if (!validateCommands) {
			    sendResult(system, lReqResult);
			    if (!isLoadfIP) {
				MailClient.sendFailMail(lReqResult, true, true);
			    }
			} else {
			    if (!isLoadfIP) {
				MailClient.sendSuccessMail(lReqResult, true, true);
			    }
			}
		    }
		} else {
		    // In Progress Messages
		    lReqResult = getMessage(isLoadfIP, lSearch);
		    lReqResult.setReturnValue(pResult.getReturnValue());
		    lReqResult.setLast(false);
		    lReqResult.setMessage(pResult.getMessage());
		    sendToClient(system, lReqResult);
		}
	    } else {
		LOG.error("Message Not Found for " + pResult.getSystem() + "," + pResult.getCpuName() + "," + pResult.getCommand() + ", " + pResult.getLoadset());
	    }
	} catch (JMSException ex) {
	    LOG.error("Error in sending in Message", ex);
	}
    }

    public TOSResult getMessage(boolean isLoadfIP, String pSearchKey) {
	if (isLoadfIP) {
	    LinkedBlockingQueue<TOSResult> lQueue = lIPMessages.get(pSearchKey);
	    if (lQueue != null && !lQueue.isEmpty()) {
		return lQueue.peek();
	    } else {
		return null;
	    }
	} else {
	    return lMessages.get(pSearchKey);
	}
    }

    public TOSResult removeMessage(boolean isLoadfIP, String pSearchKey) {
	if (isLoadfIP) {
	    LinkedBlockingQueue<TOSResult> lQueue = lIPMessages.get(pSearchKey);
	    if (lQueue != null && !lQueue.isEmpty()) {
		return lQueue.poll();
	    } else {
		return null;
	    }
	} else {
	    return lMessages.remove(pSearchKey);
	}
    }

    public boolean containsMessage(boolean isLoadfIP, String pSearchKey) {
	if (isLoadfIP) {
	    LinkedBlockingQueue<TOSResult> lQueue = lIPMessages.get(pSearchKey);
	    if (lQueue != null) {
		return !lQueue.isEmpty();
	    } else {
		return false;
	    }
	} else {
	    return lMessages.containsKey(pSearchKey);
	}
    }

    public void sendToClient(String system, TOSResult lResult) throws JMSException {
	String lText = new Gson().toJson(lResult);
	TextMessage lMessage = session.createTextMessage(lText);

	if (lResult.getCommand().equals("LOADFIP")) {
	    lOutgoingIPQueueProducer.send(lMessage);
	    LOG.error("Sent TOS IP result for system " + system);
	} else {
	    lOutgoingQueueProducer.send(lMessage);
	    LOG.error("Sent TOS Message result for system " + system);
	}
    }

    @Override
    public void run() {
	while (!TOSClientImpl.stopSend) {
	    try {
		TOSResult lListenerResult;
		while ((lListenerResult = lResults.poll()) != null) {
		    sendResult(lListenerResult.getSystem(), lListenerResult);
		}
		long difference = new Date().getTime() - lastRan.getTime();
		if (difference > 60) {
		    clearTimedOutIPMessages();
		    lastRan = new Date();
		}
	    } catch (Exception e) {
		LOG.error("Error in Sending Result", e);
	    }
	}
	LOG.info("Exiting Application Sending Thread");
    }

    public synchronized void checkIPaddress(TOSRequest lRequest) {
	String lIPs = TOSClientImpl.lMessageIPs.get(lRequest.hashSystemCPU());
	if (lIPs != null && !lIPs.equals(lRequest.hashIPAddress())) {
	    LOG.error("Closing Old Connection for " + lRequest.getSystem() + ", " + lRequest.getCpuName());
	    TOSClient lClient = TOSClientImpl.lMessageClients.remove(lRequest.hashSystemCPU());
	    if (lClient != null) {
		lClient.close();
	    }
	    TOSClientImpl.lMessageIPs.remove(lRequest.hashSystemCPU());
	}
    }

    public synchronized TOSClient connectTOSClient(TOSRequest lRequest, boolean isIP) {
	TOSClient lClient = new TOSClient(this);
	LOG.error("Creating New Connection for " + lRequest.getSystem() + " CPU: " + lRequest.getCpuName() + " IP: " + lRequest.hashIPAddress());
	if (!lRequest.getPrimaryAddress().startsWith("192.168.") && lClient.connect(lRequest.getPrimaryAddress(), lRequest.getSecondaryAddress(), lRequest.getSystem())) {
	    LOG.error("Connection Successfully for Connection " + lRequest.getSystem() + ", " + lRequest.getCpuName());
	    lClient.setListener(lRequest.getSystem(), lRequest.getCpuName());
	    TOSClientImpl.lMessageIPs.put(lRequest.hashSystemCPU(), lRequest.hashIPAddress());
	    TOSClientImpl.lMessageClients.put(lRequest.hashSystemCPU(), lClient);
	    MailClient.sendConnectionSuccessMail(lRequest, lClient.getListener().getName());
	} else {
	    {
		TOSResult lReqResult = lRequest.getResult(true, 8, "Not able to communicate to TOS Server");
		lReqResult.setIsPrimary(true);
		sendResult(lRequest.getSystem(), lReqResult);
		MailClient.sendConnectionFailMail(lRequest);
	    }
	    if (!isIP && lRequest.isCoexist()) {
		TOSResult lReqResult = lRequest.getResult(true, 8, "Not able to communicate to TOS Server");
		lReqResult.setIsPrimary(false);
		sendResult(lRequest.getSystem(), lReqResult);
		MailClient.sendConnectionFailMail(lRequest);
	    }
	}
	return lClient;
    }

    private void clearTimedOutIPMessages() {
	for (Map.Entry<String, LinkedBlockingQueue<TOSResult>> entry : lIPMessages.entrySet()) {
	    LinkedBlockingQueue<TOSResult> value = entry.getValue();
	    if (!value.isEmpty()) {
		TOSResult lReqResult = value.peek();
		if (lReqResult != null) {
		    long difference = new Date().getTime() - lReqResult.getCreatedTime().getTime();
		    while (difference > 0) {
			int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(difference);
			if (seconds > 60) {
			    TOSResult lReqResult1 = value.poll();
			    LOG.info("Removed from queue " + lReqResult1.getSystem() + " | " + lReqResult1.getCommand() + " | " + lReqResult1.getHost());
			    lReqResult = value.peek();
			    if (lReqResult != null) {
				difference = new Date().getTime() - lReqResult.getCreatedTime().getTime();
			    } else {
				difference = -1;
			    }
			} else {
			    difference = -1;
			}
		    }
		}
	    }
	}
    }
}
