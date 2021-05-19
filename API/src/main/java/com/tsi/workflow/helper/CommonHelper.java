package com.tsi.workflow.helper;

import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dao.SystemLoadActions;
import com.tsi.workflow.beans.dto.SystemLoadDTO;
import com.tsi.workflow.beans.ui.DependenciesForm;
import com.tsi.workflow.beans.ui.RFCDetailsDTO;
import com.tsi.workflow.beans.ui.RepositoryView;
import com.tsi.workflow.beans.ui.SourceArtifactSearchResult;
import com.tsi.workflow.beans.ui.SystemBasedMetaData;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.PreProductionLoadsDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.GitBranchSearchResult;
import com.tsi.workflow.git.GitSearchResult;
import com.tsi.workflow.git.IJGITSearchUtils;
import com.tsi.workflow.utils.CheckoutUtils;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.BUILD_TYPE;
import com.tsi.workflow.utils.EncryptUtil;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CommonHelper {

    private static final Logger LOG = Logger.getLogger(CommonHelper.class.getName());

    @Autowired
    LDAPAuthenticatorImpl authenticator;
    @Autowired
    SSHClientUtils sSHClientUtils;
    @Autowired
    SystemDAO systemDAO;
    @Autowired
    CheckoutSegmentsDAO checkoutSegmentsDAO;
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    GITHelper gitHelper;
    @Autowired
    BuildDAO buildDAO;
    @Autowired
    PreProductionLoadsDAO preProductionLoadsDAO;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    SystemLoadActionsDAO systemLoadActionsDAO;
    @Autowired
    ImplementationDAO implementationDAO;
    @Autowired
    IJGITSearchUtils jGITSearchUtils;
    @Autowired
    WFConfig wfConfig;
    @Autowired
    CacheClient cacheClient;

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public SystemLoadActionsDAO getSystemLoadActionsDAO() {
	return systemLoadActionsDAO;
    }

    public BuildDAO getBuildDAO() {
	return buildDAO;
    }

    public SystemDAO getSystemDAO() {
	return systemDAO;
    }

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public CheckoutSegmentsDAO getCheckoutSegmentsDAO() {
	return checkoutSegmentsDAO;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public GITHelper getGitHelper() {
	return gitHelper;
    }

    public PreProductionLoadsDAO getPreProductionLoadsDAO() {
	return preProductionLoadsDAO;
    }

    public ImplementationDAO getImplementationDAO() {
	return implementationDAO;
    }

    public IJGITSearchUtils getjGITSearchUtils() {
	return jGITSearchUtils;
    }

    public Set<String> getPlanStatusBasedOnId(String planStatusids) {

	Set<String> planStatus = new HashSet<>();
	if (planStatusids != null) {
	    Arrays.asList(planStatusids.split(",")).forEach(val -> {

		if (val.trim().contains("-")) {

		    List<Integer> statusIdRange = Arrays.asList(val.trim().split("-")).stream().map(e -> Integer.parseInt(e.trim())).collect(Collectors.toList());
		    if (statusIdRange.size() <= 1) {
			LOG.error("Status range id's isn't defined properly. Wrong value: " + val);
		    } else {
			int minVal = Collections.min(statusIdRange);
			int maxVal = Collections.max(statusIdRange);

			for (int i = minVal; i <= maxVal; i++) {
			    planStatus.add(Constants.PlanStatus.getStatusMapByPosition(i));
			}
		    }
		} else if (!val.trim().isEmpty()) {
		    planStatus.add(Constants.PlanStatus.getStatusMapByPosition(Integer.parseInt(val.trim())));
		}
	    });
	}
	return planStatus;
    }

    public Set<Integer> getListOfValuesFromString(String value) {
	Set<Integer> intValues = new HashSet<>();
	if (value != null) {
	    Arrays.asList(value.split(",")).forEach(val -> {

		if (val.trim().contains("-")) {

		    List<Integer> statusIdRange = Arrays.asList(val.trim().split("-")).stream().map(e -> Integer.parseInt(e.trim())).collect(Collectors.toList());
		    if (statusIdRange.size() <= 1) {
			LOG.error("Status range id's isn't defined properly. Wrong value: " + val);
		    } else {
			int minVal = Collections.min(statusIdRange);
			int maxVal = Collections.max(statusIdRange);

			for (int i = minVal; i <= maxVal; i++) {
			    intValues.add(i);
			}
		    }
		} else if (!val.trim().isEmpty()) {
		    intValues.add(Integer.parseInt(val.trim()));
		}
	    });
	}
	return intValues;
    }

    public void setMailAndNameBasedOnId(SystemBasedMetaData val) {
	if (val.getDevid() != null && !val.getDevid().isEmpty()) {
	    User lUser = authenticator.getUserDetails(val.getDevid());
	    if (lUser != null) {
		val.setDeveloperEmail(lUser.getMailId());
		val.setDevelopername(lUser.getDisplayName());
	    }
	}

	if (val.getPeerreviewerids() != null && !val.getPeerreviewerids().isEmpty()) {

	    List<String> emailIds = new ArrayList<>();
	    List<String> peerNames = new ArrayList<>();

	    Arrays.asList(val.getPeerreviewerids().split(",")).stream().forEach(peerid -> {
		User lUser = authenticator.getUserDetails(peerid);
		if (lUser != null) {
		    emailIds.add(lUser.getMailId());
		    peerNames.add(lUser.getDisplayName());
		}
	    });
	    val.setReviewerMail(emailIds.stream().collect(Collectors.joining(",")));
	    val.setReviewerName(peerNames.stream().collect(Collectors.joining(",")));
	}

	if (val.getLeadid() != null && !val.getLeadid().isEmpty()) {
	    User lUser = authenticator.getUserDetails(val.getLeadid());
	    if (lUser != null) {
		val.setAdlMail(lUser.getMailId());
		val.setAdlName(lUser.getDisplayName());
	    }
	}

	if (val.getDevmanager() != null && !val.getDevmanager().isEmpty()) {
	    User lUser = authenticator.getUserDetails(val.getDevmanager());
	    if (lUser != null) {
		val.setManagerMail(lUser.getMailId());
		val.setManagerName(lUser.getDisplayName());
	    }
	}

	if (val.getApprover() != null && !val.getApprover().isEmpty()) {
	    User lUser = authenticator.getUserDetails(val.getApprover());
	    if (lUser != null) {
		val.setApprovingManMail(lUser.getMailId());
		val.setApprovingManName(lUser.getDisplayName());
	    }
	}

	if (val.getLoadattendeeid() != null && !val.getLoadattendeeid().isEmpty()) {
	    User lUser = authenticator.getUserDetails(val.getLoadattendeeid());
	    if (lUser != null) {
		val.setLoadAttnMail(lUser.getMailId());
		val.setLaodAttnName(lUser.getDisplayName());
	    }
	}
    }

    public List<SystemBasedMetaData> getUniqueDataMetaData(Map<String, List<SystemBasedMetaData>> metaDataMap) {
	List<SystemBasedMetaData> uniqueMetaData = new ArrayList<>();

	metaDataMap.forEach((key, values) -> {
	    Set<String> fileTypes = new HashSet<>();
	    Set<String> programNames = new HashSet<>();
	    HashMap<String, Set<String>> funcAreaWiseSeg = new HashMap<>();
	    TreeMap<String, Set<String>> sortedMap = new TreeMap<>();
	    values.forEach(val -> {
		if (val.getFiletype() != null) {
		    String derivedObj = "";
		    if (val.getFiletype().equalsIgnoreCase(Constants.FILE_TYPE.IBM.name())) {
			derivedObj = Constants.STAGE_IBM_BASE_PATH + val.getPlanid().toLowerCase() + "/" + val.getTargetsys().toLowerCase();
		    } else if (val.getFiletype().equalsIgnoreCase(Constants.FILE_TYPE.NON_IBM.name())) {
			derivedObj = Constants.STAGE_NON_IBM_BASE_PATH + val.getPlanid().toLowerCase() + "/" + val.getTargetsys().toLowerCase();
		    }
		    if (!derivedObj.trim().isEmpty()) {
			fileTypes.add(derivedObj);
		    }
		}

		if (val.getFuncarea() != null) {
		    if (funcAreaWiseSeg.get(val.getFuncarea()) != null) {
			funcAreaWiseSeg.get(val.getFuncarea()).add(val.getProgname());
		    } else {
			funcAreaWiseSeg.put(val.getFuncarea(), new HashSet<>());
			funcAreaWiseSeg.get(val.getFuncarea()).add(val.getProgname());
		    }
		}
	    });
	    sortedMap.putAll(funcAreaWiseSeg);
	    List<String> funcAreaList = new ArrayList<>();
	    List<String> programNameList = new ArrayList<>();

	    for (Map.Entry<String, Set<String>> val : sortedMap.entrySet()) {
		funcAreaList.add(StringUtils.repeat(val.getKey(), " ", val.getValue().size()));
		programNameList.add(val.getValue().stream().collect(Collectors.joining(" ")));
	    }

	    SystemBasedMetaData systemBasedMetaData = values.stream().findAny().get();
	    if (systemBasedMetaData != null) {

		systemBasedMetaData.setSrcObj(programNameList.stream().collect(Collectors.joining(" ")));
		systemBasedMetaData.setFuncarea(funcAreaList.stream().collect(Collectors.joining(" ")));

		if (!fileTypes.isEmpty()) {
		    systemBasedMetaData.setDerivedObj(fileTypes.stream().collect(Collectors.joining(",")));
		}
		uniqueMetaData.add(systemBasedMetaData);
	    }
	});

	return uniqueMetaData;
    }

    public String getFormattedOutput(List<SystemBasedMetaData> metaData, HashMap<String, String> lDepPlanMap, String Dependent) {

	String startDelimiter = ">>";
	String endDelimiter = "<<";

	StringBuilder sb = new StringBuilder();
	metaData.forEach(val -> {

	    // Target System
	    sb.append(startDelimiter + "TS" + startDelimiter);
	    sb.append(val.getTargetsys() != null ? val.getTargetsys() : "");
	    sb.append(endDelimiter + "TS" + endDelimiter);

	    // Imp Plan
	    sb.append(startDelimiter + "IP" + startDelimiter);
	    sb.append(val.getPlanid() != null ? val.getPlanid() : "");
	    sb.append(endDelimiter + "IP" + endDelimiter);

	    // Imp
	    sb.append(startDelimiter + "IM" + startDelimiter);
	    sb.append(val.getImpid() != null ? val.getImpid() : "");
	    sb.append(endDelimiter + "IM" + endDelimiter);

	    // FUnc Area
	    sb.append(startDelimiter + "FA" + startDelimiter);
	    sb.append(val.getFuncarea() != null ? val.getFuncarea() : "");
	    sb.append(endDelimiter + "FA" + endDelimiter);

	    // Load Date
	    sb.append(startDelimiter + "LD" + startDelimiter);
	    sb.append(val.getLoaddatetime() != null ? new SimpleDateFormat("yyyyMMdd").format(val.getLoaddatetime()) : "");
	    sb.append(endDelimiter + "LD" + endDelimiter);

	    // Load time
	    sb.append(startDelimiter + "LT" + startDelimiter);
	    sb.append(val.getLoaddatetime() != null ? new SimpleDateFormat("HHmmss").format(val.getLoaddatetime()) : "");
	    sb.append(endDelimiter + "LT" + endDelimiter);

	    // Load Category
	    sb.append(startDelimiter + "LC" + startDelimiter);
	    sb.append(val.getLoadcategory() != null ? val.getLoadcategory() : "");
	    sb.append(endDelimiter + "LC" + endDelimiter);

	    // Status
	    sb.append(startDelimiter + "ST" + startDelimiter);
	    sb.append(val.getPlanstatus() != null ? val.getPlanstatus() : "");
	    sb.append(endDelimiter + "ST" + endDelimiter);

	    // Developer name
	    sb.append(startDelimiter + "DN" + startDelimiter);
	    sb.append(val.getDevelopername() != null ? val.getDevelopername() : "");
	    sb.append(endDelimiter + "DN" + endDelimiter);

	    // developer email
	    sb.append(startDelimiter + "DE" + startDelimiter);
	    sb.append(val.getDeveloperEmail() != null ? val.getDeveloperEmail() : "");
	    sb.append(endDelimiter + "DE" + endDelimiter);

	    // Reviewer name
	    sb.append(startDelimiter + "RN" + startDelimiter);
	    sb.append(val.getReviewerName() != null ? val.getReviewerName() : "");
	    sb.append(endDelimiter + "RN" + endDelimiter);

	    // Reviewer email
	    sb.append(startDelimiter + "RE" + startDelimiter);
	    sb.append(val.getReviewerMail() != null ? val.getReviewerMail() : "");
	    sb.append(endDelimiter + "RE" + endDelimiter);

	    // Bypass Reg
	    sb.append(startDelimiter + "BR" + startDelimiter);
	    sb.append(val.isPlanBypassedRegression() != null ? val.isPlanBypassedRegression() : "");
	    sb.append(endDelimiter + "BR" + endDelimiter);

	    // Prog type
	    sb.append(startDelimiter + "PT" + startDelimiter);
	    sb.append(val.getLoadtype() != null ? val.getLoadtype() : "");
	    sb.append(endDelimiter + "PT" + endDelimiter);

	    // ADL Name
	    sb.append(startDelimiter + "LN" + startDelimiter);
	    sb.append(val.getAdlName() != null ? val.getAdlName() : "");
	    sb.append(endDelimiter + "LN" + endDelimiter);

	    // ADL Mail
	    sb.append(startDelimiter + "LE" + startDelimiter);
	    sb.append(val.getAdlMail() != null ? val.getAdlMail() : "");
	    sb.append(endDelimiter + "LE" + endDelimiter);

	    // CSR Name
	    sb.append(startDelimiter + "CS" + startDelimiter);
	    sb.append(val.getProjnumber() != null ? val.getProjnumber() : "");
	    sb.append(endDelimiter + "CS" + endDelimiter);

	    // Prob Ticket
	    sb.append(startDelimiter + "PT" + startDelimiter);
	    sb.append(val.getPrtktnum() != null ? val.getPrtktnum() : "");
	    sb.append(endDelimiter + "PT" + endDelimiter);

	    // DBCR
	    sb.append(startDelimiter + "DB" + startDelimiter);
	    sb.append(val.getDbcrname() != null ? val.getDbcrname() : "");
	    sb.append(endDelimiter + "DB" + endDelimiter);

	    // Plan Desc
	    sb.append(startDelimiter + "DS" + startDelimiter);
	    sb.append(val.getPlandesc() != null ? val.getPlandesc() : "");
	    sb.append(endDelimiter + "DS" + endDelimiter);

	    // Manager name
	    sb.append(startDelimiter + "MN" + startDelimiter);
	    sb.append(val.getManagerName() != null ? val.getManagerName() : "");
	    sb.append(endDelimiter + "MN" + endDelimiter);

	    // Manager mail
	    sb.append(startDelimiter + "ME" + startDelimiter);
	    sb.append(val.getManagerMail() != null ? val.getManagerMail() : "");
	    sb.append(endDelimiter + "ME" + endDelimiter);

	    // Approve manager
	    sb.append(startDelimiter + "AM" + startDelimiter);
	    sb.append(val.getApprovingManName() != null ? val.getApprovingManName() : "");
	    sb.append(endDelimiter + "AM" + endDelimiter);

	    // Approving manager
	    sb.append(startDelimiter + "AE" + startDelimiter);
	    sb.append(val.getApprovingManMail() != null ? val.getApprovingManMail() : "");
	    sb.append(endDelimiter + "AE" + endDelimiter);

	    // Load attendee name
	    sb.append(startDelimiter + "LA" + startDelimiter);
	    sb.append(val.getLaodAttnName() != null ? val.getLaodAttnName() : "");
	    sb.append(endDelimiter + "LA" + endDelimiter);

	    // Derived Objects
	    sb.append(startDelimiter + "DO" + startDelimiter);
	    sb.append(val.getDerivedObj() != null ? val.getDerivedObj() : "");
	    sb.append(endDelimiter + "DO" + endDelimiter);

	    // Source Objects
	    sb.append(startDelimiter + "SO" + startDelimiter);
	    sb.append(val.getSrcObj() != null ? val.getSrcObj() : "");
	    sb.append(endDelimiter + "SO" + endDelimiter);

	    // Loadset Type
	    sb.append(startDelimiter + "LS" + startDelimiter);
	    sb.append(val.getLoadsettype() != null ? val.getLoadsettype() : "");
	    sb.append(endDelimiter + "LS" + endDelimiter);

	    // Dependency Plan list
	    if (Dependent.equalsIgnoreCase("Y")) {
		sb.append(startDelimiter + "DP" + startDelimiter);
		LOG.info(" Is dep plan available: " + (val.getPlanid() != null && !val.getPlanid().isEmpty() && lDepPlanMap != null && lDepPlanMap.containsKey(val.getPlanid())));
		sb.append((val.getPlanid() != null && !val.getPlanid().isEmpty() && lDepPlanMap != null && lDepPlanMap.containsKey(val.getPlanid())) ? lDepPlanMap.get(val.getPlanid()) : "");
		sb.append(endDelimiter + "DP" + endDelimiter);
	    }
	    // Line of Business(LOB)
	    sb.append(startDelimiter + "LB" + startDelimiter);
	    sb.append(val.getLineofbusiness() != null ? val.getLineofbusiness() : "");
	    sb.append(endDelimiter + "LB" + endDelimiter);

	    // QA Functional Tester
	    sb.append(startDelimiter + "QA" + startDelimiter);
	    sb.append(val.getQafunctionaltester() != null ? val.getQafunctionaltester() : "");
	    sb.append(endDelimiter + "QA" + endDelimiter);

	    // QA Testing Status
	    sb.append(startDelimiter + "QS" + startDelimiter);
	    sb.append(val.getQabypassstatus() != null ? val.getQabypassstatus() : "");
	    sb.append(endDelimiter + "QS" + endDelimiter);

	    if (val.getPlanid().toUpperCase().startsWith("D")) {
		LOG.info("Sys : " + val.getTargetsys());
		LOG.info("Result : " + val.getTargetsys() + ":" + val.getRfcnumber() != null ? val.getRfcnumber() : "");
		sb.append(startDelimiter + "RN" + startDelimiter);
		sb.append(val.getTargetsys() + ":");
		sb.append(val.getRfcnumber() != null ? val.getRfcnumber() : "");
		sb.append(endDelimiter + "RN" + endDelimiter);
	    }

	    sb.append(java.lang.System.lineSeparator());
	});

	return sb.toString();

    }

    public JSONResponse writeDataIntoFile(List<SystemBasedMetaData> metaData, String outputPath) throws IOException {

	JSONResponse lResponse = new JSONResponse();
	String seprator = " ";
	StringBuilder sb = new StringBuilder();
	metaData.forEach(val -> {
	    if (!sb.toString().trim().isEmpty()) {
		sb.append(java.lang.System.lineSeparator());
	    }
	    sb.append(val.getTargetsys());
	    sb.append(seprator);
	    sb.append(outputPath);
	    sb.append(seprator);
	    sb.append(val.getFiletype());
	    sb.append(seprator);
	    sb.append(val.getFilename());
	    sb.append(seprator);
	    if (Constants.PlanStatus.ONLINE.name().equalsIgnoreCase(val.getPlanstatus()) || Constants.PlanStatus.FALLBACK.name().equalsIgnoreCase(val.getPlanstatus())) {
		sb.append(val.getPlanstatus());
		sb.append(seprator);
		sb.append(val.getStatusrank() - 1);
		sb.append(seprator);
		sb.append(val.getRepodetail());
		sb.append(seprator);
		sb.append(val.getCommitid());
	    } else {
		sb.append(val.getPlanid());
	    }
	    sb.append(seprator);
	    sb.append(val.getLoadsettype());
	});

	String fileName = "/tmp/gAPI_" + Constants.JENKINS_DATEFORMAT.get().format(new Date()) + "_" + RandomStringUtils.randomAlphanumeric(4);
	LOG.info("File name to linux: " + fileName);
	String lCommand = "echo '" + sb.toString() + "' >> " + fileName;
	// ${MTP_ENV}/mtpgitgetapifile /tmp/gAPI_20190125120711_judg.txt
	String lCommandShell = Constants.SystemScripts.GIT_GENERIC_API.getScript() + " " + fileName;
	System system = getSystemDAO().findByName(metaData.stream().findAny().get().getTargetsys().toUpperCase());
	JSONResponse lCommandResponse = getsSHClientUtils().executeCommand(system, lCommand);

	if (lCommandResponse.getStatus()) {
	    // Call shell
	    JSONResponse commandResponse = getsSHClientUtils().executeCommand(system, lCommandShell);
	    lResponse.setStatus(commandResponse.getStatus());
	    if (!commandResponse.getStatus()) {
		LOG.info("Error message: " + commandResponse.getErrorMessage());
		LOG.error("Failed to create a seg file using file from temp folder with System based meta data." + lCommandResponse.getErrorMessage());
		lResponse.setErrorMessage("Failed to create a seg file using file from temp folder with System based meta data." + lCommandResponse.getErrorMessage());
	    }
	} else {
	    LOG.error("Failed to create a file in temp folder with System based meta data." + lCommandResponse.getErrorMessage());
	    lResponse.setStatus(false);
	    lResponse.setErrorMessage("Failed to create a file in temp folder with System based meta data." + lCommandResponse.getErrorMessage());
	}
	return lCommandResponse;
    }

    public void validateToChangePutLevel(List<SystemLoadDTO> sytemLoadDTO) {
	for (SystemLoadDTO pSystemLoadDTO : sytemLoadDTO) {
	    ImpPlan pPlan = getImpPlanDAO().find(pSystemLoadDTO.getSystemLoad().getPlanId().getId());
	    List<CheckoutSegments> lCheckoutSegements = getCheckoutSegmentsDAO().findPlanBySystem(pSystemLoadDTO.getSystemLoad().getPlanId().getId(), pSystemLoadDTO.getSystemLoad().getSystemId().getName());

	    if (lCheckoutSegements != null && !lCheckoutSegements.isEmpty()) {
		lCheckoutSegements.stream().filter(seg -> Constants.FILE_TYPE.IBM.name().equals(seg.getFileType())).forEach(seg -> pSystemLoadDTO.setAllowPutLevelChange(false));
	    }
	    if (!pPlan.getPlanStatus().equals(Constants.PlanStatus.ACTIVE.name())) {
		pSystemLoadDTO.setAllowPutLevelChange(false);
	    }
	}
    }

    public String getFormattedOutput(Map<Integer, String> planStatusList) {

	String startDelimiter = ">>";
	String endDelimiter = "<<";

	StringBuilder sb = new StringBuilder();
	planStatusList.entrySet().forEach((t) -> {
	    // ID = Integer Value
	    sb.append(startDelimiter).append("ID").append(startDelimiter);
	    sb.append(t.getKey());
	    sb.append(endDelimiter).append("ID").append(endDelimiter);

	    // SN = Status Name
	    sb.append(startDelimiter).append("SN").append(startDelimiter);
	    sb.append(t.getValue());
	    sb.append(endDelimiter).append("SN").append(endDelimiter);

	    sb.append(java.lang.System.lineSeparator());
	});

	return sb.toString();
    }

    public String getFormattedOutputForLDAPList(Map<String, String> inputList, String role) {

	String startDelimiter = ">>";
	String endDelimiter = "<<";

	StringBuilder sb = new StringBuilder();
	for (Map.Entry<String, String> user : inputList.entrySet()) {
	    // ID = Ldap Id
	    sb.append(startDelimiter).append("ID").append(startDelimiter);
	    sb.append(user.getKey());
	    sb.append(endDelimiter).append("ID").append(endDelimiter);
	    // DN - Display Name
	    sb.append(startDelimiter).append("DN").append(startDelimiter);
	    sb.append(user.getValue());
	    sb.append(endDelimiter).append("DN").append(endDelimiter);
	    // RL - Roles
	    sb.append(startDelimiter).append("RL").append(startDelimiter);
	    sb.append(role);
	    sb.append(endDelimiter).append("RL").append(endDelimiter);

	    sb.append(java.lang.System.lineSeparator());
	}

	return sb.toString();
    }

    public JSONResponse writeDataIntoFile(String systemName, String outputPath, List<String> dataToWrite) throws IOException {

	// Form data which needs to write into File
	StringBuilder sb = new StringBuilder();
	LOG.info(dataToWrite.size());
	dataToWrite.forEach(t -> {
	    if (!sb.toString().trim().isEmpty()) {
		sb.append(java.lang.System.lineSeparator());
	    }
	    sb.append(t);
	});

	// Write the Information to File
	String fileName = "gAPI_" + Constants.JENKINS_DATEFORMAT.get().format(new Date()) + "_" + RandomStringUtils.randomAlphanumeric(4);
	LOG.info("File name to linux: " + fileName);
	// String lCommand = "echo '" + sb.toString() + "' >> " + outputPath +
	// File.separator + fileName;
	String lCommand = "echo '" + sb.toString().replace("'", "") + "' >> " + outputPath + "/" + fileName;
	LOG.info(lCommand);
	// ${MTP_ENV}/mtpgitgetapifile /tmp/gAPI_20190125120711_judg.txt
	// String lCommandShell = Constants.SystemScripts.GIT_GENERIC_API.getScript() +
	// " " + fileName;
	System system = getSystemDAO().findByName(systemName.toUpperCase());
	JSONResponse lCommandResponse = getsSHClientUtils().executeCommand(system, lCommand);
	if (lCommandResponse.getStatus()) {
	    lCommandResponse.setData("OutPut File Name-" + outputPath + "/" + fileName);
	}
	return lCommandResponse;
    }

    public List<GitSearchResult> getNonProdFileDetails(List<CheckoutSegments> lResult, Implementation lImp, Set<String> lRepoList) throws IllegalAccessException, InvocationTargetException {
	List<GitSearchResult> lReturnList = new ArrayList<>();
	List<String> putLevelStatus = new ArrayList<>();
	putLevelStatus.add(Constants.PUTLevelOptions.PRODUCTION.name());
	HashMap<String, String> tarSysAndPutLevel = getGitHelper().getSystemBasedPutLevels(putLevelStatus);
	List<String> lDeactivatedPlanInProd = getImpPlanDAO().getListOfPlansDeactivatedInProd();
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
		lKey.setProdFlag("NONPROD");
		lKey.addAdditionalInfo("planId", lSegment.getPlanId().getId());
		lKey.addAdditionalInfo("planStatus", lSegment.getPlanId().getPlanStatus());
		lKey.setFileNameWithHash(CheckoutUtils.getHexStringWithPlanSegment(lSegment));
		lValue.addAdditionalInfo("planId", lSegment.getPlanId().getId());
		lValue.setTargetSystem("master_" + lSegment.getTargetSystem().toLowerCase());
		lValue.setRefLoadDate(lSegment.getSystemLoad().getLoadDateTime());
		lValue.setRefStatus(lSegment.getPlanId().getPlanStatus());
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

		// 2061 To include active segments, but restrict access to checkout
		if (Constants.PlanStatus.ACTIVE.getDisplayName().equalsIgnoreCase(lSegment.getPlanId().getPlanStatus())) {
		    lValue.setIsCheckoutAllowed(Boolean.FALSE);
		    lValue.setIsBranchSelected(Boolean.FALSE);
		}
		// 2126 - If segment in plan, plan status is READY FOR PRODUCTION DEPLOYMENT.
		// Loadset of the plan is deactivated in Production
		// then throw warning message to User about selection of this segment
		lKey.addAdditionalInfo("isPlanDeactivatedInProd", "");
		if (lDeactivatedPlanInProd != null && !lDeactivatedPlanInProd.isEmpty() && lDeactivatedPlanInProd.contains(lSegment.getPlanId().getId())) {
		    String lWarnMsg = "Attention!! Segment(s) " + lSegment.getProgramName() + " being selected from Implementation Plan(s) " + lSegment.getPlanId().getId() + " is in deactivate status. You would like to proceed?";
		    lKey.addAdditionalInfo("isPlanDeactivatedInProd", lWarnMsg);
		}

		if (lSegment.getSystemLoad().getLoadCategoryId() != null) {
		    lValue.addAdditionalInfo("categoryName", lSegment.getSystemLoad().getLoadCategoryId().getName());
		}

		// ZTPFM-2404 Code changes to show new file tag if it is in Active status
		if (Constants.PlanStatus.getBeforeApprovedStatus().keySet().contains(lSegment.getPlanId().getPlanStatus()) && (lSegment.getRefStatus() == null || lSegment.getRefStatus().isEmpty() || lSegment.getRefStatus().equalsIgnoreCase("newfile") || lSegment.getRefStatus().equalsIgnoreCase("New File"))) {
		    lKey.addAdditionalInfo("planStatus", "New File");
		    lValue.setRefStatus("New File");
		    lValue.setIsCheckoutAllowed(Boolean.FALSE);
		    lValue.setIsBranchSelected(Boolean.FALSE);
		}

		String idStringWithPlan = CheckoutUtils.getIdStringWithPlan(lSegment);
		if (lFilteredList.containsKey(idStringWithPlan)) {
		    if (!lFilteredSystemList.get(CheckoutUtils.getIdStringWithPlanSysCheck(lSegment)).contains(lSegment.getTargetSystem())) {
			lFilteredList.get(idStringWithPlan).getTargetSystems().add(lValue.getTargetSystem());
			lFilteredList.get(idStringWithPlan).addBranch(lValue);
			lFilteredSystemList.get(CheckoutUtils.getIdStringWithPlanSysCheck(lSegment)).add(lSegment.getTargetSystem());
		    }
		} else {
		    lKey.getTargetSystems().add(lValue.getTargetSystem());
		    lKey.addBranch(lValue);
		    lFilteredList.put(idStringWithPlan, lKey);
		    List lSystemList = new ArrayList();
		    lSystemList.add(lSegment.getTargetSystem());
		    lFilteredSystemList.put(CheckoutUtils.getIdStringWithPlanSysCheck(lSegment), lSystemList);
		    lReturnList.add(lKey);
		}
	    }
	}
	return lReturnList;
    }

    public boolean isDevBuildCompleted(ImpPlan impPlan) {
	boolean isDevBuildDone = true;
	List<Build> devBuilds = getBuildDAO().findAll(impPlan, BUILD_TYPE.DVL_BUILD).stream().filter(dev -> dev.getActive().equals("Y")).collect(Collectors.toList());
	if (impPlan.getImplementationList() == null || impPlan.getImplementationList().isEmpty() || devBuilds == null || devBuilds.isEmpty() || devBuilds.stream().filter(devBuild -> devBuild.getBuildStatus() == null || (devBuild.getBuildStatus() != null && !devBuild.getBuildStatus().equals("FULL"))).findAny().isPresent()) {
	    isDevBuildDone = false;
	}
	return isDevBuildDone;
    }

    public boolean isDevLoadSetCompleted(ImpPlan impPlan) {
	boolean isDevLoadsetDone = true;
	List<Build> devLoads = getBuildDAO().findAll(impPlan, BUILD_TYPE.DVL_LOAD).stream().filter(dev -> dev.getActive().equals("Y")).collect(Collectors.toList());
	if (impPlan.getImplementationList() == null || impPlan.getImplementationList().isEmpty() || devLoads == null || devLoads.isEmpty() || devLoads.stream().filter(devBuild -> !devBuild.getJobStatus().equals("S")).findAny().isPresent()) {
	    isDevLoadsetDone = false;
	}
	return isDevLoadsetDone;
    }

    public void getActSystemLoadActionsList(ImpPlan lPlan, Constants.VPARSEnvironment env, Boolean referSysLoadActions, Set<String> lSystemList, Set<String> lPreProdLoadSystemList) {
	List<SystemLoad> lSystemLoads = getSystemLoadDAO().findByImpPlan(lPlan.getId());

	for (SystemLoad lSystemLoad : lSystemLoads) {
	    if (env.name().equals(Constants.VPARSEnvironment.QA_FUCTIONAL.name())) {
		if (lSystemLoad.getQaBypassStatus().equals(Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name()) || lSystemLoad.getQaBypassStatus().equals(Constants.SYSTEM_QA_TESTING_STATUS.NONE.name())) {
		    lSystemList.add(lSystemLoad.getSystemId().getName());
		}
	    } else if (env.name().equals(Constants.VPARSEnvironment.QA_REGRESSION.name())) {
		if (lSystemLoad.getQaBypassStatus().equals(Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name()) || lSystemLoad.getQaBypassStatus().equals(Constants.SYSTEM_QA_TESTING_STATUS.NONE.name())) {
		    lSystemList.add(lSystemLoad.getSystemId().getName());
		}
	    } else {
		lSystemList.add(lSystemLoad.getSystemId().getName());
	    }

	    if (referSysLoadActions) {
		List<SystemLoadActions> lPreProductionLoads = getSystemLoadActionsDAO().findBySystemLoadEnv(lSystemLoad, env);
		int actCount = 0;
		for (SystemLoadActions lPreProdLoad : lPreProductionLoads) {
		    if (lPreProdLoad != null && (lPreProdLoad.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name()))) {
			actCount++;
		    }
		}
		if (actCount != 0 && actCount == lPreProductionLoads.size()) {
		    lPreProdLoadSystemList.add(lSystemLoad.getSystemId().getName());
		}
	    } else {
		List<PreProductionLoads> lPreProductionLoads = getPreProductionLoadsDAO().findBySystemLoadId(lSystemLoad);
		int actCount = 0;
		LOG.info(lPreProductionLoads.size());
		for (PreProductionLoads lPreProdLoad : lPreProductionLoads) {
		    if (lPreProdLoad != null && (lPreProdLoad.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name()))) {
			actCount++;
		    }
		}
		if (actCount != 0 && actCount == lPreProductionLoads.size()) {
		    lPreProdLoadSystemList.add(lSystemLoad.getSystemId().getName());
		}
	    }
	}
	LOG.info("System count and Load Count " + lPreProdLoadSystemList.size() + "/" + lSystemList.size());
    }

    public void updateLatestChangeDate(User lUser, Map<String, List<String>> systemBasedChangedSegs, Map<String, List<CheckoutSegments>> lGrouped) {
	// ZTPFM-2447 Code changes to upate Last changed value for recently updated
	// segments
	Date recentCheckedInDate = new Date();
	List<CheckoutSegments> checkInDateUpdateList = new ArrayList<>();
	if (systemBasedChangedSegs != null && !systemBasedChangedSegs.isEmpty()) {
	    systemBasedChangedSegs.forEach((key, value) -> {
		if (lGrouped.containsKey(key)) {
		    lGrouped.get(key).stream().filter(seg -> value.contains(seg.getFileName())).forEach(seg -> {
			seg.setLastChangedTime(recentCheckedInDate);
			checkInDateUpdateList.add(seg);
		    });
		}
	    });
	}

	// update the segments which has new check in date time
	checkInDateUpdateList.forEach(lSegment -> {
	    lSegment.setReviewStatus(Boolean.FALSE);
	    getCheckoutSegmentsDAO().update(lUser, lSegment);
	});
    }

    public void validateRFCUpdateAllowed(List<RFCDetailsDTO> rfcDetails) {
	rfcDetails.stream().filter(rfc -> Constants.PlanStatus.getAllPlanStatusTillPreProd().keySet().contains(rfc.getRfcDetails().getPlanId().getPlanStatus())).forEach(rfc -> {
	    rfc.setIsupdateallowed(true);
	});
    }

    public List<GitSearchResult> getProdAndNonProdMakSegs(User pUser, String makFile, String sysName, String implId) throws IOException {
	List<GitSearchResult> makFileList = new ArrayList<>();
	JSONResponse response = new JSONResponse();
	response = nonProdFileSearchBySystem(pUser, implId, makFile, sysName);
	LOG.info("Response from non prod : " + response.getStatus());
	if (response.getStatus()) {
	    for (GitSearchResult gitRes : (List<GitSearchResult>) response.getData()) {
		gitRes.setProdFlag("NONPROD");
	    }
	    makFileList.addAll((List<GitSearchResult>) response.getData());
	}
	LOG.info("Non prod size: " + makFileList.size() + " Missing from non prod : " + makFileList.stream().map(x -> x.getProgramName()).collect(Collectors.joining(",")));

	// To get Legacy Mak file from Git DB
	Implementation lImplementation = getImplementationDAO().find(implId);
	String lCompanyName = lImplementation.getPlanId().getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
	Set<String> lAllowedRepos = getGitHelper().getUserAllowedReadAndAboveRepos(pUser.getId(), lCompanyName);
	Set<String> lObsRepos = getGitHelper().getObsRepoList(lCompanyName);
	lAllowedRepos.removeAll(lObsRepos);
	List<String> systemList = new ArrayList<>();
	systemList.add(sysName);
	Collection<GitSearchResult> lSearchResult = getjGITSearchUtils().SearchAllRepos(lCompanyName.toLowerCase(), makFile.toLowerCase(), lImplementation.getPlanId().getMacroHeader(), null, lAllowedRepos, systemList);
	LOG.info("prod size: " + lSearchResult.size() + " Missing from prod : " + lSearchResult.stream().map(x -> x.getProgramName()).collect(Collectors.joining(",")));

	if (lSearchResult != null && !lSearchResult.isEmpty()) {
	    makFileList.addAll(lSearchResult);
	}
	LOG.info("mak size: " + makFileList.size() + " Mak files : " + makFileList.stream().map(x -> x.getProgramName()).collect(Collectors.joining(",")));

	if (!makFileList.isEmpty()) {
	    getGitHelper().searchFileResultFilter(pUser.getId(), makFileList, lCompanyName, null);
	}
	return makFileList;
    }

    public JSONResponse nonProdFileSearchBySystem(User user, String implId, String fileName, String targetSystem) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    Implementation lImp = getImplementationDAO().find(implId);
	    String lCompanyName = lImp.getPlanId().getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
	    Set<String> lUserAllowedRepos = getGitHelper().getUserAllowedReadAndAboveRepos(user.getId(), lCompanyName);
	    if (lUserAllowedRepos == null) {
		throw new WorkflowException("Unable to get search result, dont have access permission for any repositories.");
	    }
	    Set<String> lRepoList = new HashSet();
	    for (String lRepo : lUserAllowedRepos) {
		lRepoList.add(getGitHelper().getSourceURLByRepoName(lRepo));
	    }
	    List<CheckoutSegments> lResult = getCheckoutSegmentsDAO().findByFileNameBySystem(fileName, implId, lRepoList, targetSystem);
	    List<GitSearchResult> lReturnList = new ArrayList();
	    if (lResult != null && !lResult.isEmpty()) {
		Map<String, GitSearchResult> lFilteredList = new HashMap();
		Map<String, List> lFilteredSystemList = new HashMap();
		for (CheckoutSegments lSegment : lResult) {
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
		    lValue.setRefLoadDate(lSegment.getSystemLoad().getLoadDateTime());
		    if (lImp.getPlanId().getMacroHeader() && !(lSegment.getProgramName().endsWith(".mac") || lSegment.getProgramName().endsWith(".hpp") || lSegment.getProgramName().endsWith(".h") || lSegment.getProgramName().endsWith(".cpy") || lSegment.getProgramName().endsWith(".inc") || lSegment.getProgramName().endsWith(".incafs"))) {
			lValue.setIsCheckoutAllowed(Boolean.FALSE);
			lValue.setIsBranchSelected(Boolean.FALSE);
		    }
		    if (lSegment.getSystemLoad().getLoadCategoryId() != null) {
			lValue.addAdditionalInfo("categoryName", lSegment.getSystemLoad().getLoadCategoryId().getName());
		    }
		    String idStringWithPlan = CheckoutUtils.getIdStringWithPlan(lSegment);
		    if (lFilteredList.containsKey(idStringWithPlan)) {
			if (!lFilteredSystemList.get(CheckoutUtils.getIdStringWithPlanSysCheck(lSegment)).contains(lSegment.getTargetSystem())) {
			    lFilteredList.get(idStringWithPlan).getTargetSystems().add(lValue.getTargetSystem());
			    lFilteredList.get(idStringWithPlan).addBranch(lValue);
			    lFilteredSystemList.get(CheckoutUtils.getIdStringWithPlanSysCheck(lSegment)).add(lSegment.getTargetSystem());
			}
		    } else {
			lKey.getTargetSystems().add(lValue.getTargetSystem());
			lKey.addBranch(lValue);
			lFilteredList.put(idStringWithPlan, lKey);
			List lSystemList = new ArrayList();
			lSystemList.add(lSegment.getTargetSystem());
			lFilteredSystemList.put(CheckoutUtils.getIdStringWithPlanSysCheck(lSegment), lSystemList);
			lReturnList.add(lKey);
		    }
		}
		if (!lReturnList.isEmpty()) {
		    getGitHelper().searchFileResultFilter(user.getId(), lReturnList, lCompanyName, null);
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

    public ByteArrayOutputStream getInMemoryZipFile(String fileIds) {

	Set<String> fileNames = Arrays.asList(EncryptUtil.decrypt(fileIds, Constants.SECRET_HASH).split(",")).stream().filter(x -> x != null && !x.isEmpty()).collect(Collectors.toSet());
	ByteArrayOutputStream baos = null;
	if (fileNames != null && !fileNames.isEmpty()) {
	    baos = new ByteArrayOutputStream();
	    try (ZipOutputStream zipOutputStream = new ZipOutputStream(baos)) {
		for (String file : fileNames) {
		    File lFile = new File(wfConfig.getAttachmentDirectory() + file);
		    if (lFile.exists()) {
			zipOutputStream.putNextEntry(new ZipEntry(lFile.getName()));
			IOUtils.copy(new FileInputStream(lFile), zipOutputStream);
			zipOutputStream.closeEntry();
		    }
		}
	    } catch (IOException e) {
		LOG.error("Failed to stream input file: " + e.getMessage(), e);
	    }
	}
	return baos;
    }

    @Transactional
    public Map<String, List<DependenciesForm>> getPlanDependencyList(List<String> pPlanIds, String pFilter, Integer offset, Integer limit) {
	Map<String, List<DependenciesForm>> planBasedDepPlans = new HashMap<>();
	try {
	    for (String planId : pPlanIds) {
		List<DependenciesForm> lDepPlanList = new ArrayList<>();
		List<DependenciesForm> segmentRelatedPlans = new ArrayList<>();
		// Manual Dependent Plans
		segmentRelatedPlans = getImpPlanDAO().getManualDependentPlans(planId);
		if (segmentRelatedPlans != null && !segmentRelatedPlans.isEmpty()) {
		    lDepPlanList.addAll(segmentRelatedPlans);
		    segmentRelatedPlans.clear();
		}

		// Devops Plan List
		segmentRelatedPlans = getImpPlanDAO().getPreSegmentDepententRelatedPlans(planId, pFilter, offset, limit);
		if (segmentRelatedPlans != null && !segmentRelatedPlans.isEmpty()) {
		    lDepPlanList.addAll(segmentRelatedPlans);
		}

		// Legacy Plan List
		List<String> lLegacyList = getLegacyImpPlanList(planId);
		DependenciesForm legacyPlanForm = new DependenciesForm();
		for (String lLegacyPlan : lLegacyList) {
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		    String[] split = lLegacyPlan.split(",");
		    legacyPlanForm.setPlanid(split[0]);
		    legacyPlanForm.setTargetsystem(split[1]);
		    legacyPlanForm.setLoadtype(split[2]);
		    legacyPlanForm.setStatus(split[3]);
		    legacyPlanForm.setSegments(split[6]);
		    Date loadDateTime = dateFormat.parse(split[4]);
		    legacyPlanForm.setLoaddatetime(loadDateTime);
		    lDepPlanList.add(legacyPlanForm);
		}

		planBasedDepPlans.put(planId, lDepPlanList);
	    }
	} catch (Exception ex) {
	    LOG.error("Unable to get the depenent Plan", ex);
	    throw new WorkflowException("Unable to get the depenent Plan", ex);
	}
	return planBasedDepPlans;
    }

    public List<String> getLegacyImpPlanList(String planId) {
	List<SystemLoad> lSystemLoads = getSystemLoadDAO().getSystemLoadsFromImp(planId);
	List<String> lLegacyList = new ArrayList<>();
	if (lSystemLoads != null) {
	    for (SystemLoad lSystemLoad : lSystemLoads) {
		JSONResponse lCommandResponse = new JSONResponse();
		List<CheckoutSegments> lSegmentList = getCheckoutSegmentsDAO().findBySystemLoad(lSystemLoad.getId());
		List<String> lSegments = new ArrayList<>();
		for (CheckoutSegments lSegment : lSegmentList) {
		    if (!lSegment.getProgramName().contains(".mak")) {
			lSegments.add(lSegment.getProgramName());
		    }
		}
		String lCommand = Constants.SystemScripts.DATE_AUDIT_CROSS_CHECK.getScript() + " " + lSystemLoad.getSystemId().getName().toLowerCase() + " " + Constants.REX_DATEFORMAT.get().format(lSystemLoad.getLoadDateTime()) + " " + StringUtils.join(lSegments, ",");

		lCommandResponse = getsSHClientUtils().executeCommand(lSystemLoad.getSystemId(), lCommand);
		if (!lCommandResponse.getStatus() && lCommandResponse.getErrorMessage() != null) {
		    try {
			/*
			 * String lString=null;
			 * lString="H180177001,WSP,STANDARD,PASS REGR,20181109,0500,TLLRAM\r\n" +
			 * "H180178001,WSP,STANDARD,PASS REGR,20181109,0500,TLIRA4\r\n" + "1\r\n" +
			 * "RXSNDIU: return code 1 from UTILSEGT wsp 20181109 0500 tlir.asm tllr.asm\r\n"
			 * ; LOG.info("lString"+ lString);
			 */
			String lString = (String) lCommandResponse.getErrorMessage();
			List<String> legacyListPlan = IOUtils.readLines(new StringReader(lString));
			for (String legacyPlan : legacyListPlan) {
			    legacyPlan = legacyPlan.replace("Error Code: 8", "");
			    if (legacyPlan.trim().equals("0") || legacyPlan.trim().equals("1")) {
				break;
			    }
			    lLegacyList.add(legacyPlan);
			}
		    } catch (IOException ex) {
			LOG.error("Error in getting command response: " + ex);
			throw new WorkflowException("Error in getting command response", ex);
		    }
		}
	    }
	}
	return lLegacyList;
    }

    public HashMap<String, String> getPlanBasedDependencyList(List<String> pPlanIds, String pFilter, Integer offset, Integer limit) {
	Map<String, List<DependenciesForm>> lDepPlanMap = getPlanDependencyList(pPlanIds, pFilter, offset, limit);
	HashMap<String, String> planBasedDepMap = new HashMap<>();
	if (lDepPlanMap != null && !lDepPlanMap.isEmpty()) {
	    lDepPlanMap.forEach((key, value) -> {
		Set<String> commaSepDepPlanIds = value.stream().map(x -> x.getPlanid()).collect(Collectors.toSet());
		planBasedDepMap.put(key, commaSepDepPlanIds.stream().collect(Collectors.joining(",")));
	    });
	}
	return planBasedDepMap;

    }

    public Boolean updateBuildQueueCache() throws Exception {
	List<String> inQueuePlans = getBuildDAO().getBuildQueuePlan(Arrays.asList(Constants.BUILD_TYPE.DVL_BUILD.name(), Constants.BUILD_TYPE.STG_BUILD.name()));
	Set<String> runBuildPlans = cacheClient.getBuildQueueInfo().get("RUN");
	Set<String> queueBuildPlans = cacheClient.getBuildQueueInfo().get("WAIT");
	Set<String> removePlans = new HashSet();

	if (runBuildPlans == null && queueBuildPlans == null) {
	    return true;
	}

	if (inQueuePlans == null || inQueuePlans.isEmpty()) {
	    cacheClient.getBuildQueueInfo().clear();
	    return true;
	}

	if (runBuildPlans != null && !runBuildPlans.isEmpty()) {
	    runBuildPlans.stream().filter(t -> !inQueuePlans.contains(t)).forEach(t -> {
		removePlans.remove(t);
	    });
	    runBuildPlans.removeAll(removePlans);
	    removePlans.clear();
	    cacheClient.getBuildQueueInfo().put("RUN", runBuildPlans);
	}

	if (queueBuildPlans != null && !queueBuildPlans.isEmpty()) {
	    queueBuildPlans.stream().filter(t -> !inQueuePlans.contains(t)).forEach(t -> {
		removePlans.remove(t);
	    });
	    queueBuildPlans.removeAll(removePlans);
	    removePlans.clear();
	    cacheClient.getBuildQueueInfo().put("WAIT", queueBuildPlans);
	}

	return true;
    }

    public Boolean updateFuncAreaWithDesc(List<SourceArtifactSearchResult> segments) {
	segments.stream().forEach(seg -> {
	    // Update Source Repo Information - 2697
	    String repoName = "";
	    if (seg.getCheckoutrefstatus() == null || !seg.getCheckoutrefstatus().equalsIgnoreCase("prod")) {
		repoName = getGitHelper().getRepositoryNameBySourceURL(seg.getSourceurl());
		repoName = getGitHelper().getTrimmedName(repoName);
	    } else {
		repoName = getGitHelper().getTrimmedName(seg.getSourceurl());
	    }
	    if (repoName != null && !repoName.isEmpty()) {
		RepositoryView repoView = cacheClient.getFilteredRepositoryMap().get(repoName);
		if (repoView != null) {
		    seg.setFuncpackage(repoView.getRepository().getDescription() + "[" + seg.getFuncpackage() + "]");
		}
	    }

	});
	return true;
    }

    public Boolean updateFuncAreaWithDescOfSegments(List<CheckoutSegments> segments) {
	segments.stream().forEach(seg -> {
	    // Update Source Repo Information - 2697
	    String repoName = getGitHelper().getRepositoryNameBySourceURL(seg.getSourceUrl());
	    if (repoName != null && !repoName.isEmpty()) {
		RepositoryView repoView = cacheClient.getFilteredRepositoryMap().get(repoName.replace(".git", "").toUpperCase());
		if (repoView != null) {
		    seg.setRepoDesc(repoView.getRepository().getDescription() + "[" + seg.getFuncArea() + "]");
		}
	    }
	});

	return true;
    }
}
