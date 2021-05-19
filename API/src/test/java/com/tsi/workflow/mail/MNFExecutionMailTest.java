package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import org.junit.Test;

public class MNFExecutionMailTest {

    @Test
    public void test00() throws Throwable {
	MNFExecutionMail mNFExecutionMail = new MNFExecutionMail();
	mNFExecutionMail.setInfo("info");
	assertEquals("info", mNFExecutionMail.getInfo());
	mNFExecutionMail.setKey("key");
	assertEquals("key", mNFExecutionMail.getKey());
	mNFExecutionMail.setSystemName("WSP");
	assertEquals("WSP", mNFExecutionMail.getSystemName());
	mNFExecutionMail.processMessage();

    }

}
