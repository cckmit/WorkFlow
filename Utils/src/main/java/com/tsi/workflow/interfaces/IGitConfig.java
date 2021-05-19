/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.interfaces;

/**
 *
 * @author USER
 */
public interface IGitConfig {

    public String getGitBasePath();

    public String getSSHKeysPath();

    public String getGitProdPath();

    public String getGitSourcePath();

    public String getGitDerivedPath();

    public String getGitHost();

    public Integer getGitDataPort();

    public String getGitblitRestUrl();

    public String getGitblitTicketUrl();

    public String getJGitRestUrl();

    public String getServiceUserID();

    public String getServiceSecret();

    public Integer getGitSshPort();

    public String getWfLoadBalancerHost();

}
