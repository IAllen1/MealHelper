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
                ),
                @ForeignKey(
                        entity = IngredientEntity.class,
                        parentColumns = "ingredientId",
                        childColumns = "ingredientId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index(value = "shoppingListId"), @Index("ingredientId")}
)
public class ShoppingListItemEntity {

    @PrimaryKey (autoGenerate = true) Integer shoppingListItemId;
    @ColumnInfo(name = "shoppingListId") Integer shoppingListId;
    @ColumnInfo(name = "ingredientId") Integer ingredientId;
    @ColumnInfo(name = "isChecked", defaultValue = "0") boolean isChecked;

    public Integer getShoppingListItemId() {
        return shoppingListItemId;
    }
    public void setShoppingListItemId(Integer shoppingListItemId) {
        this.shoppingListItemId = shoppingListItemId;
    }

    public Integer getShoppingListId() {
        return shoppingListId;
    }
    public void setShoppingListId(Integer shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public Integer getIngredientId() {
        return ingredientId;
    }
    public void setIngredientId(Integer ingredientId) {
        this.ingredientId = ingredientId;
    }

    public boolean isChecked() {
        return isChecked;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
