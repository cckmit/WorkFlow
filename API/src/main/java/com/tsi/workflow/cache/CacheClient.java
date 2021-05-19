/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.cache;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ISet;
import com.hazelcast.core.ITopic;
import com.tsi.workflow.CacheConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.audit.beans.dao.ApiActions;
import com.tsi.workflow.audit.beans.dao.ApiTransaction;
import com.tsi.workflow.audit.beans.dao.GiTransaction;
import com.tsi.workflow.audit.beans.dao.LinuxServers;
import com.tsi.workflow.beans.ui.DependentPlanRejectDetail;
import com.tsi.workflow.beans.ui.RepositoryView;
import com.tsi.workflow.beans.ui.TosActionQueue;
import com.tsi.workflow.gitblit.model.AccessPermission;
import com.tsi.workflow.gitblit.model.Repository;
import com.tsi.workflow.gitblit.model.UserModel;
import com.tsi.workflow.helper.DelegateHelper;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.websocket.WSBroadCaster;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author USER
 */
@Component
public class CacheClient {

    private static final Logger LOG = Logger.getLogger(CacheClient.class.getName());
    @Autowired
    CacheConfig cacheConfig;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    DelegateHelper delegateHelper;
    HazelcastInstance lClient;

    public HazelcastInstance getCacheInstance() {
	if (!(lClient != null && lClient.getLifecycleService().isRunning())) {
	    ClientConfig clientConfig = new ClientConfig();
	    clientConfig.setConnectionAttemptLimit(10);
	    clientConfig.setConnectionAttemptPeriod(24 * 60);
	    clientConfig.setConnectionTimeout(5000);
	    LOG.info("Connecting to " + cacheConfig.getCacheServer() + ":" + cacheConfig.getCachePort() + " with key: " + wFConfig.getSessionKey());
	    clientConfig.addAddress(cacheConfig.getCacheServer() + ":" + cacheConfig.getCachePort());
	    lClient = HazelcastClient.newHazelcastClient(clientConfig);
	    ITopic<String> socketTopic = lClient.getTopic(Constants.CacheCodes.WEB_SOCKET.name() + wFConfig.getSessionKey());
	    socketTopic.addMessageListener(new WSBroadCaster());
	    ITopic<String> delegateCacheTopic = lClient.getTopic(Constants.CacheCodes.DELEGATION.name() + wFConfig.getSessionKey());
	    delegateCacheTopic.addMessageListener(new DelegateCacheListener(wFConfig, delegateHelper));
	}
	return lClient;
    }

    // public IMap<String, DefaultMutableTreeNode> getDelegationMap() {
    // return getCacheInstance().getMap(Constants.CacheCodes.DELEGATION.name());
    // }
    public ITopic<String> getSocketTopic() {
	return getCacheInstance().getTopic(Constants.CacheCodes.WEB_SOCKET.name() + wFConfig.getSessionKey());
    }

    public IMap<String, String> getSocketUserMap() {
	return getCacheInstance().getMap(Constants.CacheCodes.WEB_SOCKET_USER_MAP.name() + wFConfig.getSessionKey());
    }

    public ITopic<String> getDelegationTopic() {
	return getCacheInstance().getTopic(Constants.CacheCodes.DELEGATION.name() + wFConfig.getSessionKey());
    }
    // public ITopic<String> getSuperUserTopic() {
    // return getCacheInstance().getTopic(Constants.CacheCodes.SUPER_USER.name());
    // }
    // public IMap<String, SortedSet<User>> getSuperUserMap() {
    // return getCacheInstance().getMap(Constants.CacheCodes.SUPER_USER.name());
    // }

    public IMap<String, User> getLoginMap() {
	return getCacheInstance().getMap(Constants.CacheCodes.LOGIN.name() + wFConfig.getSessionKey());
    }

    public IMap<String, LocalDateTime> getLoginSessionMap() {
	return getCacheInstance().getMap(Constants.CacheCodes.LOGIN_SESSION.name() + wFConfig.getSessionKey());
    }

    /**
     * [RepositoryCommonName, [Repository]]
     */
    public IMap<String, List<Repository>> getAllRepositoryMap() {
	return getCacheInstance().getMap(Constants.CacheCodes.ALL_REPOSITORY.name() + wFConfig.getSessionKey());
    }

    /**
     * [UserName, UserModel]
     */
    public IMap<String, UserModel> getAllRepoUsersMap() {
	return getCacheInstance().getMap(Constants.CacheCodes.ALL_USER.name() + wFConfig.getSessionKey());
    }

    /**
     * RepositoryName, [UserName, AccessPermission]
     */
    public IMap<String, Map<String, AccessPermission>> getRepoWiseUserMap() {
	return getCacheInstance().getMap(Constants.CacheCodes.REPO_WISE_USER.name() + wFConfig.getSessionKey());
    }

    /**
     * [RepositoryCommonName, RepositoryView]
     */
    public IMap<String, RepositoryView> getFilteredRepositoryMap() {
	return getCacheInstance().getMap(Constants.CacheCodes.FILTERED_REPOSITORY.name() + wFConfig.getSessionKey());
    }

    public IMap<String, User> getPlanUpdateStatusMap() {
	return getCacheInstance().getMap(Constants.CacheCodes.PLAN_STATUS_UPDATE.name() + wFConfig.getSessionKey());
    }

    public IMap<String, DependentPlanRejectDetail> getInProgressRelatedPlanMap() {
	return getCacheInstance().getMap(Constants.CacheCodes.IN_PROGRESS_RELATED_PLANS.name() + wFConfig.getSessionKey());
    }

    public ISet<String> getReconAllCommitNames() {
	return getCacheInstance().getSet("R_COMMIT_LIST_" + wFConfig.getSessionKey() + "_git");
    }

    public boolean isReconRunning() {
	ILock lock = getCacheInstance().getLock("R_COMMIT_LIST_" + wFConfig.getSessionKey() + "_git");
	if (lock.isLocked()) {
	    LOG.info("Already Recon Application is running");
	    return true;
	} else {
	    LOG.info("No Recon Application is running.");
	    return false;
	}

    }

    public IMap<String, TosActionQueue> getOnlineAcceptPlanMap() {
	return getCacheInstance().getMap(Constants.CacheCodes.ONLINE_PLAN.name() + wFConfig.getSessionKey());
    }

    public IMap<String, TosActionQueue> getProdTOSLoadActPlansMap() {
	return getCacheInstance().getMap(Constants.CacheCodes.PROD_TOS_LOAD_ACT_PLANS.name() + wFConfig.getSessionKey());
    }

    public IMap<String, TosActionQueue> getProdTOSAcceptPlansMap() {
	return getCacheInstance().getMap(Constants.CacheCodes.PROD_TOS_ACCEPT_PLANS.name() + wFConfig.getSessionKey());
    }

    public IMap<String, String> getInprogressCheckoutMap() {
	return getCacheInstance().getMap(Constants.CacheCodes.INPROGRESS_CHECKOUT.name() + wFConfig.getSessionKey());
    }

    public IMap<String, User> getInprogressMoveArtifactMap() {
	return getCacheInstance().getMap(Constants.CacheCodes.INPROGRESS_MOVE_ARTIFACT.name() + wFConfig.getSessionKey());
    }

    public IMap<String, TosActionQueue> getProdTosCommandProcessingMap() {
	return getCacheInstance().getMap(Constants.CacheCodes.PROD_TOS_TPF_COMMAND.name() + wFConfig.getSessionKey());
    }

    public IMap<String, ApiTransaction> getAuditApiTransactioMap() {
	return getCacheInstance().getMap(Constants.CacheCodes.API_TRANSACTION_INFO.name() + wFConfig.getSessionKey());
    }

    public IMap<String, ApiActions> getAuditApiActionsMap() {
	return getCacheInstance().getMap(Constants.CacheCodes.AUDIT_API_ACTIONS.name() + wFConfig.getSessionKey());
    }

    public IMap<String, LinuxServers> getAuditLinuxServersMap() {
	return getCacheInstance().getMap(Constants.CacheCodes.AUDIT_LNX_SERVERS.name() + wFConfig.getSessionKey());
    }

    public IMap<String, Boolean> getAuditConfig() {
	return getCacheInstance().getMap(Constants.CacheCodes.AUDIT_CONFIG.name() + wFConfig.getSessionKey());
    }

    public IMap<String, GiTransaction> getAuditGiTransactionMap() {
	return getCacheInstance().getMap(Constants.CacheCodes.AUDIT_GI_TRANSACTION_INFO.name() + wFConfig.getSessionKey());
    }

    public ISet<String> getRequestMonitor() {
	return getCacheInstance().getSet(Constants.CacheCodes.REQUEST_MONITOR.name() + wFConfig.getSessionKey());
    }

    public IMap<String, Map<List<String>, Map<String, String>>> getValidateMakFileMap() {
	return getCacheInstance().getMap(Constants.CacheCodes.VALIDATE_MAK_FILE.name() + wFConfig.getSessionKey());
    }

    public IMap<String, Set<String>> getBuildQueueInfo() {
	return getCacheInstance().getMap(Constants.CacheCodes.BUILD_QUEUE_INFO.name() + wFConfig.getSessionKey());
    }

    public IMap<String, Set<String>> getPlanAndActions() {
	return getCacheInstance().getMap(Constants.CacheCodes.PLAN_ACTIONS.name() + wFConfig.getSessionKey());
    }

}
