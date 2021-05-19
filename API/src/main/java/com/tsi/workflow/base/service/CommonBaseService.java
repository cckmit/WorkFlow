/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.base.service;

import com.tsi.workflow.WFConfig;
import com.tsi.workflow.base.BaseService;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ActivityLog;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.Dbcr;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.LoadCategories;
import com.tsi.workflow.beans.dao.LoadFreezeGrouped;
import com.tsi.workflow.beans.dao.Platform;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dao.SystemLoadActions;
import com.tsi.workflow.beans.dao.SystemPdddsMapping;
import com.tsi.workflow.beans.dto.SystemLoadDTO;
import com.tsi.workflow.beans.external.ProblemTicket;
import com.tsi.workflow.beans.ui.ImpPlanInboxView;
import com.tsi.workflow.beans.ui.ImpPlanView;
import com.tsi.workflow.beans.ui.ImplementationInboxView;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.DbcrDAO;
import com.tsi.workflow.dao.ImpPlanApprovalsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.LoadCategoriesDAO;
import com.tsi.workflow.dao.LoadFreezeDAO;
import com.tsi.workflow.dao.LoadWindowDAO;
import com.tsi.workflow.dao.PlatformDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.dao.ProjectDAO;
import com.tsi.workflow.dao.PutLevelDAO;
import com.tsi.workflow.dao.SystemCpuDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.dao.SystemPdddsMappingDAO;
import com.tsi.workflow.dao.VparsDAO;
import com.tsi.workflow.dao.external.ProblemTicketDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.helper.CommonHelper;
import com.tsi.workflow.snow.pr.SnowPRClientUtils;
import com.tsi.workflow.snow.pr.model.PRDetails;
import com.tsi.workflow.snow.pr.model.PRResponse;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
@Transactional
public class CommonBaseService extends BaseService {

    // <editor-fold defaultstate="collapsed" desc="DAO Declarations">
    private static final Logger LOG = Logger.getLogger(CommonBaseService.class.getName());

    @Autowired
    protected ActivityLogDAO activityLogDAO;
    @Autowired
    protected BuildDAO buildDAO;
    @Autowired
    protected ImpPlanApprovalsDAO impPlanApprovalsDAO;
    @Autowired
    protected ImpPlanDAO impPlanDAO;
    @Autowired
    protected CheckoutSegmentsDAO checkoutSegmentsDAO;
    @Autowired
    protected ImplementationDAO implementationDAO;
    @Autowired
    protected LoadCategoriesDAO loadCategoriesDAO;
    @Autowired
    protected LoadFreezeDAO loadFreezeDAO;
    @Autowired
    protected LoadWindowDAO loadWindowDAO;
    @Autowired
    protected PlatformDAO platformDAO;
    @Autowired
    protected ProjectDAO projectDAO;
    @Autowired
    protected PutLevelDAO putLevelDAO;
    @Autowired
    protected SystemDAO systemDAO;
    @Autowired
    protected SystemLoadDAO systemLoadDAO;
    @Autowired
    protected VparsDAO vparsDAO;
    @Autowired
    protected ProblemTicketDAO problemTicketDAO;
    @Autowired
    protected SystemLoadActionsDAO systemLoadActionsDAO;
    @Autowired
    protected DbcrDAO dbcrDAO;
    @Autowired
    protected ProductionLoadsDAO productionLoadsDAO;
    @Autowired
    protected SystemCpuDAO systemCpuDAO;
    @Autowired
    protected SystemPdddsMappingDAO systemPdddsMappingDAO;
    @Autowired
    CommonHelper commonHelper;
    @Autowired
    WFConfig wfConfig;
    @Autowired
    SnowPRClientUtils snowPRClientUtiles;

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public void setActivityLogDAO(ActivityLogDAO activityLogDAO) {
	this.activityLogDAO = activityLogDAO;
    }

    public BuildDAO getBuildDAO() {
	return buildDAO;
    }

    public void setBuildDAO(BuildDAO buildDAO) {
	this.buildDAO = buildDAO;
    }

    public ImpPlanApprovalsDAO getImpPlanApprovalsDAO() {
	return impPlanApprovalsDAO;
    }

    public void setImpPlanApprovalsDAO(ImpPlanApprovalsDAO impPlanApprovalsDAO) {
	this.impPlanApprovalsDAO = impPlanApprovalsDAO;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public void setImpPlanDAO(ImpPlanDAO impPlanDAO) {
	this.impPlanDAO = impPlanDAO;
    }

    public CheckoutSegmentsDAO getCheckoutSegmentsDAO() {
	return checkoutSegmentsDAO;
    }

    public void setCheckoutSegmentsDAO(CheckoutSegmentsDAO checkoutSegmentsDAO) {
	this.checkoutSegmentsDAO = checkoutSegmentsDAO;
    }

    public ImplementationDAO getImplementationDAO() {
	return implementationDAO;
    }

    public void setImplementationDAO(ImplementationDAO implementationDAO) {
	this.implementationDAO = implementationDAO;
    }

    public LoadCategoriesDAO getLoadCategoriesDAO() {
	return loadCategoriesDAO;
    }

    public void setLoadCategoriesDAO(LoadCategoriesDAO loadCategoriesDAO) {
	this.loadCategoriesDAO = loadCategoriesDAO;
    }

    public LoadFreezeDAO getLoadFreezeDAO() {
	return loadFreezeDAO;
    }

    public void setLoadFreezeDAO(LoadFreezeDAO loadFreezeDAO) {
	this.loadFreezeDAO = loadFreezeDAO;
    }

    public LoadWindowDAO getLoadWindowDAO() {
	return loadWindowDAO;
    }

    public void setLoadWindowDAO(LoadWindowDAO loadWindowDAO) {
	this.loadWindowDAO = loadWindowDAO;
    }

    public PlatformDAO getPlatformDAO() {
	return platformDAO;
    }

    public void setPlatformDAO(PlatformDAO platformDAO) {
	this.platformDAO = platformDAO;
    }

    public ProjectDAO getProjectDAO() {
	return projectDAO;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
	this.projectDAO = projectDAO;
    }

    public PutLevelDAO getPutLevelDAO() {
	return putLevelDAO;
    }

    public void setPutLevelDAO(PutLevelDAO putLevelDAO) {
	this.putLevelDAO = putLevelDAO;
    }

    public SystemDAO getSystemDAO() {
	return systemDAO;
    }

    public void setSystemDAO(SystemDAO systemDAO) {
	this.systemDAO = systemDAO;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public void setSystemLoadDAO(SystemLoadDAO systemLoadDAO) {
	this.systemLoadDAO = systemLoadDAO;
    }

    public VparsDAO getVparsDAO() {
	return vparsDAO;
    }

    public void setVparsDAO(VparsDAO vparsDAO) {
	this.vparsDAO = vparsDAO;
    }

    public ProblemTicketDAO getProblemTicketDAO() {
	return problemTicketDAO;
    }

    public void setProblemTicketDAO(ProblemTicketDAO problemTicketDAO) {
	this.problemTicketDAO = problemTicketDAO;
    }

    public SystemLoadActionsDAO getSystemLoadActionsDAO() {
	return systemLoadActionsDAO;
    }

    public void setSystemLoadActionsDAO(SystemLoadActionsDAO systemLoadActionsDAO) {
	this.systemLoadActionsDAO = systemLoadActionsDAO;
    }

    public DbcrDAO getDbcrDAO() {
	return dbcrDAO;
    }

    public void setDbcrDAO(DbcrDAO dbcrDAO) {
	this.dbcrDAO = dbcrDAO;
    }

    public ProductionLoadsDAO getProductionLoadsDAO() {
	return productionLoadsDAO;
    }

    public void setProductionLoadsDAO(ProductionLoadsDAO productionLoadsDAO) {
	this.productionLoadsDAO = productionLoadsDAO;
    }

    public SystemPdddsMappingDAO getSystemPdddsMappingDAO() {
	return systemPdddsMappingDAO;
    }

    public WFConfig getWFConfig() {
	return wfConfig;
    }

    public SnowPRClientUtils getSnowPRClientUtils() {
	return snowPRClientUtiles;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Calls By Id">
    @Transactional
    public JSONResponse getVpars(Integer id) {
	JSONResponse lResponse = new JSONResponse();
	IBeans find = vparsDAO.find(id);
	if (find != null) {
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(find);
	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Requested ID not Found");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getSystemLoad(Integer id) {
	JSONResponse lResponse = new JSONResponse();
	IBeans find = systemLoadDAO.find(id);
	if (find != null) {
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(find);
	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Requested ID not Found");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getPutLevel(Integer id) {
	JSONResponse lResponse = new JSONResponse();
	IBeans find = putLevelDAO.find(id);
	if (find != null) {
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(find);
	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Requested ID not Found");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getSystem(Integer id) {
	JSONResponse lResponse = new JSONResponse();
	IBeans find = systemDAO.find(id);
	if (find != null) {
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(find);
	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Requested ID not Found");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getProject(Integer id) {
	JSONResponse lResponse = new JSONResponse();
	IBeans find = projectDAO.find(id);
	if (find != null) {
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(find);
	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Requested ID not Found");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getPlatform(Integer id) {
	JSONResponse lResponse = new JSONResponse();
	IBeans find = platformDAO.find(id);
	if (find != null) {
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(find);
	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Requested ID not Found");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getLoadWindow(Integer id) {
	JSONResponse lResponse = new JSONResponse();
	IBeans find = loadWindowDAO.find(id);
	if (find != null) {
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(find);
	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Requested ID not Found");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getLoadFreeze(Integer id) {
	JSONResponse lResponse = new JSONResponse();
	IBeans find = loadFreezeDAO.find(id);
	if (find != null) {
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(find);
	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Requested ID not Found");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getLoadCategory(Integer id) {
	JSONResponse lResponse = new JSONResponse();
	IBeans find = loadCategoriesDAO.find(id);
	if (find != null) {
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(find);
	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Requested ID not Found");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getImplementation(String id) {
	JSONResponse lResponse = new JSONResponse();
	IBeans find = implementationDAO.find(id);
	if (find != null) {
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(find);
	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Requested ID not Found");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getSegmentMapping(Integer id) {
	JSONResponse lResponse = new JSONResponse();
	IBeans find = checkoutSegmentsDAO.find(id);
	if (find != null) {
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(find);
	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Requested ID not Found");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getPlan(String id) {
	JSONResponse lResponse = new JSONResponse();
	IBeans find = impPlanDAO.find(id);
	if (find != null) {
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(find);
	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Requested ID not Found");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getPlanApprovals(Integer id) {
	JSONResponse lResponse = new JSONResponse();
	IBeans find = impPlanApprovalsDAO.find(id);
	if (find != null) {
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(find);
	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Requested ID not Found");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getBuild(Integer id) {
	JSONResponse lResponse = new JSONResponse();
	IBeans find = buildDAO.find(id);
	if (find != null) {
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(find);
	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Requested ID not Found");
	}
	return lResponse;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List Calls">

    @Transactional
    public JSONResponse getPlanApprovalsList(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = impPlanApprovalsDAO.findAll(pOffset, pLimit, pOrderBy);
	Long count = impPlanApprovalsDAO.count();
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getBuildList(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = buildDAO.findAll(pOffset, pLimit, pOrderBy);
	Long count = buildDAO.count();
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getSegmentMappingList(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = checkoutSegmentsDAO.findAll(pOffset, pLimit, pOrderBy);
	Long count = checkoutSegmentsDAO.count();
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getLoadFreezeList(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = loadFreezeDAO.findAll(pOffset, pLimit, pOrderBy);
	Long count = loadFreezeDAO.count();
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getGroupLoadFreezeList(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy, String filter) {
	JSONResponse lResponse = new JSONResponse();
	Long count;
	List<LoadFreezeGrouped> lGrouped = new ArrayList<>();
	if (filter.isEmpty()) {
	    lGrouped = loadFreezeDAO.getLoadFreezeGrouped(pOffset, pLimit, pOrderBy);
	    count = loadFreezeDAO.getLoadFreezeGroupedCount();
	} else {
	    lGrouped = loadFreezeDAO.getLoadFreezeGroupedBySystem(pOffset, pLimit, pOrderBy, filter);
	    count = loadFreezeDAO.getLoadFreezeGroupedBySystemCount(filter);
	}
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(lGrouped);
	return lResponse;
    }

    @Transactional
    public JSONResponse getLoadCategoryList(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = loadCategoriesDAO.findAll(pOffset, pLimit, pOrderBy);
	Long count = loadCategoriesDAO.count();
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getLoadWindowList(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = loadWindowDAO.findAll(pOffset, pLimit, pOrderBy);
	Long count = loadWindowDAO.count();
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getPlatformList(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = platformDAO.findAll(pOffset, pLimit, pOrderBy);
	Long count = platformDAO.count();
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getProjectList(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = projectDAO.findAll(pOffset, pLimit, pOrderBy);
	Long count = projectDAO.count();
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getPutLevelList(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy, String filter) {
	JSONResponse lResponse = new JSONResponse();
	List findAll;
	Long count;
	if (filter.isEmpty()) {
	    findAll = putLevelDAO.findAll(pOffset, pLimit, pOrderBy);
	    count = putLevelDAO.count();
	} else {
	    findAll = putLevelDAO.findBySystem(Integer.parseInt(filter));
	    count = Long.valueOf(findAll != null ? findAll.size() : 0);
	}
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getSystemLoadList(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = systemLoadDAO.findAll(pOffset, pLimit, pOrderBy);
	Long count = systemLoadDAO.count();
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getSystemList(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	SortedSet findAll = new TreeSet(systemDAO.findAll(pOffset, pLimit, pOrderBy));
	Long count = systemDAO.count();
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getVparsList(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = vparsDAO.findAll(pOffset, pLimit, pOrderBy);
	Long count = vparsDAO.count();
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getPlanList(Integer pOffset, Integer pLimit, String filter, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	List<ImpPlan> findAll = new ArrayList();
	if (filter.isEmpty()) {
	    findAll = impPlanDAO.findAll(pOffset, pLimit, pOrderBy);
	    Long count = impPlanDAO.count();
	    lResponse.setCount(count);
	} else {
	    findAll = impPlanDAO.findById(filter, pOffset, pLimit, pOrderBy);
	    Long count = impPlanDAO.countById(filter);
	    lResponse.setCount(count);
	}
	List<ImpPlanView> lReturn = new ArrayList();
	for (ImpPlan find : findAll) {
	    ImpPlanView impPlanView = new ImpPlanView();
	    impPlanView.setPlan(find);
	    impPlanView.setIsDeleteAllowed(Boolean.FALSE);
	    List<CheckoutSegments> lSegments = getCheckoutSegmentsDAO().findByImpPlan(find.getId());
	    if (lSegments != null && lSegments.isEmpty()) {
		impPlanView.setIsDeleteAllowed(Boolean.TRUE);
	    }
	    Map<String, Integer> lDeleteImpStatus = new HashMap();
	    List<Implementation> lImpList = getImplementationDAO().findByImpPlan(find.getId());
	    for (Implementation lImp : lImpList) {
		List<CheckoutSegments> lSegmentCount = getCheckoutSegmentsDAO().findByImplementation(lImp.getId());
		if (lSegmentCount == null) {
		    lDeleteImpStatus.put(lImp.getId(), 0);
		} else {
		    lDeleteImpStatus.put(lImp.getId(), lSegmentCount.size());
		}
	    }
	    impPlanView.setImpDeleteStatus(lDeleteImpStatus);
	    lReturn.add(impPlanView);
	}
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(lReturn);
	return lResponse;
    }

    @Transactional
    public JSONResponse getImplementationList(Integer pOffset, Integer pLimit, String filter, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = new ArrayList();
	if (filter.isEmpty()) {
	    findAll = implementationDAO.findAll(pOffset, pLimit, pOrderBy);
	    Long count = implementationDAO.count();
	    lResponse.setCount(count);
	} else {
	    findAll = implementationDAO.findById(filter, pOffset, pLimit, pOrderBy);
	    Long count = implementationDAO.countById(filter);
	    lResponse.setCount(count);
	}
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Calls by Table References">

    public JSONResponse getBuildByPlan(String[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = buildDAO.findByImpPlan(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    public JSONResponse getBuildBySystem(Integer[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = buildDAO.findBySystem(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    public JSONResponse getImplementationByPlan(String[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = implementationDAO.findByImpPlan(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    public JSONResponse getPlanByProject(Integer[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = impPlanDAO.findByProject(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    public JSONResponse getPlanApprovalsByPlan(String[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = impPlanApprovalsDAO.findByImpPlan(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    public JSONResponse getSegmentMappingByPlan(String[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = checkoutSegmentsDAO.findByImpPlan(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    public JSONResponse getSegmentMappingByImplementation(String[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = checkoutSegmentsDAO.findByImplementation(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    public JSONResponse getLoadCategoriesBySystem(Integer[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = new ArrayList<LoadCategories>(loadCategoriesDAO.findBySystem(ids));
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    public JSONResponse getLoadFreezeByLoadCategories(Integer[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = loadFreezeDAO.findByLoadCategories(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    public JSONResponse getLoadWindowByLoadCategories(Integer[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = loadWindowDAO.findByLoadCategories(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    public JSONResponse getPutLevelBySystem(Integer[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List<PutLevel> findAll = putLevelDAO.findBySystem(ids);
	List<PutLevel> putList = new ArrayList<>();
	findAll.forEach(put -> {
	    if (put.getStatus().equals(Constants.PUTLevelOptions.PRODUCTION.name())) {
		putList.add(put);
	    } else if (Constants.getFuturePutLevel().contains(put.getStatus())) {
		putList.add(put);
	    }
	});
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(putList);
	return lResponse;
    }

    public JSONResponse getSystemByPlatform(Integer[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = systemDAO.findByPlatform(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    public JSONResponse getSystemLoadByLoadCategories(Integer[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = systemLoadDAO.findByLoadCategories(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    public JSONResponse getSystemLoadByPutLevel(Integer[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = systemLoadDAO.findByPutLevel(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    public JSONResponse getSystemLoadByPlan(String[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = systemLoadDAO.findByImpPlan(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    public JSONResponse getSystemLoadBySystem(Integer[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = systemLoadDAO.findBySystem(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    public JSONResponse getVparsBySystem(Integer[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = vparsDAO.findBySystem(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    public JSONResponse getSegmentMappingBySystemLoad(Integer[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = checkoutSegmentsDAO.findBySystemLoad(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }
    // </editor-fold>

    @Transactional
    public JSONResponse getProblemTicket(String ticketNumber) {
	JSONResponse response = new JSONResponse();
	List<ProblemTicket> problemTicket = new ArrayList();
	try {
	    if (getWFConfig().getIsSdmDBMigrated()) {
		// ZTPFM-2624 Validate the PR Number from SNOW using REST API
		PRResponse prNumberStatus = getSnowPRClientUtils().getPRNumberStatus(ticketNumber);
		if (prNumberStatus != null && prNumberStatus.getResult() != null && !prNumberStatus.getResult().isEmpty()) {
		    ProblemTicket prTicket = new ProblemTicket();
		    PRDetails prDetails = prNumberStatus.getResult().stream().findFirst().get();
		    if (!Constants.PRNumberStatuses.getPRClosedStatus().contains(prDetails.getState())) {
			prTicket.setRefNum(prDetails.getNumber());
			prTicket.setStatus(prDetails.getState());
			problemTicket.add(prTicket);
		    } else {
			LOG.info("Error occurs on Problem Ticket number valdiation");
			throw new WorkflowException("Entered Problem Ticket - " + ticketNumber + " is not in ACTIVE status, Please validate the given number");
		    }
		} else {
		    LOG.info("Error occurs on Problem Ticket number valdiation");
		    throw new WorkflowException("Entered Problem Ticket number - " + ticketNumber + " is not valid");
		}
	    } else {
		problemTicket = problemTicketDAO.getProblemTicket(ticketNumber);
		if (problemTicket.isEmpty()) {
		    response.setStatus(Boolean.FALSE);
		    response.setErrorMessage("Invalid Problem Ticket Number");
		}
	    }
	} catch (WorkflowException ex) {
	    LOG.info("Error occurs in REST API call for SDM ticket", ex);
	    throw ex;
	} catch (Exception ex) {
	    LOG.info("Exception occurs on SDM Ticket validation ", ex);
	    throw new WorkflowException("Unexpected Error occurs on getting the Problem Ticket Number", ex);
	}
	response.setData(problemTicket);
	response.setStatus(Boolean.TRUE);
	return response;
    }

    @Transactional
    public JSONResponse getSystemListByPlan(String planId) {
	JSONResponse response = new JSONResponse();
	ImpPlan lPlan = impPlanDAO.find(planId);
	Platform platformId = lPlan.getSystemLoadList().get(0).getSystemId().getPlatformId();
	List<System> findByPlatform = systemDAO.findByPlatform(platformId.getId());
	response.setData(findByPlatform.stream().sorted(Comparator.comparing(System::getId).reversed()).collect(Collectors.toList()));
	response.setStatus(Boolean.TRUE);
	return response;
    }

    @Transactional
    public JSONResponse getActivityLogByPlanId(String[] ids) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	List findAll = activityLogDAO.findByPlanId(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getActivityLogByImpId(String[] ids) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	List findAll = activityLogDAO.findByImpId(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getActivityLogList(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	List findAll = activityLogDAO.findAll(pOffset, pLimit, pOrderBy);
	Long count = activityLogDAO.count();
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getActivityLog(Integer id) {
	JSONResponse lResponse = new JSONResponse();
	ActivityLog find = activityLogDAO.find(id);
	lResponse.setData(find);
	return lResponse;
    }

    @Transactional
    public JSONResponse getSystemLoadActionsBySystemId(Integer[] ids) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	List findAll = systemLoadActionsDAO.findBySystemId(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getSystemLoadActionsByPlanId(String[] ids) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	List findAll = systemLoadActionsDAO.findByPlanId(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getSystemLoadActionsBySystemLoadId(Integer[] ids) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	List findAll = systemLoadActionsDAO.findBySystemLoadId(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getSystemLoadActionsByVparId(Integer[] ids) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	List findAll = systemLoadActionsDAO.findByVparId(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getSystemLoadActionsList(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	List findAll = systemLoadActionsDAO.findAll(pOffset, pLimit, pOrderBy);
	Long count = systemLoadActionsDAO.count();
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getSystemLoadActions(Integer id) {
	JSONResponse lResponse = new JSONResponse();
	SystemLoadActions find = systemLoadActionsDAO.find(id);
	lResponse.setData(find);
	return lResponse;
    }

    @Transactional
    public JSONResponse getDbcrByPlanId(String[] ids) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	List findAll = dbcrDAO.findByPlanId(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getDbcrBySystemId(Integer[] ids) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	List findAll = dbcrDAO.findBySystemId(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getDbcrList(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	List findAll = dbcrDAO.findAll(pOffset, pLimit, pOrderBy);
	Long count = dbcrDAO.count();
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getDbcr(Integer id) {
	JSONResponse lResponse = new JSONResponse();
	Dbcr find = dbcrDAO.find(id);
	lResponse.setData(find);
	return lResponse;
    }

    @Transactional
    public JSONResponse getProductionLoadsByPlanId(String[] ids) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	List findAll = productionLoadsDAO.findByPlanId(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getProductionLoadsBySystemId(Integer[] ids) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	List findAll = productionLoadsDAO.findBySystemId(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getProductionLoadsBySystemLoadId(Integer[] ids) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	List findAll = productionLoadsDAO.findBySystemLoadId(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getProductionLoadsList(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	List findAll = productionLoadsDAO.findAll(pOffset, pLimit, pOrderBy);
	Long count = productionLoadsDAO.count();
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getProductionLoads(Integer id) {
	JSONResponse lResponse = new JSONResponse();
	ProductionLoads find = productionLoadsDAO.find(id);
	lResponse.setData(find);
	return lResponse;
    }

    @Transactional
    public JSONResponse getSystemCpuList(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	List findAll = systemCpuDAO.findAll(pOffset, pLimit, pOrderBy);
	Long count = systemCpuDAO.count();
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse getSystemPdddsMapping(Integer id) {
	JSONResponse lResponse = new JSONResponse();
	List<SystemPdddsMapping> lSysPdddsMapping = getSystemPdddsMappingDAO().findBySystemLoadId(id);
	lResponse.setData(lSysPdddsMapping);
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse getSystemPdddsMappingList(Integer[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List<SystemPdddsMapping> lSysPdddsMapping = getSystemPdddsMappingDAO().findBySystemLoadId(ids);
	lResponse.setData(lSysPdddsMapping);
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    public JSONResponse getSearchViewPlanList(Integer pOffset, Integer pLimit, String object, LinkedHashMap<String, String> pOrderBy, String filter) {
	JSONResponse lResponse = new JSONResponse();
	int start = pLimit * pOffset;

	List<ImpPlan> findAll = new ArrayList();
	if (pOrderBy != null && !pOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> order : pOrderBy.entrySet()) {
		if (order.getKey().equals("loaddatetime")) {
		    Long count = impPlanDAO.countById(filter);
		    lResponse.setCount(count);
		    findAll = getImpPlanDAO().findPlansByLoadDateTime(null, null, pOffset, pLimit, pOrderBy, filter);
		} else {
		    findAll = getplans(pOffset, pLimit, pOrderBy, filter, lResponse);
		}
	    }
	} else {
	    findAll = getplans(pOffset, pLimit, pOrderBy, filter, lResponse);
	}

	List<ImpPlanView> lReturn = new ArrayList();
	for (ImpPlan find : findAll) {
	    ImpPlanView impPlanView = new ImpPlanView();
	    impPlanView.setPlan(find);
	    impPlanView.setIsDeleteAllowed(Boolean.FALSE);
	    List<CheckoutSegments> lSegments = getCheckoutSegmentsDAO().findByImpPlan(find.getId());
	    if (lSegments != null && lSegments.isEmpty()) {
		impPlanView.setIsDeleteAllowed(Boolean.TRUE);
	    }
	    Map<String, Integer> lDeleteImpStatus = new HashMap();
	    List<Implementation> lImpList = getImplementationDAO().findByImpPlan(find.getId());
	    for (Implementation lImp : lImpList) {
		List<CheckoutSegments> lSegmentCount = getCheckoutSegmentsDAO().findByImplementation(lImp.getId());
		if (lSegmentCount == null) {
		    lDeleteImpStatus.put(lImp.getId(), 0);
		} else {
		    lDeleteImpStatus.put(lImp.getId(), lSegmentCount.size());
		}
	    }
	    impPlanView.setImpDeleteStatus(lDeleteImpStatus);
	    lReturn.add(impPlanView);
	}
	if (lReturn.size() == 0) {
	    lResponse.setCount(0);
	    lResponse.setErrorMessage("No Plans Found");
	    lResponse.setStatus(Boolean.FALSE);
	    return lResponse;
	}
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(getPlansAndSysLoadDetails(lReturn));
	return lResponse;
    }

    public JSONResponse getSearchViewImplementationList(Integer pOffset, Integer pLimit, String object, LinkedHashMap<String, String> pOrderBy, String filter) {
	JSONResponse lResponse = new JSONResponse();
	int start = pLimit * pOffset;
	List<Implementation> implementationList = new ArrayList<>();
	if (pOrderBy != null && !pOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> order : pOrderBy.entrySet()) {
		if (order.getKey().equals("loaddatetime")) {
		    Long count = implementationDAO.countById(filter);
		    lResponse.setCount(count);
		    implementationList = getImplementationDAO().findImpByLoadDateTime(null, null, pOffset, pLimit, pOrderBy, filter);
		} else {
		    implementationList = getImplList(pOffset, pLimit, lResponse, pOrderBy, filter);
		}
	    }
	} else {
	    implementationList = getImplList(pOffset, pLimit, lResponse, pOrderBy, filter);
	}
	if (implementationList.size() == 0) {
	    lResponse.setCount(0);
	    lResponse.setErrorMessage("No Plans Found");
	    lResponse.setStatus(Boolean.FALSE);
	    return lResponse;
	}

	lResponse.setData(getImplAndSysLoadDetails(implementationList));
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    public List<SystemLoadDTO> getSystemLoadDetails(String[] planStrings) {

	List findAll = getSystemLoadDAO().findByImpPlanSorted(planStrings);
	List<SystemLoadDTO> systemLoadDTOs = new ArrayList<>();

	if (findAll != null) {
	    findAll.forEach(sys -> {
		SystemLoadDTO systemLoadDTO = new SystemLoadDTO();
		systemLoadDTO.setSystemLoad((SystemLoad) sys);
		systemLoadDTOs.add(systemLoadDTO);
	    });
	}
	commonHelper.validateToChangePutLevel(systemLoadDTOs);

	return systemLoadDTOs.isEmpty() ? null : systemLoadDTOs;
    }

    private List<ImpPlan> getplans(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy, String filter, JSONResponse lResponse) {
	List<ImpPlan> findAll = new ArrayList();
	if (filter.isEmpty()) {
	    findAll = impPlanDAO.findAll(pOffset, pLimit, pOrderBy);
	    Long count = impPlanDAO.count();
	    lResponse.setCount(count);
	} else {
	    findAll = impPlanDAO.findById(filter, pOffset, pLimit, pOrderBy);
	    Long count = impPlanDAO.countById(filter);
	    lResponse.setCount(count);
	}
	return findAll;
    }

    public Map<String, ImpPlanInboxView> getPlansAndSysLoadDetails(List<ImpPlanView> lReturn) {

	Set<String> planIDs = lReturn.stream().map(ImpPlanView::getPlan).map(ImpPlan::getId).collect(Collectors.toSet());

	String[] planStrings = new String[planIDs.size()];
	planIDs.toArray(planStrings);
	List<SystemLoadDTO> systemLoadDTOs = getSystemLoadDetails(planStrings);

	Map<String, List<SystemLoadDTO>> systemLoadMap = systemLoadDTOs.stream().collect(Collectors.groupingBy(T -> T.getSystemLoad().getPlanId().getId()));

	List<ImpPlanInboxView> finalImpPlaninboxList = new ArrayList<ImpPlanInboxView>();

	lReturn.forEach(plan -> {
	    ImpPlanInboxView impPlanInboxView = new ImpPlanInboxView();
	    impPlanInboxView.setPlanView(plan);
	    if (systemLoadMap.containsKey(plan.getPlan().getId())) {
		impPlanInboxView.setSystemLoadDetails(systemLoadMap.get(plan.getPlan().getId()));
	    }
	    finalImpPlaninboxList.add(impPlanInboxView);
	});

	// List<ImpPlanInboxView> sortedPlansForms =
	// getImpPlansSortedList(finalImpPlaninboxList, pOrderBy);
	LinkedHashMap<String, ImpPlanInboxView> planViewMap = new LinkedHashMap<>();
	finalImpPlaninboxList.forEach(planView -> {
	    String planID = planView.getPlanID();
	    if (!planViewMap.containsKey(planID)) {
		planViewMap.put(planID, planView);
	    }
	});

	int countForMap = 0;
	Map<String, ImpPlanInboxView> finalPlanViewMap = new LinkedHashMap<>();
	for (Map.Entry<String, ImpPlanInboxView> entry : planViewMap.entrySet()) {
	    countForMap++;
	    finalPlanViewMap.put(String.format("%010d", countForMap) + "_" + entry.getKey(), entry.getValue());
	}
	return finalPlanViewMap;
    }

    public Map<String, ImplementationInboxView> getImplAndSysLoadDetails(List<Implementation> implementationList) {

	Set<String> planIDs = implementationList.stream().map(Implementation::getPlanId).map(ImpPlan::getId).collect(Collectors.toSet());
	String[] planStrings = new String[planIDs.size()];
	planIDs.toArray(planStrings);
	List findAll = getSystemLoadDAO().findByImpPlanSorted(planStrings);
	List<SystemLoadDTO> systemLoadDTOs = getSystemLoadDetails(planStrings);

	Map<String, List<SystemLoadDTO>> systemLoadMap = systemLoadDTOs.stream().collect(Collectors.groupingBy(T -> T.getSystemLoad().getPlanId().getId()));

	List<ImplementationInboxView> finalImpinboxList = new ArrayList<ImplementationInboxView>();

	implementationList.forEach(impl -> {
	    ImplementationInboxView implementationInboxView = new ImplementationInboxView();
	    implementationInboxView.setImpl(impl);
	    if (systemLoadMap.containsKey(impl.getPlanId().getId())) {
		implementationInboxView.setSystemLoadDetails(systemLoadMap.get(impl.getPlanId().getId()));
	    }
	    finalImpinboxList.add(implementationInboxView);
	});

	LinkedHashMap<String, ImplementationInboxView> implInboxPlanMap = new LinkedHashMap<>();
	finalImpinboxList.forEach(impl -> {
	    String implID = impl.getImpl().getId();
	    if (!implInboxPlanMap.containsKey(implID)) {
		implInboxPlanMap.put(implID, impl);
	    }
	});

	int count = 0;
	Map<String, ImplementationInboxView> finalImplInboxPlanMap = new LinkedHashMap<>();
	for (Map.Entry<String, ImplementationInboxView> entry : implInboxPlanMap.entrySet()) {
	    count++;
	    finalImplInboxPlanMap.put(String.format("%010d", count) + "_" + entry.getKey(), entry.getValue());
	}
	return finalImplInboxPlanMap;
    }

    private List<Implementation> getImplList(Integer pOffset, Integer pLimit, JSONResponse lResponse, LinkedHashMap<String, String> pOrderBy, String filter) {
	List<Implementation> findAll = new ArrayList<>();
	if (filter.isEmpty()) {
	    findAll = implementationDAO.findAll(pOffset, pLimit, pOrderBy);
	    Long count = implementationDAO.count();
	    lResponse.setCount(count);
	} else {
	    findAll = implementationDAO.findById(filter, pOffset, pLimit, pOrderBy);
	    Long count = implementationDAO.countById(filter);
	    lResponse.setCount(count);
	}
	return findAll;
    }

}
