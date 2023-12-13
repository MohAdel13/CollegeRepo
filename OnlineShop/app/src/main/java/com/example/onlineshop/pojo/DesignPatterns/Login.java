package com.example.onlineshop.pojo.DesignPatterns;

import android.content.Context;

public interface Login {
    boolean authenticate(Context context, String username, String password);
    String getType();
}
