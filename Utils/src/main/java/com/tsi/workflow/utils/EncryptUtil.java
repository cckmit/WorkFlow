/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.utils;

import org.apache.log4j.Logger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 *
 * @author User
 */
public class EncryptUtil {

    private static final Logger LOG = Logger.getLogger(EncryptUtil.class.getName());

    public static String encrypt(String input, String salt) {
	try {
	    StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
	    encryptor.setPassword(salt);
	    encryptor.setAlgorithm(Constants.APP_ENCRYPT_ALGORITHM);
	    encryptor.setStringOutputType("0X");
	    return encryptor.encrypt(input);
	} catch (Exception e) {
	    LOG.error("Error in Encryprion", e);
	}
	return "";
    }

    public static String decrypt(String input, String salt) {
	try {
	    StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
	    decryptor.setPassword(salt);
	    decryptor.setAlgorithm(Constants.APP_ENCRYPT_ALGORITHM);
	    decryptor.setStringOutputType("0X");
	    return decryptor.decrypt(input);
	} catch (Exception e) {
	    LOG.error("Error in Decryprion", e);
	}
	return "";
    }

}
