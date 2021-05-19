/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.service;

import static com.tsi.workflow.utils.Constants.PlanStatus.DEPLOYED_IN_QA_REGRESSION;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.LdapGroupConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.activity.PlanStatusActivityMessage;
import com.tsi.workflow.activity.QATestingStatusActivityMessage;
import com.tsi.workflow.base.BaseService;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dao.SystemLoadActions;
import com.tsi.workflow.beans.dao.Vpars;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.bpm.model.TaskVariable;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.PreProductionLoadsDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.dao.VparsDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.ldap.config.LDAPGroup;
import com.tsi.workflow.mail.StatusChangeToDependentPlanMail;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.BUILD_TYPE;
import com.tsi.workflow.utils.Constants.TagStatus;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
@Service
public class QualityAssuranceService extends BaseService {

    private static final Logger LOG = Logger.getLogger(QualityAssuranceService.class.getName());

    @Autowired
    SystemLoadActionsDAO systemLoadActionsDAO;
    @Autowired
    RejectHelper rejectHelper;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    ImplementationDAO implementationDAO;
    @Autowired
    SystemDAO systemDAO;
    @Autowired
    VparsDAO vparsDAO;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    GITConfig gITConfig;
    @Autowired
    JGitClientUtils lGitUtils;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    BPMClientUtils lBPMClientUtils;
    @Autowired
    LdapGroupConfig ldapGroupConfig;
    @Autowired
    SSHClientUtils sSHClientUtils;
    @Autowired
    PlanHelper planHelper;
    @Autowired
    PreProductionLoadsDAO preProductionLoadsDAO;

    public PreProductionLoadsDAO getPreProductionLoadsDAO() {
	return preProductionLoadsDAO;
    }

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public SystemLoadActionsDAO getSystemLoadActionsDAO() {
	return systemLoadActionsDAO;
    }

    public LdapGroupConfig getLdapGroupConfig() {
	return ldapGroupConfig;
    }

    public BPMClientUtils getBPMClientUtils() {
	return lBPMClientUtils;
    }

    public RejectHelper getRejectHelper() {
	return rejectHelper;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public ImplementationDAO getImplementationDAO() {
	return implementationDAO;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public SystemDAO getSystemDAO() {
	return systemDAO;
    }

    public VparsDAO getVparsDAO() {
	return vparsDAO;
    }

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public JGitClientUtils getJGitClientUtils() {
	return lGitUtils;
    }

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    public PlanHelper getPlanHelper() {
	return planHelper;
    }

    @Transactional
    public JSONResponse updatePlanTestStatus(User pUser, String planId, Integer systemId, Integer[] vparId, String status, Integer[] vparsId) throws WorkflowException {
	// TODO: Need to revist the logic
	JSONResponse lResponse = new JSONResponse();
	try {
	    LOG.info("Updating Testing Status for plan " + planId + " as " + status);
	    ImpPlan plan = getImpPlanDAO().find(planId);
	    List<SystemLoadActions> lLoadSets = getSystemLoadActionsDAO().findByPlanId(planId).stream().filter(lLoadSet -> lLoadSet.getSystemId().getId().equals(systemId) && Arrays.asList(vparId).indexOf(lLoadSet.getVparId().getId()) >= 0).collect(Collectors.toList());
	    for (SystemLoadActions lLoadSet : lLoadSets) {
		lLoadSet.setTestStatus(status);
	    }
	    if ("FAIL".equalsIgnoreCase(status)) {
		List<String> lVaprList = new ArrayList<>();
		List<Vpars> lVparsList = getVparsDAO().findByVpars(vparId);
		for (Vpars lVpars : lVparsList) {
		    lVaprList.add(lVpars.getName());
		}
		// ZTPFM-1455 QA Functional Tester's Reject Plan Mail Notification
		if (((plan.getPlanStatus().equals(Constants.PlanStatus.APPROVED.name())) || (plan.getPlanStatus().equals(Constants.PlanStatus.DEPLOYED_IN_QA_FUNCTIONAL.name())))) {
		    String planStatus = TagStatus.REJECTED.name();
		    getPlanHelper().sendMailNotificationQAFunTesterPassFail(plan, pUser, planStatus);
		}
		String rejectReason = getSystemDAO().find(systemId).getName() + " - " + String.join(",", lVaprList) + " " + Constants.REJECT_REASON.QA_FAIL.getValue();
		getRejectHelper().rejectPlan(pUser, planId, rejectReason, Constants.AUTOREJECT_COMMENT.REJECT.getValue(), Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);

	    } else {
		String[] pId = new String[] { planId };
		List<SystemLoadActions> lActiveLoadSets = getSystemLoadActionsDAO().findBy(pId, vparsId);
		List<SystemLoadActions> lPassedLoadSets = new ArrayList<>();
		// TODO: Check with the Status
		String lByPassedType = plan.getPlanStatus().equals(Constants.PlanStatus.DEPLOYED_IN_QA_FUNCTIONAL.name()) ? Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name() : Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name();
		List<SystemLoad> lActiveSystemLoads = getSystemLoadDAO().findPlanByQATestingStatus(plan.getId(), Arrays.asList("NONE", lByPassedType));
		Set<String> lPassedSystems = new TreeSet<>();

		for (SystemLoadActions lLoadSet : lActiveLoadSets) {
		    if (lLoadSet.getTestStatus() != null && lLoadSet.getTestStatus().equals(status)) {
			lPassedLoadSets.add(lLoadSet);
			lPassedSystems.add(lLoadSet.getSystemId().getName());
		    }
		}
		for (SystemLoad lDefSystemLoad : lActiveSystemLoads) {
		    if (lDefSystemLoad.getSystemId().getId().equals(systemId)) {
			if (plan.getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.DEPLOYED_IN_QA_FUNCTIONAL.name())) {
			    lDefSystemLoad.setQaFunctionalBypassComment("*PASSED*");
			} else {
			    lDefSystemLoad.setQaRegressionBypassComment("*PASSED*");
			}
			getSystemLoadDAO().update(pUser, lDefSystemLoad);
		    }
		}

		if (lPassedLoadSets.size() == lActiveLoadSets.size() && lActiveSystemLoads.size() == lPassedSystems.size()) {
		    List<SystemLoad> lSystemLoad = getSystemLoadDAO().findByImpPlan(plan);
		    Boolean flag = Boolean.TRUE;
		    List<SystemLoadActions> lFilteredLoadSets = new ArrayList();
		    for (SystemLoad systemLoad : lSystemLoad) {
			lFilteredLoadSets.addAll(lActiveLoadSets.stream().filter(t -> t.getSystemId().getId().equals(systemLoad.getSystemId().getId())).collect(Collectors.toList()));
		    }
		    if (lFilteredLoadSets.isEmpty()) {
			flag = Boolean.FALSE;
		    }
		    if (flag) {
			LOG.info("All Loads are Activated Marking as Pass and Changing plan status");
			String lOldStatus = plan.getPlanStatus();
			LOG.info("Current Status of Plan " + lOldStatus);
			Constants.PlanStatus lPlanStatus;

			// QA Functional Pass
			if (lOldStatus.equalsIgnoreCase(Constants.PlanStatus.DEPLOYED_IN_QA_FUNCTIONAL.name())) {
			    lPlanStatus = Constants.PlanStatus.PASSED_FUNCTIONAL_TESTING;
			    if (lActiveSystemLoads.size() != lSystemLoad.size()) {
				lPlanStatus = Constants.PlanStatus.PARTIAL_FUNCTIONAL_TESTING;
			    }
			    plan.setPlanStatus(lPlanStatus.name());

			    // Activity Log on QA Functional Testing ByPassed
			    PlanStatusActivityMessage planStatusActivityMessage = new PlanStatusActivityMessage(plan, null);
			    planStatusActivityMessage.setStatus(lPlanStatus.toString());
			    getActivityLogDAO().save(pUser, planStatusActivityMessage);

			    getImpPlanDAO().update(pUser, plan);

			    String lCompanyName = plan.getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
			    String lRepoName = getJGitClientUtils().getPlanRepoName(lCompanyName, plan.getId().toLowerCase());
			    List<String> lBranchList = getJGitClientUtils().getAllBranchList(lCompanyName, plan.getId());

			    if (!getsSHClientUtils().addImplementationTag(lRepoName, lPlanStatus, lBranchList)) {
				LOG.error("Unable to add tag-" + lPlanStatus.name() + " to Plan-" + plan.getId());
			    }

			    if (plan.getQaBypassStatus().equalsIgnoreCase(Constants.PLAN_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name())) {
				plan.setPlanStatus(Constants.PlanStatus.BYPASSED_REGRESSION_TESTING.name());
			    } else {
				List<SystemLoadActions> lRegLoads = new ArrayList();
				List<PreProductionLoads> lPreProdLoads = getPreProductionLoadsDAO().findByPlanId(plan);
				lPreProdLoads.stream().filter(t -> t.getSystemLoadActionsId() != null && t.getSystemLoadActionsId().getVparId().getTssDeploy() && (!Constants.SYSTEM_QA_TESTING_STATUS.getByPassedRegressionStatus().contains(t.getSystemLoadId().getQaBypassStatus()))).forEach((lVparsLoads) -> {
				    List<Vpars> lVpars = getVparsDAO().findBySystem(Arrays.asList(lVparsLoads.getSystemId()), lVparsLoads.getSystemLoadActionsId().getVparId().getName(), Constants.VPARSEnvironment.QA_REGRESSION);
				    lVpars.stream().forEach((t) -> {
					SystemLoadActions lRegActions = new SystemLoadActions();
					BeanUtils.copyProperties(lVparsLoads.getSystemLoadActionsId(), lRegActions);
					lRegActions.setVparId(t);
					lRegActions.setTestStatus(null);
					getSystemLoadActionsDAO().save(pUser, lRegActions);
					lRegLoads.add(lRegActions);
					lVparsLoads.setSystemLoadActionsId(lRegActions);
					getPreProductionLoadsDAO().update(pUser, lVparsLoads);
				    });
				});
				if (!lRegLoads.isEmpty()) {
				    getPlanHelper().updatePlanAsDeployedInPreProd(pUser, plan.getId(), Constants.VPARSEnvironment.QA_REGRESSION, true);
				}
			    }

			    List<SystemLoad> lSysLoadList = getSystemLoadDAO().findPlanByQATestingStatus(planId, Arrays.asList(Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name(), Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_BOTH.name()));
			    for (SystemLoad sysLoad : lSysLoadList) {
				QATestingStatusActivityMessage lActSysMessage = new QATestingStatusActivityMessage(plan, null);
				lActSysMessage.setSystemName(sysLoad.getSystemId().getName());
				lActSysMessage.setQaPhaseName("Regression");
				getActivityLogDAO().save(pUser, lActSysMessage);
			    }

			    StatusChangeToDependentPlanMail statusChangeToDependentPlanMail = (StatusChangeToDependentPlanMail) getMailMessageFactory().getTemplate(StatusChangeToDependentPlanMail.class);
			    statusChangeToDependentPlanMail.addToAddressUserId(plan.getLeadId(), Constants.MailSenderRole.LEAD);
			    List<Implementation> findByImpPlan = getImplementationDAO().findByImpPlan(plan);
			    for (Implementation implementation : findByImpPlan) {
				statusChangeToDependentPlanMail.addcCAddressUserId(implementation.getDevId(), Constants.MailSenderRole.DEVELOPER);
				String[] reviewers = implementation.getPeerReviewers().split("\\,");
				for (String reviewer : reviewers) {
				    statusChangeToDependentPlanMail.addcCAddressUserId(reviewer, Constants.MailSenderRole.PEER_REVIEWER);
				}
			    }
			    statusChangeToDependentPlanMail.setImpPlanId(plan.getId());
			    statusChangeToDependentPlanMail.setOldStatus(lOldStatus);
			    statusChangeToDependentPlanMail.setNewStatus(plan.getPlanStatus());
			    getMailMessageFactory().push(statusChangeToDependentPlanMail);

			    // ZTPFM-1455 QA Functional Tester's plan Pass Mail Notification
			    if (plan.getPlanStatus().equals(Constants.PlanStatus.PASSED_FUNCTIONAL_TESTING.name())) {
				String planStatus = Constants.PlanStatus.PASSED_FUNCTIONAL_TESTING.name();
				getPlanHelper().sendMailNotificationQAFunTesterPassFail(plan, pUser, planStatus);
			    }

			} else if (lOldStatus.equalsIgnoreCase(Constants.PlanStatus.DEPLOYED_IN_QA_REGRESSION.name())) {
			    LOG.info("Old status of the Plan - " + lOldStatus);

			    // update plan status in LTCUST Plan Deployed in Pre Prod - ZTPFM-1556
			    System lsystem = getSystemDAO().find(systemId);
			    PreProductionLoads lpreProductionLoads = getPreProductionLoadsDAO().findByPlanIdByLoad(plan, lsystem);
			    if (lpreProductionLoads != null) {
				// Activity Log update Passed Regression Testing
				PlanStatusActivityMessage planStatusActivityMessage = new PlanStatusActivityMessage(plan, null);
				planStatusActivityMessage.setStatus(Constants.PlanStatus.PASSED_REGRESSION_TESTING.name());
				getActivityLogDAO().save(pUser, planStatusActivityMessage);
				// LTCUST Mail send Passed Regression Testing
				StatusChangeToDependentPlanMail statusChangeToDependentPlanMail = (StatusChangeToDependentPlanMail) getMailMessageFactory().getTemplate(StatusChangeToDependentPlanMail.class);
				statusChangeToDependentPlanMail.addToAddressUserId(plan.getLeadId(), Constants.MailSenderRole.LEAD);
				List<Implementation> findByImpPlan = getImplementationDAO().findByImpPlan(plan);
				for (Implementation implementation : findByImpPlan) {
				    statusChangeToDependentPlanMail.addcCAddressUserId(implementation.getDevId(), Constants.MailSenderRole.DEVELOPER);
				    String[] reviewers = implementation.getPeerReviewers().split("\\,");
				    for (String reviewer : reviewers) {
					statusChangeToDependentPlanMail.addcCAddressUserId(reviewer, Constants.MailSenderRole.PEER_REVIEWER);
				    }
				}
				if (lpreProductionLoads.getSystemLoadId().getQaFunctionalTesters() != null) {
				    List<String> qaFunctionalTesterList = Arrays.asList(lpreProductionLoads.getSystemLoadId().getQaFunctionalTesters().split(","));
				    for (String qaFunctionalTester : qaFunctionalTesterList) {
					statusChangeToDependentPlanMail.addcCAddressUserId(qaFunctionalTester, Constants.MailSenderRole.QA_FUNCTIONAL);
				    }
				}
				statusChangeToDependentPlanMail.setImpPlanId(plan.getId());
				statusChangeToDependentPlanMail.setNewStatus(Constants.PlanStatus.PASSED_REGRESSION_TESTING.name());
				statusChangeToDependentPlanMail.setTssDeployStatus(Boolean.TRUE);
				getMailMessageFactory().push(statusChangeToDependentPlanMail);

				lPlanStatus = Constants.PlanStatus.DEPLOYED_IN_PRE_PRODUCTION;
				plan.setPlanStatus(lPlanStatus.name());

			    } else {
				lPlanStatus = Constants.PlanStatus.PASSED_REGRESSION_TESTING;
				if (lActiveSystemLoads.size() != lSystemLoad.size()) {
				    lPlanStatus = Constants.PlanStatus.PARTIAL_REGRESSION_TESTING;
				}
				plan.setPlanStatus(lPlanStatus.name());
				for (SystemLoad lDefSystemLoad : lActiveSystemLoads) {
				    if (lDefSystemLoad.getSystemId().getId().equals(systemId)) {
					lDefSystemLoad.setQaRegressionBypassComment("*PASSED*");
					getSystemLoadDAO().update(pUser, lDefSystemLoad);
				    }
				}
			    }
			}

			if (Constants.PlanStatus.getQARegressiontestingStatus().keySet().contains(plan.getPlanStatus())) {
			    LOG.info(plan.getPlanStatus());
			    getBPMClientUtils().setTaskAsCompleted(pUser, plan.getProcessId());

			    StringBuilder lGroupNames = new StringBuilder();
			    List<LDAPGroup> lGroups = getLdapGroupConfig().getSystemSupportGroups();
			    lGroups.forEach((lLDAPGroup) -> {
				lGroupNames.append(lLDAPGroup.getLdapGroupName()).append(",");
			    });
			    if (lGroupNames != null && !lGroupNames.toString().isEmpty()) {
				lGroupNames.setLength(lGroupNames.length() - 1);
			    }
			    List<TaskVariable> lTaskVars = new ArrayList<>();
			    getBPMClientUtils().assignTaskToGroup(pUser, plan.getProcessId(), lGroupNames.toString(), lTaskVars);

			    // update plan status in LTCUST Plan Deployed in Pre Prod - ZTPFM-1556
			    getPlanHelper().updatePlanAsDeployedInPreProd(pUser, plan.getId(), Constants.VPARSEnvironment.PRE_PROD, false);

			    // System lsystem = getSystemDAO().find(systemId);
			    // PreProductionLoads lpreProductionLoads =
			    // getPreProductionLoadsDAO().findByPlanIdByLoad(plan, lsystem);
			    // if (lpreProductionLoads != null) {
			    // if
			    // (plan.getPlanStatus().equals(Constants.PlanStatus.BYPASSED_REGRESSION_TESTING.name()))
			    // {
			    // plan.setPlanStatus(Constants.PlanStatus.DEPLOYED_IN_PRE_PRODUCTION.name());
			    // }
			    // }
			    getImpPlanDAO().update(pUser, plan);

			    // getActivityLogDAO().save(pUser, new QATestingActivityMessage(plan, null));
			    // Activity Log on QA Functional Testing ByPassed
			    PlanStatusActivityMessage planStatusActivityMessage = new PlanStatusActivityMessage(plan, null);
			    planStatusActivityMessage.setStatus(plan.getPlanStatus());
			    getActivityLogDAO().save(pUser, planStatusActivityMessage);

			    StatusChangeToDependentPlanMail statusChangeToDependentPlanMail = (StatusChangeToDependentPlanMail) getMailMessageFactory().getTemplate(StatusChangeToDependentPlanMail.class);
			    statusChangeToDependentPlanMail.addToAddressUserId(plan.getLeadId(), Constants.MailSenderRole.LEAD);
			    List<Implementation> findByImpPlan = getImplementationDAO().findByImpPlan(plan);
			    for (Implementation implementation : findByImpPlan) {
				statusChangeToDependentPlanMail.addcCAddressUserId(implementation.getDevId(), Constants.MailSenderRole.DEVELOPER);
				String[] reviewers = implementation.getPeerReviewers().split("\\,");
				for (String reviewer : reviewers) {
				    statusChangeToDependentPlanMail.addcCAddressUserId(reviewer, Constants.MailSenderRole.PEER_REVIEWER);
				}
			    }
			    statusChangeToDependentPlanMail.setImpPlanId(plan.getId());
			    statusChangeToDependentPlanMail.setOldStatus(lOldStatus);
			    statusChangeToDependentPlanMail.setNewStatus(plan.getPlanStatus());
			    getMailMessageFactory().push(statusChangeToDependentPlanMail);
			}
		    }
		}
	    }
	    lResponse.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Unable to update Implementation Plan Test Status", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getQAFunDeploymentPlanList(User lUser, boolean isFunctional, Constants.LoaderTypes loaderTypes, String pFilter, Integer offset, Integer limit, LinkedHashMap<String, String> lOrderBy) {
	// (QA Functional Testers)
	User pUser = lUser.getCurrentOrDelagateUser();
	JSONResponse lResponse = new JSONResponse();
	List<String> status = new ArrayList<>();
	List<String> lQAStatusFilter = new ArrayList<>();
	BUILD_TYPE build_type;
	List<ImpPlan> impPlanList = new ArrayList<>();
	Long lcount;
	try {
	    status = new ArrayList(Constants.PlanStatus.getQAFunctionalDeploymentStatus().keySet());
	    build_type = BUILD_TYPE.STG_LOAD;
	    lQAStatusFilter.add("NONE");
	    lQAStatusFilter.add(Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name());
	    impPlanList = getImpPlanDAO().findQAFunPlanList(pUser, status, pFilter, build_type, loaderTypes, lQAStatusFilter, offset, limit, lOrderBy);
	    lcount = getImpPlanDAO().countByQAFunStatusList(pUser, status, pFilter, build_type, loaderTypes, lQAStatusFilter);

	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setCount(lcount);
	    lResponse.setData(impPlanList);
	} catch (Exception ex) {
	    LOG.error("Unable to fetch Implementation Plan List for Deployment", ex);
	    throw new WorkflowException("Unable to fetch Implementation Plan List for Deployment!", ex);
	}
	return lResponse;
    }

}
