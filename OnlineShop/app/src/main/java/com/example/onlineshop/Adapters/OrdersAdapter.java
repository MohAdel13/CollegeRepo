package com.example.onlineshop.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.databinding.OrderItemLayoutBinding;
import com.example.onlineshop.pojo.Models.OrderModel;
import com.example.onlineshop.pojo.Models.UserModel;
import com.example.onlineshop.pojo.RoomDataBases.OrderDB;
import com.example.onlineshop.pojo.RoomDataBases.UserDB;

import java.util.List;


public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ordersHolder>{
    String user;
    UserModel us;
    List<OrderModel> orders;
    UserDB userDB;
    OrderDB orderDB;
    Context context;

    public OrdersAdapter(String user, Context context)
    {
        //initializing the class attributes and getting instances of the used databases
        this.context = context;
        this.user = user;
        userDB = UserDB.getInstance(context);
        orderDB = OrderDB.getInstance(context);

        us = userDB.userDao().checkUser(user).get(0);
    }

    @NonNull
    @Override
    public OrdersAdapter.ordersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //linking the holder with the item in layout
        OrderItemLayoutBinding binding = OrderItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ordersHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ordersHolder holder, int position) {
        OrderItemLayoutBinding binding = holder.holderBinding;

        binding.orderPriceTV.setText(Float.toString(orders.get(position).totalPrice));
        String info = "";
        for (int i=0;i<orders.get(position).products.size();i++)
        {
            info += Integer.toString(orders.get(position).countOfEachProduct.get(i)) + "*\n" +
                    orders.get(position).products.get(i).title + "\n\n";
        }

        binding.productsTV.setText(info);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setOrders(List<OrderModel> orders){
        this.orders = orders;
    }

    public class ordersHolder extends RecyclerView.ViewHolder {
        OrderItemLayoutBinding holderBinding;
        public ordersHolder(@NonNull OrderItemLayoutBinding binding) {
            super(binding.getRoot());
            this.holderBinding = binding;

            binding.cancelTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    us.orders.remove(getAdapterPosition());
                    userDB.userDao().updateCart(us);
                    orderDB.orderDao().deleteOrder(orders.get(getAdapterPosition()));
                    Toast.makeText(context, "Order is Deleted", Toast.LENGTH_SHORT).show();
                    orders.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });
        }
    }
}
