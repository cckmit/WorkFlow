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
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.LoginErrorCode;
import java.util.TreeSet;

/**
 *
 * @author Radha.Adhimoolam
 */
public class LDAPAuthenticatorImplDataHub {

    public static void init() {
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser().getId());
	    paramInOut.addIn(getUser().getPassword());
	    paramInOut.setOut(Constants.LoginErrorCode.SUCCESS);
	    ParameterMap.addParameterInOut("LDAPAuthenticatorImpl.validate", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser().getId());
	    paramInOut.addIn(getUser().getPassword());
	    paramInOut.setOut(Constants.LoginErrorCode.SUCCESS);
	    ParameterMap.addParameterInOut("LDAPAuthenticatorImpl.validvalidateLDAPLogin", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("LDAPAuthenticatorImpl.getUserDetails", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.setOut(LoginErrorCode.SUCCESS);
	    ParameterMap.addParameterInOut("LDAPAuthenticatorImpl.getLinuxGroupDetails", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("LDAPAuthenticatorImpl.setDelegatedLinuxGroupDetails", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(new TreeSet());
	    ParameterMap.addParameterInOut("LDAPAuthenticatorImpl.getLinuxUsers", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getDevManager());
	    paramInOut.setOut(getUser());
	    ParameterMap.addParameterInOut("LDAPAuthenticatorImpl.getUserDetails", paramInOut);
	}

    }

}
