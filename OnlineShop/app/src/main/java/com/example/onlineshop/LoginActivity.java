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

public class LoginActivity extends AppCompatActivity {
    UserDB userDB;
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(LayoutInflater.from(LoginActivity.this));
        setContentView(binding.getRoot());

        userDB = UserDB.getInstance(getApplicationContext());
        if(!userDB.userDao().checkIsRemembered().isEmpty())
        {
            Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
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
                        if (!userDB.userDao().getUser(user, pass).isEmpty()) {
                            Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
                            startActivity(intent);

                            if (binding.rememberCB.isChecked()) {
                                userDB.userDao().logout();
                                userDB.userDao().updateIsRemembered(true, user);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Enter Correct Data", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

            binding.forgetTV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onFocusChange(View view, boolean b) {
                    binding.forgetTV.setTextColor(R.color.orange);
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