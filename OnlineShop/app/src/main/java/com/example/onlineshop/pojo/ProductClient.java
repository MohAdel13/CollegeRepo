package com.example.onlineshop.pojo;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductClient {
    private static ProductClient INSTANCE;
    public ProductInterface productInterface;

    public ProductClient()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fakestoreapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        productInterface = retrofit.create(ProductInterface.class);
    }

    public static ProductClient getINSTANCE()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new ProductClient();
        }
        return INSTANCE;
    }
}
