package com.tsi.workflow.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Properties;
import javax.servlet.ServletContextEvent;
import org.junit.Before;
import org.junit.Test;

public class InitCorsContextTest {

    @Before
    public void setUp() throws Exception {
    }

    final ServletContextEvent event = mock(ServletContextEvent.class);

    @Test
    public void test00() {
	InitCorsContext initCorsContext = new InitCorsContext();
	initCorsContext.contextDestroyed(event);
    }

    @Test
    public void test01() {
	InitCorsContext instance = spy(new InitCorsContext());
	Properties prop = mock(Properties.class);
	when(prop.getProperty("wf.cors.env")).thenReturn("AV");
	System.setProperty("wf.cors.env", "AV");
	instance.contextInitialized(event);
	when(prop.getProperty("wf.git.search.type")).thenReturn("AV");
	System.setProperty("wf.git.search.type", "AV");
	instance.contextInitialized(event);
	when(prop.getProperty("wf.cache.session.key")).thenReturn("AV");
	instance.contextInitialized(event);

    }

}
