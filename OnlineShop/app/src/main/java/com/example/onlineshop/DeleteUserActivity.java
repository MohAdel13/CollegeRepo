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

        binding = ActivityDeleteUserBinding.inflate(LayoutInflater.from(DeleteUserActivity.this));
        setContentView(binding.getRoot());

        userDB = UserDB.getInstance(getApplicationContext());

        binding.deleteUserBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<UserModel> user = userDB.userDao().checkUser(binding.deleteUsernameET.getText().toString());
                if(!user.isEmpty())
                {
                    userDB.userDao().deleteUser(user.get(0));
                    Toast.makeText(DeleteUserActivity.this, "User Is Deleted..", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(DeleteUserActivity.this, "User not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}