package com.example.onlineshop.pojo.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.onlineshop.pojo.converters.CartItemConverter;
import com.example.onlineshop.pojo.converters.ProductsNameListConverter;

import java.util.List;

@Entity(tableName = "USER_TABLE")
public class UserModel {
    public String email;
    @PrimaryKey
            @NonNull
    public String username;
    public String password;
    public String birthDate;
    public boolean isRemembered;
    public String favCategory;
    @TypeConverters(CartItemConverter.class)
    public List<CartProductModel> cartItems;
    @TypeConverters(ProductsNameListConverter.class)
    public List<String> productsNames;
}
