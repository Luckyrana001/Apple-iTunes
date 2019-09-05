package com.apple.itunes.controller.services;


import com.apple.itunes.model.AppleItunesApiDataResponse;
import com.apple.itunes.model.SongListModel;
import com.google.gson.Gson;
import com.apple.itunes.model.db.dao.RecordsDao;
import com.apple.itunes.model.db.entity.RecordsDataDao;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


public class LocalServices implements ILocalServices {

    public LocalServices() {
    }

    public Observable<ArrayList<SongListModel>> getDataFromDatabase(RecordsDao mRecordsDao) {
        return Observable.defer(() -> Observable.just(queryDatafromDatabase(mRecordsDao)));
    }

    @Override
    public Observable<Long[]> insertDataIntoDatabase(RecordsDao recordsDao, String result) {
        return Observable.defer(() -> Observable.just(insertDataIntoDb(recordsDao, result)));
    }


    public Long[] insertDataIntoDb(RecordsDao recordsDao, String result) {
        recordsDao.deleteAll();

        RecordsDataDao user = new RecordsDataDao();
        user.setRecords(result);
        Long[] value = recordsDao.insertAll(user);
        return value;
    }

    public ArrayList<SongListModel> queryDatafromDatabase(RecordsDao mRecordsDao) {
        List<RecordsDataDao> data = mRecordsDao.getAll();
        if (data.isEmpty()) {
            ArrayList<SongListModel> weatherDatalist = new ArrayList<>();
            return weatherDatalist;
        } else {
            RecordsDataDao value = data.get(0);
            String records = value.getRecords();
            AppleItunesApiDataResponse recordData = new Gson().fromJson(records, AppleItunesApiDataResponse.class);
            ArrayList<SongListModel> weatherDatalist = recordData.getResults();
            return weatherDatalist;
        }

    }


}
