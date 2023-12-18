package com.example.onlineshop.pojo.RoomDataBases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.onlineshop.pojo.Models.OrderModel;
import com.example.onlineshop.pojo.converters.ProductModelConverter;

@Database(entities = OrderModel.class,version = 4,exportSchema = false)
@TypeConverters(ProductModelConverter.class)
public abstract class OrderDB extends RoomDatabase {
    private static OrderDB INSTANCE;
    public abstract OrderDao orderDao();
    public static OrderDB getInstance(Context context)
    {
        if(INSTANCE==null)
        {
            INSTANCE = Room.databaseBuilder(context,OrderDB.class,"order.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries().
                    build();
        }
        return INSTANCE;
    }
}
