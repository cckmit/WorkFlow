package com.tsi.workflow.tos.client;

import com.ibm.tpf.etos.api.ETOSClient;
import com.ibm.tpf.etos.api.MessageBlock;
import com.ibm.tpf.etos.comm.CommunicationBlock;
import com.tsi.workflow.tos.client.listener.TOSListener;
import com.tsi.workflow.tos.model.TOSResult;
import org.apache.log4j.Logger;

public class TOSClient {

    private static final Logger LOG = Logger.getLogger(TOSClient.class.getName());

    private final ETOSClient etosClient;
    private boolean overflow;
    private boolean close;
    TOSClientImpl lClientImpl;
    private TOSListener listener;

    public TOSClient(TOSClientImpl pClientImpl) {
	this.lClientImpl = pClientImpl;
	this.etosClient = new ETOSClient(false);
	this.overflow = false;
	this.close = false;
	this.etosClient.addFilterConfig("filterrules.dat");
    }

    public boolean getOverflow() {
	return overflow;
    }

    public void setOverflow(boolean overflow) {
	this.overflow = overflow;
    }

    public boolean connect(String primaryAddress, String secondaryAddress, String system) {
	CommunicationBlock lPrimaryBlock = getCommunicationBlock(primaryAddress, "D0", "DF", system);
	CommunicationBlock lFallbackBlock = getCommunicationBlock(secondaryAddress, "DF", "DF", system);
	int ret = etosClient.open(lPrimaryBlock, lFallbackBlock, 5, 5, false, true, null);
	return validateLogin(ret);
    }

    public CommunicationBlock getCommunicationBlock(String ipAddress, String tA, String maxTa, String system) {
	CommunicationBlock commBlock = new CommunicationBlock();

	String[] parts = ipAddress.split(":");
	String address = parts[0];
	int port = Integer.parseInt(parts[1]);

	commBlock.setServerIP(address);
	commBlock.setHostPort((short) port);
	commBlock.setServerPort(10001);
	commBlock.setHeartBeat((short) 20);
	commBlock.setTA(tA);
	commBlock.setMaxTA(maxTa);
	commBlock.setFSC(0xC000); // 0x0002 // 0x4000 // 0xC000
	commBlock.setNum_Retry(-1);
	commBlock.setSec_Retry(5);
	commBlock.setShadow(true);
	commBlock.setShadowTA("FF");
	return commBlock;
    }

    public int getLastOpenRC() {
	return etosClient.getLastOpenRC(ETOSClient.PRCCONN);
    }

    public String getAssignedTA(boolean lParam) {
	return etosClient.getAssignedTA(lParam);
    }

    public boolean validateLogin(int ret) {
	if (ret == ETOSClient.ETOS_ERROR) {
	    LOG.info("GetLastOpenRc = " + getLastOpenRC());
	    LOG.info("bye.");
	    return false;
	} else {
	    LOG.info("open: " + ret);
	    LOG.info("GetLastOpenRc = " + getLastOpenRC());
	}
	if (ret != ETOSClient.ETOS_SUCCESS) {
	    LOG.info("bye.");
	    return false;
	}
	LOG.info("Assigned TA: " + getAssignedTA(ETOSClient.PRCCONN));
	return true;
    }

    public void getOverflowMessages() {
	if (getOverflow()) {
	    MessageBlock newMsg = new MessageBlock();
	    while (etosClient.receive(newMsg, 500) == ETOSClient.ETOS_SUCCESS) {
		LOG.info(newMsg.getTA() + ":0x" + Integer.toHexString(newMsg.getFSC()).toUpperCase() + ">>" + newMsg.getMsg());
	    }
	}
    }

    public int sendMessage(String pMessage) {
	return etosClient.send(pMessage.trim());
    }

    public boolean validateCommands(int ret, TOSResult result) {
	if (ret == ETOSClient.ETOS_NO_OPEN && getOverflow()) {
	    LOG.info("Shutdown after message buffer overflow.");
	    result.setReturnValue(1);
	    result.setMessage("Shutdown after message buffer overflow.");
	    return false;
	} else {
	    switch (ret) {
	    case ETOSClient.ETOS_SUCCESS:
		return true;
	    case ETOSClient.ETOS_SERVER_CMD_REJECT:
		LOG.info("Command rejected by server.");
		result.setReturnValue(1);
		result.setMessage("Command rejected by server.");
		return false;
	    case ETOSClient.ETOS_TPF_CMD_REJECT:
		LOG.info("Command rejected by TPF.");
		result.setReturnValue(1);
		result.setMessage("Command rejected by TPF.");
		return false;
	    case ETOSClient.ETOS_BELOW_1052:
		LOG.info("Command rejected - below 1052 state.");
		result.setReturnValue(1);
		result.setMessage("Command rejected - below 1052 state.");
		return false;
	    case ETOSClient.ETOS_INVALID_ROUTE:
		LOG.info("Command rejected - Invalid route message.");
		result.setReturnValue(1);
		result.setMessage("Command rejected - Invalid route message.");
		return false;
	    case ETOSClient.ETOS_RECONN_IN_PROGRESS:
		LOG.info("Client reconnect in progress.");
		result.setReturnValue(1);
		result.setMessage("Client reconnect in progress.");
		return false;
	    case ETOSClient.ETOS_NO_OPEN:
		LOG.info("Ending client send thread. Session not open: " + ret);
		result.setReturnValue(1);
		result.setMessage("Ending client send thread. Session not open: " + ret);
		return false;
	    default:
		LOG.info("Error sending: " + ret);
		result.setReturnValue(1);
		result.setMessage("Error sending: " + ret);
		return false;
	    }
	}
    }

    public int receiveMessage(MessageBlock newMsg) {
	if (newMsg == null) {
	    newMsg = new MessageBlock();
	}
	return etosClient.receive(newMsg, 500);
    }

    public void close() {
	if (listener != null) {
	    listener.close();
	}
	if (etosClient != null) {
	    etosClient.close();
	}
	close = true;
    }

    public boolean isClosed() {
	return close;
    }

    public void setListener(String system, String cpuName) {
	listener = new TOSListener(this, system, cpuName);
	listener.start();
    }

    // public void sendResult(String system, TOSResult lResult) {
    // lClientImpl.sendResult(system, lResult);
    // }
    public TOSListener getListener() {
	return listener;
    }
}
