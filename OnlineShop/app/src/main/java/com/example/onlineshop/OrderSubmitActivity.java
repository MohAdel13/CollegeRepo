package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ContentInfoCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.onlineshop.databinding.ActivityOrderSubmitBinding;
import com.example.onlineshop.pojo.CartProductModel;
import com.example.onlineshop.pojo.ProductModel;
import com.example.onlineshop.pojo.UserDB;
import com.example.onlineshop.pojo.UserModel;

import java.util.List;

public class OrderSubmitActivity extends AppCompatActivity {
    UserDB userDB;
    UserModel us;
    ActivityOrderSubmitBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOrderSubmitBinding.inflate(LayoutInflater.from(OrderSubmitActivity.this));
        setContentView(binding.getRoot());

        userDB = UserDB.getInstance(getApplicationContext());
        Intent intent = getIntent();
        String user = intent.getStringExtra("user");
        us = userDB.userDao().checkUser(user).get(0);
        new getPrice().execute();
        //Toast.makeText(this, user, Toast.LENGTH_SHORT).show();

        binding.checkoutBackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                intent.putExtra("user", user);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        binding.orderSubmitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OrderSubmitActivity.this, "Order Submitted Successfully..", Toast.LENGTH_SHORT).show();
                us.productsNames.clear();
                us.cartItems.clear();
                userDB.userDao().updateCart(us);

                Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("category", us.favCategory);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        binding.cashRB.setChecked(true);

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
    }


    private class getPrice extends AsyncTask<Void,Void, Float>
    {
        @Override
        protected Float doInBackground(Void... voids) {
            List<CartProductModel> cart  = us.cartItems;
            float price = 0;
            for(int i=0; i<cart.size();i++)
            {
                price += cart.get(i).product.price * cart.get(i).count;
            }
            return price;
        }

        @Override
        protected void onPostExecute(Float price) {
            super.onPostExecute(price);
            binding.orderTotalPriceTV.setText("USD " + Float.toString(price));
            binding.DeliveryPriceTV.setText("USD 1.0");
            binding.totalPriceTV.setText("USD "+ Float.toString(price+1));
        }
    }
}