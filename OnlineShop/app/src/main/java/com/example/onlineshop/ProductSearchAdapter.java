package com.example.onlineshop;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.onlineshop.pojo.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class ProductSearchAdapter extends BaseAdapter {

    private Context context;
    private List<ProductModel> originalProductList;
    public List<ProductModel> filteredProductList;

    public ProductSearchAdapter(Context context, List<ProductModel> productList) {
        this.context = context;
        this.originalProductList = productList;
        this.filteredProductList = new ArrayList<>(productList);
    }
    @Override
    public int getCount() {
        return originalProductList.size();
    }

    @Override
    public Object getItem(int i) {
        return originalProductList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    public void filter(String query) {
        filteredProductList.clear();

        if (TextUtils.isEmpty(query)) {
            filteredProductList.addAll(originalProductList);
        } else {
            query = query.toLowerCase();

            for (ProductModel product : originalProductList) {
                if (product.title.toLowerCase().contains(query)
                        || product.description.toLowerCase().contains(query)) {
                    filteredProductList.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }
}
