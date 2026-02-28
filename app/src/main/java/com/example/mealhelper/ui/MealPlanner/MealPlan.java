package com.example.mealhelper.ui.MealPlanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.mealhelper.ActivityBase;
import com.example.mealhelper.R;

public class MealPlan extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        View contentView = LayoutInflater.from(this)
                .inflate(R.layout.activity_meal_plan,
                        findViewById(R.id.content_frame), true);

        setupBottomNav(R.id.nav_planner);

        //This activity is where the Meal Planner stuff will go
    }
}