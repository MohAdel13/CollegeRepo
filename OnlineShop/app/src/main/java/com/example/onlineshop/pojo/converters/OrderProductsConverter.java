package com.example.onlineshop.pojo.converters;

import androidx.room.TypeConverter;

import com.example.onlineshop.pojo.Models.ProductModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class OrderProductsConverter {
    @TypeConverter
    public static List<ProductModel> fromString(String value) {
        Type listType = new TypeToken<List<ProductModel>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toString(List<ProductModel> list) {
        return new Gson().toJson(list);
    }
}
