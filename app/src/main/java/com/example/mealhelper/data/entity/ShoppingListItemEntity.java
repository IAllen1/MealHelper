package com.example.mealhelper.data.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

//Setting up the Foreign Key
@Entity(tableName = "ShoppingListItem",
        foreignKeys = {
                @ForeignKey(
                        entity = ShoppingListEntity.class,
                        parentColumns ="shoppingListId",
                        childColumns ="shoppingListId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index(value = "shoppingListId")}
)
public class ShoppingListItemEntity {

    //Primary Key for table, not auto generated as ID will be taken from the API
    @PrimaryKey
    @ColumnInfo(name = "ingredientId") Integer ingredientId;
    @ColumnInfo(name = "shoppingListId") Integer shoppingListId;
    @ColumnInfo(name = "ingredientName") String ingredientName;
    @ColumnInfo(name = "isChecked") boolean isChecked;

    public Integer getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Integer ingredientId) {
        this.ingredientId = ingredientId;
    }

    public Integer getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(Integer shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
