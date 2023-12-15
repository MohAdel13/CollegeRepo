package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.onlineshop.databinding.ActivityAdminPanelBinding;
import com.example.onlineshop.databinding.ActivityAdminProductsBinding;

public class AdminPanelActivity extends AppCompatActivity {
    ActivityAdminPanelBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //linking activity to its layout
        binding = ActivityAdminPanelBinding.inflate(LayoutInflater.from(AdminPanelActivity.this));
        setContentView(binding.getRoot());

        //setting the edit products button onClick
        binding.editProductsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to the admin products activity
                Intent intent = new Intent(getApplicationContext(), AdminProductsActivity.class);
                startActivity(intent);
            }
        });

        //setting the edit categories button onClick
        binding.editCategoriesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to the admin categories activity
                Intent intent = new Intent(getApplicationContext(), AdminCategoriesActivity.class);
                startActivity(intent);
            }
        });

        //setting the edit users button
        binding.editUsersBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to the delete user activity
                Intent intent = new Intent(getApplicationContext(), DeleteUserActivity.class);
                startActivity(intent);
            }
        });

        //setting the show orders button
        binding.showOrdersBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to the admin order activity
                Intent intent = new Intent(getApplicationContext(), AdminOrderActivity.class);
                startActivity(intent);
            }
        });
    }
}