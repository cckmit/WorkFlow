/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.httpclients;

import com.google.gson.Gson;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.audit.models.RequestQueueModel;
import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Radha.Adhimoolam
 */
@Component
public class ApiHttpClients {

    private static final Logger LOG = Logger.getLogger(ApiHttpClients.class.getName());

    @Autowired
    WFConfig wfConfig;

    private String auditUrl = "";

    private String getAuditUrl() {
	// return wfConfig.getAuditServerName() + ":" + wfConfig.getAuditServerPort() +
	// "/WorkFlowAPI/audit/api/saveTransaction";
	return "";
    }

    public String sendPOST(RequestQueueModel requestQueue) throws IOException {

	String result = "";
	HttpPost post = new HttpPost(getAuditUrl());

	Gson lGson = new Gson();
	post.setEntity(new StringEntity(lGson.toJson(requestQueue)));

	try (CloseableHttpClient httpClient = HttpClients.createDefault(); CloseableHttpResponse response = httpClient.execute(post)) {

	    result = EntityUtils.toString(response.getEntity());
	    LOG.info("Response -> " + result);
	}

	return result;
    }

}
