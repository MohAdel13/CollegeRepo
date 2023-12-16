package com.example.onlineshop.pojo.Models;

import com.example.onlineshop.pojo.DesignPatterns.CreditCardModelInterface;

public class CreditCardModel implements CreditCardModelInterface {
    public int id;
    public float credit;

    @Override
    public boolean validateTransaction(float amount) {
        return credit >= amount;
    }
}
