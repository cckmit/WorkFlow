package com.tsi.workflow.helper;

import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.activity.CommonActivityMessage;
import com.tsi.workflow.beans.dao.Dbcr;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.RFCDetails;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.AdvancedMetaSearchResult;
import com.tsi.workflow.beans.ui.RFCInboxForm;
import com.tsi.workflow.beans.ui.RFCReportForm;
import com.tsi.workflow.beans.ui.RFCSysDetail;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.DbcrDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.RFCDetailsDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.report.RFCReportCreator;
import com.tsi.workflow.service.RFCChangeMgmtService;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.EncryptUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RFCHelper {

    private static final Logger LOG = Logger.getLogger(RFCHelper.class.getName());
    @Autowired
    ImplementationDAO implementationDAO;

    @Autowired
    WFConfig wfConfig;

    @Autowired
    DbcrDAO dbcrDAO;

    @Autowired
    SystemDAO systemDAO;

    @Autowired
    RFCDetailsDAO rfcDetailsDAO;

    @Autowired
    ImpPlanDAO impPlanDAO;

    @Autowired
    WFConfig wFConfig;

    @Autowired
    SystemLoadDAO systemLoadDAO;

    @Autowired
    ActivityLogDAO activityLogDAO;

    @Autowired
    CommonHelper commonHelper;

    @Autowired
    RFCChangeMgmtService rFCChangeMgmtService;

    public RFCChangeMgmtService getRFCChangeMgmtService() {
	return rFCChangeMgmtService;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public RFCDetailsDAO getRfcDetailsDAO() {
	return rfcDetailsDAO;
    }

    public ImplementationDAO getImplementationDAO() {
	return implementationDAO;
    }

    public DbcrDAO getDbcrDAO() {
	return dbcrDAO;
    }

    public SystemDAO getSystemDAO() {
	return systemDAO;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public WFConfig getWFConfig() {
	return wFConfig;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public List<RFCInboxForm> getRFCFormList(List<AdvancedMetaSearchResult> rfcInboxPlans) {
	HashMap<String, List<String>> dbcrDetails = new HashMap<>();
	HashMap<String, RFCInboxForm> rfcFormPlans = new HashMap<>();

	List<ImpPlan> impPlans = getImpPlanDAO().find(rfcInboxPlans.stream().map(x -> x.getPlanid()).collect(Collectors.toList()));

	if (impPlans != null && !impPlans.isEmpty()) {
	    HashMap<String, ImpPlan> impPlanBasedOnPlanMap = new HashMap<>();
	    impPlans.forEach(impPlan -> {
		impPlanBasedOnPlanMap.put(impPlan.getId(), impPlan);
	    });

	    rfcInboxPlans.forEach(rfcDetail -> {
		String key = rfcDetail.getPlanid() + "-" + rfcDetail.getTargetsystem() + "-" + rfcDetail.getLoadcategory();
		if (dbcrDetails.get(key) == null) {
		    dbcrDetails.put(key, new ArrayList<String>());
		}
		dbcrDetails.get(key).add(rfcDetail.getDbcrname());

		if (!rfcFormPlans.containsKey(rfcDetail.getPlanid())) {
		    RFCInboxForm rfcInboxForm = new RFCInboxForm();
		    rfcInboxForm.setId(impPlanBasedOnPlanMap.get(rfcDetail.getPlanid()));

		    /**
		     * RFC Schedular Flag check plan level
		     */
		    List<RFCDetails> lRFCDetailsList = rfcDetailsDAO.checkRFCSchedularFlag(rfcDetail.getPlanid());
		    for (RFCDetails lRFCDetails : lRFCDetailsList) {
			if (lRFCDetails.getReadyToSchedule()) {
			    rfcInboxForm.setRfcSchedularFlag(Boolean.TRUE);
			} else {
			    rfcInboxForm.setRfcSchedularFlag(Boolean.FALSE);
			}

		    }

		    if (lRFCDetailsList.isEmpty()) {
			rfcInboxForm.setRfcSchedularFlag(null);
		    }
		    rfcInboxForm.setLoadType(rfcDetail.getLoadtype());
		    rfcInboxForm.setPlanStatus(rfcDetail.getPlanstatus());

		    List<RFCSysDetail> rfcForms = new ArrayList<>();
		    rfcInboxForm.setRfcDetails(rfcForms);
		    rfcFormPlans.put(rfcDetail.getPlanid(), rfcInboxForm);
		}

		RFCSysDetail rfcSysDetail = new RFCSysDetail();
		rfcSysDetail.setTargetSystem(rfcDetail.getTargetsystem());
		rfcSysDetail.setLoadDateTime(rfcDetail.getLoaddatetime());
		rfcSysDetail.setCategory(rfcDetail.getLoadcategory());
		rfcSysDetail.setRfcNumber(rfcDetail.getRfcnumber());
		rfcFormPlans.get(rfcDetail.getPlanid()).getRfcDetails().add(rfcSysDetail);

	    });

	    rfcFormPlans.forEach((key, value) -> {
		value.getRfcDetails().forEach(rfcDetail -> {
		    String dbcrKey = key + "-" + rfcDetail.getTargetSystem() + "-" + rfcDetail.getCategory();
		    if (dbcrDetails.containsKey(dbcrKey)) {
			rfcDetail.setDbcr(dbcrDetails.get(dbcrKey));
		    }
		});
	    });
	}
	return rfcFormPlans.values().stream().collect(Collectors.toList());
    }

    public HashMap<String, String> getApprovalFileList(List<RFCReportForm> rfcReports) {
	HashMap<String, Set<String>> planAndApprovalFiles = new HashMap<>();
	HashMap<String, String> planIdAndUrls = new HashMap<>();

	rfcReports.stream().filter(rfc -> rfc.getApprovalfilename() != null && !rfc.getApprovalfilename().trim().isEmpty()).forEach(rfc -> {
	    if (!planAndApprovalFiles.containsKey(rfc.getPlanid())) {
		Set<String> fileNames = new HashSet<>();
		planAndApprovalFiles.put(rfc.getPlanid(), fileNames);
	    }
	    planAndApprovalFiles.get(rfc.getPlanid()).add(rfc.getPlanid() + File.separator + getWFConfig().getLoadApprovalDirectory() + File.separator + rfc.getApprovalfilename());
	});

	planAndApprovalFiles.forEach((key, values) -> {
	    StringBuffer sb = new StringBuffer();
	    sb.append(wfConfig.getSsoAPIUrl() + "/");
	    sb.append("common/downloadFile?fileId=");
	    sb.append(EncryptUtil.encrypt(values.stream().collect(Collectors.joining(",")), Constants.SECRET_HASH));
	    planIdAndUrls.put(key, sb.toString());
	});

	return planIdAndUrls;
    }

    public HashMap<String, Set<String>> getProgNamesBasedOnPlanId(List<RFCReportForm> rfcReports) {
	HashMap<String, Set<String>> planAndSegNames = new HashMap<>();
	rfcReports.stream().filter(rfc -> rfc.getProgramname() != null && !rfc.getProgramname().isEmpty()).forEach(rfc -> {
	    if (!planAndSegNames.containsKey(rfc.getPlanid())) {
		Set<String> progNames = new HashSet<>();
		progNames.add(rfc.getProgramname());
		planAndSegNames.put(rfc.getPlanid(), progNames);
	    } else {
		planAndSegNames.get(rfc.getPlanid()).add(rfc.getProgramname());
	    }
	});
	return planAndSegNames;
    }

    public HashMap<String, String> getTestFileBasedOnPlanIds(List<RFCReportForm> rfcReports) {
	HashMap<String, Set<String>> planIdAndTestFiles = new HashMap<>();
	HashMap<String, String> planIdAndTestFilesUrl = new HashMap<>();

	if (rfcReports != null && !rfcReports.isEmpty()) {
	    List<ImpPlan> impPlans = new ArrayList<>();
	    rfcReports.stream().map(rfc -> rfc.getPlanid()).forEach(id -> {
		impPlans.add(new ImpPlan(id));
	    });

	    getImplementationDAO().findByImpPlan(impPlans).forEach(impl -> {
		Set<String> lResult = new HashSet<>();
		File lDir = new File(wfConfig.getAttachmentDirectory() + impl.getPlanId().getId() + File.separator + wfConfig.getTestResultsDir() + File.separator + impl.getId() + File.separator);
		if (lDir.exists()) {
		    List<File> files = (List<File>) FileUtils.listFiles(lDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		    for (File file : files) {
			lResult.add(impl.getPlanId().getId() + File.separator + getWFConfig().getTestResultsDir() + File.separator + impl.getId() + File.separator + file.getName());
		    }
		}

		if (!lResult.isEmpty()) {
		    if (!planIdAndTestFiles.containsKey(impl.getPlanId().getId())) {
			planIdAndTestFiles.put(impl.getPlanId().getId(), new HashSet<String>());
		    }
		    planIdAndTestFiles.get(impl.getPlanId().getId()).addAll(lResult);
		}
	    });
	}

	planIdAndTestFiles.forEach((key, value) -> {
	    StringBuffer sb = new StringBuffer();
	    sb.append(wfConfig.getSsoAPIUrl() + "/");
	    sb.append("common/downloadFile?fileId=");
	    sb.append(EncryptUtil.encrypt(value.stream().collect(Collectors.joining(",")), Constants.SECRET_HASH));
	    planIdAndTestFilesUrl.put(key, sb.toString());
	});
	return planIdAndTestFilesUrl;
    }

    public HashMap<String, Set<String>> getDbcrDetails(List<String> systemNames, List<RFCReportForm> rfcReports) {
	HashMap<String, Set<String>> dbcrByPlanMap = new HashMap<>();
	List<ImpPlan> impPlans = new ArrayList<>();
	rfcReports.stream().map(rfc -> rfc.getPlanid()).forEach(id -> {
	    impPlans.add(new ImpPlan(id));
	});

	List<Dbcr> dbcrList = new ArrayList<>();

	if (systemNames != null && !systemNames.isEmpty()) {
	    for (String systemName : systemNames) {
		com.tsi.workflow.beans.dao.System system = getSystemDAO().findByName(systemName);
		dbcrList.addAll(getDbcrDAO().findByPlanSystemId(impPlans, system.getId()));
	    }
	} else {
	    dbcrList = getDbcrDAO().findByPlanId(impPlans);
	}

	dbcrList.forEach(dbcr -> {
	    String key = dbcr.getPlanId().getId() + "_" + dbcr.getSystemId().getName();
	    if (!dbcrByPlanMap.containsKey(key)) {
		dbcrByPlanMap.put(key, new HashSet<String>());
	    }
	    dbcrByPlanMap.get(key).add(dbcr.getDbcrName());
	});

	return dbcrByPlanMap;
    }

    public void getRFCReportForm(RFCReportCreator rfcReportCreator, List<String> system, Date startDate, Date endDate, String rfcNumber, Boolean formHyperLink, LinkedHashMap<String, String> pOrderBy) {
	List<RFCReportForm> rfcReports = getRfcDetailsDAO().getRFCReport(system, startDate, endDate, rfcNumber);
	Set<ImpPlan> planIds = new HashSet<>();

	rfcReports.forEach(rfc -> {
	    planIds.add(new ImpPlan(rfc.getPlanid()));
	});

	HashMap<String, String> approvalFileMap = getApprovalFileList(rfcReports);
	HashMap<String, Set<String>> planBasedSegments = getProgNamesBasedOnPlanId(rfcReports);
	HashMap<String, String> testFileNameMap = getTestFileBasedOnPlanIds(rfcReports);
	HashMap<String, Set<String>> dbcrByPlans = getDbcrDetails(system, rfcReports);
	List<RFCReportForm> uniqueRfcReport = getSortedRFCForms(pOrderBy, rfcReports);
	rfcReportCreator.addRFCDetails(commonHelper, uniqueRfcReport, approvalFileMap, planBasedSegments, testFileNameMap, dbcrByPlans, formHyperLink);

    }

    public void updateSystemwiseRFCDetails(User user, RFCDetails pRFCDetails) throws Exception {
	List<RFCDetails> rfcDetails = getRfcDetailsDAO().findByImpPlan(pRFCDetails.getPlanId().getId());

	List<SystemLoad> systemLoads = getSystemLoadDAO().findByImpPlan(pRFCDetails.getPlanId().getId());
	String processedSystem = pRFCDetails.getSystemLoadId().getSystemId().getName();
	for (SystemLoad sysLoad : systemLoads) {
	    RFCDetails tempRFCDetails = new RFCDetails();
	    BeanUtils.copyProperties(tempRFCDetails, pRFCDetails);
	    if (!sysLoad.getSystemId().getName().equalsIgnoreCase(processedSystem)) {
		CommonActivityMessage lMessage = new CommonActivityMessage(pRFCDetails.getPlanId(), null);
		StringBuilder sb = new StringBuilder();
		if (rfcDetails != null && !rfcDetails.isEmpty() && rfcDetails.stream().filter(rfc -> rfc.getActive().equals("Y") && rfc.getSystemLoadId().getId() == sysLoad.getId()).findAny().isPresent()) {
		    RFCDetails lRFCDetails = rfcDetails.stream().filter(rfc -> rfc.getActive().equals("Y") && rfc.getSystemLoadId().getId() == sysLoad.getId()).findAny().get();
		    tempRFCDetails.setSystemLoadId(sysLoad);
		    tempRFCDetails.setId(lRFCDetails.getId());
		    tempRFCDetails.setActive("Y");
		    tempRFCDetails.setRfcNumber((lRFCDetails.getRfcNumber() != null && !lRFCDetails.getRfcNumber().isEmpty()) ? lRFCDetails.getRfcNumber() : null);
		    getRfcDetailsDAO().update(user, tempRFCDetails);
		    sb.append("RFC fields updated for the System " + sysLoad.getSystemId().getName() + " in the Implementation Plan " + pRFCDetails.getPlanId().getId());
		} else {
		    tempRFCDetails.setId(null);
		    tempRFCDetails.setSystemLoadId(sysLoad);
		    tempRFCDetails.setRfcNumber(null);
		    tempRFCDetails.setActive("Y");
		    tempRFCDetails.setModifiedBy(null);
		    tempRFCDetails.setModifiedDt(null);
		    getRfcDetailsDAO().save(user, tempRFCDetails);
		    sb.append("RFC fields added for the System " + sysLoad.getSystemId().getName() + " in the Implementation Plan " + pRFCDetails.getPlanId().getId());
		}

		lMessage.setStatus("SUCCESS");
		lMessage.setMessage(sb.toString());
		getActivityLogDAO().save(user, lMessage);
	    }

	}
    }

    public List<RFCReportForm> getSortedRFCForms(LinkedHashMap<String, String> pOrderBy, List<RFCReportForm> rfcForms) {
	List<RFCReportForm> sortedRfcForms = new ArrayList<>();
	if (pOrderBy == null || pOrderBy.isEmpty()) {
	    sortedRfcForms = rfcForms.stream().sorted(Comparator.comparing(RFCReportForm::getLoaddatetime).thenComparing(RFCReportForm::getPlanid).thenComparing(RFCReportForm::getTargetsystem)).collect(Collectors.toList());
	} else if (pOrderBy != null && !pOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> entrySet : pOrderBy.entrySet()) {
		String key = entrySet.getKey();
		String value = entrySet.getValue();
		if ("planid".equalsIgnoreCase(key)) {
		    if (value.equalsIgnoreCase("asc")) {
			sortedRfcForms = rfcForms.stream().sorted(Comparator.comparing(RFCReportForm::getPlanid)).collect(Collectors.toList());
		    } else {
			sortedRfcForms = rfcForms.stream().sorted(Comparator.comparing(RFCReportForm::getPlanid).reversed()).collect(Collectors.toList());
		    }
		} else if ("rfcnumber".equalsIgnoreCase(key)) {
		    if (value.equalsIgnoreCase("asc")) {
			sortedRfcForms = rfcForms.stream().sorted(Comparator.comparing(RFCReportForm::getRfcnumber).thenComparing(RFCReportForm::getLoaddatetime).thenComparing(RFCReportForm::getPlanid).thenComparing(RFCReportForm::getTargetsystem)).collect(Collectors.toList());
		    } else {
			sortedRfcForms = rfcForms.stream().sorted(Comparator.comparing(RFCReportForm::getRfcnumber).reversed().thenComparing(RFCReportForm::getLoaddatetime).thenComparing(RFCReportForm::getPlanid).thenComparing(RFCReportForm::getTargetsystem)).collect(Collectors.toList());
		    }
		} else if ("targetsystem".equalsIgnoreCase(key)) {
		    if (value.equalsIgnoreCase("asc")) {
			sortedRfcForms = rfcForms.stream().sorted(Comparator.comparing(RFCReportForm::getTargetsystem).thenComparing(RFCReportForm::getLoaddatetime).thenComparing(RFCReportForm::getPlanid)).collect(Collectors.toList());
		    } else {
			sortedRfcForms = rfcForms.stream().sorted(Comparator.comparing(RFCReportForm::getTargetsystem).reversed().thenComparing(RFCReportForm::getLoaddatetime).thenComparing(RFCReportForm::getPlanid)).collect(Collectors.toList());
		    }
		} else if ("loaddatetime".equalsIgnoreCase(key)) {
		    if (value.equalsIgnoreCase("asc")) {
			sortedRfcForms = rfcForms.stream().sorted(Comparator.comparing(RFCReportForm::getLoaddatetime).thenComparing(RFCReportForm::getPlanid)).collect(Collectors.toList());
		    } else {
			sortedRfcForms = rfcForms.stream().sorted(Comparator.comparing(RFCReportForm::getLoaddatetime).reversed().thenComparing(RFCReportForm::getPlanid)).collect(Collectors.toList());
		    }
		}
	    }
	}

	return sortedRfcForms.stream().distinct().collect(Collectors.toList());
    }
}
