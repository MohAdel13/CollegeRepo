package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.onlineshop.databinding.ActivityForgetPasswordBinding;
import com.example.onlineshop.pojo.UserDB;

import java.util.Random;

public class ForgetPasswordActivity extends AppCompatActivity {
    ActivityForgetPasswordBinding binding;
    UserDB userDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent =new Intent();

        binding = ActivityForgetPasswordBinding.inflate(LayoutInflater.from(ForgetPasswordActivity.this));
        setContentView(binding.getRoot());

        binding.forgetBackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.forgetBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = binding.forgetUserET.getText().toString();
                String pass = binding.forgetPassET.getText().toString();
                String confPass = binding.confPassET.getText().toString();
                if(pass.equals("")||
                        user.equals("")|| confPass.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Valid Data",Toast.LENGTH_LONG).show();
                }
                else{
                    userDB = UserDB.getInstance(getApplicationContext());
                    if(userDB.userDao().checkUser(user).isEmpty())
                    {
                        Toast.makeText(getApplicationContext(),"This User is Not Correct",Toast.LENGTH_LONG).show();
                    }
                    else if(!pass.equals(confPass))
                    {
                        Toast.makeText(getApplicationContext(),"Password Not Matched",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        userDB.userDao().updatePassword(user,pass);
                        Toast.makeText(getApplicationContext(),"Password is Changed",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        });

        binding.register2TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}