/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.snow.pr;

import com.google.gson.GsonBuilder;
import com.tsi.workflow.rest.AbstractClient;
import com.tsi.workflow.rest.RestClient;
import com.tsi.workflow.utils.Constants;
import java.io.IOException;
import okhttp3.OkHttpClient;

/**
 *
 * @author Radha.Adhimoolam
 */
public class SnowPRClient extends AbstractClient<SnowPRClient> {

    protected static final Object LOCK = new Object();

    protected static SnowPRClient mInstance;

    protected SnowPRAPI snowPRAPI;

    public static SnowPRClient getInstance() {
	synchronized (LOCK) {
	    return mInstance;
	}
    }

    protected SnowPRClient(RestClient restClient, OkHttpClient okHttpClient) {
	super(restClient, okHttpClient);
    }

    public SnowPRAPI getSnowPRAPI() {
	return snowPRAPI = (snowPRAPI == null) ? getAPI(SnowPRAPI.class) : snowPRAPI;
    }

    public static class Builder extends AbstractClient.Builder<SnowPRClient> {

	@Override
	public String getUSerAgent() {
	    return "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
	    // return "Alfresco-BPM-Client/" + Version.SDK;
	}

	@Override
	public SnowPRClient create(RestClient restClient, OkHttpClient okHttpClient) {
	    return new SnowPRClient(restClient, okHttpClient);
	}

	@Override
	public GsonBuilder getDefaultGsonBuilder() {
	    return Utils.getDefaultGsonBuilder();
	}

	public SnowPRClient build() throws IOException {
	    mInstance = super.build();
	    return mInstance;
	}
    }

    public static class Utils {

	public static GsonBuilder getDefaultGsonBuilder() {
	    return new GsonBuilder().setDateFormat(Constants.APP_DATE_TIME_FORMAT_STRING);
	}
    }

}
