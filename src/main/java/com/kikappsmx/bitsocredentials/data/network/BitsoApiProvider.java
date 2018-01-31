package com.kikappsmx.bitsocredentials.data.network;

import com.kikappsmx.bitsocredentials.data.entities.BalancesResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface BitsoApiProvider {

    @GET("balance")
    Observable<BalancesResponse> requestBalances(@Header("Authorization") String authorization);
}