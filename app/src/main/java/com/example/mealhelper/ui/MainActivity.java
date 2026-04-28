package com.example.mealhelper.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mealhelper.R;
import com.example.mealhelper.ui.MealPlanner.MealPlan;
import com.example.mealhelper.ui.ShoppingList.ShoppingLists;
import com.example.mealhelper.ui.recipe.RecipeFinder;
import com.example.mealhelper.data.MealDatabase;
import com.example.mealhelper.data.dao.IngredientDao;
import com.example.mealhelper.data.entity.IngredientEntity;
import com.example.mealhelper.ui.Ingredient.IngredientList;
import com.example.mealhelper.ui.recipe.SavedRecipes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    MealDatabase mealDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mealDatabase = MealDatabase.getMealDatabase(getApplicationContext());

    }
    public void goToSavedRecipes(View view) {
        Intent intent = new Intent(MainActivity.this, SavedRecipes.class);
        startActivity(intent);
    }

    public void goToIngredientList(View view) {
        Intent intent = new Intent(MainActivity.this, IngredientList.class);
        startActivity(intent);
    }

    public void goToShoppingList(View view){
        Intent intent = new Intent(MainActivity.this, ShoppingLists.class);
        startActivity(intent);
    }

    public void goToMealPlanner(View view){
        Intent intent = new Intent(MainActivity.this, MealPlan.class);
        startActivity(intent);
    }
}