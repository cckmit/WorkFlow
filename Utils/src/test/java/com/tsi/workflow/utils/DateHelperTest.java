package com.tsi.workflow.utils;

import static org.junit.Assert.*;

import java.util.Date;
import junit.framework.TestCase;
import org.junit.Test;

public class DateHelperTest extends TestCase {

    public DateHelperTest(String test) {
	super(test);
    }

    @Test
    public void test() {
	Date date = new Date();
	String test = "test";
	String result = DateHelper.convertGMTtoEST(date);
	String result2 = DateHelper.convertGMTtoESTDate(date);
	assertNotEquals(test, result);
	assertNotEquals(test, result2);
	DateHelper dateHelper = new DateHelper();
	dateHelper.convertGMTtoEST(date);
	dateHelper.convertGMTtoESTDate(date);
    }

}
