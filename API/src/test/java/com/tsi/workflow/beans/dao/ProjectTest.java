
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
public class ProjectTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	Project project0 = new Project((Integer) null);
	project0.setSponsorId("");
	String string0 = project0.getSponsorId();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	Project project0 = new Project();
	project0.setProjectNumber("=}Z~w(nB1rxhDDFZ");
	String string0 = project0.getProjectNumber();
	assertEquals("=}Z~w(nB1rxhDDFZ", string0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	Project project0 = new Project((Integer) null);
	project0.setProjectNumber("");
	String string0 = project0.getProjectNumber();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	Integer integer0 = new Integer((-1043));
	Project project0 = new Project(integer0);
	project0.setProjectName("#!CgFO&IUPt");
	String string0 = project0.getProjectName();
	assertEquals("#!CgFO&IUPt", string0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	Project project0 = new Project((Integer) null);
	project0.setProjectName("");
	String string0 = project0.getProjectName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	Integer integer0 = new Integer(1412);
	Project project0 = new Project(integer0);
	Date mockDate0 = new Date();
	project0.setModifiedDt(mockDate0);
	Date date0 = project0.getModifiedDt();
	assertNotEquals("Fri Feb 14 20:21:21 GMT 2014", date0.toString());
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	Project project0 = new Project((Integer) null);
	project0.setModifiedBy("com.tsi.workflow.beans.dao.SystemLoad");
	String string0 = project0.getModifiedBy();
	assertEquals("com.tsi.workflow.beans.dao.SystemLoad", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	Project project0 = new Project((Integer) null);
	project0.setModifiedBy("");
	String string0 = project0.getModifiedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	Project project0 = new Project();
	project0.setManagerId("|<s)C,A");
	String string0 = project0.getManagerId();
	assertEquals("|<s)C,A", string0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	Integer integer0 = new Integer(838);
	Project project0 = new Project(integer0);
	project0.setManagerId("");
	String string0 = project0.getManagerId();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	Project project0 = new Project();
	project0.setLineOfBusiness("com.tsi.workflow.beans.dao.Project[ id=null ]");
	String string0 = project0.getLineOfBusiness();
	assertEquals("com.tsi.workflow.beans.dao.Project[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	Project project0 = new Project();
	project0.setLineOfBusiness("");
	String string0 = project0.getLineOfBusiness();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	Integer integer0 = new Integer(0);
	Project project0 = new Project(integer0);
	Boolean boolean0 = Boolean.TRUE;
	project0.setIsDelta(boolean0);
	Boolean boolean1 = project0.getIsDelta();
	assertTrue(boolean1);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	Integer integer0 = new Integer(1288);
	Project project0 = new Project(integer0);
	Boolean boolean0 = Boolean.valueOf("com.fasterxml.jackson.databind.JsonDeserializer");
	project0.setIsDelta(boolean0);
	Boolean boolean1 = project0.getIsDelta();
	assertFalse(boolean1);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	Integer integer0 = new Integer(3);
	Project project0 = new Project(integer0);
	LinkedList<ImpPlan> linkedList0 = new LinkedList<ImpPlan>();
	project0.setImpPlanList(linkedList0);
	List<ImpPlan> list0 = project0.getImpPlanList();
	assertTrue(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	Integer integer0 = new Integer((-1043));
	Project project0 = new Project(integer0);
	LinkedList<ImpPlan> linkedList0 = new LinkedList<ImpPlan>();
	ImpPlan impPlan0 = new ImpPlan("");
	linkedList0.add(impPlan0);
	project0.setImpPlanList(linkedList0);
	List<ImpPlan> list0 = project0.getImpPlanList();
	assertFalse(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	Integer integer0 = Integer.getInteger("NQk-9V|K*BBd3^V", (-4818));
	Project project0 = new Project(integer0);
	Integer integer1 = project0.getId();
	assertEquals((-4818), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	Project project0 = new Project((Integer) null);
	Integer integer0 = new Integer(0);
	project0.setId(integer0);
	Integer integer1 = project0.getId();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	Integer integer0 = new Integer(14);
	Project project0 = new Project(integer0);
	Integer integer1 = project0.getId();
	assertEquals(14, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	Integer integer0 = new Integer(2030);
	Project project0 = new Project(integer0);
	Integer integer1 = project0.getId();
	assertEquals(2030, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	Integer integer0 = new Integer(1);
	Project project0 = new Project(integer0);
	Date mockDate0 = new Date();
	project0.setCreatedDt(mockDate0);
	Date date1 = project0.getCreatedDt();
	assertSame(date1, mockDate0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	Project project0 = new Project();
	project0.setCreatedBy("com.tsi.workflow.beans.dao.Project[ id=null ]");
	String string0 = project0.getCreatedBy();
	assertEquals("com.tsi.workflow.beans.dao.Project[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	Project project0 = new Project();
	project0.setCreatedBy("");
	String string0 = project0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	Project project0 = new Project((Integer) null);
	project0.setActive("1vJt");
	String string0 = project0.getActive();
	assertEquals("1vJt", string0);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	Project project0 = new Project();
	project0.setActive("");
	String string0 = project0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	Integer integer0 = new Integer(838);
	Project project0 = new Project(integer0);
	Project project1 = new Project();
	boolean boolean0 = project0.equals(project1);
	assertFalse(boolean0);
	assertFalse(project1.equals((Object) project0));
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	Project project0 = new Project();
	Integer integer0 = new Integer(2615);
	Project project1 = new Project(integer0);
	boolean boolean0 = project0.equals(project1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	Project project0 = new Project((Integer) null);
	boolean boolean0 = project0.equals(project0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	Project project0 = new Project((Integer) null);
	boolean boolean0 = project0.equals("1vJt");
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	Integer integer0 = new Integer(838);
	Project project0 = new Project(integer0);
	boolean boolean0 = project0.equals(project0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	Integer integer0 = new Integer(838);
	Project project0 = new Project(integer0);
	project0.hashCode();
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	Project project0 = new Project();
	project0.hashCode();
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	Project project0 = new Project((Integer) null);
	String string0 = project0.getSponsorId();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	Project project0 = new Project();
	String string0 = project0.toString();
	assertEquals("com.tsi.workflow.beans.dao.Project[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test34() throws Throwable {
	Project project0 = new Project();
	String string0 = project0.getActive();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test35() throws Throwable {
	Integer integer0 = new Integer(838);
	Project project0 = new Project(integer0);
	String string0 = project0.getManagerId();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test36() throws Throwable {
	Integer integer0 = new Integer(1412);
	Project project0 = new Project(integer0);
	String string0 = project0.getCreatedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test37() throws Throwable {
	Integer integer0 = new Integer(838);
	Project project0 = new Project(integer0);
	Date date0 = project0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test38() throws Throwable {
	Integer integer0 = new Integer(838);
	Project project0 = new Project(integer0);
	Boolean boolean0 = project0.getIsDelta();
	assertNull(boolean0);
    }

    @Test(timeout = 4000)
    public void test39() throws Throwable {
	Integer integer0 = new Integer(838);
	Project project0 = new Project(integer0);
	String string0 = project0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test40() throws Throwable {
	Integer integer0 = new Integer(838);
	Project project0 = new Project(integer0);
	String string0 = project0.getProjectNumber();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test41() throws Throwable {
	Integer integer0 = new Integer(838);
	Project project0 = new Project(integer0);
	List<ImpPlan> list0 = project0.getImpPlanList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test42() throws Throwable {
	Project project0 = new Project();
	project0.setSponsorId("XoE +[Xw)0.(pi(RT`");
	String string0 = project0.getSponsorId();
	assertEquals("XoE +[Xw)0.(pi(RT`", string0);
    }

    @Test(timeout = 4000)
    public void test43() throws Throwable {
	Project project0 = new Project();
	Integer integer0 = project0.getId();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test44() throws Throwable {
	Project project0 = new Project();
	Date date0 = project0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test45() throws Throwable {
	Project project0 = new Project();
	String string0 = project0.getLineOfBusiness();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test46() throws Throwable {
	Project project0 = new Project();
	String string0 = project0.getProjectName();
	assertNull(string0);
    }
}
