package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import org.junit.Test;

public class GIXMLUITest {

    @Test
    public void test00() throws Throwable {
	GIXMLUI gIXMLUI = new GIXMLUI();
	gIXMLUI.setXml("xml");
	String xml = gIXMLUI.getXml();
	assertEquals("xml", xml);
    }

}
