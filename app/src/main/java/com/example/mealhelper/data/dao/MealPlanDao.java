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

    //1. Grab all rows where the date is this
    //2.
    //Do this for the current day and the next 7 days
}
