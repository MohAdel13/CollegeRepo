package com.example.onlineshop.pojo.converters;

import androidx.room.TypeConverter;

import com.example.onlineshop.pojo.Models.CreditCardModel;
import com.google.gson.Gson;

public class CreditCardConverter {
    @TypeConverter
    public CreditCardModel GsonToSource(String s)
    {
        return new Gson().fromJson(s,CreditCardModel.class);
    }

    @TypeConverter
    public String FromSource(CreditCardModel c)
    {
        return new Gson().toJson(c);
    }
}
