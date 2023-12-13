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

public class AddNewProductActivity extends AppCompatActivity {

     ActivityAddNewProductBinding binding;
    ProductDB productDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddNewProductBinding.inflate(LayoutInflater.from(AddNewProductActivity.this));
        setContentView(binding.getRoot());

        productDB=ProductDB.getInstance(getApplicationContext());

        binding.addProdBackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nt=new Intent(getApplicationContext(), AdminProductsActivity.class);
                startActivity(nt);

                finish();
            }
        });

        binding.addProductBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(binding.addTitleET.getText().toString().equals(""))
                        && !(binding.addProdDesET.getText().toString().equals(""))
                        && !(binding.addPriceET.getText().toString().equals(""))
                        && !(binding.addCatET.getText().toString().equals(""))
                        && !(binding.addCountET.getText().toString().equals(""))
                        && !(binding.addUrlET.getText().toString().equals(""))){

                    ProductModel prod=new ProductModel();
                    RatingModel rate=new RatingModel();

                    rate.rate=0;
                    rate.count=0;

                    prod.title=binding.addTitleET.getText().toString();
                    prod.category=binding.addCatET.getText().toString();
                    prod.count=Integer.parseInt(binding.addCountET.getText().toString());
                    prod.description=binding.addProdDesET.getText().toString();
                    prod.image=binding.addUrlET.getText().toString();
                    prod.price=Float.parseFloat(binding.addPriceET.getText().toString());
                    prod.rating=rate;

                    productDB.productDao().insertProduct(prod);
                    Toast.makeText(AddNewProductActivity.this, "Product Added", Toast.LENGTH_SHORT).show();

                }

                else{
                    Toast.makeText(AddNewProductActivity.this, "Enter Valid Data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}