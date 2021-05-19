/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;
import static com.tsi.workflow.DataWareHouse.getUser;

import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;

/**
 *
 * @author Radha.Adhimoolam
 */
public class DateAuditCrossCheckDataHub {

    public static void init() {

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan());
	    paramInOut.setOut("PASSED");
	    ParameterMap.addParameterInOut("DateAuditCrossCheck.dateAutditForMigration", paramInOut);
	}

    }

}
