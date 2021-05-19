/// *
// * To change this license header, choose License Headers in Project
/// Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
// package com.tsi.workflow.base;
//
// import com.tsi.workflow.mail.DSLFileErrorMail;
// import org.junit.After;
// import org.junit.AfterClass;
// import static org.junit.Assert.assertNotNull;
// import org.junit.Before;
// import org.junit.BeforeClass;
// import org.junit.Test;
// import org.mockito.Mockito;
// import static org.mockito.Mockito.mock;
// import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
// import org.springframework.context.ApplicationContext;
//
/// **
// *
// * @author yeshwanth.shenoy
// */
// public class MailMessageFactoryTest {
//
// public MailMessageFactoryTest() {
// }
//
// @BeforeClass
// public static void setUpClass() {
// }
//
// @AfterClass
// public static void tearDownClass() {
// }
//
// @Before
// public void setUp() {
// }
//
// @After
// public void tearDown() {
// }
//
// /**
// * Test of getTemplate method, of class MailMessageFactory.
// */
// @Test
// public void testGetTemplate() {
// Class<? extends MailMessage> pClassName = DSLFileErrorMail.class;
// MailMessageFactory instance = new MailMessageFactory();
// instance.applicationContext = Mockito.mock(ApplicationContext.class);
// Mockito.when(instance.applicationContext.getAutowireCapableBeanFactory()).thenReturn(mock(AutowireCapableBeanFactory.class));
// MailMessage result = instance.getTemplate(pClassName);
// assertNotNull(result);
// }
//
// /**
// * Test of getTemplate method, of class MailMessageFactory.
// */
// @Test
// public void testGetTemplate2() {
// Class<? extends MailMessage> pClassName = DSLFileErrorMail.class;
// MailMessageFactory instance = new MailMessageFactory();
// instance.applicationContext = Mockito.mock(ApplicationContext.class);
// try {
// MailMessage result = instance.getTemplate(pClassName);
// } catch (Exception ex) {
// assert (true);
// }
// }
//
// }
