package com.example.onlineshop.pojo;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class ProductModelConverter {
    @TypeConverter
    public static ProductModel fromJson(String value) {
        Type type = new TypeToken<ProductModel>() {}.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    @TypeConverters(RatingConverter.class)
    public static String toJson(ProductModel productModel) {
        return productModel == null ? null : new Gson().toJson(productModel);
    }
}
