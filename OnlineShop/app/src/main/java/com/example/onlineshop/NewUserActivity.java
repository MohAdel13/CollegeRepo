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
import com.example.onlineshop.pojo.ProductClient;
import com.example.onlineshop.pojo.ProductModel;
import com.example.onlineshop.pojo.UserDB;

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
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewUserBinding.inflate(LayoutInflater.from(NewUserActivity.this));
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String user = intent.getStringExtra("user");

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
                categories = cats;
                adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.catSpinner.setAdapter(adapter);
                binding.progressBar2.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                binding.progressBar2.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"You Are Offline...", Toast.LENGTH_LONG).show();
            }
        });


        binding.usernameTV.setText(user);

        binding.catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedItem = categories[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        binding.nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedItem == null)
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
                    userDB.userDao().updateCat(selectedItem, user);

                    Intent intent = new Intent(getApplicationContext(),ProductsActivity.class);
                    intent.putExtra("category", selectedItem);
                    startActivity(intent);
                }
            }
        });
    }
}