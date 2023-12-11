package com.example.onlineshop.pojo;

import android.content.Context;

import java.util.List;

public class AuthenticationService {
    UserDB userDB;
    public AuthenticationService(Context context)
    {
       userDB = UserDB.getInstance(context);
    }

    public UserModel authenticate(String username, String password)
    {
        UserModel user = userDB.userDao().getUser(username, password).get(0);
        return user;
    }
}
