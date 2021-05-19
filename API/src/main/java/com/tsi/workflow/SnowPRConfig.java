/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import com.tsi.workflow.interfaces.ISnowPRConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Radha.Adhimoolam
 */
@Component
public class SnowPRConfig extends WFProxyConfig implements ISnowPRConfig {
    @Value("${snow.pr.rest.api.url}")
    private String restSNOWPRUrl;
    @Value("${snow.pr.rest.api.userid}")
    private String restSNOWPRUserId;
    @Value("${snow.pr.rest.api.passcode}")
    private String restSNOWPRPasscode;

    public String getRestSNOWPRUrl() {
	return restSNOWPRUrl;
    }

    public void setRestSNOWPRUrl(String restSNOWPRUrl) {
	this.restSNOWPRUrl = restSNOWPRUrl;
    }

    public String getRestSNOWPRUserId() {
	return restSNOWPRUserId;
    }

    public void setRestSNOWPRUserId(String restSNOWPRUserId) {
	this.restSNOWPRUserId = restSNOWPRUserId;
    }

    public String getRestSNOWPRPasscode() {
	return restSNOWPRPasscode;
    }

    public void setRestSNOWPRPasscode(String restSNOWPRPasscode) {
	this.restSNOWPRPasscode = restSNOWPRPasscode;
    }

}
