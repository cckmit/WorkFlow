package com.tsi.workflow.service;

import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.GitSearchDAO;
import com.tsi.workflow.utils.JSONResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GitHookUpdateService {

    private static final Logger LOG = Logger.getLogger(GitHookUpdateService.class.getName());
    @Autowired
    CacheClient cacheClient;

    @Autowired
    GitSearchDAO gitSearchDAO;

    @Autowired
    LDAPAuthenticatorImpl lDAPAuthenticatorImpl;

    public GitSearchDAO getGitSearchDAO() {
	return gitSearchDAO;
    }

    public CacheClient getCacheClient() {
	return cacheClient;
    }

    public JSONResponse gitDbUpdateProcess(String refPlan, String refStatus, String refLoadDateTime, String sourceCommitId, String sourceCommitDate, String derivedCommitId, String derivedCommitDate, String sourceRepo, String fileName, String targetSystem, boolean isModify) throws Exception {
	JSONResponse lResponse = new JSONResponse();
	if (refStatus.equals("Newfile")) {
	    refStatus = "New File";
	}
	LOG.info("Input messages from shell: refPlan:" + refPlan + " refStatus: " + refStatus + " refLoadDateTime: " + refLoadDateTime + " sourceCommitId: " + sourceCommitId + " sourceCommitDate: " + sourceCommitDate + " derivedCommitId:" + derivedCommitId + " sourceRepo: " + sourceRepo + " fileName: " + fileName + " targetSystem: " + targetSystem + " isModify: " + isModify);
	String committerName = lDAPAuthenticatorImpl.getServiceUser().getDisplayName();
	String committerMailId = lDAPAuthenticatorImpl.getServiceUser().getMailId();

	if (refPlan != null) {
	    refPlan = refPlan.toLowerCase();
	}
	List<String> fileNames = new ArrayList<>();
	HashMap<String, String> fileNameAndHashCode = new HashMap<>();

	if (fileName != null && !fileName.isEmpty()) {
	    String[] fileNameList = fileName.split(",");

	    if (fileNameList.length > 0 && fileNameList.length % 2 == 0) {
		int count = 1;
		for (int i = 0; i < fileNameList.length; i++) {
		    if (count % 2 == 0) {
			fileNames.add(fileNameList[i]);
			fileNameAndHashCode.put(fileNameList[i], fileNameList[i - 1]);
		    }
		    count++;
		}
	    } else {
		LOG.error("File name and file hash code details are missing in the following filename: " + fileName);
	    }
	}

	Date refLoadDate = null;
	if (refLoadDateTime != null) {
	    refLoadDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(refLoadDateTime);
	}
	Date srcCommitDate = null;
	if (sourceCommitDate != null) {
	    srcCommitDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(sourceCommitDate);
	}
	// Date derCommitDate = new
	// SimpleDateFormat(Constants.JENKINS_DATEFORMAT.toString()).parse(derivedCommitDate);

	// Get Sub repo Id
	int subRepoId = 0;
	subRepoId = getGitSearchDAO().getSubRepoDetail(sourceRepo);
	LOG.info("Sub Repo id: " + subRepoId);
	if (subRepoId == 0) {
	    LOG.error("Sub Repo id not found for sub Repo Name: " + sourceRepo);
	}

	for (String file : fileNames) {
	    // Get File id
	    int fileId = getGitSearchDAO().getFileDetail(sourceRepo, file, targetSystem);
	    LOG.info("File Detail before: " + fileId);

	    if (fileId > 0 && refStatus.equals("Deleted")) {
		getGitSearchDAO().updateFileListDeleteFlag(fileId, true);
	    } else if (fileId == 0 && refStatus.equals("Deleted")) {
		LOG.error("File Id isn't available. But ref status is deleted for the file : " + file + " Soruce repo: " + sourceRepo + " Target System: " + targetSystem);
		continue;
	    }

	    if (fileId == 0) {
		getGitSearchDAO().saveNewFileList(subRepoId, file, targetSystem);
		fileId = getGitSearchDAO().getFileDetail(sourceRepo, file, targetSystem);
	    }

	    int reppoCommitId = getGitSearchDAO().getCommitDetailId(fileId, sourceCommitId);
	    LOG.info("File Detail after: " + fileId + " repo commit id: " + reppoCommitId + " Is modified : " + isModify);

	    if (reppoCommitId > 0 && isModify) {
		LOG.info("Inside the update commit processs");
		getGitSearchDAO().updateCommitDetail(fileId, sourceCommitId, committerName, committerMailId, srcCommitDate, refPlan, refStatus, refLoadDate, fileNameAndHashCode.get(file), derivedCommitId, reppoCommitId);
	    } else {
		getGitSearchDAO().saveCommitDetail(subRepoId, fileId, sourceCommitId, committerName, committerMailId, srcCommitDate, refPlan, refStatus, refLoadDate, fileNameAndHashCode.get(file));

		// ZTPFM-2441 Code changes to update Cache if new records were added in
		// Git.repo_commit table
		if (getCacheClient().isReconRunning()) {
		    getCacheClient().getReconAllCommitNames().add(subRepoId + "|" + sourceCommitId);
		}
	    }
	}

	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }
}
