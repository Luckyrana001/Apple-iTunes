package com.apple.itunes.controller.services;


import com.apple.itunes.model.AppleItunesApiDataResponse;

import io.reactivex.Observable;


public interface IRemoteServices {


    Observable<AppleItunesApiDataResponse> requestiTunesSearchApi(String url);

}


