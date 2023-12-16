package com.example.onlineshop.pojo.DesignPatterns;

import android.content.Context;

public class AdminLogin implements Login{
    @Override
    public boolean Authenticate(String user, String pass, Context context) {
        return (pass.equals("admin") && user.equals("admin"));
    }

    @Override
    public String getType() {
        return "admin";
    }
}
