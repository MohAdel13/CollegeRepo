package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.onlineshop.Adapters.FeedbackAdapter;
import com.example.onlineshop.databinding.ActivityFeedbacksBinding;
import com.example.onlineshop.pojo.Models.FeedbackModel;
import com.example.onlineshop.pojo.Models.ProductModel;
import com.example.onlineshop.pojo.RoomDataBases.ProductDB;

import java.util.List;

public class FeedbacksActivity extends AppCompatActivity {
    ActivityFeedbacksBinding binding;
    String product;
    ProductModel prod;
    List<FeedbackModel> feedbacks;
    ProductDB productDB;
    FeedbackAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //linking activity to its layout
        binding = ActivityFeedbacksBinding.inflate(LayoutInflater.from(FeedbacksActivity.this));
        setContentView(binding.getRoot());

        //read the data sent from the previous activity
        Intent intent = getIntent();
        product = intent.getStringExtra("product");

        //get instance from product database
        productDB = ProductDB.getInstance(getApplicationContext());

        //get the product by its name from the database
        prod = productDB.productDao().getProduct(product).get(0);

        //get the list of feedbacks sent for this product
        feedbacks = prod.feedbacks;

        //initializing an adapter for the recyclerView
        adapter = new FeedbackAdapter(getApplicationContext());

        //setting the feedbacks got from the product to be the list for adapter data
        adapter.setFeedbacks(feedbacks);

        //link the adapter with the recyclerView
        binding.feedbacksRV.setAdapter(adapter);
    }
}