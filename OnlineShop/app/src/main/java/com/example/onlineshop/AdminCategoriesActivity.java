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
    String selectedItem =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminCategoriesBinding.inflate(LayoutInflater.from(AdminCategoriesActivity.this));
        setContentView(binding.getRoot());

        categoryDB = CategoryDB.getInstance(getApplicationContext());

        //get all the categories from database
        List<CategoryModel> cats = categoryDB.categoryDao().getAllCategories();

        //convert the categories to array to use it in the spinner
        categories = cats.stream()
                .map(CategoryModel::getName)
                .toArray(String[]::new);

        //making an adapter to hold the array and use it in the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_spinner_dropdown_item, R.id.text1, categories);
        binding.adminCatSpinner.setAdapter(adapter);

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

        binding.categoryDeleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryDB.categoryDao().deleteCategory(selectedItem);
                Toast.makeText(getApplicationContext(),"Deleted Successfully..", Toast.LENGTH_SHORT).show();
            }
        });

        binding.categoryAddBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminCategoriesActivity.this, R.style.MyDialogTheme);

                View v = getLayoutInflater().inflate(R.layout.new_category_dialog,null);

                builder.setView(v);

                builder.setTitle("Enter new category:");

                builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EditText dialogET = v.findViewById(R.id.dialogET);
                        CategoryModel category = new CategoryModel();
                        category.name = dialogET.getText().toString();
                        categoryDB.categoryDao().insertCategory(category);
                        Toast.makeText(AdminCategoriesActivity.this, "Added Successfully..", Toast.LENGTH_SHORT).show();
                    }
                });

                //showing the dialog
                builder.show();
            }
        });
    }
}