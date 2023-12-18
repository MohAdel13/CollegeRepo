package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.onlineshop.databinding.ActivityOrderSubmitBinding;
import com.example.onlineshop.pojo.DesignPatterns.AccessControlProxy;
import com.example.onlineshop.pojo.Models.CartProductModel;
import com.example.onlineshop.pojo.Models.OrderModel;
import com.example.onlineshop.pojo.RoomDataBases.OrderDB;
import com.example.onlineshop.pojo.RoomDataBases.ProductDB;
import com.example.onlineshop.pojo.RoomDataBases.UserDB;
import com.example.onlineshop.pojo.Models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class OrderSubmitActivity extends AppCompatActivity {
    UserDB userDB;
    UserModel us;
    ActivityOrderSubmitBinding binding;
    ProductDB productDB;
    OrderDB orderDB;
    float totalPrice;
    float totalSale = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //linking the activity to its layout
        binding = ActivityOrderSubmitBinding.inflate(LayoutInflater.from(OrderSubmitActivity.this));
        setContentView(binding.getRoot());

        //taking instances of used databases
        productDB = ProductDB.getInstance(getApplicationContext());
        userDB = UserDB.getInstance(getApplicationContext());
        orderDB = OrderDB.getInstance(getApplicationContext());

        //get the passed value from the previous activity
        Intent intent = getIntent();
        String user = intent.getStringExtra("user");

        //get the user from the database
        us = userDB.userDao().checkUser(user).get(0);

        //execute getPrice class method which i will describe below
        new getPrice().execute();


        //setting the back button onClick
        binding.checkoutBackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Navigate to the cart activity
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);

                //pass a value with the username to the new activity
                intent.putExtra("user", user);

                //the below line makes the back activity cleared
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                //the below line clear this activity
                finish();
            }
        });


        //setting the submit button onClick
        binding.orderSubmitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //proxy design pattern lines to check if the credit card has enough money for the order
                AccessControlProxy proxy = new AccessControlProxy(us.card);
                boolean flag = true;
                if(binding.visaRB.isChecked())
                {
                    if(!proxy.validateTransaction(totalPrice + 1))
                    {
                        flag = false;
                    }
                }
                if(flag) {

                    //initializing a new order with the content of the order
                    OrderModel order = new OrderModel();
                    order.username = user;
                    order.state = 0;
                    order.totalPrice = totalPrice;
                    order.products = new ArrayList<>();
                    order.countOfEachProduct = new ArrayList<>();

                    for (int i = 0; i < us.cartItems.size(); i++) {
                        //updating the database of products by decreasing the count of the product
                        productDB.productDao().setCount(us.cartItems.get(i).product.title, us.cartItems.get(i).count);

                        //add the products to the the order
                        order.products.add(us.cartItems.get(i).product);

                        //add the count of each product to the order
                        order.countOfEachProduct.add(us.cartItems.get(i).count);

                        //increase the sold amount of each product in database
                        productDB.productDao().incrementSold(us.cartItems.get(i).count, us.cartItems.get(i).product.title);
                    }

                    //add the order to the database of orders
                    orderDB.orderDao().insertOrder(order);

                    //add the order to the database of the user
                    us.orders.add(order);

                    //clearing the user cart in database
                    us.productsNames.clear();
                    us.cartItems.clear();
                    userDB.userDao().updateCart(us);

                    //inform the user that the order is submitted
                    Toast.makeText(OrderSubmitActivity.this, "Order Submitted Successfully..", Toast.LENGTH_SHORT).show();

                    //navigate to the products activity
                    Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);

                    //pass values to the new activity with the username and his favourite category
                    intent.putExtra("user", user);
                    intent.putExtra("category", us.favCategory);

                    //the below line makes the back activity cleared
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    //the below line clears this activity
                    finish();
                }
                else{
                    //if the check of proxy design pattern is false
                    Toast.makeText(OrderSubmitActivity.this, "Sorry not Enough Credit..", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //initialize one of the radio buttons as checked
        binding.cashRB.setChecked(true);

        //makes the checking of one radio button cancels the other
        /*------------------------------------------------------------------------------*/
        binding.cashRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(binding.cashRB.isChecked())
                {
                    binding.visaRB.setChecked(false);
                }
            }
        });

        binding.visaRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(binding.visaRB.isChecked())
                {
                    binding.cashRB.setChecked(false);
                }
            }
        });
        /*------------------------------------------------------------------------------*/
    }


    //this internal class extends AsyncTask which is used for multithreading so we can get data from the database
        //in the same time of inflating the components as some of these components depends on the data we got.
    private class getPrice extends AsyncTask<Void,Void, Float>
    {
        @Override
        protected Float doInBackground(Void... voids)
        {

            //getting the total price of products in the cart * their amount
            List<CartProductModel> cart  = us.cartItems;
            float price = 0;
            for(int i=0; i<cart.size();i++)
            {
                price += cart.get(i).product.price * cart.get(i).count;
                totalSale += cart.get(i).product.sale * cart.get(i).count;
            }

            totalPrice = price - totalSale;
            return price;
        }

        @Override
        protected void onPostExecute(Float price) {
            super.onPostExecute(price);

            //writing the prices in textViews
            binding.orderTotalPriceTV.setText("USD " + Float.toString(price));
            binding.DeliveryPriceTV.setText("USD 1.0");
            binding.totalPriceTV.setText("USD "+ Float.toString(totalPrice+1));
            binding.saleTV.setText("USD "+ Float.toString(totalSale));
        }
    }
}