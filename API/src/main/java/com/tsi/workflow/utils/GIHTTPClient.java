/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.utils;

import com.tsi.workflow.gi.request.Request;
import com.tsi.workflow.gi.response.Projects;
import com.tsi.workflow.helper.GIXMLHelper;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

/**
 *
 * @author prabhu.prabhakaran
 */
public class GIHTTPClient {

    String lListAllProjects = "<Request><Service><Projects-List /></Service></Request>";
    HttpClient client;
    private static final Logger LOG = Logger.getLogger(GIHTTPClient.class.getName());

    public GIHTTPClient() {
	client = new DefaultHttpClient();
    }

    public Projects getProjectList(String hostName, Integer port) throws Exception {
	URIBuilder lBuilder = new URIBuilder("http://" + hostName + ":" + port + "/app/projects/xmlrequest");
	HttpPut lPut = new HttpPut(lBuilder.build());
	lPut.setEntity(new StringEntity(lListAllProjects));
	HttpResponse lResponse = client.execute(lPut);
	List<String> readLines = IOUtils.readLines(lResponse.getEntity().getContent());
	StatusLine statusLine = lResponse.getStatusLine();
	int statusCode = statusLine.getStatusCode();
	if (statusCode == 200) {
	    String lXML = String.join(System.lineSeparator(), readLines);
	    if (lXML == null || lXML.trim().isEmpty()) {
		return new Projects();
	    } else {
		return new GIXMLHelper().convertResponseToObject(lXML);
	    }
	} else {
	    LOG.error("Error in GI Project Sync, HTTP Error Code " + statusCode);
	    return null;
	}
    }

    public JSONResponse processProjectSync(String hostName, Integer port, Request request) throws Exception {
	URIBuilder lBuilder = new URIBuilder("http://" + hostName + ":" + port + "/app/projects/xmlrequest");
	HttpPut lPut = new HttpPut(lBuilder.build());
	String xml = new GIXMLHelper().convertRequestToString(request);
	lPut.setEntity(new StringEntity(xml));
	HttpResponse lHTTPResponse = client.execute(lPut);
	List<String> readLines = IOUtils.readLines(lHTTPResponse.getEntity().getContent());
	StatusLine statusLine = lHTTPResponse.getStatusLine();
	int statusCode = statusLine.getStatusCode();

	JSONResponse lResponse = new JSONResponse();
	if (statusCode == 200) {
	    String lStrResponse = String.join(System.lineSeparator(), readLines);
	    if (lStrResponse.contains("<Msg>OK</Msg>")) {
		lResponse.setStatus(true);
	    } else {
		lResponse.setStatus(false);
		lResponse.setErrorMessage(lStrResponse);
	    }
	} else {
	    lResponse.setStatus(false);
	    lResponse.setErrorMessage("Error in GI Project Sync, HTTP Error Code " + statusCode);
	}
	return lResponse;
    }
}
