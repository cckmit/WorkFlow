package com.tsi.workflow.bpm;

import com.tsi.workflow.User;
import com.tsi.workflow.bpm.model.BPMProcess;
import com.tsi.workflow.bpm.model.GroupVariable;
import com.tsi.workflow.bpm.model.TaskReponse;
import com.tsi.workflow.bpm.model.TaskRepresentation;
import com.tsi.workflow.bpm.model.TaskVariable;
import com.tsi.workflow.interfaces.IBPMConfig;
import com.tsi.workflow.utils.Constants;
import java.io.IOException;
import java.util.List;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import retrofit2.Call;
import retrofit2.Response;

/**
 *
 * @author User
 */
public class BPMClientUtils {

    private static final Logger LOG = Logger.getLogger(BPMClientUtils.class.getName());

    IBPMConfig bPMConfig;

    public BPMClientUtils(IBPMConfig bPMConfig) {
	this.bPMConfig = bPMConfig;
    }

    public String createADLProcess(User pUser) throws IOException {
	BPMClient lClient = new BPMClient.Builder().connect(bPMConfig.getBpmRestUrl(), pUser.getId(), pUser.getDecryptedPassword()).build();
	BPMProcess bpmProcess = new BPMProcess();
	bpmProcess.setProcessDefinitionKey(bPMConfig.getAdlProcessKey());
	Call<BPMProcess> lCreateProcess = lClient.getActivitiBPMAPI().createProcess(bpmProcess);
	Response<BPMProcess> lResponse = lCreateProcess.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    LOG.info("BPM: Created ADL Process ID: " + lResponse.body().getId());
	    return lResponse.body().getId();
	}
	return "";
    }

    public String createDeveloperProcess(User pUser, String implementationId) throws IOException {
	BPMClient lClient = new BPMClient.Builder().connect(bPMConfig.getBpmRestUrl(), pUser.getId(), pUser.getDecryptedPassword()).build();

	BPMProcess bpmProcess = new BPMProcess();
	bpmProcess.setProcessDefinitionKey(bPMConfig.getDlProcessKey());
	bpmProcess.addTaskVariable(Constants.BPM_IMPLEMENTATION_ID, implementationId);
	Call<BPMProcess> lCreateProcess = lClient.getActivitiBPMAPI().createProcess(bpmProcess);
	Response<BPMProcess> lResponse = lCreateProcess.execute();

	if (lResponse.code() == HttpStatus.SC_OK || lResponse.code() == HttpStatus.SC_CREATED) {
	    LOG.info("BPM: Created Developer Proccess ID: " + lResponse.body().getId());
	    return lResponse.body().getId();
	}
	return "";
    }

    public Boolean removeUserFromTask(User pUser, String processId) throws IOException {

	BPMClient lClient = new BPMClient.Builder().connect(bPMConfig.getBpmRestUrl(), pUser.getId(), pUser.getDecryptedPassword()).build();
	Call<TaskReponse> lProcessDetails = lClient.getActivitiBPMAPI().getUserTaskDetail(processId);
	Response<TaskReponse> lProcessResponse = lProcessDetails.execute();
	if (lProcessResponse.code() != HttpStatus.SC_OK) {
	    LOG.error("BPM: Process Info not found for the Process ID: " + processId);
	    return false;
	}
	TaskReponse lProcessInfo = lProcessResponse.body();

	if (lProcessInfo == null || lProcessInfo.getData() == null || lProcessInfo.getData().isEmpty()) {
	    LOG.error("BPM: Process Info not found for the Process ID: " + processId);
	    return false;
	}
	TaskRepresentation lProcess = lProcessInfo.getData().get(0);
	LOG.info("BPM: Getting Task ID:" + lProcess.getId() + " is mapped with  Process ID: " + processId);
	BPMProcess lBPMProcess = new BPMProcess();
	lBPMProcess.setAction(Constants.BPM_CLAIM);
	lBPMProcess.setAssignee(null);
	Call<Void> lAssignAndUnassignTask = lClient.getActivitiBPMAPI().assignAndUnassignTask(lBPMProcess, lProcess.getId());
	Response<Void> lResponse = lAssignAndUnassignTask.execute();
	boolean lReturn = lResponse.code() == HttpStatus.SC_OK || lResponse.code() == HttpStatus.SC_CREATED;
	if (lReturn) {
	    LOG.info("BPM: Task ID:" + lProcess.getId() + " Process ID: " + processId + " is removed from user successfully");
	} else {
	    LOG.error("BPM: Task ID:" + lProcess.getId() + " Process ID: " + processId + ", Error in removing user ");
	}
	return lReturn;
    }

    public Boolean assignTask(User pUser, String pProcessId, String pAssigneeId, List<TaskVariable> lVars) throws IOException {

	BPMClient lClient = new BPMClient.Builder().connect(bPMConfig.getBpmRestUrl(), pUser.getId(), pUser.getDecryptedPassword()).build();
	Call<TaskReponse> lProcessDetails = lClient.getActivitiBPMAPI().getUserTaskDetail(pProcessId);
	Response<TaskReponse> lProcessResponse = lProcessDetails.execute();
	if (lProcessResponse.code() != HttpStatus.SC_OK) {
	    LOG.error("BPM: Process Info not found for the Process ID: " + pProcessId);
	    return false;
	}
	TaskReponse lProcessInfo = lProcessResponse.body();

	if (lProcessInfo == null || lProcessInfo.getData() == null || lProcessInfo.getData().isEmpty()) {
	    LOG.error("BPM: Process Info not found for the Process ID: " + pProcessId);
	    return false;
	}
	TaskRepresentation lProcess = lProcessInfo.getData().get(0);
	LOG.info("BPM: Getting Task ID:" + lProcess.getId() + " is mapped with  Process ID: " + pProcessId);
	BPMProcess lBPMProcess = new BPMProcess();
	lBPMProcess.setAction(Constants.BPM_CLAIM);
	lBPMProcess.setAssignee(pAssigneeId);
	Call<Void> lAssignAndUnassignTask = lClient.getActivitiBPMAPI().assignAndUnassignTask(lBPMProcess, lProcess.getId());
	Response<Void> lResponse = lAssignAndUnassignTask.execute();
	if ((lResponse.code() == HttpStatus.SC_OK || lResponse.code() == HttpStatus.SC_CREATED)) {
	    LOG.info("BPM: Task ID:" + lProcess.getId() + " Process ID: " + pProcessId + " is assigned to user " + pAssigneeId + " successfully");
	    if (!lVars.isEmpty()) {
		return updateProcessVariables(lClient, pProcessId, lVars);
	    } else {
		return true;
	    }
	} else {
	    LOG.error("BPM: Task ID:" + lProcess.getId() + " Process ID: " + pProcessId + ", Error in assigning to user " + pAssigneeId);
	    return false;
	}
    }

    // private Boolean updateTaskVariables(BPMClient pClient, String pTaskId,
    // List<TaskVariable> pTaskVariables) throws IOException {
    // Call<List<TaskVariable>> lUpdateTaskVariables =
    // pClient.getActivitiBPMAPI().updateTaskVariables(pTaskVariables, pTaskId);
    // Response<List<TaskVariable>> lResponse = lUpdateTaskVariables.execute();
    // boolean lReturn = lResponse.code() == HttpStatus.SC_OK || lResponse.code() ==
    // HttpStatus.SC_CREATED;
    // if (lReturn) {
    // LOG.info("BPM: Task ID:" + pTaskId + " Variables updated successfully");
    // } else {
    // LOG.error("BPM: Task ID:" + pTaskId + ", Error in updating variables");
    // }
    // return lReturn;
    // }
    private Boolean updateProcessVariables(BPMClient pClient, String pProcessId, List<TaskVariable> pTaskVariables) throws IOException {
	Call<List<TaskVariable>> lUpdateTaskVariables = pClient.getActivitiBPMAPI().updateProcessVariables(pTaskVariables, pProcessId);
	Response<List<TaskVariable>> lResponse = lUpdateTaskVariables.execute();

	boolean lReturn = lResponse.code() == HttpStatus.SC_OK || lResponse.code() == HttpStatus.SC_CREATED;

	if (lReturn) {
	    LOG.info("BPM: Process ID:" + pProcessId + " Variables updated successfully");
	} else {
	    LOG.error("BPM: Process ID:" + pProcessId + ", Error in updating variables");
	}
	return lReturn;
    }

    public TaskReponse getTaskList(User pLoginUser, String pTaskForUserId, String pTaskType, Integer offset, Integer limit, String sortKey, String sortType) throws IOException {
	BPMClient lClient = new BPMClient.Builder().connect(bPMConfig.getBpmRestUrl(), pLoginUser.getId(), pLoginUser.getDecryptedPassword()).build();
	Call<TaskReponse> lUserTaskList = lClient.getActivitiBPMAPI().getUserTaskList(true, pTaskForUserId, offset, limit, sortKey, sortType);
	Response<TaskReponse> lResponse = lUserTaskList.execute();

	if (lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body();
	}
	return null;
    }

    public Boolean setTaskAsCompleted(User pUser, String processId) throws IOException {
	BPMClient lClient = new BPMClient.Builder().connect(bPMConfig.getBpmRestUrl(), pUser.getId(), pUser.getDecryptedPassword()).build();
	Call<TaskReponse> lProcessDetails = lClient.getActivitiBPMAPI().getUserTaskDetail(processId);
	Response<TaskReponse> lProcessResponse = lProcessDetails.execute();
	if (lProcessResponse.code() != HttpStatus.SC_OK) {
	    LOG.error("BPM: Process Info not found for the Process ID: " + processId);
	    return false;
	}
	TaskReponse lProcessInfo = lProcessResponse.body();

	if (lProcessInfo == null || lProcessInfo.getData() == null || lProcessInfo.getData().isEmpty()) {
	    LOG.error("BPM: Process Info not found for the Process ID: " + processId);
	    return false;
	}
	TaskRepresentation lProcess = lProcessInfo.getData().get(0);
	LOG.info("BPM: Getting Task ID:" + lProcess.getId() + " is mapped with  Process ID: " + processId);
	BPMProcess lBPMProcess = new BPMProcess();
	lBPMProcess.setAction(Constants.BPM_COMPLETE);
	Call<Void> lAssignAndUnassignTask = lClient.getActivitiBPMAPI().assignAndUnassignTask(lBPMProcess, lProcess.getId());
	Response<Void> lResponse = lAssignAndUnassignTask.execute();
	boolean lReturn = lResponse.code() == HttpStatus.SC_OK || lResponse.code() == HttpStatus.SC_CREATED;
	if (lReturn) {
	    LOG.info("BPM: Task ID:" + lProcess.getId() + " Process ID: " + processId + " is marked task as completed successfully");
	} else {
	    LOG.error("BPM: Task ID:" + lProcess.getId() + " Process ID: " + processId + ", Error in marking complete");
	}
	return lReturn;
    }

    public Boolean setTaskAsCompletedWithVariables(User pUser, String processId, List<TaskVariable> taskVars) throws IOException {
	BPMClient lClient = new BPMClient.Builder().connect(bPMConfig.getBpmRestUrl(), pUser.getId(), pUser.getDecryptedPassword()).build();
	Call<TaskReponse> lProcessDetails = lClient.getActivitiBPMAPI().getUserTaskDetail(processId);
	Response<TaskReponse> lProcessResponse = lProcessDetails.execute();
	if (lProcessResponse.code() != HttpStatus.SC_OK) {
	    LOG.error("BPM: Process Info not found for the Process ID: " + processId);
	    return false;
	}
	TaskReponse lProcessInfo = lProcessResponse.body();

	if (lProcessInfo == null || lProcessInfo.getData() == null || lProcessInfo.getData().isEmpty()) {
	    LOG.error("BPM: Process Info not found for the Process ID: " + processId);
	    return false;
	}
	TaskRepresentation lProcess = lProcessInfo.getData().get(0);
	LOG.info("BPM: Getting Task ID:" + lProcess.getId() + " is mapped with  Process ID: " + processId);
	BPMProcess lBPMProcess = new BPMProcess();
	lBPMProcess.setAction(Constants.BPM_COMPLETE);
	lBPMProcess.setVariables(taskVars);
	Call<Void> lAssignAndUnassignTask = lClient.getActivitiBPMAPI().assignAndUnassignTask(lBPMProcess, lProcess.getId());
	Response<Void> lResponse = lAssignAndUnassignTask.execute();

	boolean lReturn = lResponse.code() == HttpStatus.SC_OK || lResponse.code() == HttpStatus.SC_CREATED;
	if (lReturn) {
	    LOG.info("BPM: Task ID:" + lProcess.getId() + " Process ID: " + processId + " is marked task as completed successfully");
	} else {
	    LOG.error("BPM: Task ID:" + lProcess.getId() + " Process ID: " + processId + ", Error in marking complete");
	}
	return lReturn;
    }

    public Boolean assignTaskToGroup(User pUser, String processId, String pGroupName, List<TaskVariable> pTaskVariables) throws IOException {
	BPMClient lClient = new BPMClient.Builder().connect(bPMConfig.getBpmRestUrl(), pUser.getId(), pUser.getDecryptedPassword()).build();

	Call<TaskReponse> lProcessDetails = lClient.getActivitiBPMAPI().getUserTaskDetail(processId);
	Response<TaskReponse> lProcessResponse = lProcessDetails.execute();
	if (lProcessResponse.code() != HttpStatus.SC_OK) {
	    LOG.error("BPM: Process Info not found for the Process ID: " + processId);
	    return false;
	}

	TaskReponse lProcessInfo = lProcessResponse.body();
	if (lProcessInfo == null || lProcessInfo.getData() == null || lProcessInfo.getData().isEmpty()) {
	    LOG.error("BPM: Process Info not found for the Process ID: " + processId);
	    return false;
	}

	TaskRepresentation lProcess = lProcessInfo.getData().get(0);
	LOG.info("BPM: Getting Task ID:" + lProcess.getId() + " is mapped with  Process ID: " + processId);
	GroupVariable lBPMProcess = new GroupVariable();
	lBPMProcess.setGroup(pGroupName);
	lBPMProcess.setType(Constants.BPM_PROPERTY_CANDIDATE);
	Call<BPMProcess> lAssignAndUnassignTask = lClient.getActivitiBPMAPI().assignTaskToGroup(lBPMProcess, lProcess.getId());
	Response<BPMProcess> lResponse = lAssignAndUnassignTask.execute();
	if (lResponse.code() == HttpStatus.SC_OK || lResponse.code() == HttpStatus.SC_CREATED) {
	    LOG.info("BPM: Task ID:" + lProcess.getId() + " Process ID: " + processId + " is assigned to group " + pGroupName + " successfully");
	    return updateProcessVariables(lClient, processId, pTaskVariables);
	} else {
	    LOG.error("BPM: Task ID:" + lProcess.getId() + " Process ID: " + processId + ", Error in assigning to group " + pGroupName);
	    return false;
	}
    }

    // public List<String> getTaskListByGroup(User pUser, String pTaskType, String
    // pGroupName) throws IOException {
    // BPMClient lClient = new BPMClient.Builder()
    // .connect(bPMConfig.getBpmRestUrl(), pUser.getId(),
    // pUser.getDecryptedPassword())
    // .build();
    // Call<TaskReponse> lUserTaskList =
    // lClient.getActivitiBPMAPI().getGroupTaskList("true", pGroupName);
    // Response<TaskReponse> lResponse = lUserTaskList.execute();
    //
    // if (lResponse == null || lResponse.code() != HttpStatus.SC_OK) {
    // return new ArrayList();
    // } else {
    // List<String> lTaskList = new ArrayList();
    // List<TaskRepresentation> data = lResponse.body().getData();
    // for (TaskRepresentation lTaskVariable : data) {
    // List<TaskVariable> variables = lTaskVariable.getVariables();
    // for (TaskVariable variable : variables) {
    // if (variable.getName().equalsIgnoreCase(pTaskType)) {
    // lTaskList.add(variable.getValue().toString());
    // }
    // }
    // }
    // return lTaskList;
    // }
    // }
    // Not Used anywhere
    public BPMProcess getProcessDetails(User pUser, String pProcessId) throws IOException {
	BPMClient lClient = new BPMClient.Builder().connect(bPMConfig.getBpmRestUrl(), pUser.getId(), pUser.getDecryptedPassword()).build();

	Call<BPMProcess> lProcessDetails = lClient.getActivitiBPMAPI().getProcessDetails(pProcessId);
	Response<BPMProcess> lResponse = lProcessDetails.execute();
	if (lResponse.code() != HttpStatus.SC_OK) {
	    return null;
	}
	return lResponse.body();
    }
}
