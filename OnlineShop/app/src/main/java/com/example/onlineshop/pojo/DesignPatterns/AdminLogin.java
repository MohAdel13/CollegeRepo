package com.example.onlineshop.pojo.DesignPatterns;

import android.content.Context;

public class AdminLogin implements Login{
    @Override
    public boolean authenticate(Context context, String username, String password) {
        return (username.equals("admin") && password.equals("admin"));
    }

    @Override
    public String getType() {
        return "admin";
    }
}
