/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.gitblit;

import com.tsi.workflow.gitblit.model.Repository;
import com.tsi.workflow.gitblit.model.RepositoryPermission;
import com.tsi.workflow.gitblit.model.UserModel;
import java.util.List;
import java.util.Map;
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
public interface GitBlitAPI {

    @Headers({ "Content-type: application/json" })
    @POST("./")
    Call<Void> createRepository(@Body Repository request, @Query("req") String requestType);

    @Headers({ "Content-type: application/json" })
    @POST("./")
    Call<Void> setRepositoryMemberPermission(@Body List<RepositoryPermission> request, @Query("req") String requestType, @Query("name") String repositoryName);

    @Headers({ "Content-type: application/json" })
    @POST("./")
    Call<Map<String, Repository>> listRepositories(@Query("req") String requestType);

    @Headers({ "Content-type: application/json" })
    @POST("./")
    Call<Void> editRepository(@Body Repository request, @Query("req") String requestType, @Query("RepositoryName") String repositoryName);

    @Headers({ "Content-type: application/json" })
    @POST("./")
    Call<Void> deleteRepository(@Body Repository request, @Query("req") String requestType);

    @Headers({ "Content-type: application/json" })
    @GET("./")
    Call<List<RepositoryPermission>> listRepositoryUserList(@Query("name") String repositoryName, @Query("req") String requestType);

    @Headers({ "Content-type: application/json" })
    @GET("./")
    Call<Void> refreshIndex(@Query("name") String repositoryName, @Query("req") String requestType);

    @Headers({ "Content-type: application/json" })
    @GET("./")
    Call<List<UserModel>> getUsersList(@Query("req") String requestType);

    @Headers({ "Content-type: application/json" })
    @GET("./")
    Call<UserModel> getUser(@Query("req") String requestType, @Query("name") String userName);

    @Headers({ "Content-type: application/json" })
    @POST("./")
    Call<Map<String, List<String>>> listRepositoryBranches(@Query("req") String requestType);

}
