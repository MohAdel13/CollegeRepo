package com.example.onlineshop.pojo;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

public class RatingConverter {
    @TypeConverter
    public RatingModel GsonToSource(String s)
    {
        return new Gson().fromJson(s,RatingModel.class);
    }

    @TypeConverter
    public String FromSource(RatingModel r)
    {
        return new Gson().toJson(r);
    }
}
