/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.TOSConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.activity.CommonActivityMessage;
import com.tsi.workflow.activity.DbcrValidationMessage;
import com.tsi.workflow.activity.MacroHeaderPlanFallBackActivity;
import com.tsi.workflow.activity.PlanStatusActivityMessage;
import com.tsi.workflow.activity.ProdFTPActivityMessage;
import com.tsi.workflow.activity.TOSActivityMessage;
import com.tsi.workflow.activity.TSDLoadSetDeactivateActivityMessage;
import com.tsi.workflow.base.BaseService;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.Dbcr;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.OnlineBuild;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.RFCDetails;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemCpu;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.AcceptForm;
import com.tsi.workflow.beans.ui.JobDetails;
import com.tsi.workflow.beans.ui.OnlineFeedbackQueueForm;
import com.tsi.workflow.beans.ui.ProdTOSForm;
import com.tsi.workflow.beans.ui.ProductionLoadsForm;
import com.tsi.workflow.beans.ui.TosActionQueue;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.DbcrDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.OnlineBuildDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.dao.RFCDetailsDAO;
import com.tsi.workflow.dao.SystemCpuDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.helper.DbcrHelper;
import com.tsi.workflow.helper.FTPHelper;
import com.tsi.workflow.helper.FallbackHelper;
import com.tsi.workflow.helper.PRNumberHelper;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.mail.ExceptionSourceContentionMail;
import com.tsi.workflow.mail.StatusChangeToDependentPlanMail;
import com.tsi.workflow.tos.TOSHelper;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.tsi.workflow.websocket.WSMessagePublisher;
import com.workflow.ssh.SSHClientUtils;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author
 */
@Service
public class TSDService extends BaseService {

    public static final Logger LOG = Logger.getLogger(TSDService.class.getName());

    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    JGitClientUtils jGitClientUtils;
    @Autowired
    GITConfig gITConfig;
    @Autowired
    TOSHelper tOSHelper;
    @Autowired
    RejectHelper rejectHelper;
    @Autowired
    SystemLoadActionsDAO systemLoadActionsDAO;
    @Autowired
    ProductionLoadsDAO productionLoadsDAO;
    @Autowired
    BuildDAO buildDAO;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    SystemCpuDAO systemCpuDAO;
    @Autowired
    SystemDAO systemDAO;
    @Autowired
    BPMClientUtils bPMClientUtils;
    @Autowired
    FTPHelper fTPHelper;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    TOSConfig tosConfig;
    @Autowired
    FallbackHelper fallbackHelper;
    @Autowired
    DbcrHelper dbcrHelper;
    @Autowired
    DbcrDAO dbcrDAO;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    @Qualifier("lPlanOnlineFallbackStatusMap")
    ConcurrentHashMap<String, String> lPlanOnlineFallbackStatusMap;
    @Autowired
    SSHClientUtils sSHClientUtils;
    @Autowired
    PRNumberHelper prStatusUpdateinNAS;
    @Autowired
    WSMessagePublisher wsserver;
    @Autowired
    ImplementationDAO implementationDAO;
    @Autowired
    PlanHelper planHelper;
    @Autowired
    OnlineBuildDAO onlineBuildDAO;
    @Autowired
    ConcurrentHashMap<String, Long> lAsyncPlansStartTimeMap;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    RFCDetailsDAO lRFCDetailsDAO;
    @Autowired
    CacheClient cacheClient;

    public RFCDetailsDAO getRFCDetailsDAO() {
	return lRFCDetailsDAO;
    }

    public WFConfig getWFConfig() {
	return wFConfig;
    }

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public TOSConfig getTOSConfig() {
	return tosConfig;
    }

    public SystemCpuDAO getSystemCpuDAO() {
	return systemCpuDAO;
    }

    public JGitClientUtils getJGitClientUtils() {
	return jGitClientUtils;
    }

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public TOSHelper getTOSHelper() {
	return tOSHelper;
    }

    public RejectHelper getRejectHelper() {
	return rejectHelper;
    }

    public SystemLoadActionsDAO getSystemLoadActionsDAO() {
	return systemLoadActionsDAO;
    }

    public ProductionLoadsDAO getProductionLoadsDAO() {
	return productionLoadsDAO;
    }

    public BuildDAO getBuildDAO() {
	return buildDAO;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public SystemDAO getSystemDAO() {
	return systemDAO;
    }

    public BPMClientUtils getBPMClientUtils() {
	return bPMClientUtils;
    }

    public FTPHelper getFTPHelper() {
	return fTPHelper;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public FallbackHelper getFallbackHelper() {
	return fallbackHelper;
    }

    public DbcrHelper getDbcrHelper() {
	return dbcrHelper;
    }

    public DbcrDAO getDbcrDAO() {
	return dbcrDAO;
    }

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    public PRNumberHelper getPRStatusUpdateinNAS() {
	return prStatusUpdateinNAS;
    }

    public ImplementationDAO getImplementationDAO() {
	return implementationDAO;
    }

    public PlanHelper getPlanHelper() {
	return planHelper;
    }

    public OnlineBuildDAO getOnlineBuildDAO() {
	return onlineBuildDAO;
    }

    @Transactional
    public JSONResponse setOnline(User currentUser, String planId) {
	JSONResponse lResponse = new JSONResponse();
	JobDetails lwssSetOnline = new JobDetails();

	StringBuilder lReturn = new StringBuilder("");
	SortedSet<String> lSet = new TreeSet<String>();
	try {
	    ImpPlan impPlan = getImpPlanDAO().find(planId);
	    if (impPlan.getInprogressStatus() != null && impPlan.getInprogressStatus().equalsIgnoreCase(Constants.PlanInProgressStatus.ONLINE.name())) {
		throw new WorkflowException("Plan - " + planId + " already in progress for ACCEPT process, Kindly wait for some time.");
	    }
	    List<String> split = Arrays.asList(impPlan.getRelatedPlans().split(","));
	    if (!split.isEmpty()) {
		List<Object[]> relatedPlanDetails = getImpPlanDAO().getAllRelatedPlanDetail(planId, split);
		for (Object[] plan : relatedPlanDetails) {
		    String planStatus = plan[1].toString();
		    ImpPlan lDepPlanInfo = getImpPlanDAO().find(plan[0].toString().split("/")[0]);
		    if (!(Constants.PlanStatus.getPreSegmentOnlineStatus().containsKey(planStatus))) {
			lSet.add(plan[0].toString());
		    } else if (lDepPlanInfo.getInprogressStatus() != null && lDepPlanInfo.getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.ONLINE.name()) && lDepPlanInfo.getInprogressStatus().equalsIgnoreCase(Constants.PlanInProgressStatus.NONE.name())) {
			Integer lFailedCnt = getPlanHelper().getFailedOnlineJobCnt(lDepPlanInfo.getId());
			if (lFailedCnt >= 1) {
			    String lErrMsg = "Unable to process ONLINE operation, Plan - " + lDepPlanInfo.getId() + " , GIT operations are not processed for all the system. Please contact Devops Support Team";
			    throw new WorkflowException(lErrMsg);
			}
		    }
		}
	    }

	    List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPreSegmentRelatedPlans(planId);
	    for (Object[] plan : segmentRelatedPlans) {
		ImpPlan lDepPlanInfo = getImpPlanDAO().find(plan[0].toString().split("/")[0]);
		String planStatus = plan[1].toString();
		if (!(Constants.PlanStatus.getPreSegmentOnlineStatus().containsKey(planStatus))) {
		    lSet.add(plan[0].toString());
		}
	    }
	    if (!lSet.isEmpty()) {
		lReturn.append("Plans (").append(StringUtils.join(lSet, ", ")).append(") has to be accept before Plan ").append(planId).append(". ");
		lResponse.setErrorMessage(lReturn.toString());
		lResponse.setStatus(Boolean.FALSE);
		return lResponse;
	    }

	    Long lCount = getProductionLoadsDAO().findLoadSetInProgress(impPlan, Constants.LOAD_SET_STATUS.ACCEPTED);
	    if (lCount > 0) {
		lResponse.setErrorMessage("Accept Already in Progress");
		lResponse.setStatus(Boolean.FALSE);
		return lResponse;
	    }
	    lCount = getProductionLoadsDAO().findLoadSetInProgress(impPlan, Constants.LOAD_SET_STATUS.ACTIVATED);
	    if (lCount > 0) {
		lResponse.setErrorMessage("Activation is yet in Progress");
		lResponse.setStatus(Boolean.FALSE);
		return lResponse;
	    }
	    List<SystemLoad> lSystemLoadList = getSystemLoadDAO().findByImpPlan(impPlan);
	    for (SystemLoad systemLoad : lSystemLoadList) {
		ProductionLoads lProdLoad = getProductionLoadsDAO().findByPlanId(impPlan, systemLoad.getSystemId());
		if ((!Constants.LOAD_SET_STATUS.getAcceptScenarios().contains(lProdLoad.getStatus())) || (lProdLoad.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name()) && lProdLoad.getLastActionStatus().equals("INPROGRESS")) || (lProdLoad.getStatus().equals(Constants.LOAD_SET_STATUS.ACCEPTED.name()) && !lProdLoad.getLastActionStatus().equals("SUCCESS"))) {
		    lResponse.setErrorMessage("Loadset for the System " + lProdLoad.getSystemId().getName() + " is not in Activated State");
		    lResponse.setStatus(Boolean.FALSE);
		    return lResponse;
		}
	    }
	    for (SystemLoad systemLoad : lSystemLoadList) {
		ProductionLoads lProdLoad = getProductionLoadsDAO().findByPlanId(impPlan, systemLoad.getSystemId());
		String lOldStatus = lProdLoad.getStatus();
		if (!lOldStatus.equals(Constants.LOAD_SET_STATUS.ACCEPTED.name())) {

		    lwssSetOnline.setStatus(impPlan.getId() + ": zTPF Online feedback has started for " + systemLoad.getSystemId().getName());
		    wsserver.sendMessage(Constants.Channels.ONLINE_PROCESS, currentUser, lwssSetOnline);

		    lProdLoad.setStatus(Constants.LOAD_SET_STATUS.ACCEPTED.name());
		    lProdLoad.setLastActionStatus("INPROGRESS");
		    getProductionLoadsDAO().update(currentUser, lProdLoad);

		    TosActionQueue lTosQueue = new TosActionQueue();
		    lTosQueue.setOldStatus(lOldStatus);
		    lTosQueue.setPlanId(lProdLoad.getPlanId().getId());
		    lTosQueue.setTosRecId(lProdLoad.getId());
		    lTosQueue.setAction(lProdLoad.getStatus());
		    lTosQueue.setSystemName(lProdLoad.getSystemId().getName());
		    lTosQueue.setUser(currentUser);
		    lTosQueue.setQueueStatus("TOPROCESS");
		    cacheClient.getProdTosCommandProcessingMap().put(lProdLoad.getPlanId().getId() + "_" + lProdLoad.getSystemId().getName(), lTosQueue);

		}
	    }
	    impPlan.setInprogressStatus(Constants.PlanInProgressStatus.ACCEPT.name());
	    getImpPlanDAO().update(currentUser, impPlan);

	} catch (WorkflowException ex) {
	    LOG.info("Unable to process the plan - " + planId, ex);
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("Unable to set the plan as Online for plan - " + planId, ex);
	    throw new WorkflowException("Unable to set the plan as Online", ex);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse acceptFallback(User currentUser, String planId, String rejectReason) {
	LOG.info("rejectReason - " + rejectReason);
	JSONResponse lResponse = new JSONResponse();
	try {
	    ImpPlan impPlan = getImpPlanDAO().find(planId);

	    // 2502 Validation
	    List<ImpPlan> lAcceptedPlans = getImpPlanDAO().getAcceptedInProductionPlan();
	    if (!lAcceptedPlans.isEmpty()) {
		StringBuilder lErrMsg = new StringBuilder("zoldr accept of the Fallback loadset(s) cannot be be done till the ONLINE feedback is completed for ");
		List<String> lPlanNames = new ArrayList();
		lAcceptedPlans.stream().forEach(action -> lPlanNames.add(action.getId()));
		lErrMsg.append(String.join(",", lPlanNames)).append(". Please retry after they are completed");

		CommonActivityMessage lMessage = new CommonActivityMessage(impPlan, null);
		lMessage.setMessage(lErrMsg.toString());
		lMessage.setStatus("Fail");
		getActivityLogDAO().save(currentUser, lMessage);

		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage(lErrMsg.toString());
		return lResponse;
	    }

	    Long lCount = getProductionLoadsDAO().findLoadSetInProgress(impPlan, Constants.LOAD_SET_STATUS.FALLBACK_LOADED);
	    if (lCount > 0) {
		lResponse.setErrorMessage("Fallback Load is in Progress");
		lResponse.setStatus(Boolean.FALSE);
		return lResponse;
	    }
	    lCount = getProductionLoadsDAO().findLoadSetInProgress(impPlan, Constants.LOAD_SET_STATUS.FALLBACK_ACTIVATED);
	    if (lCount > 0) {
		lResponse.setErrorMessage("Fallback Activation is in Progress");
		lResponse.setStatus(Boolean.FALSE);
		return lResponse;
	    }
	    lCount = getProductionLoadsDAO().findLoadSetInProgress(impPlan, Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED);
	    if (lCount > 0) {
		lResponse.setErrorMessage("Fallback Accept is in Progress");
		lResponse.setStatus(Boolean.FALSE);
		return lResponse;
	    }

	    List<SystemLoad> systemLoadList = getSystemLoadDAO().findByImpPlan(impPlan);
	    for (SystemLoad systemLoad : systemLoadList) {
		if (!systemLoad.getProdLoadStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name())) {
		    ProductionLoads lProdLoad = getProductionLoadsDAO().findByPlanId(impPlan, systemLoad.getSystemId());
		    if ((!lProdLoad.getStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_ACTIVATED.name())) || (lProdLoad.getStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_ACTIVATED.name()) && lProdLoad.getLastActionStatus().equals("INPROGRESS"))) {
			lResponse.setErrorMessage("Loadset for the System " + lProdLoad.getSystemId().getName() + " is not in Fallback Activated State");
			lResponse.setStatus(Boolean.FALSE);
			return lResponse;
		    }
		}
	    }
	    StringBuilder lErrReturn = new StringBuilder("");
	    SortedSet<String> lSet = new TreeSet<>();
	    for (SystemLoad lLoadSet : systemLoadList) {
		if (!lLoadSet.getProdLoadStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name())) {
		    List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPostSegmentRelatedPlansProdLoadFallback(lLoadSet.getPlanId().getId());
		    for (Object[] plan : segmentRelatedPlans) {
			String planStatus = plan[1].toString();
			if (Constants.PlanStatus.getApprovedStatusMap().containsKey(planStatus)) {
			    if (!Constants.PlanStatus.FALLBACK.name().equals(planStatus)) {
				lSet.add(plan[0].toString());
			    }
			}
		    }
		}
	    }
	    if (!lSet.isEmpty()) {
		lErrReturn.append("Implementation Plans (").append(StringUtils.join(lSet, ", ")).append(") needs to be fallen back first as they have a later load date than current implementation Plan ").append(planId).append(".");
		lResponse.setErrorMessage(lErrReturn.toString());
		lResponse.setStatus(Boolean.FALSE);
		return lResponse;
	    }

	    for (SystemLoad systemLoad : systemLoadList) {
		if (!systemLoad.getProdLoadStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name())) {
		    ProductionLoads lProdLoad = getProductionLoadsDAO().findByPlanId(impPlan, systemLoad.getSystemId());
		    String lOldStatus = lProdLoad.getStatus();
		    lProdLoad.setStatus(Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name());
		    if (!lAsyncPlansStartTimeMap.containsKey(impPlan.getId() + "-" + Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name())) {
			lAsyncPlansStartTimeMap.put(impPlan.getId() + "-" + Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name(), java.lang.System.currentTimeMillis());
		    }
		    boolean lResult = getTOSHelper().doFallbackTOSOperation(currentUser, Constants.LoadSetCommands.ACCEPT, lProdLoad, lOldStatus, systemLoad, rejectReason);
		    setStatus(lResult, lProdLoad, lOldStatus);
		    getProductionLoadsDAO().update(currentUser, lProdLoad);
		    getActivityLogDAO().save(currentUser, new TOSActivityMessage(lProdLoad.getPlanId(), null, lProdLoad));

		}

	    }
	} catch (Exception ex) {
	    LOG.error("Unable to accept the fallback for the plan - " + planId, ex);
	    throw new WorkflowException("Unable to accept the fallback for the plan - " + planId, ex);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse getLoadsToAccept(User currentUser, boolean isFallback, Integer offset, Integer limit, Map<String, String> orderBy, String filter) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    Map<String, AcceptForm> lReturnList = new HashMap<>();
	    List<ProductionLoads> lProdLoads = new ArrayList<>();
	    if (!isFallback) {
		lProdLoads = getProductionLoadsDAO().findTobeLoaded(orderBy);
		List<ProductionLoads> lRCatProdLoads = getProductionLoadsDAO().getRCatPlansProdLoads(Arrays.asList(Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name(), Constants.PlanStatus.DEPLOYED_IN_PRODUCTION.name()), Arrays.asList(Constants.LOAD_SET_STATUS.ACTIVATED.name()));
		if (lRCatProdLoads != null && !lRCatProdLoads.isEmpty()) {
		    lProdLoads.addAll(lRCatProdLoads);
		}
	    } else {
		lProdLoads = getProductionLoadsDAO().findTobeFallbackLoaded(orderBy);
		List<ProductionLoads> lRCatProdLoads = getProductionLoadsDAO().getRCatPlansProdLoads(Arrays.asList(Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name(), Constants.PlanStatus.DEPLOYED_IN_PRODUCTION.name(), Constants.PlanStatus.ONLINE.name(), Constants.PlanStatus.FALLBACK_DEPLOYED_IN_PRODUCTION.name()), Arrays.asList(Constants.LOAD_SET_STATUS.FALLBACK_ACTIVATED.name(), Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name()));
		if (lRCatProdLoads != null && !lRCatProdLoads.isEmpty()) {
		    lProdLoads.addAll(lRCatProdLoads);
		}
	    }
	    Map<ImpPlan, List<ProductionLoads>> collect = new HashMap<>();

	    if (filter != null && !filter.isEmpty()) {
		collect = lProdLoads.stream().filter(prod -> prod.getPlanId().getId().contains(filter.toUpperCase())).collect(Collectors.groupingBy(t -> t.getPlanId()));
	    } else {
		collect = lProdLoads.stream().collect(Collectors.groupingBy(t -> t.getPlanId()));
	    }

	    for (Map.Entry<ImpPlan, List<ProductionLoads>> entrySet : collect.entrySet()) {
		ImpPlan key = entrySet.getKey();
		List<ProductionLoads> value = Lists.newArrayList(Sets.newHashSet(entrySet.getValue()));
		List<SystemLoad> lSystemLoads = getSystemLoadDAO().findByImpPlan(key.getId());
		Long lLoadCount = getSystemLoadDAO().countByImpPlan(key);
		int lCount = offset;

		// ZTPFM-1980 R Cat Plan Inclusion
		if (lCount >= offset && lCount < limit) {
		    List<SystemLoad> lRCatSystemList = lSystemLoads.stream().filter(lSystemLoad -> lSystemLoad.getLoadCategoryId().getName().equalsIgnoreCase(Constants.RestrictedCatForAcceptPlan.R.name())).collect(Collectors.toList());
		    if (!lRCatSystemList.isEmpty()) {
			Boolean lAccAllButton = Boolean.FALSE;
			List<String> lSysList = new ArrayList();
			if (!isFallback) {
			    Long lActSysCnt = value.stream().filter(t -> Constants.LOAD_SET_STATUS.getActivateAndAbove().contains(t.getStatus())).count();
			    lAccAllButton = lActSysCnt.equals(Long.valueOf(lSystemLoads.size()));
			    lSysList = getSystemLoadDAO().getSystemsToAcceptForR(key.getId(), Arrays.asList(Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name(), Constants.PlanStatus.DEPLOYED_IN_PRODUCTION.name()), Constants.LOAD_SET_STATUS.ACTIVATED.name());
			} else {
			    Long lActSysCnt = value.stream().filter(t -> Constants.LOAD_SET_STATUS.getFallbackActivateAndAbove().contains(t.getStatus())).count();
			    lAccAllButton = lActSysCnt.equals(Long.valueOf(lSystemLoads.size()));
			    lSysList = getSystemLoadDAO().getSystemsToAcceptForR(key.getId(), Arrays.asList(Constants.PlanStatus.ONLINE.name(), Constants.PlanStatus.FALLBACK_DEPLOYED_IN_PRODUCTION.name()), Constants.LOAD_SET_STATUS.FALLBACK_ACTIVATED.name());
			}
			lReturnList.put(key.getId(), new AcceptForm(Boolean.FALSE, value, lAccAllButton, lSysList));
			lCount++;
			if (lCount > limit) {
			    break;
			}
			continue;
		    }
		}

		// Normal Plan Inclusion in List
		if (lLoadCount == value.size()) {
		    if (lCount >= offset && lCount < limit) {
			Long lProdLoadsCount = 0L;
			Long lonlineBuildCount = 0L;
			{
			    if (!isFallback) {
				lProdLoadsCount = getProductionLoadsDAO().findLoadSetInProgress(key, Constants.LOAD_SET_STATUS.ACCEPTED);
				lonlineBuildCount = getOnlineBuildDAO().findOnlineBuildInProgress(key, Constants.BUILD_TYPE.ONL_BUILD);
			    } else {
				lProdLoadsCount = getProductionLoadsDAO().findLoadSetInProgress(key, Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED);
				lonlineBuildCount = getOnlineBuildDAO().findOnlineBuildInProgress(key, Constants.BUILD_TYPE.FAL_BUILD);
			    }
			    if (lProdLoadsCount > 0 && lonlineBuildCount > 0) {
				lReturnList.put(key.getId(), new AcceptForm(Boolean.TRUE, value));
			    } else {
				lReturnList.put(key.getId(), new AcceptForm(Boolean.FALSE, value));
			    }

			}
			lCount++;
			if (lCount > limit) {
			    break;
			}
		    }
		}
	    }

	    Map<String, AcceptForm> collect1 = new LinkedHashMap<String, AcceptForm>();
	    if (orderBy != null && !orderBy.isEmpty()) {
		for (Map.Entry<String, String> entrySet : orderBy.entrySet()) {
		    collect1 = getsortedProductionLoadsList(lReturnList, entrySet.getValue(), entrySet.getKey());
		}
	    } else {
		collect1 = getsortedProductionLoadsList(lReturnList, null, null);
	    }
	    Map<String, AcceptForm> finalList = collect1.entrySet().stream().skip(offset * limit).limit(limit).collect(Collectors.toMap((entry) -> entry.getKey(), (entry) -> entry.getValue()));
	    lResponse.setData(finalList);
	    lResponse.setCount(finalList.size());
	} catch (Exception e) {
	    LOG.error("Unable to get list of Implementation Plan ", e);
	    throw new WorkflowException("Unable to get list of Implementation Plan ", e);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    private Map<String, AcceptForm> getsortedProductionLoadsList(Map<String, AcceptForm> collect, String sortType, String sortValue) {
	TreeMap<Object, List<String>> loadCatAndImpId = new TreeMap<>();
	Map<String, AcceptForm> sortedMap = new LinkedHashMap<>();
	Map<String, AcceptForm> unSortedMap = new LinkedHashMap<>();
	if (sortType == null || sortType.trim().isEmpty()) {
	    sortType = "asc";
	}
	collect.forEach((key, values) -> {
	    AcceptForm sortVal = values;
	    List<ProductionLoads> tempSearchRes = new ArrayList<>();

	    if ((sortValue == null || sortValue.isEmpty() || ("sysload.loadDateTime".equalsIgnoreCase(sortValue))) && values.getProductionLoadsList().stream().filter(res -> res.getSystemLoadId().getLoadDateTime() != null).findAny().isPresent()) {
		tempSearchRes.add(values.getProductionLoadsList().stream().filter(res -> res.getSystemLoadId().getLoadDateTime() != null).min(Comparator.comparing(ProductionLoads::getSystemLoadId, Comparator.comparing(SystemLoad::getLoadDateTime))).get());
	    }
	    if ((sortValue == null || sortValue.isEmpty() || ("loads.activatedDateTime".equalsIgnoreCase(sortValue))) && values.getProductionLoadsList().stream().filter(res -> res.getActivatedDateTime() != null).findAny().isPresent()) {
		tempSearchRes.add(values.getProductionLoadsList().stream().filter(res -> res.getActivatedDateTime() != null).min(Comparator.comparing(ProductionLoads::getActivatedDateTime)).get());
	    }
	    if (tempSearchRes.isEmpty()) {
		tempSearchRes.addAll(values.getProductionLoadsList());
	    }
	    tempSearchRes.forEach((t) -> {
		Object sortKey = null;
		if ("loads.planId.loadType".equalsIgnoreCase(sortValue)) {
		    sortKey = t.getPlanId().getLoadType();
		} else if ("loads.planId.id".equalsIgnoreCase(sortValue)) {
		    sortKey = t.getPlanId().getId();
		} else if ("sysload.loadDateTime".equalsIgnoreCase(sortValue)) {
		    sortKey = t.getSystemLoadId().getLoadDateTime();
		} else if ("loads.activatedDateTime".equalsIgnoreCase(sortValue)) {
		    sortKey = t.getActivatedDateTime();
		} else {
		    // default Sorting by load date time
		    sortKey = t.getPlanId().getId();
		}
		if (sortKey != null) {
		    List<String> impIds = new ArrayList<>();
		    if (loadCatAndImpId.containsKey(sortKey)) {
			impIds = loadCatAndImpId.get(sortKey);
		    }
		    impIds.add(key);
		    loadCatAndImpId.put(sortKey, impIds);
		} else {
		    unSortedMap.put(key, values);
		}
	    });
	});
	int count = 0;
	if (sortType.trim().equalsIgnoreCase("desc")) {
	    count = 0;
	    for (Map.Entry<Object, List<String>> entry : loadCatAndImpId.descendingMap().entrySet()) {
		count++;
		for (String impId : entry.getValue()) {
		    if (collect.containsKey(impId)) {
			sortedMap.put(impId, collect.get(impId));
		    }
		}
	    }
	} else {
	    count = 0;
	    for (Map.Entry<Object, List<String>> entry : loadCatAndImpId.entrySet()) {
		count++;
		for (String impId : entry.getValue()) {
		    if (collect.containsKey(impId)) {
			sortedMap.put(impId, collect.get(impId));
		    }
		}
	    }
	}

	unSortedMap.forEach((key, value) -> {
	    sortedMap.put(key, value);
	});
	return sortedMap;
    }

    @Transactional
    public JSONResponse postActivationAction(User lUser, ProductionLoads lLoadSet, boolean forceActivate, String loadsetDeactivateChangeComment) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	Constants.LOAD_SET_STATUS lStatus = Constants.LOAD_SET_STATUS.valueOf(lLoadSet.getStatus());
	System lSystem = getSystemDAO().find(lLoadSet.getSystemId().getId());

	String lOldStatus = null;
	if (lLoadSet.getId() != null) {
	    ProductionLoads lOldLoad = null;
	    lOldLoad = getProductionLoadsDAO().find(lLoadSet.getId());
	    if (lOldLoad != null) {
		if (lOldLoad.getCpuId() != null) {
		    if (lOldLoad.getStatus().equals(lLoadSet.getStatus())) {
			lResponse.setStatus(Boolean.FALSE);
			lResponse.setErrorMessage("Loadset is already in status " + lLoadSet.getStatus());
			return lResponse;
		    }
		}
		lOldStatus = lOldLoad.getStatus();
	    }
	}

	SystemLoad lSystemLoad = getSystemLoadDAO().find(lLoadSet.getSystemLoadId().getId());

	if (lLoadSet.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
	    Date currentDateTime = new Date();
	    Date loadDateTime = lSystemLoad.getLoadDateTime();

	    if (!forceActivate) {
		if (currentDateTime.before(loadDateTime)) {
		    lResponse.setStatus(Boolean.FALSE);
		    lResponse.setErrorMessage("ZOLDR action not possible as the Plan has not reached its Load date and time!");
		    return lResponse;
		}
	    }
	}

	// 2107 - Load/Activate Validation
	if (lLoadSet.getStatus().equals(Constants.LOAD_SET_STATUS.LOADED.name()) || lLoadSet.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {

	    // If RFC Number isn't filled in, then process shouldn't be proceeded further
	    if (lSystemLoad.getPlanId().getId().startsWith("D") && lSystemLoad.getPlanId().getRfcFlag()) {
		RFCDetails rfcDetails = getRFCDetailsDAO().findByImpPlanAndSysLoad(lSystemLoad.getPlanId().getId(), lSystemLoad.getId());
		if (rfcDetails.getActive().equals("Y") && (rfcDetails.getRfcNumber() == null || rfcDetails.getRfcNumber().isEmpty())) {
		    throw new WorkflowException("Unable to Load/Activate until RFC Number populated for System: " + lSystem.getName() + " . Please Contact DL Core Change team.");
		}
	    }

	    SortedSet<String> lParentPlans = new TreeSet();
	    SortedSet<String> lErrorPlan = new TreeSet();
	    List<ProductionLoads> lProdLoads = new ArrayList();
	    List<ProductionLoads> lProdLoadsList = new ArrayList();
	    List<String> split = Arrays.asList(lLoadSet.getPlanId().getRelatedPlans().split(","));
	    if (split != null && !split.isEmpty()) {
		List<Object[]> relatedPlanDetails = getImpPlanDAO().getAllRelatedPlanDetail(lLoadSet.getPlanId().getId(), split);
		if (relatedPlanDetails != null && !relatedPlanDetails.isEmpty()) {
		    relatedPlanDetails.forEach(t -> lParentPlans.add(t[0].toString() + "/" + t[1].toString()));
		}
	    }
	    List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPreSegmentRelatedPlans(lLoadSet.getPlanId().getId());

	    if (segmentRelatedPlans != null && !segmentRelatedPlans.isEmpty()) {
		segmentRelatedPlans.forEach(t -> lParentPlans.add(t[0].toString() + "/" + t[1].toString()));
	    }

	    for (String lParentPlan : lParentPlans) {
		String lPlanId = lParentPlan.split("/")[0];
		String lSystemName = lParentPlan.split("/")[1];
		String lPlanStatus = lParentPlan.split("/")[2];

		LOG.info("Dep Plan Id info - " + lParentPlan);
		if (!lSystemName.equalsIgnoreCase(lSystem.getName())) {
		    continue;
		}

		ImpPlan lTempPlan = getImpPlanDAO().find(lPlanId);

		if (!Constants.PlanStatus.getPreSegmentProdLoadActivateStatus().containsKey(lPlanStatus)) {
		    lErrorPlan.add(lPlanId);
		    continue;
		}

		if (lTempPlan.getMacroHeader()) {
		    continue;
		}

		if (Constants.PlanStatus.ONLINE.name().equalsIgnoreCase(lPlanStatus)) {
		    lProdLoads = getProductionLoadsDAO().findInfoByPlanAndSystemId(new ImpPlan(lPlanId), new System(lLoadSet.getSystemId().getId()));
		    if (lProdLoads.stream().filter(t -> !t.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACCEPTED.name())).findAny().isPresent()) {
			lErrorPlan.add(lPlanId);
		    }
		}

		if (Constants.PlanStatus.getPRODDeploymentPlanStatus().keySet().contains(lPlanStatus)) {
		    lProdLoads = getProductionLoadsDAO().findInfoByPlanAndSystemId(new ImpPlan(lPlanId), new System(lLoadSet.getSystemId().getId()));
		    lProdLoadsList = getProductionLoadsDAO().findALLByPlanId(new ImpPlan(lPlanId), new System(lLoadSet.getSystemId().getId()));
		    if (lProdLoadsList == null || lProdLoadsList.isEmpty()) {
			lErrorPlan.add(lPlanId);
		    } else {
			if (lLoadSet.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.LOADED.name())) {
			    Boolean isParentLoaded = lProdLoadsList.stream().filter(t -> Constants.LOAD_SET_STATUS.getLoadAndAbove().contains(t.getStatus())).findAny().isPresent();
			    if (!isParentLoaded) {
				lErrorPlan.add(lPlanId);
			    }
			} else if (lLoadSet.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
			    // Is Parent activated in all CPU
			    Boolean isParentActivatedInAllCPU = lProdLoadsList.stream().filter(t -> t.getCpuId() == null && !t.getLastActionStatus().equalsIgnoreCase("INPROGRESS") && Constants.LOAD_SET_STATUS.getActivateAndAbove().contains(t.getStatus())).findAny().isPresent();
			    Boolean isAnyCPUNotActivated = lProdLoadsList.stream().filter(t -> t.getCpuId() != null && !t.getLastActionStatus().equalsIgnoreCase("INPROGRESS") && !Constants.LOAD_SET_STATUS.getActivateAndAbove().contains(t.getStatus())).findAny().isPresent();
			    if (isAnyCPUNotActivated) {
				isParentActivatedInAllCPU = false;
			    }
			    if (lLoadSet.getCpuId() == null) {
				// if single cpu is deactivated then all cpu record still holds activated status
				if (!isParentActivatedInAllCPU) {
				    lErrorPlan.add(lPlanId);
				}
			    } else if (!isParentActivatedInAllCPU) {
				Boolean isParentActivatedInSingleCPU = lProdLoadsList.stream().filter(t -> Constants.LOAD_SET_STATUS.getActivateAndAbove().contains(t.getStatus()) && t.getCpuId() != null && t.getCpuId().getId().equals(lLoadSet.getCpuId().getId()) && !t.getLastActionStatus().equalsIgnoreCase("INPROGRESS")).findAny().isPresent();

				if (!isParentActivatedInSingleCPU) {
				    lErrorPlan.add(lPlanId);
				}
			    }
			}
		    }
		}
	    }

	    if (!lErrorPlan.isEmpty()) {
		String lErrMsg = lLoadSet.getPlanId().getId() + " cannot be " + lLoadSet.getStatus() + " in prod until " + String.join(",", lErrorPlan) + " are " + lLoadSet.getStatus();
		if (lLoadSet.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
		    lErrMsg = lErrMsg + ", Kindly make sure that " + String.join(",", lErrorPlan) + " are activated in respective CPU or ALL CPU";
		}
		throw new WorkflowException(lErrMsg);
	    }
	}

	if (lLoadSet.getStatus().equals(Constants.LOAD_SET_STATUS.DEACTIVATED.name())) {
	    StringBuilder lErrReturn = new StringBuilder("");
	    SortedSet<String> lSet = new TreeSet<>();

	    List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPostSegmentRelatedPlansProdLoad(lLoadSet.getPlanId().getId());
	    for (Object[] plan : segmentRelatedPlans) {
		String planStatus = plan[1].toString();
		if (Constants.PlanStatus.getPRODDeploymentPlanStatus().keySet().contains(planStatus)) {
		    lSet.add(plan[0].toString());
		}
	    }
	    if (!lSet.isEmpty()) {
		lErrReturn.append("Implementation Plans (").append(StringUtils.join(lSet, ", ")).append(") needs to be deactivated first as they have a later load date than current implementation Plan ").append(lLoadSet.getPlanId().getId()).append(".");
		lResponse.setErrorMessage(lErrReturn.toString());
		lResponse.setStatus(Boolean.FALSE);
		return lResponse;
	    }

	    // ZTPFM-2303 Deactivate Comment activity log
	    TSDLoadSetDeactivateActivityMessage tsdLoadSetDeactivateActivityMessage = new TSDLoadSetDeactivateActivityMessage(lLoadSet.getPlanId(), null);
	    tsdLoadSetDeactivateActivityMessage.setLoadSetComment(loadsetDeactivateChangeComment);
	    getActivityLogDAO().save(lUser, tsdLoadSetDeactivateActivityMessage);
	}

	if (lLoadSet.getStatus().equals(Constants.LOAD_SET_STATUS.DELETED.name())) {
	    StringBuilder lErrReturn = new StringBuilder("");
	    SortedSet<String> lSet = new TreeSet<>();

	    List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPostSegmentRelatedPlansProdLoadFallback(lLoadSet.getPlanId().getId());
	    for (Object[] plan : segmentRelatedPlans) {
		String planStatus = plan[1].toString();
		if (Constants.PlanStatus.getReadyForProductionandAboveMap().containsKey(planStatus)) {
		    if (!Constants.PlanStatus.FALLBACK.name().equals(planStatus)) {
			lSet.add(plan[0].toString());
		    }
		}
	    }
	    if (!lSet.isEmpty()) {
		lErrReturn.append("Implementation Plans (").append(StringUtils.join(lSet, ", ")).append(") needs to be fallen back first as they have a later load date than current implementation Plan ").append(lLoadSet.getPlanId().getId()).append(".");
		lResponse.setErrorMessage(lErrReturn.toString());
		lResponse.setStatus(Boolean.FALSE);
		return lResponse;
	    }
	}
	Boolean lFTP = true;

	if (lLoadSet.getStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_LOADED.name()) || lLoadSet.getStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_ACTIVATED.name())) {
	    StringBuilder lErrReturn = new StringBuilder("");
	    SortedSet<String> lSet = new TreeSet<>();

	    List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPostSegmentRelatedPlansProdLoadFallbackLoad(lLoadSet.getPlanId().getId(), Boolean.TRUE);
	    for (Object[] plan : segmentRelatedPlans) {
		String planStatus = plan[1].toString();
		if (Constants.PlanStatus.getReadyForProductionandAboveMap().containsKey(planStatus)) {
		    if (!Constants.PlanStatus.FALLBACK.name().equals(planStatus)) {
			lSet.add(plan[0].toString());
		    }
		}
	    }
	    if (!lSet.isEmpty()) {
		lErrReturn.append("Implementation Plans (").append(StringUtils.join(lSet, ", ")).append(") needs to be fallen back first as they have a later load date than current implementation Plan ").append(lLoadSet.getPlanId().getId()).append(".");
		lResponse.setErrorMessage(lErrReturn.toString());
		lResponse.setStatus(Boolean.FALSE);
		return lResponse;
	    }
	}

	if (lLoadSet.getStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_LOADED.name())) {
	    // FTP FALLBACK LOADSET
	    Build lBuild = getBuildDAO().findLastSuccessfulBuild(lLoadSet.getPlanId().getId(), lLoadSet.getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD);
	    boolean ip = getTOSHelper().requestIP(lUser, lSystemLoad);
	    if (!ip) {
		LOG.error("Unable to get IP Adress for system " + lSystemLoad.getSystemId().getName());
	    }
	    String ipAddress = getTOSHelper().getIP(lSystemLoad.getId());
	    if (ipAddress.isEmpty()) {
		LOG.error("Unable to get IP Adress for system " + lSystemLoad.getSystemId().getName());
	    }

	    ProdFTPActivityMessage lMessage = new ProdFTPActivityMessage(lBuild.getPlanId(), null, lSystemLoad);
	    lMessage.setIpAddress(ipAddress);
	    lMessage.setFallback(true);
	    getActivityLogDAO().save(lUser, lMessage);

	    JSONResponse lSSHResponse = getFTPHelper().doFTP(lUser, lSystemLoad, lBuild, ipAddress, true);
	    if (!lSSHResponse.getStatus()) {
		lMessage.setStatus("Failed");
		getActivityLogDAO().save(lUser, lMessage);
	    } else {
		lMessage.setStatus("Success");
		getActivityLogDAO().save(lUser, lMessage);
		getFTPHelper().getYodaLoadSetPath(lUser, lLoadSet.getPlanId(), lSSHResponse);
	    }
	    if (!lSSHResponse.getStatus()) {
		lFTP = false;
		setStatus(false, lLoadSet, lOldStatus);
	    }
	}

	if (lLoadSet.getId() == null) {
	    getProductionLoadsDAO().save(lUser, lLoadSet);
	}

	switch (lStatus) {
	case LOADED: {
	    LOG.info("Calling IBM TOS Load Activate : " + lSystemLoad.getLoadSetName());
	    lLoadSet.setLastActionStatus("INPROGRESS");
	    getProductionLoadsDAO().update(lUser, lLoadSet);

	    TosActionQueue lTosQueue = new TosActionQueue();
	    lTosQueue.setOldStatus(Constants.LOAD_SET_STATUS.DELETED.name());
	    lTosQueue.setPlanId(lLoadSet.getPlanId().getId());
	    lTosQueue.setTosRecId(lLoadSet.getId());
	    lTosQueue.setAction(lLoadSet.getStatus());
	    lTosQueue.setSystemName(lSystemLoad.getSystemId().getName());
	    lTosQueue.setUser(lUser);
	    lTosQueue.setQueueStatus("TOPROCESS");
	    cacheClient.getProdTosCommandProcessingMap().put(lLoadSet.getPlanId().getId() + "_" + lSystemLoad.getSystemId().getName(), lTosQueue);
	    lResponse.setStatus(Boolean.TRUE);
	    return lResponse;
	    // ZTPFM-2733 we have moved this business of executing the TPF command into
	    // schedular, as we have faced issue on thread sync, we have received the
	    // response for TPF command before completing this thread
	    // break;
	}
	case ACTIVATED: {
	    LOG.info("Calling IBM TOS Activate : " + lSystemLoad.getLoadSetName());
	    if (lOldStatus == null) {
		lOldStatus = Constants.LOAD_SET_STATUS.LOADED.name();
	    }
	    boolean rResult = prodDBCRValidate(lLoadSet.getPlanId().getId(), lUser, lSystemLoad);
	    if (!rResult) {
		setStatus(rResult, lLoadSet, lOldStatus);
		break;
	    }
	    lLoadSet.setLastActionStatus("INPROGRESS");
	    getProductionLoadsDAO().update(lUser, lLoadSet);

	    TosActionQueue lTosQueue = new TosActionQueue();
	    lTosQueue.setOldStatus(lOldStatus);
	    lTosQueue.setPlanId(lLoadSet.getPlanId().getId());
	    lTosQueue.setTosRecId(lLoadSet.getId());
	    lTosQueue.setAction(lLoadSet.getStatus());
	    lTosQueue.setSystemName(lSystemLoad.getSystemId().getName());
	    lTosQueue.setUser(lUser);
	    lTosQueue.setQueueStatus("TOPROCESS");
	    cacheClient.getProdTosCommandProcessingMap().put(lLoadSet.getPlanId().getId() + "_" + lSystemLoad.getSystemId().getName(), lTosQueue);
	    if (forceActivate) {
		TOSActivityMessage forceActivateActivityMessage = new TOSActivityMessage(lLoadSet.getPlanId(), null, lLoadSet);
		forceActivateActivityMessage.setforceActivate(true);
		getActivityLogDAO().save(lUser, forceActivateActivityMessage);
	    }
	    lResponse.setStatus(Boolean.TRUE);
	    return lResponse;
	}
	case DEACTIVATED: {
	    LOG.info("Calling IBM TOS Deactivate : " + lSystemLoad.getLoadSetName());
	    boolean lResult = getTOSHelper().doTOSOperation(lUser, Constants.LoadSetCommands.DEACTIVATE, lLoadSet, lOldStatus, lSystemLoad);
	    if (lResult && !lAsyncPlansStartTimeMap.containsKey(lSystemLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoadSet.getStatus())) {
		lAsyncPlansStartTimeMap.put(lSystemLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoadSet.getStatus(), java.lang.System.currentTimeMillis());
	    }
	    setStatus(lResult, lLoadSet, lOldStatus);
	    break;
	}
	case DELETED: {
	    LOG.info("Calling IBM TOS Delete : " + lSystemLoad.getLoadSetName());
	    boolean lResult = getTOSHelper().doTOSOperation(lUser, Constants.LoadSetCommands.DELETE, lLoadSet, lOldStatus, lSystemLoad);
	    if (lResult && !lAsyncPlansStartTimeMap.containsKey(lSystemLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoadSet.getStatus())) {
		lAsyncPlansStartTimeMap.put(lSystemLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoadSet.getStatus(), java.lang.System.currentTimeMillis());
	    }
	    setStatus(lResult, lLoadSet, lOldStatus);
	    break;
	}
	case FALLBACK_LOADED: {
	    if (lFTP) {
		LOG.info("Calling IBM TOS Fallback Loaded : " + lSystemLoad.getFallbackLoadSetName());
		boolean lResult = getTOSHelper().doFallbackTOSOperation(lUser, Constants.LoadSetCommands.LOAD, lLoadSet, lOldStatus, lSystemLoad, null);
		if (lResult && !lAsyncPlansStartTimeMap.containsKey(lSystemLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoadSet.getStatus())) {
		    lAsyncPlansStartTimeMap.put(lSystemLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoadSet.getStatus(), java.lang.System.currentTimeMillis());
		}
		setStatus(lResult, lLoadSet, lOldStatus);
	    }
	    break;
	}
	case FALLBACK_ACTIVATED: {
	    LOG.info("Calling IBM TOS Activate : " + lSystemLoad.getFallbackLoadSetName());
	    boolean lResult = getTOSHelper().doFallbackTOSOperation(lUser, Constants.LoadSetCommands.ACTIVATE, lLoadSet, lOldStatus, lSystemLoad, null);
	    if (lResult && !lAsyncPlansStartTimeMap.containsKey(lSystemLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoadSet.getStatus())) {
		lAsyncPlansStartTimeMap.put(lSystemLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoadSet.getStatus(), java.lang.System.currentTimeMillis());
	    }
	    setStatus(lResult, lLoadSet, lOldStatus);
	    break;
	}
	case FALLBACK_DEACTIVATED: {
	    LOG.info("Calling IBM TOS Deactivate : " + lSystemLoad.getFallbackLoadSetName());
	    boolean lResult = getTOSHelper().doFallbackTOSOperation(lUser, Constants.LoadSetCommands.DEACTIVATE, lLoadSet, lOldStatus, lSystemLoad, null);
	    if (lResult && !lAsyncPlansStartTimeMap.containsKey(lSystemLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoadSet.getStatus())) {
		lAsyncPlansStartTimeMap.put(lSystemLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoadSet.getStatus(), java.lang.System.currentTimeMillis());
	    }
	    setStatus(lResult, lLoadSet, lOldStatus);
	    break;
	}
	case FALLBACK_DELETED: {
	    LOG.info("Calling IBM TOS Delete DeActivate : " + lSystemLoad.getFallbackLoadSetName());
	    boolean lResult = getTOSHelper().doFallbackTOSOperation(lUser, Constants.LoadSetCommands.DELETE, lLoadSet, lOldStatus, lSystemLoad, null);
	    if (lResult && !lAsyncPlansStartTimeMap.containsKey(lSystemLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoadSet.getStatus())) {
		lAsyncPlansStartTimeMap.put(lSystemLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoadSet.getStatus(), java.lang.System.currentTimeMillis());
	    }
	    setStatus(lResult, lLoadSet, lOldStatus);
	    break;
	}
	default:
	    break;
	}
	getProductionLoadsDAO().update(lUser, lLoadSet);
	/*
	 * author: Ramkumar Seenivasan Created date: 29/July/2019 Story No: 2386
	 * Comments: ForceActivate activity log generation Call.
	 */
	getActivityLogDAO().save(lUser, new TOSActivityMessage(lLoadSet.getPlanId(), null, lLoadSet));
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse getProductionLoads(String[] ids, boolean isFallback, String systemName) {
	JSONResponse lResponse = new JSONResponse();
	Date date = Date.from(LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toInstant());

	System lSystem = getSystemDAO().findByName(systemName);

	List<ProductionLoadsForm> lLoadsFormList = new ArrayList<>();
	List<ProductionLoads> lAllProductionLoads = getProductionLoadsDAO().findByPlanId(ids);

	List<ProductionLoads> lProductionLoads = new ArrayList<>();
	if (isFallback) {
	    lProductionLoads = getProductionLoadsDAO().findByFallbackPlanId(ids, lSystem);
	} else {
	    lProductionLoads = getProductionLoadsDAO().findByPlanId(ids, lSystem);
	}

	Map<String, List<ProductionLoads>> lProductionLoadsMap = lProductionLoads.stream().collect(Collectors.groupingBy(t -> t.getPlanId().getId()));
	Map<String, List<ProductionLoads>> lAllProductionLoadsMap = lAllProductionLoads.stream().collect(Collectors.groupingBy(t -> t.getPlanId().getId()));
	List<ProductionLoads> prodPlans = getProductionLoadsDAO().getProdLoadDeactivatedPlans(date);
	HashMap<String, List<ProductionLoads>> planIdBasedProdPlans = new HashMap<>();

	prodPlans.forEach(prodPlan -> {
	    if (planIdBasedProdPlans.containsKey(prodPlan.getPlanId().getId())) {
		planIdBasedProdPlans.get(prodPlan.getPlanId().getId()).add(prodPlan);
	    } else {
		List<ProductionLoads> temp = new ArrayList<>();
		temp.add(prodPlan);
		planIdBasedProdPlans.put(prodPlan.getPlanId().getId(), temp);
	    }
	});
	// LOG.info("Plan Id based prod plans size: "+planIdBasedProdPlans.size()+" Key
	// sets:
	// "+planIdBasedProdPlans.keySet().stream().collect(Collectors.joining(",")));
	for (String planId : ids) {
	    ProductionLoadsForm loadsForm = new ProductionLoadsForm();
	    List<ProductionLoads> lLoads = lProductionLoadsMap.get(planId);
	    List<ProductionLoads> lAllLoads = lAllProductionLoadsMap.get(planId);
	    ImpPlan lPlan = getImpPlanDAO().find(planId);
	    loadsForm.setPlan(lPlan);
	    if (lAllLoads != null && !lAllLoads.isEmpty()) {
		loadsForm.setIsAnyLoadsDeleted(lAllLoads.stream().filter(t -> t.getStatus().equals("DELETED") || t.getStatus().equals("FALLBACK_DELETED")).count() > 0);
	    }

	    if (lLoads != null && !lLoads.isEmpty()) {
		loadsForm.setProductionLoadsList(lLoads);
		loadsForm.setIsAnyLoadsInProgress(lLoads.stream().filter(t -> t.getLastActionStatus().equals("INPROGRESS")).findAny().isPresent());
	    }
	    // 2276 -> Show Delete Button when system Loadset is Deactivated on the system
	    loadsForm.setShowDeleteButton(Boolean.FALSE);
	    loadsForm.setShowFallbackPendingButton(Boolean.FALSE);

	    if (lAllLoads != null && !lAllLoads.isEmpty() && lLoads != null && !lLoads.isEmpty()) {
		Boolean isLoaded = lLoads.stream().filter(t -> t.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.LOADED.name()) && !t.getLastActionStatus().equalsIgnoreCase("INPROGRESS")).findAny().isPresent();
		if (isLoaded) {
		    loadsForm.setShowDeleteButton(Boolean.TRUE);
		} else {
		    int deactivatedCnt = (int) lLoads.stream().filter(t -> t.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DEACTIVATED.name())).count();
		    if (deactivatedCnt == lLoads.size()) {
			loadsForm.setShowDeleteButton(Boolean.TRUE);
		    }
		}
		Boolean isActivatedOnOthers = lAllLoads.stream().filter(t -> !t.getSystemId().getId().equals(lSystem.getId()) && (t.getActivatedDateTime() != null && !t.getActivatedDateTime().toString().isEmpty())).findAny().isPresent();
		if (isLoaded && isActivatedOnOthers) {
		    loadsForm.setShowDeleteButton(Boolean.FALSE);
		}

		// 2286 - Pending Fallback Status
		int deactivatedCnt = (int) lLoads.stream().filter(t -> t.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DEACTIVATED.name()) && !t.getLastActionStatus().equalsIgnoreCase("INPROGRESS")).count();
		if (deactivatedCnt == lLoads.size() && !lPlan.getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.PENDING_FALLBACK.name())) {
		    loadsForm.setShowFallbackPendingButton(Boolean.TRUE);
		}
	    }

	    if (lLoads != null && !loadsForm.getIsAnyLoadsInProgress() && !lLoads.isEmpty()) {
		String lLoadCategoryName = lLoads.get(0).getSystemLoadId().getLoadCategoryId().getName();
		String lAllLoadStatus = "";

		List<SystemCpu> lCpus = new ArrayList<>();
		List<Integer> lCpuIds = new ArrayList<>();
		for (ProductionLoads lLoad : lLoads) {
		    lCpus.add(lLoad.getCpuId());
		    if (lLoad.getCpuId() != null) {
			lCpuIds.add(lLoad.getCpuId().getId());
		    } else {
			lAllLoadStatus = lLoad.getStatus();
		    }
		}
		if (Constants.lLoadCategoryforCPUs.contains(lLoadCategoryName)) {
		    loadsForm.setIsMultipleCPUAllowed(Boolean.TRUE);
		    List<Object[]> lProdLoadsCount = getProductionLoadsDAO().getLoadsCountByLoadStatus(lLoads.get(0).getPlanId().getId(), lLoads.get(0).getSystemId().getId());
		    List<Object[]> lAllProdLoadsCount = getProductionLoadsDAO().getAllLoadsCountByLoadStatus(lLoads.get(0).getPlanId().getId(), lLoads.get(0).getSystemId().getId());

		    Integer lActivatedCount = 0, lDeactivatedCount = 0;
		    Integer lAllActivatedCount = 0, lAllDeactivatedCount = 0;

		    for (Object[] lProdLoad : lProdLoadsCount) {
			if (lProdLoad[0].toString().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
			    lActivatedCount = Integer.valueOf(lProdLoad[1].toString());
			} else if (lProdLoad[0].toString().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DEACTIVATED.name())) {
			    lDeactivatedCount = Integer.valueOf(lProdLoad[1].toString());
			}
		    }

		    for (Object[] lProdLoad : lAllProdLoadsCount) {
			if (lProdLoad[0].toString().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
			    lAllActivatedCount = Integer.valueOf(lProdLoad[1].toString());
			} else if (lProdLoad[0].toString().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DEACTIVATED.name())) {
			    lAllDeactivatedCount = Integer.valueOf(lProdLoad[1].toString());
			}
		    }

		    if (lAllDeactivatedCount > 0 && lActivatedCount > 0) {
			loadsForm.setSelectDeActivateAll(Boolean.FALSE);
		    }

		    if (lAllActivatedCount > 0 && lDeactivatedCount > 0) {
			loadsForm.setSelectActivateAll(Boolean.FALSE);
		    }

		    if (lCpus.contains(null)) {
			// Multiple ROW After All
			if (lAllLoadStatus.equals(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
			    if (getTOSConfig().getTosSystemType().equals(Constants.TOSEnvironment.PRODUCTION.name())) {
				loadsForm.setDeActivationSystemCpusList(getSystemCpuDAO().findCpusOtherthan(lSystem, lCpuIds, Constants.TOSEnvironment.PRODUCTION.name(), false));
			    } else {
				loadsForm.setDeActivationSystemCpusList(getSystemCpuDAO().findCpusOtherthan(lSystem, lCpuIds, Constants.TOSEnvironment.NATIVE_CPU.name(), false));
			    }
			    if (loadsForm.getDeActivationSystemCpusList().isEmpty()) {
				loadsForm.setShowAddDeActivateButton(Boolean.FALSE);
			    } else {
				loadsForm.setShowAddDeActivateButton(Boolean.TRUE);
			    }
			} else if (lAllLoadStatus.equals(Constants.LOAD_SET_STATUS.DEACTIVATED.name())) {
			    if (getTOSConfig().getTosSystemType().equals(Constants.TOSEnvironment.PRODUCTION.name())) {
				loadsForm.setActivationSystemCpusList(getSystemCpuDAO().findCpusOtherthan(lSystem, lCpuIds, Constants.TOSEnvironment.PRODUCTION.name(), false));
			    } else {
				loadsForm.setActivationSystemCpusList(getSystemCpuDAO().findCpusOtherthan(lSystem, lCpuIds, Constants.TOSEnvironment.NATIVE_CPU.name(), false));
			    }
			    if (loadsForm.getActivationSystemCpusList().isEmpty()) {
				loadsForm.setShowAddActivateButton(Boolean.FALSE);
			    } else {
				loadsForm.setShowAddActivateButton(Boolean.TRUE);
			    }
			} else if (lAllLoadStatus.equals(Constants.LOAD_SET_STATUS.LOADED.name())) {
			    if (getTOSConfig().getTosSystemType().equals(Constants.TOSEnvironment.PRODUCTION.name())) {
				loadsForm.setActivationSystemCpusList(getSystemCpuDAO().findCpusOtherthan(lSystem, lCpuIds, Constants.TOSEnvironment.PRODUCTION.name(), true));
			    } else {
				loadsForm.setActivationSystemCpusList(getSystemCpuDAO().findCpusOtherthan(lSystem, lCpuIds, Constants.TOSEnvironment.NATIVE_CPU.name(), true));
			    }
			    loadsForm.setShowAddActivateButton(Boolean.FALSE);
			    loadsForm.setShowAddDeActivateButton(Boolean.FALSE);
			} else {
			    loadsForm.setShowAddActivateButton(Boolean.FALSE);
			    loadsForm.setShowAddDeActivateButton(Boolean.FALSE);
			}
		    } else {
			// Multiple ROW Before All
			if (getTOSConfig().getTosSystemType().equals(Constants.TOSEnvironment.PRODUCTION.name())) {
			    loadsForm.setActivationSystemCpusList(getSystemCpuDAO().findCpusOtherthan(lSystem, lCpuIds, Constants.TOSEnvironment.PRODUCTION.name(), true));
			} else {
			    loadsForm.setActivationSystemCpusList(getSystemCpuDAO().findCpusOtherthan(lSystem, lCpuIds, Constants.TOSEnvironment.NATIVE_CPU.name(), true));
			}
			if (!loadsForm.getActivationSystemCpusList().isEmpty()) {
			    loadsForm.setShowAddActivateButton(Boolean.TRUE);
			} else {
			    loadsForm.setShowAddActivateButton(Boolean.FALSE);
			}
			loadsForm.setShowAddDeActivateButton(Boolean.FALSE);
		    }
		}
	    }

	    loadsForm.setLoadsetDeavctivatedInfo(getConactMsgOnDeactivate(planIdBasedProdPlans, planId, lSystem));
	    loadsForm.setLoadsetAddtInfo(getLoadsetDeactMsg(planIdBasedProdPlans, planId, lSystem));
	    lLoadsFormList.add(loadsForm);
	}

	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(lLoadsFormList);
	return lResponse;
    }

    private String getLoadsetDeactMsg(HashMap<String, List<ProductionLoads>> planIdBasedProdPlans, String planId, System lSystem) {
	String loadsetDeactMsg = "";
	if (planIdBasedProdPlans.containsKey(planId)) {
	    List<ProductionLoads> prodLoads = planIdBasedProdPlans.get(planId);
	    Set<String> deactSystemList = prodLoads.stream().filter(prod -> prod.getActive().equalsIgnoreCase("Y") && (prod.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DEACTIVATED.name()) || prod.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DELETED.name()))).map(prod -> prod.getSystemId().getName().toUpperCase()).collect(Collectors.toSet());
	    if (deactSystemList != null && !deactSystemList.isEmpty()) {
		String deactSystems = String.join(",", deactSystemList);
		List<ProductionLoads> lprodLoadList = getProductionLoadsDAO().findByPlanIdWithCpu(new ImpPlan(planId), lSystem);

		if (lprodLoadList == null || lprodLoadList.isEmpty()) {
		    loadsetDeactMsg = "Zoldr load action is not allowed, as deactivated on " + deactSystems;
		} else {
		    ProductionLoads lProdLoad = lprodLoadList.stream().findFirst().get();
		    if (lProdLoad.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
			loadsetDeactMsg = "Zoldr deactivate action required, as deactivated on " + deactSystems;
		    } else if (lProdLoad.getStatus().equals(Constants.LOAD_SET_STATUS.LOADED.name()) || lProdLoad.getStatus().equals(Constants.LOAD_SET_STATUS.DEACTIVATED.name())) {
			loadsetDeactMsg = "Zoldr delete action required, as deactivated on " + deactSystems;
		    }
		}
	    }
	}
	return loadsetDeactMsg;
    }

    private String getConactMsgOnDeactivate(HashMap<String, List<ProductionLoads>> planIdBasedProdPlans, String planId, System lSystem) {
	String contactMsg = "";
	if (planIdBasedProdPlans.containsKey(planId)) {
	    List<ProductionLoads> prodLoads = planIdBasedProdPlans.get(planId);
	    Set<String> deactSystemList = prodLoads.stream().filter(prod -> prod.getActive().equalsIgnoreCase("Y") && (prod.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DEACTIVATED.name()) || prod.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DELETED.name()))).map(prod -> prod.getSystemId().getName().toUpperCase()).collect(Collectors.toSet());
	    if (deactSystemList != null && !deactSystemList.isEmpty()) {
		String deactSystems = String.join(",", deactSystemList);
		ProductionLoads lprodLoad = getProductionLoadsDAO().findByPlanId(new ImpPlan(planId), lSystem);
		if (lprodLoad == null || lprodLoad.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name()) || lprodLoad.getStatus().equals(Constants.LOAD_SET_STATUS.DEACTIVATED.name()) || lprodLoad.getStatus().equals(Constants.LOAD_SET_STATUS.LOADED.name())) {
		    contactMsg = "Loadset(s) on " + deactSystems + " have been deactivated for more than 24 hours. Contact Impl Plan Lead/Dev Manager to resolve disposition of Plan";
		}
	    }
	}
	return contactMsg;
    }

    private void setStatus(Boolean lResult, ProductionLoads lLoad, String lOldStatus) {
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

    @Transactional
    public JSONResponse getFallBackSystemLoadListBySystemId(Integer pId, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setMetaData(new Date());
	lResponse.setData(getSystemLoadDAO().findFallbackLoadsBySystem(pId, pOffset, pLimit));
	lResponse.setCount(getSystemLoadDAO().countFallbackLoadsBySystem(pId));
	return lResponse;
    }

    @Transactional
    public JSONResponse getSystemLoadListBySystemId(Integer pId, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy, String pFilter) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setMetaData(new Date());
	LOG.info("Plan Statuses -> " + Constants.PlanStatus.getPRODDeploymentStatus().keySet());
	lResponse.setData(getSystemLoadDAO().findBySystem(pId, Constants.PlanStatus.getPRODDeploymentStatus().keySet(), false, pOffset, pLimit, pFilter));
	lResponse.setCount(getSystemLoadDAO().getCountBySystem(pId, Constants.PlanStatus.getPRODDeploymentStatus().keySet(), false, pFilter));
	return lResponse;
    }

    private boolean prodDBCRValidate(String pId, User lUser, SystemLoad systemLoad) {
	ImpPlan lPlan = getImpPlanDAO().find(pId);
	List<Dbcr> dbcrList = getDbcrDAO().findByPlanSystemEnvironment(lPlan.getId(), systemLoad.getSystemId().getId(), Constants.DBCR_ENVIRONMENT.PROD.name());
	if (!dbcrList.isEmpty()) {
	    Dbcr dbcrError = null;
	    boolean dbcrStatus = Boolean.TRUE;
	    for (Dbcr dbcr : dbcrList) {
		if (dbcr.getMandatory().equalsIgnoreCase("Y")) {
		    if (!getDbcrHelper().isDbcrComplete(dbcr)) {
			dbcrStatus = Boolean.FALSE;
			dbcrError = dbcr;
			break;
		    }
		}
	    }
	    if (!dbcrStatus && dbcrError != null) {
		DbcrValidationMessage dbcrMessage = new DbcrValidationMessage(lPlan, null);
		dbcrMessage.setDbcrName(dbcrError.getDbcrName());
		dbcrMessage.setSystemName(dbcrError.getSystemId().getName());
		dbcrMessage.setEnvironment(Constants.DBCR_ENVIRONMENT.PROD.getValue());
		getActivityLogDAO().save(lUser, dbcrMessage);
		return false;
	    }
	}
	return true;
    }

    @Transactional
    public JSONResponse getAuxLoads(User currentUser, Integer offset, Integer limit, Map<String, String> orderBy, String filter) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    List<ImpPlan> lPlanList = getImpPlanDAO().findByLoad(Arrays.asList(Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name()), Constants.LoaderTypes.A.name(), offset, limit, orderBy, filter);
	    Long lPlanCount = getImpPlanDAO().getCountOfLoad(Arrays.asList(Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name()), Constants.LoaderTypes.A.name(), filter);
	    lResponse.setData(lPlanList);
	    lResponse.setCount(lPlanCount);
	} catch (Exception e) {
	    LOG.error("Unable to get list of Implementation Plan ", e);
	    throw new WorkflowException("Unable to get list of Implementation Plan ", e);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse markAuxAsOnline(User currentUser, String planId) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setUserId(currentUser.getId());
	lPlanOnlineFallbackStatusMap.put(planId, "ONLINE");
	try {
	    ImpPlan lPlan = getImpPlanDAO().find(planId);
	    JSONResponse lValidationRes = markOnlineFallbackValidation(lPlan);
	    if (!lValidationRes.getStatus()) {
		throw new WorkflowException(lValidationRes.getErrorMessage());
	    }
	    lPlan.setPlanStatus(Constants.PlanStatus.ACCEPTED_IN_PRODUCTION.name());
	    lPlan.setTpfAcceptedDateTime(new Date());
	    lPlan.setInprogressStatus(Constants.PlanInProgressStatus.NONE.name());
	    getImpPlanDAO().update(currentUser, lPlan);

	    TosActionQueue lAcceptQueue = new TosActionQueue();
	    lAcceptQueue.setAction("TOPROCESS");
	    lAcceptQueue.setUser(currentUser);
	    cacheClient.getOnlineAcceptPlanMap().put(planId, lAcceptQueue);

	    if (lPlan.getLoadType().equalsIgnoreCase(Constants.LoadTypes.EXCEPTION.name())) {
		rejectHelper.rejectDependentPlans(currentUser, lPlan.getId(), Constants.REJECT_REASON.ONLINE.getValue(), Constants.AUTOREJECT_COMMENT.ONLINE.getValue(), true, Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE);
		// Get List of Unsecured dependent plans and send source contention mail
		List<Object[]> autoDependentPlanList = impPlanDAO.getPostSegmentRelatedPlans(lPlan.getId(), Boolean.FALSE);
		SortedSet<String> planIds = new TreeSet();
		for (Object[] plan : autoDependentPlanList) {
		    if (Constants.PlanStatus.ACTIVE.name().equalsIgnoreCase(plan[1].toString())) {
			planIds.add(plan[2].toString());
		    }
		}
		for (String plan : planIds) {
		    ImpPlan depImpPlan = impPlanDAO.find(plan);
		    ExceptionSourceContentionMail sourceContentionMail = (ExceptionSourceContentionMail) mailMessageFactory.getTemplate(ExceptionSourceContentionMail.class);
		    sourceContentionMail.setOnlinePlan(lPlan.getId());
		    sourceContentionMail.setDependentPlan(depImpPlan.getId());
		    sourceContentionMail.setLeadId(depImpPlan.getLeadId());
		    mailMessageFactory.push(sourceContentionMail);
		}
	    }

	    lResponse.setStatus(Boolean.TRUE);
	} catch (WorkflowException ex) {
	    lResponse.setErrorMessage(ex.getMessage());
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("Unable to set plan as ONLINE ", ex);
	    throw new WorkflowException("Unable to mark the implementation plan as ONLINE", ex);
	} finally {
	    lPlanOnlineFallbackStatusMap.remove(planId);
	    lResponse.setMetaData(planId);
	    wsserver.sendMessage(Constants.Channels.PLAN_AUX_ONLINE, currentUser, lResponse);
	}
	return lResponse;
    }

    public JSONResponse markOnlineFallbackValidation(ImpPlan planInfo) {
	JSONResponse lResponse = new JSONResponse();
	List<SystemLoad> lSystemLoadList = getSystemLoadDAO().findByImpPlan(planInfo);
	if (planInfo.getInprogressStatus() != null && planInfo.getInprogressStatus().equalsIgnoreCase(Constants.PlanInProgressStatus.ONLINE.name())) {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Unable to Sync Plan - " + planInfo.getId() + ", ONLINE process is already INPROGRESS");
	    return lResponse;
	}

	for (SystemLoad lSystemLoad : lSystemLoadList) {
	    Date currentDateTime = new Date();
	    Date loadDateTime = lSystemLoad.getLoadDateTime();

	    if (!Constants.isAllowLoadAnyTime) {
		if (currentDateTime.before(loadDateTime)) {
		    lResponse.setStatus(Boolean.FALSE);
		    lResponse.setErrorMessage("Marking plan as ONLINE is not allowed as the Plan has not reached its Load date and time!");
		    return lResponse;
		}
	    }
	}

	StringBuilder lErrReturn = new StringBuilder("");
	SortedSet<String> lSet = new TreeSet<String>();
	List<String> lDepPlans = Arrays.asList(planInfo.getRelatedPlans().split(","));
	if (!lDepPlans.isEmpty()) {
	    List<Object[]> relatedPlanDetails = getImpPlanDAO().getAllRelatedPlanDetail(planInfo.getId(), lDepPlans);
	    for (Object[] plan : relatedPlanDetails) {
		String planStatus = plan[1].toString();
		if (!(Constants.PlanStatus.getPreSegmentOnlineStatus().containsKey(planStatus))) {
		    lSet.add(plan[0].toString());
		}
	    }
	}

	List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPreSegmentRelatedPlans(planInfo.getId());
	segmentRelatedPlans.forEach((plan) -> {
	    String planStatus = plan[1].toString();
	    if (!(Constants.PlanStatus.getPreSegmentOnlineStatus().containsKey(planStatus))) {
		lSet.add(plan[0].toString());
	    }
	});
	if (!lSet.isEmpty()) {
	    lErrReturn.append("Plans (").append(StringUtils.join(lSet, ", ")).append(") has to move to production before Plan ").append(planInfo.getId()).append(". ");
	    lResponse.setErrorMessage(lErrReturn.toString());
	    lResponse.setStatus(Boolean.FALSE);
	    return lResponse;
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse markAuxAsFallback(User currentUser, String planId, String rejectReason, Constants.FALLBACK_STATUS fallbackType) {
	LOG.info("rejectReason - " + rejectReason);
	JSONResponse lResponse = new JSONResponse();
	lResponse.setUserId(currentUser.getId());
	lPlanOnlineFallbackStatusMap.put(planId, "FALLBACK");
	try {
	    ImpPlan lPlan = getImpPlanDAO().find(planId);
	    StringBuilder lErrReturn = new StringBuilder("");
	    SortedSet<String> lSet = new TreeSet<>();

	    boolean flag = true;

	    if (lPlan.getMacroHeader()) {
		flag = false;
	    }
	    List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPostSegmentRelatedPlansProdLoadFallbackLoad(lPlan.getId(), flag);
	    for (Object[] plan : segmentRelatedPlans) {
		String planStatus = plan[1].toString();
		String lplanId = Arrays.asList(plan[0].toString().split("/")).get(0);
		ImpPlan lImpPlan = getImpPlanDAO().find(lplanId);
		if (lImpPlan.getMacroHeader()) {
		    if (Constants.PlanStatus.getApprovedToOnline().containsKey(planStatus)) {
			lSet.add(plan[0].toString());
		    }
		} else {
		    String prodLoadStatus = "";
		    if (plan[3] != null) {
			prodLoadStatus = plan[3].toString();
		    }
		    if (!prodLoadStatus.isEmpty() && Constants.PROD_LOAD_STATUS.getAcceptScenarios().containsKey(prodLoadStatus)) {
			lSet.add(plan[0].toString());
		    }

		}

	    }

	    if (!lSet.isEmpty()) {

		lErrReturn.append("Implementation Plans (").append(StringUtils.join(lSet, ", ")).append(") needs to be fallen back first as they have a later load date than current implementation Plan ").append(lPlan.getId()).append(".");
		MacroHeaderPlanFallBackActivity macroHeaderPlanFallBackActivity = new MacroHeaderPlanFallBackActivity(lPlan, null);
		macroHeaderPlanFallBackActivity.setMessage(lErrReturn.toString());
		getActivityLogDAO().save(currentUser, macroHeaderPlanFallBackActivity);

		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage(lErrReturn.toString());
		return lResponse;
	    }

	    if (!lAsyncPlansStartTimeMap.containsKey(lPlan.getId() + "-" + Constants.BUILD_TYPE.FAL_BUILD.name())) {
		lAsyncPlansStartTimeMap.put(lPlan.getId() + "-" + Constants.BUILD_TYPE.FAL_BUILD.name(), java.lang.System.currentTimeMillis());
	    }

	    String lOldStatus = lPlan.getPlanStatus();
	    fallbackHelper.fallBackStatusUpdate(currentUser, lPlan.getId(), fallbackType, rejectReason, null);

	    StatusChangeToDependentPlanMail statusChangeToDependentPlanMail = (StatusChangeToDependentPlanMail) mailMessageFactory.getTemplate(StatusChangeToDependentPlanMail.class);
	    statusChangeToDependentPlanMail.addToAddressUserId(lPlan.getLeadId(), Constants.MailSenderRole.LEAD);
	    statusChangeToDependentPlanMail.setImpPlanId(lPlan.getId());
	    statusChangeToDependentPlanMail.setOldStatus(lOldStatus);
	    statusChangeToDependentPlanMail.setNewStatus(Constants.PlanStatus.FALLBACK.name());
	    statusChangeToDependentPlanMail.setReason(rejectReason);
	    mailMessageFactory.push(statusChangeToDependentPlanMail);
	    lResponse.setStatus(Boolean.TRUE);

	    getPRStatusUpdateinNAS().writeFileInNAS(lPlan);
	} catch (WorkflowException ex) {
	    lResponse.setErrorMessage(ex.getMessage());
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("Unable to set plan as ONLINE ", ex);
	    throw new WorkflowException("Unable to mark the implementation plan as ONLINE", ex);
	} finally {
	    lPlanOnlineFallbackStatusMap.remove(planId);
	    lResponse.setMetaData(planId);
	    wsserver.sendMessage(Constants.Channels.PLAN_AUX_FALLBACK, currentUser, lResponse);
	}
	return lResponse;
    }

    public JSONResponse getAuxPlanOpStatus(User currentUser) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setCount(lPlanOnlineFallbackStatusMap.size());
	lResponse.setData(lPlanOnlineFallbackStatusMap);
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    public JSONResponse DeleteAuxPlanInOpStatus(User currentUser, String planId) {
	JSONResponse lResponse = new JSONResponse();
	lPlanOnlineFallbackStatusMap.remove(planId);
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse getImplementationPlanSync(User currentUser, Constants.LoaderTypes loadType, Integer offset, Integer limit, LinkedHashMap<String, String> lOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	List<String> statusList = new ArrayList<String>();
	statusList.add(Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name());
	statusList.add(Constants.PlanStatus.DEPLOYED_IN_PRODUCTION.name());
	int offsetNew = offset * limit;
	List<ImpPlan> impPlanList = getImpPlanDAO().findByStatusList(statusList, false, false, null, null, Constants.BUILD_TYPE.STG_LOAD, loadType, offsetNew, limit, lOrderBy, true);
	lResponse.setCount(getImpPlanDAO().countByStatusList(statusList, false, null, Constants.BUILD_TYPE.STG_LOAD, loadType));
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(impPlanList);
	return lResponse;
    }

    @Transactional
    public JSONResponse uploadFallbackLoadset(User lUser, String planId) {
	JSONResponse lResponse = new JSONResponse();

	List<SystemLoad> systemLoadList = getSystemLoadDAO().findByImpPlan(planId);
	for (SystemLoad lSystemLoad : systemLoadList) {
	    if (lSystemLoad.getLoadCategoryId().getName().equals("R")) {
		Constants.BUILD_TYPE lBuildType = Constants.BUILD_TYPE.STG_LOAD;
		Build lBuild = getBuildDAO().findLastSuccessfulBuild(lSystemLoad.getPlanId().getId(), lSystemLoad.getSystemId().getId(), lBuildType);
		boolean ip = getTOSHelper().requestIP(lUser, lSystemLoad);
		if (!ip) {
		    LOG.error("Unable to get IP Adress for system " + lSystemLoad.getSystemId().getName());
		    throw new WorkflowException("Unable to request for system " + lSystemLoad.getSystemId().getName());
		}

		String ipAddress = getTOSHelper().getIP(lSystemLoad.getId());

		if (ipAddress.isEmpty()) {
		    LOG.error("Unable to get IP Adress for system " + lSystemLoad.getSystemId().getName());
		    throw new WorkflowException("Unable to get IP Address for system " + lSystemLoad.getSystemId().getName());
		}

		ProdFTPActivityMessage lMessage = new ProdFTPActivityMessage(lBuild.getPlanId(), null, lSystemLoad);
		lMessage.setIpAddress(ipAddress);
		lMessage.setFallback(true);
		getActivityLogDAO().save(lUser, lMessage);
		JSONResponse lSSHResponse = getFTPHelper().doFTP(lUser, lSystemLoad, lBuild, ipAddress, true);
		if (!lSSHResponse.getStatus()) {
		    lResponse.setStatus(false);
		    lResponse.setErrorMessage("Fallback Loadset FTP Failed");
		    lMessage.setStatus("Failed");
		    getActivityLogDAO().save(lUser, lMessage);
		} else {
		    lResponse.setStatus(true);
		    lMessage.setStatus("Success");
		    getActivityLogDAO().save(lUser, lMessage);
		}
	    }
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getFallBackMacroHeaderPlan(User pCurrentUser, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setMetaData(new Date());
	User currentOrDelagateUser = pCurrentUser.getCurrentOrDelagateUser();
	lResponse.setData(getImpPlanDAO().findFallBackMacroHeaderPlan("", pOffset, pLimit));
	lResponse.setCount(getImpPlanDAO().countFallBackMacroHeaderPlan(""));
	return lResponse;
    }

    @Transactional
    public JSONResponse postTOSOperationProcess(User user, ProdTOSForm prodLoads) {
	JSONResponse lResponse = new JSONResponse();
	Set<TosActionQueue> lQueueList = new HashSet();
	String action = prodLoads.getAction();
	Set<String> lCurrentPlanList = new HashSet();
	try {

	    Integer systemId = null;
	    for (ProductionLoads lProdLoad : prodLoads.getProdLoads()) {
		lCurrentPlanList.add(lProdLoad.getPlanId().getId());
		systemId = lProdLoad.getSystemId().getId();
	    }

	    if (systemId == null) {
		throw new WorkflowException("Unable to process the request, Unknown system identified");
	    }

	    System lSystem = getSystemDAO().find(systemId);
	    LOG.info("List of Plans " + lCurrentPlanList.toString());
	    List<String> lRemovePlans = new ArrayList();
	    Boolean isPreviousRequestInProgress = cacheClient.getProdTOSLoadActPlansMap().entrySet().stream().filter(t -> t.getValue().getUser().getId().equalsIgnoreCase(user.getId()) && t.getValue().getSystemName().equalsIgnoreCase(lSystem.getName()) && (t.getValue().getQueueStatus().equalsIgnoreCase("TOPROCESS") || t.getValue().getQueueStatus().equalsIgnoreCase("INPROGRESS"))).findAny().isPresent();
	    if (isPreviousRequestInProgress) {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("Unable to process your request, previous TPF action for some plans are inprogress for system - " + lSystem.getName() + ", please wait and click report for more information on inprogress requests");
		return lResponse;
	    } else {
		cacheClient.getProdTOSLoadActPlansMap().entrySet().stream().filter(t -> t.getValue().getUser().getId().equalsIgnoreCase(user.getId()) && t.getValue().getSystemName().equalsIgnoreCase(lSystem.getName())).forEach(t -> lRemovePlans.add(t.getKey()));
	    }
	    if (!lRemovePlans.isEmpty()) {
		lRemovePlans.stream().forEach(t -> cacheClient.getProdTOSLoadActPlansMap().remove(t));
	    }

	    // Pre Validations
	    for (ProductionLoads lProdLoad : prodLoads.getProdLoads()) {
		ImpPlan lPlan = getImpPlanDAO().find(lProdLoad.getPlanId().getId());
		SystemLoad lSystemLoad = getSystemLoadDAO().findBy(lProdLoad.getPlanId(), lProdLoad.getSystemId());
		ProductionLoads lDBProd = new ProductionLoads();
		String lOldStatus = action;
		if (lProdLoad.getId() != null) {
		    lDBProd = getProductionLoadsDAO().find(lProdLoad.getId());
		    lOldStatus = lDBProd.getStatus();
		    if (lDBProd.getLastActionStatus() != null && lDBProd.getLastActionStatus().equalsIgnoreCase("INPROGRESS")) {
			String lErrMessage = "Previous action - " + lDBProd.getStatus() + " is INPROGRESS for plan - " + lDBProd.getPlanId().getId() + " , kindly wait until previous action get complete";
			throw new WorkflowException(lErrMessage);
		    }
		}

		Date currentDateTime = new Date();
		Date loadDateTime = lSystemLoad.getLoadDateTime();

		if (action.equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
		    if (currentDateTime.before(loadDateTime)) {
			String lErrMessage = "ZOLDR action not possible as the Plan - " + lSystemLoad.getPlanId().getId() + " has not reached its Load date and time!";
			throw new WorkflowException(lErrMessage);
		    }
		}

		LOG.info("Dependency Valdiation Started");

		Set<String> lParentPlans = new TreeSet();
		SortedSet<String> lErrorPlan = new TreeSet();

		List<String> split = Arrays.asList(lProdLoad.getPlanId().getRelatedPlans().split(","));
		if (split != null && !split.isEmpty()) {
		    List<Object[]> relatedPlanDetails = getImpPlanDAO().getAllRelatedPlanDetail(lProdLoad.getPlanId().getId(), split);
		    if (relatedPlanDetails != null) {
			relatedPlanDetails.forEach(t -> lParentPlans.add(t[0].toString() + "/" + t[1].toString()));
		    }
		}

		List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPreSegmentRelatedPlans(lProdLoad.getPlanId().getId());
		if (segmentRelatedPlans != null && !segmentRelatedPlans.isEmpty()) {
		    segmentRelatedPlans.forEach(t -> lParentPlans.add(t[0].toString() + "/" + t[1].toString()));
		}
		for (String lParentPlan : lParentPlans) {
		    String lPlanId = lParentPlan.split("/")[0];
		    String lTarSystem = lParentPlan.split("/")[1];
		    String lPlanStatus = lParentPlan.split("/")[2];

		    LOG.info("Processing Plan Id - " + lParentPlan);

		    if (!lTarSystem.equalsIgnoreCase(lSystem.getName())) {
			continue;
		    }

		    if (lCurrentPlanList.contains(lPlanId)) {
			LOG.info("Parent Plan - " + lPlanId + " is available in current plan list");
			continue;
		    }

		    ImpPlan lTempPlan = getImpPlanDAO().find(lPlanId);

		    if (!Constants.PlanStatus.getPreSegmentProdLoadActivateStatus().containsKey(lPlanStatus)) {
			lErrorPlan.add(lPlanId);
			continue;
		    }

		    if (lTempPlan.getMacroHeader()) {
			continue;
		    }

		    if (Constants.PlanStatus.ONLINE.name().equalsIgnoreCase(lPlanStatus)) {
			List<ProductionLoads> lProdLoads = new ArrayList();
			lProdLoads = getProductionLoadsDAO().findInfoByPlanAndSystemId(new ImpPlan(lPlanId), new System(lProdLoad.getSystemId().getId()));
			if (lProdLoads.stream().filter(t -> !t.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACCEPTED.name())).findAny().isPresent()) {
			    lErrorPlan.add(lPlanId);
			}
		    }

		    if (Constants.PlanStatus.getPRODDeploymentPlanStatus().keySet().contains(lPlanStatus)) {
			List<ProductionLoads> lProdLoads = new ArrayList();
			lProdLoads = getProductionLoadsDAO().findInfoByPlanAndSystemId(new ImpPlan(lPlanId), new System(lProdLoad.getSystemId().getId()));
			if (lProdLoads == null || lProdLoads.isEmpty()) {
			    lErrorPlan.add(lPlanId);
			} else {
			    for (ProductionLoads lTProdLoad : lProdLoads) {
				if (lProdLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.LOADED.name()) && (!Constants.LOAD_SET_STATUS.getLoadAndAbove().contains(lTProdLoad.getStatus()))) {
				    lErrorPlan.add(lPlanId);
				} else if ((lProdLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name())) && (!Constants.LOAD_SET_STATUS.getActivateAndAbove().contains(lTProdLoad.getStatus()))) {
				    lErrorPlan.add(lPlanId);
				}
			    }
			}
		    }
		}

		if (lErrorPlan.size() > 0) {
		    String lErrMsg = lProdLoad.getPlanId().getId() + " cannot be " + action + " in prod until " + String.join(",", lErrorPlan) + " are " + action;
		    if (lProdLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
			lErrMsg = lErrMsg + ", Kindly make sure that " + String.join(",", lErrorPlan) + " are activated in all CPU";
		    }
		    throw new WorkflowException(lErrMsg);
		} else {
		    if (lProdLoad.getId() == null) {
			getProductionLoadsDAO().save(user, lProdLoad);
			if (action.equalsIgnoreCase(Constants.LOAD_SET_STATUS.LOADED.name())) {
			    lOldStatus = Constants.LOAD_SET_STATUS.DELETED.name();
			} else if (action.equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
			    lOldStatus = Constants.LOAD_SET_STATUS.LOADED.name();
			}
		    } else {
			lOldStatus = lDBProd.getStatus();
		    }
		}
		// SystemLoad Vs PreProductionLoads needs to be used for Sorting if needed

		TosActionQueue lPPActionQueue = new TosActionQueue(user, lOldStatus, lProdLoad.getId(), "TOPROCESS", "", action, lProdLoad.getPlanId().getId());
		lPPActionQueue.setSystemName(lProdLoad.getSystemId().getName());
		lQueueList.add(lPPActionQueue);
	    }

	    lQueueList.stream().forEach(t -> {
		ProductionLoads lProdLoad = getProductionLoadsDAO().find(t.getTosRecId());
		lProdLoad.setStatus(action);
		lProdLoad.setLastActionStatus("INPROGRESS");
		getProductionLoadsDAO().update(user, lProdLoad);

		cacheClient.getProdTOSLoadActPlansMap().put(t.getPlanId() + "_" + lProdLoad.getSystemId().getName(), t);
	    });

	    LOG.info("Queue Addition Completed, size of queue " + cacheClient.getProdTOSLoadActPlansMap().size());

	} catch (WorkflowException ex) {
	    LOG.info("Error Occurs - ", ex);
	    throw ex;
	} catch (Exception Ex) {
	    LOG.info("Error Occurs", Ex);
	    throw new WorkflowException("Unable to Process your request", Ex);
	}

	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    public JSONResponse removeLoadAndActivateInTOS(User user, String planId, String systemName, Integer preProd) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	try {
	    Set<TosActionQueue> lRemovePlans = new HashSet();
	    if (planId != null && !planId.isEmpty()) {
		if (cacheClient.getProdTOSLoadActPlansMap().containsKey(planId)) {
		    cacheClient.getProdTOSLoadActPlansMap().remove(planId);
		}
	    } else {
		cacheClient.getProdTOSLoadActPlansMap().entrySet().stream().filter(tosAction -> tosAction.getValue().getUser().getId().equalsIgnoreCase(user.getId())).forEach(t -> lRemovePlans.add(t.getValue()));
		lRemovePlans.stream().forEach(t -> cacheClient.getProdTOSLoadActPlansMap().remove(t.getPlanId()));
	    }
	} catch (Exception Ex) {
	    throw new WorkflowException("Unable to delete the record from cache");
	}
	return lResponse;
    }

    public JSONResponse getLoadAndActivateInTOS(User user) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(cacheClient.getProdTOSLoadActPlansMap());
	return lResponse;
    }

    @Transactional
    public JSONResponse acceptMultiPlans(User user, String[] planIds) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    Set<TosActionQueue> lAllowedPlans = new HashSet();
	    Set<String> lInComingPlanList = new TreeSet();
	    lInComingPlanList.addAll(Arrays.asList(planIds));
	    LOG.info("Received Plan List - " + lInComingPlanList.toString());

	    // Throw error when any one of the plan is in progress
	    for (String lProdLoad : planIds) {
		ImpPlan lPlan = getImpPlanDAO().find(lProdLoad);
		if (lPlan.getInprogressStatus() != null && (lPlan.getInprogressStatus().equalsIgnoreCase(Constants.PlanInProgressStatus.ONLINE.name()) || lPlan.getInprogressStatus().equalsIgnoreCase(Constants.PlanInProgressStatus.ACCEPT.name()))) {
		    String lErrMsg = "Plan - " + lPlan.getId() + " is already in INPROGRESS for ACCEPT operation, Kindly exclude from multi selection and try again";
		    throw new WorkflowException(lErrMsg);
		}
	    }

	    for (String lProdLoad : planIds) {
		ImpPlan lPlan = getImpPlanDAO().find(lProdLoad);
		List<SystemLoad> lSysList = getSystemLoadDAO().findByImpPlan(lPlan);

		LOG.info("Processing Plan - " + lPlan.getId());
		for (SystemLoad lSysLoad : lSysList) {
		    if (Constants.RestrictedCatForAcceptPlan.getCatList().contains(lSysLoad.getLoadCategoryId().getName())) {
			throw new WorkflowException("Plan - " + lPlan.getId() + " System - " + lSysLoad.getSystemId().getName() + " has category R which is restricted for Multi Select Feature");
		    }
		}
		// Plan Validation
		Set<String> lDepPlans = new TreeSet();
		Set<String> lErrorPlans = new TreeSet();
		// Segment and Manual Dependency Plan List
		if (lPlan.getRelatedPlans() != null && !lPlan.getRelatedPlans().isEmpty()) {
		    lDepPlans.addAll(Arrays.asList(lPlan.getRelatedPlans().split(",")));
		}
		List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPreSegmentRelatedPlans(lPlan.getId());
		segmentRelatedPlans.stream().map((plan) -> plan[0].toString().split("/")[0]).forEachOrdered((lPlanId) -> {
		    lDepPlans.add(lPlanId.trim());
		});

		for (String lPlanId : lDepPlans) {
		    ImpPlan lDepPlanInfo = getImpPlanDAO().find(lPlanId);
		    if (lInComingPlanList.contains(lPlanId)) {
			LOG.info("Plan - " + lPlanId + " is available in processing plan List");
			continue;
		    }

		    if (lDepPlanInfo.getInprogressStatus() != null && Constants.PlanInProgressStatus.ONLINE.name().equalsIgnoreCase(lDepPlanInfo.getInprogressStatus())) {
		    } else if (lDepPlanInfo.getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.ONLINE.name()) && lDepPlanInfo.getInprogressStatus() != null && Constants.PlanInProgressStatus.NONE.name().equalsIgnoreCase(lDepPlanInfo.getInprogressStatus())) {
			Integer lFailedCnt = getPlanHelper().getFailedOnlineJobCnt(lPlanId);
			if (lFailedCnt >= 1) {
			    lErrorPlans.add(lDepPlanInfo.getId());
			}
		    } else if (!(Constants.PlanStatus.getPreSegmentOnlineStatus().containsKey(lDepPlanInfo.getPlanStatus()))) {
			lErrorPlans.add(lDepPlanInfo.getId());
		    }
		}

		if (!lErrorPlans.isEmpty()) {
		    StringBuilder lErrMsg = new StringBuilder("");
		    lErrMsg.append("Plans (").append(StringUtils.join(lErrorPlans, ", ")).append(") has to be accept before Plan ").append(lPlan.getId()).append(". ");
		    CommonActivityMessage lMessage = new CommonActivityMessage(lPlan, null);
		    lMessage.setMessage(lErrMsg.toString());
		    lMessage.setStatus("Fail");
		    getActivityLogDAO().save(user, lMessage);
		    wsserver.sendMessage(Constants.Channels.PLAN_AUX_ONLINE, user, lErrMsg);

		    throw new WorkflowException(lErrMsg.toString());

		} else {
		    lPlan.setInprogressStatus(Constants.PlanInProgressStatus.ACCEPT.name());
		    getImpPlanDAO().update(user, lPlan);

		    TosActionQueue lTosAction = new TosActionQueue();
		    lTosAction.setAction(Constants.LOAD_SET_STATUS.ACCEPTED.name());
		    lTosAction.setPlanId(lPlan.getId());
		    lTosAction.setQueueStatus("TOPROCESS");
		    lTosAction.setUser(user);
		    lAllowedPlans.add(lTosAction);
		}
	    }
	    if (!lAllowedPlans.isEmpty()) {
		lAllowedPlans.stream().forEach(t -> {
		    cacheClient.getProdTOSAcceptPlansMap().put(t.getPlanId(), t);
		});
		LOG.info("Size of accept cache - " + cacheClient.getProdTOSAcceptPlansMap().size());
	    }
	} catch (WorkflowException Ex) {
	    LOG.info("Unable to Mark the Plan as ONLINE - ", Ex);
	    throw Ex;
	} catch (Exception Ex) {
	    LOG.info("Unable to Mark the Plan as ONLINE", Ex);
	    throw new WorkflowException("Unable to mark the Plan as ONLINE");
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse isAcceptInProgress(String[] pPlanId) {
	JSONResponse response = new JSONResponse();
	List<System> systemList = new ArrayList<>();
	for (String planId : pPlanId) {
	    List<SystemLoad> lSystemLoads = getSystemLoadDAO().findByImpPlan(planId);
	    for (SystemLoad pLoad : lSystemLoads) {
		systemList.add(pLoad.getSystemId());
	    }
	    List<OnlineBuild> buildList = getOnlineBuildDAO().findLastBuildInProgress(planId, systemList, Constants.BUILD_TYPE.ONL_BUILD);
	    for (OnlineBuild lBuild : buildList) {
		response.setData(lBuild);
		response.setStatus(Boolean.TRUE);
	    }
	}

	return response;
    }

    @Transactional
    public JSONResponse acceptLoadsetForSystem(User user, String planId, String systemName) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    ImpPlan lPlan = getImpPlanDAO().find(planId);
	    System lSystem = getSystemDAO().findByName(systemName);
	    if (lSystem == null) {
		throw new WorkflowException("Invalid System Name - " + systemName);
	    }

	    ProductionLoads lProdLoad = getProductionLoadsDAO().findByPlanId(lPlan, lSystem);
	    if (lProdLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACCEPTED.name())) {
		if (lProdLoad.getLastActionStatus().equalsIgnoreCase("INPROGRESS")) {
		    throw new WorkflowException("Accept process is already in progress for System - " + systemName);
		} else {
		    throw new WorkflowException("System - " + systemName + " already accepted");
		}
	    }

	    // Plan Validation
	    Set<String> lDepPlans = new TreeSet();
	    Set<String> lErrorPlans = new TreeSet();

	    // Segment and Manual Dependency Plan List
	    if (lPlan.getRelatedPlans() != null && !lPlan.getRelatedPlans().isEmpty()) {
		lDepPlans.addAll(Arrays.asList(lPlan.getRelatedPlans().split(",")));
	    }
	    List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPreSegmentRelatedPlans(lPlan.getId());
	    segmentRelatedPlans.stream().map((plan) -> plan[0].toString().split("/")[0]).forEachOrdered((lPlanId) -> {
		lDepPlans.add(lPlanId.trim());
	    });

	    for (String lPlanId : lDepPlans) {
		ImpPlan lDepPlanInfo = getImpPlanDAO().find(lPlanId);
		if (lDepPlanInfo.getInprogressStatus() != null && Constants.PlanInProgressStatus.ONLINE.name().equalsIgnoreCase(lDepPlanInfo.getInprogressStatus())) {
		} else if (lDepPlanInfo.getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.ONLINE.name()) && lDepPlanInfo.getInprogressStatus() != null && Constants.PlanInProgressStatus.NONE.name().equalsIgnoreCase(lDepPlanInfo.getInprogressStatus())) {
		    Integer lFailedCnt = getPlanHelper().getFailedOnlineJobCnt(lPlanId);
		    if (lFailedCnt >= 1) {
			lErrorPlans.add(lDepPlanInfo.getId());
		    }
		} else if (!(Constants.PlanStatus.getPreSegmentOnlineStatus().containsKey(lDepPlanInfo.getPlanStatus()))) {
		    lErrorPlans.add(lDepPlanInfo.getId());
		}
	    }

	    if (!lErrorPlans.isEmpty()) {
		StringBuilder lErrMsg = new StringBuilder("");
		lErrMsg.append("Plans (").append(StringUtils.join(lErrorPlans, ", ")).append(") has to be accept before Plan ").append(lPlan.getId()).append(". ");
		CommonActivityMessage lMessage = new CommonActivityMessage(lPlan, null);
		lMessage.setMessage(lErrMsg.toString());
		lMessage.setStatus("Fail");
		getActivityLogDAO().save(user, lMessage);
		throw new WorkflowException(lErrMsg.toString());
	    } else {
		acceptLoadset(user, lProdLoad, Boolean.FALSE, "");
	    }
	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception ex) {
	    throw new WorkflowException("Unable to process the ACCEPT request for Plan - " + planId + ", system - " + systemName);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse acceptFallbackLoadsetForSystem(User user, String planId, String systemName) {
	JSONResponse lResponse = new JSONResponse();
	StringBuilder lAdvisoryMsg = new StringBuilder("");
	try {
	    ImpPlan lPlan = getImpPlanDAO().find(planId);
	    System lSystem = getSystemDAO().findByName(systemName);
	    List<SystemLoad> systemLoadList = getSystemLoadDAO().findByImpPlan(lPlan);
	    List<String> lErrToLoadSystems = new ArrayList();
	    List<String> lErrToDelSystems = new ArrayList();

	    if (lSystem == null) {
		throw new WorkflowException("Invalid System Name - " + systemName);
	    }

	    ProductionLoads lCurrentPlanProdLoad = getProductionLoadsDAO().findByPlanId(lPlan, lSystem);
	    if (lCurrentPlanProdLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name())) {
		if (lCurrentPlanProdLoad.getLastActionStatus().equalsIgnoreCase("INPROGRESS")) {
		    throw new WorkflowException("Accept process is already in progress for System - " + systemName);
		} else {
		    throw new WorkflowException("System - " + systemName + " already Accepted");
		}
	    }

	    for (SystemLoad systemLoad : systemLoadList) {
		if (systemLoad.getSystemId().getName().equalsIgnoreCase(lSystem.getName())) {
		    continue;
		}
		ProductionLoads lOtherProdLoad = getProductionLoadsDAO().findByPlanId(lPlan, systemLoad.getSystemId());
		if (lOtherProdLoad == null) {
		    continue;
		}
		if (lOtherProdLoad.getStatus().equalsIgnoreCase("ACCEPTED")) {
		    lErrToLoadSystems.add(systemLoad.getSystemId().getName());
		} else if (Arrays.asList(Constants.LOAD_SET_STATUS.ACTIVATED.name(), Constants.LOAD_SET_STATUS.DEACTIVATED.name()).contains(lOtherProdLoad.getStatus())) {
		    lErrToDelSystems.add(systemLoad.getSystemId().getName());
		}
	    }

	    if (!lErrToLoadSystems.isEmpty()) {
		lAdvisoryMsg.append("Please load/activate/accept the fallback loadset for target system(s) ").append(lErrToLoadSystems.toString());
	    }
	    if (!lErrToDelSystems.isEmpty()) {
		lAdvisoryMsg.append("<br> Please deactivate/delete the fallback loadset for target system(s) ").append(lErrToDelSystems.toString());
	    }

	    StringBuilder lErrReturn = new StringBuilder("");
	    SortedSet<String> lSet = new TreeSet<>();
	    for (SystemLoad lLoadSet : systemLoadList) {
		if (lLoadSet.getProdLoadStatus() != null && !lLoadSet.getProdLoadStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name())) {
		    List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPostSegmentRelatedPlansProdLoadFallback(lLoadSet.getPlanId().getId());
		    for (Object[] plan : segmentRelatedPlans) {
			String planStatus = plan[1].toString();
			if (Constants.PlanStatus.getApprovedStatusMap().containsKey(planStatus)) {
			    if (!Constants.PlanStatus.FALLBACK.name().equals(planStatus)) {
				lSet.add(plan[0].toString());
			    }
			}
		    }
		}
	    }
	    if (!lSet.isEmpty()) {
		lErrReturn.append("Implementation Plans (").append(StringUtils.join(lSet, ", ")).append(") needs to be fallen back first as they have a later load date than current implementation Plan ").append(planId).append(".");
		lResponse.setErrorMessage(lErrReturn.toString());
		lResponse.setStatus(Boolean.FALSE);
		return lResponse;
	    }

	    acceptLoadset(user, lCurrentPlanProdLoad, Boolean.TRUE, "Fallback Accepted");

	} catch (WorkflowException ex) {
	    LOG.info("Unable to process the Fallback Accept for Plan " + planId, ex);
	    throw ex;
	} catch (Exception ex) {
	    LOG.info("Unable to process the Fallback Accept for Plan " + planId, ex);
	    throw new WorkflowException("Unable to process the ACCEPT request for Plan - " + planId + ", system - " + systemName);
	}
	lResponse.setStatus(Boolean.TRUE);
	if (!lAdvisoryMsg.toString().isEmpty()) {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage(lAdvisoryMsg.toString());
	}
	return lResponse;
    }

    private Boolean acceptLoadset(User user, ProductionLoads prodLoad, Boolean isFallback, String rejectReason) {
	String lOldStatus = prodLoad.getStatus();
	String lNewStatus = isFallback ? Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name() : Constants.LOAD_SET_STATUS.ACCEPTED.name();
	Boolean lResult = Boolean.FALSE;

	prodLoad.setStatus(lNewStatus);
	if (!lAsyncPlansStartTimeMap.containsKey(prodLoad.getPlanId().getId() + "-" + lNewStatus)) {
	    lAsyncPlansStartTimeMap.put(prodLoad.getPlanId().getId() + "-" + lNewStatus, java.lang.System.currentTimeMillis());
	}
	if (isFallback) {
	    lResult = getTOSHelper().doFallbackTOSOperation(user, Constants.LoadSetCommands.ACCEPT, prodLoad, lOldStatus, prodLoad.getSystemLoadId(), rejectReason);
	} else {
	    lResult = getTOSHelper().doTOSOperation(user, Constants.LoadSetCommands.ACCEPT, prodLoad, lOldStatus, prodLoad.getSystemLoadId());
	}
	setStatus(lResult, prodLoad, lOldStatus);
	getProductionLoadsDAO().update(user, prodLoad);
	getActivityLogDAO().save(user, new TOSActivityMessage(prodLoad.getPlanId(), null, prodLoad));

	return lResult;
    }

    @Transactional
    public JSONResponse enablePlanForAccept(User user, String[] planIds) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    Arrays.asList(planIds).stream().forEach(lPlanId -> {
		ImpPlan lPlan = getImpPlanDAO().find(lPlanId);
		lPlan.setIsAcceptEnabled(Boolean.TRUE);
		getImpPlanDAO().update(user, lPlan);
		CommonActivityMessage lMessage = new CommonActivityMessage(lPlan, null);
		lMessage.setMessage("Action - \"Enable for ACCEPT by target system\" has been processed successfully");
		getActivityLogDAO().save(user, lMessage);
	    });
	} catch (WorkflowException ex) {
	    LOG.info("Unable to Enable the Plan for Accept", ex);
	    throw ex;
	} catch (Exception ex) {
	    throw new WorkflowException("Unable to Enable the Plan for Accept operation ");
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse setPlanasPendingFallback(User user, String planId, String comments) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    ImpPlan lPlanInfo = getImpPlanDAO().find(planId);
	    lPlanInfo.setPlanStatus(Constants.PlanStatus.PENDING_FALLBACK.name());
	    getImpPlanDAO().update(user, lPlanInfo);

	    // Update Activity log for Plan status
	    PlanStatusActivityMessage planStatusActivityMessage = new PlanStatusActivityMessage(lPlanInfo, null);
	    planStatusActivityMessage.setStatus(Constants.PlanStatus.PENDING_FALLBACK.name());
	    planStatusActivityMessage.setReason(comments);
	    getActivityLogDAO().save(user, planStatusActivityMessage);

	} catch (WorkflowException ex) {
	    LOG.info("Unable to update the Plan status as Pending Fallback for plan - " + planId, ex);
	    throw ex;
	} catch (Exception ex) {
	    LOG.info("Unable to update the Plan status as Pending Fallback for plan - " + planId, ex);
	    throw new WorkflowException("Unable to update the Plan status as Pending Fallback for plan - " + planId, ex);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse getActionNotAllowedPlans(User user, String[] planIds, String systemName, String action) {
	JSONResponse lReturn = new JSONResponse();
	try {
	    List<String> lAllowedPlans = new ArrayList();
	    if (action.equalsIgnoreCase(Constants.LOAD_SET_STATUS.LOADED.name())) {
		lAllowedPlans = getSystemLoadDAO().getLoadNotAllowedPlans(Arrays.asList(planIds), systemName);
	    } else if (action.equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
		lAllowedPlans = getSystemLoadDAO().getAllowedPlansToActivate(Arrays.asList(planIds), systemName);
	    }
	    lReturn.setData(lAllowedPlans);
	} catch (WorkflowException ex) {
	    LOG.info("Unable to do valdiation for Load/Activate action against plans", ex);
	    throw ex;
	} catch (Exception ex) {
	    LOG.info("Unable to do valdiation for Load/Activate action against plans", ex);
	    throw new WorkflowException("Unable to do valdiation for Load/Activate action against plans", ex);
	}

	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    // Generic function to convert list to set
    public static <T> Set<T> convertListToSet(List<T> list) {
	// create an empty set
	Set<T> set = new HashSet<>();

	// Add each element of list into the set
	for (T t : list) {
	    set.add(t);
	}

	// return the set
	return set;
    }

    @Transactional
    public JSONResponse getProdTOSActionReport(User user, String action, String systemName) {
	JSONResponse lReturn = new JSONResponse();
	List<TosActionQueue> lQueues = new ArrayList();
	if (action.equalsIgnoreCase(Constants.LOAD_SET_STATUS.LOADED.name()) || action.equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
	    cacheClient.getProdTOSLoadActPlansMap().entrySet().stream().filter(tosAction -> tosAction.getValue().getUser().getId().equalsIgnoreCase(user.getId()) && systemName.equalsIgnoreCase(tosAction.getValue().getSystemName())).forEach(tosAction -> lQueues.add(tosAction.getValue()));
	} else if (action.equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACCEPTED.name())) {
	    cacheClient.getProdTOSAcceptPlansMap().entrySet().stream().filter(tosAction -> tosAction.getValue().getUser().getId().equalsIgnoreCase(user.getId())).forEach(tosAction -> lQueues.add(tosAction.getValue()));
	}
	lReturn.setStatus(Boolean.TRUE);
	lReturn.setData(lQueues);
	return lReturn;
    }

    @Transactional
    public JSONResponse getAcceptedPlans(User user, Integer offset, Integer limit, String[] orderBy, String filter) {
	JSONResponse lReturn = new JSONResponse();
	try {
	    List<OnlineFeedbackQueueForm> lReturnData = new ArrayList();
	    Map<String, String> lOrderBy = new HashMap();
	    if (orderBy != null && orderBy.length > 0) {
		Arrays.asList(orderBy).stream().filter(t -> t.contains(",")).forEach(t -> lOrderBy.put(t.split(",")[0], t.split(",")[1]));
	    }
	    List<ImpPlan> lPlanList = getImpPlanDAO().getAcceptedInProductionPlans(offset, limit, lOrderBy, filter);
	    lPlanList.stream().forEach(plan -> {
		List<ProductionLoads> lProdLoads = getProductionLoadsDAO().findByPlanId(plan);
		OnlineFeedbackQueueForm lOnlineForm = new OnlineFeedbackQueueForm(plan, lProdLoads);
		lReturnData.add(lOnlineForm);
	    });
	    Long lPlanCount = getImpPlanDAO().getAcceptedInProductionPlansCount();
	    lReturn.setData(lReturnData);
	    lReturn.setCount(lPlanCount.intValue());
	} catch (Exception ex) {
	    LOG.info("Unable to get he list of plans which are accepted in production", ex);
	    throw new WorkflowException("Unable to get he list of plans which are accepted in production", ex);
	}
	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    public JSONResponse removeAcceptInprogressPlan(User user, String planId) {
	JSONResponse lReturn = new JSONResponse();
	cacheClient.getProdTOSLoadActPlansMap().clear();
	cacheClient.getProdTOSAcceptPlansMap().clear();
	cacheClient.getOnlineAcceptPlanMap().clear();

	return lReturn;
    }

    public JSONResponse getAcceptProcessingPlans(User user) {
	JSONResponse lReturn = new JSONResponse();
	lReturn.setData(cacheClient.getProdTOSAcceptPlansMap());
	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    public JSONResponse getTosLoadActProcessingPlans(User user) {
	JSONResponse lReturn = new JSONResponse();
	lReturn.setData(cacheClient.getProdTOSLoadActPlansMap());
	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    public JSONResponse clearProdTOSLoadActCache(User user, String action, String systemName) {
	JSONResponse lReturn = new JSONResponse();
	Boolean isAnyPlanInProgress = Boolean.FALSE;
	if (!action.equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACCEPTED.name())) {
	    isAnyPlanInProgress = cacheClient.getProdTOSLoadActPlansMap().entrySet().stream().filter(tosAction -> tosAction.getValue().getUser().getId().equalsIgnoreCase(user.getId()) && systemName.equalsIgnoreCase(tosAction.getValue().getSystemName()) && (tosAction.getValue().getQueueStatus().equalsIgnoreCase("INPROGRESS") || tosAction.getValue().getQueueStatus().equalsIgnoreCase("TOPROCESS"))).findAny().isPresent();
	    if (isAnyPlanInProgress) {
		lReturn.setStatus(Boolean.FALSE);
		lReturn.setErrorMessage("Few of the previous request were not yet processed, please wait for while and click on report to view the status of those previous request");
		return lReturn;
	    } else {
		Set<String> lPlanToRemove = new HashSet();
		cacheClient.getProdTOSLoadActPlansMap().entrySet().stream().filter(tosAction -> tosAction.getValue().getUser().getId().equalsIgnoreCase(user.getId()) && systemName.equalsIgnoreCase(tosAction.getValue().getSystemName())).forEach(t -> lPlanToRemove.add(t.getKey()));
		lPlanToRemove.stream().forEach(t -> cacheClient.getProdTOSLoadActPlansMap().remove(t));
	    }
	} else {
	    isAnyPlanInProgress = cacheClient.getProdTOSAcceptPlansMap().entrySet().stream().filter(tosAction -> tosAction.getValue().getUser().getId().equalsIgnoreCase(user.getId()) && (tosAction.getValue().getQueueStatus().equalsIgnoreCase("INPROGRESS") || tosAction.getValue().getQueueStatus().equalsIgnoreCase("TOPROCESS"))).findAny().isPresent();
	    if (isAnyPlanInProgress) {
		lReturn.setStatus(Boolean.FALSE);
		lReturn.setErrorMessage("Few of the previous request were not yet processed, please wait for while and click on report to view the status of those previous request");
		return lReturn;
	    } else {
		Set<String> lPlanToRemove = new HashSet();
		cacheClient.getProdTOSAcceptPlansMap().entrySet().stream().filter(tosAction -> tosAction.getValue().getUser().getId().equalsIgnoreCase(user.getId())).forEach(t -> lPlanToRemove.add(t.getKey()));
		lPlanToRemove.stream().forEach(t -> cacheClient.getProdTOSLoadActPlansMap().remove(t));
	    }
	}
	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    private Boolean isParentActivatedInCpu(ProductionLoads childProdLoad, List<ProductionLoads> parentProdLoads) {
	if (parentProdLoads.stream().filter(t -> t.getCpuId() == null).findAny().isPresent()) {
	    return Boolean.TRUE;
	}
	if (parentProdLoads.stream().filter(t -> t.getCpuId() != null && t.getCpuId().equals(childProdLoad.getCpuId())).findAny().isPresent()) {
	    return Boolean.TRUE;
	}
	return Boolean.FALSE;
    }

}
