package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import org.junit.Test;

public class CommonMailTest {

    @Test
    public void test() {
	CommonMail commonMail = new CommonMail();
	commonMail.processMessage();
    }

}
