package com.example.mealhelper;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert void addRecipe(RecipeEntity recipeEntity);

    @Query("SELECT * FROM Recipe")
    List<RecipeEntity> getAll();
}
