package com.example.onlineshop.pojo.DesignPatterns;

public class UserLoginFactory implements LoginFactory{
    @Override
    public Login createLogin() {
        return new UserLogin();
    }
}
