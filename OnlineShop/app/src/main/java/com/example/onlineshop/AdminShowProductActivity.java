package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.onlineshop.databinding.ActivityAdminShowProductBinding;
import com.example.onlineshop.pojo.RoomDataBases.ProductDB;
import com.example.onlineshop.pojo.Models.ProductModel;

public class AdminShowProductActivity extends AppCompatActivity {
    ActivityAdminShowProductBinding binding;
    String product;
    ProductDB productDB;
    ProductModel prod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminShowProductBinding.inflate(LayoutInflater.from(AdminShowProductActivity.this));
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        product = intent.getStringExtra("product");

        productDB = ProductDB.getInstance(getApplicationContext());
        prod = productDB.productDao().getProduct(product).get(0);

        binding.adminProductCatTV.setText(prod.category);
        binding.adminProductDesTV.setText(prod.description);
        Glide.with(getApplicationContext()).load(prod.image).into(binding.adminProductImageIV);
        binding.adminProductPriceTV.setText(Float.toString(prod.price));
        binding.adminProductNameTV.setText(prod.title);
        binding.adminProdCountTV.setText(Integer.toString(prod.count));

        binding.deleteOrderBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productDB.productDao().deleteProduct(prod);
                Toast.makeText(AdminShowProductActivity.this, "deleted successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), AdminProductsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
        });

        binding.editOrderBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProductActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }
        });

        binding.productFeedbackTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FeedbacksActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }
        });

    }
}