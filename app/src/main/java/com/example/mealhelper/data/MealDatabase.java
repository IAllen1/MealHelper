package com.example.mealhelper.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mealhelper.data.entity.IngredientEntity;
import com.example.mealhelper.data.entity.MealPlanEntity;
import com.example.mealhelper.data.entity.RecipeEntity;
import com.example.mealhelper.data.entity.ShoppingListEntity;
import com.example.mealhelper.data.entity.ShoppingListItemEntity;
import com.example.mealhelper.data.dao.IngredientDao;
import com.example.mealhelper.data.dao.MealPlanDao;
import com.example.mealhelper.data.dao.RecipeDao;
import com.example.mealhelper.data.dao.ShoppingListDao;
import com.example.mealhelper.data.dao.ShoppingListItemDao;


@Database(entities =
        {IngredientEntity.class,
        RecipeEntity.class,
        MealPlanEntity.class,
        ShoppingListEntity.class,
        ShoppingListItemEntity.class
        }, version = 1)

public abstract class MealDatabase extends RoomDatabase {
    private static final String dbName = "MainDb";

    private static MealDatabase mealDatabase;

    public static synchronized MealDatabase getMealDatabase(Context context){
        if (mealDatabase == null){
            mealDatabase = Room.databaseBuilder(context, MealDatabase.class, dbName)
                    .fallbackToDestructiveMigration().build();
        }
        return mealDatabase;
    }
    public abstract IngredientDao ingredientDao();
    public abstract RecipeDao recipeDao();
    public abstract MealPlanDao mealPlanDao();
    public abstract ShoppingListDao shoppingListDao();
    public abstract ShoppingListItemDao shoppingListItemDao();
}
