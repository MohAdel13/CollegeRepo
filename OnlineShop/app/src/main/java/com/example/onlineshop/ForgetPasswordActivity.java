package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.onlineshop.databinding.ActivityForgetPasswordBinding;
import com.example.onlineshop.pojo.RoomDataBases.UserDB;

public class ForgetPasswordActivity extends AppCompatActivity {
    ActivityForgetPasswordBinding binding;
    UserDB userDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //linking the activity with its layout
        binding = ActivityForgetPasswordBinding.inflate(LayoutInflater.from(ForgetPasswordActivity.this));
        setContentView(binding.getRoot());


        //setting the back button onClick
        binding.forgetBackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //navigate to the login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

                //can't go back to this activity any more
                finish();
            }
        });

        //setting the reset password button onClick
        binding.resetPasswordBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get the fields values
                String user = binding.forgetUserET.getText().toString();
                String pass = binding.forgetPassET.getText().toString();
                String confPass = binding.confPassET.getText().toString();

                //check if any of the fields is missing
                if(pass.equals("")||
                        user.equals("")|| confPass.equals(""))
                {
                    //make a message to the user to tell him to enter valid data
                    Toast.makeText(getApplicationContext(),"Please Enter Valid Data",Toast.LENGTH_LONG).show();
                }
                else
                {
                    //getting an instance of the user database
                    userDB = UserDB.getInstance(getApplicationContext());

                    //checking if the username isn't correct
                    if(userDB.userDao().checkUser(user).isEmpty())
                    {
                        //inform the user that the username is incorrect
                        Toast.makeText(getApplicationContext(),"This User is Not Correct..",Toast.LENGTH_LONG).show();
                    }

                    //checking if the pass and confirm pass not matched
                    else if(!pass.equals(confPass))
                    {
                        //inform the user that the password fields are not matched
                        Toast.makeText(getApplicationContext(),"Password Not Matched..",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        //updating the database with the new password
                        userDB.userDao().updatePassword(user,pass);

                        //tell the user that the password is changed
                        Toast.makeText(getApplicationContext(),"Password is Changed..",Toast.LENGTH_LONG).show();

                        //navigate to the login activity
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);

                        //can't back to this activity anymore
                        finish();
                    }

                }
            }
        });

        //setting the register button onClick
        binding.register2TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //navigate to the register activity
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);

                //can't back to this activity anymore
                finish();
            }
        });
    }
}