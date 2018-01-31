package com.kikappsmx.bitsocredentials.data.entities;

import com.google.gson.annotations.SerializedName;

public class BalancesResponse {

    public Boolean success;
    @SerializedName("payload")
    public Balances balances;
}