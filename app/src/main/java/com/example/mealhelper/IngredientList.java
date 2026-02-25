package com.example.mealhelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Grid;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class IngredientList extends AppCompatActivity implements IngredientRecyclerViewInterface{

    MealDatabase mealDatabase;
    IngredientDao ingredientDao;
    IngredientRecyclerAdapter adapter;
    ArrayList<IngredientViewModel> ingredientViewModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_list);

        mealDatabase = MealDatabase.getMealDatabase(getApplicationContext());
        ingredientDao = mealDatabase.ingredientDao();
        RecyclerView recyclerView = findViewById(R.id.viewIngredientList);

        //For listing all the ingredients within the ingredients table - Should be done

        //User can select a single/multiple ingredient(s)
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

                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("DEBUG", "Before: " + ingredient.isChecked());
                        ingredient.setChecked(!ingredient.isChecked());
                        Log.d("DEBUG", "After: " + ingredient.isChecked());
                    }
                });*/
            }
        }).start();
    }
}