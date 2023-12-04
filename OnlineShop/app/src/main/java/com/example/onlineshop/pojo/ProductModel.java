package com.example.onlineshop.pojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
}
