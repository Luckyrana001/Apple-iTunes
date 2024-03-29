package com.apple.itunes.controller;

import android.content.Context;
import android.net.ConnectivityManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apple.itunes.common.helper.LCEStatus;
import com.apple.itunes.common.helper.ServiceRuntimeException;
import com.apple.itunes.common.helper.Utils;
import com.apple.itunes.controller.services.ILocalServices;
import com.apple.itunes.controller.services.IRemoteServices;
import com.apple.itunes.model.SongListModel;
import com.apple.itunes.model.db.AppDatabase;
import com.apple.itunes.model.db.dao.RecordsDao;
import com.google.gson.Gson;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.apple.itunes.common.helper.Constants.DATA_INSERT_FAILED;
import static com.apple.itunes.common.helper.Constants.DATA_LOAD_FAILED_TITLE;
import static com.apple.itunes.common.helper.Constants.LOADING_DATA;
import static com.apple.itunes.common.helper.Constants.RETRY_AGAIN;
import static com.apple.itunes.common.helper.Constants.TABLE_DATA_UPDATED;
import static com.apple.itunes.common.helper.Constants.TABLE_INSERT_ERROR;


public class SongsListController extends ViewModel {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private IRemoteServices mRemoteServices;
    private ILocalServices mLocalServices;
    private MutableLiveData<LCEStatus> mlLceStatus = new MutableLiveData<>();
    private MutableLiveData<String> mlWarningStatus = new MutableLiveData<>();
    private MutableLiveData<ArrayList<SongListModel>> mlSongsListData = new MutableLiveData<>();
    private RecordsDao recordsDao;
    private Context context;
    private String searchQuery = "";

    public SongsListController(IRemoteServices rservice, ILocalServices lservice, Context ctx) {

        mRemoteServices = rservice;
        mLocalServices = lservice;
        context = ctx;

        recordsDao = AppDatabase.getInstance(context).userDao();

    }

    public void getDataFromApi(String searchText) {
        searchQuery = searchText;
        Utils utils = new Utils(context);
        if (utils.isNetworkAvailable()) {
            mRemoteServices
                    .requestiTunesSearchApi(searchQuery)
                    .doOnSubscribe(disposable -> mlLceStatus.setValue(LCEStatus.loading(LOADING_DATA)))
                    .doOnTerminate(() -> mlLceStatus.setValue(LCEStatus.success()))
                    .subscribe(result -> {
                        mlSongsListData.setValue(result.getResults());
                        insertDataIntoDB(new Gson().toJson(result));

                    }, throwable -> {

                        if (throwable instanceof ServiceRuntimeException) {
                            getSavedDataFromDbIfHaveAny();
                        } else {
                            mlLceStatus.postValue(LCEStatus.error(DATA_LOAD_FAILED_TITLE, RETRY_AGAIN));
                        }
                    });

        } else {
            getSavedDataFromDbIfHaveAny();
        }
    }

    public void insertDataIntoDB(final String result) {
        Disposable dbDataDisposable = mLocalServices.insertDataIntoDatabase(recordsDao, result)
                .subscribeOn(Schedulers.single())
                .subscribe(getWeatherData -> {
                    System.out.print(TABLE_DATA_UPDATED);
                }, throwable -> {
                    mlLceStatus.postValue(LCEStatus.error(DATA_INSERT_FAILED, TABLE_INSERT_ERROR));
                });


        compositeDisposable.add(dbDataDisposable);

    }

    public ArrayList<SongListModel> getSongList(){
        return mlSongsListData.getValue();
    }

    public void getSavedDataFromDbIfHaveAny() {
        Disposable dbDataDisposable = mLocalServices.getDataFromDatabase(recordsDao)
                .subscribeOn(Schedulers.single())
                .subscribe(getSavedSearchResult -> {
                    if (getSavedSearchResult != null || !getSavedSearchResult.isEmpty()) {
                        LCEStatus.success();
                        mlSongsListData.postValue(getSavedSearchResult);
                    }

                });


        compositeDisposable.add(dbDataDisposable);


    }


    public LiveData<LCEStatus> getLceStatus() {
        return mlLceStatus;
    }


    public LiveData<String> getWarning() {
        return mlWarningStatus;
    }


    public LiveData<ArrayList<SongListModel>> getiTunesSearchApiData() {
        return mlSongsListData;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
