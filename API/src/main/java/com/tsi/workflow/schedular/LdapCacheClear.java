/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.beans.dao.UserSettings;
import com.tsi.workflow.dao.UserSettingsDAO;
import com.tsi.workflow.helper.DelegateHelper;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;
import javax.management.timer.Timer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
@Component
public class LdapCacheClear {

    private static final Logger LOG = Logger.getLogger(LdapCacheClear.class.getName());

    @Autowired
    @Qualifier("lLdapUserMap")
    ConcurrentHashMap<String, User> lLdapUserMap;
    @Autowired
    @Qualifier("lLdapGroupMap")
    ConcurrentHashMap<String, SortedSet<User>> lLdapGroupMap;
    @Autowired
    UserSettingsDAO userSettingsDAO;
    @Autowired
    DelegateHelper delegateHelper;
    @Autowired
    WFConfig wFConfig;

    public DelegateHelper getDelegateHelper() {
	return delegateHelper;
    }

    public UserSettingsDAO getUserSettingsDAO() {
	return userSettingsDAO;
    }

    @Scheduled(initialDelay = Timer.ONE_HOUR, fixedDelay = Timer.ONE_HOUR)
    public void clearCache() {
	lLdapUserMap.clear();
	lLdapGroupMap.clear();
    }

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_HOUR)
    @Transactional
    public void populateDelegationCache() {
	List<UserSettings> lDelegateSettings = getUserSettingsDAO().findAllActiveDelegations();
	for (UserSettings lDelegateSetting : lDelegateSettings) {
	    if (!getDelegateHelper().addToCache(lDelegateSetting.getUserId(), lDelegateSetting.getValue(), false)) {
		LOG.error("Error in Mapping Delegation for the User " + lDelegateSetting.getUserId());
	    }
	}
    }
}
