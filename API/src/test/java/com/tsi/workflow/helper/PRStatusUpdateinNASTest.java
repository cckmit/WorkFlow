package com.tsi.workflow.helper;

import static org.junit.Assert.*;

import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.interfaces.IPRConfig;
import com.workflow.tos.PRWriter;
import org.junit.Test;

public class PRStatusUpdateinNASTest {

    @Test(timeout = 4000)
    public void test0() throws Throwable {
	PRNumberHelper pRStatusUpdateinNAS0 = new PRNumberHelper();
	try {
	    pRStatusUpdateinNAS0.writeFileInNAS((ImpPlan) null);
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	    //
	    // no message in exception (getMessage() returned null)
	    //
	}
    }

    @Test(timeout = 4000)
    public void test1() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	impPlan0.setSdmTktNum("");
	PRNumberHelper pRStatusUpdateinNAS0 = new PRNumberHelper();
	PRWriter pRWriter0 = new PRWriter((IPRConfig) null);
	pRStatusUpdateinNAS0.writeFileInNAS(impPlan0);
	assertNull(impPlan0.getLoadAttendee());
    }

    @Test(timeout = 4000)
    public void test2() throws Throwable {
	PRNumberHelper pRStatusUpdateinNAS0 = new PRNumberHelper();
	PRWriter pRWriter0 = new PRWriter((IPRConfig) null);
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setSdmTktNum("S");
	pRStatusUpdateinNAS0.writeFileInNAS(impPlan0);
	assertNull(impPlan0.getId());
    }

    @Test(timeout = 4000)
    public void test3() throws Throwable {
	PRNumberHelper pRStatusUpdateinNAS0 = new PRNumberHelper();
	PRWriter pRWriter0 = new PRWriter((IPRConfig) null);
	ImpPlan impPlan0 = new ImpPlan();
	pRStatusUpdateinNAS0.writeFileInNAS(impPlan0);
	assertNull(impPlan0.getDevMgrComment());
    }
}
