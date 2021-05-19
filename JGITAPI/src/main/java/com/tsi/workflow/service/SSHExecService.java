/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.service;

import com.tsi.workflow.ExecModel;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.ssh.SSHUtil;
import com.tsi.workflow.utils.JSONResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author USER
 */
@Service
public class SSHExecService {

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

    public JSONResponse executeCommand(ExecModel pModel) {
	SSHUtil lSSHUtil = getSSHUtil();
	if (pModel.getUser() == null && pModel.getSystem() != null) {
	    lSSHUtil.connectSSH(pModel.getSystem());
	} else if (pModel.getUser() != null && pModel.getSystem() == null) {
	    lSSHUtil.connectSSH(pModel.getUser());
	} else if (pModel.getUser() != null && pModel.getSystem() != null) {
	    lSSHUtil.connectSSH(pModel.getUser(), pModel.getSystem());
	} else if (pModel.getUser() == null && pModel.getSystem() == null) {
	    lSSHUtil.connectSSH();
	}
	JSONResponse executeCommand = lSSHUtil.executeCommand(pModel.getCommand());
	lSSHUtil.disconnectSSH();
	return executeCommand;
    }
}
