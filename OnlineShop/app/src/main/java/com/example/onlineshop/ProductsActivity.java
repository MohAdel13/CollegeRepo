package com.example.onlineshop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricManager;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.onlineshop.databinding.ActivityProductsBinding;
import com.example.onlineshop.pojo.ProductClient;
import com.example.onlineshop.pojo.ProductDB;
import com.example.onlineshop.pojo.ProductModel;
import com.example.onlineshop.pojo.ProductSearchAdapter;
import com.example.onlineshop.pojo.UserDB;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductsActivity extends AppCompatActivity {
    ActivityProductsBinding binding;
    List<ProductModel> productsList;
    ProductsAdapter adapter;

    UserDB userDB;

    ProductDB productDB;

    String[] categories;

    String cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductsBinding.inflate(LayoutInflater.from(ProductsActivity.this));
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        adapter = new ProductsAdapter(getApplicationContext());

        productDB = ProductDB.getInstance(getApplicationContext());

        productsList = new ArrayList<>();

        // in case of first run to get data from API (Don't look at it, you will lose your mind)
        if (productDB.productDao().getAllProducts().isEmpty()) {
            Single<List<ProductModel>> products;
            products = ProductClient.getINSTANCE().productInterface.getAllProducts()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

            products.subscribe(new SingleObserver<List<ProductModel>>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onSuccess(@NonNull List<ProductModel> prod) {
                    for (int i = 0; i < prod.size(); i++) {
                        productDB.productDao().insertProduct(prod.get(i));
                    }
                    Toast.makeText(ProductsActivity.this, "done.", Toast.LENGTH_SHORT).show();
                    new getList().execute();
                    new getCategories().execute();

                    binding.progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "You Are Offline...", Toast.LENGTH_LONG).show();
                }
            });
        }

        // in case of another runs after saving the data to the database
        //TODO "This case is the required case for the project"
        else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            new getList().execute();
            new getCategories().execute();
            //Toast.makeText(this, "From Database", Toast.LENGTH_SHORT).show();
        }


        binding.productsLogoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDB = UserDB.getInstance(getApplicationContext());
                userDB.userDao().logout();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.productBarCodeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });

        binding.productVoiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speechToText();
            }
        });

        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ProductSearchAdapter productSearchAdapter = new ProductSearchAdapter(getApplicationContext()
                        , productsList);
                productSearchAdapter.filter(s);
                adapter.setProducts(productSearchAdapter.filteredProductList);
                adapter.notifyDataSetChanged();
                return false;
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
        binding.search.setQuery(result.getContents(), false);
    });

    private void speechToText(){

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO},
                    200);
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Searching For...");
        startActivityForResult(intent, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200 && resultCode == RESULT_OK){
            assert data != null;
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            binding.search.setQuery(result.get(0), false);
        }
    }

    private class getList extends AsyncTask<Void,Void,List<ProductModel>>
    {
        @Override
        protected List<ProductModel> doInBackground(Void... voids) {
            Intent intent = getIntent();
            cat = intent.getStringExtra("category");

            if (cat == null) {
                productsList = productDB.productDao().getAllProducts();
            } else {
                productsList = productDB.productDao().getProductsByCategory(cat);
            }
            return productsList;
        }

        @Override
        protected void onPostExecute(List<ProductModel> productModels) {
            super.onPostExecute(productModels);
            adapter.setProducts(productModels);
            binding.productsRV.setAdapter(adapter);
        }
    }

    private class getCategories extends AsyncTask<Void,Void,String[]>
    {

        @Override
        protected String[] doInBackground(Void... voids) {
            categories = productDB.productDao().getAllCategories();
            return categories;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            binding.productFilterBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(getApplicationContext(), binding.productFilterBTN, 0, 0, R.style.AppTheme_PopupMenu);
                    popup.getMenuInflater().inflate(R.menu.category_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            productsList.clear();
                            productsList = productDB.productDao().getProductsByCategory(menuItem.getTitle().toString());
                            adapter = new ProductsAdapter(getApplicationContext());
                            adapter.setProducts(productsList);
                            binding.productsRV.setAdapter(adapter);
                            return true;
                        }
                    });

                        popup.show();
                }
            });
        }
    }

}