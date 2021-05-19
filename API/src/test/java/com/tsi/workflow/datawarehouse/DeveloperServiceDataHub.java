package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPositiveResponse;
import static com.tsi.workflow.DataWareHouse.limit;
import static com.tsi.workflow.DataWareHouse.offset;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.git.GitBranchSearchResult;
import com.tsi.workflow.git.GitSearchResult;
import com.tsi.workflow.utils.Constants;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;

public class DeveloperServiceDataHub {

    public static void init() {
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(offset);
	    paramInOut.addIn(limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperService.getImplementationList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0));
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperService.saveImplementation", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0));
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperService.updateImplementation", paramInOut);
	}
	{
	    Implementation lImp = new Implementation();
	    try {
		BeanUtils.copyProperties(lImp, DataWareHouse.getPlan().getImplementationList().get(0));
	    } catch (IllegalAccessException ex) {
		Logger.getLogger(DeveloperServiceDataHub.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (InvocationTargetException ex) {
		Logger.getLogger(DeveloperServiceDataHub.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    lImp.setSubstatus(Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name());
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(lImp);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperService.updateImplementation", paramInOut);
	}
	{
	    Implementation lImp = new Implementation();
	    try {
		BeanUtils.copyProperties(lImp, DataWareHouse.getPlan().getImplementationList().get(0));
	    } catch (IllegalAccessException ex) {
		Logger.getLogger(DeveloperServiceDataHub.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (InvocationTargetException ex) {
		Logger.getLogger(DeveloperServiceDataHub.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    lImp.setSubstatus(Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED.name());
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(lImp);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperService.updateImplementation", paramInOut);
	}

	{
	    GitSearchResult lGitSearchResult = DataWareHouse.getGitSearchResult();
	    for (GitBranchSearchResult branch : lGitSearchResult.getBranch()) {
		String lTargetSystem = branch.getTargetSystem();
		branch.setTargetSystem(lTargetSystem.replace("master_", ""));
	    }

	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn(Arrays.asList(lGitSearchResult));
	    paramInOut.setOut(getPositiveResponse());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperService.createSourceArtifact", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperService.requestPeerReview", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId() + "1");
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperService.requestPeerReview", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn(Arrays.asList(DataWareHouse.getGitSearchResult()));
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperService.populateIBMSegment", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getProgramName());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperService.nonProdFileSearch", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getProgramName());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperService.ibmVanillaFileSearch", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn(Arrays.asList(DataWareHouse.getGitSearchResult()));
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperService.checkoutFile", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn(Arrays.asList(DataWareHouse.getGitSearchResult()));
	    paramInOut.addIn("Commit Message");
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperService.commit", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperService.checkin", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn("Origin");
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperService.getLatest", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperService.listTestCases", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getProgramName());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperService.deleteTestCase", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getProgramName());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperService.downloadTestCase", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn(new ArrayList());
	    paramInOut.setOut(DataWareHouse.getNegativeResponse());
	    ParameterMap.addParameterInOut("DeveloperService.uploadTestCase", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(new Integer[] { DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getId() });
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperService.deleteFile", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperService.createWorkspace", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getProgramName());
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperService.prodFileSearch", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(offset);
	    paramInOut.addIn(limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperService.getUserTaskList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getUser());
	    paramInOut.addIn(offset);
	    paramInOut.addIn(limit);
	    paramInOut.addIn("{\"id\":\"asc\"}");
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperService.getUserTaskList", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.addIn(offset);
	    paramInOut.addIn(limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(DataWareHouse.getPositiveResponse());
	    ParameterMap.addParameterInOut("DeveloperService.getActivityLogList", paramInOut);
	}
    }
}
