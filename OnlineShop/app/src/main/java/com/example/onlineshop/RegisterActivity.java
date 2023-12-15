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

        //setting the login button onClick
        binding.loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //navigate to login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

                //make the app can't back to this one
                finish();
            }
        });

        //setting the register button onClick
        binding.registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //read the fields text
                String pass = binding.regPassET.getText().toString();
                String user = binding.regUserET.getText().toString();
                String email = binding.regEmailET.getText().toString();

                //checking if any of fields is empty
                if(pass.equals("")||
                        user.equals("")|| email.equals(""))
                {
                    //making a message for the user to tell him to enter valid data
                    Toast.makeText(getApplicationContext(),"Please Enter Valid Data",Toast.LENGTH_LONG).show();
                }

                else
                {
                    //taking instance of user database
                    userDB = UserDB.getInstance(getApplicationContext());

                    //check if the username or the email aren't used before
                    if(userDB.userDao().failedRegUser(user,email).isEmpty())
                    {
                        //navigate to the NewUserActivity
                        Intent intent = new Intent(getApplicationContext(), NewUserActivity.class);

                        //send strings to the new activity to use there
                        intent.putExtra("pass",pass);
                        intent.putExtra("email",email);
                        intent.putExtra("user",user);
                        startActivity(intent);

                        //make the app can't back to here
                        finish();
                    }

                    //else if one of them is used before
                    else {

                        //make a message for the user to tell him that the user or the email is used before
                        Toast.makeText(getApplicationContext(),"Email or Username is already used",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}