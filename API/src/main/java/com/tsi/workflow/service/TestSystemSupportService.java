/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.service;

import com.tsi.workflow.User;
import com.tsi.workflow.activity.DbcrValidationMessage;
import com.tsi.workflow.activity.PreProdActionsActivityLog;
import com.tsi.workflow.activity.ProdFTPActivityMessage;
import com.tsi.workflow.base.BaseService;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.Dbcr;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemCpu;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dao.SystemLoadActions;
import com.tsi.workflow.beans.dao.Vpars;
import com.tsi.workflow.beans.ui.PreProdTOSForm;
import com.tsi.workflow.beans.ui.PreProductionLoadsForm;
import com.tsi.workflow.beans.ui.TosActionQueue;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.DbcrDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.PreProductionLoadsDAO;
import com.tsi.workflow.dao.SystemCpuDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.helper.DbcrHelper;
import com.tsi.workflow.helper.FTPHelper;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.tos.TOSHelper;
import com.tsi.workflow.tos.model.TOSResult;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.tsi.workflow.websocket.WSMessagePublisher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
@Service
public class TestSystemSupportService extends BaseService {

    private static final Logger LOG = Logger.getLogger(TestSystemSupportService.class.getName());
    @Autowired
    SystemDAO systemDAO;
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    SystemCpuDAO systemCpuDAO;
    @Autowired
    PreProductionLoadsDAO preProductionLoadsDAO;
    @Autowired
    BuildDAO buildDAO;
    @Autowired
    TOSHelper tOSHelper;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    FTPHelper fTPHelper;
    @Autowired
    WSMessagePublisher wsserver;
    @Autowired
    PlanHelper planHelper;
    @Autowired
    SystemLoadActionsDAO systemLoadActionsDAO;
    @Autowired
    ConcurrentHashMap<String, Set<TosActionQueue>> lPreProdTOSOperationMap;
    DbcrHelper dbcrHelper;
    @Autowired
    DbcrDAO dbcrDAO;
    @Autowired
    ConcurrentHashMap<String, Long> lAsyncPlansStartTimeMap;

    public DbcrHelper getDbcrHelper() {
	return dbcrHelper;
    }

    public void setDbcrHelper(DbcrHelper dbcrHelper) {
	this.dbcrHelper = dbcrHelper;
    }

    public DbcrDAO getDbcrDAO() {
	return dbcrDAO;
    }

    public void setDbcrDAO(DbcrDAO dbcrDAO) {
	this.dbcrDAO = dbcrDAO;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public TOSHelper getTOSHelper() {
	return tOSHelper;
    }

    public SystemDAO getSystemDAO() {
	return systemDAO;
    }

    public SystemCpuDAO getSystemCpuDAO() {
	return systemCpuDAO;
    }

    public BuildDAO getBuildDAO() {
	return buildDAO;
    }

    public PreProductionLoadsDAO getPreProductionLoadsDAO() {
	return preProductionLoadsDAO;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public FTPHelper getFTPHelper() {
	return fTPHelper;
    }

    public PlanHelper getPlanHelper() {
	return planHelper;
    }

    public SystemLoadActionsDAO getSystemLoadActionsDAO() {
	return systemLoadActionsDAO;
    }

    @Transactional
    public JSONResponse getDeploymentPlanList(String pFilter, Integer pOffset, Integer pLimit) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setMetaData(new Date());
	ArrayList<String> lStatusList = new ArrayList<>(Constants.PlanStatus.getTSSDeploymentStatus().keySet());
	// lResponse.setData(getImpPlanDAO().findByLoad(lStatusList, pFilter, false,
	// Constants.LoaderTypes.E.name(), pOffset, pLimit, false));
	lResponse.setData(getSortedTssImpPlansDetails(lStatusList, pFilter, false, Constants.LoaderTypes.E.name(), pOffset, pLimit, lStatusList));
	lResponse.setCount(getImpPlanDAO().getCountOfLoad(lStatusList, pFilter, false, Constants.LoaderTypes.E.name()));
	return lResponse;
    }

    // ZTPFM-1548
    @Transactional
    public JSONResponse getPlansDeployedInPreProdList(String pFilter, Integer pOffset, Integer pLimit) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setMetaData(new Date());
	String lPlanStatus = Constants.PlanStatus.PASSED_ACCEPTANCE_TESTING.name();
	lResponse.setData(getImpPlanDAO().findLoadByPassedAcceptance(lPlanStatus, pFilter.toUpperCase(), pOffset, pLimit));
	lResponse.setCount(getImpPlanDAO().getCountOfLoadByPassedAcceptance(lPlanStatus, pFilter));
	return lResponse;
    }

    @Transactional
    public JSONResponse getAuxDeploymentPlanList(String pFilter, Integer pOffset, Integer pLimit) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setMetaData(new Date());
	ArrayList<String> lStatusList = new ArrayList<>(Constants.PlanStatus.getTSSDeploymentStatus().keySet());
	// lResponse.setData(getImpPlanDAO().findByLoad(lStatusList, pFilter, false,
	// Constants.LoaderTypes.A.name(), pOffset, pLimit, false));
	lResponse.setData(getSortedTssImpPlansDetails(lStatusList, pFilter, false, Constants.LoaderTypes.A.name(), pOffset, pLimit, lStatusList));
	lResponse.setCount(getImpPlanDAO().getCountOfLoad(lStatusList, pFilter, false, Constants.LoaderTypes.A.name()));
	return lResponse;
    }

    private List<ImpPlan> getSortedTssImpPlansDetails(List<String> planStatus, String pFilter, boolean isDelta, String loadSetType, Integer offset, Integer limit, ArrayList<String> lStatusList) {

	List<ImpPlan> finalList = new ArrayList<>();
	List<String> processedPlans = new ArrayList<>();
	List<ImpPlan> impPlans = getImpPlanDAO().findByLoad(lStatusList, pFilter, false, loadSetType, offset, limit, false);
	LOG.info(String.join(",", lStatusList));
	// Get filtered list
	List<ImpPlan> filteredList = new ArrayList<>();
	if (impPlans.size() > 0) {
	    List<PreProductionLoads> preProdPlans = getPreProductionLoadsDAO().findByPlanId(impPlans);
	    List<PreProductionLoads> lPPLoadsToRemove = new ArrayList();
	    if (preProdPlans != null && !preProdPlans.isEmpty()) {
		Set<ImpPlan> plansTobeRemovedForReg = new HashSet();
		preProdPlans.stream().filter(lPPLoad -> Arrays.asList(Constants.PlanStatus.PASSED_FUNCTIONAL_TESTING.name(), Constants.PlanStatus.PARTIAL_FUNCTIONAL_TESTING.name(), Constants.PlanStatus.DEPLOYED_IN_QA_REGRESSION.name()).contains(lPPLoad.getPlanId().getPlanStatus())).forEach(lPPLoad -> {
		    if (lPPLoad.getSystemLoadActionsId() != null && lPPLoad.getSystemLoadActionsId().getVparId().getType().equalsIgnoreCase(Constants.VPARSEnvironment.QA_FUCTIONAL.name())) {
			lPPLoadsToRemove.add(lPPLoad);
			plansTobeRemovedForReg.add(lPPLoad.getPlanId());
		    }
		});
		if (!lPPLoadsToRemove.isEmpty()) {
		    preProdPlans.removeAll(lPPLoadsToRemove);
		}
		plansTobeRemovedForReg.forEach((lPlan) -> {
		    if (!preProdPlans.stream().filter(t -> t.getPlanId().getId().equalsIgnoreCase(lPlan.getId())).findAny().isPresent()) {
			impPlans.remove(lPlan);
		    }
		});
	    }

	    // Group by Plan id
	    HashMap<String, List<PreProductionLoads>> PlanIdandPreProdDetails = new HashMap<>();
	    preProdPlans.forEach(preProd -> {
		if (PlanIdandPreProdDetails.containsKey(preProd.getPlanId().getId())) {
		    PlanIdandPreProdDetails.get(preProd.getPlanId().getId()).add(preProd);
		} else {
		    List<PreProductionLoads> temp = new ArrayList<>();
		    temp.add(preProd);
		    PlanIdandPreProdDetails.put(preProd.getPlanId().getId(), temp);
		}
	    });

	    Set<String> preProdPlanIdSet = preProdPlans.stream().map(pre -> pre.getPlanId().getId()).collect(Collectors.toSet());
	    CopyOnWriteArrayList<String> preProdPlanIds = new CopyOnWriteArrayList<>();
	    preProdPlanIds.addAll(preProdPlanIdSet);
	    LOG.info("Pre Prod Size: " + preProdPlanIds.size());
	    // Get list of plans which isn't available in pre prod
	    List<ImpPlan> planToBeLoaded = impPlans.stream().filter(plan -> !preProdPlanIds.contains(plan.getId())).collect(Collectors.toList());

	    // Plans Which needs to be loaded(Plans status: Fallback Activated, Deleted,
	    // null)
	    impPlans.stream().filter(plan -> preProdPlanIds.contains(plan.getId())).forEach(plan -> {
		List<PreProductionLoads> preProds = PlanIdandPreProdDetails.get(plan.getId());
		if (preProds.stream().anyMatch(pre -> pre.getStatus() == null || pre.getStatus().isEmpty() || pre.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DELETED.name()) || pre.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.FALLBACK_ACTIVATED.name()))) {
		    planToBeLoaded.add(plan);
		    preProdPlanIds.remove(plan.getId());
		}
	    });
	    getImpPlanBasedOnLoadDateSorted(planToBeLoaded, finalList, processedPlans);
	    LOG.info("After planToBeLoaded Pre Prod Size: " + preProdPlanIds.size());

	    // Plan which is loaded and to be activated
	    List<ImpPlan> planToBeActivated = new ArrayList<>();
	    impPlans.stream().filter(plan -> preProdPlanIds.contains(plan.getId())).forEach(plan -> {
		List<PreProductionLoads> preProds = PlanIdandPreProdDetails.get(plan.getId());
		if (preProds.stream().anyMatch(pre -> pre.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.LOADED.name()))) {
		    planToBeActivated.add(plan);
		    preProdPlanIds.remove(plan.getId());
		}
	    });
	    getImpPlanBasedOnLoadDateSorted(planToBeActivated, finalList, processedPlans);
	    LOG.info("After planToBeActivated Pre Prod Size: " + preProdPlanIds.size());

	    // Plan which is loaded and to be Deactivated
	    List<ImpPlan> planToBeDeactivated = new ArrayList<>();
	    impPlans.stream().filter(plan -> preProdPlanIds.contains(plan.getId())).forEach(plan -> {
		List<PreProductionLoads> preProds = PlanIdandPreProdDetails.get(plan.getId());
		if (preProds.stream().anyMatch(pre -> pre.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name()))) {
		    planToBeDeactivated.add(plan);
		    preProdPlanIds.remove(plan.getId());
		}
	    });
	    getImpPlanBasedOnLoadDateSorted(planToBeDeactivated, finalList, processedPlans);
	    LOG.info("After planToBeDeactivated Pre Prod Size: " + preProdPlanIds.size());

	    List<ImpPlan> planExistInPreProd = impPlans.stream().filter(plan -> preProdPlanIds.contains(plan.getId())).collect(Collectors.toList());
	    getImpPlanBasedOnLoadDateSorted(planExistInPreProd, finalList, processedPlans);

	    LOG.info("Final List size: " + finalList.size());
	    int lStartOffset = offset * limit;
	    int size = finalList.size();
	    int lendindex = lStartOffset + limit;
	    int endValue = 0;
	    if (lStartOffset < size && lendindex < size) {
		endValue = lendindex;
	    } else if (lStartOffset < size && lendindex >= size) {
		endValue = size;
	    }
	    LOG.info("lStartOffset : " + lStartOffset + " lendindex: " + lendindex + " endValue: " + endValue);
	    if (endValue > 0) {
		for (int i = lStartOffset; i < endValue; i++) {
		    filteredList.add(finalList.get(i));
		}
	    }
	    LOG.info("Final list size: " + filteredList.size());
	}

	return filteredList;
    }

    private void getImpPlanBasedOnLoadDateSorted(List<ImpPlan> impPlans, List<ImpPlan> finalList, List<String> processedPlans) {

	HashMap<Date, List<ImpPlan>> loadDateAndPlans = new HashMap<>();
	impPlans.forEach(plan -> {

	    SystemLoad sysLoad = getSystemLoadDAO().findByImpPlan(plan).stream().min(Comparator.comparing(SystemLoad::getLoadDateTime)).get();
	    if (sysLoad != null) {
		if (loadDateAndPlans.containsKey(sysLoad.getLoadDateTime())) {
		    loadDateAndPlans.get(sysLoad.getLoadDateTime()).add(plan);
		} else {
		    List<ImpPlan> tempList = new ArrayList<>();
		    tempList.add(plan);
		    loadDateAndPlans.put(sysLoad.getLoadDateTime(), tempList);
		}
	    }
	});

	List<Date> loadDates = new ArrayList<>(loadDateAndPlans.keySet());
	loadDates.sort((p1, p2) -> ((Date) p1).compareTo((Date) p2));
	loadDates.forEach(loadDate -> {
	    if (loadDateAndPlans.containsKey(loadDate)) {
		loadDateAndPlans.get(loadDate).forEach(plan -> {
		    if (!processedPlans.contains(plan.getId())) {
			finalList.add(plan);
			processedPlans.add(plan.getId());
		    }
		});
	    }
	});
    }

    @Transactional
    public JSONResponse deleteActivationAction(User pUser, Integer pLoad) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);

	try {
	    PreProductionLoads lPreProdAction = getPreProductionLoadsDAO().find(pLoad);
	    if (lPreProdAction != null) {
		getPreProductionLoadsDAO().delete(pUser, pLoad);
		if (lPreProdAction.getSystemLoadActionsId() == null) {
		    // TOS UNDO for TSS DEPLOY ONLY
		    getPlanHelper().updatePlanAsDeployedInPreProd(pUser, lPreProdAction.getPlanId().getId(), Constants.VPARSEnvironment.PRE_PROD, false);
		}
	    } else {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("Unable to remove, Invalid PreProduction Id");
	    }
	} catch (Exception ex) {
	    LOG.error("unable to remove preProductionActions id - " + pLoad);
	    throw new WorkflowException("Unable to remove the test system from Plan");
	}
	return lResponse;
    }

    // @Transactional
    // public JSONResponse getSystemLoadListBySystemId(Integer pOffset, Integer
    // pLimit, LinkedHashMap<String, String> pOrderBy) {
    // JSONResponse lResponse = new JSONResponse();
    // lResponse.setStatus(Boolean.TRUE);
    // lResponse.setMetaData(new Date());
    // lResponse.setData(getSystemLoadDAO().findByStatus(Constants.PlanStatus.PASSED_REGRESSION_TESTING.name(),
    // pOffset, pLimit));
    // lResponse.setCount(getSystemLoadDAO().getCountByStatus(Constants.PlanStatus.PASSED_REGRESSION_TESTING.name()));
    // return lResponse;
    // }
    @Transactional
    public JSONResponse getPreProductionLoads(String[] ids) {
	JSONResponse lResponse = new JSONResponse();

	List<PreProductionLoadsForm> lLoadsFormList = new ArrayList<>();
	List<PreProductionLoads> preProductionLoads = getPreProductionLoadsDAO().findByPlanId(ids);
	// Remove QA Functional Records from PreProductionLoads where plan in QA
	// Regression Page
	List<PreProductionLoads> lPPLoadsToRemove = new ArrayList();
	if (preProductionLoads != null && !preProductionLoads.isEmpty()) {
	    preProductionLoads.stream().filter(lPPLoad -> Arrays.asList(Constants.PlanStatus.PASSED_FUNCTIONAL_TESTING.name(), Constants.PlanStatus.PARTIAL_FUNCTIONAL_TESTING.name(), Constants.PlanStatus.DEPLOYED_IN_QA_REGRESSION.name()).contains(lPPLoad.getPlanId().getPlanStatus())).forEach(lPPLoad -> {
		if (lPPLoad.getSystemLoadActionsId() != null && lPPLoad.getSystemLoadActionsId().getVparId().getType().equalsIgnoreCase(Constants.VPARSEnvironment.QA_FUCTIONAL.name())) {
		    lPPLoadsToRemove.add(lPPLoad);
		}
	    });
	    if (!lPPLoadsToRemove.isEmpty()) {
		preProductionLoads.removeAll(lPPLoadsToRemove);
	    }
	}

	List<Object[]> planNotLoaded = getPreProductionLoadsDAO().plansNotInPreProd(ids);
	List<PreProductionLoads> plansNotInPreProd = new ArrayList<PreProductionLoads>();
	for (Object[] preProd : planNotLoaded) {
	    LOG.info(preProd[0].toString() + preProd[1].toString() + preProd[2].toString());
	    String planId = preProd[0].toString();
	    int systemId = Integer.parseInt(preProd[1].toString());
	    int sysCpuId = Integer.parseInt(preProd[2].toString());
	    SystemCpu sysCpu = new SystemCpu();
	    System sId = new System();
	    sysCpu.setId(sysCpuId);
	    sId.setId(systemId);
	    PreProductionLoads lPreProductionLoads = new PreProductionLoads();
	    lPreProductionLoads.setPlanId(getImpPlanDAO().find(planId));
	    lPreProductionLoads.setSystemId(sId);
	    lPreProductionLoads.setCpuId(sysCpu);
	    plansNotInPreProd.add(lPreProductionLoads);
	}
	preProductionLoads.addAll(plansNotInPreProd);
	Map<ImpPlan, List<PreProductionLoads>> collect = preProductionLoads.stream().collect(Collectors.groupingBy(t -> t.getPlanId()));

	HashMap<String, List<Integer>> preProdPlansAndSys = new HashMap<>();

	List<Object[]> planByStatus = getPreProductionLoadsDAO().getPreProdSystemByPlanStatus(ids);
	for (Object[] lpreProd : planByStatus) {
	    String lplanId = lpreProd[0].toString();
	    int lsystemId = Integer.parseInt(lpreProd[1].toString());

	    if (preProdPlansAndSys.containsKey(lplanId)) {
		preProdPlansAndSys.get(lplanId).add(lsystemId);
	    } else {
		List<Integer> tempSys = new ArrayList<>();
		tempSys.add(lsystemId);
		preProdPlansAndSys.put(lplanId, tempSys);
	    }

	}
	for (Map.Entry<ImpPlan, List<PreProductionLoads>> entrySet : collect.entrySet()) {
	    PreProductionLoadsForm loadsForm = new PreProductionLoadsForm();
	    ImpPlan lPlan = entrySet.getKey();
	    List<PreProductionLoads> lLoads = entrySet.getValue();
	    loadsForm.setPlan(lPlan);
	    if (preProdPlansAndSys.containsKey(lPlan.getId())) {
		List<PreProductionLoads> preLoads = new ArrayList<>();
		lLoads.forEach(preProd -> {
		    if ((preProdPlansAndSys.get(lPlan.getId()).contains(preProd.getSystemId().getId()))) {
			preLoads.add(preProd);
		    }
		});
		loadsForm.setPreProductionLoadsList(preLoads);
	    } else {
		loadsForm.setPreProductionLoadsList(lLoads);
	    }

	    loadsForm.setIsAnyLoadsInProgress(lLoads.stream().filter(t -> t.getLastActionStatus() != null && t.getLastActionStatus().equals("INPROGRESS")).count() > 0);
	    if (!loadsForm.getPreProductionLoadsList().isEmpty()) {
		lLoadsFormList.add(loadsForm);
	    }

	}

	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(lLoadsFormList);
	return lResponse;
    }

    @Transactional
    public JSONResponse getDeploymentVParsList(User lUser, String[] pPlanIds) {
	HashMap<String, List<SystemCpu>> lReturn = new HashMap<>();
	JSONResponse response = new JSONResponse();
	for (String lPlanId : pPlanIds) {
	    List<SystemCpu> lVpars = new ArrayList<>();
	    ImpPlan lPlan = getImpPlanDAO().find(lPlanId);
	    Constants.BUILD_TYPE lBuildType = Constants.BUILD_TYPE.STG_LOAD;
	    Constants.TOSEnvironment lVparsType = Constants.TOSEnvironment.PRE_PROD_TOS;

	    if (lBuildType != null) {
		List<Build> buildList = getBuildDAO().findAll(lPlan, lBuildType);
		if (!buildList.isEmpty()) {
		    List<com.tsi.workflow.beans.dao.System> lSystems = new ArrayList<>();
		    for (Build b : buildList) {
			if (b.getJobStatus() != null && b.getJobStatus().equals("S")) {
			    lSystems.add(b.getSystemId());
			}
		    }
		    if (!lSystems.isEmpty()) {
			lVpars = new ArrayList<>(getSystemCpuDAO().findBySystem(lSystems, lVparsType));
		    } else {
			LOG.info("No Success Build found for the plan " + lPlanId);
		    }
		}
	    }
	    Collections.sort(lVpars);
	    lReturn.put(lPlanId, lVpars);
	}
	response.setStatus(Boolean.TRUE);
	response.setData(lReturn);
	return response;
    }

    @Transactional
    public JSONResponse getTosDeploymentPreProdVParsList(User lUser, String[] pPlanIds, Boolean isDeployedInPreProdLoads) {
	HashMap<String, List<SystemCpu>> lReturnCpu = new HashMap<>();
	HashMap<String, List<Vpars>> lReturnVpar = new HashMap<>();
	Boolean delta = Boolean.FALSE;
	Boolean traveport = Boolean.FALSE;
	JSONResponse response = new JSONResponse();
	for (String lPlanId : pPlanIds) {
	    List<SystemCpu> lVpars = new ArrayList<>();
	    List<Vpars> lVparsList = new ArrayList<>();
	    ImpPlan lPlan = getImpPlanDAO().find(lPlanId);
	    List<SystemLoad> lSysLoad = getSystemLoadDAO().findByImpPlan(lPlan);
	    String lCompany = lSysLoad.get(0).getSystemId().getPlatformId().getNickName();

	    if (isDeployedInPreProdLoads && lCompany.equals("tp")) {
		List<PreProductionLoads> lPreProdList = getPreProductionLoadsDAO().findByPlanId(lPlan);
		for (PreProductionLoads lLoadedList : lPreProdList) {
		    SystemCpu lCpu = lLoadedList.getCpuId();
		    lVpars.add(lCpu);
		    Collections.sort(lVpars);
		    lReturnCpu.put(lPlanId, lVpars);
		    traveport = Boolean.TRUE;
		}
	    } else if (isDeployedInPreProdLoads && lCompany.equals("dl")) {
		List<SystemLoadActions> lSysLoadActionList = getSystemLoadActionsDAO().findByPlanAndEnv(lPlanId, Constants.VPARSEnvironment.PRE_PROD);
		for (SystemLoadActions lLoadedList : lSysLoadActionList) {
		    Vpars lList = lLoadedList.getVparId();
		    lVparsList.add(lList);
		    Collections.sort(lVparsList);
		    lReturnVpar.put(lPlanId, lVparsList);
		    delta = Boolean.TRUE;
		}
	    }
	}
	if (traveport) {
	    response.setData(lReturnCpu);
	} else if (delta) {
	    response.setData(lReturnVpar);
	}
	response.setStatus(Boolean.TRUE);
	return response;
    }

    @Transactional
    public JSONResponse postActivationAction(User lUser, PreProductionLoads lLoadSet, Boolean isAux) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setUserId(lUser.getId());

	if (lLoadSet.getStatus() == null || lLoadSet.getStatus().isEmpty()) {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Please select a valid action");
	    return lResponse;
	}
	Constants.LOAD_SET_STATUS lStatus = Constants.LOAD_SET_STATUS.valueOf(lLoadSet.getStatus());

	String lOldStatus = null;
	if (lLoadSet.getId() != null) {
	    PreProductionLoads lOldLoad = null;
	    lOldLoad = getPreProductionLoadsDAO().find(lLoadSet.getId());
	    if (lOldLoad != null) {
		if (lOldLoad.getCpuId() != null) {
		    if (lOldLoad.getLastActionStatus() != null && lOldLoad.getLastActionStatus().equals("INPROGRESS")) {
			lResponse.setStatus(Boolean.FALSE);
			lResponse.setErrorMessage("Last Action - " + lOldLoad.getStatus() + " is in progress");
			return lResponse;
		    }
		    if (lOldLoad.getStatus().equals(lLoadSet.getStatus())) {
			lResponse.setStatus(Boolean.FALSE);
			lResponse.setErrorMessage("Loadset is already in status " + lLoadSet.getStatus());
			return lResponse;
		    }
		}
		lOldStatus = lOldLoad.getStatus();
	    }
	}

	if (lLoadSet.getId() == null) {
	    lOldStatus = Constants.LOAD_SET_STATUS.DELETED.name();
	    getPreProductionLoadsDAO().save(lUser, lLoadSet);
	}
	SystemLoad lSystemLoad = getSystemLoadDAO().find(lLoadSet.getSystemLoadId().getId());
	PreProdActionsActivityLog preProdMessage = new PreProdActionsActivityLog(lLoadSet.getPlanId(), null);
	boolean ftpFail = false;
	switch (lStatus) {
	case LOADED: {
	    LOG.info("Calling IBM PRE PROD TOS Load Activate : " + lSystemLoad.getLoadSetName());

	    SortedSet<String> deptPlans = checkDeptPlanCurrentStatus(lLoadSet, "Load");

	    if (deptPlans.size() > 0) {
		StringBuffer sb = new StringBuffer();
		sb.append(lLoadSet.getPlanId().getId()).append("/").append(lSystemLoad.getSystemId().getName());
		sb.append(" cannot be loaded to Pre-Prod as dependent Plan(s) ");
		sb.append(StringUtils.join(deptPlans, ", "));
		sb.append(" are not yet loaded in Pre-Prod. Please contact leads of dependent plans to get them loaded in Pre-Prod ");

		preProdMessage.setStatus(false);
		preProdMessage.setMessage(sb.toString());
		getActivityLogDAO().save(lUser, preProdMessage);

		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage(sb.toString());
		LOG.info("PreProd Status in dependent plan: " + lLoadSet.getStatus());
		lLoadSet.setStatus(lOldStatus);
		lLoadSet.setLastActionStatus("FAILED");
	    } else {
		if (lOldStatus == null) {
		    lOldStatus = Constants.LOAD_SET_STATUS.DELETED.name();
		}
		boolean ip = getTOSHelper().requestPreProdIP(lUser, lSystemLoad, lLoadSet.getCpuId());
		if (!ip) {
		    LOG.error("Unable to get IP Adress for system " + lSystemLoad.getSystemId().getName());
		    throw new WorkflowException("Unable to request for system " + lSystemLoad.getSystemId().getName());
		}

		String ipAddress = getTOSHelper().getIP(lSystemLoad.getId());

		if (ipAddress.isEmpty()) {
		    LOG.error("Unable to get IP Adress for system " + lSystemLoad.getSystemId().getName());
		    throw new WorkflowException("Unable to get IP Address for system " + lSystemLoad.getSystemId().getName());
		}

		Constants.BUILD_TYPE lBuildType = Constants.BUILD_TYPE.STG_LOAD;
		Build lBuild = getBuildDAO().findLastSuccessfulBuild(lLoadSet.getPlanId().getId(), lSystemLoad.getSystemId().getId(), lBuildType);
		if (lBuild == null) {
		    throw new WorkflowException("Staging loadset not created");
		}
		ProdFTPActivityMessage lMessage = new ProdFTPActivityMessage(lLoadSet.getPlanId(), null, lSystemLoad);
		lMessage.setIpAddress(ipAddress);
		getActivityLogDAO().save(lUser, lMessage);
		JSONResponse lSSHResponse = getFTPHelper().doFTP(lUser, lSystemLoad, lBuild, ipAddress, false);
		if (!lSSHResponse.getStatus()) {
		    lMessage.setStatus("Failed");
		    getActivityLogDAO().save(lUser, lMessage);
		    lLoadSet.setLastActionStatus("FAILED");
		    ftpFail = true;
		} else {
		    lMessage.setStatus("Success");
		    getActivityLogDAO().save(lUser, lMessage);
		    getFTPHelper().getYodaLoadSetPath(lUser, lLoadSet.getPlanId(), lSSHResponse);
		    lLoadSet.setLastActionStatus("FAILED");
		}

		if (!isAux && ftpFail) {
		    lResponse.setStatus(Boolean.FALSE);
		    lResponse.setErrorMessage("FTP Failed for the Loadset " + lSystemLoad.getLoadSetName() + ", System " + lSystemLoad.getSystemId().getName());
		    setStatus(false, lLoadSet, lOldStatus);
		} else if (!isAux && !ftpFail) {
		    lAsyncPlansStartTimeMap.put(lLoadSet.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoadSet.getStatus(), java.lang.System.currentTimeMillis());
		    boolean lResult = getTOSHelper().doPreTOSOperation(lUser, Constants.LoadSetCommands.LOAD, lLoadSet, lOldStatus, lSystemLoad, false);
		    setStatus(lResult, lLoadSet, lOldStatus);
		} else if (isAux && ftpFail) {
		    lLoadSet.setStatus("DELETED");
		    lLoadSet.setLastActionStatus("FAILED");
		    lResponse.setStatus(Boolean.FALSE);
		    lResponse.setErrorMessage("FTP Failed for the Loadset " + lSystemLoad.getLoadSetName() + ", System " + lSystemLoad.getSystemId().getName());
		} else if (isAux && !ftpFail) {
		    lLoadSet.setStatus(Constants.LOAD_SET_STATUS.ACTIVATED.name());
		    lLoadSet.setLastActionStatus("SUCCESS");
		    lResponse.setStatus(Boolean.TRUE);
		    lResponse.setData(new TOSResult("FTP", lSystemLoad.getLoadSetName(), 0, "Success", lSystemLoad.getSystemId().getName(), "", false));
		}
	    }
	    break;
	}
	case ACTIVATED: {
	    LOG.info("Calling IBM PRE PROD TOS Activate : " + lSystemLoad.getSystemId().getName());
	    SortedSet<String> deptPlans = checkDeptPlanCurrentStatus(lLoadSet, "Activate");

	    if (deptPlans.size() > 0) {
		StringBuffer sb = new StringBuffer();
		sb.append(lLoadSet.getPlanId().getId()).append("/").append(lSystemLoad.getSystemId().getName());
		sb.append(" cannot be Activated to Pre-Prod as dependent Plan(s) ");
		sb.append(StringUtils.join(deptPlans, ", "));
		sb.append(" are not yet Activated in Pre-Prod. Please contact leads of dependent plans to get them Activated in Pre-Prod ");

		preProdMessage.setStatus(false);
		preProdMessage.setMessage(sb.toString());
		getActivityLogDAO().save(lUser, preProdMessage);

		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage(sb.toString());
		lLoadSet.setStatus(lOldStatus);
		lLoadSet.setLastActionStatus("FAILED");
	    } else if (preProdDBCRValidate(lLoadSet.getPlanId().getId(), lUser, lSystemLoad)) {
		lAsyncPlansStartTimeMap.put(lLoadSet.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoadSet.getStatus(), java.lang.System.currentTimeMillis());
		boolean lResult = getTOSHelper().doPreTOSOperation(lUser, Constants.LoadSetCommands.ACTIVATE, lLoadSet, lOldStatus, lSystemLoad, false);
		setStatus(lResult, lLoadSet, lOldStatus);
	    }

	    break;
	}
	case DEACTIVATED: {
	    LOG.info("Calling IBM PRE PROD TOS Deactivate : " + lSystemLoad.getLoadSetName());
	    SortedSet<String> deptPlans = checkDeptPlanCurrentStatus(lLoadSet, "Deactivate");
	    LOG.info("Dept plans before deactivate: ");
	    if (deptPlans.size() > 0) {
		StringBuffer sb = new StringBuffer();
		sb.append(lLoadSet.getPlanId().getId()).append("/").append(lSystemLoad.getSystemId().getName());
		sb.append(" cannot be Deactivated to Pre-Prod as dependent Plan(s) ");
		sb.append(StringUtils.join(deptPlans, ", "));
		sb.append(" are not yet Deactivated in Pre-Prod. Please contact leads of dependent plans to get them Deactivated in Pre-Prod ");

		preProdMessage.setStatus(false);
		preProdMessage.setMessage(sb.toString());
		getActivityLogDAO().save(lUser, preProdMessage);

		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage(sb.toString());
		lLoadSet.setStatus(lOldStatus);
		lLoadSet.setLastActionStatus("FAILED");
	    } else {
		lAsyncPlansStartTimeMap.put(lLoadSet.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoadSet.getStatus(), java.lang.System.currentTimeMillis());
		boolean lResult = getTOSHelper().doPreTOSOperation(lUser, Constants.LoadSetCommands.DEACTIVATE, lLoadSet, lOldStatus, lSystemLoad, false);
		setStatus(lResult, lLoadSet, lOldStatus);
	    }

	    break;
	}
	case DELETED: {
	    LOG.info("Calling IBM PRE PROD TOS Delete DeActivate : " + lSystemLoad.getLoadSetName());
	    SortedSet<String> deptPlans = checkDeptPlanCurrentStatus(lLoadSet, "Deleted");

	    if (deptPlans.size() > 0) {
		StringBuffer sb = new StringBuffer();
		sb.append(lLoadSet.getPlanId().getId()).append("/").append(lSystemLoad.getSystemId().getName());
		sb.append(" cannot be deleted to Pre-Prod as dependent Plan(s) ");
		sb.append(StringUtils.join(deptPlans, ", "));
		sb.append(" are not yet deleted in Pre-Prod. Please contact leads of dependent plans to get them deleted in Pre-Prod ");

		preProdMessage.setStatus(false);
		preProdMessage.setMessage(sb.toString());
		getActivityLogDAO().save(lUser, preProdMessage);

		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage(sb.toString());
		lLoadSet.setStatus(lOldStatus);
		lLoadSet.setLastActionStatus("FAILED");
	    } else {
		lAsyncPlansStartTimeMap.put(lLoadSet.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoadSet.getStatus(), java.lang.System.currentTimeMillis());
		boolean lResult = getTOSHelper().doPreTOSOperation(lUser, Constants.LoadSetCommands.DELETE, lLoadSet, lOldStatus, lSystemLoad, false);
		setStatus(lResult, lLoadSet, lOldStatus);
	    }
	    break;
	}
	default:
	    break;
	}

	getPreProductionLoadsDAO().update(lUser, lLoadSet);
	wsserver.sendMessage(Constants.Channels.PRE_PROD_LOAD, lUser, lResponse);

	if (lResponse.getErrorMessage() == null || lResponse.getErrorMessage().isEmpty()) {
	    lResponse.setStatus(Boolean.TRUE);
	}

	return lResponse;
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

    public SortedSet<String> checkDeptPlanCurrentStatus(PreProductionLoads lLoadSet, String status) {
	SortedSet<String> lSet = new TreeSet<String>();
	List<String> split = Arrays.asList(lLoadSet.getPlanId().getRelatedPlans().split(","));
	System lSystem = getSystemDAO().find(lLoadSet.getSystemId().getId());
	if (status.equalsIgnoreCase("Load")) {
	    if (!split.isEmpty()) {
		List<Object[]> relatedPlanDetails = getImpPlanDAO().getAllRelatedPlanDetail(lLoadSet.getPlanId().getId(), split);
		for (Object[] plan : relatedPlanDetails) {
		    String lPlanId = plan[0].toString().split("/")[0].toUpperCase();
		    String lSystemName = plan[0].toString().split("/")[1].toUpperCase();
		    String planStatus = plan[1].toString();

		    ImpPlan lImpPlan = getImpPlanDAO().find(lPlanId);
		    if (lImpPlan.getMacroHeader()) {
			continue;
		    }
		    if (lSystemName.equalsIgnoreCase(lSystem.getName()) && (Constants.PlanStatus.getApprovedToPreProd().containsKey(planStatus))) {
			List<PreProductionLoads> lPreProdList = getPreProductionLoadsDAO().findByPlanIdAndSystemId(new ImpPlan(lPlanId), lSystem);
			if (lPreProdList == null || lPreProdList.isEmpty()) {
			    lSet.add(lPlanId);
			} else {
			    for (PreProductionLoads lPreProd : lPreProdList) {
				if (!Constants.LOAD_SET_STATUS.getLoadAndAbove().contains(lPreProd.getStatus())) {
				    lSet.add(lPlanId);
				}
			    }
			}
		    }
		}
	    }

	    List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPreSegmentRelatedPlans(lLoadSet.getPlanId().getId());
	    for (Object[] plan : segmentRelatedPlans) {
		String lPlanId = plan[0].toString().split("/")[0].toUpperCase();
		String lSystemName = plan[0].toString().split("/")[1].toUpperCase();
		String planStatus = plan[1] != null ? plan[1].toString() : null;

		ImpPlan lImpPlan = getImpPlanDAO().find(lPlanId);
		if (lImpPlan.getMacroHeader()) {
		    continue;
		}
		if (lSystemName.equalsIgnoreCase(lSystem.getName()) && (Constants.PlanStatus.getApprovedToPreProd().containsKey(planStatus))) {
		    List<PreProductionLoads> lPreProdList = getPreProductionLoadsDAO().findByPlanIdAndSystemId(new ImpPlan(lPlanId), lSystem);
		    if (lPreProdList == null || lPreProdList.isEmpty()) {
			lSet.add(lPlanId);
		    } else {
			for (PreProductionLoads lPreProd : lPreProdList) {
			    if (!Constants.LOAD_SET_STATUS.getLoadAndAbove().contains(lPreProd.getStatus())) {
				lSet.add(lPlanId);
			    }
			}
		    }
		}
	    }
	} else if (status.equalsIgnoreCase("Activate")) {
	    // ZTPFM-1816 Dependent check
	    if (!split.isEmpty()) {
		List<Object[]> relatedPlanDetails = getImpPlanDAO().getAllRelatedPlanDetail(lLoadSet.getPlanId().getId(), split);
		for (Object[] plan : relatedPlanDetails) {
		    String lPlanId = plan[0].toString().split("/")[0].toUpperCase();
		    String lSystemName = plan[0].toString().split("/")[1].toUpperCase();
		    String planStatus = plan[1].toString();

		    if (lSystemName.equalsIgnoreCase(lSystem.getName()) && (Constants.PlanStatus.getApprovedToPreProd().containsKey(planStatus))) {
			List<PreProductionLoads> lPreProdList = getPreProductionLoadsDAO().findByPlanId(lPlanId);
			ImpPlan lImpPlan = getImpPlanDAO().find(lPlanId);
			if (lImpPlan.getMacroHeader()) {
			    continue;
			}
			if (lPreProdList == null || lPreProdList.isEmpty()) {
			    lSet.add(lPlanId);
			} else {
			    for (PreProductionLoads lPreProd : lPreProdList) {
				if (!Constants.LOAD_SET_STATUS.getActivateAndAbove().contains(lPreProd.getStatus())) {
				    lSet.add(lPlanId);
				}
			    }
			}
		    }
		}
	    }

	    List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPreSegmentRelatedPlans(lLoadSet.getPlanId().getId());
	    for (Object[] plan : segmentRelatedPlans) {
		String lPlanId = plan[0].toString().split("/")[0].toUpperCase();
		String lSystemName = plan[0].toString().split("/")[1].toUpperCase();
		String planStatus = plan[1] != null ? plan[1].toString() : null;

		ImpPlan lImpPlan = getImpPlanDAO().find(lPlanId);
		if (lImpPlan.getMacroHeader()) {
		    continue;
		}
		if (lSystemName.equalsIgnoreCase(lSystem.getName()) && (Constants.PlanStatus.getApprovedToPreProd().containsKey(planStatus))) {
		    List<PreProductionLoads> lPreProdList = getPreProductionLoadsDAO().findByPlanIdAndSystemId(new ImpPlan(lPlanId), lSystem);
		    if (lPreProdList == null || lPreProdList.isEmpty()) {
			lSet.add(lPlanId);
		    } else {
			for (PreProductionLoads lPreProd : lPreProdList) {
			    if (!Constants.LOAD_SET_STATUS.getActivateAndAbove().contains(lPreProd.getStatus())) {
				lSet.add(lPlanId);
			    }
			}
		    }
		}
	    }
	} else if (status.equalsIgnoreCase("Deactivate")) {
	    List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPostSegmentRelatedPlansPreProdLoad(lLoadSet.getPlanId().getId());
	    for (Object[] plan : segmentRelatedPlans) {
		String lPlanId = plan[0].toString().split("/")[0].toUpperCase();
		String lSystemName = plan[0].toString().split("/")[1].toUpperCase();
		String planStatus = plan[1].toString();
		if (lSystemName.equalsIgnoreCase(lSystem.getName()) && Constants.PlanStatus.getApprovedToPreProd().containsKey(planStatus)) {
		    List<PreProductionLoads> lPreProdList = getPreProductionLoadsDAO().findByPlanIdAndSystemId(new ImpPlan(lPlanId), lSystem);
		    if (lPreProdList != null && !lPreProdList.isEmpty()) {
			for (PreProductionLoads lPreProd : lPreProdList) {
			    if (!Constants.LOAD_SET_STATUS.getDeactivateAndAbove().contains(lPreProd.getStatus())) {
				lSet.add(plan[0].toString());
			    }
			}
		    }
		}
	    }
	} else if (status.equalsIgnoreCase("Deleted")) {
	    List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPostSegmentRelatedPlansPreProdLoad(lLoadSet.getPlanId().getId());
	    for (Object[] plan : segmentRelatedPlans) {
		String lPlanId = plan[0].toString().split("/")[0].toUpperCase();
		String lSystemName = plan[0].toString().split("/")[1].toUpperCase();
		String planStatus = plan[1].toString();
		if (lSystemName.equalsIgnoreCase(lSystem.getName()) && Constants.PlanStatus.getApprovedToPreProd().containsKey(planStatus)) {
		    List<PreProductionLoads> lPreProdList = getPreProductionLoadsDAO().findByPlanIdAndSystemId(new ImpPlan(lPlanId), lSystem);
		    if (lPreProdList != null && !lPreProdList.isEmpty()) {
			for (PreProductionLoads lPreProd : lPreProdList) {
			    if (!Constants.LOAD_SET_STATUS.getDeleteAndAbove().contains(lPreProd.getStatus())) {
				lSet.add(plan[0].toString());
			    }
			}
		    }
		}
	    }
	}
	return lSet;

    }

    private boolean preProdDBCRValidate(String pId, User lUser, SystemLoad systemLoad) {
	ImpPlan lPlan = getImpPlanDAO().find(pId);
	List<Dbcr> dbcrList = getDbcrDAO().findByPlanSystemEnvironment(lPlan.getId(), systemLoad.getSystemId().getId(), Constants.DBCR_ENVIRONMENT.COPY.name());
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
		dbcrMessage.setEnvironment(Constants.DBCR_ENVIRONMENT.COPY.getValue());
		getActivityLogDAO().save(lUser, dbcrMessage);
		return false;
	    }
	}
	return true;
    }

    @Transactional
    public JSONResponse postTOSOperationProcess(User user, PreProdTOSForm preProdLoads) {
	JSONResponse lResponse = new JSONResponse();
	Map<String, Set<TosActionQueue>> lQueueList = new HashMap();
	String action = preProdLoads.getAction();
	Set<String> lCurrentPlanList = new HashSet();

	preProdLoads.getPreProdLoads().forEach((lParamPreProds) -> {
	    lCurrentPlanList.add(lParamPreProds.getPlanId().getId());
	    LOG.info(lParamPreProds.getPlanId().getId());
	});
	LOG.info("List of Plans " + lCurrentPlanList.toString());

	try {
	    // Pre Validations
	    for (PreProductionLoads lPreProdLoad : preProdLoads.getPreProdLoads()) {
		SystemLoad lSystemLoad = getSystemLoadDAO().findBy(lPreProdLoad.getPlanId(), lPreProdLoad.getSystemId());
		System lSystem = lSystemLoad.getSystemId();
		String lOldStatus = action;
		ImpPlan lPlan = getImpPlanDAO().find(lPreProdLoad.getPlanId().getId());

		LOG.info("Dependency Valdiation Started");

		SortedSet<String> deptPlans = new TreeSet<String>();
		List<String> split = Arrays.asList(lPlan.getRelatedPlans().split(","));
		if (!split.isEmpty()) {
		    List<Object[]> relatedPlanDetails = getImpPlanDAO().getAllRelatedPlanDetail(lPreProdLoad.getPlanId().getId(), split);
		    for (Object[] plan : relatedPlanDetails) {
			String lPlanId = plan[0].toString().split("/")[0].toUpperCase();
			String lSystemName = plan[0].toString().split("/")[1].toUpperCase();
			String planStatus = plan[1].toString();
			ImpPlan lImpPlan = getImpPlanDAO().find(lPlanId);
			if (lImpPlan.getMacroHeader()) {
			    continue;
			}

			if (lSystemName.equalsIgnoreCase(lSystem.getName()) && Constants.PlanStatus.getApprovedToPreProd().containsKey(planStatus)) {
			    List<PreProductionLoads> lPreProdList = getPreProductionLoadsDAO().findByPlanIdAndSystemId(new ImpPlan(lPlanId), lSystemLoad.getSystemId());
			    if (lPreProdList == null || lPreProdList.isEmpty()) {
				deptPlans.add(lPlanId);
			    } else {
				for (PreProductionLoads lPreProd : lPreProdList) {
				    if (action.equalsIgnoreCase(Constants.LOAD_SET_STATUS.LOADED.name()) && (!Constants.LOAD_SET_STATUS.getLoadAndAbove().contains(lPreProd.getStatus()))) {
					deptPlans.add(lPlanId);
				    } else if ((action.equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name())) && (!Constants.LOAD_SET_STATUS.getActivateAndAbove().contains(lPreProd.getStatus()))) {
					deptPlans.add(lPlanId);
				    }
				}
			    }
			}
		    }
		}

		List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPreSegmentRelatedPlans(lPlan.getId());
		for (Object[] plan : segmentRelatedPlans) {
		    String lPlanId = plan[0].toString().split("/")[0].toUpperCase();
		    String lSystemName = plan[0].toString().split("/")[1].toUpperCase();
		    String planStatus = plan[1].toString();

		    ImpPlan lImpPlan = getImpPlanDAO().find(lPlanId);
		    if (lImpPlan.getMacroHeader()) {
			continue;
		    }
		    if (lSystemName.equalsIgnoreCase(lSystemLoad.getSystemId().getName()) && Constants.PlanStatus.getApprovedToPreProd().containsKey(planStatus)) {
			List<PreProductionLoads> lLoadedList = getPreProductionLoadsDAO().findByPlanIdAndSystemId(new ImpPlan(lPlanId), lSystemLoad.getSystemId());
			if (lLoadedList == null || lLoadedList.isEmpty()) {
			    deptPlans.add(lPlanId);
			} else {
			    for (PreProductionLoads lLoaded : lLoadedList) {
				LOG.info(lLoaded.getPlanId().getId() + " " + lLoaded.getStatus() + " " + lLoaded.getLastActionStatus());
				if ((lLoaded.getStatus() == null)) {
				    deptPlans.add(lPlanId);
				} else if (action.equalsIgnoreCase(Constants.LOAD_SET_STATUS.LOADED.name()) && (!Constants.LOAD_SET_STATUS.getLoadAndAbove().contains(lLoaded.getStatus()))) {
				    deptPlans.add(lPlanId);
				} else if ((action.equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name())) && (!Constants.LOAD_SET_STATUS.getActivateAndAbove().contains(lLoaded.getStatus()))) {
				    deptPlans.add(lPlanId);
				}
			    }
			}
		    }
		}
		Boolean isPlanNotInProcessList = false;
		List<String> lErrorList = new ArrayList();
		if (deptPlans.size() > 0) {
		    LOG.info("List of Dependendt Plans - " + deptPlans.toString());
		    for (String lTPlan : deptPlans) {
			if (!lCurrentPlanList.contains(lTPlan)) {
			    lErrorList.add(lTPlan);
			    isPlanNotInProcessList = true;
			}
		    }
		}
		if (isPlanNotInProcessList) {
		    StringBuilder sb = new StringBuilder();
		    sb.append(lPreProdLoad.getPlanId().getId()).append("/").append(lSystemLoad.getSystemId().getName()).append(" cannot be ").append(action).append(" in Pre-Prod until ").append(StringUtils.join(lErrorList, ", ")).append(" are ").append(action).append(" in Pre-Prod");
		    LOG.info("Dependency Error - " + sb.toString());
		    LOG.info("PreProd Status in dependent plan: " + lPreProdLoad.getStatus());

		    PreProdActionsActivityLog preProdMessage = new PreProdActionsActivityLog(lPreProdLoad.getPlanId(), null);
		    preProdMessage.setStatus(false);
		    preProdMessage.setMessage(sb.toString());
		    getActivityLogDAO().save(user, preProdMessage);

		    // lResponse.setStatus(Boolean.FALSE);
		    // lResponse.setErrorMessage(sb.toString());
		    // return lResponse;
		    throw new WorkflowException(sb.toString());

		} else {
		    if (lPreProdLoad.getId() == null) {
			lPreProdLoad.setStatus(Constants.LOAD_SET_STATUS.DELETED.name());
			lPreProdLoad.setLastActionStatus("SUCCESS");
			getPreProductionLoadsDAO().save(user, lPreProdLoad);
			lOldStatus = Constants.LOAD_SET_STATUS.DELETED.name();
		    } else {
			PreProductionLoads lDBPreProd = new PreProductionLoads();
			lDBPreProd = getPreProductionLoadsDAO().find(lPreProdLoad.getId());
			if (lDBPreProd.getLastActionStatus() != null && lDBPreProd.getLastActionStatus().equalsIgnoreCase("INPROGRESS")) {
			    throw new WorkflowException("Previous action - " + lDBPreProd.getStatus() + " is INPROGRESS for plan - " + lDBPreProd.getPlanId().getId() + " , kindly wait until previous action get complete");
			}
			lOldStatus = lDBPreProd.getStatus();
			getPreProductionLoadsDAO().update(user, lPreProdLoad);

		    }
		}
		LOG.info("Dependency Valdiation completed");
		// SystemLoad Vs PreProductionLoads needs to be used for Sorting if needed
		TosActionQueue lPPActionQueue = new TosActionQueue(user, lOldStatus, lPreProdLoad.getId());
		String lKey = lPreProdLoad.getPlanId().getId() + "_" + lSystemLoad.getSystemId().getId();
		if (lQueueList.containsKey(lKey)) {
		    lQueueList.get(lKey).add(lPPActionQueue);
		} else {
		    Set<TosActionQueue> lTempPreProdIds = new HashSet();
		    lTempPreProdIds.add(lPPActionQueue);
		    lQueueList.put(lKey, lTempPreProdIds);
		}
	    }

	    // Adding into Queue
	    for (Map.Entry<String, Set<TosActionQueue>> lPlanQueue : lQueueList.entrySet()) {
		for (TosActionQueue lQueue : lPlanQueue.getValue()) {
		    PreProductionLoads lPreProdLoads = getPreProductionLoadsDAO().find(lQueue.getTosRecId());
		    lPreProdLoads.setStatus(action);
		    lPreProdLoads.setLastActionStatus("INPROGRESS");
		    getPreProductionLoadsDAO().update(user, lPreProdLoads);
		}
		if (lPreProdTOSOperationMap.containsKey(lPlanQueue.getKey())) {
		    Set<TosActionQueue> lTempPreProdIds = lPreProdTOSOperationMap.get(lPlanQueue.getKey());
		    lTempPreProdIds.addAll(lPlanQueue.getValue());
		    lPreProdTOSOperationMap.replace(lPlanQueue.getKey(), lTempPreProdIds);
		} else {
		    Set<TosActionQueue> lTempPreProdIds = new HashSet();
		    lTempPreProdIds.addAll(lPlanQueue.getValue());
		    lPreProdTOSOperationMap.put(lPlanQueue.getKey(), lTempPreProdIds);
		}
	    }

	    LOG.info("Queue Addition Completed, size of queue " + lPreProdTOSOperationMap.size());

	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception Ex) {
	    LOG.info("Error Occurs", Ex);
	    throw new WorkflowException("Unable to process your request", Ex);
	}

	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    public JSONResponse removeLoadAndActivateInTOS(User user, String planId, String systemName, Integer preProd) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	try {
	    if (lPreProdTOSOperationMap.containsKey(planId + "_" + systemName)) {
		Set<TosActionQueue> lTempPreProdIds = lPreProdTOSOperationMap.get(planId + "_" + systemName);
		TosActionQueue lTempQueue = new TosActionQueue();
		for (TosActionQueue lQueue : lTempPreProdIds) {
		    if (lQueue.getTosRecId().equals(preProd)) {
			lTempQueue = lQueue;
		    }
		}
		lTempPreProdIds.remove(lTempQueue);
		lPreProdTOSOperationMap.replace(planId + "_" + systemName, lTempPreProdIds);
	    }
	} catch (Exception Ex) {
	    throw new WorkflowException("Unable to delete the record from cache");
	}
	return lResponse;
    }

    public JSONResponse getLoadAndActivateInTOS(User user) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(lPreProdTOSOperationMap);
	return lResponse;
    }
}
