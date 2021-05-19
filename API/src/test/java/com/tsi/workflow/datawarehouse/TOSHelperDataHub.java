/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.datawarehouse;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.utils.Constants;

/**
 *
 * @author Radha.Adhimoolam
 */
public class TOSHelperDataHub {

    public static void init() {

	for (Constants.LoadSetCommands value : Constants.LoadSetCommands.values()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(value);
	    paramInOut.addIn(DataWareHouse.getPlan().getProductionLoadsList().get(0));
	    paramInOut.addIn(DataWareHouse.getPlan().getProductionLoadsList().get(0).getStatus());
	    paramInOut.addIn(DataWareHouse.getPlan().getBuildList().get(0));
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("TOSHelper.doTOSOperation", paramInOut);
	}
	for (Constants.LoadSetCommands value : Constants.LoadSetCommands.values()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(value);
	    paramInOut.addIn(DataWareHouse.getPlan().getProductionLoadsList().get(0));
	    paramInOut.addIn(DataWareHouse.getPlan().getProductionLoadsList().get(0).getStatus());
	    paramInOut.addIn(DataWareHouse.getPlan().getBuildList().get(0));
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("TOSHelper.doFallbackTOSOperation", paramInOut);
	}
	for (SystemLoad lSystemLoad : DataWareHouse.getPlan().getSystemLoadList()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(lSystemLoad);
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("TOSHelper.requestIP", paramInOut);
	}
	for (SystemLoad lSystemLoad : DataWareHouse.getPlan().getSystemLoadList()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lSystemLoad.getId());
	    paramInOut.setOut("");
	    ParameterMap.addParameterInOut("TOSHelper.getIP", paramInOut);
	}
	for (SystemLoad lSystemLoad : DataWareHouse.getPlan().getSystemLoadList()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(lSystemLoad);
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("TOSHelper.refresh", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getProductionLoadsList().get(0));
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("TOSHelper.refreshOne", paramInOut);
	}

    }

}
