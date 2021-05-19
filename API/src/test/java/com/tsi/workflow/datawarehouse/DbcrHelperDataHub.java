/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;

/**
 *
 * @author User
 */
public class DbcrHelperDataHub {

    public static void init() {
	for (int i = 0; i < getPlan().getDbcrList().size(); i++) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getDbcrList().get(i).getSystemId().getId());
	    paramInOut.addIn(getPlan().getDbcrList().get(i).getDbcrName());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DbcrHelper.validateDbcr", paramInOut);
	}
	for (int i = 0; i < getPlan().getDbcrList().size(); i++) {
	    {
		ParamInOut paramInOut = new ParamInOut();
		paramInOut.addIn(getPlan().getDbcrList().get(i));
		paramInOut.setOut(false);
		ParameterMap.addParameterInOut("DbcrHelper.isDbcrComplete", paramInOut);
	    }
	}
    }
}
