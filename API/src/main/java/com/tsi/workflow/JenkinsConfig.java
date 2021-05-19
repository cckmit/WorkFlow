/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import com.tsi.workflow.interfaces.IJenkinsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author USER
 */
@Component
public class JenkinsConfig implements IJenkinsConfig {

    @Value("${jenkins.rest.api.url}")
    String URL;
    @Value("${service.username}")
    String serviceUser;
    @Value("${service.password}")
    String servicePassword;

    public String getURL() {
	return URL;
    }

    public void setURL(String URL) {
	this.URL = URL;
    }

    public String getServiceUser() {
	return serviceUser;
    }

    public void setServiceUser(String serviceUser) {
	this.serviceUser = serviceUser;
    }

    public String getServicePassword() {
	return servicePassword;
    }

    public void setServicePassword(String servicePassword) {
	this.servicePassword = servicePassword;
    }

}
