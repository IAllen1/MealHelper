package com.example.mealhelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mealhelper.ui.Ingredient.IngredientList;
import com.example.mealhelper.ui.recipe.SavedRecipes;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mealhelper.databinding.ActivityBaseBinding;

public abstract class ActivityBase extends AppCompatActivity {

    protected void setupBottomNav(int selectedItemId) {

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setSelectedItemId(selectedItemId);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_saved){
                startActivity(new Intent(this, SavedRecipes.class));
                return true;
            }
            else if(id == R.id.nav_search){
                startActivity(new Intent(this, IngredientList.class));
                return true;
            }
            return false;
        });
    }
}