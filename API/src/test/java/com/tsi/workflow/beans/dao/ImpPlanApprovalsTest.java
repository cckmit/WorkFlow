
package com.tsi.workflow.beans.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class ImpPlanApprovalsTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	Integer integer0 = new Integer(2);
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals(integer0);
	Date mockDate0 = new Date();
	impPlanApprovals0.setModifiedDt(mockDate0);
	Date date0 = impPlanApprovals0.getModifiedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	impPlanApprovals0.setModifiedBy("/ Y^Nw@30pTZMu\"e");
	String string0 = impPlanApprovals0.getModifiedBy();
	assertNotNull(string0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals((Integer) null);
	impPlanApprovals0.setModifiedBy("");
	String string0 = impPlanApprovals0.getModifiedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	Integer integer0 = new Integer(0);
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals(integer0);
	Integer integer1 = impPlanApprovals0.getId();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	Integer integer0 = new Integer(1);
	impPlanApprovals0.setId(integer0);
	Integer integer1 = impPlanApprovals0.getId();
	assertEquals(1, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	Integer integer0 = new Integer((-2171));
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals(integer0);
	Integer integer1 = impPlanApprovals0.getId();
	assertEquals((-2171), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals((Integer) null);
	impPlanApprovals0.setFileName("");
	String string0 = impPlanApprovals0.getFileName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	Date mockDate0 = new Date();
	impPlanApprovals0.setCreatedDt(mockDate0);
	Date date0 = impPlanApprovals0.getCreatedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	impPlanApprovals0.setCreatedBy("com.tsi.workflow.beans.dao.ImpPlanApprovals[ id=null ]");
	String string0 = impPlanApprovals0.getCreatedBy();
	assertEquals("com.tsi.workflow.beans.dao.ImpPlanApprovals[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	Integer integer0 = new Integer(0);
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals(integer0);
	impPlanApprovals0.setCreatedBy("");
	String string0 = impPlanApprovals0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	impPlanApprovals0.setComments("");
	String string0 = impPlanApprovals0.getComments();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	impPlanApprovals0.setApprovedBy("2dOjc QEu|");
	String string0 = impPlanApprovals0.getApprovedBy();
	assertEquals("2dOjc QEu|", string0);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals((Integer) null);
	impPlanApprovals0.setApprovedBy("");
	String string0 = impPlanApprovals0.getApprovedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	impPlanApprovals0.setApprovalType("Type id handling not implemented for type %s (by serializer of type %s)");
	String string0 = impPlanApprovals0.getApprovalType();
	assertEquals("Type id handling not implemented for type %s (by serializer of type %s)", string0);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	Integer integer0 = new Integer(0);
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals(integer0);
	impPlanApprovals0.setApprovalType("");
	String string0 = impPlanApprovals0.getApprovalType();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	impPlanApprovals0.setActive("-T=[[EblABZ_Ki\"");
	String string0 = impPlanApprovals0.getActive();
	assertEquals("-T=[[EblABZ_Ki\"", string0);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	impPlanApprovals0.setActive("");
	String string0 = impPlanApprovals0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	Integer integer0 = new Integer(0);
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals(integer0);
	ImpPlanApprovals impPlanApprovals1 = new ImpPlanApprovals();
	boolean boolean0 = impPlanApprovals0.equals(impPlanApprovals1);
	assertFalse(impPlanApprovals1.equals((Object) impPlanApprovals0));
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	Integer integer0 = new Integer(13);
	ImpPlanApprovals impPlanApprovals1 = new ImpPlanApprovals(integer0);
	boolean boolean0 = impPlanApprovals0.equals(impPlanApprovals1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	boolean boolean0 = impPlanApprovals0.equals(impPlanApprovals0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	Integer integer0 = new Integer(1);
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals(integer0);
	Object object0 = new Object();
	boolean boolean0 = impPlanApprovals0.equals(object0);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	Integer integer0 = new Integer(1);
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals(integer0);
	boolean boolean0 = impPlanApprovals0.equals(impPlanApprovals0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	Integer integer0 = new Integer(1);
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals(integer0);
	impPlanApprovals0.hashCode();
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	impPlanApprovals0.hashCode();
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	String string0 = impPlanApprovals0.getFileName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	String string0 = impPlanApprovals0.getApprovedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	ImpPlan impPlan0 = new ImpPlan();
	impPlanApprovals0.setPlanId(impPlan0);
	ImpPlan impPlan1 = impPlanApprovals0.getPlanId();
	assertNull(impPlan1.getActive());
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	Integer integer0 = new Integer(1);
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals(integer0);
	String string0 = impPlanApprovals0.toString();
	assertEquals("com.tsi.workflow.beans.dao.ImpPlanApprovals[ id=1 ]", string0);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	impPlanApprovals0.setComments("com.tsi.workflow.beans.dao.ImpPlanApprovals[ id=");
	String string0 = impPlanApprovals0.getComments();
	assertEquals("com.tsi.workflow.beans.dao.ImpPlanApprovals[ id=", string0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	Integer integer0 = new Integer(1);
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals(integer0);
	String string0 = impPlanApprovals0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	Integer integer0 = impPlanApprovals0.getId();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	String string0 = impPlanApprovals0.getApprovalType();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	Integer integer0 = new Integer(1);
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals(integer0);
	Date date0 = impPlanApprovals0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	String string0 = impPlanApprovals0.getCreatedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test34() throws Throwable {
	Integer integer0 = new Integer(1);
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals(integer0);
	impPlanApprovals0.setFileName("`z'2>:CBxb,");
	String string0 = impPlanApprovals0.getFileName();
	assertEquals("`z'2>:CBxb,", string0);
    }

    @Test(timeout = 4000)
    public void test35() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	String string0 = impPlanApprovals0.getActive();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test36() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	ImpPlan impPlan0 = impPlanApprovals0.getPlanId();
	assertNull(impPlan0);
    }

    @Test(timeout = 4000)
    public void test37() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	Date date0 = impPlanApprovals0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test38() throws Throwable {
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	String string0 = impPlanApprovals0.getComments();
	assertNull(string0);
    }
}
