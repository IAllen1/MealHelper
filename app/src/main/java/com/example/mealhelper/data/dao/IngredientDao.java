package com.example.mealhelper.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mealhelper.data.entity.IngredientEntity;

import java.util.List;

@Dao
public interface IngredientDao {

    @Insert void addIngredient(IngredientEntity ingredientEntity);

    //Get all Ingredients stored in ingredient table, sorted alphabetically
    @Query("SELECT * FROM Ingredient ORDER BY ingredientName DESC")
    List<IngredientEntity> getAll();

    @Query("SELECT * FROM Ingredient WHERE ingredientId = :ingredientId LIMIT 1")
    IngredientEntity getIngredientById(int ingredientId);

    @Query("SELECT * FROM Ingredient WHERE LOWER(ingredientName) = LOWER(:name) LIMIT 1")
    IngredientEntity getIngredientNames(String name);

    @Query("UPDATE Ingredient SET isChecked = :checked WHERE ingredientId = :id")
    void updateChecked(int id, boolean checked);

    @Query ("SELECT ingredientName FROM Ingredient WHERE isChecked = 1")
    List<String> getCheckedIngredientNames();
}
