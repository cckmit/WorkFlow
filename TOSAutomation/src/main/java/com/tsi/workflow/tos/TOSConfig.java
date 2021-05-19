/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 *
 * @author USER
 */
public class TOSConfig {

    private static final Logger LOG = Logger.getLogger(TOSConfig.class.getName());
    private static final String[] lThreads = { "alpha", "beta", "delta", "epsilon", "eta", "gamma", "iota", "kappa", "lambda", "mu", "nu", "omicron", "pi", "rho", "sigma", "tau", "theta", "upsilon", "xi", "zeta" };
    private static int lThreadCount = -1;
    private static int lMultiplier = 0;
    private static TOSConfig lTOSConfig;
    Properties properties;
    StandardPBEStringEncryptor encryptor;

    private TOSConfig() throws IOException {
	FileReader configFileReader = null;
	try {
	    properties = new Properties();
	    encryptor = new StandardPBEStringEncryptor();
	    encryptor.setPassword("TsiWorkflow");
	    encryptor.setAlgorithm("PBEWITHMD5ANDDES");
	    encryptor.setStringOutputType("0X");
	    File lConfigFile = new File("app.properties");
	    LOG.info("Loading Config : " + lConfigFile.getAbsolutePath());
	    configFileReader = new FileReader(lConfigFile);
	    properties.load(configFileReader);
	} finally {
	    IOUtils.closeQuietly(configFileReader);
	}
    }

    public static TOSConfig getInstance() {
	if (lTOSConfig == null) {
	    try {
		lTOSConfig = new TOSConfig();
	    } catch (IOException ex) {
		LOG.error("Error in reading property file", ex);
	    }
	}
	return lTOSConfig;
    }

    public String getServiceUserID() {
	return properties.getProperty("service.username");
    }

    public String getNextThreadName() {
	lThreadCount++;
	if (lThreads.length - 1 < lThreadCount) {
	    lThreadCount = 0;
	    lMultiplier++;
	}
	return lThreads[lThreadCount] + "_" + lMultiplier;
    }

    public String getServiceSecret() {
	String property = properties.getProperty("service.password");
	if (property.startsWith("ENC")) {
	    return encryptor.decrypt(property.substring(4, property.length() - 1));
	} else {
	    return property;
	}
    }

    public String getEncrptorValue(String name) {
	String property = properties.getProperty(name);
	if (property.startsWith("ENC")) {
	    return encryptor.decrypt(property.substring(4, property.length() - 1));
	} else {
	    return property;
	}
    }

    public String getProfileId() {
	return properties.getProperty("service.username");
    }

    public String getTOSEnvName() {
	return properties.getProperty("tos.server.id");
    }

    public String getAzureMailURL() {
	return getEncrptorValue("mail.azure.url");
    }

    public String getAzureMailKey() {
	return getEncrptorValue("mail.azure.password");
    }

    public String getAzureId() {
	return properties.getProperty("mail.azure.senderid");
    }

    public String getFromMailId() {
	return properties.getProperty("mail.azure.from");
    }

    public String getSendingApplication() {
	return properties.getProperty("mail.send.app.name");
    }

    public String getDevOpsMailId() {
	return properties.getProperty("wf.dev.ops.centre.mail.id");
    }

}
