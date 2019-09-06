package com.apple.itunes;

import com.apple.itunes.controller.services.iTunesRestService;
import com.apple.itunes.model.AppleItunesApiDataResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.mock.BehaviorDelegate;

import static androidx.test.InstrumentationRegistry.getInstrumentation;

public class MockServiceTest implements iTunesRestService {

    private final BehaviorDelegate<iTunesRestService> delegate;

    public MockServiceTest(BehaviorDelegate<iTunesRestService> service) {
        this.delegate = service;
    }


    @Override
    public Call<AppleItunesApiDataResponse> getWeatherApiData(String url) {

        String fileName = "apple_itunes_api_200_response.json";
        String data = null;
        try {
            data = com.apple.itunes.RestServiceTestHelper.getStringFromFile(getInstrumentation().getContext(), fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppleItunesApiDataResponse itunesApiDataResponseModel = new Gson().fromJson(data, AppleItunesApiDataResponse.class);

        return delegate.returningResponse(itunesApiDataResponseModel).getWeatherApiData();

    }

    @Override
    public Call<AppleItunesApiDataResponse> getWeatherApiData() {
        return null;
    }


}
