package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;
import static com.tsi.workflow.DataWareHouse.getPositiveResponse;
import static com.tsi.workflow.DataWareHouse.getUser;
import static com.tsi.workflow.DataWareHouse.limit;
import static com.tsi.workflow.DataWareHouse.offset;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import java.util.HashMap;

public class DeveloperLeadServiceDataHub {

    public static void init() {
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getDbcrList().get(0).getDbcrName());
	    paramInOut.addIn(getPlan().getDbcrList().get(0).getSystemId().getId().toString());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperLeadService.validateDbcr", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperLeadService.savePlan", paramInOut);
	}

	// {
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(getUser());
	// paramInOut.addIn(getPlan());
	// paramInOut.setOut(getPositiveResponse());
	// ParameterMap.addParameterInOut("DeveloperLeadService.passAcceptanceTesting",
	// paramInOut);
	// }
	{
	    HashMap<String, String> lOrderBy = new HashMap();
	    lOrderBy.put("id", "asc");
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser()); // User
	    paramInOut.addIn(offset); // Integer
	    paramInOut.addIn(limit); // Integer
	    paramInOut.addIn(null); // OrderBy
	    paramInOut.setOut(getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperLeadService.getUserTaskList", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperLeadService.submitToDevManager", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperLeadService.isSubmitReady", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn("T1700485");
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperLeadService.isSubmitReady", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(offset);
	    paramInOut.addIn(limit);
	    paramInOut.addIn(getPlan().getProjectId().getId().toString());
	    paramInOut.addIn(null);
	    paramInOut.setOut(getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperLeadService.getProjectList", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn(offset);
	    paramInOut.addIn(limit);
	    paramInOut.addIn(null);
	    paramInOut.addIn("");
	    paramInOut.setOut(getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperLeadService.getActivityLogList", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getDbcrList().get(0).getId().toString());
	    paramInOut.setOut(getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperLeadService.deleteDbcr", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getDbcrList());
	    paramInOut.setOut(getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperLeadService.saveDbcrList", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(new String[] { getPlan().getId() });
	    paramInOut.setOut(getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperLeadService.getDbcrList", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(offset);
	    paramInOut.addIn(limit);
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn(null);
	    paramInOut.setOut(getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperLeadService.getPlanList", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.setOut(getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperLeadService.isCheckForCSV", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("DeveloperLeadService.setCheckoutSegmentsDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("DeveloperLeadService.setDbcrDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("DeveloperLeadService.setImpPlanDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("DeveloperLeadService.setImplementationDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("DeveloperLeadService.setProjectDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("DeveloperLeadService.setSystemLoadDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("DeveloperLeadService.setCSRNumberDAO", paramInOut);
	}

    }
}
