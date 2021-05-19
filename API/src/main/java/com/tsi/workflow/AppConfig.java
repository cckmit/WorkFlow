/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import com.tsi.workflow.audit.config.AuditConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.git.IJGITSearchUtils;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.git.JGitClientUtilsDB;
import com.tsi.workflow.git.JGitSearchUtils;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.route.GITClassicSearchCondition;
import com.tsi.workflow.route.GITDBSearchCondition;
import com.tsi.workflow.snow.pr.SnowPRClientUtils;
import com.tsi.workflow.tdx.executor.TdxShellExecutor;
import com.workflow.mail.AzureMailUtil;
import com.workflow.ssh.SSHClientUtils;
import com.workflow.tos.PRWriter;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.log.NullLogChute;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jasypt.spring31.properties.EncryptablePropertyPlaceholderConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author USER
 */
@Configuration
@EnableScheduling
@EnableJms
@EnableAsync
public class AppConfig {

    private static final Logger LOG = Logger.getLogger(AppConfig.class.getName());

    @Bean(initMethod = "createAnonymsContext", destroyMethod = "closeAnonymousConnection")
    @DependsOn(value = "ldapConfig")
    public LDAPAuthenticatorImpl LDAPAuthenticatorImpl() {
	return new LDAPAuthenticatorImpl();
    }

    @Bean
    public VelocityEngine velocityEngine() throws Exception {
	Properties properties = new Properties();
	properties.setProperty("input.encoding", "UTF-8");
	properties.setProperty("output.encoding", "UTF-8");
	properties.setProperty("resource.loader", "class");
	properties.setProperty("runtime.log.logsystem.class", NullLogChute.class.getName());
	properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
	VelocityEngine velocityEngine = new VelocityEngine(properties);
	return velocityEngine;
    }

    @Bean
    public GITConfig GITConfig() {
	return new GITConfig();
    }

    @Bean(initMethod = "loadLdapConfig")
    public LdapGroupConfig LdapGroupConfig() {
	return new LdapGroupConfig();
    }

    @Bean
    public WFConfig WFConfig() {
	return new WFConfig();
    }

    @Bean
    public TOSConfig TOSConfig() {
	return new TOSConfig();
    }

    @Bean
    public PRConfig PRConfig() {
	return new PRConfig();
    }

    @Bean
    public PRWriter PRWriter() {
	return new PRWriter(PRConfig());
    }

    @Bean
    public JenkinsConfig JenkinsConfig() {
	return new JenkinsConfig();
    }

    @Bean
    public BPMConfig BPMConfig() {
	return new BPMConfig();
    }

    @Bean
    public MailConfig MailConfig() {
	return new MailConfig();
    }

    @Bean
    public AzureMailUtil AzureMailUtil() {
	return new AzureMailUtil(MailConfig());
    }

    @Bean(initMethod = "getCacheInstance")
    public CacheClient cacheClient() {
	return new CacheClient();
    }

    @Bean
    public JGitClientUtils GITUtils() {
	return new JGitClientUtils(GITConfig());
    }

    @Bean(name = "jGITSearchUtils")
    @Conditional(GITClassicSearchCondition.class)
    public IJGITSearchUtils JGITDBSearchUtils() {
	LOG.info("Creating JGIT Bean");
	return new JGitSearchUtils(GITConfig());
    }

    @Bean(name = "jGITSearchUtils")
    @Conditional(GITDBSearchCondition.class)
    public IJGITSearchUtils JGITClassicSearchUtils() {
	LOG.info("Creating JGIT DB Bean");
	return new JGitClientUtilsDB();
    }

    @Bean
    public SSHClientUtils SSHGITUtils() {
	return new SSHClientUtils(GITConfig());
    }

    @Bean
    public JenkinsClient JenkinsClient() throws IOException {
	return new JenkinsClient(JenkinsConfig());
    }

    @Bean
    public BPMClientUtils BPMClientUtils() {
	return new BPMClientUtils(BPMConfig());
    }

    @Bean
    public GitBlitClientUtils GitBlitClientUtils() {
	return new GitBlitClientUtils(GITConfig());
    }

    @Bean
    public MailMessageFactory MailMessageFactory() {
	return new MailMessageFactory();
    }

    @Bean(name = "mailQueue")
    @Qualifier("mailQueue")
    public BlockingQueue<MailMessage> mailQueue() {
	return new LinkedBlockingQueue<>();
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
	ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
	connectionFactory.setBrokerURL(WFConfig().getTosJMSUrl());
	connectionFactory.setUserName(WFConfig().getServiceUserID());
	connectionFactory.setPassword(WFConfig().getServicePassword());
	return connectionFactory;
    }

    @Bean
    public SingleConnectionFactory singleConnectionFactory() {
	return new SingleConnectionFactory(connectionFactory());
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

    @Bean
    public AuditConfig AuditConfig() {
	return new AuditConfig();
    }

    @Bean
    public WFProxyConfig WFProxyConfig() {
	return new WFProxyConfig();
    }

    @Bean
    public SnowPRConfig SnowPRConfig() {
	return new SnowPRConfig();
    }

    @Bean
    public SnowPRClientUtils SnowPRClientUtils() {
	return new SnowPRClientUtils(SnowPRConfig());
    }

    @Bean
    public TaskExecutor threadPoolTaskExecutor() {
	ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	executor.setCorePoolSize(8);
	executor.setMaxPoolSize(16);
	executor.setThreadNamePrefix("default_task_executor_thread");
	executor.initialize();
	return executor;
    }

    // @Bean
    // public TdxShellExecutor checkinExecutor() {
    // return new TdxShellExecutor();
    // }
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public TdxShellExecutor todos() {
	return new TdxShellExecutor();
    }
}
