package com.apple.itunes.model.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RecordsDataDao {
    @PrimaryKey
    private int uid;
    @ColumnInfo(name = "records")
    private String records;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getRecords() {
        return records;
    }

    public void setRecords(String records) {
        this.records = records;
    }


    @Override
    public String toString() {
        return "\nUser{" +
                "uid=" + uid +
                ", records='" + records + '\'' +

                '}' + "\n\n";
    }
}