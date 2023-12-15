package com.example.onlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.onlineshop.databinding.ActivityNewUserBinding;
import com.example.onlineshop.pojo.Models.CreditCardModel;
import com.example.onlineshop.pojo.Models.UserModel;
import com.example.onlineshop.pojo.RoomDataBases.CategoryDB;
import com.example.onlineshop.pojo.Models.CategoryModel;
import com.example.onlineshop.pojo.API.ProductClient;
import com.example.onlineshop.pojo.RoomDataBases.ProductDB;
import com.example.onlineshop.pojo.RoomDataBases.UserDB;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NewUserActivity extends AppCompatActivity {
    ActivityNewUserBinding binding;
    UserDB userDB;
    String[] categories;
    String selectedItem = null;
    ProductDB productDB;
    CategoryDB categoryDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //linking the activity with its layout
        binding = ActivityNewUserBinding.inflate(LayoutInflater.from(NewUserActivity.this));
        setContentView(binding.getRoot());

        //get the passed values from the previous activity
        Intent intent = getIntent();
        String user = intent.getStringExtra("user");
        String pass = intent.getStringExtra("pass");
        String email = intent.getStringExtra("email");

        //getting instances for the used databases
        productDB = ProductDB.getInstance(getApplicationContext());
        categoryDB = CategoryDB.getInstance(getApplicationContext());
        userDB = UserDB.getInstance(getApplicationContext());

        // in case of first run to get data from API (Don't look at it, you will lose your mind)
/*<- click here if opened*/        if(categoryDB.categoryDao().getAllCategories().isEmpty()) {
            Single<String[]> cat = ProductClient.getINSTANCE().productInterface.getAllCategories()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

            cat.subscribe(new SingleObserver<String[]>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    binding.progressBar2.setVisibility(View.VISIBLE);
                }

                @Override
                public void onSuccess(@NonNull String[] cats) {
                    CategoryModel category;
                    for(int i=0; i<cats.length;i++)
                    {
                        category  = new CategoryModel();
                        category.name = cats[i];
                        categoryDB.categoryDao().insertCategory(category);
                    }
                    categories = cats;
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_spinner_dropdown_item, R.id.text1, categories);
                    binding.catSpinner.setAdapter(adapter);
                    binding.progressBar2.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    binding.progressBar2.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "You Are Offline...", Toast.LENGTH_LONG).show();
                }
            });
        }


        // in case of another runs after saving the data to the database
        //TODO "This case is the required case for the project"
        else {
            //get all the categories from database
            List<CategoryModel> cats = categoryDB.categoryDao().getAllCategories();

            //convert the categories to array to use it later in the spinner
            categories = cats.stream()
                    .map(CategoryModel::getName)
                    .toArray(String[]::new);

            //making an adapter to hold the array and use it in the spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_spinner_dropdown_item, R.id.text1, categories);
            binding.catSpinner.setAdapter(adapter);

            //stop the preloader
            binding.progressBar2.setVisibility(View.INVISIBLE);

            //Toast.makeText(this, "From Database", Toast.LENGTH_SHORT).show();
        }

        //say hello with the username
        binding.usernameTV.setText(user);

        //setting the spinner functionality
        binding.catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //saving the selected item
                selectedItem = categories[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        //set the next button onClick
        binding.nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if nothing is selected from the spinner
                if(selectedItem == null)
                {
                    Toast.makeText(NewUserActivity.this, "Please Enter A Category", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //check if the ssn field is empty
                    if(binding.newUserSsnET.equals(""))
                    {
                        Toast.makeText(NewUserActivity.this, "Please Enter Right Data", Toast.LENGTH_SHORT).show();
                    }

                    //converting the birthdate from datePicker to string
                    String day = Integer.toString(binding.datePicker.getDayOfMonth());
                    String month = Integer.toString(binding.datePicker.getMonth() + 1);
                    String year = Integer.toString(binding.datePicker.getYear());
                    String date = day + " / " + month + " / " + year;

                    //creating new user and initialize it with the data
                    UserModel us = new UserModel();
                    us.cartItems = new ArrayList<>();
                    us.email = email;
                    us.password = pass;
                    us.productsNames = new ArrayList<>();
                    us.birthDate = date;
                    us.orders = new ArrayList<>();
                    us.username = user;
                    us.favCategory = selectedItem;
                    us.isRemembered = false;
                    us.SSN = Integer.parseInt(binding.newUserSsnET.getText().toString());
                    CreditCardModel card = new CreditCardModel();
                    card.id = Integer.parseInt(binding.creditCardET.getText().toString());
                    card.credit = 2000;
                    us.card = card;

                    //insert the new user to the database
                    userDB.userDao().insertUser(us);

                    //navigate to the products activity
                    Intent intent = new Intent(getApplicationContext(),ProductsActivity.class);

                    //pass values to the next activity
                    intent.putExtra("category", selectedItem);
                    intent.putExtra("user", user);
                    startActivity(intent);

                    //cant go back to this activity any more
                    finish();
                }
            }
        });
    }
}