package com.example.onlineshop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.onlineshop.databinding.ActivityUserProfileBinding;
import com.example.onlineshop.pojo.RoomDataBases.CategoryDB;
import com.example.onlineshop.pojo.Models.CategoryModel;
import com.example.onlineshop.pojo.RoomDataBases.UserDB;
import com.example.onlineshop.pojo.Models.UserModel;

import java.util.List;

public class UserProfileActivity extends AppCompatActivity {
    ActivityUserProfileBinding binding;
    String user;
    CategoryDB categoryDB;
    UserDB userDB;
    String[] categories;
    String selectedItem = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //linking activity to its layout
        binding = ActivityUserProfileBinding.inflate(LayoutInflater.from(UserProfileActivity.this));
        setContentView(binding.getRoot());

        //get the passed values from the above activity
        Intent intent = getIntent();
        user = intent.getStringExtra("user");

        //get instance of used databased
        categoryDB = CategoryDB.getInstance(getApplicationContext());
        userDB = UserDB.getInstance(getApplicationContext());

        //get all the categories from database
        List<CategoryModel> cats = categoryDB.categoryDao().getAllCategories();

        //convert the categories to array to use it in the spinner
        categories = cats.stream()
                .map(CategoryModel::getName)
                .toArray(String[]::new);

        //making an adapter to hold the array and use it in the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_spinner_dropdown_item, R.id.text1, categories);
        binding.profileSpinner.setAdapter(adapter);

        //get the user from database
        UserModel us = userDB.userDao().checkUser(user).get(0);
        if(us.favCategory!=null) {
            //if the user has stored favourite category set it as initial value for the spinner
            binding.profileSpinner.setSelection(adapter.getPosition(us.favCategory));
            selectedItem = us.favCategory;
        }

        if(us.birthDate!=null)
        {
            //if the birthdate of the user is stored can the datePicker starts from it
            String[] userBirth = us.birthDate.split(" / ");
            binding.datePicker2.init(Integer.parseInt(userBirth[2]),
                    Integer.parseInt(userBirth[1])-1,Integer.parseInt(userBirth[0]),null);
        }

        //setting the spinner functionality
        binding.profileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //saving the selected item
                selectedItem = categories[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        //set the back button
        binding.profileBackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //set the save button
        binding.profileSaveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedItem == null)
                {
                    Toast.makeText(getApplicationContext(), "Please Enter A Category", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //converting the birthdate from datePicker to string
                    String day = Integer.toString(binding.datePicker2.getDayOfMonth());
                    String month = Integer.toString(binding.datePicker2.getMonth() + 1);
                    String year = Integer.toString(binding.datePicker2.getYear());
                    String date = day + " / " + month + " / " + year;

                    //store the data in the database
                    userDB.userDao().updateBirth(user, date);
                    userDB.userDao().updateCat(selectedItem, user);

                    Intent intent = new Intent(getApplicationContext(),ProductsActivity.class);
                    intent.putExtra("category", selectedItem);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                }
            }
        });

        //set the change password button
        binding.profileChangePassTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.profileRmvAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create the alertDialog and give it the theme we created
                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this, R.style.MyDialogTheme);

                //setting a layout we created for the alertDialog to the builder
                builder.setView(getLayoutInflater().inflate(R.layout.dialog_layout2,null));

                //setting title for the dialog
                builder.setTitle("Are You Sure?!");

                //setting Positive button for applying the account remove
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        userDB.userDao().deleteUser(us);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

                //setting negative button for canceling(Doing nothing)
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                //showing the dialog
                builder.show();
            }
        });

    }
}