package com.example.onlineshop.pojo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = CategoryModel.class,version = 3,exportSchema = false)
public abstract class CategoryDB extends RoomDatabase {
    private static CategoryDB INSTANCE;
    public abstract CategoryDao categoryDao();
    public static CategoryDB getInstance(Context context)
    {
        if(INSTANCE==null)
        {
            INSTANCE = Room.databaseBuilder(context,CategoryDB.class,"category.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries().
                    build();
        }
        return INSTANCE;
    }
}
