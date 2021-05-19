/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;
import static com.tsi.workflow.DataWareHouse.getUser;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.git.GitBranchSearchResult;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import java.util.Arrays;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Radha.Adhimoolam
 */
public class SSHUtilDataHub {

    public static void init() {
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SSHUtil.getConnectedUser", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SSHUtil.setConnectedUser", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SSHUtil.getConnectedHost", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SSHUtil.setConnectedHost", paramInOut);
	}

	for (int i = 0; i < DataWareHouse.getSystemList().size(); i++) {
	    {
		ParamInOut paramInOut = new ParamInOut();
		paramInOut.addIn(DataWareHouse.getSystemList().get(i));
		paramInOut.setOut(Boolean.TRUE);
		ParameterMap.addParameterInOut("SSHUtil.connectSSH", paramInOut);
	    }
	}
	for (int i = 0; i < DataWareHouse.getSystemList().size(); i++) {
	    {
		ParamInOut paramInOut = new ParamInOut();
		paramInOut.addIn(getUser());
		paramInOut.addIn(DataWareHouse.getSystemList().get(i));
		paramInOut.setOut(Boolean.TRUE);
		ParameterMap.addParameterInOut("SSHUtil.connectSSH", paramInOut);
	    }
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("SSHUtil.connectSSH", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("SSHUtil.connectSSH", paramInOut);
	}
	for (int i = 0; i < DataWareHouse.getSystemList().size(); i++) {
	    {
		ParamInOut paramInOut = new ParamInOut();
		paramInOut.addIn(DataWareHouse.getSystemList().get(i).getIpaddress());
		paramInOut.addIn(DataWareHouse.getSystemList().get(i).getPortno());
		paramInOut.addIn(DataWareHouse.getUser().getId());
		paramInOut.addIn(DataWareHouse.getUser().getPassword());
		paramInOut.setOut(Boolean.TRUE);
		ParameterMap.addParameterInOut("SSHUtil.connectSSHWithPassword", paramInOut);
	    }
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SSHUtil.disconnectSSH", paramInOut);
	}

	for (GitBranchSearchResult lBranch : DataWareHouse.getGitSearchResult().getBranch()) {
	    String lCommand = Constants.SystemScripts.CHECKOUT.getScript() + DataWareHouse.getPlan().getImplementationList().get(0).getId().toLowerCase() + "_" + lBranch.getTargetSystem().replace("master_", "").toLowerCase() + " " + DataWareHouse.getGitSearchResult().getFileName() + " " + DataWareHouse.getGitSearchResult().getFileHashCode() + " " + lBranch.getCommitId() + " " + lBranch.getSourceUrl() + " " + getPlan().getId().toLowerCase();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (GitBranchSearchResult lBranch : DataWareHouse.getGitSearchResult().getBranch()) {
	    String lCommand = Constants.SystemScripts.CREATE_SOURCE_ARTIFACT.getScript() + DataWareHouse.getPlan().getImplementationList().get(0).getId().toLowerCase() + " " + lBranch.getTargetSystem().replace("master_", "").toLowerCase() + " " + lBranch.getSourceUrl() + " " + DataWareHouse.getGitSearchResult().getFileName();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	// {
	// String lBranch = "";
	// for(String lBranchs : DataWareHouse.getBranchList()){
	// lBranch = lBranch + "," + lBranchs ;
	// }
	// lBranch = lBranch.substring(1);
	// String lCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript()
	// + DataWareHouse.RepoName + " "
	// + lBranch;
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(lCommand);
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	// }
	for (System lSystem : DataWareHouse.getSystemList()) {
	    String lCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + DataWareHouse.RepoName + " " + DataWareHouse.getPlan().getImplementationList().get(0).getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}

	{
	    String lCommand = Constants.SystemScripts.DELETE_FILE.getScript() + DataWareHouse.getPlan().getImplementationList().get(0).getId().toLowerCase() + " " + DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getTargetSystem().toLowerCase() + " " + DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getFileName();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	// for (GitBranchSearchResult lBranch :
	// DataWareHouse.getGitSearchResult().getBranch()) {
	// JSONResponse lResponse = new JSONResponse();
	// lResponse.setStatus(Boolean.TRUE);
	// lResponse.setData(DataWareHouse.getGitSearchResult().getProgramName() +
	// ".mak");
	// String lCommand = Constants.SystemScripts.SEARCH_MAK_FILE.getScript() +
	// DataWareHouse.getGitSearchResult().getProgramName() + " " +
	// lBranch.getTargetSystem().replace("master_", "").toLowerCase();
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(lCommand);
	// paramInOut.setOut(lResponse);
	// ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	// }

	{
	    String lCommand = Constants.SystemScripts.GIT_CONFIG_USER.getScript() + getUser().getDisplayName();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.SystemScripts.GIT_CONFIG_MAIL.getScript() + getUser().getMailId();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (int i = 0; i < getPlan().getDbcrList().size(); i++) {
	    JSONResponse lResponse = new JSONResponse();
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData("test " + "Constants.SystemScripts.DBCR_VALIDATE.getScript() + getPlan().getDbcrList().get(i).getDbcrName() " + getPlan().getDbcrList().get(i).getSystemId().getName() + " " + "Four" + "five");
	    String lCommand = Constants.SystemScripts.DBCR_VALIDATE.getScript() + getPlan().getDbcrList().get(i).getDbcrName();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(lResponse);
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}

	{
	    String lCommand = Constants.SystemScripts.FTP.getScript() + getPlan().getImplementationList().get(0).getId().toLowerCase() + "_" + getPlan().getBuildList().get(0).getSystemId().getName().toLowerCase() + " " + "ipaddress need to set" + " " + getPlan().getBuildList().get(0).getLoadSetType() + " " + getPlan().getSystemLoadList().get(0).getLoadSetName() + Constants.LOAD_FILE_EXTENSION.OLDR_EXTN.getValue() + " " + getPlan().getBuildList().get(0).getBuildType().substring(0, 3);
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.SystemScripts.FTP.getScript() + getPlan().getImplementationList().get(0).getId().toLowerCase() + "_" + getPlan().getBuildList().get(0).getSystemId().getName().toLowerCase() + " " + "ipaddress need to set" + " " + getPlan().getBuildList().get(0).getLoadSetType() + " " + getPlan().getSystemLoadList().get(0).getLoadSetName() + Constants.LOAD_FILE_EXTENSION.TLDR_EXTN.getValue() + " " + getPlan().getBuildList().get(0).getBuildType().substring(0, 3);
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}

	{
	    String lCommand = Constants.SystemScripts.FTP.getScript() + getPlan().getImplementationList().get(0).getId().toLowerCase() + "_" + getPlan().getBuildList().get(0).getSystemId().getName().toLowerCase() + " " + "ipaddress need to set" + " " + getPlan().getBuildList().get(0).getLoadSetType() + " " + getPlan().getSystemLoadList().get(0).getFallbackLoadSetName() + Constants.LOAD_FILE_EXTENSION.OLDR_EXTN.getValue() + " " + getPlan().getBuildList().get(0).getBuildType().substring(0, 3);
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.SystemScripts.FTP.getScript() + getPlan().getImplementationList().get(0).getId().toLowerCase() + "_" + getPlan().getBuildList().get(0).getSystemId().getName().toLowerCase() + " " + "ipaddress need to set" + " " + getPlan().getBuildList().get(0).getLoadSetType() + " " + getPlan().getSystemLoadList().get(0).getFallbackLoadSetName() + Constants.LOAD_FILE_EXTENSION.TLDR_EXTN.getValue() + " " + getPlan().getBuildList().get(0).getBuildType().substring(0, 3);
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (GitBranchSearchResult lBranch : DataWareHouse.getGitSearchResult().getBranch()) {
	    String lCommand = Constants.SystemScripts.COMMIT.getScript() + " \"" + (getPlan().getImplementationList().get(0).getId() + "_" + lBranch.getTargetSystem().replace("master_", "")).toLowerCase() + "\" \"" + String.join(",", DataWareHouse.getGitSearchResult().getFileName()) + "\" \"" + "Commit Message" + "\"";
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (System lSystem : DataWareHouse.getSystemList()) {

	    String lCommand = Constants.SystemScripts.GET_LATEST.getScript() + " " + (getPlan().getImplementationList().get(0).getId() + "_" + lSystem.getName()).toLowerCase() + " " + "Origin" + " " + getPlan().getId().toLowerCase();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (System lSystem : DataWareHouse.getSystemList()) {

	    String lCommand = Constants.SystemScripts.CHECK_IN.getScript() + (getPlan().getImplementationList().get(0).getId() + "_" + lSystem.getName()).toLowerCase();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (System lSystem : DataWareHouse.getSystemList()) {

	    String lCommand = Constants.SystemScripts.CREATE_DEVL_WORKSPACE.getScript() + (getPlan().getImplementationList().get(0).getId() + "_" + lSystem.getName()).toLowerCase();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (System lSystem : DataWareHouse.getSystemList()) {

	    String lCommand = Constants.SystemScripts.EXPORT.getScript() + "/home/" + getUser().getId() + "/projects/" + (getPlan().getImplementationList().get(0).getId() + "_" + lSystem.getName()).toLowerCase() + " \"" + (getPlan().getImplementationList().get(0).getId() + "_" + lSystem.getName()).toLowerCase() + "\" \"" + "git Service Id" + "\"";
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	// for (SystemLoadActions lSystemLoadAction :
	// DataWareHouse.getPlan().getSystemLoadActionsList()) {
	//
	// String lCommand = Constants.SystemScripts.DSL_FILE_UPDATE.getScript() + " " +
	// getPlan().getImplementationList().get(0).getId().toLowerCase() + " " +
	// lSystemLoadAction.getSystemId().getName().toLowerCase() + " " +
	// lSystemLoadAction.getVparId().getName().toLowerCase() + " " +
	// getPlan().getSystemLoadList().get(0).getLoadSetName() + " " +
	// Constants.REX_DATEFORMAT.get().format(lSystemLoadAction.getSystemLoadId().getLoadDateTime());
	//
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(lCommand);
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	// }
	// for (SystemLoadActions lSystemLoadAction :
	// DataWareHouse.getPlan().getSystemLoadActionsList()) {
	//
	// String lCommand = Constants.SystemScripts.DSL_FILE_DELETE.getScript() + " " +
	// getPlan().getImplementationList().get(0).getId().toLowerCase() + " " +
	// lSystemLoadAction.getSystemId().getName().toLowerCase() + " " +
	// lSystemLoadAction.getVparId().getName().toLowerCase() + " " +
	// getPlan().getSystemLoadList().get(0).getLoadSetName() + " " +
	// Constants.REX_DATEFORMAT.get().format(lSystemLoadAction.getSystemLoadId().getLoadDateTime());
	//
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(lCommand);
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	// }
	// {
	// String lCommand =
	// Constants.SystemScripts.DELETE_STAGING_WORKSPACE.getScript() +
	// getPlan().getId().toLowerCase();
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(lCommand);
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	// }
	// for (SystemLoad lSystemLoad : DataWareHouse.getPlan().getSystemLoadList()) {
	// for (Constants.PROD_SCRIPT_PARAMS value :
	// Constants.PROD_SCRIPT_PARAMS.values()) {
	// String lCommand = Constants.SystemScripts.PRODUCTION_MERGE.getScript() +
	// getPlan().getImplementationList().get(0).getId().toLowerCase() + "_" +
	// lSystemLoad.getSystemId().getName().toLowerCase() + "_" +
	// Constants.JENKINS_DATEFORMAT.get().format(lSystemLoad.getLoadDateTime()) + "
	// " + value.name();
	//
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(lCommand);
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	// }
	// }
	// for (System lSystem : DataWareHouse.getSystemList()) {
	//
	// String lCommand = Constants.SystemScripts.TPF_MAINTENANCE_CHECK.getScript();
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(lCommand);
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	// }
	// for (System lSystem : DataWareHouse.getSystemList()) {
	//
	// String lCommand = "nohup " +
	// Constants.SystemScripts.TPF_TEST_SYSTEM_MAINTENANCE.getScript() + " " + "&";
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(lCommand);
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	// }
	// {
	//
	// String lCommand =
	// Constants.SystemScripts.TPF_TEST_SYSTEM_MAINTENANCE_OLDR_CHECK.getScript();
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(lCommand);
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	// }
	{

	    String lCommand = Constants.SystemScripts.TPF_TEST_SYSTEM_MAINTENANCE_OLDR_CREATION.getScript();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (System lSystem : DataWareHouse.getSystemList()) {
	    JSONResponse lResponse = new JSONResponse();
	    lResponse.setStatus(Boolean.TRUE);
	    String lCommand = Constants.SystemScripts.COMPILER_CONTROL_VALIDATION.getScript() + " " + getPlan().getImplementationList().get(0).getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(lResponse);
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (SystemLoad lSystemLoad : DataWareHouse.getPlan().getSystemLoadList()) {

	    String lCommand = Constants.SystemScripts.DATE_AUDIT_CROSS_CHECK.getScript() + " " + lSystemLoad.getSystemId().getName().toLowerCase() + " " + Constants.REX_DATEFORMAT.get().format(lSystemLoad.getLoadDateTime()) + " " + StringUtils.join(Arrays.asList(DataWareHouse.getGitSearchResult().getFileName()), ",");
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (SystemLoad lSystemLoad : DataWareHouse.getPlan().getSystemLoadList()) {

	    String lCommand = Constants.GitScripts.CREATE_TICKET.getScript() + DataWareHouse.PlanSSHUrl + " " + getPlan().getImplementationList().get(0).getId().toLowerCase() + " " + getPlan().getImplementationList().get(0).getPeerReviewers();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (SystemLoad lSystemLoad : DataWareHouse.getPlan().getSystemLoadList()) {
	    String lCommand = Constants.GitScripts.IBM_POPULATE.getScript() + DataWareHouse.getGitSearchResult().getFileName().trim() + " " + lSystemLoad.getPutLevelId().getScmUrl() + " " + Constants.BRANCH_MASTER + lSystemLoad.getSystemId().getName().toLowerCase();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.GitScripts.TAG_CMD.getScript() + Constants.TagStatus.SECURED.name() + " " + DataWareHouse.RepoName + " " + String.join(",", Arrays.asList("master"));
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.GitScripts.TAG_CMD.getScript() + Constants.TagStatus.READY_FOR_QA.name() + " " + DataWareHouse.RepoName + " " + String.join(",", Arrays.asList("master"));
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.GitScripts.TAG_CMD.getScript() + Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name() + " " + DataWareHouse.RepoName + " " + String.join(",", Arrays.asList("master"));
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.GitScripts.TAG_CMD.getScript() + Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED.name() + " " + DataWareHouse.RepoName + " " + String.join(",", Arrays.asList("master"));
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.GitScripts.TAG_CMD.getScript() + Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name() + " " + DataWareHouse.RepoName + " " + String.join(",", Arrays.asList("master"));
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.GitScripts.TAG_CMD.getScript() + Constants.ImplementationSubStatus.PEER_REVIEW_COMPLETED.name() + " " + DataWareHouse.RepoName + " " + String.join(",", Arrays.asList("master"));
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}

	{
	    String lCommand = Constants.GitScripts.REMOVE_TAG_CMD.getScript() + Constants.ImplementationSubStatus.PEER_REVIEW_COMPLETED.name() + " " + DataWareHouse.RepoName;
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.GitScripts.REMOVE_TAG_CMD.getScript() + Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name() + " " + DataWareHouse.RepoName;
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.GitScripts.REMOVE_TAG_CMD.getScript() + Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED.name() + " " + DataWareHouse.RepoName;
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.GitScripts.REMOVE_TAG_CMD.getScript() + Constants.TagStatus.SECURED.name() + " " + DataWareHouse.RepoName;
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}

	for (GitBranchSearchResult lBranch : DataWareHouse.getGitSearchResult().getBranch()) {
	    String lCommand = Constants.SystemScripts.CHECKOUT.getScript() + DataWareHouse.getPlan().getImplementationList().get(0).getId().toLowerCase() + "_" + lBranch.getTargetSystem().replace("master_", "").toLowerCase() + " " + DataWareHouse.getGitSearchResult().getFileName() + " " + DataWareHouse.getGitSearchResult().getFileHashCode() + " " + lBranch.getCommitId() + " " + lBranch.getSourceUrl() + " " + getPlan().getId().toLowerCase();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (GitBranchSearchResult lBranch : DataWareHouse.getGitSearchResult().getBranch()) {
	    String lCommand = Constants.SystemScripts.CREATE_SOURCE_ARTIFACT.getScript() + DataWareHouse.getPlan().getImplementationList().get(0).getId().toLowerCase() + " " + lBranch.getTargetSystem().replace("master_", "").toLowerCase() + " " + lBranch.getSourceUrl() + " " + DataWareHouse.getGitSearchResult().getFileName();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	// {
	// String lBranch = "";
	// for(String lBranchs : DataWareHouse.getBranchList()){
	// lBranch = lBranch + lBranchs + ",";
	// }
	// lBranch = lBranch.substring(1);
	// String lCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript()
	// + DataWareHouse.RepoName + " "
	// + lBranch;
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(lCommand);
	// paramInOut.addIn(Boolean.FALSE);
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	// }
	{
	    String lCommand = Constants.SystemScripts.DELETE_FILE.getScript() + DataWareHouse.getPlan().getImplementationList().get(0).getId().toLowerCase() + " " + DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getTargetSystem().toLowerCase() + " " + DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getFileName();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	// for (GitBranchSearchResult lBranch :
	// DataWareHouse.getGitSearchResult().getBranch()) {
	// String lCommand = Constants.SystemScripts.SEARCH_MAK_FILE.getScript() +
	// DataWareHouse.getGitSearchResult().getProgramName() + " " +
	// lBranch.getTargetSystem().replace("master_", "").toLowerCase();
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(lCommand);
	// paramInOut.addIn(Boolean.FALSE);
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	// }
	{
	    String lCommand = Constants.SystemScripts.GIT_CONFIG_USER.getScript() + getUser().getDisplayName();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.SystemScripts.GIT_CONFIG_MAIL.getScript() + getUser().getMailId();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.SystemScripts.DBCR_VALIDATE.getScript() + getPlan().getDbcrList().get(0).getDbcrName();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}

	{
	    String lCommand = Constants.SystemScripts.FTP.getScript() + getPlan().getImplementationList().get(0).getId().toLowerCase() + "_" + getPlan().getBuildList().get(0).getSystemId().getName().toLowerCase() + " " + "ipaddress need to set" + " " + getPlan().getBuildList().get(0).getLoadSetType() + " " + getPlan().getSystemLoadList().get(0).getLoadSetName() + Constants.LOAD_FILE_EXTENSION.OLDR_EXTN.getValue() + " " + getPlan().getBuildList().get(0).getBuildType().substring(0, 3);
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.SystemScripts.FTP.getScript() + getPlan().getImplementationList().get(0).getId().toLowerCase() + "_" + getPlan().getBuildList().get(0).getSystemId().getName().toLowerCase() + " " + "ipaddress need to set" + " " + getPlan().getBuildList().get(0).getLoadSetType() + " " + getPlan().getSystemLoadList().get(0).getLoadSetName() + Constants.LOAD_FILE_EXTENSION.TLDR_EXTN.getValue() + " " + getPlan().getBuildList().get(0).getBuildType().substring(0, 3);
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}

	{
	    String lCommand = Constants.SystemScripts.FTP.getScript() + getPlan().getImplementationList().get(0).getId().toLowerCase() + "_" + getPlan().getBuildList().get(0).getSystemId().getName().toLowerCase() + " " + "ipaddress need to set" + " " + getPlan().getBuildList().get(0).getLoadSetType() + " " + getPlan().getSystemLoadList().get(0).getFallbackLoadSetName() + Constants.LOAD_FILE_EXTENSION.OLDR_EXTN.getValue() + " " + getPlan().getBuildList().get(0).getBuildType().substring(0, 3);
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.SystemScripts.FTP.getScript() + getPlan().getImplementationList().get(0).getId().toLowerCase() + "_" + getPlan().getBuildList().get(0).getSystemId().getName().toLowerCase() + " " + "ipaddress need to set" + " " + getPlan().getBuildList().get(0).getLoadSetType() + " " + getPlan().getSystemLoadList().get(0).getFallbackLoadSetName() + Constants.LOAD_FILE_EXTENSION.TLDR_EXTN.getValue() + " " + getPlan().getBuildList().get(0).getBuildType().substring(0, 3);
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (GitBranchSearchResult lBranch : DataWareHouse.getGitSearchResult().getBranch()) {
	    String lCommand = Constants.SystemScripts.COMMIT.getScript() + " \"" + (getPlan().getImplementationList().get(0).getId() + "_" + lBranch.getTargetSystem().replace("master_", "")).toLowerCase() + "\" \"" + String.join(",", DataWareHouse.getGitSearchResult().getFileName()) + "\" \"" + "Commit" + "\"";
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (System lSystem : DataWareHouse.getSystemList()) {

	    String lCommand = Constants.SystemScripts.GET_LATEST.getScript() + " " + (getPlan().getImplementationList().get(0).getId() + "_" + lSystem.getName()).toLowerCase() + " " + "Origin" + " " + getPlan().getId().toLowerCase();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (System lSystem : DataWareHouse.getSystemList()) {

	    String lCommand = Constants.SystemScripts.CHECK_IN.getScript() + (getPlan().getImplementationList().get(0).getId() + "_" + lSystem.getName()).toLowerCase();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (System lSystem : DataWareHouse.getSystemList()) {

	    String lCommand = Constants.SystemScripts.CREATE_DEVL_WORKSPACE.getScript() + (getPlan().getImplementationList().get(0).getId() + "_" + lSystem.getName()).toLowerCase();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (System lSystem : DataWareHouse.getSystemList()) {

	    String lCommand = Constants.SystemScripts.EXPORT.getScript() + "/home/" + getUser().getId() + "/projects/" + (getPlan().getImplementationList().get(0).getId() + "_" + lSystem.getName()).toLowerCase() + " \"" + (getPlan().getImplementationList().get(0).getId() + "_" + lSystem.getName()).toLowerCase() + "\" \"" + "git Service Id" + "\"";
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	// for (SystemLoadActions lSystemLoadAction :
	// DataWareHouse.getPlan().getSystemLoadActionsList()) {
	//
	// String lCommand = Constants.SystemScripts.DSL_FILE_UPDATE.getScript() + " " +
	// getPlan().getImplementationList().get(0).getId().toLowerCase() + " " +
	// lSystemLoadAction.getSystemId().getName().toLowerCase() + " " +
	// lSystemLoadAction.getVparId().getName().toLowerCase() + " " +
	// getPlan().getSystemLoadList().get(0).getLoadSetName() + " " +
	// Constants.REX_DATEFORMAT.get().format(lSystemLoadAction.getSystemLoadId().getLoadDateTime());
	//
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(lCommand);
	// paramInOut.addIn(Boolean.FALSE);
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	// }
	// for (SystemLoadActions lSystemLoadAction :
	// DataWareHouse.getPlan().getSystemLoadActionsList()) {
	//
	// String lCommand = Constants.SystemScripts.DSL_FILE_DELETE.getScript() + " " +
	// getPlan().getImplementationList().get(0).getId().toLowerCase() + " " +
	// lSystemLoadAction.getSystemId().getName().toLowerCase() + " " +
	// lSystemLoadAction.getVparId().getName().toLowerCase() + " " +
	// getPlan().getSystemLoadList().get(0).getLoadSetName() + " " +
	// Constants.REX_DATEFORMAT.get().format(lSystemLoadAction.getSystemLoadId().getLoadDateTime());
	//
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(lCommand);
	// paramInOut.addIn(Boolean.FALSE);
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	// }
	{
	    String lCommand = Constants.SystemScripts.DELETE_STAGING_WORKSPACE.getScript() + getPlan().getId().toLowerCase();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (SystemLoad lSystemLoad : DataWareHouse.getPlan().getSystemLoadList()) {
	    for (Constants.PROD_SCRIPT_PARAMS value : Constants.PROD_SCRIPT_PARAMS.values()) {
		String lCommand = Constants.SystemScripts.PRODUCTION_MERGE.getScript() + getPlan().getImplementationList().get(0).getId().toLowerCase() + "_" + lSystemLoad.getSystemId().getName().toLowerCase() + "_" + Constants.JENKINS_DATEFORMAT.get().format(lSystemLoad.getLoadDateTime()) + " " + value.name();

		ParamInOut paramInOut = new ParamInOut();
		paramInOut.addIn(lCommand);
		paramInOut.addIn(Boolean.FALSE);
		paramInOut.setOut(DataWareHouse.getPositiveResponse());
		ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	    }
	}
	// for (System lSystem : DataWareHouse.getSystemList()) {
	//
	// String lCommand = Constants.SystemScripts.TPF_MAINTENANCE_CHECK.getScript();
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(lCommand);
	// paramInOut.addIn(Boolean.FALSE);
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	// }
	// for (System lSystem : DataWareHouse.getSystemList()) {
	//
	// String lCommand = "nohup " +
	// Constants.SystemScripts.TPF_TEST_SYSTEM_MAINTENANCE.getScript() + " " + "&";
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(lCommand);
	// paramInOut.addIn(Boolean.FALSE);
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	// }
	// {
	//
	// String lCommand =
	// Constants.SystemScripts.TPF_TEST_SYSTEM_MAINTENANCE_OLDR_CHECK.getScript();
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(lCommand);
	// paramInOut.addIn(Boolean.FALSE);
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	// }
	{

	    String lCommand = Constants.SystemScripts.TPF_TEST_SYSTEM_MAINTENANCE_OLDR_CREATION.getScript();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (System lSystem : DataWareHouse.getSystemList()) {

	    String lCommand = Constants.SystemScripts.COMPILER_CONTROL_VALIDATION.getScript() + " " + getPlan().getImplementationList().get(0).getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (SystemLoad lSystemLoad : DataWareHouse.getPlan().getSystemLoadList()) {

	    String lCommand = Constants.SystemScripts.DATE_AUDIT_CROSS_CHECK.getScript() + " " + lSystemLoad.getSystemId().getName().toLowerCase() + " " + Constants.REX_DATEFORMAT.get().format(lSystemLoad.getLoadDateTime()) + " " + StringUtils.join(Arrays.asList(DataWareHouse.getGitSearchResult().getFileName()), ",");
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (SystemLoad lSystemLoad : DataWareHouse.getPlan().getSystemLoadList()) {

	    String lCommand = Constants.GitScripts.CREATE_TICKET.getScript() + DataWareHouse.PlanSSHUrl + " " + getPlan().getImplementationList().get(0).getId().toLowerCase() + " " + getPlan().getImplementationList().get(0).getPeerReviewers();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (SystemLoad lSystemLoad : DataWareHouse.getPlan().getSystemLoadList()) {

	    String lCommand = Constants.GitScripts.IBM_POPULATE.getScript() + DataWareHouse.getGitSearchResult().getFileName().trim() + " " + lSystemLoad.getPutLevelId().getScmUrl() + " " + Constants.BRANCH_MASTER + lSystemLoad.getSystemId().getName().toLowerCase();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.GitScripts.TAG_CMD.getScript() + Constants.TagStatus.SECURED.name() + " " + DataWareHouse.RepoName + " " + String.join(",", Arrays.asList("master"));
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.GitScripts.TAG_CMD.getScript() + Constants.TagStatus.READY_FOR_QA.name() + " " + DataWareHouse.RepoName + " " + String.join(",", Arrays.asList("master"));
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.GitScripts.TAG_CMD.getScript() + Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name() + " " + DataWareHouse.RepoName + " " + String.join(",", Arrays.asList("master"));
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.GitScripts.TAG_CMD.getScript() + Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED.name() + " " + DataWareHouse.RepoName + " " + String.join(",", Arrays.asList("master"));
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.GitScripts.TAG_CMD.getScript() + Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name() + " " + DataWareHouse.RepoName + " " + String.join(",", Arrays.asList("master"));
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.GitScripts.TAG_CMD.getScript() + Constants.ImplementationSubStatus.PEER_REVIEW_COMPLETED.name() + " " + DataWareHouse.RepoName + " " + String.join(",", Arrays.asList("master"));
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}

	{
	    String lCommand = Constants.GitScripts.REMOVE_TAG_CMD.getScript() + Constants.ImplementationSubStatus.PEER_REVIEW_COMPLETED.name() + " " + DataWareHouse.RepoName;
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.GitScripts.REMOVE_TAG_CMD.getScript() + Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name() + " " + DataWareHouse.RepoName;
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.GitScripts.REMOVE_TAG_CMD.getScript() + Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED.name() + " " + DataWareHouse.RepoName;
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = Constants.GitScripts.REMOVE_TAG_CMD.getScript() + Constants.TagStatus.SECURED.name() + " " + DataWareHouse.RepoName;
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.addIn(Boolean.FALSE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	{
	    String lCommand = "readlink -f " + " | find $(awk '{print $1}') -type f -name " + DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getProgramName() + "*";
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}
	for (System lSystem : DataWareHouse.getSystemList()) {
	    String lCommand = Constants.SystemScripts.SEARCH_SO.getScript() + getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getProgramName().substring(0, 3).toLowerCase() + " " + lSystem.getName().toLowerCase();
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lCommand);
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("SSHUtil.executeCommand", paramInOut);
	}

    }

}
