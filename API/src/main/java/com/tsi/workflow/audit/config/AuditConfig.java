/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Radha.Adhimoolam
 */
@Component
public class AuditConfig {

    private static final Logger LOG = Logger.getLogger(AuditConfig.class.getName());

    @Value("${wf.performance.audit.system}")
    private Boolean auditSystem;

    @Value("${wf.performance.audit.transaction}")
    private Boolean auditTransaction;

    @Value("${wf.performance.audit.methods}")
    private String auditMethods;

    // @Value("${wf.performance.audit.servername}")
    // private String auditServerName;
    //
    // @Value("${wf.performance.audit.port}")
    // private String auditServerPort;

    public Boolean getAuditSystem() {
	return auditSystem;
    }

    public void setAuditSystem(Boolean auditSystem) {
	this.auditSystem = auditSystem;
    }

    public String getAuditMethods() {
	return auditMethods;
    }

    public void setAuditMethods(String auditMethods) {
	this.auditMethods = auditMethods;
    }

    public List<String> getAuditMethodsAsList() {
	if (getAuditMethods() != null && !getAuditMethods().isEmpty()) {
	    return Arrays.asList(getAuditMethods().split(","));
	}
	return new ArrayList();
    }

    public Boolean getAuditTransaction() {
	return auditTransaction;
    }

    public void setAuditTransaction(Boolean auditTransaction) {
	this.auditTransaction = auditTransaction;
    }

    // public String getAuditServerName() {
    // return auditServerName;
    // }
    //
    // public void setAuditServerName(String auditServerName) {
    // this.auditServerName = auditServerName;
    // }
    //
    // public String getAuditServerPort() {
    // return auditServerPort;
    // }
    //
    // public void setAuditServerPort(String auditServerPort) {
    // this.auditServerPort = auditServerPort;
    // }

}
