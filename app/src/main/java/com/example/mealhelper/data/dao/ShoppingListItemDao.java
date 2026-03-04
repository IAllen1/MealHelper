package com.example.mealhelper.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mealhelper.data.entity.ShoppingListItemEntity;

import java.util.List;

@Dao
public interface ShoppingListItemDao {

    @Insert void addItem(ShoppingListItemEntity shoppingListItemEntity);


    //Use this to populate the activity with the ingredients from each shopping list, will likely need updated
    @Query("SELECT * FROM shoppinglistitem")
    List<ShoppingListItemEntity> getAll();

    //List all shopping list items based on a specific shopping list ID

    @Query("SELECT * FROM shoppinglistitem WHERE shoppingListId = :id")
    List<ShoppingListItemEntity> getIngredientsById(int id);

}
