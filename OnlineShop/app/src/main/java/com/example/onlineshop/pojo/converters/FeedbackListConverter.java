package com.example.onlineshop.pojo.converters;

import androidx.room.TypeConverter;

import com.example.onlineshop.pojo.Models.FeedbackModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class FeedbackListConverter {
    @TypeConverter
    public static List<FeedbackModel> fromJson(String value) {
        Type listType = new TypeToken<List<FeedbackModel>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toJson(List<FeedbackModel> list) {
        return new Gson().toJson(list);
    }
}
