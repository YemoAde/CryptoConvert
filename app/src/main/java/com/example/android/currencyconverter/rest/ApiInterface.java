package com.example.android.currencyconverter.rest;

import com.example.android.currencyconverter.Model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by adeyemogabriel on 8/28/17.
 */

public interface ApiInterface  {

    /**
     * End Point that gets all exchange rates
     */
    @GET("data/price?tsyms=USD,EUR,NGN,GBP,ZAR,AED,CNY,RUB,JPY,CHF,CFP,GHS,AUD,BRL,ARS,CAD,BND,DKK,SAR,SGD")
    Call<ApiResponse> getConversionData(@Query("fsym") String base_currency);
}



