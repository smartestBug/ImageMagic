package com.msemyak.imagemagic.mvp.model.remote;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://api.doitserver.in.ua/";
    private static DoitServerAPI doAPI = null;
    private static Retrofit retrofit = null;

    private static Retrofit getRetrofit()
    {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static DoitServerAPI getDoitServerAPI() {

        if (doAPI == null) {
            doAPI = getRetrofit().create(DoitServerAPI.class);
        }

        return doAPI;
    }



}
