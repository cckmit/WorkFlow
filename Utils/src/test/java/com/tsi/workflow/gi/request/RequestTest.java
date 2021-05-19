package com.tsi.workflow.gi.request;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RequestTest {

    private Request request;

    @Before
    public void setUp() throws Exception {
	request = new Request();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRequest() {
	assertNotNull(request.getService());
    }

}
