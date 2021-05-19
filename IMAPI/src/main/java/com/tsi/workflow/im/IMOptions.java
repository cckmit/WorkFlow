/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.im;

import java.util.regex.Pattern;

/**
 *
 * @author USER
 */
public class IMOptions {

    public static enum LoadType {

	PRE_PROD("Load Request - PreProduction", "Created Load Request - PreProduction (\\d+)"),
	PROD("Load Request - Production", "Created Load Request - Production (\\d+)");

	String lName;
	Pattern lPattern;

	private LoadType(String pName, String pPattern) {
	    lName = pName;
	    lPattern = Pattern.compile(pPattern);
	}

	public String getName() {
	    return lName;
	}

	public Pattern getResultPattern() {
	    return lPattern;
	}

    }

    public enum OtherKeys {

	COMPANY("Company", "app.mks.company"),
	JOBNAME("Job Name", "app.mks.job.name"),
	FILEPATH("Upload File Path", "java.io.tmpdir"),
	REQUESTOR_PASSWORD("Requestor Name", "app.mks.requestor.password"),
	FALLBACK_VERSION("Fallback Version", "app.mks.product.fallback.version"),
	PROD_LINUX_SERVERS("Prod Linux Servers List", "app.mks.prod.linux.servers"),
	PROD_ZLINUX_SERVERS("Prod ZLinux Servers List", "app.mks.prod.zlinux.servers"),
	PROD_F5_SERVERS("Prod F5 Servers List", "app.mks.prod.f5.servers"),
	PROD_TOS_SERVERS("Prod TOS Servers List", "app.mks.prod.tos.servers"),
	PRE_PROD_LINUX_SERVERS("Pre Prod Linux Servers List", "app.mks.preprod.linux.servers"),
	PRE_PROD_ZLINUX_SERVERS("Pre Prod ZLinux Servers List", "app.mks.preprod.zlinux.servers"),
	PRE_PROD_F5_SERVERS("Pre Prod F5 Servers List", "app.mks.preprod.f5.servers"),
	PRE_PROD_TOS_SERVERS("Pre Prod TOS Servers List", "app.mks.preprod.tos.servers");
	String name;
	String key;
	String defaultValue;

	private OtherKeys(String name, String key) {
	    this.name = name;
	    this.key = key;
	    this.defaultValue = "";
	}

	private OtherKeys(String name, String key, String defaultValue) {
	    this.name = name;
	    this.key = key;
	    this.defaultValue = defaultValue;
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public String getKey() {
	    return key;
	}

	public void setKey(String key) {
	    this.key = key;
	}

	public String getDefaultValue() {
	    return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
	    this.defaultValue = defaultValue;
	}
    }

    public enum ProdKeys {

	ASSIGNED_MANAGER("Assigned Manager", "app.mks.manager.name"),
	ASSIGNED_QA("Assigned QA", "app.mks.qa.name"),
	CHECKPOINT_LABEL("Checkpoint Label", "app.mks.checkpoint.label", "label in script"),
	COMMENTS("Comments", "app.mks.comments"),
	CUSTOMER_IMPACT("Customer Impact Logical", "app.mks.customer.impact", "false"),
	CSR_NUMBER("CSR Number", "app.mks.csr.number"),
	CURL_ARGUMENTS("cURL Arguments", "app.mks.url.arguments"),
	DEPENDENCIES("Dependencies", "app.mks.dependencies", "none"),
	DEPLOYMENTURL("DeploymentURL", "app.mks.deployment.url"),
	FALLBACKURL("FallbackURL", "app.mks.fallback.url"),
	FALLBACK_PLAN("Fallback Plan", "app.mks.fallback.plan", "This code would be replaced with the previous version"),
	LINE_OF_BUSINESS("Line of Business", "app.mks.lob", "Infrastructure Services"),
	LOAD_LEVEL("Load Level (1, 2, 3)", "app.mks.loadlevel", "1"),
	LOAD_TIME_ISSPECIFIC("Load is Date and Time Specific", "app.mks.loadtime.isspecific", "false"),
	MONITORING_AFFECTED("Monitoring Affected", "app.mks.moitor"),
	PLANNED_END("Planned End", "app.mks.load.end.date"),
	PRODUCT_NUMBER("Product Number", "app.mks.product.number", "1"),
	PRODUCT_VERSION("Product Version", "app.mks.product.version"),
	PROJECT("Project", "app.mks.project.name", "/zTPFDevOpsToolChain"),
	PURPOSE("Purpose", "app.mks.purpose"),
	REQUESTED_LOAD_DATE("Requested Load Date", "app.mks.load.start.date"),
	REQUESTOR_NAME("Requestor Name", "app.mks.requestor.name"),
	REQUESTOR_PHONE("Requestor Phone", "app.mks.requestor.phone"),
	RESTART_REQUIRED("Restart Required", "app.mks.restart.required", "false"),
	SERVERS("Servers", "app.mks.servers", "Multiple servers in script"),
	SERVER_OS("Server OS", "app.mks.server.os", "Linux"),
	SPECIAL_INSTRUCTIONS("Special Instructions", "app.mks.special.inst", "Automated scripts will be attached"),
	SUMMARY("Summary", "app.mks.summary", "zTPF Modernization Tool - Workflow"),
	TESTING_INSTRUCTIONS("Testing Instructions", "app.mks.testing.inst", "QA will run regression script after deployment"),
	TRAVELPORT_NOTIFICATION("Travelport Notification", "app.mks.tp.notification", "false");

	String name;
	String key;
	String defaultValue;

	private ProdKeys(String name, String key) {
	    this.name = name;
	    this.key = key;
	    this.defaultValue = "";
	}

	private ProdKeys(String name, String key, String defaultValue) {
	    this.name = name;
	    this.key = key;
	    this.defaultValue = defaultValue;
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public String getKey() {
	    return key;
	}

	public void setKey(String key) {
	    this.key = key;
	}

	public String getDefaultValue() {
	    return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
	    this.defaultValue = defaultValue;
	}

    }

    public enum PreProdKeys {

	ASSIGNED_QA("Assigned QA", "app.mks.qa.name"),
	BYPASS_QA_TESTING("Bypass QA Testing", "app.mks.bypass.qa", "true"),
	CHECKPOINT_LABEL("Checkpoint Label", "app.mks.checkpoint.label", "label in script"),
	COMMENTS("Comments", "app.mks.comments"),
	CSR_NUMBER("CSR Number", "app.mks.csr.number"),
	CURL_ARGUMENTS("cURL Arguments", "app.mks.url.arguments"),
	DEPENDENCIES("Dependencies", "app.mks.dependencies", "none"),
	DEPLOYMENTURL("DeploymentURL", "app.mks.deployment.url"),
	FALLBACKURL("FallbackURL", "app.mks.fallback.url"),
	FALLBACK_PLAN("Fallback Plan", "app.mks.fallback.plan", "This code would be replaced with the previous version"),
	LINE_OF_BUSINESS("Line of Business", "app.mks.lob", "Infrastructure Services"),
	PLANNED_END("Planned End", "app.mks.load.end.date"),
	PRODUCT_NUMBER("Product Number", "app.mks.product.number", "1"),
	PRODUCT_VERSION("Product Version", "app.mks.product.version"),
	PROJECT("Project", "app.mks.project.name", "/zTPFDevOpsToolChain"),
	PURPOSE("Purpose", "app.mks.purpose"),
	REQUESTED_LOAD_DATE("Requested Load Date", "app.mks.load.start.date"),
	REQUESTOR_NAME("Requestor Name", "app.mks.requestor.name"),
	REQUESTOR_PHONE("Requestor Phone", "app.mks.requestor.phone"),
	REQUEST_FILE_NAME("Request File Name", "app.mks.request.file.name", "NA"),
	RESTART_REQUIRED("Restart Required", "app.mks.restart.required", "false"),
	SERVERS("Servers", "app.mks.servers", "Multiple servers in script"),
	SERVER_OS("Server OS", "app.mks.server.os", "Linux"),
	SPECIAL_INSTRUCTIONS("Special Instructions", "app.mks.special.inst", "Automated scripts will be attached"),
	SUMMARY("Summary", "app.mks.summary", "zTPF Modernization Tool - Workflow"),
	TESTING_INSTRUCTIONS("Testing Instructions", "app.mks.testing.inst", "QA will run regression script after deployment"),
	TRAVELPORT_NOTIFICATION("Travelport Notification", "app.mks.tp.notification", "false");

	String name;
	String key;
	String defaultValue;

	private PreProdKeys(String name, String key) {
	    this.name = name;
	    this.key = key;
	    this.defaultValue = "";
	}

	private PreProdKeys(String name, String key, String defaultValue) {
	    this.name = name;
	    this.key = key;
	    this.defaultValue = defaultValue;
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public String getKey() {
	    return key;
	}

	public void setKey(String key) {
	    this.key = key;
	}

	public String getDefaultValue() {
	    return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
	    this.defaultValue = defaultValue;
	}

    }

    public static String getProperty(PreProdKeys pKey) {
	return System.getProperty(pKey.getKey(), pKey.getDefaultValue());
    }

    public static String getProperty(OtherKeys pKey) {
	return System.getProperty(pKey.getKey(), pKey.getDefaultValue());
    }

    public static String getProperty(ProdKeys pKey) {
	return System.getProperty(pKey.getKey(), pKey.getDefaultValue());
    }

}
