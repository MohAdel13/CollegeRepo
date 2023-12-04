package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.onlineshop.databinding.ActivityNewUserBinding;
import com.example.onlineshop.pojo.UserDB;

public class NewUserActivity extends AppCompatActivity {
    ActivityNewUserBinding binding;
    UserDB userDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewUserBinding.inflate(LayoutInflater.from(NewUserActivity.this));
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String user = intent.getStringExtra("user");

        binding.usernameTV.setText(user);

        binding.nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cat = binding.categoryET.getText().toString();
                if(cat.equals(""))
                {
                    Toast.makeText(NewUserActivity.this, "Please Enter A Category", Toast.LENGTH_SHORT).show();
                }
                else {
                    userDB = UserDB.getInstance(getApplicationContext());

                    String day = Integer.toString(binding.datePicker.getDayOfMonth());
                    String month = Integer.toString(binding.datePicker.getMonth() + 1);
                    String year = Integer.toString(binding.datePicker.getYear());
                    String date = day + " / " + month + " / " + year;

                    userDB.userDao().updateBirth(user, date);
                    userDB.userDao().updateCat(cat, user);

                    Intent intent = new Intent(getApplicationContext(),ProductsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}