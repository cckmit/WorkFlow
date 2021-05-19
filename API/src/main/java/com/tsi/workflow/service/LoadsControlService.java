/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.service;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.LdapGroupConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.activity.CommonActivityMessage;
import com.tsi.workflow.activity.PlanStatusActivityMessage;
import com.tsi.workflow.activity.ProdFTPActivityMessage;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.BaseService;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.LoadCategories;
import com.tsi.workflow.beans.dao.LoadFreeze;
import com.tsi.workflow.beans.dao.LoadFreezeGrouped;
import com.tsi.workflow.beans.dao.LoadWindow;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.beans.dao.RFCDetails;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dto.PutLevelDTO;
import com.tsi.workflow.beans.dto.SystemLoadDTO;
import com.tsi.workflow.beans.ui.LoadCategoriesForm;
import com.tsi.workflow.beans.ui.LoadFreezeCalendar;
import com.tsi.workflow.beans.ui.LoadFreezeForm;
import com.tsi.workflow.beans.ui.LoadWindowUI;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.bpm.model.TaskVariable;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.DbcrDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.LoadCategoriesDAO;
import com.tsi.workflow.dao.LoadFreezeDAO;
import com.tsi.workflow.dao.LoadWindowDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.dao.PutLevelDAO;
import com.tsi.workflow.dao.RFCDetailsDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.helper.CommonHelper;
import com.tsi.workflow.helper.DbcrHelper;
import com.tsi.workflow.helper.FTPHelper;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.mail.ActivePlanOnPutUpgradeMail;
import com.tsi.workflow.mail.PutDateChangeMail;
import com.tsi.workflow.mail.RejectionOnPutUpgradeMail;
import com.tsi.workflow.mail.StatusChangeToDependentPlanMail;
import com.tsi.workflow.tos.TOSHelper;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.BUILD_TYPE;
import com.tsi.workflow.utils.Constants.PUTLevelOptions;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
@Service
public class LoadsControlService extends BaseService {

    private static final Logger LOG = Logger.getLogger(LoadsControlService.class.getName());

    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    LoadCategoriesDAO loadCategoriesDAO;
    @Autowired
    LoadFreezeDAO loadFreezeDAO;
    @Autowired
    LoadWindowDAO loadWindowDAO;
    @Autowired
    PutLevelDAO putLevelDAO;
    @Autowired
    BuildDAO buildDAO;
    @Autowired
    DbcrDAO dbcrDAO;
    @Autowired
    ProductionLoadsDAO productionLoadsDAO;
    @Autowired
    DbcrHelper dbcrHelper;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    SystemDAO systemDAO;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    LdapGroupConfig ldapGroupConfig;
    @Autowired
    JGitClientUtils jGitClientUtils;
    @Autowired
    TOSHelper tOSHelper;
    @Autowired
    BPMClientUtils bPMClientUtils;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    FTPHelper fTPHelper;
    @Autowired
    GITConfig gITConfig;
    @Autowired
    SSHClientUtils sSHClientUtils;
    @Autowired
    LDAPAuthenticatorImpl authenticator;
    @Autowired
    CheckoutSegmentsDAO checkoutSegmentsDAO;
    @Autowired
    ImplementationDAO implementationDAO;
    @Autowired
    RejectHelper rejectHelper;
    @Autowired
    CommonHelper commonHelper;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    RFCDetailsDAO lRFCDetailsDAO;

    public RFCDetailsDAO getRFCDetailsDAO() {
	return lRFCDetailsDAO;
    }

    public WFConfig getWFConfig() {
	return wFConfig;
    }

    public LDAPAuthenticatorImpl getLDAPAuthenticatorImpl() {
	return authenticator;
    }

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public LoadCategoriesDAO getLoadCategoriesDAO() {
	return loadCategoriesDAO;
    }

    public LoadFreezeDAO getLoadFreezeDAO() {
	return loadFreezeDAO;
    }

    public LoadWindowDAO getLoadWindowDAO() {
	return loadWindowDAO;
    }

    public PutLevelDAO getPutLevelDAO() {
	return putLevelDAO;
    }

    public BuildDAO getBuildDAO() {
	return buildDAO;
    }

    public DbcrDAO getDbcrDAO() {
	return dbcrDAO;
    }

    public ProductionLoadsDAO getProductionLoadsDAO() {
	return productionLoadsDAO;
    }

    public DbcrHelper getDbcrHelper() {
	return dbcrHelper;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public SystemDAO getSystemDAO() {
	return systemDAO;
    }

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    public LdapGroupConfig getLdapGroupConfig() {
	return ldapGroupConfig;
    }

    public JGitClientUtils getJGitClientUtils() {
	return jGitClientUtils;
    }

    public TOSHelper getTOSHelper() {
	return tOSHelper;
    }

    public BPMClientUtils getBPMClientUtils() {
	return bPMClientUtils;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public FTPHelper getFTPHelper() {
	return fTPHelper;
    }

    public CheckoutSegmentsDAO getCheckoutSegmentsDAO() {
	return checkoutSegmentsDAO;
    }

    public RejectHelper getRejectHelper() {
	return rejectHelper;
    }

    public ImplementationDAO getImplementationDAO() {
	return implementationDAO;
    }

    // <editor-fold defaultstate="collapsed" desc="LoadCategories Actions">
    @Transactional
    public JSONResponse getAllLoadCategoryList(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	getLoadCategoriesDAO().disableFilter();
	List<LoadCategories> findAll = getLoadCategoriesDAO().findAll(new HashMap<>(), pOffset, pLimit, pOrderBy);
	Long count = getLoadCategoriesDAO().count(new HashMap<>());
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	lResponse.setCount(count);
	return lResponse;
    }

    @Transactional
    public JSONResponse saveLoadCategory(User lUser, LoadCategoriesForm loadCategoryForm) {
	JSONResponse lResponse = new JSONResponse();
	LoadCategories loadCategory = loadCategoryForm.getLoadCategory();
	if (!getLoadCategoriesDAO().isExists(loadCategory)) {
	    getLoadCategoriesDAO().save(lUser, loadCategory);
	    List<LoadWindow> loadWindows = loadCategoryForm.getLoadWindows();
	    for (LoadWindow loadWindow : loadWindows) {
		loadWindow.setLoadCategoryId(loadCategory);
		getLoadWindowDAO().save(lUser, loadWindow);
	    }
	    lResponse.setStatus(Boolean.TRUE);
	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Loadcategory Already Exists");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse updateLoadCategory(User lUser, LoadCategoriesForm loadCategoryForm) {
	JSONResponse lResponse = new JSONResponse();
	LoadCategories loadCategory = loadCategoryForm.getLoadCategory();
	if (!getLoadCategoriesDAO().isExists(loadCategory)) {
	    getLoadCategoriesDAO().update(lUser, loadCategory);
	    List<LoadWindow> dbLoadWindowList = getLoadWindowDAO().findByLoadCategories(loadCategory);
	    List<LoadWindow> loadWindows = loadCategoryForm.getLoadWindows();
	    Collection<LoadWindow> delLoadWindows = CollectionUtils.subtract(dbLoadWindowList, loadWindows);
	    for (LoadWindow delLoadWindow : delLoadWindows) {
		getLoadWindowDAO().delete(lUser, delLoadWindow);
	    }
	    for (LoadWindow loadWindow : loadWindows) {
		loadWindow.setLoadCategoryId(loadCategory);
		if (loadWindow.getId() != null) {
		    getLoadWindowDAO().update(lUser, loadWindow);
		} else {
		    getLoadWindowDAO().save(lUser, loadWindow);
		}
	    }
	    lResponse.setStatus(Boolean.TRUE);
	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Loadcategory Already Exists");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse deleteLoadCategory(User lUser, Integer id) {
	JSONResponse lResponse = new JSONResponse();
	getLoadCategoriesDAO().delete(lUser, id);
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Load Freeze Actions">
    @Transactional
    public JSONResponse saveLoadFreeze(User lUser, List<LoadFreezeForm> loadFreezeFormList) {
	JSONResponse lResponse = new JSONResponse();

	if (loadFreezeFormList != null && loadFreezeFormList.size() > 0) {
	    boolean falg = true;

	    for (int i = 0; i < loadFreezeFormList.size(); i++) {
		LoadFreezeForm loadFreezeForm = loadFreezeFormList.get(i);
		List<String> lLoadFreezeListByCategories = getLoadFreezeDAO().lLoadFreezeListByCategories(loadFreezeForm.getLoadFreeze());
		if (!lLoadFreezeListByCategories.isEmpty()) {

		    falg = false;
		    break;
		}
	    }
	    if (falg) {
		for (int i = 0; i < loadFreezeFormList.size(); i++) {
		    LoadFreezeForm loadFreezeForm = loadFreezeFormList.get(i);
		    getLoadFreezeDAO().save(lUser, loadFreezeForm.getLoadFreeze());
		}
		lResponse.setStatus(Boolean.TRUE);
	    } else {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("LoadFreeze Already Exists");
	    }

	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("LoadFreeze List is Empty");
	}

	/*
	 * if (loadFreezeForm.getLoadFreeze().getLoadCategoryId() != null) {
	 * List<String> lLoadFreezeListByCategories =
	 * getLoadFreezeDAO().lLoadFreezeListByCategories(loadFreezeForm.getLoadFreeze()
	 * ); if (!lLoadFreezeListByCategories.isEmpty()) {
	 * lResponse.setStatus(Boolean.FALSE);
	 * lResponse.setErrorMessage("LoadFreeze Already Exists"); } else {
	 * getLoadFreezeDAO().save(lUser, loadFreezeForm.getLoadFreeze());
	 * lResponse.setStatus(Boolean.TRUE); } } else { List<LoadCategories>
	 * loadCategories =
	 * getLoadCategoriesDAO().findBySystem(loadFreezeForm.getSystem()); List<String>
	 * lLoadFreezeList = null; if (!loadCategories.isEmpty()) { for (LoadCategories
	 * lLoadCategories : loadCategories) { lLoadFreezeList =
	 * getLoadFreezeDAO().lLoadFreezeList(loadFreezeForm, lLoadCategories); } } if
	 * (!lLoadFreezeList.isEmpty()) { lResponse.setStatus(Boolean.FALSE);
	 * lResponse.setErrorMessage("LoadFreeze Already Exists"); } else { for
	 * (LoadCategories loadCategory : loadCategories) { LoadFreeze loadFreeze = new
	 * LoadFreeze();
	 * loadFreeze.setReason(loadFreezeForm.getLoadFreeze().getReason());
	 * loadFreeze.setFromDate(loadFreezeForm.getLoadFreeze().getFromDate());
	 * loadFreeze.setToDate(loadFreezeForm.getLoadFreeze().getToDate());
	 * loadFreeze.setLoadCategoryId(loadCategory); getLoadFreezeDAO().save(lUser,
	 * loadFreeze); } lResponse.setStatus(Boolean.TRUE);
	 * 
	 * } }
	 */
	return lResponse;
    }

    @Transactional
    public JSONResponse updateLoadFreeze(User lUser, LoadFreeze loadFreeze) throws Exception {
	JSONResponse lResponse = new JSONResponse();
	getLoadFreezeDAO().update(lUser, loadFreeze);
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse groupUpdateLoadFreeze(User lUser, List<LoadFreeze> loadFreezeList) throws Exception {
	JSONResponse lResponse = new JSONResponse();
	if (loadFreezeList != null && loadFreezeList.size() > 0) {
	    boolean falg = true;
	    List<LoadFreeze> insertLoadFreezeList = new ArrayList<>();
	    List<LoadFreeze> updateFreezeList = new ArrayList<>();
	    for (int i = 0; i < loadFreezeList.size(); i++) {
		LoadFreeze loadFreeze = loadFreezeList.get(i);
		List<String> lLoadFreezeListByCategories = getLoadFreezeDAO().lLoadFreezeListByCategories(loadFreeze);

		if (loadFreeze.getId() != null) {
		    if (lLoadFreezeListByCategories != null && lLoadFreezeListByCategories.size() > 1) {

			falg = false;
			break;
		    } else {

			LoadFreeze lFreeze = getLoadFreezeDAO().find(loadFreeze.getId());
			lFreeze.setFromDate(loadFreeze.getFromDate());
			lFreeze.setToDate(loadFreeze.getToDate());
			lFreeze.setActive(loadFreeze.getActive());
			loadFreezeList.set(i, lFreeze);
		    }
		} else {
		    if (lLoadFreezeListByCategories != null && lLoadFreezeListByCategories.size() > 0) {
			falg = false;
			break;
		    }
		}
	    }
	    if (falg) {
		for (int i = 0; i < loadFreezeList.size(); i++) {
		    LoadFreeze loadFreeze = loadFreezeList.get(i);
		    if (loadFreeze.getId() != null) {
			updateFreezeList.add(loadFreeze);
		    } else {
			insertLoadFreezeList.add(loadFreeze);
		    }
		}
		if (insertLoadFreezeList != null && insertLoadFreezeList.size() > 0) {
		    for (int i = 0; i < insertLoadFreezeList.size(); i++) {
			LoadFreeze loadFreeze = insertLoadFreezeList.get(i);
			getLoadFreezeDAO().save(lUser, loadFreeze);
		    }
		}
		if (updateFreezeList != null && updateFreezeList.size() > 0) {
		    for (int i = 0; i < updateFreezeList.size(); i++) {
			LoadFreeze loadFreeze = updateFreezeList.get(i);
			getLoadFreezeDAO().update(lUser, loadFreeze);

		    }
		}
		lResponse.setStatus(Boolean.TRUE);
	    } else {

		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("LoadFreeze Already Exists");
		return lResponse;
	    }
	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("LoadFreeze List is Empty");
	}
	/*
	 * List<String> lLoadFreezeListByCategories = new ArrayList<>(); List<String>
	 * lLoadCategoryList = new ArrayList<>(); for (String id :
	 * lLoadFreezeGrouped.getListIds()) { LoadFreeze lFreeze =
	 * getLoadFreezeDAO().find(Integer.valueOf(id)); lLoadFreezeListByCategories =
	 * getLoadFreezeDAO().lLoadFreezeUpdateByDate(lFreeze,
	 * lLoadFreezeGrouped.getFrom_date(), lLoadFreezeGrouped.getTo_date());
	 * lLoadCategoryList.addAll(lLoadFreezeListByCategories); if
	 * (lLoadCategoryList.isEmpty()) {
	 * lFreeze.setFromDate(lLoadFreezeGrouped.getFrom_date());
	 * lFreeze.setToDate(lLoadFreezeGrouped.getTo_date());
	 * getLoadFreezeDAO().update(lUser, lFreeze); lResponse.setStatus(Boolean.TRUE);
	 * } } if (!lLoadCategoryList.isEmpty()) { lResponse.setStatus(Boolean.FALSE);
	 * lResponse.setErrorMessage("LoadFreeze Already Exists"); }
	 */

	return lResponse;
    }

    @Transactional
    public JSONResponse deleteLoadFreeze(User lUser, Integer id) throws Exception {
	JSONResponse lResponse = new JSONResponse();
	getLoadFreezeDAO().delete(lUser, id);
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse deleteGroupLoadFreeze(User lUser, LoadFreezeGrouped lLoadFreezeGrouped) throws Exception {
	JSONResponse lResponse = new JSONResponse();
	for (String id : lLoadFreezeGrouped.getListIds()) {
	    getLoadFreezeDAO().delete(lUser, Integer.valueOf(id));
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Put Level Actions">
    @Transactional
    public JSONResponse getPutLevelList(User lUser, Integer offset, Integer limit, LinkedHashMap lOrderBy, String filter) {
	JSONResponse lResponse = new JSONResponse();
	List<PutLevel> lResult = new ArrayList<>();
	List<PutLevelDTO> lReturn = new ArrayList<>();
	Long count;
	if (filter.isEmpty()) {
	    lResult = putLevelDAO.findAll(offset, limit, lOrderBy);
	    count = putLevelDAO.count();
	} else {
	    HashMap<String, Serializable> lFilter = new HashMap<>();
	    lFilter.put("systemId", new System(Integer.parseInt(filter)));
	    lFilter.put("active", "Y");
	    lResult = putLevelDAO.findAll(lFilter, offset, limit, lOrderBy);
	    count = putLevelDAO.count(lFilter);
	}
	for (PutLevel lPutLevel : lResult) {
	    lReturn.add(new PutLevelDTO(lPutLevel, lUser.getId(), lUser.getCurrentRole()).populate());
	}
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(lReturn);
	return lResponse;
    }

    @Transactional
    public JSONResponse updatePutLevel(User lUser, PutLevel putLevel) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    if (putLevel.getStatus() == null) {
		putLevel.setStatus(Constants.PUTLevelOptions.INITIAL.name());
	    }

	    PutLevel putLevelDetails = getPutLevelDAO().findBySystemAndPutName(putLevel.getPutLevel(), putLevel.getSystemId().getId());
	    if (putLevelDetails == null && !putLevel.getStatus().equals(Constants.PUTLevelOptions.INITIAL.name())) {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage(" Put level does not exists for the system " + putLevel.getSystemId().getName());
		return lResponse;
	    }
	    if (putLevel != null && putLevelDetails != null && ((putLevel.getId() != null) ? !(putLevel.getId().equals(putLevelDetails.getId())) : Boolean.TRUE) && putLevel.getPutLevel().equals(putLevelDetails.getPutLevel())) {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage(" zTPF Level already exists for the system " + putLevel.getSystemId().getName());
		return lResponse;
	    }
	    PutLevel lOldInitalPutLevel = getPutLevelDAO().findByPutLevelandSystem(putLevel.getPutLevel(), putLevel.getSystemId().getId(), Constants.PUTLevelOptions.INITIAL.name());
	    if (lOldInitalPutLevel != null) {
		if (putLevel.getStatus().equals(Constants.PUTLevelOptions.INITIAL.name()) && getPutLevelDAO().isExists(putLevel.getSystemId(), Constants.PUTLevelOptions.INITIAL.name())) {
		    if (putLevel != null && putLevel.getId() == null || (!putLevel.getId().equals(lOldInitalPutLevel.getId()))) {
			lResponse.setStatus(Boolean.FALSE);
			lResponse.setErrorMessage("Initial zTPF level already exists for the system");
			return lResponse;
		    }
		}
	    }

	    PutLevel lOldDevelopmentPutLevel = getPutLevelDAO().findByPutLevelandSystem(putLevel.getPutLevel(), putLevel.getSystemId().getId(), Constants.PUTLevelOptions.DEVELOPMENT.name());
	    if (lOldDevelopmentPutLevel != null) {
		if (putLevel.getStatus().equals(Constants.PUTLevelOptions.DEVELOPMENT.name()) && getPutLevelDAO().isExists(putLevel.getSystemId(), Constants.PUTLevelOptions.DEVELOPMENT.name())) {
		    if (putLevel != null && putLevel.getId() == null || (!putLevel.getId().equals(lOldDevelopmentPutLevel.getId()))) {
			lResponse.setStatus(Boolean.FALSE);
			lResponse.setErrorMessage("Development zTPF level already exists for the system");
			return lResponse;
		    }
		}
	    }

	    PutLevel lOldPreProdPutLevel = getPutLevelDAO().findByPutLevelandSystem(putLevel.getPutLevel(), putLevel.getSystemId().getId(), Constants.PUTLevelOptions.PRE_PROD_CO_EXIST.name());
	    if (lOldPreProdPutLevel != null) {
		if (putLevel.getStatus().equals(Constants.PUTLevelOptions.PRE_PROD_CO_EXIST.name()) && getPutLevelDAO().isExists(putLevel.getSystemId(), Constants.PUTLevelOptions.PRE_PROD_CO_EXIST.name())) {
		    if (putLevel != null && putLevel.getId() == null || (!putLevel.getId().equals(lOldPreProdPutLevel.getId()))) {
			lResponse.setStatus(Boolean.FALSE);
			lResponse.setErrorMessage("Pre Prod Co-Exist zTPF level already exists for the system");
			return lResponse;
		    }
		}
	    }

	    PutLevel lOldProdPutLevel = getPutLevelDAO().findByPutLevelandSystem(putLevel.getPutLevel(), putLevel.getSystemId().getId(), Constants.PUTLevelOptions.PROD_CO_EXIST.name());
	    if (lOldProdPutLevel != null) {
		if (putLevel.getStatus().equals(Constants.PUTLevelOptions.PROD_CO_EXIST.name()) && getPutLevelDAO().isExists(putLevel.getSystemId(), Constants.PUTLevelOptions.PROD_CO_EXIST.name())) {
		    if (putLevel != null && putLevel.getId() == null || (!putLevel.getId().equals(lOldProdPutLevel.getId()))) {
			lResponse.setStatus(Boolean.FALSE);
			lResponse.setErrorMessage("Prod Co-Exist zTPF level already exists for the system");
			return lResponse;
		    }
		}
	    }

	    if (putLevel.getStatus().equals(Constants.PUTLevelOptions.PRODUCTION.name())) {
		if (!getJGitClientUtils().isRepositoryExist(putLevel.getScmUrl())) {
		    lResponse.setStatus(Boolean.FALSE);
		    lResponse.setErrorMessage("zTPF level SCM Url is invalid");
		    return lResponse;
		}
	    }
	    if (putLevel.getId() != null) {
		PutLevel oldPutLevel = getPutLevelDAO().find(putLevel.getId());
		if (oldPutLevel.getStatus().equals(Constants.PUTLevelOptions.PRODUCTION.name()) || oldPutLevel.getStatus().equals(Constants.PUTLevelOptions.ARCHIVE.name())) {
		    lResponse.setStatus(Boolean.FALSE);
		    lResponse.setErrorMessage("Not Allowed to update zTPF level for Production and Archive");
		    return lResponse;
		}
	    }

	    List<String> ownerIds = Arrays.asList(putLevel.getOwnerids().split(","));
	    List<String> ownerNames = new ArrayList<>();
	    for (String ownerId : ownerIds) {
		ownerNames.add(getLDAPAuthenticatorImpl().getUserDetails(ownerId).getDisplayName());
	    }
	    putLevel.setOwnerNames(String.join(",", ownerNames));

	    PutLevel lCurrentProdPut = getPutLevelDAO().getPutLevel(new System(putLevel.getSystemId().getId()), Constants.PUTLevelOptions.PRODUCTION.name());

	    if (putLevel.getId() != null) {
		PutLevel oldPutLevel = getPutLevelDAO().find(putLevel.getId());
		if ((oldPutLevel.getStatus().equals(Constants.PUTLevelOptions.LOCKDOWN.name()) || oldPutLevel.getStatus().equals(Constants.PUTLevelOptions.PROD_CO_EXIST.name())) && putLevel.getStatus().equals(Constants.PUTLevelOptions.PRODUCTION.name())) {
		    if (getPutLevelDAO().isExists(putLevel.getSystemId(), Constants.PUTLevelOptions.BACKUP.name())) {
			PutLevel lBackupPut = getPutLevelDAO().getPutLevel(putLevel.getSystemId(), Constants.PUTLevelOptions.BACKUP.name());
			lBackupPut.setStatus(Constants.PUTLevelOptions.ARCHIVE.name());
			getPutLevelDAO().update(lUser, lBackupPut);
		    }
		    if (getPutLevelDAO().isExists(putLevel.getSystemId(), Constants.PUTLevelOptions.PRODUCTION.name())) {
			PutLevel lBackupPut = getPutLevelDAO().getPutLevel(putLevel.getSystemId(), Constants.PUTLevelOptions.PRODUCTION.name());
			lBackupPut.setStatus(Constants.PUTLevelOptions.BACKUP.name());
			getPutLevelDAO().update(lUser, lBackupPut);
		    }
		} else if (oldPutLevel.getStatus().equals(Constants.PUTLevelOptions.BACKUP.name()) && putLevel.getStatus().equals(Constants.PUTLevelOptions.PRODUCTION.name())) {
		    if (getPutLevelDAO().isExists(putLevel.getSystemId(), Constants.PUTLevelOptions.PRODUCTION.name())) {
			PutLevel lBackupPut = getPutLevelDAO().getPutLevel(putLevel.getSystemId(), Constants.PUTLevelOptions.PRODUCTION.name());
			lBackupPut.setStatus(Constants.PUTLevelOptions.LOCKDOWN.name());
			getPutLevelDAO().update(lUser, lBackupPut);
		    }
		}

		if (oldPutLevel.getPutDateTime().compareTo(putLevel.getPutDateTime()) != 0) {
		    List<String> lDateChangeAllowedStatus = Arrays.asList(PUTLevelOptions.INACTIVE.name(), PUTLevelOptions.INITIAL.name(), PUTLevelOptions.DEVELOPMENT.name(), PUTLevelOptions.PRE_PROD_CO_EXIST.name(), PUTLevelOptions.LOCKDOWN.name());
		    List<SystemLoad> systemLoadList = systemLoadDAO.findByPutLevel(putLevel);

		    if (!lDateChangeAllowedStatus.contains(oldPutLevel.getStatus())) { // 2489
			lResponse.setStatus(Boolean.FALSE);
			lResponse.setErrorMessage("Date changes are not allowed when PUT Level status are not in INACTIVE/INITIAL/DEVELOPMENT/LOCKDOWN");
			return lResponse;
		    }

		    for (SystemLoad systemLoad : systemLoadList) {
			ImpPlan impPlan = systemLoad.getPlanId();
			LOG.info("Put Date change - " + systemLoad.getLoadDateTime() + " " + putLevel.getPutDateTime() + " " + oldPutLevel.getPutDateTime());
			if (systemLoad.getLoadDateTime() != null && systemLoad.getLoadDateTime().before(putLevel.getPutDateTime())) {
			    PutDateChangeMail putDateChangeMailNotification = (PutDateChangeMail) getMailMessageFactory().getTemplate(PutDateChangeMail.class);
			    putDateChangeMailNotification.setPlan(impPlan);
			    putDateChangeMailNotification.setPutBeforeUpdate(oldPutLevel);
			    putDateChangeMailNotification.setPutAfterUpdate(putLevel);
			    getMailMessageFactory().push(putDateChangeMailNotification);
			}
		    }
		    putLevel.setDeploymentDateMailFlag(Boolean.FALSE);
		}
		getPutLevelDAO().update(lUser, putLevel);

		// 1809 - Reject the plan when Put Level get Upgraded
		if (putLevel.getStatus().equals(Constants.PUTLevelOptions.PRODUCTION.name()) && lCurrentProdPut != null) {
		    rejectPlanOnPutLevelUpgrade(lUser, lCurrentProdPut);
		}
	    } else {
		putLevel.setDeploymentDateMailFlag(Boolean.FALSE);
		getPutLevelDAO().save(lUser, putLevel);
	    }

	    System lSystem = putLevel.getSystemId();
	    if (putLevel.getStatus().equals(Constants.PUTLevelOptions.PRODUCTION.name())) {
		lSystem.setDefalutPutLevel(putLevel.getId());
		getSystemDAO().update(lUser, lSystem);
	    }
	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("", ex);
	    throw new WorkflowException("Unable to Save/Update PutLevel", ex);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse deletePutLevel(User lUser, Integer id) {
	JSONResponse lResponse = new JSONResponse();
	// TODO: Doubts for deleted plans or loaded plans
	List<SystemLoad> lLoads = getSystemLoadDAO().findByPutLevel(id);
	if (lLoads.isEmpty()) {
	    getPutLevelDAO().delete(lUser, id);
	    lResponse.setStatus(Boolean.TRUE);
	} else {
	    List<String> builder = new ArrayList<>();
	    for (SystemLoad lLoad : lLoads) {
		builder.add(lLoad.getPlanId().getId());
	    }
	    lResponse.setErrorMessage("Implementation Plan ( " + String.join(",", builder) + " ) are dependent on this PutLevel");
	    lResponse.setStatus(Boolean.FALSE);
	}
	return lResponse;
    }
    // </editor-fold>

    @Transactional
    public JSONResponse getUserTaskList(User lUser, String pFilter, Integer pOffset, Integer pLimit) {
	JSONResponse lResponse = new JSONResponse();
	List<ImpPlan> lReturnPlanList = new ArrayList();
	List<String> lPlanStatus = new ArrayList<>();
	lPlanStatus.add(Constants.PlanStatus.DEV_MGR_APPROVED.name());
	lReturnPlanList = getImpPlanDAO().getPlansByLoadDateTime(pFilter, lPlanStatus, pOffset, pLimit);
	Long count = getImpPlanDAO().countPlansByLoadDateTime(pFilter, lPlanStatus);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(lReturnPlanList);
	lResponse.setCount(count);
	return lResponse;
    }

    @Transactional
    public JSONResponse getLoadHistory(User lUser) {
	JSONResponse lResponse = new JSONResponse();
	List<ImpPlan> lReturnPlanList = new ArrayList();
	HashMap<String, Serializable> pFilter = new HashMap();
	pFilter.put("planStatus", Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name());
	lReturnPlanList = getImpPlanDAO().findAll(pFilter);
	Long count = getImpPlanDAO().count(pFilter);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(lReturnPlanList);
	lResponse.setCount(count);
	return lResponse;
    }

    @Transactional
    public JSONResponse getProductionLoadsHistory(String[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = getProductionLoadsDAO().findByPlanId(ids);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse readyForProdDeploy(User lUser, String[] lPlanIds) {
	JSONResponse lResponse = new JSONResponse();
	HashMap<String, String> lReturn = new HashMap<>();
	try {
	    List<ImpPlan> lPlans = getImpPlanDAO().find(Arrays.asList(lPlanIds));
	    for (ImpPlan lPlan : lPlans) {

		if (lPlan.getId().startsWith("D") && lPlan.getRfcFlag()) {
		    List<RFCDetails> rfcDetails = getRFCDetailsDAO().findByImpPlan(lPlan.getId());
		    if (rfcDetails.stream().filter(rfc -> rfc.getActive().equals("Y") && (rfc.getRfcNumber() == null || rfc.getRfcNumber().isEmpty())).findAny().isPresent()) {
			throw new WorkflowException("Ready for Production Deployment cannot be done until RFC Number populated. Please Contact DL Core Change team.");
		    }
		}
		LOG.error("Processing for the plan ID: " + lPlan.getId());

		Constants.BUILD_TYPE lBuildType = BUILD_TYPE.STG_LOAD;
		List<SystemLoad> systemLoadList = getSystemLoadDAO().findByImpPlan(lPlan);
		for (SystemLoad systemLoad : systemLoadList) {
		    LOG.error("Processing for the system ID: " + systemLoad.getSystemId().getName());
		    if (!systemLoad.getActive().equals("Y")) {
			LOG.error("Skip for the system ID: " + systemLoad.getSystemId().getName());
			continue;
		    }
		    Build lBuild = getBuildDAO().findLastSuccessfulBuild(lPlan.getId(), systemLoad.getSystemId().getId(), lBuildType);
		    if (lBuild == null) {
			throw new WorkflowException("Staging loadset not created");
		    }

		    boolean ip = getTOSHelper().requestIP(lUser, systemLoad);
		    if (!ip) {
			lReturn.put(lPlan.getId(), "FALSE");
			LOG.error("Unable to get IP Adress for system " + systemLoad.getSystemId().getName());
			throw new WorkflowException("Unable to request for system " + systemLoad.getSystemId().getName());
		    }

		    String ipAddress = getTOSHelper().getIP(systemLoad.getId());

		    if (ipAddress.isEmpty()) {
			lReturn.put(lPlan.getId(), "FALSE");
			LOG.error("Unable to get IP Adress for system " + systemLoad.getSystemId().getName());
			throw new WorkflowException("Unable to get IP Address for system " + systemLoad.getSystemId().getName());
		    }

		    ProdFTPActivityMessage lMessage = new ProdFTPActivityMessage(lBuild.getPlanId(), null, systemLoad);
		    lMessage.setIpAddress(ipAddress);
		    lMessage.setFallback(false);
		    getActivityLogDAO().save(lUser, lMessage);
		    JSONResponse lSSHResponse = getFTPHelper().doFTP(lUser, systemLoad, lBuild, ipAddress, false);
		    if (!lSSHResponse.getStatus()) {
			lReturn.put(lPlan.getId(), "FALSE");
			lMessage.setStatus("Failed");
			getActivityLogDAO().save(lUser, lMessage);
			throw new WorkflowException("FTP failed for system " + systemLoad.getSystemId().getName());
		    } else {
			lReturn.put(lPlan.getId(), "TRUE");
			lMessage.setStatus("Success");
			getActivityLogDAO().save(lUser, lMessage);
			getFTPHelper().getYodaLoadSetPath(lUser, lPlan, lSSHResponse);
		    }
		}
		String lOldStatus = lPlan.getPlanStatus();
		lPlan.setPlanStatus(Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name());
		getImpPlanDAO().update(lUser, lPlan);

		PlanStatusActivityMessage planStatusActivityMessage = new PlanStatusActivityMessage(lPlan, null);
		planStatusActivityMessage.setStatus(Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name());
		getActivityLogDAO().save(lUser, planStatusActivityMessage);

		StatusChangeToDependentPlanMail statusChangeToDependentPlanMail = (StatusChangeToDependentPlanMail) getMailMessageFactory().getTemplate(StatusChangeToDependentPlanMail.class);
		List<ImpPlan> lDependentPlanList = impPlanDAO.findDependentPlans(lPlan.getId());
		if (!lDependentPlanList.isEmpty()) {
		    for (ImpPlan lDependentPlan : lDependentPlanList) {
			statusChangeToDependentPlanMail.addToAddressUserId(lDependentPlan.getLeadId(), Constants.MailSenderRole.LEAD);
		    }
		}

		statusChangeToDependentPlanMail.addToAddressUserId(lPlan.getLeadId(), Constants.MailSenderRole.LEAD);
		statusChangeToDependentPlanMail.setImpPlanId(lPlan.getId());
		statusChangeToDependentPlanMail.setOldStatus(lOldStatus);
		statusChangeToDependentPlanMail.setNewStatus(Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name());
		getMailMessageFactory().push(statusChangeToDependentPlanMail);

		List<TaskVariable> lTaskVars = new ArrayList<>();
		getBPMClientUtils().assignTask(lUser, lPlan.getProcessId(), lUser.getId(), lTaskVars);
		getBPMClientUtils().setTaskAsCompleted(lUser, lPlan.getProcessId());
		String lServiceDeskGroupName = getLdapGroupConfig().getServiceDeskGroups().get(0).getLdapGroupName();
		getBPMClientUtils().assignTaskToGroup(lUser, lPlan.getProcessId(), lServiceDeskGroupName, lTaskVars);

		String lPlatformName = lPlan.getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
		String lRepoName = getJGitClientUtils().getPlanRepoName(lPlatformName, lPlan.getId().toLowerCase());
		List<String> lBranchList = getJGitClientUtils().getAllBranchList(lPlatformName, lPlan.getId().toLowerCase());

		if (!getsSHClientUtils().addImplementationTag(lRepoName, Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT, lBranchList)) {
		    LOG.error("Error in Adding tag READY_FOR_PRODUCTION_DEPLOYMENT for the plan ID : " + lPlan.getId());
		    lReturn.put(lPlan.getId(), "FALSE");
		}
	    }
	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("Unable to Change the Status to Ready for Production Deployment", ex);
	    throw new WorkflowException("Unable to Change the Status to Ready for Production Deployment", ex);
	}
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(lReturn);
	return lResponse;
    }

    @Transactional
    public JSONResponse getLoadFreezeDateByMonth(Integer systemId, Integer year, Integer month) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    Date lLoadDate = new SimpleDateFormat(Constants.LOAD_FREEZE_DATE_TIME_FORMAT).parse(year + "-" + month + "-01");
	    List<LoadFreezeCalendar> lDateList = getLoadFreezeDAO().findCommonDateBySystem(systemId, lLoadDate);
	    Set<LoadFreezeGrouped> lLoadFreezeList = new HashSet();
	    for (LoadFreezeCalendar date : lDateList) {
		List<LoadFreezeGrouped> lLoadFreeze = getLoadFreezeDAO().getLoadFreezeGroupedByDateAndSystem(date.getDate(), systemId);
		if (lLoadFreeze != null && !lLoadFreeze.isEmpty()) {
		    lLoadFreezeList.addAll(lLoadFreeze);
		}
	    }
	    lResponse.setData(lLoadFreezeList);
	    lResponse.setStatus(Boolean.TRUE);
	    return lResponse;
	} catch (Exception Ex) {
	    LOG.error("Unable to get load Freeze date for the date - " + year + " - " + month + " and system id - " + systemId, Ex);
	    throw new WorkflowException("Unable to get Load Freeze date for month  - " + year + " - " + month, Ex);
	}
    }

    @Transactional
    public JSONResponse getSystemLoadByPlan(String[] ids) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = getSystemLoadDAO().findByImpPlanSorted(ids);

	List<SystemLoadDTO> systemLoadDTOs = new ArrayList<>();

	if (findAll != null) {
	    findAll.forEach(sys -> {
		SystemLoadDTO systemLoadDTO = new SystemLoadDTO();
		systemLoadDTO.setSystemLoad((SystemLoad) sys);
		systemLoadDTOs.add(systemLoadDTO);
	    });
	}
	commonHelper.validateToChangePutLevel(systemLoadDTOs);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(systemLoadDTOs.isEmpty() ? null : systemLoadDTOs);
	return lResponse;
    }

    @Transactional
    public JSONResponse getLoadWindowDateByLoadCategory(Integer loadCategoryId, Integer year, Integer month) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    Date lLoadDate = new SimpleDateFormat(Constants.LOAD_FREEZE_DATE_TIME_FORMAT).parse(year + "-" + month + "-01");
	    Collection lDateList = getLoadFreezeDAO().findCommonDateByLoadCategory(loadCategoryId, lLoadDate);
	    lResponse.setData(lDateList);
	    lResponse.setStatus(Boolean.TRUE);
	    return lResponse;
	} catch (Exception Ex) {
	    LOG.error("Unable to get load Freeze date for the date - " + year + " - " + month + " and load category id - " + loadCategoryId, Ex);
	    throw new WorkflowException("Unable to get Load Freeze date for month  - " + year + " - " + month, Ex);
	}
    }

    @Transactional
    public JSONResponse getLoadCategoriesByDate(User user, Integer systemId, String date) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    Date lLoadDate = new SimpleDateFormat(Constants.LOAD_FREEZE_DATE_TIME_FORMAT).parse(date);
	    String lDay = new SimpleDateFormat("EE").format(lLoadDate);
	    List<LoadCategories> lLoadCategories = getLoadCategoriesDAO().getLoadCategoriesByDay(systemId, lDay);
	    if (lLoadCategories != null && !lLoadCategories.isEmpty()) {
		List<LoadCategories> lLoadCategoriesFromFreeze = getLoadCategoriesDAO().getLoadCategoriesBy(lLoadCategories, lLoadDate);
		if (lLoadCategoriesFromFreeze != null && !lLoadCategoriesFromFreeze.isEmpty()) {
		    lLoadCategories.removeAll(lLoadCategoriesFromFreeze);
		}
	    }
	    lResponse.setData(lLoadCategories != null ? new TreeSet<LoadCategories>(lLoadCategories) : null);
	    lResponse.setStatus(Boolean.TRUE);
	    return lResponse;
	} catch (Exception Ex) {
	    LOG.error("Unable to get Load Categories for the date - " + date, Ex);
	    throw new WorkflowException("Unable to get Load Categories for selected date", Ex);
	}
    }

    @Transactional(readOnly = true)
    public JSONResponse getLoadWindowByDay(User user, Integer loadCategoryId, String date, String day) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    List<LoadWindowUI> lLoadWindowsUI = new ArrayList<>();
	    LOG.info("Receievd Date = " + date);
	    LOG.info("Receievd Day = " + day);
	    SimpleDateFormat lReturnFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	    lReturnFormat.setTimeZone(TimeZone.getTimeZone("EST5EDT"));

	    List<LoadWindow> lLoadWindows = getLoadWindowDAO().findByLoadCategoriesAndDay(loadCategoryId, day);

	    for (LoadWindow lLoadWindow : lLoadWindows) {
		LoadWindowUI lLoadWindowUI = new LoadWindowUI();

		String lEST_TimeSlot = lLoadWindow.getTimeSlot();
		LOG.info("Time From DB (EST/EDT) = " + lEST_TimeSlot);
		Date newTimeSlot = lReturnFormat.parse(date + " " + lEST_TimeSlot);
		LOG.info("Converted Return Passed Date = " + Constants.APP_DATE_TIME_FORMAT.get().format(newTimeSlot));

		lLoadWindowUI.setId(lLoadWindow.getId());
		lLoadWindowUI.setActive(lLoadWindow.getActive());
		lLoadWindowUI.setCreatedBy(lLoadWindow.getCreatedBy());
		lLoadWindowUI.setCreatedDt(lLoadWindow.getCreatedDt());
		lLoadWindowUI.setDaysOfWeek(lLoadWindow.getDaysOfWeek());
		lLoadWindowUI.setLoadCategoryId(lLoadWindow.getLoadCategoryId());
		lLoadWindowUI.setModifiedBy(lLoadWindow.getModifiedBy());
		lLoadWindowUI.setModifiedDt(lLoadWindow.getModifiedDt());
		lLoadWindowUI.setTimeSlot(newTimeSlot);
		lLoadWindowsUI.add(lLoadWindowUI);
	    }

	    lResponse.setData(lLoadWindowsUI);
	    lResponse.setStatus(Boolean.TRUE);
	    return lResponse;
	} catch (Exception Ex) {
	    LOG.error("Unable to get Load information for the date - " + date, Ex);
	    throw new WorkflowException("Unable to get Load information for selected date", Ex);
	}
    }

    private Boolean rejectPlanOnPutLevelUpgrade(User user, PutLevel previousProdPut) throws Exception {

	PutLevel lBackUpPut = getPutLevelDAO().find(previousProdPut.getId());
	PutLevel lProdPut = null;
	if (lBackUpPut != null) {
	    lProdPut = getPutLevelDAO().getPutLevel(lBackUpPut.getSystemId(), Constants.PUTLevelOptions.PRODUCTION.name());
	}

	if (lBackUpPut == null || lProdPut == null) {
	    throw new WorkflowException("Prod/BackUp information not found");
	}

	// Secured Plan Operation - 1809
	List<String> lIBMPlansToReject = getCheckoutSegmentsDAO().getPlanByPutLevel(lBackUpPut.getScmUrl(), new ArrayList<>(Constants.PlanStatus.getAfterSubmitStatus().keySet()), lBackUpPut.getSystemId().getName());
	LOG.info("List of Secured Plans to reject " + lIBMPlansToReject.toString());
	for (String lPlan : lIBMPlansToReject) {
	    ImpPlan lImpPlan = getImpPlanDAO().find(lPlan);
	    // Check to avoid processing the already rejected plan i.e., if it is rejected
	    // as part of dependent of
	    // parent
	    if (!lImpPlan.getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.ACTIVE.name())) {
		rejectSecuredPlan(user, lPlan, lBackUpPut, lProdPut);
	    }
	}

	// Unsecured Plan Operation - 1853
	List<String> lIBMPlanList = getCheckoutSegmentsDAO().getPlanByPutLevel(lBackUpPut.getScmUrl(), Arrays.asList(Constants.PlanStatus.ACTIVE.name()), lBackUpPut.getSystemId().getName());
	LOG.info("List of unSecured IBM Plans to process " + lIBMPlanList.toString());
	for (String lPlan : lIBMPlanList) {
	    processActivePlan(user, lPlan, lBackUpPut, lProdPut);
	}

	List<ImpPlan> lNonIBMPlan = getImpPlanDAO().getPlanBySystemAndStatus(lBackUpPut.getSystemId().getId(), Constants.PlanStatus.ACTIVE.name()); // Get all active Plans
	List<String> lNonActiveIBMPlan = new ArrayList();
	lNonIBMPlan.stream().forEach(lPlan -> lNonActiveIBMPlan.add(lPlan.getId()));
	lNonActiveIBMPlan.removeAll(lIBMPlanList); // Remove IBM Plan from list
	LOG.info("List of unSecured Non - IBM Plans to process " + lNonActiveIBMPlan.toString());
	for (String lPlan : lNonActiveIBMPlan) {
	    processActiveNonIBMPlan(user, lPlan, lBackUpPut, lProdPut);
	}

	return Boolean.TRUE;
    }

    private Boolean rejectSecuredPlan(User user, String impPlan, PutLevel backUpPut, PutLevel prodPut) throws Exception {
	ImpPlan lPlan = getImpPlanDAO().find(impPlan);
	String rejectReasonText = "it had IBM source artifacts belonging to an older PUT level - " + backUpPut.getPutLevel();
	getRejectHelper().rejectPlan(user, lPlan.getId(), rejectReasonText, Constants.AUTOREJECT_COMMENT.REJECT.getValue(), Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);

	// putLevelActivityMessage(user, backUpPut, prodPut, lPlan);
	// Send Mail Notification to Lead, Developer, Reviewer, Developer Manager
	RejectionOnPutUpgradeMail lMail = (RejectionOnPutUpgradeMail) getMailMessageFactory().getTemplate(RejectionOnPutUpgradeMail.class);
	lMail.setPlan(lPlan);
	lMail.setBackUpPut(backUpPut);
	lMail.setProdPutLevel(prodPut);

	List<Implementation> findByImpPlan = getImplementationDAO().findByImpPlan(lPlan.getId());
	lMail.addToAddressUserId(lPlan.getLeadId(), Constants.MailSenderRole.LEAD);
	lMail.addToAddressUserId(lPlan.getDevManager(), Constants.MailSenderRole.DEV_MANAGER);
	for (Implementation implementation : findByImpPlan) {
	    lMail.addToAddressUserId(implementation.getDevId(), Constants.MailSenderRole.DEVELOPER);
	    String[] reviewers = implementation.getPeerReviewers().split("\\,");
	    for (String reviewer : reviewers) {
		lMail.addToAddressUserId(reviewer, Constants.MailSenderRole.PEER_REVIEWER);
	    }
	}
	getMailMessageFactory().push(lMail);

	return Boolean.TRUE;
    }

    private void putLevelActivityMessage(User user, PutLevel backUpPut, PutLevel prodPut, ImpPlan lPlan) {
	CommonActivityMessage lMessage = new CommonActivityMessage(lPlan, null);
	String lActMessage = "Default PUT level for System - " + prodPut.getSystemId().getName() + " , Plan - " + lPlan.getId() + " was changed from " + backUpPut.getPutLevel() + " to " + prodPut.getPutLevel();
	lMessage.setMessage(lActMessage);
	lMessage.setStatus("Success");
	getActivityLogDAO().save(user, lMessage);
    }

    private Boolean processActivePlan(User user, String impPlan, PutLevel backUpPut, PutLevel prodPut) throws Exception {
	ImpPlan lPlan = getImpPlanDAO().find(impPlan);

	putLevelActivityMessage(user, backUpPut, prodPut, lPlan);

	// Update Default Put Level with Production
	SystemLoad lSystemLoad = getSystemLoadDAO().findBy(lPlan, backUpPut.getSystemId());
	lSystemLoad.setPutLevelId(prodPut);
	getSystemLoadDAO().update(user, lSystemLoad);

	// Send Email Notification to Developer and Lead
	ActivePlanOnPutUpgradeMail lMail = (ActivePlanOnPutUpgradeMail) getMailMessageFactory().getTemplate(ActivePlanOnPutUpgradeMail.class);
	lMail.setPlan(lPlan);
	lMail.setBackUpPut(backUpPut);
	lMail.setProdPutLevel(prodPut);
	lMail.addToAddressUserId(lPlan.getLeadId(), Constants.MailSenderRole.LEAD);
	List<Implementation> findByImpPlan = getImplementationDAO().findByImpPlan(lPlan.getId());
	findByImpPlan.stream().forEach((imp) -> lMail.addToAddressUserId(imp.getDevId(), Constants.MailSenderRole.DEVELOPER));
	getMailMessageFactory().push(lMail);

	return Boolean.TRUE;
    }

    private Boolean processActiveNonIBMPlan(User user, String impPlan, PutLevel backUpPut, PutLevel prodPut) throws Exception {
	ImpPlan lPlan = getImpPlanDAO().find(impPlan);

	putLevelActivityMessage(user, backUpPut, prodPut, lPlan);

	// Update Default Put Level with Production
	SystemLoad lSystemLoad = getSystemLoadDAO().findBy(lPlan, backUpPut.getSystemId());
	lSystemLoad.setPutLevelId(prodPut);
	getSystemLoadDAO().update(user, lSystemLoad);

	return Boolean.TRUE;
    }
}
