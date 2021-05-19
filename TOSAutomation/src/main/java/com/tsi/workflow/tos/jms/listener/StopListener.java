/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos.jms.listener;

import com.tsi.workflow.tos.client.TOSClientImpl;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.apache.log4j.Logger;

/**
 *
 * @author USER
 */
public class StopListener implements MessageListener {

    private static final Logger LOG = Logger.getLogger(StopListener.class.getName());

    @Override
    public void onMessage(Message msg) {
	if (msg instanceof TextMessage) {
	    LOG.info("Got STOP Message from MQ");
	    if (!TOSClientImpl.exited) {
		try {
		    TextMessage lMessage = (TextMessage) msg;
		    if (lMessage.getText().equals("Exit")) {
			LOG.info("Marked Application as Exited, Exiting in a Minute");
			TOSClientImpl.exited = true;
			new StopActionThread(0).start();
		    }
		} catch (JMSException ex) {
		    LOG.error("Error in Stopping Client", ex);
		    new StopActionThread(1).start();
		} catch (Exception ex) {
		    LOG.error("Error in Stopping Client", ex);
		    new StopActionThread(1).start();
		}
	    }
	}
    }
}

class StopActionThread extends Thread {

    private static final Logger LOG = Logger.getLogger(StopActionThread.class.getName());
    final Integer lCode;

    public StopActionThread(Integer pCode) {
	lCode = pCode;
    }

    @Override
    public void run() {
	try {
	    Thread.sleep(1000L);
	    LOG.info("Application Exiting Now Code : " + lCode);
	    System.exit(lCode);
	} catch (InterruptedException ex) {
	    LOG.error("Error in Exit Waiting", ex);
	}
    }

}
