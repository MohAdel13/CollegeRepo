package com.example.onlineshop;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.onlineshop.pojo.Models.ProductModel;

import java.util.ArrayList;
import java.util.List;

//Custom adapter for searchView
public class ProductSearchAdapter extends BaseAdapter {

    private Context context;
    private List<ProductModel> originalProductList;
    public List<ProductModel> filteredProductList;

    public ProductSearchAdapter(Context context, List<ProductModel> productList) {
        //initializing the attributes of the class
        this.context = context;
        this.originalProductList = productList;
        this.filteredProductList = new ArrayList<>(productList);
    }

    //used to know the count of the listOfProducts
    @Override
    public int getCount() {
        return originalProductList.size();
    }

    //used to get an element from the list
    @Override
    public Object getItem(int i) {
        return originalProductList.get(i);
    }

    //Ma7tota 8sb 3n 3een elgmee3 mlha4 lazma
    @Override
    public long getItemId(int i) {
        return 0;
    }

    //IDK bsara7a bs ma7tota 8sb bardo
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    //this is the important method which we give the string we are searching for and it stores the list of items
        //based on this string
    public void filter(String query) {
        filteredProductList.clear();

        //if the string is empty it gives the whole original list
        if (TextUtils.isEmpty(query))
        {
            filteredProductList.addAll(originalProductList);
        }
        else
        {
            //in summary it converts the string to lowercase and the name and description of all products too
                //and trying to find any product contains this string

            query = query.toLowerCase();
            for (ProductModel product : originalProductList) {
                if (product.title.toLowerCase().contains(query)
                        || product.description.toLowerCase().contains(query)) {
                    filteredProductList.add(product);
                }
            }
        }

        //notify the adapter that the dataset is changed
        notifyDataSetChanged();
    }
}
