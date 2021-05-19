package com.tsi.workflow.rest;

import com.google.gson.GsonBuilder;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jpascal on 22/01/2016.
 */
public abstract class AbstractClient<T> {

    protected RestClient restClient;

    protected OkHttpClient okHttpClient;

    private static final Logger LOG = Logger.getLogger(AbstractClient.class.getName());

    protected AbstractClient(RestClient restClient, OkHttpClient okHttpClient) {
	this.restClient = restClient;
	this.okHttpClient = okHttpClient;
    }

    public <T> T getAPI(final Class<T> service) {
	return restClient.retrofit.create(service);
    }

    protected static String getBasicAuth(String username, String secret) {
	// Prepare Basic AUTH
	if (username != null && secret != null) {
	    String credentials = username + ":" + secret;
	    return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
	}
	throw new IllegalArgumentException("Invalid Credentials");
    }

    public static abstract class Builder<T> {

	protected String endpoint, username, secret, auth;

	protected OkHttpClient okHttpClient;

	protected Retrofit retrofit;

	protected GsonBuilder gsonBuilder;

	protected int retryCount = 2;

	protected HttpLoggingInterceptor.Level logginLevel = HttpLoggingInterceptor.Level.BASIC;

	protected String proxyHostName;

	protected Integer proxyHostPort;

	public Builder<T> connect(String endpoint, String username, String secret) {
	    this.endpoint = endpoint;
	    this.username = username;
	    this.secret = secret;
	    return this;
	}

	public Builder<T> connectWithProxy(String endpoint, String username, String secret, String proxyHostName, Integer proxyHostPort) {
	    this.endpoint = endpoint;
	    this.username = username;
	    this.secret = secret;
	    this.proxyHostName = proxyHostName;
	    this.proxyHostPort = proxyHostPort;
	    return this;
	}

	public Builder<T> connect(String endpoint, String auth) {
	    this.endpoint = endpoint;
	    this.auth = auth;
	    return this;
	}

	public T build() throws IOException {
	    // Check Parameters
	    if (endpoint == null || endpoint.isEmpty()) {
		throw new IllegalArgumentException("Invalid url");
	    }

	    // Prepare OKHTTP Layer
	    if (okHttpClient == null) {
		TrustAllTrustManager trustAllTrustManager = new TrustAllTrustManager();
		SSLUtil sslUtil = new SSLUtil(trustAllTrustManager);
		SSLSocketFactory createSSLSocketFactory;
		try {
		    createSSLSocketFactory = sslUtil.createSSLSocketFactory();
		} catch (GeneralSecurityException ex) {
		    throw new IOException("Cannot create ssl socket");
		}
		OkHttpClient.Builder builder;
		if (proxyHostName == null || proxyHostName.isEmpty()) {
		    builder = new OkHttpClient.Builder().sslSocketFactory(createSSLSocketFactory, trustAllTrustManager);
		} else {
		    builder = new OkHttpClient.Builder().proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHostName, proxyHostPort))).sslSocketFactory(createSSLSocketFactory, trustAllTrustManager);
		}

		ArrayList<Protocol> protocols = new ArrayList<>();
		protocols.add(Protocol.HTTP_1_1);
		builder.protocols(protocols);
		builder.connectTimeout(8, TimeUnit.MINUTES);
		builder.writeTimeout(8, TimeUnit.MINUTES);
		builder.readTimeout(8, TimeUnit.MINUTES);

		HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {

		    @Override
		    public void log(String message) {
			LOG.info(message);
		    }
		});

		logging.setLevel(logginLevel);

		builder.addInterceptor(logging);

		if (username != null && secret != null) {
		    String credentials = username + ":" + secret;
		    auth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
		}

		builder.addInterceptor(new Interceptor() {
		    @Override
		    public Response intercept(Chain chain) throws IOException {

			Request.Builder lBuilder = chain.request().newBuilder();
			if (auth != null) {
			    lBuilder.addHeader("Authorization", auth);
			}
			lBuilder.removeHeader("User-Agent").addHeader("User-Agent", getUSerAgent());

			Request newRequest = lBuilder.build();

			Response response = chain.proceed(newRequest);
			int tryCount = 0;

			while (tryCount <= retryCount && !response.isSuccessful() && response.code() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
			    tryCount++;
			    LOG.warn("Got Internal Server Error (500), Retrying... (" + tryCount + "/" + retryCount + ")");
			    response = chain.proceed(newRequest);
			}

			return response;
		    }
		});

		okHttpClient = builder.build();
	    }

	    // Prepare Retrofit
	    if (retrofit == null) {
		Retrofit.Builder builder = new Retrofit.Builder().baseUrl(endpoint).client(okHttpClient).addCallAdapterFactory(RxJavaCallAdapterFactory.create());

		if (gsonBuilder == null) {
		    gsonBuilder = getDefaultGsonBuilder();
		}
		builder.addConverterFactory(GsonConverterFactory.create(gsonBuilder.create())).build();

		retrofit = builder.build();
	    }

	    return create(new RestClient(endpoint, retrofit, username), okHttpClient);
	}

	public abstract GsonBuilder getDefaultGsonBuilder();

	public abstract String getUSerAgent();

	public abstract T create(RestClient restClient, OkHttpClient okHttpClient);

    }
}
