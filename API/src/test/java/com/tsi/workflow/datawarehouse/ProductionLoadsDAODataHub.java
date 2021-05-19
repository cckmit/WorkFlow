package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.User;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.utils.Constants;
import java.util.Arrays;

public class ProductionLoadsDAODataHub {

    private static System lSystem = new System();
    private static SystemLoad lSystemLoad = new SystemLoad();

    public static void init() {

	lSystemLoad = getPlan().getSystemLoadList().get(0);
	lSystem = lSystemLoad.getSystemId();
	for (int i = 0; i < getPlan().getSystemLoadList().size(); i++) {
	    {
		ParamInOut paramInOut = new ParamInOut();
		paramInOut.addIn(getPlan());
		paramInOut.addIn(getPlan().getSystemLoadList().get(i).getSystemId());
		paramInOut.setOut(getPlan().getProductionLoadsList().get(i));
		ParameterMap.addParameterInOut("ProductionLoadsDAO.findByPlanId", paramInOut);
	    }
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { getPlan().getId() });
	    paramInOut.addIn(lSystem);
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findByPlanId", paramInOut);
	}
	for (Constants.LOAD_SET_STATUS value : Constants.LOAD_SET_STATUS.values()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan());
	    paramInOut.addIn(value);
	    paramInOut.setOut(Long.valueOf(0));
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findLoadSetInProgress", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { getPlan().getId() });
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan());
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(Arrays.asList(getPlan()));
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lSystemLoad.getId());
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findBySystemLoadId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { lSystemLoad.getId() });
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findBySystemLoadId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lSystemLoad);
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findBySystemLoadId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(Arrays.asList(lSystemLoad));
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findBySystemLoadId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lSystem.getId());
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findBySystemId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { lSystem.getId() });
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findBySystemId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lSystem);
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findBySystemId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(Arrays.asList(lSystem));
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findBySystemId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findTobeLoaded", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findAllLastSuccessfulBuild", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan());
	    paramInOut.addIn(lSystem);
	    paramInOut.setOut(getPlan().getProductionLoadsList());
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findALLByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.count", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.count", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.update", paramInOut);
	}
	for (ProductionLoads lProdLoads : DataWareHouse.getPlan().getProductionLoadsList()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lProdLoads.getId());
	    paramInOut.setOut(lProdLoads);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.find", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.find", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.find", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.delete", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.delete", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.delete", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.save", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findE", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.hardDelete", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.hardDelete", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.hardDelete", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer(1));
	    paramInOut.addIn("test");
	    paramInOut.setOut(new ProductionLoads());
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.findByLoadset", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new User());
	    paramInOut.addIn(new ProductionLoads());
	    paramInOut.setOut(Void.TYPE);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.updateAllActivated", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new User());
	    paramInOut.addIn(new ProductionLoads());
	    paramInOut.setOut(Void.TYPE);
	    ParameterMap.addParameterInOut("ProductionLoadsDAO.updateAllDeActivated", paramInOut);
	}
    }
}
