package com.tsi.workflow.helper;

import com.tsi.workflow.config.AppConfig;
import com.tsi.workflow.recon.git.GitBranchSearchResult;
import com.tsi.workflow.recon.git.GitMetaResult;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.TreeWalk;

public class GitHelper {

    private static final Logger LOG = Logger.getLogger(GitHelper.class.getName());

    AppConfig gitConfig;

    // To-Do Remove Below Log Message format(4,5,6) when Arul migrate the commit
    // message with SourceRef from SourcRef
    public Pattern LOG_MESSAGE_FORMAT1 = Pattern.compile("Date:(\\d{12}),\\s*Type:\\w+,\\s*PlanID:([\\w\\.\\-]+),\\s*PlanOwner:[\\w\\.]+,\\s*SourceRef:\\w+,\\s*Status:(\\w+)");
    public Pattern LOG_MESSAGE_FORMAT2 = Pattern.compile("Date:(\\d{8}),\\s*Type:\\w+,\\s*PlanID:([\\w\\.\\-]+),\\s*PlanOwner:[\\w\\.]+,\\s*SourceRef:\\w+,\\s*Status:(\\w+)");
    public Pattern LOG_MESSAGE_FORMAT3 = Pattern.compile("Date:(\\d{14}),\\s*Type:\\w+,\\s*PlanID:([\\w\\.\\-]+),\\s*PlanOwner:[\\w\\.]+,\\s*SourceRef:\\w+,\\s*Status:(\\w+)");
    public Pattern LOG_MESSAGE_FORMAT4 = Pattern.compile("Date:(\\d{12}),\\s*Type:\\w+,\\s*PlanID:([\\w\\.\\-]+),\\s*PlanOwner:[\\w\\.]+,\\s*SourcRef:\\w+,\\s*Status:(\\w+)");
    public Pattern LOG_MESSAGE_FORMAT5 = Pattern.compile("Date:(\\d{8}),\\s*Type:\\w+,\\s*PlanID:([\\w\\.\\-]+),\\s*PlanOwner:[\\w\\.]+,\\s*SourcRef:\\w+,\\s*Status:(\\w+)");
    public Pattern LOG_MESSAGE_FORMAT6 = Pattern.compile("Date:(\\d{14}),\\s*Type:\\w+,\\s*PlanID:([\\w\\.\\-]+),\\s*PlanOwner:[\\w\\.]+,\\s*SourcRef:\\w+,\\s*Status:(\\w+)");

    Map<String, String> lReposMap = new HashMap<>();

    public static final ThreadLocal<DateFormat> JGIT_COMMENT_DATEFORMAT_1 = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat("yyyyMMddHHmm");
	}
    };
    public static final ThreadLocal<DateFormat> JGIT_COMMENT_DATEFORMAT_2 = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat("yyyyMMdd");
	}
    };
    public static final ThreadLocal<DateFormat> JGIT_COMMENT_DATEFORMAT_3 = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat("yyyyMMddHHmmss");
	}
    };
    public static final ThreadLocal<DateFormat> JGIT_COMMENT_DATEFORMAT_4 = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat("yyyyMMddHHmm");
	}
    };
    public static final ThreadLocal<DateFormat> JGIT_COMMENT_DATEFORMAT_5 = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat("yyyyMMdd");
	}
    };
    public static final ThreadLocal<DateFormat> JGIT_COMMENT_DATEFORMAT_6 = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
	    return new SimpleDateFormat("yyyyMMddHHmmss");
	}
    };

    public GitHelper() {
	gitConfig = AppConfig.getInstance();
    }

    public void addOrUpdateGitBrachSearchList(GitMetaResult lFile, Map<String, List> lRedefinedTags, Git lGit, List<GitBranchSearchResult> addGitSearchRes, RevCommit lCommitLogs, List<GitBranchSearchResult> updateSearchRes) throws Exception {
	boolean continueProcess = true;
	// PRCP: we can move this check 1 earlier
	GitBranchSearchResult lBranchSearchResult = new GitBranchSearchResult();
	lBranchSearchResult.setFileId(lFile.getId());
	lBranchSearchResult.setSubRepoId(lFile.getSubRepoId());

	List lTags = lRedefinedTags.get(lCommitLogs.getId().name());
	if (lTags == null || lTags.isEmpty()) {
	    LOG.info(lFile.getId() + "|" + lFile.getProgramName() + " doesn't have tags");
	    continueProcess = false;
	} else {
	    if (lTags.get(0).toString().startsWith("online")) {
		lBranchSearchResult.setRefStatus("Online");
	    } else if (lTags.get(0).toString().startsWith("fallback")) {
		lBranchSearchResult.setRefStatus("Fallback");
	    } else if (lTags.get(0).toString().startsWith("delete")) {
		lBranchSearchResult.setRefStatus("Deleted");
	    } else if (lTags.get(0).toString().startsWith("pending")) {
		lBranchSearchResult.setRefStatus("Pending");
	    } else if (lTags.get(0).toString().startsWith("reject")) {
		lBranchSearchResult.setRefStatus("Rejected");
	    } else if (lTags.get(0).toString().startsWith("newfile")) {
		lBranchSearchResult.setRefStatus("New File");
	    } else {
		LOG.info(lFile.getId() + "|" + lFile.getProgramName() + " doesn't have tags");
		continueProcess = false;
	    }
	}

	if (continueProcess && !lBranchSearchResult.getRefStatus().equals("Deleted")) {
	    RevTree tree = lCommitLogs.getTree();
	    TreeWalk treeWalk = new TreeWalk(lGit.getRepository());
	    treeWalk.addTree(tree);
	    treeWalk.setRecursive(Boolean.TRUE);
	    boolean gotHistory = false;
	    while (treeWalk.next()) {
		if (treeWalk.getPathString().toLowerCase().equals(lFile.getFileName().toLowerCase())) {
		    lBranchSearchResult.setFileHashCode(treeWalk.getObjectId(0).name());
		    gotHistory = true;
		    break;
		}
	    }
	    if (!gotHistory) {
		continueProcess = false;
	    }
	}
	if (continueProcess) {
	    Matcher matcher1 = LOG_MESSAGE_FORMAT1.matcher(lCommitLogs.getShortMessage().replaceAll("\\{", "").replaceAll("\\}", "").trim());
	    Matcher matcher2 = LOG_MESSAGE_FORMAT2.matcher(lCommitLogs.getShortMessage().replaceAll("\\{", "").replaceAll("\\}", "").trim());
	    Matcher matcher3 = LOG_MESSAGE_FORMAT3.matcher(lCommitLogs.getShortMessage().replaceAll("\\{", "").replaceAll("\\}", "").trim());
	    Matcher matcher4 = LOG_MESSAGE_FORMAT4.matcher(lCommitLogs.getShortMessage().replaceAll("\\{", "").replaceAll("\\}", "").trim());
	    Matcher matcher5 = LOG_MESSAGE_FORMAT5.matcher(lCommitLogs.getShortMessage().replaceAll("\\{", "").replaceAll("\\}", "").trim());
	    Matcher matcher6 = LOG_MESSAGE_FORMAT6.matcher(lCommitLogs.getShortMessage().replaceAll("\\{", "").replaceAll("\\}", "").trim());

	    Date lLoadDate = null;
	    String lRefPlan = "";
	    if (matcher1.matches()) {
		lLoadDate = JGIT_COMMENT_DATEFORMAT_1.get().parse(matcher1.group(1));
		lRefPlan = matcher1.group(2);
	    } else if (matcher2.matches()) {
		lLoadDate = JGIT_COMMENT_DATEFORMAT_2.get().parse(matcher2.group(1));
		lRefPlan = matcher2.group(2);
	    } else if (matcher3.matches()) {
		lLoadDate = JGIT_COMMENT_DATEFORMAT_3.get().parse(matcher3.group(1));
		lRefPlan = matcher3.group(2);
	    } else if (matcher4.matches()) {
		lLoadDate = JGIT_COMMENT_DATEFORMAT_4.get().parse(matcher4.group(1));
		lRefPlan = matcher4.group(2);
	    } else if (matcher5.matches()) {
		lLoadDate = JGIT_COMMENT_DATEFORMAT_5.get().parse(matcher5.group(1));
		lRefPlan = matcher5.group(2);
	    } else if (matcher6.matches()) {
		lLoadDate = JGIT_COMMENT_DATEFORMAT_6.get().parse(matcher6.group(1));
		lRefPlan = matcher6.group(2);
	    } else {
		continueProcess = false;
	    }

	    if (continueProcess) {
		lBranchSearchResult.setRefPlan(lRefPlan.trim());
		lBranchSearchResult.setRefLoadDate(lLoadDate);
		// lBranchSearchResult.setFileType(lGit.getRepository().getDirectory().getName().contains(File.separator
		// + "ibm" + File.separator) ? "IBM" : "NON_IBM");
		lBranchSearchResult.setCommitId(lCommitLogs.getId().name());
		lBranchSearchResult.setTargetSystem(lFile.getBranch().replace(Constants.R_HEADS, ""));
		lBranchSearchResult.setCommitterName(lCommitLogs.getCommitterIdent().getName());
		lBranchSearchResult.setCommitterMailId(lCommitLogs.getCommitterIdent().getEmailAddress());
		lBranchSearchResult.setCommitDateTime(new Date(lCommitLogs.getCommitTime() * 1000L));
		// PRCP: why this logic here
		if (updateSearchRes != null) {
		    updateSearchRes.add(lBranchSearchResult);
		} else {
		    addGitSearchRes.add(lBranchSearchResult);
		}
	    }
	}
    }
}
