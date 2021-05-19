package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getUser;
import static com.tsi.workflow.DataWareHouse.limit;
import static com.tsi.workflow.DataWareHouse.offset;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.helper.RejectHelper;

public class TSDServiceDataHub {

    public static void init() {
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("TSDService.setBuildDAO", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new ImpPlanDAO());
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("TSDService.setImpPlanDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { DataWareHouse.getPlan().getId() });
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getName());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("TSDService.getProductionLoads", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new ActivityLogDAO());
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("TSDService.setActivityLogDAO", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new SystemLoadActionsDAO());
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("TSDService.setSystemLoadActionsDAO", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new ProductionLoadsDAO());
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("TSDService.setProductionLoadsDAO", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new SystemDAO());
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("TSDService.setSystemDAO", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new SystemLoadDAO());
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("TSDService.setSystemLoadDAO", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new GITConfig());
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("TSDService.setGITConfig", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new RejectHelper());
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("TSDService.setRejectHelper", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("TSDService.setGITSSHUtils", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getId());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("TSDService.setOnline", paramInOut);
	}
	{
	    // Error Case
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn("");
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("TSDService.setOnline", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getId());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("TSDService.acceptFallback", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn("");
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("TSDService.acceptFallback", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(offset);
	    paramInOut.addIn(limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("TSDService.getFallBackLoads", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("TSDService.setJGitClientUtils", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getId());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("TSDService.deActivateFallback", paramInOut);
	}
	{
	    // Error Case
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn("");
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("TSDService.deActivateFallback", paramInOut);
	}

	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("TSDService.setBPMClientUtils", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getId());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("TSDService.setFallback", paramInOut);
	}
	for (ProductionLoads lProdLoads : DataWareHouse.getPlan().getProductionLoadsList()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(lProdLoads);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("TSDService.postActivationAction", paramInOut);
	    break;
	}
	for (ProductionLoads lProdLoads : DataWareHouse.getPlan().getProductionLoadsList()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(lProdLoads.getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("TSDService.getProdRefresh", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(offset);
	    paramInOut.addIn(limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("TSDService.getLoadsToAccept", paramInOut);
	}

	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("TSDService.setTOSHelper", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("TSDService.setFTPHelper", paramInOut);
	}
    }
}
