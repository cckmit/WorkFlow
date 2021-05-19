/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workflow.ssh;

import com.tsi.workflow.AuthModel;
import com.tsi.workflow.ExecModel;
import com.tsi.workflow.utils.JSONResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 *
 * @author USER
 */
public interface SSHAPI {

    @Headers({ "Content-type: application/json" })
    @POST("tdbox/authenticate")
    Call<JSONResponse> authenticate(@Body AuthModel pModel);

    @Headers({ "Content-type: application/json" })
    @POST("tdbox/execute")
    Call<JSONResponse> execute(@Body ExecModel pCommand);

}
