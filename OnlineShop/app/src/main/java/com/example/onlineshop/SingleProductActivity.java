package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.onlineshop.databinding.ActivitySingleProductBinding;
import com.example.onlineshop.pojo.Models.CartProductModel;
import com.example.onlineshop.pojo.RoomDataBases.ProductDB;
import com.example.onlineshop.pojo.Models.ProductModel;
import com.example.onlineshop.pojo.Models.RatingModel;
import com.example.onlineshop.pojo.RoomDataBases.UserDB;
import com.example.onlineshop.pojo.Models.UserModel;

import java.util.List;

public class SingleProductActivity extends AppCompatActivity {
    ActivitySingleProductBinding binding;
    String product;
    String user;
    ProductDB productDB;
    ProductModel prod;
    UserDB userDB;
    UserModel us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //for linking activity to its layout
        binding = ActivitySingleProductBinding.inflate(LayoutInflater.from(SingleProductActivity.this));
        setContentView(binding.getRoot());

        //get the values passed from the above activity
        Intent intent = getIntent();
        product = intent.getStringExtra("product");
        user = intent.getStringExtra("user");

        //get instance for used databases
        productDB = ProductDB.getInstance(getApplicationContext());
        userDB = UserDB.getInstance(getApplicationContext());

        //get the product and the user from the database
        prod = productDB.productDao().getProduct(product).get(0);
        us = userDB.userDao().checkUser(user).get(0);

        //Glide is a package used to draw pictures as the picture of each product is stored as its uri
        Glide.with(getApplicationContext()).load(prod.image).into(binding.productImageIV);

        //writing the product information to the activity views
        binding.productNameTV.setText(prod.title);
        binding.productCatTV.setText(prod.category);
        binding.productPriceTV.setText("USD " + Float.toString(prod.price));
        binding.productDesTV.setText(prod.description);
        binding.prodCountTV.setText(Integer.toString(prod.count));

        //set rating buttons
        /*-------------------------------------------------------------------------------*/
        binding.rate1StarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //converting the outlined star to solid one to look like its pressed
                binding.rate1StarBTN.setImageDrawable(getDrawable(R.drawable.baseline_star_rate_26));

                //perform star click method implemented below
                starClick(1);
            }
        });

        //as above
        binding.rate2StarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.rate1StarBTN.setImageDrawable(getDrawable(R.drawable.baseline_star_rate_26));
                binding.rate2StarBTN.setImageDrawable(getDrawable(R.drawable.baseline_star_rate_26));
                starClick(2);
            }
        });

        //as above
        binding.rate3StarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.rate1StarBTN.setImageDrawable(getDrawable(R.drawable.baseline_star_rate_26));
                binding.rate2StarBTN.setImageDrawable(getDrawable(R.drawable.baseline_star_rate_26));
                binding.rate3StarBTN.setImageDrawable(getDrawable(R.drawable.baseline_star_rate_26));
                starClick(3);
            }
        });

        //as above
        binding.rate4StarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.rate1StarBTN.setImageDrawable(getDrawable(R.drawable.baseline_star_rate_26));
                binding.rate2StarBTN.setImageDrawable(getDrawable(R.drawable.baseline_star_rate_26));
                binding.rate3StarBTN.setImageDrawable(getDrawable(R.drawable.baseline_star_rate_26));
                binding.rate4StarBTN.setImageDrawable(getDrawable(R.drawable.baseline_star_rate_26));
                starClick(4);
            }
        });

        //as above
        binding.rate5StarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.rate1StarBTN.setImageDrawable(getDrawable(R.drawable.baseline_star_rate_26));
                binding.rate2StarBTN.setImageDrawable(getDrawable(R.drawable.baseline_star_rate_26));
                binding.rate3StarBTN.setImageDrawable(getDrawable(R.drawable.baseline_star_rate_26));
                binding.rate4StarBTN.setImageDrawable(getDrawable(R.drawable.baseline_star_rate_26));
                binding.rate5StarBTN.setImageDrawable(getDrawable(R.drawable.baseline_star_rate_26));
                starClick(5);

            }
        });
        /*-------------------------------------------------------------------------------*/

        //checking if the user has this product in his cart
        List<String> products = us.productsNames;
        if (products.contains(product))
        {
            //enable the button of add to cart and show the count of this product in the cart
            binding.addToCartBTN.setBackgroundColor(getColor(R.color.orange));

            //checking if the products count in the user cart database is available or not
            if(us.cartItems.get(products.indexOf(prod.title)).count > prod.count)
            {
                binding.countTV.setText(Integer.toString(prod.count));
                List<CartProductModel> cart = us.cartItems;
                cart.get(products.indexOf(prod.title)).count = prod.count;
                UserModel nowUser = userDB.userDao().checkUser(user).get(0);
                nowUser.cartItems = cart;
                userDB.userDao().updateCart(nowUser);
            }
            else {
                binding.countTV.setText(Integer.toString(us.cartItems.get(products.indexOf(prod.title)).count));
            }
        }

        //set the plus btn
        binding.plusTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if the product has more amount to add to add to cart
                int nowTotalCount = Integer.parseInt(binding.countTV.getText().toString()) + 1;
                if(prod.count >= nowTotalCount)
                {
                    if (nowTotalCount == 1) {
                        //if its the last element disable the add to cart btn
                        binding.addToCartBTN.setBackgroundColor(getColor(R.color.orange));
                    }

                    //show the new count
                    binding.countTV.setText(Integer.toString(nowTotalCount));
                }
                else
                {
                    //notify the user when no more amount in the stock
                    Toast.makeText(SingleProductActivity.this, "Sorry, This Product is Out of Stock", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //set the minus btn
        binding.minusTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(binding.countTV.getText().toString());
                if(count > 0)
                {
                    //decrease the amount and show the new count
                    count--;
                    binding.countTV.setText(Integer.toString(count));

                    //if count now is 0 disable the addToCart button
                    if(count == 0)
                    {
                        binding.addToCartBTN.setBackgroundColor(getColor(R.color.lightGrey));
                    }
                }
            }
        });

        //set the add to cart button
        binding.addToCartBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check if the chosen amount > 0
                int newCount = Integer.parseInt(binding.countTV.getText().toString());
                if(newCount>0)
                {
                    //check if the user already has this product in his cart
                    List<String> products = us.productsNames;
                    if (products.contains(product))
                    {
                        //update the count of the product for the cart
                        us.cartItems.get(products.indexOf(product)).count = newCount;
                    }
                    else
                    {
                        //add this product to the user database and its amount
                        CartProductModel cartProduct = new CartProductModel();
                        cartProduct.count = newCount;
                        cartProduct.product = prod;
                        us.cartItems.add(cartProduct);
                        us.productsNames.add(product);
                    }

                    //update the amount with the new amount in the user database
                    userDB.userDao().updateCart(us);
                    Toast.makeText(SingleProductActivity.this, "Added to Cart..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //starClick method used for rating a product
    private void starClick(float rate)
    {
        //get the product old rate
        RatingModel oldRate = prod.rating;
        RatingModel NewRate = new RatingModel();

        //add one to the count of the rate
        NewRate.count = oldRate.count + 1;

        //add his rate to the equation
        NewRate.rate = ((oldRate.rate * oldRate.count) + rate) / NewRate.count;
        prod.rating = NewRate;

        //update the database with the new rate
        productDB.productDao().updateRate(prod);
    }
}