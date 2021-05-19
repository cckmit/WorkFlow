package com.tsi.workflow;

import com.tsi.workflow.interfaces.IGitConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author USER
 */
@Component
public class GITConfig implements IGitConfig {

    @Value("${git.data.path}")
    private String gitBasePath;
    @Value("${git.data.path.prod}")
    private String gitProdPath;
    @Value("${git.data.path.source}")
    private String gitSourcePath;
    @Value("${git.data.path.derived}")
    private String gitDerivedPath;
    @Value("${git.host}")
    private String gitHost;
    @Value("${git.data.port}")
    private Integer gitDataPort;
    @Value("${gitblit.rest.api.url}")
    private String gitblitRestUrl;
    @Value("${gitblit.ticket.url}")
    private String gitblitTicketUrl;
    @Value("${jgit.rest.api.url}")
    private String JGitRestUrl;
    @Value("${service.username}")
    private String serviceUserID;
    @Value("${service.password}")
    private String serviceSecret;
    @Value("${git.ssh.port}")
    private Integer gitSshPort;
    @Value("${ssh.key.path}")
    private String SSHKeysPath;
    @Value("${wf.load.balancer.host}")
    private String wfLoadBalancerHost;
    @Value("${wf.git.search.type}")
    private String gitSearchType;

    public String getGitSearchType() {
	return gitSearchType;
    }

    public void setGitSearchType(String gitSearchType) {
	this.gitSearchType = gitSearchType;
    }

    public String getGitBasePath() {
	return gitBasePath;
    }

    public void setGitBasePath(String gitBasePath) {
	this.gitBasePath = gitBasePath;
    }

    public String getGitProdPath() {
	return gitProdPath;
    }

    public void setGitProdPath(String gitProdPath) {
	this.gitProdPath = gitProdPath;
    }

    public String getGitSourcePath() {
	return gitSourcePath;
    }

    public void setGitSourcePath(String gitSourcePath) {
	this.gitSourcePath = gitSourcePath;
    }

    public String getGitDerivedPath() {
	return gitDerivedPath;
    }

    public void setGitDerivedPath(String gitDerivedPath) {
	this.gitDerivedPath = gitDerivedPath;
    }

    public String getGitHost() {
	return gitHost;
    }

    public void setGitHost(String gitHost) {
	this.gitHost = gitHost;
    }

    public Integer getGitDataPort() {
	return gitDataPort;
    }

    public void setGitDataPort(Integer gitDataPort) {
	this.gitDataPort = gitDataPort;
    }

    public String getGitblitRestUrl() {
	return gitblitRestUrl;
    }

    public void setGitblitRestUrl(String gitblitRestUrl) {
	this.gitblitRestUrl = gitblitRestUrl;
    }

    public String getGitblitTicketUrl() {
	return gitblitTicketUrl;
    }

    public void setGitblitTicketUrl(String gitblitTicketUrl) {
	this.gitblitTicketUrl = gitblitTicketUrl;
    }

    public String getJGitRestUrl() {
	return JGitRestUrl;
    }

    public void setJGitRestUrl(String JGitRestUrl) {
	this.JGitRestUrl = JGitRestUrl;
    }

    public String getServiceUserID() {
	return serviceUserID;
    }

    public void setServiceUserID(String serviceUserID) {
	this.serviceUserID = serviceUserID;
    }

    public String getServiceSecret() {
	return serviceSecret;
    }

    public void setServiceSecret(String serviceSecret) {
	this.serviceSecret = serviceSecret;
    }

    public Integer getGitSshPort() {
	return gitSshPort;
    }

    public void setGitSshPort(Integer gitSshPort) {
	this.gitSshPort = gitSshPort;
    }

    public String getSSHKeysPath() {
	return SSHKeysPath;
    }

    public void setSSHKeysPath(String SSHKeysPath) {
	this.SSHKeysPath = SSHKeysPath;
    }

    public String getWfLoadBalancerHost() {
	return wfLoadBalancerHost;
    }

    public void setWfLoadBalancerHost(String wfLoadBalancerHost) {
	this.wfLoadBalancerHost = wfLoadBalancerHost;
    }

}
