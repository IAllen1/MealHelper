package com.example.mealhelper.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Ingredient")

public class IngredientEntity {

    //Primary Key for table, not auto generated as ID will be taken from the API
    @PrimaryKey (autoGenerate = true) Integer ingredientId;
    @ColumnInfo (name = "ingredientName") String ingredientName;
    @ColumnInfo (name = "isChecked", defaultValue = "0") boolean isChecked;
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
    public boolean getChecked() {
        return isChecked;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
