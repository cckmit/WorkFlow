package com.tsi.workflow.schedular;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.service.TDBoxAuthService;
import com.tsi.workflow.ssh.SSHUtil;
import com.tsi.workflow.utils.JSONResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReconsileSchedular {

    @Autowired
    GITConfig gITConfig;
    @Autowired
    TDBoxAuthService lTDBoxAuthService;

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public SSHUtil getSSHUtil() {
	return new SSHUtil(getGITConfig(), lTDBoxAuthService);
    }

    private static final Logger LOG = Logger.getLogger(ReconsileSchedular.class.getName());

    @Scheduled(cron = "0 0 3 * * ?")
    public void gitReconsileJob() {
	if (gITConfig.getGitSearchType().equals("GIT_DB")) {
	    LOG.info("Init Reconsile job");
	    SSHUtil sshUtil = getSSHUtil();
	    sshUtil.connectSSH();
	    JSONResponse executeCommand = sshUtil.executeCommand("${MTP_ENV}/mtpgitreconsilejob");
	    sshUtil.disconnectSSH();
	    LOG.info("Return Code for Reconsile job Init :" + executeCommand.getStatus());
	}
    }
}
