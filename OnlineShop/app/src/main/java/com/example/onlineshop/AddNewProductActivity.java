package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.example.onlineshop.databinding.ActivityAddNewProductBinding;

import com.example.onlineshop.pojo.RoomDataBases.ProductDB;
import com.example.onlineshop.pojo.Models.ProductModel;
import com.example.onlineshop.pojo.Models.RatingModel;

import java.util.ArrayList;

public class AddNewProductActivity extends AppCompatActivity {

    ActivityAddNewProductBinding binding;
    ProductDB productDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //linking activity to its layout
        binding = ActivityAddNewProductBinding.inflate(LayoutInflater.from(AddNewProductActivity.this));
        setContentView(binding.getRoot());

        //get instance from product database
        productDB = ProductDB.getInstance(getApplicationContext());

        // setting the back button onClick
        binding.addProdBackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //navigate to AdminProductsActivity
                Intent intent = new Intent(getApplicationContext(), AdminProductsActivity.class);
                startActivity(intent);

                //this line prevent going back to this activity
                finish();
            }
        });

        //setting the add product button onClick
        binding.addProductBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check that all the fields not empty
                if(!(binding.addTitleET.getText().toString().equals(""))
                        && !(binding.addProdDesET.getText().toString().equals(""))
                        && !(binding.addPriceET.getText().toString().equals(""))
                        && !(binding.addCatET.getText().toString().equals(""))
                        && !(binding.addCountET.getText().toString().equals(""))
                        && !(binding.addUrlET.getText().toString().equals(""))){

                    //initializing a new product model
                    ProductModel prod=new ProductModel();

                    //adding initial values to the new product and the values that was set in editTexts
                    RatingModel rate=new RatingModel();
                    rate.rate = 0;
                    rate.count = 0;
                    prod.title = binding.addTitleET.getText().toString();
                    prod.category = binding.addCatET.getText().toString();
                    prod.count = Integer.parseInt(binding.addCountET.getText().toString());
                    prod.description = binding.addProdDesET.getText().toString();
                    prod.image = binding.addUrlET.getText().toString();
                    prod.price = Float.parseFloat(binding.addPriceET.getText().toString());
                    prod.rating = rate;
                    prod.feedbacks = new ArrayList<>();

                    //if the admin didn't set sale for the product it will be with 0 value
                    if(!binding.addProductSaleET.getText().toString().equals(""))
                    {
                        prod.sale = Float.parseFloat(binding.addProductSaleET.getText().toString());
                    }
                    else {
                        prod.sale = 0;
                    }

                    //insert the new product in the database
                    productDB.productDao().insertProduct(prod);

                    //inform the admin that the product is added
                    Toast.makeText(AddNewProductActivity.this, "Product Added..", Toast.LENGTH_SHORT).show();

                }

                //else if some of fields is missing
                else{

                    //tell the admin to enter valid data
                    Toast.makeText(AddNewProductActivity.this, "Enter Valid Data..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}