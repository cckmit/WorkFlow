package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;
import static com.tsi.workflow.DataWareHouse.limit;
import static com.tsi.workflow.DataWareHouse.offset;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.utils.Constants;

public class DeveloperManagerControllerDataHub {

    public static void init() {
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn("Comments");
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperManagerController.rejectPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(limit);
	    paramInOut.addIn(offset);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperManagerController.getMyPlanTasks", paramInOut);
	}
	// {
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(null);
	// paramInOut.addIn(null);
	// paramInOut.addIn(getPlan().getId());
	// paramInOut.addIn("Comments");
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("DeveloperManagerController.approvePlan",
	// paramInOut);
	// }
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn("Comments");
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperManagerController.approvePlan", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("DeveloperManagerController.setDeveloperManagerService", paramInOut);
	}
	// {
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(null);
	// paramInOut.addIn(null);
	// paramInOut.addIn(getPlan().getId());
	// paramInOut.addIn(Constants.PlanStatus.APPROVED.name());
	// paramInOut.addIn("Comments");
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("DeveloperManagerController.approveAndRejectPlan",
	// paramInOut);
	// }
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn(Constants.PlanStatus.APPROVED.name());
	    paramInOut.addIn("Comments");
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperManagerController.approveAndRejectPlan", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperManagerController.handleException", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("DeveloperManagerController.getCurrentUser", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperManagerController.handleWorkflowException", paramInOut);
	}
    }
}
