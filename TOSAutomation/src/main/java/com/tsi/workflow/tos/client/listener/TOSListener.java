/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos.client.listener;

import com.ibm.tpf.etos.api.ETOSClient;
import com.ibm.tpf.etos.api.MessageBlock;
import com.tsi.workflow.tos.TOSConfig;
import com.tsi.workflow.tos.client.TOSClient;
import com.tsi.workflow.tos.client.TOSClientImpl;
import com.tsi.workflow.tos.model.TOSResult;
import com.tsi.workflow.tos.util.MailClient;
import java.util.regex.Matcher;
import org.apache.log4j.Logger;

/**
 *
 * @author USER
 */
public class TOSListener extends Thread {

    TOSClient tosClient;
    private static final Logger LOG = Logger.getLogger(TOSListener.class.getName());
    boolean die = false, reconnect = false;
    String system;
    String cpuName;

    public TOSListener(TOSClient client, String system, String cpuName) {
	this.tosClient = client;
	this.system = system;
	this.cpuName = cpuName;
	setName(system + ":" + cpuName + "-" + TOSConfig.getInstance().getNextThreadName());
	LOG.info("Creating Listener for " + getName());

    }

    public void run() {
	int ret = 0;
	LOG.info("Starting Listener for " + getName());
	while (!die) {
	    try {
		MessageBlock newMsg = new MessageBlock();
		ret = tosClient.receiveMessage(newMsg);
		if (ret == ETOSClient.ETOS_NO_OPEN && tosClient.getOverflow()) {
		    LOG.info("Shutdown after message buffer overflow.");
		    die = true;
		} else {
		    switch (ret) {
		    case ETOSClient.ETOS_SUCCESS:
			LOG.info(newMsg.getTA() + ":0x" + Integer.toHexString(newMsg.getFSC()).toUpperCase() + ">>" + newMsg.getMsg()); {
			// AAES0009I\s.*\sAAER0700I\s.*\s(\w+)-MSG\s([\w-]+)\sRC=(\d+)\s(.*)
			Matcher ceMatcher = com.tsi.workflow.utils.Constants.OUT_MESSAGE_PATTERN_CE.matcher(newMsg.getMsg());
			if (ceMatcher.matches() && !ceMatcher.group(4).contains("SECOND")) {
			    LOG.info("Got Out Message Match" + ceMatcher.group(4));
			    if (ceMatcher.group(1).equals("LOADFIP")) {
				TOSResult lListenerResult = new TOSResult(ceMatcher.group(1), Integer.parseInt(ceMatcher.group(3)), ceMatcher.group(4), system, cpuName, false);
				LOG.info("Adding to Sending Queue for " + system + ":" + cpuName + " SECONDARY ");
				TOSClientImpl.lResults.add(lListenerResult);
			    } else if (!ceMatcher.group(1).equals("LOADFIP")) {
				TOSResult lListenerResult = new TOSResult(ceMatcher.group(1), ceMatcher.group(2), Integer.parseInt(ceMatcher.group(3)), ceMatcher.group(4), system, cpuName, false);
				LOG.info("Adding to Sending Queue for " + system + ":" + cpuName + " SECONDARY " + ceMatcher.group(2));
				TOSClientImpl.lResults.add(lListenerResult);
			    }
			    break;
			}
		    } {
			// AAES0009I\s.*\s(\w+)-MSG\s([\w-]+)\sRC=(\d+)\s(.*)
			Matcher nonceMatcher = com.tsi.workflow.utils.Constants.OUT_MESSAGE_PATTERN_NON_CE.matcher(newMsg.getMsg());
			if (nonceMatcher.matches() && !nonceMatcher.group(4).contains("SECOND")) {
			    LOG.info("Got Out Message Match" + nonceMatcher.group(4));
			    if (nonceMatcher.group(1).equals("LOADFIP")) {
				TOSResult lListenerResult = new TOSResult(nonceMatcher.group(1), Integer.parseInt(nonceMatcher.group(3)), nonceMatcher.group(4), system, cpuName, true);
				LOG.info("Adding to Sending Queue for " + system + ":" + cpuName + " PRIMARY ");
				TOSClientImpl.lResults.add(lListenerResult);
			    } else if (!nonceMatcher.group(1).equals("LOADFIP")) {
				TOSResult lListenerResult = new TOSResult(nonceMatcher.group(1), nonceMatcher.group(2), Integer.parseInt(nonceMatcher.group(3)), nonceMatcher.group(4), system, cpuName, true);
				LOG.info("Adding to Sending Queue for " + system + ":" + cpuName + " PRIMARY " + nonceMatcher.group(1));
				TOSClientImpl.lResults.add(lListenerResult);
			    }
			    break;
			}
		    } {
			// AAES0008I\s(\d*)\s.*\s(\w+)-MSG\s(\w+)\sRC=(\d+)\s(.*)
			Matcher retryMatcher = com.tsi.workflow.utils.Constants.OUT_MESSAGE_PATTERN_RETRY.matcher(newMsg.getMsg());
			if (retryMatcher.matches() && retryMatcher.group(5).contains("SECOND")) {
			    if (retryMatcher.group(2).equals("LOADFIP")) {
				TOSResult lListenerResult = new TOSResult(retryMatcher.group(2), Integer.parseInt(retryMatcher.group(4)), retryMatcher.group(5), system, cpuName, retryMatcher.group(1).equals("09"));
				LOG.info("Adding to Sending Queue for " + system + ":" + cpuName + " " + (retryMatcher.group(2).equals("09") ? "PRIMARY" : "SECONDARY"));
				TOSClientImpl.lResults.add(lListenerResult);
			    } else if (!retryMatcher.group(2).equals("LOADFIP")) {
				TOSResult lListenerResult = new TOSResult(retryMatcher.group(2), retryMatcher.group(3), Integer.parseInt(retryMatcher.group(4)), retryMatcher.group(5), system, cpuName, retryMatcher.group(1).equals("09"));
				LOG.info("Adding to Sending Queue for " + system + ":" + cpuName + " " + retryMatcher.group(1) + " " + (retryMatcher.group(2).equals("09") ? "PRIMARY" : "SECONDARY"));
				TOSClientImpl.lResults.add(lListenerResult);
			    }
			    break;
			}
		    }
			break;
		    case ETOSClient.ETOS_NONE:
			if (reconnect) {
			    reconnect = false;
			    LOG.info("After reconnect, assigned TA for PRC: " + tosClient.getAssignedTA(ETOSClient.PRCCONN));
			    LOG.info("After reconnect, assigned TA for FBK: " + tosClient.getAssignedTA(ETOSClient.FBKCONN));
			}
			try {
			    Thread.sleep(100);
			} catch (InterruptedException e) {
			    LOG.info("Sleep failed.");
			    die = true;
			}
			break;
		    case ETOSClient.ETOS_RECONN_IN_PROGRESS:
			if (!reconnect) {
			    LOG.info("Client reconnect in progress.");
			    reconnect = true;
			}
			try {
			    Thread.sleep(100);
			} catch (InterruptedException e) {
			    LOG.info("Sleep failed.");
			    die = true;
			}
			break;
		    case ETOSClient.ETOS_MSG_OVERFLOW:
			tosClient.setOverflow(true);
			LOG.info("Message buffer overflow!");
			break;
		    case ETOSClient.ETOS_NO_OPEN:
			LOG.info("Ending client receive thread. Session not open: " + ret);
			die = true;
			break;
		    default:
			die = true;
			LOG.info("Ending client receive thread. Bad return code: " + ret);
			break;
		    }
		}

	    } catch (Exception e) {
		LOG.error("Error in Receiving Message", e);
	    }
	}
	tosClient.close();
	MailClient.sendDisconnectionMail(system, cpuName, getName());
	LOG.info("Exiting Listener for " + getName());
    }

    public void close() {
	die = true;
    }
}
