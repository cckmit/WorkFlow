/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import com.tsi.workflow.WFConfig;
import com.tsi.workflow.git.GitUserDetails;
import com.tsi.workflow.helper.GITHelper;
import javax.management.timer.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Radha.Adhimoolam
 */
@Component
public class GITCache {

    @Autowired
    GITHelper gitHelper;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    GitUserDetails gitUserDetails;

    public GitUserDetails gitUserDetails() {
	return gitUserDetails;
    }

    public GITHelper getGITHelper() {
	return gitHelper;
    }

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_HOUR)
    @Transactional
    public void populateGitInfo() {
	if (wFConfig.getPrimary()) {
	    gitUserDetails().populateGitUserDetails();
	    gitUserDetails().deleteOldDelegations();
	    // getGITHelper().populateGitCache();
	    getGITHelper().populateGitCacheNew();
	}
    }

}
