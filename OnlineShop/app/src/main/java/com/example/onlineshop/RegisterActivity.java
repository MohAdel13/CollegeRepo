package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.onlineshop.databinding.ActivityRegisterBinding;
import com.example.onlineshop.pojo.RoomDataBases.UserDB;
import com.example.onlineshop.pojo.Models.UserModel;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    UserDB userDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //linking the activity to its layout
        binding = ActivityRegisterBinding.inflate(LayoutInflater.from(RegisterActivity.this));
        setContentView(binding.getRoot());

        //setting the login button
        binding.loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //setting the register button
        binding.registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = binding.regPassET.getText().toString();
                String user = binding.regUserET.getText().toString();
                String email = binding.regEmailET.getText().toString();

                //checking if any of fields is empty
                if(pass.equals("")||
                        user.equals("")|| email.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Valid Data",Toast.LENGTH_LONG).show();
                }

                else
                {
                    //taking instance of user database
                    userDB = UserDB.getInstance(getApplicationContext());

                    //check if the username or the email aren't used before
                    if(userDB.userDao().failedRegUser(user,email).isEmpty())
                    {
                        //initializing a new user with the data got from the user
                        UserModel us = new UserModel();
                        us.email = email;
                        us.password = pass;
                        us.username = user;
                        us.cartItems = new ArrayList<>();
                        us.productsNames = new ArrayList<>();

                        //update the database
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