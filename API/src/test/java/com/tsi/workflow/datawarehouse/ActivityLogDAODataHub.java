package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;
import static com.tsi.workflow.DataWareHouse.getUser;
import static com.tsi.workflow.DataWareHouse.offset;

import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import java.util.Arrays;

public class ActivityLogDAODataHub {

    public static void init() {
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getActivityLogList().get(0));
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.save", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.setOut(getPlan().getActivityLogList());
	    ParameterMap.addParameterInOut("ActivityLogDAO.findByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { getPlan().getId() });
	    paramInOut.setOut(getPlan().getActivityLogList());
	    ParameterMap.addParameterInOut("ActivityLogDAO.findByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn("check");
	    paramInOut.addIn(offset);
	    paramInOut.addIn(getPlan().getActivityLogList().size());
	    paramInOut.addIn(null);
	    paramInOut.setOut(getPlan().getActivityLogList());
	    ParameterMap.addParameterInOut("ActivityLogDAO.findByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(Arrays.asList(getPlan()).toArray());
	    paramInOut.setOut(getPlan().getActivityLogList());
	    ParameterMap.addParameterInOut("ActivityLogDAO.findByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan());
	    paramInOut.setOut(getPlan().getActivityLogList());
	    ParameterMap.addParameterInOut("ActivityLogDAO.findByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getImplementationList().get(0).getId());
	    paramInOut.setOut(getPlan().getImplementationList().get(0).getActivityLogList());
	    ParameterMap.addParameterInOut("ActivityLogDAO.findByImpId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(Arrays.asList(getPlan().getImplementationList().get(0).getId()).toArray());
	    paramInOut.setOut(getPlan().getImplementationList().get(0).getActivityLogList());
	    ParameterMap.addParameterInOut("ActivityLogDAO.findByImpId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getImplementationList().get(0));
	    paramInOut.setOut(getPlan().getImplementationList().get(0).getActivityLogList());
	    ParameterMap.addParameterInOut("ActivityLogDAO.findByImpId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(Arrays.asList(getPlan().getImplementationList().get(0)));
	    paramInOut.setOut(getPlan().getImplementationList().get(0).getActivityLogList());
	    ParameterMap.addParameterInOut("ActivityLogDAO.findByImpId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn("");
	    paramInOut.setOut(new Long(getPlan().getActivityLogList().size()));
	    ParameterMap.addParameterInOut("ActivityLogDAO.countByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(new Long(getPlan().getActivityLogList().size()));
	    ParameterMap.addParameterInOut("ActivityLogDAO.count", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.count", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getActivityLogList().get(0));
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.update", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getActivityLogList().get(0).getId());
	    paramInOut.addIn(getPlan().getActivityLogList().get(0));
	    paramInOut.setOut(getPlan().getActivityLogList().get(0));
	    ParameterMap.addParameterInOut("ActivityLogDAO.find", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(getPlan().getActivityLogList().get(0));
	    ParameterMap.addParameterInOut("ActivityLogDAO.find", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.find", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getActivityLogList().get(0).getId());
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.delete", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getActivityLogList().get(0));
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.delete", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.delete", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getUser());
	    paramInOut.addIn(getPlan().getActivityLogList().get(0));
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.save", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.findAll", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.findAll", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.findAll", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.findAll", paramInOut);
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
	    ParameterMap.addParameterInOut("ActivityLogDAO.findAll", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.findAll", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.findAll", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.findAll", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.findAll", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.findAll", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.findAll", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.findAll", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.findAll", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.findAll", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.findAll", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.hardDelete", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.hardDelete", paramInOut);
	}
	{

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("ActivityLogDAO.hardDelete", paramInOut);
	}
    }
}
