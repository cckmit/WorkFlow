/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import com.tsi.workflow.gi.request.Request;
import com.tsi.workflow.gi.response.Projects;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

/**
 *
 * @author prabhu.prabhakaran
 */
public class GIXMLHelper {

    public Projects convertResponseToObject(String pProjects) {
	return JAXB.unmarshal(new StringReader(pProjects), Projects.class);
    }

    public String convertRequestToString(Request pRequest) {
	try {
	    StringWriter stringWriter = new StringWriter();
	    JAXBContext context = JAXBContext.newInstance(Request.class);
	    Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
	    m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
	    m.marshal(pRequest, stringWriter);
	    stringWriter.close();
	    return "<?xml version=\"1.0\" ?>" + stringWriter.toString();
	} catch (Exception ex) {
	}
	return "";
    }
}
