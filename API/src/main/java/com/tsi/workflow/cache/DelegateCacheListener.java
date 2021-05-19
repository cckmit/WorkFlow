/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.cache;

import com.google.gson.Gson;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.helper.DelegateHelper;
import org.apache.log4j.Logger;

/**
 *
 * @author USER
 */
public class DelegateCacheListener implements MessageListener<String> {

    WFConfig wFConfig;
    DelegateHelper lDelegateHelper;
    private static final Logger LOG = Logger.getLogger(DelegateCacheListener.class.getName());

    public DelegateCacheListener(WFConfig wFConfig, DelegateHelper lDelegateHelper) {
	this.wFConfig = wFConfig;
	this.lDelegateHelper = lDelegateHelper;
    }

    @Override
    public void onMessage(Message<String> message) {
	LOG.info("Got Delegate Cache Sync Message");
	String lDelegateCacheString = message.getMessageObject();
	DelegateCache lDelegateCache = new Gson().fromJson(lDelegateCacheString, DelegateCache.class);
	if (!lDelegateCache.getProfile().equals(wFConfig.getProfileName())) {
	    LOG.info("Got Delegate Cache Sync Message from Another Server");
	    if (lDelegateCache.getAction().equals("ADD")) {
		LOG.info("Got Delegate Cache Sync Message from Another Server for ADDING " + lDelegateCache.getFrom() + ", " + lDelegateCache.getTo());
		lDelegateHelper.addToCache(lDelegateCache.getFrom(), lDelegateCache.getTo(), false);
	    } else {
		LOG.info("Got Delegate Cache Sync Message from Another Server for REMOVING " + lDelegateCache.getFrom() + ", " + lDelegateCache.getTo());
		lDelegateHelper.removeFromCache(lDelegateCache.getFrom(), lDelegateCache.getTo(), false);
	    }
	}
    }
}
