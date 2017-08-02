package com.jelvix.kotlincodesample.inject.module;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.jelvix.kotlincodesample.BuildConfig;
import com.jelvix.kotlincodesample.api.api.IApi;
import com.jelvix.kotlincodesample.api.api.RxJava2CallAdapterFactory;
import com.jelvix.kotlincodesample.manager.IApiManager;
import com.jelvix.kotlincodesample.manager.IPreferenceManager;
import com.jelvix.kotlincodesample.manager.impl.ApiManager;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@Module
public class ApiModule {

    private static final String QUERY_TOKEN = "access_token";
    private static final int CONNECTION_TIMEOUT = 30;
    private static final int MILLISECONDS_IN_SECOND = 1000;

    @Nullable
    @Provides
    public String provideToken(IPreferenceManager preferenceManager) {
        return preferenceManager.getToken();
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (src, typeOfSrc, context) -> new JsonPrimitive(src.getTime() / MILLISECONDS_IN_SECOND))
                .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> {
                    String time = json.getAsString();
                    try {
                        Long timestamp = Long.parseLong(time);
                        return new Date(timestamp * MILLISECONDS_IN_SECOND);
                    } catch (NumberFormatException exception) {
                        throw new RuntimeException("Date parsing error: ", exception);
                    }
                });
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(final Provider<String> tokenProvider) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        clientBuilder.addNetworkInterceptor(interceptor);
        clientBuilder.addNetworkInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder();

            String token = tokenProvider.get();
            HttpUrl url = original.url().newBuilder()
                    .addQueryParameter(QUERY_TOKEN, token)
                    .build();
            requestBuilder.url(url);
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });
        clientBuilder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        clientBuilder.readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);

        return clientBuilder.build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BuildConfig.BASE_URL)
                .build();
    }

    @Provides
    @Singleton
    public IApi provideApi(Retrofit retrofit) {
        return retrofit.create(IApi.class);
    }

    @Provides
    public IApiManager provideApiManager(IApi api) {
        return new ApiManager(api);
    }

}
