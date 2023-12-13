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

        binding = ActivityAdminPanelBinding.inflate(LayoutInflater.from(AdminPanelActivity.this));
        setContentView(binding.getRoot());

        binding.editProductsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminProductsActivity.class);
                startActivity(intent);
            }
        });
    }
}