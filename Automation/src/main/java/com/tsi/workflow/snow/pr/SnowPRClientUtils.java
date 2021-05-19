/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.snow.pr;

import com.google.gson.Gson;
import com.tsi.workflow.interfaces.ISnowPRConfig;
import com.tsi.workflow.snow.pr.model.PRDetails;
import com.tsi.workflow.snow.pr.model.PRResponse;
import com.tsi.workflow.snow.pr.model.PRUpdate;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import retrofit2.Call;
import retrofit2.Response;

/**
 *
 * @author Radha.Adhimoolam
 */
public class SnowPRClientUtils {

    private static final Logger LOG = Logger.getLogger(SnowPRClientUtils.class.getName());

    ISnowPRConfig snowPRConfig;

    public SnowPRClientUtils(ISnowPRConfig snowPRConfig) {
	this.snowPRConfig = snowPRConfig;
    }

    public PRResponse getPRNumberStatus(String prNumber) throws Exception {
	SnowPRClient lClient = new SnowPRClient.Builder().connectWithProxy(snowPRConfig.getRestSNOWPRUrl(), snowPRConfig.getRestSNOWPRUserId(), snowPRConfig.getRestSNOWPRPasscode(), snowPRConfig.getWFProxyHostName(), snowPRConfig.getWFProxyHostPort()).build();
	Call<PRResponse> validatePRNumber = lClient.getSnowPRAPI().getPRNumberInfo("number=" + prNumber, "true", "number,state");
	Response<PRResponse> lResponse = validatePRNumber.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body();
	}
	return null;
    }

    public PRUpdate updatePRNumberAndStatus(String prNumber, String planStatus, Date loadDateTime) throws IOException {

	SnowPRClient lClient = new SnowPRClient.Builder().connectWithProxy(snowPRConfig.getRestSNOWPRUrl(), snowPRConfig.getRestSNOWPRUserId(), snowPRConfig.getRestSNOWPRPasscode(), snowPRConfig.getWFProxyHostName(), snowPRConfig.getWFProxyHostPort()).build();
	PRDetails prUpdate = new PRDetails();
	prUpdate.setU_load_status(planStatus);
	prUpdate.setU_problem_number(prNumber);

	if (loadDateTime != null) {
	    SimpleDateFormat lDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    prUpdate.setU_effective_date(lDateFormat.format(loadDateTime));
	}

	LOG.info(new Gson().toJson(prUpdate));
	Call<PRUpdate> validatePRNumber = lClient.getSnowPRAPI().updatePRNumber(prUpdate);
	Response<PRUpdate> lResponse = validatePRNumber.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body();
	}
	return null;

    }

    public PRUpdate updatePRNumberAndStatus(String prNumber, String planStatus) throws IOException {
	return updatePRNumberAndStatus(prNumber, planStatus, null);
    }

}
