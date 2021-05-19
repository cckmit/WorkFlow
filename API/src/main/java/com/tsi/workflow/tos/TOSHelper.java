/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos;

import com.google.gson.Gson;
import com.tsi.workflow.TOSConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemCpu;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.dao.PreProductionLoadsDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.dao.PutLevelDAO;
import com.tsi.workflow.dao.SystemCpuDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.tos.model.TOSRequest;
import com.tsi.workflow.utils.Constants;
import java.util.concurrent.ConcurrentHashMap;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

/**
 *
 * @author USER
 */
@Component
public class TOSHelper {

    private static final Logger LOG = Logger.getLogger(TOSHelper.class.getName());

    @Autowired
    JmsTemplate AppToTOSQueue;
    @Autowired
    JmsTemplate AppToTOSIPQueue;
    @Autowired
    TOSConfig config;
    @Autowired
    SystemCpuDAO systemCpuDAO;
    @Autowired
    ConcurrentHashMap<Integer, String> tosIpMap;
    @Autowired
    PutLevelDAO putLevelDAO;
    @Autowired
    ProductionLoadsDAO productionLoadsDAO;
    @Autowired
    PreProductionLoadsDAO preProductionLoadsDAO;

    private void sendMessage(final TOSRequest request) {

	AppToTOSQueue.send(new MessageCreator() {
	    @Override
	    public Message createMessage(Session session) throws JMSException {
		String lText = new Gson().toJson(request);
		LOG.info("--> TOS Client : " + lText);
		return session.createTextMessage(lText);
	    }
	});
    }

    private void requestIP(final TOSRequest request) {

	AppToTOSIPQueue.send(new MessageCreator() {
	    @Override
	    public Message createMessage(Session session) throws JMSException {
		String lText = new Gson().toJson(request);
		LOG.info("--> TOS IP Client : " + lText);
		return session.createTextMessage(lText);
	    }
	});
    }

    public Boolean doTOSOperation(User user, Constants.LoadSetCommands pOperation, ProductionLoads lLoadSet, String lOldLoadSetStatus, SystemLoad pLoad) {
	try {
	    TOSRequest tosRequest = new TOSRequest();
	    System lSystem = pLoad.getSystemId();
	    SystemCpu lCpu;
	    if (config.getTosSystemType().equals(Constants.TOSEnvironment.PRODUCTION.name())) {
		lCpu = systemCpuDAO.find(lSystem.getDefaultProdCpu());
	    } else {
		lCpu = systemCpuDAO.find(lSystem.getDefaultNativeCpu());
	    }
	    if (lCpu == null) {
		throw new WorkflowException("Default CPU not found");
	    }
	    String lIP1 = lCpu.getPrimaryIpAddress();
	    String lIP2 = lCpu.getSecondaryIpAddress();
	    tosRequest.setPrimaryAddress(lIP1);
	    tosRequest.setSecondaryAddress(lIP2);
	    tosRequest.setSystem(lSystem.getName());
	    tosRequest.setHost(config.getTosServerId());
	    tosRequest.setId(lLoadSet.getId());
	    tosRequest.setUser(user);
	    tosRequest.setCpuName(lCpu.getCpuName());

	    if (lLoadSet.getCpuId() != null) {
		if (pOperation == Constants.LoadSetCommands.ACTIVATE || pOperation == Constants.LoadSetCommands.DEACTIVATE) {
		    tosRequest.setCommand(pOperation.getScript() + pLoad.getLoadSetName().toUpperCase() + " " + lLoadSet.getCpuId().getCpuCode());
		} else {
		    tosRequest.setCommand(pOperation.getScript() + pLoad.getLoadSetName().toUpperCase());
		}
	    } else {
		// ZTPFM-2152 PutLevel Upgrade
		String loadStatus = getLoadStatus(pOperation);
		if (loadStatus != null && loadStatus.equals(lLoadSet.getStatus())) {
		    PutLevel putLevel = putLevelDAO.getPutLevel(lSystem, Constants.PUTLevelOptions.PROD_CO_EXIST.name());
		    if (putLevel != null) {
			tosRequest.setCoexist(Boolean.TRUE);
			lLoadSet.setPrimaryTosRequest("INPROGRESS");
			lLoadSet.setSecondaryTosRequest("INPROGRESS");
		    } else {
			tosRequest.setCoexist(Boolean.FALSE);
			lLoadSet.setPrimaryTosRequest("INPROGRESS");
			lLoadSet.setSecondaryTosRequest("NONE");
		    }
		    productionLoadsDAO.update(user, lLoadSet);
		}
		tosRequest.setCommand(pOperation.getScript() + pLoad.getLoadSetName().toUpperCase());
	    }
	    if (lOldLoadSetStatus != null) {
		tosRequest.setOldStatus(lOldLoadSetStatus);
	    } else {
		tosRequest.setOldStatus(Constants.LOAD_SET_STATUS.DELETED.name());
	    }

	    sendMessage(tosRequest);

	    return true;
	} catch (WorkflowException e) {
	    throw e;
	} catch (Exception e) {
	    LOG.error("Error in sending TOS Command,", e);
	}
	return false;
    }

    private String getLoadStatus(Constants.LoadSetCommands pOperation) {
	String loadStatus = null;
	if (pOperation == Constants.LoadSetCommands.LOAD) {
	    loadStatus = Constants.LOAD_SET_STATUS.LOADED.name();
	} else if (pOperation == Constants.LoadSetCommands.ACTIVATE) {
	    loadStatus = Constants.LOAD_SET_STATUS.ACTIVATED.name();
	} else if (pOperation == Constants.LoadSetCommands.DEACTIVATE) {
	    loadStatus = Constants.LOAD_SET_STATUS.DEACTIVATED.name();
	} else if (pOperation == Constants.LoadSetCommands.DELETE) {
	    loadStatus = Constants.LOAD_SET_STATUS.DELETED.name();
	} else if (pOperation == Constants.LoadSetCommands.ACCEPT) {
	    loadStatus = Constants.LOAD_SET_STATUS.ACCEPTED.name();
	}
	return loadStatus;
    }

    private String getFallBackLoadStatus(Constants.LoadSetCommands pOperation) {
	String loadStatus = null;
	if (pOperation == Constants.LoadSetCommands.LOAD) {
	    loadStatus = Constants.LOAD_SET_STATUS.FALLBACK_LOADED.name();
	} else if (pOperation == Constants.LoadSetCommands.ACTIVATE) {
	    loadStatus = Constants.LOAD_SET_STATUS.FALLBACK_ACTIVATED.name();
	} else if (pOperation == Constants.LoadSetCommands.DEACTIVATE) {
	    loadStatus = Constants.LOAD_SET_STATUS.FALLBACK_DEACTIVATED.name();
	} else if (pOperation == Constants.LoadSetCommands.DELETE) {
	    loadStatus = Constants.LOAD_SET_STATUS.FALLBACK_DELETED.name();
	} else if (pOperation == Constants.LoadSetCommands.ACCEPT) {
	    loadStatus = Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name();
	}
	return loadStatus;
    }

    public Boolean doFallbackTOSOperation(User user, Constants.LoadSetCommands pOperation, ProductionLoads lLoadSet, String lOldLoadSetStatus, SystemLoad pLoad, String reason) {
	try {
	    TOSRequest tosRequest = new TOSRequest();
	    System lSystem = pLoad.getSystemId();
	    SystemCpu lCpu;
	    if (config.getTosSystemType().equals(Constants.TOSEnvironment.PRODUCTION.name())) {
		lCpu = systemCpuDAO.find(lSystem.getDefaultProdCpu());
	    } else {
		lCpu = systemCpuDAO.find(lSystem.getDefaultNativeCpu());
	    }
	    if (lCpu == null) {
		throw new WorkflowException("Default CPU not found");
	    }
	    String lIP1 = lCpu.getPrimaryIpAddress();
	    String lIP2 = lCpu.getSecondaryIpAddress();
	    tosRequest.setPrimaryAddress(lIP1);
	    tosRequest.setSecondaryAddress(lIP2);
	    tosRequest.setSystem(lSystem.getName());
	    tosRequest.setHost(config.getTosServerId());
	    tosRequest.setId(lLoadSet.getId());
	    tosRequest.setRejectReason(reason);
	    tosRequest.setUser(user);
	    tosRequest.setCpuName(lCpu.getCpuName());

	    tosRequest.setCommand(pOperation.getScript() + pLoad.getFallbackLoadSetName().toUpperCase());

	    // ZTPFM-2152 PutLevel Upgrade
	    String loadStatus = getFallBackLoadStatus(pOperation);
	    if (loadStatus != null && loadStatus.equals(lLoadSet.getStatus())) {
		PutLevel putLevel = putLevelDAO.getPutLevel(lSystem, Constants.PUTLevelOptions.PROD_CO_EXIST.name());
		if (putLevel != null) {
		    tosRequest.setCoexist(Boolean.TRUE);
		    lLoadSet.setPrimaryTosRequest("INPROGRESS");
		    lLoadSet.setSecondaryTosRequest("INPROGRESS");
		} else {
		    tosRequest.setCoexist(Boolean.FALSE);
		    lLoadSet.setPrimaryTosRequest("INPROGRESS");
		    lLoadSet.setSecondaryTosRequest("NONE");
		}
		productionLoadsDAO.update(user, lLoadSet);
	    }

	    if (lOldLoadSetStatus != null) {
		tosRequest.setOldStatus(lOldLoadSetStatus);
	    } else {
		tosRequest.setOldStatus(Constants.LOAD_SET_STATUS.DELETED.name());
	    }
	    sendMessage(tosRequest);
	    return true;
	} catch (WorkflowException e) {
	    throw e;
	} catch (Exception e) {
	    LOG.error("Error in sending TOS Command,", e);
	}
	return false;
    }

    public Boolean requestIP(User user, SystemLoad systemLoad) {
	SystemCpu lCpu;
	if (config.getTosSystemType().equals(Constants.TOSEnvironment.PRODUCTION.name())) {
	    lCpu = systemCpuDAO.find(systemLoad.getSystemId().getDefaultProdCpu());
	} else {
	    lCpu = systemCpuDAO.find(systemLoad.getSystemId().getDefaultNativeCpu());
	}
	if (lCpu == null) {
	    throw new WorkflowException("Default CPU not found");
	}
	return requestIP(user, systemLoad, lCpu);
    }

    public Boolean requestPreProdIP(User user, SystemLoad systemLoad, SystemCpu lCpu) {
	return requestIP(user, systemLoad, lCpu);
    }

    private Boolean requestIP(User user, SystemLoad systemLoad, SystemCpu lCpu) {
	try {
	    TOSRequest tosRequest = new TOSRequest();
	    String lIP1 = lCpu.getPrimaryIpAddress();
	    String lIP2 = lCpu.getSecondaryIpAddress();
	    tosRequest.setPrimaryAddress(lIP1);
	    tosRequest.setSecondaryAddress(lIP2);
	    tosRequest.setSystem(systemLoad.getSystemId().getName());
	    tosRequest.setHost(config.getTosServerId());
	    tosRequest.setId(systemLoad.getId());
	    tosRequest.setUser(user);
	    tosRequest.setCpuName(lCpu.getCpuName());

	    tosRequest.setCommand(Constants.LoadSetCommands.GETIP.getScript());
	    tosIpMap.remove(systemLoad.getId());
	    requestIP(tosRequest);
	    return true;
	} catch (WorkflowException e) {
	    throw e;
	} catch (Exception e) {
	    LOG.error("Error in sending TOS Command,", e);
	}
	return false;
    }

    public String getIP(Integer id) {
	String ipAddress = "";
	try {
	    int lCount = 0;
	    do {
		Thread.sleep(500L);
		ipAddress = tosIpMap.remove(id);
		lCount++;
	    } while (ipAddress == null && lCount < 120);
	    if (ipAddress == null) {
		ipAddress = "";
	    }
	} catch (Exception ex) {
	    LOG.error("Exception on wait", ex);
	    ipAddress = "";
	}
	return ipAddress;
    }

    public Boolean doPreTOSOperation(User user, Constants.LoadSetCommands pOperation, PreProductionLoads lLoadSet, String lOldLoadSetStatus, SystemLoad pLoad, boolean isReject) {
	try {
	    TOSRequest tosRequest = new TOSRequest();
	    System lSystem = pLoad.getSystemId();
	    SystemCpu lCpu = lLoadSet.getCpuId();
	    if (lCpu == null) {
		throw new WorkflowException("Default CPU not found");
	    }
	    String lIP1 = lCpu.getPrimaryIpAddress();
	    String lIP2 = lCpu.getSecondaryIpAddress();
	    tosRequest.setPrimaryAddress(lIP1);
	    tosRequest.setSecondaryAddress(lIP2);
	    tosRequest.setSystem(lSystem.getName());
	    if (isReject) {
		tosRequest.setRejectReason("CASCADE");
	    }
	    tosRequest.setHost(config.getTosServerId());
	    tosRequest.setId(lLoadSet.getId());
	    tosRequest.setUser(user);
	    tosRequest.setCommand(pOperation.getScript() + pLoad.getLoadSetName().toUpperCase());
	    tosRequest.setCpuName(lCpu.getCpuName());

	    String loadStatus = getLoadStatus(pOperation);
	    if (loadStatus != null && loadStatus.equals(lLoadSet.getStatus())) {
		PutLevel putLevel = putLevelDAO.getPutLevel(lSystem, Constants.PUTLevelOptions.PRE_PROD_CO_EXIST.name());
		if (putLevel != null) {
		    tosRequest.setCoexist(Boolean.TRUE);
		    lLoadSet.setPrimaryTosRequest("INPROGRESS");
		    lLoadSet.setSecondaryTosRequest("INPROGRESS");
		} else {
		    tosRequest.setCoexist(Boolean.FALSE);
		    lLoadSet.setPrimaryTosRequest("INPROGRESS");
		    lLoadSet.setSecondaryTosRequest("NONE");
		}
		preProductionLoadsDAO.update(user, lLoadSet);
	    }

	    if (lOldLoadSetStatus != null) {
		tosRequest.setOldStatus(lOldLoadSetStatus);
	    } else {
		tosRequest.setOldStatus(Constants.LOAD_SET_STATUS.DELETED.name());
	    }
	    sendMessage(tosRequest);
	    return true;
	} catch (WorkflowException e) {
	    throw e;
	} catch (Exception e) {
	    LOG.error("Error in sending TOS Command,", e);
	}
	return false;
    }

    public Boolean doFallbackPreProdTOSOperation(User user, Constants.LoadSetCommands pOperation, PreProductionLoads lLoadSet, String lOldLoadSetStatus, SystemLoad pLoad, String reason) {
	try {
	    TOSRequest tosRequest = new TOSRequest();
	    System lSystem = pLoad.getSystemId();

	    SystemCpu lCpu = lLoadSet.getCpuId();
	    if (lCpu == null) {
		throw new WorkflowException("Default CPU not found");
	    }
	    String lIP1 = lCpu.getPrimaryIpAddress();
	    String lIP2 = lCpu.getSecondaryIpAddress();
	    tosRequest.setPrimaryAddress(lIP1);
	    tosRequest.setSecondaryAddress(lIP2);
	    tosRequest.setSystem(lSystem.getName());
	    tosRequest.setHost(config.getTosServerId());
	    tosRequest.setId(lLoadSet.getId());
	    tosRequest.setRejectReason(reason);
	    tosRequest.setUser(user);
	    tosRequest.setCpuName(lCpu.getCpuName());

	    tosRequest.setCommand(pOperation.getScript() + pLoad.getFallbackLoadSetName().toUpperCase());

	    // ZTPFM-2152 PutLevel Upgrade
	    String loadStatus = getFallBackLoadStatus(pOperation);

	    if (loadStatus != null && loadStatus.equals(lLoadSet.getStatus())) {
		PutLevel putLevel = putLevelDAO.getPutLevel(lSystem, Constants.PUTLevelOptions.PRE_PROD_CO_EXIST.name());
		if (putLevel != null) {
		    tosRequest.setCoexist(Boolean.TRUE);
		    lLoadSet.setPrimaryTosRequest("INPROGRESS");
		    lLoadSet.setSecondaryTosRequest("INPROGRESS");
		} else {
		    tosRequest.setCoexist(Boolean.FALSE);
		    lLoadSet.setPrimaryTosRequest("INPROGRESS");
		    lLoadSet.setSecondaryTosRequest("NONE");
		}
		preProductionLoadsDAO.update(user, lLoadSet);
	    }

	    if (lOldLoadSetStatus != null) {
		tosRequest.setOldStatus(lOldLoadSetStatus);
	    } else {
		tosRequest.setOldStatus(Constants.LOAD_SET_STATUS.DELETED.name());
	    }
	    sendMessage(tosRequest);
	    return true;
	} catch (WorkflowException e) {
	    throw e;
	} catch (Exception e) {
	    LOG.error("Error in sending TOS Command,", e);
	}
	return false;
    }
}
