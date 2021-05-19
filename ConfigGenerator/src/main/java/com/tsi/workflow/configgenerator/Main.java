/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.tsi.workflow.configgenerator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.maven.model.Model;
import org.apache.maven.model.Profile;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

/**
 *
 * @author prabhu.prabhakaran
 */
public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    SimpleDateFormat lFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
    CommandLineParser parser = new DefaultParser();
    /**
     * Params
     */
    String lEnvironment;
    String lCompany;
    String lBuildID;
    Boolean lIsEncrypted;
    Boolean lIsDouble;
    String[] lLinuxServers;
    String[] lTOSServers;
    // String[] lZLinuxServers;
    /**
     * Generated
     */
    String lBuildProfileName;
    String lMasterProfileName;
    /**
     * Common Configs
     */
    String lDBConfig;
    String lTOSConfig;
    String lCacheConfig;
    /**
     * Others
     */
    Map<String, Profile> lProfiles;
    static Set<String> lEnvProps = new TreeSet<>();

    int Server1LocalPort = 8446;
    int Server2LocalPort = 8446;

    int Server1PublicPort = 8443;
    int Server2PublicPort = 8443;

    public static void main(String[] args) {
	BasicConfigurator.configure();
	LOG.info("Config Generation Started");
	Main lMain = new Main();
	if (!lMain.parseAndValidate(args)) {
	    LOG.error("Invalid Arguments.");
	    HelpFormatter formatter = new HelpFormatter();
	    formatter.printHelp("Invalid Arguments : ", getOptions());
	    System.exit(1);
	}
	LOG.info("Arguments Parsing Done");

	if (!lMain.readPomFile()) {
	    LOG.error("Error in Reading POM File");
	    System.exit(1);
	}
	LOG.info("POM file reading Done");

	populateProps();

	if (!lMain.generateConfigs()) {
	    LOG.error("Error in Writing Properties File");
	    System.exit(1);
	}
    }

    private static void populateProps() {
	lEnvProps.add("db.app.username");
	lEnvProps.add("db.app.password");
	lEnvProps.add("db.ticket.username");
	lEnvProps.add("db.ticket.password");
	lEnvProps.add("db.csr.username");
	lEnvProps.add("db.csr.password");
	lEnvProps.add("db.vpar.username");
	lEnvProps.add("db.vpar.password");
	lEnvProps.add("service.username");
	lEnvProps.add("service.password");
	lEnvProps.add("tos.file.domain");
	lEnvProps.add("tos.file.username");
	lEnvProps.add("tos.file.password");
    }

    public static Options getOptions() {
	Options options = new Options();
	options.addOption("buildId", true, "Jenkins Build Id (1.22_14)");
	options.addOption("company", true, "Company Name TP/DL");
	options.addOption("environment", true, "Environment to Generate Config DEV/QA/PRE_PROD/PROD/DR");
	// options.addOption("LBAddress", true, "LoadBalancer Address");
	options.addOption("linux", true, "List of Linux Servers");
	options.addOption("tos", true, "List of TOS Servers");
	// options.addOption("zlinux", true, "List of ZLinux Servers");
	options.addOption("encrypted", true, "if encryptrd set true");
	options.addOption("tomcattype", true, "Single/Double");
	return options;
    }

    public boolean parseAndValidate(String[] args) {
	try {
	    CommandLine cmd = parser.parse(getOptions(), args);

	    if (!cmd.hasOption("environment")) {
		LOG.error("No Environment in Argument");
		return false;
	    }

	    if (!cmd.hasOption("company")) {
		LOG.error("No Company in Argument");
		return false;
	    }

	    if (!cmd.hasOption("linux")) {
		LOG.error("No Linux Servers in Argument");
		return false;
	    }

	    if (!cmd.hasOption("tos")) {
		LOG.error("No TOS Servers in Argument");
		return false;
	    }

	    if (!cmd.hasOption("encrypted")) {
		LOG.error("No Encryption flag in Argument");
		return false;
	    }

	    if (!cmd.hasOption("tomcattype")) {
		LOG.error("No Config Type in Argument");
		return false;
	    }

	    lCompany = cmd.getOptionValue("company");
	    lEnvironment = cmd.getOptionValue("environment");
	    lBuildID = cmd.getOptionValue("buildId");
	    lLinuxServers = cmd.getOptionValue("linux").split("\\,");
	    lTOSServers = cmd.getOptionValue("tos").split("\\,");
	    // lZLinuxServers = cmd.getOptionValue("zlinux").split("\\,");
	    lIsEncrypted = cmd.getOptionValue("encrypted").equalsIgnoreCase("TRUE");
	    lIsDouble = cmd.getOptionValue("tomcattype").equalsIgnoreCase("DUAL");

	    if (lIsDouble) {
		Server2LocalPort = 9446;
		Server2PublicPort = 9443;
	    }

	    lBuildProfileName = lCompany + "_" + (lEnvironment.equals("DR") ? "PROD" : lEnvironment);
	    lMasterProfileName = "MASTER_" + lCompany + "_" + (lEnvironment.equals("DR") ? "PROD" : lEnvironment);
	    // lOutPath = "/opt/delivery/release/WF/" + lBuildProfileName + "/" + lBuildID;

	    LOG.info("Parsed Arguments");
	    LOG.info("Company " + lCompany);
	    LOG.info("Master Profile " + lMasterProfileName);
	    LOG.info("Build Profile " + lBuildProfileName);
	    LOG.info("Environment " + lEnvironment);
	    LOG.info("Build ID " + lBuildID);
	    LOG.info("Linux Servers " + String.join(", ", lLinuxServers));
	    LOG.info("TOS Servers " + String.join(", ", lTOSServers));
	    LOG.info("Encrypted " + lIsEncrypted);
	    LOG.info("Double Server " + lIsDouble);
	    return true;
	} catch (Exception ex) {
	    LOG.error("Error in Parsing Argument", ex);
	}
	return false;
    }

    private boolean readPomFile() {
	try {
	    MavenXpp3Reader reader = new MavenXpp3Reader();
	    FileReader lPOMFileReader = new FileReader("pom.xml");
	    Model model = reader.read(lPOMFileReader);
	    IOUtils.closeQuietly(lPOMFileReader);
	    lProfiles = model.getProfiles().stream().collect(Collectors.toMap(Profile::getId, Function.identity()));
	    return true;
	} catch (Exception ex) {
	    LOG.error("Error in reading POM File", ex);
	}
	return false;
    }

    private boolean generateConfigs() {
	Set<String> lIps = new TreeSet<>();
	LOG.info("Generating Common Configuration for the Servers ");
	for (String lLinuxServer : lLinuxServers) {
	    InetAddress byName;
	    try {
		byName = InetAddress.getByName(lLinuxServer);
		lIps.add(byName.getHostAddress());
	    } catch (Exception ex) {
		LOG.error("Error in parsing IP of " + lLinuxServer, ex);
		return false;
	    }
	}
	lDBConfig = "jdbc:postgresql://" + String.join(":5432,", lIps) + ":5432/workflow?targetServerType=master";
	lTOSConfig = "failover:tcp://" + String.join(":61616,tcp://", lTOSServers) + ":61616";
	lCacheConfig = String.join(",", lLinuxServers);
	LOG.info("Common Configuration for the Servers Done");
	for (int i = 0; i < lLinuxServers.length; i++) {
	    if (!generateConfig(i, lLinuxServers[i])) {
		return false;
	    }
	}
	return true;
    }

    // CRON Change - 0 6 0 * * ?|3,1|2,2
    HashMap<String, String> lCrons = new HashMap<>();

    private boolean generateConfig(int i, String lLinuxServer) {
	try {
	    LOG.info("Generating Configuration for the Server " + lLinuxServer);
	    // String lLinuxServer = lLinuxServers[i];
	    SortedProperties lProperties = new SortedProperties();
	    Profile lMasterProfile = lProfiles.get(lMasterProfileName);
	    lProperties.putAll(lMasterProfile.getProperties());
	    Profile lBuildProfile = lProfiles.get(lBuildProfileName);
	    lProperties.putAll(lBuildProfile.getProperties());
	    for (String lEnvProp : lEnvProps) {
		lProperties.put(lEnvProp, System.getProperties().get(lEnvProp));
	    }
	    for (Map.Entry<Object, Object> entry : lProperties.entrySet()) {
		String key = (String) entry.getKey();
		String value = (String) entry.getValue();
		if (key.startsWith("cron.")) {
		    String[] lCron = null;
		    String lUpdatedValue = lCrons.get(key);
		    if (lUpdatedValue != null) {
			String[] lValues = lUpdatedValue.split("\\|");
			for (int j = 0; j < lValues.length; j++) {
			    if (j == 0) {
				lCron = lValues[j].split("\\s");
			    } else {
				if (i < 0) {
				    String[] lInc = lValues[j].split("\\,");
				    int position = Integer.parseInt(lInc[0]) - 1;
				    lCron[position] = "" + (Integer.parseInt(lCron[position]) + (Integer.parseInt(lInc[1]) * i));
				}
			    }
			}
			lProperties.put(key, String.join(" ", lCron));
		    } else {
			String[] lValues = value.split("\\|");
			lProperties.put(key, lValues[0]);
			lCrons.put(key, value);
		    }
		}
	    }
	    if (i == 0) {
		lProperties.put("isPrimary", "true");
	    } else {
		lProperties.put("isPrimary", "false");
	    }
	    lProperties.put("db.app.url", lDBConfig);
	    lProperties.put("tos.jms.url", lTOSConfig);
	    lProperties.put("wf.cache.sync.host", lCacheConfig);
	    lProperties.put("bpm.rest.api.url", "https://" + lLinuxServer + ":" + Server2LocalPort + "/activiti-rest/");
	    lProperties.put("git.host", lLinuxServer);
	    lProperties.put("gitblit.rest.api.url", "https://" + lLinuxServer + ":" + Server1LocalPort + "/gitblit/rpc/");
	    lProperties.put("jenkins.rest.api.url", "https://" + lLinuxServer + ":" + Server2LocalPort + "/jenkins");
	    lProperties.put("jgit.rest.api.url", "https://" + lLinuxServer + ":" + Server1LocalPort + "/JGitAPI/");
	    lProperties.put("profileName", lCompany + "_" + lEnvironment + "_" + (i + 1));
	    lProperties.put("tos.server.id", lCompany + "_" + lEnvironment + "_TOS_" + (i + 1));
	    lProperties.put("wf.cache.server.host", lLinuxServer);
	    if (lEnvironment.equals("DEV")) {
		lProperties.put("tos.server.id", lEnvironment + "_TOS_" + (i + 1));
		lProperties.put("profileName", lEnvironment + "_" + (i + 1));
		lProperties.put("tos.system.type", "NATIVE");
		lProperties.put("wf.maintenance", "false");
		lProperties.put("wf.allowAllRoles", "true");
	    } else if (lEnvironment.equals("QA")) {
		lProperties.put("tos.server.id", lEnvironment + "_TOS_" + (i + 1));
		lProperties.put("profileName", lEnvironment + "_" + (i + 1));
		lProperties.put("tos.system.type", "NATIVE");
		lProperties.put("wf.maintenance", "false");
		lProperties.put("wf.allowAllRoles", "true");
	    } else if (lEnvironment.equals("PRE_PROD")) {
		lProperties.put("tos.system.type", "NATIVE");
		lProperties.put("wf.maintenance", "false");
		lProperties.put("wf.allowAllRoles", "true");
	    } else if (lEnvironment.equals("PROD")) {
		lProperties.put("tos.system.type", "PRODUCTION");
		lProperties.put("wf.maintenance", "true");
		lProperties.put("wf.allowAllRoles", "false");
	    } else if (lEnvironment.equals("DR")) {
		lProperties.put("wf.cors.env", "DR");
		lProperties.put("tos.system.type", "PRODUCTION");
		lProperties.put("wf.maintenance", "true");
		lProperties.put("wf.allowAllRoles", "false");
	    }
	    File lBaseFolder = new File(lLinuxServer);
	    lBaseFolder.mkdirs();
	    LOG.info("Writing the Property file in " + lBaseFolder.getAbsolutePath() + File.separator + "app.properties");
	    FileWriter fileWriter = new FileWriter(lBaseFolder.getAbsolutePath() + File.separator + "app.properties");
	    if (lIsEncrypted) {
		EncryptionImpl.encryptProperties(lProperties, "TsiWorkflow");
	    } else {
		EncryptionImpl.decryptProperties(lProperties, "TsiWorkflow");
	    }
	    lProperties.store(fileWriter, "zTPF DevOps Toolchain " + lLinuxServer + " Properties" + System.lineSeparator() + "Date : " + lFormat.format(new Date()));
	    IOUtils.closeQuietly(fileWriter);
	    return true;
	} catch (Exception ex) {
	    LOG.error("Error in Generating Config for " + lLinuxServer, ex);
	}
	return false;
    }

}
