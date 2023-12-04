package com.example.onlineshop.pojo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = ProductModel.class,version = 1,exportSchema = false)
@TypeConverters(RatingConverter.class)
public abstract class ProductDB extends RoomDatabase {
    private static ProductDB INSTANCE;
    public abstract ProductDao productDao();
    public static ProductDB getInstance(Context context)
    {
        if(INSTANCE==null)
        {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),ProductDB.class,"product.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
