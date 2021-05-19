/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.gbldap;

import com.gitblit.IStoredSettings;
import com.gitblit.Keys;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alfred Schmid
 *
 */
public final class TPLdapSyncService implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(TPLdapSyncService.class);

    private final IStoredSettings settings;

    private final TPLdapAuthProvider ldapAuthProvider;

    private final AtomicBoolean running = new AtomicBoolean(false);

    public TPLdapSyncService(IStoredSettings settings, TPLdapAuthProvider ldapAuthProvider) {
	this.settings = settings;
	this.ldapAuthProvider = ldapAuthProvider;
    }

    /**
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
	logger.info("Starting user and group sync with ldap service");
	if (!running.getAndSet(true)) {
	    try {
		ldapAuthProvider.sync();
	    } catch (Exception e) {
		logger.error("Failed to synchronize with ldap", e);
	    } finally {
		running.getAndSet(false);
	    }
	}
	logger.info("Finished user and group sync with ldap service");
    }

    public boolean isReady() {
	return settings.getBoolean(Keys.realm.ldap.synchronize, false);
    }

}
