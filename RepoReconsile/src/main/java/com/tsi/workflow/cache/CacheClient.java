package com.tsi.workflow.cache;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ISet;
import com.tsi.workflow.Main;
import com.tsi.workflow.config.AppConfig;
import org.apache.log4j.Logger;

/**
 *
 * @author User
 */
public class CacheClient {

    private HazelcastInstance instance;
    private static CacheClient lClient;
    private static final Logger LOG = Logger.getLogger(CacheClient.class.getName());

    private CacheClient() {
    }

    public static CacheClient getInstance() {
	if (lClient == null) {
	    lClient = new CacheClient();
	}
	return lClient;
    }

    public void shutdown() {
	instance.shutdown();
    }

    private HazelcastInstance getCache() {
	if (instance == null) {
	    ClientConfig clientConfig = new ClientConfig();
	    clientConfig.setConnectionAttemptLimit(10);
	    clientConfig.setConnectionAttemptPeriod(24 * 60);
	    clientConfig.setConnectionTimeout(5000);
	    LOG.info("Connecting to " + AppConfig.getInstance().getCacheServer() + ":" + AppConfig.getInstance().getCachePort() + " with key: " + AppConfig.getInstance().getSessionKey() + "_" + Main.lDBSchema);
	    clientConfig.addAddress(AppConfig.getInstance().getCacheServer() + ":" + AppConfig.getInstance().getCachePort());
	    instance = HazelcastClient.newHazelcastClient(clientConfig);
	}
	return instance;
    }

    public boolean isAlreadyRunning() {
	ILock lock = getCache().getLock("RECON_" + AppConfig.getInstance().getSessionKey() + "_" + Main.lDBSchema);
	if (lock.isLocked()) {
	    LOG.error("Already Recon Application is running");
	    return true;
	} else {
	    LOG.error("No Recon Application is running, Trying to Lock");
	}
	Boolean lReturn = lock.tryLock();
	if (lReturn) {
	    LOG.error("Recon Application Locked");
	    return false;
	} else {
	    LOG.error("Recon Application Not Locked");
	    return true;
	}
    }

    public void unlock() {
	ILock lock = getCache().getLock("RECON_" + AppConfig.getInstance().getSessionKey() + "_" + Main.lDBSchema);
	if (lock.isLocked()) {
	    lock.unlock();
	    LOG.error("No Recon Application is running, UnLock Done");
	}
    }

    public void clearAllCache() {
	getAllSubRepositoryNames().clear();
	getAllRepositoryNames().clear();
	getAllRepositoryDetails().clear();
	getAllSubRepositoryDetails().clear();
	getAllSystemFileMap().clear();
	getAllCommitNames().clear();
	// CacheClient.getInstance().getAllSystemFileNames().clear();
    }

    public ISet<String> getAllRepositoryNames() {
	return getCache().getSet("R_REPO_NAME_LIST_" + AppConfig.getInstance().getSessionKey() + "_" + Main.lDBSchema);
    }

    public ISet<String> getAllSubRepositoryNames() {
	return getCache().getSet("R_SUB_REPO_NAME_LIST_" + AppConfig.getInstance().getSessionKey() + "_" + Main.lDBSchema);
    }

    public ISet<String> getAllCommitNames() {
	return getCache().getSet("R_COMMIT_LIST_" + AppConfig.getInstance().getSessionKey() + "_" + Main.lDBSchema);
    }

    public IMap<String, Integer> getAllSystemFileMap() {
	return getCache().getMap("R_FILE_NAME_MAP_" + AppConfig.getInstance().getSessionKey() + "_" + Main.lDBSchema);
    }

    public IMap<String, Integer> getAllRepositoryDetails() {
	return getCache().getMap("R_REPO_ID_MAP_" + AppConfig.getInstance().getSessionKey() + "_" + Main.lDBSchema);
    }

    public IMap<String, Integer> getAllSubRepositoryDetails() {
	return getCache().getMap("R_SUB_REPO_NAME_MAP_" + AppConfig.getInstance().getSessionKey() + "_" + Main.lDBSchema);
    }

    public IMap<Integer, String> getAllSubRepositoryIDDetails() {
	return getCache().getMap("R_SUB_REPO_ID_MAP_" + AppConfig.getInstance().getSessionKey() + "_" + Main.lDBSchema);
    }

}
