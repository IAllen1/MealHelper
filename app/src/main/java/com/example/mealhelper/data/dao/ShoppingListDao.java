package com.example.mealhelper.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mealhelper.data.entity.ShoppingListEntity;

import java.util.List;

@Dao
public interface ShoppingListDao {

    @Insert void addList(ShoppingListEntity shoppingListEntity);

    @Query("SELECT * FROM shoppinglist")
    List<ShoppingListEntity> getAll();

    @Query("SELECT * FROM ShoppingList WHERE LOWER(shoppingListName) = LOWER(:name) LIMIT 1")
    ShoppingListEntity getShoppingListNames(String name);
}
