package com.example.onlineshop.pojo.DesignPatterns;

import android.content.Context;

import com.example.onlineshop.pojo.Models.UserModel;
import com.example.onlineshop.pojo.RoomDataBases.UserDB;

import java.util.List;

public class UserLogin implements Login{
    private UserModel us;
    @Override
    public boolean Authenticate(String user, String pass, Context context) {
        UserDB userDB = UserDB.getInstance(context);

        List<UserModel> userGot = userDB.userDao().getUser(user, pass);
        if(!userGot.isEmpty())
        {
            us = userGot.get(0);
            return true;
        }
        return false;
    }

    @Override
    public String getType() {
        return "user";
    }

    public UserModel getUser()
    {
        return us;
    }
}
