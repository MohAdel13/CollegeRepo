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

        binding = ActivityLoginBinding.inflate(LayoutInflater.from(LoginActivity.this));
        setContentView(binding.getRoot());

        userDB = UserDB.getInstance(getApplicationContext());
        List<UserModel> reUser = userDB.userDao().checkIsRemembered();
        if(!reUser.isEmpty())
        {
            Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
            intent.putExtra("category", reUser.get(0).favCategory);
            startActivity(intent);
        }
        else {

            binding.forgetTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                    startActivity(intent);
                }
            });

            binding.loginBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pass = binding.loginPassET.getText().toString();
                    String user = binding.loginUserET.getText().toString();
                    if (pass.equals("") ||
                            user.equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Enter Valid Data", Toast.LENGTH_LONG).show();
                    } else {
                        userDB = UserDB.getInstance(getApplicationContext());
                        List<UserModel> us = userDB.userDao().getUser(user, pass);
                        if (!us.isEmpty()) {

                            if (binding.rememberCB.isChecked()) {
                                userDB.userDao().logout();
                                userDB.userDao().updateIsRemembered(true, user);
                            }

                            Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
                            intent.putExtra("category",us.get(0).favCategory);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Enter Correct Data", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

            binding.registerTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}