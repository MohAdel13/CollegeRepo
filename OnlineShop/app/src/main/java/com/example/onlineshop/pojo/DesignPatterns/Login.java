package com.example.onlineshop.pojo.DesignPatterns;

import android.content.Context;

public interface Login {
    boolean Authenticate(String user, String pass, Context context);
    String getType();
}
