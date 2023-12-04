package com.example.onlineshop.pojo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "USER_TABLE")
public class UserModel {
    public String email;
    @PrimaryKey
            @NonNull
    public String username;
    public String password;
    public String birthDate;
    public boolean isRemembered;
    public String favCategory;
}
