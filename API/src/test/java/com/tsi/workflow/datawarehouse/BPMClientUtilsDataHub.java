/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;
import static com.tsi.workflow.DataWareHouse.getUser;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.bpm.model.TaskVariable;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class BPMClientUtilsDataHub {

    public static void init() {

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getUser().getMailId());
	    paramInOut.setOut(getPlan().getProcessId());
	    ParameterMap.addParameterInOut("BPMClientUtils.createADLProcess", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn(getPlan().getImplementationList().get(0).getImpDesc());
	    paramInOut.addIn(getUser().getDisplayName());
	    paramInOut.addIn(getUser().getMailId());
	    paramInOut.setOut(getPlan().getImplementationList().get(0).getProcessId());
	    ParameterMap.addParameterInOut("BPMClientUtils.createDeveloperProcess", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getProcessId());
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("BPMClientUtils.removeUserFromTask", paramInOut);
	}

	{
	    List<TaskVariable> lVars = new ArrayList<>();
	    lVars.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_PLAN_ID, getPlan().getId()));
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getProcessId());
	    paramInOut.addIn(getUser().getId());
	    paramInOut.addIn(lVars);
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("BPMClientUtils.assignTask", paramInOut);
	}
	{
	    List<TaskVariable> lVars = new ArrayList<>();
	    lVars.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_PLAN_ID, getPlan().getId()));
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getProcessId());
	    paramInOut.addIn(getPlan().getDevManager());
	    paramInOut.addIn(lVars);
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("BPMClientUtils.assignTask", paramInOut);
	}

	{
	    List<TaskVariable> lVars = new ArrayList<>();
	    lVars.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_PLAN_ID, getPlan().getId()));

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getProcessId());
	    paramInOut.addIn(lVars);
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("BPMClientUtils.updateProcessVariables", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getUser().getId());
	    paramInOut.addIn(Constants.BPM_IMPLEMENTATION_ID);
	    paramInOut.addIn(DataWareHouse.offset);
	    paramInOut.addIn(DataWareHouse.limit);
	    paramInOut.addIn("createTime");
	    paramInOut.addIn("desc");
	    paramInOut.setOut(DataWareHouse.getTaskResponse());
	    ParameterMap.addParameterInOut("BPMClientUtils.getTaskList", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getProcessId());
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("BPMClientUtils.setTaskAsCompleted", paramInOut);
	}
	{
	    List<TaskVariable> lVars = new ArrayList<>();
	    lVars.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_PLAN_ID, getPlan().getId()));

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getProcessId());
	    paramInOut.addIn(lVars);
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("BPMClientUtils.setTaskAsCompletedWithVariables", paramInOut);
	}
	{
	    List<TaskVariable> lVars = new ArrayList<>();
	    lVars.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_PLAN_ID, getPlan().getId()));

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getProcessId());
	    paramInOut.addIn("QA");
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("BPMClientUtils.assignTaskToGroup", paramInOut);
	}
	// {
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(getUser());
	// paramInOut.addIn(getPlan().getProcessId());
	// paramInOut.addIn("QA");
	// paramInOut.setOut(Arrays.asList(getPlan().getId()));
	// ParameterMap.addParameterInOut("BPMClientUtils.getTaskListByGroup",
	// paramInOut);
	// }

    }
}
