/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.utils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author USER
 */
public class WFLOGGER {

    private static final Logger LOG = Logger.getLogger(WFLOGGER.class.getName());

    public static void LOG(Class pClass, Level pLevel, String pMessage) {
	LOG.log(pLevel, encode(pMessage));
    }

    public static void LOG(Class pClass, Level pLevel, String pMessage, Throwable exception) {
	LOG.log(pClass.getSimpleName(), pLevel, encode(pMessage), exception);
    }

    public static String encode(String message) {
	message = message.replace('\n', '_').replace('\r', '_').replace('\t', '_');
	return message;
    }
}
