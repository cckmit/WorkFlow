/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.websocket;

import com.google.gson.Gson;
import com.tsi.workflow.User;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author prabhu.prabhakaran
 */
@Component
public class WSMessagePublisher {

    private static final Logger LOG = Logger.getLogger(WSMessagePublisher.class.getName());

    @Autowired
    CacheClient cacheClient;

    // public void sendMessage(Constants.Channels channel, String planId, Object
    // message) {
    // if (planId == null) {
    // sendMessage(channel, "*", "*", message);
    // } else {
    // sendMessage(channel, "*", planId, message);
    // }
    // }
    public void sendMessage(Constants.Channels channel, String userId, Object message) {
	if (userId == null || userId.isEmpty()) {
	    sendMessage(channel, "*", "*", message);
	} else {
	    sendMessage(channel, userId, "*", message);
	}
    }

    public void sendMessage(Constants.Channels channel, User userId, Object message) {
	if (userId == null) {
	    sendMessage(channel, "*", "*", message);
	} else {
	    sendMessage(channel, userId.getId(), "*", message);
	}
    }

    // public void sendMessage(Constants.Channels channel, Object message) {
    // sendMessage(channel, "*", "*", message);
    // }
    public void sendMessage(Constants.Channels channel, String userId, String planId, Object message) {
	WSSMessage wssMessage = new WSSMessage(channel, userId, planId, message);
	String lMessage = new Gson().toJson(wssMessage);
	LOG.info("Publishing Message to Cache - " + lMessage);
	cacheClient.getSocketTopic().publish(lMessage);
    }
}
