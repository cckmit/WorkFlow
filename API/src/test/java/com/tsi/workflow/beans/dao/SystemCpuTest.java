
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
public class SystemCpuTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	System system0 = new System();
	systemCpu0.setSystemId(system0);
	System system1 = systemCpu0.getSystemId();
	assertNull(system1.getCreatedBy());
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	Integer integer0 = new Integer(4);
	SystemCpu systemCpu0 = new SystemCpu(integer0);
	systemCpu0.setSecondaryIpAddress(">$,]JE");
	String string0 = systemCpu0.getSecondaryIpAddress();
	assertEquals(">$,]JE", string0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	systemCpu0.setSecondaryIpAddress("");
	String string0 = systemCpu0.getSecondaryIpAddress();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu((Integer) null);
	LinkedList<ProductionLoads> linkedList0 = new LinkedList<ProductionLoads>();
	systemCpu0.setProductionLoadsList(linkedList0);
	List<ProductionLoads> list0 = systemCpu0.getProductionLoadsList();
	assertTrue(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	LinkedList<ProductionLoads> linkedList0 = new LinkedList<ProductionLoads>();
	ProductionLoads productionLoads0 = new ProductionLoads();
	linkedList0.add(productionLoads0);
	systemCpu0.setProductionLoadsList(linkedList0);
	List<ProductionLoads> list0 = systemCpu0.getProductionLoadsList();
	assertTrue(list0.contains(productionLoads0));
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu((Integer) null);
	systemCpu0.setPrimaryIpAddress("O*><");
	String string0 = systemCpu0.getPrimaryIpAddress();
	assertEquals("O*><", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu((Integer) null);
	systemCpu0.setPrimaryIpAddress("");
	String string0 = systemCpu0.getPrimaryIpAddress();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	Date mockDate0 = new Date();
	systemCpu0.setModifiedDt(mockDate0);
	Date date0 = systemCpu0.getModifiedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	systemCpu0.setModifiedBy("Zwb ->6Bc!L>L<,zib");
	String string0 = systemCpu0.getModifiedBy();
	assertEquals("Zwb ->6Bc!L>L<,zib", string0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	systemCpu0.setModifiedBy("");
	String string0 = systemCpu0.getModifiedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	Integer integer0 = new Integer(0);
	SystemCpu systemCpu0 = new SystemCpu(integer0);
	Integer integer1 = systemCpu0.getId();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	Integer integer0 = new Integer(1);
	SystemCpu systemCpu0 = new SystemCpu(integer0);
	Integer integer1 = systemCpu0.getId();
	assertEquals(1, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	Integer integer0 = new Integer((-2601));
	SystemCpu systemCpu0 = new SystemCpu(integer0);
	Integer integer1 = systemCpu0.getId();
	assertEquals((-2601), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu((Integer) null);
	systemCpu0.setDisplayName("com.tsi.workflow.beans.dao.SystemCpu[ id=null ]");
	String string0 = systemCpu0.getDisplayName();
	assertEquals("com.tsi.workflow.beans.dao.SystemCpu[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	systemCpu0.setDisplayName("");
	String string0 = systemCpu0.getDisplayName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu((Integer) null);
	Date mockDate0 = new Date(1906, (-1426), 0, 0, 1906, 0);
	systemCpu0.setCreatedDt(mockDate0);
	Date date0 = systemCpu0.getCreatedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu((Integer) null);
	systemCpu0.setCreatedBy("O*><");
	String string0 = systemCpu0.getCreatedBy();
	assertEquals("O*><", string0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	systemCpu0.setCreatedBy("");
	String string0 = systemCpu0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	Integer integer0 = new Integer(4);
	SystemCpu systemCpu0 = new SystemCpu(integer0);
	systemCpu0.setCpuType("");
	String string0 = systemCpu0.getCpuType();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	systemCpu0.setCpuName(" ]");
	String string0 = systemCpu0.getCpuName();
	assertEquals(" ]", string0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	systemCpu0.setCpuName("");
	String string0 = systemCpu0.getCpuName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	systemCpu0.setCpuCode("h");
	String string0 = systemCpu0.getCpuCode();
	assertEquals("h", string0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	systemCpu0.setCpuCode("");
	String string0 = systemCpu0.getCpuCode();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	systemCpu0.setActive(" ]");
	String string0 = systemCpu0.getActive();
	assertEquals(" ]", string0);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	systemCpu0.setActive("");
	String string0 = systemCpu0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	systemCpu0.setCpuName("wsa_Da8V!Mtu");
	int int0 = systemCpu0.compareTo(systemCpu0);
	assertEquals(0, int0);
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	systemCpu0.setCpuName("&G5QRSrhgA]Dp");
	Integer integer0 = new Integer(1474);
	SystemCpu systemCpu1 = new SystemCpu(integer0);
	systemCpu1.setCpuName("q");
	int int0 = systemCpu1.compareTo(systemCpu0);
	assertEquals(75, int0);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	Integer integer0 = new Integer(0);
	SystemCpu systemCpu0 = new SystemCpu(integer0);
	systemCpu0.setCpuName("BSf5,0T(pJt?9F");
	SystemCpu systemCpu1 = new SystemCpu(integer0);
	systemCpu1.setCpuName("QCF");
	int int0 = systemCpu0.compareTo(systemCpu1);
	assertEquals((-15), int0);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	String string0 = systemCpu0.getCpuName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	SystemCpu systemCpu1 = new SystemCpu((Integer) null);
	Integer integer0 = new Integer((-973));
	systemCpu0.setId(integer0);
	boolean boolean0 = systemCpu0.equals(systemCpu1);
	assertFalse(systemCpu1.equals((Object) systemCpu0));
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	Integer integer0 = new Integer(0);
	SystemCpu systemCpu1 = new SystemCpu(integer0);
	boolean boolean0 = systemCpu0.equals(systemCpu1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	Integer integer0 = new Integer((-973));
	systemCpu0.setId(integer0);
	boolean boolean0 = systemCpu0.equals(systemCpu0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	Integer integer0 = new Integer(4);
	SystemCpu systemCpu0 = new SystemCpu(integer0);
	boolean boolean0 = systemCpu0.equals((Object) null);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	boolean boolean0 = systemCpu0.equals(systemCpu0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test34() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	systemCpu0.hashCode();
    }

    @Test(timeout = 4000)
    public void test35() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	String string0 = systemCpu0.getCreatedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test36() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	Integer integer0 = systemCpu0.getId();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test37() throws Throwable {
	Integer integer0 = new Integer(4);
	SystemCpu systemCpu0 = new SystemCpu(integer0);
	String string0 = systemCpu0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test38() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	String string0 = systemCpu0.getCpuCode();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test39() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu((Integer) null);
	systemCpu0.setCpuType("org.apache.commons.io.filefilter.WildcardFilter");
	String string0 = systemCpu0.getCpuType();
	assertEquals("org.apache.commons.io.filefilter.WildcardFilter", string0);
    }

    @Test(timeout = 4000)
    public void test40() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	// Undeclared exception!
	try {
	    systemCpu0.compareTo(systemCpu0);
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {

	}
    }

    @Test(timeout = 4000)
    public void test41() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	Date date0 = systemCpu0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test42() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	String string0 = systemCpu0.getDisplayName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test43() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	String string0 = systemCpu0.getCpuType();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test44() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	System system0 = systemCpu0.getSystemId();
	assertNull(system0);
    }

    @Test(timeout = 4000)
    public void test45() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	List<ProductionLoads> list0 = systemCpu0.getProductionLoadsList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test46() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	String string0 = systemCpu0.getPrimaryIpAddress();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test47() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	Date date0 = systemCpu0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test48() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	String string0 = systemCpu0.getActive();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test49() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	String string0 = systemCpu0.toString();
	assertEquals("com.tsi.workflow.beans.dao.SystemCpu[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test50() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	Integer integer0 = new Integer((-22));
	systemCpu0.setId(integer0);
	systemCpu0.hashCode();
    }

    @Test(timeout = 4000)
    public void test51() throws Throwable {
	SystemCpu systemCpu0 = new SystemCpu();
	String string0 = systemCpu0.getSecondaryIpAddress();
	assertNull(string0);
    }
}
