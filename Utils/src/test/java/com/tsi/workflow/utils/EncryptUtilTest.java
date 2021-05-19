/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.utils;

import junit.framework.TestCase;

/**
 *
 * @author USER
 */
public class EncryptUtilTest extends TestCase {

    public EncryptUtilTest(String testName) {
	super(testName);
    }

    public void testEncrypt() {
	String input = "123";
	String salt = "123";
	String expResult = "123";
	EncryptUtil enc = new EncryptUtil();
	String resultE = EncryptUtil.encrypt(input, salt);
	String resultD = EncryptUtil.decrypt(resultE, salt);
	assertEquals(expResult, resultD);
	salt = null;
	enc.encrypt(input, salt);
	enc.decrypt(input, salt);
    }

}
