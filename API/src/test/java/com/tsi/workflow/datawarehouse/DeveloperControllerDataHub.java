package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPositiveResponse;
import static com.tsi.workflow.DataWareHouse.limit;
import static com.tsi.workflow.DataWareHouse.offset;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.Arrays;

public class DeveloperControllerDataHub {

    public static void init() {
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(limit);
	    paramInOut.addIn(offset);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperController.getImplementation", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("DeveloperController.setDeveloperService", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0));
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperController.saveImplementation", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0));
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperController.updateImplementation", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(Arrays.asList(DataWareHouse.getGitSearchResult()));
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.setOut(getPositiveResponse());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperController.createSourceArtifact", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperController.requestPeerReview", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(limit);
	    paramInOut.addIn(offset);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperController.listImplementations", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn(Arrays.asList(DataWareHouse.getGitSearchResult()));
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperController.populateIBMSegment", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getProgramName());
	    paramInOut.addIn(Constants.FILE_SEARCH_PRODFLAG);
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperController.fileSearch", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getProgramName());
	    paramInOut.addIn(Constants.FILE_SEARCH_NONPRODFLAG);
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperController.fileSearch", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getProgramName());
	    paramInOut.addIn(Constants.FILE_SEARCH_IBMVANILLA);
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperController.fileSearch", paramInOut);
	}
	// {
	// ParamInOut paramInOut = new ParamInOut();
	// paramInOut.addIn(null);
	// paramInOut.addIn(null);
	// paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getProgramName());
	// paramInOut.addIn("Not Valid");
	// paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	// paramInOut.setOut(DataWareHouse.getPositiveResponse());
	// ParameterMap.addParameterInOut("DeveloperController.fileSearch", paramInOut);
	// }

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(Arrays.asList(DataWareHouse.getGitSearchResult()));
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperController.checkoutFile", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(Arrays.asList(DataWareHouse.getGitSearchResult()));
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn("Commit Message");
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperController.commit", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperController.checkin", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn("origin");
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperController.getLatest", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.getPlan().getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperController.listTestCases", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getProgramName());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperController.deleteTestCase", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getProgramName());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperController.downloadTestCase", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getId());
	    paramInOut.addIn(new ArrayList());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperController.uploadTestCase", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperController.deleteFile", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperController.createWorkspace", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(limit);
	    paramInOut.addIn(offset);
	    paramInOut.addIn(null);
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperController.getActivityLogList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperController.handleException", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("DeveloperController.getCurrentUser", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperController.handleWorkflowException", paramInOut);
	}
    }
}
