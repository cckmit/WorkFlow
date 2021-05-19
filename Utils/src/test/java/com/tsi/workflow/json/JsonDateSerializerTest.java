/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.json;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author deepa.jayakumar
 */
public class JsonDateSerializerTest {

    public JsonDateSerializerTest() {
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
     * Test of serialize method, of class JsonDateSerializer.
     */
    @Test
    public void testSerialize() throws Exception {
	Date date = new Date();
	JsonGenerator gen = mock(JsonGenerator.class);
	SerializerProvider provider = mock(SerializerProvider.class);
	JsonDateSerializer instance = spy(new JsonDateSerializer());
	instance.serialize(date, gen, provider);
    }

}
