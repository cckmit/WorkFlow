
package com.tsi.workflow.beans.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class SystemTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	Integer integer0 = new Integer(1);
	System system0 = new System(integer0);
	// Undeclared exception!
	try {
	    system0.compareTo((System) null);
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	}
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	System system0 = new System();
	LinkedList<Vpars> linkedList0 = new LinkedList<Vpars>();
	system0.setVparsList(linkedList0);
	List<Vpars> list0 = system0.getVparsList();
	assertTrue(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	System system0 = new System((Integer) null);
	LinkedList<Vpars> linkedList0 = new LinkedList<Vpars>();
	Vpars vpars0 = new Vpars();
	linkedList0.add(vpars0);
	system0.setVparsList(linkedList0);
	List<Vpars> list0 = system0.getVparsList();
	assertFalse(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	System system0 = new System();
	LinkedList<SystemLoad> linkedList0 = new LinkedList<SystemLoad>();
	system0.setSystemLoadList(linkedList0);
	List<SystemLoad> list0 = system0.getSystemLoadList();
	assertTrue(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	System system0 = new System();
	LinkedList<SystemLoadActions> linkedList0 = new LinkedList<SystemLoadActions>();
	system0.setSystemLoadActionsList(linkedList0);
	List<SystemLoadActions> list0 = system0.getSystemLoadActionsList();
	assertTrue(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	Integer integer0 = new Integer((-1411));
	System system0 = new System(integer0);
	LinkedList<SystemLoadActions> linkedList0 = new LinkedList<SystemLoadActions>();
	SystemLoadActions systemLoadActions0 = new SystemLoadActions(integer0);
	linkedList0.offerLast(systemLoadActions0);
	system0.setSystemLoadActionsList(linkedList0);
	List<SystemLoadActions> list0 = system0.getSystemLoadActionsList();
	assertTrue(list0.contains(systemLoadActions0));
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	Integer integer0 = new Integer(1);
	System system0 = new System(integer0);
	LinkedList<SystemCpu> linkedList0 = new LinkedList<SystemCpu>();
	system0.setSystemCpuList(linkedList0);
	List<SystemCpu> list0 = system0.getSystemCpuList();
	assertTrue(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	System system0 = new System();
	LinkedList<PutLevel> linkedList0 = new LinkedList<PutLevel>();
	system0.setPutLevelList(linkedList0);
	List<PutLevel> list0 = system0.getPutLevelList();
	assertTrue(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	Integer integer0 = new Integer(1);
	System system0 = new System(integer0);
	system0.setPortno(integer0);
	Integer integer1 = system0.getPortno();
	assertEquals(1, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	System system0 = new System();
	Integer integer0 = new Integer((-1853));
	system0.setPortno(integer0);
	Integer integer1 = system0.getPortno();
	assertEquals((-1853), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	System system0 = new System();
	Integer integer0 = new Integer((-3069));
	Platform platform0 = new Platform(integer0);
	system0.setPlatformId(platform0);
	Platform platform1 = system0.getPlatformId();
	assertNull(platform1.getModifiedBy());
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	Integer integer0 = new Integer(1);
	System system0 = new System(integer0);
	system0.setName("");
	String string0 = system0.getName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	System system0 = new System();
	Date mockDate0 = new Date();
	system0.setModifiedDt(mockDate0);
	Date date0 = system0.getModifiedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	System system0 = new System();
	system0.setModifiedBy("com.tsi.workflow.beans.dao.System");
	String string0 = system0.getModifiedBy();
	assertEquals("com.tsi.workflow.beans.dao.System", string0);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	System system0 = new System();
	system0.setModifiedBy("");
	String string0 = system0.getModifiedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	Integer integer0 = new Integer(1);
	System system0 = new System(integer0);
	system0.setLoadsetNamePrefix("@?w");
	String string0 = system0.getLoadsetNamePrefix();
	assertEquals("@?w", string0);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	System system0 = new System();
	system0.setLoadsetNamePrefix("");
	String string0 = system0.getLoadsetNamePrefix();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	Integer integer0 = new Integer(2605);
	System system0 = new System(integer0);
	LinkedList<LoadCategories> linkedList0 = new LinkedList<LoadCategories>();
	system0.setLoadCategoriesList(linkedList0);
	List<LoadCategories> list0 = system0.getLoadCategoriesList();
	assertTrue(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	System system0 = new System();
	system0.setIpaddress("ko;a}fIsX");
	String string0 = system0.getIpaddress();
	assertEquals("ko;a}fIsX", string0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	System system0 = new System();
	system0.setIpaddress("");
	String string0 = system0.getIpaddress();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	Integer integer0 = new Integer(0);
	System system0 = new System(integer0);
	Integer integer1 = system0.getId();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	Integer integer0 = new Integer(1);
	System system0 = new System(integer0);
	Integer integer1 = system0.getId();
	assertEquals(1, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	System system0 = new System();
	Integer integer0 = new Integer((-2156));
	system0.setId(integer0);
	Integer integer1 = system0.getId();
	assertEquals((-2156), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	System system0 = new System();
	Integer integer0 = new Integer(0);
	system0.setDefaultProdCpu(integer0);
	Integer integer1 = system0.getDefaultProdCpu();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	System system0 = new System();
	Integer integer0 = new Integer((-1412));
	system0.setDefaultProdCpu(integer0);
	Integer integer1 = system0.getDefaultProdCpu();
	assertEquals((-1412), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	Integer integer0 = new Integer(1);
	System system0 = new System(integer0);
	system0.setDefaultPreProdCpu(integer0);
	Integer integer1 = system0.getDefaultPreProdCpu();
	assertEquals(1, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	Integer integer0 = new Integer((-1360));
	System system0 = new System(integer0);
	system0.setDefaultPreProdCpu(integer0);
	Integer integer1 = system0.getDefaultPreProdCpu();
	assertEquals((-1360), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	Integer integer0 = new Integer((-327));
	System system0 = new System(integer0);
	system0.setDefaultNativeCpu(integer0);
	Integer integer1 = system0.getDefaultNativeCpu();
	assertEquals((-327), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	System system0 = new System();
	Integer integer0 = new Integer(1792);
	system0.setDefalutPutLevel(integer0);
	Integer integer1 = system0.getDefalutPutLevel();
	assertEquals(1792, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	Integer integer0 = new Integer(1);
	System system0 = new System(integer0);
	system0.setDefalutPutLevel(integer0);
	Integer integer1 = system0.getDefalutPutLevel();
	assertEquals(1, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	System system0 = new System();
	Integer integer0 = new Integer((-1));
	system0.setDefalutPutLevel(integer0);
	Integer integer1 = system0.getDefalutPutLevel();
	assertEquals((-1), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	System system0 = new System();
	LinkedList<Dbcr> linkedList0 = new LinkedList<Dbcr>();
	system0.setDbcrList(linkedList0);
	List<Dbcr> list0 = system0.getDbcrList();
	assertTrue(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	System system0 = new System();
	Date mockDate0 = new Date();
	system0.setCreatedDt(mockDate0);
	Date date0 = system0.getCreatedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	System system0 = new System();
	system0.setCreatedBy(" ]");
	String string0 = system0.getCreatedBy();
	assertEquals(" ]", string0);
    }

    @Test(timeout = 4000)
    public void test34() throws Throwable {
	System system0 = new System();
	LinkedList<Build> linkedList0 = new LinkedList<Build>();
	system0.setBuildList(linkedList0);
	List<Build> list0 = system0.getBuildList();
	assertEquals(0, list0.size());
    }

    @Test(timeout = 4000)
    public void test35() throws Throwable {
	System system0 = new System();
	LinkedList<Build> linkedList0 = new LinkedList<Build>();
	Build build0 = new Build();
	linkedList0.add(build0);
	system0.setBuildList(linkedList0);
	List<Build> list0 = system0.getBuildList();
	assertTrue(list0.contains(build0));
    }

    @Test(timeout = 4000)
    public void test36() throws Throwable {
	Integer integer0 = new Integer(1);
	System system0 = new System(integer0);
	system0.setActive("com.tsi.workflow.beans.dao.System[ id=1 ]");
	String string0 = system0.getActive();
	assertEquals("com.tsi.workflow.beans.dao.System[ id=1 ]", string0);
    }

    @Test(timeout = 4000)
    public void test37() throws Throwable {
	System system0 = new System();
	system0.setActive("");
	String string0 = system0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test38() throws Throwable {
	System system0 = new System();
	system0.setName("");
	int int0 = system0.compareTo(system0);
	assertEquals(0, int0);
    }

    @Test(timeout = 4000)
    public void test39() throws Throwable {
	Integer integer0 = new Integer((-1));
	System system0 = new System(integer0);
	Integer integer1 = new Integer(2605);
	System system1 = new System(integer1);
	boolean boolean0 = system0.equals(system1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test40() throws Throwable {
	System system0 = new System();
	Integer integer0 = new Integer(0);
	System system1 = new System(integer0);
	boolean boolean0 = system0.equals(system1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test41() throws Throwable {
	System system0 = new System();
	boolean boolean0 = system0.equals(system0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test42() throws Throwable {
	System system0 = new System();
	Object object0 = new Object();
	boolean boolean0 = system0.equals(object0);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test43() throws Throwable {
	Integer integer0 = new Integer((-1));
	System system0 = new System(integer0);
	boolean boolean0 = system0.equals(system0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test44() throws Throwable {
	Integer integer0 = new Integer(1);
	System system0 = new System(integer0);
	system0.hashCode();
    }

    @Test(timeout = 4000)
    public void test45() throws Throwable {
	System system0 = new System();
	system0.hashCode();
    }

    @Test(timeout = 4000)
    public void test46() throws Throwable {
	System system0 = new System();
	Date date0 = system0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test47() throws Throwable {
	System system0 = new System();
	List<SystemLoad> list0 = system0.getSystemLoadList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test48() throws Throwable {
	Integer integer0 = new Integer(1);
	System system0 = new System(integer0);
	List<PutLevel> list0 = system0.getPutLevelList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test49() throws Throwable {
	System system0 = new System();
	Integer integer0 = system0.getDefaultPreProdCpu();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test50() throws Throwable {
	System system0 = new System();
	Integer integer0 = new Integer(1);
	system0.setDefaultNativeCpu(integer0);
	Integer integer1 = system0.getDefaultNativeCpu();
	assertEquals(1, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test51() throws Throwable {
	System system0 = new System();
	List<Dbcr> list0 = system0.getDbcrList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test52() throws Throwable {
	System system0 = new System();
	system0.setCreatedBy("");
	String string0 = system0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test53() throws Throwable {
	System system0 = new System();
	LinkedList<ProductionLoads> linkedList0 = new LinkedList<ProductionLoads>();
	system0.setProductionLoadsList(linkedList0);
	List<ProductionLoads> list0 = system0.getProductionLoadsList();
	assertEquals(0, list0.size());
    }

    @Test(timeout = 4000)
    public void test54() throws Throwable {
	System system0 = new System();
	Integer integer0 = system0.getDefalutPutLevel();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test55() throws Throwable {
	Integer integer0 = new Integer(1);
	System system0 = new System(integer0);
	system0.setName("com.tsi.workflow.beans.dao.System[ id=1 ]");
	String string0 = system0.getName();
	assertEquals("com.tsi.workflow.beans.dao.System[ id=1 ]", string0);
    }

    @Test(timeout = 4000)
    public void test56() throws Throwable {
	Integer integer0 = new Integer(1);
	System system0 = new System(integer0);
	String string0 = system0.toString();
	assertEquals("com.tsi.workflow.beans.dao.System[ id=1 ]", string0);
    }

    @Test(timeout = 4000)
    public void test57() throws Throwable {
	System system0 = new System();
	String string0 = system0.getCreatedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test58() throws Throwable {
	Integer integer0 = new Integer(1);
	System system0 = new System(integer0);
	List<SystemCpu> list0 = system0.getSystemCpuList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test59() throws Throwable {
	System system0 = new System();
	List<SystemLoadActions> list0 = system0.getSystemLoadActionsList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test60() throws Throwable {
	System system0 = new System();
	Integer integer0 = system0.getId();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test61() throws Throwable {
	System system0 = new System();
	String string0 = system0.getLoadsetNamePrefix();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test62() throws Throwable {
	System system0 = new System();
	List<LoadCategories> list0 = system0.getLoadCategoriesList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test63() throws Throwable {
	System system0 = new System();
	Date date0 = system0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test64() throws Throwable {
	System system0 = new System();
	List<Build> list0 = system0.getBuildList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test65() throws Throwable {
	System system0 = new System();
	List<ProductionLoads> list0 = system0.getProductionLoadsList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test66() throws Throwable {
	Integer integer0 = new Integer((-1));
	System system0 = new System(integer0);
	Platform platform0 = system0.getPlatformId();
	assertNull(platform0);
    }

    @Test(timeout = 4000)
    public void test67() throws Throwable {
	Integer integer0 = new Integer(1);
	System system0 = new System(integer0);
	String string0 = system0.getActive();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test68() throws Throwable {
	Integer integer0 = new Integer(1);
	System system0 = new System(integer0);
	Integer integer1 = system0.getPortno();
	assertNull(integer1);
    }

    @Test(timeout = 4000)
    public void test69() throws Throwable {
	System system0 = new System();
	Integer integer0 = new Integer(1504);
	system0.setDefaultProdCpu(integer0);
	Integer integer1 = system0.getDefaultProdCpu();
	assertEquals(1504, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test70() throws Throwable {
	Integer integer0 = new Integer(1);
	System system0 = new System(integer0);
	String string0 = system0.getName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test71() throws Throwable {
	System system0 = new System();
	String string0 = system0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test72() throws Throwable {
	System system0 = new System();
	Integer integer0 = system0.getDefaultProdCpu();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test73() throws Throwable {
	Integer integer0 = new Integer(1);
	System system0 = new System(integer0);
	String string0 = system0.getIpaddress();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test74() throws Throwable {
	System system0 = new System();
	Integer integer0 = new Integer(0);
	system0.setDefaultPreProdCpu(integer0);
	Integer integer1 = system0.getDefaultPreProdCpu();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test75() throws Throwable {
	System system0 = new System();
	List<Vpars> list0 = system0.getVparsList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test76() throws Throwable {
	System system0 = new System();
	Integer integer0 = system0.getDefaultNativeCpu();
	assertNull(integer0);
    }
}
