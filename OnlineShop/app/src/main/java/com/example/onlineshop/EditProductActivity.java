package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.onlineshop.databinding.ActivityEditProductBinding;
import com.example.onlineshop.pojo.RoomDataBases.ProductDB;
import com.example.onlineshop.pojo.Models.ProductModel;

public class EditProductActivity extends AppCompatActivity {

    ProductDB productDB;
    String product;
    ProductModel prod;
    ActivityEditProductBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditProductBinding.inflate(LayoutInflater.from(EditProductActivity.this));
        setContentView(binding.getRoot());

        Intent intent =  getIntent();
        product = intent.getStringExtra("product");

        productDB = ProductDB.getInstance(getApplicationContext());
        prod = productDB.productDao().getProduct(product).get(0);

        binding.editCatET.setText(prod.category);
        binding.editCountET.setText(Integer.toString(prod.count));
        binding.editPriceET.setText(Float.toString(prod.price));
        binding.editProdDesET.setText(prod.description);
        binding.editUrlET.setText(prod.image);

        binding.editProductBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prod.count = Integer.parseInt(binding.editCountET.getText().toString());
                prod.price = Float.parseFloat(binding.editPriceET.getText().toString());
                prod.category = binding.editCatET.getText().toString();
                prod.description = binding.editProdDesET.getText().toString();
                prod.image = binding.editUrlET.getText().toString();

                productDB.productDao().updateRate(prod);

                Toast.makeText(EditProductActivity.this, "Product Updated Successfully..", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}