/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.tsi.workflow.utils.Constants.AUTOREJECT_COMMENT;
import com.tsi.workflow.utils.Constants.Channels;
import com.tsi.workflow.utils.Constants.DBCR_STATUS;
import com.tsi.workflow.utils.Constants.DEVL;
import com.tsi.workflow.utils.Constants.FALLBACK_STATUS;
import com.tsi.workflow.utils.Constants.GitScripts;
import com.tsi.workflow.utils.Constants.LoadTypes;
import com.tsi.workflow.utils.Constants.PROD_LOAD_STATUS;
import com.tsi.workflow.utils.Constants.PROD_SCRIPT_PARAMS;
import com.tsi.workflow.utils.Constants.REJECT_REASON;
import com.tsi.workflow.utils.Constants.STAGING;
import com.tsi.workflow.utils.Constants.TOSEnvironment;
import com.tsi.workflow.utils.Constants.UserGroup;
import com.tsi.workflow.utils.Constants.UserSettings;
import com.tsi.workflow.utils.Constants.VPARSEnvironment;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class ConstantsTest {

    public ConstantsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetSourceArtifactDirectory() {
	String pFileExt = "c";
	String expResult = "src";
	String result = Constants.getSourceArtifactDirectory(pFileExt);
	assertEquals(expResult, result);
	String result1 = Constants.getSourceArtifactDirectory("test");
	assertEquals("", result1);
	UserSettings x1 = UserSettings.DELEGATE_USER;
	List<String> delegatedUserList = UserGroup.getDelegatedUserList();
	assertNotNull(delegatedUserList);
	LoadTypes x2 = Constants.LoadTypes.STANDARD;
	String description = Constants.LoginErrorCode.SUCCESS.getDescription();
	assertNotNull(description);
	Map<String, String> statusMap = Constants.TagStatus.getStatusMap();
	assertNotNull(statusMap);
	Map<String, String> statusMap1 = Constants.ImplementationStatus.getStatusMap();
	assertNotNull(statusMap1);
	TOSEnvironment x3 = Constants.TOSEnvironment.NATIVE_CPU;
	List<String> autoDeactivationEnv = VPARSEnvironment.getAutoDeactivationEnv();
	assertNotNull(autoDeactivationEnv);
	Map<String, String> vparsMap = VPARSEnvironment.getVparsMap();
	assertNotNull(vparsMap);
	Map<String, String> statusMap2 = Constants.ImplementationSubStatus.getStatusMap();
	assertNotNull(statusMap2);
	SortedSet<String> ibmSourceArtificatExtList = Constants.SourceArtificatExtension.getIBMSourceArtificatExtList();
	assertNotNull(ibmSourceArtificatExtList);
	SortedSet<String> nonibmSourceArtificatExtList = Constants.SourceArtificatExtension.getNONIBMSourceArtificatExtList();
	assertNotNull(nonibmSourceArtificatExtList);
	LinkedHashMap<String, String> map1 = Constants.PlanStatus.getAfterSubmitStatus();
	assertNotNull(map1);
	LinkedHashMap<String, String> map2 = Constants.PlanStatus.getApprovedAndAboveStatus();
	assertNotNull(map2);
	LinkedHashMap<String, String> map3 = Constants.PlanStatus.getApprovedStatusMap();
	assertNotNull(map3);
	LinkedHashMap<String, String> map4 = Constants.PlanStatus.getBeforeApprovedStatus();
	assertNotNull(map4);
	Map<String, String> map7 = Constants.PlanStatus.getNonProdStatusMap();
	assertNotNull(map7);
	LinkedHashMap<String, String> map9 = Constants.PlanStatus.getSecuredBeforeReadyForProduction();
	assertNotNull(map9);
	LinkedHashMap<String, String> map10 = Constants.PlanStatus.getSecuredStatusMap();
	assertNotNull(map10);
	LinkedHashMap<String, String> map11 = Constants.PlanStatus.getTSSDeploymentStatus();
	assertNotNull(map11);
	LinkedHashMap<String, String> map12 = Constants.PlanStatus.getStatusMap();
	assertNotNull(map12);
	String script = Constants.SystemScripts.GET_LATEST.getScript();
	PROD_LOAD_STATUS x4 = Constants.PROD_LOAD_STATUS.ACCEPTED;
	x4.getProdLoadStatus();
	LinkedHashMap<String, String> map16 = Constants.PROD_LOAD_STATUS.getProdLoadStatusList();
	assertNotNull(map16);
	// Constants.SSOHeaders.AUTHORIZATION.getKey();
	Constants.LoadSetCommands.GETIP.getScript();
	FALLBACK_STATUS x5 = Constants.FALLBACK_STATUS.ACCEPT_FALLBACK_LOADSET;
	PROD_SCRIPT_PARAMS x6 = Constants.PROD_SCRIPT_PARAMS.FALLBACK_ACCEPT;
	Channels x7 = Constants.Channels.CREATE_WORKSPACE;
	Constants.LOAD_FILE_EXTENSION.OLDR_EXTN.getValue();
	Constants.DBCR_ENVIRONMENT.COPY.getValue();
	DBCR_STATUS x8 = Constants.DBCR_STATUS.COMPLETE;
	List<String> deleteScenarios = Constants.LOAD_SET_STATUS.getDeleteScenarios();
	assertNotNull(deleteScenarios);
	List<String> loadScenarios = Constants.LOAD_SET_STATUS.getLoadScenarios();
	assertNotNull(loadScenarios);
	Constants.LOAD_SET_STATUS.LOADED.getLoadsetStatus();
	LinkedHashMap<String, String> map15 = Constants.LOAD_SET_STATUS.getLoadsetStatusList();
	assertNotNull(map15);
	List<String> lLoadSetStatus1 = Constants.LOAD_SET_STATUS.getAcceptScenarios();
	assertNotNull(lLoadSetStatus1);
	List<String> lLoadSetStatus2 = Constants.LOAD_SET_STATUS.getAcceptAndFallbackList();
	assertNotNull(lLoadSetStatus2);
	AUTOREJECT_COMMENT x9 = Constants.AUTOREJECT_COMMENT.FALLBACK;
	Constants.AUTOREJECT_COMMENT.FALLBACK.getValue();
	REJECT_REASON x10 = Constants.REJECT_REASON.FALLBACK;
	Constants.REJECT_REASON.FALLBACK.getValue();
	Constants.LoaderTypes x11 = Constants.LoaderTypes.A;
	Constants.BUILD_TYPE x12 = Constants.BUILD_TYPE.DVL_BUILD;
	Constants.BUILD_TYPE.DVL_BUILD.getBuildTypes();
	LinkedHashMap<String, String> map14 = Constants.BUILD_TYPE.getStagingBuildType();
	assertNotNull(map14);
	STAGING x13 = Constants.STAGING.BLD_STG_;
	DEVL x14 = Constants.DEVL.BLD_DVL_;
	GitScripts x15 = Constants.GitScripts.CREATE_TICKET;
	Constants.GitScripts.CREATE_TICKET.getScript();
	Constants.YodaActivtiyMessage.values();
	Constants.YodaActivtiyMessage.ACTIVATE.getDescription();
	Constants.LoaderTypes.getLoaderTypesList();
	Constants.LoaderTypes.values();

    }

    @Test
    public void testConstants() {
	Constants instance = new Constants();
	Constants.LoadTypes.STANDARD.getLoadTypes();
	LinkedHashMap<String, String> map1 = Constants.LoadTypes.getLoadTypesList();
	assertNotNull(map1);
	Constants.HOOK_DATE_FORMAT.get();
	Constants.SHELL_DATEFORMAT.get();
	Constants.JENKINS_DATEFORMAT.get();
	Constants.REX_DATEFORMAT.get();
	Constants.TOS_DATEFORMAT.get();
	Constants.JGIT_COMMENT_DATEFORMAT_1.get();
	Constants.JGIT_COMMENT_DATEFORMAT_2.get();
	Constants.JGIT_COMMENT_DATEFORMAT_3.get();
	Constants.JGIT_COMMENT_DATEFORMAT_4.get();
	Constants.JGIT_COMMENT_DATEFORMAT_5.get();
	Constants.JGIT_COMMENT_DATEFORMAT_6.get();
	Constants.CacheCodes.DELEGATION.name();
	Constants.UserGroup.DevManager.isAccessCheck();
	Constants.UserGroup.DevManager.setAccessCheck(true);
	Constants.UserGroup.getKeyMappingGroupList();
	Constants.UserGroup.getLoadAttendeeUserList();
	Constants.PlanStatus.getAllStatusMap();
	Constants.PlanStatus.getPRStatus();
	Constants.LOAD_SET_STATUS.getFallbackFinalList();
	Constants.CheckinStatus.FAILED.name();
	Constants.CheckinStatus.SUCCESS.name();
	Constants.SYSTEM_QA_TESTING_STATUS.getSystemQATestingStatusList();
	Constants.PLAN_QA_TESTING_STATUS.getPlanQATestingStatusList();
	Constants.RepoPermission.getPermissionList(true);
	Constants.RepoPermission.getKey("R");
	Constants.RepoPermission.getValue("READ");

    }

}
