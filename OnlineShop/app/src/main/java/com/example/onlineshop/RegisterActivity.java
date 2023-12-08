package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.onlineshop.databinding.ActivityRegisterBinding;
import com.example.onlineshop.pojo.CartProductModel;
import com.example.onlineshop.pojo.ProductModel;
import com.example.onlineshop.pojo.UserDB;
import com.example.onlineshop.pojo.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    UserDB userDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(LayoutInflater.from(RegisterActivity.this));
        setContentView(binding.getRoot());
        binding.loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        binding.registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = binding.regPassET.getText().toString();
                String user = binding.regUserET.getText().toString();
                String email = binding.regEmailET.getText().toString();
                if(pass.equals("")||
                        user.equals("")|| email.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Valid Data",Toast.LENGTH_LONG).show();
                }
                else
                {
                    userDB = UserDB.getInstance(getApplicationContext());
                    if(userDB.userDao().failedRegUser(user,email).isEmpty())
                    {
                        UserModel us = new UserModel();
                        us.email = email;
                        us.password = pass;
                        us.username = user;
                        us.cartItems = new ArrayList<>();
                        us.productsNames = new ArrayList<>();
                        userDB.userDao().insertUser(us);
                        Intent intent = new Intent(getApplicationContext(), NewUserActivity.class);
                        intent.putExtra("user",user);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Email or Username is already used",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}