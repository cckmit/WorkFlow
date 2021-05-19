package com.tsi.workflow.beans.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class YodaResultTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	YodaResult yodaResult0 = new YodaResult();
	yodaResult0.setRc(1914);
	int int0 = yodaResult0.getRc();
	assertEquals(1914, int0);
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	YodaResult yodaResult0 = new YodaResult();
	yodaResult0.setRc((-1699));
	int int0 = yodaResult0.getRc();
	assertEquals((-1699), int0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	YodaResult yodaResult0 = new YodaResult();
	yodaResult0.setMessage("\"w8'tab{=Qa2d3PLFp");
	String string0 = yodaResult0.getMessage();
	assertEquals("\"w8'tab{=Qa2d3PLFp", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	YodaResult yodaResult0 = new YodaResult();
	yodaResult0.setMessage("");
	String string0 = yodaResult0.getMessage();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	YodaResult yodaResult0 = new YodaResult();
	yodaResult0.setLab(", Message : ");
	String string0 = yodaResult0.getLab();
	assertEquals(", Message : ", string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	YodaResult yodaResult0 = new YodaResult();
	yodaResult0.setLab("");
	String string0 = yodaResult0.getLab();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	YodaResult yodaResult0 = new YodaResult();
	yodaResult0.setIp("");
	String string0 = yodaResult0.getIp();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	YodaResult yodaResult0 = new YodaResult();
	String string0 = yodaResult0.getMessage();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	YodaResult yodaResult0 = new YodaResult();
	yodaResult0.setMessage("ERROR:");
	// Undeclared exception!
	try {
	    yodaResult0.getLogMessage();
	    fail("Expecting exception: StringIndexOutOfBoundsException");

	} catch (StringIndexOutOfBoundsException e) {
	}
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	YodaResult yodaResult0 = new YodaResult();
	String string0 = yodaResult0.getLab();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	YodaResult yodaResult0 = new YodaResult();
	yodaResult0.setMessage("V}lzCWbQGN");
	String string0 = yodaResult0.getLogMessage();
	assertEquals(", Message : V}lzCWbQGN", string0);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	YodaResult yodaResult0 = new YodaResult();
	yodaResult0.setIp("V}lzCWbQGN");
	String string0 = yodaResult0.getIp();
	assertEquals("V}lzCWbQGN", string0);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	YodaResult yodaResult0 = new YodaResult();
	String string0 = yodaResult0.getLogMessage();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	YodaResult yodaResult0 = new YodaResult();
	String string0 = yodaResult0.getIp();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	YodaResult yodaResult0 = new YodaResult();
	int int0 = yodaResult0.getRc();
	assertEquals(0, int0);
    }
}
