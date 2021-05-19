/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import com.tsi.workflow.cache.CacheServer;
import java.io.File;
import org.apache.log4j.Logger;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jasypt.spring31.properties.EncryptablePropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 * @author USER
 */
@Configuration
@EnableScheduling
public class AppConfig {

    private static final Logger LOG = Logger.getLogger(AppConfig.class.getName());

    @Bean
    public GITConfig GITConfig() {
	return new GITConfig();
    }

    @Bean
    public CacheConfig cacheConfig() {
	return new CacheConfig();
    }

    @Bean
    public EnvironmentStringPBEConfig environmentVariablesConfiguration() {
	EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
	config.setAlgorithm("PBEWithMD5AndDES");
	config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
	config.setStringOutputType("0X");
	config.setPoolSize(1);
	config.setPassword("TsiWorkflow");
	return config;
    }

    @Bean
    public PooledPBEStringEncryptor stringEncryptor() {
	PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
	encryptor.setConfig(environmentVariablesConfiguration());
	return encryptor;
    }

    @Bean
    public PropertyPlaceholderConfigurer propertyConfigurer() {
	EncryptablePropertyPlaceholderConfigurer propertyConfigurer = new EncryptablePropertyPlaceholderConfigurer(stringEncryptor());
	String lFileDirPath = System.getenv("MTP_ENV");
	File lConfigFile = new File(lFileDirPath, "app.properties");
	LOG.info("Loading Config : " + lConfigFile.getAbsolutePath());
	propertyConfigurer.setLocation(new FileSystemResource(lConfigFile));
	return propertyConfigurer;
    }

    @Bean(initMethod = "startServer", destroyMethod = "stopServer")
    public CacheServer cacheServer() {
	return new CacheServer();
    }

}
