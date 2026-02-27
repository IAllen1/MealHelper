package com.example.mealhelper.ui.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.mealhelper.ActivityBase;
import com.example.mealhelper.R;
import com.example.mealhelper.data.MealDatabase;
import com.example.mealhelper.data.dao.RecipeDao;
import com.example.mealhelper.data.entity.RecipeEntity;

import java.util.ArrayList;
import java.util.List;

public class SavedRecipes extends ActivityBase implements RecipeRecyclerViewInterface {

    MealDatabase mealDatabase;
    RecipeDao recipeDao;
    RecipeRecyclerAdapter adapter;
    ArrayList<RecipeViewModel> recipeViewModels = new ArrayList<>();
    RecyclerView savedRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        View contentView = LayoutInflater.from(this)
                .inflate(R.layout.activity_saved_recipes,
                        findViewById(R.id.content_frame), true);
        setupBottomNav(R.id.nav_saved);

        mealDatabase = MealDatabase.getMealDatabase(getApplicationContext());
        recipeDao = mealDatabase.recipeDao();
        savedRecipes = contentView.findViewById(R.id.savedRecipesList);

        adapter = new RecipeRecyclerAdapter(this, recipeViewModels, this);
        savedRecipes.setAdapter(adapter);
        savedRecipes.setLayoutManager(new LinearLayoutManager(this));
        setupRecipeViewModels();
    }

    public void setupRecipeViewModels(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<RecipeEntity> existingRecipes = recipeDao.getAll();

                ArrayList<RecipeViewModel> list = new ArrayList<>();

                for (RecipeEntity recipe : existingRecipes){
                    list.add(new RecipeViewModel(
                            recipe.getRecipeID(),
                            recipe.getRecipeTitle(),
                            "",
                            "",
                            recipe.getImageUrl()
                    ));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recipeViewModels.clear();
                        recipeViewModels.addAll(list);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onItemClick(int position){
        Intent i = new Intent(SavedRecipes.this, RecipeDetails.class);

        i.putExtra("recipeId", recipeViewModels.get(position).getRecipeId());
        i.putExtra("recipeName", recipeViewModels.get(position).getRecipeName());

        startActivity(i);
    }
}