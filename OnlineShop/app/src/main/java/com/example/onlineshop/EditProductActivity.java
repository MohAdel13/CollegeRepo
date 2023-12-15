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

        //linking activity to its layout
        binding = ActivityEditProductBinding.inflate(LayoutInflater.from(EditProductActivity.this));
        setContentView(binding.getRoot());

        //get the value passed from the previous activity
        Intent intent =  getIntent();
        product = intent.getStringExtra("product");

        //take instance from product database
        productDB = ProductDB.getInstance(getApplicationContext());

        //get the product from database by its name
        prod = productDB.productDao().getProduct(product).get(0);

        //initialize the editTexts with the values of the product
        binding.editCatET.setText(prod.category);
        binding.editCountET.setText(Integer.toString(prod.count));
        binding.editPriceET.setText(Float.toString(prod.price));
        binding.editProdDesET.setText(prod.description);
        binding.editUrlET.setText(prod.image);
        binding.editSaleET.setText(Float.toString(prod.sale));

        //set the edit product button onClick
        binding.editProductBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //change the values of the product to the new values in editTexts
                prod.count = Integer.parseInt(binding.editCountET.getText().toString());
                prod.price = Float.parseFloat(binding.editPriceET.getText().toString());
                prod.category = binding.editCatET.getText().toString();
                prod.description = binding.editProdDesET.getText().toString();
                prod.image = binding.editUrlET.getText().toString();
                prod.sale = Float.parseFloat(binding.editSaleET.getText().toString());

                //update the product in the database (the method is just called update rate but it update the whole product)
                productDB.productDao().updateRate(prod);

                //inform the admin that the product is updated
                Toast.makeText(EditProductActivity.this, "Product Updated Successfully..", Toast.LENGTH_SHORT).show();

                //close this activity and go to the previous one
                finish();
            }
        });
    }
}