package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;
import static com.tsi.workflow.DataWareHouse.systemList;

import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.utils.Constants;
import java.util.Arrays;

public class BuildDAODataHub {

    private static System lSystem = new System();

    public static void init() {

	lSystem = getPlan().getSystemLoadList().get(0).getSystemId();
	for (int i = 0; i < getPlan().getBuildList().size(); i++) {
	    {
		ParamInOut paramInOut = new ParamInOut();
		paramInOut.addIn(getPlan().getBuildList().get(i).getPlanId().getId());
		paramInOut.addIn(getPlan().getBuildList().get(i).getSystemId().getId());
		paramInOut.addIn(Constants.BUILD_TYPE.STG_LOAD);
		paramInOut.setOut(getPlan().getBuildList().get(i));
		ParameterMap.addParameterInOut("BuildDAO.findLastSuccessfulBuild", paramInOut);
	    }

	}
	for (Constants.BUILD_TYPE value : Constants.BUILD_TYPE.values()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan());
	    paramInOut.addIn(value);
	    paramInOut.setOut(getPlan().getBuildList());
	    ParameterMap.addParameterInOut("BuildDAO.findAll", paramInOut);
	}
	for (Constants.BUILD_TYPE value : Constants.BUILD_TYPE.values()) {
	    for (SystemLoad systemLoad : getPlan().getSystemLoadList()) {
		if (systemLoad.getActive().equals("Y")) {
		    systemList.add(systemLoad.getSystemId());
		}
	    }
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn(systemList);
	    paramInOut.addIn(value);
	    paramInOut.setOut(getPlan().getBuildList());
	    ParameterMap.addParameterInOut("BuildDAO.findLastBuild", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn(lSystem);
	    paramInOut.addIn(getPlan().getBuildList().get(0).getId());
	    paramInOut.addIn(getPlan().getBuildList().get(0).getBuildType());
	    paramInOut.setOut(getPlan().getBuildList());
	    ParameterMap.addParameterInOut("BuildDAO.findByBuild", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lSystem.getId());
	    paramInOut.setOut(getPlan().getBuildList());
	    ParameterMap.addParameterInOut("BuildDAO.findBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { lSystem.getId() });
	    paramInOut.setOut(getPlan().getBuildList());
	    ParameterMap.addParameterInOut("BuildDAO.findBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lSystem);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.findBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(Arrays.asList(lSystem));
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.findBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.setOut(getPlan().getBuildList());
	    ParameterMap.addParameterInOut("BuildDAO.findByImpPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { getPlan().getId() });
	    paramInOut.setOut(getPlan().getBuildList());
	    ParameterMap.addParameterInOut("BuildDAO.findByImpPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan());
	    paramInOut.setOut(getPlan().getBuildList());
	    ParameterMap.addParameterInOut("BuildDAO.findByImpPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(Arrays.asList(getPlan()));
	    paramInOut.setOut(getPlan().getBuildList());
	    ParameterMap.addParameterInOut("BuildDAO.findByImpPlan", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.count", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.count", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.update", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.find", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.find", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.find", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.delete", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.delete", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.delete", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.save", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.findAll", paramInOut);
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
	    ParameterMap.addParameterInOut("BuildDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan());
	    paramInOut.addIn(getPlan().getBuildList().get(0).getBuildType());
	    paramInOut.setOut(getPlan().getBuildList());
	    ParameterMap.addParameterInOut("BuildDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.findAll", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.findE", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.hardDelete", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.hardDelete", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("BuildDAO.hardDelete", paramInOut);
	}

	for (Constants.BUILD_TYPE value : Constants.BUILD_TYPE.values()) {
	    for (SystemLoad systemLoad : getPlan().getSystemLoadList()) {
		if (systemLoad.getActive().equals("Y")) {
		    systemList.add(systemLoad.getSystemId());
		}
	    }
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn(systemList);
	    paramInOut.addIn(value);
	    paramInOut.setOut(getPlan().getBuildList());
	    ParameterMap.addParameterInOut("BuildDAO.findLastBuildByPlan", paramInOut);
	}

    }
}
