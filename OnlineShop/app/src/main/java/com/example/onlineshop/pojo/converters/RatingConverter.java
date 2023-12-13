package com.example.onlineshop.pojo.converters;

import androidx.room.TypeConverter;

import com.example.onlineshop.pojo.Models.RatingModel;
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
