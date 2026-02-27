package com.example.mealhelper.ui.recipe;

public class RecipeViewModel {

    int recipeId;
    String recipeName;
    String usedIngredients;
    String missedIngredients;
    String imageUrl;

    public RecipeViewModel(int recipeId, String recipeName, String usedIngredients, String missedIngredients, String imageUrl){
        this.recipeId = recipeId;
        this.recipeName= recipeName;
        this.usedIngredients = usedIngredients;
        this.missedIngredients = missedIngredients;
        this.imageUrl = imageUrl;
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
    public String getImageUrl() {
        return imageUrl;
    }
}
