/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.beans.dao.Project;
import com.tsi.workflow.dao.ProjectDAO;
import com.tsi.workflow.dao.external.CSRNumberDAO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.management.timer.Timer;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
@Component
public class CSRNumberPopulate {

    @Autowired
    CSRNumberDAO cSRNumberDAO;
    @Autowired
    ProjectDAO projectDAO;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    LDAPAuthenticatorImpl lDAPAuthenticatorImpl;

    @Transactional
    // @Scheduled(cron = "0 7 * * *")
    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_HOUR * 6)
    public void doMonitor() {
	if (wFConfig.getPrimary()) {
	    populateTable();
	}
    }

    private void populateTable() {

	User lUser = lDAPAuthenticatorImpl.getServiceUser();
	List<Project> lCSRNumbers = cSRNumberDAO.findAll();
	List<String> lCurrentActiveProjectNumbers = new ArrayList<>();
	Map<String, Project> lActiveProjectNumbers = projectDAO.findActiveProjectNumbers();
	Map<String, Project> lNonActiveProjectNumbers = projectDAO.findNonActiveProjectNumbers();

	for (Project lProject : lCSRNumbers) {
	    if (lActiveProjectNumbers.keySet().contains(lProject.getProjectNumber())) {
	    } else if (lNonActiveProjectNumbers.keySet().contains(lProject.getProjectNumber())) {
		Project project = lNonActiveProjectNumbers.get(lProject.getProjectNumber());
		project.setActive("Y");
		projectDAO.update(lUser, project);
	    } else {
		lProject.setIsDelta(Boolean.FALSE);
		projectDAO.save(lUser, lProject);
	    }
	    lCurrentActiveProjectNumbers.add(lProject.getProjectNumber());
	}
	Collection<String> lRemainingProjectNumbers = CollectionUtils.subtract(lActiveProjectNumbers.keySet(), lCurrentActiveProjectNumbers);
	for (String lRemainingProjectNumber : lRemainingProjectNumbers) {
	    Project lProject = lActiveProjectNumbers.get(lRemainingProjectNumber);
	    projectDAO.delete(lUser, lProject);
	}

    }
}
