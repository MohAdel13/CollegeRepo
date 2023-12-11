package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.onlineshop.databinding.ActivityAdminPanelBinding;
import com.example.onlineshop.pojo.ProductDB;
import com.example.onlineshop.pojo.ProductModel;

import java.util.List;

public class AdminPanelActivity extends AppCompatActivity {
    ProductDB productDB;
    ActivityAdminPanelBinding binding;
    List<ProductModel> products;
    AdminProductsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminPanelBinding.inflate(LayoutInflater.from(AdminPanelActivity.this));
        setContentView(binding.getRoot());

        productDB = ProductDB.getInstance(getApplicationContext());
        adapter = new AdminProductsAdapter(getApplicationContext());

        binding.AdminLogoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });

        new getList().execute();
    }

    private class getList extends AsyncTask<Void,Void,List<ProductModel>>
    {
        @Override
        protected List<ProductModel> doInBackground(Void... voids) {

            products = productDB.productDao().getAllProducts();
            return products;
        }

        @Override
        protected void onPostExecute(List<ProductModel> productModels) {
            super.onPostExecute(productModels);

            //set the productsList for the adapter and pass it to the recyclerView to show
            adapter.setProducts(productModels);
            binding.productsRV.setAdapter(adapter);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}