package com.tsi.workflow;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Deepa
 */
@Component
public class CacheConfig {

    @Value("${wf.cache.server.host}")
    private String cacheServer;
    @Value("${wf.cache.server.port}")
    private int cachePort;

    public String getCacheServer() {
	return cacheServer;
    }

    public void setCacheServer(String cacheServer) {
	this.cacheServer = cacheServer;
    }

    public int getCachePort() {
	return cachePort;
    }

    public void setCachePort(int cachePort) {
	this.cachePort = cachePort;
    }
}
