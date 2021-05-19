/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;

import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;

/**
 *
 * @author Radha.Adhimoolam
 */
public class SequenceGeneratorDataHub {

    public static void init() {

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.setOut(getPlan().getImplementationList().get(0).getId());
	    ParameterMap.addParameterInOut("SequenceGenerator.getNewImplementationId", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn("" + getPlan().getSystemLoadList().get(0).getSystemId().getPlatformId().getName().charAt(0));
	    paramInOut.setOut(getPlan().getId());
	    ParameterMap.addParameterInOut("SequenceGenerator.getNewImplementationPlanId", paramInOut);
	}

    }
}
