package com.example.onlineshop.pojo.DesignPatterns;

public class AdminLoginFactory implements LoginFactory{
    @Override
    public Login createLogin() {
        return new AdminLogin();
    }
}
