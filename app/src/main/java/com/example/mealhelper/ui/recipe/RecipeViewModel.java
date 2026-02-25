package com.example.mealhelper.ui.recipe;

public class RecipeViewModel {

    int recipeId;
    String recipeName;

    public RecipeViewModel(int recipeId, String recipeName){
        this.recipeId = recipeId;
        this.recipeName= recipeName;
    }
    public int getRecipeId() {
        return recipeId;
    }
    public String getRecipeName() {
        return recipeName;
    }
}
