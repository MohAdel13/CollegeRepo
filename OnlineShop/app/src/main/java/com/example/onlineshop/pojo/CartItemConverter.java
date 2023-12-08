package com.example.onlineshop.pojo;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CartItemConverter {
    @TypeConverter
    public static List<CartProductModel> fromString(String value) {
        Type listType = new TypeToken<List<CartProductModel>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toString(List<CartProductModel> list) {
        return new Gson().toJson(list);
    }
}
