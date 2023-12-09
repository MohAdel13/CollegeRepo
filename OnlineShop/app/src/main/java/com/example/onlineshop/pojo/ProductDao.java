package com.example.onlineshop.pojo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProduct(ProductModel product);

    @Query("SELECT * FROM PRODUCT_TABLE")
    List<ProductModel> getAllProducts();

    @Delete
    void deleteProduct(ProductModel Product);

    @Query("SELECT * FROM PRODUCT_TABLE WHERE category =:category")
    List<ProductModel> getProductsByCategory(String category);

    @Query("SELECT DISTINCT category FROM PRODUCT_TABLE")
    String[] getAllCategories();

    @Query("Select * From PRODUCT_TABLE Where title =:title")
    List<ProductModel> getProduct(String title);

    @Query("SELECT count FROM PRODUCT_TABLE WHERE title =:title")
    List<Integer> getCount(String title);

    @Query("Update PRODUCT_TABLE SET count = count -:count WHERE title =:title")
    void setCount(String title,int count);

    @Update
    void updateRate(ProductModel product);
}
