/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import com.hazelcast.core.IMap;
import com.tsi.workflow.User;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.EncryptUtil;
import com.tsi.workflow.websocket.WSMessagePublisher;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import javax.management.timer.Timer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author prabhu.prabhakaran
 */
@Component
public class SessionInvalidator {

    @Autowired
    CacheClient lCacheClient;

    @Autowired
    WSMessagePublisher wsserver;

    private static final Logger LOG = Logger.getLogger(SessionInvalidator.class.getName());

    @Scheduled(initialDelay = Timer.ONE_MINUTE * 30, fixedDelay = Timer.ONE_MINUTE)
    public void invalidateSession() {
	IMap<String, LocalDateTime> loginSessionMap = lCacheClient.getLoginSessionMap();
	LocalDateTime now = LocalDateTime.now();
	for (Map.Entry<String, LocalDateTime> entry : loginSessionMap.entrySet()) {
	    String key = entry.getKey();
	    LocalDateTime value = entry.getValue();
	    Duration between = Duration.between(value, now);
	    if (between.toMinutes() >= 5 * 60) {
		String userName = EncryptUtil.decrypt(key, Constants.SECRET_HASH);
		wsserver.sendMessage(Constants.Channels.SESSION_TIMEOUT, new User(userName), userName);
		LOG.info("Invalidating Session for " + EncryptUtil.decrypt(key, Constants.SECRET_HASH));
		lCacheClient.getLoginMap().remove(key);
		lCacheClient.getLoginSessionMap().remove(key);
	    }
	}
    }
}
