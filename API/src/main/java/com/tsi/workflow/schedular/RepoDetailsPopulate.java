/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.RepositoryDetails;
import com.tsi.workflow.beans.ui.RepositoryView;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.RepoDetailsDAO;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.management.timer.Timer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author
 */
@Component
public class RepoDetailsPopulate {

    private static final Logger LOG = Logger.getLogger(RepoDetailsPopulate.class.getName());

    @Autowired
    RepoDetailsDAO repoDetailsDAO;
    @Autowired
    LDAPAuthenticatorImpl lDAPAuthenticatorImpl;
    @Autowired
    CacheClient cacheClient;
    @Autowired
    MailMessageFactory mailMessageFactory;

    @Autowired
    WFConfig wFConfig;

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    public CacheClient getCacheClient() {
	return cacheClient;
    }

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_DAY)
    @Transactional
    public void repoDetailsPopulate() {
	if (wFConfig.getPrimary()) {
	    try {
		User serviceUser = lDAPAuthenticatorImpl.getServiceUser();
		Collection<RepositoryView> values = getCacheClient().getFilteredRepositoryMap().values();
		Set<RepositoryView> lReturn = new TreeSet<>(values.stream().filter((t) -> !t.getRepository().getName().contains("DERIVED_")).sorted().collect(Collectors.toSet()));
		for (RepositoryView lRepo : lReturn) {
		    RepositoryDetails lRpoObj = repoDetailsDAO.findByTrimeName(lRepo.getRepository().getTrimmedName());
		    if (lRpoObj == null) {
			RepositoryDetails lRepoDetails = new RepositoryDetails();
			lRepoDetails.setDefaultFileCreate("Yes");
			lRepoDetails.setRepoName(lRepo.getRepository().getName());
			lRepoDetails.setTrimmedName(lRepo.getRepository().getTrimmedName());
			lRepoDetails.setRepoDescription(lRepo.getRepository().getDescription());
			lRepoDetails.setFuncArea(lRepo.getRepository().getFuncArea());
			repoDetailsDAO.save(serviceUser, lRepoDetails);
		    }
		}

	    } catch (Exception e) {
		LOG.error("Unable to update Repo details: ", e);
	    }
	}
    }

}
