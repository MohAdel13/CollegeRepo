package com.example.onlineshop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlineshop.databinding.ActivityAdminCategoriesBinding;
import com.example.onlineshop.pojo.RoomDataBases.CategoryDB;
import com.example.onlineshop.pojo.Models.CategoryModel;

import java.util.List;

public class AdminCategoriesActivity extends AppCompatActivity {
    CategoryDB categoryDB;
    ActivityAdminCategoriesBinding binding;
    String[] categories;
    String selectedItem = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //linking activity to its layout
        binding = ActivityAdminCategoriesBinding.inflate(LayoutInflater.from(AdminCategoriesActivity.this));
        setContentView(binding.getRoot());

        //get instance from category database
        categoryDB = CategoryDB.getInstance(getApplicationContext());

        //get all the categories from database
        List<CategoryModel> cats = categoryDB.categoryDao().getAllCategories();

        //convert the categories to array to use it in the spinner(drop down menu)
        categories = cats.stream()
                .map(CategoryModel::getName)
                .toArray(String[]::new);

        //making an adapter to hold the array and use it in the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_spinner_dropdown_item, R.id.text1, categories);
        binding.adminCatSpinner.setAdapter(adapter);

        //setting the item select event for an item in spinner
        binding.adminCatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                //saving the selected item
                selectedItem = categories[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        //setting the delete button onClick
        binding.categoryDeleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //delete the selected category from the database
                categoryDB.categoryDao().deleteCategory(selectedItem);

                //inform the admin that the category is deleted
                Toast.makeText(getApplicationContext(),"Deleted Successfully..", Toast.LENGTH_SHORT).show();
            }
        });

        //setting the add button onClick
        binding.categoryAddBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //initializing an alert dialog for the admin to take the new category name
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminCategoriesActivity.this, R.style.MyDialogTheme);
                View v = getLayoutInflater().inflate(R.layout.new_category_dialog,null);
                builder.setView(v);
                builder.setTitle("Enter new category:");

                //make a save word button to do the saving action
                builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //linking the editText used in alertDialog
                        EditText dialogET = v.findViewById(R.id.dialogET);

                        //initializing a new category and give it the name that the admin put
                        CategoryModel category = new CategoryModel();
                        category.name = dialogET.getText().toString();

                        //insert the new category to the database
                        categoryDB.categoryDao().insertCategory(category);

                        //inform the admin that the category is added
                        Toast.makeText(AdminCategoriesActivity.this, "Added Successfully..", Toast.LENGTH_SHORT).show();
                    }
                });

                //showing the dialog
                builder.show();
            }
        });
    }
}