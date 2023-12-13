package com.example.onlineshop.pojo.DesignPatterns;

import android.content.Context;

import com.example.onlineshop.pojo.Models.UserModel;

public class UserLogin implements Login{
    private UserModel user;
    @Override
    public boolean authenticate(Context context, String username, String password) {
        AuthenticationService authenticationService = new AuthenticationService(context);
        user = authenticationService.authenticate(username, password);
        return user != null;
    }

    @Override
    public String getType() {
        return "user";
    }

    public UserModel getUserModel()
    {
        return user;
    }
}
