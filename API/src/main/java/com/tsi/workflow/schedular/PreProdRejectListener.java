/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import com.tsi.workflow.User;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.ui.RejectPlans;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.PreProductionLoadsDAO;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.mail.CommonMail;
import com.tsi.workflow.tos.TOSHelper;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import javax.management.timer.Timer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author prabhu.prabhakaran
 */
@Component
public class PreProdRejectListener {

    private static final Logger LOG = Logger.getLogger(PreProdRejectListener.class.getName());

    @Autowired
    @Qualifier("preProdRejectPlans")
    ConcurrentLinkedQueue<RejectPlans> lRejectPlans;

    @Autowired
    PreProductionLoadsDAO preProductionLoadsDAO;

    @Autowired
    TOSHelper tOSHelper;

    @Autowired
    ImpPlanDAO impPlanDAO;

    @Autowired
    MailMessageFactory mailMessageFactory;

    @Autowired
    RejectHelper rejectHelper;

    private Map<String, Date> lPlanVsTriggerDate = new HashMap();
    private Set<String> lMailedPlan = new HashSet();

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND)
    @Transactional
    public void doReject() {
	try {
	    List<RejectPlans> lRemovalPlans = new ArrayList();
	    for (RejectPlans lRejectPlan : lRejectPlans) {
		User pUser = lRejectPlan.getUser();
		List<String> lRemovePlan = new ArrayList();
		Boolean isReject = lRejectPlan.getIsReject();
		for (Map.Entry<String, Date> lPlan : lRejectPlan.getDependentPlanIds().entrySet()) {
		    ImpPlan plan = impPlanDAO.find(lPlan.getKey());
		    LOG.info("Deleting Pre Prod Loads for the plan " + plan.getId() + " Load date time " + lPlan.getValue());

		    List<PreProductionLoads> lLoadSets = preProductionLoadsDAO.findByPlanId(plan);
		    if (lLoadSets == null || lLoadSets.isEmpty()) {
			if (isReject && !lRejectPlan.getIsFallback()) {
			    try {
				LOG.info("Deleting Staging workspace for plan " + plan.getId());
				rejectHelper.deleteStagingWorkspace(pUser, plan);
				rejectHelper.deleteBuilds(pUser, plan);
			    } catch (Exception ex) {
				LOG.error("Error in Deleting Staging Workspace on Delete", ex);
			    }
			}

			lPlanVsTriggerDate.remove(plan.getId());
			lMailedPlan.remove(plan.getId());
			lRemovePlan.add(plan.getId());
		    } else {
			if (lPlanVsTriggerDate.containsKey(plan.getId())) {
			    Long lTimeDiff = (new Date()).getTime() - lPlanVsTriggerDate.get(plan.getId()).getTime();
			    Long lMinDiff = TimeUnit.MINUTES.convert(lTimeDiff, TimeUnit.MINUTES);

			    if (lMinDiff > Long.valueOf(20 * Timer.ONE_MINUTE)) {
				if (!lMailedPlan.contains(plan.getId())) {
				    LOG.info(plan.getId() + "Plan is in process for more than expected time, kindly review it");
				    lMailedPlan.add(plan.getId());

				    // Send Email to development Team
				    CommonMail lActionMail = (CommonMail) mailMessageFactory.getTemplate(CommonMail.class);
				    String lSubject = "Action Required: Issue in Deactivate/Delete the Loadset for Plan - " + plan.getId();
				    StringBuilder lMessage = new StringBuilder();
				    lMessage.append("Pan - ").append(plan.getId()).append(" was rejected and have issue in deactivate/Delete the loadset from TSS Systems.").append("It is been in process for more than expected time. Kindly take action as eary as possible");
				    if (lRejectPlan.getDependentPlanIds().size() > 1) {
					lMessage.append("<br> List of plans which are waiting for completion of this plan are ").append(lRejectPlan.getDependentPlanIds().toString());
				    }
				    lActionMail.addToDevOpsCentre(true);
				    lActionMail.setMessage(lMessage.toString());
				    lActionMail.setSubject(lSubject);
				    mailMessageFactory.push(lActionMail);
				}
			    }
			    break; // Stop processing the other plan
			} else {
			    List<Object[]> lDependentPlan = impPlanDAO.getPostSegmentRelatedPlans(plan.getId(), Boolean.FALSE);
			    List<String> lChildPlans = new ArrayList();
			    for (Object[] lPlanInfo : lDependentPlan) {
				String lChildPlanStatus = lPlanInfo[1].toString();
				String lChildPlanId = lPlanInfo[2].toString();
				List<PreProductionLoads> lPreProdLoads = preProductionLoadsDAO.findByPlanId(lChildPlanId);
				if (!Constants.PlanStatus.getOnlineAndAbove().containsKey(lChildPlanStatus) && !lPreProdLoads.isEmpty()) {
				    lChildPlans.add(lChildPlanId);
				}
			    }

			    if (!lChildPlans.isEmpty()) {
				LOG.info("List of Child Plans are not yet removed,  " + String.join(",", lChildPlans));
				break;
			    }

			    for (PreProductionLoads lLoadSet : lLoadSets) {
				if (lLoadSet.getLastActionStatus().equalsIgnoreCase("INPROGRESS")) {
				    continue;
				}

				if (lLoadSet.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
				    LOG.info("Load in Activated Status, Going to Deactivate " + plan.getId());
				    lLoadSet.setStatus(Constants.LOAD_SET_STATUS.DEACTIVATED.name());
				    Boolean lPreTOSOperation = tOSHelper.doPreTOSOperation(pUser, Constants.LoadSetCommands.DEACTIVATE, lLoadSet, Constants.LOAD_SET_STATUS.ACTIVATED.name(), lLoadSet.getSystemLoadId(), isReject);
				    setStatus(lPreTOSOperation, lLoadSet, Constants.LOAD_SET_STATUS.ACTIVATED.name());
				    if (!lPreTOSOperation) {
					lLoadSet.setActive("N");
				    }
				} else if (lLoadSet.getStatus().equals(Constants.LOAD_SET_STATUS.DEACTIVATED.name())) {
				    LOG.info("Load in DeActivated Status, Going to Delete " + plan.getId());
				    lLoadSet.setStatus(Constants.LOAD_SET_STATUS.DELETED.name());
				    Boolean lPreTOSOperation = tOSHelper.doPreTOSOperation(pUser, Constants.LoadSetCommands.DELETE, lLoadSet, Constants.LOAD_SET_STATUS.DEACTIVATED.name(), lLoadSet.getSystemLoadId(), isReject);
				    setStatus(lPreTOSOperation, lLoadSet, Constants.LOAD_SET_STATUS.DEACTIVATED.name());
				    if (!lPreTOSOperation) {
					lLoadSet.setActive("N");
				    }
				}
				lPlanVsTriggerDate.put(plan.getId(), new Date());
			    }
			    break; // Stop processing the other plan
			}
		    }
		}
		if (!lRemovePlan.isEmpty()) {
		    lRemovePlan.stream().forEach(lPlan -> {
			ImpPlan lPlanInfo = impPlanDAO.find(lPlan);
			lPlanInfo.setInprogressStatus(Constants.PlanInProgressStatus.NONE.name());
			impPlanDAO.update(pUser, lPlanInfo);

			lRejectPlan.getDependentPlanIds().remove(lPlan);
		    });
		}
		if (lRejectPlan.getDependentPlanIds().isEmpty()) {
		    lRemovalPlans.add(lRejectPlan);
		}
	    }
	    lRemovalPlans.stream().forEach(t -> lRejectPlans.remove(t));
	} catch (Exception ex) {
	    LOG.error("Error Occurs in Pre Production Rejection Listener", ex);
	}
    }

    private void setStatus(Boolean lResult, PreProductionLoads lLoad, String lOldStatus) {
	if (!lResult) {
	    lLoad.setLastActionStatus("FAILED");
	    if (lOldStatus != null) {
		lLoad.setStatus(lOldStatus);
	    } else {
		lLoad.setStatus(Constants.LOAD_SET_STATUS.DELETED.name());
	    }
	} else {
	    lLoad.setLastActionStatus("INPROGRESS");
	}
    }

    public Boolean removePlan(String planId) {
	lRejectPlans.stream().filter((lRejectPlan) -> (lRejectPlan.getDependentPlanIds().containsKey(planId))).forEachOrdered((lRejectPlan) -> {
	    lRejectPlan.getDependentPlanIds().remove(planId);
	});
	return Boolean.TRUE;
    }
}
