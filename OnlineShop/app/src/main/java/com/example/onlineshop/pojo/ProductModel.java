package com.example.onlineshop.pojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.onlineshop.pojo.RatingModel;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "PRODUCT_TABLE")
public class ProductModel {

    @PrimaryKey
    public int id;

    @SerializedName("title")
    public String title;

    @SerializedName("price")
    public float price;

    @SerializedName("description")
    public String description;

    @SerializedName("category")
    public String category;

    @SerializedName("image")
    public String image;

    @SerializedName("rating")
    public RatingModel rating;

    @SerializedName("count")
    public int count;
}
