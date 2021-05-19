package com.tsi.workflow.git;

import com.tsi.workflow.utils.Constants;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface IJGit {

    // Search All Repos
    public Collection<GitSearchResult> SearchAllRepos(String pCompany, String pFileFilter, Boolean pMacroHeader, Constants.PRODSearchType pPendingStatusReq, Collection<String> pAllowedRepos) throws IOException;

    // Plan Repo Name
    public String getPlanRepoName(String nickName, String pPlanId) throws IOException;

    // Plan repo full name
    public String getPlanRepoFullName(String nickName, String pPlanId) throws IOException;

    // Plan LFS repo full name
    public String getPlanLFSRepoFullName(String nickName, String pPlanId) throws IOException;

    // All Branch list
    public List<String> getAllBranchList(String nickName, String pPlanId) throws IOException;

    // Prod repo list
    public List<String> getProductionRepoList(String nickName) throws IOException;

    // repo exist
    public Boolean isRepositoryExist(String scmUrl);

    // Branch exist in repo
    public Boolean isBranchExistInRepository(String scmUrl, String branchName) throws IOException;

    // Prod repo branches
    public List<String> getProdRepoBranches(String pRepoName) throws IOException;

}
