/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos;

import static com.tsi.workflow.utils.Constants.lTOSReturnSubStatusList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.workflow.TOSConfig;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.PreProductionLoadsDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.tos.model.TOSResult;
import com.tsi.workflow.utils.Constants;
import javax.jms.Message;
import javax.jms.TextMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
@Component
public class TOSMessageReceiver {

    private static final Logger LOG = Logger.getLogger(TOSMessageReceiver.class.getName());
    @Autowired
    PreProdMessageProcessor preProdMessageProcessor;

    @Autowired
    ProdMessageProcessor prodMessageProcessor;

    @Autowired
    TOSConfig tosConfig;

    @Autowired
    ProductionLoadsDAO productionLoadsDAO;

    @Autowired
    PreProductionLoadsDAO preProductionLoadsDAO;

    @Autowired
    ActivityLogDAO activityLogDAO;

    @JmsListener(destination = Constants.TOS_TOPIC_TOS_APP, containerFactory = "jmsListenerContainerFactory")
    @Transactional
    public void receiveMessage(final Message message) {
	if (!(message instanceof TextMessage)) {
	    return;
	}
	try {
	    TextMessage lMessage = (TextMessage) message;
	    LOG.info("<-- TOS Client : " + lMessage.getText());
	    TOSResult result = new ObjectMapper().readValue(lMessage.getText(), TOSResult.class);
	    if (!result.getHost().equals(tosConfig.getTosServerId()) && Constants.TOS_SECOND_SERVER_UP) {
		return;
	    }
	    LOG.info("TOS -> ID: " + result.getId() + " Command: " + result.getCommand() + " Old Status: " + result.getOldStatus() + " Loadset: " + result.getLoadset() + " Last: " + result.isLast() + " RC: " + result.getReturnValue());
	    PreProductionLoads lPreProdLoad = this.getPreProductionLoads(result.getId(), result.getLoadset());
	    LOG.info(lPreProdLoad);
	    if (lPreProdLoad != null) {
		LOG.info("DB  -> PRE PROD ID: " + lPreProdLoad.getId() + " Status: " + lPreProdLoad.getStatus() + " Action: " + lPreProdLoad.getLastActionStatus());

		if (result.isIsPrimary() && result.isCoexist()) {
		    if (result.isLast() && result.getReturnValue() == 0) {
			lPreProdLoad.setPrimaryTosRequest("SUCCESS");
		    } else if (result.isLast() && result.getReturnValue() > 4) {
			lPreProdLoad.setPrimaryTosRequest("FAILED");
		    } else {
			lPreProdLoad.setPrimaryTosRequest("INPROGRESS");
		    }
		} else if (!result.isIsPrimary() && result.isCoexist()) {
		    if (result.isLast() && result.getReturnValue() == 0) {
			lPreProdLoad.setSecondaryTosRequest("SUCCESS");
		    } else if (result.isLast() && result.getReturnValue() > 4) {
			lPreProdLoad.setSecondaryTosRequest("FAILED");
		    } else {
			lPreProdLoad.setSecondaryTosRequest("INPROGRESS");
		    }
		} else if (!result.isCoexist()) {
		    if (result.isLast() && result.getReturnValue() == 0) {
			lPreProdLoad.setPrimaryTosRequest("SUCCESS");
		    } else if (result.isLast() && result.getReturnValue() > 4) {
			lPreProdLoad.setPrimaryTosRequest("FAILED");
		    } else {
			lPreProdLoad.setPrimaryTosRequest("INPROGRESS");
		    }
		    lPreProdLoad.setSecondaryTosRequest("NONE");
		}
		preProductionLoadsDAO.update(result.getUser(), lPreProdLoad);

		/*
		 * // Activity Log PreTOSActivityMessage lActivityMessage = new
		 * PreTOSActivityMessage(lPreProdLoad.getPlanId(), null, lPreProdLoad);
		 * lActivityMessage.setLoadActionStatus(lPreProdLoad.getStatus());
		 * lActivityMessage.settOSResult(result); activityLogDAO.save(result.getUser(),
		 * lActivityMessage);
		 */
		if (lTOSReturnSubStatusList.contains(lPreProdLoad.getPrimaryTosRequest() + "|" + lPreProdLoad.getPrimaryTosRequest())) {
		    int ret = preProdMessageProcessor.processPreProductionTOSMessage(result);
		    lPreProdLoad = this.getPreProductionLoads(result.getId(), result.getLoadset());

		    if (lPreProdLoad != null) {
			LOG.info("UPDATED  -> PRE PROD ID: " + lPreProdLoad.getId() + " Status: " + lPreProdLoad.getStatus() + " Action: " + lPreProdLoad.getLastActionStatus());
		    }

		    if (ret == 0) {
			if (result.getRejectReason() != null && result.getRejectReason().equals("CASCADE")) {

			} else {
			    preProdMessageProcessor.updateIfQATSSDeploy(result.getUser(), result.getId());
			}
		    } else if (ret == 1) {
			// Response of FALLBACK LOAD
			if (result.getRejectReason() != null && result.getRejectReason().equals("CASCADE")) {
			    // FALLBACK Activate - During normal Deactivatation of Loadset
			    preProdMessageProcessor.initFallBackActivate(result.getUser(), result.getId());
			} else {
			    preProdMessageProcessor.initFallBackActivate(result.getUser(), result.getId(), Boolean.FALSE);
			}
		    } else if (ret == 2) {
			// Response of DEACTIVATE with Load not exist
			if (result.getRejectReason() != null && result.getRejectReason().equals("CASCADE")) {
			    preProdMessageProcessor.doFtpAndFallbackLoad(result.getUser(), result.getId(), true);
			} else {
			    preProdMessageProcessor.updateIfQATSSDeploy(result.getUser(), result.getId());
			    preProdMessageProcessor.doFtpAndFallbackLoad(result.getUser(), result.getId(), false);
			}
		    } else if (ret == 3) {
			// Response of DEACTIVATE
			preProdMessageProcessor.initDelete(result.getUser(), result.getId());
		    }
		}
		return;
	    }

	    ProductionLoads lProdLoad = this.getProductionLoads(result.getId(), result.getLoadset());
	    if (lProdLoad != null) {
		LOG.info("DB  -> PROD ID: " + lProdLoad.getId() + " Status: " + lProdLoad.getStatus() + " Action: " + lProdLoad.getLastActionStatus());

		/*
		 * // Activity Log TOSActivityMessage lActivityMessage = new
		 * TOSActivityMessage(lProdLoad.getPlanId(), null, lProdLoad);
		 * lActivityMessage.settOSResult(result); activityLogDAO.save(result.getUser(),
		 * lActivityMessage);
		 */

		if (result.isIsPrimary() && result.isCoexist()) {
		    if (result.isLast() && result.getReturnValue() == 0) {
			lProdLoad.setPrimaryTosRequest("SUCCESS");
		    } else if (result.isLast() && result.getReturnValue() > 4) {
			lProdLoad.setPrimaryTosRequest("FAILED");
		    } else {
			lProdLoad.setPrimaryTosRequest("INPROGRESS");
		    }
		} else if (!result.isIsPrimary() && result.isCoexist()) {
		    if (result.isLast() && result.getReturnValue() == 0) {
			lProdLoad.setSecondaryTosRequest("SUCCESS");
		    } else if (result.isLast() && result.getReturnValue() > 4) {
			lProdLoad.setSecondaryTosRequest("FAILED");
		    } else {
			lProdLoad.setSecondaryTosRequest("INPROGRESS");
		    }
		} else if (!result.isCoexist()) {
		    if (result.isLast() && result.getReturnValue() == 0) {
			lProdLoad.setPrimaryTosRequest("SUCCESS");
		    } else if (result.isLast() && result.getReturnValue() > 4) {
			lProdLoad.setPrimaryTosRequest("FAILED");
		    } else {
			lProdLoad.setPrimaryTosRequest("INPROGRESS");
		    }
		    lProdLoad.setSecondaryTosRequest("NONE");
		}
		productionLoadsDAO.update(result.getUser(), lProdLoad);

		if (lTOSReturnSubStatusList.contains(lProdLoad.getPrimaryTosRequest() + "|" + lProdLoad.getPrimaryTosRequest())) {
		    int ret = prodMessageProcessor.processProductionTOSMessage(result);
		    lProdLoad = this.getProductionLoads(result.getId(), result.getLoadset());

		    if (lProdLoad != null) {
			LOG.info("UPDATED  -> PROD ID: " + lProdLoad.getId() + " Status: " + lProdLoad.getStatus() + " Action: " + lProdLoad.getLastActionStatus());
		    }

		    if (ret == 1) {
			prodMessageProcessor.checkAllDeleted(result.getUser(), result.getId());
		    } else if (ret == 2) {
			prodMessageProcessor.checkFallbackDeleted(result.getUser(), result.getId());
		    } else if (ret == 3) {
			prodMessageProcessor.checkAllAccepted(result.getUser(), result.getId(), result.getLoadset());
		    } else if (ret == 4) {
			prodMessageProcessor.checkAllFallbackAccepted(result.getUser(), result.getId(), result.getLoadset(), result.getRejectReason());
		    } else if (ret == 5) {
			prodMessageProcessor.checkAllActivated(result.getUser(), result.getId());
		    } else if (ret == 6) {
			prodMessageProcessor.checkAllFallbackActivated(result.getUser(), result.getId());
		    } else if (ret == 7) {
			prodMessageProcessor.checkAllDeActivated(result.getUser(), result.getId());
		    }
		}
		return;
	    }
	} catch (Exception ex) {
	    LOG.error("Error in Receiving Message", ex);
	}
    }

    public PreProductionLoads getPreProductionLoads(int id, String loadset) {
	return preProductionLoadsDAO.findByLoadset(id, loadset);
    }

    public ProductionLoads getProductionLoads(int id, String loadset) {
	return productionLoadsDAO.findByLoadset(id, loadset);
    }

}
