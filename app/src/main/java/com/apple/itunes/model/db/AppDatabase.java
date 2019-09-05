package com.apple.itunes.model.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.apple.itunes.model.db.dao.RecordsDao;
import com.apple.itunes.model.db.entity.RecordsDataDao;


@Database(entities = {RecordsDataDao.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase appDatabase = null;

    /**
     * from developers android, made my own singleton
     *
     * @param context
     * @return
     */
    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "database-name").build();
        }
        return appDatabase;
    }

    public abstract RecordsDao userDao();
}