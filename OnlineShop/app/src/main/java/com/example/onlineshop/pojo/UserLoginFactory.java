package com.example.onlineshop.pojo;

public class UserLoginFactory implements LoginFactory{
    @Override
    public Login createLogin() {
        return new UserLogin();
    }
}
