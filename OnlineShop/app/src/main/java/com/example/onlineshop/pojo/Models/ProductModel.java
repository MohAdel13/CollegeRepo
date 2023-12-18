package com.example.onlineshop.pojo.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.onlineshop.pojo.converters.FeedbackListConverter;

import java.util.List;

@Entity(tableName = "PRODUCT_TABLE")
public class ProductModel {

    @PrimaryKey
    public int id;

    public String title;

    public float price;

    public String description;

    public String category;

    public String image;
    public RatingModel rating;

    public int count;

    public float sale;

    @TypeConverters(FeedbackListConverter.class)
    public List<FeedbackModel> feedbacks;

    public int soldCount;
}
