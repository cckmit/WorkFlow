/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;
import static com.tsi.workflow.DataWareHouse.getUser;

import com.offbytwo.jenkins.model.BuildResult;
import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.utils.Constants;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Radha.Adhimoolam
 */
public class JenkinsClientDataHub {

    public static void init() {
	{
	    {
		ParamInOut paramInOut = new ParamInOut();
		paramInOut.addIn(null);
		paramInOut.setOut(null);
		ParameterMap.addParameterInOut("JenkinsClient.getJobXMLByName", paramInOut);
	    }
	    {
		ParamInOut paramInOut = new ParamInOut();
		paramInOut.addIn(null);
		paramInOut.addIn(null);
		paramInOut.setOut(null);
		ParameterMap.addParameterInOut("JenkinsClient.createJobByXML", paramInOut);
	    }

	    {
		ParamInOut paramInOut = new ParamInOut();
		paramInOut.addIn(null);
		paramInOut.setOut(null);
		ParameterMap.addParameterInOut("JenkinsClient.validateJob", paramInOut);
	    }
	    for (Constants.DEVL value : Constants.DEVL.values()) {
		for (int i = 0; i < DataWareHouse.getPlan().getSystemLoadList().size(); i++) {
		    {
			Map<String, String> lJobParams = new HashMap<>();
			lJobParams.put("ImplementationID", getPlan().getImplementationList().get(0).getId().toLowerCase() + "_" + DataWareHouse.getPlan().getSystemLoadList().get(i).getSystemId().getName().toLowerCase());
			ParamInOut paramInOut = new ParamInOut();
			paramInOut.addIn(getUser());
			paramInOut.addIn(value.name() + DataWareHouse.getPlan().getSystemLoadList().get(i).getSystemId().getName());
			paramInOut.addIn(lJobParams);
			paramInOut.setOut(new JenkinsBuild(getUser()));
			ParameterMap.addParameterInOut("JenkinsClient.executeJob", paramInOut);
		    }
		}
	    }
	    for (Constants.STAGING value : Constants.STAGING.values()) {
		for (int i = 0; i < DataWareHouse.getPlan().getSystemLoadList().size(); i++) {
		    {
			JenkinsBuild jenkinsBuild = new JenkinsBuild(getUser());
			Map<String, String> lJobParams = new HashMap<>();
			lJobParams.put("ImplementationID", getPlan().getImplementationList().get(0).getId().toLowerCase() + "_" + DataWareHouse.getPlan().getSystemLoadList().get(i).getSystemId().getName().toLowerCase());
			ParamInOut paramInOut = new ParamInOut();
			paramInOut.addIn(getUser());
			paramInOut.addIn(value.name() + DataWareHouse.getPlan().getSystemLoadList().get(i).getSystemId().getName());
			paramInOut.addIn(lJobParams);
			paramInOut.setOut(jenkinsBuild);
			ParameterMap.addParameterInOut("JenkinsClient.executeJob", paramInOut);
		    }
		}
	    }

	    {
		ParamInOut paramInOut = new ParamInOut();
		paramInOut.addIn(null);
		paramInOut.addIn(null);
		paramInOut.setOut(null);
		ParameterMap.addParameterInOut("JenkinsClient.stopBuild", paramInOut);
	    }
	    {
		ParamInOut paramInOut = new ParamInOut();
		paramInOut.addIn(null);
		paramInOut.addIn(null);
		paramInOut.setOut(null);
		ParameterMap.addParameterInOut("JenkinsClient.getJobResult", paramInOut);
	    }
	    {
		ParamInOut paramInOut = new ParamInOut();
		paramInOut.addIn(null);
		paramInOut.addIn(null);
		paramInOut.setOut(null);
		ParameterMap.addParameterInOut("JenkinsClient.getPercentCompleted", paramInOut);
	    }
	    {
		ParamInOut paramInOut = new ParamInOut();
		paramInOut.addIn(null);
		paramInOut.setOut(null);
		ParameterMap.addParameterInOut("JenkinsClient.getPercentCompleted", paramInOut);
	    }
	    {
		ParamInOut paramInOut = new ParamInOut();
		paramInOut.addIn(null);
		paramInOut.setOut(null);
		ParameterMap.addParameterInOut("JenkinsClient.getBuildNumber", paramInOut);
	    }
	    for (Constants.DEVL value : Constants.DEVL.values()) {
		for (int i = 0; i < DataWareHouse.getPlan().getSystemLoadList().size(); i++) {
		    {
			ParamInOut paramInOut = new ParamInOut();
			paramInOut.addIn(value + DataWareHouse.getPlan().getSystemLoadList().get(i).getSystemId().getName());
			paramInOut.addIn(getPlan().getBuildList().get(0).getBuildNumber());
			paramInOut.setOut("Sample Console Data");
			ParameterMap.addParameterInOut("JenkinsClient.getConsoleLog", paramInOut);
		    }
		}
	    }
	    for (Constants.DEVL value : Constants.DEVL.values()) {
		for (int i = 0; i < DataWareHouse.getPlan().getSystemLoadList().size(); i++) {
		    {
			ParamInOut paramInOut = new ParamInOut();
			paramInOut.addIn(value + DataWareHouse.getPlan().getSystemLoadList().get(i).getSystemId().getName());
			paramInOut.addIn(getPlan().getBuildList().get(0).getBuildNumber());
			paramInOut.setOut(BuildResult.SUCCESS);
			ParameterMap.addParameterInOut("JenkinsClient.getJobResult", paramInOut);
		    }
		}
	    }

	    for (Constants.STAGING value : Constants.STAGING.values()) {
		for (int i = 0; i < DataWareHouse.getPlan().getSystemLoadList().size(); i++) {
		    {
			ParamInOut paramInOut = new ParamInOut();
			paramInOut.addIn(value + DataWareHouse.getPlan().getSystemLoadList().get(i).getSystemId().getName());
			paramInOut.addIn(getPlan().getBuildList().get(0).getBuildNumber());
			paramInOut.setOut(BuildResult.SUCCESS);
			ParameterMap.addParameterInOut("JenkinsClient.getJobResult", paramInOut);
		    }
		}
	    }

	    {
		ParamInOut paramInOut = new ParamInOut();
		paramInOut.addIn(null);
		paramInOut.setOut(null);
		ParameterMap.addParameterInOut("JenkinsClient.isInQueue", paramInOut);
	    }
	    {
		ParamInOut paramInOut = new ParamInOut();
		paramInOut.addIn(null);
		paramInOut.setOut(null);
		ParameterMap.addParameterInOut("JenkinsClient.isBuildable", paramInOut);
	    }
	    {
		ParamInOut paramInOut = new ParamInOut();
		paramInOut.addIn(null);
		paramInOut.setOut(null);
		ParameterMap.addParameterInOut("JenkinsClient.getLastBuild", paramInOut);
	    }

	}
    }

}
