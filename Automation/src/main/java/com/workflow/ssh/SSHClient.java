package com.workflow.ssh;

import com.google.gson.GsonBuilder;
import com.tsi.workflow.rest.AbstractClient;
import com.tsi.workflow.rest.RestClient;
import com.tsi.workflow.utils.Constants;
import java.io.IOException;
import okhttp3.OkHttpClient;

public class SSHClient extends AbstractClient<SSHClient> {

    protected static final Object LOCK = new Object();

    protected static SSHClient mInstance;

    protected SSHAPI activitiBPMAPI;

    public static SSHClient getInstance() {
	synchronized (LOCK) {
	    return mInstance;
	}
    }

    protected SSHClient(RestClient restClient, OkHttpClient okHttpClient) {
	super(restClient, okHttpClient);
    }

    public SSHAPI getSSHAPI() {
	return activitiBPMAPI = (activitiBPMAPI == null) ? getAPI(SSHAPI.class) : activitiBPMAPI;
    }

    public static class Builder extends AbstractClient.Builder<SSHClient> {

	@Override
	public String getUSerAgent() {
	    return "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
	    // return "Alfresco-BPM-Client/" + Version.SDK;
	}

	@Override
	public SSHClient create(RestClient restClient, OkHttpClient okHttpClient) {
	    return new SSHClient(restClient, okHttpClient);
	}

	@Override
	public GsonBuilder getDefaultGsonBuilder() {
	    return Utils.getDefaultGsonBuilder();
	}

	public SSHClient build() throws IOException {
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
