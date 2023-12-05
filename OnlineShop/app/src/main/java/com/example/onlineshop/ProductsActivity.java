package com.example.onlineshop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.onlineshop.databinding.ActivityProductsBinding;
import com.example.onlineshop.pojo.ProductClient;
import com.example.onlineshop.pojo.ProductModel;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

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

        Intent intent = getIntent();
        String cat = intent.getStringExtra("category");

        binding = ActivityProductsBinding.inflate(LayoutInflater.from(ProductsActivity.this));
        setContentView(binding.getRoot());

        adapter = new ProductsAdapter(getApplicationContext());

        binding.productBarCodeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });

        Single<List<ProductModel>> products;
        if(cat == null)
        {
             products = ProductClient.getINSTANCE().productInterface.getAllProducts()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }

        else
        {
            products = ProductClient.getINSTANCE().productInterface.getProductsByCategory(cat)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }

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

    private void scanCode()
    {
        ScanOptions options  =new ScanOptions();
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureActivity.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(),result->{
        binding.search.setQuery(result.getContents(), true);
    });
}