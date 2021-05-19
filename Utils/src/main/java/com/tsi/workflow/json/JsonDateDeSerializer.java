/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.tsi.workflow.utils.Constants;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 *
 * @author USER
 */
public class JsonDateDeSerializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser jsonparser, DeserializationContext dc) throws IOException, JsonProcessingException {
	String date = jsonparser.getText();
	try {
	    Date lDate = Constants.APP_DATE_TIME_FORMAT.get().parse(date);
	    return lDate;
	} catch (ParseException e) {
	    throw new RuntimeException(e);
	}
    }

}
