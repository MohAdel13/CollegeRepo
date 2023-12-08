package com.example.onlineshop.pojo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insertCategory(CategoryModel category);

    @Delete
    void deleteCategory(CategoryModel category);

    @Query("SELECT * FROM category_table")
    List<CategoryModel> getAllCategories();
}
