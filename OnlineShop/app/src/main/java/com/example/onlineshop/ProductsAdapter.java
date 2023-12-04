package com.example.onlineshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshop.databinding.ProductItemBinding;
import com.example.onlineshop.pojo.ProductDB;
import com.example.onlineshop.pojo.ProductModel;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>{
    List<ProductModel> products;
    Context context;
    ProductItemBinding binding;
    ProductDB db;

    ProductsAdapter(Context context)
    {
        this.context = context;
        db = ProductDB.getInstance(context);
    }

    @NonNull
    @Override
    public ProductsAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ProductItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ProductViewHolder holder, int position) {
        Glide.with(context).load(products.get(position).image).into(binding.productIV);
        binding.priceTV.setText("USD " + Float.toString(products.get(position).price));
        binding.prodTitleTV.setText(products.get(position).title);
        binding.rateTV.setText(Float.toString(products.get(position).rating.rate));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(List<ProductModel> products)
    {
        this.products = products;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ProductItemBinding binding;
        public ProductViewHolder(@NonNull ProductItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
