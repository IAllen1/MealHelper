package com.example.mealhelper;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MealPlanDao {
    @Insert void addPlan(MealPlanEntity mealPlanEntity);

    @Query("SELECT * FROM mealplan")
    List<MealPlanEntity> getAll();
}
