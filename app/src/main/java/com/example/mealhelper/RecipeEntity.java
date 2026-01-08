package com.example.mealhelper;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Recipe")

public class RecipeEntity {

    //Primary Key for table, not auto generated as ID will be taken from the API
    @PrimaryKey
    @ColumnInfo(name = "recipeId") Integer recipeID;
    @ColumnInfo(name = "recipeTitle") String recipeTitle;
    @ColumnInfo(name = "imageUrl") String imageUrl;
    @ColumnInfo(name = "sourceUrl") String sourceUrl;

    public Integer getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(Integer recipeID) {
        this.recipeID = recipeID;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}
