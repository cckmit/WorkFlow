package com.tsi.workflow.gitblit;

import com.google.gson.GsonBuilder;
import com.tsi.workflow.gitblit.model.AccessPermission;
import com.tsi.workflow.gitblit.model.AccessPermissionTypeAdapter;
import com.tsi.workflow.rest.AbstractClient;
import com.tsi.workflow.rest.RestClient;
import com.tsi.workflow.utils.Constants;
import java.io.IOException;
import okhttp3.OkHttpClient;

public class GitBlitClient extends AbstractClient<GitBlitClient> {

    protected static final Object LOCK = new Object();

    protected static GitBlitClient mInstance;

    protected GitBlitAPI gitBlitAPI;

    public static GitBlitClient getInstance() {
	synchronized (LOCK) {
	    return mInstance;
	}
    }

    protected GitBlitClient(RestClient restClient, OkHttpClient okHttpClient) {
	super(restClient, okHttpClient);
    }

    public GitBlitAPI getGitBlitAPI() {
	return gitBlitAPI = (gitBlitAPI == null) ? getAPI(GitBlitAPI.class) : gitBlitAPI;
    }

    public static class Builder extends AbstractClient.Builder<GitBlitClient> {

	@Override
	public String getUSerAgent() {
	    return "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
	    // return "Alfresco-BPM-Client/" + Version.SDK;
	}

	@Override
	public GitBlitClient create(RestClient restClient, OkHttpClient okHttpClient) {
	    return new GitBlitClient(restClient, okHttpClient);
	}

	@Override
	public GsonBuilder getDefaultGsonBuilder() {
	    return Utils.getDefaultGsonBuilder();
	}

	public GitBlitClient build() throws IOException {
	    mInstance = super.build();
	    return mInstance;
	}
    }

    public static class Utils {

	public static GsonBuilder getDefaultGsonBuilder() {
	    return new GsonBuilder().registerTypeAdapter(AccessPermission.class, new AccessPermissionTypeAdapter()).setDateFormat(Constants.APP_DATE_TIME_FORMAT_STRING);
	}
    }
}
