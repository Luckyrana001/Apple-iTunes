package com.apple.itunes.common;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.ViewModelProviders;

import com.apple.itunes.controller.SongsListController;
import com.apple.itunes.controller.SongsViewModelProvider;
import com.apple.itunes.controller.services.IRemoteServices;
import com.apple.itunes.controller.services.LocalServices;
import com.apple.itunes.controller.services.RemoteServices;

public class MyApplication  extends Application {

    public Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }
}
