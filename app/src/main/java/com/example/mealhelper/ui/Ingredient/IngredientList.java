package com.example.mealhelper.ui.Ingredient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.mealhelper.ActivityBase;
import com.example.mealhelper.R;
import com.example.mealhelper.data.MealDatabase;
import com.example.mealhelper.data.dao.IngredientDao;
import com.example.mealhelper.data.entity.IngredientEntity;
import com.example.mealhelper.ui.recipe.RecipeFinder;

import java.util.ArrayList;
import java.util.List;

public class IngredientList extends ActivityBase implements IngredientRecyclerViewInterface {

    MealDatabase mealDatabase;
    IngredientDao ingredientDao;
    IngredientRecyclerAdapter adapter;
    ArrayList<IngredientViewModel> ingredientViewModels = new ArrayList<>();
    Button generateRecipes, addIngredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        View contentView = LayoutInflater.from(this)
                .inflate(R.layout.activity_ingredient_list,
                        findViewById(R.id.content_frame), true);

        setupBottomNav(R.id.nav_search);

        mealDatabase = MealDatabase.getMealDatabase(getApplicationContext());
        ingredientDao = mealDatabase.ingredientDao();

        RecyclerView recyclerView = contentView.findViewById(R.id.viewIngredientList);

        adapter = new IngredientRecyclerAdapter(this, ingredientViewModels, this, R.layout.ingredient_recycler_view_layout, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        setupIngredientViewModels();

        generateRecipes = contentView.findViewById(R.id.btnGenerateRecipe);
        addIngredient = contentView.findViewById(R.id.btnAddIngredient);

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
        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddIngredientDialog();
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

                if (existingIngredients.isEmpty()){
                    buildIngredientList();
                    existingIngredients = ingredientDao.getAll();
                }

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

    //Pop up dialog box for user to add a new ingredient
    private void showAddIngredientDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Ingredient");

        final AutoCompleteTextView input = new AutoCompleteTextView(this);
        input.setHint("Enter ingredient name");

        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {

            String newIngredient = input.getText().toString().trim();

            if (newIngredient.isEmpty()) {
                Toast.makeText(this, "Enter an ingredient", Toast.LENGTH_SHORT).show();
                return;
            }

            insertIngredient(newIngredient);
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    //Adds a new ingredient to the ingredient table
    private void insertIngredient(String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                IngredientEntity existing = ingredientDao.getIngredientNames(name);

                if (existing != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Ingredient already exists", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                IngredientEntity ingredient = new IngredientEntity();
                ingredient.setIngredientName(name);
                ingredient.setChecked(false);

                ingredientDao.addIngredient(ingredient);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Ingredient added", Toast.LENGTH_SHORT).show();
                        setupIngredientViewModels();
                    }
                });
            }
        }).start();
    }
}