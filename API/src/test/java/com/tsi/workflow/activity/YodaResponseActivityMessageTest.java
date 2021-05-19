
package com.tsi.workflow.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.ui.YodaResult;
import com.tsi.workflow.utils.Constants;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class YodaResponseActivityMessageTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	YodaResult yodaResult0 = new YodaResult();
	yodaResponseActivityMessage0.setlYodaResult(yodaResult0);
	yodaResult0.setRc(32);
	// Undeclared exception!
	try {
	    yodaResponseActivityMessage0.processMessage();
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	    //
	    // no message in exception (getMessage() returned null)
	    //
	}
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	Constants.YodaActivtiyMessage constants_YodaActivtiyMessage0 = Constants.YodaActivtiyMessage.IPADDRESS;
	yodaResponseActivityMessage0.yodaActivity = constants_YodaActivtiyMessage0;
	yodaResponseActivityMessage0.systemName = "";
	YodaResult yodaResult0 = new YodaResult();
	yodaResponseActivityMessage0.setlYodaResult(yodaResult0);
	yodaResult0.setRc(32);
	String string0 = yodaResponseActivityMessage0.processMessage();
	assertEquals("Unable to get IP Address of  VPARS null, RC: 32", string0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	Constants.YodaActivtiyMessage constants_YodaActivtiyMessage0 = Constants.YodaActivtiyMessage.IPADDRESS;
	yodaResponseActivityMessage0.yodaActivity = constants_YodaActivtiyMessage0;
	yodaResponseActivityMessage0.systemName = "";
	YodaResult yodaResult0 = new YodaResult();
	yodaResponseActivityMessage0.setlYodaResult(yodaResult0);
	String string0 = yodaResponseActivityMessage0.processMessage();
	assertEquals("IP Address of  VPARS null has been successfully returned as null", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	YodaResult yodaResult0 = new YodaResult();
	yodaResult0.setRc((-782));
	yodaResponseActivityMessage0.setlYodaResult(yodaResult0);
	Constants.YodaActivtiyMessage constants_YodaActivtiyMessage0 = Constants.YodaActivtiyMessage.DEACTIVATEANDDELETE;
	yodaResponseActivityMessage0.setYodaActivity(constants_YodaActivtiyMessage0);
	String string0 = yodaResponseActivityMessage0.processMessage();
	assertEquals("Plan  Deactivate and Delete is FailedRC: -782 Message: ", string0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	YodaResult yodaResult0 = new YodaResult();
	yodaResponseActivityMessage0.setlYodaResult(yodaResult0);
	YodaResult yodaResult1 = yodaResponseActivityMessage0.getlYodaResult();
	assertNull(yodaResult1.getMessage());
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	YodaResult yodaResult0 = new YodaResult();
	yodaResult0.setRc((-782));
	yodaResponseActivityMessage0.setlYodaResult(yodaResult0);
	YodaResult yodaResult1 = yodaResponseActivityMessage0.getlYodaResult();
	assertNull(yodaResult1.getMessage());
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	Constants.YodaActivtiyMessage constants_YodaActivtiyMessage0 = Constants.YodaActivtiyMessage.IPADDRESS;
	yodaResponseActivityMessage0.yodaActivity = constants_YodaActivtiyMessage0;
	Constants.YodaActivtiyMessage constants_YodaActivtiyMessage1 = yodaResponseActivityMessage0.getYodaActivity();
	assertSame(constants_YodaActivtiyMessage1, constants_YodaActivtiyMessage0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	yodaResponseActivityMessage0.setVparName("hpp");
	String string0 = yodaResponseActivityMessage0.getVparName();
	assertEquals("hpp", string0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	yodaResponseActivityMessage0.setVparName("");
	String string0 = yodaResponseActivityMessage0.getVparName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage((ImpPlan) null, implementation0);
	yodaResponseActivityMessage0.setSystemName("uHS)>?-O8b");
	String string0 = yodaResponseActivityMessage0.getSystemName();
	assertEquals("uHS)>?-O8b", string0);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	yodaResponseActivityMessage0.systemName = "";
	String string0 = yodaResponseActivityMessage0.getSystemName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("4");
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	Level level0 = (Level) Priority.FATAL;
	yodaResponseActivityMessage0.lPriority = (Priority) level0;
	Priority priority0 = yodaResponseActivityMessage0.getLogLevel();
	assertEquals(50000, priority0.toInt());
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("E");
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	YodaResult yodaResult0 = new YodaResult();
	yodaResponseActivityMessage0.setlYodaResult(yodaResult0);
	// Undeclared exception!
	try {
	    yodaResponseActivityMessage0.processMessage();
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	    //
	    // no message in exception (getMessage() returned null)
	    //
	}
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	Constants.YodaActivtiyMessage constants_YodaActivtiyMessage0 = Constants.YodaActivtiyMessage.IPADDRESS;
	yodaResponseActivityMessage0.yodaActivity = constants_YodaActivtiyMessage0;
	YodaResult yodaResult0 = new YodaResult();
	yodaResponseActivityMessage0.setlYodaResult(yodaResult0);
	String string0 = yodaResponseActivityMessage0.processMessage();
	assertEquals("IP Address of null VPARS null has been successfully returned as null", string0);

	Priority priority0 = yodaResponseActivityMessage0.getLogLevel();
	assertEquals("INFO", priority0.toString());
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	Constants.YodaActivtiyMessage constants_YodaActivtiyMessage0 = Constants.YodaActivtiyMessage.IPADDRESS;
	yodaResponseActivityMessage0.yodaActivity = constants_YodaActivtiyMessage0;
	YodaResult yodaResult0 = new YodaResult();
	yodaResult0.setRc((-782));
	yodaResponseActivityMessage0.setlYodaResult(yodaResult0);
	String string0 = yodaResponseActivityMessage0.processMessage();
	assertEquals("Unable to get IP Address of null VPARS null, RC: -782", string0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	IBeans[] iBeansArray0 = new IBeans[1];
	yodaResponseActivityMessage0.setArguments(iBeansArray0);
	assertEquals(1, iBeansArray0.length);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	String string0 = yodaResponseActivityMessage0.getVparName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	YodaResult yodaResult0 = yodaResponseActivityMessage0.getlYodaResult();
	assertNull(yodaResult0);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	String string0 = yodaResponseActivityMessage0.getSystemName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	YodaResult yodaResult0 = new YodaResult();
	yodaResponseActivityMessage0.setlYodaResult(yodaResult0);
	yodaResult0.setRc(32);
	YodaResult yodaResult1 = yodaResponseActivityMessage0.getlYodaResult();
	assertNull(yodaResult1.getMessage());
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	Priority priority0 = yodaResponseActivityMessage0.getLogLevel();
	assertNull(priority0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	Implementation implementation0 = new Implementation();
	YodaResponseActivityMessage yodaResponseActivityMessage0 = new YodaResponseActivityMessage(impPlan0, implementation0);
	yodaResponseActivityMessage0.getYodaActivity();
    }
}
