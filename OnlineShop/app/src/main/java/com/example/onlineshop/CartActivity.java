package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.onlineshop.databinding.ActivityCartBinding;
import com.example.onlineshop.pojo.CartProductModel;
import com.example.onlineshop.pojo.ProductModel;
import com.example.onlineshop.pojo.UserDB;
import com.example.onlineshop.pojo.UserModel;

import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    UserDB userDB;
    List<CartProductModel> cart;

    UserModel us;

    String user;

    List<ProductModel> productsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCartBinding.inflate(LayoutInflater.from(CartActivity.this));
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        user = intent.getStringExtra("user");

        userDB = UserDB.getInstance(getApplicationContext());
        us = userDB.userDao().checkUser(user).get(0);
        cart = us.cartItems;

        CartAdapter adapter = new CartAdapter(user, getApplicationContext());
        adapter.setCart(cart);

        binding.CartRV.setAdapter(adapter);

        binding.cartBackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.checkoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!us.cartItems.isEmpty())
                {
                    Intent intent = new Intent(getApplicationContext(),OrderSubmitActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                }
            }
        });
    }
}