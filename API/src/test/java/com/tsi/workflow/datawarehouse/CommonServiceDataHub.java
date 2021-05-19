package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.utils.Constants;
import java.text.MessageFormat;

public class CommonServiceDataHub {

    public static void init() {
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { DataWareHouse.getPlan().getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getFileTypeByPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("CommonService.getFileTypeByPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(getPlan().getSystemLoadList().get(0).getSystemId().getName());
	    String lExpected = MessageFormat.format("{0}_{1}_{2} {3} {0}_{1}_{2} {0}_{1}_{2}", getPlan().getId(), getPlan().getSystemLoadList().get(0).getSystemId().getName(), Constants.JENKINS_DATEFORMAT.get().format(getPlan().getSystemLoadList().get(0).getLoadDateTime()), getPlan().getSystemLoadList().get(0).getPutLevelId().getPutLevel());
	    paramInOut.setOut("");
	    ParameterMap.addParameterInOut("CommonService.getLocalConfigDetails", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn(getPlan().getSystemLoadList().get(0).getSystemId().getName());
	    String lExpected = MessageFormat.format("{0}_{1}_{2} {3} {0}_{1}_{2},{0}_{2} {0}_{1}_{2}", getPlan().getId(), getPlan().getSystemLoadList().get(0).getSystemId().getName(), Constants.JENKINS_DATEFORMAT.get().format(getPlan().getSystemLoadList().get(0).getLoadDateTime()), getPlan().getSystemLoadList().get(0).getPutLevelId().getPutLevel());
	    paramInOut.setOut(lExpected);
	    ParameterMap.addParameterInOut("CommonService.getLocalConfigDetails", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn(getPlan().getCheckoutSegmentsList().get(0).getProgramName());
	    paramInOut.setOut(getPlan().getImplementationList().get(0).getId() + "|NODATA");
	    ParameterMap.addParameterInOut("CommonService.getFileSyncInfo", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(getPlan().getCheckoutSegmentsList().get(0).getProgramName());
	    paramInOut.setOut("");
	    ParameterMap.addParameterInOut("CommonService.getFileSyncInfo", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn(null);
	    paramInOut.setOut(getPlan().getImplementationList().get(0).getId() + "|NODATA");
	    ParameterMap.addParameterInOut("CommonService.getFileSyncInfo", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSegmentMapping", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getImplementation", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getPlanApprovalsList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSegmentMappingList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getImplementationList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getLoadFreezeList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getLoadCategoryList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getLoadWindowList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSystemLoadList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getImplementationByPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getPlanApprovalsByPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSystemList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getVparsList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getBuildByPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getBuildBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getPlanByProject", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getVparsBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getProblemTicket", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getDbcrByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getDbcrList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getDbcr", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonService.setBuildDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonService.setImpPlanDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonService.setLoadFreezeDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonService.setLoadWindowDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonService.setPlatformDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonService.setProjectDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonService.setPutLevelDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getActivityLog", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSystemLoadActions", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getDbcrBySystemId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getProductionLoadsByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getProductionLoadsBySystemId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getProductionLoadsBySystemLoadId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getProductionLoadsList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getProductionLoads", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonService.setActivityLogDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonService.setImpPlanApprovalsDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonService.setCheckoutSegmentsDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonService.setImplementationDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonService.setLoadCategoriesDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonService.setProblemTicketDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonService.setSystemLoadActionsDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonService.setProductionLoadsDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getLoadWindow", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getPlanApprovals", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getLoadFreeze", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getLoadCategory", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getBuild", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getPlatform", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getProject", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getPutLevel", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSystemLoad", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getVpars", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getBuildList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getPlanList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getPlatformList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getProjectList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getPutLevelList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSegmentMappingByImplementation", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSystemLoadActionsBySystemLoadId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSegmentMappingByPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSegmentMappingBySystemLoad", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getLoadCategoriesBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getLoadFreezeByLoadCategories", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getLoadWindowByLoadCategories", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getPutLevelBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSystemByPlatform", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSystemLoadByLoadCategories", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSystemLoadByPutLevel", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSystemLoadByPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSystemLoadBySystem", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSystemListByPlan", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getActivityLogByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getActivityLogByImpId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getActivityLogList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSystemLoadActionsBySystemId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSystemLoadActionsByPlanId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSystemLoadActionsByVparId", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("CommonService.getSystemLoadActionsList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonService.setSystemDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonService.setSystemLoadDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonService.setVparsDAO", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CommonService.setDbcrDAO", paramInOut);
	}
    }
}
