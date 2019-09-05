package com.apple.itunes.controller;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.apple.itunes.controller.services.ILocalServices;
import com.apple.itunes.controller.services.IRemoteServices;
import com.apple.itunes.controller.services.LocalServices;

import static com.apple.itunes.controller.services.helper.Constants.VIEWMODEL_EXPECTED;


public class SongsViewModelProvider implements ViewModelProvider.Factory {


    private IRemoteServices mRs;
    private ILocalServices mls;
    private Context context;

    public SongsViewModelProvider(IRemoteServices mRs, LocalServices mls, Context ctx) {
        this.mRs = mRs;
        this.mls = mls;
        this.context = ctx;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SongsListController.class)) {
            return (T) new SongsListController(mRs, mls, context);
        }
        throw new IllegalArgumentException(VIEWMODEL_EXPECTED);
    }
}
