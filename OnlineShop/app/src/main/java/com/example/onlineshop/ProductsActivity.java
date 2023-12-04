package com.example.onlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.onlineshop.databinding.ActivityProductsBinding;
import com.example.onlineshop.pojo.ProductClient;
import com.example.onlineshop.pojo.ProductModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity {
    ActivityProductsBinding binding;
    List<ProductModel> productsList;

    ProductsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductsBinding.inflate(LayoutInflater.from(ProductsActivity.this));
        setContentView(binding.getRoot());

        adapter = new ProductsAdapter(getApplicationContext());

        Single<List<ProductModel>> products = ProductClient.getINSTANCE().productInterface.getAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        products.subscribe(new SingleObserver<List<ProductModel>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                binding.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(@NonNull List<ProductModel> prod) {
                productsList = prod;
                adapter.setProducts(productsList);
                binding.productsRV.setAdapter(adapter);
                binding.progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"You Are Offline...", Toast.LENGTH_LONG).show();
            }
        });
    }
}