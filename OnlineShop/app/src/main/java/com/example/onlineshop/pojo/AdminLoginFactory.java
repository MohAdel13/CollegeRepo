package com.example.onlineshop.pojo;

public class AdminLoginFactory implements LoginFactory{
    @Override
    public Login createLogin() {
        return new AdminLogin();
    }
}
