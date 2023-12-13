package com.example.onlineshop.pojo.RoomDataBases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlineshop.pojo.Models.OrderModel;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    void insertOrder(OrderModel order);

    @Delete
    void deleteOrder(OrderModel order);

    @Update
    void updateOrder(OrderModel order);

    @Query("SELECT * FROM ORDER_TABLE")
    List<OrderModel> getAllOrders();
}
