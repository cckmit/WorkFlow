package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.utils.Constants;
import java.text.MessageFormat;

public class CommonControllerDataHub {

    public static void init() {
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { DataWareHouse.getPlan().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getFileTypeByPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.getPlan().getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getName());
	    String lExpected = MessageFormat.format("{0}_{1}_{2} {3} {0}_{1}_{2} {0}_{1}_{2}", getPlan().getId(), getPlan().getSystemLoadList().get(0).getSystemId().getName(), Constants.JENKINS_DATEFORMAT.get().format(getPlan().getSystemLoadList().get(0).getLoadDateTime()), getPlan().getSystemLoadList().get(0).getPutLevelId().getPutLevel());
	    paramInOut.setOut(lExpected);
	    ParameterMap.addParameterInOut("CommonController.getLocalConfigDetails", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser().getRole().iterator().next());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getUsersByRole", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonController.setCommonService", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonController.setlDAPService", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonController.setCommonBaseService", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getCheckoutSegmentsList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSegmentMapping", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getImplementation", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.offset);
	    paramInOut.addIn(DataWareHouse.limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getPlanApprovalsList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.offset);
	    paramInOut.addIn(DataWareHouse.limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSegmentMappingList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.offset);
	    paramInOut.addIn(DataWareHouse.limit);
	    paramInOut.addIn(null);
	    paramInOut.addIn("");
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getImplementationList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.offset);
	    paramInOut.addIn(DataWareHouse.limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getLoadFreezeList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.offset);
	    paramInOut.addIn(DataWareHouse.limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getLoadCategoryList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.offset);
	    paramInOut.addIn(DataWareHouse.limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getLoadWindowList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.offset);
	    paramInOut.addIn(DataWareHouse.limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSystemLoadList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { DataWareHouse.getPlan().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getImplementationByPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { DataWareHouse.getPlan().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getPlanApprovalsByPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.offset);
	    paramInOut.addIn(DataWareHouse.limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSystemList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.offset);
	    paramInOut.addIn(DataWareHouse.limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getVparsList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { DataWareHouse.getPlan().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getBuildByPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getBuildBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getProjectId().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getPlanByProject", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getVparsBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getProblemTicket().getRefNum());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getProblemTicket", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { DataWareHouse.getPlan().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getDbcrByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.offset);
	    paramInOut.addIn(DataWareHouse.limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getDbcrList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getDbcrList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getDbcr", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getActivityLogList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getActivityLog", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSystemLoadActions", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getDbcrList().get(0).getSystemId().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getDbcrBySystemId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { DataWareHouse.getPlan().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getProductionLoadsByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getProductionLoadsBySystemId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getSystemLoadList().get(0).getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getProductionLoadsBySystemLoadId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.offset);
	    paramInOut.addIn(DataWareHouse.limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getProductionLoadsList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getProductionLoadsList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getProductionLoads", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadCategoryId().getLoadWindowList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getLoadWindow", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getImpPlanApprovalsList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getPlanApprovals", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadCategoryId().getLoadFreezeList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getLoadFreeze", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadCategoryId().getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getLoadCategory", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getBuildList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getBuild", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getPlatformId().getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getPlatform", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getProjectId().getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getProject", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadList().get(0).getPutLevelId().getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getPutLevel", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSystemLoad", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getVparsList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getVpars", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.offset);
	    paramInOut.addIn(DataWareHouse.limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getBuildList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.offset);
	    paramInOut.addIn(DataWareHouse.limit);
	    paramInOut.addIn(null);
	    paramInOut.addIn("");
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getPlanList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.offset);
	    paramInOut.addIn(DataWareHouse.limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getPlatformList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.offset);
	    paramInOut.addIn(DataWareHouse.limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getProjectList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.offset);
	    paramInOut.addIn(DataWareHouse.limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getPutLevelList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { DataWareHouse.getPlan().getImplementationList().get(0).getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSegmentMappingByImplementation", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getSystemLoadList().get(0).getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSystemLoadActionsBySystemLoadId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { DataWareHouse.getPlan().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSegmentMappingByPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getSystemLoadList().get(0).getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSegmentMappingBySystemLoad", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getLoadCategoriesBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadCategoryId().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getLoadFreezeByLoadCategories", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadCategoryId().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getLoadWindowByLoadCategories", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getPutLevelBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getPlatformId().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSystemByPlatform", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadCategoryId().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSystemLoadByLoadCategories", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getSystemLoadList().get(0).getPutLevelId().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSystemLoadByPutLevel", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { DataWareHouse.getPlan().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSystemLoadByPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSystemLoadBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSystemListByPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { DataWareHouse.getPlan().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getActivityLogByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { DataWareHouse.getPlan().getImplementationList().get(0).getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getActivityLogByImpId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.offset);
	    paramInOut.addIn(DataWareHouse.limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getActivityLogList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getSystemId().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSystemLoadActionsBySystemId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { DataWareHouse.getPlan().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSystemLoadActionsByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getVparId().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSystemLoadActionsByVparId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.offset);
	    paramInOut.addIn(DataWareHouse.limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.getSystemLoadActionsList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(new Exception("Exception Log"));
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.handleException", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonController.getCurrentUser", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(new Exception("Exception Log"));
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonController.handleWorkflowException", paramInOut);
	}
    }
}
