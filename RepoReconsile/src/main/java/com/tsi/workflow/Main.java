/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.config.AppConfig;
import com.tsi.workflow.util.MailUtil;
import com.tsi.workflow.utils.FileIOUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author prabhu.prabhakaran
 */
public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static boolean lStop = false;
    static CommandLineParser parser = new DefaultParser();
    public static String lDBSchema = "git";
    public static boolean swapDB = false;
    public static long duration = 0;
    public static List<String> excludedRepos = new ArrayList<>();
    public static List<String> includedRepos = new ArrayList<>();
    public static List<String> duplicateFiles = new ArrayList<>();
    public static List<String> suspiciousFiles = new ArrayList<>();

    public static void main(String args[]) {
	AppConfig.getInstance();
	try {
	    parseOptions(args);
	    if (!CacheClient.getInstance().isAlreadyRunning()) {
		long startTime = System.currentTimeMillis();
		try {

		    LOG.info("Data population started for " + AppConfig.getInstance().getSessionKey() + " Process ID : " + FileIOUtils.getCurrentProcessID());

		    MailUtil.getInstance().sendStartMail();

		    RepoDetailPopulate lRepoDetail = new RepoDetailPopulate();

		    LOG.info("Initiating Cache Clear");
		    lRepoDetail.clearCache();

		    LOG.info("Initiating Schema");
		    lRepoDetail.initSchema(lDBSchema);

		    LOG.info("Populating Cache");
		    lRepoDetail.populateCache();

		    LOG.info("Populating Repositories");
		    lRepoDetail.populateRepositories();

		    LOG.info("Populating FileList");
		    lRepoDetail.populateFiles();

		    LOG.info("Populating Commits");
		    lRepoDetail.populateCommits();

		    if (Main.swapDB) {
			LOG.info("Swapping Database");
			lRepoDetail.swapDatabase();
		    } else {
			LOG.info("Skip Swapping Database");
		    }

		    duration = System.currentTimeMillis() - startTime;
		    LOG.info("Repo Data population completed sucessfully in " + DurationFormatUtils.formatDuration(duration, "HH:mm:ss:SS", true));

		    MailUtil.getInstance().sendCompletionMail();
		}

		catch (Exception ex) {
		    LOG.error("Error in main", ex);
		} finally {
		    CacheClient.getInstance().unlock();
		}

	    }
	} catch (Exception e) {
	    LOG.error("Error in main", e);
	} finally {
	    LOG.info("Data population Closing for " + AppConfig.getInstance().getSessionKey() + " Process ID : " + FileIOUtils.getCurrentProcessID());
	    CacheClient.getInstance().clearAllCache();
	    // CacheClient.getInstance().unlock();
	    CacheClient.getInstance().shutdown();
	    System.exit(0);
	}

    }

    public static Options getOptions() {
	Options options = new Options();
	options.addOption("excludeRepo", true, "To exclude any repositories for reconsile (,) separated (Eg: tpf/tp/ibm/ibm_put13b.git)");
	options.addOption("includeRepo", true, "To reconsile a specific repository (,) separated  (Eg: tpf/tp/ibm/ibm_put13b.git)");
	options.addOption("database", true, "populate to different database");
	options.addOption("swapDB", true, "swap to GIT DB at the end of population");
	return options;
    }

    private static void parseOptions(String[] args) throws ParseException {
	CommandLine cmd = parser.parse(getOptions(), args);

	if (cmd.hasOption("excludeRepo")) {
	    if (cmd.hasOption("includeRepo")) {
		LOG.error("excludeRepo and includeRepo should not used at same time");
		return;
	    }
	    excludedRepos = Arrays.asList(StringUtils.split(cmd.getOptionValue("excludeRepo"), ","));
	    LOG.info("Excluded Repos : " + excludedRepos);
	}

	if (cmd.hasOption("includeRepo")) {
	    if (cmd.hasOption("excludeRepo")) {
		LOG.error("excludeRepo and includeRepo should not used at same time");
		return;
	    }
	    includedRepos = Arrays.asList(StringUtils.split(cmd.getOptionValue("includeRepo"), ","));
	    LOG.info("Included Repos : " + includedRepos);
	}

	if (cmd.hasOption("database")) {
	    lDBSchema = cmd.getOptionValue("database").toLowerCase();
	    LOG.info("DB Schema : " + lDBSchema);
	}

	if (cmd.hasOption("swapDB")) {
	    swapDB = Boolean.parseBoolean(cmd.getOptionValue("swapDB").toLowerCase());
	    LOG.info("Swap Schema : " + swapDB);
	}
    }

}
