/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import com.tsi.workflow.interfaces.IWFProxyConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Radha.Adhimoolam
 */
@Component
public class WFProxyConfig implements IWFProxyConfig {

    @Value("${wf.proxy.host.name}")
    private String wfProxyHostName;

    @Value("${wf.proxy.host.port}")
    private Integer wfProxyHostPort;

    @Override
    public String getWFProxyHostName() {
	return wfProxyHostName;
    }

    @Override
    public Integer getWFProxyHostPort() {
	return wfProxyHostPort;
    }

}
