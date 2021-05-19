/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.workflow.TOSConfig;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.tos.model.TOSResult;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.tsi.workflow.websocket.WSMessagePublisher;
import java.util.concurrent.ConcurrentHashMap;
import javax.jms.Message;
import javax.jms.TextMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author USER
 */
@Component
public class TOSIPReceiver {

    @Autowired
    ProductionLoadsDAO productionLoadsDAO;
    @Autowired
    TOSConfig tosConfig;
    @Autowired
    ConcurrentHashMap<Integer, String> tosIpMap;
    @Autowired
    WSMessagePublisher wsserver;

    private static final Logger LOG = Logger.getLogger(TOSIPReceiver.class.getName());

    @JmsListener(destination = Constants.TOS_TOPIC_IP_TOS_APP, containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(final Message message) {
	if (message instanceof TextMessage) {
	    try {
		TextMessage lMessage = (TextMessage) message;
		LOG.info("<-- TOS IP Client : " + lMessage.getText());
		TOSResult result = new ObjectMapper().readValue(lMessage.getText(), TOSResult.class);
		if (!result.getHost().equals(tosConfig.getTosServerId()) && Constants.TOS_SECOND_SERVER_UP) {
		    return;
		}
		JSONResponse lResponse = new JSONResponse();
		if (result.getReturnValue() == 0) {
		    lResponse.setStatus(true);
		    result.setIpAddress(result.getMessage().replace("STATUS=", "").replaceAll("\"", "").replaceAll("^0+", "").replaceAll("\\.0+", "\\."));
		    lResponse.setData(result);
		} else {
		    lResponse.setErrorMessage(result.getMessage());
		    lResponse.setStatus(false);
		}
		if (result.isLast()) {
		    if (result.getIpAddress() == null) {
			tosIpMap.put(result.getId(), "");
		    } else {
			tosIpMap.put(result.getId(), result.getIpAddress());
		    }
		    wsserver.sendMessage(Constants.Channels.PROD_FTP_IP, result.getUser(), lResponse);
		} else {
		    wsserver.sendMessage(Constants.Channels.PROD_FTP_IP, result.getUser(), lResponse);
		}
	    } catch (Exception ex) {
		LOG.error("Error in Receriving Message", ex);
	    }
	}
    }
}
