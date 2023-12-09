package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.onlineshop.databinding.ActivityLoginBinding;
import com.example.onlineshop.pojo.UserDB;
import com.example.onlineshop.pojo.UserModel;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    UserDB userDB;
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //linking the activity with its layout
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(LoginActivity.this));
        setContentView(binding.getRoot());

        //taking instance from the user database
        userDB = UserDB.getInstance(getApplicationContext());

        //check if there's a rememberMe user to login with it
        List<UserModel> reUser = userDB.userDao().checkIsRemembered();
        if(!reUser.isEmpty())
        {
            Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
            intent.putExtra("category", reUser.get(0).favCategory);
            intent.putExtra("user", reUser.get(0).username);
            startActivity(intent);
            finish();
        }
        else
        {
            //setting the forgetPassword button
            binding.forgetTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                    startActivity(intent);
                }
            });

            //setting the login button
            binding.loginBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pass = binding.loginPassET.getText().toString();
                    String user = binding.loginUserET.getText().toString();

                    //check if there's any field missing
                    if (pass.equals("") ||
                            user.equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Enter Valid Data", Toast.LENGTH_LONG).show();
                    }

                    else
                    {
                        //check the username and the password from the database and if found login
                        List<UserModel> us = userDB.userDao().getUser(user, pass);
                        if (!us.isEmpty())
                        {
                            //if the rememberMe check box is checked update the user field isRemembered in
                                //the database
                            if (binding.rememberCB.isChecked()) {
                                userDB.userDao().logout();
                                userDB.userDao().updateIsRemembered(true, user);
                            }
                            Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
                            intent.putExtra("category",us.get(0).favCategory);
                            intent.putExtra("user", us.get(0).username);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Please Enter Correct Data", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

            //setting the register button
            binding.registerTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}