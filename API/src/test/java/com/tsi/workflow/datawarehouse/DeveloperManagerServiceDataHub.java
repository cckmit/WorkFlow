package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;
import static com.tsi.workflow.DataWareHouse.getUser;
import static com.tsi.workflow.DataWareHouse.limit;
import static com.tsi.workflow.DataWareHouse.offset;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.utils.Constants;

public class DeveloperManagerServiceDataHub {

    public static void init() {
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn("Comments");
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperManagerService.rejectPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(offset);
	    paramInOut.addIn(limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperManagerService.getMyPlanTasks", paramInOut);
	}
	// {
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(getUser());
	// paramInOut.addIn(getPlan().getId());
	// paramInOut.addIn("Comments");
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("DeveloperManagerService.approvePlan",
	// paramInOut);
	// }
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn("Comments");
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperManagerService.approvePlan", paramInOut);
	}

	// {
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(getUser());
	// paramInOut.addIn(getPlan().getId());
	// paramInOut.addIn(Constants.PlanStatus.APPROVED.name());
	// paramInOut.addIn("Comments");
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("DeveloperManagerService.approveAndRejectPlan",
	// paramInOut);
	// }
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn(Constants.PlanStatus.APPROVED.name());
	    paramInOut.addIn("Comments");
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperManagerService.approveAndRejectPlan", paramInOut);
	}

	// {
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(null);
	// paramInOut.addIn(null);
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("DeveloperManagerService.approveProcessinBPMForPlan",
	// paramInOut);
	// }
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperManagerService.approveProcessinBPMForPlan", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("DeveloperManagerService.setJenkinsClient", paramInOut);
	}
    }
}
