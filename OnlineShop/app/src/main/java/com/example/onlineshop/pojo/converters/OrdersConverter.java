package com.example.onlineshop.pojo.converters;

import androidx.room.TypeConverter;

import com.example.onlineshop.pojo.Models.OrderModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class OrdersConverter {
    @TypeConverter
    public static List<OrderModel> fromJson(String value) {
        Type listType = new TypeToken<List<OrderModel>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toJson(List<OrderModel> list) {
        return new Gson().toJson(list);
    }
}
