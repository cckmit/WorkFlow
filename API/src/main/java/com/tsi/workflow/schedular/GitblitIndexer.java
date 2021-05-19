/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import com.tsi.workflow.WFConfig;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import java.io.IOException;
import java.util.List;
import javax.management.timer.Timer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
@Component
public class GitblitIndexer {

    @Autowired
    GitBlitClientUtils lGitBlitClientUtils;
    @Autowired
    ImplementationDAO implementationDAO;
    @Autowired
    WFConfig wFConfig;

    private static final Logger LOG = Logger.getLogger(GitblitIndexer.class.getName());

    public GitBlitClientUtils getGitBlitClientUtils() {
	return lGitBlitClientUtils;
    }

    public ImplementationDAO getImplementationDAO() {
	return implementationDAO;
    }

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND * 30)
    @Transactional
    public void reindexTickets() {
	if (wFConfig.getPrimary()) {
	    LOG.info("Triggerring Refresh index");
	    List<String> lAllReviewPlans = getImplementationDAO().findAllReviewPlans();
	    for (String lReviewPlan : lAllReviewPlans) {
		// Need to remove hardcoded
		String pCompany = lReviewPlan.toLowerCase().startsWith("t") ? "tp" : "dl";
		try {
		    getGitBlitClientUtils().refreshIndex(pCompany, lReviewPlan);
		} catch (IOException ex) {
		    LOG.error("Error in Indexing Ticket for the plan " + lReviewPlan, ex);
		}
	    }
	}
    }
}
