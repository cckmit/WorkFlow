/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.git;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 *
 * @author USER
 */
public interface JGitAPI {

    // ZTPM-1749
    // - Change GET to POST call
    // @Headers({"Content-type: application/json"})
    // @GET("jgit/SearchAllRepos")
    // public Call<Collection<GitSearchResult>> SearchAllRepos(@Query("pCompany")
    // String pCompany, @Query("pFileFilter") String pFileFilter,
    // @Query("pMacroHeader") Boolean pMacroHeader, @Query("pStatusReq") Integer
    // StatusReq,
    // @Query("pReposAllowed") Collection<String> pReposAllowed);

    @Headers({ "Content-type: application/json" })
    @POST("jgit/SearchAllRepos")
    public Call<Collection<GitSearchResult>> SearchAllRepos(@Query("pCompany") String pCompany, @Query("pFileFilter") String pFileFilter, @Query("pMacroHeader") Boolean pMacroHeader, @Query("pStatusReq") Integer StatusReq, @Body Collection<String> pReposAllowed);

    // @Headers({"Content-type: application/json"})
    // @GET("jgit/getMakFileDetail")
    // public Call<Collection<GitBaseMetaResult>> getMakFileDetail(@Query("pUrl")
    // String pUrl, @Query("pCommit") String pCommit, @Query("pFileName") String
    // pFileName);
    @Headers({ "Content-type: application/json" })
    @GET("jgit/getPlanRepoName")
    public Call<String> getPlanRepoName(@Query("nickName") String nickName, @Query("pPlanId") String pPlanId);

    @Headers({ "Content-type: application/json" })
    @GET("jgit/getPlanRepoFullName")
    public Call<String> getPlanRepoFullName(@Query("nickName") String nickName, @Query("pPlanId") String pPlanId);

    @Headers({ "Content-type: application/json" })
    @GET("jgit/getPlanLFSRepoFullName")
    public Call<String> getPlanLFSRepoFullName(@Query("nickName") String nickName, @Query("pPlanId") String pPlanId);

    @Headers({ "Content-type: application/json" })
    @GET("jgit/getAllBranchList")
    public Call<List<String>> getAllBranchList(@Query("nickName") String nickName, @Query("pPlanId") String pPlanId);

    @Headers({ "Content-type: application/json" })
    @GET("jgit/createBranches")
    public Call<Map<String, Boolean>> createBranches(@Query("id") String id, @Query("lBranchList") Set<String> lBranchList, @Query("lNickName") String lNickName);

    @Headers({ "Content-type: application/json" })
    @GET("jgit/getPlanSSHURL")
    public Call<String> getPlanSSHURL(@Query("nickName") String nickName, @Query("pPlanid") String pPlanid, @Query("pUserId") String pUserId);

    @Headers({ "Content-type: application/json" })
    @GET("jgit/getProductionRepoList")
    public Call<List<String>> getProductionRepoList(@Query("nickName") String nickName);

    @Headers({ "Content-type: application/json" })
    @GET("jgit/isRepositoryExist")
    public Call<Boolean> isRepositoryExist(@Query("scmUrl") String scmUrl);

    @Headers({ "Content-type: application/json" })
    @GET("jgit/isBranchExistInRepository")
    public Call<Boolean> isBranchExistInRepository(@Query("scmUrl") String scmUrl, @Query("branchName") String branchName);

    @Headers({ "Content-type: application/json" })
    @GET("jgit/deleteProdBranches")
    public Call<Boolean> deleteProdBranches(@Query("companyName") String companyName, @Query("branchList") List<String> branchList);

    @Headers({ "Content-type: application/json" })
    @GET("jgit/getProdRepoBranches")
    public Call<List<String>> getProdRepoBranches(@Query("repoName") String repoName);

    @Headers({ "Content-type: application/json" })
    @GET("jgit/deletePlansImpBranches")
    public Call<Boolean> deletePlansImpBranches(@Query("planId") String planId, @Query("impId") String impId, @Query("companyName") String companyName, @Query("branchList") Set<String> branchList);

}
