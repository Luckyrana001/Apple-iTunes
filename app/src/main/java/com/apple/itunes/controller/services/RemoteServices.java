package com.apple.itunes.controller.services;


import com.apple.itunes.common.helper.RequestType;
import com.apple.itunes.common.helper.ServiceRuntimeException;
import com.apple.itunes.model.AppleItunesApiDataResponse;

import io.reactivex.Observable;


public class RemoteServices implements IRemoteServices {

    public RemoteServices() {
    }

    @Override
    public Observable<AppleItunesApiDataResponse> requestiTunesSearchApi(String url) {

        return Observable.create(emitter -> {
            RequestHandler.getRequestHandler()
                    .calliTunesSearchRequest(responseArgs -> {
                        if (responseArgs.requestType == RequestType.iTunesSearchApiResponse) {
                            AppleItunesApiDataResponse response = (AppleItunesApiDataResponse) responseArgs.args;


                            emitter.onNext(response);
                            emitter.onComplete();

                        } else if (responseArgs.requestType == RequestType.errorResponse) {
                            ServiceRuntimeException ex = new ServiceRuntimeException("0", "Please try again later");
                            emitter.onError(ex);
                        }
                    }, url);
        });
    }
}

