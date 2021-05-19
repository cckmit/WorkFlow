/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.bpm;

import com.tsi.workflow.bpm.model.BPMProcess;
import com.tsi.workflow.bpm.model.GroupVariable;
import com.tsi.workflow.bpm.model.TaskReponse;
import com.tsi.workflow.bpm.model.TaskVariable;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 *
 * @author USER
 */
public interface ActivitiBPMAPI {

    @Headers({ "Content-type: application/json" })
    @POST("service/runtime/process-instances")
    Call<BPMProcess> createProcess(@Body BPMProcess request);

    @Headers({ "Content-type: application/json" })
    @POST("service/runtime/tasks/{TaskId}")
    Call<Void> assignAndUnassignTask(@Body BPMProcess request, @Path("TaskId") String taskId);

    @Headers({ "Content-type: application/json" })
    @POST("service/runtime/tasks/{TaskId}/identitylinks")
    Call<BPMProcess> assignTaskToGroup(@Body GroupVariable request, @Path("TaskId") String taskId);

    @Headers({ "Content-type: application/json" })
    @PUT("service/runtime/tasks/{TaskId}/variables")
    Call<List<TaskVariable>> updateTaskVariables(@Body List<TaskVariable> request, @Path("TaskId") String taskId);

    @Headers({ "Content-type: application/json" })
    @PUT("service/runtime/process-instances/{ProcessId}/variables")
    Call<List<TaskVariable>> updateProcessVariables(@Body List<TaskVariable> request, @Path("ProcessId") String ProcessId);

    @Headers({ "Content-type: application/json" })
    @GET("service/runtime/tasks")
    Call<TaskReponse> getGroupTaskList(@Query("includeTaskLocalVariables") String localVars, @Query("candidateGroup") String groupId);

    @Headers({ "Content-type: application/json" })
    @GET("service/runtime/tasks")
    Call<TaskReponse> getUserTaskList(@Query("includeTaskLocalVariables") boolean localVars, @Query("assignee") String userId, @Query("start") Integer offset, @Query("size") Integer limit, @Query("sort") String sortKey, @Query("order") String sortType);

    @GET("service/runtime/tasks")
    Call<TaskReponse> getUserTaskDetail(@Query("processInstanceId") String processId);

    @GET("service/runtime/process-instances/{processInstanceId}")
    Call<BPMProcess> getProcessDetails(@Path("processInstanceId") String ProcessId);

}
