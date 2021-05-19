package com.tsi.workflow.git;

import com.google.gson.GsonBuilder;
import com.tsi.workflow.rest.AbstractClient;
import com.tsi.workflow.rest.RestClient;
import com.tsi.workflow.utils.Constants;
import java.io.IOException;
import okhttp3.OkHttpClient;

public class JGitClient extends AbstractClient<JGitClient> {

    protected static final Object LOCK = new Object();

    protected static JGitClient mInstance;

    protected JGitAPI jGitAPI;

    public static JGitClient getInstance() {
	synchronized (LOCK) {
	    return mInstance;
	}
    }

    protected JGitClient(RestClient restClient, OkHttpClient okHttpClient) {
	super(restClient, okHttpClient);
    }

    public JGitAPI getJGitAPI() {
	return jGitAPI = (jGitAPI == null) ? getAPI(JGitAPI.class) : jGitAPI;
    }

    public static class Builder extends AbstractClient.Builder<JGitClient> {

	@Override
	public String getUSerAgent() {
	    return "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
	    // return "Alfresco-BPM-Client/" + Version.SDK;
	}

	@Override
	public JGitClient create(RestClient restClient, OkHttpClient okHttpClient) {
	    return new JGitClient(restClient, okHttpClient);
	}

	@Override
	public GsonBuilder getDefaultGsonBuilder() {
	    return Utils.getDefaultGsonBuilder();
	}

	public JGitClient build() throws IOException {
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
