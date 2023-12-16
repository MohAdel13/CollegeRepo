package com.example.onlineshop.pojo.DesignPatterns;

import com.example.onlineshop.pojo.Models.CreditCardModel;

public class AccessControlProxy implements CreditCardModelInterface {
    private final CreditCardModel realModel;

    public AccessControlProxy(CreditCardModel realModel) {
        this.realModel = realModel;
    }

    @Override
    public boolean validateTransaction(float amount) {
        return realModel.validateTransaction(amount);
    }
}
