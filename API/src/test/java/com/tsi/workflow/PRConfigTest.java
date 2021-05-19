
package com.tsi.workflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PRConfigTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	PRConfig pRConfig0 = new PRConfig();
	pRConfig0.prDirPath = "UF$ZHD{]t@bvZneb";
	String string0 = pRConfig0.getPrFilePath();
	assertNotNull(string0);
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	PRConfig pRConfig0 = new PRConfig();
	pRConfig0.prDirPath = "";
	String string0 = pRConfig0.getPrFilePath();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	PRConfig pRConfig0 = new PRConfig();
	pRConfig0.prDomain = "R>";
	String string0 = pRConfig0.getPRUserDomain();
	assertEquals("R>", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	PRConfig pRConfig0 = new PRConfig();
	pRConfig0.prDomain = "";
	String string0 = pRConfig0.getPRUserDomain();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	PRConfig pRConfig0 = new PRConfig();
	pRConfig0.prUser = "%=mm6gKE'";
	String string0 = pRConfig0.getPRUser();
	assertEquals("%=mm6gKE'", string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	PRConfig pRConfig0 = new PRConfig();
	pRConfig0.prUser = "";
	String string0 = pRConfig0.getPRUser();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	PRConfig pRConfig0 = new PRConfig();
	pRConfig0.prPassword = "";
	pRConfig0.prPassword = "IPND?{lloRn";
	String string0 = pRConfig0.getPRPassword();
	assertEquals("IPND?{lloRn", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	PRConfig pRConfig0 = new PRConfig();
	pRConfig0.prPassword = "";
	String string0 = pRConfig0.getPRPassword();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	PRConfig pRConfig0 = new PRConfig();
	String string0 = pRConfig0.getPrFilePath();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	PRConfig pRConfig0 = new PRConfig();
	String string0 = pRConfig0.getPRUserDomain();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	PRConfig pRConfig0 = new PRConfig();
	String string0 = pRConfig0.getPRPassword();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	PRConfig pRConfig0 = new PRConfig();
	String string0 = pRConfig0.getPRUser();
	assertNull(string0);
    }
}
