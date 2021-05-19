/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;
import static com.tsi.workflow.DataWareHouse.getUser;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.utils.Constants;
import java.util.Arrays;

/**
 *
 * @author Radha.Adhimoolam
 */
public class GitBlitClientUtilsDataHub {

    public static void init() {

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.RepoName);
	    paramInOut.addIn(getPlan().getPlanDesc());
	    paramInOut.addIn(Arrays.asList(getPlan().getLeadId()));
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("GitBlitClientUtils.createGitRepository", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.RepoName);
	    paramInOut.addIn(getUser().getId());
	    paramInOut.addIn(Constants.GIT_PERMISSION_READWRITE);
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("GitBlitClientUtils.setPermissionForGitRepository", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.RepoName);
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("GitBlitClientUtils.freezeRepository", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.RepoName);
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("GitBlitClientUtils.unFreezeRepository", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.RepoName);
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn(null); // String Ticket - response from shell script
	    paramInOut.setOut(""); // Ticket Number
	    ParameterMap.addParameterInOut("GitBlitClientUtils.getImplementationTicketURL", paramInOut);
	}

    }

}
