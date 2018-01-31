package com.kikappsmx.bitsocredentials.data.network;

import com.kikappsmx.bitsocredentials.data.entities.BalancesResponse;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BitsoApiManager {

    private static final String BASE_URL = "https://api.bitso.com/v3/";
    private static final int TIMEOUT = 30;

    private BitsoApiProvider bitsoApiProvider;

    public BitsoApiManager() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bitsoApiProvider = retrofit.create(BitsoApiProvider.class);
    }

    public Observable<BalancesResponse> requestBalances(String authorization) {
        return bitsoApiProvider.requestBalances(authorization);
    }
}