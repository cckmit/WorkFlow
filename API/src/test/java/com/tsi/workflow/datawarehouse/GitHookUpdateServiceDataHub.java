package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;
import static com.tsi.workflow.DataWareHouse.getPositiveResponse;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;

public class GitHookUpdateServiceDataHub {

    public static void init() {
	ParamInOut paramInOut = new ParamInOut();
	paramInOut.addIn(getPlan().getId());
	paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getRefStatus());
	paramInOut.addIn(DataWareHouse.getPlan().getCreatedDt());
	paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getCommitId());
	paramInOut.addIn(DataWareHouse.getPlan().getCreatedDt());
	paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getCommitId());
	paramInOut.addIn(DataWareHouse.getPlan().getCreatedDt());
	paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getCommitId());
	paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getProgramName());
	paramInOut.addIn(DataWareHouse.getSystemList().get(0).getName());
	paramInOut.addIn(Boolean.TRUE);
	paramInOut.setOut(DataWareHouse.getPositiveResponse());
	ParameterMap.addParameterInOut("GitHookUpdateService.gitDbUpdateProcess", paramInOut);

    }

}
