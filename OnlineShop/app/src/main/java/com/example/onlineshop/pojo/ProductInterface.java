package com.example.onlineshop.pojo;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface ProductInterface {
    @GET("/products")
    Single<List<ProductModel>>getAllProducts();
}
