package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.onlineshop.Adapters.CartAdapter;
import com.example.onlineshop.databinding.ActivityCartBinding;
import com.example.onlineshop.pojo.Models.CartProductModel;
import com.example.onlineshop.pojo.RoomDataBases.UserDB;
import com.example.onlineshop.pojo.Models.UserModel;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
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

        //for getting the passed element from the previous activity
        Intent intent = getIntent();
        user = intent.getStringExtra("user");

        //taking an instance from user database and get the user from it and his cart
        userDB = UserDB.getInstance(getApplicationContext());
        us = userDB.userDao().checkUser(user).get(0);
        cart = us.cartItems;

        //making an adapter for the recyclerView
        CartAdapter adapter = new CartAdapter(user, getApplicationContext());

        //adding the cart to the adapter
        adapter.setCart(cart);

        //link the adapter with the recyclerView
        binding.CartRV.setAdapter(adapter);

        //checking that the cart isn't empty to make the checkout button disabled
        if(!us.cartItems.isEmpty()){
            binding.checkoutBTN.setBackgroundColor(getColor(R.color.orange));
        }

        //setting the backBtn operation
        binding.cartBackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close this activity and go to the previous one
                finish();
            }
        });

        //setting the orders button onClick
        binding.ordersBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to orders activity
                Intent intent = new Intent(getApplicationContext(), ShowOrdersActivity.class);

                //pass the username to the new activity
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        //setting the operation of the checkout button
        binding.checkoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check first that the cart of the user is not empty
                if(!us.cartItems.isEmpty())
                {
                    //navigate to the order submit activity
                    Intent intent = new Intent(getApplicationContext(),OrderSubmitActivity.class);

                    //pass the username to the new activity
                    intent.putExtra("user",user);
                    startActivity(intent);
                }
            }
        });
    }
}