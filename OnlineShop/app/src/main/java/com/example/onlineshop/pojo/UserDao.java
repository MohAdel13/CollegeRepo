package com.example.onlineshop.pojo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(UserModel user);

    @Delete
    void deleteUser(UserModel user);

    @Query("SELECT * FROM USER_TABLE")
    List<UserModel> getAllUsers();

    @Query("UPDATE USER_TABLE SET password =:password WHERE username =:username")
    void updatePassword(String username, String password);

    @Query("UPDATE USER_TABLE SET birthDate =:birthDate WHERE username =:username")
    void updateBirth(String username, String birthDate);

    @Query("SELECT * FROM USER_TABLE WHERE username =:username AND password =:password")
    List<UserModel> getUser(String username,String password);

    @Query("SELECT * FROM USER_TABLE WHERE username =:username OR email =:email")
    List<UserModel> failedRegUser(String username,String email);

    @Query("SELECT * FROM USER_TABLE WHERE username =:username")
    List<UserModel> checkUser(String username);

    @Query("UPDATE USER_TABLE SET isRemembered =:isRemembered WHERE username =:username")
    void updateIsRemembered(boolean isRemembered,String username);

    @Query("UPDATE USER_TABLE SET isRemembered = 0")
    void logout();

    @Query("SELECT * FROM USER_TABLE WHERE isRemembered = 1")
    List<UserModel> checkIsRemembered();

    @Query("Update USER_TABLE SET favCategory =:cat WHERE username =:username")
    void updateCat(String cat,String username);
}
