/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import com.tsi.workflow.base.MailMessage;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author yeshwanth.shenoy
 */
public class MailListenerTest {

    public MailListenerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of sendMails method, of class MailListener.
     */
    @Test
    public void testSendMails() {
	MailListener instance = new MailListener();
	BlockingQueue<MailMessage> mailQueue = new ArrayBlockingQueue<>(1);
	MailMessage mailMessage = mock(MailMessage.class);
	mailQueue.add(mailMessage);
	instance.mailQueue = mailQueue;
	instance.sendMails();
    }

    /**
     * Test of sendMails method, of class MailListener.
     */
    @Test
    public void testSendMails2() {
	MailListener instance = new MailListener();
	BlockingQueue<MailMessage> mailQueue = new ArrayBlockingQueue<>(1);
	MailMessage mailMessage = mock(MailMessage.class);
	mailQueue.add(mailMessage);
	instance.mailQueue = mailQueue;
	doThrow(new RuntimeException()).when(mailMessage).processMessage();
	instance.sendMails();
    }

}
