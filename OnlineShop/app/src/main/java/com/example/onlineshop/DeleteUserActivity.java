package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.onlineshop.databinding.ActivityDeleteUserBinding;
import com.example.onlineshop.pojo.Models.UserModel;
import com.example.onlineshop.pojo.RoomDataBases.UserDB;

import java.util.List;

public class DeleteUserActivity extends AppCompatActivity {
    UserDB userDB;
    ActivityDeleteUserBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //linking activity to its layout
        binding = ActivityDeleteUserBinding.inflate(LayoutInflater.from(DeleteUserActivity.this));
        setContentView(binding.getRoot());

        //get instance from user database
        userDB = UserDB.getInstance(getApplicationContext());

        //setting the delete button onClick
        binding.deleteUserBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check if the username written by admin is exist in database
                List<UserModel> user = userDB.userDao().checkUser(binding.deleteUsernameET.getText().toString());
                if(!user.isEmpty())
                {
                    //if found delete it
                    userDB.userDao().deleteUser(user.get(0));

                    //inform the admin with message that the user is deleted
                    Toast.makeText(DeleteUserActivity.this, "User Is Deleted..", Toast.LENGTH_SHORT).show();
                }
                else {
                    //else if not found tell the admin that it's not found
                    Toast.makeText(DeleteUserActivity.this, "User not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}