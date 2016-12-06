package com.beelancrp.genesis.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by beeLAN on 03.12.2016.
 */

public class UniversalService {
    static String SERVICE_ENDPOINT = "https://www.googleapis.com/";

    public static <T> T createRetrofitService(final Class<T> clazz) {
        Retrofit.Builder service = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SERVICE_ENDPOINT);
        return service.build().create(clazz);
    }
}
