package com.example.onlineshop.pojo.RoomDataBases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.onlineshop.pojo.Models.UserModel;
import com.example.onlineshop.pojo.converters.ProductModelConverter;
import com.example.onlineshop.pojo.converters.RatingConverter;

@Database(entities = UserModel.class,version = 15,exportSchema = false)
@TypeConverters({ProductModelConverter.class, RatingConverter.class})
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
