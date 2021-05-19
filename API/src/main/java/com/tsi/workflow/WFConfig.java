/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author USER
 */
@Component
public class WFConfig {

    @Value("${wf.attachment.base.dir}")
    private String attachmentDirectory;
    @Value("${wf.attachment.load.approval.dir}")
    private String loadApprovalDirectory;
    @Value("${wf.ibm.vanilla.dir}")
    private String ibmVanillaDirectory;
    @Value("${wf.attachment.test.results.dir}")
    private String testResultsDir;
    @Value("${wf.build.log.dir}")
    private String buildLogDir;
    @Value("${wf.dev.centre.mail.id}")
    private String devCentreMailID;
    @Value("${tos.jms.url}")
    private String tosJMSUrl;
    @Value("${service.username}")
    private String serviceUserID;
    @Value("${service.password}")
    private String servicePassword;
    @Value("${profileName}")
    private String profileName;
    @Value("${isPrimary}")
    private Boolean primary;
    @Value("${wf.cache.session.key}")
    private String sessionKey;
    @Value("${wf.maintenance}")
    private Boolean maintenance;
    @Value("${wf.sso.api.url}")
    private String ssoAPIUrl;
    @Value("${wf.api.url}")
    private String apiUrl;
    @Value("${wf.dl.fallback.online.mail.id}")
    private String deltaFallbackOnloneMailId;
    @Value("${wf.loadsControl.centre.mail.id}")
    private String loadsControlCentreMailId;
    @Value("${wf.git.search.type}")
    private String gitSearchType;
    @Value("${wf.ea.vm.dev.centre.mail.id}")
    private String eaVmDevCentreMailId;
    @Value("${wf.dev.ops.centre.mail.id}")
    private String devOpsCentreMailId;
    @Value("${tos.server.id}")
    private String tosServerId;
    @Value("${wf.dlcoreteam.centre.mail.id}")
    private String dlCoreTeamMail;
    @Value("${wf.sdm.db.migrated}")
    private Boolean isSdmDBMigrated;

    /*
     * ZTPFM-1795 Production Load Summary Mail
     */
    @Value("${wf.prod.load.centre.mail.id}")
    private String prodLoadsCentreMailId;
    @Value("${isMultipleBuildAllowed}")
    private Boolean multipleBuildAllowed;
    @Value("${wf.run.shellscript.parallel}")
    private Boolean runShellScriptParallel;

    /*
     * ZTPFM-2704 Move the derived artifacts at scheduled time
     */
    @Value("${wf.move.derived.artifacts.scheduler}")
    private Integer moveDerivedArtifactsScheduledTime;

    public String getGitSearchType() {
	return gitSearchType;
    }

    public void setGitSearchType(String gitSearchType) {
	this.gitSearchType = gitSearchType;
    }

    public String getSessionKey() {
	return sessionKey;
    }

    public boolean getPrimary() {
	return primary;
    }

    public void setPrimary(Boolean primary) {
	this.primary = primary;
    }

    public boolean isMultipleBuildAllowed() {
	return multipleBuildAllowed;
    }

    public void setMultipleBuildAllowed(Boolean buildChnage) {
	this.multipleBuildAllowed = buildChnage;
    }

    public void setSessionKey(String sessionKey) {
	this.sessionKey = sessionKey;
    }

    public Boolean getMaintenance() {
	return maintenance;
    }

    public void setMaintenance(Boolean maintenance) {
	this.maintenance = maintenance;
    }

    public Boolean getIsDeltaApp() {
	return profileName.startsWith("DL") || profileName.startsWith("local") || profileName.startsWith("DEV") || profileName.startsWith("QA");
    }

    public Boolean getIsTravelportApp() {
	return profileName.startsWith("TP") || profileName.startsWith("local") || profileName.startsWith("DEV") || profileName.startsWith("QA");
    }

    public String getProfileName() {
	return profileName;
    }

    public void setProfileName(String profileName) {
	this.profileName = profileName;
    }

    public String getServiceUserID() {
	return serviceUserID;
    }

    public void setServiceUserID(String serviceUserID) {
	this.serviceUserID = serviceUserID;
    }

    public String getServicePassword() {
	return servicePassword;
    }

    public void setServicePassword(String servicePassword) {
	this.servicePassword = servicePassword;
    }

    public String getBuildLogDir() {
	return buildLogDir;
    }

    public String getDVLBuildLogDir() {
	return buildLogDir + "DVL/";
    }

    public String getSTGBuildLogDir() {
	return buildLogDir + "STG/";
    }

    public String getONLINEBuildLogDir() {
	return buildLogDir + "ONLINE/";
    }

    public String getFALLBACKBuildLogDir() {
	return buildLogDir + "FALLBACK/";
    }

    public void setBuildLogDir(String buildLogDir) {
	this.buildLogDir = buildLogDir;
    }

    public String getTestResultsDir() {
	return testResultsDir;
    }

    public void setTestResultsDir(String testResultsDir) {
	this.testResultsDir = testResultsDir;
    }

    public String getIbmVanillaDirectory() {
	return ibmVanillaDirectory;
    }

    public void setIbmVanillaDirectory(String ibmVanillaDirectory) {
	this.ibmVanillaDirectory = ibmVanillaDirectory;
    }

    public String getAttachmentDirectory() {
	return attachmentDirectory;
    }

    public void setAttachmentDirectory(String attachmentDirectory) {
	this.attachmentDirectory = attachmentDirectory;
    }

    public String getLoadApprovalDirectory() {
	return loadApprovalDirectory;
    }

    public void setLoadApprovalDirectory(String loadApprovalDirectory) {
	this.loadApprovalDirectory = loadApprovalDirectory;
    }

    public String getDevCentreMailID() {
	return devCentreMailID;
    }

    public void setDevCentreMailID(String devCentreMailID) {
	this.devCentreMailID = devCentreMailID;
    }

    public String getTosJMSUrl() {
	return tosJMSUrl;
    }

    public void setTosJMSUrl(String tosJMSUrl) {
	this.tosJMSUrl = tosJMSUrl;
    }

    public String getSsoAPIUrl() {
	return ssoAPIUrl;
    }

    public void setSsoAPIUrl(String ssoAPIUrl) {
	this.ssoAPIUrl = ssoAPIUrl;
    }

    public String getApiUrl() {
	return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
	this.apiUrl = apiUrl;
    }

    public String getDeltaFallbackOnloneMailId() {
	return deltaFallbackOnloneMailId;
    }

    public void setDeltaFallbackOnloneMailId(String deltaFallbackOnloneMailId) {
	this.deltaFallbackOnloneMailId = deltaFallbackOnloneMailId;
    }

    public String getLoadsControlCentreMailId() {
	return loadsControlCentreMailId;
    }

    public void setLoadsControlCentreMailId(String loadsControlCentreMailId) {
	this.loadsControlCentreMailId = loadsControlCentreMailId;
    }

    public String getEaVmDevCentreMailId() {
	return eaVmDevCentreMailId;
    }

    public void setEaVmDevCentreMailId(String eaVmDevCentreMailId) {
	this.eaVmDevCentreMailId = eaVmDevCentreMailId;
    }

    public String getDevOpsCentreMailId() {
	return devOpsCentreMailId;
    }

    public void setDevOpsCentreMailId(String devOpsCentreMailId) {
	this.devOpsCentreMailId = devOpsCentreMailId;
    }

    public String getTOSServerId() {
	return tosServerId;
    }

    public void setTOSServerId(String serverId) {
	this.tosServerId = serverId;
    }

    public String getProdLoadsCentreMailId() {
	return prodLoadsCentreMailId;
    }

    public void setProdLoadsCentreMailId(String prodLoadsCentreMailId) {
	this.prodLoadsCentreMailId = prodLoadsCentreMailId;
    }

    public String getDlCoreTeamMail() {
	return dlCoreTeamMail;
    }

    public void setDlCoreTeamMail(String dlCoreTeamMail) {
	this.dlCoreTeamMail = dlCoreTeamMail;
    }

    public String getTosServerId() {
	return tosServerId;
    }

    public void setTosServerId(String tosServerId) {
	this.tosServerId = tosServerId;
    }

    public Boolean getIsSdmDBMigrated() {
	return isSdmDBMigrated;
    }

    public void setIsSdmDBMigrated(Boolean isSdmDBMigrated) {
	this.isSdmDBMigrated = isSdmDBMigrated;
    }

    public Boolean getRunShellScriptParallel() {
	return runShellScriptParallel;
    }

    public void setRunShellScriptParallel(Boolean runShellScriptParallel) {
	this.runShellScriptParallel = runShellScriptParallel;
    }

    public Integer getMoveDerivedArtifactsScheduledTime() {
	return moveDerivedArtifactsScheduledTime;
    }

    public void setMoveDerivedArtifactsScheduledTime(Integer moveDerivedArtifactsScheduledTime) {
	this.moveDerivedArtifactsScheduledTime = moveDerivedArtifactsScheduledTime;
    }

}
