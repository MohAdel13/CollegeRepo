package com.example.onlineshop.pojo.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.onlineshop.pojo.converters.OrderProductsConverter;
import com.example.onlineshop.pojo.converters.OrderProductsCountConverter;

import java.util.List;

@Entity(tableName = "ORDER_TABLE")
public class OrderModel {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String username;

    @TypeConverters(OrderProductsConverter.class)
    public List<ProductModel> products;
    public Float totalPrice;
    @TypeConverters(OrderProductsCountConverter.class)
    public List<Integer> countOfEachProduct;
    public int state;
}
