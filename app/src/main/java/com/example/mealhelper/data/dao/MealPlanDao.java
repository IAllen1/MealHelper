package com.example.mealhelper.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mealhelper.data.entity.MealPlanEntity;

import java.util.List;

@Dao
public interface MealPlanDao {
    @Insert void addPlan(MealPlanEntity mealPlanEntity);

    @Query("SELECT * FROM mealplan")
    List<MealPlanEntity> getAll();
}
