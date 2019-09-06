package com.apple.itunes.controller.services;


import com.apple.itunes.model.SongListModel;
import com.apple.itunes.model.db.dao.RecordsDao;

import java.util.ArrayList;

import io.reactivex.Observable;


public interface ILocalServices {

    Observable<ArrayList<SongListModel>> getDataFromDatabase(RecordsDao recordsDao);


    Observable<Long[]> insertDataIntoDatabase(RecordsDao recordsDao, String result);


}
