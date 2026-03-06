package com.example.mealhelper.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mealhelper.data.entity.ShoppingListItemEntity;

import java.util.List;

@Dao
public interface ShoppingListItemDao {

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void add(ShoppingListItemEntity shoppingListItemEntity);


    //Use this to populate the activity with the ingredients from each shopping list, will likely need updated
    @Query("SELECT * FROM shoppinglistitem")
    List<ShoppingListItemEntity> getAll();

    //List all shopping list items based on a specific shopping list ID

    @Query("SELECT * FROM shoppinglistitem WHERE shoppingListId = :id")
    List<ShoppingListItemEntity> getIngredientsById(int id);

    @Query("UPDATE ShoppingListItem SET isChecked =:checked WHERE ingredientId=:id AND shoppingListId =:listId")
    void updateChecked(int id, int listId, boolean checked);

}
