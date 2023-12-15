package com.example.onlineshop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.Toast;

import com.example.onlineshop.Adapters.ProductSearchAdapter;
import com.example.onlineshop.Adapters.ProductsAdapter;
import com.example.onlineshop.databinding.ActivityProductsBinding;
import com.example.onlineshop.pojo.RoomDataBases.CategoryDB;
import com.example.onlineshop.pojo.Models.CategoryModel;
import com.example.onlineshop.pojo.API.ProductClient;
import com.example.onlineshop.pojo.RoomDataBases.ProductDB;
import com.example.onlineshop.pojo.Models.ProductModel;
import com.example.onlineshop.pojo.RoomDataBases.UserDB;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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
    String user;
    CategoryDB categoryDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //linking activity to its layout
        binding = ActivityProductsBinding.inflate(LayoutInflater.from(ProductsActivity.this));
        setContentView(binding.getRoot());

        //get the passed values from the above activity
        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        cat = intent.getStringExtra("category");

        //initializing the adapter of the recyclerView to hold the list of data
        adapter = new ProductsAdapter(getApplicationContext(),user);

        //get instances for the used databases
        productDB = ProductDB.getInstance(getApplicationContext());
        userDB = UserDB.getInstance(getApplicationContext());
        categoryDB = CategoryDB.getInstance(getApplicationContext());
        userDB = UserDB.getInstance(getApplicationContext());

        //get the user birthdate
        String userBirthDate = userDB.userDao().checkUser(user).get(0).birthDate;
        if(userBirthDate != null)
        {
            //if the birthdate of the user is stored we convert it to array of {day,month,year}
            String[] userBirth = userBirthDate.split(" / ");

            //get the date of today from Calendar and convert it to the same array
            int[] today = new int[3];
            Calendar c = Calendar.getInstance();
            today[0] = c.get(Calendar.DAY_OF_MONTH);
            today[1] = c.get(Calendar.MONTH) + 1;
            today[2] = c.get(Calendar.YEAR);

            //check if the day,month,year of the user birthdate = to that of today
            int i = 0;
            for(;i<3;i++)
            {
                if(today[i] != Integer.parseInt(userBirth[i]))
                {
                    break;
                }
            }

            //if today is the birthday of the user create alertDialog to say happy birthday for the user
            if(i == 3)
            {
                //create the alertDialog and give it the theme we created
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);

                //setting a layout we created for the alertDialog to the builder
                builder.setView(getLayoutInflater().inflate(R.layout.dialog_layout,null));

                //setting title for the dialog
                builder.setTitle("Hey There");

                //showing the dialog
                builder.show();
            }
        }

        // in case of first run to get data from API (Don't look at it, you will lose your mind)
/*<- click here if opened*/  if (productDB.productDao().getAllProducts().isEmpty()) {
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
                        Random random = new Random();
                        prod.get(i).count = random.nextInt(50) + 1;
                        prod.get(i).sale = 0;
                        prod.get(i).feedbacks = new ArrayList<>();
                        productDB.productDao().insertProduct(prod.get(i));
                    }
                    //Toast.makeText(ProductsActivity.this, "done.", Toast.LENGTH_SHORT).show();
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
        else
        {
            //executing getList and getCategories class methods which i will describe below
            new getList().execute();
            new getCategories().execute();

            //stop the preloader
            binding.progressBar.setVisibility(View.INVISIBLE);
        }

        //set the logout button
        binding.productsLogoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove any rememberMe stored value for any user in the database and then go to the login activity
                userDB.userDao().logout();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

                //the line below makes finish the current activity
                finish();
            }
        });

        //set the barcode button
        binding.productBarCodeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //method to scan barcode implemented below
                scanCode();
            }
        });


        //set the voice search button
        binding.productVoiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //method to convert the speech to text implemented below
                speechToText();
            }
        });

        //set the searchView queryListener
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //this method is used to do something when the user submit the search query <not used here>
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            //this method is used to something when any change happen to the text of the searchView
            @Override
            public boolean onQueryTextChange(String s) {

                //making adapter to hold the search data
                ProductSearchAdapter productSearchAdapter = new ProductSearchAdapter(getApplicationContext(), productsList);

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

        //set the cart button
        binding.productsCartBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        //set the Profile button
        binding.productsProfileBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
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
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(),result->{

        //set the searchView query as the resulted text from the barcode scanning
        binding.search.setQuery(result.getContents(), false);
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
            binding.search.setQuery(result.get(0), false);
        }
    }

    //this class extends AsyncTask class which is used to do multithreading to load the data from the database
        //while inflating the views as some of these views require some data.
    private class getList extends AsyncTask<Void,Void,List<ProductModel>>
    {
        @Override
        protected List<ProductModel> doInBackground(Void... voids) {

            //check that the user saved his favourite category to load its data direct or get all the products
                //from the database
            if (cat == null)
            {
                //get all products from the database
                productsList = productDB.productDao().getAllProducts();
            }

            else
            {
                //get the products of the favourite category only
                productsList = productDB.productDao().getProductsByCategory(cat);
            }
            return productsList;
        }

        @Override
        protected void onPostExecute(List<ProductModel> productModels) {
            super.onPostExecute(productModels);

            //set the productsList for the adapter and pass it to the recyclerView to show
            adapter.setProducts(productModels);
            binding.productsRV.setAdapter(adapter);
        }
    }

    //this class as the above one but for getting different data
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
            binding.productFilterBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    //creating a popup menu to attach it to this button
                    PopupMenu popup = new PopupMenu(getApplicationContext(), binding.productFilterBTN, 0, 0, R.style.AppTheme_PopupMenu);

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
                            productsList.clear();
                            productsList = productDB.productDao().getProductsByCategory(menuItem.getTitle().toString());

                            //updating the adapter with the new list of products and add it to the adapter
                            adapter = new ProductsAdapter(getApplicationContext(), user);
                            adapter.setProducts(productsList);
                            binding.productsRV.setAdapter(adapter);
                            return true;
                        }
                    });

                    //show the menu
                    popup.show();
                }
            });
        }
    }

}