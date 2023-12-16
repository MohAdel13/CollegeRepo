package com.example.onlineshop.pojo.DesignPatterns;

public class LoginFactory {
    public Login determineLoginFactory(String username)
    {
        if(username.equals("admin")){
            return new AdminLogin();
        }

        else {
            return new UserLogin();
        }
    }
}
