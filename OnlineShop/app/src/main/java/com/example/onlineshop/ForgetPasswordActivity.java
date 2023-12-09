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

        //linking the activity with its layout
        binding = ActivityForgetPasswordBinding.inflate(LayoutInflater.from(ForgetPasswordActivity.this));
        setContentView(binding.getRoot());


        //setting the back btn
        binding.forgetBackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //setting the reset password button
        binding.resetPasswordBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = binding.forgetUserET.getText().toString();
                String pass = binding.forgetPassET.getText().toString();
                String confPass = binding.confPassET.getText().toString();

                //check if any of the fields is missing
                if(pass.equals("")||
                        user.equals("")|| confPass.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Valid Data",Toast.LENGTH_LONG).show();
                }
                else
                {
                    //getting an instance of the database
                    userDB = UserDB.getInstance(getApplicationContext());

                    //checking if the username isn't correct
                    if(userDB.userDao().checkUser(user).isEmpty())
                    {
                        Toast.makeText(getApplicationContext(),"This User is Not Correct",Toast.LENGTH_LONG).show();
                    }

                    //checking if the pass and confirm pass not matched
                    else if(!pass.equals(confPass))
                    {
                        Toast.makeText(getApplicationContext(),"Password Not Matched",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        //updating the database with the new password
                        userDB.userDao().updatePassword(user,pass);
                        Toast.makeText(getApplicationContext(),"Password is Changed",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        });

        //setting the register button
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