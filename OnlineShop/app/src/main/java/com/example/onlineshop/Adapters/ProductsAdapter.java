package com.example.onlineshop.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshop.SingleProductActivity;
import com.example.onlineshop.databinding.ProductItemBinding;
import com.example.onlineshop.pojo.Models.ProductModel;

import java.util.List;

//adapter to carry data for the products activity
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {
    private List<ProductModel> products;
    private Context context;
    private String user;

    public ProductsAdapter(Context context, String user) {
        //initializing the class attributes
        this.context = context;
        this.user = user;
    }

    //onCreate is the first method is done when the class is opened
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        //linking the holder of the adapter to the layout of product item
        ProductItemBinding binding = ProductItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    //for drawing views of the recyclerView
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductItemBinding binding = holder.binding;

        //Glide is a package used to draw pictures as the products are stored as its uri
        Glide.with(context).load(products.get(position).image).into(binding.productIV);
        binding.priceTV.setText("USD " + Float.toString(products.get(position).price));
        binding.prodTitleTV.setText(products.get(position).title);
        binding.rateTV.setText(Float.toString(products.get(position).rating.rate));

        if(products.get(position).sale != 0)
        {
            binding.prodSaleTV.setText("Sale" + Float.toString(products.get(position).sale));
        }
        else {
            binding.prodSaleTV.setVisibility(View.INVISIBLE);
        }
    }

    //used to get the count of productList of the adapter
    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    //used to set the productsList of the adapter
    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }

    //view holder is a class used to hold the views of each item in the adapter
    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ProductItemBinding binding;
        public ProductViewHolder(@NonNull ProductItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            //set the product item click
            binding.prodItemCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), SingleProductActivity.class);
                    intent.putExtra("product", products.get(getAdapterPosition()).title);
                    intent.putExtra("user", user);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
