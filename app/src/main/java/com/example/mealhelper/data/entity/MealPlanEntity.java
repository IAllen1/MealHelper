package com.example.mealhelper.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

//Setting up the Foreign Key
@Entity(tableName = "MealPlan",
        foreignKeys = {
                @ForeignKey(
                        entity = RecipeEntity.class,
                        parentColumns = "recipeId",
                        childColumns = "recipeId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
            @Index(value = "recipeId"),
                //Will ensure date and meal type combination will be unique e.g. 06/03/2026 + "Dinner" will only appear once
            @Index(value = {"date", "mealType"}, unique = true)
        }
)

public class MealPlanEntity {

    @PrimaryKey(autoGenerate = true) Integer MealPlanId;
    @ColumnInfo(name = "recipeId") Integer recipeId;
    @ColumnInfo(name = "date") String date;
    @ColumnInfo(name = "mealType") String mealType;


    public Integer getMealPlanId() {
        return MealPlanId;
    }
    public void setMealPlanId(Integer mealPlanId) {
        MealPlanId = mealPlanId;
    }

    public Integer getRecipeId() {
        return recipeId;
    }
    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getMealType() {
        return mealType;
    }
    public void setMealType(String mealType) {
        this.mealType = mealType;
    }
}
