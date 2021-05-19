/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getUser;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.utils.Constants;

/**
 *
 * @author Radha.Adhimoolam
 */
public class GITSSHUtilsDataHub {

    public static void init() {

	for (Constants.PlanStatus value : Constants.PlanStatus.values()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(DataWareHouse.RepoName);
	    paramInOut.addIn(value);
	    paramInOut.addIn(DataWareHouse.getBranchList());
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("GITSSHUtils.addImplementationTag", paramInOut);
	}
	for (Constants.PlanStatus value : Constants.PlanStatus.values()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.RepoName);
	    paramInOut.addIn(value);
	    paramInOut.addIn(DataWareHouse.getBranchList());
	    paramInOut.setOut(Boolean.FALSE);
	    ParameterMap.addParameterInOut("GITSSHUtils.addImplementationTag", paramInOut);
	}
	for (Constants.TagStatus value : Constants.TagStatus.values()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(DataWareHouse.RepoName);
	    paramInOut.addIn(value);
	    paramInOut.addIn(DataWareHouse.getBranchList());
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("GITSSHUtils.addImplementationTag", paramInOut);
	}
	for (Constants.TagStatus value : Constants.TagStatus.values()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.RepoName);
	    paramInOut.addIn(value);
	    paramInOut.addIn(DataWareHouse.getBranchList());
	    paramInOut.setOut(Boolean.FALSE);
	    ParameterMap.addParameterInOut("GITSSHUtils.addImplementationTag", paramInOut);
	}

	for (Constants.ImplementationSubStatus value : Constants.ImplementationSubStatus.values()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(DataWareHouse.RepoName);
	    paramInOut.addIn(value);
	    paramInOut.addIn(DataWareHouse.getBranchList());
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("GITSSHUtils.addImplementationTag", paramInOut);
	}
	for (Constants.ImplementationSubStatus value : Constants.ImplementationSubStatus.values()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn("");
	    paramInOut.addIn(value);
	    paramInOut.addIn(DataWareHouse.getBranchList());
	    paramInOut.setOut(Boolean.FALSE);
	    ParameterMap.addParameterInOut("GITSSHUtils.addImplementationTag", paramInOut);
	}
	for (Constants.TagStatus value : Constants.TagStatus.values()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(DataWareHouse.RepoName);
	    paramInOut.addIn(value);
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("GITSSHUtils.removeTag", paramInOut);
	}
	for (Constants.TagStatus value : Constants.TagStatus.values()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.RepoName);
	    paramInOut.addIn(value);
	    paramInOut.setOut(Boolean.FALSE);
	    ParameterMap.addParameterInOut("GITSSHUtils.removeTag", paramInOut);
	}

    }
}
