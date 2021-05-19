/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;

import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import java.util.Arrays;

/**
 *
 * @author Radha.Adhimoolam
 */
public class CSRNumberDAODataHub {
    public static void init() {
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getProjectId().getId().toString());
	    paramInOut.setOut(Arrays.asList(getPlan().getProjectId()));
	    ParameterMap.addParameterInOut("CSRNumberDAO.findFiltered", paramInOut);
	}
    }
}
