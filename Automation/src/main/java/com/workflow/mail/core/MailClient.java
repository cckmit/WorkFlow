package com.workflow.mail.core;

import com.google.gson.GsonBuilder;
import com.tsi.workflow.rest.AbstractClient;
import com.tsi.workflow.rest.RestClient;
import java.io.IOException;
import okhttp3.OkHttpClient;

public class MailClient extends AbstractClient<MailClient> {

    protected static final Object LOCK = new Object();

    protected static MailClient mInstance;

    protected MailAPI mailAPI;

    public static MailClient getInstance() {
	synchronized (LOCK) {
	    return mInstance;
	}
    }

    protected MailClient(RestClient restClient, OkHttpClient okHttpClient) {
	super(restClient, okHttpClient);
    }

    public MailAPI getMailAPI() {
	return mailAPI = (mailAPI == null) ? getAPI(MailAPI.class) : mailAPI;
    }

    public static class Builder extends AbstractClient.Builder<MailClient> {

	@Override
	public String getUSerAgent() {
	    return "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
	    // return "Alfresco-BPM-Client/" + Version.SDK;
	}

	@Override
	public MailClient create(RestClient restClient, OkHttpClient okHttpClient) {
	    return new MailClient(restClient, okHttpClient);
	}

	@Override
	public GsonBuilder getDefaultGsonBuilder() {
	    return Utils.getDefaultGsonBuilder();
	}

	public MailClient build() throws IOException {
	    mInstance = super.build();
	    return mInstance;
	}
    }

    public static class Utils {

	public static GsonBuilder getDefaultGsonBuilder() {
	    return new GsonBuilder();
	}
    }
}
