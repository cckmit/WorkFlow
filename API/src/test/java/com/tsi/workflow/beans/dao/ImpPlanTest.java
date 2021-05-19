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
public class ImpPlanTest {

    @Test(timeout = 4000)
    public void test000() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("C");
	impPlan0.hashCode();
    }

    @Test(timeout = 4000)
    public void test001() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	LinkedList<SystemPdddsMapping> linkedList0 = new LinkedList<SystemPdddsMapping>();
	impPlan0.setSystemPdddsMappingList(linkedList0);
	List<SystemPdddsMapping> list0 = impPlan0.getSystemPdddsMappingList();
	assertEquals(0, list0.size());
    }

    @Test(timeout = 4000)
    public void test002() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setSdmTktNum(" ]");
	String string0 = impPlan0.getSdmTktNum();
	assertEquals(" ]", string0);
    }

    @Test(timeout = 4000)
    public void test003() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setRelatedPlans("2oQoP?bh#]Rf_");
	String string0 = impPlan0.getRelatedPlans();
	assertEquals("2oQoP?bh#]Rf_", string0);
    }

    @Test(timeout = 4000)
    public void test004() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("R4F^TeL;V$?cC]=-#U");
	impPlan0.setQaBypassStatus("");
	String string0 = impPlan0.getQaBypassStatus();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test005() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Project project0 = new Project();
	impPlan0.setProjectId(project0);
	Project project1 = impPlan0.getProjectId();
	assertSame(project1, project0);
    }

    @Test(timeout = 4000)
    public void test006() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan((String) null);
	LinkedList<ProductionLoads> linkedList0 = new LinkedList<ProductionLoads>();
	Integer integer0 = new Integer((-1416));
	ProductionLoads productionLoads0 = new ProductionLoads(integer0);
	linkedList0.add(productionLoads0);
	linkedList0.removeLast();
	impPlan0.setProductionLoadsList(linkedList0);
	List<ProductionLoads> list0 = impPlan0.getProductionLoadsList();
	assertEquals(0, list0.size());
    }

    @Test(timeout = 4000)
    public void test007() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setProdVer("bEx@");
	String string0 = impPlan0.getProdVer();
	assertEquals("bEx@", string0);
    }

    @Test(timeout = 4000)
    public void test008() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	impPlan0.setProdVer("");
	String string0 = impPlan0.getProdVer();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test009() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setProcessId("4LH:PYwSatm!7");
	String string0 = impPlan0.getProcessId();
	assertEquals("4LH:PYwSatm!7", string0);
    }

    @Test(timeout = 4000)
    public void test010() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	impPlan0.setProcessId("");
	String string0 = impPlan0.getProcessId();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test011() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setPlanStatus("com.tsi.workflow.beans.dao.System[ id=");
	String string0 = impPlan0.getPlanStatus();
	assertEquals("com.tsi.workflow.beans.dao.System[ id=", string0);
    }

    @Test(timeout = 4000)
    public void test012() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	impPlan0.setPlanStatus("");
	String string0 = impPlan0.getPlanStatus();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test013() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setPlanDesc("com.tsi.workflow.beans.dao.ImpPlan");
	String string0 = impPlan0.getPlanDesc();
	assertEquals("com.tsi.workflow.beans.dao.ImpPlan", string0);
    }

    @Test(timeout = 4000)
    public void test014() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	impPlan0.setOthContact("");
	String string0 = impPlan0.getOthContact();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test015() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setMgmtComment("");
	String string0 = impPlan0.getMgmtComment();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test016() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Boolean boolean0 = Boolean.TRUE;
	impPlan0.setMacroHeader(boolean0);
	Boolean boolean1 = impPlan0.getMacroHeader();
	assertTrue(boolean1);
    }

    @Test(timeout = 4000)
    public void test017() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Integer integer0 = new Integer((-353));
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	Boolean boolean0 = checkoutSegments0.getCommonFile();
	impPlan0.setMacroHeader(boolean0);
	Boolean boolean1 = impPlan0.getMacroHeader();
	assertFalse(boolean1);
    }

    @Test(timeout = 4000)
    public void test018() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan(" ]");
	impPlan0.setLoadType("");
	String string0 = impPlan0.getLoadType();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test019() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	impPlan0.setLeadName("");
	String string0 = impPlan0.getLeadName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test020() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setLeadId("");
	String string0 = impPlan0.getLeadId();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test021() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	impPlan0.setLeadContact("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	String string0 = impPlan0.getLeadContact();
	assertEquals("com.tsi.workflow.beans.dao.ImpPlan[ id=", string0);
    }

    @Test(timeout = 4000)
    public void test022() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setLeadContact("");
	String string0 = impPlan0.getLeadContact();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test023() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	LinkedList<Implementation> linkedList0 = new LinkedList<Implementation>();
	Implementation implementation0 = new Implementation("]3c|ql|vFmaYKig, ");
	linkedList0.add(implementation0);
	impPlan0.setImplementationList(linkedList0);
	List<Implementation> list0 = impPlan0.getImplementationList();
	assertFalse(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test024() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	LinkedList<ImpPlanApprovals> linkedList0 = new LinkedList<ImpPlanApprovals>();
	impPlan0.setImpPlanApprovalsList(linkedList0);
	List<ImpPlanApprovals> list0 = impPlan0.getImpPlanApprovalsList();
	assertEquals(0, list0.size());
    }

    @Test(timeout = 4000)
    public void test025() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan(" ]");
	LinkedList<ImpPlanApprovals> linkedList0 = new LinkedList<ImpPlanApprovals>();
	ImpPlanApprovals impPlanApprovals0 = new ImpPlanApprovals();
	linkedList0.offerLast(impPlanApprovals0);
	impPlan0.setImpPlanApprovalsList(linkedList0);
	List<ImpPlanApprovals> list0 = impPlan0.getImpPlanApprovalsList();
	assertEquals(1, list0.size());
    }

    @Test(timeout = 4000)
    public void test026() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	String string0 = impPlan0.getId();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test027() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	String string0 = impPlan0.getId();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test028() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setDevMgrComment("");
	String string0 = impPlan0.getDevMgrComment();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test029() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Date mockDate0 = new Date(1, 1, 3269, 0, 0, 261);
	impPlan0.setDevMgrApproveDateTime(mockDate0);
	Date date0 = impPlan0.getDevMgrApproveDateTime();
	assertNotEquals("Thu Jan 13 00:04:21 GMT 1910", date0.toString());
    }

    @Test(timeout = 4000)
    public void test030() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	impPlan0.setDevManagerName("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	String string0 = impPlan0.getDevManagerName();
	assertEquals("com.tsi.workflow.beans.dao.ImpPlan[ id=", string0);
    }

    @Test(timeout = 4000)
    public void test031() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setDevManagerName("");
	String string0 = impPlan0.getDevManagerName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test032() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan(";AHZ7cw");
	impPlan0.setDevManager(";AHZ7cw");
	String string0 = impPlan0.getDevManager();
	assertEquals(";AHZ7cw", string0);
    }

    @Test(timeout = 4000)
    public void test033() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	impPlan0.setDevManager("");
	String string0 = impPlan0.getDevManager();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test034() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setDelegateName("4LH:PYwSatm!7");
	String string0 = impPlan0.getDelegateName();
	assertEquals("4LH:PYwSatm!7", string0);
    }

    @Test(timeout = 4000)
    public void test035() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	impPlan0.setDelegateName("");
	String string0 = impPlan0.getDelegateName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test036() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	impPlan0.setDelegateId("B^N+e(");
	String string0 = impPlan0.getDelegateId();
	assertEquals("B^N+e(", string0);
    }

    @Test(timeout = 4000)
    public void test037() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	impPlan0.setDelegateId("");
	String string0 = impPlan0.getDelegateId();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test038() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	LinkedList<Dbcr> linkedList0 = new LinkedList<Dbcr>();
	impPlan0.setDbcrList(linkedList0);
	List<Dbcr> list0 = impPlan0.getDbcrList();
	assertEquals(0, list0.size());
    }

    @Test(timeout = 4000)
    public void test039() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan((String) null);
	Integer integer0 = new Integer((-1416));
	LinkedList<Dbcr> linkedList0 = new LinkedList<Dbcr>();
	Dbcr dbcr0 = new Dbcr(integer0);
	linkedList0.add(0, dbcr0);
	impPlan0.setDbcrList(linkedList0);
	List<Dbcr> list0 = impPlan0.getDbcrList();
	assertTrue(list0.contains(dbcr0));
    }

    @Test(timeout = 4000)
    public void test040() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setCreatedBy("");
	String string0 = impPlan0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test041() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setClsComment("");
	String string0 = impPlan0.getClsComment();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test042() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	LinkedList<CheckoutSegments> linkedList0 = new LinkedList<CheckoutSegments>();
	linkedList0.add((CheckoutSegments) null);
	impPlan0.setCheckoutSegmentsList(linkedList0);
	List<CheckoutSegments> list0 = impPlan0.getCheckoutSegmentsList();
	assertFalse(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test043() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setApproverName("n'?Hkyb>");
	String string0 = impPlan0.getApproverName();
	assertEquals("n'?Hkyb>", string0);
    }

    @Test(timeout = 4000)
    public void test044() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	impPlan0.setApproverName("");
	String string0 = impPlan0.getApproverName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test045() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setApprover("");
	String string0 = impPlan0.getApprover();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test046() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("C");
	impPlan0.setActive("");
	String string0 = impPlan0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test047() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	Date mockDate0 = new Date((-1567), (-1567), 1443);
	impPlan0.setAcceptedDateTime(mockDate0);
	Date date0 = impPlan0.getAcceptedDateTime();
	assertNotEquals("Tue May 13 00:00:00 GMT 206", date0.toString());
    }

    @Test(timeout = 4000)
    public void test048() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setId("~I-B");
	ImpPlan impPlan1 = new ImpPlan();
	boolean boolean0 = impPlan0.equals(impPlan1);
	assertFalse(impPlan1.equals((Object) impPlan0));
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test049() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	ImpPlan impPlan1 = new ImpPlan("~I-B");
	boolean boolean0 = impPlan0.equals(impPlan1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test050() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setId("~I-B");
	boolean boolean0 = impPlan0.equals(impPlan0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test051() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	boolean boolean0 = impPlan0.equals((Object) null);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test052() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	boolean boolean0 = impPlan0.equals(impPlan0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test053() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.hashCode();
    }

    @Test(timeout = 4000)
    public void test054() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan(" ]");
	List<Implementation> list0 = impPlan0.getImplementationList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test055() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setMgmtComment("E>_:2 HJA<$If\";}");
	String string0 = impPlan0.getMgmtComment();
	assertEquals("E>_:2 HJA<$If\";}", string0);
    }

    @Test(timeout = 4000)
    public void test056() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	String string0 = impPlan0.getDevMgrComment();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test057() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Boolean boolean0 = impPlan0.getMacroHeader();
	assertNull(boolean0);
    }

    @Test(timeout = 4000)
    public void test058() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	String string0 = impPlan0.toString();
	assertEquals("com.tsi.workflow.beans.dao.ImpPlan[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test059() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	LinkedList<CheckoutSegments> linkedList0 = new LinkedList<CheckoutSegments>();
	impPlan0.setCheckoutSegmentsList(linkedList0);
	List<CheckoutSegments> list0 = impPlan0.getCheckoutSegmentsList();
	assertEquals(0, list0.size());
    }

    @Test(timeout = 4000)
    public void test060() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Project project0 = impPlan0.getProjectId();
	assertNull(project0);
    }

    @Test(timeout = 4000)
    public void test061() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	Date date0 = impPlan0.getDevMgrApproveDateTime();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test062() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	Date date0 = impPlan0.getFallbackDateTime();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test063() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	String string0 = impPlan0.getLeadContact();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test064() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("2alk='B2bc%t-#a*D-");
	String string0 = impPlan0.getId();
	assertEquals("2alk='B2bc%t-#a*D-", string0);
    }

    @Test(timeout = 4000)
    public void test065() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setSdmTktNum("");
	String string0 = impPlan0.getSdmTktNum();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test066() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	String string0 = impPlan0.getLoadType();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test067() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan(" ]");
	String string0 = impPlan0.getLoadAttendee();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test068() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	List<ImpPlanApprovals> list0 = impPlan0.getImpPlanApprovalsList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test069() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setModifiedBy("?{Z");
	String string0 = impPlan0.getModifiedBy();
	assertEquals("?{Z", string0);
    }

    @Test(timeout = 4000)
    public void test070() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setApproveDateTime((Date) null);
	assertNull(impPlan0.getSdmTktNum());
    }

    @Test(timeout = 4000)
    public void test071() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan(" ]");
	String string0 = impPlan0.getCreatedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test072() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	String string0 = impPlan0.getSdmTktNum();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test073() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	List<ActivityLog> list0 = impPlan0.getActivityLogList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test074() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan(" ]");
	Date date0 = impPlan0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test075() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	List<SystemLoadActions> list0 = impPlan0.getSystemLoadActionsList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test076() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setModifiedDt((Date) null);
	assertNull(impPlan0.getDevManagerName());
    }

    @Test(timeout = 4000)
    public void test077() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	String string0 = impPlan0.getPlanStatus();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test078() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan(" ]");
	impPlan0.setActive("~X9]YmZX%<+_M-");
	String string0 = impPlan0.getActive();
	assertEquals("~X9]YmZX%<+_M-", string0);
    }

    @Test(timeout = 4000)
    public void test079() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan(" ]");
	String string0 = impPlan0.getClsComment();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test080() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan(" ]");
	String string0 = impPlan0.getActive();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test081() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	List<SystemPdddsMapping> list0 = impPlan0.getSystemPdddsMappingList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test082() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	String string0 = impPlan0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test083() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	String string0 = impPlan0.getOthContact();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test084() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	impPlan0.setFallbackDateTime((Date) null);
	assertNull(impPlan0.getLeadId());
    }

    @Test(timeout = 4000)
    public void test085() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	String string0 = impPlan0.getMgmtComment();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test086() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	List<Build> list0 = impPlan0.getBuildList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test087() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setLeadName("l-bZYY=]t~");
	String string0 = impPlan0.getLeadName();
	assertEquals("l-bZYY=]t~", string0);
    }

    @Test(timeout = 4000)
    public void test088() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	Date date0 = impPlan0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test089() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	String string0 = impPlan0.getLeadName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test090() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setBuildList((List<Build>) null);
	assertNull(impPlan0.getPlanStatus());
    }

    @Test(timeout = 4000)
    public void test091() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	impPlan0.setRelatedPlans("");
	String string0 = impPlan0.getRelatedPlans();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test092() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	impPlan0.setSystemLoadList((List<SystemLoad>) null);
	assertNull(impPlan0.getActive());
    }

    @Test(timeout = 4000)
    public void test093() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	String string0 = impPlan0.getLeadId();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test094() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("R4F^TeL;V$?cC]=-#U");
	impPlan0.setDevMgrComment("|w");
	String string0 = impPlan0.getDevMgrComment();
	assertEquals("|w", string0);
    }

    @Test(timeout = 4000)
    public void test095() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("R4F^TeL;V$?cC]=-#U");
	impPlan0.setLoadAttendee("|w");
	String string0 = impPlan0.getLoadAttendee();
	assertEquals("|w", string0);
    }

    @Test(timeout = 4000)
    public void test096() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	String string0 = impPlan0.getProcessId();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test097() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan(" ]");
	List<Dbcr> list0 = impPlan0.getDbcrList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test098() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setOthContact("iPP4*2b_>&%");
	String string0 = impPlan0.getOthContact();
	assertEquals("iPP4*2b_>&%", string0);
    }

    @Test(timeout = 4000)
    public void test099() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	String string0 = impPlan0.getApprover();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test100() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Date date0 = impPlan0.getAcceptedDateTime();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test101() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	impPlan0.setApprover("com.tsi.workflow.beans.dao.Dbcr[ id=");
	String string0 = impPlan0.getApprover();
	assertEquals("com.tsi.workflow.beans.dao.Dbcr[ id=", string0);
    }

    @Test(timeout = 4000)
    public void test102() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setCreatedBy("*4^UAmi");
	String string0 = impPlan0.getCreatedBy();
	assertEquals("*4^UAmi", string0);
    }

    @Test(timeout = 4000)
    public void test103() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	String string0 = impPlan0.getQaBypassStatus();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test104() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	List<CheckoutSegments> list0 = impPlan0.getCheckoutSegmentsList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test105() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan(" ]");
	List<SystemLoad> list0 = impPlan0.getSystemLoadList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test106() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	String string0 = impPlan0.getDelegateId();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test107() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	String string0 = impPlan0.getApproverName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test108() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	String string0 = impPlan0.getProdVer();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test109() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setLoadType("4LH:PYwSatm!7");
	String string0 = impPlan0.getLoadType();
	assertEquals("4LH:PYwSatm!7", string0);
    }

    @Test(timeout = 4000)
    public void test110() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	String string0 = impPlan0.getPlanDesc();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test111() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan(";AHZ7cw");
	Date date0 = impPlan0.getApproveDateTime();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test112() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setActivityLogList((List<ActivityLog>) null);
	assertNull(impPlan0.getPlanStatus());
    }

    @Test(timeout = 4000)
    public void test113() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setClsComment("4LH:PYwSatm!7");
	String string0 = impPlan0.getClsComment();
	assertEquals("4LH:PYwSatm!7", string0);
    }

    @Test(timeout = 4000)
    public void test114() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan(";AHZ7cw");
	String string0 = impPlan0.getDevManager();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test115() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	impPlan0.setSystemLoadActionsList((List<SystemLoadActions>) null);
	assertNull(impPlan0.getProdVer());
    }

    @Test(timeout = 4000)
    public void test116() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	String string0 = impPlan0.getDelegateName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test117() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	String string0 = impPlan0.getDevManagerName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test118() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("com.tsi.workflow.beans.dao.ImpPlan[ id=");
	impPlan0.setCreatedDt((Date) null);
	assertNull(impPlan0.getMgmtComment());
    }

    @Test(timeout = 4000)
    public void test119() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	String string0 = impPlan0.getRelatedPlans();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test120() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setLeadId("0_");
	String string0 = impPlan0.getLeadId();
	assertEquals("0_", string0);
    }

    @Test(timeout = 4000)
    public void test121() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	impPlan0.setQaBypassStatus("*4^UAmi");
	String string0 = impPlan0.getQaBypassStatus();
	assertEquals("*4^UAmi", string0);
    }

    @Test(timeout = 4000)
    public void test122() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	List<ProductionLoads> list0 = impPlan0.getProductionLoadsList();
	assertNull(list0);
    }
}
