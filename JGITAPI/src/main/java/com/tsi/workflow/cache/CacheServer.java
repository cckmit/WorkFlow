/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.cache;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.tsi.workflow.CacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author USER
 */
@Component
public class CacheServer {

    @Autowired
    CacheConfig lCacheConfig;
    private HazelcastInstance instance;

    public void startServer() {
	Config lConfig = new Config();
	lConfig.setInstanceName(lCacheConfig.getCacheServer());
	NetworkConfig network = lConfig.getNetworkConfig();
	network.setPortAutoIncrement(false);
	network.setPortCount(1);
	network.setPort(lCacheConfig.getCachePort());
	JoinConfig join = network.getJoin();
	join.getAwsConfig().setEnabled(false);
	join.getTcpIpConfig().addMember(lCacheConfig.getSyncServers());
	join.getTcpIpConfig().setEnabled(true);
	join.getMulticastConfig().setEnabled(false);
	instance = Hazelcast.newHazelcastInstance(lConfig);
    }

    public void stopServer() {
	instance.shutdown();
    }
}
