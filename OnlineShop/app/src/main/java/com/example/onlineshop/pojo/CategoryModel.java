package com.example.onlineshop.pojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "CATEGORY_TABLE")
public class CategoryModel {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;

    public String getName() {
        return name;
    }
}
