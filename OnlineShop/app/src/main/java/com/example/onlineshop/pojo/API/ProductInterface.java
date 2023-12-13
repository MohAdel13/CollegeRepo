package com.example.onlineshop.pojo.API;

import com.example.onlineshop.pojo.Models.ProductModel;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductInterface {
    @GET("/products")
    Single<List<ProductModel>>getAllProducts();

    @GET("/products/category/{categoryName}")
    Single<List<ProductModel>> getProductsByCategory(@Path("categoryName") String categoryName);

    @GET("/products/categories")
    Single<String[]> getAllCategories();
}
