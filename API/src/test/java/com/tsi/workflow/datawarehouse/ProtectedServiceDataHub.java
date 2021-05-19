package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;
import static com.tsi.workflow.DataWareHouse.getUser;
import static com.tsi.workflow.DataWareHouse.limit;
import static com.tsi.workflow.DataWareHouse.offset;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.User;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.utils.Constants;
import java.util.LinkedHashMap;

public class ProtectedServiceDataHub {

    public static void init() {
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(new String[] { getPlan().getId() });
	    paramInOut.addIn(new Integer[] { getPlan().getSystemLoadList().get(0).getSystemId().getVparsList().get(0).getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.getSystemLoadActions", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { getPlan().getImplementationList().get(0).getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.getSegmentMappingByImplementation", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.uploadExceptionLoadApproval", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getImpPlanApprovalsList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.deleteLoadApproval", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn("filename.txt");
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("ProtectedService.downloadLoadApproval", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.rejectPlanAndDependentPlans", paramInOut);
	}
	{
	    User user = getUser();
	    user.setCurrentRole(Constants.UserGroup.Lead.name());
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(user);
	    paramInOut.addIn(offset);
	    paramInOut.addIn(limit);
	    paramInOut.addIn(new LinkedHashMap<String, String>());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.getDeploymentPlanList", paramInOut);
	}
	{
	    User user = getUser();
	    user.setCurrentRole(Constants.UserGroup.QA.name());
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(user);
	    paramInOut.addIn(offset);
	    paramInOut.addIn(limit);
	    paramInOut.addIn(new LinkedHashMap<String, String>());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.getDeploymentPlanList", paramInOut);
	}
	{
	    User user = getUser();
	    user.setCurrentRole(Constants.UserGroup.SystemSupport.name());
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(user);
	    paramInOut.addIn(offset);
	    paramInOut.addIn(limit);
	    paramInOut.addIn(new LinkedHashMap<String, String>());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.getDeploymentPlanList", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(new String[] { getPlan().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.getDeploymentVParsList", paramInOut);
	}
	{
	    User user = getUser();
	    user.setCurrentRole(Constants.UserGroup.QA.name());
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(user);
	    paramInOut.addIn(new String[] { getPlan().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.getDeploymentVParsList", paramInOut);
	}
	{
	    User user = getUser();
	    user.setCurrentRole(Constants.UserGroup.SystemSupport.name());
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(user);
	    paramInOut.addIn(new String[] { getPlan().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.getDeploymentVParsList", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.getSystemLoadListBySystemId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.getCpuListBySystemId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.getTOSServerListBySystemId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getImpPlanApprovalsList().get(0));
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.saveExceptionLoadApproval", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.getLatestBuildByPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getSystemLoadActionsList());
	    paramInOut.addIn(true);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.postActivationAction", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getSystemLoadActionsList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.deleteActivationAction", paramInOut);
	}
	for (SystemLoad lSystemLoad : DataWareHouse.getPlan().getSystemLoadList()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan());
	    paramInOut.addIn(getPlan().getImplementationList().get(0));
	    paramInOut.addIn(lSystemLoad);
	    paramInOut.addIn(Constants.BUILD_TYPE.DVL_BUILD);
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("ProtectedService.buildPlanForSystem", paramInOut);
	}
	for (SystemLoad lSystemLoad : getPlan().getSystemLoadList()) {
	    for (Constants.BUILD_TYPE value : Constants.BUILD_TYPE.values()) {
		ParamInOut paramInOut = new ParamInOut();
		paramInOut.addIn(getUser());
		paramInOut.addIn(lSystemLoad.getPlanId());
		paramInOut.addIn(getPlan().getImplementationList().get(0));
		paramInOut.addIn(lSystemLoad);
		paramInOut.addIn(value);
		paramInOut.addIn("E");
		paramInOut.setOut(DataWareHouse.getNegativeResponse());
		ParameterMap.addParameterInOut("ProtectedService.createLoaderFileForSystem", paramInOut);
	    }
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(DataWareHouse.getUserSettings());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("ProtectedService.saveDelegation", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getImplementationList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.setReadyForQA", paramInOut);
	}
	for (System lSystem : DataWareHouse.getSystemList()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getProgramName().substring(0, 3));
	    paramInOut.addIn(Constants.APP_DATE_TIME_FORMAT.get().format(getPlan().getSystemLoadList().get(0).getLoadDateTime()));
	    paramInOut.addIn(lSystem.getId());
	    paramInOut.addIn(offset);
	    paramInOut.addIn(limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("ProtectedService.getSharedObjects", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getProgramName().substring(0, 3));
	    paramInOut.addIn(Constants.APP_DATE_TIME_FORMAT.get().format(getPlan().getSystemLoadList().get(0).getLoadDateTime()));
	    paramInOut.addIn(Integer.valueOf(10));
	    paramInOut.addIn(offset);
	    paramInOut.addIn(limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("ProtectedService.getSharedObjects", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn(getPlan().getSystemLoadList());
	    paramInOut.addIn(Constants.BUILD_TYPE.DVL_BUILD);
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("ProtectedService.buildPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn(Constants.BUILD_TYPE.DVL_BUILD);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.cancelBuild", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn(Constants.BUILD_TYPE.DVL_BUILD);
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("ProtectedService.cancelBuild", paramInOut);
	}

	for (Constants.BUILD_TYPE value : Constants.BUILD_TYPE.values()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn("E");
	    paramInOut.addIn(value);
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("ProtectedService.createLoaderFile", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getSystemLoadList().get(0).getSystemId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.updateSystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("ProtectedService.updatePlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.getSettingsList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn(getPlan().getSystemLoadList().get(0).getSystemId().getName());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("ProtectedService.retriveBuildLog", paramInOut);
	}
	for (System lSystem : DataWareHouse.getSystemList()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getImplementationList().get(0));
	    paramInOut.addIn(lSystem);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("ProtectedService.compilerControlValidationForSystem", paramInOut);
	}
    }
}
