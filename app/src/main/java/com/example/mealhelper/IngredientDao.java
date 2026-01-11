package com.example.mealhelper;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IngredientDao {

    @Insert void addIngredient(IngredientEntity ingredientEntity);

    //Get all Ingredients stored in ingredient table, sorted alphabetically
    @Query("SELECT * FROM Ingredient ORDER BY ingredientName DESC")
    List<IngredientEntity> getAll();

    @Query("SELECT * FROM Ingredient WHERE ingredientId = :ingredientId LIMIT 1")
    IngredientEntity getIngredientById(int ingredientId);


}
