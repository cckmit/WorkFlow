/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.beans.dao.Dbcr;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author
 */
@Component
public class DbcrHelper {

    private static final Logger LOG = Logger.getLogger(DbcrHelper.class.getName());

    @Autowired
    SystemDAO systemDAO;
    @Autowired
    GITConfig gITConfig;
    @Autowired
    SSHClientUtils sSHClientUtils;

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public SystemDAO getSystemDAO() {
	return systemDAO;
    }

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public JSONResponse validateDbcr(Integer systemId, String dbcrName) {
	JSONResponse lResponse = new JSONResponse();
	com.tsi.workflow.beans.dao.System system = getSystemDAO().find(systemId);
	String command = Constants.SystemScripts.DBCR_VALIDATE.getScript() + dbcrName;
	lResponse = getsSHClientUtils().executeCommand(system, command);
	if (lResponse.getStatus()) {
	    String data = lResponse.getData().toString();
	    if (!data.contains(system.getName())) {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("DBCR not valid for system - " + system.getName());
		LOG.info("System name does not match");
	    } else {
		String[] output = data.split(" ");
		Dbcr dbcr = new Dbcr();
		dbcr.setDbcrName(output[1]);
		dbcr.setSystemId(new com.tsi.workflow.beans.dao.System(systemId));
		dbcr.setEnvironment(output[3].toUpperCase());
		dbcr.setStatus(output[4].toUpperCase().trim());
		lResponse.setStatus(Boolean.TRUE);
		lResponse.setData(dbcr);
	    }
	}
	return lResponse;
    }

    public Boolean isDbcrComplete(Dbcr dbcr) {
	JSONResponse response = validateDbcr(dbcr.getSystemId().getId(), dbcr.getDbcrName());
	boolean retValue = response.getStatus();
	if (response.getStatus()) {
	    Dbcr dbcrResponse = (Dbcr) (response.getData());
	    if (dbcrResponse.getStatus().equalsIgnoreCase(Constants.DBCR_STATUS.COMPLETE.toString()) && dbcrResponse.getEnvironment().equalsIgnoreCase(dbcr.getEnvironment())) {
		retValue = Boolean.TRUE;
	    } else {
		retValue = Boolean.FALSE;
	    }
	}
	return retValue;
    }
}
