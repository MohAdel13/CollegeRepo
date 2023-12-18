package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.onlineshop.databinding.ActivityLoginBinding;
import com.example.onlineshop.pojo.DesignPatterns.Login;
import com.example.onlineshop.pojo.DesignPatterns.LoginFactory;
import com.example.onlineshop.pojo.DesignPatterns.UserLogin;
import com.example.onlineshop.pojo.RoomDataBases.UserDB;
import com.example.onlineshop.pojo.Models.UserModel;

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
            //navigate to products activity
            Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);

            //pass values to the new activity
            intent.putExtra("category", reUser.get(0).favCategory);
            intent.putExtra("user", reUser.get(0).username);
            startActivity(intent);

            //can't go back to here anymore
            finish();
        }
        else
        {
            //setting the forgetPassword button onClick
            binding.forgetTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //navigate to the forget password activity
                    Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                    startActivity(intent);
                }
            });

            //setting the login button onClick
            binding.loginBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pass = binding.loginPassET.getText().toString();
                    String user = binding.loginUserET.getText().toString();

                    //check if there's any field missing
                    if (pass.equals("") ||
                            user.equals("")) {

                        //inform the user to enter valid data
                        Toast.makeText(getApplicationContext(), "Please Enter Valid Data", Toast.LENGTH_LONG).show();
                    }

                    else
                    {
                        //performing factory design pattern for authentication even for user or admin
                        LoginFactory loginFactory = new LoginFactory();
                        Login login = loginFactory.determineLoginFactory(user);
                        boolean found = login.Authenticate(user, pass, getApplicationContext());

                        //if the authentication succeeded
                        if (found)
                        {
                            //if it's user go to user activity
                            if(login.getType().equals("user")) {
                                //if the rememberMe check box is checked update the user field isRemembered in
                                    // the database
                                if (binding.rememberCB.isChecked()) {
                                    userDB.userDao().logout();
                                    userDB.userDao().updateIsRemembered(true, user);
                                }

                                //get the user (in factory design pattern there's a method do the authentication)
                                UserModel us = ((UserLogin)login).getUser();

                                //navigate to the products activity
                                Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);

                                //pass values to the new activity
                                intent.putExtra("category", us.favCategory);
                                intent.putExtra("user", us.username);
                                startActivity(intent);

                                //can't go back to here anymore
                                finish();
                            }

                            //else go to admin activity
                            else{
                                Intent intent = new Intent(getApplicationContext(), AdminPanelActivity.class);
                                startActivity(intent);
                            }
                        }
                        //if the authentication failed inform the user to enter correct data
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Please Enter Correct Data", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });


            //setting the register button onClick
            binding.registerTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //navigate to the register activity
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);

                    //can't go back to here anymore
                    finish();
                }
            });
        }
    }
}