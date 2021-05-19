/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import com.tsi.workflow.interfaces.ITOSConfig;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author USER
 */
public class TOSConfig implements ITOSConfig {

    @Value("${tos.system.type}")
    private String tosSystemType;
    @Value("${tos.file.domain}")
    private String tosFileDomain;
    @Value("${tos.file.username}")
    private String tosFileUser;
    @Value("${tos.file.password}")
    private String tosFilePassword;
    @Value("${tos.file.path}")
    private String tosFilePath;
    @Value("${tos.server.id}")
    private String tosServerId;

    public String getTosServerId() {
	return tosServerId;
    }

    public void setTosServerId(String tosServerId) {
	this.tosServerId = tosServerId;
    }

    public String getTosFilePath() {
	return tosFilePath;
    }

    public String getTosSystemType() {
	return tosSystemType;
    }

    public String getTosFileDomain() {
	return tosFileDomain;
    }

    public String getTosFileUser() {
	return tosFileUser;
    }

    public String getTosFilePassword() {
	return tosFilePassword;
    }

    public void setTosSystemType(String tosSystemType) {
	this.tosSystemType = tosSystemType;
    }

    public void setTosFileDomain(String tosFileDomain) {
	this.tosFileDomain = tosFileDomain;
    }

    public void setTosFileUser(String tosFileUser) {
	this.tosFileUser = tosFileUser;
    }

    public void setTosFilePassword(String tosFilePassword) {
	this.tosFilePassword = tosFilePassword;
    }

    public void setTosFilePath(String tosFilePath) {
	this.tosFilePath = tosFilePath;
    }

}
