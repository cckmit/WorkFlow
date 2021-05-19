package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;
import static com.tsi.workflow.DataWareHouse.implementationList;
import static com.tsi.workflow.DataWareHouse.limit;
import static com.tsi.workflow.DataWareHouse.offset;
import static com.tsi.workflow.DataWareHouse.planList;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.SystemLoad;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class CheckoutSegmentsDAODataHub {

    private static Implementation lImplementation = new Implementation();
    private static SystemLoad lSystemLoad = new SystemLoad();

    public static void init() {

	lImplementation = getPlan().getImplementationList().get(0);
	lSystemLoad = getPlan().getSystemLoadList().get(0);

	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.count", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.count", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.update", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0));
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.find", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.find", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.find", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.delete", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.delete", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.delete", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.save", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findAll", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findAll", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findAll", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findAll", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findAll", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findAll", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findAll", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findAll", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findAll", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findAll", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findAll", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findAll", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findAll", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findAll", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findE", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.hardDelete", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.hardDelete", paramInOut);
	}
	{
	    // Need to Replace
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(null);
	    paramInOut.addIn(null);
	    paramInOut.setOut(null);
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.hardDelete", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.setOut(getPlan().getCheckoutSegmentsList());
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findByImpPlan", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(planList);
	    paramInOut.setOut(getPlan().getCheckoutSegmentsList());
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findByImpPlan", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan());
	    paramInOut.setOut(getPlan().getCheckoutSegmentsList());
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findByImpPlan", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { getPlan().getId() });
	    paramInOut.setOut(getPlan().getCheckoutSegmentsList());
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findByImpPlan", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lImplementation.getId());
	    paramInOut.setOut(getPlan().getCheckoutSegmentsList());
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findByImplementation", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { lImplementation.getId() });
	    paramInOut.setOut(getPlan().getCheckoutSegmentsList());
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findByImplementation", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lImplementation);
	    paramInOut.setOut(getPlan().getCheckoutSegmentsList());
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findByImplementation", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(implementationList);
	    paramInOut.setOut(getPlan().getCheckoutSegmentsList());
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findByImplementation", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lSystemLoad);
	    paramInOut.setOut(getPlan().getCheckoutSegmentsList());
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findBySystemLoad", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(Arrays.asList(lSystemLoad));
	    paramInOut.setOut(getPlan().getCheckoutSegmentsList());
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findBySystemLoad", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new Integer[] { lSystemLoad.getId() });
	    paramInOut.setOut(getPlan().getCheckoutSegmentsList());
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findBySystemLoad", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lSystemLoad.getId());
	    paramInOut.setOut(getPlan().getCheckoutSegmentsList());
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findBySystemLoad", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getProgramName());
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    paramInOut.setOut(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList());
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findByFileName", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lImplementation.getId());
	    paramInOut.addIn("");
	    paramInOut.setOut(getPlan().getCheckoutSegmentsList());
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.getDependentSegments", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new CheckoutSegments());
	    paramInOut.setOut(getPlan().getCheckoutSegmentsList());
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.getSourceArtifact", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { "123" });
	    paramInOut.addIn(new String[] { "123" });
	    paramInOut.addIn(new Date());
	    paramInOut.addIn(lSystemLoad.getSystemId());
	    paramInOut.addIn(offset);
	    paramInOut.addIn(limit);
	    paramInOut.addIn(null);
	    paramInOut.setOut(getPlan().getCheckoutSegmentsList());
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findByFiles", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { "123" });
	    paramInOut.addIn(new String[] { "123" });
	    paramInOut.addIn(new Date());
	    paramInOut.addIn(new CheckoutSegments());
	    paramInOut.setOut(new Long(0));
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.countByFiles", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn(lSystemLoad.getSystemId().getName());
	    paramInOut.setOut(new Long(1));
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.segmentCount", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(new String[] { "123" });
	    paramInOut.setOut(new HashMap<String, List<String>>());
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.getFileTypesByPlan", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getProgramName());
	    paramInOut.addIn(lImplementation.getId());
	    paramInOut.addIn(lSystemLoad.getSystemId().getName());
	    paramInOut.setOut(getPlan().getCheckoutSegmentsList());
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findByFileName", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getProgramName());
	    paramInOut.addIn(lImplementation.getId());
	    paramInOut.setOut(getPlan().getCheckoutSegmentsList());
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findByFileNameAndImpl", paramInOut);
	}

	{
	    Integer offset = 0;
	    Integer limit = 100;
	    LinkedHashMap<String, String> lOrderBy = new LinkedHashMap();
	    lOrderBy.put("programName", "asc");
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lImplementation.getId());
	    paramInOut.addIn(offset);
	    paramInOut.addIn(limit);
	    paramInOut.addIn(lOrderBy);
	    paramInOut.setOut(getPlan().getCheckoutSegmentsList());
	    ParameterMap.addParameterInOut("CheckoutSegmentsDAO.findByImplementation", paramInOut);
	}

    }

}
