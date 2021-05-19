package com.tsi.workflow.schedular;

import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dao.SystemLoadActions;
import com.tsi.workflow.beans.dao.Vpars;
import com.tsi.workflow.beans.ui.ImpPlanAndUserDetail;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.dao.VparsDAO;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.BUILD_TYPE;
import com.tsi.workflow.utils.Constants.LOAD_SET_STATUS;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.management.timer.Timer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DSLFilePopulate {
    @Autowired
    @Qualifier("dslFileGenerationList")
    ConcurrentLinkedQueue<ImpPlanAndUserDetail> dslFileGenerationList;

    @Autowired
    @Qualifier("qaBypassedLoadsetList")
    ConcurrentLinkedQueue<ImpPlanAndUserDetail> qaBypassedLoadsetList;

    @Autowired
    SystemLoadDAO systemLoadDAO;

    @Autowired
    VparsDAO vparsDAO;
    @Autowired
    SSHClientUtils sSHClientUtils;

    @Autowired
    ActivityLogDAO activityLogDAO;

    @Autowired
    MailMessageFactory mailMessageFactory;

    @Autowired
    BuildDAO buildDAO;

    @Autowired
    WFConfig wFConfig;

    @Autowired
    SystemLoadActionsDAO systemLoadActionsDAO;

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public VparsDAO getVparsDAO() {
	return vparsDAO;
    }

    public BuildDAO getBuildDAO() {
	return buildDAO;
    }

    public WFConfig getWFConfig() {
	return wFConfig;
    }

    public SystemLoadActionsDAO getSystemLoadActionsDAO() {
	return systemLoadActionsDAO;
    }

    private static final Logger LOG = Logger.getLogger(DSLFilePopulate.class.getName());

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND)
    @Transactional
    public void doDSlFilePopulate() {
	for (ImpPlanAndUserDetail planAndUserDetail : dslFileGenerationList) {
	    try {
		JSONResponse lResponse = new JSONResponse();
		ImpPlan lPlan = null;
		User pUser = planAndUserDetail.getUser();
		List<SystemLoad> lSystemLoadList = systemLoadDAO.findByImpPlan(planAndUserDetail.getImpPlan());
		for (SystemLoad lSystemLoad : lSystemLoadList) {
		    if (Constants.getDSLLoadsetSystem().contains(lSystemLoad.getSystemId().getName())) {

			// Add WSP system information
			planAndUserDetail.setSystemLoad(lSystemLoad);

			lPlan = lSystemLoad.getPlanId();
			System lSystem = lSystemLoad.getSystemId();

			// String command = Constants.SystemScripts.TPF_MAINTENANCE_CHECK.getScript();
			// lResponse = getsSHClientUtils().executeCommand(lSystem, command);
			//
			// if (!lResponse.getStatus()) {
			// LOG.error("Nightly Maintenance is in progress " + lSystem.getName());
			// continue;
			// }

			Build lBuild = getBuildDAO().findLastSuccessfulBuild(lPlan.getId(), lSystem.getId(), BUILD_TYPE.STG_LOAD);

			if (lBuild == null) {
			    LOG.error("Plan " + lPlan.getId() + "dont have successful Build");
			    continue;
			}

			// DSL File populate in LTQA
			List<System> systemIds = new ArrayList<>();
			systemIds.add(lSystem);
			List<Vpars> vparsList = getVparsDAO().findBySystem(systemIds, Boolean.TRUE, Constants.VPARSEnvironment.QA_FUCTIONAL);

			if (vparsList == null || vparsList.size() <= 0) {
			    LOG.error("Vpars is not available during DSL file generation process.");
			    continue;
			}

			// Set initial Status Systyem Load
			SystemLoadActions lLoad = saveSystemLoadAction(lPlan, lSystemLoad, lSystem, vparsList);
			getSystemLoadActionsDAO().save(pUser, lLoad);

			// String dslFileUpdateCommand =
			// Constants.SystemScripts.DSL_FILE_UPDATE.getScript() + " " +
			// lPlan.getImplementationList().get(0).getId().toLowerCase() + " " +
			// lSystem.getName().toLowerCase() + " " +
			// vparsList.get(0).getName().toLowerCase() + " " + lSystemLoad.getLoadSetName()
			// + " " + Constants.REX_DATEFORMAT.get().format(lSystemLoad.getLoadDateTime());
			// lResponse = getsSHClientUtils().executeCommand(lSystem,
			// dslFileUpdateCommand);
			//
			// // Activity log update based on the status
			// DSLFilePopulateActivityMsg lMessage = new DSLFilePopulateActivityMsg(lPlan,
			// null, lSystemLoad);
			// lMessage.setStatus(lResponse.getStatus());
			// activityLogDAO.save(planAndUserDetail.getUser(), lMessage);
			//
			// // If File population failed send a mail to Lead and developers
			// if (!lResponse.getStatus()) {
			// DSLFilePopulateFailureMail lFailureMail = (DSLFilePopulateFailureMail)
			// mailMessageFactory.getTemplate(DSLFilePopulateFailureMail.class);
			// lFailureMail.setPlanId(lPlan.getId());
			// lFailureMail.setLoadSetName(lSystemLoad.getLoadSetName());
			// mailMessageFactory.push(lFailureMail);
			// } else {

			qaBypassedLoadsetList.add(planAndUserDetail);
		    }
		}
	    } catch (Exception ex) {
		LOG.error("Error in DSL File generation job monitor process", ex);
	    } finally {
		dslFileGenerationList.remove(planAndUserDetail);
	    }
	}
    }

    private SystemLoadActions saveSystemLoadAction(ImpPlan lPlan, SystemLoad lSystemLoad, System lSystem, List<Vpars> vparsList) {
	SystemLoadActions lLoadAction = new SystemLoadActions();
	lLoadAction.setPlanId(lPlan);
	lLoadAction.setIsAutoDeploy(true);
	lLoadAction.setIsVparActivated(Boolean.FALSE);
	lLoadAction.setLastActionStatus("FAILED");
	lLoadAction.setDslUpdate("N");
	lLoadAction.setSystemId(lSystem);
	lLoadAction.setVparId(vparsList.get(0));
	lLoadAction.setSystemLoadId(lSystemLoad);
	lLoadAction.setStatus(LOAD_SET_STATUS.DELETED.name());
	return lLoadAction;
    }

}
