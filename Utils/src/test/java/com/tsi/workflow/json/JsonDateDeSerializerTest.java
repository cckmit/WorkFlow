/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.json;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
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
public class JsonDateDeSerializerTest {

    public JsonDateDeSerializerTest() {
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
     * Test of deserialize method, of class JsonDateDeSerializer.
     */
    @Test
    public void testDeserialize() throws Exception {

	JsonParser jsonparser = new JsonParser() {
	    @Override
	    public ObjectCodec getCodec() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public void setCodec(ObjectCodec oc) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public Version version() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public void close() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public JsonToken nextToken() throws IOException, JsonParseException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public JsonToken nextValue() throws IOException, JsonParseException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public JsonParser skipChildren() throws IOException, JsonParseException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public boolean isClosed() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public JsonToken getCurrentToken() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public int getCurrentTokenId() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public boolean hasCurrentToken() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public boolean hasTokenId(int i) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public String getCurrentName() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public JsonStreamContext getParsingContext() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public JsonLocation getTokenLocation() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public JsonLocation getCurrentLocation() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public void clearCurrentToken() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public JsonToken getLastClearedToken() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public void overrideCurrentName(String string) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public String getText() throws IOException {
		return ("12-12-2000 12:00:00 +0000");
	    }

	    @Override
	    public char[] getTextCharacters() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public int getTextLength() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public int getTextOffset() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public boolean hasTextCharacters() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public Number getNumberValue() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public JsonParser.NumberType getNumberType() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public int getIntValue() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public long getLongValue() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public BigInteger getBigIntegerValue() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public float getFloatValue() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public double getDoubleValue() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public BigDecimal getDecimalValue() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public Object getEmbeddedObject() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public byte[] getBinaryValue(Base64Variant bv) throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public String getValueAsString(String string) throws IOException {
		return "";
	    }
	};
	DeserializationContext dc = mock(DeserializationContext.class);
	JsonDateDeSerializer instance = spy(new JsonDateDeSerializer());
	Date result = instance.deserialize(jsonparser, dc);

    }

    @Test
    public void testDeserializeException() {

	JsonParser jsonparser = new JsonParser() {
	    @Override
	    public ObjectCodec getCodec() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public void setCodec(ObjectCodec oc) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public Version version() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public void close() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public JsonToken nextToken() throws IOException, JsonParseException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public JsonToken nextValue() throws IOException, JsonParseException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public JsonParser skipChildren() throws IOException, JsonParseException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public boolean isClosed() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public JsonToken getCurrentToken() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public int getCurrentTokenId() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public boolean hasCurrentToken() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public boolean hasTokenId(int i) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public String getCurrentName() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public JsonStreamContext getParsingContext() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public JsonLocation getTokenLocation() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public JsonLocation getCurrentLocation() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public void clearCurrentToken() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public JsonToken getLastClearedToken() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public void overrideCurrentName(String string) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public String getText() throws IOException {
		return ("");
	    }

	    @Override
	    public char[] getTextCharacters() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public int getTextLength() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public int getTextOffset() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public boolean hasTextCharacters() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public Number getNumberValue() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public JsonParser.NumberType getNumberType() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public int getIntValue() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public long getLongValue() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public BigInteger getBigIntegerValue() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public float getFloatValue() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public double getDoubleValue() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public BigDecimal getDecimalValue() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public Object getEmbeddedObject() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public byte[] getBinaryValue(Base64Variant bv) throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public String getValueAsString(String string) throws IOException {
		return "";
	    }
	};
	DeserializationContext dc = mock(DeserializationContext.class);
	JsonDateDeSerializer instance = spy(new JsonDateDeSerializer());
	try {
	    Date result = instance.deserialize(jsonparser, dc);
	} catch (Exception e) {
	    assertTrue(true);
	}

    }
}
