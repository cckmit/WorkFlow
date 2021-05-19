package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getUser;
import static com.tsi.workflow.DataWareHouse.getUserSettings;

import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.utils.Constants;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

public class UserSettingsDAODataHub {

    public static void init() {
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser().getId());
	    paramInOut.setOut(Arrays.asList(getUserSettings()));
	    ParameterMap.addParameterInOut("UserSettingsDAO.findAll", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.count", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.count", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.update", paramInOut);
	}
	{
	    HashMap<String, Serializable> lFilter = new HashMap<>();
	    lFilter.put("active", "Y");
	    lFilter.put("name", Constants.UserSettings.DELEGATION.name());
	    lFilter.put("userId", getUser().getId());

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lFilter);
	    paramInOut.setOut(getUserSettings());
	    ParameterMap.addParameterInOut("UserSettingsDAO.find", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.find", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.find", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser().getId());
	    paramInOut.addIn(Constants.UserSettings.DELEGATION.name());
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.find", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.delete", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.delete", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.delete", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.save", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.findAll", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.findE", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.hardDelete", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.hardDelete", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("UserSettingsDAO.hardDelete", paramInOut);
	}
    }

    {
	ParamInOut paramInOut = new ParamInOut();
	paramInOut.addIn(getUser().getId());
	paramInOut.addIn(Constants.UserSettings.DELEGATION.name());
	paramInOut.setOut(getUserSettings());
	ParameterMap.addParameterInOut("UserSettingsDAO.findBySettingsName", paramInOut);
    }

    {
	ParamInOut paramInOut = new ParamInOut();
	paramInOut.addIn(getUserSettings().getUserId());
	paramInOut.setOut(Arrays.asList(getUserSettings()));
	ParameterMap.addParameterInOut("UserSettingsDAO.findAssignedDelegationTo", paramInOut);
    }

    {
	ParamInOut paramInOut = new ParamInOut();
	paramInOut.addIn(Arrays.asList(getUserSettings().getUserId()));
	paramInOut.setOut(Arrays.asList(getUserSettings()));
	ParameterMap.addParameterInOut("UserSettingsDAO.findAssignedDelegationFrom", paramInOut);
    }

}
