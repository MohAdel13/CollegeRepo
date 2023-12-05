package com.example.onlineshop.pojo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = UserModel.class,version = 5,exportSchema = false)
public abstract class UserDB extends RoomDatabase {
    private static UserDB INSTANCE;
    public abstract UserDao userDao();
    public static UserDB getInstance(Context context)
    {
        if(INSTANCE==null)
        {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),UserDB.class,"user.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
