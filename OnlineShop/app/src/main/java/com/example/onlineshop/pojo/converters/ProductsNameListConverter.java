package com.example.onlineshop.pojo.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ProductsNameListConverter {
    @TypeConverter
    public static List<String> fromJson(String value) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toJson(List<String> list) {
        return new Gson().toJson(list);
    }
}
