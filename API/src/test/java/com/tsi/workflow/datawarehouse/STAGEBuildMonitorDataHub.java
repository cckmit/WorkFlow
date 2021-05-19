/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.datawarehouse;

import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.schedular.jenkins.STAGEBuildMonitor;

/**
 *
 * @author deepa.jayakumar
 */
public class STAGEBuildMonitorDataHub {

    private static STAGEBuildMonitor stage = new STAGEBuildMonitor();

    public static void init() {
	{
	    ParamInOut paramInOut = new ParamInOut();
	    ParameterMap.addParameterInOut("STAGEBuildMonitor.doMonitor", paramInOut);
	}
    }
}
