package com.tsi.workflow.rest;

import retrofit2.Retrofit;

public class RestClient {

    public final String username;

    public final String endpoint;

    public final Retrofit retrofit;

    public RestClient(String endpoint, Retrofit retrofit, String username) {
	this.endpoint = endpoint;
	this.retrofit = retrofit;
	this.username = username;
    }
}
