package com.example.onlineshop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.SearchView;

import com.example.onlineshop.Adapters.AdminProductsAdapter;
import com.example.onlineshop.Adapters.ProductSearchAdapter;
import com.example.onlineshop.databinding.ActivityAdminProductsBinding;
import com.example.onlineshop.pojo.RoomDataBases.CategoryDB;
import com.example.onlineshop.pojo.Models.CategoryModel;
import com.example.onlineshop.pojo.RoomDataBases.ProductDB;
import com.example.onlineshop.pojo.Models.ProductModel;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminProductsActivity extends AppCompatActivity {
    ProductDB productDB;
    ActivityAdminProductsBinding binding;
    List<ProductModel> products;
    AdminProductsAdapter adapter;
    CategoryDB categoryDB;
    String[] categories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminProductsBinding.inflate(LayoutInflater.from(AdminProductsActivity.this));
        setContentView(binding.getRoot());

        productDB = ProductDB.getInstance(getApplicationContext());
        categoryDB = CategoryDB.getInstance(getApplicationContext());
        adapter = new AdminProductsAdapter(getApplicationContext());

        binding.adminLogoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });

        binding.bestSellBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), BestSellActivity.class);
                startActivity(intent);

                finish();
            }
        });

        binding.addIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), AddNewProductActivity.class);
                startActivity(intent);

                finish();
            }
        });

        new getList().execute();
        new getCategories().execute();

        binding.adminBarCodeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //method to scan barcode implemented below
                scanCode();
            }
        });


        //set the voice search button
        binding.adminVoiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //method to convert the speech to text implemented below
                speechToText();
            }
        });

        //set the searchView queryListener
        binding.adminSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //this method is used to do something when the user submit the search query <not used here>
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            //this method is used to something when any change happen to the text of the searchView
            @Override
            public boolean onQueryTextChange(String s) {

                //making adapter to hold the search data
                ProductSearchAdapter productSearchAdapter = new ProductSearchAdapter(getApplicationContext(), products);

                //this method filters the list to get the required products in the query
                productSearchAdapter.filter(s);

                // adding the filtered list to the adapter of the RecyclerView to show them
                adapter.setProducts(productSearchAdapter.filteredProductList);

                //notify the adapter that the dataset is changed
                adapter.notifyDataSetChanged();

                //this return is not required
                return false;
            }
        });
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
            binding.adminProductsRV.setAdapter(adapter);
            binding.adminProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private class getCategories extends AsyncTask<Void,Void,String[]>
    {
        @Override
        protected String[] doInBackground(Void... voids) {

            //check if the categories isn't stored in the database (which means that the data is got from the
            //API <first run on this device for this database version>)
            if(categoryDB.categoryDao().getAllCategories().isEmpty())
            {
                //so, if this is what happened get the categories from the products database
                categories = productDB.productDao().getAllCategories();
            }

            else
            {
                //get the categories from its database
                List<CategoryModel> cats = categoryDB.categoryDao().getAllCategories();

                //converting the categories list to string array to use later
                categories = cats.stream()
                        .map(CategoryModel::getName)
                        .toArray(String[]::new);
            }
            return categories;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);

            //set the search filter button
            binding.adminFilterBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    //creating a popup menu to attach it to this button
                    PopupMenu popup = new PopupMenu(getApplicationContext(), binding.adminFilterBTN, 0, 0, R.style.AppTheme_PopupMenu);

                    //adding the categories names as items for this menu
                    for (int i = 0; i < strings.length; i++)
                    {
                        popup.getMenu().add(Menu.NONE, i, Menu.NONE, strings[i]);
                    }

                    //set the item listener for each item
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            //update the products list to have only the products of chosen category from the database
                            products.clear();
                            products = productDB.productDao().getProductsByCategory(menuItem.getTitle().toString());

                            //updating the adapter with the new list of products and add it to the adapter
                            adapter = new AdminProductsAdapter(getApplicationContext());
                            adapter.setProducts(products);
                            binding.adminProductsRV.setAdapter(adapter);
                            return true;
                        }
                    });

                    //show the menu
                    popup.show();
                }
            });
        }
    }

    //this method is used to scan barcode
    private void scanCode()
    {
        //ScanOptions is a class used for bluetooth,image,barcode scanning, etc.
        ScanOptions options  =new ScanOptions();

        //enable Beep sound when it detects the code
        options.setBeepEnabled(true);

        //setting the orientation of the screen not locked (landscape, portrait)
        options.setOrientationLocked(false);

        //setting the capture activity to be CaptureActivity class which handle the camera view, barcode detection, etc.
        options.setCaptureActivity(CaptureActivity.class);

        //to launch the activity options and get the results
        barLauncher.launch(options);
    }

    //ActivityResultLauncher is a class used to launch activity and get its results
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->{

        //set the searchView query as the resulted text from the barcode scanning
        binding.adminSearch.setQuery(result.getContents(), false);
    });

    //this method is used to convert speech to text
    private void speechToText(){

        //checking that the permission for recording audio is enabled for this device
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            //if not enabled a request opened to ask for enabling it
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO},
                    200);
        }

        //initializing the recognizer activity and set the language to english and starting it
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Searching For...");
        startActivityForResult(intent, 200);
    }

    //what to do with the result of the recognizer activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the request for the activity success and the result is ok
        if(requestCode == 200 && resultCode == RESULT_OK){
            assert data != null;

            //get the resulted String
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            //add it to the search query
            binding.adminSearch.setQuery(result.get(0), false);
        }
    }
}