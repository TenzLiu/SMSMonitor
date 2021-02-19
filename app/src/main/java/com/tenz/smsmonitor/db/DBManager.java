package com.tenz.smsmonitor.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.tenz.smsmonitor.app.App;
import com.tenz.smsmonitor.entity.SMSEntity;


@Database(entities = {SMSEntity.class}, version = 1, exportSchema = false)
public abstract class DBManager extends RoomDatabase {

    public abstract SMSDao playSongDao();

    private static DBManager dbManager;
    public static DBManager newInstance() {
        if (dbManager == null) {
            synchronized (DBManager.class) {
                if (dbManager == null) {
                    dbManager = Room.databaseBuilder(App.getContext(),DBManager.class,"tenz_smsmonitor")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return dbManager;
    }

}
