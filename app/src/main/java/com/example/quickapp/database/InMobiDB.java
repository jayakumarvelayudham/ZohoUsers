package com.example.quickapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.quickapp.dao.RandomUserDao;
import com.example.quickapp.entity.RandomUser;

@Database(entities = RandomUser.class,version = 1)
public abstract class InMobiDB extends RoomDatabase {
    public abstract RandomUserDao randomUserDao();

    public static volatile InMobiDB inMobiRoomInstance;

    public static InMobiDB getDatabase(final Context context){
        if(inMobiRoomInstance == null){
            synchronized (InMobiDB.class){
                if(inMobiRoomInstance == null){
                    inMobiRoomInstance = Room.databaseBuilder(context.getApplicationContext(),InMobiDB.class,"InMobiDB").build();
                }
            }
        }
        return inMobiRoomInstance;
    }
}
