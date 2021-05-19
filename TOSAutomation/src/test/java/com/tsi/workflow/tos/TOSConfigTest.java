/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class TOSConfigTest {

    public TOSConfigTest() {
    }

    @Test
    public void testGetInstance() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, IOException {
	TOSConfig instance = TOSConfig.getInstance();
	assertNotNull(instance);
	instance.properties = mock(Properties.class);
	when(instance.properties.getProperty("service.password")).thenReturn("ENC(3FB59808F28A8ABCA9C915B26FB516003D3A2CFE11F90AAFE46E909345556E3E2DC173F5F40C103E43B134F54D2E21B3A9681EDC804BD0F7)");
	String result = instance.getServiceUserID();
	assertNull(result);
	Field field = TOSConfig.class.getDeclaredField("lThreadCount");
	field.setAccessible(true);
	field.set(null, 20);
	result = instance.getNextThreadName();
	assertNotNull(result);
	result = instance.getServiceSecret();
	assertNotNull(result);
	when(instance.properties.getProperty("service.password")).thenReturn("ABC");
	result = instance.getServiceSecret();
	assertNotNull(result);
    }

}
