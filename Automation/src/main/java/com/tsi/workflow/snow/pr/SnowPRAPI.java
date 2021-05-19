/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.snow.pr;

import com.tsi.workflow.snow.pr.model.PRDetails;
import com.tsi.workflow.snow.pr.model.PRResponse;
import com.tsi.workflow.snow.pr.model.PRUpdate;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 *
 * @author Radha.Adhimoolam
 */
public interface SnowPRAPI {

    // @Headers({ "Content-type: application/json" })
    @GET("now/table/problem")
    Call<PRResponse> getPRNumberInfo(@Query("sysparm_query") String prnumber, @Query("sysparm_display_value") String sysparmDisplayValue, @Query("sysparm_fields") String sysparmFields);

    @Headers({ "Content-type: application/json" })
    @POST("now/table/u_dev_tool_chain")
    Call<PRUpdate> updatePRNumber(@Body PRDetails request);

}
