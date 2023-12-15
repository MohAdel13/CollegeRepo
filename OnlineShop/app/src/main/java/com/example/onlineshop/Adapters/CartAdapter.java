package com.example.onlineshop.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshop.databinding.CartItemBinding;
import com.example.onlineshop.pojo.Models.CartProductModel;
import com.example.onlineshop.pojo.RoomDataBases.ProductDB;
import com.example.onlineshop.pojo.RoomDataBases.UserDB;
import com.example.onlineshop.pojo.Models.UserModel;

import java.util.List;

//class adapter for the cart activity recyclerView to hold the products of the cart
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.cartViewHolder>{
    UserDB userDB;
    String user;
    Context context;
    List<CartProductModel> cart;
    ProductDB productDB;

    public CartAdapter(String user, Context context)
    {
        //initializing the class attributes and getting instances of the used databases
        this.context = context;
        this.user = user;
        userDB = UserDB.getInstance(context);
        productDB = ProductDB.getInstance(context);
    }


    //onCreate method which starts right when the activity is opened
    @NonNull
    @Override
    public CartAdapter.cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //linking the holder with the item in layout
        CartItemBinding binding = CartItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new cartViewHolder(binding);
    }


    //onBind method which draw the adapter items
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.cartViewHolder holder, int position) {
        CartItemBinding binding = holder.holderBinding;


        //writing on the holder items depending on the data in the cart
        /*--------------------------------------------------------------------------------*/
        binding.cartItemNameTV.setText(cart.get(position).product.title);
        binding.cartItemPriceTV.setText("USD " + Float.toString(cart.get(position).product.price));

        Glide.with(context).load(cart.get(position).product.image).into(binding.cartItemIV);
        //Glide is package used to draw pictures as the pictures of products is stored as its uri

        //getting the count available in database of this product and put the value he choose or the max available
        List<Integer> availableCount = productDB.productDao().getCount(cart.get(position).product.title);
        if(availableCount.isEmpty())
        {
            binding.cartCountTV.setText("0");
        }
        else if(cart.get(position).count <= availableCount.get(0))
        {
            binding.cartCountTV.setText(Integer.toString(cart.get(position).count));
        }
        else
        {
            binding.cartCountTV.setText(Integer.toString(availableCount.get(0)));
            cart.get(position).count = availableCount.get(0);
            UserModel nowUser = userDB.userDao().checkUser(user).get(0);
            nowUser.cartItems = cart;
            userDB.userDao().updateCart(nowUser);
        }
        /*--------------------------------------------------------------------------------*/

    }

    //used to know the size of the list used in the adapter
    @Override
    public int getItemCount() {
        return cart.size();
    }

    //method used to set the list used in the adapter
    public void setCart(List<CartProductModel>cart)
    {
        this.cart = cart;
    }


    //this internal class is used to hold the items of the design
    public class cartViewHolder extends RecyclerView.ViewHolder {
        CartItemBinding holderBinding;
        public cartViewHolder(@NonNull CartItemBinding binding) {
            super(binding.getRoot());
            this.holderBinding = binding;


            //setting the remove button in each item card
            holderBinding.cartRemoveTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //updating the database after removing the element from the cart
                    UserModel nowUser = userDB.userDao().checkUser(user).get(0);
                    nowUser.cartItems.remove(getAdapterPosition());
                    nowUser.productsNames.remove(getAdapterPosition());
                    userDB.userDao().updateCart(nowUser);
                    cart.remove(getAdapterPosition());


                    //notify the adapter that there's item removed
                    CartAdapter.this.notifyItemRemoved(getAdapterPosition());
                }
            });


            //setting the button of plus count to add more elements
            holderBinding.cartPlusTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int availableCount = productDB.productDao().getCount(cart.get(getAdapterPosition()).product.title).get(0);
                    int oldCount = cart.get(getAdapterPosition()).count;
                    //checking the available count of the product by checking the difference between the
                        //count of the product in the database and the count that the user already have in cart
                    if(availableCount > (oldCount + 1))
                    {
                        //adding more element to the cart and update the database
                        cart.get(getAdapterPosition()).count++;
                        UserModel nowUser = userDB.userDao().checkUser(user).get(0);
                        nowUser.cartItems.get(getAdapterPosition()).count++;
                        userDB.userDao().updateCart(nowUser);
                        holderBinding.cartCountTV.setText(Integer.toString(oldCount + 1));
                        Toast.makeText(context, "Cart Updated..", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context, "Sorry, This Product is Out of Stock", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //setting the button of minus to decrease the count of this product
            holderBinding.cartMinusTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserModel nowUser = userDB.userDao().checkUser(user).get(0);
                    int oldCount = cart.get(getAdapterPosition()).count;

                    //decrease the count
                    if(oldCount>1)
                    {
                        cart.get(getAdapterPosition()).count--;
                        nowUser.cartItems.get(getAdapterPosition()).count--;
                        holderBinding.cartCountTV.setText(Integer.toString(oldCount - 1));
                    }

                    //if the count now equals zero it's time to remove it
                    else
                    {
                        CartProductModel nowProduct = cart.get(getAdapterPosition());
                        cart.remove(nowProduct);
                        nowUser.productsNames.remove(getAdapterPosition());
                        nowUser.cartItems.remove(getAdapterPosition());
                        CartAdapter.this.notifyItemRemoved(getAdapterPosition());
                    }

                    //update the database after the decreasing operation
                    userDB.userDao().updateCart(nowUser);
                    Toast.makeText(context, "Cart Updated..", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
