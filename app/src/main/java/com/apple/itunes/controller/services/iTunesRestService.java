package com.apple.itunes.controller.services;


import com.apple.itunes.model.AppleItunesApiDataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface iTunesRestService {

    @GET("search")
    Call<AppleItunesApiDataResponse> getWeatherApiData(@Query("term") String searchKey);


    Call<AppleItunesApiDataResponse> getWeatherApiData();

}
