/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

/**
 *
 * @author prabhu
 */
public class AppConfig {

    private static final Logger LOG = Logger.getLogger(AppConfig.class.getName());

    private static AppConfig lAppConfig = null;
    Properties lConfig = null;

    private AppConfig() {
	PropertyConfigurator.configure(AppConfig.class.getResourceAsStream("/log4j-reconsile.properties"));
	StandardPBEStringEncryptor lEncryptor = new StandardPBEStringEncryptor();
	lEncryptor.setPassword("TsiWorkflow");
	lEncryptor.setAlgorithm("PBEWITHMD5ANDDES");
	lEncryptor.setStringOutputType("0X");
	lConfig = new EncryptableProperties(lEncryptor);
	FileReader stream = null;
	try {
	    stream = new FileReader(getSystemPath() + "/app.properties");
	    lConfig.load(stream);
	} catch (IOException ex) {
	    LOG.fatal("Error in Loading Config File", ex);
	} finally {
	    IOUtils.closeQuietly(stream);
	}
    }

    public static AppConfig getInstance() {
	if (lAppConfig == null) {
	    lAppConfig = new AppConfig();
	}
	return lAppConfig;
    }

    public static String getSystemPath() {
	return System.getenv("MTP_ENV");
    }

    public String getServicePassword() {
	return lConfig.getProperty("service.password");
    }

    public String getServiceUserName() {
	return lConfig.getProperty("service.username");
    }

    public String getGitblitURL() {
	return lConfig.getProperty("gitblit.rest.api.url").replace("/rpc/", "");
    }

    public String getWfLoadBalancerHost() {
	return lConfig.getProperty("wf.load.balancer.host");
    }

    public String getGitDataPort() {
	return lConfig.getProperty("git.data.port");
    }

    public String getSessionKey() {
	return lConfig.getProperty("wf.cache.session.key");
    }

    public String getCacheServer() {
	return lConfig.getProperty("wf.cache.server.host");
    }

    public String getCachePort() {
	return lConfig.getProperty("wf.cache.server.port");
    }

    public String getDBDriver() {
	return lConfig.getProperty("db.app.driver");
    }

    public String getDBUserName() {
	return lConfig.getProperty("db.app.username");
    }

    public String getDBPassword() {
	return lConfig.getProperty("db.app.password");
    }

    public String getDBURL() {
	return lConfig.getProperty("db.app.url");
    }

    public CharSequence getGitBasePath() {
	return lConfig.getProperty("git.data.path");
    }

    public String getGitSourcePath() {
	return lConfig.getProperty("git.data.path.source");
    }

    public String getGitDerivedPath() {
	return lConfig.getProperty("git.data.path.derived");
    }

    public String getAzureMailURL() {
	return lConfig.getProperty("mail.azure.url");
    }

    public String getAzureMailKey() {
	return lConfig.getProperty("mail.azure.password");
    }

    public String getAzureId() {
	return lConfig.getProperty("mail.azure.senderid");
    }

    public String getFromMailId() {
	return lConfig.getProperty("mail.azure.from");
    }

    public String getDevOpsMailId() {
	return lConfig.getProperty("wf.dev.ops.centre.mail.id");
    }
}
