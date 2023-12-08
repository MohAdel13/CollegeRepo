package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.onlineshop.databinding.ActivitySingleProductBinding;
import com.example.onlineshop.pojo.CartProductModel;
import com.example.onlineshop.pojo.ProductDB;
import com.example.onlineshop.pojo.ProductModel;
import com.example.onlineshop.pojo.UserDB;
import com.example.onlineshop.pojo.UserModel;

import java.util.List;
import java.util.Map;

public class SingleProductActivity extends AppCompatActivity {
    ActivitySingleProductBinding binding;
    String product;
    String user;
    ProductDB productDB;
    ProductModel prod;
    UserDB userDB;
    UserModel us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySingleProductBinding.inflate(LayoutInflater.from(SingleProductActivity.this));
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        product = intent.getStringExtra("product");
        user = intent.getStringExtra("user");

        //Toast.makeText(this, product+ " " + user, Toast.LENGTH_SHORT).show();

        productDB = ProductDB.getInstance(getApplicationContext());
        prod = productDB.productDao().getProduct(product).get(0);

        userDB = UserDB.getInstance(getApplicationContext());
        us = userDB.userDao().checkUser(user).get(0);

        Glide.with(getApplicationContext()).load(prod.image).into(binding.productImageIV);

        binding.productNameTV.setText(prod.title);

        binding.productCatTV.setText(prod.category);

        binding.productPriceTV.setText("USD " + Float.toString(prod.price));

        binding.productDesTV.setText(prod.description);

        binding.plusTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(binding.countTV.getText().toString());
                count++;
                binding.countTV.setText(Integer.toString(count));
            }
        });

        binding.minusTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(binding.countTV.getText().toString());
                if(count > 0)
                {
                    count--;
                    binding.countTV.setText(Integer.toString(count));
                }
            }
        });

        binding.addToCartBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newCount = Integer.parseInt(binding.countTV.getText().toString());
                List<String> products = us.productsNames;
                if(newCount>0) {
                    if (products.contains(product))
                    {
                        us.cartItems.get(products.indexOf(product)).count += newCount;
                        userDB.userDao().updateCart(us);
                    } else
                    {
                        CartProductModel cartProduct = new CartProductModel();
                        cartProduct.count = newCount;
                        cartProduct.product = prod;
                        us.cartItems.add(cartProduct);
                        us.productsNames.add(product);
                    }
                    userDB.userDao().updateCart(us);
                    Toast.makeText(SingleProductActivity.this, "Added to Cart..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}