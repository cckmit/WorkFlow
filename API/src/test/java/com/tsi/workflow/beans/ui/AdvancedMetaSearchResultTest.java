
package com.tsi.workflow.beans.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Date;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class AdvancedMetaSearchResultTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setTargetsystem("Z7u9YV^(t");
	String string0 = advancedMetaSearchResult0.getTargetsystem();
	assertEquals("Z7u9YV^(t", string0);
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setTargetsystem("");
	String string0 = advancedMetaSearchResult0.getTargetsystem();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setQastatus("The magic number must contain at least one byte");
	String string0 = advancedMetaSearchResult0.getQastatus();
	assertEquals("The magic number must contain at least one byte", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setProjectname("The list of names must not be null");
	String string0 = advancedMetaSearchResult0.getProjectname();
	assertEquals("The list of names must not be null", string0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setProjectname("");
	String string0 = advancedMetaSearchResult0.getProjectname();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setProgramname("com.tsi.workflow.beans.ui.AdvancedMetaSearchResult");
	String string0 = advancedMetaSearchResult0.getProgramname();
	assertEquals("com.tsi.workflow.beans.ui.AdvancedMetaSearchResult", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setProgramname("");
	String string0 = advancedMetaSearchResult0.getProgramname();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setPlanstatus("org.apache.commons.io.filefilter.WildcardFilter");
	String string0 = advancedMetaSearchResult0.getPlanstatus();
	assertEquals("org.apache.commons.io.filefilter.WildcardFilter", string0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setPlanstatus("");
	String string0 = advancedMetaSearchResult0.getPlanstatus();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setPlanid("7|.^%Rha&zWizjK");
	String string0 = advancedMetaSearchResult0.getPlanid();
	assertEquals("7|.^%Rha&zWizjK", string0);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setPlandescription("");
	String string0 = advancedMetaSearchResult0.getPlandescription();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setPeerreviewer("L>}&{}\"");
	String string0 = advancedMetaSearchResult0.getPeerreviewer();
	assertEquals("L>}&{}\"", string0);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setPeerreviewer("");
	String string0 = advancedMetaSearchResult0.getPeerreviewer();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setManagername("");
	String string0 = advancedMetaSearchResult0.getManagername();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setLoadinstruction("q'-,v");
	String string0 = advancedMetaSearchResult0.getLoadinstruction();
	assertEquals("q'-,v", string0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setLoadinstruction("");
	String string0 = advancedMetaSearchResult0.getLoadinstruction();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	Date date0 = new Date();
	advancedMetaSearchResult0.setLoaddatetime(date0);
	Date date1 = advancedMetaSearchResult0.getLoaddatetime();
	assertNotEquals("Thu Jan 01 00:00:01 GMT 1970", date1.toString());
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setLoadcategory("~K{I^H(xl^Y");
	String string0 = advancedMetaSearchResult0.getLoadcategory();
	assertEquals("~K{I^H(xl^Y", string0);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setLoadattendee("]%vOOB");
	String string0 = advancedMetaSearchResult0.getLoadattendee();
	assertEquals("]%vOOB", string0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setLoadattendee("");
	String string0 = advancedMetaSearchResult0.getLoadattendee();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setLeadname("Can not update object of type ");
	String string0 = advancedMetaSearchResult0.getLeadname();
	assertEquals("Can not update object of type ", string0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setLeadname("");
	String string0 = advancedMetaSearchResult0.getLeadname();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setFunctionalarea("rGz6{@}");
	String string0 = advancedMetaSearchResult0.getFunctionalarea();
	assertEquals("rGz6{@}", string0);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setFunctionalarea("");
	String string0 = advancedMetaSearchResult0.getFunctionalarea();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	Date mockDate0 = new Date();
	advancedMetaSearchResult0.setFallbackdatetime(mockDate0);
	Date date0 = advancedMetaSearchResult0.getFallbackdatetime();
	assertNotEquals("Fri Feb 14 20:21:21 GMT 2014", date0.toString());
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setDevelopername("e>-(");
	String string0 = advancedMetaSearchResult0.getDevelopername();
	assertEquals("e>-(", string0);
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setDevelopername("");
	String string0 = advancedMetaSearchResult0.getDevelopername();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setDbcrname("nwKni52|DF/H");
	String string0 = advancedMetaSearchResult0.getDbcrname();
	assertEquals("nwKni52|DF/H", string0);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setDbcrname("");
	String string0 = advancedMetaSearchResult0.getDbcrname();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setCsrnumber("_)LbcygWez}P-oo}");
	String string0 = advancedMetaSearchResult0.getCsrnumber();
	assertEquals("_)LbcygWez}P-oo}", string0);
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setCsrnumber("");
	String string0 = advancedMetaSearchResult0.getCsrnumber();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	Date mockDate0 = new Date(2, 0, (-1479));
	advancedMetaSearchResult0.setActivateddatetime(mockDate0);
	Date date0 = advancedMetaSearchResult0.getActivateddatetime();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	String string0 = advancedMetaSearchResult0.getDevelopername();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setManagername("com.tsi.workflow.beans.ui.AdvancedMetaSearchResult");
	String string0 = advancedMetaSearchResult0.getManagername();
	assertEquals("com.tsi.workflow.beans.ui.AdvancedMetaSearchResult", string0);
    }

    @Test(timeout = 4000)
    public void test34() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	String string0 = advancedMetaSearchResult0.getFunctionalarea();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test35() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	Date date0 = advancedMetaSearchResult0.getFallbackdatetime();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test36() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	String string0 = advancedMetaSearchResult0.getLoadattendee();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test37() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	String string0 = advancedMetaSearchResult0.getQastatus();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test38() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setLoadcategory("");
	String string0 = advancedMetaSearchResult0.getLoadcategory();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test39() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setQastatus("");
	String string0 = advancedMetaSearchResult0.getQastatus();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test40() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	String string0 = advancedMetaSearchResult0.getPlandescription();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test41() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	String string0 = advancedMetaSearchResult0.getProgramname();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test42() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	String string0 = advancedMetaSearchResult0.getCsrnumber();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test43() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setPlandescription("Can not update object of type ");
	String string0 = advancedMetaSearchResult0.getPlandescription();
	assertEquals("Can not update object of type ", string0);
    }

    @Test(timeout = 4000)
    public void test44() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	advancedMetaSearchResult0.setPlanid("");
	String string0 = advancedMetaSearchResult0.getPlanid();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test45() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	Date date0 = advancedMetaSearchResult0.getActivateddatetime();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test46() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	String string0 = advancedMetaSearchResult0.getTargetsystem();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test47() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	String string0 = advancedMetaSearchResult0.getPlanid();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test48() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	String string0 = advancedMetaSearchResult0.getProjectname();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test49() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	Date date0 = advancedMetaSearchResult0.getLoaddatetime();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test50() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	String string0 = advancedMetaSearchResult0.getLoadcategory();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test51() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	String string0 = advancedMetaSearchResult0.getDbcrname();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test52() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	String string0 = advancedMetaSearchResult0.getLeadname();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test53() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	String string0 = advancedMetaSearchResult0.getPeerreviewer();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test54() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	String string0 = advancedMetaSearchResult0.getPlanstatus();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test55() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	String string0 = advancedMetaSearchResult0.getLoadinstruction();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test56() throws Throwable {
	AdvancedMetaSearchResult advancedMetaSearchResult0 = new AdvancedMetaSearchResult();
	String string0 = advancedMetaSearchResult0.getManagername();
	assertNull(string0);
    }

}
