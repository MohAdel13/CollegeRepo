package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.onlineshop.databinding.ActivityCartBinding;
import com.example.onlineshop.pojo.CartProductModel;
import com.example.onlineshop.pojo.ProductDB;
import com.example.onlineshop.pojo.ProductModel;
import com.example.onlineshop.pojo.UserDB;
import com.example.onlineshop.pojo.UserModel;

import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    public ActivityCartBinding binding;
    UserDB userDB;
    List<CartProductModel> cart;
    UserModel us;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //for linking the activity with its layout
        binding = ActivityCartBinding.inflate(LayoutInflater.from(CartActivity.this));
        setContentView(binding.getRoot());

        //for getting the passed element from the above activity
        Intent intent = getIntent();
        user = intent.getStringExtra("user");

        //taking an instance from user database and get the from it and his cart
        userDB = UserDB.getInstance(getApplicationContext());
        us = userDB.userDao().checkUser(user).get(0);
        cart = us.cartItems;

        //making an adapter for the recyclerView and pass to it the cart then add it to the recyclerView
        CartAdapter adapter = new CartAdapter(user, getApplicationContext());
        adapter.setCart(cart);
        binding.CartRV.setAdapter(adapter);

        //checking that the cart isn't empty to make the checkout button disabled
        if(!us.cartItems.isEmpty()){
            binding.checkoutBTN.setBackgroundColor(getColor(R.color.orange));
        }

        //setting the backBtn operation
        binding.cartBackBTN.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
        });

        //setting the operation of the checkout button
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