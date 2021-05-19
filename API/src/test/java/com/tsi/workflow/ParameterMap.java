/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author USER
 */
public class ParameterMap {

    public static HashMap<String, ParameterMap> lMap = new HashMap<>();

    String methodName;
    List<ParamInOut> paramInOuts = new ArrayList<>();

    public String getMethodName() {
	return methodName;
    }

    public void setMethodName(String methodName) {
	this.methodName = methodName;
    }

    public List<ParamInOut> getParamInOuts() {
	return paramInOuts;
    }

    public void setParamInOuts(List<ParamInOut> paramInOuts) {
	this.paramInOuts = paramInOuts;
    }

    public void addParameterInOut(ParamInOut paramInOut) {
	paramInOuts.add(paramInOut);
    }

    public static void addParameterInOut(String methodName, ParamInOut paramInOut) {
	if (lMap.get(methodName) == null) {
	    ParameterMap parameterMap = new ParameterMap();
	    parameterMap.setMethodName(methodName);
	    lMap.put(methodName, parameterMap);
	}
	lMap.get(methodName).addParameterInOut(paramInOut);
    }

}
