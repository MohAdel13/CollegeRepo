package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.onlineshop.Adapters.AdminOrderAdapter;
import com.example.onlineshop.databinding.ActivityAdminOrderBinding;
import com.example.onlineshop.pojo.Models.OrderModel;
import com.example.onlineshop.pojo.RoomDataBases.OrderDB;

import java.util.List;

public class AdminOrderActivity extends AppCompatActivity {

    ActivityAdminOrderBinding binding;
    OrderDB orderDB;
    List<OrderModel> orders;
    AdminOrderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //linking activity to its layout
        binding = ActivityAdminOrderBinding.inflate(LayoutInflater.from(AdminOrderActivity.this));
        setContentView(binding.getRoot());

        //get instance from order database
        orderDB = OrderDB.getInstance(getApplicationContext());

        //get all the orders from database
        orders = orderDB.orderDao().getAllOrders();

        //initializing an adapter for recyclerView
        adapter = new AdminOrderAdapter(getApplicationContext());

        //setting the orders got from the database to be the list for adapter data
        adapter.setOrders(orders);

        //link the adapter with the recyclerView
        binding.adminOrdersRV.setAdapter(adapter);
    }
}