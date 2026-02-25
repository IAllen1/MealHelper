package com.example.mealhelper.ui.Ingredient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mealhelper.R;
import com.example.mealhelper.data.MealDatabase;
import com.example.mealhelper.data.dao.IngredientDao;
import com.example.mealhelper.data.entity.IngredientEntity;
import com.example.mealhelper.ui.recipe.RecipeFinder;

import java.util.ArrayList;
import java.util.List;

public class IngredientList extends AppCompatActivity implements IngredientRecyclerViewInterface {

    MealDatabase mealDatabase;
    IngredientDao ingredientDao;
    IngredientRecyclerAdapter adapter;
    ArrayList<IngredientViewModel> ingredientViewModels = new ArrayList<>();
    Button generateRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_list);

        mealDatabase = MealDatabase.getMealDatabase(getApplicationContext());
        ingredientDao = mealDatabase.ingredientDao();
        RecyclerView recyclerView = findViewById(R.id.viewIngredientList);

        //For listing all the ingredients alphabetically within the ingredients table - Should be done

        //User can select a single/multiple ingredient(s) - Done
        //User can tap generate recipes - taking them to a new view with the results based on the ingredients selected
        //User can add new ingredients to the database if needed


        //On first app launch, add 10 ingredients from the API - check if the table is empty first - DONE
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Query table if there are ingredients

                List<IngredientEntity> existingIngredients = ingredientDao.getAll();

                if (existingIngredients.isEmpty()){
                    buildIngredientList();
                }
            }
        }).start();

        setupIngredientViewModels();
        adapter = new IngredientRecyclerAdapter(this, ingredientViewModels, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        generateRecipes = findViewById(R.id.btnGenerateRecipe);

        generateRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //Query the ingredients table for selected items
                    List<String> selectedIngredients = ingredientDao.getCheckedIngredientNames();

                    //Check if the list is empty, if empty then there are no selected ingredients
                        if (selectedIngredients.isEmpty()){
                            //Tell the user to select an ingredient
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Select at least 1 ingredient", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }

                        String ingredientBuilder = TextUtils.join(",", selectedIngredients);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(IngredientList.this, RecipeFinder.class);

                                i.putExtra("ingredientBuilder", ingredientBuilder);

                                startActivity(i);
                            }
                        });
                    }
                }).start();
            }
        });

    }
    private void buildIngredientList(){
        IngredientEntity ingredientEntity1 = new IngredientEntity();
        ingredientEntity1.setIngredientName("chicken breast");
        ingredientDao.addIngredient(ingredientEntity1);
        IngredientEntity ingredientEntity2 = new IngredientEntity();
        ingredientEntity2.setIngredientName("beef");
        ingredientDao.addIngredient(ingredientEntity2);
        IngredientEntity ingredientEntity3 = new IngredientEntity();
        ingredientEntity3.setIngredientName("salmon");
        ingredientDao.addIngredient(ingredientEntity3);
        IngredientEntity ingredientEntity4 = new IngredientEntity();
        ingredientEntity4.setIngredientName("egg");
        ingredientDao.addIngredient(ingredientEntity4);
        IngredientEntity ingredientEntity5 = new IngredientEntity();
        ingredientEntity5.setIngredientName("bacon");
        ingredientDao.addIngredient(ingredientEntity5);
        IngredientEntity ingredientEntity6 = new IngredientEntity();
        ingredientEntity6.setIngredientName("onion");
        ingredientDao.addIngredient(ingredientEntity6);
        IngredientEntity ingredientEntity7 = new IngredientEntity();
        ingredientEntity7.setIngredientName("garlic");
        ingredientDao.addIngredient(ingredientEntity7);
        IngredientEntity ingredientEntity8 = new IngredientEntity();
        ingredientEntity8.setIngredientName("tomato");
        ingredientDao.addIngredient(ingredientEntity8);
        IngredientEntity ingredientEntity9 = new IngredientEntity();
        ingredientEntity9.setIngredientName("rice");
        ingredientDao.addIngredient(ingredientEntity9);
        IngredientEntity ingredientEntity10 = new IngredientEntity();
        ingredientEntity10.setIngredientName("pasta");
        ingredientDao.addIngredient(ingredientEntity10);
    }

    private void setupIngredientViewModels(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Get the Ingredients currently in the database
                List<IngredientEntity> existingIngredients = ingredientDao.getAll();

                List<IngredientViewModel> list = new ArrayList<>();

                //Creating new ingredient view models from the list of existing ingredients
                for (IngredientEntity ingredient : existingIngredients){
                    list.add(new IngredientViewModel(
                            ingredient.getIngredientId(),
                            ingredient.getIngredientName(),
                            ingredient.getChecked()
                    ));
                }

                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        ingredientViewModels.clear();
                        ingredientViewModels.addAll(list);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onItemClick(int position){
        IngredientViewModel ingredient = ingredientViewModels.get(position);
        // Toggle state
        ingredient.setChecked(!ingredient.isChecked());
        // Update UI
        adapter.notifyItemChanged(position);

        new Thread(new Runnable() {
            @Override
            public void run() {
                ingredientDao.updateChecked(
                        ingredient.getIngredientId(),
                        ingredient.isChecked()
                );
            }
        }).start();
    }
}