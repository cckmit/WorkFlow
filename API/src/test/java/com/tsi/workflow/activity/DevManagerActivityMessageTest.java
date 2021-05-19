
package com.tsi.workflow.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import com.tsi.workflow.User;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.System;
import org.apache.log4j.Priority;
import org.junit.Test;

public class DevManagerActivityMessageTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	Implementation implementation0 = new Implementation("7");
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage((ImpPlan) null, implementation0);
	devManagerActivityMessage0.setErrorMessage("y%#%R$/(o9$O");
	assertEquals("y%#%R$/(o9$O", devManagerActivityMessage0.getErrorMessage());
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	Implementation implementation0 = new Implementation("7");
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage((ImpPlan) null, implementation0);
	System system0 = new System();
	devManagerActivityMessage0.setSystem(system0);
	System system1 = devManagerActivityMessage0.getSystem();
	assertNull(system1.getLoadsetNamePrefix());
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("Z*c>-pOP");
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage(impPlan0, (Implementation) null);
	devManagerActivityMessage0.setQaRegressionComment("]];]0@v%s(e`|Kee");
	String string0 = devManagerActivityMessage0.getQaRegressionComment();
	assertEquals("]];]0@v%s(e`|Kee", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	Implementation implementation0 = new Implementation("7");
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage((ImpPlan) null, implementation0);
	devManagerActivityMessage0.setQaRegressionComment("");
	String string0 = devManagerActivityMessage0.getQaRegressionComment();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage((ImpPlan) null, (Implementation) null);
	devManagerActivityMessage0.setQaFunctionalComment("Lead {0} has initiated submit for the implementation plan {1}");
	String string0 = devManagerActivityMessage0.getQaFunctionalComment();
	assertEquals("Lead {0} has initiated submit for the implementation plan {1}", string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	Implementation implementation0 = new Implementation();
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage(impPlan0, implementation0);
	devManagerActivityMessage0.setQaFunctionalComment("");
	String string0 = devManagerActivityMessage0.getQaFunctionalComment();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage((ImpPlan) null, (Implementation) null);
	devManagerActivityMessage0.errorMessage = "+vVrF3dBNy/UDIx";
	String string0 = devManagerActivityMessage0.getErrorMessage();
	assertEquals("+vVrF3dBNy/UDIx", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage((ImpPlan) null, (Implementation) null, (IBeans[]) null);
	devManagerActivityMessage0.errorMessage = "";
	String string0 = devManagerActivityMessage0.getErrorMessage();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage((ImpPlan) null, (Implementation) null, (IBeans[]) null);
	// Undeclared exception!
	try {
	    devManagerActivityMessage0.setArguments((IBeans[]) null);
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	}
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("X09[U.]");
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage(impPlan0, (Implementation) null);
	IBeans[] iBeansArray0 = new IBeans[0];
	// Undeclared exception!
	try {
	    devManagerActivityMessage0.setArguments(iBeansArray0);
	    fail("Expecting exception: ArrayIndexOutOfBoundsException");

	} catch (ArrayIndexOutOfBoundsException e) {
	}
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("Z*c>-pOP");
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage(impPlan0, (Implementation) null);
	User user0 = new User();
	devManagerActivityMessage0.setUser(user0);
	devManagerActivityMessage0.setQaFunctionalComment("{8W)");
	// Undeclared exception!
	try {
	    devManagerActivityMessage0.processMessage();
	    fail("Expecting exception: IllegalArgumentException");

	} catch (IllegalArgumentException e) {
	}
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan((String) null);
	Implementation implementation0 = new Implementation("");
	IBeans[] iBeansArray0 = new IBeans[0];
	DevManagerActivityMessage devManagerActivityMessage0 = null;
	try {
	    devManagerActivityMessage0 = new DevManagerActivityMessage(impPlan0, implementation0, iBeansArray0);
	    fail("Expecting exception: ArrayIndexOutOfBoundsException");

	} catch (ArrayIndexOutOfBoundsException e) {
	}
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	IBeans[] iBeansArray0 = new IBeans[7];
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage((ImpPlan) null, (Implementation) null, iBeansArray0);
	devManagerActivityMessage0.setArguments(iBeansArray0);
	assertNull(devManagerActivityMessage0.getQaRegressionComment());
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("Z*c>-pOP");
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage(impPlan0, (Implementation) null);
	User user0 = new User();
	devManagerActivityMessage0.setUser(user0);
	System system0 = new System();
	devManagerActivityMessage0.system = system0;
	String string0 = devManagerActivityMessage0.processMessage();
	assertEquals("Lead null has initiated submit for the implementation plan Z*c>-pOP, system null", string0);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("Z*c>-pOP");
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage(impPlan0, (Implementation) null);
	User user0 = new User();
	user0.setCurrentDelegatedUser(user0);
	devManagerActivityMessage0.setUser(user0);
	String string0 = devManagerActivityMessage0.processMessage();
	assertEquals("Lead null has initiated submit for the implementation plan Z*c>-pOP, on behalf of null", string0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("Z*c>-pOP");
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage(impPlan0, (Implementation) null);
	devManagerActivityMessage0.setQaFunctionalComment("jn[OJn[~}jO5`#cdRy");
	devManagerActivityMessage0.setQaRegressionComment("]];]0@v%s(e`|Kee");
	// Undeclared exception!
	try {
	    devManagerActivityMessage0.processMessage();
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	}
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	Implementation implementation0 = new Implementation();
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage((ImpPlan) null, implementation0);
	devManagerActivityMessage0.setQaRegressionComment("");
	// Undeclared exception!
	try {
	    devManagerActivityMessage0.processMessage();
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	}
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("Z*c>-pOP");
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage(impPlan0, (Implementation) null);
	devManagerActivityMessage0.setQaRegressionComment("]];]0@v%s(e`|Kee");
	// Undeclared exception!
	try {
	    devManagerActivityMessage0.processMessage();
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	}
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation((String) null);
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage(impPlan0, implementation0);
	devManagerActivityMessage0.setQaFunctionalComment("");
	// Undeclared exception!
	try {
	    devManagerActivityMessage0.processMessage();
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	}
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	IBeans[] iBeansArray0 = new IBeans[7];
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage((ImpPlan) null, (Implementation) null, iBeansArray0);
	String string0 = devManagerActivityMessage0.getErrorMessage();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("Z*c>-pOP");
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage(impPlan0, (Implementation) null);
	Priority priority0 = devManagerActivityMessage0.getLogLevel();
	assertEquals(20000, Priority.INFO_INT);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	IBeans[] iBeansArray0 = new IBeans[7];
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage((ImpPlan) null, (Implementation) null, iBeansArray0);
	String string0 = devManagerActivityMessage0.getQaFunctionalComment();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("Z*c>-pOP");
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage(impPlan0, (Implementation) null);
	System system0 = devManagerActivityMessage0.getSystem();
	assertNull(system0);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	IBeans[] iBeansArray0 = new IBeans[7];
	DevManagerActivityMessage devManagerActivityMessage0 = new DevManagerActivityMessage((ImpPlan) null, (Implementation) null, iBeansArray0);
	String string0 = devManagerActivityMessage0.getQaRegressionComment();
	assertNull(string0);
    }
}
