package com.example.onlineshop.pojo.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class OrderProductsCountConverter {

    @TypeConverter
    public static List<Integer> fromJson(String value) {
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toJson(List<Integer> list) {
        return new Gson().toJson(list);
    }
}
