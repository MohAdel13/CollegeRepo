package com.example.onlineshop.pojo.RoomDataBases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlineshop.pojo.Models.ProductModel;

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

    @Query("UPDATE PRODUCT_TABLE SET soldCount = soldCount +:newCount WHERE title = :title")
    void incrementSold(int newCount, String title);

    @Query("SELECT * FROM PRODUCT_TABLE ORDER BY soldCount DESC LIMIT 10")
    List<ProductModel> getTopProducts();
}
