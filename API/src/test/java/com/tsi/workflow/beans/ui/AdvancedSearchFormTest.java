
package com.tsi.workflow.beans.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.tsi.workflow.beans.dao.System;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class AdvancedSearchFormTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	advancedSearchForm0.targetSystems = null;
	List<System> list0 = advancedSearchForm0.getTargetSystems();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	LinkedList<System> linkedList0 = new LinkedList<System>();
	linkedList0.add((System) null);
	advancedSearchForm0.setTargetSystems(linkedList0);
	List<System> list0 = advancedSearchForm0.getTargetSystems();
	assertEquals(1, list0.size());
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	Date mockDate0 = new Date();
	advancedSearchForm0.setStartDate(mockDate0);
	Date date0 = advancedSearchForm0.getStartDate();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	LinkedList<String> linkedList0 = new LinkedList<String>();
	linkedList0.addLast("");
	advancedSearchForm0.setRole(linkedList0);
	List<String> list0 = advancedSearchForm0.getRole();
	assertTrue(list0.contains(""));
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	LinkedList<Locale.LanguageRange> linkedList0 = new LinkedList<Locale.LanguageRange>();
	List<String> list0 = Locale.filterTags((List<Locale.LanguageRange>) linkedList0, (Collection<String>) null);
	advancedSearchForm0.setRole(list0);
	List<String> list1 = advancedSearchForm0.getRole();
	assertSame(list1, list0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	advancedSearchForm0.setProgramName("<");
	String string0 = advancedSearchForm0.getProgramName();
	assertEquals("<", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	advancedSearchForm0.setProgramName("");
	String string0 = advancedSearchForm0.getProgramName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	LinkedList<String> linkedList0 = new LinkedList<String>();
	advancedSearchForm0.setImplPlanStatus(linkedList0);
	linkedList0.add("}yDA@<xSJt");
	List<String> list0 = advancedSearchForm0.getImplPlanStatus();
	assertTrue(list0.contains("}yDA@<xSJt"));
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	LinkedList<Locale.LanguageRange> linkedList0 = new LinkedList<Locale.LanguageRange>();
	Locale.FilteringMode locale_FilteringMode0 = Locale.FilteringMode.REJECT_EXTENDED_RANGES;
	List<String> list0 = Locale.filterTags((List<Locale.LanguageRange>) linkedList0, (Collection<String>) null, locale_FilteringMode0);
	advancedSearchForm0.setImplPlanStatus(list0);
	List<String> list1 = advancedSearchForm0.getImplPlanStatus();
	assertSame(list1, list0);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	LinkedList<String> linkedList0 = new LinkedList<String>();
	linkedList0.addFirst("");
	advancedSearchForm0.setFunctionalPackages(linkedList0);
	List<String> list0 = advancedSearchForm0.getFunctionalPackages();
	assertTrue(list0.contains(""));
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	LinkedList<Locale.LanguageRange> linkedList0 = new LinkedList<Locale.LanguageRange>();
	Locale.FilteringMode locale_FilteringMode0 = Locale.FilteringMode.REJECT_EXTENDED_RANGES;
	List<String> list0 = Locale.filterTags((List<Locale.LanguageRange>) linkedList0, (Collection<String>) null, locale_FilteringMode0);
	advancedSearchForm0.setFunctionalPackages(list0);
	List<String> list1 = advancedSearchForm0.getFunctionalPackages();
	assertSame(list1, list0);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	Date mockDate0 = new Date(5, 38, (-1), 5, 0);
	advancedSearchForm0.setEndDate(mockDate0);
	Date date0 = advancedSearchForm0.getEndDate();
	assertNotEquals("Fri Feb 28 05:00:00 GMT 1908", date0.toString());
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	LinkedList<String> linkedList0 = new LinkedList<String>();
	linkedList0.add("Pattern is missing");
	advancedSearchForm0.setCsrNumber(linkedList0);
	List<String> list0 = advancedSearchForm0.getCsrNumber();
	assertTrue(list0.contains("Pattern is missing"));
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	LinkedList<Locale.LanguageRange> linkedList0 = new LinkedList<Locale.LanguageRange>();
	Set<String> set0 = ZoneId.getAvailableZoneIds();
	Locale.FilteringMode locale_FilteringMode0 = Locale.FilteringMode.AUTOSELECT_FILTERING;
	List<String> list0 = Locale.filterTags((List<Locale.LanguageRange>) linkedList0, (Collection<String>) set0, locale_FilteringMode0);
	advancedSearchForm0.setCsrNumber(list0);
	List<String> list1 = advancedSearchForm0.getCsrNumber();
	assertSame(list1, list0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	String string0 = advancedSearchForm0.getProgramName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	Date date0 = advancedSearchForm0.getEndDate();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	List<String> list0 = advancedSearchForm0.getFunctionalPackages();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	List<System> list0 = advancedSearchForm0.getTargetSystems();
	advancedSearchForm0.setTargetSystems(list0);
	assertNull(advancedSearchForm0.getProgramName());
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	Date date0 = advancedSearchForm0.getStartDate();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	List<String> list0 = advancedSearchForm0.getImplPlanStatus();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	List<String> list0 = advancedSearchForm0.getRole();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	AdvancedSearchForm advancedSearchForm0 = new AdvancedSearchForm();
	List<String> list0 = advancedSearchForm0.getCsrNumber();
	assertNull(list0);
    }
}
