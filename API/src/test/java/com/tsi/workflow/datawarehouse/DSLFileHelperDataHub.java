/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.datawarehouse;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.beans.dao.SystemLoadActions;

/**
 *
 * @author Radha.Adhimoolam
 */
public class DSLFileHelperDataHub {

    public static void init() {
	for (SystemLoadActions lSysLoad : DataWareHouse.getPlan().getSystemLoadActionsList()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(lSysLoad);

	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DbcrHelper.updatePlanInfoInDSLFile", paramInOut);
	}
	for (SystemLoadActions lSysLoad : DataWareHouse.getPlan().getSystemLoadActionsList()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(lSysLoad);
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DbcrHelper.deletePlanInfoInDSLFile", paramInOut);
	}
    }
}
