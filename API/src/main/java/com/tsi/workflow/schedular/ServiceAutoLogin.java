/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.utils.Constants;
import com.workflow.ssh.SSHClientUtils;
import java.util.List;
import javax.management.timer.Timer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author prabhu.prabhakaran
 */
@Component
public class ServiceAutoLogin {

    @Autowired
    SystemDAO systemDAO;
    @Autowired
    LDAPAuthenticatorImpl lDAPAuthenticatorImpl;
    @Autowired
    GITConfig gITConfig;
    @Autowired
    SSHClientUtils sSHClientUtils;
    private static final Logger LOG = Logger.getLogger(ServiceAutoLogin.class.getName());

    @Scheduled(initialDelay = Timer.ONE_SECOND * 30, fixedDelay = Timer.ONE_DAY)
    @Transactional
    public void serviceUserAutoLogin() {
	if (!Constants.MTPSERVICE_KEY_MAP) {
	    LOG.info("Trying to Authenticate Serivce User");
	    List lSystems = systemDAO.findAll();
	    User user = lDAPAuthenticatorImpl.getServiceUser();
	    user.setEncryptedPassword(gITConfig.getServiceSecret());
	    Boolean lClientResponse;
	    try {
		lClientResponse = sSHClientUtils.authenticate(user, lSystems);
		if (lClientResponse) {
		    Constants.MTPSERVICE_KEY_MAP = true;
		    LOG.info("Serivce User Authentication Success, Application is UP.");
		} else {
		    LOG.error("Serivce User Authentication Failed, Login Blocked");
		}
	    } catch (Exception ex) {
		LOG.error("Error in Authenticating Service User", ex);
	    }

	}
    }
}
