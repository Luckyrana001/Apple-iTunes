package com.apple.itunes;


import androidx.test.filters.SmallTest;

import com.apple.itunes.model.AppleItunesApiDataResponse;
import com.apple.itunes.controller.services.iTunesRestService;

import junit.framework.Assert;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;


@SuppressWarnings("ALL")
public class MockAdapterTest {
    private MockRetrofit mockRetrofit;
    private Retrofit retrofit;


    public MockAdapterTest() {
        try {
            setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUp() throws Exception {
        retrofit = new Retrofit.Builder().baseUrl("http://test.com")
                .client(new OkHttpClient())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        NetworkBehavior behavior = NetworkBehavior.create();

        mockRetrofit = new MockRetrofit.Builder(retrofit)
                .networkBehavior(behavior)
                .build();
    }


    @SmallTest
    public void testRandomDataRetrieval() throws Exception {
        BehaviorDelegate<iTunesRestService> delegate = mockRetrofit.create(iTunesRestService.class);
        iTunesRestService mockDataService = new com.apple.itunes.MockServiceTest(delegate);


        //Actual Test
        Call<AppleItunesApiDataResponse> itunesRequest = mockDataService.getWeatherApiData("jack+johnson");
        Response<AppleItunesApiDataResponse> itunesMockRequest = itunesRequest.execute();


        //Asserting response
        Assert.assertTrue(itunesMockRequest.isSuccessful());

    }


}