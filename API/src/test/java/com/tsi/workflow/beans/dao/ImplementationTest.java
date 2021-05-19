
package com.tsi.workflow.beans.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class ImplementationTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	Implementation implementation0 = new Implementation(":GBTGPV*");
	implementation0.setTktUrl("!;iEe}=4ym^n");
	String string0 = implementation0.getTktUrl();
	assertEquals("!;iEe}=4ym^n", string0);
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setTktUrl("");
	String string0 = implementation0.getTktUrl();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setTktNum("S6e");
	String string0 = implementation0.getTktNum();
	assertEquals("S6e", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setTktNum("");
	String string0 = implementation0.getTktNum();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	Implementation implementation0 = new Implementation("ar/q8@G0h3+");
	implementation0.setSubstatus("21awyx;EvJw");
	String string0 = implementation0.getSubstatus();
	assertEquals("21awyx;EvJw", string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setReassignFlag("");
	String string0 = implementation0.getReassignFlag();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	Implementation implementation0 = new Implementation("ar/q8@G0h3+");
	implementation0.setProdVer("com.tsi.workflow.beans.dao.Implementation[ id=");
	String string0 = implementation0.getProdVer();
	assertEquals("com.tsi.workflow.beans.dao.Implementation[ id=", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	Implementation implementation0 = new Implementation("ar/q8@G0h3+");
	implementation0.setProdVer("");
	String string0 = implementation0.getProdVer();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	Implementation implementation0 = new Implementation(":GBTGPV*");
	implementation0.setProcessId("");
	String string0 = implementation0.getProcessId();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setPrTktNum("[T4~>`");
	String string0 = implementation0.getPrTktNum();
	assertEquals("[T4~>`", string0);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setPrTktNum("");
	String string0 = implementation0.getPrTktNum();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	Implementation implementation0 = new Implementation();
	ImpPlan impPlan0 = new ImpPlan("");
	implementation0.setPlanId(impPlan0);
	ImpPlan impPlan1 = implementation0.getPlanId();
	assertNull(impPlan1.getLoadType());
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setPeerReviewersName("Z<FwY#Gwa");
	String string0 = implementation0.getPeerReviewersName();
	assertEquals("Z<FwY#Gwa", string0);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setPeerReviewersName("");
	String string0 = implementation0.getPeerReviewersName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setPeerReviewers("RTUOS#r@<d+vY*~?");
	String string0 = implementation0.getPeerReviewers();
	assertEquals("RTUOS#r@<d+vY*~?", string0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	Implementation implementation0 = new Implementation("");
	implementation0.setPeerReviewers("");
	String string0 = implementation0.getPeerReviewers();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	Implementation implementation0 = new Implementation("ar/q8@G0h3+");
	implementation0.setPeerReview("?mF-;");
	String string0 = implementation0.getPeerReview();
	assertEquals("?mF-;", string0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	Implementation implementation0 = new Implementation();
	Date mockDate0 = new Date((-2949), (-2949), 0, 0, 0);
	implementation0.setModifiedDt(mockDate0);
	Date date0 = implementation0.getModifiedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	Implementation implementation0 = new Implementation("com.tsi.workflow.beans.dao.CheckoutSegments");
	implementation0.setModifiedBy("The wildcard list must not be null");
	String string0 = implementation0.getModifiedBy();
	assertEquals("The wildcard list must not be null", string0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	Implementation implementation0 = new Implementation(":GBTGPV*");
	implementation0.setModifiedBy("");
	String string0 = implementation0.getModifiedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setLastCheckinStatus("~/JC|bkj6Rc4Hv~");
	String string0 = implementation0.getLastCheckinStatus();
	assertEquals("~/JC|bkj6Rc4Hv~", string0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setLastCheckinStatus("");
	String string0 = implementation0.getLastCheckinStatus();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	Implementation implementation0 = new Implementation();
	Boolean boolean0 = Boolean.valueOf(true);
	implementation0.setIsCheckedin(boolean0);
	Boolean boolean1 = implementation0.getIsCheckedin();
	assertTrue(boolean1);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setImpStatus("org.apache.commons.io.filefilter.CanReadFileFilter");
	String string0 = implementation0.getImpStatus();
	assertEquals("org.apache.commons.io.filefilter.CanReadFileFilter", string0);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	Implementation implementation0 = new Implementation("Ti<d{");
	implementation0.setImpStatus("");
	String string0 = implementation0.getImpStatus();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setImpDesc("com.tsi.workflow.beans.dao.Implementation[ id=");
	String string0 = implementation0.getImpDesc();
	assertEquals("com.tsi.workflow.beans.dao.Implementation[ id=", string0);
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	Implementation implementation0 = new Implementation("");
	implementation0.setImpDesc("");
	String string0 = implementation0.getImpDesc();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setId("@3sRa-<GJ");
	String string0 = implementation0.getId();
	assertEquals("@3sRa-<GJ", string0);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	Implementation implementation0 = new Implementation("");
	String string0 = implementation0.getId();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	Implementation implementation0 = new Implementation("<N1Z");
	implementation0.setDevName("com.tsi.workflow.beans.dao.Implementation");
	String string0 = implementation0.getDevName();
	assertEquals("com.tsi.workflow.beans.dao.Implementation", string0);
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	Implementation implementation0 = new Implementation("");
	implementation0.setDevName("");
	String string0 = implementation0.getDevName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	Implementation implementation0 = new Implementation("com.tsi.workflow.beans.dao.CheckoutSegments");
	implementation0.setDevLocation("com.tsi.workflow.beans.dao.CheckoutSegments");
	String string0 = implementation0.getDevLocation();
	assertEquals("com.tsi.workflow.beans.dao.CheckoutSegments", string0);
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setDevId("$VALUES");
	String string0 = implementation0.getDevId();
	assertEquals("$VALUES", string0);
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setDevId("");
	String string0 = implementation0.getDevId();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test34() throws Throwable {
	Implementation implementation0 = new Implementation("ar/q8@G0h3+");
	implementation0.setDevContact("?mF-;");
	String string0 = implementation0.getDevContact();
	assertEquals("?mF-;", string0);
    }

    @Test(timeout = 4000)
    public void test35() throws Throwable {
	Implementation implementation0 = new Implementation("ar/q8@G0h3+");
	implementation0.setDevContact("");
	String string0 = implementation0.getDevContact();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test36() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setDbcrRef("6c%c,z!$-7?Y7SH~");
	String string0 = implementation0.getDbcrRef();
	assertEquals("6c%c,z!$-7?Y7SH~", string0);
    }

    @Test(timeout = 4000)
    public void test37() throws Throwable {
	Implementation implementation0 = new Implementation("5w6IG0:I~# _!#C");
	implementation0.setDbcrRef("");
	String string0 = implementation0.getDbcrRef();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test38() throws Throwable {
	Implementation implementation0 = new Implementation("^ve=O+ ]YA2y");
	Date mockDate0 = new Date((-1575), (-1575), 1435, 1435, (-1575));
	implementation0.setCreatedDt(mockDate0);
	Date date0 = implementation0.getCreatedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test39() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setCreatedBy("47Y) P2!Ub}p,UI>;n");
	String string0 = implementation0.getCreatedBy();
	assertEquals("47Y) P2!Ub}p,UI>;n", string0);
    }

    @Test(timeout = 4000)
    public void test40() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setCreatedBy("");
	String string0 = implementation0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test41() throws Throwable {
	Implementation implementation0 = new Implementation("");
	LinkedList<CheckoutSegments> linkedList0 = new LinkedList<CheckoutSegments>();
	implementation0.setCheckoutSegmentsList(linkedList0);
	List<CheckoutSegments> list0 = implementation0.getCheckoutSegmentsList();
	assertEquals(0, list0.size());
    }

    @Test(timeout = 4000)
    public void test42() throws Throwable {
	Implementation implementation0 = new Implementation("");
	LinkedList<CheckoutSegments> linkedList0 = new LinkedList<CheckoutSegments>();
	Integer integer0 = new Integer(1307);
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	linkedList0.add(checkoutSegments0);
	implementation0.setCheckoutSegmentsList(linkedList0);
	List<CheckoutSegments> list0 = implementation0.getCheckoutSegmentsList();
	assertFalse(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test43() throws Throwable {
	Implementation implementation0 = new Implementation("Fq*J");
	Date mockDate0 = new Date(197, (-1061), 1801);
	implementation0.setCheckinDateTime(mockDate0);
	Date date0 = implementation0.getCheckinDateTime();
	assertNotEquals("Sat Jul 06 00:00:00 GMT 2013", date0.toString());
    }

    @Test(timeout = 4000)
    public void test44() throws Throwable {
	Implementation implementation0 = new Implementation("Ti<d{");
	Boolean boolean0 = Boolean.valueOf(true);
	implementation0.setBypassPeerReview(boolean0);
	Boolean boolean1 = implementation0.getBypassPeerReview();
	assertTrue(boolean1);
    }

    @Test(timeout = 4000)
    public void test45() throws Throwable {
	Implementation implementation0 = new Implementation("3XP,K9aMam4");
	Boolean boolean0 = new Boolean(false);
	implementation0.setBypassPeerReview(boolean0);
	Boolean boolean1 = implementation0.getBypassPeerReview();
	assertFalse(boolean1);
    }

    @Test(timeout = 4000)
    public void test46() throws Throwable {
	Implementation implementation0 = new Implementation();
	LinkedList<ActivityLog> linkedList0 = new LinkedList<ActivityLog>();
	linkedList0.add((ActivityLog) null);
	implementation0.setActivityLogList(linkedList0);
	List<ActivityLog> list0 = implementation0.getActivityLogList();
	assertEquals(1, list0.size());
    }

    @Test(timeout = 4000)
    public void test47() throws Throwable {
	Implementation implementation0 = new Implementation("2d[3_eNM2");
	implementation0.setActive(">:('3cR");
	String string0 = implementation0.getActive();
	assertEquals(">:('3cR", string0);
    }

    @Test(timeout = 4000)
    public void test48() throws Throwable {
	Implementation implementation0 = new Implementation("7o-Qef;*&c_=yKW6g8N");
	Implementation implementation1 = new Implementation();
	boolean boolean0 = implementation0.equals(implementation1);
	assertFalse(boolean0);
	assertFalse(implementation1.equals((Object) implementation0));
    }

    @Test(timeout = 4000)
    public void test49() throws Throwable {
	Implementation implementation0 = new Implementation();
	Implementation implementation1 = new Implementation("");
	boolean boolean0 = implementation0.equals(implementation1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test50() throws Throwable {
	Implementation implementation0 = new Implementation("ar/q8@G0h3+");
	implementation0.setId((String) null);
	boolean boolean0 = implementation0.equals(implementation0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test51() throws Throwable {
	Implementation implementation0 = new Implementation();
	boolean boolean0 = implementation0.equals((Object) null);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test52() throws Throwable {
	Implementation implementation0 = new Implementation("ar/q8@G0h3+");
	boolean boolean0 = implementation0.equals(implementation0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test53() throws Throwable {
	Implementation implementation0 = new Implementation("ar/q8@G0h3+");
	implementation0.hashCode();
    }

    @Test(timeout = 4000)
    public void test54() throws Throwable {
	Implementation implementation0 = new Implementation();
	ImpPlan impPlan0 = implementation0.getPlanId();
	assertNull(impPlan0);
    }

    @Test(timeout = 4000)
    public void test55() throws Throwable {
	Implementation implementation0 = new Implementation();
	Date date0 = implementation0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test56() throws Throwable {
	Implementation implementation0 = new Implementation();
	String string0 = implementation0.getDevLocation();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test57() throws Throwable {
	Implementation implementation0 = new Implementation();
	String string0 = implementation0.getLastCheckinStatus();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test58() throws Throwable {
	Implementation implementation0 = new Implementation();
	String string0 = implementation0.getPeerReviewers();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test59() throws Throwable {
	Implementation implementation0 = new Implementation();
	String string0 = implementation0.getDevName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test60() throws Throwable {
	Implementation implementation0 = new Implementation(":GBTGPV*");
	Date date0 = implementation0.getCheckinDateTime();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test61() throws Throwable {
	Implementation implementation0 = new Implementation();
	String string0 = implementation0.getId();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test62() throws Throwable {
	Implementation implementation0 = new Implementation("ar/q8@G0h3+");
	String string0 = implementation0.getDevContact();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test63() throws Throwable {
	Implementation implementation0 = new Implementation();
	String string0 = implementation0.getReassignFlag();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test64() throws Throwable {
	Implementation implementation0 = new Implementation();
	String string0 = implementation0.getImpDesc();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test65() throws Throwable {
	Implementation implementation0 = new Implementation("ar/q8@G0h3+");
	implementation0.setId((String) null);
	implementation0.hashCode();
    }

    @Test(timeout = 4000)
    public void test66() throws Throwable {
	Implementation implementation0 = new Implementation("7o-Qef;*&c_=yKW6g8N");
	List<CheckoutSegments> list0 = implementation0.getCheckoutSegmentsList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test67() throws Throwable {
	Implementation implementation0 = new Implementation();
	String string0 = implementation0.getPeerReviewersName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test68() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setActive("");
	String string0 = implementation0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test69() throws Throwable {
	Implementation implementation0 = new Implementation("ar/q8@G0h3+");
	String string0 = implementation0.getPeerReview();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test70() throws Throwable {
	Implementation implementation0 = new Implementation("7o-Qef;*&c_=yKW6g8N");
	String string0 = implementation0.toString();
	assertEquals("com.tsi.workflow.beans.dao.Implementation[ id=7o-Qef;*&c_=yKW6g8N ]", string0);
    }

    @Test(timeout = 4000)
    public void test71() throws Throwable {
	Implementation implementation0 = new Implementation();
	Boolean boolean0 = Boolean.valueOf("");
	implementation0.setIsCheckedin(boolean0);
	Boolean boolean1 = implementation0.getIsCheckedin();
	assertFalse(boolean1);
    }

    @Test(timeout = 4000)
    public void test72() throws Throwable {
	Implementation implementation0 = new Implementation("ar/q8@G0h3+");
	String string0 = implementation0.getProdVer();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test73() throws Throwable {
	Implementation implementation0 = new Implementation();
	String string0 = implementation0.getTktNum();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test74() throws Throwable {
	Implementation implementation0 = new Implementation();
	List<ActivityLog> list0 = implementation0.getActivityLogList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test75() throws Throwable {
	Implementation implementation0 = new Implementation();
	Boolean boolean0 = implementation0.getBypassPeerReview();
	assertNull(boolean0);
    }

    @Test(timeout = 4000)
    public void test76() throws Throwable {
	Implementation implementation0 = new Implementation();
	Boolean boolean0 = implementation0.getIsCheckedin();
	assertNull(boolean0);
    }

    @Test(timeout = 4000)
    public void test77() throws Throwable {
	Implementation implementation0 = new Implementation();
	LinkedList<ActivityLog> linkedList0 = new LinkedList<ActivityLog>();
	implementation0.setActivityLogList(linkedList0);
	List<ActivityLog> list0 = implementation0.getActivityLogList();
	assertEquals(0, list0.size());
    }

    @Test(timeout = 4000)
    public void test78() throws Throwable {
	Implementation implementation0 = new Implementation("ar/q8@G0h3+");
	implementation0.setDevLocation("");
	String string0 = implementation0.getDevLocation();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test79() throws Throwable {
	Implementation implementation0 = new Implementation("ar/q8@G0h3+");
	String string0 = implementation0.getSubstatus();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test80() throws Throwable {
	Implementation implementation0 = new Implementation();
	String string0 = implementation0.getDbcrRef();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test81() throws Throwable {
	Implementation implementation0 = new Implementation("7o-Qef;*&c_=yKW6g8N");
	String string0 = implementation0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test82() throws Throwable {
	Implementation implementation0 = new Implementation("7o-Qef;*&c_=yKW6g8N");
	String string0 = implementation0.getTktUrl();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test83() throws Throwable {
	Implementation implementation0 = new Implementation();
	String string0 = implementation0.getDevId();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test84() throws Throwable {
	Implementation implementation0 = new Implementation();
	String string0 = implementation0.getProcessId();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test85() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setProcessId("8VwomeE,<gZco.,SH}-");
	String string0 = implementation0.getProcessId();
	assertEquals("8VwomeE,<gZco.,SH}-", string0);
    }

    @Test(timeout = 4000)
    public void test86() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setReassignFlag("com.tsi.workflow.beans.dao.Implementation[ id=null ]");
	String string0 = implementation0.getReassignFlag();
	assertEquals("com.tsi.workflow.beans.dao.Implementation[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test87() throws Throwable {
	Implementation implementation0 = new Implementation();
	String string0 = implementation0.getPrTktNum();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test88() throws Throwable {
	Implementation implementation0 = new Implementation();
	Date date0 = implementation0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test89() throws Throwable {
	Implementation implementation0 = new Implementation();
	String string0 = implementation0.getCreatedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test90() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setSubstatus("");
	String string0 = implementation0.getSubstatus();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test91() throws Throwable {
	Implementation implementation0 = new Implementation();
	String string0 = implementation0.getImpStatus();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test92() throws Throwable {
	Implementation implementation0 = new Implementation();
	implementation0.setPeerReview("");
	String string0 = implementation0.getPeerReview();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test93() throws Throwable {
	Implementation implementation0 = new Implementation();
	String string0 = implementation0.getActive();
	assertNull(string0);
    }
}
