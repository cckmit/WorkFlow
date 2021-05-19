
package com.tsi.workflow.beans.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JobDetailsTest {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	JobDetails jobDetails0 = new JobDetails();
	jobDetails0.setSystemName("");
	String string0 = jobDetails0.getSystemName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	JobDetails jobDetails0 = new JobDetails();
	jobDetails0.setStatus("");
	String string0 = jobDetails0.getStatus();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	JobDetails jobDetails0 = new JobDetails();
	jobDetails0.setPlanId("9@");
	String string0 = jobDetails0.getPlanId();
	assertEquals("9@", string0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	JobDetails jobDetails0 = new JobDetails();
	String string0 = jobDetails0.getSystemName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	JobDetails jobDetails0 = new JobDetails();
	String string0 = jobDetails0.getStatus();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	JobDetails jobDetails0 = new JobDetails();
	String string0 = jobDetails0.getPlanId();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	JobDetails jobDetails0 = new JobDetails();
	jobDetails0.setStatus("Q");
	String string0 = jobDetails0.getStatus();
	assertEquals("Q", string0);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	JobDetails jobDetails0 = new JobDetails();
	jobDetails0.setSystemName("2:u%");
	String string0 = jobDetails0.getSystemName();
	assertEquals("2:u%", string0);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	JobDetails jobDetails0 = new JobDetails();
	jobDetails0.setPlanId("");
	jobDetails0.setMessage("test");
	String string0 = jobDetails0.getPlanId();
	String string1 = jobDetails0.getMessage();
	assertNotNull(string1);
	assertEquals("", string0);
    }
}
