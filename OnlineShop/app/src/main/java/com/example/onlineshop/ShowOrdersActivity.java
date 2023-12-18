package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.onlineshop.Adapters.OrdersAdapter;
import com.example.onlineshop.databinding.ActivityShowOrdersBinding;
import com.example.onlineshop.pojo.Models.OrderModel;
import com.example.onlineshop.pojo.Models.UserModel;
import com.example.onlineshop.pojo.RoomDataBases.UserDB;

import java.util.List;

public class ShowOrdersActivity extends AppCompatActivity {

    String user;
    UserModel us;
    UserDB userDB;
    List<OrderModel> orders;
    OrdersAdapter adapter;

    ActivityShowOrdersBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //linking the activity to its layout
        binding = ActivityShowOrdersBinding.inflate(LayoutInflater.from(ShowOrdersActivity.this));
        setContentView(binding.getRoot());

        //get the passed value from the previous activity
        Intent intent = getIntent();
        user = intent.getStringExtra("user");

        //get instance from user database
        userDB = UserDB.getInstance(getApplicationContext());

        //get user by its username from the database
        us = userDB.userDao().checkUser(user).get(0);

        //creating an adapter for recyclerView
        adapter = new OrdersAdapter(user, getApplicationContext());

        //pass the user orders as list of data for the adapter
        adapter.setOrders(us.orders);

        //link the adapter and the recyclerView
        binding.userOrderRV.setAdapter(adapter);
    }
}