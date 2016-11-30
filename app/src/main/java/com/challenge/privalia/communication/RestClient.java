package com.challenge.privalia.communication;

import com.challenge.privalia.configuration.MovieConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.androidannotations.annotations.EBean;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by raulpascual on 30/11/16.
 */
@EBean(scope = EBean.Scope.Singleton)
public class RestClient {

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(MovieConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(getClient())
            .build();

    public OkHttpClient getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        return httpClient.build();
    }

    ApiService service = retrofit.create(ApiService.class);

    public ApiService getService() {
        return service;
    }
}
