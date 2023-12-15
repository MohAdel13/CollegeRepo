package com.example.onlineshop.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.databinding.OrderItemLayoutBinding;
import com.example.onlineshop.pojo.Models.OrderModel;

import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.ViewHolder> {
    List<OrderModel> orders;
    Context context;
    public AdminOrderAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderItemLayoutBinding binding = OrderItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItemLayoutBinding binding = holder.holderBinding;

        binding.orderPriceTV.setText(Float.toString(orders.get(position).totalPrice));
        String info = "";
        for (int i=0;i<orders.get(position).products.size();i++)
        {
            info += Integer.toString(orders.get(position).countOfEachProduct.get(i)) + "*\n" +
                    orders.get(position).products.get(i).title + "\n\n";
        }

        binding.productsTV.setText(info);
        binding.cancelTV.setVisibility(View.INVISIBLE);
    }

    public void setOrders(List<OrderModel> orders) {
        this.orders = orders;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        OrderItemLayoutBinding holderBinding;
        public ViewHolder(@NonNull OrderItemLayoutBinding binding) {
            super(binding.getRoot());
            holderBinding = binding;
        }
    }
}
