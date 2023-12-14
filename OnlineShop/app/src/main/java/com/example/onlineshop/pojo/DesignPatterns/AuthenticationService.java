package com.example.onlineshop.pojo.DesignPatterns;

import android.content.Context;

import com.example.onlineshop.pojo.Models.UserModel;
import com.example.onlineshop.pojo.RoomDataBases.UserDB;

import java.util.List;

public class AuthenticationService {
    UserDB userDB;
    public AuthenticationService(Context context)
    {
        userDB = UserDB.getInstance(context);
    }

    public UserModel authenticate(String username, String password)
    {

        List<UserModel> user = userDB.userDao().getUser(username, password);
        if(user.isEmpty())
        {
            return null;
        }
        return user.get(0);
    }
}