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
import com.tsi.workflow.audit.helper.AuditCommonHelper;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.service.CommonBaseService;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.GiPorts;
import com.tsi.workflow.beans.dao.GitProdSearchDb;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.DSLDetailsForm;
import com.tsi.workflow.beans.ui.JobDetails;
import com.tsi.workflow.beans.ui.ProdLoadStatusDetails;
import com.tsi.workflow.beans.ui.RepoBasedSrcArtifacts;
import com.tsi.workflow.beans.ui.SystemBasedMetaData;
import com.tsi.workflow.beans.ui.SystemBasedSrcArtifacts;
import com.tsi.workflow.beans.ui.TosActionQueue;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.GiPortsDAO;
import com.tsi.workflow.dao.GitSearchDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.GitBranchSearchResult;
import com.tsi.workflow.git.GitSearchResult;
import com.tsi.workflow.git.IJGITSearchUtils;
import com.tsi.workflow.helper.CommonHelper;
import com.tsi.workflow.helper.GITHelper;
import com.tsi.workflow.helper.ImpPlanTrackHelper;
import com.tsi.workflow.helper.ImplementationTrackHelper;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.ldap.config.LDAPGroup;
import com.tsi.workflow.report.RepoBasedSrcArtifactExport;
import com.tsi.workflow.schedular.PlanSubmissionGitOperation;
import com.tsi.workflow.schedular.PreProdRejectListener;
import com.tsi.workflow.tracker.PlanTrackerData;
import com.tsi.workflow.utils.CheckoutUtils;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.EncryptUtil;
import com.tsi.workflow.utils.JSONResponse;
import com.tsi.workflow.websocket.WSMessagePublisher;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommonService extends CommonBaseService {

    private static final Logger LOG = Logger.getLogger(CommonService.class.getName());

    @Autowired
    WSMessagePublisher wsserver;

    @Autowired
    CacheClient cacheClient;

    @Autowired
    GITConfig gitConfig;

    @Autowired
    WFConfig wfConfig;

    @Autowired
    CommonHelper commonHelper;

    @Autowired
    LDAPAuthenticatorImpl lDAPAuthenticatorImpl;

    @Autowired
    LdapGroupConfig ldapGroupConfig;

    @Autowired
    GITHelper gitHelper;

    @Autowired
    IJGITSearchUtils jGITSearchUtils;

    @Autowired
    GitSearchDAO gitSearchDAO;

    @Autowired
    GiPortsDAO giPortsDAO;

    @Autowired
    GITConfig gITConfig;

    @Autowired
    ImplementationTrackHelper implementationTrackHelper;

    @Autowired
    ImpPlanTrackHelper impPlanTrackHelper;

    @Autowired
    PreProdRejectListener preProdRejectListener;

    @Autowired // Constants.BUILD_TYPE.DEVL
    ConcurrentHashMap<String, JenkinsBuild> develBuildJob;

    @Autowired
    @Qualifier("develLoaderJob")
    ConcurrentLinkedQueue<JenkinsBuild> develLoaderJob;

    @Autowired
    @Qualifier("stagingWorkspaceCreationJobs")
    ConcurrentLinkedQueue<JenkinsBuild> stagingWorkspaceCreationJobs;

    @Autowired
    @Qualifier("stagingBuildJobs")
    ConcurrentLinkedQueue<JenkinsBuild> stagingBuildJobs;

    @Autowired
    @Qualifier("stagingLoaderJobs")
    ConcurrentLinkedQueue<JenkinsBuild> stagingLoaderJobs;

    @Autowired
    @Qualifier("onlineBuildJobs")
    ConcurrentLinkedQueue<JenkinsBuild> onlineBuildJobs;

    @Autowired
    @Qualifier("fallBackBuildJobs")
    ConcurrentLinkedQueue<JenkinsBuild> fallBackBuildJobs;

    @Autowired
    @Qualifier("lPlanUpdateStatusMap")
    ConcurrentHashMap<String, User> lPlanUpdateStatusMap;

    @Autowired
    @Qualifier("lPlanOnlineFallbackStatusMap")
    ConcurrentHashMap<String, String> lPlanOnlineFallbackStatusMap;

    @Autowired
    ConcurrentHashMap<String, Set<TosActionQueue>> lPreProdTOSOperationMap;

    @Autowired
    AuditCommonHelper auditCommonHelper;

    @Autowired
    PlanHelper planHelper;

    @Autowired
    PlanSubmissionGitOperation planSubmissionGitOperation;

    public ImpPlanTrackHelper getImpPlanTrackHelper() {
	return impPlanTrackHelper;
    }

    public ImplementationTrackHelper getImplementationTrackHelper() {
	return implementationTrackHelper;
    }

    public IJGITSearchUtils getjGITSearchUtils() {
	return jGITSearchUtils;
    }

    public GITHelper getGitHelper() {
	return gitHelper;
    }

    public CommonHelper getCommonHelper() {
	return commonHelper;
    }

    public LDAPAuthenticatorImpl getLDAPAuthenticatorImpl() {
	return lDAPAuthenticatorImpl;
    }

    public LdapGroupConfig getLdapGroupConfig() {
	return ldapGroupConfig;
    }

    public GitSearchDAO getGitSearchDAO() {
	return gitSearchDAO;
    }

    public PreProdRejectListener getPreProdRejectListener() {
	return preProdRejectListener;
    }

    public CacheClient getCacheClient() {
	return cacheClient;
    }

    public PlanHelper getPlanHelper() {
	return planHelper;
    }

    public PlanSubmissionGitOperation getPlanSubmissionGitOperation() {
	return planSubmissionGitOperation;
    }

    @Transactional
    public JSONResponse getFileTypeByPlan(String[] ids) {
	JSONResponse response = new JSONResponse();
	if (ids == null) {
	    response.setErrorMessage("Plan Id(s) are Empty");
	    response.setStatus(Boolean.FALSE);
	    return response;
	}
	HashMap<String, List<String>> lResult = getCheckoutSegmentsDAO().getFileTypesByPlan(ids);
	response.setData(lResult);
	response.setStatus(Boolean.TRUE);
	return response;
    }

    @Transactional
    public String getLocalConfigDetails(String pPlanId, String pSystem) {
	if (pPlanId == null || pPlanId.isEmpty()) {
	    return "ERROR: Plan id should not be empty";
	}
	if (pSystem == null || pSystem.isEmpty()) {
	    return "ERROR: System Name should not be empty";
	}

	ImpPlan lPlan = getImpPlanDAO().find(pPlanId);
	if (lPlan == null) {
	    return "ERROR: Plan id " + pPlanId + " is not valid";
	}
	StringBuilder builder = new StringBuilder();
	System lSystem = getSystemDAO().findByName(pSystem);
	if (lSystem == null) {
	    return "ERROR: System Name " + pSystem + " should be valid";
	}
	SystemLoad systemLoad = getSystemLoadDAO().findBy(lPlan, lSystem);
	if (systemLoad == null) {
	    return "ERROR: System Name " + pSystem + " is not defined in the plan " + pPlanId;
	}
	if (systemLoad.getLoadDateTime() == null) {
	    return "ERROR: Failed to generate .cfg file as load date are missing for target system " + pSystem + ". ADL needs to add load dates in the implementation plan - " + pPlanId;
	}
	String param1 = lPlan.getId() + "_" + systemLoad.getSystemId().getName() + "_" + Constants.JENKINS_DATEFORMAT.get().format(systemLoad.getLoadDateTime()) + "0000";

	String param2 = systemLoad.getPutLevelId().getPutLevel();
	String param7 = "prod";
	if (systemLoad.getPutLevelId().getStatus().equals(Constants.PUTLevelOptions.DEVELOPMENT.name()) || systemLoad.getPutLevelId().getStatus().equals(Constants.PUTLevelOptions.PRE_PROD_CO_EXIST.name())) {
	    param7 = "devl";
	}

	StringBuilder param3 = new StringBuilder(param1);
	String[] lPlans = lPlan.getRelatedPlans().split(",");
	for (int i = 0; i < lPlans.length; i++) {
	    SystemLoad lDepLoad = getSystemLoadDAO().findBy(new ImpPlan(lPlans[i]), systemLoad.getSystemId());
	    if (lDepLoad != null) {
		if (lDepLoad.getLoadDateTime() == null) {
		    return "ERROR: Failed to generate .cfg file as load date are missing for target system " + pSystem + ". ADL needs to add load dates in the implementation plan - " + lPlans[i];
		}
		param3.append(",").append(lPlans[i]).append("_");
		param3.append(Constants.JENKINS_DATEFORMAT.get().format(lDepLoad.getLoadDateTime()));
	    }
	}
	// To-Do: Need to check, we have removed the segment dependency in
	// stagingDependentPlans during DVL and Stage
	// build cal. We need to conform whether same applicable to this.
	// As of Now, changing as per dvl and stage build call
	// List<String> stagingDepedendentPlans =
	// getImpPlanDAO().getStagingDepedendentPlans(lPlan.getId(),
	// systemLoad.getSystemId().getId(), new Date());
	// for (String stagingDepedendentPlan : stagingDepedendentPlans) {
	// param4.append(",").append(stagingDepedendentPlan);
	// }

	List<Object[]> stagingDepedendentPlans = getSystemLoadDAO().getDependentPlanByApproveDate(lPlan.getId(), systemLoad.getSystemId().getId(), systemLoad.getLoadDateTime(), Constants.PlanStatus.getAfterSubmitStatus().keySet(), systemLoad.getPutLevelId().getId());
	StringBuilder param4 = new StringBuilder(param1);
	Integer lPlanOrder = 1;
	Date lDate = new Date();
	for (Object[] lSysLoadAndPlan : stagingDepedendentPlans) {
	    SystemLoad lSysLoad = (SystemLoad) lSysLoadAndPlan[0];
	    // ImpPlan lOrderedPlan = (ImpPlan) lSysLoadAndPlan[1];
	    param4.append(",");
	    param4.append(lSysLoad.getPlanId().getId().toLowerCase()).append("_");
	    param4.append(Constants.JENKINS_DATEFORMAT.get().format(lSysLoad.getLoadDateTime()));
	    if (lDate.equals(lSysLoad.getLoadDateTime())) {
		lPlanOrder++;
	    } else {
		lPlanOrder = 1;
	    }
	    param4.append(new DecimalFormat("0000").format(lPlanOrder));
	    lDate = lSysLoad.getLoadDateTime();
	}

	builder.append(param1).append(" ").append(param2).append(" ").append(param7).append(" ").append(param3).append(" ").append(param4);
	return builder.toString();
    }

    @Transactional
    public String getFileSyncInfo(String implId, String file, String systemName) {
	if (implId == null || implId.isEmpty()) {
	    return "";
	}
	if (file == null || file.isEmpty()) {
	    return implId + "|NODATA";
	}
	List<String> lStatus = getCheckoutSegmentsDAO().findCommonFilesStatus(implId, file, systemName);
	if (lStatus.isEmpty()) {
	    return implId + "|NODATA";
	}
	List<String> lList = getCheckoutSegmentsDAO().findCommonFiles(implId, file);
	if (lList.isEmpty()) {
	    return implId + "|NODATA";
	}
	return implId.toLowerCase() + "|" + StringUtils.join(lList, "|");
    }

    @Transactional
    public String getTAPDetails(String planId, String systemName, String loadDate, boolean isforward, boolean preProdPlansOnly) {
	StringBuilder builder = new StringBuilder();
	List<SystemLoad> lLoads = new ArrayList<>();
	HashMap<String, List<String>> lPlanTypes = new HashMap<>();
	LOG.info("Plan Id " + planId + " System " + systemName + " Reload Date " + loadDate);
	try {
	    if (planId != null && systemName != null && loadDate == null) {
		System lSystem = getSystemDAO().findByName(systemName);
		SystemLoad lLoadTime = getSystemLoadDAO().findBy(new ImpPlan(planId), lSystem);

		lLoads = getSystemLoadDAO().getStagingDepedendentPlansWithDate(planId, lSystem.getId(), lLoadTime.getLoadDateTime(), Constants.PlanStatus.getApprovedStatusMap().keySet(), isforward);

		List<String> lPlanList = new ArrayList<>();
		lLoads.stream().forEach(t -> lPlanList.add(t.getPlanId().getId()));
		if (!lPlanList.isEmpty() && !preProdPlansOnly) {
		    lPlanTypes = getCheckoutSegmentsDAO().getFileTypesByPlan(lPlanList.toArray(new String[0]), new ArrayList(Constants.PlanStatus.getApprovedAndAboveStatus().keySet()));
		} else if (!lPlanList.isEmpty() && preProdPlansOnly) {
		    lPlanTypes = getCheckoutSegmentsDAO().getFileTypesByPlanforTravelport(lPlanList, new ArrayList(Constants.PlanStatus.getDeployedInPreProdToOnline().keySet()), lSystem.getId(), Constants.LOAD_SET_STATUS.ACTIVATED.name());
		}

	    } else if (planId != null && systemName == null && loadDate == null) {
		ImpPlan lPlan = getImpPlanDAO().findByPlanStatus(planId, new ArrayList(Constants.PlanStatus.getApprovedStatusMap().keySet()));
		if (lPlan != null && !preProdPlansOnly) {
		    lLoads = getSystemLoadDAO().findByImpPlan(lPlan);
		    lPlanTypes = getCheckoutSegmentsDAO().getFileTypesByPlan(new String[] { lPlan.getId() }, new ArrayList(Constants.PlanStatus.getApprovedAndAboveStatus().keySet()));
		} else if (lPlan != null && preProdPlansOnly) {
		    lLoads = getSystemLoadDAO().findByImpPlan(lPlan);
		    lPlanTypes = getCheckoutSegmentsDAO().getFileTypesByPlan(new String[] { lPlan.getId() }, new ArrayList(Constants.PlanStatus.getDeployedInPreProdToOnline().keySet()));
		}

	    } else if (planId == null && systemName != null && loadDate != null) {
		System lSystem = getSystemDAO().findByName(systemName);
		Date lLoadDate = Constants.JENKINS_DATEFORMAT.get().parse(loadDate);
		lLoads = getSystemLoadDAO().getStagingDepedendentPlansWithDate("", lSystem.getId(), lLoadDate, Constants.PlanStatus.getApprovedStatusMap().keySet(), isforward);
		List<String> lPlanList = new ArrayList<>();
		lLoads.stream().forEach(t -> lPlanList.add(t.getPlanId().getId()));
		if (!lPlanList.isEmpty() && !preProdPlansOnly) {
		    lPlanTypes = getCheckoutSegmentsDAO().getFileTypesByPlan(lPlanList.toArray(new String[0]), new ArrayList(Constants.PlanStatus.getApprovedAndAboveStatus().keySet()));
		} else if (!lPlanList.isEmpty() && preProdPlansOnly) {
		    lPlanTypes = getCheckoutSegmentsDAO().getFileTypesByPlanforTravelport(lPlanList, new ArrayList(Constants.PlanStatus.getDeployedInPreProdToOnline().keySet()), lSystem.getId(), Constants.LOAD_SET_STATUS.ACTIVATED.name());
		}

	    }

	    for (SystemLoad lLoad : lLoads) {
		Boolean isAux = false;
		List<Build> lbuildType = getBuildDAO().findByImpPlan(lLoad.getPlanId().getId());
		List<String> buildList = new ArrayList<>();
		for (Build lbuild : lbuildType) {
		    buildList.add(lbuild.getLoadSetType());
		}
		if (buildList.contains("A")) {
		    isAux = true;
		}
		if (lPlanTypes.get(lLoad.getPlanId().getId()) != null && lPlanTypes.get(lLoad.getPlanId().getId()).contains(Constants.FILE_TYPE.IBM.name())) {
		    if (!isAux) {
			builder.append(lLoad.getSystemId().getName()).append("\t").append(lLoad.getPlanId().getId()).append("\t").append("IBM").append("\t").append(Constants.JENKINS_DATEFORMAT.get().format(lLoad.getLoadDateTime())).append("\t").append(Constants.STAGE_IBM_BASE_PATH).append(lLoad.getPlanId().getId().toLowerCase()).append(File.separator).append(lLoad.getSystemId().getName().toLowerCase());
		    } else {
			builder.append(lLoad.getSystemId().getName()).append("\t").append(lLoad.getPlanId().getId()).append("\t").append("AUX").append("\t").append(Constants.JENKINS_DATEFORMAT.get().format(lLoad.getLoadDateTime())).append("\t").append(Constants.STAGE_IBM_BASE_PATH).append(lLoad.getPlanId().getId().toLowerCase()).append(File.separator).append(lLoad.getSystemId().getName().toLowerCase());
		    }
		    if (isforward) {
			String lPlanStatus = Constants.PlanStatus.valueOf(lLoad.getPlanId().getPlanStatus()).getDisplayName();
			builder.append("\t").append("\"").append(lPlanStatus).append("\"");
		    }
		    builder.append(java.lang.System.lineSeparator());
		}
		if (lPlanTypes.get(lLoad.getPlanId().getId()) != null && lPlanTypes.get(lLoad.getPlanId().getId()).contains(Constants.FILE_TYPE.NON_IBM.name())) {
		    if (!isAux) {
			builder.append(lLoad.getSystemId().getName()).append("\t").append(lLoad.getPlanId().getId()).append("\t").append("NON").append("\t").append(Constants.JENKINS_DATEFORMAT.get().format(lLoad.getLoadDateTime())).append("\t").append(Constants.STAGE_NON_IBM_BASE_PATH).append(lLoad.getPlanId().getId().toLowerCase()).append(File.separator).append(lLoad.getSystemId().getName().toLowerCase());
		    } else {
			builder.append(lLoad.getSystemId().getName()).append("\t").append(lLoad.getPlanId().getId()).append("\t").append("AUX").append("\t").append(Constants.JENKINS_DATEFORMAT.get().format(lLoad.getLoadDateTime())).append("\t").append(Constants.STAGE_NON_IBM_BASE_PATH).append(lLoad.getPlanId().getId().toLowerCase()).append(File.separator).append(lLoad.getSystemId().getName().toLowerCase());
		    }
		    if (isforward) {
			String lPlanStatus = Constants.PlanStatus.valueOf(lLoad.getPlanId().getPlanStatus()).getDisplayName();
			builder.append("\t").append("\"").append(lPlanStatus).append("\"");
		    }
		    builder.append(java.lang.System.lineSeparator());
		}
	    }
	    return builder.toString();
	} catch (Exception ex) {
	    LOG.error("Error in getting TAP Details", ex);
	}
	return "";
    }

    @Transactional
    public String getTAPDetailsForDelta(String planId, String systemName, String loadDate, boolean isforward, boolean preProdPlansOnly) {
	StringBuilder builder = new StringBuilder();
	List<SystemLoad> lLoads = new ArrayList<>();
	HashMap<String, List<String>> lPlanTypes = new HashMap<>();
	try {
	    if (planId != null && systemName != null && loadDate == null) {
		System lSystem = getSystemDAO().findByName(systemName);
		SystemLoad lLoadTime = getSystemLoadDAO().findBy(new ImpPlan(planId), lSystem);

		lLoads = getSystemLoadDAO().getStagingDepedendentPlansWithDate(planId, lSystem.getId(), lLoadTime.getLoadDateTime(), Constants.PlanStatus.getApprovedStatusMap().keySet(), isforward);

		List<String> lPlanList = new ArrayList<>();
		lLoads.stream().forEach(t -> lPlanList.add(t.getPlanId().getId()));
		if (!lPlanList.isEmpty() && !preProdPlansOnly) {
		    lPlanTypes = getCheckoutSegmentsDAO().getFileTypesByPlan(lPlanList.toArray(new String[0]));
		} else {
		    lPlanTypes = getCheckoutSegmentsDAO().getPreProdAndAbovePlanForDelta(lPlanList.toArray(new String[0]));
		}
	    } else if (planId != null && systemName == null && loadDate == null) {
		ImpPlan lPlan = getImpPlanDAO().findByPlanStatus(planId, new ArrayList(Constants.PlanStatus.getApprovedStatusMap().keySet()));
		if (lPlan != null && !preProdPlansOnly) {
		    lLoads = getSystemLoadDAO().findByImpPlan(lPlan);
		    lPlanTypes = getCheckoutSegmentsDAO().getFileTypesByPlan(new String[] { lPlan.getId() });
		} else if (lPlan != null && preProdPlansOnly) {
		    lLoads = getSystemLoadDAO().findByImpPlan(lPlan);
		    lPlanTypes = getCheckoutSegmentsDAO().getPreProdAndAbovePlanForDelta(new String[] { lPlan.getId() });
		}

	    } else if (planId == null && systemName != null && loadDate != null) {
		System lSystem = getSystemDAO().findByName(systemName);
		Date lLoadDate = Constants.JENKINS_DATEFORMAT.get().parse(loadDate);
		lLoads = getSystemLoadDAO().getStagingDepedendentPlansWithDate("", lSystem.getId(), lLoadDate, Constants.PlanStatus.getApprovedStatusMap().keySet(), isforward);
		List<String> lPlanList = new ArrayList<>();
		lLoads.stream().forEach(t -> lPlanList.add(t.getPlanId().getId()));
		if (!lPlanList.isEmpty() && !preProdPlansOnly) {
		    lPlanTypes = getCheckoutSegmentsDAO().getFileTypesByPlan(lPlanList.toArray(new String[0]));
		} else if (!lPlanList.isEmpty() && preProdPlansOnly) {
		    lPlanTypes = getCheckoutSegmentsDAO().getDeltaPlansDeployedInPreProd(lPlanList, lSystem.getId(), Constants.LOAD_SET_STATUS.ACTIVATED.name());
		}
	    }

	    for (SystemLoad lLoad : lLoads) {
		Boolean isAux = false;
		List<Build> lbuildType = getBuildDAO().findByImpPlan(lLoad.getPlanId().getId());
		List<String> buildList = new ArrayList<>();
		for (Build lbuild : lbuildType) {
		    buildList.add(lbuild.getLoadSetType());
		}
		if (buildList.contains("A")) {
		    isAux = true;
		}
		if (lPlanTypes.get(lLoad.getPlanId().getId()) != null && lPlanTypes.get(lLoad.getPlanId().getId()).contains(Constants.FILE_TYPE.IBM.name())) {
		    if (!isAux) {
			builder.append(lLoad.getSystemId().getName()).append("\t").append(lLoad.getPlanId().getId()).append("\t").append("IBM").append("\t").append(Constants.JENKINS_DATEFORMAT.get().format(lLoad.getLoadDateTime())).append("\t").append(Constants.STAGE_IBM_BASE_PATH).append(lLoad.getPlanId().getId().toLowerCase()).append(File.separator).append(lLoad.getSystemId().getName().toLowerCase());
		    } else {
			builder.append(lLoad.getSystemId().getName()).append("\t").append(lLoad.getPlanId().getId()).append("\t").append("AUX").append("\t").append(Constants.JENKINS_DATEFORMAT.get().format(lLoad.getLoadDateTime())).append("\t").append(Constants.STAGE_IBM_BASE_PATH).append(lLoad.getPlanId().getId().toLowerCase()).append(File.separator).append(lLoad.getSystemId().getName().toLowerCase());
		    }
		    if (isforward) {
			String lPlanStatus = Constants.PlanStatus.valueOf(lLoad.getPlanId().getPlanStatus()).getDisplayName();
			builder.append("\t").append("\"").append(lPlanStatus).append("\"");
		    }
		    builder.append(java.lang.System.lineSeparator());
		}
		if (lPlanTypes.get(lLoad.getPlanId().getId()) != null && lPlanTypes.get(lLoad.getPlanId().getId()).contains(Constants.FILE_TYPE.NON_IBM.name())) {
		    if (!isAux) {
			builder.append(lLoad.getSystemId().getName()).append("\t").append(lLoad.getPlanId().getId()).append("\t").append("NON").append("\t").append(Constants.JENKINS_DATEFORMAT.get().format(lLoad.getLoadDateTime())).append("\t").append(Constants.STAGE_NON_IBM_BASE_PATH).append(lLoad.getPlanId().getId().toLowerCase()).append(File.separator).append(lLoad.getSystemId().getName().toLowerCase());
		    } else {
			builder.append(lLoad.getSystemId().getName()).append("\t").append(lLoad.getPlanId().getId()).append("\t").append("AUX").append("\t").append(Constants.JENKINS_DATEFORMAT.get().format(lLoad.getLoadDateTime())).append("\t").append(Constants.STAGE_NON_IBM_BASE_PATH).append(lLoad.getPlanId().getId().toLowerCase()).append(File.separator).append(lLoad.getSystemId().getName().toLowerCase());
		    }
		    if (isforward) {
			String lPlanStatus = Constants.PlanStatus.valueOf(lLoad.getPlanId().getPlanStatus()).getDisplayName();
			builder.append("\t").append("\"").append(lPlanStatus).append("\"");
		    }
		    builder.append(java.lang.System.lineSeparator());
		}
	    }
	    return builder.toString();
	} catch (Exception ex) {
	    LOG.error("Error in getting TAP Details", ex);
	}
	return "";
    }

    public HashMap<String, String> getNONIBMSourceArtificatExtListWithFilePath() {
	HashMap<String, String> lNONIBMSourceArtificatExtListWithFilePath = new HashMap<>();
	for (String fileType : Constants.SourceArtificatExtension.getNONIBMSourceArtificatExtList()) {
	    lNONIBMSourceArtificatExtListWithFilePath.put(fileType, File.separator + Constants.getSourceArtifactDirectory(fileType));
	}
	return lNONIBMSourceArtificatExtListWithFilePath;
    }

    @Transactional
    public String getPutDeployDate(String putName, String systemName) {
	try {
	    System lSystem = getSystemDAO().findByName(systemName);
	    if (lSystem != null) {
		PutLevel lPutLevel = getPutLevelDAO().findBySystemAndPutName(putName, lSystem.getId());
		if (lPutLevel != null) {
		    return lPutLevel.getPutLevel().toLowerCase() + " " + Constants.JENKINS_DATEFORMAT.get().format(lPutLevel.getPutDateTime());
		}
	    }
	} catch (Exception Ex) {
	    LOG.error("Unable to get Put Details", Ex);
	    return "";
	}
	return "";
    }

    @Transactional
    public void publishMessage(String topic, String message, String userId, String planId) {
	JSONResponse lJSONResponse = new JSONResponse();
	JobDetails lwssPublishMsg = new JobDetails();
	lwssPublishMsg.setStatus(message);
	Constants.Channels lChannel = Constants.Channels.valueOf(topic);
	if (userId.equals("*")) {
	    String lUser = getCacheClient().getSocketUserMap().get(planId);
	    if (lUser != null) {
		wsserver.sendMessage(lChannel, lUser, planId, lwssPublishMsg);
	    } else {
		wsserver.sendMessage(lChannel, userId, planId, lwssPublishMsg);
	    }
	} else {
	    wsserver.sendMessage(lChannel, userId, planId, lwssPublishMsg);
	}
    }

    @Transactional
    public StringBuilder getAllNonProdSegments(String pCompany, String pFileFilter, String pBranch) {
	List<CheckoutSegments> lGitSearchResults = getCheckoutSegmentsDAO().findAllNonProd(pCompany, pFileFilter, pBranch);
	StringBuilder builder = new StringBuilder();
	for (CheckoutSegments lGitSearchResult : lGitSearchResults) {
	    builder.append(lGitSearchResult.getProgramName()).append(",").append(lGitSearchResult.getPlanId().getPlanStatus()).append(",").append(lGitSearchResult.getPlanId().getId()).append(",");
	    if (lGitSearchResult.getSystemLoad().getLoadDateTime() != null) {
		builder.append(Constants.APP_DATE_TIME_FORMAT.get().format(lGitSearchResult.getSystemLoad().getLoadDateTime())).append(",");
	    } else {
		builder.append("NO_LOAD_DATE").append(",");
	    }
	    builder.append(lGitSearchResult.getFileName()).append(",").append(FilenameUtils.separatorsToUnix(File.separator + gitConfig.getGitProdPath() + pCompany + File.separator + gitConfig.getGitSourcePath() + lGitSearchResult.getPlanId().getId().toLowerCase() + ".git" + File.separator + "COMMIT_ID" + File.separator + lGitSearchResult.getFileName())).append(",").append(lGitSearchResult.getFuncArea()).append(java.lang.System.lineSeparator());
	}
	return builder;
    }

    public JSONResponse getSystemBasedMetadata(String retriveType, String targetSystem, String impPlan, String planStatusids, String progName, String progType, String onlineVersion, String fallbackVersion, String fromLoadDate, String toLoadDate, String funcArea, String devId, String devMangerId, String outputPath, Boolean planStatusList, Boolean ldapIdList, String userName, String role, String Dependent) throws Exception {

	JSONResponse lResponse = new JSONResponse();

	// 2115 - System Validation - Tool should not allow for targetSystem,
	// retriveType or outputPath parameter
	// combination
	if (retriveType != null && targetSystem != null && impPlan == null && planStatusids == null && progName == null && progType == null && onlineVersion == null && fallbackVersion == null && fromLoadDate == null && toLoadDate == null && funcArea == null && devId == null && devMangerId == null && userName == null && role == null && planStatusList == null && ldapIdList == null) {
	    lResponse.setErrorMessage("Kindly include more filter option with parameter targetSystem, Valid parameters are [impPlan, planStatusids, progName, progType, " + "onlineVersion, fallbackVersion, fromLoadDate, toLoadDate, funcArea, devId, devMangerId]");
	    lResponse.setStatus(false);
	    return lResponse;
	}

	// Validation - If Retrieve Type = Program then parameter output path should be
	// provided.
	if ("Program".equalsIgnoreCase(retriveType) && (outputPath == null || outputPath.isEmpty())) {
	    lResponse.setErrorMessage("Output path is mandatory for program retrival type");
	    lResponse.setStatus(false);
	    return lResponse;
	}

	// Validation - LdapIdList and role parameter are mutually execlusive.
	// Valid Roles - Developer/DevManger
	List<String> lRoleList = Arrays.asList(Constants.UserGroup.DevManager.name(), Constants.UserGroup.Developer.name());

	if (ldapIdList != null && ldapIdList && (role == null || !lRoleList.contains(role))) {
	    lResponse.setErrorMessage("role parameter is mandatory for fetching LDAP Id list, valid roles are " + String.join(",", lRoleList));
	    lResponse.setStatus(false);
	    return lResponse;
	} else if (role != null && ldapIdList == null) {
	    lResponse.setErrorMessage("ldapIdList parameter is missing in the paramaters");
	    lResponse.setStatus(false);
	    return lResponse;
	}

	if (ldapIdList != null && ldapIdList) {

	    Map<String, String> lUserList = new HashMap();
	    // Get Users List
	    List<LDAPGroup> lGroups = getLdapGroupConfig().getLdapRolesMap().get(role);
	    if (lGroups != null && !lGroups.isEmpty()) {
		SortedSet<User> lRoleUserList = getLDAPAuthenticatorImpl().getLinuxUsers(lGroups);
		for (User lUser : lRoleUserList) {
		    lUserList.put(lUser.getId(), lUser.getDisplayName());
		}
	    }

	    switch (retriveType.toLowerCase()) {
	    case "list":
		String outmsg = getCommonHelper().getFormattedOutputForLDAPList(lUserList, role);
		lResponse.setData(outmsg.length() == 0 ? "No Records Found" : outmsg);
		lResponse.setStatus(Boolean.TRUE);
		return lResponse;

	    case "program":
		List<String> lDataToWrite = new ArrayList();
		Map<String, String> result = new LinkedHashMap<>();
		lUserList.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(x -> result.put(x.getKey(), x.getValue()));
		result.entrySet().forEach((t) -> {
		    lDataToWrite.add(t.getKey() + " " + t.getValue() + " " + role);
		});
		lResponse = getCommonHelper().writeDataIntoFile(targetSystem, outputPath, lDataToWrite);
		if (!lResponse.getStatus()) {
		    LOG.info("Unable to write the file " + lResponse.getErrorMessage());
		    lResponse.setErrorMessage("Return Code: 1");
		    lResponse.setStatus(Boolean.FALSE);
		} else {
		    lResponse.setData(lResponse.getStatus() ? "Return Code: 0\n" + lResponse.getData() : "");
		}
		return lResponse;
	    }
	}

	if (planStatusList != null && planStatusList) {
	    Map<Integer, String> lPlanStatusList = Constants.PlanStatus.getStatusForGenericAPI();
	    switch (retriveType.toLowerCase()) {
	    case "list":
		String outmsg = getCommonHelper().getFormattedOutput(lPlanStatusList);
		lResponse.setData(outmsg.length() == 0 ? "No Records Found" : outmsg);
		lResponse.setStatus(Boolean.TRUE);
		return lResponse;

	    case "program":
		List<String> lDataToWrite = new ArrayList();
		lPlanStatusList.entrySet().forEach((t) -> {
		    lDataToWrite.add(t.getKey() + " " + t.getValue());
		});
		lResponse = getCommonHelper().writeDataIntoFile(targetSystem, outputPath, lDataToWrite);
		if (!lResponse.getStatus()) {
		    LOG.info("Unable to write the file " + lResponse.getErrorMessage());
		    lResponse.setErrorMessage("Return Code: 1");
		    lResponse.setStatus(Boolean.FALSE);
		} else {
		    lResponse.setData(lResponse.getStatus() ? "Return Code: 0\n" + lResponse.getData() : "");
		}
		return lResponse;
	    }
	}

	if ((impPlan != null || planStatusids != null) && (onlineVersion != null || fallbackVersion != null)) {
	    lResponse.setErrorMessage("Plan Id/Plan status are mutually exclusive with Online/Fallback version.");
	    lResponse.setStatus(false);
	    return lResponse;
	}

	List<String> impPlans = new ArrayList<>();
	if (impPlan != null && !impPlan.isEmpty()) {
	    impPlans = Arrays.asList(impPlan.split(",")).stream().map(e -> e.trim().toUpperCase()).collect(Collectors.toList());
	}

	// Plan Status ids
	Set<String> planStatus = commonHelper.getPlanStatusBasedOnId(planStatusids);

	List<String> progNames = null;
	if (progName != null && !progName.isEmpty()) {
	    progNames = Arrays.asList(progName.trim().split(",")).stream().map(e -> e.trim()).collect(Collectors.toList());
	}

	List<String> progTypes = null;
	if (progType != null && !progType.isEmpty()) {
	    progTypes = Arrays.asList(progType.trim().split(",")).stream().map(e -> e.trim().toUpperCase()).collect(Collectors.toList());
	}

	Set<Integer> onlineVers = null;
	if (onlineVersion != null && !onlineVersion.isEmpty()) {
	    onlineVers = commonHelper.getListOfValuesFromString(onlineVersion);
	}

	Set<Integer> fallbackVers = null;
	if (fallbackVersion != null && !fallbackVersion.isEmpty()) {
	    fallbackVers = commonHelper.getListOfValuesFromString(fallbackVersion);
	}

	Date startLoadDate = null;
	if (fromLoadDate != null && !fromLoadDate.trim().isEmpty()) {
	    startLoadDate = new SimpleDateFormat("yyyyMMdd").parse(fromLoadDate);
	}

	Date endLoadDate = null;
	if (toLoadDate != null && !toLoadDate.trim().isEmpty()) {
	    endLoadDate = new SimpleDateFormat("yyyyMMdd").parse(toLoadDate);
	}

	List<String> funAreas = new ArrayList<>();
	if (funcArea != null && !funcArea.trim().isEmpty()) {
	    funAreas = Arrays.asList(funcArea.trim().split(",")).stream().map(e -> e.trim().toUpperCase()).collect(Collectors.toList());
	}

	List<String> allowedStatus = new ArrayList<>();
	if (onlineVersion != null && !onlineVersion.isEmpty()) {
	    allowedStatus.add(Constants.PlanStatus.ONLINE.name());
	}
	if (fallbackVersion != null && !fallbackVersion.isEmpty()) {
	    allowedStatus.add(Constants.PlanStatus.FALLBACK.name());
	}

	List<String> devIds = new ArrayList<>();
	if (devId != null && !devId.isEmpty()) {
	    devIds = Arrays.asList(devId.trim().split(",")).stream().map(e -> e.trim().toUpperCase()).collect(Collectors.toList());
	}

	List<String> devMangerIds = new ArrayList<>();
	if (devMangerId != null && !devMangerId.isEmpty()) {
	    devMangerIds = Arrays.asList(devMangerId.trim().split(",")).stream().map(e -> e.trim().toUpperCase()).collect(Collectors.toList());
	}

	List<SystemBasedMetaData> metaData = getImpPlanDAO().getSystemBasedMetaData(targetSystem, progNames, fromLoadDate, toLoadDate);
	LOG.info("Meta data value: " + metaData.size());
	HashMap<String, List<SystemBasedMetaData>> metaDataMap = new HashMap<>();
	List<SystemBasedMetaData> lDepMetaData = new ArrayList<>();
	List<SystemBasedMetaData> finalMetaData = new ArrayList<>();
	for (SystemBasedMetaData val : metaData) {

	    String key = (val.getPlanid() + "-" + val.getImpid() + "-" + val.getLoadcategory()).toUpperCase();
	    // Check Imp Plan ids
	    if (impPlans != null && !impPlans.isEmpty() && !impPlans.contains(val.getPlanid().toUpperCase())) {
		continue;
	    }

	    // Plan Status
	    if (planStatus != null && !planStatus.isEmpty() && !planStatus.contains(val.getPlanstatus())) {
		continue;
	    }

	    // Prog Types
	    if (progTypes != null && !progTypes.isEmpty() && !progTypes.contains(Arrays.asList(val.getProgname().split("\\.")).get(1).toUpperCase())) {
		continue;
	    }

	    // If Online or fallback version given, then other Plan status shouldn't allowed
	    if (!allowedStatus.isEmpty() && !allowedStatus.contains(val.getPlanstatus().toUpperCase())) {
		continue;
	    }

	    // Online versions
	    if (onlineVers != null && !onlineVers.isEmpty() && val.getPlanstatus().equalsIgnoreCase(Constants.PlanStatus.ONLINE.name()) && !onlineVers.contains(val.getStatusrank() - 1)) {
		continue;
	    }

	    // Fallback versions
	    if (fallbackVers != null && !fallbackVers.isEmpty() && val.getPlanstatus().equalsIgnoreCase(Constants.PlanStatus.FALLBACK.name()) && !fallbackVers.contains(val.getStatusrank() - 1)) {
		continue;
	    }

	    // Func Area
	    if (funAreas != null && !funAreas.isEmpty() && !funAreas.contains(val.getFuncarea().toUpperCase())) {
		continue;
	    }

	    // Dev Id
	    if (devIds != null && !devIds.isEmpty() && (val.getDevid() == null || !devIds.contains(val.getDevid().toUpperCase()))) {
		continue;
	    }

	    // Dev manager Id
	    if (devMangerIds != null && !devMangerIds.isEmpty() && (val.getDevmanager() == null || !devMangerIds.contains(val.getDevmanager().toUpperCase()))) {
		continue;
	    }
	    // Display Name
	    if ((userName != null && !userName.isEmpty()) && ((val.getDevelopername() == null || !val.getDevelopername().toUpperCase().contains(userName.toUpperCase())) && (val.getDevmanagername() == null || !val.getDevmanagername().toUpperCase().contains(userName.toUpperCase())))) {
		continue;
	    }

	    // Set Mail id and Name based on given id
	    commonHelper.setMailAndNameBasedOnId(val);
	    lDepMetaData.add(val);
	    if (metaDataMap.containsKey(key)) {
		metaDataMap.get(key).add(val);
	    } else {
		List<SystemBasedMetaData> temp = new ArrayList<>();
		temp.add(val);
		metaDataMap.put(key, temp);
	    }
	}
	HashMap<String, String> lDepPlanMap = new HashMap<>();
	if (Dependent.equalsIgnoreCase("Y")) {
	    List<String> lDepPlansIds = lDepMetaData.stream().map(val -> val.getPlanid()).filter(t -> (t.startsWith("T") || t.startsWith("D"))).collect(Collectors.toList());
	    lDepPlanMap = getCommonHelper().getPlanBasedDependencyList(lDepPlansIds, null, 0, 0);
	}
	if (retriveType.equalsIgnoreCase("List")) {
	    finalMetaData = commonHelper.getUniqueDataMetaData(metaDataMap);
	    String output = commonHelper.getFormattedOutput(finalMetaData, lDepPlanMap, Dependent);
	    output = output.isEmpty() ? "No Records Found" : output;

	    lResponse.setData(output);
	    lResponse.setStatus(true);
	} else {
	    List<SystemBasedMetaData> metaDatas = new ArrayList<>();
	    metaDataMap.forEach((key, values) -> {
		values.forEach(val -> {
		    metaDatas.add(val);
		});
	    });
	    JSONResponse lWriteResponse = commonHelper.writeDataIntoFile(metaDatas, outputPath);
	    if (lWriteResponse.getStatus()) {
		lResponse.setData("Return Code: 0");
	    } else {
		lResponse.setData("Return Code: 1");
	    }
	    lResponse.setStatus(true);
	}
	return lResponse;
    }

    @Transactional
    public StringBuilder getPutLevelInfo(String pCompany, String pSystem) {
	StringBuilder builder = new StringBuilder();
	List<String> lStatusList = new ArrayList<>();
	lStatusList.add(Constants.PUTLevelOptions.PRODUCTION.name());
	lStatusList.add(Constants.PUTLevelOptions.LOCKDOWN.name());
	lStatusList.add(Constants.PUTLevelOptions.DEVELOPMENT.name());
	List<PutLevel> lPutLevelList = getPutLevelDAO().findPutLevelBySys(pCompany, pSystem, lStatusList);
	for (PutLevel lPutLevel : lPutLevelList) {
	    builder.append(lPutLevel.getSystemId().getName()).append("|").append(lPutLevel.getPutLevel()).append("|");
	    if (lPutLevel.getPutDateTime() != null) {
		builder.append(Constants.JENKINS_DATEFORMAT.get().format(lPutLevel.getPutDateTime())).append("|");
	    } else {
		builder.append("NO_DEPLOYMENT_DATE").append("|");
	    }
	    builder.append(lPutLevel.getStatus()).append(java.lang.System.lineSeparator());
	}
	return builder;

    }

    @Transactional
    public JSONResponse dofilterTest(JSONResponse prodFileSearch, String targetSystem, String implId) {
	JSONResponse lResponse = new JSONResponse();
	Implementation lImplementation = getImplementationDAO().find(implId);
	// LOG.info("lSearchResult:" + prodFileSearch.getData());
	try {

	    Set<String> lBranchList = new HashSet<>();
	    Map<String, List<CheckoutSegments>> lSegmentsMap = new HashMap<>();
	    Map<String, GitBranchSearchResult> lTempGitSearchSegments = new HashMap<>();
	    Collection<GitSearchResult> lSearchResult = (Collection<GitSearchResult>) prodFileSearch.getData();
	    List<GitSearchResult> lfinal = new ArrayList<>();
	    // LOG.info("lSearchResult:" + lSearchResult);

	    for (GitSearchResult repo : lSearchResult) {
		List<GitBranchSearchResult> gitBranchSearchResult = new ArrayList<>();
		List<GitBranchSearchResult> lBranches = repo.getBranch();
		long count = lBranches.stream().filter(p -> p.getIsBranchSelected()).count();
		for (GitBranchSearchResult lBranch : lBranches) {
		    if (lBranch.getIsBranchSelected()) {
			String lMasterBranchName = lBranch.getTargetSystem().replaceAll(Constants.BRANCH_MASTER, "").toUpperCase();
			;
			// LOG.info("Master branch: " + lMasterBranchName + " Target Sytem: " +
			// targetSystem);
			if (lMasterBranchName.equalsIgnoreCase(targetSystem) && lBranch.getRefStatus().equals("Online")) {
			    lBranchList.add(lMasterBranchName.toLowerCase());
			    String lSystem = lMasterBranchName.replace(Constants.BRANCH_MASTER, "");
			    lBranchList.add(lMasterBranchName.replace("master", lImplementation.getId().toLowerCase()));
			    CheckoutSegments lSegment = new CheckoutSegments();
			    BeanUtils.copyProperties(lSegment, lBranch);
			    BeanUtils.copyProperties(lSegment, repo);
			    lSegment.setTargetSystem(lSegment.getTargetSystem().replaceAll(Constants.BRANCH_MASTER, "").toUpperCase());
			    lSegment.setCommonFile(count > 1);
			    if (lSegmentsMap.get(lSystem) == null) {
				lSegmentsMap.put(lSystem, new ArrayList<>());
			    }
			    lSegmentsMap.get(lSystem).add(lSegment);
			    String idString = CheckoutUtils.getIdString(lSegment);
			    lTempGitSearchSegments.put(idString, lBranch);
			    gitBranchSearchResult.add(lBranch);

			    // LOG.info("target system info" + lTempGitSearchSegments);
			}
		    }
		}
		if (!gitBranchSearchResult.isEmpty()) {
		    repo.setBranch(gitBranchSearchResult);
		    lfinal.add(repo);
		}

		// LOG.info("gitBranchSearchResult:" + gitBranchSearchResult);
		// LOG.info("lfinal:" + lfinal);
	    }

	    lResponse.setStatus(true);
	    lResponse.setData(lfinal);
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Error in listing files from Production ", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getFuncPackSrcArtifacts(String repoName, String progName, Integer limit, Integer offset, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse jsonResponse = new JSONResponse();
	try {
	    LOG.info("Repo Name " + repoName);
	    repoName = repoName.toLowerCase() + ".git";
	    progName = progName != null ? progName.toLowerCase() : progName;
	    List<RepoBasedSrcArtifacts> repoBasedSrcArtifacts = getGitSearchDAO().getRepoBasedSrcArtifcats(repoName, progName, limit, offset, pOrderBy);

	    repoBasedSrcArtifacts.forEach(srcArtifact -> {
		String sourceArtifactLink = gITConfig.getGitblitTicketUrl() + FilenameUtils.separatorsToUnix(srcArtifact.getSubsourcerepo().replace("/", "!") + File.separator + srcArtifact.getSorucecommitid() + File.separator + srcArtifact.getFilename().replace("/", "!"));
		srcArtifact.setSrcArtifactLink(sourceArtifactLink.replace(Constants.TICKETS, Constants.BLOB));
	    });
	    LOG.info("data >>>" + repoBasedSrcArtifacts);
	    jsonResponse.setData(repoBasedSrcArtifacts);
	    jsonResponse.setCount(getGitSearchDAO().getRepoBasedSrcArtifcatsCount(repoName, progName));
	    jsonResponse.setStatus(true);
	} catch (Exception ex) {
	    LOG.error("Error in Getting Data  ", ex);
	    jsonResponse.setErrorMessage("Error in Getting Data");
	    jsonResponse.setStatus(Boolean.TRUE);
	}
	return jsonResponse;
    }

    public JSONResponse exportFuncPackSrcArtifacts(String repoName, String progName, Integer limit, Integer offset, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	repoName = repoName.toLowerCase() + ".git";
	progName = progName != null ? progName.toLowerCase() : progName;
	RepoBasedSrcArtifactExport creator = new RepoBasedSrcArtifactExport();
	creator.addSearchResult(getGitSearchDAO().getRepoBasedSrcArtifcats(repoName, progName, limit, offset, pOrderBy));
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
    public StringBuilder getNewFileList(String planId, String pSystem) {
	StringBuilder builder = new StringBuilder();
	List<CheckoutSegments> checkoutList = getCheckoutSegmentsDAO().findNewFileCreationPlanBySystem(planId, pSystem);
	for (CheckoutSegments pCheckout : checkoutList) {
	    builder.append(pCheckout.getFileName()).append(",").append(pCheckout.getSourceUrl()).append(java.lang.System.lineSeparator());
	}
	return builder;

    }

    @Transactional
    public StringBuilder getFileList(String planId, String pSystem) {
	StringBuilder builder = new StringBuilder();
	List<CheckoutSegments> checkoutList = getCheckoutSegmentsDAO().findPlanBySystem(planId, pSystem);
	for (CheckoutSegments pCheckout : checkoutList) {
	    List<String> uRLString = Arrays.asList(pCheckout.getSourceUrl().split("/"));
	    builder.append(pCheckout.getFileName()).append(",").append(uRLString.get(uRLString.size() - 1)).append(java.lang.System.lineSeparator());
	}
	return builder;
    }

    @Transactional
    public String getDslDetailBasedOnSystem(String systemName) {
	StringBuilder finalSystemVpars = new StringBuilder();
	List<DSLDetailsForm> dslDetails = getSystemLoadDAO().getDslDetails(systemName);
	LOG.info("DSL details :- " + " System Name:- " + systemName + "  Count :-  " + dslDetails.size());
	HashMap<String, List<String>> sysAndLoadSetsMap = new HashMap<>();
	if (dslDetails != null && !dslDetails.isEmpty()) {
	    dslDetails.forEach(dslFile -> {

		String key = dslFile.getSystemname() + "_" + dslFile.getVparname();
		if (sysAndLoadSetsMap.containsKey(key)) {
		    sysAndLoadSetsMap.get(key).add(dslFile.getLoadsetname() + "_" + dslFile.getLoadDate() + "_" + dslFile.getLoadtime());
		} else {
		    List<String> temp = new ArrayList<>();
		    temp.add(dslFile.getLoadsetname() + "_" + dslFile.getLoadDate() + "_" + dslFile.getLoadtime());
		    sysAndLoadSetsMap.put(key, temp);
		}

	    });
	    sysAndLoadSetsMap.forEach((key, value) -> {
		String loadSetNames = value.stream().collect(Collectors.joining(","));
		finalSystemVpars.append(key + "|" + DigestUtils.md5Hex(loadSetNames) + ":" + loadSetNames).append(java.lang.System.lineSeparator());
	    });
	    if (finalSystemVpars.length() > 1) {
		finalSystemVpars.setLength(finalSystemVpars.length() - 1);
	    }
	    return finalSystemVpars.toString();

	} else {
	    return "NOT_FOUND";
	}
    }

    @Transactional
    public JSONResponse getTrackerData(String planId) {
	JSONResponse lResponse = new JSONResponse();
	PlanTrackerData lPlanTrackerData = new PlanTrackerData();
	ImpPlan lPlan = getImpPlanDAO().find(planId);
	lPlanTrackerData.setPlanId(lPlan.getId());
	if (lPlan.getMacroHeader()) {
	    lPlanTrackerData.setStages(Constants.MacroPlanTrackStatus.getMacroPlanTrackStatus());
	    getImpPlanTrackHelper().setCurrentStage(lPlanTrackerData, lPlan);
	    // lPlanTrackerData.setCurrentStage(Constants.MacroPlanTrackStatus.getMacroPlanTrackStatus().get(RandomUtils.nextInt(lPlanTrackerData.getStages().size())));
	} else {
	    lPlanTrackerData.setStages(Constants.ImpPlanTrackStatus.getImpPlanTrackStatus());
	    getImpPlanTrackHelper().setCurrentStage(lPlanTrackerData, lPlan);
	}

	lPlanTrackerData.getImplementations().addAll(getImplementationTrackHelper().getImplementationStatus(lPlan));
	lResponse.setData(lPlanTrackerData);
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    public JSONResponse removePlanFromPPRejectCache(String planId) {
	JSONResponse lReturn = new JSONResponse();
	lReturn.setStatus(Boolean.TRUE);
	try {
	    getPreProdRejectListener().removePlan(planId);
	} catch (Exception ex) {
	    LOG.info("Unable to remove the plan from PPRejection Cache - " + planId, ex);
	    throw new WorkflowException("Unable to remove the plan from PP Rejection Cache", ex);
	}
	return lReturn;
    }

    public void updateGIPorts(String userId, Integer portNo, String ipAddr) {
	if (lDAPAuthenticatorImpl.getLinuxUsers(ldapGroupConfig.getDeveloperGroups()).contains(new User(userId))) {
	    List<GiPorts> lUserPorts = giPortsDAO.findByUserId(userId);
	    for (GiPorts lUserPort : lUserPorts) {
		giPortsDAO.hardDelete(lUserPort);
	    }
	    GiPorts lPort = new GiPorts();
	    lPort.setUserId(userId);
	    lPort.setPortNo(portNo);
	    lPort.setIpAddr(ipAddr);
	    User lUser = lDAPAuthenticatorImpl.getServiceUser();
	    giPortsDAO.save(lUser, lPort);
	    wsserver.sendMessage(Constants.Channels.GI_PORT_CHANGE, userId, "GI Ports Changed");
	}
    }

    public void deletGIPorts(String userId) {
	List<GiPorts> lUserPorts = giPortsDAO.findByUserId(userId);
	for (GiPorts lUserPort : lUserPorts) {
	    giPortsDAO.hardDelete(lUserPort);
	}
	wsserver.sendMessage(Constants.Channels.GI_PORT_CHANGE, userId, "GI Ports Changed");
    }

    public JSONResponse getGIPorts(String userId) {
	JSONResponse lJSONResponse = new JSONResponse();
	List<GiPorts> lUserPorts = giPortsDAO.findByUserId(userId);
	if (lUserPorts.size() == 1) {
	    lJSONResponse.setStatus(true);
	    lJSONResponse.setData(lUserPorts.get(0));
	} else {
	    lJSONResponse.setStatus(false);
	    lJSONResponse.setErrorMessage("A port association was not found for the given user ID");
	}
	return lJSONResponse;
    }

    public JSONResponse planIdCleanupFromCache(String progressType, String planId) {
	JSONResponse lJSONResponse = new JSONResponse();
	lJSONResponse.setStatus(true);
	getCacheClient().getInProgressRelatedPlanMap().remove(planId);
	LOG.info("PlanId: " + planId + " has been removed from cache Process type: " + progressType);
	return lJSONResponse;
    }

    @Transactional
    public StringBuilder getAllProdSegments(String companyName, String fileName, String systemName) {
	StringBuilder lReturnMsg = new StringBuilder("");
	try {
	    Set<String> lAllowedRepos = getGitHelper().getAllReposList(companyName);
	    Set<String> lObsRepos = getGitHelper().getObsRepoList(companyName);
	    lAllowedRepos.removeAll(lObsRepos);

	    // Get Put levels for each systems
	    List<String> putLevelStatus = new ArrayList<>();
	    putLevelStatus.add(Constants.PUTLevelOptions.PRODUCTION.name());
	    HashMap<String, String> tarSysAndPutLevel = gitHelper.getSystemBasedPutLevels(putLevelStatus);

	    List<GitProdSearchDb> searchRes = getGitSearchDAO().findByProgramName(companyName, fileName, Constants.PRODSearchType.ONLINE_ONLY, null, null, Arrays.asList("Fallback"));

	    for (GitProdSearchDb git : searchRes) {
		if (!git.getTargetSystem().contains(systemName)) {
		    continue;
		}

		String tarSystem = git.getTargetSystem().replace("master_", "").toUpperCase();
		// ZTPFM-1271: IBM seg, based on target system prod put level isn't matching
		// with the func area, then those
		// segments should be ignored.
		if (git.getFileType().equalsIgnoreCase(Constants.FILE_TYPE.IBM.name()) && !tarSysAndPutLevel.get(tarSystem).equalsIgnoreCase(git.getFuncArea())) {
		    continue;
		}

		String refStatus = git.getRefStatus().equalsIgnoreCase("Online") && git.getVersion() > 1 ? git.getRefStatus() + "-" + (git.getVersion() - 1) : git.getRefStatus();

		lReturnMsg.append(git.getProgramName()).append(",").append(refStatus).append(",").append(git.getRefPlan()).append(",").append(Constants.APP_DATE_TIME_FORMAT.get().format(git.getRefLoadDateTime())).append(",").append(git.getFileName()).append(",").append("/").append(getGitHelper().getRepositoryNameBySourceURL(git.getSourceUrl())).append("/").append(git.getSourceCommitId().substring(0, 6)).append("/").append(git.getFileName()).append(",").append(git.getFuncArea())
			.append(java.lang.System.lineSeparator());
	    }
	    if (lReturnMsg.toString().isEmpty()) {
		lReturnMsg.append("NOT FOUND");
	    }

	} catch (Exception ex) {
	    LOG.error("Error in searching the segment - " + fileName, ex);
	    lReturnMsg.append("NOT FOUND");
	}
	return lReturnMsg;
    }

    public String downloadFile(String fileId) {
	StringBuilder sb = new StringBuilder();
	File lTestFile = new File(wfConfig.getAttachmentDirectory() + EncryptUtil.decrypt(fileId, Constants.SECRET_HASH));
	if (lTestFile.exists()) {
	    try {
		sb.append(FileUtils.readFileToByteArray(lTestFile));
	    } catch (IOException ex) {
		LOG.error("Error while trying to download file: " + (wfConfig.getAttachmentDirectory() + EncryptUtil.decrypt(fileId, Constants.SECRET_HASH)));
	    }
	}
	return sb.toString();
    }

    public void downloadFile(String fileIds, HttpServletResponse response) {

	Set<String> fileNames = Arrays.asList(EncryptUtil.decrypt(fileIds, Constants.SECRET_HASH).split(",")).stream().filter(x -> x != null && !x.isEmpty()).collect(Collectors.toSet());

	if (fileNames != null && !fileNames.isEmpty()) {
	    try {
		response.reset();
		response.setStatus(HttpServletResponse.SC_OK);
		response.addHeader("Content-Disposition", "attachment; filename=\"" + "Business/Test_doc_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")) + ".zip" + "\"");
		response.setContentType("application/octet-stream");
		try (ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {
		    for (String file : fileNames) {
			File lFile = new File(wfConfig.getAttachmentDirectory() + file);
			if (lFile.exists()) {
			    zipOutputStream.putNextEntry(new ZipEntry(lFile.getName()));
			    IOUtils.copy(new FileInputStream(lFile), zipOutputStream);
			    zipOutputStream.closeEntry();
			}
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		    LOG.error("Failed to download file: " + e.getMessage(), e);
		    response.setContentType("text/plain");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write("Failed to download Zip file");
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    @Transactional
    public StringBuilder getSystemBasedSegments(String systemName) {
	StringBuilder lReturnMsg = new StringBuilder("");
	try {
	    System lSystem = getSystemDAO().findByName(systemName);
	    if (lSystem == null) {
		LOG.info("System - " + systemName + " is not available");
		lReturnMsg.append("ERROR: System Not Found");
		return lReturnMsg;
	    }
	    List<SystemBasedSrcArtifacts> lSystemBasedSrcArtifacts = new ArrayList();
	    lSystemBasedSrcArtifacts = getGitSearchDAO().getSystemBasedSegments(Arrays.asList(systemName), null, null, null, null);
	    SimpleDateFormat lDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
	    lSystemBasedSrcArtifacts.stream().forEach(t -> {
		lReturnMsg.append(t.getTargetsystem()).append(" ").append(t.getProgname()).append(" ").append(t.getFileext()).append(" ").append((StringUtils.split((StringUtils.split(t.getSourcerepo(), "_")[1]), ".")[0]).toUpperCase()).append(" ").append(lDateFormat.format(t.getLoaddatetime())).append(java.lang.System.lineSeparator());
	    });
	    if (lReturnMsg.toString().isEmpty()) {
		lReturnMsg.append("NOT FOUND");
	    }

	} catch (Exception ex) {
	    LOG.error("Error in Getting Data  ", ex);
	    lReturnMsg.append("NOT FOUND");
	}
	return lReturnMsg;
    }

    public JSONResponse getProdLoadsForUpdateplan(String planId) {
	JSONResponse response = new JSONResponse();
	List<ProdLoadStatusDetails> systemAndProdLoadStatusList = getSystemLoadDAO().findSystemsAndProdLoadStatusByPlan(planId);
	response.setStatus(Boolean.TRUE);
	response.setData(systemAndProdLoadStatusList);
	return response;
    }

    public JSONResponse implIdCleanupFromCache(String impId, String progressType) {
	JSONResponse lJSONResponse = new JSONResponse();
	lJSONResponse.setStatus(true);
	LOG.info("Is Implementation present : " + getCacheClient().getInprogressCheckoutMap().containsKey(impId));
	getCacheClient().getInprogressCheckoutMap().remove(impId);
	LOG.info("Implementation : " + impId + " has been removed from cache Process type: " + progressType);
	return lJSONResponse;
    }

    public JSONResponse programCleanupFromCache(String programName, String progressType) {
	JSONResponse lJSONResponse = new JSONResponse();
	lJSONResponse.setStatus(true);
	LOG.info("Is Implementation present : " + getCacheClient().getInprogressMoveArtifactMap().containsKey(programName));
	getCacheClient().getInprogressMoveArtifactMap().remove(programName);
	LOG.info("File : " + programName + " has been removed from cache Process type: " + progressType);
	return lJSONResponse;
    }

    public JSONResponse removePlanFromJenkinsCache(String type, String key) {
	return removePlanFromJenkinsCache(type, key);
    }

    public JSONResponse removePlanFromJenkinsCache(String type, String key, String value) {
	JSONResponse lJSONResponse = new JSONResponse();
	lJSONResponse.setStatus(true);
	Constants.BUILD_TYPE buildType = null;
	for (Constants.BUILD_TYPE bType : Constants.BUILD_TYPE.values()) {
	    if (bType.toString().equals(type)) {
		buildType = Constants.BUILD_TYPE.valueOf(type);
	    }
	}
	if (buildType != null) {
	    switch (buildType) {
	    case DVL_BUILD:
		develBuildJob.remove(key);
		break;
	    case DVL_LOAD:
		develLoaderJob.stream().filter(t -> t.getSystemLoadId().equalsIgnoreCase(key)).forEach(t -> {
		    develLoaderJob.remove(t);
		    auditCommonHelper.saveApiTransaction(t.getUser(), t.getBuildType(), t.getStartedDate(), t.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(t.getBuildType()));
		});
		break;
	    case STG_CWS:
		stagingWorkspaceCreationJobs.stream().filter(t -> t.getSystemLoadId().equalsIgnoreCase(key)).forEach(t -> {
		    stagingWorkspaceCreationJobs.remove(t);
		    auditCommonHelper.saveApiTransaction(t.getUser(), t.getBuildType(), t.getStartedDate(), t.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(t.getBuildType()));
		});
		break;
	    case STG_BUILD:
		stagingBuildJobs.stream().filter(t -> t.getSystemLoadId().equalsIgnoreCase(key)).forEach(t -> {
		    stagingBuildJobs.remove(t);
		    auditCommonHelper.saveApiTransaction(t.getUser(), t.getBuildType(), t.getStartedDate(), t.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(t.getBuildType()));
		});
		break;
	    case STG_LOAD:
		stagingLoaderJobs.stream().filter(t -> t.getSystemLoadId().equalsIgnoreCase(key)).forEach(t -> {
		    stagingLoaderJobs.remove(t);
		    auditCommonHelper.saveApiTransaction(t.getUser(), t.getBuildType(), t.getStartedDate(), t.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(t.getBuildType()));
		});
		break;

	    case ONL_BUILD:

		onlineBuildJobs.stream().filter(t -> t.getSystemLoadId().equalsIgnoreCase(key)).forEach(t -> {
		    onlineBuildJobs.remove(t);
		});
		break;
	    case FAL_BUILD:
		fallBackBuildJobs.stream().filter(t -> t.getSystemLoadId().equalsIgnoreCase(key)).forEach(t -> {
		    fallBackBuildJobs.remove(t);
		});
		break;
	    }
	} else {
	    switch (type) {
	    case "SUBMIT_INPROGRESS":
		lPlanUpdateStatusMap.remove(key);
		break;
	    case "ONLINE":
		lPlanOnlineFallbackStatusMap.remove(key);
		break;
	    case "TSS_LTCUST":
		lPreProdTOSOperationMap.remove(key);
		break;
	    case "INPROGRESS_CACHE":
		getCacheClient().getPlanUpdateStatusMap().remove(key);
		break;
	    case "DEVL_LOADSET_CREATION":
		getPlanHelper().clearPlanActionFromCache(key, value);
		break;
	    }
	}

	return lJSONResponse;
    }

    @Transactional
    public synchronized JSONResponse updateBuildQueueInfo(String planKey, String runStatus) {
	JSONResponse lJSONResponse = new JSONResponse();

	try {
	    LOG.info("Build Queue Information -> " + planKey + " Value -- " + runStatus);
	    String systemName = planKey.split("_")[2].toUpperCase();
	    String planId = planKey.split("_")[0].toUpperCase();

	    if (runStatus.equalsIgnoreCase("RUN")) {
		List<String> systems = getSystemDAO().getAliasSystemBySystemName(systemName);
		// systems.remove(systemName);
		if (systems != null && !systems.isEmpty()) {
		    getBuildDAO().updateAllInProgressBuild(systems, Arrays.asList(Constants.BUILD_TYPE.DVL_BUILD.name(), Constants.BUILD_TYPE.STG_BUILD.name()), Arrays.asList(Constants.TDXRunningStatus.INPROGRESS.getTDXRunningStatus()));
		}

		getBuildDAO().updateBuildAsInprogress(planId, systemName, Arrays.asList(Constants.BUILD_TYPE.DVL_BUILD.name(), Constants.BUILD_TYPE.STG_BUILD.name()), Arrays.asList(Constants.TDXRunningStatus.PENDING.getTDXRunningStatus()));

	    } else if (runStatus.equalsIgnoreCase("WAIT")) {
		getBuildDAO().updateBuildAsWaiting(planId, systemName, Arrays.asList(Constants.BUILD_TYPE.DVL_BUILD.name(), Constants.BUILD_TYPE.STG_BUILD.name()), Arrays.asList(Constants.TDXRunningStatus.PENDING.getTDXRunningStatus()));
	    }
	    lJSONResponse.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.info("Exception in feting the information ", ex);
	}
	return lJSONResponse;
    }

    // Temporary Code for 22.0 Performnace story testing (Jmeter)
    public JSONResponse deleteSegmentids(String impl) {
	JSONResponse lJSONResponse = new JSONResponse();
	List<CheckoutSegments> segList = getCheckoutSegmentsDAO().findByImpPlan(impl);
	String ids = segList.stream().map(t -> ("ids=" + t.getId())).collect(Collectors.joining("&"));
	LOG.info("IDS : " + ids);
	lJSONResponse.setData(ids);
	lJSONResponse.setStatus(true);
	return lJSONResponse;
    }

    public JSONResponse clearValidateMakCache(String impl, User lUser) {

	JSONResponse lJSONResponse = new JSONResponse();
	String cachekey = impl + "_" + lUser.getDisplayName();
	lJSONResponse.setStatus(false);
	if (getCacheClient().getValidateMakFileMap().containsKey(cachekey)) {
	    getCacheClient().getValidateMakFileMap().remove(cachekey);
	    lJSONResponse.setStatus(true);

	}
	return lJSONResponse;
    }

    @Transactional
    public JSONResponse moveDerivedArtifactsToGit(User user, String planId) {
	JSONResponse lReturn = new JSONResponse();
	try {
	    if (planId == null || planId.isEmpty()) {
		getPlanSubmissionGitOperation().moveDerivedArtifactsToGit();
		lReturn.setStatus(Boolean.TRUE);
	    } else {
		ImpPlan plan = getImpPlanDAO().find(planId);
		Boolean isPlanSubmissionSuccess = getPlanSubmissionGitOperation().movePlanDerivedArtifactsToGit(plan);
		lReturn.setStatus(isPlanSubmissionSuccess);
	    }
	} catch (Exception ex) {
	    LOG.info("Error occurs in moving derived artifact to git server", ex);
	}
	return lReturn;
    }

    @Transactional
    public JSONResponse getBuildQueueCache() {
	JSONResponse lReturn = new JSONResponse();
	try {
	    lReturn.setData(getCacheClient().getBuildQueueInfo());
	    lReturn.setStatus(true);
	} catch (Exception ex) {
	    LOG.info("Error occurs in moving derived artifact to git server", ex);
	}
	return lReturn;
    }

}
