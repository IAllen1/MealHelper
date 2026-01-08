package com.example.mealhelper;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ShoppingListItemDao {

    @Insert void addItem(ShoppingListItemEntity shoppingListItemEntity);

    @Query("SELECT * FROM shoppinglistitem")
    List<ShoppingListItemEntity> getAll();
}
