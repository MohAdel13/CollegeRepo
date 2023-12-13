package com.example.onlineshop.pojo.RoomDataBases;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.onlineshop.pojo.Models.CategoryModel;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insertCategory(CategoryModel category);

    @Query("SELECT * FROM category_table")
    List<CategoryModel> getAllCategories();

    @Query("DELETE FROM category_table WHERE name =:name")
    void deleteCategory(String name);
}
