/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos;

import com.tsi.workflow.utils.Constants;
import javax.jms.JMSException;
import javax.jms.Message;
import org.apache.log4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author USER
 */
@Component
public class AdvisoryListener {

    private static final Logger LOG = Logger.getLogger(AdvisoryListener.class.getName());

    @JmsListener(destination = Constants.TOS_ADV_TOPIC_TOS_APP, containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(final Message message) {
	try {
	    int consumerCount = message.getIntProperty("consumerCount");
	    LOG.info("Consumer Count : " + consumerCount);
	    if (consumerCount == 1) {
		Constants.TOS_SECOND_SERVER_UP = Boolean.FALSE;
	    } else {
		Constants.TOS_SECOND_SERVER_UP = Boolean.TRUE;
	    }
	} catch (JMSException ex) {
	    LOG.error("Error in Monitoring Topic", ex);
	}

    }

}
