package com.example.mealhelper.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mealhelper.data.entity.RecipeEntity;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert void addRecipe(RecipeEntity recipeEntity);

    @Query("SELECT * FROM Recipe")
    List<RecipeEntity> getAll();

    @Query("SELECT * FROM Recipe WHERE recipeId = :recipeId LIMIT 1")
    RecipeEntity getRecipeById(int recipeId);
}
