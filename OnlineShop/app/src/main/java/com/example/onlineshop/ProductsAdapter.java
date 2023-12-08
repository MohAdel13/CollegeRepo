package com.example.onlineshop;

import android.content.Context;
import android.content.Intent;
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

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {
    private List<ProductModel> products;
    private Context context;
    private ProductDB db;
    private String user;

    ProductsAdapter(Context context, String user) {
        this.context = context;
        db = ProductDB.getInstance(context);
        this.user = user;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ProductItemBinding binding = ProductItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductItemBinding binding = holder.binding;

        Glide.with(context).load(products.get(position).image).into(binding.productIV);
        binding.priceTV.setText("USD " + Float.toString(products.get(position).price));
        binding.prodTitleTV.setText(products.get(position).title);
        binding.rateTV.setText(Float.toString(products.get(position).rating.rate));
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ProductItemBinding binding;

        public ProductViewHolder(@NonNull ProductItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

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
