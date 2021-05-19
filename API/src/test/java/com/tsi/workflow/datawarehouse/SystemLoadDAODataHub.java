package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;
import static com.tsi.workflow.DataWareHouse.limit;
import static com.tsi.workflow.DataWareHouse.offset;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import java.util.Arrays;

public class SystemLoadDAODataHub {

    private static SystemLoad lSystemLoad = new SystemLoad();

    public static void init() {

	lSystemLoad = getPlan().getSystemLoadList().get(0);

	for (System lSystem : DataWareHouse.getSystemList()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan());
	    paramInOut.addIn(lSystem);
	    paramInOut.setOut(lSystemLoad);
	    ParameterMap.addParameterInOut("SystemLoadDAO.find", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { lSystemLoad.getLoadCategoryId().getId() });
	    paramInOut.setOut(Arrays.asList(lSystemLoad));
	    ParameterMap.addParameterInOut("SystemLoadDAO.findByLoadCategories", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lSystemLoad.getLoadCategoryId().getId());
	    paramInOut.setOut(Arrays.asList(lSystemLoad));
	    ParameterMap.addParameterInOut("SystemLoadDAO.findByLoadCategories", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lSystemLoad);
	    paramInOut.setOut(Arrays.asList(lSystemLoad));
	    ParameterMap.addParameterInOut("SystemLoadDAO.findByLoadCategories", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(Arrays.asList(lSystemLoad));
	    paramInOut.setOut(Arrays.asList(lSystemLoad));
	    ParameterMap.addParameterInOut("SystemLoadDAO.findByLoadCategories", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lSystemLoad.getSystemId().getId());
	    paramInOut.addIn(getPlan().getPlanStatus());
	    paramInOut.addIn(offset);
	    paramInOut.addIn(limit);
	    paramInOut.setOut(Arrays.asList(lSystemLoad));
	    ParameterMap.addParameterInOut("SystemLoadDAO.findBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(Arrays.asList(lSystemLoad.getSystemId()));
	    paramInOut.setOut(Arrays.asList(lSystemLoad));
	    ParameterMap.addParameterInOut("SystemLoadDAO.findBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.findBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.findBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.findBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.findBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.setOut(Arrays.asList(lSystemLoad));
	    ParameterMap.addParameterInOut("SystemLoadDAO.findByImpPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { getPlan().getId() });
	    paramInOut.setOut(Arrays.asList(lSystemLoad));
	    ParameterMap.addParameterInOut("SystemLoadDAO.findByImpPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan());
	    paramInOut.setOut(Arrays.asList(lSystemLoad));
	    ParameterMap.addParameterInOut("SystemLoadDAO.findByImpPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(Arrays.asList(getPlan()));
	    paramInOut.setOut(Arrays.asList(lSystemLoad));
	    ParameterMap.addParameterInOut("SystemLoadDAO.findByImpPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lSystemLoad.getPutLevelId().getId());
	    paramInOut.setOut(Arrays.asList(lSystemLoad));
	    ParameterMap.addParameterInOut("SystemLoadDAO.findByPutLevel", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { lSystemLoad.getPutLevelId().getId() });
	    paramInOut.setOut(Arrays.asList(lSystemLoad));
	    ParameterMap.addParameterInOut("SystemLoadDAO.findByPutLevel", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lSystemLoad.getPutLevelId());
	    paramInOut.setOut(Arrays.asList(lSystemLoad));
	    ParameterMap.addParameterInOut("SystemLoadDAO.findByPutLevel", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(Arrays.asList(lSystemLoad.getPutLevelId()));
	    paramInOut.setOut(Arrays.asList(lSystemLoad));
	    ParameterMap.addParameterInOut("SystemLoadDAO.findByPutLevel", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan());
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId());
	    paramInOut.setOut(DataWareHouse.getPlan().getSystemLoadList().get(0));
	    ParameterMap.addParameterInOut("SystemLoadDAO.findBy", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.getCountBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lSystemLoad.getSystemId().getId());
	    paramInOut.addIn(getPlan().getPlanStatus());
	    paramInOut.setOut(new Long(1));
	    ParameterMap.addParameterInOut("SystemLoadDAO.getCountBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan());
	    Integer count = getPlan().getSystemLoadList().size();
	    paramInOut.setOut(count.longValue());
	    ParameterMap.addParameterInOut("SystemLoadDAO.countByImpPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.count", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.count", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.update", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lSystemLoad.getId());
	    paramInOut.setOut(lSystemLoad);
	    ParameterMap.addParameterInOut("SystemLoadDAO.find", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.delete", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.delete", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.delete", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.save", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.findAll", paramInOut);
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
	    ParameterMap.addParameterInOut("SystemLoadDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.findAll", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.findE", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.hardDelete", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.hardDelete", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("SystemLoadDAO.hardDelete", paramInOut);
	}
    }
}
