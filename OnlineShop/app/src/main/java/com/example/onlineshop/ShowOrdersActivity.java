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

        binding = ActivityShowOrdersBinding.inflate(LayoutInflater.from(ShowOrdersActivity.this));
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        user = intent.getStringExtra("user");

        userDB = UserDB.getInstance(getApplicationContext());
        us = userDB.userDao().checkUser(user).get(0);

        orders = us.orders;
        adapter = new OrdersAdapter(user, getApplicationContext());

        adapter.setOrders(orders);
        binding.userOrderRV.setAdapter(adapter);
    }
}