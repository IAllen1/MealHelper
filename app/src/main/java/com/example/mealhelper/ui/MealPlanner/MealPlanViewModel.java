package com.example.mealhelper.ui.MealPlanner;

public class MealPlanViewModel {

    String date;
    String breakfast;
    String lunch;
    String dinner;
    String supper;

    public MealPlanViewModel(String date, String breakfast, String lunch, String dinner, String supper){
        this.date = date;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.supper = supper;
    }

    public String getDate() {
        return date;
    }
    public String getBreakfast() {
        return breakfast;
    }
    public String getLunch() {
        return lunch;
    }
    public String getDinner() {
        return dinner;
    }
    public String getSupper() {
        return supper;
    }
}
