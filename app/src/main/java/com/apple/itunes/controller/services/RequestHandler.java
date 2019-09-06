package com.apple.itunes.controller.services;

import android.util.Log;

import com.apple.itunes.common.helper.Constants;
import com.apple.itunes.common.helper.IResponseReceivedNotifyInterface;
import com.apple.itunes.common.helper.RequestType;
import com.apple.itunes.common.helper.ResponseArgs;
import com.apple.itunes.common.helper.ResponseStatus;
import com.apple.itunes.model.AppleItunesApiDataResponse;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestHandler {
    private static final String TAG = RequestHandler.class.getName();

    public static RequestHandler requestHandler;
    private Retrofit retrofit;
    private iTunesRestService service;

    public RequestHandler() {


        try {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    // .addConverterFactory(JacksonConverterFactory.create())
                    .client(new OkHttpClient())
                    .build();

            service = retrofit.create(iTunesRestService.class);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static RequestHandler getRequestHandler() {

        if (requestHandler == null) {
            requestHandler = new RequestHandler();
        }
        return requestHandler;
    }


    public void calliTunesSearchRequest(final IResponseReceivedNotifyInterface iResponseReceivedNotifyInterface, String searchKey) {


        Call<AppleItunesApiDataResponse> call = service.getWeatherApiData(searchKey);
        call.enqueue(new Callback<AppleItunesApiDataResponse>() {
            @Override
            public void onResponse(Call<AppleItunesApiDataResponse> call, Response<AppleItunesApiDataResponse> response) {
                if (response.isSuccessful()) {
                    Type type = new TypeToken<AppleItunesApiDataResponse>() {
                    }.getType();
                    iResponseReceivedNotifyInterface.responseReceived(new ResponseArgs(response.body(), ResponseStatus.success, RequestType.iTunesSearchApiResponse));


                } else {
                    try {
                        Converter<ResponseBody, AppleItunesApiDataResponse> errorConverter = retrofit.responseBodyConverter(AppleItunesApiDataResponse.class, new Annotation[0]);
                        AppleItunesApiDataResponse error = errorConverter.convert(response.errorBody());
                        Type type = new TypeToken<AppleItunesApiDataResponse>() {
                        }.getType();
                        iResponseReceivedNotifyInterface.responseReceived(new ResponseArgs(error, ResponseStatus.success, RequestType.errorResponse));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<AppleItunesApiDataResponse> call, Throwable t) {
                iResponseReceivedNotifyInterface.responseReceived(new ResponseArgs(null, ResponseStatus.badRequest, RequestType.errorResponse));
                Log.e(TAG, t.toString());
            }
        });
    }
}
