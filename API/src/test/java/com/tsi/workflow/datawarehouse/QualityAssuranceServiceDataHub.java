package com.tsi.workflow.datawarehouse;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;

public class QualityAssuranceServiceDataHub {

    public static void init() {
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("QualityAssuranceService.setImpPlanDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("QualityAssuranceService.setActivityLogDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("QualityAssuranceService.setSystemLoadActionsDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("QualityAssuranceService.setSystemDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("QualityAssuranceService.setVparsDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("QualityAssuranceService.setRejectHelper", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getSystemId().getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getVparId().getId());
	    paramInOut.addIn("PASS");
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getVparId().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("QualityAssuranceService.updatePlanTestStatus", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getSystemId().getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getVparId().getId());
	    paramInOut.addIn("FAIL");
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getVparId().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("QualityAssuranceService.updatePlanTestStatus", paramInOut);
	}

    }
}
