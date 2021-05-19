/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.service;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.LdapGroupConfig;
import com.tsi.workflow.TOSConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.activity.CommonActivityMessage;
import com.tsi.workflow.activity.DbcrActivityMessage;
import com.tsi.workflow.activity.DbcrValidationMessage;
import com.tsi.workflow.activity.DevManagerChangeActivityMessage;
import com.tsi.workflow.activity.DevlBuildActivityMessage;
import com.tsi.workflow.activity.DevlLoadActivityMessage;
import com.tsi.workflow.activity.ImpPlanApprovalActivityMessage;
import com.tsi.workflow.activity.ImplementationStatusRevertActivityMessage;
import com.tsi.workflow.activity.LoadDateChangesActivityMessage;
import com.tsi.workflow.activity.LoadSetActivityMessage;
import com.tsi.workflow.activity.LoadTypeChangesActivityMessage;
import com.tsi.workflow.activity.PlanCreationActivityMessage;
import com.tsi.workflow.activity.PlanStatusActivityMessage;
import com.tsi.workflow.activity.PreProdActionsActivityLog;
import com.tsi.workflow.activity.ProdFTPActivityMessage;
import com.tsi.workflow.activity.QAFunTesterAssignReAssignActivityMessage;
import com.tsi.workflow.activity.ReassignImplementationPlanActivityMessage;
import com.tsi.workflow.activity.RejectionActivityMessage;
import com.tsi.workflow.activity.SetReadyForQAActivityMessage;
import com.tsi.workflow.activity.StgLoadActivityMessage;
import com.tsi.workflow.activity.StgLoadFallBackActivityMessage;
import com.tsi.workflow.activity.TssResponseActivityMessage;
import com.tsi.workflow.activity.YodaResponseActivityMessage;
import com.tsi.workflow.audit.helper.AuditCommonHelper;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.BaseService;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.Dbcr;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.ImpPlanApprovals;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.PdddsLibrary;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.Project;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.beans.dao.RFCDetails;
import com.tsi.workflow.beans.dao.RepositoryDetails;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemCpu;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dao.SystemLoadActions;
import com.tsi.workflow.beans.dao.SystemPdddsMapping;
import com.tsi.workflow.beans.dao.UserSettings;
import com.tsi.workflow.beans.dao.Vpars;
import com.tsi.workflow.beans.ui.AdvanceSearchSystemBasedDetails;
import com.tsi.workflow.beans.ui.AdvancedMetaSearchResult;
import com.tsi.workflow.beans.ui.AdvancedSearchForm;
import com.tsi.workflow.beans.ui.AdvancedSearchResultbyPlan;
import com.tsi.workflow.beans.ui.BuildLogBean;
import com.tsi.workflow.beans.ui.BuildQueueForm;
import com.tsi.workflow.beans.ui.BuildQueuePlanForm;
import com.tsi.workflow.beans.ui.DependentPlanRejectDetail;
import com.tsi.workflow.beans.ui.DevlBuildForm;
import com.tsi.workflow.beans.ui.FileExtnReportForm;
import com.tsi.workflow.beans.ui.JobDetails;
import com.tsi.workflow.beans.ui.PutLevelUIForm;
import com.tsi.workflow.beans.ui.RepoDetailsSearch;
import com.tsi.workflow.beans.ui.RepoReportView;
import com.tsi.workflow.beans.ui.RepoSearchForm;
import com.tsi.workflow.beans.ui.ReportDetailView;
import com.tsi.workflow.beans.ui.ReportForm;
import com.tsi.workflow.beans.ui.ReportModel;
import com.tsi.workflow.beans.ui.ReportQATestingContent;
import com.tsi.workflow.beans.ui.ReportQATestingData;
import com.tsi.workflow.beans.ui.ReportQATestingSummary;
import com.tsi.workflow.beans.ui.ReportTable;
import com.tsi.workflow.beans.ui.ReportView;
import com.tsi.workflow.beans.ui.RepositoryView;
import com.tsi.workflow.beans.ui.SegmentBasedActionDetail;
import com.tsi.workflow.beans.ui.SegmentReportDetailView;
import com.tsi.workflow.beans.ui.SegmentSearchForm;
import com.tsi.workflow.beans.ui.SourceArtifactSearchForm;
import com.tsi.workflow.beans.ui.SourceArtifactSearchResult;
import com.tsi.workflow.beans.ui.SummaryDetailView;
import com.tsi.workflow.beans.ui.YodaResult;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.bpm.model.TaskVariable;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.AdvanceSearchViewDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.DbcrDAO;
import com.tsi.workflow.dao.ImpPlanApprovalsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.PdddsLibraryDAO;
import com.tsi.workflow.dao.PlatformDAO;
import com.tsi.workflow.dao.PreProductionLoadsDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.dao.ProjectDAO;
import com.tsi.workflow.dao.PutLevelDAO;
import com.tsi.workflow.dao.RFCDetailsDAO;
import com.tsi.workflow.dao.RepoDetailsDAO;
import com.tsi.workflow.dao.SystemCpuDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.dao.SystemPdddsMappingDAO;
import com.tsi.workflow.dao.UserSettingsDAO;
import com.tsi.workflow.dao.VparsDAO;
import com.tsi.workflow.dao.external.TestSystemLoadDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.GitBranchSearchResult;
import com.tsi.workflow.git.GitProdCommitMessage;
import com.tsi.workflow.git.GitSearchResult;
import com.tsi.workflow.git.IJGITSearchUtils;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import com.tsi.workflow.gitblit.model.AccessPermission;
import com.tsi.workflow.gitblit.model.Repository;
import com.tsi.workflow.helper.CommonHelper;
import com.tsi.workflow.helper.DateAuditCrossCheck;
import com.tsi.workflow.helper.DbcrHelper;
import com.tsi.workflow.helper.DelegateHelper;
import com.tsi.workflow.helper.FTPHelper;
import com.tsi.workflow.helper.GITHelper;
import com.tsi.workflow.helper.LoadActivationCompare;
import com.tsi.workflow.helper.PRNumberHelper;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.mail.CompilerValidationMail;
import com.tsi.workflow.mail.DevManagerAssignmentMail;
import com.tsi.workflow.mail.LoadDateChangedMail;
import com.tsi.workflow.mail.PreProdLoadsetActivationMail;
import com.tsi.workflow.mail.QALoadsetActivationMail;
import com.tsi.workflow.mail.QaFunctionalTesterReassignmentMail;
import com.tsi.workflow.mail.ReassignImplementationPlanMail;
import com.tsi.workflow.mail.RfcCalendarInviteMail;
import com.tsi.workflow.report.SearchExcelCreator;
import com.tsi.workflow.snow.pr.SnowPRClientUtils;
import com.tsi.workflow.snow.pr.model.PRResponse;
import com.tsi.workflow.utils.CheckoutUtils;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.BUILD_TYPE;
import com.tsi.workflow.utils.Constants.LOAD_SET_STATUS;
import com.tsi.workflow.utils.Constants.VPARSEnvironment;
import com.tsi.workflow.utils.DateHelper;
import com.tsi.workflow.utils.DeploymentActivityExcelExport;
import com.tsi.workflow.utils.FileIOUtils;
import com.tsi.workflow.utils.JSONResponse;
import com.tsi.workflow.utils.LoadSetUtils;
import com.tsi.workflow.websocket.WSMessagePublisher;
import com.workflow.ssh.SSHClientUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author USER
 */
@Service
public class ProtectedService extends BaseService {

    private static final Logger LOG = Logger.getLogger(ProtectedService.class.getName());

    @Autowired
    LDAPAuthenticatorImpl lDAPAuthenticatorImpl;
    @Autowired
    CacheClient lCacheClient;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    TOSConfig tosConfig;
    @Autowired
    ImpPlanApprovalsDAO impPlanApprovalsDAO;
    @Autowired
    ImplementationDAO implementationDAO;
    @Autowired
    CheckoutSegmentsDAO checkoutSegmentsDAO;
    @Autowired
    PreProductionLoadsDAO preProductionLoadsDAO;
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    AdvanceSearchViewDAO advanceSearchViewDAO;
    @Autowired
    SystemDAO systemDAO;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    SystemLoadActionsDAO systemLoadActionsDAO;
    @Autowired
    BuildDAO buildDAO;
    @Autowired
    VparsDAO vparsDAO;
    @Autowired
    JGitClientUtils jGitClientUtils;
    @Autowired
    GITConfig gITConfig;
    @Autowired
    GitBlitClientUtils gitBlitClientUtils;
    @Autowired
    JenkinsClient jenkinsClient;
    @Autowired
    ConcurrentHashMap<String, JenkinsBuild> develBuildJob;
    @Autowired
    RejectHelper rejectHelper;
    @Autowired
    SystemCpuDAO systemCpuDAO;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    TestSystemLoadDAO testSystemLoadDAO;
    @Autowired
    @Qualifier("develLoaderJob")
    ConcurrentLinkedQueue<JenkinsBuild> develLoaderJob;
    @Autowired
    @Qualifier("stagingBuildJobs")
    ConcurrentLinkedQueue<JenkinsBuild> stagingBuildJobs;
    @Autowired
    @Qualifier("stagingLoaderJobs")
    ConcurrentLinkedQueue<JenkinsBuild> stagingLoaderJobs;
    @Autowired
    DbcrDAO dbcrDAO;
    @Autowired
    DbcrHelper dbcrHelper;
    @Autowired
    FTPHelper fTPHelper;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    UserSettingsDAO userSettingsDAO;
    @Autowired
    DateAuditCrossCheck dateAuditCrossCheck;
    @Autowired
    DelegateHelper delegateHelper;
    @Autowired
    @Qualifier("lSuperUserMap")
    ConcurrentHashMap<String, SortedSet<User>> lSuperUserMap;
    @Autowired
    RepoDetailsDAO repoDetailsDAO;
    /*
     * @Autowired
     *
     * @Qualifier("lPlanUpdateStatusMap") ConcurrentHashMap<String, User>
     * lPlanUpdateStatusMap;
     */
    @Autowired
    BPMClientUtils lBPMClientUtils;
    @Autowired
    LdapGroupConfig ldapGroupConfig;
    @Autowired
    SystemPdddsMappingDAO systemPdddsMappingDAO;
    @Autowired
    PdddsLibraryDAO pdddsLibraryDAO;
    @Autowired
    PlatformDAO platformDAO;
    @Autowired
    GITHelper gitHelper;
    @Autowired
    SSHClientUtils sSHClientUtils;
    @Autowired
    CacheClient cacheClient;
    @Autowired
    WSMessagePublisher wsserver;
    @Autowired
    GitBlitClientUtils lGitBlitClientUtils;
    @Autowired
    PlanHelper planHelper;
    @Autowired
    DeploymentActivityExcelExport deploymentActivityExcelExport;
    @Autowired
    ProjectDAO projectDAO;
    @Autowired
    IJGITSearchUtils jGITSearchUtils;
    @Autowired
    ConcurrentHashMap<String, Long> lAsyncPlansStartTimeMap;
    @Autowired
    CommonHelper commonHelper;
    @Autowired
    RFCDetailsDAO lRFCDetailsDAO;
    @Autowired
    ProductionLoadsDAO productionLoadsDAO;
    @Autowired
    AuditCommonHelper auditCommonHelper;
    @Autowired
    PutLevelDAO putLevelDAO;
    @Autowired
    SnowPRClientUtils snowPRClientUtils;
    @Autowired
    PRNumberHelper prNumberHelper;

    public ProductionLoadsDAO getProductionLoadsDAO() {
	return productionLoadsDAO;
    }

    public RFCDetailsDAO getRFCDetailsDAO() {
	return lRFCDetailsDAO;
    }

    public IJGITSearchUtils getjGITSearchUtils() {
	return jGITSearchUtils;
    }

    public ProjectDAO getProjectDAO() {
	return projectDAO;
    }

    public CacheClient getCacheClient() {
	return cacheClient;
    }

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public PlatformDAO getPlatformDAO() {
	return platformDAO;
    }

    public LDAPAuthenticatorImpl getLDAPAuthenticatorImpl() {
	return lDAPAuthenticatorImpl;
    }

    public LdapGroupConfig getLdapGroupConfig() {
	return ldapGroupConfig;
    }

    public WFConfig getWFConfig() {
	return wFConfig;
    }

    public BPMClientUtils getBPMClientUtils() {
	return lBPMClientUtils;
    }

    public TOSConfig getTOSConfig() {
	return tosConfig;
    }

    public ImpPlanApprovalsDAO getImpPlanApprovalsDAO() {
	return impPlanApprovalsDAO;
    }

    public ImplementationDAO getImplementationDAO() {
	return implementationDAO;
    }

    public CheckoutSegmentsDAO getCheckoutSegmentsDAO() {
	return checkoutSegmentsDAO;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public SystemDAO getSystemDAO() {
	return systemDAO;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public PreProductionLoadsDAO getPreProductionLoadsDAO() {
	return preProductionLoadsDAO;
    }

    public SystemLoadActionsDAO getSystemLoadActionsDAO() {
	return systemLoadActionsDAO;
    }

    public BuildDAO getBuildDAO() {
	return buildDAO;
    }

    public VparsDAO getVparsDAO() {
	return vparsDAO;
    }

    public JGitClientUtils getJGitClientUtils() {
	return jGitClientUtils;
    }

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public GitBlitClientUtils getGitBlitClientUtils() {
	return gitBlitClientUtils;
    }

    public JenkinsClient getJenkinsClient() {
	return jenkinsClient;
    }

    public RejectHelper getRejectHelper() {
	return rejectHelper;
    }

    public SystemCpuDAO getSystemCpuDAO() {
	return systemCpuDAO;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public TestSystemLoadDAO getTestSystemLoadDAO() {
	return testSystemLoadDAO;
    }

    public DbcrDAO getDbcrDAO() {
	return dbcrDAO;
    }

    public DbcrHelper getDbcrHelper() {
	return dbcrHelper;
    }

    public FTPHelper getFTPHelper() {
	return fTPHelper;
    }

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    public UserSettingsDAO getUserSettingsDAO() {
	return userSettingsDAO;
    }

    public DateAuditCrossCheck getDateAuditCrossCheck() {
	return dateAuditCrossCheck;
    }

    public DelegateHelper getDelegateHelper() {
	return delegateHelper;
    }

    public SystemPdddsMappingDAO getSystemPdddsMappingDAO() {
	return systemPdddsMappingDAO;
    }

    public PdddsLibraryDAO getPdddsLibraryDAO() {
	return pdddsLibraryDAO;
    }

    public GITHelper getGitHelper() {
	return gitHelper;
    }

    public GitBlitClientUtils getlGitBlitClientUtils() {
	return lGitBlitClientUtils;
    }

    public PlanHelper getPlanHelper() {
	return planHelper;
    }

    public CommonHelper getCommonHelper() {
	return commonHelper;
    }

    public AuditCommonHelper getAuditCommonHelper() {
	return auditCommonHelper;
    }

    public PRNumberHelper getPRNumberHelper() {
	return prNumberHelper;
    }

    public RepoDetailsDAO getRepoDetailsDAO() {
	return repoDetailsDAO;
    }

    public SnowPRClientUtils getSnowPRClientUtils() {
	return snowPRClientUtils;
    }

    public PutLevelDAO getPutLevelDAO() {
	return putLevelDAO;
    }

    @Transactional
    public JSONResponse getDeploymentPlanList(User lUser, boolean isFunctional, Constants.LoaderTypes loaderTypes, String pFilter, Integer offset, Integer limit, LinkedHashMap<String, String> lOrderBy) {
	// (QA / LEAD / TSS Delta), TSS Travelport Separate
	User pUser = lUser.getCurrentOrDelagateUser();
	JSONResponse lResponse = new JSONResponse();
	List<String> status = new ArrayList();
	List<String> lQAStatusFilter = new ArrayList();
	BUILD_TYPE build_type;
	List<ImpPlan> impPlanList = new ArrayList();
	Long lcount;
	try {
	    if (pUser.getCurrentRole().equals(Constants.UserGroup.Lead.name())) {
		status = new ArrayList(Constants.PlanStatus.getNonProdStatusMap().keySet());
		build_type = BUILD_TYPE.DVL_LOAD;
	    } else if (isFunctional && pUser.getCurrentRole().equals(Constants.UserGroup.QA.name())) {
		status = new ArrayList(Constants.PlanStatus.getQAFunctionalDeploymentStatus().keySet());
		build_type = BUILD_TYPE.STG_LOAD;
		lQAStatusFilter.add("NONE");
		lQAStatusFilter.add(Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name());
	    } else if (!isFunctional && pUser.getCurrentRole().equals(Constants.UserGroup.QA.name())) {
		status = new ArrayList(Constants.PlanStatus.getQARegressionDeploymentStatus().keySet());
		build_type = BUILD_TYPE.STG_LOAD;
		lQAStatusFilter.add("NONE");
		lQAStatusFilter.add(Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name());
	    } else if (pUser.getCurrentRole().equals(Constants.UserGroup.SystemSupport.name())) {
		status = new ArrayList(Constants.PlanStatus.getTSSDeploymentStatus().keySet());
		build_type = BUILD_TYPE.STG_LOAD;
	    } else {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("Not a valid role for deployment");
		return lResponse;
	    }
	    int offsetNew = offset * limit;
	    if (pUser.getCurrentRole().equals(Constants.UserGroup.QA.name())) {
		impPlanList = getImpPlanDAO().findQAPlanList(status, pFilter, build_type, loaderTypes, lQAStatusFilter, offset, limit, lOrderBy);
		lcount = getImpPlanDAO().countByQAStatusList(status, pFilter, build_type, loaderTypes, lQAStatusFilter);
	    } else if (pUser.getCurrentRole().equals(Constants.UserGroup.SystemSupport.name())) {
		List<ImpPlan> planList = getImpPlanDAO().findByStatusList(status, true, false, null, pFilter, build_type, loaderTypes, offsetNew, limit, lOrderBy, false);
		impPlanList = getSortedYodaPlansDetails(lUser, planList, offset, limit);
		lcount = getImpPlanDAO().countByStatusList(status, true, pFilter, build_type, loaderTypes);
	    } else {
		impPlanList = getImpPlanDAO().findByStatusList(status, false, true, pUser.getId(), pFilter, build_type, loaderTypes, offsetNew, limit, lOrderBy, true);

		if (impPlanList.size() < limit) {
		    lcount = getImpPlanDAO().countByStatusListUser(status, pUser.getId(), false, pFilter, build_type, loaderTypes);
		    offset = Integer.valueOf("" + ((offset * limit) - lcount));
		    limit = limit - impPlanList.size();

		    impPlanList.addAll(getImpPlanDAO().findByStatusList(status, false, false, pUser.getId(), pFilter, build_type, loaderTypes, offset, limit, lOrderBy, true));
		}
		lcount = getImpPlanDAO().countByStatusList(status, false, pFilter, build_type, loaderTypes);

	    }
	    List<ImpPlan> impPlanFinalList = new ArrayList();
	    if (lOrderBy != null && !lOrderBy.isEmpty()) {
		for (Map.Entry<String, String> entrySet : lOrderBy.entrySet()) {
		    String key = entrySet.getKey();
		    String value = entrySet.getValue();
		    if ("id".equalsIgnoreCase(key)) {
			if (value.equalsIgnoreCase("asc")) {
			    impPlanFinalList = impPlanList.stream().sorted(Comparator.comparing(ImpPlan::getId)).collect(Collectors.toList());
			} else {
			    impPlanFinalList = impPlanList.stream().sorted(Comparator.comparing(ImpPlan::getId).reversed()).collect(Collectors.toList());
			}
		    }
		}
	    } else {
		impPlanFinalList = impPlanList.stream().sorted(Comparator.comparing(ImpPlan::getId)).collect(Collectors.toList());
	    }

	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setCount(lcount);
	    lResponse.setData(impPlanFinalList);
	} catch (Exception ex) {
	    LOG.error("Unable to fetch Implementation Plan List for Deployment", ex);
	    throw new WorkflowException("Unable to fetch Implementation Plan List for Deployment!", ex);
	}
	return lResponse;
    }

    private List<ImpPlan> getSortedYodaPlansDetails(User lUser, List<ImpPlan> impPlanList, Integer offset, Integer limit) {

	List<ImpPlan> finalList = new ArrayList<>();
	List<String> processedPlans = new ArrayList<>();

	// Get filtered list
	List<ImpPlan> filteredList = new ArrayList<>();
	if (impPlanList.size() > 0) {
	    List<Constants.VPARSEnvironment> pVpars = new ArrayList<>();
	    pVpars.add(Constants.VPARSEnvironment.PRE_PROD);
	    JSONResponse lResponse = new JSONResponse();
	    String[] temp = new String[impPlanList.size()];
	    for (int i = 0; i < impPlanList.size(); i++) {
		temp[i] = impPlanList.get(i).getId();
	    }
	    lResponse = getSystemLoadActions(lUser, false, temp);
	    if (lResponse.getStatus()) {
		List<SystemLoadActions> preProdPlans = (List<SystemLoadActions>) lResponse.getData();
		if (preProdPlans != null) {

		    // Group by Plan id
		    HashMap<String, List<SystemLoadActions>> PlanIdandSysLoadDetails = new HashMap<>();
		    preProdPlans.forEach(preProd -> {
			if (PlanIdandSysLoadDetails.containsKey(preProd.getPlanId().getId())) {
			    PlanIdandSysLoadDetails.get(preProd.getPlanId().getId()).add(preProd);
			} else {
			    List<SystemLoadActions> temp1 = new ArrayList<>();
			    temp1.add(preProd);
			    PlanIdandSysLoadDetails.put(preProd.getPlanId().getId(), temp1);
			}
		    });

		    Set<String> preProdPlanIds = preProdPlans.stream().map(pre -> pre.getPlanId().getId()).collect(Collectors.toSet());
		    // Get list of plans which isn't available in pre prod
		    List<ImpPlan> plansToBeLoaded = impPlanList.stream().filter(plan -> !preProdPlanIds.contains(plan.getId())).collect(Collectors.toList());

		    // Plans Which needs to be loaded(Plans status: Fallback Activated, Deleted,
		    // null)
		    impPlanList.stream().filter(plan -> preProdPlanIds.contains(plan.getId())).forEach(plan -> {
			List<SystemLoadActions> preProds = PlanIdandSysLoadDetails.get(plan.getId());
			if (preProds.stream().anyMatch(pre -> pre.getStatus() == null || pre.getStatus().isEmpty() || pre.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DELETED.name()))) {
			    plansToBeLoaded.add(plan);
			    preProdPlanIds.remove(plan.getId());
			}
		    });
		    getImpPlanBasedOnLoadDateSorted(plansToBeLoaded, finalList, processedPlans);

		    // Plan which is loaded and to be activated
		    List<ImpPlan> planToBeActivated = new ArrayList<>();
		    impPlanList.stream().filter(plan -> preProdPlanIds.contains(plan.getId())).forEach(plan -> {
			List<SystemLoadActions> preProds = PlanIdandSysLoadDetails.get(plan.getId());
			if (preProds.stream().anyMatch(pre -> pre.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.LOADED.name()))) {
			    planToBeActivated.add(plan);
			    preProdPlanIds.remove(plan.getId());
			}
		    });
		    getImpPlanBasedOnLoadDateSorted(planToBeActivated, finalList, processedPlans);

		    // Plan which is loaded and to be Desactivated
		    List<ImpPlan> planToBeDeactivated = new ArrayList<>();
		    impPlanList.stream().filter(plan -> preProdPlanIds.contains(plan.getId())).forEach(plan -> {
			List<SystemLoadActions> preProds = PlanIdandSysLoadDetails.get(plan.getId());
			if (preProds.stream().anyMatch(pre -> pre.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name()))) {
			    planToBeDeactivated.add(plan);
			    preProdPlanIds.remove(plan.getId());
			}
		    });
		    getImpPlanBasedOnLoadDateSorted(planToBeDeactivated, finalList, processedPlans);

		    // Get list of plans which is available in pre prod
		    List<ImpPlan> planExistInPreProd = impPlanList.stream().filter(plan -> preProdPlanIds.contains(plan.getId())).collect(Collectors.toList());
		    getImpPlanBasedOnLoadDateSorted(planExistInPreProd, finalList, processedPlans);

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
	    }
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
    public JSONResponse getSystemLoadActions(User pUser, boolean isFunctional, String[] planids) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	User lUser = pUser.getCurrentOrDelagateUser();
	List<VPARSEnvironment> lVparsType = new ArrayList<>();
	List<SystemLoadActions> sysLoadActionList = new ArrayList<>();

	if (lUser.getCurrentRole().equals(Constants.UserGroup.Lead.name())) {
	    lVparsType.add(Constants.VPARSEnvironment.INTEGRATION);
	    lVparsType.add(Constants.VPARSEnvironment.PRIVATE);
	} else if (isFunctional && lUser.getCurrentRole().equals(Constants.UserGroup.QA.name())) {
	    lVparsType.add(Constants.VPARSEnvironment.QA_FUCTIONAL);
	} else if (isFunctional && lUser.getCurrentRole().equals(Constants.UserGroup.QADeployLead.name())) {
	    lVparsType.add(Constants.VPARSEnvironment.QA_FUCTIONAL);
	} else if (!isFunctional && lUser.getCurrentRole().equals(Constants.UserGroup.QA.name())) {
	    lVparsType.add(Constants.VPARSEnvironment.QA_REGRESSION);
	} else if (lUser.getCurrentRole().equals(Constants.UserGroup.SystemSupport.name())) {
	    lVparsType.add(Constants.VPARSEnvironment.PRE_PROD);
	}

	List<SystemLoadActions> pLoadActionList = getSystemLoadActionsDAO().findByPlan(planids, lVparsType, pUser);

	for (SystemLoadActions loadAct : pLoadActionList) {
	    if (loadAct != null && (loadAct.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name()))) {
		loadAct.setIsVparActivated(Boolean.TRUE);
	    }
	    sysLoadActionList.add(loadAct);
	}

	List<SystemLoadActions> planList = new ArrayList<>();
	List<Object[]> planNotInSystemLoadActions = getSystemLoadActionsDAO().plansNotInSystemLoadActions(planids, lVparsType);
	for (Object[] loadAction : planNotInSystemLoadActions) {
	    SystemLoadActions systemLoadActions = new SystemLoadActions();
	    String planId = loadAction[0].toString();
	    int systemId = Integer.parseInt(loadAction[1].toString());
	    int vparid = Integer.parseInt(loadAction[2].toString());
	    if (!sysLoadActionList.stream().filter(sysAct -> planId.equals(sysAct.getPlanId().getId()) && systemId == sysAct.getSystemId().getId()).findAny().isPresent()) {
		System sId = new System();
		Vpars vPar = new Vpars();
		vPar.setId(vparid);
		sId.setId(systemId);
		systemLoadActions.setPlanId(getImpPlanDAO().find(planId));
		systemLoadActions.setSystemId(sId);
		systemLoadActions.setVparId(vPar);
		planList.add(systemLoadActions);
	    }
	}
	sysLoadActionList.addAll(planList);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(sysLoadActionList);
	lResponse.setCount(sysLoadActionList.size());
	return lResponse;
    }

    @Transactional
    public JSONResponse getDeploymentVParsList(User lUser, boolean isFunctional, String[] pPlanIds) {
	User pUser = lUser.getCurrentOrDelagateUser();
	HashMap<String, List<Vpars>> lReturn = new HashMap<>();
	JSONResponse response = new JSONResponse();
	for (String lPlanId : pPlanIds) {
	    List<Vpars> lVpars = new ArrayList<>();
	    ImpPlan lPlan = getImpPlanDAO().find(lPlanId);
	    BUILD_TYPE lBuildType = null;
	    VPARSEnvironment lVparsType = null;

	    if (pUser.getCurrentRole().equals(Constants.UserGroup.Lead.name())) {
		if (Constants.PlanStatus.getBeforeApprovedStatus().containsKey(lPlan.getPlanStatus())) {
		    lBuildType = BUILD_TYPE.DVL_LOAD;
		} else {
		    lBuildType = BUILD_TYPE.STG_LOAD;
		}
		lVparsType = Constants.VPARSEnvironment.INTEGRATION;
	    } else if (isFunctional && pUser.getCurrentRole().equals(Constants.UserGroup.QA.name())) {
		lBuildType = BUILD_TYPE.STG_LOAD;
		lVparsType = Constants.VPARSEnvironment.QA_FUCTIONAL;
	    } else if (isFunctional && pUser.getCurrentRole().equals(Constants.UserGroup.QADeployLead.name())) {
		lBuildType = BUILD_TYPE.STG_LOAD;
		lVparsType = Constants.VPARSEnvironment.QA_FUCTIONAL;
	    } else if (!isFunctional && pUser.getCurrentRole().equals(Constants.UserGroup.QA.name())) {
		lBuildType = BUILD_TYPE.STG_LOAD;
		lVparsType = Constants.VPARSEnvironment.QA_REGRESSION;
	    } else if (pUser.getCurrentRole().equals(Constants.UserGroup.SystemSupport.name())) {
		lBuildType = BUILD_TYPE.STG_LOAD;
		lVparsType = Constants.VPARSEnvironment.PRE_PROD;
	    } else {
		response.setStatus(Boolean.FALSE);
		response.setErrorMessage("Not a valid role for deployment");
		return response;
	    }

	    if (lBuildType != null) {
		List<Build> buildList = getBuildDAO().findAll(lPlan, lBuildType);
		if (!buildList.isEmpty()) {
		    List<System> lETypeSystems = new ArrayList<>();
		    List<System> lATypeSystems = new ArrayList<>();
		    for (Build b : buildList) {
			if (b.getJobStatus() != null && b.getLoadSetType() != null && b.getJobStatus().equals("S") && b.getLoadSetType().equals(Constants.LoaderTypes.E.name())) {
			    lETypeSystems.add(b.getSystemId());
			} else if (b.getJobStatus() != null && b.getLoadSetType() != null && b.getJobStatus().equals("S") && b.getLoadSetType().equals(Constants.LoaderTypes.A.name())) {
			    lATypeSystems.add(b.getSystemId());
			}
		    }

		    if (!lETypeSystems.isEmpty()) {
			lVpars = new ArrayList<>(getVparsDAO().findBySystem(lETypeSystems, lVparsType));
			if (pUser.getCurrentRole().equals(Constants.UserGroup.Lead.name())) {
			    List<Vpars> findBySystem = getVparsDAO().findBySystem(lETypeSystems, Constants.VPARSEnvironment.PRIVATE, pUser.getId());
			    if (findBySystem != null && !findBySystem.isEmpty()) {
				lVpars.addAll(findBySystem);
			    }
			}
		    } else if (!lATypeSystems.isEmpty()) {
			lVpars = new ArrayList<>(getVparsDAO().findBySystemNotAutoDeployedTSS(lATypeSystems, lVparsType));
			if (pUser.getCurrentRole().equals(Constants.UserGroup.Lead.name())) {
			    List<Vpars> findBySystem = getVparsDAO().findBySystem(lATypeSystems, Constants.VPARSEnvironment.PRIVATE, pUser.getId());
			    if (findBySystem != null && !findBySystem.isEmpty()) {
				lVpars.addAll(findBySystem);
			    }
			}
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
    public JSONResponse uploadExceptionLoadApproval(User pUser, String planId, MultipartFile pFile, String approvalCmt) {
	// TODO: ACCESS
	JSONResponse lResponse = new JSONResponse();
	try {
	    if (pFile != null) {
		String lFileName = getWFConfig().getAttachmentDirectory() + planId + File.separator + getWFConfig().getLoadApprovalDirectory() + pFile.getOriginalFilename();
		File lFile = new File(lFileName);
		if (!lFile.getParentFile().exists()) {
		    lFile.getParentFile().mkdirs();
		}
		pFile.transferTo(lFile);
		ImpPlan lPlan = getImpPlanDAO().find(planId);
		ImpPlanApprovalActivityMessage lPlanApprovalActivity = new ImpPlanApprovalActivityMessage(lPlan, null);
		lPlanApprovalActivity.setFileName(pFile.getOriginalFilename());
		lPlanApprovalActivity.setAction("");
		lPlanApprovalActivity.setApprovalComment(approvalCmt);
		getActivityLogDAO().save(pUser, lPlanApprovalActivity);
		lResponse.setStatus(Boolean.TRUE);
	    }
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Unable to upload exception load file", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse saveExceptionLoadApproval(User pUser, ImpPlanApprovals pApproval) {
	// TODO: ACCESS
	JSONResponse lResponse = new JSONResponse();
	try {
	    getImpPlanApprovalsDAO().save(pUser, pApproval);
	    // ZTPFM-2013 Save ActivityLog Message while adding data

	    ImpPlanApprovalActivityMessage lPlanApprovalActivity = new ImpPlanApprovalActivityMessage(pApproval.getPlanId(), null);
	    lPlanApprovalActivity.setAction("added");
	    lPlanApprovalActivity.setApprovalComment(pApproval.getComments());
	    getActivityLogDAO().save(pUser, lPlanApprovalActivity);
	    lResponse.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Unable to Save Approval Document", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse updateExceptionLoadApproval(User pUser, ImpPlanApprovals pApproval) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    if (pApproval.getCreatedDt() == null && pApproval.getFileName() != null) {
		pApproval.setCreatedDt(new Date());
	    }
	    String oldApprovalComment = null;
	    if (pApproval.getComments() == null || pApproval.getComments().isEmpty()) {
		ImpPlanApprovals planApp = getImpPlanApprovalsDAO().findByUniquePlan(pApproval.getPlanId().getId());
		oldApprovalComment = planApp.getComments();
	    }
	    getImpPlanApprovalsDAO().update(pUser, pApproval);
	    ImpPlanApprovalActivityMessage lPlanApprovalActivity = new ImpPlanApprovalActivityMessage(pApproval.getPlanId(), null);
	    if (oldApprovalComment != null) {
		lPlanApprovalActivity.setFileName(pApproval.getFileName());
		lPlanApprovalActivity.setOldComment(oldApprovalComment);
		lPlanApprovalActivity.setAction("");
	    } else {
		lPlanApprovalActivity.setAction("updated");
		lPlanApprovalActivity.setApprovalComment(pApproval.getComments());
	    }
	    getActivityLogDAO().save(pUser, lPlanApprovalActivity);
	    lResponse.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Unable to Update Approval Document", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse deleteAttachedFile(User pUser, String pPlanId, String pFileName) {
	JSONResponse lResponse = new JSONResponse();
	File lTestFile = new File(getWFConfig().getAttachmentDirectory() + pPlanId + File.separator + getWFConfig().getLoadApprovalDirectory() + pFileName);
	ImpPlanApprovals planApp = getImpPlanApprovalsDAO().findByPlanAndFileName(pPlanId, pFileName);
	if (lTestFile.exists() && planApp != null) {
	    planApp.setFileName(null);
	    planApp.setCreatedDt(null);
	    getImpPlanApprovalsDAO().update(pUser, planApp);

	    boolean delete = lTestFile.delete();
	    if (!delete) {
		lResponse.setErrorMessage(" Unable to to delete test results file ");
	    }
	    lResponse.setStatus(delete);
	    lResponse.setData("File Deleted Success ");
	} else {
	    lResponse.setErrorMessage("Testcase not found");
	    lResponse.setStatus(Boolean.FALSE);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse deleteLoadApproval(User pUser, Integer pId) {
	// TODO: ACCESS
	JSONResponse lResponse = new JSONResponse();
	try {
	    ImpPlanApprovals pApproval = getImpPlanApprovalsDAO().find(pId);

	    getImpPlanApprovalsDAO().delete(pUser, pId);

	    ImpPlanApprovalActivityMessage lPlanApprovalActivity = new ImpPlanApprovalActivityMessage(pApproval.getPlanId(), null);
	    lPlanApprovalActivity.setAction("deleted");
	    lPlanApprovalActivity.setApprovalComment(pApproval.getComments());
	    getActivityLogDAO().save(pUser, lPlanApprovalActivity);
	    lResponse.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Unable to Mark the Acceptance Testing", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse downloadLoadApproval(User pUser, String pPlanId, String pFile) {
	// TODO: ACCESS
	JSONResponse lResponse = new JSONResponse();
	String lFileNameWithPath = getWFConfig().getAttachmentDirectory() + pPlanId + File.separator + getWFConfig().getLoadApprovalDirectory() + File.separator + pFile;
	File lLoadApproverFile = new File(lFileNameWithPath);
	if (lLoadApproverFile.exists()) {
	    try {
		lResponse.setData(FileIOUtils.getFileContent(lLoadApproverFile));
		lResponse.setMetaData(FileIOUtils.getFileContentType(lLoadApproverFile));
		lResponse.setStatus(Boolean.TRUE);
	    } catch (Exception ex) {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("Load Approver File not found");
	    }
	} else {
	    lResponse.setErrorMessage("Load Approver File not found");
	    lResponse.setStatus(Boolean.FALSE);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getSegmentMappingByImplementation(String[] ids) {
	JSONResponse lResponse = new JSONResponse();
	HashMap<String, GitSearchResult> lValues = new HashMap<>();
	List<GitSearchResult> lResultList = new ArrayList<>();
	try {
	    List<CheckoutSegments> findAll = getCheckoutSegmentsDAO().findByImplementation(ids);
	    List<CheckoutSegments> findAllUniqSegments = removeDuplicates(findAll);
	    for (CheckoutSegments lSegments : findAllUniqSegments) {
		GitSearchResult lResult = lValues.get(CheckoutUtils.getIdStringWithOutSystem(lSegments));
		if (lResult == null) {
		    lResult = new GitSearchResult();
		    lValues.put(CheckoutUtils.getIdStringWithOutSystem(lSegments), lResult);
		    if (Boolean.TRUE.equals(lSegments.getReviewStatus())) {
			lResult.addAdditionalInfo("reviewStatus", lSegments.getReviewStatus().toString());
		    }
		    lResultList.add(lResult);
		}
		BeanUtils.copyProperties(lResult, lSegments);
		GitBranchSearchResult lBranch = new GitBranchSearchResult();
		BeanUtils.copyProperties(lResult, lSegments);
		BeanUtils.copyProperties(lBranch, lSegments);

		// Generate GITSource URL
		System lSystem = lSegments.getImpId().getCheckoutSegmentsList().get(0).getPlanId().getSystemLoadList().get(0).getSystemId();
		String nickName = lSystem.getPlatformId().getNickName();
		String lTicketURL = getGitBlitClientUtils().getSegmentImplementationTicketURL(nickName, lSegments.getPlanId().getId(), lSegments.getImpId().getId(), lBranch.getTargetSystem(), lSegments.getFileName());
		String gitSourceURL = lTicketURL.replace(Constants.TICKETS, Constants.BLOB);
		lBranch.setGitSourceURL(gitSourceURL);
		lResult.addBranch(lBranch);

	    }

	} catch (Exception ex) {
	    LOG.error("Error in Listing Segemnts for Implementation", ex);
	    throw new WorkflowException("Error in Listing Checkedout Segments", ex);
	}
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(lResultList);
	return lResponse;
    }

    public List<CheckoutSegments> removeDuplicates(List<CheckoutSegments> list) {
	Set set = new TreeSet(new Comparator() {

	    @Override
	    public int compare(Object o1, Object o2) {
		if (((CheckoutSegments) o1).getProgramName().equalsIgnoreCase(((CheckoutSegments) o2).getProgramName()) && ((CheckoutSegments) o1).getTargetSystem().equalsIgnoreCase(((CheckoutSegments) o2).getTargetSystem()) && ((CheckoutSegments) o1).getFuncArea().equalsIgnoreCase(((CheckoutSegments) o2).getFuncArea()) && ((CheckoutSegments) o1).getActive().equals("Y") && ((CheckoutSegments) o2).getActive().equals("Y")) {
		    return 0;
		}
		return 1;
	    }
	});
	set.addAll(list);

	final List newList = new ArrayList(set);
	return newList;
    }

    @Transactional
    public JSONResponse setReadyForQA(User pUser, String pImpl) throws WorkflowException {
	// TODO: LEAD DEVELOPER
	JSONResponse lResponse = new JSONResponse();
	try {
	    List<String> usersToBeDeleted = new ArrayList<>();

	    Implementation lImpl = getImplementationDAO().find(pImpl);
	    ImpPlan limpPlan = getImpPlanDAO().find(lImpl.getPlanId().getId());
	    if (lImpl.getSubstatus().equals(Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED.name())) {
		List<String> lTempSegList = getCheckoutSegmentsDAO().getDependentSegments(lImpl.getId(), Constants.ImplementationStatus.READY_FOR_QA.name());
		Set<String> lImpIds = new HashSet(lTempSegList);
		if (!lImpIds.isEmpty() && limpPlan.getLoadType().equals(Constants.LoadTypes.STANDARD.name())) {
		    String lMsg = "Unable to Mark the Implementation as \"Ready for QA\", Dependent Implementation id - ";
		    String lSegList = String.join(",", lImpIds);
		    lResponse.setStatus(Boolean.FALSE);
		    lResponse.setErrorMessage(lMsg + lSegList + " are not in \"Ready for QA\" status ");
		    return lResponse;
		}

		String nickName = lImpl.getPlanId().getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
		String lRepoName = getJGitClientUtils().getPlanRepoName(nickName, lImpl.getPlanId().getId().toLowerCase());
		List<String> lBranchList = getJGitClientUtils().getAllBranchList(nickName, lImpl.getPlanId().getId().toLowerCase());
		for (int i = 0; i < lBranchList.size(); i++) {
		    if (!lBranchList.get(i).contains(lImpl.getId().toLowerCase())) {
			lBranchList.remove(i);
		    }
		}

		if (!getsSHClientUtils().addImplementationTag(lRepoName, Constants.TagStatus.READY_FOR_QA, lBranchList)) {
		    LOG.error("Ready  For QA Tagging is not completed for Implementation Id -" + lImpl.getId());
		    lResponse.setStatus(Boolean.FALSE);
		    lResponse.setErrorMessage("Unable to Set the Implementation as Ready For QA");
		    return lResponse;
		}
		// String lRepoFullName = getJGitClientUtils().getPlanRepoFullName(nickName,
		// lImpl.getPlanId().getId().toLowerCase());
		// if (!getGitBlitClientUtils().setPermissionForGitRepository(lRepoFullName,
		// lImpl.getDevId(),
		// Constants.GIT_PERMISSION_READ)) {
		// LOG.error("Error in Setting Read permission");
		// lResponse.setStatus(Boolean.FALSE);
		// lResponse.setErrorMessage("Unable to Set the Implementation as Ready For
		// QA");
		// return lResponse;
		// }
		lImpl.setImpStatus(Constants.ImplementationStatus.READY_FOR_QA.name());
		usersToBeDeleted.add(lImpl.getDevId());
		getImplementationDAO().update(pUser, lImpl);
		getGitHelper().updateImplementationPlanRepoPermissions(lImpl.getPlanId().getId().toLowerCase(), usersToBeDeleted);
		getActivityLogDAO().save(pUser, new SetReadyForQAActivityMessage(lImpl.getPlanId(), lImpl));
		lResponse.setStatus(Boolean.TRUE);
		return lResponse;
	    } else {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("Unable to Set the Implementation as Ready For QA, Integration Testing is not Completed");
		return lResponse;
	    }
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Unable to Set the Implementation as Ready For QA", ex);
	}
    }

    @Transactional
    public JSONResponse getSharedObjects(User pUser, String pSOName, String pLoadDateString, Integer pSystemId, Integer pOffset, Integer pLimit, LinkedHashMap pOrderBy) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	try {
	    Date pLoadDate = Constants.SHELL_DATEFORMAT.get().parse(pLoadDateString);
	    System lSystem = getSystemDAO().find(pSystemId);
	    if (lSystem == null) {
		LOG.error("System Not Found");
		lResponse.setErrorMessage("System Not Found");
		lResponse.setStatus(Boolean.FALSE);
		return lResponse;
	    }

	    String lCommand = Constants.SystemScripts.SEARCH_SO.getScript() + pSOName.toLowerCase() + " " + lSystem.getName().toLowerCase();
	    JSONResponse lCommandResponse = new JSONResponse();
	    lCommandResponse = getsSHClientUtils().executeCommand(lSystem, lCommand);
	    if (!lCommandResponse.getStatus()) {
		LOG.info("Unable to get Shared Object List for User - " + pUser.getDisplayName() + "Response from Script - " + lCommandResponse.getErrorMessage());
		lResponse.setErrorMessage("Unable to get Shared Object List");
		lResponse.setStatus(Boolean.FALSE);
		return lResponse;
	    }
	    List<String> lMakFileList = new ArrayList();
	    List<CheckoutSegments> lList = new ArrayList();
	    Long lCount;
	    if (lCommandResponse.getData().toString() != null && !lCommandResponse.getData().toString().trim().isEmpty() && lCommandResponse.getData().toString().trim().contains(".mak")) {
		lMakFileList = Arrays.asList(lCommandResponse.getData().toString().trim().split(","));
		lList = getCheckoutSegmentsDAO().findByFiles(new ArrayList<>(Constants.PlanStatus.getSecuredStatusMap().keySet()), lMakFileList, pLoadDate, lSystem, pOffset, pLimit, pOrderBy);
		lCount = getCheckoutSegmentsDAO().countByFiles(new ArrayList<>(Constants.PlanStatus.getSecuredStatusMap().keySet()), lMakFileList, pLoadDate, lSystem);
	    } else {
		lList = getCheckoutSegmentsDAO().findByFiles(new ArrayList<>(Constants.PlanStatus.getSecuredStatusMap().keySet()), pSOName, pLoadDate, lSystem, pOffset, pLimit, pOrderBy);
		lCount = getCheckoutSegmentsDAO().countByFiles(new ArrayList<>(Constants.PlanStatus.getSecuredStatusMap().keySet()), pSOName, pLoadDate, lSystem);
	    }
	    lResponse.setData(lList);
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setCount(lCount);
	    return lResponse;
	} catch (Exception Ex) {
	    LOG.error("Unable to get Shared object List for User -" + pUser.getDisplayName(), Ex);
	    throw new WorkflowException("Unable to get Shared Object List", Ex);
	}
    }

    @Transactional
    public JSONResponse rejectPlanAndDependentPlans(User currentUser, String planId, String rejectReason, Boolean deleteLoadSetFlag) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	try {
	    String rejectReasonText = (rejectReason.isEmpty() ? Constants.REJECT_REASON.REJECTION.getValue() : rejectReason);
	    LOG.info("Rejecting Plan " + planId + " with reason " + rejectReasonText + " loadset delete " + deleteLoadSetFlag);
	    getRejectHelper().rejectPlan(currentUser, planId, rejectReasonText, Constants.AUTOREJECT_COMMENT.REJECT.getValue(), Boolean.TRUE, Boolean.FALSE, deleteLoadSetFlag);

	} catch (Exception e) {
	    LOG.error("Unable to reject Implementation Plan and its dependent Plans", e);
	    throw new WorkflowException("Unable to reject Implementation Plan and its dependent Plans", e);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse buildPlan(User lUser, String implId, List<SystemLoad> loads, BUILD_TYPE pBuildType, Boolean systemRemoveFlag, Boolean rebuildAll) {
	JSONResponse lResponse = new JSONResponse();
	JobDetails lwssDevilBuild = new JobDetails();
	try {
	    Implementation lImpl = getImplementationDAO().find(implId);
	    cacheClient.getSocketUserMap().put(lImpl.getPlanId().getId(), lUser.getId());
	    ArrayList<String> errorSystems = new ArrayList<>();
	    // ZTPFM-1890 System Removed if load date is null
	    loads = removedSysLoadDateNull(lUser, loads, systemRemoveFlag, lImpl);
	    List<SystemLoad> lSystemLoadList = getSystemLoadDAO().findByImpPlan(lImpl.getPlanId());

	    for (SystemLoad lSystemLoad : lSystemLoadList) {
		JSONResponse lPutResponse = getPlanHelper().putLevelSegmentValidation(lSystemLoad, lImpl.getPlanId().getId());
		if (!lPutResponse.getStatus()) {
		    CommonActivityMessage lMessage = new CommonActivityMessage(lImpl.getPlanId(), null);
		    lMessage.setMessage(lPutResponse.getErrorMessage());
		    lMessage.setStatus("Fail");
		    getActivityLogDAO().save(lUser, lMessage);

		    return lPutResponse;
		}
	    }

	    for (SystemLoad load : loads) {
		if (load.getSystemPdddsMappingList() != null) {
		    // remove deleted system pddds mapping from list
		    List<SystemPdddsMapping> dbLoadLists = getSystemPdddsMappingDAO().findBySystemLoadId(load.getId());
		    List<SystemPdddsMapping> lSysPdddsList = load.getSystemPdddsMappingList();
		    Collection<SystemPdddsMapping> delSysPdddsLists = CollectionUtils.subtract(dbLoadLists, lSysPdddsList);
		    for (SystemPdddsMapping delSysPddds : delSysPdddsLists) {
			if (delSysPddds.getId() != null) {
			    getSystemPdddsMappingDAO().delete(lUser, delSysPddds);
			}
		    }

		    for (SystemPdddsMapping lSysPddds : load.getSystemPdddsMappingList()) {
			if (lSysPddds.getId() == null) {
			    getSystemPdddsMappingDAO().save(lUser, lSysPddds);
			}
		    }
		}
		if (load.getLoadDateTime() != null) {
		    continue;
		}
		errorSystems.add(load.getSystemId().getName());
		getSystemLoadDAO().update(lUser, load);
	    }
	    if (!errorSystems.isEmpty()) {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("Please fill load dates for the system(s) " + String.join(",", errorSystems));
		return lResponse;
	    }
	    if (getWFConfig().isMultipleBuildAllowed()) {
		for (SystemLoad load : loads) {
		    getJenkinsClient().validateJob(Constants.DEVL.BLD_DVL_.name() + load.getSystemId().getName());
		}
	    } else {
		for (SystemLoad load : loads) {
		    getJenkinsClient().validateJob(Constants.BUILD.BLD_ALL_.name() + load.getSystemId().getAliasName());
		}
	    }
	    for (SystemLoad load : loads) {
		JSONResponse lJobResponse = compilerControlValidationForSystem(lUser, lImpl, load.getSystemId());
		lwssDevilBuild.setMessage(lImpl.getPlanId().getId() + ": Devl build has Started for " + load.getSystemId().getName());
		wsserver.sendMessage(Constants.Channels.DEVEL_BUILD_STATUS, lUser.getId(), lImpl.getPlanId().getId(), lwssDevilBuild);
		if (!lJobResponse.getStatus()) {
		    // Send mail to tool admin that Compiler control table is not available for
		    // respective system
		    if (lJobResponse.getErrorMessage().contains("Warning")) {
			CompilerValidationMail lCompilerValidationMail = (CompilerValidationMail) mailMessageFactory.getTemplate(CompilerValidationMail.class);
			lCompilerValidationMail.setSystem(load.getSystemId().getName());
			lCompilerValidationMail.setIpAddress(load.getSystemId().getIpaddress());
			lCompilerValidationMail.addToDEVCentre();
			mailMessageFactory.push(lCompilerValidationMail);
			LOG.error("Compiler control table is not available for " + load.getSystemId().getName() + " and system " + load.getSystemId().getName());
		    } else {
			LOG.error("Compiler option mentioned by developer for system - IP Address " + load.getSystemId().getName() + " and name " + load.getSystemId().getName() + "is not available in the compiler control table. Plan - " + lImpl.getPlanId());
		    }
		}
		DevlBuildActivityMessage lMessage = new DevlBuildActivityMessage(lImpl.getPlanId(), null, load.getSystemId());
		lMessage.setStatus("initiated");
		activityLogDAO.save(lUser, lMessage);
		lJobResponse = buildPlanForSystem(lUser, lImpl.getPlanId(), lImpl, load, pBuildType, false, rebuildAll);
		if (lJobResponse.getStatus().equals(Boolean.FALSE)) {
		    lResponse.setStatus(Boolean.FALSE);
		    lResponse.setErrorMessage("Unable to Build Implementation Plan - " + lImpl.getPlanId().getId());
		    return lResponse;
		}
		if (!lAsyncPlansStartTimeMap.containsKey(lImpl.getPlanId().getId() + "-" + pBuildType.name())) {
		    lAsyncPlansStartTimeMap.put(lImpl.getPlanId().getId() + "-" + pBuildType.name(), java.lang.System.currentTimeMillis());
		}
	    }

	    ImpPlan impPlan = lImpl.getPlanId();
	    impPlan.setFullBuildDt(new Date());
	    impPlanDAO.update(lUser, impPlan);

	} catch (WorkflowException e) {
	    throw e;
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Error in Job Build", ex);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    private List<SystemLoad> removedSysLoadDateNull(User lUser, List<SystemLoad> loads, Boolean systemRemoveFlag, Implementation lImpl) {
	if (systemRemoveFlag && lUser.getCurrentRole().equals(Constants.UserGroup.Lead.name())) {
	    List<SystemLoad> lSystemLoadList = getCheckoutSegmentsDAO().getSystemLoadHavingDateNull(lImpl.getPlanId().getId());
	    List<String> lSystem = new ArrayList<>();
	    for (SystemLoad lSystemLoad : lSystemLoadList) {
		lSystem.add(lSystemLoad.getSystemId().getName());
		List<CheckoutSegments> lCheckoutSegmentsList = getCheckoutSegmentsDAO().findBySystemLoad(lSystemLoad);
		if (lCheckoutSegmentsList != null && !lCheckoutSegmentsList.isEmpty()) {
		    throw new WorkflowException("Please provide the load date for the system " + String.join(", ", lSystem) + " with having checkout segements");
		} else if (lCheckoutSegmentsList == null || lCheckoutSegmentsList.isEmpty()) {
		    getSystemLoadDAO().delete(lUser, lSystemLoad);
		}
	    }
	    loads = getSystemLoadDAO().findByImpPlan(lImpl.getPlanId().getId());
	}
	return loads;
    }

    public JSONResponse buildPlanForSystem(User pUser, ImpPlan pPlan, Implementation pImp, SystemLoad pSystemLoad, BUILD_TYPE pBuildType, Boolean byPassRegression) {
	return buildPlanForSystem(pUser, pPlan, pImp, pSystemLoad, pBuildType, byPassRegression, null);
    }

    public JSONResponse buildPlanForSystem(User pUser, ImpPlan pPlan, Implementation pImp, SystemLoad pSystemLoad, BUILD_TYPE pBuildType, Boolean byPassRegression, Boolean rebuildAll) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.FALSE);
	try {
	    if (pBuildType.equals(BUILD_TYPE.DVL_BUILD)) {
		List<Build> lBuilds = new ArrayList();
		lBuilds = getBuildDAO().findBuildWithPlanAndSystem(pPlan.getId(), Arrays.asList(pSystemLoad.getSystemId()), BUILD_TYPE.DVL_BUILD);
		lBuilds.addAll(getBuildDAO().findBuildWithPlanAndSystem(pPlan.getId(), Arrays.asList(pSystemLoad.getSystemId()), BUILD_TYPE.DVL_LOAD));
		for (Build lBuild : lBuilds) {
		    getBuildDAO().delete(pUser, lBuild);
		}
	    }

	    if (pBuildType.equals(BUILD_TYPE.STG_BUILD)) {
		List<Build> lBuilds = new ArrayList();
		lBuilds = getBuildDAO().findBuildWithPlanAndSystem(pPlan.getId(), Arrays.asList(pSystemLoad.getSystemId()), BUILD_TYPE.STG_BUILD);
		lBuilds.addAll(getBuildDAO().findBuildWithPlanAndSystem(pPlan.getId(), Arrays.asList(pSystemLoad.getSystemId()), BUILD_TYPE.STG_CWS));
		lBuilds.addAll(getBuildDAO().findBuildWithPlanAndSystem(pPlan.getId(), Arrays.asList(pSystemLoad.getSystemId()), BUILD_TYPE.STG_LOAD));
		for (Build lBuild : lBuilds) {
		    getBuildDAO().delete(pUser, lBuild);
		}
	    }

	    String param1 = pImp.getId() + "_" + pSystemLoad.getSystemId().getName() + "_" + Constants.JENKINS_DATEFORMAT.get().format(pSystemLoad.getLoadDateTime()) + "0000";

	    String param2 = pSystemLoad.getPutLevelId().getPutLevel();
	    String param7 = "prod";
	    if (pSystemLoad.getPutLevelId().getStatus().equals(Constants.PUTLevelOptions.DEVELOPMENT.name()) || pSystemLoad.getPutLevelId().getStatus().equals(Constants.PUTLevelOptions.PRE_PROD_CO_EXIST.name())) {
		param7 = "devl";
	    }

	    StringBuilder param3 = new StringBuilder(param1);
	    String[] lPlans = pPlan.getRelatedPlans().split(",");
	    for (int i = 0; i < lPlans.length; i++) {
		SystemLoad lDepLoad = getSystemLoadDAO().findBy(new ImpPlan(lPlans[i]), pSystemLoad.getSystemId());
		if (lDepLoad != null && lDepLoad.getPlanId().getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.ACTIVE.name())) {
		    param3.append(",").append(lPlans[i]).append("_");
		    param3.append(Constants.JENKINS_DATEFORMAT.get().format(lDepLoad.getLoadDateTime()));
		}
	    }

	    StringBuilder param4 = new StringBuilder(param1);
	    List<Object[]> stagingDepedendentPlans = getSystemLoadDAO().getDependentPlanByApproveDate(pPlan.getId(), pSystemLoad.getSystemId().getId(), pSystemLoad.getLoadDateTime(), Constants.PlanStatus.getAfterSubmitStatus().keySet(), pSystemLoad.getPutLevelId().getId());
	    Integer lPlanOrder = 1;
	    Date lSysDate = new Date();

	    for (Object[] lSysLoadAndPlan : stagingDepedendentPlans) {
		SystemLoad lSysLoad = (SystemLoad) lSysLoadAndPlan[0];
		ImpPlan lPlan = (ImpPlan) lSysLoadAndPlan[1];
		param4.append(",");
		param4.append(lSysLoad.getPlanId().getId().toLowerCase()).append("_");
		param4.append(Constants.JENKINS_DATEFORMAT.get().format(lSysLoad.getLoadDateTime()));
		if (lSysDate.equals(lSysLoad.getLoadDateTime())) {
		    lPlanOrder++;
		} else {
		    lPlanOrder = 1;
		}
		param4.append(new DecimalFormat("0000").format(lPlanOrder));
		lSysDate = lSysLoad.getLoadDateTime();
	    }

	    // Pddds Library
	    String param5 = "NULL";
	    String param6 = "NULL";
	    List<SystemPdddsMapping> lSysPdddsList = getSystemPdddsMappingDAO().findBySystemLoadId(pSystemLoad.getId());
	    List<Integer> lPddsIds = new ArrayList();
	    for (SystemPdddsMapping lSysPdds : lSysPdddsList) {
		lPddsIds.add(lSysPdds.getPdddsLibraryId().getId());
	    }
	    if (!lPddsIds.isEmpty()) {
		List<PdddsLibrary> lPdddsList = getPdddsLibraryDAO().findBySystemLoadId(pSystemLoad.getId());
		List<String> lLnxPddds = new ArrayList();
		List<String> lZosPddds = new ArrayList();
		for (PdddsLibrary lPddds : lPdddsList) {
		    if (lPddds.getType().equalsIgnoreCase(Constants.PdddsType.PDDDS_LIB_1.name())) {
			lLnxPddds.add(lPddds.getName());
		    } else if (lPddds.getType().equalsIgnoreCase(Constants.PdddsType.PDDDS_LIB_2.name())) {
			lZosPddds.add(lPddds.getName());
		    } else {
			LOG.warn("Unidentified Pdds Type - " + lPddds.getType() + " id - " + lPddds.getId());
		    }
		}
		param5 = !lLnxPddds.isEmpty() ? String.join(",", lLnxPddds) : "NULL";
		param6 = !lZosPddds.isEmpty() ? String.join(",", lZosPddds) : "NULL";
	    }
	    HashMap<String, String> params = new HashMap<>();
	    params.put("IMP_ID_LoadDate", param1.toLowerCase());
	    params.put("PutLevel", param2.toLowerCase());
	    params.put("PutStatus", param7.toLowerCase());
	    params.put("StagDependent", param4.toString().toLowerCase());
	    params.put("lnxPdds", param5);
	    params.put("zosPdds", param6);

	    Build lbuild = new Build();
	    lbuild.setPlanId(pPlan);
	    lbuild.setSystemId(pSystemLoad.getSystemId());
	    lbuild.setBuildType(pBuildType.name());
	    JenkinsBuild executeJob = null;
	    String changedSegments = null;

	    if (getWFConfig().isMultipleBuildAllowed()) {
		if (pBuildType.equals(Constants.BUILD_TYPE.DVL_BUILD)) {
		    params.put("DevlDependent", param3.toString().toLowerCase());
		    params.put("ChangedFiles", "ALL");
		    params.put("RebuildAll", "true");
		    if (rebuildAll != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(pUser.getCurrentRole() + " " + pUser.getDisplayName());
			if (!rebuildAll) {
			    params.put("RebuildAll", "false");
			    changedSegments = getCheckoutSegmentsDAO().getSegmentsByPlanAndSystem(pPlan.getId(), pSystemLoad.getSystemId().getName());
			    if (changedSegments != null && !changedSegments.isEmpty()) {
				params.put("ChangedFiles", changedSegments);
				sb.append(" Initiated a rebuild to the following components  " + changedSegments + " for target system " + pSystemLoad.getSystemId().getName());
			    } else {
				params.put("ChangedFiles", "NULL");
			    }
			} else {
			    sb.append(" Selected the REBUILD ALL option for the DEVL build of implementation plan " + pPlan.getId());
			}
			CommonActivityMessage lMessage = new CommonActivityMessage(pPlan, null);
			lMessage.setMessage(sb.toString());
			getActivityLogDAO().save(pUser, lMessage);
		    }
		    executeJob = getJenkinsClient().executeJob(pUser, Constants.DEVL.BLD_DVL_.name() + pSystemLoad.getSystemId().getName(), params);
		} else if (pBuildType.equals(Constants.BUILD_TYPE.STG_BUILD)) {
		    executeJob = getJenkinsClient().executeJob(pUser, Constants.STAGING.BLD_STG_.name() + pSystemLoad.getSystemId().getName(), params);
		    executeJob.setSystemLoadId("" + pSystemLoad.getId());
		}

		if (changedSegments != null && !changedSegments.isEmpty()) {
		    executeJob.getChangedFiles().addAll(Arrays.asList(changedSegments.split(",")));
		}
	    } else {
		if (pBuildType.equals(Constants.BUILD_TYPE.DVL_BUILD)) {
		    params.put("DevlDependent", param3.toString().toLowerCase());
		    params.put("BuildType", Constants.TYPE.DVL.name());
		    params.put("ChangedFiles", "ALL");
		    params.put("RebuildAll", "true");
		    if (rebuildAll != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(pUser.getCurrentRole() + " " + pUser.getDisplayName());
			if (!rebuildAll) {
			    params.put("RebuildAll", "false");
			    changedSegments = getCheckoutSegmentsDAO().getSegmentsByPlanAndSystem(pPlan.getId(), pSystemLoad.getSystemId().getName());
			    if (changedSegments != null && !changedSegments.isEmpty()) {
				params.put("ChangedFiles", changedSegments);
				sb.append(" has initiated a rebuild to the following components  " + changedSegments + " for target system " + pSystemLoad.getSystemId().getName());
			    } else {
				params.put("ChangedFiles", "NULL");
				sb.append(" has initiated a rebuild with no changes in any of the components for target system " + pSystemLoad.getSystemId().getName());
			    }
			} else {
			    sb.append(" has selected the REBUILD ALL option for the DEVL build of implementation plan " + pPlan.getId());
			}
			CommonActivityMessage lMessage = new CommonActivityMessage(pPlan, null);
			lMessage.setMessage(sb.toString());
			getActivityLogDAO().save(pUser, lMessage);
		    }
		    executeJob = getJenkinsClient().executeJob(pUser, Constants.BUILD.BLD_ALL_.name() + pSystemLoad.getSystemId().getAliasName(), params);
		} else if (pBuildType.equals(Constants.BUILD_TYPE.STG_BUILD)) {
		    params.put("BuildType", Constants.TYPE.STG.name());
		    executeJob = getJenkinsClient().executeJob(pUser, Constants.BUILD.BLD_ALL_.name() + pSystemLoad.getSystemId().getAliasName(), params);
		    executeJob.setSystemLoadId("" + pSystemLoad.getId());
		}
	    }

	    if (executeJob != null) {
		if (changedSegments != null && !changedSegments.isEmpty()) {
		    executeJob.getChangedFiles().addAll(Arrays.asList(changedSegments.split(",")));
		}
		lbuild.setBuildDateTime(executeJob.getBuildTime());
		lbuild.setBuildNumber(executeJob.getBuildNumber());
		lbuild.setJenkinsUrl(executeJob.getQueueUrl());
		lbuild.setJobStatus("P");
		lbuild.setTdxRunningStatus(Constants.TDXRunningStatus.PENDING.getTDXRunningStatus());
		getBuildDAO().save((User) pUser, lbuild);

		executeJob.setByPassRegression(byPassRegression);
		executeJob.setStartedDate(lbuild.getCreatedDt());
		executeJob.setBuildType(pBuildType.name());
		executeJob.setPlanId(pPlan.getId());

		// Due to Timing Issue, Coded below lines to job synchronisation, after save
		if (pBuildType.equals(Constants.BUILD_TYPE.DVL_BUILD)) {
		    develBuildJob.put(pPlan.getId() + "_" + pSystemLoad.getSystemId().getName(), executeJob);
		} else if (pBuildType.equals(Constants.BUILD_TYPE.STG_BUILD)) {
		    if (cacheClient.getInProgressRelatedPlanMap() != null && cacheClient.getInProgressRelatedPlanMap().containsKey(pPlan.getId())) {
			DependentPlanRejectDetail rejectDetail = cacheClient.getInProgressRelatedPlanMap().get(pPlan.getId());
			RejectionActivityMessage rejectActivityMessage = new RejectionActivityMessage(pPlan, null);
			if (rejectDetail.getAutoRejectReason() != null) {
			    rejectActivityMessage.setComments(rejectDetail.getAutoRejectReason());
			}
			getActivityLogDAO().save(rejectDetail.getUser(), rejectActivityMessage);
			getPlanHelper().revertSubmittedPlanToActive(rejectDetail.getUser(), pPlan.getId());
		    } else {
			stagingBuildJobs.add(executeJob);
		    }
		}
		getAuditCommonHelper().addApiTransaction(pUser, pBuildType.name(), lbuild.getCreatedDt());
		lResponse.setStatus(Boolean.TRUE);

	    }
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    lResponse.setStatus(Boolean.FALSE);
	}
	return lResponse;

    }

    /**
     * Retrive DevBuild Log based on file Name ZTPFM-2389 , ZTPFM-2328
     */
    public JSONResponse retriveBuildLog(User lUser, String fileName) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    String lPath = getWFConfig().getDVLBuildLogDir() + fileName;
	    File file = new File(lPath);
	    if (!file.exists()) {
		lResponse.setErrorMessage("File Not Found");
		lResponse.setStatus(Boolean.FALSE);
		return lResponse;
	    }
	    byte[] fileContent = FileIOUtils.getFileContent(file);
	    lResponse.setData(new String(fileContent));
	    lResponse.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Error in retrieving log", ex);
	}
	return lResponse;
    }

    /**
     * Retrive Stage Build Log based on file Name ZTPFM-2389 , ZTPFM-2328
     */
    public JSONResponse retriveStageBuildLog(User lUser, String fileName) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    String lPath = getWFConfig().getSTGBuildLogDir() + fileName;
	    File file = new File(lPath);
	    if (!file.exists()) {
		lResponse.setErrorMessage("File Not Found");
		lResponse.setStatus(Boolean.FALSE);
		return lResponse;
	    }
	    byte[] fileContent = FileIOUtils.getFileContent(file);
	    lResponse.setData(new String(fileContent));
	    lResponse.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Error in retrieving log", ex);
	}
	return lResponse;
    }

    /**
     * ZTPFM-2328 , ZTPFM-2329 Dev build log and stage build log created : Vinoth
     * ponnurangan Description: Getting stage build data based on system and
     * buildtype
     *
     */
    public JSONResponse retriveLatestFiveBuildLog(User lUser, String planId, String systemName, String builType) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    SortedSet<String> buildNameList = new TreeSet<>();
	    SortedSet<String> loaderNameList = new TreeSet<>();
	    SortedSet<String> buildNameFinalList = new TreeSet<>(Collections.reverseOrder());
	    SortedSet<String> loaderNameFinalList = new TreeSet<>(Collections.reverseOrder());
	    File lPath = null;
	    String command = null;
	    /**
	     * Based on Build Type we are display Path
	     */
	    if (builType.equals(Constants.BUILD_TYPE.DVL_BUILD.name()) || builType.equals(Constants.BUILD_TYPE.DVL_LOAD.name())) {
		lPath = new File(getWFConfig().getDVLBuildLogDir());
		if (builType.equals(Constants.BUILD_TYPE.DVL_BUILD.name())) {
		    command = "ls -dt " + getWFConfig().getDVLBuildLogDir() + planId + "_" + systemName + "_BLD" + "*.txt" + " | tail -n +6 ";
		} else {
		    command = "ls -dt " + getWFConfig().getDVLBuildLogDir() + planId + "_" + systemName + "_LDR" + "*.txt" + " | tail -n +6 ";
		}
	    }
	    if (builType.equals(Constants.BUILD_TYPE.STG_BUILD.name()) || builType.equals(Constants.BUILD_TYPE.STG_LOAD.name())) {
		lPath = new File(getWFConfig().getSTGBuildLogDir());
		if (builType.equals(Constants.BUILD_TYPE.STG_BUILD.name())) {
		    command = "ls -dt " + getWFConfig().getSTGBuildLogDir() + planId + "_" + systemName + "_BLD" + "*.txt" + " | tail -n +6 ";
		} else {
		    command = "ls -dt " + getWFConfig().getSTGBuildLogDir() + planId + "_" + systemName + "_LDR" + "*.txt" + " | tail -n +6 ";
		}
	    }
	    LOG.info(" file Delete commend " + command);
	    if (command != null) {
		lResponse = getsSHClientUtils().executeCommand(command);
		if (lResponse.getData().toString() != null && !lResponse.getData().toString().isEmpty()) {
		    List<String> lDeletedFileList = Arrays.asList(lResponse.getData().toString().split(java.lang.System.lineSeparator()));
		    LOG.info("lResponse Data " + lResponse.getData().toString() + "lDeleted File List " + lDeletedFileList);
		    for (String fileName : lDeletedFileList) {
			Boolean deleteFLag = new File(fileName).delete();
			if (!deleteFLag) {
			    LOG.info("Unable to delete file");
			}
		    }

		}
	    }
	    BuildLogBean logBean = new BuildLogBean();
	    if (lPath != null) {
		File[] files = lPath.listFiles((FilenameFilter) new WildcardFileFilter(planId + "_" + systemName + "*.txt"));
		logBean.setPlanId(planId);
		logBean.setSystemName(systemName);
		for (File file : files) {
		    if (file.getName().startsWith(planId + "_" + systemName + "_BLD")) {
			buildNameList.add(file.getName());
		    }
		    if (file.getName().startsWith(planId + "_" + systemName + "_LDR")) {
			loaderNameList.add(file.getName());
		    }

		}
		buildNameList.stream().limit(5).forEach(build -> {
		    buildNameFinalList.add(build);
		});

		loaderNameList.stream().limit(5).forEach(loader -> {
		    loaderNameFinalList.add(loader);
		});
		// rm `ls -dt /opt/workflow/buildlog/planid_system_bld* | tail -n +6` -rf
		LOG.info("Dev Build Name Final List" + buildNameFinalList);
		LOG.info("Dev Loader Name Final List" + loaderNameFinalList);
		logBean.setBuildFileNameList(buildNameFinalList);
		logBean.setLoaderFileNameList(loaderNameFinalList);
		lResponse.setStatus(Boolean.TRUE);
		lResponse.setData(logBean);
	    }
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Error in retrieving log", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse cancelBuild(User lUser, String planId, Constants.BUILD_TYPE buildType) throws IOException {
	JSONResponse lResponse = new JSONResponse();
	for (Map.Entry<String, JenkinsBuild> entrySet : develBuildJob.entrySet()) {
	    String key = entrySet.getKey();
	    if (key.startsWith(planId)) {
		String lplanId = key.split("_")[0];
		ImpPlan impPlan = getImpPlanDAO().find(lplanId);
		String lSystemName = key.split("_")[1];
		JenkinsBuild build = entrySet.getValue();
		System lSystem = getSystemDAO().findByName(lSystemName);
		Build lDBBuild = getBuildDAO().findByBuild(lplanId, lSystem, build.getBuildNumber(), buildType);
		ImpPlan lPlan = getImpPlanDAO().find(lplanId);
		DevlBuildActivityMessage lMessage = new DevlBuildActivityMessage(impPlan, null, lSystem);
		if (build.getBuildNumber() > 0) {
		    boolean stopBuild = getJenkinsClient().stopBuild(build.getJobName(), build.getBuildNumber());
		    if (stopBuild && build != null) {
			lDBBuild.setJobStatus("C");
			// develBuildJob.remove(key);
			getBuildDAO().update(lUser, lDBBuild);
			getAuditCommonHelper().saveApiTransaction(lUser, build.getBuildType(), build.getStartedDate(), lDBBuild.getPlanId().getId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(build.getBuildType()));
			List<CheckoutSegments> lSegemntList = getCheckoutSegmentsDAO().findPlanBySystem(lplanId, lSystemName.toUpperCase());
			if (lSegemntList != null) {
			    List<CheckoutSegments> lProgramList = lSegemntList.stream().filter(t -> t.getProgramName().contains(".sbt")).collect(Collectors.toList());
			    if (lProgramList != null && !lProgramList.isEmpty()) {
				String command = Constants.SystemScripts.SABRE_LOCK.getScript() + " " + lplanId.toLowerCase();
				lResponse = getsSHClientUtils().executeCommand(lSystem, command);
			    }
			}

		    }
		} else {
		    List<String> lBuildURLLsit = new ArrayList<String>();
		    List<Build> lDBBuildList = getBuildDAO().findAll(lPlan, buildType);
		    for (Build lBuild : lDBBuildList) {
			lBuildURLLsit.add(lBuild.getJenkinsUrl());
		    }
		    boolean stopBuild = getJenkinsClient().stopBuildInQueue(lBuildURLLsit);
		    LOG.info("Build Stop Flag: " + stopBuild + "Plan Id : " + lDBBuild.getPlanId().getId() + " Count : " + lBuildURLLsit + "| " + lDBBuildList.size() + "System Name : " + lDBBuild.getSystemId().getName());
		    if (stopBuild && build != null) {
			lDBBuild.setJobStatus("C");
			develBuildJob.remove(key);
			getBuildDAO().update(lUser, lDBBuild);
			lMessage.setStatus("cancelled");
			activityLogDAO.save(build.getUser(), lMessage);
			getAuditCommonHelper().saveApiTransaction(lUser, build.getBuildType(), build.getStartedDate(), lDBBuild.getPlanId().getId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(build.getBuildType()));
		    }
		}

	    }
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse getLatestBuildByPlan(String ids) {
	JSONResponse lResponse = new JSONResponse();
	List<SystemLoad> systemLoadList = getSystemLoadDAO().findByImpPlan(new ImpPlan(ids));
	List<System> systemList = new ArrayList<>();
	for (SystemLoad systemLoad : systemLoadList) {
	    systemList.add(systemLoad.getSystemId());
	}
	if (!systemList.isEmpty()) {
	    List<Build> lBuilds = getBuildDAO().findLastBuild(ids, systemList, BUILD_TYPE.DVL_BUILD);
	    lResponse.setData(lBuilds);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    /**
     *
     * ZTPFM-2328, ZTPFM-2329 Dev Build Details Display
     *
     */
    @Transactional
    public JSONResponse getLatestDevBuildByPlan(String ids) {
	JSONResponse lResponse = new JSONResponse();
	List<SystemLoad> systemLoadList = getSystemLoadDAO().findByImpPlan(new ImpPlan(ids));
	List<System> systemList = new ArrayList<>();
	for (SystemLoad systemLoad : systemLoadList) {
	    systemList.add(systemLoad.getSystemId());
	}
	if (!systemList.isEmpty()) {
	    List<Build> lBuilds = getBuildDAO().findLastBuildDetails(ids, systemList, BUILD_TYPE.DVL_BUILD);
	    lResponse.setData(lBuilds);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    /**
     * ZTPFM-2328 and 2329 get stage build by plan
     */
    @Transactional
    public JSONResponse getLatestStageBuildByPlan(String ids) {
	JSONResponse lResponse = new JSONResponse();
	List<SystemLoad> systemLoadList = getSystemLoadDAO().findByImpPlan(new ImpPlan(ids));
	List<System> systemList = new ArrayList<>();
	for (SystemLoad systemLoad : systemLoadList) {
	    systemList.add(systemLoad.getSystemId());
	}
	if (!systemList.isEmpty()) {
	    List<Build> lBuilds = getBuildDAO().findLastBuildDetails(ids, systemList, BUILD_TYPE.STG_BUILD);
	    lResponse.setData(lBuilds);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse createLoaderFile(User pUser, String pImpPlanId, String pLoaderType, BUILD_TYPE pBuildType) {
	JSONResponse lResponse = new JSONResponse();
	String planId = pImpPlanId.split("_")[0];
	try {

	    ImpPlan lPlan = getImpPlanDAO().find(pImpPlanId);
	    if (!getPlanHelper().addPlanActionFromCache(lPlan.getId(), Constants.PlanActions.DEVL_LOADSET_CREATION.name())) {
		throw new WorkflowException("Previous Loadset creation process for plan " + lPlan.getId() + " is in progress, please try after some time");
	    }

	    List<String> builds = getBuildDAO().getBuildInProgressPlan(Arrays.asList(lPlan.getId()), Arrays.asList(Constants.BUILD_TYPE.DVL_LOAD.name()));
	    if (builds != null && !builds.isEmpty()) {
		throw new WorkflowException("Previous Loadset creation process for plan " + lPlan.getId() + " is in progress, please try after some time");
	    }

	    ArrayList<String> errorSystems = new ArrayList<>();
	    List<SystemLoad> lSystemLoadList = getSystemLoadDAO().findByImpPlan(pImpPlanId);
	    for (SystemLoad lSystemload : lSystemLoadList) {
		JSONResponse lPutResponse = getPlanHelper().putLevelSegmentValidation(lSystemload, lPlan.getId());
		if (!lPutResponse.getStatus()) {
		    CommonActivityMessage lMessage = new CommonActivityMessage(lPlan, null);
		    lMessage.setMessage(lPutResponse.getErrorMessage());
		    lMessage.setStatus("Fail");
		    getActivityLogDAO().save(pUser, lMessage);
		    return lPutResponse;
		}

		if (lSystemload.getLoadDateTime() != null) {
		    continue;
		}
		errorSystems.add(lSystemload.getSystemId().getName());
	    }
	    if (!errorSystems.isEmpty()) {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("Please fill load dates for the system(s) " + String.join(",", errorSystems));
		return lResponse;
	    }
	    if (getWFConfig().isMultipleBuildAllowed()) {
		for (SystemLoad lSystemload : lSystemLoadList) {
		    if (pBuildType.equals(Constants.BUILD_TYPE.DVL_LOAD)) {
			getJenkinsClient().validateJob(Constants.DEVL.LDR_DVL_.name() + lSystemload.getSystemId().getName());
		    } else if (pBuildType.equals(Constants.BUILD_TYPE.STG_LOAD)) {
			getJenkinsClient().validateJob(Constants.STAGING.LDR_STG_.name() + lSystemload.getSystemId().getName());
		    }
		}
	    } else {
		for (SystemLoad lSystemload : lSystemLoadList) {
		    if (pBuildType.equals(Constants.BUILD_TYPE.DVL_LOAD) || pBuildType.equals(Constants.BUILD_TYPE.STG_LOAD)) {
			getJenkinsClient().validateJob(Constants.BUILD.LDR_ALL_.name() + lSystemload.getSystemId().getAliasName());
		    }
		}
	    }

	    String lImpId = "";
	    for (Implementation lImp : lPlan.getImplementationList()) {
		lImpId = lImp.getId();
		break;
	    }
	    for (SystemLoad lSystemLoad : lSystemLoadList) {
		JSONResponse lJobResponse = createLoaderFileForSystem(pUser, lSystemLoad.getPlanId(), lPlan.getImplementationList().get(0), lSystemLoad, pBuildType, pLoaderType, false);
		if (lJobResponse.getStatus().equals(Boolean.FALSE)) {
		    lResponse.setStatus(Boolean.FALSE);
		    lResponse.setErrorMessage("Unable to Create Loader(OLDR/TDLR) file");
		    return lResponse;
		}
	    }
	    if (!lAsyncPlansStartTimeMap.containsKey(lPlan.getId() + "-" + pBuildType.name()) && pBuildType.equals(Constants.BUILD_TYPE.DVL_LOAD)) {
		lAsyncPlansStartTimeMap.put(lPlan.getId() + "-" + pBuildType.name(), java.lang.System.currentTimeMillis());
	    }
	} catch (WorkflowException e) {
	    throw e;
	} catch (Exception ex) {
	    getPlanHelper().clearPlanActionFromCache(planId, Constants.PlanActions.DEVL_LOADSET_CREATION.name());
	    LOG.error("Error in Creating the Loader File for Plan id - " + pImpPlanId, ex);
	    throw new WorkflowException("Unable to Create Loader(OLDR/TDLR) file", ex);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    public JSONResponse createLoaderFileForSystem(User pUser, ImpPlan pPlan, Implementation pImp, SystemLoad pSystemLoad, BUILD_TYPE pWorkspaceType, String pLoaderType, Boolean byPassRegression) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    String lPlanSystemInfo = pImp.getId() + "_" + pSystemLoad.getSystemId().getName() + "_" + Constants.JENKINS_DATEFORMAT.get().format(pSystemLoad.getLoadDateTime());

	    HashMap<String, String> params = new HashMap<>();
	    params.put("IMP_ID_LoadDate", lPlanSystemInfo.toLowerCase());
	    params.put("Loadset_Name", LoadSetUtils.getLoadSetName(pPlan, pSystemLoad));
	    params.put("Load_Type", pLoaderType.toUpperCase());

	    JenkinsBuild executeJob = null;
	    if (pWorkspaceType.equals(Constants.BUILD_TYPE.DVL_LOAD)) {
		List<Build> lBuilds = getBuildDAO().findBuildWithPlanAndSystem(pPlan.getId(), Arrays.asList(pSystemLoad.getSystemId()), Constants.BUILD_TYPE.DVL_LOAD);
		for (Build lBuild : lBuilds) {
		    getBuildDAO().delete(pUser, lBuild);
		}
		if (getWFConfig().isMultipleBuildAllowed()) {
		    executeJob = getJenkinsClient().executeJob(pUser, Constants.DEVL.LDR_DVL_.name() + pSystemLoad.getSystemId().getName(), params);
		} else {
		    params.put("BuildType", Constants.TYPE.DVL.name());
		    executeJob = getJenkinsClient().executeJob(pUser, Constants.BUILD.LDR_ALL_.name() + pSystemLoad.getSystemId().getAliasName(), params);
		}
		DevlLoadActivityMessage lMessage = new DevlLoadActivityMessage(pPlan, null, pSystemLoad.getSystemId());
		lMessage.setStatus("initiated");
		lMessage.setOldrType(pLoaderType);
		activityLogDAO.save(pUser, lMessage);

	    } else if (pWorkspaceType.equals(Constants.BUILD_TYPE.STG_LOAD)) {
		Set<GitProdCommitMessage> lFallback_list = new TreeSet<>();
		List<SystemLoad> lSysLoads = getSystemLoadDAO().getFallbackLoadSetPlanIds(pPlan.getId(), Constants.PlanStatus.getApprovedAndAboveStatus().keySet(), pSystemLoad.getSystemId().getId());
		for (SystemLoad lSysLoad : lSysLoads) {
		    LOG.info("Collecting secured fallback plans for " + pPlan.getId() + ": " + lSysLoad.getPlanId().getId() + " / " + lSysLoad.getLoadDateTime());
		    lFallback_list.add(new GitProdCommitMessage(lSysLoad.getPlanId().getId(), lSysLoad.getLoadDateTime()));
		    List<String> lProgramList = getImpPlanDAO().getSecuredSegmentList(pPlan.getId(), lSysLoad.getPlanId().getId());
		    LOG.info("Collecting secured fallback plans and segments for " + pPlan.getId() + ": " + lSysLoad.getPlanId().getId() + " / " + String.join(",", lProgramList));
		}

		// Get Result from Git Repository
		List<CheckoutSegments> lSegmentList = getCheckoutSegmentsDAO().findBySystemLoad(pSystemLoad);
		Set<String> lPlanList = new HashSet<String>();
		for (CheckoutSegments lSegment : lSegmentList) {
		    String lSearchRepoName = getGitHelper().getRepositoryNameBySourceURL(lSegment.getSourceUrl());
		    Collection<GitSearchResult> lGITFileList = getjGITSearchUtils().SearchAllRepos(pSystemLoad.getSystemId().getPlatformId().getNickName(), lSegment.getProgramName(), pPlan.getMacroHeader(), Constants.PRODSearchType.BOTH, Arrays.asList(lSearchRepoName));
		    for (GitSearchResult lResult : lGITFileList) {
			for (GitBranchSearchResult lBranch : lResult.getBranch()) {
			    if (lResult.getFileName().equals(lSegment.getFileName()) && lBranch.getTargetSystem().contains(pSystemLoad.getSystemId().getName().toLowerCase()) && (lBranch.getRefStatus().equalsIgnoreCase("online") || lBranch.getRefStatus().equalsIgnoreCase("pending"))) {
				LOG.info("RefStatus in Branch - " + lBranch.getRefStatus());
				LOG.info("Collecting Online fallback plans for   " + lSegment.getPlanId().getId() + " / " + lSegment.getSystemLoad().getLoadDateTime());
				GitProdCommitMessage lGitProdCommitMessage = new GitProdCommitMessage();
				lGitProdCommitMessage.setDate(lBranch.getRefLoadDate());
				lGitProdCommitMessage.setPlanID(lBranch.getRefPlan());
				lFallback_list.add(lGitProdCommitMessage);
			    }
			}
		    }
		}
		StringBuilder sb = new StringBuilder();
		for (GitProdCommitMessage lFallBack : lFallback_list) {
		    lPlanList.add(lFallBack.getPlanID());
		    sb.append("," + lFallBack.getPlanID() + "_" + Constants.JENKINS_DATEFORMAT.get().format(lFallBack.getDate()));
		}

		String lFallback_parm = "";
		if (!sb.toString().isEmpty()) {
		    lFallback_parm = sb.toString().substring(1).toLowerCase();
		}
		params.put("Fallback_List", lFallback_parm);
		if (cacheClient.getInProgressRelatedPlanMap() != null && cacheClient.getInProgressRelatedPlanMap().containsKey(pPlan.getId())) {
		    DependentPlanRejectDetail rejectDetail = cacheClient.getInProgressRelatedPlanMap().get(pPlan.getId());
		    RejectionActivityMessage rejectActivityMessage = new RejectionActivityMessage(pPlan, null);
		    if (rejectDetail.getAutoRejectReason() != null) {
			rejectActivityMessage.setComments(rejectDetail.getAutoRejectReason());
		    }
		    getActivityLogDAO().save(rejectDetail.getUser(), rejectActivityMessage);
		    getPlanHelper().revertSubmittedPlanToActive(rejectDetail.getUser(), pPlan.getId());
		    lResponse.setStatus(Boolean.FALSE);
		    return lResponse;
		} else {
		    if (getWFConfig().isMultipleBuildAllowed()) {
			executeJob = getJenkinsClient().executeJob(pUser, Constants.STAGING.LDR_STG_.name() + pSystemLoad.getSystemId().getName(), params);
		    } else {
			params.put("BuildType", Constants.TYPE.STG.name());
			executeJob = getJenkinsClient().executeJob(pUser, Constants.BUILD.LDR_ALL_.name() + pSystemLoad.getSystemId().getAliasName(), params);
		    }
		    // ZTPFM-2008 Fallback loadset plan list
		    StgLoadFallBackActivityMessage lFallMessage = new StgLoadFallBackActivityMessage(pPlan, null, pSystemLoad.getSystemId());
		    lFallMessage.setlPlanList(lPlanList);
		    activityLogDAO.save(pUser, lFallMessage);

		    StgLoadActivityMessage lMessage = new StgLoadActivityMessage(pPlan, null, pSystemLoad.getSystemId());
		    lMessage.setStatus("initiated");
		    lMessage.setOldrType(pLoaderType);
		    activityLogDAO.save(pUser, lMessage);
		}

	    }

	    if (executeJob != null) {
		executeJob.setSystemLoadId("" + pSystemLoad.getId());
		// DB Save to Build table
		Build lbuild = new Build();
		lbuild.setPlanId(pSystemLoad.getPlanId());
		lbuild.setSystemId(pSystemLoad.getSystemId());
		lbuild.setBuildDateTime(executeJob.getBuildTime());
		lbuild.setBuildType(pWorkspaceType.name());
		lbuild.setLoadSetType(pLoaderType);
		lbuild.setBuildNumber(executeJob.getBuildNumber());
		lbuild.setJenkinsUrl(executeJob.getQueueUrl());
		lbuild.setJobStatus("P");
		getBuildDAO().save(pUser, lbuild);
		executeJob.setByPassRegression(byPassRegression);
		executeJob.setBuildType(pWorkspaceType.name());
		executeJob.setStartedDate(lbuild.getCreatedDt());
		executeJob.setUser(pUser);
		executeJob.setPlanId(pSystemLoad.getPlanId().getId());

		getAuditCommonHelper().addApiTransaction(pUser, pWorkspaceType.name(), lbuild.getCreatedDt());
		if (pWorkspaceType.equals(Constants.BUILD_TYPE.DVL_LOAD)) {
		    develLoaderJob.add(executeJob);
		} else if (pWorkspaceType.equals(Constants.BUILD_TYPE.STG_LOAD)) {
		    if (cacheClient.getInProgressRelatedPlanMap() != null && cacheClient.getInProgressRelatedPlanMap().containsKey(pPlan.getId())) {
			DependentPlanRejectDetail rejectDetail = cacheClient.getInProgressRelatedPlanMap().get(pPlan.getId());
			RejectionActivityMessage rejectActivityMessage = new RejectionActivityMessage(pPlan, null);
			if (rejectDetail.getAutoRejectReason() != null) {
			    rejectActivityMessage.setComments(rejectDetail.getAutoRejectReason());
			}
			getActivityLogDAO().save(rejectDetail.getUser(), rejectActivityMessage);
			getPlanHelper().revertSubmittedPlanToActive(rejectDetail.getUser(), pPlan.getId());
			lResponse.setStatus(Boolean.FALSE);
			return lResponse;
		    } else {
			stagingLoaderJobs.add(executeJob);
		    }

		}
	    } else {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("Unable to Create Loader(OLDR/TDLR) file");
		LOG.error("Unable to Create Loader File for Implemenation Plan - " + pPlan.getId() + ", Jenkins Job Fails for System - " + pSystemLoad.getSystemId().getName());
		return lResponse;
	    }
	} catch (Exception ex) {
	    lResponse.setStatus(Boolean.FALSE);
	    LOG.error("Unable to Create Loader File for Implemenation Plan - " + pPlan.getId() + ", Jenkins Job Fails for System - " + pSystemLoad.getSystemId().getName(), ex);
	    return lResponse;
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse getSystemLoadListBySystemId(Integer pId, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	// TODO: Need to move to TSD
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setMetaData(new Date());
	lResponse.setData(getSystemLoadDAO().findBySystem(pId, Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name(), pOffset, pLimit));
	lResponse.setCount(getSystemLoadDAO().getCountBySystem(pId, Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name()));
	return lResponse;
    }

    @Transactional
    public JSONResponse getCpuListBySystemId(Integer pId) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	if (getTOSConfig().getTosSystemType().equals(Constants.TOSEnvironment.PRODUCTION.name())) {
	    lResponse.setData(getSystemCpuDAO().findBySystem(pId, Constants.TOSEnvironment.PRODUCTION.name()));
	} else {
	    lResponse.setData(getSystemCpuDAO().findBySystem(pId, Constants.TOSEnvironment.NATIVE_CPU.name()));
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getTOSServerListBySystemId(Integer pId, String pType) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	HashMap<Integer, List> lResult = new HashMap<>();
	if (pId == null) {
	    List<System> lAllSystems = getSystemDAO().findAll();
	    for (System lSystem : lAllSystems) {
		List<SystemCpu> lAllCpus = getSystemCpuDAO().findBySystem(lSystem.getId(), pType);
		lResult.put(lSystem.getId(), lAllCpus);
	    }
	} else {
	    List<SystemCpu> lAllCpus = getSystemCpuDAO().findBySystem(pId, pType);
	    lResult.put(pId, lAllCpus);
	}
	lResponse.setData(lResult);
	return lResponse;
    }

    @Transactional
    public JSONResponse postActivationAction(User pUser, List<SystemLoadActions> pLoads, Boolean isDeactivate, Boolean isAuxType, Boolean skipPassed) {
	String ipAddress = "";

	JSONResponse lResponse = new JSONResponse();
	pLoads.sort(new LoadActivationCompare(isDeactivate));
	try {
	    for (SystemLoadActions lLoad : pLoads) {

		if ((lLoad.getVparId().getId() == null) || (!lLoad.getVparId().getTssDeploy())) {
		    SystemLoadActions lOldLoad = null;
		    // Check the TD boxes whether the Maintenace is in progress before proceeding
		    // the Load,Activate,Deactivate and Delete Action
		    System system = getSystemDAO().find(lLoad.getSystemId().getId());
		    // ZTPFM-1497 Code changes done to get dsl flag from system load action table
		    // ZTPFM-2230 Code changes to avoid dsl file maintenance
		    // if (lLoad.getDslUpdate().equals("Y")) {
		    //
		    // String command = Constants.SystemScripts.TPF_MAINTENANCE_CHECK.getScript();
		    // lResponse = getsSHClientUtils().executeCommand(system, command);
		    //
		    // if (!lResponse.getStatus()) {
		    // lResponse.setStatus(Boolean.FALSE);
		    // lResponse.setErrorMessage("Please try again as nightly maintenance is in
		    // progress");
		    // LOG.error("Nightly Maintenance is in progress " + system.getName());
		    // return lResponse;
		    // }
		    // }
		    ImpPlan lPlan = lLoad.getPlanId();
		    if (lLoad.getId() != null) {
			lOldLoad = getSystemLoadActionsDAO().find(lLoad.getId());
			if (lOldLoad != null && lOldLoad.getStatus().equals(lLoad.getStatus())) {
			    continue;
			}
		    } else if (lLoad.getStatus() == null || lLoad.getStatus().isEmpty()) {
			lLoad.setStatus(LOAD_SET_STATUS.DELETED.name());
		    }

		    if (isDeactivate && LOAD_SET_STATUS.getLoadScenarios().contains(lLoad.getStatus())) {
			continue;
		    } else if ((!isDeactivate) && LOAD_SET_STATUS.getDeleteScenarios().contains(lLoad.getStatus())) {
			continue;
		    }

		    LOG.info("Loadset " + (isDeactivate ? "Deactivation" : "Activation") + " initiated for the Plan   " + lLoad.getPlanId().getId() + " System " + lLoad.getSystemId().getName());

		    BUILD_TYPE lBuildType = null;
		    User lUser = pUser.getCurrentOrDelagateUser();
		    if (lUser.getCurrentRole().equals(Constants.UserGroup.Lead.name())) {
			if (Constants.PlanStatus.getBeforeApprovedStatus().containsKey(lPlan.getPlanStatus())) {
			    lBuildType = BUILD_TYPE.DVL_LOAD;
			} else {
			    lBuildType = BUILD_TYPE.STG_LOAD;
			}
		    } else if (lUser.getCurrentRole().equals(Constants.UserGroup.QA.name())) {
			lBuildType = BUILD_TYPE.STG_LOAD;
		    } else if (lUser.getCurrentRole().equals(Constants.UserGroup.QADeployLead.name())) {
			lBuildType = BUILD_TYPE.STG_LOAD;
		    } else if (lUser.getCurrentRole().equals(Constants.UserGroup.SystemSupport.name())) {
			lBuildType = BUILD_TYPE.STG_LOAD;
		    }

		    if (lBuildType == null) {
			lResponse.setStatus(Boolean.FALSE);
			lResponse.setErrorMessage("Load Process is restricted");
			LOG.error("Load process is restricted for User  " + pUser.getDisplayName() + " Role " + pUser.getCurrentRole());
			return lResponse;
		    }

		    if (lUser.getCurrentRole().equals(Constants.UserGroup.Lead.name()) && lLoad.getVparId().getId() == null && !lLoad.getVparId().getName().matches("\\d*\\.\\d*\\.\\d*\\.\\d*")) {
			lLoad.getVparId().setSystemId(system);
			lLoad.getVparId().setType(Constants.VPARSEnvironment.PRIVATE.name());
			lLoad.getVparId().setOwnerId(lUser.getId());
			lLoad.getVparId().setTssDeploy(Boolean.FALSE);
			lLoad.getVparId().setDefault_cpu("N");
			lLoad.getVparId().setName(lLoad.getVparId().getName().toUpperCase());
			getVparsDAO().save(pUser, lLoad.getVparId());
		    } else if (!lUser.getCurrentRole().equals(Constants.UserGroup.Lead.name()) && lLoad.getVparId().getId() == null) {
			lResponse.setStatus(Boolean.FALSE);
			lResponse.setErrorMessage("Private System not allowed for the role " + lUser.getCurrentRole());
			LOG.error("Private System not allowed for the role " + lUser.getCurrentRole());
			return lResponse;
		    }

		    Build lBuild = getBuildDAO().findLastSuccessfulBuild(lLoad.getPlanId().getId(), lLoad.getSystemId().getId(), lBuildType);

		    if (lBuild == null) {
			lResponse.setStatus(Boolean.FALSE);
			lResponse.setErrorMessage("Please do successful build before proceeding");
			LOG.error("Plan " + lLoad.getPlanId().getId() + "dont have successful Build");
			return lResponse;
		    }

		    LOG.info("Getting Build type " + lBuildType.name() + " Number : " + lBuild.getId());

		    LOAD_SET_STATUS lStatus = LOAD_SET_STATUS.valueOf(lLoad.getStatus());

		    boolean ftpFlag = true;
		    boolean dbcrStatus = Boolean.TRUE;

		    if (!isDeactivate) {
			// DBCR Load complete check
			if (lBuildType.name().equalsIgnoreCase(BUILD_TYPE.STG_LOAD.name())) {
			    Constants.DBCR_ENVIRONMENT env = null;
			    if (lLoad.getVparId().getType() != null) {
				if (lLoad.getVparId().getType().equalsIgnoreCase(Constants.VPARSEnvironment.QA_FUCTIONAL.name())) {
				    env = Constants.DBCR_ENVIRONMENT.TEST;
				} else if (lLoad.getVparId().getType().equalsIgnoreCase(Constants.VPARSEnvironment.QA_REGRESSION.name())) {
				    env = Constants.DBCR_ENVIRONMENT.TEST;
				} else if (lLoad.getVparId().getType().equalsIgnoreCase(Constants.VPARSEnvironment.PRE_PROD.name())) {
				    env = Constants.DBCR_ENVIRONMENT.COPY;
				}
				// Need to change to call TSDService::prodDBCRValidate
				if (env != null) {
				    List<Dbcr> dbcrList = getDbcrDAO().findByPlanSystemEnvironment(lLoad.getPlanId().getId(), lLoad.getSystemId().getId(), env.name());
				    if (!dbcrList.isEmpty()) {
					Dbcr dbcrError = null;
					for (Dbcr dbcr : dbcrList) {
					    if (dbcr.getMandatory().equalsIgnoreCase("Y")) {
						if (!getDbcrHelper().isDbcrComplete(dbcr)) {
						    LOG.error("DBCR not deployed");
						    dbcrStatus = Boolean.FALSE;
						    dbcrError = dbcr;
						    break;
						}
					    }
					}
					if (!dbcrStatus && dbcrError != null) {
					    DbcrValidationMessage dbcrMessage = new DbcrValidationMessage(lBuild.getPlanId(), null);
					    dbcrMessage.setDbcrName(dbcrError.getDbcrName());
					    dbcrMessage.setSystemName(dbcrError.getSystemId().getName());
					    dbcrMessage.setEnvironment(env.getValue());
					    setStatus(dbcrStatus, lLoad, lOldLoad);
					    getActivityLogDAO().save(pUser, dbcrMessage);
					}
				    }
				}
			    }
			}
		    }
		    if (dbcrStatus) {
			if (lStatus.equals(LOAD_SET_STATUS.LOADED)) {
			    // FTP
			    JobDetails lwssMessageDeployStatus = new JobDetails();
			    String vparName = lLoad.getVparId().getName();

			    if (vparName != null && !vparName.matches("\\d*\\.\\d*\\.\\d*\\.\\d*")) {

				List<YodaResult> list = getTestSystemLoadDAO().getVparsIP(vparName);

				if (list != null && list.size() > 0 && list.get(0).getRc() == 0) {
				    ipAddress = list.get(0).getIp();
				    LOG.info("YODA Received IP Address: " + ipAddress);
				    ipAddress = ipAddress.replaceAll("^0+", "").replaceAll("\\.0+", "\\.");
				    lwssMessageDeployStatus.setStatus(lPlan.getId() + ": Received IP Address for " + vparName + " from YODA API");
				    wsserver.sendMessage(Constants.Channels.E_AUX_DEPLOY_STATUS, pUser, lwssMessageDeployStatus);
				} else if (list.size() > 0) {
				    LOG.error("YODA Execution RC: " + list.get(0).getRc() + " Message : " + list.get(0).getLogMessage());
				    lwssMessageDeployStatus.setStatus(lPlan.getId() + " YODA API Error. RC: " + list.get(0).getRc() + " Message : " + list.get(0).getErrorMessage());
				    wsserver.sendMessage(Constants.Channels.E_AUX_DEPLOY_STATUS, pUser, lwssMessageDeployStatus);
				}

				if (list != null && list.size() > 0) {
				    YodaResponseActivityMessage responseActivityMessage = new YodaResponseActivityMessage(lPlan, null);
				    responseActivityMessage.setlYodaResult(list.get(0));
				    responseActivityMessage.setSystemName(lLoad.getSystemId().getName());
				    responseActivityMessage.setVparName(vparName);
				    responseActivityMessage.setYodaActivity(Constants.YodaActivtiyMessage.IPADDRESS);
				    getActivityLogDAO().save(pUser, responseActivityMessage);
				}
			    } else {
				ipAddress = vparName;

			    }
			    if (ipAddress != null && !ipAddress.isEmpty()) {

				lwssMessageDeployStatus.setStatus(lPlan.getId() + ": FTP of " + lLoad.getSystemLoadId().getLoadSetName() + " to " + vparName + " has started");
				wsserver.sendMessage(Constants.Channels.E_AUX_DEPLOY_STATUS, pUser, lwssMessageDeployStatus);

				ProdFTPActivityMessage lMessage = new ProdFTPActivityMessage(lBuild.getPlanId(), null, lLoad.getSystemLoadId());
				lMessage.setIpAddress(ipAddress);
				lMessage.setVparsName(vparName);
				lMessage.setFallback(false);
				getActivityLogDAO().save(pUser, lMessage);
				JSONResponse lSSHResponse = getFTPHelper().doFTP(pUser, lLoad.getSystemLoadId(), lBuild, ipAddress, false);
				if (!lSSHResponse.getStatus()) {
				    LOG.info(lPlan.getId() + ": FTP of " + lLoad.getSystemLoadId().getLoadSetName() + " to " + vparName + " has failed");

				    lwssMessageDeployStatus.setStatus(lPlan.getId() + ": FTP of " + lLoad.getSystemLoadId().getLoadSetName() + " to " + vparName + " has failed");
				    wsserver.sendMessage(Constants.Channels.E_AUX_DEPLOY_STATUS, pUser, lwssMessageDeployStatus);

				    YodaResult lYodaResult = new YodaResult();
				    lYodaResult.setRc(8);
				    lYodaResult.setMessage(lSSHResponse.getErrorMessage());
				    YodaResponseActivityMessage responseActivityMessage = new YodaResponseActivityMessage(lPlan, null);
				    responseActivityMessage.setlYodaResult(lYodaResult);
				    responseActivityMessage.setYodaActivity(Constants.YodaActivtiyMessage.FTP);
				    getActivityLogDAO().save(pUser, responseActivityMessage);
				    lLoad.setStatus(LOAD_SET_STATUS.DELETED.name());
				    lLoad.setIsAutoDeploy(Boolean.FALSE);
				    lLoad.setLastActionStatus("FAILED");
				    ftpFlag = false;
				} else {
				    LOG.info("FTP completed successfully for the plan id " + lBuild.getPlanId().getId());

				    lwssMessageDeployStatus.setStatus(lPlan.getId() + ": FTP of " + lLoad.getSystemLoadId().getLoadSetName() + " to " + vparName + " has completed");
				    wsserver.sendMessage(Constants.Channels.E_AUX_DEPLOY_STATUS, pUser, lwssMessageDeployStatus);

				    lMessage.setStatus("Success");
				    getActivityLogDAO().save(pUser, lMessage);
				    getFTPHelper().getYodaLoadSetPath(pUser, lPlan, lSSHResponse);
				    if (isAuxType) {
					lLoad.setStatus(LOAD_SET_STATUS.ACTIVATED.name());
					lLoad.setLastActionStatus("SUCCESS");
					lLoad.setIsAutoDeploy(Boolean.FALSE);
				    }
				}
			    } else {
				ftpFlag = false;
				LOG.error("Unable to get IP Adress for VPAR " + vparName);

				lwssMessageDeployStatus.setStatus(lPlan.getId() + ": Unable to receive IP Address for" + vparName + " from YODA API");
				wsserver.sendMessage(Constants.Channels.E_AUX_DEPLOY_STATUS, pUser, lwssMessageDeployStatus);

				lLoad.setStatus(LOAD_SET_STATUS.DELETED.name());
				lLoad.setLastActionStatus("FAILED");
				lLoad.setIsAutoDeploy(Boolean.FALSE);
			    }
			}
			PreProdActionsActivityLog preProdMessage = new PreProdActionsActivityLog(lLoad.getPlanId(), null);
			if (ftpFlag && !isAuxType) {
			    switch (lStatus) {
			    case LOADED: {
				Set<String> deptPlans = new HashSet<>();
				if (lUser.getCurrentRole().equals(Constants.UserGroup.SystemSupport.name())) {
				    deptPlans = checkDeptPlanCurrentStatus(lLoad, "Activate");
				}
				if (deptPlans.size() > 0) {
				    StringBuffer sb = new StringBuffer();
				    sb.append(lLoad.getPlanId().getId()).append("/").append(lLoad.getSystemId().getName());
				    sb.append(" cannot be loaded to Pre-Prod as dependent Plan(s) ");
				    sb.append(StringUtils.join(deptPlans, ", "));
				    sb.append(" are not yet loaded in Pre-Prod. Please contact leads of dependent plans to get them loaded in Pre-Prod ");

				    preProdMessage.setStatus(false);
				    preProdMessage.setMessage(sb.toString());
				    getActivityLogDAO().save(lUser, preProdMessage);

				    lResponse.setStatus(Boolean.FALSE);
				    lResponse.setErrorMessage(sb.toString());

				    return lResponse;
				} else {
				    LOG.info("Calling IBM Test System Load Activate : " + lLoad.getSystemLoadId().getLoadSetName());
				    List<YodaResult> lList = getTestSystemLoadDAO().loadAndActivate(pUser, lLoad.getVparId().getName(), lLoad.getSystemLoadId().getLoadSetName());
				    boolean lResult = false;
				    if (lList != null && lList.size() > 0) {
					YodaResponseActivityMessage responseActivityMessage = new YodaResponseActivityMessage(lPlan, null);
					responseActivityMessage.setlYodaResult(lList.get(0));
					responseActivityMessage.setYodaActivity(Constants.YodaActivtiyMessage.LOADANDACTIVATE);
					getActivityLogDAO().save(pUser, responseActivityMessage);
					lResult = lList.get(0).getRc() == 0;
				    }
				    setStatus(lResult, lLoad, lOldLoad);
				    // Send Mail Notification to ADL and Developer
				    if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.LOADED.name()) && lUser.getCurrentRole().equals(Constants.UserGroup.SystemSupport.name())) {
					sendMailNotificationADLAndDev(lLoad);
					lLoad.getSystemLoadId().setPreProdLoadStatus(Constants.PROD_LOAD_STATUS.PRE_PROD_ACTIVATED.name());
				    }
				    if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.LOADED.name()) && lUser.getCurrentRole().equals(Constants.UserGroup.QA.name()) || lUser.getCurrentRole().equals(Constants.UserGroup.QADeployLead.name())) {
					sendMailNotificationADLandQATester(lLoad, pUser);
				    }
				    if (lResult) {
					if (lLoad.getIsAutoDeploy() == null || lLoad.getIsAutoDeploy().equals(Boolean.TRUE)) {
					    lLoad.setIsAutoDeploy(Boolean.FALSE);
					}
					if (lLoad.getIsAutoDeploy()) {
					    lLoad.setDslUpdate("Y");
					}
					lLoad.setStatus(LOAD_SET_STATUS.ACTIVATED.name());
					lLoad.setDeActivatedDateTime(null);
					lLoad.setActivatedDateTime(new Date());
					LOG.info("System Load Action id - " + lLoad.getId());
					// ZTPFM-1497 Code changes done to get dsl flag from system load action table
					// if (lLoad.getDslUpdate().equals("Y")) {
					// dSLFileHelper.updatePlanInfoInDSLFile(pUser, lLoad);
					// }

				    }
				    break;
				}

			    }
			    case ACTIVATED: {
				Set<String> deptPlans = new HashSet<>();
				if (lUser.getCurrentRole().equals(Constants.UserGroup.SystemSupport.name())) {
				    deptPlans = checkDeptPlanCurrentStatus(lLoad, "Activate");
				}
				if (deptPlans.size() > 0) {
				    StringBuffer sb = new StringBuffer();
				    sb.append(lLoad.getPlanId().getId()).append("/").append(lLoad.getSystemId().getName());
				    sb.append(" cannot be Activated to Pre-Prod as dependent Plan(s) ");
				    sb.append(StringUtils.join(deptPlans, ", "));
				    sb.append(" are not yet Activated in Pre-Prod. Please contact leads of dependent plans to get them Activated in Pre-Prod ");

				    preProdMessage.setStatus(false);
				    preProdMessage.setMessage(sb.toString());
				    getActivityLogDAO().save(lUser, preProdMessage);

				    lResponse.setStatus(Boolean.FALSE);
				    lResponse.setErrorMessage(sb.toString());

				    return lResponse;
				} else {
				    LOG.info("Calling IBM Test System Activate : " + lLoad.getSystemLoadId().getLoadSetName());
				    List<YodaResult> lList = getTestSystemLoadDAO().activate(pUser, lLoad.getVparId().getName(), lLoad.getSystemLoadId().getLoadSetName());
				    boolean lResult = false;
				    if (lList != null && lList.size() > 0) {
					YodaResponseActivityMessage responseActivityMessage = new YodaResponseActivityMessage(lPlan, null);
					responseActivityMessage.setlYodaResult(lList.get(0));
					responseActivityMessage.setYodaActivity(Constants.YodaActivtiyMessage.ACTIVATE);
					getActivityLogDAO().save(pUser, responseActivityMessage);
					lResult = lList.get(0).getRc() == 0;
				    }
				    setStatus(lResult, lLoad, lOldLoad);
				    if (lResult) {
					lLoad.setDeActivatedDateTime(null);
					lLoad.setActivatedDateTime(new Date());
				    }
				    if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name()) && lUser.getCurrentRole().equals(Constants.UserGroup.SystemSupport.name())) {
					sendMailNotificationADLAndDev(lLoad);
					lLoad.getSystemLoadId().setPreProdLoadStatus(Constants.PROD_LOAD_STATUS.PRE_PROD_ACTIVATED.name());
				    }
				    if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name()) && lUser.getCurrentRole().equals(Constants.UserGroup.QA.name()) || lUser.getCurrentRole().equals(Constants.UserGroup.QADeployLead.name())) {
					sendMailNotificationADLandQATester(lLoad, pUser);
				    }
				    break;
				}

			    }
			    case DEACTIVATED: {
				Set<String> deptPlans = new HashSet<>();
				if (lUser.getCurrentRole().equals(Constants.UserGroup.SystemSupport.name())) {
				    deptPlans = checkDeptPlanCurrentStatus(lLoad, "Deactivate");
				}
				if (deptPlans.size() > 0) {
				    StringBuffer sb = new StringBuffer();
				    sb.append(lLoad.getPlanId().getId()).append("/").append(lLoad.getSystemId().getName());
				    sb.append(" cannot be Deactivated to Pre-Prod as dependent Plan(s) ");
				    sb.append(StringUtils.join(deptPlans, ", "));
				    sb.append(" are not yet Deactivated in Pre-Prod. Please contact leads of dependent plans to get them Deactivated in Pre-Prod ");

				    preProdMessage.setStatus(false);
				    preProdMessage.setMessage(sb.toString());
				    getActivityLogDAO().save(lUser, preProdMessage);

				    lResponse.setStatus(Boolean.FALSE);
				    lResponse.setErrorMessage(sb.toString());

				    return lResponse;
				} else {
				    LOG.info("Calling IBM Test System Deactivate : " + lLoad.getSystemLoadId().getLoadSetName());
				    List<YodaResult> lList = getTestSystemLoadDAO().deActivate(pUser, lLoad.getVparId().getName(), lLoad.getSystemLoadId().getLoadSetName());
				    boolean lResult = false;
				    if (lList != null && lList.size() > 0) {
					YodaResponseActivityMessage responseActivityMessage = new YodaResponseActivityMessage(lPlan, null);
					responseActivityMessage.setlYodaResult(lList.get(0));
					responseActivityMessage.setYodaActivity(Constants.YodaActivtiyMessage.DEACTIVATE);
					getActivityLogDAO().save(pUser, responseActivityMessage);
					lResult = lList.get(0).getRc() == 0;
				    }
				    // Enable Load and Activate when deactivate fails
				    setStatus(lResult, lLoad, null);
				    if (lResult) {
					lLoad.setDeActivatedDateTime(new Date());
				    }
				    if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.DEACTIVATED.name()) && lUser.getCurrentRole().equals(Constants.UserGroup.SystemSupport.name())) {
					sendMailNotificationADLAndDev(lLoad);
					lLoad.getSystemLoadId().setPreProdLoadStatus(Constants.PROD_LOAD_STATUS.PRE_PROD_DEACTIVATED.name());
				    }
				    if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.DEACTIVATED.name()) && lUser.getCurrentRole().equals(Constants.UserGroup.QA.name()) || lUser.getCurrentRole().equals(Constants.UserGroup.QADeployLead.name())) {
					sendMailNotificationADLandQATester(lLoad, pUser);
				    }
				    break;
				}
			    }
			    case DELETED: {
				Set<String> deptPlans = new HashSet<>();
				if (lUser.getCurrentRole().equals(Constants.UserGroup.SystemSupport.name())) {
				    deptPlans = checkDeptPlanCurrentStatus(lLoad, "Deleted");
				}
				if (deptPlans.size() > 0) {
				    StringBuffer sb = new StringBuffer();
				    sb.append(lLoad.getPlanId().getId()).append("/").append(lLoad.getSystemId().getName());
				    sb.append(" cannot be deleted to Pre-Prod as dependent Plan(s) ");
				    sb.append(StringUtils.join(deptPlans, ", "));
				    sb.append(" are not yet deleted in Pre-Prod. Please contact leads of dependent plans to get them deleted in Pre-Prod ");

				    preProdMessage.setStatus(false);
				    preProdMessage.setMessage(sb.toString());
				    getActivityLogDAO().save(lUser, preProdMessage);

				    lResponse.setStatus(Boolean.FALSE);
				    lResponse.setErrorMessage(sb.toString());

				    return lResponse;
				} else {
				    LOG.info("Calling IBM Test System Delete DeActivate : " + lLoad.getSystemLoadId().getLoadSetName());
				    List<YodaResult> lList = getTestSystemLoadDAO().deleteAndDeActivate(pUser, lLoad.getVparId().getName(), lLoad.getSystemLoadId().getLoadSetName());
				    boolean lResult = false;
				    if (lList != null && lList.size() > 0) {
					YodaResponseActivityMessage responseActivityMessage = new YodaResponseActivityMessage(lPlan, null);
					responseActivityMessage.setlYodaResult(lList.get(0));
					responseActivityMessage.setYodaActivity(Constants.YodaActivtiyMessage.DEACTIVATEANDDELETE);
					getActivityLogDAO().save(pUser, responseActivityMessage);
					lResult = lList.get(0).getRc() == 0;
				    }
				    setStatus(lResult, lLoad, null);
				    if (lResult) {
					lLoad.setDeActivatedDateTime(new Date());
					// ZTPFM-1497 Code changes done to get dsl flag from system load action table
					// if (lLoad.getDslUpdate().equals("Y")) {
					// dSLFileHelper.deletePlanInfoInDSLFile(pUser, lLoad);
					// // lLoad.setDslUpdate("N");
					// }
				    }
				    if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.DELETED.name()) && lUser.getCurrentRole().equals(Constants.UserGroup.SystemSupport.name())) {
					sendMailNotificationADLAndDev(lLoad);
					lLoad.getSystemLoadId().setPreProdLoadStatus(Constants.PROD_LOAD_STATUS.PRE_PROD_DELETED.name());
				    }
				    if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.DELETED.name()) && lUser.getCurrentRole().equals(Constants.UserGroup.QA.name()) || lUser.getCurrentRole().equals(Constants.UserGroup.QADeployLead.name())) {
					sendMailNotificationADLandQATester(lLoad, pUser);
				    }
				    break;
				}
			    }
			    }
			}
			getSystemLoadDAO().update(pUser, lLoad.getSystemLoadId());
		    }
		    boolean isFunctional = false;
		    if (!lLoad.getVparId().getName().matches("\\d*\\.\\d*\\.\\d*\\.\\d*")) {
			if (lLoad.getId() != null) {
			    getSystemLoadActionsDAO().update(pUser, lLoad);
			} else {
			    lLoad.setIsVparActivated(Boolean.FALSE);
			    getSystemLoadActionsDAO().save(pUser, lLoad);
			}
			isFunctional = lLoad.getVparId().getType().equalsIgnoreCase(Constants.VPARSEnvironment.QA_FUCTIONAL.name());
		    }
		    if (!skipPassed) {
			if (!isAuxType && lUser.getCurrentRole().equals(Constants.UserGroup.SystemSupport.name())) {
			    // DELTA PRE PROD
			    getPlanHelper().updatePlanAsDeployedInPreProd(pUser, lLoad.getPlanId().getId(), VPARSEnvironment.PRE_PROD, true);
			} else if (lUser.getCurrentRole().equals(Constants.UserGroup.QA.name()) && isFunctional) {
			    getPlanHelper().updatePlanAsDeployedInPreProd(pUser, lLoad.getPlanId().getId(), VPARSEnvironment.QA_FUCTIONAL, true);
			} else if (lUser.getCurrentRole().equals(Constants.UserGroup.QADeployLead.name()) && isFunctional) {
			    getPlanHelper().updatePlanAsDeployedInPreProd(pUser, lLoad.getPlanId().getId(), VPARSEnvironment.QA_FUCTIONAL, true);
			} else if (lUser.getCurrentRole().equals(Constants.UserGroup.QA.name()) && !isFunctional) {
			    getPlanHelper().updatePlanAsDeployedInPreProd(pUser, lLoad.getPlanId().getId(), VPARSEnvironment.QA_REGRESSION, true);
			}
		    }
		    if (!isAuxType) {
			if (lLoad.getStatus() != null && !lLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DELETED.name()) && !lLoad.getLastActionStatus().equals("FAILED")) {
			    getActivityLogDAO().save(pUser, new LoadSetActivityMessage(lPlan, null, lLoad));
			}
		    }
		    LOG.info("Loading/Activation completed for the System " + lLoad.getSystemId().getName());
		} else {
		    User lUser = pUser.getCurrentOrDelagateUser();
		    LOAD_SET_STATUS lStatus = LOAD_SET_STATUS.valueOf(lLoad.getStatus());
		    switch (lStatus) {
		    case LOADED: {
			if (lLoad.getId() != null && lLoad.getVparId().getTssDeploy()) {
			    getSystemLoadActionsDAO().update(pUser, lLoad);
			} else {
			    lLoad.setIsAutoDeploy(Boolean.FALSE);
			    lLoad.setStatus(LOAD_SET_STATUS.DELETED.name());
			    if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.LOADED.name()) && lUser.getCurrentRole().equals(Constants.UserGroup.QA.name()) || lUser.getCurrentRole().equals(Constants.UserGroup.QADeployLead.name())) {
				sendMailNotificationADLandQATester(lLoad, pUser);
			    }
			    getSystemLoadActionsDAO().save(pUser, lLoad);
			    TssResponseActivityMessage tssResponseActivityMessage = new TssResponseActivityMessage(lLoad.getPlanId(), null);
			    tssResponseActivityMessage.setLoadStatus(lLoad.getStatus());
			    tssResponseActivityMessage.setSystemName(lLoad.getSystemId().getName());
			    tssResponseActivityMessage.setVparName(lLoad.getVparId().getName());
			    tssResponseActivityMessage.setEnv(lLoad.getVparId().getType().contains(Constants.VPARSEnvironment.QA_REGRESSION.name()) ? "Regression" : "Functional");
			    getActivityLogDAO().save(pUser, tssResponseActivityMessage);
			    SystemLoadActions lSystemLoadAction = getSystemLoadActionsDAO().find(lLoad.getId());

			    PreProductionLoads lpreProductionLoads = getPreProductionLoadsDAO().findByPlanIdByLoad(lLoad.getPlanId(), lLoad.getSystemId());
			    if (lpreProductionLoads != null) {
				lpreProductionLoads.setSystemLoadActionsId(lLoad);
				lpreProductionLoads.setSystemLoadId(lLoad.getSystemLoadId());
				lpreProductionLoads.setStatus(LOAD_SET_STATUS.DELETED.name());
				getPreProductionLoadsDAO().update(pUser, lpreProductionLoads);
			    } else {
				PreProductionLoads lPreProd = saveAndUpdateProLoad(lSystemLoadAction, pUser);
				getPreProductionLoadsDAO().save(pUser, lPreProd);
			    }
			}
			getPlanHelper().updatePlanAsDeployedInPreProd(pUser, lLoad.getPlanId().getId(), VPARSEnvironment.valueOf(lLoad.getVparId().getType()), true);

			break;
		    }
		    }
		}
	    }
	} catch (Exception Ex) {
	    LOG.error("Error Occurs in Load/Activate/Delete/Deactivation of Loadset process ", Ex);
	    throw new WorkflowException("Unable to process your request, please try after some time", Ex);
	}
	lResponse.setData(pLoads);
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    private PreProductionLoads saveAndUpdateProLoad(SystemLoadActions lLoad, User pUser) {
	System id = lLoad.getSystemId();
	SystemCpu cpu = getSystemCpuDAO().findBySystemAndFlag(id);
	PreProductionLoads lPreProd = new PreProductionLoads();
	lPreProd.setPlanId(lLoad.getPlanId());
	lPreProd.setStatus(LOAD_SET_STATUS.DELETED.name());
	lPreProd.setSystemId(lLoad.getSystemId());
	lPreProd.setSystemLoadId(lLoad.getSystemLoadId());
	lPreProd.setCreatedBy(pUser.getDisplayName());
	lPreProd.setSystemLoadActionsId(lLoad);
	lPreProd.setCpuId(cpu);
	return lPreProd;
    }

    @Transactional
    public JSONResponse deleteActivationAction(User pUser, Integer pLoad) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    SystemLoadActions lSysLoadActions = getSystemLoadActionsDAO().find(pLoad);
	    if (lSysLoadActions != null) {
		List<PreProductionLoads> lPreLoads = getPreProductionLoadsDAO().findBySystemLoadId(lSysLoadActions.getSystemLoadId());
		if (lPreLoads != null) {
		    for (PreProductionLoads lPreLoad : lPreLoads) {
			if (lPreLoad.getSystemLoadActionsId() != null && lPreLoad.getSystemLoadActionsId().getId().equals(pLoad)) {
			    getPreProductionLoadsDAO().delete(pUser, lPreLoad);
			}
		    }
		}
	    }

	    boolean isFunctional = lSysLoadActions.getVparId().getType().equalsIgnoreCase(Constants.VPARSEnvironment.QA_FUCTIONAL.name());

	    List<Build> lBuildList = buildDAO.findByImpPlan(lSysLoadActions.getPlanId());
	    boolean auxFlag = false;
	    for (Build lbuild : lBuildList) {
		if (lbuild.getLoadSetType() != null && lbuild.getLoadSetType().equals(Constants.LoaderTypes.A.name())) {
		    auxFlag = true;
		}
	    }
	    getSystemLoadActionsDAO().delete(pUser, pLoad);
	    if (!auxFlag && lSysLoadActions != null && pUser.getCurrentRole().equals(Constants.UserGroup.SystemSupport.name())) {
		getPlanHelper().updatePlanAsDeployedInPreProd(pUser, lSysLoadActions.getPlanId().getId(), VPARSEnvironment.PRE_PROD, true);
	    } else if (pUser.getCurrentRole().equals(Constants.UserGroup.QA.name()) && isFunctional) {
		getPlanHelper().updatePlanAsDeployedInPreProd(pUser, lSysLoadActions.getPlanId().getId(), VPARSEnvironment.QA_FUCTIONAL, true);
	    } else if (pUser.getCurrentRole().equals(Constants.UserGroup.QADeployLead.name()) && isFunctional) {
		getPlanHelper().updatePlanAsDeployedInPreProd(pUser, lSysLoadActions.getPlanId().getId(), VPARSEnvironment.QA_FUCTIONAL, true);
	    } else if (pUser.getCurrentRole().equals(Constants.UserGroup.QA.name()) && !isFunctional) {
		getPlanHelper().updatePlanAsDeployedInPreProd(pUser, lSysLoadActions.getPlanId().getId(), VPARSEnvironment.QA_REGRESSION, true);
	    }
	} catch (Exception ex) {
	    LOG.info("Unable to remove test system ", ex);
	    throw new WorkflowException("Unable to remove test system ", ex);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    private void setStatus(boolean lResult, SystemLoadActions lLoad, SystemLoadActions lOldLoad) {
	if (!lResult) {
	    lLoad.setLastActionStatus("FAILED");
	    if (lOldLoad != null) {
		lLoad.setStatus(lOldLoad.getStatus());
	    } else {
		lLoad.setStatus(LOAD_SET_STATUS.DELETED.name());
	    }
	} else {
	    lLoad.setLastActionStatus("SUCCESS");
	}
    }

    @Transactional
    public JSONResponse updatePlan(User pUser, ImpPlan pPlan, boolean warningFlag, boolean allowDateChange, String loadTypeChangeComment) {
	JSONResponse lResponse = new JSONResponse();
	boolean loadDateTypeChanged = Boolean.FALSE;
	String planStatus = null;
	cacheClient.getPlanUpdateStatusMap().put(pPlan.getId(), pUser);
	cacheClient.getSocketUserMap().put(pPlan.getId(), pUser.getId());
	try {
	    boolean deleteBuild = false;
	    ImpPlan impPlan = getImpPlanDAO().find(pPlan.getId());
	    List<SystemLoad> dbLoadLists = getSystemLoadDAO().findByImpPlan(pPlan);
	    String lDBLeadId = impPlan.getLeadId();

	    // ZTPFM-2271 If in progress status is REJECT. Then plan update shoundn't be
	    // allowed.
	    List<PreProductionLoads> lPreProdList = getPreProductionLoadsDAO().findByPlanId(pPlan);
	    if ((lPreProdList != null && !lPreProdList.isEmpty()) && (impPlan.getInprogressStatus() != null || pPlan.getInprogressStatus() != null) && (impPlan.getInprogressStatus().equals(Constants.PlanInProgressStatus.REJECT.name()) || pPlan.getInprogressStatus().equals(Constants.PlanInProgressStatus.REJECT.name()))) {
		throw new WorkflowException("Rejection is In-progress. Please try again after sometime.");
	    }

	    // 1980 - R Category Validation
	    JSONResponse lTResponse = getPlanHelper().planRCategoryValidation(impPlan.getId());
	    if (!lTResponse.getStatus()) {
		throw new WorkflowException(lTResponse.getErrorMessage());
	    }

	    // ZTPFM-1271 If Put level changes
	    for (SystemLoad pSystemLoad : pPlan.getSystemLoadList()) {
		String pCurrentPutLevel = pSystemLoad.getPutLevelId().getPutLevel();
		List<CheckoutSegments> lCheckoutSegements = getCheckoutSegmentsDAO().findPlanBySystem(impPlan.getId(), pSystemLoad.getSystemId().getName());

		lCheckoutSegements.stream().filter(seg -> Constants.FILE_TYPE.IBM.name().equalsIgnoreCase(seg.getFileType()) && !seg.getSystemLoad().getPutLevelId().getPutLevel().equals(pCurrentPutLevel)).forEach(seg -> {
		    throw new WorkflowException("Put Level change for the Plan " + impPlan.getId() + " is not allowed, Since it is having IBM Segments in the system " + pSystemLoad.getSystemId().getName() + ".");
		});

		for (SystemLoad sys : dbLoadLists) {
		    if (sys.getSystemId().getId().equals(pSystemLoad.getSystemId().getId()) && !sys.getPutLevelId().getPutLevel().equals(pCurrentPutLevel)) {
			deleteBuild = true;
		    }
		}
	    }
	    if (deleteBuild) {
		for (Build pBuild : impPlan.getBuildList()) {
		    getBuildDAO().delete(pUser, pBuild);
		}
	    }

	    if (Constants.PlanStatus.getSecuredBeforeReadyForProduction().containsKey(pPlan.getPlanStatus())) {
		planStatus = impPlan.getPlanStatus();
		if (impPlan.getLoadType().equalsIgnoreCase(Constants.LoadTypes.STANDARD.name()) && pPlan.getLoadType().equalsIgnoreCase(Constants.LoadTypes.EXCEPTION.name())) {
		    loadDateTypeChanged = Boolean.TRUE;
		} else if (pPlan.getLoadType().equalsIgnoreCase(Constants.LoadTypes.STANDARD.name()) && impPlan.getLoadType().equalsIgnoreCase(Constants.LoadTypes.EXCEPTION.name())) {
		    throw new WorkflowException("Plan has to be rejected to change the load type to Standard!");
		}
	    }
	    LOG.info("Exception case - " + loadDateTypeChanged);

	    // Check for Macro Header Type
	    if (!pPlan.getMacroHeader().equals(impPlan.getMacroHeader()) && pPlan.getMacroHeader()) {
		Integer lNONMacroHeaderCount = getCheckoutSegmentsDAO().isMacroHeaderPlan(pPlan.getId());
		if (lNONMacroHeaderCount != 0) {
		    throw new WorkflowException("Plan - " + pPlan.getId() + " has segments which are other than Macro/Header/Include(h,hpp,mac,cpy,inc,incafs) file types");
		}
	    }
	    // remove system loads which deleted
	    List<SystemLoad> lLoadLists = pPlan.getSystemLoadList();
	    Collection<SystemLoad> delLoadLists = CollectionUtils.subtract(dbLoadLists, lLoadLists);

	    // For Delta Plans If system load deleted, then delete RFC Details if available
	    List<RFCDetails> rfcDetails = new ArrayList<>();
	    List<Integer> rfcBasedSystemIds = new ArrayList<>();
	    List<RFCDetails> rfcListToDelete = new ArrayList<>();

	    LOG.info("Delta Flag: " + pPlan.getId().startsWith("D"));
	    rfcDetails = getRFCDetailsDAO().findByImpPlan(pPlan.getId());
	    if (pPlan.getId().startsWith("D") && rfcDetails != null) {

		if (delLoadLists != null && !delLoadLists.isEmpty()) {
		    LOG.info("Delete Load list size: " + delLoadLists.size());
		    // Add all the deleted system load ids
		    rfcBasedSystemIds.addAll(delLoadLists.stream().map(sys -> sys.getId()).collect(Collectors.toList()));
		    rfcListToDelete.addAll(rfcDetails.stream().filter(rfc -> rfcBasedSystemIds.contains(rfc.getSystemLoadId().getId())).collect(Collectors.toList()));
		} else if (impPlan.getRfcFlag() && !pPlan.getRfcFlag()) {
		    LOG.info("Imp Plan delete Load list size: " + delLoadLists.size());
		    // If RFc flag removed in update, but any system has RFC #, then add those
		    // System load id
		    rfcListToDelete.addAll(rfcDetails);
		    rfcBasedSystemIds.addAll(rfcDetails.stream().filter(rfc -> rfc.getRfcNumber() != null && !rfc.getRfcNumber().isEmpty()).map(rfc -> rfc.getSystemLoadId().getId()).collect(Collectors.toList()));
		}
		LOG.info("Delete System Ids: " + rfcBasedSystemIds.stream().map(x -> x.toString()).collect(Collectors.joining(",")));
		// Imp Plan updated by deleting system or removed RFC flag, then check RFC
		// number updated w.r.t System and throw error if applicable

		if (!pUser.getCurrentRole().equals(Constants.UserGroup.DLCoreChangeTeam.name()) && ((impPlan.getRfcFlag() && !pPlan.getRfcFlag()) || (delLoadLists != null && !delLoadLists.isEmpty())) && rfcDetails.stream().filter(rfc -> rfc.getRfcNumber() != null && !rfc.getRfcNumber().isEmpty() && rfcBasedSystemIds.contains(rfc.getSystemLoadId().getId())).findAny().isPresent()) {
		    if (impPlan.getRfcFlag() && !pPlan.getRfcFlag()) {
			throw new WorkflowException("RFC Flag cannot be removed. Please contact DL Core change team to update plan.");
		    } else {
			throw new WorkflowException("System Can't be deleted. Please contact DL Core change team to update plan.");
		    }

		} else {
		    rfcListToDelete.forEach(rfc -> {
			if (rfc.getRfcNumber() != null && !rfc.getRfcNumber().isEmpty()) {
			    RfcCalendarInviteMail rfcCalendarInviteMail = (RfcCalendarInviteMail) getMailMessageFactory().getTemplate(RfcCalendarInviteMail.class);
			    rfcCalendarInviteMail.setCalendarFileName(getWFConfig().getAttachmentDirectory() + "invites/" + rfc.getPlanId().getId() + "_" + rfc.getSystemLoadId().getSystemId().getName() + "_RFC.ics");
			    rfcCalendarInviteMail.setRfcNumber(rfc.getRfcNumber());
			    rfcCalendarInviteMail.setTargetSystem(rfc.getSystemLoadId().getSystemId().getName());
			    rfcCalendarInviteMail.setLoadDateTime(rfc.getSystemLoadId().getLoadDateTime());
			    rfcCalendarInviteMail.setImpPlan(rfc.getPlanId());
			    rfcCalendarInviteMail.addToDlCoreChangeTeamMail();
			    getMailMessageFactory().push(rfcCalendarInviteMail);
			}
			// getRFCDetailsDAO().delete(pUser, rfc); RFC details should not removed once
			// Delta person fill the RFC details.
		    });
		}
	    }

	    for (SystemLoad delLoad : delLoadLists) {
		getSystemLoadDAO().delete(pUser, delLoad);
		List<Build> lBuilds = getBuildDAO().findBuildWithPlanAndSystem(delLoad.getPlanId().getId(), Arrays.asList(delLoad.getSystemId()));
		if (lBuilds != null) {
		    lBuilds.stream().forEach(t -> getBuildDAO().delete(pUser, t));
		}
	    }

	    for (SystemLoad lLoadList : lLoadLists) {
		if (lLoadList.getPutLevelId() == null) {
		    throw new WorkflowException(lLoadList.getSystemId().getName() + " doesn't have Putlevel");
		}
	    }

	    // ZTPFM-2415 - Check for DevManager change
	    if (!impPlan.getDevManager().equals(pPlan.getDevManager())) {
		// Activity Log
		DevManagerChangeActivityMessage devManagerChangeActivityMessage = new DevManagerChangeActivityMessage(pPlan, null);
		devManagerChangeActivityMessage.setNewDevManager(pPlan.getDevManagerName());
		devManagerChangeActivityMessage.setOldDevManager(impPlan.getDevManagerName());
		getActivityLogDAO().save(pUser, devManagerChangeActivityMessage);
		// Mail
		DevManagerAssignmentMail devManagerAssignmentMail = (DevManagerAssignmentMail) getMailMessageFactory().getTemplate(DevManagerAssignmentMail.class);
		devManagerAssignmentMail.setDevManagerChange(true);
		devManagerAssignmentMail.setNewDevManager(pPlan.getDevManagerName());
		devManagerAssignmentMail.setOldDevManager(impPlan.getDevManagerName());
		devManagerAssignmentMail.setPlanId(pPlan.getId());
		devManagerAssignmentMail.setLeadName(impPlan.getLeadName());
		devManagerAssignmentMail.addToAddressUserId(pPlan.getDevManager(), Constants.MailSenderRole.DEV_MANAGER);
		devManagerAssignmentMail.addToAddressUserId(impPlan.getDevManager(), Constants.MailSenderRole.DEV_MANAGER);
		devManagerAssignmentMail.addToAddressUserId(impPlan.getLeadId(), Constants.MailSenderRole.LEAD);
		getMailMessageFactory().push(devManagerAssignmentMail);
	    }

	    for (SystemLoad lLoadList : lLoadLists) {
		if (lLoadList.getLoadCategoryId() == null) {
		    throw new WorkflowException("select loadCategory for this system " + lLoadList.getSystemId().getName());
		}
	    }

	    // getting QA Functional tester along with instruction From SystemLoad
	    Set<String> qaOldFunTesters = new HashSet<>();
	    Set<String> qaNewFunTesters = new HashSet<>();

	    for (SystemLoad lSystemLoad : dbLoadLists) {
		if (lSystemLoad.getQaFunctionalTesters() != null) {
		    List<String> qaFunctionalTesterList = Arrays.asList(lSystemLoad.getQaFunctionalTesters().split(","));
		    for (String qaFunctionalTester : qaFunctionalTesterList) {
			qaOldFunTesters.add(qaFunctionalTester);
		    }
		}
	    }

	    for (SystemLoad lSystemLoad : pPlan.getSystemLoadList()) {
		if (lSystemLoad.getQaFunctionalTesters() != null) {
		    List<String> qaNewFunctionalTesterList = Arrays.asList(lSystemLoad.getQaFunctionalTesters().split(","));
		    List<String> qaFunctionalNamesIds = new ArrayList<>();
		    for (String qaNewFunctionalTester : qaNewFunctionalTesterList) {
			qaNewFunTesters.add(qaNewFunctionalTester);
			User qaUser = getLDAPAuthenticatorImpl().getUserDetails(qaNewFunctionalTester);
			if (qaUser != null && qaUser.getDisplayName() != null) {
			    qaFunctionalNamesIds.add(qaUser.getDisplayName());
			}
		    }
		    lSystemLoad.setQaFunctionalTesterName(String.join(",", qaFunctionalNamesIds));
		}
	    }

	    for (SystemLoad commonLoad : dbLoadLists) {
		for (SystemLoad updatedLoad : lLoadLists) {
		    if (commonLoad.getId().equals(updatedLoad.getId())) {
			if (commonLoad.getLoadDateTime() == null) {
			    commonLoad.setLoadDateTime(updatedLoad.getLoadDateTime());
			    getSystemLoadDAO().update(pUser, commonLoad);
			}
		    }
		}
	    }

	    dbLoadLists = getSystemLoadDAO().findByImpPlan(pPlan);
	    // TODO: Check the dependent Plan Load Dates
	    List<String> invalidRelatedPlans = new ArrayList<>();
	    if (pPlan.getRelatedPlans() != null && !pPlan.getRelatedPlans().isEmpty()) {
		List<String> lDependentPlanLists = new ArrayList<>();
		lDependentPlanLists = Arrays.asList(pPlan.getRelatedPlans().split(","));
		for (SystemLoad lLoadList : lLoadLists) {
		    invalidRelatedPlans.addAll(getImpPlanDAO().getInvalidRelatedPlans(lDependentPlanLists, lLoadList.getSystemId(), lLoadList.getLoadDateTime()));
		}
	    }
	    if (!invalidRelatedPlans.isEmpty()) {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("Load date should be later than " + invalidRelatedPlans);
		return lResponse;
	    }

	    // If the load Date Changed
	    // get developer name of the relatedplans, common files of the implementation
	    // plan, with program names
	    // TODO: send mail notification for the developers
	    SortedSet<String> dependentPlanIds = new TreeSet();
	    SortedSet<String> allCascadeDependentPlanIds = new TreeSet();
	    SortedSet<String> prodDeplyomentStartedPlans = new TreeSet();
	    boolean loadDateChangedToLater = false;
	    boolean loadDateTimeChanged = false;
	    boolean loadDateTimeChangedToEalier = false;
	    if (dbLoadLists != null) {
		OUTER: for (SystemLoad commonLoad : dbLoadLists) {
		    for (SystemLoad updatedLoad : lLoadLists) {
			if (commonLoad.getId().equals(updatedLoad.getId())) {
			    // Need to review
			    if (commonLoad.getLoadDateTime() == null) {
				// loadDateChangedToLater = true;
				// break OUTER;
			    } else if (commonLoad.getLoadDateTime() != null && !(Constants.APP_DATE_TIME_FORMAT.get().format(commonLoad.getLoadDateTime()).equals(Constants.APP_DATE_TIME_FORMAT.get().format(updatedLoad.getLoadDateTime())))) {
				loadDateTimeChanged = true;
				updatedLoad.setLoadDateMailFlag(Boolean.FALSE);
				pPlan.setLoadDateMailFlag(Boolean.FALSE);
				pPlan.setLoadDateMacroMailFlag(Boolean.FALSE);
				LOG.info("Load Date Time update occurs");
				if (pPlan.getLoadType().equalsIgnoreCase(Constants.LoadTypes.EXCEPTION.name()) || pPlan.getLoadType().equalsIgnoreCase(Constants.LoadTypes.EMERGENCY.name())) {
				    if (commonLoad.getLoadDateTime().before(updatedLoad.getLoadDateTime())) {
					loadDateChangedToLater = true;
					updatedLoad.setLoadDateMailFlag(Boolean.FALSE);
					pPlan.setLoadDateMailFlag(Boolean.FALSE);
				    } else if (commonLoad.getLoadDateTime().after(updatedLoad.getLoadDateTime())) {
					loadDateTimeChangedToEalier = true;
				    }
				    break OUTER;
				}
			    }
			}
		    }
		}
	    }

	    // ZTPFM-2174 Code changes to avoid update Load date time if RFC Number assigned
	    // in Delta
	    if (loadDateTimeChanged && pPlan.getId().startsWith("D") && rfcDetails != null) {

		// If RFC Flag removed
		if (!pUser.getCurrentRole().equals(Constants.UserGroup.DLCoreChangeTeam.name())) {
		    for (SystemLoad commonLoad : dbLoadLists) {
			for (SystemLoad updatedLoad : lLoadLists) {
			    if (commonLoad.getId().equals(updatedLoad.getId()) && commonLoad.getLoadDateTime() != null && !(Constants.APP_DATE_TIME_FORMAT.get().format(commonLoad.getLoadDateTime()).equals(Constants.APP_DATE_TIME_FORMAT.get().format(updatedLoad.getLoadDateTime())))) {
				if (rfcDetails.stream().filter(rfc -> rfc.getSystemLoadId().getId().equals(commonLoad.getId()) && rfc.getRfcNumber() != null && !rfc.getRfcNumber().isEmpty()).findAny().isPresent()) {
				    throw new WorkflowException("RFC number already assigned.  Send email to IT, TPF Changes to request new Load date/time.");
				}
			    }
			}
		    }
		} else if (pUser.getCurrentRole().equals(Constants.UserGroup.DLCoreChangeTeam.name())) {
		    // To update RFC number as null if load date time got changed w.r.t System
		    for (SystemLoad commonLoad : dbLoadLists) {
			for (SystemLoad updatedLoad : lLoadLists) {
			    if (commonLoad.getId().equals(updatedLoad.getId()) && commonLoad.getLoadDateTime() != null && !(Constants.APP_DATE_TIME_FORMAT.get().format(commonLoad.getLoadDateTime()).equals(Constants.APP_DATE_TIME_FORMAT.get().format(updatedLoad.getLoadDateTime())))) {
				LOG.info("Update RFC Number as null..");
				rfcDetails.stream().filter(rfc -> rfc.getSystemLoadId().getId().equals(commonLoad.getId()) && rfc.getRfcNumber() != null && !rfc.getRfcNumber().isEmpty()).forEach(rfc -> {
				    LOG.info("Update RFC NUmber for id: " + rfc.getId());
				    String rfcNumber = rfc.getRfcNumber();
				    rfc.setRfcNumber(null);
				    getRFCDetailsDAO().update(pUser, rfc);
				    RfcCalendarInviteMail rfcCalendarInviteMail = (RfcCalendarInviteMail) getMailMessageFactory().getTemplate(RfcCalendarInviteMail.class);
				    rfcCalendarInviteMail.setCalendarFileName(getWFConfig().getAttachmentDirectory() + "invites/" + rfc.getPlanId().getId() + "_" + rfc.getSystemLoadId().getSystemId().getName() + "_RFC.ics");
				    rfcCalendarInviteMail.setRfcNumber(rfcNumber);
				    rfcCalendarInviteMail.setTargetSystem(rfc.getSystemLoadId().getSystemId().getName());
				    rfcCalendarInviteMail.setLoadDateTime(rfc.getSystemLoadId().getLoadDateTime());
				    rfcCalendarInviteMail.setImpPlan(rfc.getPlanId());
				    rfcCalendarInviteMail.addToDlCoreChangeTeamMail();
				    getMailMessageFactory().push(rfcCalendarInviteMail);
				});
			    }
			}
		    }
		}
	    }

	    if (loadDateTimeChanged) {
		// do date audit for update, if false return error
		StringBuilder lValidate = new StringBuilder("");
		if (pPlan.getLoadType().equalsIgnoreCase(Constants.LoadTypes.STANDARD.name())) {
		    for (SystemLoad lLoadList : lLoadLists) {
			String lPlanAuditResult = getImpPlanDAO().doPlanAuditForUpdate(pPlan.getId(), lLoadList.getSystemId().getId(), lLoadList.getLoadDateTime());
			if (lPlanAuditResult != null) {
			    lValidate.append(lPlanAuditResult);
			}
		    }

		    // ZTPFM-1872
		    if (!lValidate.toString().isEmpty() && Constants.PlanStatus.getAfterSubmitStatus().keySet().contains(pPlan.getPlanStatus())) {
			lValidate.insert(0, "This action cannot be performed. Because ");
			throw new WorkflowException(lValidate.toString());
		    }

		    // 1516 - code changes to allow date time update active implementation plan even
		    // if there are
		    // secured plans between new given date..
		    if (!lValidate.toString().isEmpty() && !allowDateChange) {
			if (loadDateTypeChanged) {
			    lValidate.insert(0, "This action cannot be performed. Because ");
			    lResponse.setStatus(Boolean.TRUE);
			    lResponse.setData(lValidate.toString() + " Do you want to continue with this update?");
			    return lResponse;
			}
		    }
		}

		if (Constants.PlanStatus.getAfterSubmitStatus().containsKey(pPlan.getPlanStatus())) {
		    if (loadDateChangedToLater) {
			for (SystemLoad planLoad : pPlan.getSystemLoadList()) {
			    LOG.info("Load Date Time - " + planLoad.getLoadDateTime());
			    List<Object[]> dependentList = getImpPlanDAO().getBtwnSegmentRelatedPlansForLaterDate(pPlan.getId(), planLoad.getSystemId().getId(), planLoad.getLoadDateTime(), new ArrayList(Constants.PlanStatus.getAfterSubmitStatus().keySet()));
			    for (Object[] plan : dependentList) {
				dependentPlanIds.add(plan[2].toString());
			    }
			}
			LOG.info(dependentPlanIds);

			// All Cascade Dependent identification
			for (String planId : dependentPlanIds) {
			    SortedSet<String> dependents = new TreeSet();
			    dependents = rejectHelper.findAllDependentPlanIds(planId, false, null);
			    allCascadeDependentPlanIds.addAll(dependents);
			}
			LOG.info(allCascadeDependentPlanIds);

			// Send to User for confirmation
			if (warningFlag && !allCascadeDependentPlanIds.isEmpty()) {

			    // ZTPFM-2391
			    for (String dependentPlan : allCascadeDependentPlanIds) {
				List<ProductionLoads> lProdLoads = getProductionLoadsDAO().findByPlanId(dependentPlan);
				if (lProdLoads != null && !lProdLoads.isEmpty()) {
				    prodDeplyomentStartedPlans.add(dependentPlan);
				}
			    }
			    if (prodDeplyomentStartedPlans.isEmpty()) {
				lResponse.setStatus(Boolean.TRUE);
				String warningMsg = MessageFormat.format("Warning! This date change will cause an auto-reject of {0} as they have dependent components. Do you want to continue with this update?", StringUtils.join(allCascadeDependentPlanIds, ", "));
				lResponse.setData(warningMsg);
				LOG.warn("Dependents Plans Found! -" + warningMsg);
				return lResponse;
			    } else {
				LOG.warn("Dependents Plans Found! -" + prodDeplyomentStartedPlans);
				throw new WorkflowException("Unable to Update the Load Date and Time, plan - " + prodDeplyomentStartedPlans.toString() + " are secured between the date and deployment activity has started.");
			    }
			}
		    } else if (loadDateTimeChangedToEalier) {
			for (SystemLoad planLoad : pPlan.getSystemLoadList()) {
			    LOG.info("Load Date Time - " + planLoad.getLoadDateTime());
			    List<Object[]> dependentList = getImpPlanDAO().getBtwnSegmentRelatedPlansForBeforeDate(pPlan.getId(), planLoad.getSystemId().getId(), planLoad.getLoadDateTime(), new ArrayList(Constants.PlanStatus.getAfterSubmitStatus().keySet()));
			    for (Object[] plan : dependentList) {
				dependentPlanIds.add(plan[2].toString());
			    }
			}
			LOG.info(dependentPlanIds);
			if (!dependentPlanIds.isEmpty()) {
			    throw new WorkflowException("Unable to Update the Load Date and Time, plan - " + dependentPlanIds.toString() + " are secured between the date.");
			}
		    }

		    // ZTPFM-2625 Update status of PR Ticket as Reject
		    getPRNumberHelper().updatePRNumber(pPlan, Constants.PRNumberStatuses.RESCHEDULED.getPRStatus());

		}

		// Date Audit Validation for Old Process Implementation Plan
		String valMessage = getDateAuditCrossCheck().dateAutditForMigration(pUser, pPlan);
		if (!valMessage.equals("PASSED")) {
		    LOG.error("Date Audit for Old Process fails for Plan - " + pPlan.getId());
		    throw new WorkflowException("Unable to update the Implementation Plan as load date for plan is earlier then load date of non moderization Implementation Plan(s) - " + valMessage);
		}
	    }

	    // ZTPFM-1785 LoadType Change Mail and activityLog
	    if (loadTypeChangeComment != null && !loadTypeChangeComment.equals("")) {
		getPlanHelper().setLoadTypeChangePlan(pUser, pPlan, impPlan, loadTypeChangeComment);
	    }

	    Boolean lIsBuildCleared = Boolean.FALSE;
	    for (SystemLoad commonLoad : dbLoadLists) {
		for (SystemLoad updatedLoad : lLoadLists) {
		    if (commonLoad.getId().equals(updatedLoad.getId())) {
			if ((commonLoad.getLoadDateTime() == null) || (commonLoad.getLoadDateTime() != null && !(Constants.APP_DATE_TIME_FORMAT.get().format(commonLoad.getLoadDateTime()).equals(Constants.APP_DATE_TIME_FORMAT.get().format(updatedLoad.getLoadDateTime()))))) {
			    LOG.info("Load Date Time update occurs");
			    List<Build> lBuilds = new ArrayList();
			    lBuilds.addAll(getBuildDAO().findAll(pPlan, BUILD_TYPE.DVL_BUILD));
			    lBuilds.addAll(getBuildDAO().findAll(pPlan, BUILD_TYPE.DVL_LOAD));
			    if (pPlan.getPlanStatus().equals(Constants.PlanStatus.ACTIVE.name()) && pPlan.getRejectedDateTime() == null) {
				for (Build lBuild : lBuilds) {
				    getBuildDAO().delete(pUser, lBuild);
				}
			    }
			    lIsBuildCleared = Boolean.TRUE;
			    notifyOtherDevelopers(pPlan, commonLoad.getSystemId().getName(), commonLoad.getLoadDateTime(), updatedLoad.getLoadDateTime());
			    LoadDateChangesActivityMessage lLoadDateChangesActivityMessage = new LoadDateChangesActivityMessage(pPlan, null);
			    lLoadDateChangesActivityMessage.setSystemLoad(updatedLoad);
			    lLoadDateChangesActivityMessage.setPreviousLoadDate(commonLoad.getLoadDateTime());
			    getActivityLogDAO().save(pUser, lLoadDateChangesActivityMessage);
			}
			// 2413 -> If there is change in the load category, then enforce the user for
			// DVL LOADSET generate
			if (commonLoad.getLoadCategoryId() != null && updatedLoad.getLoadCategoryId() != null && !commonLoad.getLoadCategoryId().getId().equals(updatedLoad.getLoadCategoryId().getId())) {
			    List<Build> lBuilds = new ArrayList();
			    lBuilds.addAll(getBuildDAO().findAll(pPlan, BUILD_TYPE.DVL_LOAD));
			    lBuilds.stream().forEach(t -> {
				getBuildDAO().delete(pUser, t);
			    });
			}
		    }
		}
	    }
	    for (SystemLoad lLoad : lLoadLists) {
		// if (lIsBuildCleared &&
		// pPlan.getPlanStatus().equals(Constants.PlanStatus.ACTIVE.name())) {
		// lLoad.setLoadSetName(null);
		// lLoad.setFallbackLoadSetName(null);
		// }
		if (lLoad.getId() != null) {
		    lLoad.setPlanId(pPlan);
		    getSystemLoadDAO().update(pUser, lLoad);
		} else {
		    lLoad.setPlanId(pPlan);
		    getSystemLoadDAO().save(pUser, lLoad);
		}
	    }
	    User lUser = lDAPAuthenticatorImpl.getUserDetails(pPlan.getLeadId());
	    if (pPlan.getLeadEmail() == null || !pPlan.getLeadId().equals(impPlan.getLeadId())) {
		pPlan.setLeadEmail(lUser.getMailId());
	    }
	    lResponse.setStatus(Boolean.TRUE);
	    getImpPlanDAO().update(pUser, pPlan);

	    if (!delLoadLists.isEmpty()) {
		PlanCreationActivityMessage planCreationActivityMessage = new PlanCreationActivityMessage(pPlan, null);
		planCreationActivityMessage.setUpdate(true);
		planCreationActivityMessage.setSystemLoads((List) delLoadLists);
		getActivityLogDAO().save(pUser, planCreationActivityMessage);
	    }

	    Collection<SystemLoad> addLoadLists = CollectionUtils.subtract(lLoadLists, dbLoadLists);
	    if (!addLoadLists.isEmpty()) {
		PlanCreationActivityMessage planCreationActivityMessage = new PlanCreationActivityMessage(pPlan, null);
		planCreationActivityMessage.setUpdate(false);
		planCreationActivityMessage.setSystemLoads((List) addLoadLists);
		getActivityLogDAO().save(pUser, planCreationActivityMessage);
	    }
	    // Activity Log on change in load type
	    if (loadDateTypeChanged && (loadTypeChangeComment == null || loadTypeChangeComment.equals(""))) {
		LoadTypeChangesActivityMessage lLoadTypeChangesActivityMessage = new LoadTypeChangesActivityMessage(pPlan, null);
		lLoadTypeChangesActivityMessage.setOldLoadType(impPlan.getLoadType());
		getActivityLogDAO().save(pUser, lLoadTypeChangesActivityMessage);
	    }

	    if ((planStatus != null && !pPlan.getPlanStatus().equalsIgnoreCase(planStatus)) && (pPlan.getPlanStatus().equals(Constants.PlanStatus.PASSED_ACCEPTANCE_TESTING.name()))) {

		if ((pPlan.getId().startsWith("D") || pPlan.getId().toLowerCase().startsWith("d")) && pPlan.getRfcFlag()) {
		    if (rfcDetails.isEmpty() || rfcDetails.stream().filter(rfc -> !rfc.isRFCDetailsFilled()).findAny().isPresent()) {
			throw new WorkflowException("Plan not ready to move to Passed Acceptance testing. RFC Process tab must be completed including RFC number(s) assigned by the TPF Change team.");
		    }
		}

		List<SystemLoad> systemLoadList = pPlan.getSystemLoadList();
		Boolean lPreProdLoaded = Boolean.TRUE;
		for (SystemLoad systemLoad : systemLoadList) {
		    if (systemLoad.getSystemId().getPlatformId().getName().equalsIgnoreCase("Travelport")) {
			List<PreProductionLoads> lProductionLoads = getPreProductionLoadsDAO().findBySystemLoadId(systemLoad);
			if (lProductionLoads.isEmpty()) {
			    lPreProdLoaded = Boolean.FALSE;
			    break;
			}
			for (PreProductionLoads lProductionLoad : lProductionLoads) {
			    if (!lProductionLoad.getStatus().equals(LOAD_SET_STATUS.ACTIVATED.name())) {
				lPreProdLoaded = Boolean.FALSE;
				break;
			    }
			}
			if (!lPreProdLoaded) {
			    break;
			}
		    } else {
			List<SystemLoadActions> lProductionLoads = getSystemLoadActionsDAO().findBySystemLoadEnv(systemLoad, VPARSEnvironment.PRE_PROD);
			if (lProductionLoads.isEmpty()) {
			    lPreProdLoaded = Boolean.FALSE;
			    break;
			}
			for (SystemLoadActions lProductionLoad : lProductionLoads) {
			    if (!lProductionLoad.getStatus().equals(LOAD_SET_STATUS.ACTIVATED.name())) {
				lPreProdLoaded = Boolean.FALSE;
				break;
			    }
			}
			if (!lPreProdLoaded) {
			    break;
			}
		    }
		}
		if (!lPreProdLoaded) {
		    throw new WorkflowException("The Plan " + pPlan.getId() + " is not deployed into Pre Prod Systems");
		}
		impPlan.setApproveRequestDateTime(new Date());
		PlanStatusActivityMessage planStatusActivityMessage = new PlanStatusActivityMessage(pPlan, null);
		planStatusActivityMessage.setStatus("Passed Acceptance Testing");
		getActivityLogDAO().save(pUser, planStatusActivityMessage);

		String lCompanyName = pPlan.getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
		String lRepoName = getJGitClientUtils().getPlanRepoName(lCompanyName, pPlan.getId().toLowerCase());
		List<String> lBranchList = getJGitClientUtils().getAllBranchList(lCompanyName, pPlan.getId());

		if (!getsSHClientUtils().addImplementationTag(lRepoName, Constants.PlanStatus.PASSED_ACCEPTANCE_TESTING, lBranchList)) {
		    LOG.error("Unable to add tag-" + Constants.PlanStatus.PASSED_ACCEPTANCE_TESTING.name() + " to Plan-" + pPlan.getId());
		}

		List<TaskVariable> lTaskVars = new ArrayList<>();
		getBPMClientUtils().setTaskAsCompleted(pUser, pPlan.getProcessId());
		getBPMClientUtils().assignTask(pUser, pPlan.getProcessId(), pPlan.getDevManager(), lTaskVars);

		DevManagerAssignmentMail devManagerAssignmentMail = (DevManagerAssignmentMail) mailMessageFactory.getTemplate(DevManagerAssignmentMail.class);
		devManagerAssignmentMail.setLeadName(pPlan.getLeadName());
		devManagerAssignmentMail.setPlanId(pPlan.getId());
		devManagerAssignmentMail.addToAddressUserId(pPlan.getDevManager(), Constants.MailSenderRole.DEV_MANAGER);
		mailMessageFactory.push(devManagerAssignmentMail);
	    }

	    if (!warningFlag && !allCascadeDependentPlanIds.isEmpty()) {
		for (String dependentPlan : dependentPlanIds) {
		    LOG.info("Rejected dependent plan after moved to later date : " + dependentPlan);
		    getRejectHelper().rejectDependentPlans(pUser, dependentPlan, Constants.REJECT_REASON.LOAD_DATE_CHANGE.getValue() + pPlan.getId(), Constants.AUTOREJECT_COMMENT.LOAD_DATE_CHANGE.getValue() + pPlan.getId(), Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, pPlan.getId(), Boolean.TRUE);
		}
	    }

	    // ADL reassignment
	    if (!(lDBLeadId.equalsIgnoreCase(pPlan.getLeadId()))) {
		String lOldLeadName = getLDAPAuthenticatorImpl().getUserDetails(lDBLeadId).getDisplayName();
		ReassignImplementationPlanMail reassignImplementationPlanMail = (ReassignImplementationPlanMail) mailMessageFactory.getTemplate(ReassignImplementationPlanMail.class);

		List<Implementation> lImpList = getImplementationDAO().findByImpPlan(impPlan.getId());
		Set<String> lToList = new HashSet();
		Set<String> lCcList = new HashSet();
		if (lImpList != null && lImpList.size() > 0) {
		    for (Implementation impl : lImpList) {
			if (impl.getPeerReviewers() != null && !impl.getPeerReviewers().equals("")) {
			    lCcList.add(impl.getPeerReviewers());
			    // reassignImplementationPlanMail.addToAddressUserId(impl.getPeerReviewers());
			}
			lCcList.add(impl.getDevId());
		    }
		}

		reassignImplementationPlanMail.setImpPlanId(pPlan.getId());
		reassignImplementationPlanMail.setNewLead(pPlan.getLeadName());
		reassignImplementationPlanMail.setOldLead(lOldLeadName);
		reassignImplementationPlanMail.setRole(pUser.getCurrentRole());
		reassignImplementationPlanMail.setCurrentUser(pUser.getDisplayName());
		lToList.add(pPlan.getLeadId());
		lCcList.add(lDBLeadId);

		// reassignImplementationPlanMail.addToAddressUserId(pPlan.getDevManager());
		// reassignImplementationPlanMail.addToAddressUserId(pPlan.getLeadId());
		if (dbLoadLists != null && dbLoadLists.size() > 0) {
		    for (SystemLoad sysLoad : dbLoadLists) {
			if (sysLoad.getLoadAttendeeId() != null && !sysLoad.getLoadAttendeeId().equals("")) {
			    lCcList.add(sysLoad.getLoadAttendeeId());
			    // reassignImplementationPlanMail.addToAddressUserId(sysLoad.getLoadAttendeeId());
			}

		    }
		}
		lToList.stream().forEach(t -> reassignImplementationPlanMail.addToAddressUserId(t, Constants.MailSenderRole.LEAD));
		lCcList.stream().forEach(c -> reassignImplementationPlanMail.addcCAddressUserId(c, Constants.MailSenderRole.LOADS_ATTENDEE));
		reassignImplementationPlanMail.addcCAddressUserId(pPlan.getDevManager(), Constants.MailSenderRole.DEV_MANAGER);
		reassignImplementationPlanMail.setDevManager(pPlan.getDevManagerName());
		mailMessageFactory.push(reassignImplementationPlanMail);
		ReassignImplementationPlanActivityMessage reassignLeadActivityMessage = new ReassignImplementationPlanActivityMessage(pPlan, null);
		reassignLeadActivityMessage.setRole(pUser.getCurrentRole());
		reassignLeadActivityMessage.setOldLeadName(lOldLeadName);
		getActivityLogDAO().save(pUser, reassignLeadActivityMessage);

	    }

	    LOG.info("QA Old Functional Tester's " + qaOldFunTesters);
	    LOG.info("QA New Functional Tester's " + qaNewFunTesters);
	    // ZTPFM-1455 QA Functional Testers added and removed.
	    if (!qaNewFunTesters.equals(qaOldFunTesters)) {
		List<String> currentQAFunTesterList = Arrays.asList(qaNewFunTesters.toString().split(","));
		List<String> currentQAFunNameList = new ArrayList<>();
		List<String> allQAFunTesters = new ArrayList<>();

		for (String reviewer : currentQAFunTesterList) {
		    currentQAFunNameList.add(getLDAPAuthenticatorImpl().getUserDetails(reviewer).getDisplayName());
		}

		List<String> removedQAFunTestersName = new ArrayList<>();
		List<String> newQAFunTestersName = new ArrayList<>();

		for (String oldFunTester : qaOldFunTesters) {
		    if (!qaNewFunTesters.contains(oldFunTester)) {
			removedQAFunTestersName.add(getLDAPAuthenticatorImpl().getUserDetails(oldFunTester).getDisplayName());
		    }
		}
		for (String newFunTester : qaNewFunTesters) {
		    if (!qaOldFunTesters.contains(newFunTester)) {
			newQAFunTestersName.add(getLDAPAuthenticatorImpl().getUserDetails(newFunTester).getDisplayName());
		    }
		}

		allQAFunTesters.addAll(qaOldFunTesters);
		allQAFunTesters.addAll(qaNewFunTesters);

		/**
		 * Activity Log
		 */
		QAFunTesterAssignReAssignActivityMessage lQAFunTesterActivity = new QAFunTesterAssignReAssignActivityMessage(pPlan, null);
		lQAFunTesterActivity.setUser(pUser);
		if (!newQAFunTestersName.isEmpty()) {
		    lQAFunTesterActivity.setQaFunTesterList(newQAFunTestersName);
		    lQAFunTesterActivity.setAction("added");
		    getActivityLogDAO().save(pUser, lQAFunTesterActivity);
		}
		if (!removedQAFunTestersName.isEmpty()) {
		    lQAFunTesterActivity.setQaFunTesterList(removedQAFunTestersName);
		    lQAFunTesterActivity.setAction("removed");
		    getActivityLogDAO().save(pUser, lQAFunTesterActivity);
		}

		// Getting Developer Id from Implementation
		List<String> lDevAndADLlist = getPlanHelper().getDevAndAdlList(pPlan);
		// Getting project Details from impPlan
		Project lProject = getProjectDAO().find(pPlan.getProjectId().getId());
		Map<String, String> qaInsAndTrgtSysMap = getPlanHelper().getSytemandQAInstruction(pPlan);

		/**
		 * Mails
		 */
		if (!newQAFunTestersName.isEmpty()) {
		    QaFunctionalTesterReassignmentMail qaFunctionalTesterReassignmentMail = (QaFunctionalTesterReassignmentMail) getMailMessageFactory().getTemplate(QaFunctionalTesterReassignmentMail.class);
		    qaFunctionalTesterReassignmentMail.setRemoved(false);
		    qaFunctionalTesterReassignmentMail.setPlanId(pPlan.getId());
		    qaFunctionalTesterReassignmentMail.setUser(pUser);
		    qaFunctionalTesterReassignmentMail.setProjectName(lProject.getProjectName());
		    qaFunctionalTesterReassignmentMail.setProjectCSRNum(lProject.getProjectNumber());
		    qaFunctionalTesterReassignmentMail.setQaFunTestersId(allQAFunTesters);
		    qaFunctionalTesterReassignmentMail.setAddedQaFunTestersName(newQAFunTestersName);
		    qaFunctionalTesterReassignmentMail.setProgramNameTargetSys(qaInsAndTrgtSysMap);
		    allQAFunTesters.stream().forEach(t -> qaFunctionalTesterReassignmentMail.addToAddressUserId(t, Constants.MailSenderRole.QA_FUNCTIONAL));
		    lDevAndADLlist.stream().forEach(t -> qaFunctionalTesterReassignmentMail.addcCAddressUserId(t, Constants.MailSenderRole.LEAD));
		    if (!newQAFunTestersName.isEmpty() && !removedQAFunTestersName.isEmpty()) {
			qaFunctionalTesterReassignmentMail.setReAssigned(true);
			qaFunctionalTesterReassignmentMail.setRemoveQaFunTestersName(removedQAFunTestersName);
		    }
		    getMailMessageFactory().push(qaFunctionalTesterReassignmentMail);
		}

		if (!removedQAFunTestersName.isEmpty() && newQAFunTestersName.isEmpty()) {
		    QaFunctionalTesterReassignmentMail qaFunctionalTesterReassignmentMail = (QaFunctionalTesterReassignmentMail) getMailMessageFactory().getTemplate(QaFunctionalTesterReassignmentMail.class);
		    qaFunctionalTesterReassignmentMail.setRemoved(true);
		    qaFunctionalTesterReassignmentMail.setPlanId(pPlan.getId());
		    qaFunctionalTesterReassignmentMail.setUser(pUser);
		    qaFunctionalTesterReassignmentMail.setProjectName(lProject.getProjectName());
		    qaFunctionalTesterReassignmentMail.setProjectCSRNum(lProject.getProjectNumber());
		    qaFunctionalTesterReassignmentMail.setQaFunTestersId(allQAFunTesters);
		    qaFunctionalTesterReassignmentMail.setRemoveQaFunTestersName(removedQAFunTestersName);
		    qaFunctionalTesterReassignmentMail.setProgramNameTargetSys(qaInsAndTrgtSysMap);
		    allQAFunTesters.stream().forEach(t -> qaFunctionalTesterReassignmentMail.addToAddressUserId(t, Constants.MailSenderRole.QA_FUNCTIONAL));
		    lDevAndADLlist.stream().forEach(t -> qaFunctionalTesterReassignmentMail.addcCAddressUserId(t, Constants.MailSenderRole.LEAD));
		    getMailMessageFactory().push(qaFunctionalTesterReassignmentMail);
		}
	    }

	} catch (WorkflowException e) {
	    lResponse.setStatus(false);
	    lResponse.setErrorMessage(e.getMessage());
	    throw e;
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    lResponse.setStatus(false);
	    lResponse.setErrorMessage("Unable to Update Implementation Plan" + ex.getMessage());
	    throw new WorkflowException("Unable to Update Implementation Plan", ex);
	} finally {
	    cacheClient.getPlanUpdateStatusMap().remove(pPlan.getId());
	    lResponse.setMetaData(pPlan.getId());
	    wsserver.sendMessage(Constants.Channels.PLAN_UPDATE, pUser.getId(), pPlan.getId(), lResponse);
	}
	return lResponse;
    }

    public void notifyOtherDevelopers(ImpPlan pPlan, String system, Date beforeUpdaet, Date afterUpdate) {
	List<Object[]> planList = getImpPlanDAO().getDevelopersByTargetSystem(pPlan.getId(), system);
	Map<String, List<Object[]>> collect = planList.stream().collect(Collectors.groupingBy(t -> t[0].toString()));

	for (Map.Entry<String, List<Object[]>> entrySet : collect.entrySet()) {
	    LOG.info("Mail Notification for same source artifact with load date time");
	    String key = entrySet.getKey();
	    List<Object[]> value = entrySet.getValue();
	    String devId = "", leadId = "";
	    String toPlanId = "";
	    for (Object[] segment : value) {
		devId = segment[3].toString();
		leadId = segment[2].toString();
		toPlanId = segment[1].toString();

	    }
	    LoadDateChangedMail loadDateChangedMail = (LoadDateChangedMail) mailMessageFactory.getTemplate(LoadDateChangedMail.class);
	    loadDateChangedMail.setPlanId(pPlan.getId());
	    loadDateChangedMail.setPlanLoadType(pPlan.getLoadType());
	    loadDateChangedMail.setToPlanId(toPlanId);
	    loadDateChangedMail.setLeadId(leadId);
	    loadDateChangedMail.setDeveloperId(devId);
	    loadDateChangedMail.setSystem(system);
	    loadDateChangedMail.setBeforeUpdate(beforeUpdaet);
	    loadDateChangedMail.setAfterUpdate(afterUpdate);
	    mailMessageFactory.push(loadDateChangedMail);
	}
    }

    public JSONResponse compilerControlValidationForSystem(User pUser, Implementation pImplementation, System lSystem) {
	JSONResponse lResponse = new JSONResponse();

	if (lSystem == null) {
	    LOG.error("System Not Found");
	    lResponse.setErrorMessage("System Not Found");
	    lResponse.setStatus(Boolean.FALSE);
	    return lResponse;
	}

	String lCommand = Constants.SystemScripts.COMPILER_CONTROL_VALIDATION.getScript() + " " + pImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	lResponse = getsSHClientUtils().executeCommand(lSystem, lCommand);

	return lResponse;
    }

    @Transactional
    public JSONResponse updateSystem(User lUser, System pSystem) {
	JSONResponse lResponse = new JSONResponse();
	getSystemDAO().update(lUser, pSystem);
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse saveDelegation(User lUser, UserSettings pUserSetting) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    Constants.UserSettings lChanged = Constants.UserSettings.valueOf(pUserSetting.getName());
	    UserSettings lCurrentSettings = getUserSettingsDAO().find(pUserSetting.getUserId(), Constants.UserSettings.DELEGATE_USER.name());
	    if (lCurrentSettings != null) {
		if (lChanged == Constants.UserSettings.DELEGATION && pUserSetting.getValue().equalsIgnoreCase("TRUE")) {
		    if (!getDelegateHelper().addToCache(lCurrentSettings.getUserId(), lCurrentSettings.getValue(), true)) {
			User lFromUser = getLDAPAuthenticatorImpl().getUserDetails(lCurrentSettings.getValue());
			User lToUser = getLDAPAuthenticatorImpl().getUserDetails(lCurrentSettings.getUserId());
			lResponse.setStatus(Boolean.FALSE);
			lResponse.setErrorMessage("User " + lToUser.getDisplayName() + " is already delegated to " + lFromUser.getDisplayName());
			return lResponse;
		    }
		} else if (lChanged == Constants.UserSettings.DELEGATION) {
		    getDelegateHelper().removeFromCache(lCurrentSettings.getUserId(), lCurrentSettings.getValue(), true);
		}
	    }

	    if (pUserSetting.getId() == null) {
		getUserSettingsDAO().save(lUser, pUserSetting);
	    } else {
		getUserSettingsDAO().update(lUser, pUserSetting);
	    }

	    switch (lChanged) {
	    case DELEGATE_USER:
		getDelegateHelper().onAssignDelegate(lUser, pUserSetting);
		break;
	    case DELEGATION:
		if (pUserSetting.getValue().equalsIgnoreCase("TRUE")) {
		    getDelegateHelper().onActivateDelegate(lUser, pUserSetting);
		} else {
		    getDelegateHelper().onDeActivateDelegate(lUser, pUserSetting);
		}
		break;
	    }
	    lResponse.setStatus(Boolean.TRUE);
	    return lResponse;
	} catch (Exception Ex) {
	    LOG.error("Unable to Save Delegates", Ex);
	    throw new WorkflowException("Unable to save delegates", Ex);
	}
    }

    @Transactional
    public JSONResponse setSuperUser(User lUser, UserSettings pUserSetting) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    getDelegateHelper().onActivateSuperUser(lUser, pUserSetting);
	    lResponse.setStatus(Boolean.TRUE);
	    return lResponse;
	} catch (Exception Ex) {
	    LOG.error("Unable to Save Super User", Ex);
	    throw new WorkflowException("Unable to save Super User", Ex);
	}
    }

    @Transactional
    public JSONResponse getSettingsList(User lCurrentUser) {
	JSONResponse lResponse = new JSONResponse();
	List<UserSettings> findAll = getUserSettingsDAO().findAll(lCurrentUser.getId());
	lResponse.setData(findAll);
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse getSettingsListforSuperUser(User lCurrentUser) {
	JSONResponse lResponse = new JSONResponse();
	SortedSet<User> lUsers = lSuperUserMap.get(lCurrentUser.getId());
	if (lUsers == null) {
	    lResponse.setData(new TreeSet<>());
	} else {
	    lResponse.setData(lUsers);
	}
	lResponse.setData(lUsers);
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse revertImplementation(User user, String impId) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    Implementation implementation = implementationDAO.find(impId);
	    List<SystemLoad> lSystemLoads = getSystemLoadDAO().findByImpPlan(implementation.getPlanId().getId());
	    String lCompanyName = lSystemLoads.get(0).getSystemId().getPlatformId().getNickName();
	    String lRepoName = getJGitClientUtils().getPlanRepoName(lCompanyName, implementation.getPlanId().getId().toLowerCase());
	    // Boolean status =
	    // getlGitBlitClientUtils().setPermissionForGitRepository(lRepoName,
	    // implementation.getDevId(),
	    // Constants.GIT_PERMISSION_READWRITE)
	    // if (!status) {
	    // throw new WorkflowException("Unable to provide Read/Write Access to Plan - "
	    // +
	    // implementation.getPlanId().getId() + " , Contact Support Team");
	    // }
	    implementation.setSubstatus(null);
	    implementation.setReviewersDone("");
	    implementation.setCheckinDateTime(null);
	    implementation.setImpStatus(Constants.ImplementationStatus.IN_PROGRESS.name());
	    implementation.setIsCheckedin(Boolean.FALSE);
	    implementation.setLastCheckinStatus(null);
	    implementationDAO.update(user, implementation);
	    getGitHelper().updateImplementationPlanRepoPermissions(implementation.getPlanId().getId(), null);
	    ImplementationStatusRevertActivityMessage lMessage = new ImplementationStatusRevertActivityMessage(implementation.getPlanId(), implementation);
	    lMessage.setStatus("In Progress");
	    activityLogDAO.save(user, lMessage);
	} catch (WorkflowException ex) {
	    LOG.error("Unable to Revert the Implemenation - " + impId, ex);
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("Unable to Revert the Implemenation - " + impId, ex);
	    throw new WorkflowException("Unable to revert the implemetation - " + impId + ", Contact Support team");
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse getDevLoadByPlan(String ids) {
	JSONResponse lResponse = new JSONResponse();
	List<SystemLoad> systemLoadList = getSystemLoadDAO().findByImpPlan(new ImpPlan(ids));
	List<System> systemList = new ArrayList<>();
	for (SystemLoad systemLoad : systemLoadList) {
	    systemList.add(systemLoad.getSystemId());
	}
	if (!systemList.isEmpty()) {
	    List<Build> lBuilds = getBuildDAO().findLastBuild(ids, systemList, BUILD_TYPE.DVL_LOAD);
	    lResponse.setData(lBuilds);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse deleteDbcr(User user, String dbcrId) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	try {
	    getDbcrDAO().delete(user, Integer.parseInt(dbcrId));
	    lResponse.setStatus(Boolean.TRUE);
	    getDbcrDAO().findByDBCRId(Integer.parseInt(dbcrId)).forEach(dbcr -> {
		DbcrActivityMessage dbcrActivityMessage = new DbcrActivityMessage(dbcr.getPlanId(), null);
		dbcrActivityMessage.setCreatedBy(user.getCurrentRole() + " " + user.getDisplayName());
		dbcrActivityMessage.setDbcrName(dbcr.getDbcrName());
		dbcrActivityMessage.setTargetSystem(getSystemDAO().find(dbcr.getSystemId().getId()).getName());
		dbcrActivityMessage.setAction("Removed");
		getActivityLogDAO().save(user, dbcrActivityMessage);
	    });
	} catch (Exception ex) {
	    LOG.error("Unable to delete Dbcr for plan", ex);
	    throw new WorkflowException("Unable to delete DBCR for implementation plan. ", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse saveDbcrList(User user, List<Dbcr> dbcrList) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	try {
	    for (Dbcr dbcr : dbcrList) {
		boolean isDbcrAdded = false;
		if (dbcr.getId() == null) {
		    isDbcrAdded = true;
		    getDbcrDAO().save(user, dbcr);
		} else {
		    getDbcrDAO().update(user, dbcr);
		}
		DbcrActivityMessage dbcrActivityMessage = new DbcrActivityMessage(dbcr.getPlanId(), null);
		dbcrActivityMessage.setCreatedBy(user.getCurrentRole() + " " + user.getDisplayName());
		dbcrActivityMessage.setDbcrName(dbcr.getDbcrName());
		dbcrActivityMessage.setTargetSystem(getSystemDAO().find(dbcr.getSystemId().getId()).getName());
		dbcrActivityMessage.setAction(isDbcrAdded ? " Added " : " Updated ");
		getActivityLogDAO().save(user, dbcrActivityMessage);
	    }
	    lResponse.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error("Unable to save Dbcr for plan", ex);
	    throw new WorkflowException("Unable to save DBCR for implementation plan. ", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getPlanByAdvancedSearch(AdvancedSearchForm searchForm, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) throws WorkflowException, IOException {
	JSONResponse lResponse = new JSONResponse();
	List<AdvancedMetaSearchResult> findAll = advanceSearchViewDAO.getPlanByAdvancedSearchView(searchForm, pOffset, pLimit, pOrderBy);
	for (int i = 0; i < findAll.size(); i++) {
	    AdvancedMetaSearchResult advancedMetaSearchResult = findAll.get(i);
	    if (searchForm.getFunctionalPackages() != null && searchForm.getFunctionalPackages().size() > 0) {
		advancedMetaSearchResult.setFunctionalarea(objectCompareUnlikeRemove(advancedMetaSearchResult.getFunctionalarea(), searchForm.getFunctionalPackages()));
	    }
	    if (searchForm.getProgramName() != null) {
		if (searchForm.getExactSegment()) {
		    advancedMetaSearchResult.setProgramname(objectCompareUnlikeRemove(advancedMetaSearchResult.getProgramname(), searchForm.getProgramName()));
		}
	    }
	}
	Map<String, List<AdvancedMetaSearchResult>> collect = findAll.stream().collect(Collectors.groupingBy(T -> T.getPlanid() + T.getTargetsystem()));
	// ZTPFM-1449
	String complanyName = null;
	if (getWFConfig().getIsDeltaApp()) {
	    complanyName = "dl";
	} else {
	    complanyName = "tp";
	}
	if (searchForm.getProgramName() != null && !searchForm.getProgramName().isEmpty()) {
	    Collection<GitSearchResult> lCompanyResult = getjGITSearchUtils().SearchAllRepos(complanyName, searchForm.getProgramName(), false, Constants.PRODSearchType.PENDING_ONLY, new ArrayList());
	    Integer systems[] = new Integer[7];
	    int i = 0;
	    for (System sys : searchForm.getTargetSystems()) {
		systems[i] = sys.getId();
		i++;
	    }
	    List<System> lSystems = getSystemDAO().findByIds(systems);
	    List<String> systemList = lSystems.stream().map(sys -> "master_" + sys.getName().toLowerCase()).collect(Collectors.toList());
	    for (GitSearchResult lResult : lCompanyResult) {
		for (GitBranchSearchResult lBranch : lResult.getBranch()) {
		    if (lBranch.getRefLoadDate().after(searchForm.getStartDate()) && lBranch.getRefLoadDate().before(searchForm.getEndDate()) && systemList.contains(lBranch.getTargetSystem())) {
			String targetSystem[] = lBranch.getTargetSystem().split("_");
			AdvancedMetaSearchResult obj = new AdvancedMetaSearchResult();
			obj.setPlanid(lBranch.getRefPlan());
			obj.setTargetsystem(targetSystem[1].toUpperCase());
			obj.setProgramname(lResult.getProgramName());
			obj.setPlanstatus(lBranch.getRefStatus().toUpperCase());
			obj.setFunctionalarea(lBranch.getFuncArea());
			obj.setLoaddatetime(lBranch.getRefLoadDate());
			List<AdvancedMetaSearchResult> searchList = new ArrayList<AdvancedMetaSearchResult>();
			if (collect.containsKey(lBranch.getRefPlan() + targetSystem[1].toUpperCase())) {
			    searchList = collect.get(lBranch.getRefPlan() + targetSystem[1].toUpperCase());
			}
			searchList.add(obj);
			collect.put(lBranch.getRefPlan() + targetSystem[1].toUpperCase(), searchList);
		    }
		}
	    }
	}

	// 2161 Changes
	Map<String, AdvancedSearchResultbyPlan> advancedSearchResultbyPlanMap = new HashMap<String, AdvancedSearchResultbyPlan>();
	collect.forEach((key, values) -> {
	    AdvancedMetaSearchResult advancedMetaSearchResult = values.stream().distinct().findFirst().get();
	    AdvancedSearchResultbyPlan advancedSearchResultbyPlan = new AdvancedSearchResultbyPlan();
	    if (!advancedSearchResultbyPlanMap.containsKey(advancedMetaSearchResult.getPlanid())) {
		advancedSearchResultbyPlan.setPlanid(advancedMetaSearchResult.getPlanid());
		advancedSearchResultbyPlan.setPlandescription(advancedMetaSearchResult.getPlandescription());
		advancedSearchResultbyPlan.setPlanstatus(advancedMetaSearchResult.getPlanstatus());
		advancedSearchResultbyPlan.setProjectname(advancedMetaSearchResult.getProjectname());
		advancedSearchResultbyPlan.setProjectNumber(advancedMetaSearchResult.getCsrnumber());
		advancedSearchResultbyPlan.setDevelopername(advancedMetaSearchResult.getDevelopername());
		List<AdvanceSearchSystemBasedDetails> systemBasedDetailsList = new ArrayList<>();
		systemBasedDetailsList.add(getAdvanceSearchSystemBasedDetailsList(advancedMetaSearchResult));
		advancedSearchResultbyPlan.setAdvanceSearchSystemBasedDetails(systemBasedDetailsList);
		advancedSearchResultbyPlanMap.put(advancedSearchResultbyPlan.getPlanid(), advancedSearchResultbyPlan);
	    } else {
		advancedSearchResultbyPlanMap.get(advancedMetaSearchResult.getPlanid()).getAdvanceSearchSystemBasedDetails().add(getAdvanceSearchSystemBasedDetailsList(advancedMetaSearchResult));
	    }
	});

	Map<String, AdvancedSearchResultbyPlan> collect1 = new LinkedHashMap<String, AdvancedSearchResultbyPlan>();
	if (pOrderBy != null && !pOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> entrySet : pOrderBy.entrySet()) {
		collect1 = sortByLoadCatByPlan(advancedSearchResultbyPlanMap, entrySet.getValue(), entrySet.getKey());
	    }
	} else {
	    collect1 = sortByLoadCatByPlan(advancedSearchResultbyPlanMap, null, "loaddatetime");
	}

	lResponse.setCount(collect1.size());
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(getFilteredResultbyPlan(collect1, pOffset, pLimit));
	return lResponse;
    }

    private AdvanceSearchSystemBasedDetails getAdvanceSearchSystemBasedDetailsList(AdvancedMetaSearchResult searchResult) {
	AdvanceSearchSystemBasedDetails systemBasedDetails = new AdvanceSearchSystemBasedDetails();
	systemBasedDetails.setFunctionalarea(searchResult.getFunctionalarea());
	systemBasedDetails.setQastatus(searchResult.getQastatus());
	systemBasedDetails.setTargetsystem(searchResult.getTargetsystem());
	systemBasedDetails.setLoadcategory(searchResult.getLoadcategory());
	systemBasedDetails.setLoaddatetime(searchResult.getLoaddatetime());
	systemBasedDetails.setActivateddatetime(searchResult.getActivateddatetime());
	systemBasedDetails.setFallbackdatetime(searchResult.getFallbackdatetime());
	systemBasedDetails.setProgNames(searchResult.getProgramname());
	return systemBasedDetails;
    }

    private Map<String, List<AdvancedMetaSearchResult>> getFilteredResult(Map<String, List<AdvancedMetaSearchResult>> collect, Integer pOffset, Integer pLimit) {
	Map<String, List<AdvancedMetaSearchResult>> filteredMap = new LinkedHashMap<>();

	int count = 0;
	int minVal = pOffset * pLimit;
	int maxVal = (pOffset * pLimit) + pLimit;
	for (Map.Entry<String, List<AdvancedMetaSearchResult>> entry : collect.entrySet()) {
	    if (count >= minVal && count < maxVal) {
		filteredMap.put(entry.getKey(), entry.getValue());
		count++;
	    } else if (count < maxVal) {
		count++;
	    } else if (count >= maxVal) {
		break;
	    }
	}
	return filteredMap;
    }

    private Map<String, AdvancedSearchResultbyPlan> getFilteredResultbyPlan(Map<String, AdvancedSearchResultbyPlan> collect, Integer pOffset, Integer pLimit) {
	Map<String, AdvancedSearchResultbyPlan> filteredMap = new LinkedHashMap<>();
	int count = 0;
	int minVal = pOffset * pLimit;
	int maxVal = (pOffset * pLimit) + pLimit;
	for (Map.Entry<String, AdvancedSearchResultbyPlan> entry : collect.entrySet()) {
	    if (count >= minVal && count < maxVal) {
		filteredMap.put(entry.getKey(), entry.getValue());
		count++;
	    } else if (count < maxVal) {
		count++;
	    } else if (count >= maxVal) {
		break;
	    }
	}
	return filteredMap;
    }

    private Map<String, List<AdvancedMetaSearchResult>> sortByLoadCat(Map<String, List<AdvancedMetaSearchResult>> collect, String sortType, String sortValue) {

	TreeMap<Object, List<String>> loadCatAndImpId = new TreeMap<>();
	Map<String, List<AdvancedMetaSearchResult>> sortedMap = new LinkedHashMap<>();
	Map<String, List<AdvancedMetaSearchResult>> unSortedMap = new LinkedHashMap<>();

	if (sortType == null || sortType.trim().isEmpty()) {
	    sortType = "asc";
	}

	collect.forEach((key, values) -> {
	    AdvancedMetaSearchResult sortVal = values.stream().findAny().get();
	    Object sortKey = null;
	    if ("loadcategory".equalsIgnoreCase(sortValue)) {
		sortKey = sortVal.getLoadcategory();
	    } else if ("csrnumber".equalsIgnoreCase(sortValue)) {
		sortKey = sortVal.getCsrnumber();
	    } else if ("planstatus".equalsIgnoreCase(sortValue)) {
		sortKey = sortVal.getPlanstatus();
	    } else if ("planid".equalsIgnoreCase(sortValue)) {
		sortKey = sortVal.getPlanid();
	    } else if ("qastatus".equalsIgnoreCase(sortValue)) {
		sortKey = sortVal.getQastatus();
	    } else if ("targetsystem".equalsIgnoreCase(sortValue)) {
		sortKey = sortVal.getTargetsystem();
	    } else if ("loaddatetime".equalsIgnoreCase(sortValue)) {
		sortKey = sortVal.getLoaddatetime();
	    } else if ("activateddatetime".equalsIgnoreCase(sortValue)) {
		sortKey = sortVal.getActivateddatetime();
	    } else if ("fallbackdatetime".equalsIgnoreCase(sortValue)) {
		sortKey = sortVal.getFallbackdatetime();
	    } else {
		// default Sorting by load date time
		sortKey = sortVal.getLoaddatetime();
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
	int count = 0;
	if (sortType.trim().equalsIgnoreCase("desc")) {
	    count = 0;
	    for (Map.Entry<Object, List<String>> entry : loadCatAndImpId.descendingMap().entrySet()) {
		count++;
		for (String impId : entry.getValue()) {
		    if (collect.containsKey(impId)) {
			sortedMap.put(String.format("%010d", count) + "-" + impId, collect.get(impId));
		    }
		}
	    }
	} else {
	    count = 0;
	    for (Map.Entry<Object, List<String>> entry : loadCatAndImpId.entrySet()) {
		count++;
		for (String impId : entry.getValue()) {
		    if (collect.containsKey(impId)) {
			sortedMap.put(String.format("%010d", count) + "-" + impId, collect.get(impId));
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
    private Map<String, AdvancedSearchResultbyPlan> sortByLoadCatByPlan(Map<String, AdvancedSearchResultbyPlan> collect, String sortType, String sortValue) {
	TreeMap<Object, List<String>> loadCatAndImpId = new TreeMap<>();
	Map<String, AdvancedSearchResultbyPlan> sortedMap = new LinkedHashMap<>();
	Map<String, AdvancedSearchResultbyPlan> unSortedMap = new LinkedHashMap<>();
	if (sortType == null || sortType.trim().isEmpty()) {
	    sortType = "asc";
	}
	collect.forEach((key, values) -> {
	    AdvancedSearchResultbyPlan sortVal = values;
	    List<AdvanceSearchSystemBasedDetails> tempSearchRes = new ArrayList<>();
	    if ((sortValue == null || sortValue.isEmpty() || ("loaddatetime".equalsIgnoreCase(sortValue))) && values.getAdvanceSearchSystemBasedDetails().stream().filter(res -> res.getLoaddatetime() != null).findAny().isPresent()) {
		tempSearchRes.add(values.getAdvanceSearchSystemBasedDetails().stream().filter(res -> res.getLoaddatetime() != null).min(Comparator.comparing(AdvanceSearchSystemBasedDetails::getLoaddatetime)).get());
	    }
	    if ((sortValue == null || sortValue.isEmpty() || ("activateddatetime".equalsIgnoreCase(sortValue))) && values.getAdvanceSearchSystemBasedDetails().stream().filter(res -> res.getActivateddatetime() != null).findAny().isPresent()) {
		tempSearchRes.add(values.getAdvanceSearchSystemBasedDetails().stream().filter(res -> res.getActivateddatetime() != null).min(Comparator.comparing(AdvanceSearchSystemBasedDetails::getActivateddatetime)).get());
	    }
	    if ((sortValue == null || sortValue.isEmpty() || ("fallbackdatetime".equalsIgnoreCase(sortValue))) && values.getAdvanceSearchSystemBasedDetails().stream().filter(res -> res.getFallbackdatetime() != null).findAny().isPresent()) {
		tempSearchRes.add(values.getAdvanceSearchSystemBasedDetails().stream().filter(res -> res.getFallbackdatetime() != null).min(Comparator.comparing(AdvanceSearchSystemBasedDetails::getFallbackdatetime)).get());
	    }
	    if (tempSearchRes.isEmpty()) {
		tempSearchRes.addAll(values.getAdvanceSearchSystemBasedDetails());
	    }
	    tempSearchRes.forEach((t) -> {
		Object sortKey = null;
		if ("loadcategory".equalsIgnoreCase(sortValue)) {
		    sortKey = t.getLoadcategory();
		} else if ("csrnumber".equalsIgnoreCase(sortValue)) {
		    sortKey = sortVal.getProjectNumber();
		} else if ("planstatus".equalsIgnoreCase(sortValue)) {
		    sortKey = sortVal.getPlanstatus();
		} else if ("planid".equalsIgnoreCase(sortValue)) {
		    sortKey = sortVal.getPlanid();
		} else if ("qastatus".equalsIgnoreCase(sortValue)) {
		    sortKey = t.getQastatus();
		} else if ("targetsystem".equalsIgnoreCase(sortValue)) {
		    sortKey = t.getTargetsystem();
		} else if ("loaddatetime".equalsIgnoreCase(sortValue)) {
		    sortKey = t.getLoaddatetime();
		} else if ("activateddatetime".equalsIgnoreCase(sortValue)) {
		    sortKey = t.getActivateddatetime();
		} else if ("fallbackdatetime".equalsIgnoreCase(sortValue)) {
		    sortKey = t.getFallbackdatetime();
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
			sortedMap.put(String.format("%010d", count) + "-" + impId, collect.get(impId));
		    }
		}
	    }
	} else {
	    count = 0;
	    for (Map.Entry<Object, List<String>> entry : loadCatAndImpId.entrySet()) {
		count++;
		for (String impId : entry.getValue()) {
		    if (collect.containsKey(impId)) {
			sortedMap.put(String.format("%010d", count) + "-" + impId, collect.get(impId));
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
    public JSONResponse advancedSearchExportExcel(AdvancedSearchForm searchForm, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) throws WorkflowException, IOException {
	List<AdvancedMetaSearchResult> findAll = advanceSearchViewDAO.getPlanByAdvancedSearchView(searchForm, pOffset, pLimit, pOrderBy);
	for (int i = 0; i < findAll.size(); i++) {
	    AdvancedMetaSearchResult advancedMetaSearchResult = findAll.get(i);
	    if (searchForm.getFunctionalPackages() != null && searchForm.getFunctionalPackages().size() > 0) {
		advancedMetaSearchResult.setFunctionalarea(objectCompareUnlikeRemove(advancedMetaSearchResult.getFunctionalarea(), searchForm.getFunctionalPackages()));
	    }
	    if (searchForm.getProgramName() != null) {
		if (searchForm.getExactSegment()) {
		    advancedMetaSearchResult.setProgramname(objectCompareUnlikeRemove(advancedMetaSearchResult.getProgramname(), searchForm.getProgramName()));
		}
	    }
	    if (advancedMetaSearchResult.getPlanstatus().equalsIgnoreCase(Constants.PlanStatus.APPROVED.name())) {
		advancedMetaSearchResult.setPlanstatus(Constants.PlanStatus.SUBMITTED.name());
	    } else if (advancedMetaSearchResult.getPlanstatus().equalsIgnoreCase(Constants.PlanStatus.SUBMITTED.name())) {
		advancedMetaSearchResult.setPlanstatus(Constants.PlanStatus.SUBMITTED.getDisplayName().toUpperCase());
	    } else if (advancedMetaSearchResult.getPlanstatus().equalsIgnoreCase(Constants.PlanStatus.DEV_MGR_APPROVED.name())) {
		advancedMetaSearchResult.setPlanstatus(Constants.PlanStatus.APPROVED.name());
	    }
	}

	Map<String, List<AdvancedMetaSearchResult>> collect = findAll.stream().collect(Collectors.groupingBy(T -> T.getPlanid() + T.getTargetsystem()));
	// ZTPFM-1449
	String complanyName = null;
	if (getWFConfig().getIsDeltaApp()) {
	    complanyName = "dl";
	} else {
	    complanyName = "tp";
	}
	if (searchForm.getProgramName() != null && !searchForm.getProgramName().isEmpty()) {
	    Collection<GitSearchResult> lCompanyResult = getjGITSearchUtils().SearchAllRepos(complanyName, searchForm.getProgramName(), false, Constants.PRODSearchType.PENDING_ONLY, new ArrayList<>());
	    Integer systems[] = new Integer[7];
	    int i = 0;
	    for (System sys : searchForm.getTargetSystems()) {
		systems[i] = sys.getId();
		i++;
	    }
	    List<System> lSystems = getSystemDAO().findByIds(systems);
	    List<String> systemList = lSystems.stream().map(sys -> "master_" + sys.getName().toLowerCase()).collect(Collectors.toList());

	    for (GitSearchResult lResult : lCompanyResult) {
		for (GitBranchSearchResult lBranch : lResult.getBranch()) {
		    if (lBranch.getRefLoadDate().after(searchForm.getStartDate()) && lBranch.getRefLoadDate().before(searchForm.getEndDate()) && systemList.contains(lBranch.getTargetSystem())) {
			String targetSystem[] = lBranch.getTargetSystem().split("_");
			AdvancedMetaSearchResult obj = new AdvancedMetaSearchResult();
			obj.setPlanid(lBranch.getRefPlan());
			obj.setTargetsystem(targetSystem[1].toUpperCase());
			obj.setProgramname(lResult.getProgramName());
			obj.setPlanstatus(lBranch.getRefStatus().toUpperCase());
			obj.setFunctionalarea(lBranch.getFuncArea());
			obj.setLoaddatetime(lBranch.getRefLoadDate());
			List<AdvancedMetaSearchResult> searchList = new ArrayList<AdvancedMetaSearchResult>();
			searchList.add(obj);
			collect.put(lBranch.getRefPlan() + targetSystem[1].toUpperCase(), searchList);
		    }
		}
	    }
	}

	Map<String, List<AdvancedMetaSearchResult>> collect1 = new LinkedHashMap<String, List<AdvancedMetaSearchResult>>();
	if (pOrderBy != null && !pOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> entrySet : pOrderBy.entrySet()) {
		collect1 = sortByLoadCat(collect, entrySet.getValue(), entrySet.getKey());
	    }
	} else {
	    collect1 = sortByLoadCat(collect, null, null);
	}
	JSONResponse lResponse = new JSONResponse();
	SearchExcelCreator creator = new SearchExcelCreator();
	creator.addSearchResult(collect1);
	try {
	    ByteArrayOutputStream lExcelStream = new ByteArrayOutputStream();
	    creator.getWorkBook().write(lExcelStream);
	    creator.getWorkBook().close();
	    lResponse.setData(lExcelStream.toByteArray());
	    lExcelStream.close();
	    lResponse.setMetaData("application/vnd.ms-excel");
	    lResponse.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error("Error in Excel Creation ", ex);
	    lResponse.setErrorMessage("Error in Downloading Report");
	    lResponse.setStatus(Boolean.TRUE);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getFuncAreaList(User pUser, Integer[] ids) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	Map<String, List<String>> lFuncMap = new HashMap();
	try {
	    List<System> lSystems = getSystemDAO().findByIds(ids);

	    List<AccessPermission> lRemovePermissions = new ArrayList<>();
	    lRemovePermissions.add(AccessPermission.EXCLUDE);
	    lRemovePermissions.add(AccessPermission.VIEW);
	    lRemovePermissions.add(AccessPermission.CLONE);

	    Map<String, AccessPermission> permissions = cacheClient.getAllRepoUsersMap().get(pUser.getId()).getPermissions();
	    permissions.values().removeAll(lRemovePermissions);

	    Set<String> lRepoCommonNames = new TreeSet<>();
	    for (String lRepoName : permissions.keySet()) {
		// TEMP FIX
		String lRepoName1 = FilenameUtils.removeExtension(FilenameUtils.getName(lRepoName)).toUpperCase();
		String lReturn1 = FilenameUtils.removeExtension(lRepoName).toUpperCase().replaceAll("\\d+$", "");
		String lReturn2 = FilenameUtils.removeExtension(lRepoName).toUpperCase();
		if (lRepoName1.startsWith("NONIBM_") && lRepoName.length() > 10) {
		    lRepoCommonNames.add(lReturn1);
		} else if (lRepoName.startsWith("NONIBM_") && lRepoName.length() <= 10) {
		    lRepoCommonNames.add(lReturn2);
		} else if (lRepoName.startsWith("IBM_") && lRepoName.length() > 7) {
		    lRepoCommonNames.add(lReturn1);
		} else if (lRepoName.startsWith("IBM_") && lRepoName.length() <= 7) {
		    lRepoCommonNames.add(lReturn2);
		} else if (lRepoName.startsWith("DERIVED_") && lRepoName.length() > 11) {
		    lRepoCommonNames.add(lReturn1);
		} else if (lRepoName.startsWith("DERIVED_") && lRepoName.length() <= 11) {
		    lRepoCommonNames.add(lReturn2);
		} else {
		    lRepoCommonNames.add(lReturn1);
		}
	    }

	    // Assumption: Branches Present in all Repos
	    Map<String, RepositoryView> lAllViews = cacheClient.getFilteredRepositoryMap().getAll(lRepoCommonNames);
	    List<RepositoryDetails> lRepoList = getRepoDetailsDAO().getDefaultFileCreateNoData();
	    for (System lSystem : lSystems) {
		Set<String> lFuncAreas = new HashSet<>();
		String lBranchName = "refs/heads/master_" + lSystem.getName().toLowerCase();
		lRepoList.forEach((value) -> {
		    lAllViews.entrySet().removeIf(val -> val.getValue().getRepository().getTrimmedName().equalsIgnoreCase(value.getTrimmedName()));
		});
		for (Map.Entry<String, RepositoryView> repo : lAllViews.entrySet()) {
		    if (repo.getValue().getRepository().getName().toLowerCase().contains("nonibm_") && repo.getValue().getRepository().getAvailableRefs().contains(lBranchName)) {
			String lFuncAreas1 = repo.getValue().getRepository().getFuncArea();
			String lFuncDesc = repo.getValue().getRepository().getDescription();
			lFuncAreas.add(lFuncDesc + " [" + lFuncAreas1 + "]");
		    }
		}
		lFuncMap.put(lSystem.getName(), lFuncAreas.stream().sorted().collect(Collectors.toList()));
	    }
	    lResponse.setData(lFuncMap);
	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("Error occurs in getting Package list from Git", ex);
	    throw new WorkflowException("Error occurs in getting package list", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getIBMFuncAreaList(User pUser, Integer[] ids) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    List<PutLevel> findAll = getPutLevelDAO().findBySystem(ids);
	    List<PutLevel> putList = new ArrayList<>();
	    findAll.forEach(put -> {
		if (put.getStatus().equals(Constants.PUTLevelOptions.PRODUCTION.name())) {
		    putList.add(put);
		} else if (Constants.getFuturePutLevel().contains(put.getStatus())) {
		    putList.add(put);
		}
	    });
	    List<RepositoryDetails> lAllRepoList = getRepoDetailsDAO().findAll();
	    List<RepositoryDetails> lRepoList = getRepoDetailsDAO().getDefaultFileCreateNoData();
	    Set<String> listofRepoName = lRepoList.stream().map(RepositoryDetails::getTrimmedName).collect(Collectors.toSet());
	    LOG.info("Not allow to create New file List : " + listofRepoName + " Size : " + lAllRepoList.size());
	    listofRepoName.forEach((repo) -> {
		lAllRepoList.removeIf(lRepo -> lRepo.getTrimmedName().toLowerCase().equalsIgnoreCase(repo.toLowerCase()));
		LOG.info(" After Removed Repo List Size :  " + lAllRepoList.size());
	    });
	    List<PutLevelUIForm> finalPutList = new ArrayList<>();
	    lAllRepoList.forEach((repo) -> {
		for (PutLevel lput : putList) {
		    PutLevelUIForm lputForm = new PutLevelUIForm();
		    if (lput.getScmUrl() != null && repo.getTrimmedName().toLowerCase().equalsIgnoreCase(removeExtension(lput.getScmUrl().toLowerCase()))) {
			LOG.info("Repo Name : " + repo.getTrimmedName().toLowerCase() + " Put Level URL : " + removeExtension(lput.getScmUrl().toLowerCase()));
			lputForm.setPutLevel(lput);
			lputForm.setPutNameDesc(repo.getRepoDescription() + " [" + lput.getPutLevel() + "]");
			finalPutList.add(lputForm);
		    }
		}
	    });
	    LOG.info("Final Put Level List : " + finalPutList.size());
	    lResponse.setData(finalPutList);
	    lResponse.setStatus(Boolean.TRUE);
	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("Error occurs in getting IBM putlevel List", ex);
	    throw new WorkflowException("Error occurs in getting IBM putlevel List", ex);
	}
	return lResponse;
    }

    // Remove extension from file name
    // Ignore file names starting with .
    public static String removeExtension(String fileName) {
	if (fileName.indexOf(".") > 0) {
	    return fileName.substring(0, fileName.lastIndexOf("."));
	} else {
	    return fileName;
	}

    }

    @Transactional
    public JSONResponse prodFileSearch(User lUser, String pImplId, String pFileName, boolean pending) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    Implementation lImplementation = getImplementationDAO().find(pImplId);
	    String lCompanyName = lImplementation.getPlanId().getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
	    Set<String> lAllowedRepos = getGitHelper().getUserAllowedReadAndAboveRepos(lUser.getId(), lCompanyName);
	    Set<String> lObsRepos = getGitHelper().getObsRepoList(lCompanyName);
	    lAllowedRepos.removeAll(lObsRepos);
	    Collection<GitSearchResult> lSearchResult = getjGITSearchUtils().SearchAllRepos(lCompanyName.toLowerCase(), pFileName.toLowerCase(), lImplementation.getPlanId().getMacroHeader(), (pending ? Constants.PRODSearchType.PENDING_ONLY : Constants.PRODSearchType.ONLINE_ONLY), lAllowedRepos);
	    // To validate put level details against Implementation put level
	    Collection<String> diffTargetSys = getGitHelper().validatePutLevelToAllowCheckout(pImplId, lSearchResult);

	    getGitHelper().searchFileResultFilter(lUser.getId(), lSearchResult, lCompanyName, null);

	    if (diffTargetSys != null && !diffTargetSys.isEmpty()) {
		String message = "Attn: IBM source artifacts cannot be checked out for " + diffTargetSys.stream().collect(Collectors.joining(",")) + " as default PUT level is overridden!";
		lResponse.setMetaData(message);
	    }
	    lResponse.setStatus(true);
	    lResponse.setData(lSearchResult);
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Error in listing files from Production ", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse nonProdFileSearch(User user, String implId, String fileName) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    Implementation lImp = getImplementationDAO().find(implId);
	    String lCompanyName = lImp.getPlanId().getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
	    Set<String> lUserAllowedRepos = getGitHelper().getUserAllowedReadAndAboveRepos(user.getId(), lCompanyName);
	    if (lUserAllowedRepos == null) {
		throw new WorkflowException("Unable to get search result, dont have access permission for any repositories.");
	    }

	    // Get Put levels for each systems
	    List<String> putLevelStatus = new ArrayList<>();
	    putLevelStatus.add(Constants.PUTLevelOptions.PRODUCTION.name());
	    HashMap<String, String> tarSysAndPutLevel = getGitHelper().getSystemBasedPutLevels(putLevelStatus);

	    Set<String> lRepoList = new HashSet<>();
	    for (String lRepo : lUserAllowedRepos) {
		lRepoList.add(getGitHelper().getSourceURLByRepoName(lRepo));
	    }
	    List<String> lDeactivatedPlanInProd = getImpPlanDAO().getListOfPlansDeactivatedInProd();

	    List<CheckoutSegments> lResult = getCheckoutSegmentsDAO().findByFileName(fileName.toLowerCase(), implId, new HashSet<>());
	    List<GitSearchResult> lReturnList = new ArrayList<>();
	    if (lResult != null && !lResult.isEmpty()) {
		Map<String, GitSearchResult> lFilteredList = new HashMap<>();
		Map<String, List> lFilteredSystemList = new HashMap<>();
		for (CheckoutSegments lSegment : lResult) {

		    // Only prod Put level based on each target system should be displayed for IBM.
		    if (lSegment.getFileType().equalsIgnoreCase(Constants.FILE_TYPE.IBM.name()) && !lSegment.getFuncArea().equals(tarSysAndPutLevel.get(lSegment.getTargetSystem()))) {
			continue;
		    }
		    GitSearchResult lKey = new GitSearchResult();
		    GitBranchSearchResult lValue = new GitBranchSearchResult();
		    BeanUtils.copyProperties(lKey, lSegment);
		    BeanUtils.copyProperties(lValue, lSegment);
		    lKey.addAdditionalInfo("planId", lSegment.getPlanId().getId());
		    lKey.addAdditionalInfo("planStatus", lSegment.getPlanId().getPlanStatus());
		    lKey.setFileNameWithHash(CheckoutUtils.getHexStringWithPlanSegment(lSegment));
		    lValue.addAdditionalInfo("planId", lSegment.getPlanId().getId());
		    lValue.setTargetSystem("master_" + lSegment.getTargetSystem().toLowerCase());
		    lValue.addAdditionalInfo("loadDateTime", "" + lSegment.getSystemLoad().getLoadDateTime());
		    lValue.setLoadDate(lSegment.getSystemLoad().getLoadDateTime());
		    if (lImp.getPlanId().getMacroHeader() && !(lSegment.getProgramName().endsWith(".mac") || lSegment.getProgramName().endsWith(".hpp") || lSegment.getProgramName().endsWith(".h") || lSegment.getProgramName().endsWith(".cpy") || lSegment.getProgramName().endsWith(".inc") || lSegment.getProgramName().endsWith(".incafs"))) {
			lValue.setIsCheckoutAllowed(Boolean.FALSE);
			lValue.setIsBranchSelected(Boolean.FALSE);
		    }

		    // If Repo is restricted, then shouldn't allow to checkout
		    if (lRepoList != null && !lRepoList.isEmpty() && !lRepoList.contains(lSegment.getSourceUrl())) {
			lValue.setIsCheckoutAllowed(Boolean.FALSE);
			lValue.setIsBranchSelected(Boolean.FALSE);
		    }

		    // 2126 - If segment in plan, plan status is READY FOR PRODUCTION DEPLOYMENT.
		    // Loadset of the plan is
		    // deactivated in Production
		    // then throw warning message to User about selection of this segment
		    lKey.addAdditionalInfo("isPlanDeactivatedInProd", "");
		    if (lDeactivatedPlanInProd != null && !lDeactivatedPlanInProd.isEmpty() && lDeactivatedPlanInProd.contains(lSegment.getPlanId().getId())) {
			String lWarnMsg = "Attention!! Segment(s) " + lSegment.getProgramName() + " being selected from Implementation Plan(s) " + lSegment.getPlanId().getId() + " is in deactivate status. You would like to proceed?";
			lKey.addAdditionalInfo("isPlanDeactivatedInProd", lWarnMsg);
		    }

		    if (lSegment.getSystemLoad().getLoadCategoryId() != null) {
			lValue.addAdditionalInfo("categoryName", lSegment.getSystemLoad().getLoadCategoryId().getName());
		    }
		    String idStringWithPlan = CheckoutUtils.getIdStringWithPlan(lSegment);
		    if (lFilteredList.containsKey(idStringWithPlan)) {
			if (!lFilteredSystemList.get(CheckoutUtils.getIdStringWithPlanSysCheck(lSegment)).contains(lSegment.getTargetSystem())) {
			    lFilteredList.get(idStringWithPlan).getTargetSystems().add(lValue.getTargetSystem());
			    lFilteredList.get(idStringWithPlan).getBranch().add(lValue);
			    lFilteredSystemList.get(CheckoutUtils.getIdStringWithPlanSysCheck(lSegment)).add(lSegment.getTargetSystem());
			}
		    } else {
			lKey.getTargetSystems().add(lValue.getTargetSystem());
			lKey.getBranch().add(lValue);
			lFilteredList.put(idStringWithPlan, lKey);
			List lSystemList = new ArrayList();
			lSystemList.add(lSegment.getTargetSystem());
			lFilteredSystemList.put(CheckoutUtils.getIdStringWithPlanSysCheck(lSegment), lSystemList);
			lReturnList.add(lKey);
		    }
		}
		if (!lReturnList.isEmpty()) {
		    getGitHelper().searchFileResultFilter(user.getId(), lReturnList, lCompanyName, null);
		    Collection<String> diffTargetSys = getGitHelper().validatePutLevelToAllowCheckout(implId, lReturnList);
		    if (diffTargetSys != null && !diffTargetSys.isEmpty()) {
			String message = "Attn: IBM source artifacts cannot be checked out for " + diffTargetSys.stream().collect(Collectors.joining(",")) + " as default PUT level is overridden!";
			lResponse.setMetaData(message);
		    }
		}

		lResponse.setStatus(Boolean.TRUE);
		lResponse.setData(lReturnList);
		lResponse.setCount(lReturnList.size());
	    } else {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("No Record Found");
	    }
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Error in listing files from Non-production", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse ibmVanillaFileSearch(User lUser, String implId, String fileName) {
	JSONResponse lResponse = new JSONResponse();
	Map<String, GitSearchResult> lFileSegmentList = new HashMap<>();
	try {
	    Implementation lImplId = getImplementationDAO().find(implId);
	    List<SystemLoad> lSystemLoadList = getSystemLoadDAO().findByImpPlan(lImplId.getPlanId());
	    List<System> lSystemListByPlatform = getSystemDAO().findByPlatform(lSystemLoadList.get(0).getSystemId().getPlatformId());
	    for (System lSystem : lSystemListByPlatform) {
		// if (lDNSList.contains(lSystem.getIpaddress())) {
		// if (lIPSegmentList.isEmpty()) {
		// continue;
		// } else {
		// List<GitSearchResult> lSegmentNameList =
		// lIPSegmentList.get(lSystem.getIpaddress());
		// for (GitSearchResult lSegmentName : lSegmentNameList) {
		// lSegmentName.getTargetSystems().add(lSystem.getName());
		// }
		// continue;
		// }
		// }
		// lDNSList.add(lSystem.getIpaddress());
		// TODO: convert to SHELL
		String lCommand = "readlink -f " + getWFConfig().getIbmVanillaDirectory() + " | find $(awk '{print $1}') -type f -iname " + fileName.toLowerCase() + "*";

		lResponse = getsSHClientUtils().executeCommand(lSystem, lCommand);
		if (lResponse.getStatus()) {
		    String lCommandData = (String) lResponse.getData();
		    String[] lResultSegmentList = lCommandData.split(java.lang.System.lineSeparator());
		    List<GitSearchResult> lSegmentNameList = new ArrayList<>();
		    for (String lSegment : lResultSegmentList) {
			if (lSegment != null && lSegment.length() != 0) {
			    GitBranchSearchResult lRes = new GitBranchSearchResult();
			    lRes.setTargetSystem(lSystem.getName());
			    PutLevel lPutLevel = getPutLevelDAO().find(lSystem.getDefalutPutLevel());
			    lRes.setFuncArea(lPutLevel != null ? lPutLevel.getScmUrl() : "");
			    if (lFileSegmentList.get(lSegment) != null) {
				lFileSegmentList.get(lSegment).getTargetSystems().add(lSystem.getName());
				lFileSegmentList.get(lSegment).getBranch().add(lRes);
			    } else {
				GitSearchResult lSegmentName = new GitSearchResult();
				lSegmentName.setFileName(lSegment);
				lSegmentName.setProgramName(FilenameUtils.getName(lSegment));
				lSegmentName.getTargetSystems().add(lSystem.getName());
				lSegmentName.setFileNameWithHash(DigestUtils.md5Hex(lSegment));
				lSegmentNameList.add(lSegmentName);
				lSegmentName.getBranch().add(lRes);
				lFileSegmentList.put(lSegment, lSegmentName);

			    }
			}
		    }

		}
	    }
	    if (lFileSegmentList.values().isEmpty()) {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage(" Searched file is not present in the System!!!");
		return lResponse;
	    }
	    List<GitSearchResult> lReturn = new ArrayList<>(lFileSegmentList.values());
	    Collections.sort(lReturn, lReturn.get(0));
	    Collection<String> diffTargetSys = getGitHelper().validateIBMVanillaToAllowCheckout(implId, lReturn);
	    if (diffTargetSys != null && !diffTargetSys.isEmpty()) {
		String message = "Attn: IBM source artifacts cannot be checked out for " + diffTargetSys.stream().collect(Collectors.joining(",")) + " as default PUT level is overridden!";
		lResponse.setMetaData(message);
	    }
	    lResponse.setCount(lReturn.size());
	    lResponse.setData(lReturn);
	    lResponse.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Error in listing files from IBM", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse prodMigNonIBMSearchFile(User lUser, String pFileName, String pSearchType) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    LOG.info("PROFILE NAME :" + getWFConfig().getProfileName());
	    List<String> lCompanyList = new ArrayList<>();
	    if (getWFConfig().getIsTravelportApp()) {
		lCompanyList.add("tp");
	    }
	    if (getWFConfig().getIsDeltaApp()) {
		lCompanyList.add("dl");
	    }
	    List<GitSearchResult> lSearchResult = new ArrayList<>();
	    for (String lCompanyName : lCompanyList) {
		Set<String> lAllowedRepos = getGitHelper().getUserAllowedReadAndAboveRepos(lUser.getId(), lCompanyName);
		Collection<GitSearchResult> lCompanyResult = getjGITSearchUtils().SearchAllRepos(lCompanyName.toLowerCase(), pFileName.toLowerCase(), false, Constants.PRODSearchType.ONLINE_ONLY, lAllowedRepos);
		getGitHelper().searchFileResultFilter(lUser.getId(), lCompanyResult, lCompanyName, pSearchType);
		lSearchResult.addAll(lCompanyResult);
	    }

	    lResponse.setStatus(true);
	    lResponse.setData(lSearchResult);
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Error in listing files from Production ", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse prodMigNonIBMObsSearchFile(User lUser, String pFileName, String pSearchType) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    List<String> lCompanyList = new ArrayList<>();
	    if (getWFConfig().getIsTravelportApp()) {
		lCompanyList.add("tp");
	    }
	    if (getWFConfig().getIsDeltaApp()) {
		lCompanyList.add("dl");
	    }
	    List<GitSearchResult> lSearchResult = new ArrayList<>();
	    for (String lCompanyName : lCompanyList) {
		String lRepoName = (getGITConfig().getGitProdPath() + lCompanyName + "/nonibm/nonibm_obs").toUpperCase();
		Set<String> lAllowedRepos = new HashSet();
		List<Repository> lRepoList = getCacheClient().getAllRepositoryMap().get(lRepoName);
		if (lRepoList == null) {
		    continue;
		}
		for (Repository lRepo : lRepoList) {
		    lAllowedRepos.add(lRepo.getName());
		}
		Collection<GitSearchResult> lCompanyResult = getjGITSearchUtils().SearchAllRepos(lCompanyName.toLowerCase(), pFileName.toLowerCase(), false, Constants.PRODSearchType.ONLINE_ONLY, lAllowedRepos);
		getGitHelper().searchFileResultFilter(lUser.getId(), lCompanyResult, lCompanyName, pSearchType);
		lSearchResult.addAll(lCompanyResult);
	    }

	    lResponse.setStatus(true);
	    lResponse.setData(lSearchResult);
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Error in listing files from Production ", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse migrateNonIbmFile(User lUser, List<GitSearchResult> pSearchResults) {
	List<String> programNames = new ArrayList<>();
	try {
	    JSONResponse lResponse = new JSONResponse();
	    List<String> progNames = pSearchResults.stream().map(t -> t.getProgramName()).collect(Collectors.toList());
	    for (String segName : progNames) {
		if (getCacheClient().getInprogressMoveArtifactMap().containsKey(segName)) {
		    lResponse.setStatus(Boolean.FALSE);
		    lResponse.setErrorMessage("Previous Move artifact operation for file : " + segName + " is In-Progress, please try after completion of previous operation");
		    return lResponse;
		}
		getCacheClient().getInprogressMoveArtifactMap().put(segName, lUser);
		programNames.add(segName);
		LOG.info("ADDED " + segName + " to In-progress queue : " + getCacheClient().getInprogressMoveArtifactMap().containsKey(segName));
	    }

	    wsserver.sendMessage(Constants.Channels.MOVE_SOURCE_ARTIFACT, lUser, "Started");
	    Map<String, List<CheckoutSegments>> lSegmentsMap = new HashMap<>();
	    Map<String, GitBranchSearchResult> lTempGitSearchSegments = new HashMap<>();

	    for (GitSearchResult lSearchResult : pSearchResults) {
		List<GitBranchSearchResult> lBranches = lSearchResult.getBranch();
		for (GitBranchSearchResult lBranch : lBranches) {
		    if (lBranch.getIsBranchSelected()) {
			lBranch.setIsCheckedout(Boolean.TRUE);
			CheckoutSegments lSegment = new CheckoutSegments();
			BeanUtils.copyProperties(lSegment, lBranch);
			BeanUtils.copyProperties(lSegment, lSearchResult);
			String targetSystem = lSegment.getTargetSystem().replaceAll(Constants.BRANCH_MASTER, "").toUpperCase();
			lSegment.setTargetSystem(targetSystem);
			String idString = CheckoutUtils.getIdString(lSegment);
			lTempGitSearchSegments.put(idString, lBranch);
			if (lSegmentsMap.containsKey(targetSystem)) {
			    List<CheckoutSegments> lSegments = lSegmentsMap.get(targetSystem);
			    lSegments.add(lSegment);
			    lSegmentsMap.replace(targetSystem, lSegments);
			} else {
			    List<CheckoutSegments> lSegments = new ArrayList();
			    lSegments.add(lSegment);
			    lSegmentsMap.put(targetSystem, lSegments);
			}
		    }
		}
	    }
	    for (Map.Entry<String, List<CheckoutSegments>> lSysSegment : lSegmentsMap.entrySet()) {
		System lSystem = getSystemDAO().findByName(lSysSegment.getKey());
		if (lSystem == null) {
		    for (CheckoutSegments lSegment : lSysSegment.getValue()) {
			lTempGitSearchSegments.get(CheckoutUtils.getIdString(lSegment)).setIsCheckedout(Boolean.FALSE);
		    }
		    continue;
		}
		String companyName = lSystem.getPlatformId().getNickName();
		Map<String, String> lRepoList = getGitHelper().getRepositoryList(companyName, "nonibm");
		if (lRepoList.isEmpty()) {
		    for (CheckoutSegments lSegment : lSysSegment.getValue()) {
			lTempGitSearchSegments.get(CheckoutUtils.getIdString(lSegment)).setIsCheckedout(Boolean.FALSE);
		    }
		    continue;
		}
		Map<String, List<String>> lFuncWiseSegmentMap = new HashMap();
		Map<String, List<CheckoutSegments>> lFuncWiseCSegment = new HashMap();

		for (CheckoutSegments lSegment : lSysSegment.getValue()) {
		    String lFromFuncArea = getGitHelper().getRepositoryNameBySourceURL(lSegment.getSourceUrl());
		    String lToFuncArea = lRepoList.get(lTempGitSearchSegments.get(CheckoutUtils.getIdString(lSegment)).getTargetFuncArea());
		    if ((lFromFuncArea != null && !lFromFuncArea.isEmpty()) && (lToFuncArea != null && !lToFuncArea.isEmpty())) {
			String lCommand = lSystem.getName().toLowerCase() + " " + lFromFuncArea + " " + lToFuncArea;
			if (lFuncWiseSegmentMap.containsKey(lCommand)) {
			    List<String> lSegments = lFuncWiseSegmentMap.get(lCommand);
			    lSegments.add(lSegment.getFileName());
			    lFuncWiseSegmentMap.replace(lCommand, lSegments);

			    List<CheckoutSegments> lTemSegmentList = lFuncWiseCSegment.get(lCommand);
			    lTemSegmentList.add(lSegment);
			    lFuncWiseCSegment.replace(lCommand, lTemSegmentList);
			} else {
			    List<String> lSegments = new ArrayList();
			    lSegments.add(lSegment.getFileName());
			    lFuncWiseSegmentMap.put(lCommand, lSegments);

			    List<CheckoutSegments> lTemSegmentList = new ArrayList();
			    lTemSegmentList.add(lSegment);
			    lFuncWiseCSegment.put(lCommand, lTemSegmentList);
			}
		    }
		}

		for (Map.Entry<String, List<String>> lFuncWiseSegment : lFuncWiseSegmentMap.entrySet()) {
		    JSONResponse lCmdResponse = new JSONResponse();
		    String lCommand = Constants.SystemScripts.MIGRATE_NON_IBM.getScript() + lFuncWiseSegment.getKey() + " " + String.join(",", lFuncWiseSegment.getValue()) + " " + lUser.getId();
		    LOG.info(lCommand);
		    lCmdResponse = getsSHClientUtils().executeCommand(lSystem, lCommand);
		    if (!lCmdResponse.getStatus()) {
			String[] lErrorFiles = lCmdResponse.getDisplayErrorMessage().split(",");
			for (String lErrorFile : lErrorFiles) {
			    for (CheckoutSegments lSegment : lFuncWiseCSegment.get(lFuncWiseSegment.getKey())) {
				// Need to confirm the error file name has prog name or File Name
				if (lSegment.getFileName().contains(lErrorFile)) {
				    lTempGitSearchSegments.get(CheckoutUtils.getIdString(lSegment)).setIsCheckedout(Boolean.FALSE);
				    continue;
				}
			    }
			}
		    }

		}
	    }
	    wsserver.sendMessage(Constants.Channels.MOVE_SOURCE_ARTIFACT, lUser, "Completed");
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(pSearchResults);
	    return lResponse;
	} catch (Exception ex) {
	    LOG.error("unable to checkout", ex);
	    throw new WorkflowException("Unable to checkout the segments ", ex);
	} finally {
	    if (programNames != null && !programNames.isEmpty()) {
		for (String progName : programNames) {
		    getCacheClient().getInprogressMoveArtifactMap().remove(progName);
		    LOG.info("Removed " + progName + " from In-progress queue : " + !getCacheClient().getInprogressMoveArtifactMap().containsKey(progName));
		}
	    }
	}
    }

    @Transactional
    public JSONResponse obsRepoInfo(User pUser) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.FALSE);
	List<String> lCompanyList = new ArrayList();
	if (getWFConfig().getIsTravelportApp()) {
	    lCompanyList.add("tp");
	}
	if (getWFConfig().getIsDeltaApp()) {
	    lCompanyList.add("dl");
	}

	Map<String, RepositoryView> lRepos = getCacheClient().getFilteredRepositoryMap();
	for (Map.Entry<String, RepositoryView> lRepo : lRepos.entrySet()) {
	    for (String lCompany : lCompanyList) {
		if (lRepo.getKey().equalsIgnoreCase(getGITConfig().getGitProdPath() + lCompany + "/nonibm/nonibm_obs")) {
		    lResponse.setStatus(Boolean.TRUE);
		    lResponse.setData(lRepo.getValue().getRepository());
		}
	    }
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getAllFuncAreaList(User pUser, Integer[] ids) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	Map<String, Set<String>> lFuncMap = new HashMap();
	try {
	    List<System> lSystems = getSystemDAO().findByIds(ids);

	    // Assumption: Branches Present in all Repos
	    Map<String, RepositoryView> lAllViews = getCacheClient().getFilteredRepositoryMap();

	    lSystems.forEach((lSystem) -> {
		String lBranchName = "refs/heads/master_" + lSystem.getName().toLowerCase();
		Set<String> lFuncAreas = lAllViews.values().stream().filter(x -> (!x.getRepository().getName().toLowerCase().contains("derived_") && x.getRepository().getAvailableRefs().contains(lBranchName))).map(e -> e.getRepository().getFuncArea()).collect(Collectors.toSet());
		LOG.info("Functional Area Count for " + lSystem.getName() + " size " + lFuncAreas.size());
		lFuncMap.put(lSystem.getName(), lFuncAreas);
	    });
	    lResponse.setData(lFuncMap);
	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("Error occurs in getting Package list from Git", ex);
	    throw new WorkflowException("Error occurs in getting package list", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getPlansPassedRegTesting(User lUser, boolean isFunctional, Constants.LoaderTypes loaderTypes, String pFilter, Integer offset, Integer limit, LinkedHashMap<String, String> lOrderBy) {
	// (QA / LEAD / TSS Delta), TSS Travelport Separate
	User pUser = lUser.getCurrentOrDelagateUser();
	JSONResponse lResponse = new JSONResponse();
	List<String> status = new ArrayList();
	List<String> lQAStatusFilter = new ArrayList();
	BUILD_TYPE build_type;
	List<ImpPlan> impPlanList = new ArrayList();
	Long lcount;
	try {
	    status = new ArrayList(Constants.PlanStatus.getPassedRegressionAndAboveStatus().keySet());
	    build_type = BUILD_TYPE.STG_LOAD;
	    // ZTPFM-1647 : Added BYPASSED_FUNCTIONAL_TESTING and NONE QA testing status to
	    // consider in the Passed
	    // regression testing page
	    lQAStatusFilter.add(Constants.SYSTEM_QA_TESTING_STATUS.NONE.name());
	    lQAStatusFilter.add(Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name());

	    impPlanList = getImpPlanDAO().findQAPlanList(status, pFilter, build_type, loaderTypes, lQAStatusFilter, offset, limit, lOrderBy);
	    lcount = getImpPlanDAO().countByQAStatusList(status, pFilter, build_type, loaderTypes, lQAStatusFilter);
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setCount(lcount);
	    lResponse.setData(impPlanList);
	} catch (Exception ex) {
	    LOG.error("Unable to fetch Implementation Plan List for Deployment", ex);
	    throw new WorkflowException("Unable to fetch Implementation Plan List for Deployment!", ex);
	}
	return lResponse;
    }

    private void sendMailNotificationADLAndDev(SystemLoadActions lLoad) {

	// Getting Developer Id from Implementation
	List<String> lDevAndADLlist = new ArrayList<>();
	List<Implementation> lImplementationList = getImplementationDAO().findByImpPlan(lLoad.getPlanId().getId());
	lDevAndADLlist.add(lLoad.getPlanId().getLeadId());
	for (Implementation lImplementation : lImplementationList) {
	    lDevAndADLlist.add(lImplementation.getDevId());
	}

	// Send Mail Notification to ADL and Developer
	PreProdLoadsetActivationMail lPreProdLoadsetActivationMail = (PreProdLoadsetActivationMail) mailMessageFactory.getTemplate(PreProdLoadsetActivationMail.class);
	lPreProdLoadsetActivationMail.setPlanId(lLoad.getPlanId().getId());
	lPreProdLoadsetActivationMail.setActivationDateTime(new Date());
	lPreProdLoadsetActivationMail.setLoadsetName(lLoad.getSystemLoadId().getLoadSetName());
	lPreProdLoadsetActivationMail.setPreProdSystemName(lLoad.getVparId().getName());
	lPreProdLoadsetActivationMail.setAction(lLoad.getStatus());
	lDevAndADLlist.stream().forEach(t -> lPreProdLoadsetActivationMail.addToAddressUserId(t, Constants.MailSenderRole.LEAD));
	mailMessageFactory.push(lPreProdLoadsetActivationMail);
    }

    private void sendMailNotificationADLandQATester(SystemLoadActions lLoad, User pUser) {
	// Collecting Lead,QATester,QADeployedLead from the implementation
	Set<String> lQaTesterAndADLlist = new HashSet<>();
	lQaTesterAndADLlist.add(lLoad.getPlanId().getLeadId());
	lQaTesterAndADLlist.add(pUser.getId());
	Boolean tssDeploy = lLoad.getVparId().getTssDeploy();
	List<SystemLoad> lSystemLoad = getSystemLoadDAO().findByImpPlan(lLoad.getPlanId());
	for (SystemLoad pSystemLoad : lSystemLoad) {
	    if (pSystemLoad.getQaFunctionalTesters() != null) {
		List<String> qaFunctionalTesterList = Arrays.asList(pSystemLoad.getQaFunctionalTesters().split(","));
		for (String qaFunctionalTester : qaFunctionalTesterList) {
		    lQaTesterAndADLlist.add(qaFunctionalTester);
		}
	    }
	}
	// Send Mail Notification to ADL and QATesters
	QALoadsetActivationMail qaLoadsetActivationMail = (QALoadsetActivationMail) mailMessageFactory.getTemplate(QALoadsetActivationMail.class);
	qaLoadsetActivationMail.setPlanId(lLoad.getPlanId().getId());
	qaLoadsetActivationMail.setLoadSetName(lLoad.getSystemLoadId().getLoadSetName());
	qaLoadsetActivationMail.setVparsName(lLoad.getVparId().getName());
	qaLoadsetActivationMail.setAction(lLoad.getStatus());
	qaLoadsetActivationMail.setUserDetails(pUser);
	qaLoadsetActivationMail.setEnv(lLoad.getVparId().getType().contains(Constants.VPARSEnvironment.QA_REGRESSION.name()) ? "Regression" : "Functional");
	lQaTesterAndADLlist.stream().forEach(t -> qaLoadsetActivationMail.addToAddressUserId(t, Constants.MailSenderRole.QA_FUNCTIONAL));
	qaLoadsetActivationMail.setTssdeploy(tssDeploy);
	mailMessageFactory.push(qaLoadsetActivationMail);
    }

    @Transactional
    public JSONResponse getSegmentBasedActivity(User lUser, SegmentSearchForm segmentSearchForm, Integer offset, Integer limit, LinkedHashMap<String, String> lOrderBy) {
	JSONResponse lResponse = new JSONResponse();

	List<SegmentBasedActionDetail> allSegmentsList = null;
	allSegmentsList = impPlanDAO.getAllDeploymentActivities(segmentSearchForm, offset, limit, lOrderBy, false, false);
	if (segmentSearchForm.getEnvironment().equalsIgnoreCase("Pre-Production")) {
	    allSegmentsList.forEach(plan -> {
		plan.setEnvironment("Pre-Production");
	    });
	} else if (segmentSearchForm.getEnvironment().equalsIgnoreCase("Production")) {
	    allSegmentsList.forEach(plan -> {
		plan.setEnvironment("Production");
	    });
	}
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(allSegmentsList);
	lResponse.setCount(impPlanDAO.getAllDeploymentActivities(segmentSearchForm, 0, 0, null, false, true).size());
	return lResponse;
    }

    @Transactional
    public JSONResponse getDeploymentActivitiesInExcel(User lUser, SegmentSearchForm segmentSearchForm) {
	List<SegmentBasedActionDetail> allSegmentsList = null;
	allSegmentsList = impPlanDAO.getAllDeploymentActivities(segmentSearchForm, 0, 0, null, true, true);
	if (segmentSearchForm.getEnvironment().equalsIgnoreCase("Pre-Production")) {
	    allSegmentsList.forEach(plan -> {
		plan.setEnvironment("Pre-Production");
	    });
	} else if (segmentSearchForm.getEnvironment().equalsIgnoreCase("Production")) {
	    allSegmentsList.forEach(plan -> {
		plan.setEnvironment("Production");
	    });
	}
	return deploymentActivityExcelExport.generateDeploymentActivitiesInExcel(allSegmentsList);
    }

    private String objectCompareUnlikeRemove(String functionalarea, List<String> functionalPackages) {
	StringBuffer buffer = new StringBuffer("");
	String functionalAreaList[] = functionalarea.split(",");
	if (functionalAreaList.length > 0) {
	    String prefix = "";
	    for (int i = 0; i < functionalAreaList.length; i++) {
		String string = functionalAreaList[i].trim();
		for (int j = 0; j < functionalPackages.size(); j++) {
		    String get = functionalPackages.get(j).trim();
		    if (get.equalsIgnoreCase(string)) {
			buffer.append(prefix);
			prefix = ",";
			buffer.append(get);
		    }
		}
	    }
	} else {
	    buffer.append(functionalarea);
	}
	return buffer.toString();
    }

    private String objectCompareUnlikeRemove(String programmeNameList, String programmeName) {
	StringBuffer buffer = new StringBuffer("");
	String functionalAreaList[] = programmeNameList.split(",");
	if (functionalAreaList.length > 0) {
	    String prefix = "";
	    for (int i = 0; i < functionalAreaList.length; i++) {
		String string = functionalAreaList[i].trim();

		if (string.contains(programmeName.trim())) {
		    buffer.append(prefix);
		    prefix = ", ";
		    buffer.append(string);
		}
	    }
	} else {
	    buffer.append(programmeNameList);
	}

	return buffer.toString();
    }

    private SortedSet<String> checkDeptPlanCurrentStatus(SystemLoadActions systemLoadActions, String status) {
	SortedSet<String> lSet = new TreeSet<String>();
	System lSystem = getSystemDAO().find(systemLoadActions.getSystemId().getId());
	List<String> split = Arrays.asList(systemLoadActions.getPlanId().getRelatedPlans().split(","));
	if (status.equalsIgnoreCase("Load")) {
	    if (!split.isEmpty()) {
		List<Object[]> relatedPlanDetails = getImpPlanDAO().getAllRelatedPlanDetail(systemLoadActions.getPlanId().getId(), split);
		for (Object[] plan : relatedPlanDetails) {
		    String planStatus = plan[1].toString();
		    String lPlanId = plan[0].toString().split("/")[0].toUpperCase();
		    String lSystemName = plan[0].toString().split("/")[1].toUpperCase();

		    ImpPlan lImpPlan = getImpPlanDAO().find(lPlanId);
		    if (lImpPlan.getMacroHeader()) {
			continue;
		    }
		    if (lSystem.getName().equalsIgnoreCase(lSystemName) && Constants.PlanStatus.getApprovedToPreProd().containsKey(planStatus)) {
			List<SystemLoadActions> lPreProdList = getSystemLoadActionsDAO().findByPlanSystemEnv(new ImpPlan(lPlanId), systemLoadActions.getSystemId(), VPARSEnvironment.PRE_PROD);
			if (lPreProdList == null || lPreProdList.isEmpty()) {
			    lSet.add(lPlanId);
			} else {
			    for (SystemLoadActions lPreProd : lPreProdList) {
				if (!Constants.LOAD_SET_STATUS.getLoadAndAbove().contains(lPreProd.getStatus())) {
				    lSet.add(lPlanId);
				}
			    }
			}
		    }
		}
	    }

	    List<Object[]> segmentRelatedPlans = getImpPlanDAO().getYodaPreProdRelatedPlans(systemLoadActions.getPlanId().getId());
	    for (Object[] plan : segmentRelatedPlans) {
		String planStatus = plan[1] != null ? plan[1].toString() : null;
		String lSystemName = plan[0].toString().split("/")[1].toUpperCase();
		String planId = plan[5].toString();

		ImpPlan lImpPlan = getImpPlanDAO().find(planId);
		if (lImpPlan.getMacroHeader()) {
		    continue;
		}
		if (lSystem.getName().equalsIgnoreCase(lSystemName) && Constants.PlanStatus.getApprovedToPreProd().containsKey(planStatus)) {
		    List<SystemLoadActions> lPreProdList = getSystemLoadActionsDAO().findByPlanSystemEnv(new ImpPlan(planId), systemLoadActions.getSystemId(), VPARSEnvironment.PRE_PROD);
		    if (lPreProdList == null || lPreProdList.isEmpty()) {
			lSet.add(planId);
		    } else {
			for (SystemLoadActions lPreProd : lPreProdList) {
			    if (!Constants.LOAD_SET_STATUS.getLoadAndAbove().contains(lPreProd.getStatus())) {
				lSet.add(planId);
			    }
			}
		    }
		}
	    }
	} else if (status.equalsIgnoreCase("Activate")) {
	    // ZTPFM-1816 Dependent check
	    if (!split.isEmpty()) {
		List<Object[]> relatedPlanDetails = getImpPlanDAO().getAllRelatedPlanDetail(systemLoadActions.getPlanId().getId(), split);
		for (Object[] plan : relatedPlanDetails) {
		    String planStatus = plan[1].toString();
		    String lPlanId = plan[0].toString().split("/")[0].toUpperCase();
		    String lSystemName = plan[0].toString().split("/")[1].toUpperCase();

		    ImpPlan lImpPlan = getImpPlanDAO().find(lPlanId);
		    if (lImpPlan.getMacroHeader()) {
			continue;
		    }
		    if (lSystem.getName().equalsIgnoreCase(lSystemName) && Constants.PlanStatus.getApprovedToPreProd().containsKey(planStatus)) {
			List<SystemLoadActions> lPreProdList = getSystemLoadActionsDAO().findByPlanSystemEnv(new ImpPlan(lPlanId), systemLoadActions.getSystemId(), VPARSEnvironment.PRE_PROD);
			if (lPreProdList == null || lPreProdList.isEmpty()) {
			    lSet.add(lPlanId);
			} else {
			    for (SystemLoadActions lPreProd : lPreProdList) {
				if (!Constants.LOAD_SET_STATUS.getActivateAndAbove().contains(lPreProd.getStatus())) {
				    lSet.add(lPlanId);
				}
			    }
			}
		    }
		}
	    }

	    List<Object[]> segmentRelatedPlans = getImpPlanDAO().getYodaPreProdRelatedPlans(systemLoadActions.getPlanId().getId());
	    for (Object[] plan : segmentRelatedPlans) {
		String planStatus = plan[1] != null ? plan[1].toString() : null;
		String lSystemName = plan[0].toString().split("/")[1].toUpperCase();
		String planId = plan[5].toString();

		ImpPlan lImpPlan = getImpPlanDAO().find(planId);
		if (lImpPlan.getMacroHeader()) {
		    continue;
		}
		if (lSystem.getName().equalsIgnoreCase(lSystemName) && Constants.PlanStatus.getApprovedToPreProd().containsKey(planStatus)) {
		    List<SystemLoadActions> lPreProdList = getSystemLoadActionsDAO().findByPlanSystemEnv(new ImpPlan(planId), systemLoadActions.getSystemId(), VPARSEnvironment.PRE_PROD);
		    if (lPreProdList == null || lPreProdList.isEmpty()) {
			lSet.add(planId);
		    } else {
			for (SystemLoadActions lPreProd : lPreProdList) {
			    if (!Constants.LOAD_SET_STATUS.getActivateAndAbove().contains(lPreProd.getStatus())) {
				lSet.add(planId);
			    }
			}
		    }
		}
	    }
	} else if (status.equalsIgnoreCase("Deactivate")) {
	    List<Object[]> segmentRelatedPlans = getImpPlanDAO().getYodaPostSegmentRelatedPlansPreProdLoad(systemLoadActions.getPlanId().getId());
	    for (Object[] plan : segmentRelatedPlans) {
		String planStatus = plan[1].toString();
		String lSystemName = plan[0].toString().split("/")[1].toUpperCase();
		String planId = plan[2].toString();

		if (lSystemName.equalsIgnoreCase(lSystem.getName()) && Constants.PlanStatus.getApprovedToPreProd().containsKey(planStatus)) {
		    List<SystemLoadActions> lPreProdList = getSystemLoadActionsDAO().findByPlanSystemEnv(new ImpPlan(planId), systemLoadActions.getSystemId(), VPARSEnvironment.PRE_PROD);
		    if (lPreProdList != null) {
			lPreProdList.stream().filter(lPreProd -> !Constants.LOAD_SET_STATUS.getDeactivateAndAbove().contains(lPreProd.getStatus())).forEach(lPreProd -> lSet.add(lPreProd.getPlanId().getId()));
		    }
		}
	    }
	} else if (status.equalsIgnoreCase("Deleted")) {
	    List<Object[]> segmentRelatedPlans = getImpPlanDAO().getYodaPostSegmentRelatedPlansPreProdLoad(systemLoadActions.getPlanId().getId());
	    for (Object[] plan : segmentRelatedPlans) {
		String planStatus = plan[1].toString();
		String lSystemName = plan[0].toString().split("/")[1].toUpperCase();
		String planId = plan[2].toString();

		if (lSystemName.equalsIgnoreCase(lSystem.getName()) && Constants.PlanStatus.getApprovedToPreProd().containsKey(planStatus)) {
		    List<SystemLoadActions> lPreProdList = getSystemLoadActionsDAO().findByPlanSystemEnv(new ImpPlan(planId), systemLoadActions.getSystemId(), VPARSEnvironment.PRE_PROD);
		    if (lPreProdList != null) {
			lPreProdList.stream().filter(lPreProd -> !Constants.LOAD_SET_STATUS.getDeleteAndAbove().contains(lPreProd.getStatus())).forEach(lPreProd -> lSet.add(lPreProd.getPlanId().getId()));
		    }
		}
	    }
	}
	return lSet;

    }

    @Transactional
    public JSONResponse getHistoricalVersionsBySourceArtifact(SourceArtifactSearchForm searchForm, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) throws WorkflowException, IOException {
	JSONResponse lResponse = new JSONResponse();
	List<SourceArtifactSearchResult> sourceArtifactList = getImpPlanDAO().getHistoricalVersionsBySourceArtifact(searchForm, pOffset, pLimit, pOrderBy);

	Set<String> SystemAndFuncBasedFiles = new HashSet();
	sourceArtifactList.stream().filter(np -> np.getCheckoutrefstatus() == null || np.getCheckoutrefstatus().isEmpty() || np.getCheckoutrefstatus().equalsIgnoreCase("newfile") || np.getCheckoutrefstatus().equalsIgnoreCase("New File") || Constants.PlanStatus.getApprovedStatusMap().keySet().contains(np.getPlanstatus())).forEach(nonProd -> {
	    SystemAndFuncBasedFiles.add(nonProd.getFilename() + "-" + nonProd.getTargetsystem() + "-" + nonProd.getFuncpackage());
	    if (Constants.PlanStatus.getBeforeApprovedStatus().keySet().contains(nonProd.getPlanstatus()) && (nonProd.getCheckoutrefstatus() == null || nonProd.getCheckoutrefstatus().isEmpty() || nonProd.getCheckoutrefstatus().equalsIgnoreCase("newfile") || nonProd.getCheckoutrefstatus().equalsIgnoreCase("New File"))) {
		nonProd.setPlanstatus("New File");
	    }
	});

	List<SourceArtifactSearchResult> finalSourceArtifactList = new ArrayList<>();
	sourceArtifactList.forEach(srcArt -> {
	    boolean artifactCanBeAdded = true;
	    if (srcArt.getCheckoutrefstatus() != null && !srcArt.getCheckoutrefstatus().isEmpty() && srcArt.getCheckoutrefstatus().equalsIgnoreCase("prod") && srcArt.getPlanstatus().equalsIgnoreCase("New File") && SystemAndFuncBasedFiles != null && !SystemAndFuncBasedFiles.isEmpty() && SystemAndFuncBasedFiles.contains(srcArt.getFilename() + "-" + srcArt.getTargetsystem() + "-" + srcArt.getFuncpackage())) {
		artifactCanBeAdded = false;
	    }

	    if (artifactCanBeAdded) {
		finalSourceArtifactList.add(srcArt);
	    }
	});

	gitHelper.getSorceArtifactLink(finalSourceArtifactList);
	gitHelper.getListingUrlLink(finalSourceArtifactList);
	gitHelper.updateHighlightGroupFlag(finalSourceArtifactList);
	gitHelper.updateOnlineVersions(finalSourceArtifactList);

	List<SourceArtifactSearchResult> finalList = new ArrayList<>();
	int startVal = (pOffset * pLimit);
	int endVal = (pOffset * pLimit) + pLimit;
	for (int i = startVal; i < endVal; i++) {
	    if (i < finalSourceArtifactList.size() && finalSourceArtifactList.get(i) != null) {

		finalList.add(finalSourceArtifactList.get(i));
	    }
	    if (i >= finalSourceArtifactList.size()) {
		continue;
	    }
	}
	// ZTPFM-2697 Update
	getCommonHelper().updateFuncAreaWithDesc(finalList);
	// long sourceArtifactCount =
	// getImpPlanDAO().getCountHistoricalVersionsBySourceArtifact(searchForm);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setCount(finalSourceArtifactList.size());
	lResponse.setData(finalList);
	return lResponse;
    }

    @Transactional
    public JSONResponse getSegmentRepoDetails(RepoSearchForm repoForm, Integer limit, Integer offset) {
	JSONResponse lResponse = new JSONResponse();
	List<RepoDetailsSearch> lRepoDetailsList = new ArrayList<RepoDetailsSearch>();
	List<Object[]> lFuncList = getCheckoutSegmentsDAO().getFunctionalAreaList(repoForm);
	Set<String> ownersList = new HashSet<>();
	for (Object[] lFunctionDetails : lFuncList) {
	    RepoDetailsSearch lRepoDetails = new RepoDetailsSearch();
	    String lRepoName = FilenameUtils.removeExtension(lFunctionDetails[1].toString());
	    RepositoryView lRepository = getCacheClient().getFilteredRepositoryMap().get(lRepoName.toUpperCase());
	    Repository pRepository = lRepository.getRepository();
	    String repoDescription = pRepository.getDescription();
	    Set<String> repoOwnerList = pRepository.getOwners();
	    repoOwnerList.forEach((repoOwner) -> {
		User luser = getLDAPAuthenticatorImpl().getUserDetails(repoOwner);
		if (luser.getDisplayName() != null) {
		    ownersList.add(luser.getDisplayName());
		}
	    });
	    String OwnerName = ownersList.stream().map(Object::toString).collect(Collectors.joining(", "));
	    lRepoDetails.setRepoOwners(OwnerName);
	    lRepoDetails.setFunctionalArea(pRepository.getFuncArea());
	    lRepoDetails.setDescription(repoDescription);
	    lRepoDetailsList.add(lRepoDetails);

	}
	List<RepoDetailsSearch> finalRepoDetails = new ArrayList<>();

	int startVal = (offset * limit);
	int endVal = (offset * limit) + limit;
	for (int i = startVal; i < endVal; i++) {
	    if (i < lRepoDetailsList.size() && lRepoDetailsList.get(i) != null) {

		finalRepoDetails.add(lRepoDetailsList.get(i));
	    }
	    if (i >= lRepoDetailsList.size()) {
		continue;
	    }
	}
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(finalRepoDetails);
	lResponse.setCount(lRepoDetailsList.size());

	return lResponse;
    }

    @Transactional
    public JSONResponse commonFileSearch(User lUser, String pImplId, String pFileName) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    Implementation lImplementation = getImplementationDAO().find(pImplId);
	    String lCompanyName = lImplementation.getPlanId().getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
	    Set<String> lAllowedRepos = getGitHelper().getUserAllowedReadAndAboveRepos(lUser.getId(), lCompanyName);
	    Set<String> lObsRepos = getGitHelper().getObsRepoList(lCompanyName);
	    lAllowedRepos.removeAll(lObsRepos);

	    Set<String> lRepoList = new HashSet<>();
	    for (String lRepo : lAllowedRepos) {
		lRepoList.add(getGitHelper().getSourceURLByRepoName(lRepo));
	    }
	    // Non Prod search result
	    List<CheckoutSegments> lResult = getCheckoutSegmentsDAO().findByFileName(pFileName.toLowerCase(), pImplId, new HashSet<>());
	    List<GitSearchResult> finalsearchList = new ArrayList<>();
	    Set<String> SystemAndFuncBasedFiles = new HashSet();
	    if (lResult != null && !lResult.isEmpty()) {
		List<GitSearchResult> lReturnList = getCommonHelper().getNonProdFileDetails(lResult, lImplementation, lRepoList);

		// ZTPFM-2404 Check to skip new file from Prod if same file available in secured
		// state
		if (lReturnList != null && !lReturnList.isEmpty()) {
		    lReturnList.stream().filter(np -> np.getAdditionalInfo().get("planStatus").equalsIgnoreCase("New File") || Constants.PlanStatus.getApprovedStatusMap().keySet().contains(np.getAdditionalInfo().get("planStatus"))).forEach(nonProd -> {
			nonProd.getBranch().forEach(branch -> {
			    SystemAndFuncBasedFiles.add(nonProd.getFileName() + "-" + branch.getTargetSystem() + "-" + branch.getFuncArea());
			});
		    });
		    finalsearchList.addAll(lReturnList);
		}
	    }

	    Collection<GitSearchResult> lSearchResult = getjGITSearchUtils().SearchAllRepos(lCompanyName.toLowerCase(), pFileName.toLowerCase(), lImplementation.getPlanId().getMacroHeader(), null, lAllowedRepos, null, SystemAndFuncBasedFiles);
	    if (lSearchResult != null) {
		finalsearchList.addAll(lSearchResult);
	    }

	    Collections.sort(finalsearchList, new GitSearchResult());
	    // To validate put level details against Implementation put level
	    Collection<String> diffTargetSys = getGitHelper().validatePutLevelToAllowCheckout(pImplId, finalsearchList);

	    getGitHelper().searchFileResultFilter(lUser.getId(), finalsearchList, lCompanyName, null);

	    if (diffTargetSys != null && !diffTargetSys.isEmpty()) {
		String message = "Attn: IBM source artifacts cannot be checked out for " + diffTargetSys.stream().collect(Collectors.joining(",")) + " as default PUT level is overridden!";
		lResponse.setMetaData(message);
	    }
	    lResponse.setStatus(true);
	    lResponse.setData(finalsearchList);
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Error in listing files from Production ", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getLoginUserDetails(User lUser, String[] roles) {
	JSONResponse lResponse = new JSONResponse();
	StringBuilder build = new StringBuilder();
	String userId = null;
	try {
	    List<ImpPlan> approveList = new ArrayList<>();
	    List<ImpPlan> macroList = new ArrayList<>();
	    List<String> rolesList = Arrays.asList(roles);

	    if (lUser.getCurrentDelegatedUser() == null) {
		userId = lUser.getId();
	    } else {
		userId = lUser.getCurrentDelegatedUser().getId();
	    }
	    for (String role : rolesList) {
		if (role.equals(Constants.UserGroup.Reviewer.name())) {
		    List<Implementation> pendingReview = new ArrayList<>();
		    List<Object[]> lReviwerList = getImplementationDAO().findByReviewerBasedOnTime(userId, Boolean.FALSE);
		    for (Object[] obj : lReviwerList) {
			String id = obj[0].toString();
			Implementation imp = getImplementationDAO().find(id);
			pendingReview.add(imp);
		    }
		    if (pendingReview != null && !pendingReview.isEmpty()) {
			build.append("You have Implementations waiting for Peer Review ");
			build.append("<br>");
		    }
		}
		if (role.equals(Constants.UserGroup.DevManager.name())) {
		    List<Object[]> lapproveList = getImpPlanDAO().getToBeApprovedList(Constants.PlanStatus.PASSED_ACCEPTANCE_TESTING.name(), userId, Boolean.FALSE);
		    for (Object[] obj : lapproveList) {
			String id = obj[0].toString();
			ImpPlan imp = getImpPlanDAO().find(id);
			approveList.add(imp);
		    }
		    List<Object[]> lmacroList = getImpPlanDAO().getPlansByMacroHeaderBasedOnTime(userId, Constants.PlanStatus.APPROVED.name(), Boolean.TRUE, Boolean.FALSE);
		    for (Object[] obj : lmacroList) {
			String macroId = obj[0].toString();
			ImpPlan imp = getImpPlanDAO().find(macroId);
			macroList.add(imp);
		    }
		    if ((approveList != null && !approveList.isEmpty()) || (macroList != null && !macroList.isEmpty())) {
			build.append("<br>");
			build.append("You have Implementation Plans waiting for Approval ");
			build.append("<br>");
		    }
		}

	    }
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(build.toString());
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Error in getting the roles for the current user", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse deploymentStatusChange(User lUser, String planId, String deploymentStartAndStopReason) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    ImpPlan pPlan = getImpPlanDAO().find(planId);
	    CommonActivityMessage lMessage = new CommonActivityMessage(pPlan, null);
	    if (deploymentStartAndStopReason != null && (pPlan.getDeploymentStatus() == null || pPlan.getDeploymentStatus().equals("NONE") || pPlan.getDeploymentStatus().equals("START_DEPLOYMENT"))) {
		pPlan.setDeploymentStatus("STOP_DEPLOYMENT");
		lMessage.setMessage(lUser.getCurrentRole() + " " + lUser.getDisplayName() + " has stopped deployment - " + deploymentStartAndStopReason);
	    } else {
		lMessage.setMessage(lUser.getCurrentRole() + " " + lUser.getDisplayName() + " has re allowed deployment - " + deploymentStartAndStopReason);
		pPlan.setDeploymentStatus("START_DEPLOYMENT");
	    }

	    getImpPlanDAO().update(lUser, pPlan);

	    getActivityLogDAO().save(lUser, lMessage);

	} catch (Exception ex) {
	    lResponse.setErrorMessage("Error in updating deployment status " + planId);
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Error in updating deployment status ", ex);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    /*
     * Created by : Vinoth Ponnurangan Date :08/12/2019 JIRA : 2037 Sorting Data
     * based on TotalCount
     */
    @Transactional
    public JSONResponse generateRepoReport(User lCurrentUser, FileExtnReportForm fileExtnReportForm, Integer limit, Integer offset) {
	JSONResponse lReturn = new JSONResponse();
	try {
	    Map<String, SegmentReportDetailView> systemAndUserDetails = getSegamentCountAnalysisResult(fileExtnReportForm);

	    RepoReportView lReturnData = new RepoReportView();
	    lReturnData.setFileExtnReportForm(fileExtnReportForm);

	    if (fileExtnReportForm.getSystems() == null) {
		LOG.info("Please provide system information");
		throw new WorkflowException("Please provide system information");
	    }
	    List<SegmentReportDetailView> lReturnSortedList = systemAndUserDetails.values().stream().sorted(Comparator.comparingInt(SegmentReportDetailView::getTotalAllCount).reversed()).collect(Collectors.toList());
	    int start = limit * offset;
	    lReturnData.setsystemAndUserDetails(lReturnSortedList.stream().skip(start).limit(limit).collect(Collectors.toList()));
	    lReturn.setCount(systemAndUserDetails.size());
	    lReturn.setData(lReturnData);
	    lReturn.setStatus(Boolean.TRUE);

	} catch (WorkflowException ex) {
	    LOG.info("Unable to prepare report on Sytem basis", ex);
	    throw ex;
	} catch (Exception ex) {
	    LOG.info("Unable to prepare report on Sytem basis", ex);
	    throw new WorkflowException("Unable to prepare report on Sytem basis", ex);
	}
	return lReturn;
    }

    @Transactional
    public JSONResponse generateUserReport(User user, ReportForm userReportForm) {
	JSONResponse lReturn = new JSONResponse();
	try {
	    Map<String, Integer> lPlanVsSOCnt = new HashMap();
	    ReportView lReturnData = new ReportView();
	    lReturnData.setReportForm(userReportForm);

	    if (userReportForm.getSystems() == null) {
		LOG.info("Please provide system information");
		throw new WorkflowException("Please provide system information");
	    }

	    for (String lSystem : userReportForm.getSystems()) {

		ReportTable lReport = new ReportTable();
		Map<String, ReportDetailView> lUserReport = new HashMap();
		List<String> lPlanStatus = Arrays.asList(Constants.PlanStatus.ONLINE.name(), Constants.PlanStatus.FALLBACK.name());
		List<Object[]> lPlansInfo = getImpPlanDAO().getUserReportPlans(userReportForm.getRole().equalsIgnoreCase(Constants.UserGroup.Lead.name()), userReportForm.getUserIds(), lPlanStatus, Arrays.asList(lSystem), userReportForm.getStartDate(), userReportForm.getEndDate());
		if (lPlansInfo == null || lPlansInfo.isEmpty()) {
		    continue;
		}
		Set<ReportModel> lUsersInfo = new HashSet();

		for (Object[] lPlanInfo : lPlansInfo) {
		    ReportModel lUserPlanInfo = new ReportModel(lPlanInfo[0].toString(), lPlanInfo[1].toString(), lPlanInfo[2].toString(), 1);
		    lUsersInfo.add(lUserPlanInfo);
		}
		// Get the SO Counts from Shell script and update the POJO class

		for (ReportModel lUserInfo : lUsersInfo) {
		    if (lUserReport.get(lUserInfo.getUserName()) == null) {
			ReportDetailView lUserReportInfo = new ReportDetailView();
			lUserReportInfo.setUserName(lUserInfo.getUserName());

			// Online Plans Processing
			List<ReportModel> lOnlinePlans = lUsersInfo.stream().filter(t -> t.getUserName().equalsIgnoreCase(lUserInfo.getUserName()) && t.getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.ONLINE.name())).collect(Collectors.toList());
			Integer lOnlineCnt = 0;
			List<String> lPlanIds = new ArrayList();
			if (lOnlinePlans != null && !lOnlinePlans.isEmpty()) {
			    lOnlineCnt = lOnlinePlans.size();
			    for (ReportModel lPlanInfo : lOnlinePlans) {
				lPlanIds.add(lPlanInfo.getPlanId());
				getPlanHelper().getAndUpdateSOInfo(user, lPlanInfo.getPlanId());
			    }
			}
			Long lCnt = 0L;
			if (lPlanIds != null && !lPlanIds.isEmpty()) {
			    lCnt = getCheckoutSegmentsDAO().findSOCountByImpPlan(lPlanIds, lSystem);
			}
			lUserReportInfo.setTotalOnlineSharedObjects(lCnt != null ? lCnt.intValue() : 0);
			lUserReportInfo.setTotalOnlineDeployments(lOnlineCnt);

			// Fallback Plans Processing
			lPlanIds.clear();
			List<ReportModel> lFallbackPlans = lUsersInfo.stream().filter(t -> t.getUserName().equalsIgnoreCase(lUserInfo.getUserName()) && t.getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.FALLBACK.name())).collect(Collectors.toList());
			Integer lFallbackCnt = 0;
			if (lFallbackPlans != null && !lFallbackPlans.isEmpty()) {
			    lFallbackCnt = lFallbackPlans.size();
			    for (ReportModel lPlanInfo : lFallbackPlans) {
				lPlanIds.add(lPlanInfo.getPlanId());
				getPlanHelper().getAndUpdateSOInfo(user, lPlanInfo.getPlanId());
			    }
			}
			lCnt = 0L;
			if (lPlanIds != null && !lPlanIds.isEmpty()) {
			    lCnt = getCheckoutSegmentsDAO().findSOCountByImpPlan(lPlanIds, lSystem);
			}
			lUserReportInfo.setTotalFallbackSharedObjects(lCnt != null ? lCnt.intValue() : 0);
			lUserReportInfo.setTotalFallbackDeployments(lFallbackCnt);

			lUserReportInfo.setTotoalDeployments(lFallbackCnt + lOnlineCnt);
			lUserReportInfo.setTotalSharedObjects(lUserReportInfo.getTotalFallbackSharedObjects() + lUserReportInfo.getTotalOnlineSharedObjects());

			if (!lUserReportInfo.getTotoalDeployments().equals(0)) {
			    float per = ((float) lOnlineCnt / (float) (lFallbackCnt + lOnlineCnt)) * 100;
			    lUserReportInfo.setSuccessPerForDeployment((int) per);
			} else {
			    lUserReportInfo.setSuccessPerForDeployment(0);
			}
			if (lUserReportInfo.getTotalSharedObjects().equals(0)) {
			    lUserReportInfo.setSuccessPerForSourceObjects(0);
			} else {
			    lUserReportInfo.setSuccessPerForSourceObjects((int) (((float) lUserReportInfo.getTotalOnlineSharedObjects() / (float) lUserReportInfo.getTotalSharedObjects()) * 100));
			}
			lUserReport.put(lUserInfo.getUserName(), lUserReportInfo);
		    }
		}
		List<ReportDetailView> lUserReports = lUserReport.entrySet().stream().map(t -> t.getValue()).collect(Collectors.toList());
		lReport.setSystemAndDetails(lUserReports);

		// Summary Report for System
		SummaryDetailView lSystemSummary = new SummaryDetailView();
		for (ReportDetailView lSysUserReport : lUserReports) {
		    lSystemSummary.setTotalDeployments(lSystemSummary.getTotalDeployments() + lSysUserReport.getTotoalDeployments());
		    lSystemSummary.setTotalSourceObjects(lSystemSummary.getTotalSourceObjects() + lSysUserReport.getTotalSharedObjects());
		    lSystemSummary.setTotalOnlineDeployments(lSystemSummary.getTotalOnlineDeployments() + lSysUserReport.getTotalOnlineDeployments());

		    lSystemSummary.setTotalOnlineSharedObjects(lSystemSummary.getTotalOnlineSharedObjects() + lSysUserReport.getTotalOnlineSharedObjects());
		    lSystemSummary.setTotalFallbackDeployments(lSystemSummary.getTotalFallbackDeployments() + lSysUserReport.getTotalFallbackDeployments());
		    lSystemSummary.setTotalFallbackSharedObjects(lSystemSummary.getTotalFallbackSharedObjects() + lSysUserReport.getTotalFallbackSharedObjects());
		}
		lSystemSummary.setTotalUsers(lUserReports.size());
		if (!lSystemSummary.getTotalDeployments().equals(0)) {
		    lSystemSummary.setAverageSuccessPerOnOnlineDeploy((int) (((float) lSystemSummary.getTotalOnlineDeployments() / (float) lSystemSummary.getTotalDeployments()) * 100));
		}
		if (!lSystemSummary.getTotalSourceObjects().equals(0)) {
		    lSystemSummary.setAverageSuccessPerOnSO((int) (((float) lSystemSummary.getTotalOnlineSharedObjects() / (float) lSystemSummary.getTotalSourceObjects()) * 100));
		}
		lReport.setSystemName(lSystem);

		lReport.setSystemAndSummaryDetails(lSystemSummary);
		lReturnData.getReportTable().add(lReport);
	    }

	    lReturn.setStatus(Boolean.TRUE);
	    lReturn.setData(lReturnData);
	} catch (WorkflowException ex) {
	    LOG.info("Unable to prepare report on user basis", ex);
	    throw ex;
	} catch (Exception ex) {
	    LOG.info("Unable to prepare report on user basis", ex);
	    throw new WorkflowException("Unable to prepare report on User basis", ex);
	}
	return lReturn;
    }

    public JSONResponse exportUserReport(User user, ReportView userReportView) {
	JSONResponse lReturn = new JSONResponse();
	SearchExcelCreator creator = new SearchExcelCreator();
	try {
	    creator.generateUserReport(userReportView);
	    ByteArrayOutputStream lExcelStream = new ByteArrayOutputStream();
	    creator.getWorkBook().write(lExcelStream);
	    creator.getWorkBook().close();
	    lReturn.setData(lExcelStream.toByteArray());
	    lExcelStream.close();
	    lReturn.setMetaData("application/UserReport.ms-excel");
	    lReturn.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error("Error in Excel Creation ", ex);
	    lReturn.setErrorMessage("Error in Downloading Report");
	    lReturn.setStatus(Boolean.TRUE);
	}

	return lReturn;
    }

    @Transactional
    public JSONResponse generateFuncReport(User user, ReportForm userReportForm) {
	JSONResponse lReturn = new JSONResponse();
	ReportView lReturnData = new ReportView();

	try {
	    for (String lSystem : userReportForm.getSystems()) {
		List<String> lPlanStatus = Arrays.asList(Constants.PlanStatus.ONLINE.name(), Constants.PlanStatus.FALLBACK.name());
		List<ReportModel> lFuncAreaInfo = getImpPlanDAO().getFuncAndSegmentsCount(lPlanStatus, Arrays.asList(lSystem), userReportForm.getFuncAreas(), userReportForm.getStartDate(), userReportForm.getEndDate());

		if (lFuncAreaInfo == null || lFuncAreaInfo.isEmpty()) {
		    continue;
		}

		ReportTable lReport = new ReportTable();

		Map<String, List<ReportModel>> lFuncAreaVsReports = lFuncAreaInfo.stream().collect(Collectors.groupingBy(ReportModel::getFuncArea));
		// DetailViews
		List<ReportDetailView> lFuncAreaDetailViews = new ArrayList();
		lFuncAreaVsReports.entrySet().stream().forEach((t) -> {
		    ReportDetailView lFuncAreaView = new ReportDetailView();
		    lFuncAreaView.setFuncArea(t.getKey());

		    t.getValue().stream().filter(onlinePlan -> onlinePlan.getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.ONLINE.name())).forEach((value) -> {
			lFuncAreaView.setFuncArea(value.getFuncArea());
			try {
			    getPlanHelper().getAndUpdateSOInfo(user, value.getPlanId());
			} catch (Exception ex) {
			    LOG.info("Unable to update the SO information for Plan - " + value.getPlanId(), ex);
			}
			lFuncAreaView.setTotalOnlineSegmentsCount(lFuncAreaView.getTotalOnlineSegmentsCount() + value.getSegmentsCount());
		    });
		    t.getValue().stream().filter(onlinePlan -> onlinePlan.getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.FALLBACK.name())).forEach((value) -> {
			lFuncAreaView.setTotalFallbackSegmentsCount(lFuncAreaView.getTotalFallbackSegmentsCount() + value.getSegmentsCount());
			try {
			    getPlanHelper().getAndUpdateSOInfo(user, value.getPlanId());
			} catch (Exception ex) {
			    LOG.info("Unable to update the SO information for Plan - " + value.getPlanId(), ex);
			}
		    });

		    for (RepositoryView lRepoView : getCacheClient().getFilteredRepositoryMap().values()) {
			if (lRepoView.getRepository() != null && lRepoView.getRepository().getFuncArea() != null && lRepoView.getRepository().getFuncArea().contains(t.getKey())) {
			    if (lRepoView.getRepository().getDescription() == null) {
				continue;
			    }
			    lFuncAreaView.setFuncAreaDesc(lRepoView.getRepository().getDescription());
			    break;
			}
		    }
		    List<String> lPlanIdList = t.getValue().stream().map(ts -> ts.getPlanId()).collect(Collectors.toList());
		    Long soCount = getCheckoutSegmentsDAO().findSOCountByImpPlanAndFuncArea(lPlanIdList, t.getKey());
		    lFuncAreaView.setTotalSharedObjects(soCount != null ? soCount.intValue() : 0);

		    lFuncAreaView.setSuccessPerFunc((int) (((float) lFuncAreaView.getTotalOnlineSegmentsCount() / (float) (lFuncAreaView.getTotalOnlineSegmentsCount() + lFuncAreaView.getTotalFallbackSegmentsCount())) * 100));
		    lFuncAreaDetailViews.add(lFuncAreaView);
		});
		lReport.setSystemAndDetails(lFuncAreaDetailViews);

		// Summary Views
		SummaryDetailView lSummDetail = new SummaryDetailView();
		lSummDetail.setTotalFuncAreaCnt(lFuncAreaDetailViews.size());
		lFuncAreaDetailViews.stream().forEach((lDetailView) -> {
		    lSummDetail.setTotalSourceObjects(lSummDetail.getTotalSourceObjects() + lDetailView.getTotalSharedObjects());
		    lSummDetail.setTotalOnlineSegments(lSummDetail.getTotalOnlineSegments() + lDetailView.getTotalOnlineSegmentsCount());
		    lSummDetail.setTotalFallbackSegments(lSummDetail.getTotalFallbackSegments() + lDetailView.getTotalFallbackSegmentsCount());
		    lSummDetail.setSuccessPerFunc(lSummDetail.getSuccessPerFunc() + lDetailView.getSuccessPerFunc());
		});
		lSummDetail.setSuccessPerFunc((int) (((float) lSummDetail.getTotalOnlineSegments() / (float) (lSummDetail.getTotalOnlineSegments() + lSummDetail.getTotalFallbackSegments())) * 100));
		lReport.setSystemName(lSystem);
		lReport.setSystemAndSummaryDetails(lSummDetail);
		lReturnData.getReportTable().add(lReport);
	    }

	    List<String> headerFuncArea = (userReportForm.getFuncAreas() == null || userReportForm.getFuncAreas().isEmpty()) ? Arrays.asList("ALL") : userReportForm.getFuncAreas();
	    userReportForm.setFuncAreas(headerFuncArea);
	    lReturnData.setReportForm(userReportForm);

	    lReturn.setStatus(Boolean.TRUE);
	    lReturn.setData(lReturnData);

	} catch (Exception ex) {
	    LOG.error("Unable to prepare report on user input", ex);
	    throw new WorkflowException("Unable to generate report", ex);
	}
	return lReturn;
    }

    public JSONResponse exportFuncAreaReport(User user, ReportView userReportView) {
	JSONResponse lReturn = new JSONResponse();
	SearchExcelCreator creator = new SearchExcelCreator();
	try {
	    creator.generateFuncAreaReport(userReportView);
	    ByteArrayOutputStream lExcelStream = new ByteArrayOutputStream();
	    creator.getWorkBook().write(lExcelStream);
	    creator.getWorkBook().close();
	    lReturn.setData(lExcelStream.toByteArray());
	    lExcelStream.close();

	    lReturn.setMetaData("application/FuncAreaReport.ms-excel");
	    lReturn.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error("Error in Excel Creation ", ex);
	    throw new WorkflowException("Unable to Export the report", ex);
	}

	return lReturn;
    }

    @Transactional
    public JSONResponse getAllFuncAreaListBySysName(User pUser, String[] systems) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	Map<String, Set<String>> lFuncMap = new HashMap();
	try {
	    List<String> lSystemNames = Arrays.asList(systems);
	    for (String system : lSystemNames) {
		System lSystem = getSystemDAO().findByName(system);

		// Assumption: Branches Present in all Repos
		Map<String, RepositoryView> lAllViews = getCacheClient().getFilteredRepositoryMap();

		String lBranchName = "refs/heads/master_" + lSystem.getName().toLowerCase();
		Set<String> lFuncAreas = lAllViews.values().stream().filter(x -> (!x.getRepository().getName().toLowerCase().contains("derived_") && x.getRepository().getAvailableRefs().contains(lBranchName))).map(e -> e.getRepository().getFuncArea()).collect(Collectors.toSet());
		LOG.info("Functional Area Count for " + lSystem.getName() + " size " + lFuncAreas.size());
		lFuncMap.put(lSystem.getName(), lFuncAreas);
	    }
	    lResponse.setData(lFuncMap);
	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("Error occurs in getting Package list from Git", ex);
	    throw new WorkflowException("Error occurs in getting package list", ex);
	}
	return lResponse;
    }

    /*
     * JIRA Ticket : 2038 Created By: Radhakrishnan Created Dt: 06-Aug-2019
     * Description: Generate the report on online and fallback plan with details on
     * QA Testing
     */
    @Transactional
    public JSONResponse generateQAReport(User user, ReportForm userReportForm) {
	JSONResponse lReturn = new JSONResponse();
	ReportQATestingData lReturnData = new ReportQATestingData();
	lReturnData.setReportForm(userReportForm);

	try {
	    // userReportForm.setUserIds(userReportForm.getUserIds() == null ?
	    // Arrays.asList() : userReportForm.getUserIds());
	    List<ReportQATestingContent> lDetailRows = new ArrayList();
	    Map<String, List<ImpPlan>> lDevManagersVsPlan = new HashMap();
	    List<ImpPlan> lPlans = getImpPlanDAO().getPlanByLoadDateRange(userReportForm.getUserIds(), Arrays.asList(Constants.PlanStatus.ONLINE.name(), Constants.PlanStatus.FALLBACK.name()), userReportForm.getStartDate(), userReportForm.getEndDate());
	    if (lPlans.isEmpty()) {
		throw new WorkflowException("No Records Found");
	    }

	    lPlans.stream().forEach((lPlan) -> {
		if (lDevManagersVsPlan.get(lPlan.getDevManager()) == null) {
		    List<ImpPlan> lTempPlan = new ArrayList();
		    lTempPlan.add(lPlan);
		    lDevManagersVsPlan.put(lPlan.getDevManager(), lTempPlan);
		} else {
		    lDevManagersVsPlan.get(lPlan.getDevManager()).add(lPlan);
		}
	    });

	    lDevManagersVsPlan.entrySet().stream().forEach((lDevMangersVsPlanList) -> {
		String lDevManager = lDevMangersVsPlanList.getKey();
		List<ImpPlan> lPlanList = lDevMangersVsPlanList.getValue();
		ReportQATestingContent lDetailRow = new ReportQATestingContent();

		for (ImpPlan lPlan : lPlanList) {
		    lDetailRow.setDevManager(lPlan.getDevManagerName());
		    break;
		}

		List<ImpPlan> lOnlinePlans = lPlanList.stream().filter(t -> t.getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.ONLINE.name())).collect(Collectors.toList());
		List<ImpPlan> lFallbackPlans = lPlanList.stream().filter(t -> t.getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.FALLBACK.name())).collect(Collectors.toList());

		// Online Plan Processing
		if (lOnlinePlans.isEmpty()) {
		    lDetailRow.setQaFuncOnlinePassedCnt(0);
		    lDetailRow.setQaFuncOnlineBypassedCnt(0);
		    lDetailRow.setQaRegOnlinePassedCnt(0);
		    lDetailRow.setQaRegOnlineBypassedCnt(0);
		} else {
		    Long lQaFuncBypassed = lOnlinePlans.stream().filter(t -> t.getQaBypassStatus().equalsIgnoreCase(Constants.PLAN_QA_TESTING_STATUS.BYPASSED_BOTH.name()) || t.getQaBypassStatus().equalsIgnoreCase(Constants.PLAN_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name())).count();
		    lDetailRow.setQaFuncOnlinePassedCnt(lOnlinePlans.size() - lQaFuncBypassed.intValue());
		    lDetailRow.setQaFuncOnlineBypassedCnt(lQaFuncBypassed.intValue());

		    Long lQaRegBypassed = lOnlinePlans.stream().filter(t -> t.getQaBypassStatus().equalsIgnoreCase(Constants.PLAN_QA_TESTING_STATUS.BYPASSED_BOTH.name()) || t.getQaBypassStatus().equalsIgnoreCase(Constants.PLAN_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name())).count();
		    lDetailRow.setQaRegOnlinePassedCnt(lOnlinePlans.size() - lQaRegBypassed.intValue());
		    lDetailRow.setQaRegOnlineBypassedCnt(lQaRegBypassed.intValue());
		}

		// Fallback Plan Processing
		if (lFallbackPlans.isEmpty()) {
		    lDetailRow.setQaFuncFallbackPassedCnt(0);
		    lDetailRow.setQaFuncFallbackBypassedCnt(0);
		    lDetailRow.setQaRegFallbackPassedCnt(0);
		    lDetailRow.setQaRegFallbackBypassedCnt(0);
		} else {
		    Long lQaFuncBypassed = lFallbackPlans.stream().filter(t -> t.getQaBypassStatus().equalsIgnoreCase(Constants.PLAN_QA_TESTING_STATUS.BYPASSED_BOTH.name()) || t.getQaBypassStatus().equalsIgnoreCase(Constants.PLAN_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name())).count();
		    lDetailRow.setQaFuncFallbackPassedCnt(lFallbackPlans.size() - lQaFuncBypassed.intValue());
		    lDetailRow.setQaFuncFallbackBypassedCnt(lQaFuncBypassed.intValue());

		    Long lQaRegBypassed = lFallbackPlans.stream().filter(t -> t.getQaBypassStatus().equalsIgnoreCase(Constants.PLAN_QA_TESTING_STATUS.BYPASSED_BOTH.name()) || t.getQaBypassStatus().equalsIgnoreCase(Constants.PLAN_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name())).count();
		    lDetailRow.setQaRegFallbackPassedCnt(lFallbackPlans.size() - lQaRegBypassed.intValue());
		    lDetailRow.setQaRegFallbackBypassedCnt(lQaRegBypassed.intValue());
		}

		lDetailRows.add(lDetailRow);
	    });
	    lReturnData.setDetailData(lDetailRows);

	    ReportQATestingSummary lSummaryData = new ReportQATestingSummary();
	    lDetailRows.stream().forEach((lData) -> {
		lSummaryData.setTotalQaFuncFallbackBypassedCnt(lData.getQaFuncFallbackBypassedCnt() + lSummaryData.getTotalQaFuncFallbackBypassedCnt());
		lSummaryData.setTotalQaFuncFallbackPassedCnt(lData.getQaFuncFallbackPassedCnt() + lSummaryData.getTotalQaFuncFallbackPassedCnt());
		lSummaryData.setTotalQaRegFallbackBypassedCnt(lData.getQaRegFallbackBypassedCnt() + lSummaryData.getTotalQaRegFallbackBypassedCnt());
		lSummaryData.setTotalQaRegFallbackPassedCnt(lData.getQaRegFallbackPassedCnt() + lSummaryData.getTotalQaRegFallbackPassedCnt());

		lSummaryData.setTotalQaFuncOnlineBypassedCnt(lData.getQaFuncOnlineBypassedCnt() + lSummaryData.getTotalQaFuncOnlineBypassedCnt());
		lSummaryData.setTotalQaFuncOnlinePassedCnt(lData.getQaFuncOnlinePassedCnt() + lSummaryData.getTotalQaFuncOnlinePassedCnt());
		lSummaryData.setTotalQaRegOnlineBypassedCnt(lData.getQaRegOnlineBypassedCnt() + lSummaryData.getTotalQaRegOnlineBypassedCnt());
		lSummaryData.setTotalQaRegOnlinePassedCnt(lData.getQaRegOnlinePassedCnt() + lSummaryData.getTotalQaRegOnlinePassedCnt());
	    });
	    lReturnData.setSummaryData(lSummaryData);

	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception ex) {
	    LOG.info("Unable to generate report based on QA Testing", ex);
	    throw new WorkflowException("Unable to generate report based on QA Testing", ex);
	}
	lReturn.setData(lReturnData);
	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    /*
     * JIRA Ticket : 2038 Created By: Radhakrishnan Created Dt: 06-Aug-2019
     * Description: Export the report on online and fallback plan with details on QA
     * Testing
     */
    public JSONResponse exportQAReport(User user, ReportQATestingData reportData) {
	JSONResponse lReturn = new JSONResponse();
	SearchExcelCreator creator = new SearchExcelCreator();
	try {
	    creator.generateQATestingReport(reportData);
	    ByteArrayOutputStream lExcelStream = new ByteArrayOutputStream();
	    creator.getWorkBook().write(lExcelStream);
	    creator.getWorkBook().close();
	    lReturn.setData(lExcelStream.toByteArray());
	    lExcelStream.close();

	    lReturn.setMetaData("application/QATestingReport.ms-excel");
	    lReturn.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.info("Unable to generate the report on QA Testing basis", ex);
	    throw new WorkflowException("Unable to export the report", ex);
	}
	return lReturn;
    }

    /*
     * Created by : Ramkumar Seenivasan Date :08/12/2019 JIRA : 2037 Exporting
     * RepoReport data
     */
    @Transactional
    public JSONResponse exportRepoReport(User lCurrentUser, RepoReportView repoReportView) {
	JSONResponse lReturn = new JSONResponse();

	SearchExcelCreator creator = new SearchExcelCreator();
	try {
	    List<SegmentReportDetailView> segmentReportDetailView = getSegamentCountAnalysisResult(repoReportView.getFileExtnReportForm()).values().stream().sorted(Comparator.comparingInt(SegmentReportDetailView::getTotalAllCount).reversed()).collect(Collectors.toList());
	    creator.generateRepoReport(repoReportView.getFileExtnReportForm(), segmentReportDetailView);
	    ByteArrayOutputStream lExcelStream = new ByteArrayOutputStream();
	    creator.getWorkBook().write(lExcelStream);
	    creator.getWorkBook().close();
	    lReturn.setData(lExcelStream.toByteArray());
	    lExcelStream.close();

	    lReturn.setMetaData("application/RepoReport.ms-excel");
	    lReturn.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error("Error in Excel Creation ", ex);
	    lReturn.setErrorMessage("Error in Downloading Report");
	    lReturn.setStatus(Boolean.TRUE);
	}
	return lReturn;
    }

    /*
     * Created by : Vinoth Ponnurangan Date :08/12/2019 JIRA : 2037 Retrieving
     * RepoReport data from DB and performing calculations for each segment
     */
    @Transactional
    public Map<String, SegmentReportDetailView> getSegamentCountAnalysisResult(FileExtnReportForm fileExtnReportForm) {
	Map<String, SegmentReportDetailView> systemAndUserDetails = new HashMap();
	List<String> fileExtnList = new ArrayList<>();
	if (fileExtnReportForm.getFileExten() == null || fileExtnReportForm.getFileExten().isEmpty()) {
	    fileExtnList = Constants.SourceArtificatExtension.getSourceArtificatExtList();
	    fileExtnList.add("sbt");
	} else {
	    fileExtnList = fileExtnReportForm.getFileExten();
	}
	List<Object[]> list = impPlanDAO.getSegemntDetailsBasedOnSystem(fileExtnReportForm.getSystems(), fileExtnList, fileExtnReportForm.getStartDate(), fileExtnReportForm.getEndDate());
	for (Object[] obj : list) {
	    String progName = obj[0].toString();
	    String status = obj[1].toString();
	    int count = Integer.parseInt(obj[2].toString());

	    SegmentReportDetailView segmentReportDetailView;
	    if (systemAndUserDetails.containsKey(progName)) {
		segmentReportDetailView = systemAndUserDetails.get(progName);
	    } else {
		segmentReportDetailView = new SegmentReportDetailView();
	    }
	    if (status.equalsIgnoreCase("PROD")) {
		segmentReportDetailView.setTotoalOnlineDeployments(segmentReportDetailView.getTotoalOnlineDeployments() + count);
		segmentReportDetailView.setProgramName(progName);
	    } else if (status.equalsIgnoreCase("SECURED")) {
		segmentReportDetailView.setTotalSecuredDeployments(segmentReportDetailView.getTotalSecuredDeployments() + count);
		segmentReportDetailView.setProgramName(progName);
	    } else if (status.equalsIgnoreCase("NON_SECURED")) {
		segmentReportDetailView.setTotalActiveSegCount(segmentReportDetailView.getTotalActiveSegCount() + count);
		segmentReportDetailView.setProgramName(progName);
	    } else {
		LOG.error("Status is unidentified in generateReport()");
	    }
	    segmentReportDetailView.setTotalAllCount(segmentReportDetailView.getTotoalOnlineDeployments() + segmentReportDetailView.getTotalSecuredDeployments() + segmentReportDetailView.getTotalActiveSegCount());
	    systemAndUserDetails.put(progName, segmentReportDetailView);
	}
	return systemAndUserDetails;
    }

    @Transactional
    public JSONResponse isRebuildAllowed(String pPlanId) {
	JSONResponse lReturn = new JSONResponse();
	DevlBuildForm devlBuildForm = new DevlBuildForm();
	ImpPlan lPlan = getImpPlanDAO().find(pPlanId);
	if (lPlan.getFullBuildDt() != null) {
	    devlBuildForm.setAllowRebuild(Boolean.TRUE);
	    if (getCheckoutSegmentsDAO().isChangedSegAvailable(pPlanId, null)) {
		devlBuildForm.setAllowDevlBuild(Boolean.TRUE);
	    }
	} else {
	    devlBuildForm.setAllowDevlBuild(Boolean.TRUE);
	}
	lReturn.setData(devlBuildForm);
	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    @Transactional
    public JSONResponse getFullBuildStatus(String pPlanId) {
	JSONResponse lReturn = new JSONResponse();
	lReturn.setStatus(true);
	lReturn.setData(buildDAO.getFullBuildStatus(pPlanId));
	return lReturn;
    }

    @Transactional
    public JSONResponse getSystemLoadByPlanAndSystem(String pPlanId, Integer systemId, String loadDate) throws ParseException {
	JSONResponse lReturn = new JSONResponse();
	lReturn.setStatus(true);
	SystemLoad sysLoad = systemLoadDAO.getSystemLoadByPlanAndSystem(pPlanId, systemId);

	SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss z");
	formatter.setTimeZone(TimeZone.getTimeZone("EST5EDT"));
	String formattedTime = formatter.format(sysLoad.getLoadDateTime());

	String hh = formattedTime.split(":")[0];
	String mm = formattedTime.split(":")[1];
	String ss = formattedTime.split(":")[2];
	// 09-26-2019 00:00:00
	String loadDateTime = loadDate + " " + hh + ":" + mm + ":" + ss;
	LOG.info("Before Date Time " + DateHelper.convertGMTtoEST(sysLoad.getLoadDateTime()) + " After Date Time " + loadDateTime);
	lReturn.setMetaData(loadDateTime);
	lReturn.setData(sysLoad);
	return lReturn;
    }

    @Transactional
    public JSONResponse getBuildQueueByPlan(User pUser, Integer pOffset, Integer pLimit, String filter, HashMap pOrderBy) {
	JSONResponse lReturn = new JSONResponse();
	try {

	    List<BuildQueuePlanForm> buildQueuePlanList = new ArrayList<>();
	    List<BuildQueueForm> buildPlanList = impPlanDAO.getBuildQueuePlan(pUser, pOffset, pLimit, filter, pOrderBy);
	    for (BuildQueueForm lPlan : buildPlanList) {
		ImpPlan lPlanObj = impPlanDAO.find(lPlan.getPlanid());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date triggerDate = null;
		Date startDate = null;

		if (lPlan.getBuildtriggerdate() != null && !lPlan.getBuildtriggerdate().isEmpty()) {
		    triggerDate = format.parse(lPlan.getBuildtriggerdate());
		}
		if (lPlan.getBuildstartdate() != null && !lPlan.getBuildstartdate().isEmpty()) {
		    startDate = format.parse(lPlan.getBuildstartdate());
		}
		BuildQueuePlanForm planForm = new BuildQueuePlanForm(lPlan.getPlanid(), lPlan.getTargetsystem(), lPlan.getBuildtype(), lPlan.getBuildstatus(), lPlanObj, triggerDate, lPlan.getServername(), startDate);
		buildQueuePlanList.add(planForm);
	    }

	    lReturn.setData(buildQueuePlanList);
	    lReturn.setCount(buildQueuePlanList.size());
	    lReturn.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error("Error in getting plan in build Queue ", ex);
	    lReturn.setErrorMessage("Error in getting plan in build Queue ");
	    lReturn.setStatus(Boolean.FALSE);
	}
	return lReturn;

    }

    public JSONResponse inprogressUserActioncheck(String lUser) {
	JSONResponse lJSONResponse = new JSONResponse();
	Map<String, User> inprogressMap = getCacheClient().getInprogressMoveArtifactMap();
	for (Map.Entry<String, User> entry : inprogressMap.entrySet()) {
	    if (entry.getValue().getId().equalsIgnoreCase(lUser)) {
		wsserver.sendMessage(Constants.Channels.MOVE_SOURCE_ARTIFACT, lUser, "restricted");
		lJSONResponse.setStatus(false);
		lJSONResponse.setErrorMessage("Previous move artefact action is still in-progress. Please try after some time.");
		return lJSONResponse;
	    }
	}
	lJSONResponse.setStatus(true);
	return lJSONResponse;
    }

    @Transactional
    public JSONResponse updateExistingPlanLeadEmail() {
	JSONResponse lReturn = new JSONResponse();
	try {
	    List<ImpPlan> lPlanList = impPlanDAO.getPlanDetailsActiveToOnline();
	    Map<String, String> planIdList = lPlanList.stream().collect(Collectors.toMap(ImpPlan::getId, ImpPlan::getLeadId));

	    for (ImpPlan plan : lPlanList) {
		User lUser = lDAPAuthenticatorImpl.getUserDetails(plan.getLeadId());
		plan.setLeadEmail(lUser.getMailId());
		impPlanDAO.update(lUser, plan);
	    }
	    lReturn.setData(planIdList);
	    lReturn.setCount(planIdList.size());
	    lReturn.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error("Error updating plan Lead email Id ", ex);
	    lReturn.setErrorMessage("Error updating plan Lead email Id");
	    lReturn.setStatus(Boolean.FALSE);
	}
	return lReturn;

    }

    public JSONResponse prCheck(String prNumber) {
	JSONResponse lReturn = new JSONResponse();
	try {
	    LOG.info("PR Validation starts");
	    PRResponse prNumberStatus = snowPRClientUtils.getPRNumberStatus(prNumber);
	    LOG.info("PR Validation ends");
	    if (prNumberStatus != null) {
		LOG.info("PR Number status" + prNumberStatus.getStatus());
		prNumberStatus.getResult().stream().forEach(t -> LOG.info(t.getNumber() + "---> Status " + t.getState()));
	    }
	} catch (Exception ex) {
	    LOG.info("Error occurs ", ex);
	    throw new WorkflowException("Error occurs on PR Validation");
	}
	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    @Transactional
    public JSONResponse repoDescUpdate(String company, User luser) {
	JSONResponse lReturn = new JSONResponse();
	try {
	    List<Object[]> segListObj = impPlanDAO.getSegmentListByCompany(company);
	    for (Object[] lsegList : segListObj) {
		Integer id = (Integer) lsegList[0];
		String repoDesc = lsegList[1].toString();
		CheckoutSegments lCheckout = checkoutSegmentsDAO.find(id);
		lCheckout.setRepoDesc(repoDesc);
		checkoutSegmentsDAO.update(luser, lCheckout);
	    }
	} catch (Exception ex) {
	    LOG.info("Error occurs ", ex);
	    throw new WorkflowException("Error occurs on Updatesegments");
	}
	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    public JSONResponse getRepoInfo() {
	JSONResponse lReturn = new JSONResponse();
	lReturn.setStatus(true);
	lReturn.setData(getCacheClient().getFilteredRepositoryMap());
	return lReturn;
    }
}
