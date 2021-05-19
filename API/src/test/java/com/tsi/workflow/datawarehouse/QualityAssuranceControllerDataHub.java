package com.tsi.workflow.datawarehouse;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;

public class QualityAssuranceControllerDataHub {

    public static void init() {
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("QualityAssuranceController.setQualityAssuranceService", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getSystemId().getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getVparId().getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getId());
	    paramInOut.addIn("PASS");
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getVparId().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("QualityAssuranceController.updatePlanTestStatus", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getSystemId().getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getVparId().getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getId());
	    paramInOut.addIn("FAIL");
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getVparId().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("QualityAssuranceController.updatePlanTestStatus", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("QualityAssuranceController.handleException", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("QualityAssuranceController.getCurrentUser", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("QualityAssuranceController.handleWorkflowException", paramInOut);
	}
    }
}
