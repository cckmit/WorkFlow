package com.tsi.workflow.datawarehouse;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.LoadCategoriesForm;
import com.tsi.workflow.beans.ui.LoadFreezeForm;

public class LoadsControlServiceDataHub {

    public static void init() {
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("LoadsControlService.getUserTaskList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(new LoadCategoriesForm(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadCategoryId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadCategoryId().getLoadWindowList()));
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("LoadsControlService.saveLoadCategory", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(new LoadFreezeForm(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadCategoryId().getLoadFreezeList().get(0), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId()));
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("LoadsControlService.saveLoadFreeze", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadCategoryId().getLoadFreezeList().get(0));
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("LoadsControlService.updateLoadFreeze", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadCategoryId().getLoadFreezeList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("LoadsControlService.deleteLoadFreeze", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadList().get(0).getPutLevelId());
	    paramInOut.addIn(Boolean.TRUE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("LoadsControlService.savePutLevel", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadList().get(0).getPutLevelId());
	    paramInOut.addIn(Boolean.TRUE);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("LoadsControlService.updatePutLevel", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadList().get(0).getPutLevelId().getId());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("LoadsControlService.deletePutLevel", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("LoadsControlService.getLoadHistory", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(new LoadCategoriesForm(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadCategoryId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadCategoryId().getLoadWindowList()));
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("LoadsControlService.updateLoadCategory", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadCategoryId().getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("LoadsControlService.deleteLoadCategory", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(new String[] { DataWareHouse.getPlan().getId() });
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("LoadsControlService.readyForProdDeploy", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("LoadsControlService.getAllLoadCategoryList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("LoadsControlService.getProductionLoadsHistory", paramInOut);
	}
	for (System lSystem : DataWareHouse.getSystemList()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getSystemList().get(0).getId());
	    paramInOut.addIn(2017);
	    paramInOut.addIn(11);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("LoadsControlService.getLoadFreezeDateByMonth", paramInOut);
	}
	for (System lSystem : DataWareHouse.getSystemList()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getSystemList().get(0).getId());
	    paramInOut.addIn("2017-10-10");
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("LoadsControlService.getLoadCategoriesByDate", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn("");
	    paramInOut.addIn(DataWareHouse.getSystemList().get(0).getId());
	    paramInOut.addIn("2017-10-1012");
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("LoadsControlService.getLoadCategoriesByDate", paramInOut);
	}
	for (SystemLoad lSystemLoad : DataWareHouse.getPlan().getSystemLoadList()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(lSystemLoad.getLoadCategoryId().getId());
	    paramInOut.addIn("2017-10-10");
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("LoadsControlService.getLoadWindowByDay", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn("");
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadCategoryId().getId());
	    paramInOut.addIn("2017-10-101");
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("LoadsControlService.getLoadWindowByDay", paramInOut);
	}

    }
}
