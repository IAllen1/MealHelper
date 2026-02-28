package com.example.mealhelper.ui.ShoppingList;

public class ShoppingListViewModel {
    int shoppingListId;
    String shoppingListName;

    public ShoppingListViewModel(int shoppingListId, String shoppingListName) {
        this.shoppingListId = shoppingListId;
        this.shoppingListName = shoppingListName;
    }
    public int getShoppingListId() {
        return shoppingListId;
    }
    public String getShoppingListName() {
        return shoppingListName;
    }
}
