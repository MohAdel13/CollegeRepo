package com.example.onlineshop.pojo.RoomDataBases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.onlineshop.pojo.Models.ProductModel;
import com.example.onlineshop.pojo.converters.RatingConverter;

@Database(entities = ProductModel.class,version = 12,exportSchema = false)
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
