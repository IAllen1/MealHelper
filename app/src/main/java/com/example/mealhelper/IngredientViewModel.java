package com.example.mealhelper;

public class IngredientViewModel {

    int ingredientId;
    String ingredientName;
    boolean isChecked = false;

    public IngredientViewModel(int ingredientId, String ingredientName, boolean isChecked){
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.isChecked = isChecked;
    }

    public int getIngredientId(){
        return ingredientId;
    }
    public String getIngredientName() {
        return ingredientName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked){
        this.isChecked = checked;
    }
}
