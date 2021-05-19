package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import java.util.ArrayList;
import java.util.List;

public class PreProductionLoadsDAODataHub {

    private static System lSystem = new System();
    private static SystemLoad lSystemLoad = new SystemLoad();

    public static void init() {

	lSystemLoad = getPlan().getSystemLoadList().get(0);
	lSystem = lSystemLoad.getSystemId();

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getId());
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.findByPlanId", paramInOut);
	}
	{
	    List<ImpPlan> list = new ArrayList<ImpPlan>();
	    list.add(DataWareHouse.getPlan());
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(list);
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.findByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan());
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.findByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { getPlan().getId() });
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.findByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { getPlan().getId() });
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.findByPreProdLoadPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lSystem.getId());
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.findBySystemId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getSystemList());
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.findBySystemId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lSystem);
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.findBySystemId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { lSystem.getId() });
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.findBySystemId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lSystemLoad.getId());
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.findBySystemLoadId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadList());
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.findBySystemLoadId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lSystemLoad);
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.findBySystemLoadId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { lSystemLoad.getId() });
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.findBySystemLoadId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan());
	    paramInOut.addIn(lSystem);
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.findByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan());
	    paramInOut.addIn(lSystem);
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.findByPlanIdAndSystemId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { lSystemLoad.getId() });
	    paramInOut.addIn(new String[] { DataWareHouse.getPlan().getId() });
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.findByLoadset", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadList());
	    paramInOut.addIn(new String[] { DataWareHouse.getPlan().getPlanStatus() });
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.findBySystemLoadAndStatus", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan());
	    paramInOut.setOut(lSystemLoad);
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.findByPlanIdByLoad", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn(lSystemLoad.getId());
	    paramInOut.addIn(getPlan().getSystemLoadActionsList().get(0).getVparId().getId());
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.findByPlanIdAndSystemAndCpu", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { getPlan().getId() });
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.plansNotInPreProd", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { getPlan().getId() });
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("PreProductionLoadsDAO.getPreProdSystemByPlanStatus", paramInOut);
	}
    }

}
