package com.example.onlineshop.pojo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProduct(ProductModel product);

    @Query("SELECT * FROM PRODUCT_TABLE")
    List<ProductModel> getAllProducts();

    @Delete
    void deleteProduct(ProductModel Product);
}