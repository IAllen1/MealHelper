package com.example.mealhelper;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Ingredient")

public class IngredientEntity {

    //Primary Key for table, not auto generated as ID will be taken from the API
    @PrimaryKey
    @ColumnInfo (name = "ingredientId") Integer ingredientId;
    @ColumnInfo (name = "ingredientName") String ingredientName;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @ColumnInfo (name = "ingredientImageUrl") String imageUrl;

    public Integer getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Integer ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }
}
