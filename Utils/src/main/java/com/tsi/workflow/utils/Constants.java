/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */
package com.tsi.workflow.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;

public class Constants {

    private static final Logger LOG = Logger.getLogger(Constants.class.getName());

    public enum CacheCodes {

	DELEGATION,
	SUPER_USER,
	LOGIN,
	LOGIN_SESSION,
	ALL_REPOSITORY,
	FILTERED_REPOSITORY,
	ALL_USER,
	REPO_WISE_USER,
	WEB_SOCKET,
	WEB_SOCKET_USER_MAP,
	R_SUB_REPO_ID_MAP,
	R_REPO_ID_MAP,
	R_FILE_NAME_MAP,
	PLAN_STATUS_UPDATE,
	IN_PROGRESS_RELATED_PLANS,
	PROD_TOS_LOAD_ACT_PLANS,
	PROD_TOS_ACCEPT_PLANS,
	ONLINE_PLAN,
	INPROGRESS_CHECKOUT,
	INPROGRESS_MOVE_ARTIFACT,
	PROD_TOS_TPF_COMMAND,
	API_TRANSACTION_INFO,
	AUDIT_API_ACTIONS,
	AUDIT_LNX_SERVERS,
	INPROGRESS_USER_ACTION,
	AUDIT_CONFIG,
	AUDIT_GI_TRANSACTION_INFO,
	REQUEST_MONITOR,
	VALIDATE_MAK_FILE,
	BUILD_QUEUE_INFO,
	PLAN_ACTIONS;
    }

    public enum MailSenderRole {
	LEAD,
	DEV_MANAGER,
	QA_FUNCTIONAL,
	DEVELOPER,
	LOADS_ATTENDEE,
	PEER_REVIEWER,
	REPO_OWNERS,
	NO_ROLE;
    }

    public enum PRODSearchType {
	ONLINE_ONLY,
	PENDING_ONLY,
	BOTH;
    }

    public enum UserSettings {

	DELEGATE_USER,
	DELEGATION,
	REPO_OWNER_ALERT;
    }

    public enum PUTLevelOptions {
	INITIAL,
	PRE_PROD_CO_EXIST,
	DEVELOPMENT,
	LOCKDOWN,
	PROD_CO_EXIST,
	PRODUCTION,
	BACKUP,
	ARCHIVE,
	INACTIVE
    }

    // Future PutLevel Details Display
    public static List<String> getFuturePutLevel() {
	List<String> lList = new ArrayList<String>();
	lList.add(PUTLevelOptions.DEVELOPMENT.name());
	lList.add(PUTLevelOptions.PRE_PROD_CO_EXIST.name());
	lList.add(PUTLevelOptions.LOCKDOWN.name());
	lList.add(PUTLevelOptions.PROD_CO_EXIST.name());
	lList.add(PUTLevelOptions.BACKUP.name());
	return lList;
    }

    public enum UserGroup {

	Developer(true, true),
	Lead(true, true),
	Reviewer(true, true),
	LoadsControl(false, true),
	QA(false, true),
	SystemSupport(false, true),
	DevManager(true, true),
	TechnicalServiceDesk(false, true),
	ToolAdmin(false, true),
	ServerAccess(false, true),
	Maintenance(false, false),
	QADeployLead(false, true),
	DLCoreChangeTeam(false, true);

	boolean accessCheck;
	boolean display;

	private UserGroup(boolean accessCheck, boolean display) {
	    this.accessCheck = accessCheck;
	    this.display = display;
	}

	public boolean isAccessCheck() {
	    return accessCheck;
	}

	public void setAccessCheck(boolean accessCheck) {
	    this.accessCheck = accessCheck;
	}

	public boolean isDisplay() {
	    return display;
	}

	public void setDisplay(boolean display) {
	    this.display = display;
	}

	public static List<String> getDelegatedUserList() {
	    ArrayList<String> lList = new ArrayList<String>();
	    lList.add(DevManager.name());
	    return lList;
	}

	public static List<String> getKeyMappingGroupList() {
	    ArrayList<String> lList = new ArrayList<>();
	    for (UserGroup userGroup : values()) {
		if (userGroup.isAccessCheck() && userGroup.isDisplay()) {
		    lList.add(userGroup.name());
		}
	    }
	    return lList;
	}

	public static List<String> getLoadAttendeeUserList() {
	    List<String> lReturnList = new ArrayList();
	    lReturnList.add(Developer.name());
	    lReturnList.add(Lead.name());
	    lReturnList.add(DevManager.name());
	    return lReturnList;
	}

    }

    public enum FILE_TYPE {

	IBM,
	NON_IBM
    }

    public enum LoadTypes {

	STANDARD("Standard"),
	EXCEPTION("Exception"),
	EMERGENCY("Emergency");

	private final String lLoadTypes;

	private LoadTypes(String pLoadType) {
	    lLoadTypes = pLoadType;
	}

	public String getLoadTypes() {
	    return lLoadTypes;
	}

	public static LinkedHashMap<String, String> getLoadTypesList() {
	    LinkedHashMap<String, String> lLoaderList = new LinkedHashMap<String, String>();
	    for (LoadTypes lLoadType : LoadTypes.values()) {
		lLoaderList.put(lLoadType.name(), lLoadType.getLoadTypes());
	    }
	    return lLoaderList;
	}

    }

    public enum LoginErrorCode {

	SUCCESS("Login Success"),
	USER_DISABLED("No Valid Roles Mapped, Please Contact Administator. <br><br><h5> Note: Newly added roles will be reflected in an hour</h5>"),
	WRONG_USER_NAME_OR_PASSWORD("Wrong Username or Password");

	String lDescription;

	private LoginErrorCode(String pDescription) {
	    lDescription = pDescription;
	}

	public String getDescription() {
	    return lDescription;
	}

    }

    public enum TagStatus {

	REJECTED("Rejected"),
	AUTO_REJECTED("Auto-rejected"),
	SECURED("Secured"),
	READY_FOR_QA("Ready for QA");

	String attributes;

	private TagStatus(String attributes) {
	    this.attributes = attributes;
	}

	public static Map<String, String> getStatusMap() {
	    Map<String, String> lReturnList = new HashMap<String, String>();
	    for (TagStatus lStatus : values()) {
		lReturnList.put(lStatus.name(), lStatus.attributes);
	    }
	    return lReturnList;
	}

    }

    public enum ImplementationStatus {

	IN_PROGRESS("In-Progress"),
	READY_FOR_QA("Ready for QA");

	String attributes;

	private ImplementationStatus(String pAttributes) {
	    this.attributes = pAttributes;
	}

	public static Map<String, String> getStatusMap() {
	    Map<String, String> lReturnList = new HashMap<String, String>();
	    for (ImplementationStatus lStatus : values()) {
		lReturnList.put(lStatus.name(), lStatus.attributes);
	    }
	    return lReturnList;
	}

    }

    public enum TOSEnvironment {

	PRODUCTION,
	NATIVE_CPU,
	PRE_PROD_TOS,
	NATIVE_TOS;
    }

    public enum VPARSEnvironment {

	INTEGRATION("Integration System"),
	QA_FUCTIONAL("QA Functional System"),
	QA_REGRESSION("QA Regression System"),
	PRE_PROD("Pre Prod System"),
	PRIVATE("Private System");

	String attributes;

	private VPARSEnvironment(String pAttributes) {
	    this.attributes = pAttributes;
	}

	public static Map<String, String> getVparsMap() {
	    Map<String, String> lReturnList = new HashMap<String, String>();
	    for (VPARSEnvironment lStatus : values()) {
		lReturnList.put(lStatus.name(), lStatus.attributes);
	    }
	    return lReturnList;
	}

	public static List<String> getAutoDeactivationEnv() {
	    List<String> lList = new ArrayList<String>();
	    lList.add(QA_FUCTIONAL.name());
	    lList.add(QA_REGRESSION.name());
	    lList.add(PRE_PROD.name());
	    return lList;
	}

    }

    public enum ImplementationSubStatus {

	UNIT_TESTING_COMPLETED("Unit Testing Completed"),
	PEER_REVIEW_COMPLETED("Peer Review Completed"),
	BYPASSED_PEER_REVIEW("ByPassed Peer Review"),
	INTEGRATION_TESTING_COMPLETED("Integration Testing Completed");

	String attributes;

	private ImplementationSubStatus(String pAttributes) {
	    this.attributes = pAttributes;
	}

	public static Map<String, String> getStatusMap() {
	    Map<String, String> lReturnList = new HashMap<String, String>();
	    for (ImplementationSubStatus lStatus : values()) {
		lReturnList.put(lStatus.name(), lStatus.attributes);
	    }
	    return lReturnList;
	}

    }

    public enum SourceArtificatExtension {

	ASM("asm"),
	C("c"),
	CPP("cpp"),
	MAK("mak"),
	MAC("mac"),
	CPY("cpy"),
	H("h"),
	HPP("hpp"),
	INC("inc"),
	XML("xml"),
	XSD("xsd"),
	JSON("json");
	// 2011 SBT("sbt"),

	String attributes;

	private SourceArtificatExtension(String pAttributes) {
	    this.attributes = pAttributes;
	}

	public String getExtension() {
	    return attributes;
	}

	public static SortedSet<String> getIBMSourceArtificatExtList() {
	    SortedSet<String> lExtList = new TreeSet<String>();
	    for (SourceArtificatExtension lExt : SourceArtificatExtension.values()) {
		lExtList.add(lExt.getExtension());
	    }
	    // 2011 lExtList.remove(SBT.getExtension());
	    return lExtList;
	}

	public static SortedSet<String> getNONIBMSourceArtificatExtList() {
	    SortedSet<String> lExtList = new TreeSet<String>();
	    for (SourceArtificatExtension lExt : SourceArtificatExtension.values()) {
		lExtList.add(lExt.getExtension());
	    }
	    return lExtList;
	}

	public static List<String> getSourceArtificatExtList() {
	    List<String> lExtList = new ArrayList<String>();
	    for (SourceArtificatExtension lExt : SourceArtificatExtension.values()) {
		lExtList.add(lExt.getExtension());
	    }
	    return lExtList;
	}
    }

    public enum PlanStatus {

	ACTIVE(1, "Active", true, ""),
	SUBMITTED(2, "Submit In-Progress", true, ""),
	APPROVED(3, "Submitted", true, "Scheduled"),
	DEPLOYED_IN_QA_FUNCTIONAL(4, "Deployed in QA - Functional", true, ""),
	PASSED_FUNCTIONAL_TESTING(5, "Passed Functional Testing", true, ""),
	BYPASSED_FUNCTIONAL_TESTING(6, "ByPassed Functional Testing", true, ""),
	PARTIAL_FUNCTIONAL_TESTING(7, "Passed/ByPassed Functional Testing", true, ""),
	DEPLOYED_IN_QA_REGRESSION(8, "Deployed in QA - Regression", true, ""),
	PASSED_REGRESSION_TESTING(9, "Passed Regression Testing", true, ""),
	BYPASSED_REGRESSION_TESTING(10, "Bypassed Regression Testing", true, ""),
	PARTIAL_REGRESSION_TESTING(11, "Passed/ByPassed Regression Testing", true, ""),
	DEPLOYED_IN_PRE_PRODUCTION(12, "Deployed in Pre-Production", true, ""),
	PASSED_ACCEPTANCE_TESTING(13, "Passed Acceptance Testing", true, ""),
	BYPASSED_ACCEPTANCE_TESTING(14, "ByPassed Acceptance Testing", true, ""),
	DEV_MGR_APPROVED(15, "Approved", true, ""),
	READY_FOR_PRODUCTION_DEPLOYMENT(16, "Ready for Production Deployment", true, ""),
	PARTIALLY_DEPLOYED_IN_PRODUCTION(17, "Partially Deployed In Production", true, ""),
	DEPLOYED_IN_PRODUCTION(18, "Deployed in Production", true, ""),
	PENDING_FALLBACK(19, "Pending Fallback", true, ""),
	ACCEPTED_IN_PRODUCTION(20, "Accepted in Production", true, ""),
	ONLINE(21, "Online", true, "Loaded"),
	FALLBACK_DEPLOYED_IN_PRODUCTION(22, "Fallback Deployed in Production", true, ""),
	ONLINE_RELOAD(23, "Online", false, ""),
	FALLBACK(24, "Fallback", true, "Fallback"),
	DELETED(25, "Deleted", true, "");

	// <editor-fold defaultstate="collapsed" desc="General Methods">
	private final String attributes;
	private final String prStatus;
	private final int position;
	private final boolean display;

	private PlanStatus(int position, String pAttributes, boolean display, String prStatus) {
	    this.attributes = pAttributes;
	    this.position = position;
	    this.display = display;
	    this.prStatus = prStatus;
	}

	public String getPrStatus() {
	    return prStatus;
	}

	public String getDisplayName() {
	    return attributes;
	}

	public static LinkedHashMap<String, String> getStatusMap() {
	    LinkedHashMap<String, String> lReturnList = new LinkedHashMap<String, String>();
	    for (PlanStatus lStatus : values()) {
		if (lStatus.display) {
		    lReturnList.put(lStatus.name(), lStatus.attributes);
		}
	    }
	    return lReturnList;
	}

	public static LinkedHashMap<String, String> getAllStatusMap() {
	    LinkedHashMap<String, String> lReturnList = new LinkedHashMap<String, String>();
	    for (PlanStatus lStatus : values()) {
		lReturnList.put(lStatus.name(), lStatus.attributes);
	    }
	    return lReturnList;
	}

	private static LinkedHashMap<String, String> getStatusMap(PlanStatus from) {
	    LinkedHashMap<String, String> lReturnList = new LinkedHashMap<String, String>();
	    PlanStatus[] values = values();
	    for (int i = from.position - 1; i < values.length; i++) {
		if (values[i].display) {
		    lReturnList.put(values[i].name(), values[i].attributes);
		}
	    }
	    return lReturnList;
	}

	private static LinkedHashMap<String, String> getStatusMap(PlanStatus from, PlanStatus to) {
	    LinkedHashMap<String, String> lReturnList = new LinkedHashMap<String, String>();
	    PlanStatus[] values = values();
	    for (int i = from.position - 1; i < to.position; i++) {
		if (values[i].display) {
		    lReturnList.put(values[i].name(), values[i].attributes);
		}
	    }
	    return lReturnList;
	}

	public static String getStatusMapByPosition(Integer planStatusId) {
	    String lReturn = null;
	    PlanStatus[] values = values();
	    int postion = planStatusId - 1;
	    if (values[postion] != null && values[postion].display) {
		lReturn = values[postion].name();
	    }
	    return lReturn;
	}
	// </editor-fold>

	public static LinkedHashMap<String, String> getReadyForProductionandAboveMap() {
	    return getStatusMap(READY_FOR_PRODUCTION_DEPLOYMENT);
	}

	public static LinkedHashMap<String, String> getSecuredStatusMap() {
	    return getStatusMap(SUBMITTED);
	}

	public static LinkedHashMap<String, String> getApprovedStatusMap() {
	    return getStatusMap(APPROVED);
	}

	public static LinkedHashMap<String, String> getAfterSubmitStatus() {
	    return getStatusMap(SUBMITTED, PENDING_FALLBACK);
	}

	public static LinkedHashMap<String, String> getApprovedAndAboveStatus() {
	    return getStatusMap(APPROVED, PENDING_FALLBACK);
	}

	public static LinkedHashMap<String, String> getBeforeApprovedStatus() {
	    return getStatusMap(ACTIVE, SUBMITTED);
	}

	public static LinkedHashMap<String, String> getQAFunctionalDeploymentStatus() {
	    return getStatusMap(APPROVED, DEPLOYED_IN_QA_FUNCTIONAL);
	}

	public static LinkedHashMap<String, String> getQARegressionDeploymentStatus() {
	    return getStatusMap(PASSED_FUNCTIONAL_TESTING, DEPLOYED_IN_QA_REGRESSION);
	}

	public static LinkedHashMap<String, String> getTSSDeploymentStatus() {
	    return getStatusMap(PASSED_REGRESSION_TESTING, DEPLOYED_IN_PRE_PRODUCTION);
	}

	public static LinkedHashMap<String, String> getNonProdStatusMap() {
	    return getStatusMap(ACTIVE, PENDING_FALLBACK);
	}

	public static LinkedHashMap<String, String> getPlanStatus() {
	    return getStatusMap(ACTIVE, READY_FOR_PRODUCTION_DEPLOYMENT);
	}

	public static LinkedHashMap<String, String> getSecuredBeforeReadyForProduction() {
	    return getStatusMap(APPROVED, DEV_MGR_APPROVED);
	}

	public static LinkedHashMap<String, String> getOnlineAndAbove() {
	    return getStatusMap(ONLINE, DELETED);
	}

	public static LinkedHashMap<String, String> getQARegressiontestingStatus() {
	    return getStatusMap(PASSED_REGRESSION_TESTING, DEPLOYED_IN_PRE_PRODUCTION);
	}

	public static LinkedHashMap<String, String> getDeploymentPlanStatus() {
	    return getStatusMap(DEV_MGR_APPROVED, READY_FOR_PRODUCTION_DEPLOYMENT);
	}

	// public static LinkedHashMap<String, String> getQARegressionTestingStatus() {
	// return getStatusMap(PASSED_FUNCTIONAL_TESTING, PARTIAL_FUNCTIONAL_TESTING);
	// }
	public static LinkedHashMap<String, String> getPassedRegressionAndAboveStatus() {
	    return getStatusMap(PASSED_REGRESSION_TESTING, PENDING_FALLBACK);
	}

	public static LinkedHashMap<String, String> getDeployedInPreProdToOnline() {
	    LinkedHashMap<String, String> returnList = getStatusMap(DEPLOYED_IN_QA_FUNCTIONAL, ONLINE_RELOAD);
	    return returnList;
	}

	public static LinkedHashMap<String, String> getTSSDeployPlanStatus() {
	    LinkedHashMap<String, String> lReturnList = new LinkedHashMap<String, String>();
	    // Plan status Tss Deploy Flag
	    lReturnList.put(APPROVED.name(), APPROVED.attributes);
	    lReturnList.put(PASSED_FUNCTIONAL_TESTING.name(), PASSED_FUNCTIONAL_TESTING.attributes);
	    return lReturnList;
	}

	public static LinkedHashMap<String, String> getPRStatus() {
	    LinkedHashMap<String, String> lReturnList = new LinkedHashMap<String, String>();
	    // Normal flow
	    lReturnList.put(ONLINE.name(), ONLINE.attributes);
	    lReturnList.put(FALLBACK.name(), FALLBACK.attributes);
	    lReturnList.put(APPROVED.name(), APPROVED.attributes);

	    // For handling Bypassed plans
	    lReturnList.put(BYPASSED_REGRESSION_TESTING.name(), BYPASSED_REGRESSION_TESTING.attributes);
	    lReturnList.put(BYPASSED_FUNCTIONAL_TESTING.name(), BYPASSED_FUNCTIONAL_TESTING.attributes);

	    // only for Macro Header
	    lReturnList.put(READY_FOR_PRODUCTION_DEPLOYMENT.name(), READY_FOR_PRODUCTION_DEPLOYMENT.attributes);
	    return lReturnList;
	}

	public static LinkedHashMap<String, String> getAllPlanStatus() {
	    return getStatusMap(ACTIVE, PENDING_FALLBACK);
	}

	public static LinkedHashMap<String, String> getPreSegmentOnlineStatus() {
	    LinkedHashMap<String, String> lReturnList = new LinkedHashMap<String, String>();
	    // Plan status
	    lReturnList.put(ACTIVE.name(), ACTIVE.attributes);
	    lReturnList.put(ACCEPTED_IN_PRODUCTION.name(), ACCEPTED_IN_PRODUCTION.attributes);
	    lReturnList.put(ONLINE.name(), ONLINE.attributes);
	    lReturnList.put(FALLBACK_DEPLOYED_IN_PRODUCTION.name(), FALLBACK_DEPLOYED_IN_PRODUCTION.attributes);
	    lReturnList.put(ONLINE_RELOAD.name(), ONLINE_RELOAD.attributes);
	    lReturnList.put(FALLBACK.name(), FALLBACK.attributes);
	    lReturnList.put(DELETED.name(), DELETED.attributes);
	    return lReturnList;
	}

	public static LinkedHashMap<String, String> getPreSegmentProdLoadActivateStatus() {
	    LinkedHashMap<String, String> lReturnList = new LinkedHashMap<String, String>();
	    // Plan status
	    lReturnList.put(ACTIVE.name(), ACTIVE.attributes);
	    lReturnList.put(ONLINE.name(), ONLINE.attributes);
	    lReturnList.put(READY_FOR_PRODUCTION_DEPLOYMENT.name(), READY_FOR_PRODUCTION_DEPLOYMENT.attributes);
	    lReturnList.put(PARTIALLY_DEPLOYED_IN_PRODUCTION.name(), PARTIALLY_DEPLOYED_IN_PRODUCTION.attributes);
	    lReturnList.put(DEPLOYED_IN_PRODUCTION.name(), DEPLOYED_IN_PRODUCTION.attributes);
	    lReturnList.put(ONLINE_RELOAD.name(), ONLINE_RELOAD.attributes);
	    lReturnList.put(FALLBACK.name(), FALLBACK.attributes);
	    lReturnList.put(DELETED.name(), DELETED.attributes);
	    return lReturnList;
	}

	public static String getAllPlanStatusMap() {
	    StringBuffer buffer = new StringBuffer();
	    for (Constants.PlanStatus lStatus : values()) {
		buffer.append("WHEN plan.planStatus = '");
		buffer.append(lStatus.name());
		buffer.append("' ");
		buffer.append(" THEN '");
		buffer.append(lStatus.attributes);
		buffer.append("' ");
	    }
	    buffer.append(" ELSE plan.planStatus END ");
	    return buffer.toString();
	}

	public static LinkedHashMap<String, String> getPreSegmentPreProdStatus() {
	    // Prod status
	    LinkedHashMap<String, String> lReturnList = Constants.PlanStatus.getPostSegmentPreProdStatus();
	    // Pre prod Plan status
	    lReturnList.put(PASSED_REGRESSION_TESTING.name(), PASSED_REGRESSION_TESTING.attributes);
	    lReturnList.put(BYPASSED_REGRESSION_TESTING.name(), BYPASSED_REGRESSION_TESTING.attributes);
	    lReturnList.put(PARTIAL_REGRESSION_TESTING.name(), PARTIAL_REGRESSION_TESTING.attributes);
	    return lReturnList;
	}

	public static LinkedHashMap<String, String> getPostSegmentPreProdStatus() {
	    // Prod status
	    LinkedHashMap<String, String> lReturnList = Constants.PlanStatus.getPreSegmentOnlineStatus();
	    // Pre prod Plan status
	    lReturnList.put(DEPLOYED_IN_PRE_PRODUCTION.name(), DEPLOYED_IN_PRE_PRODUCTION.attributes);
	    lReturnList.put(PASSED_ACCEPTANCE_TESTING.name(), PASSED_ACCEPTANCE_TESTING.attributes);
	    lReturnList.put(DEV_MGR_APPROVED.name(), DEV_MGR_APPROVED.attributes);
	    lReturnList.put(READY_FOR_PRODUCTION_DEPLOYMENT.name(), READY_FOR_PRODUCTION_DEPLOYMENT.attributes);
	    return lReturnList;
	}

	public static LinkedHashMap<String, String> getTSSPreSegmentPreProdStatus() {
	    LinkedHashMap<String, String> lReturnList = new LinkedHashMap<String, String>();
	    // Pre prod Plan status
	    lReturnList.put(PASSED_REGRESSION_TESTING.name(), PASSED_REGRESSION_TESTING.attributes);
	    lReturnList.put(BYPASSED_REGRESSION_TESTING.name(), BYPASSED_REGRESSION_TESTING.attributes);
	    lReturnList.put(PARTIAL_REGRESSION_TESTING.name(), PARTIAL_REGRESSION_TESTING.attributes);
	    // Prod status
	    lReturnList.put(DEPLOYED_IN_PRE_PRODUCTION.name(), DEPLOYED_IN_PRE_PRODUCTION.attributes);
	    lReturnList.put(PASSED_ACCEPTANCE_TESTING.name(), PASSED_ACCEPTANCE_TESTING.attributes);
	    return lReturnList;
	}

	public static LinkedHashMap<String, String> getSubmittedToPreProd() {
	    return getStatusMap(SUBMITTED, DEPLOYED_IN_PRE_PRODUCTION);
	}

	public static LinkedHashMap<String, String> getPRODDeploymentStatus() {
	    return getStatusMap(READY_FOR_PRODUCTION_DEPLOYMENT, PENDING_FALLBACK);
	}

	public static LinkedHashMap<String, String> getPRODFallbackDeploymentStatus() {
	    return getStatusMap(ONLINE, FALLBACK_DEPLOYED_IN_PRODUCTION);
	}

	public static LinkedHashMap<String, String> getApprovedToPreProd() {
	    return getStatusMap(APPROVED, DEPLOYED_IN_PRE_PRODUCTION);
	}

	public static LinkedHashMap<String, String> getAllPlanStatusTillPreProd() {
	    return getStatusMap(ACTIVE, DEPLOYED_IN_PRE_PRODUCTION);
	}

	public static LinkedHashMap<String, String> getApprovedToOnline() {
	    return getStatusMap(APPROVED, ONLINE);
	}

	public static LinkedHashMap<Integer, String> getStatusForGenericAPI() {
	    LinkedHashMap<Integer, String> lReturnList = new LinkedHashMap<>();
	    for (PlanStatus lStatus : values()) {
		if (lStatus.display) {
		    lReturnList.put(lStatus.position, lStatus.name());
		}
	    }
	    return lReturnList;
	}

	public static LinkedHashMap<String, String> getPRODDeploymentPlanStatus() {
	    LinkedHashMap<String, String> lReturnList = new LinkedHashMap<String, String>();
	    lReturnList.put(READY_FOR_PRODUCTION_DEPLOYMENT.name(), READY_FOR_PRODUCTION_DEPLOYMENT.attributes);
	    lReturnList.put(PARTIALLY_DEPLOYED_IN_PRODUCTION.name(), PARTIALLY_DEPLOYED_IN_PRODUCTION.attributes);
	    lReturnList.put(DEPLOYED_IN_PRODUCTION.name(), DEPLOYED_IN_PRODUCTION.attributes);

	    return lReturnList;
	}

	public static Map<String, String> getPlanStatusBeforeProdLoad() {
	    return getStatusMap(ACTIVE, DEV_MGR_APPROVED);
	}

	public static Map<String, String> getPlanStatusAfterProdLoad() {
	    return getStatusMap(READY_FOR_PRODUCTION_DEPLOYMENT, ONLINE);
	}

	public static Map<String, String> getPlanStatusAfterOnline() {
	    return getStatusMap(FALLBACK_DEPLOYED_IN_PRODUCTION, FALLBACK);
	}

	public static LinkedHashMap<String, String> getQAPlanStatusBetweenApprovedAndRegDepl() {
	    return getStatusMap(APPROVED, DEPLOYED_IN_QA_REGRESSION);
	}

	public static Map<String, String> getPlanStatusActiveToOnline() {
	    return getStatusMap(ACTIVE, ONLINE);
	}

    }

    public enum SystemScripts {

	CHECKOUT("${MTP_ENV}/mtpgitcmdchkout "),
	// CREATE_MAK_FILE("${MTP_ENV}/mtpgitcreateconfig "),
	CREATE_SOURCE_ARTIFACT("${MTP_ENV}/mtpgitcmdadd "),
	CREATE_WORKSPACE("${MTP_ENV}/mtpgitcreateworkspace "),
	DELETE_FILE("${MTP_ENV}/mtpgitcmddel "),
	DELETE_NEW_SOURCE_ARTIFACT("${MTP_ENV}/mtpgitdelprodfile "),
	// SEARCH_MAK_FILE("${MTP_ENV}/mtpgitcmdchkmak "),
	SEARCH_SO("${MTP_ENV}/mtpgitcmdmak "),
	LOGIN("$MTP_ENV/mtplogin "),
	SSH_PERMISSION("chmod 755 ${HOME} && chmod g-w ~/.ssh/*"),
	GIT_CONFIG_USER("git config --global --replace-all user.name "),
	GIT_CONFIG_MAIL("git config --global --replace-all user.email "),
	DBCR_VALIDATE("${MTP_ENV}/mtpwfdbcrchk "),
	FTP("${MTP_ENV}/mtptpfftploader "),
	COMMIT("${MTP_ENV}/mtpgitcmdsycommit "),
	GET_LATEST("${MTP_ENV}/mtpgitcmdgetnew "),
	CHECK_IN("${MTP_ENV}/mtpgitcmdcheckin "),
	CREATE_DEVL_WORKSPACE("${MTP_ENV}/mtpgitcreatedvlspace "),
	EXPORT("${MTP_ENV}/mtpgitcmdexport "),
	// DSL_FILE_UPDATE("${MTP_ENV}/mtptpfdslupdate"),
	// DSL_FILE_DELETE("${MTP_ENV}/mtptpfdsldelete"),
	DELETE_STAGING_WORKSPACE("${MTP_ENV}/mtpgitdeletestgspace "),
	PRODUCTION_MERGE("${MTP_ENV}/mtpgitcmdproduction "),
	// SSH_KEY_MAP("${MTP_ENV}/mtpsshkey"),
	// TPF_MAINTENANCE_CHECK("${MTP_ENV}/mtptpfnfmcheck"),
	// TPF_TEST_SYSTEM_MAINTENANCE("${MTP_ENV}/mtptpfnfm"),
	// TPF_TEST_SYSTEM_MAINTENANCE_OLDR_CHECK("${MTP_ENV}/mtptpfnfmcheckoldr"),
	TPF_TEST_SYSTEM_MAINTENANCE_OLDR_CREATION("${MTP_ENV}/mtptpfupdatenfmdsl"),
	COMPILER_CONTROL_VALIDATION("${MTP_ENV}/mtptpfcntchk"),
	DATE_AUDIT_CROSS_CHECK("${MTP_ENV}/mtptpfaudit"),
	DELETE_PLAN("${MTP_ENV}/mtpgitdeleteimpl "),
	LEGACY_FALL_BACK_PALN("${MTP_ENV}/mtpgitautoupdate"),
	MIGRATE_NON_IBM("${MTP_ENV}/mtpgitcmdmove "),
	IMPORT_WORKSPACE("${MTP_ENV}/mtpgitimportworkspace "),
	CREATE_REPOSITORY("${MTP_ENV}/mtpgitcreateprodrepo "),
	CREATE_BRANCH("${MTP_ENV}/mtpgitcreatebranch "),
	SABRE_LOCK("${MTP_ENV}/mtpzsbtrmlock "),
	MAK_FILE_VALIDATION("${MTP_ENV}/mtptpfctlcheck "),
	MACRO_HEADER_DEPENDENCY("${MTP_ENV}/mtptpfdependency "),
	CHECK_ALL_MAK_FILE("${MTP_ENV}/mtpgitcmdchkallmak "),
	GIT_RESTORE("${MTP_ENV}/mtpgitprojrestore "),
	GIT_REVISION("${MTP_ENV}/mtpgitcmdrevision "),
	CHECK_LOCAL_WORKSPACE_FILE("${MTP_ENV}/mtptpffilecheck "),
	GIT_GENERIC_API("${MTP_ENV}/mtpgitapigetfile "),
	COMMON_CODE_VALIDATION("${MTP_ENV}/mtptpfcommoncode "),
	GET_SO_NAME_BY_FILE_NAME("${MTP_ENV}/mtpgitsoname "),
	MOVE_DERIVED_SEGMENTS_ON_SCHEDULAR("${MTP_ENV}/mtpgitpushderivedlfs ");
	private final String script;

	private SystemScripts(String pScript) {
	    this.script = pScript;
	}

	public String getScript() {
	    return script;
	}

    }

    public enum GitScripts {

	CREATE_TICKET("${MTP_ENV}/mtpgitcreateticket "),
	IBM_POPULATE("${MTP_ENV}/mtpgitpopulate "),
	TAG_CMD("${MTP_ENV}/mtpwfrepotag "),
	REMOVE_TAG_CMD("${MTP_ENV}/mtpwfrepodeltag ");

	private final String script;

	private GitScripts(String pScript) {
	    this.script = pScript;
	}

	public String getScript() {
	    return script;
	}
    }

    public enum DEVL {

	BLD_DVL_,
	LDR_DVL_,
	FTP_DVL_;
    }

    public enum BUILD {
	BLD_ALL_,
	LDR_ALL_,
	CWS_ALL_;
    }

    public enum TYPE {
	DVL,
	STG;
    }

    public enum STAGING {

	CWS_STG_,
	BLD_STG_,
	LDR_STG_,
	FTP_STG_;
    }

    public enum ONLINE {
	FEEDBCK_;
    }

    public enum BUILD_TYPE {

	STG_CWS("Staging", "/developerLead/planSubmit"),
	STG_BUILD("Staging", "/developerLead/planSubmit"),
	STG_LOAD("Staging", "/developerLead/planSubmit"),
	DVL_BUILD("Devl", "/access/buildPlan"),
	DVL_LOAD("Devl", "/access/createLoaderFile"),
	ONL_BUILD("Online", "/tsd/setOnline"),
	FAL_BUILD("Fallback", "/tsd/acceptFallback");

	private final String buildTypes;
	private final String actionUrl;

	private BUILD_TYPE(String pBuildTypes, String pActionUrl) {
	    this.buildTypes = pBuildTypes;
	    this.actionUrl = pActionUrl;
	}

	public String getBuildTypes() {
	    return buildTypes;
	}

	public String getActionUrl() {
	    return actionUrl;
	}

	public static LinkedHashMap<String, String> getStagingBuildType() {
	    LinkedHashMap<String, String> lBuildTypeList = new LinkedHashMap<String, String>();
	    for (BUILD_TYPE lBuildType : BUILD_TYPE.values()) {
		if (lBuildType.getBuildTypes().equals("Staging")) {
		    lBuildTypeList.put(lBuildType.name(), lBuildType.getBuildTypes());
		}
	    }
	    return lBuildTypeList;
	}

	public static String getBuildTypeActionUrl(String pBuildType) {
	    BUILD_TYPE buildType = Constants.BUILD_TYPE.valueOf(pBuildType);
	    String parentActionUrl = "";
	    if (buildType != null) {
		parentActionUrl = buildType.getActionUrl();
	    }
	    return parentActionUrl;
	}
    }

    public enum LoaderTypes {

	A("Aux Type"),
	E("E-Type");

	private final String loaderTypes;

	private LoaderTypes(String pLoaderTypes) {
	    this.loaderTypes = pLoaderTypes;
	}

	public String getLoaderTypes() {
	    return loaderTypes;
	}

	public static LinkedHashMap<String, String> getLoaderTypesList() {
	    LinkedHashMap<String, String> lLoaderList = new LinkedHashMap<String, String>();
	    for (LoaderTypes lLoaderType : LoaderTypes.values()) {
		lLoaderList.put(lLoaderType.name(), lLoaderType.getLoaderTypes());
	    }
	    return lLoaderList;
	}

    }

    public enum REJECT_REASON {

	REJECTION("rejection of"),
	FALLBACK("fallback of"),
	ONLINE("online exception load of"),
	LOAD_DATE("load window passed more than two days "),
	LOAD_DATE_CHANGE("load date changed for exception load "),
	QA_FAIL("testing failed");
	private final String value;

	private REJECT_REASON(String pValue) {
	    this.value = pValue;
	}

	public String getValue() {
	    return value;
	}
    }

    public enum AUTOREJECT_COMMENT {

	ONLINE("Auto-rejected due to online exception load of Implementation Plan - "),
	FALLBACK("Auto-rejected due to fallback of Implementation Plan - "),
	REJECT("Auto-rejected due to rejection of Implementation Plan - "),
	LOAD_DATE_CHANGE("Auto-rejected due to load date change for exception load - "),
	LOAD_DATE("Auto-rejected due to load window passed more than two days for Implementation Plan - ");

	private final String value;

	private AUTOREJECT_COMMENT(String pValue) {
	    this.value = pValue;
	}

	public String getValue() {
	    return value;
	}
    }

    public enum LOAD_SET_STATUS {

	LOADED("LOADED", "PRE_PROD_LOADED"),
	ACTIVATED("ACTIVATED", "PRE_PROD_ACTIVATED"),
	DEACTIVATED("DEACTIVATED", "PRE_PROD_DEACTIVATED"),
	DELETED("DELETED", "PRE_PROD_DELETED"),
	ACCEPTED("ACCEPTED", "PRE_PROD_ACCEPTED"),
	FALLBACK_LOADED("FALLBACK_LOADED", "PRE_PROD_FALLBACK_LOADED"),
	FALLBACK_ACTIVATED("FALLBACK_ACTIVATED", "PRE_PROD_FALLBACK_ACTIVATED"),
	FALLBACK_DEACTIVATED("FALLBACK_DEACTIVATED", "PRE_PROD_FALLBACK_DEACTIVATED"),
	FALLBACK_DELETED("ACCEPTED", "PRE_PROD_FALLBACK_DELETED"),
	FALLBACK_ACCEPTED("FALLBACK_ACCEPTED", "PRE_PROD_FALLBACK_ACCEPTED");

	private final String loadsetStatus;
	private final String preProdStatus;

	private LOAD_SET_STATUS(String pLoadsetStatus, String pPreProdStatus) {
	    this.loadsetStatus = pLoadsetStatus;
	    this.preProdStatus = pPreProdStatus;
	}

	public String getLoadsetStatus() {
	    return loadsetStatus;
	}

	public String getPreProdLoadsetStatus() {
	    return preProdStatus;
	}

	public static LinkedHashMap<String, String> getPreProdLoadsetStatusList() {
	    LinkedHashMap<String, String> lLoadsetStatusList = new LinkedHashMap<String, String>();
	    for (LOAD_SET_STATUS lLoadsetStatus : LOAD_SET_STATUS.values()) {
		lLoadsetStatusList.put(lLoadsetStatus.name(), lLoadsetStatus.getPreProdLoadsetStatus());
	    }
	    return lLoadsetStatusList;
	}

	public static LinkedHashMap<String, String> getLoadsetStatusList() {
	    LinkedHashMap<String, String> lLoadsetStatusList = new LinkedHashMap<String, String>();
	    for (LOAD_SET_STATUS lLoadsetStatus : LOAD_SET_STATUS.values()) {
		lLoadsetStatusList.put(lLoadsetStatus.name(), lLoadsetStatus.getLoadsetStatus());
	    }
	    return lLoadsetStatusList;
	}

	public static List<String> getDeleteScenarios() {
	    List<String> lList = new ArrayList<String>();
	    lList.add(DEACTIVATED.name());
	    lList.add(DELETED.name());
	    return lList;
	}

	public static List<String> getLoadScenarios() {
	    List<String> lList = new ArrayList<String>();
	    lList.add(LOADED.name());
	    lList.add(ACTIVATED.name());
	    return lList;
	}

	public static List<String> getAcceptScenarios() {
	    List<String> lList = new ArrayList<String>();
	    lList.add(ACCEPTED.name());
	    lList.add(ACTIVATED.name());
	    return lList;
	}

	public static List<String> getAcceptAndFallbackList() {
	    List<String> lList = new ArrayList<String>();
	    lList.add(ACCEPTED.name());
	    lList.add(FALLBACK_LOADED.name());
	    lList.add(FALLBACK_ACTIVATED.name());
	    lList.add(FALLBACK_DEACTIVATED.name());
	    lList.add(FALLBACK_DELETED.name());
	    lList.add(FALLBACK_ACCEPTED.name());
	    return lList;
	}

	public static List<String> getFallbackFinalList() {
	    List<String> lList = new ArrayList<String>();
	    lList.add(FALLBACK_ACCEPTED.name());
	    return lList;
	}

	public static List<String> getLoadDeactDeleteScenarios() {
	    List<String> lList = new ArrayList<String>();
	    lList.add(LOADED.name());
	    lList.add(DEACTIVATED.name());
	    lList.add(DELETED.name());
	    return lList;
	}

	public static List<String> getDeleteAndAbove() {
	    List<String> lList = new ArrayList<String>();
	    lList.add(DELETED.name());
	    lList.add(FALLBACK_LOADED.name());
	    lList.add(FALLBACK_ACTIVATED.name());
	    lList.add(FALLBACK_DEACTIVATED.name());
	    lList.add(FALLBACK_DELETED.name());
	    lList.add(FALLBACK_ACCEPTED.name());
	    return lList;
	}

	public static List<String> getDeactivateAndAbove() {
	    List<String> lList = new ArrayList<String>();
	    lList.addAll(getDeleteScenarios());
	    lList.add(LOADED.name());
	    lList.add(FALLBACK_LOADED.name());
	    lList.add(FALLBACK_ACTIVATED.name());
	    lList.add(FALLBACK_DEACTIVATED.name());
	    lList.add(FALLBACK_DELETED.name());
	    lList.add(FALLBACK_ACCEPTED.name());
	    return lList;
	}

	public static List<String> getActivateAndAbove() {
	    List<String> lList = new ArrayList<String>();
	    lList.add(ACTIVATED.name());
	    lList.add(ACCEPTED.name());
	    return lList;
	}

	public static List<String> getFallbackActivateAndAbove() {
	    List<String> lList = new ArrayList<String>();
	    lList.add(FALLBACK_ACTIVATED.name());
	    lList.add(FALLBACK_ACCEPTED.name());
	    return lList;
	}

	public static List<String> getLoadAndAbove() {
	    List<String> lList = new ArrayList<String>();
	    lList.addAll(getActivateAndAbove());
	    lList.add(DEACTIVATED.name());
	    lList.add(LOADED.name());
	    return lList;
	}

	public static List<String> getLoadsetStatusForRCatPlan() {
	    List<String> lList = new ArrayList<String>();
	    lList.add(ACCEPTED.name());
	    lList.add(FALLBACK_LOADED.name());
	    lList.add(FALLBACK_ACTIVATED.name());
	    lList.add(FALLBACK_DEACTIVATED.name());
	    lList.add(FALLBACK_DELETED.name());
	    return lList;
	}

    }

    public enum DBCR_STATUS {

	COMPLETE,
	INCOMPLETE;
    }

    public enum DBCR_ENVIRONMENT {

	TEST("QA System"),
	COPY("Pre prod System"),
	PROD("Prod System");

	private final String value;

	private DBCR_ENVIRONMENT(String pValue) {
	    this.value = pValue;
	}

	public String getValue() {
	    return value;
	}
    }

    public enum LOAD_FILE_EXTENSION {

	OLDR_EXTN(".oldr"),
	TLDR_EXTN(".tldr");
	private final String value;

	private LOAD_FILE_EXTENSION(String pValue) {
	    this.value = pValue;
	}

	public String getValue() {
	    return value;
	}
    }

    public enum Channels {

	AUTO_REJECT,
	CREATE_WORKSPACE,
	DATE_AUDIT,
	DELEGATION,
	DEVEL_BUILD,
	DEVEL_BUILD_STATUS,
	DEVEL_LOAD,
	E_AUX_DEPLOY_STATUS,
	ONLINE_PROCESS,
	SH_PKG_MOVEMENT,
	PLAN_AUX_FALLBACK,
	PLAN_AUX_ONLINE,
	PLAN_SUBMIT,
	PLAN_UPDATE,
	PRE_PROD_LOAD,
	PROD_FALLBACK,
	PROD_FTP_IP,
	PROD_LOAD,
	DEFAULT,
	SH_BUILD_STATUS,
	STAGE_BUILD,
	STAGE_LOAD,
	SESSION_TIMEOUT,
	ONLINE_BUILD,
	FALLBACK_BUILD,
	GI_PORT_CHANGE,
	MOVE_SOURCE_ARTIFACT;
    }

    public enum PROD_SCRIPT_PARAMS {

	ONLINE_ACCEPT,
	ONLINE_REVERT,
	FALLBACK_ACCEPT;
    }

    public enum LoadSetCommands {

	LOAD("`05AUTO ACT AUTOLOAD "),
	ACTIVATE("`05AUTO ACT LOADACT0 "),
	DEACTIVATE("`05AUTO ACT LOADDEA "),
	DELETE("`05AUTO ACT LOADDEL "),
	ACCEPT("`05AUTO ACT LOADACC "),
	GETIP("`05AUTO ACT LOADFIP");
	// REFRESH_("`05AUTO ACT LOADDIS "),
	// REFRESH_ALL("`05AUTO ACT LOADDIS ALL");

	String lScript;

	private LoadSetCommands(String pScript) {
	    lScript = pScript;
	}

	public String getScript() {
	    return lScript;
	}
    }

    public enum FALLBACK_STATUS {

	DELETE_ALL_LOADSET,
	ACCEPT_FALLBACK_LOADSET;
    }

    // public enum SSOHeaders {
    //
    // AUTHORIZATION("authorization"), //Basic Token
    // DELLTAEMPLOYEE("delltaemployee"),
    // GECOS("gecos"), // full name
    // HOMEDIRECTORY("homedirectory"),
    // HOST("host"), // DEV/QA
    // LISTOFROLES("listofroles"),// list of roles
    // MAIL("mail"),
    // SM_AUTHDIRNAME("sm_authdirname"),//domain
    // SM_AUTHDIROID("sm_authdiroid"),
    // SM_REALM("sm_realm"),
    // SM_REALMOID("sm_realmoid"),
    // SM_SERVERSESSIONID("sm_serversessionid"),
    // SM_SERVERSESSIONSPEC("sm_serversessionspec"),
    // SM_TIMETOEXPIRE("sm_timetoexpire"),
    // SM_TRANSACTIONID("sm_transactionid"),
    // SM_UNIVERSALID("sm_universalid"),//list of uids
    // SM_USER("sm_user"), //uid
    // SM_USERDN("sm_userdn"), //cn
    // USER_AGENT("user-agent"),
    // X_FORWARDED_FOR("x-forwarded-for");
    //
    // String key;
    //
    // private SSOHeaders(String pKey) {
    // key = pKey;
    // }
    //
    // public String getKey() {
    // return key;
    // }
    //
    // }
    public enum PROD_LOAD_STATUS {

	LOADED("LOADED"),
	ACTIVATED_ON_SINGLE_CPU("ACTIVATED_ON_SINGLE_CPU"),
	ACTIVATED_ON_MULTIPLE_CPU("ACTIVATED_ON_MULTIPLE_CPU"),
	ACTIVATED_ON_ALL_CPU("ACTIVATED_ON_ALL_CPU"),
	// DEACTIVATED_ACTIVATED_ON_FEW_CPU,
	DEACTIVATED_ON_ALL_CPU("DEACTIVATED_ON_ALL_CPU"),
	DELETED("DELETED"),
	ACCEPTED("ACCEPTED"),
	FALLBACK_LOADED("FALLBACK_LOADED"),
	FALLBACK_ACTIVATED("FALLBACK_ACTIVATED"),
	FALLBACK_DEACTIVATED("FALLBACK_DEACTIVATED"),
	FALLBACK_DELETED("ACCEPTED"),
	FALLBACK_ACCEPTED("FALLBACK_ACCEPTED"),
	// ZTPFM=1551 Created for ui status
	PRE_PROD_LOADED("PRE_PROD_LOADED"),
	PRE_PROD_ACTIVATED("PRE_PROD_ACTIVATED"),
	PRE_PROD_DEACTIVATED("PRE_PROD_DEACTIVATED"),
	PRE_PROD_DELETED("PRE_PROD_DELETED"),
	PRE_PROD_ACCEPTED("PRE_PROD_ACCEPTED"),
	PRE_PROD_FALLBACK_LOADED("PRE_PROD_FALLBACK_LOADED"),
	PRE_PROD_FALLBACK_ACTIVATED("PRE_PROD_FALLBACK_ACTIVATED"),
	PRE_PROD_FALLBACK_DEACTIVATED("PRE_PROD_FALLBACK_DEACTIVATED"),
	PRE_PROD_FALLBACK_DELETED("PRE_PROD_FALLBACK_DELETED"),
	PRE_PROD_FALLBACK_ACCEPTED("PRE_PROD_FALLBACK_ACCEPTED");

	private final String prodLoadStatus;

	private PROD_LOAD_STATUS(String pProdLoadStatus) {
	    this.prodLoadStatus = pProdLoadStatus;
	}

	public String getProdLoadStatus() {
	    return prodLoadStatus;
	}

	public static LinkedHashMap<String, String> getProdLoadStatusList() {
	    LinkedHashMap<String, String> lProdLoadStatusList = new LinkedHashMap<String, String>();
	    for (PROD_LOAD_STATUS lProdLoadStatus : PROD_LOAD_STATUS.values()) {
		lProdLoadStatusList.put(lProdLoadStatus.name(), lProdLoadStatus.getProdLoadStatus());
	    }
	    return lProdLoadStatusList;
	}

	public static Map<String, String> getAcceptScenarios() {
	    Map<String, String> lList = new HashMap<>();
	    lList.put(ACCEPTED.name(), ACCEPTED.name());
	    lList.put(ACTIVATED_ON_SINGLE_CPU.name(), ACTIVATED_ON_SINGLE_CPU.name());
	    lList.put(ACTIVATED_ON_MULTIPLE_CPU.name(), ACTIVATED_ON_MULTIPLE_CPU.name());
	    lList.put(ACTIVATED_ON_ALL_CPU.name(), ACTIVATED_ON_ALL_CPU.name());
	    return lList;
	}

    }

    /**
     * Application Constants
     */
    public static final String APP_DB_SCRIPTPATH = "com/tsi/workflow/database/scripts.sql";
    public static final String APP_DB_DEL_DL = "com/tsi/workflow/database/prod_delete_delta.sql";
    public static final String APP_DB_DEL_TP = "com/tsi/workflow/database/prod_delete_travelport.sql";
    public static final String APP_ENCRYPT_ALGORITHM = "PBEWITHMD5ANDDES";
    public static final String PARM_SECRET = "password";
    public static final String PARM_USERNAME = "userName";
    public static final String PARM_AUTHORIZATION = "Authorization";
    public static final String SECRET_HASH = "TsiWorkflow";
    public static final String PR_FILE_FORMAT = "%0$-12s %-9s %s%n";

    // public static final String VIEW = "VIEW";
    // public static final String NAMED = "NAMED";
    // public static final String PUSH = "PUSH";
    // public static final String AUTHENTICATED = "AUTHENTICATED";
    public static final String TICKETS = "tickets";
    public static final String BLOB = "blob";

    /**
     * Date Format
     */
    public static final ThreadLocal<DateFormat> HOOK_DATE_FORMAT = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	}
    };
    public static final String LOAD_FREEZE_DATE_TIME_FORMAT = "yyyy-MM-dd";
    public static final String APP_DATE_TIME_FORMAT_STRING = "MM-dd-yyyy HH:mm:ss Z";

    public static final ThreadLocal<DateFormat> APP_NON_DATE_FORMAT = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat("MM-dd-yyyy HH:mm");
	}
    };
    public static final ThreadLocal<DateFormat> APP_DATE_TIME_FORMAT = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat(APP_DATE_TIME_FORMAT_STRING);
	}
    };
    public static final String APP_READABLE_DATE_TIME_FORMAT_STRING = "MM-dd-yyyy HH:mm:ss z";
    public static final ThreadLocal<DateFormat> APP_READABLE_DATE_TIME_FORMAT = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat(APP_READABLE_DATE_TIME_FORMAT_STRING);
	}
    };

    public static final String APP_READABLE_DATE_FORMAT_STRING = "MM-dd-yyyy";
    public static final ThreadLocal<DateFormat> APP_READABLE_DATE_FORMAT = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat(APP_READABLE_DATE_FORMAT_STRING);
	}
    };

    public static final ThreadLocal<DateFormat> SHELL_DATEFORMAT = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat("MM-dd-yyyy");
	}
    };
    public static final ThreadLocal<DateFormat> JENKINS_DATEFORMAT = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat("yyyyMMddHHmmss");
	}
    };
    public static final ThreadLocal<DateFormat> REX_DATEFORMAT = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat("yyyyMMdd HHmm");
	}
    };
    public static final ThreadLocal<DateFormat> TOS_DATEFORMAT = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat("MM/dd/yy");
	}
    };
    public static final ThreadLocal<DateFormat> JGIT_COMMENT_DATEFORMAT_1 = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat("yyyyMMddHHmm");
	}
    };
    public static final ThreadLocal<DateFormat> JGIT_COMMENT_DATEFORMAT_2 = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat("yyyyMMdd");
	}
    };
    public static final ThreadLocal<DateFormat> JGIT_COMMENT_DATEFORMAT_3 = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat("yyyyMMddHHmmss");
	}
    };
    public static final ThreadLocal<DateFormat> JGIT_COMMENT_DATEFORMAT_4 = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat("yyyyMMddHHmm");
	}
    };
    public static final ThreadLocal<DateFormat> JGIT_COMMENT_DATEFORMAT_5 = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat("yyyyMMdd");
	}
    };
    public static final ThreadLocal<DateFormat> JGIT_COMMENT_DATEFORMAT_6 = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat("yyyyMMddHHmmss");
	}
    };

    /**
     * One Time on Creation
     */
    // public static final String BPM_ADL_EMAIL_ID = "adlEmailId";
    // public static final String BPM_ADL_NAME = "adlName";
    // public static final String BPM_DEVELOPER_EMAIL_ID = "devEmailId";
    // public static final String BPM_DEVELOPER_NAME = "developerName";
    // public static final String BPM_IMPLEMENTATION_DESC =
    // "implementationDescription";
    /**
     * Multiple Time
     */
    public static final String BPM_IMPLEMENTATION_ID = "Implementation Id";
    public static final String BPM_IMPLEMENTATION_PLAN_ID = "Implementation Plan Id";

    /**
     * Company Name
     */
    public static final String TRAVELPORT = "tp";
    public static final String DELTA = "dl";

    /**
     *
     * One Time on Assignment
     */
    // public static final String BPM_DEV_MANAGER_ID = "devManagerId";
    public static final String BPM_PROPERTY_CANDIDATE = "candidate";
    /**
     * GIT Permissions
     */
    public static final String GIT_PERMISSION_READ = "R";
    public static final String GIT_PERMISSION_READWRITE = "RWC";
    /**
     * BPM Params
     */
    public static final String BPM_ACTION = "action";
    public static final String BPM_ASSIGNEE = "assignee";
    /**
     * Actions
     */
    public static final String BPM_CLAIM = "claim";
    public static final String BPM_COMPLETE = "complete";
    /**
     * Search Params
     */
    public static final String FILE_SEARCH_NONPRODFLAG = "NONPROD";
    public static final String FILE_SEARCH_IBMVANILLA = "IBMVANILLA";
    public static final String FILE_SEARCH_PRODFLAG = "PROD";
    public static final String FILE_SEARCH_PENDINGFLAG = "PENDING";
    public static final String FILE_MIG_NONIBM = "MIGNONIBM";
    public static final String FILE_MIG_OBS = "MIGOBS";
    public static final String BRANCH_MASTER = "master_";
    public static final String FILE_SEARCH_COMMONFLAG = "COMMON";
    /**
     * GITBlit Parms
     */
    // public static final String REPO_ACCESS_CONTROL = "PUSH";
    // public static final String REPO_AUTH_CONTROL = "NAMED";
    // public static final String REPO_REGISTRANT_TYPE = "USER";
    // public static final String REPO_PERMISSION_TYPE = "EXPLICIT";
    public static final String COEXIST_NAME = " CE";
    public static final String MTP_SERVICE_ROLE = "SERVICE";
    public static final String LOAD_CATEGORY_P = "P";
    public static boolean isAllowLoadAnyTime = Boolean.FALSE;
    public static Integer fallbackLoadDateGap = -4;
    public static final long TPF_TEST_SYSTEM_MAINT_IDLE_TIME = 20 * 60 * 1000;
    public static final String STAGE_NON_IBM_BASE_PATH = "/ztpfrepos/stg/";
    public static final String STAGE_IBM_BASE_PATH = "/ztpfsys/stg/";
    public static final String DVL_NON_IBM_BASE_PATH = "/ztpfrepos/dvl/";
    public static final String DVL_IBM_BASE_PATH = "/ztpfsys/dvl/";

    public static final String SABRE_LOCK = "rm -f /tmp/sabre.lock_";

    private static final Map<String, List<String>> lFileExtTypes = new HashMap<String, List<String>>();
    public static final ArrayList<String> lAllFileExtTypes = new ArrayList();
    public static final ArrayList<String> lIBMTemplate = new ArrayList();
    public static final ArrayList<String> lNONIBMTemplate = new ArrayList();
    public static final ArrayList<String> lBothTemplate = new ArrayList();
    public static final ArrayList<String> lUploadFileTypes = new ArrayList();
    public static final ArrayList<String> lLoadCategoryforCPUs = new ArrayList();
    public static final Locale LOCALE = Locale.getDefault();
    public static final ArrayList<String> macroHeaderFileList = new ArrayList();
    public static final ArrayList<String> lTOSReturnSubStatusList = new ArrayList();
    public static final HashMap<String, String> GITarget = new HashMap<>();
    public static final HashMap<String, String> GIPlatform = new HashMap<>();
    public static final HashMap<String, String> GIProcessingType = new HashMap<>();
    /**
     * TOS Constants
     */
    public static final String TOS_QUEUE_APP_TOS = "IN-QUEUE";
    public static final String TOS_QUEUE_IP_APP_TOS = "IN-IP-QUEUE";
    public static final String TOS_TOPIC_TOS_APP = "OUT-QUEUE";
    public static final String TOS_TOPIC_IP_TOS_APP = "OUT-IP-QUEUE";
    public static final String TOS_ADV_TOPIC_TOS_APP = "ActiveMQ.Advisory.Consumer.Queue.OUT-QUEUE";
    public static Boolean TOS_SECOND_SERVER_UP = Boolean.TRUE;
    public static Boolean MTPSERVICE_KEY_MAP = Boolean.FALSE;

    public static final String TOS_QUEUE_EXIT = "EXIT_QUEUE";
    public static Pattern IN_MESSAGE_PATTERN;
    public static Pattern FILE_PATH_PATTERN;
    public static Pattern STRICT_PATH_PATTERN;
    public static Pattern OUT_MESSAGE_PATTERN_RETRY;
    public static Pattern OUT_MESSAGE_PATTERN_NON_CE;
    public static Pattern OUT_MESSAGE_PATTERN_CE;
    public static Pattern JENKINS_ERROR_LOG_PATTERN;

    /**
     * GIT Comments LOG Pattern
     */
    public static Pattern LOG_MESSAGE_FORMAT1;
    public static Pattern LOG_MESSAGE_FORMAT2;
    public static Pattern LOG_MESSAGE_FORMAT3;
    // To-Do Remove Below Log Message format(4,5,6) when Arul migrate the commit
    // message with SourceRef from SourcRef
    public static Pattern LOG_MESSAGE_FORMAT4;
    public static Pattern LOG_MESSAGE_FORMAT5;
    public static Pattern LOG_MESSAGE_FORMAT6;
    public static Integer fallBackMacroHeaderDateGap = -2;

    /**
     *
     */
    public static final List<String> getDSLLoadsetSystem() {
	List<String> loadsetSys = new ArrayList<>();
	loadsetSys.add("WSP");
	return loadsetSys;
    }

    /**
     * Static Block
     */
    static {
	BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
	lFileExtTypes.put("src", Arrays.asList("asm", "bak", "c", "cbl", "cms", "cpp", "sbt", "java", "for", "htm", "oco", "java", "mak"));
	lFileExtTypes.put("macro", Arrays.asList("cpy", "mac"));
	lFileExtTypes.put("include", Arrays.asList("h", "hpp", "inc", "dfdl"));
	lFileExtTypes.put("tpf-fdes", Arrays.asList("xml", "xsd", "json"));
	lAllFileExtTypes.addAll(Arrays.asList("asm", "bak", "c", "cbl", "cms", "cpp", "sbt", "java", "for", "htm", "oco", "java", "mak", "cpy", "mac", "h", "hpp", "inc", "dfdl", "xml", "xsd", "json"));
	lIBMTemplate.add(Constants.FILE_TYPE.IBM.name());
	lNONIBMTemplate.add(Constants.FILE_TYPE.NON_IBM.name());
	lBothTemplate.add(Constants.FILE_TYPE.IBM.name());
	lBothTemplate.add(Constants.FILE_TYPE.NON_IBM.name());
	lUploadFileTypes.addAll(Arrays.asList("txt", "bmp", "jpg", "jpeg", "tiff", "msg", "xls", "xlsx", "doc", "docx", "xml", "rtflog", "ppt"));
	lLoadCategoryforCPUs.addAll(Arrays.asList("C", "F"));
	macroHeaderFileList.addAll(Arrays.asList("%.hpp", "%.h", "%.mac", "%.inc", "%.incafs", "%.cpy"));
	IN_MESSAGE_PATTERN = Pattern.compile("`05AUTO\\sACT\\s(\\w*)\\s(\\w*)\\s*\\w*");

	OUT_MESSAGE_PATTERN_RETRY = Pattern.compile("AAES0008I\\s(\\d*)\\s.*\\s(\\w+)-MSG\\s(\\w+)\\sRC=(\\d+)\\s(.*)");
	OUT_MESSAGE_PATTERN_NON_CE = Pattern.compile("AAES0009I\\s.*\\s(\\w+)-MSG\\s([\\w-]+)\\sRC=(\\d+)\\s(.*)");
	OUT_MESSAGE_PATTERN_CE = Pattern.compile("AAES0009I\\s.*\\sAAER0700I\\s.*\\s(\\w+)-MSG\\s([\\w-]+)\\sRC=(\\d+)\\s(.*)");

	FILE_PATH_PATTERN = Pattern.compile("^(\\/?[\\w\\.]+)+\\/?$");
	STRICT_PATH_PATTERN = Pattern.compile("^(\\/[\\w^]+)+\\/$");
	LOG_MESSAGE_FORMAT1 = Pattern.compile("Date:(\\d{12}),\\s*Type:\\w+,\\s*PlanID:([\\w\\.\\-]+),\\s*PlanOwner:[\\w\\.]+,\\s*SourceRef:\\w+,\\s*Status:(\\w+)");
	LOG_MESSAGE_FORMAT2 = Pattern.compile("Date:(\\d{8}),\\s*Type:\\w+,\\s*PlanID:([\\w\\.\\-]+),\\s*PlanOwner:[\\w\\.]+,\\s*SourceRef:\\w+,\\s*Status:(\\w+)");
	LOG_MESSAGE_FORMAT3 = Pattern.compile("Date:(\\d{14}),\\s*Type:\\w+,\\s*PlanID:([\\w\\.\\-]+),\\s*PlanOwner:[\\w\\.]+,\\s*SourceRef:\\w+,\\s*Status:(\\w+)");
	LOG_MESSAGE_FORMAT4 = Pattern.compile("Date:(\\d{12}),\\s*Type:\\w+,\\s*PlanID:([\\w\\.\\-]+),\\s*PlanOwner:[\\w\\.]+,\\s*SourcRef:\\w+,\\s*Status:(\\w+)");
	LOG_MESSAGE_FORMAT5 = Pattern.compile("Date:(\\d{8}),\\s*Type:\\w+,\\s*PlanID:([\\w\\.\\-]+),\\s*PlanOwner:[\\w\\.]+,\\s*SourcRef:\\w+,\\s*Status:(\\w+)");
	LOG_MESSAGE_FORMAT6 = Pattern.compile("Date:(\\d{14}),\\s*Type:\\w+,\\s*PlanID:([\\w\\.\\-]+),\\s*PlanOwner:[\\w\\.]+,\\s*SourcRef:\\w+,\\s*Status:(\\w+)");
	JENKINS_ERROR_LOG_PATTERN = Pattern.compile("^JSON RESULT --> (.*)$", Pattern.MULTILINE);
	lTOSReturnSubStatusList.addAll(Arrays.asList("SUCCESS|SUCCESS", "SUCCESS|FAILED", "FAILED|SUCCESS", "FAILED|FAILED", "SUCCESS|NONE", "FAILED|NONE"));

	GITarget.put("APO", "DEBUGA");
	GITarget.put("PGR", "DEBUGG");
	GITarget.put("PRE", "DEBUGP");
	GITarget.put("WSP", "DEBUG");
	GITarget.put("AIR", "DEBUGA");
	GITarget.put("RES", "DEBUGR");
	GITarget.put("OSS", "DEBUGO");

	GIPlatform.put("APO", "A");
	GIPlatform.put("PGR", "G");
	GIPlatform.put("PRE", "P");
	GIPlatform.put("WSP", "W");
	GIPlatform.put("AIR", "I");
	GIPlatform.put("RES", "S");
	GIPlatform.put("OSS", "O");

	GIProcessingType.put("APO", "zTPF/APZ");
	GIProcessingType.put("PGR", "zTPF/PGZ");
	GIProcessingType.put("PRE", "zTPF/PRZ");
	GIProcessingType.put("WSP", "zTPF/WSP");
	GIProcessingType.put("AIR", "zTPF-AIR4");
	GIProcessingType.put("RES", "zTPF-RES");
	GIProcessingType.put("OSS", "zTPF/DeltaOss");

    }

    public static final String getSourceArtifactDirectory(String pFileExt) {
	for (Map.Entry<String, List<String>> lFileTypewithExt : lFileExtTypes.entrySet()) {
	    if (lFileTypewithExt.getValue().contains(pFileExt.toLowerCase())) {
		return lFileTypewithExt.getKey().trim();
	    }
	}
	return "";

    }

    public enum YodaActivtiyMessage {

	IPADDRESS("Get IP Address"),
	FTP("FTP"),
	LOADANDACTIVATE("Load and Activate"),
	ACTIVATE("Activate"),
	DEACTIVATE("Deactivate"),
	DEACTIVATEANDDELETE("Deactivate and Delete");

	String lDescription;

	private YodaActivtiyMessage(String pDescription) {
	    lDescription = pDescription;
	}

	public String getDescription() {
	    return lDescription;
	}

    }

    public enum CheckinStatus {

	SUCCESS("S"),
	FAILED("F");

	String lDescription;

	private CheckinStatus(String pDescription) {
	    lDescription = pDescription;
	}

	public String getDescription() {
	    return lDescription;
	}

    }

    public enum SYSTEM_QA_TESTING_STATUS {

	BYPASSED_FUNCTIONAL_TESTING("ByPassed Functional Testing only"),
	BYPASSED_REGRESSION_TESTING("ByPassed Regression Testing only"),
	BYPASSED_BOTH("ByPassed Functional And Regression Testing"),
	NONE("");

	private final String qaTestingStatus;

	private SYSTEM_QA_TESTING_STATUS(String pQATestingStatus) {
	    this.qaTestingStatus = pQATestingStatus;
	}

	public String getQATestingStatus() {
	    return qaTestingStatus;
	}

	public static LinkedHashMap<String, String> getSystemQATestingStatusList() {
	    LinkedHashMap<String, String> lQATestingStatusList = new LinkedHashMap<String, String>();
	    for (SYSTEM_QA_TESTING_STATUS lQATestingStatus : SYSTEM_QA_TESTING_STATUS.values()) {
		lQATestingStatusList.put(lQATestingStatus.name(), lQATestingStatus.getQATestingStatus());
	    }
	    return lQATestingStatusList;
	}

	public static List<String> getLoadSetGenPlanStatus() {
	    List<String> testingStatusList = new ArrayList<>();
	    testingStatusList.add(BYPASSED_BOTH.name());
	    testingStatusList.add(BYPASSED_FUNCTIONAL_TESTING.name());
	    return testingStatusList;
	}

	public static List<String> getByPassedRegressionStatus() {
	    List<String> testingStatusList = new ArrayList<>();
	    testingStatusList.add(BYPASSED_BOTH.name());
	    testingStatusList.add(BYPASSED_REGRESSION_TESTING.name());
	    return testingStatusList;
	}

    }

    public enum PLAN_QA_TESTING_STATUS {

	BYPASSED_FUNCTIONAL_TESTING("ByPassed Functional Testing/Passed Functional Testing"),
	BYPASSED_REGRESSION_TESTING("Passed Functional Testing/ByPassed Regression Testing"),
	BYPASSED_BOTH("ByPassed Functional And Regression Testing");

	private final String qaTestingStatus;

	private PLAN_QA_TESTING_STATUS(String pQATestingStatus) {
	    this.qaTestingStatus = pQATestingStatus;
	}

	public String getQATestingStatus() {
	    return qaTestingStatus;
	}

	public static LinkedHashMap<String, String> getPlanQATestingStatusList() {
	    LinkedHashMap<String, String> lQATestingStatusList = new LinkedHashMap<String, String>();
	    for (SYSTEM_QA_TESTING_STATUS lQATestingStatus : SYSTEM_QA_TESTING_STATUS.values()) {
		lQATestingStatusList.put(lQATestingStatus.name(), lQATestingStatus.getQATestingStatus());
	    }
	    return lQATestingStatusList;
	}

    }

    public enum PdddsType {

	PDDDS_LIB_1("linuxPddds"),
	PDDDS_LIB_2("zosPddds");

	private final String pdddsType;

	private PdddsType(String pPdddsType) {
	    this.pdddsType = pPdddsType;
	}

	public String getPpdddsType() {
	    return pdddsType;
	}

    }

    public enum RepoPermission {

	// Don't change the below order at any cost
	// 1. Restricted, 2.. Read, 3. Read_write, 4. Owner
	RESTRICTED("V", true),
	READ("R", true),
	READ_WRITE("RWC", true),
	OWNER("RW+", false);

	private final String repoPermission;
	private final Boolean toShow;

	private RepoPermission(String repoPermission, Boolean toShow) {
	    this.repoPermission = repoPermission;
	    this.toShow = toShow;
	}

	public String getPermission() {
	    return repoPermission;
	}

	public Boolean getToShow() {
	    return toShow;
	}

	public static LinkedHashMap<String, String> getPermissionList(Boolean pToShow) {
	    LinkedHashMap<String, String> lLoaderList = new LinkedHashMap<String, String>();
	    for (RepoPermission repoPermission : RepoPermission.values()) {
		if (repoPermission.getToShow().equals(pToShow)) {
		    lLoaderList.put(repoPermission.name(), repoPermission.getPermission());
		}
	    }
	    return lLoaderList;
	}

	public static String getKey(String pRepoPermission) {
	    for (RepoPermission repoPermission : RepoPermission.values()) {
		if (repoPermission.repoPermission.equals(pRepoPermission)) {
		    return repoPermission.name();
		}
	    }
	    return "";
	}

	public static String getValue(String pRepoPermission) {
	    for (RepoPermission repoPermission : RepoPermission.values()) {
		if (repoPermission.name().equals(pRepoPermission)) {
		    return repoPermission.getPermission();
		}
	    }
	    return "";
	}

	public static LinkedHashMap<String, String> getAllPermissionList() {
	    LinkedHashMap<String, String> lLoaderList = new LinkedHashMap<String, String>();
	    for (RepoPermission repoPermission : RepoPermission.values()) {
		lLoaderList.put(repoPermission.name(), repoPermission.getPermission());
	    }
	    return lLoaderList;
	}

    }

    public enum GenericAPIParameterKeys {

	RETRIVE_TYPE("retriveType"),
	TARGET_SYSTEM("targetSystem"),
	IMP_PLAN("impPlan"),
	PLAN_STATUS_IDS("planStatusids"),
	PROG_NAME("progName"),
	PROG_TYPE("progType"),
	ONLINE_VERSION("onlineVersion"),
	FALLBACK_VERSION("fallbackVersion"),
	FROM_LOAD_DATE("fromLoadDate"),
	TO_LOAD_DATE("toLoadDate"),
	FUNC_AREA("funcArea"),
	DEV_ID("devId"),
	DEV_MANAGER_ID("devMangerId"),
	OUTPUT_PATH("outputPath"),
	PLAN_STATUS_LIST("planStatusList"),
	LDAP_ID_LIST("ldapIdList"),
	USER_NAME("userName"),
	ROLE("role"),
	DEPENDENT_PLANS("Dependent");

	private final String parameterKeys;

	private GenericAPIParameterKeys(String parameterKey) {
	    parameterKeys = parameterKey;
	}

	public String GenericAPIParameterKeys() {
	    return parameterKeys;
	}

	public static LinkedHashMap<String, String> getKeyMap() {
	    LinkedHashMap<String, String> lGenericAPIParameterKeysList = new LinkedHashMap<>();
	    for (GenericAPIParameterKeys lParameterKey : GenericAPIParameterKeys.values()) {
		lGenericAPIParameterKeysList.put(lParameterKey.name(), lParameterKey.GenericAPIParameterKeys());
	    }
	    return lGenericAPIParameterKeysList;
	}

	public static List<String> getValue() {
	    List<String> lGenericAPIParameterKeysList = new ArrayList();
	    for (GenericAPIParameterKeys lParameterKey : GenericAPIParameterKeys.values()) {
		lGenericAPIParameterKeysList.add(lParameterKey.GenericAPIParameterKeys());
	    }
	    return lGenericAPIParameterKeysList;
	}

    }

    public enum PlanInProgressStatus {
	NONE,
	SUBMIT,
	REJECT,
	ACCEPT,
	ONLINE,
	FALLBACK;
    }

    public enum RestrictedCatForAcceptPlan {
	R;

	public static List<String> getCatList() {
	    List<String> lReturnList = new ArrayList();
	    lReturnList.add(RestrictedCatForAcceptPlan.R.name());
	    return lReturnList;
	}
    }

    public enum GenericMailKey {
	NFM_FAIL_MAIL,
	NFM_ALREADY_RUNNING_MAIL,
	NFM_SUCESS_MAIL;
    }

    public enum ImpPlanTrackStatus {

	IN_PROGRESS(1, "In Progress"),
	DEVL_BUILD_OR_LOADSET(2, "DEVL Build /Loadset"),
	SUBMIT(3, "Submit"),
	QA_FUNCTIONAL_TESTING(4, "QA Functional Testing"),
	QA_REGRESSION_TESTING(5, "QA Regression Testing"),
	PRE_PRODUCTION_DEPLOYMENT(6, "Pre-Production Deployment"),
	USER_ACCEPTANCE_TESTING(7, "User Acceptance Testing"),
	DEV_MANAGER_APPROVAL(8, "Dev Manager Approval"),
	LOADS_CONTROL(9, "Loads Control"),
	TSD(10, "TSD"),
	IMP_PLAN_COMPLETED(11, "Implementation Plan completed");

	private final int position;
	private final String attribute;

	private ImpPlanTrackStatus(int position, String attribute) {
	    this.position = position;
	    this.attribute = attribute;
	}

	public static List<TrackStatusForm> getImpPlanTrackStatus() {
	    List<TrackStatusForm> trackStatusForms = new ArrayList<>();
	    for (ImpPlanTrackStatus impPlanStatus : ImpPlanTrackStatus.values()) {
		trackStatusForms.add(new TrackStatusForm(impPlanStatus.position, impPlanStatus.attribute));
	    }
	    return trackStatusForms;
	}

	public static LinkedHashMap<String, TrackStatusForm> getImpPlanTrackStatusMap() {
	    LinkedHashMap<String, TrackStatusForm> trackStatusMap = new LinkedHashMap<>();
	    for (ImpPlanTrackStatus impPlanStatus : ImpPlanTrackStatus.values()) {
		trackStatusMap.put(impPlanStatus.name(), new TrackStatusForm(impPlanStatus.position, impPlanStatus.attribute));
	    }
	    return trackStatusMap;
	}
    }

    public enum MacroPlanTrackStatus {

	IN_PROGRESS(1, "In Progress"),
	SUBMIT(2, "Submit"),
	DEV_MANAGER_APPROVAL(3, "Dev Manager Approval"),
	IMP_PLAN_COMPLETED(4, "Implementation Plan completed");

	private final int position;
	private final String attribute;

	private MacroPlanTrackStatus(int position, String attribute) {
	    this.position = position;
	    this.attribute = attribute;
	}

	public static List<TrackStatusForm> getMacroPlanTrackStatus() {
	    List<TrackStatusForm> trackStatusForms = new ArrayList<>();
	    for (MacroPlanTrackStatus macroPlanStatus : MacroPlanTrackStatus.values()) {
		trackStatusForms.add(new TrackStatusForm(macroPlanStatus.position, macroPlanStatus.attribute));
	    }
	    return trackStatusForms;
	}

	public static LinkedHashMap<String, TrackStatusForm> getMacroPlanTrackStatusMap() {
	    LinkedHashMap<String, TrackStatusForm> trackStatusMap = new LinkedHashMap<>();
	    for (MacroPlanTrackStatus macroPlanStatus : MacroPlanTrackStatus.values()) {
		trackStatusMap.put(macroPlanStatus.name(), new TrackStatusForm(macroPlanStatus.position, macroPlanStatus.attribute));
	    }
	    return trackStatusMap;
	}
    }

    public enum ImpTrackStatus {

	CODE_PREPARATION(1, "Code Preparation"),
	UNIT_TESTING(2, "Unit Testing"),
	PEER_REVIEW(3, "Peer Review"),
	INTEGRATION_TESTING(4, "Integration Testing"),
	READY_FOR_QA(5, "Ready for QA"),
	IMPL_COMPLETED(6, "Impl Completed");

	private final int position;
	private final String attribute;

	private ImpTrackStatus(int position, String attribute) {
	    this.position = position;
	    this.attribute = attribute;
	}

	public static List<TrackStatusForm> getImpTrackStatus() {
	    List<TrackStatusForm> trackStatusForms = new ArrayList<>();
	    for (ImpTrackStatus impStatus : ImpTrackStatus.values()) {
		trackStatusForms.add(new TrackStatusForm(impStatus.position, impStatus.attribute));
	    }
	    return trackStatusForms;
	}

	public static LinkedHashMap<String, TrackStatusForm> getImpTrackStatusMap() {
	    LinkedHashMap<String, TrackStatusForm> trackStatusMap = new LinkedHashMap<>();
	    for (ImpTrackStatus impStatus : ImpTrackStatus.values()) {
		trackStatusMap.put(impStatus.name(), new TrackStatusForm(impStatus.position, impStatus.attribute));
	    }
	    return trackStatusMap;
	}
    }

    public static class TrackStatusForm {

	private Integer id;
	private String status;
	private boolean byPassStatus = false;
	private Set<String> messages = new HashSet<>();
	private boolean isCurrentStage = false;
	private String currentStatus;

	public Set<String> getMessages() {
	    return messages;
	}

	public void setMessages(Set<String> messages) {
	    this.messages = messages;
	}

	public boolean isCurrentStage() {
	    return isCurrentStage;
	}

	public void setCurrentStage(boolean isCurrentStage) {
	    this.isCurrentStage = isCurrentStage;
	}

	public String getCurrentStatus() {
	    return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
	    this.currentStatus = currentStatus;
	}

	TrackStatusForm(int id, String status) {
	    this.id = id;
	    this.status = status;
	}

	public Integer getId() {
	    return id;
	}

	public void setId(Integer id) {
	    this.id = id;
	}

	public String getStatus() {
	    return status;
	}

	public void setStatus(String status) {
	    this.status = status;
	}

	public boolean isByPassStatus() {
	    return byPassStatus;
	}

	public void setByPassStatus(boolean byPassStatus) {
	    this.byPassStatus = byPassStatus;
	}
    }

    public static enum report_type {
	USER_DEPLOYMENT,
	FUNC_PACKAGE,
	QA_TESTING;
    }

    public enum TrackerStatus {
	IN_PROGRESS,
	COMPLETED;
    }

    public enum PRNumberStatuses {
	OPEN("Open"),
	KNOWN_ERROR("Known Error"),
	PENDING_CHANGE("Pending Change"),
	CLOSED("Closed"),
	NEW("New"),
	CANCELED("Canceled"),
	RESOLVED("Resolved"),
	CLOSED_REQUESTED("Closed Requested"),
	ON_HOLD("On Hold"),
	INVESTIGATE("Investigate"),
	CLOSE_REQUESTED("Close Requested"),
	REJECTED("Rejected"),
	RESCHEDULED("Rescheduled");

	private final String status;

	private PRNumberStatuses(String status) {
	    this.status = status;
	}

	public String getPRStatus() {
	    return status;
	}

	public static List<String> getPRActiveStatus() {
	    List<String> returnStatus = new ArrayList();
	    returnStatus.add(OPEN.status);
	    returnStatus.add(KNOWN_ERROR.status);
	    returnStatus.add(PENDING_CHANGE.status);
	    returnStatus.add(NEW.status);
	    returnStatus.add(ON_HOLD.status);
	    returnStatus.add(INVESTIGATE.status);
	    returnStatus.add(REJECTED.status);
	    returnStatus.add(RESCHEDULED.status);
	    return returnStatus;
	}

	public static List<String> getPRClosedStatus() {
	    List<String> returnStatus = new ArrayList();
	    returnStatus.add(CLOSED.status);
	    returnStatus.add(CANCELED.status);
	    returnStatus.add(RESOLVED.status);
	    returnStatus.add(CLOSED_REQUESTED.status);
	    returnStatus.add(CLOSE_REQUESTED.status);
	    return returnStatus;
	}

    }

    public enum PlanActions {
	DEVL_LOADSET_CREATION;
    }

    public enum TDXRunningStatus {
	INPROGRESS("In progress"),
	WAITING("Waiting"),
	PENDING("Pending"),
	COMPLETED("Completed");

	private final String status;

	private TDXRunningStatus(String status) {
	    this.status = status;
	}

	public String getTDXRunningStatus() {
	    return status;
	}
    }

}
