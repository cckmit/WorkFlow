package com.tsi.workflow.schedular;

import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.activity.ProdFTPActivityMessage;
import com.tsi.workflow.activity.YodaResponseActivityMessage;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dao.SystemLoadActions;
import com.tsi.workflow.beans.dao.Vpars;
import com.tsi.workflow.beans.ui.ImpPlanAndUserDetail;
import com.tsi.workflow.beans.ui.YodaResult;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.dao.VparsDAO;
import com.tsi.workflow.dao.external.TestSystemLoadDAO;
import com.tsi.workflow.helper.FTPHelper;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.BUILD_TYPE;
import com.tsi.workflow.utils.Constants.LOAD_SET_STATUS;
import com.tsi.workflow.utils.JSONResponse;
import java.util.ArrayList;
import java.util.Date;
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
public class LoadsetAutoGeneration {

    @Autowired
    @Qualifier("qaBypassedLoadsetList")
    ConcurrentLinkedQueue<ImpPlanAndUserDetail> qaBypassedLoadsetList;

    @Autowired
    TestSystemLoadDAO testSystemLoadDAO;

    @Autowired
    SystemLoadDAO systemLoadDAO;

    @Autowired
    ActivityLogDAO activityLogDAO;

    @Autowired
    SystemLoadActionsDAO systemLoadActionsDAO;

    @Autowired
    BuildDAO buildDAO;

    @Autowired
    FTPHelper fTPHelper;

    @Autowired
    VparsDAO vparsDAO;

    @Autowired
    WFConfig wFConfig;

    @Autowired
    ImpPlanDAO impPlanDAO;

    public TestSystemLoadDAO getTestSystemLoadDAO() {
	return testSystemLoadDAO;
    }

    public SystemLoadActionsDAO getSystemLoadActionsDAO() {
	return systemLoadActionsDAO;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public BuildDAO getBuildDAO() {
	return buildDAO;
    }

    public FTPHelper getFTPHelper() {
	return fTPHelper;
    }

    public VparsDAO getVparsDAO() {
	return vparsDAO;
    }

    public WFConfig getWFConfig() {
	return wFConfig;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    private static final Logger LOG = Logger.getLogger(LoadsetAutoGeneration.class.getName());

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND)
    @Transactional
    public void doAutoLoadsetGen() {
	for (ImpPlanAndUserDetail planAndUserDetail : qaBypassedLoadsetList) {

	    try {
		ImpPlan lPlan = null;
		User pUser = planAndUserDetail.getUser();
		SystemLoad lSystemLoad = planAndUserDetail.getSystemLoad();

		if (Constants.getDSLLoadsetSystem().contains(lSystemLoad.getSystemId().getName())) {
		    lPlan = lSystemLoad.getPlanId();

		    System lSystem = lSystemLoad.getSystemId();
		    Build lBuild = getBuildDAO().findLastSuccessfulBuild(lPlan.getId(), lSystem.getId(), BUILD_TYPE.STG_LOAD);
		    boolean ftpFlag = true;
		    List<System> systemIds = new ArrayList<>();
		    systemIds.add(lSystem);
		    List<Vpars> vparsList = getVparsDAO().findBySystem(systemIds, Boolean.TRUE, Constants.VPARSEnvironment.QA_FUCTIONAL);

		    if (vparsList == null || vparsList.isEmpty()) {
			LOG.error("Vpars is not available while auto generating Loadset.");
			continue;
		    }
		    // Set initial Status Systyem Load
		    SystemLoadActions lLoad = getSystemLoadActionsDAO().findByPlanAndVpars(lPlan.getId(), vparsList.get(0).getId(), true);

		    if (lLoad == null) {
			continue;
		    }

		    // FTP
		    List<YodaResult> list = getTestSystemLoadDAO().getVparsIP(vparsList.get(0).getName());
		    LOG.info("Yoda list: " + list);
		    String ipAddress = "";
		    String vparName = vparsList.get(0).getName();
		    if (list != null && list.size() > 0 && list.get(0).getRc() == 0) {
			ipAddress = list.get(0).getIp();
			LOG.info("YODA Received IP Address: " + ipAddress);
			ipAddress = ipAddress.replaceAll("^0+", "").replaceAll("\\.0+", "\\.");
		    } else if (list.size() > 0) {
			LOG.error("YODA Execution RC: " + list.get(0).getRc() + " Message : " + list.get(0).getMessage());
		    }

		    if (list != null && list.size() > 0) {

			if (list.get(0).getMessage() != null) {
			    YodaResponseActivityMessage responseActivityMessage = new YodaResponseActivityMessage(lPlan, null);
			    responseActivityMessage.setlYodaResult(list.get(0));
			    responseActivityMessage.setSystemName(lLoad.getSystemId().getName());
			    responseActivityMessage.setVparName(lLoad.getVparId().getName());
			    responseActivityMessage.setYodaActivity(Constants.YodaActivtiyMessage.IPADDRESS);
			    getActivityLogDAO().save(pUser, responseActivityMessage);
			} else if (list.get(0).getMessage() == null && list.get(0).getRc() > 0) {
			    YodaResponseActivityMessage responseYodaActivityMessage = new YodaResponseActivityMessage(lPlan, null);
			    YodaResult lErrorMessage = new YodaResult();
			    lErrorMessage.setMessage(" YODA Execution Failed ");
			    lErrorMessage.setRc(20);
			    responseYodaActivityMessage.setlYodaResult(lErrorMessage);
			    responseYodaActivityMessage.setYodaActivity(Constants.YodaActivtiyMessage.LOADANDACTIVATE);
			    getActivityLogDAO().save(pUser, responseYodaActivityMessage);
			}
		    }

		    if (!ipAddress.isEmpty()) {

			ProdFTPActivityMessage lMessage = new ProdFTPActivityMessage(lBuild.getPlanId(), null, lLoad.getSystemLoadId());
			lMessage.setIpAddress(ipAddress);
			lMessage.setVparsName(vparName);
			lMessage.setFallback(false);
			getActivityLogDAO().save(pUser, lMessage);
			JSONResponse lSSHResponse = getFTPHelper().doFTP(pUser, lLoad.getSystemLoadId(), lBuild, ipAddress, false);

			if (!lSSHResponse.getStatus()) {
			    LOG.info(lPlan.getId() + ": FTP of " + lLoad.getSystemLoadId().getLoadSetName() + " to " + lLoad.getVparId().getName() + " has failed");

			    YodaResult lYodaResult = new YodaResult();
			    lYodaResult.setRc(8);
			    lYodaResult.setMessage(lSSHResponse.getErrorMessage());
			    YodaResponseActivityMessage responseActivityMessage = new YodaResponseActivityMessage(lPlan, null);
			    responseActivityMessage.setlYodaResult(lYodaResult);
			    responseActivityMessage.setYodaActivity(Constants.YodaActivtiyMessage.FTP);
			    getActivityLogDAO().save(pUser, responseActivityMessage);
			    lLoad.setStatus(LOAD_SET_STATUS.DELETED.name());
			    lLoad.setLastActionStatus("FAILED");
			    ftpFlag = false;
			} else {
			    LOG.info("FTP completed successfully for the plan id " + lBuild.getPlanId().getId());

			    lMessage.setStatus("Success");
			    getActivityLogDAO().save(pUser, lMessage);
			    getFTPHelper().getYodaLoadSetPath(pUser, lPlan, lSSHResponse);
			}
		    } else {
			ftpFlag = false;
			LOG.error("Unable to get IP Adress for VPAR " + lLoad.getVparId().getName());
			lLoad.setStatus(LOAD_SET_STATUS.DELETED.name());
			lLoad.setLastActionStatus("FAILED");
		    }

		    if (ftpFlag) {
			List<YodaResult> lList = getTestSystemLoadDAO().loadAndActivate(pUser, lLoad.getVparId().getName(), lLoad.getSystemLoadId().getLoadSetName());
			boolean lResult = false;
			if (lList != null && !lList.isEmpty()) {
			    YodaResponseActivityMessage responseActivityMessage = new YodaResponseActivityMessage(lPlan, null);
			    responseActivityMessage.setlYodaResult(lList.get(0));
			    responseActivityMessage.setYodaActivity(Constants.YodaActivtiyMessage.LOADANDACTIVATE);
			    getActivityLogDAO().save(pUser, responseActivityMessage);
			    lResult = lList.get(0).getRc() == 0;
			}

			if (lResult) {
			    lLoad.setTestStatus("");
			    lLoad.setActive("Y");
			    // ZTPFM-1497 Code changes done to get dsl flag from system load action table
			    lLoad.setDslUpdate("Y");
			    lLoad.setLastActionStatus("SUCCESS");
			    lLoad.setStatus(LOAD_SET_STATUS.ACTIVATED.name());
			    lLoad.setDeActivatedDateTime(null);
			    lLoad.setActivatedDateTime(new Date());
			    // LTQA Auto load plan status update
			    // List<SystemLoadActions> lSystemLoadActionsList =
			    // getSystemLoadActionsDAO().findByPlanId(lPlan);
			    // List<SystemLoad> lSystemLoadList = getSystemLoadDAO().findByImpPlan(lPlan);
			    // Set<Integer> lSystemLoadActionsIdCount = new HashSet<Integer>();
			    // Set<Integer> lSystemLoadIdCount = new HashSet<Integer>();
			    // for (SystemLoadActions load : lSystemLoadActionsList) {
			    // int id = load.getSystemId().getId();
			    // lSystemLoadActionsIdCount.add(id);
			    // }
			    // for (SystemLoad sysLoad : lSystemLoadList) {
			    // int id = sysLoad.getSystemId().getId();
			    // lSystemLoadIdCount.add(id);
			    // }
			    // List<SystemLoad> lActiveSystemLoads =
			    // getSystemLoadDAO().findPlanByQATestingStatus(lPlan.getId(),
			    // Arrays.asList("NONE",
			    // Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name()));
			    // if (lActiveSystemLoads != null && !lActiveSystemLoads.isEmpty() &&
			    // (lSystemLoadActionsIdCount.size() == lSystemLoadIdCount.size())) {
			    // lPlan.setPlanStatus(Constants.PlanStatus.DEPLOYED_IN_QA_FUNCTIONAL.name());
			    // }
			    // getImpPlanDAO().update(pUser, lPlan);

			} else {
			    lLoad.setTestStatus("FAIL");
			    lLoad.setActive("N");
			    lLoad.setLastActionStatus("FAILED");
			    lLoad.setStatus(LOAD_SET_STATUS.DELETED.name());
			}
		    }
		    getSystemLoadActionsDAO().update(pUser, lLoad);
		}
	    } catch (Exception ex) {
		LOG.error("Error in Loadset auto generation Job monitor process", ex);
	    } finally {
		qaBypassedLoadsetList.remove(planAndUserDetail);
	    }
	}
    }

}
