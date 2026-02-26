package com.example.mealhelper.ui.recipe;

public class RecipeViewModel {

    int recipeId;
    String recipeName, usedIngredients, missedIngredients;

    public RecipeViewModel(int recipeId, String recipeName, String usedIngredients, String missedIngredients){
        this.recipeId = recipeId;
        this.recipeName= recipeName;
        this.usedIngredients = usedIngredients;
        this.missedIngredients = missedIngredients;
    }
    public int getRecipeId() {
        return recipeId;
    }
    public String getRecipeName() {
        return recipeName;
    }
    public String getUsedIngredients() {
        return usedIngredients;
    }
    public String getMissedIngredients() {
        return missedIngredients;
    }
}
