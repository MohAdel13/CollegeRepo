package com.example.onlineshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshop.databinding.CartItemBinding;
import com.example.onlineshop.pojo.CartProductModel;
import com.example.onlineshop.pojo.ProductModel;
import com.example.onlineshop.pojo.UserDB;
import com.example.onlineshop.pojo.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.cartViewHolder>{

    UserDB userDB;
    String user;
    Context context;
    List<CartProductModel> cart;

    CartAdapter(String user, Context context)
    {
        this.context = context;
        this.user = user;
        userDB = UserDB.getInstance(context);
    }

    @NonNull
    @Override
    public CartAdapter.cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartItemBinding binding = CartItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new cartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.cartViewHolder holder, int position) {
        CartItemBinding binding = holder.holderBinding;

        Glide.with(context).load(cart.get(position).product.image).into(binding.cartItemIV);
        binding.cartCountTV.setText(Integer.toString(cart.get(position).count));
        binding.cartItemNameTV.setText(cart.get(position).product.title);
        binding.cartItemPriceTV.setText("USD " + Float.toString(cart.get(position).product.price));

    }

    @Override
    public int getItemCount() {
        return cart.size();
    }

    public void setCart(List<CartProductModel>cart)
    {
        this.cart = cart;
    }

    public class cartViewHolder extends RecyclerView.ViewHolder {
        CartItemBinding holderBinding;
        public cartViewHolder(@NonNull CartItemBinding binding) {
            super(binding.getRoot());
            this.holderBinding = binding;
            holderBinding.cartRemoveTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CartProductModel nowProduct = cart.get(getAdapterPosition());

                    UserModel nowUser = userDB.userDao().checkUser(user).get(0);

                    nowUser.cartItems.remove(getAdapterPosition());

                    nowUser.productsNames.remove(getAdapterPosition());

                    userDB.userDao().updateCart(nowUser);

                    cart.remove(getAdapterPosition());

                    CartAdapter.this.notifyItemRemoved(getAdapterPosition());
                }
            });

            holderBinding.cartPlusTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int oldCount = cart.get(getAdapterPosition()).count;

                    cart.get(getAdapterPosition()).count++;

                    UserModel nowUser = userDB.userDao().checkUser(user).get(0);

                    nowUser.cartItems.get(getAdapterPosition()).count++;

                    userDB.userDao().updateCart(nowUser);

                    holderBinding.cartCountTV.setText(Integer.toString(oldCount + 1));

                    Toast.makeText(context, "Cart Updated..", Toast.LENGTH_SHORT).show();
                }
            });

            holderBinding.cartMinusTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserModel nowUser = userDB.userDao().checkUser(user).get(0);
                    int oldCount = cart.get(getAdapterPosition()).count;
                    if(oldCount>1)
                    {
                        cart.get(getAdapterPosition()).count--;
                        nowUser.cartItems.get(getAdapterPosition()).count--;
                        holderBinding.cartCountTV.setText(Integer.toString(oldCount - 1));
                    }

                    else {
                        CartProductModel nowProduct = cart.get(getAdapterPosition());
                        cart.remove(nowProduct);
                        nowUser.productsNames.remove(getAdapterPosition());
                        nowUser.cartItems.remove(getAdapterPosition());
                        CartAdapter.this.notifyItemRemoved(getAdapterPosition());
                    }
                    userDB.userDao().updateCart(nowUser);
                    Toast.makeText(context, "Cart Updated..", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
