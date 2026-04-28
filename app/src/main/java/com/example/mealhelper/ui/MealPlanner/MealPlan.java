package com.example.mealhelper.ui.MealPlanner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.mealhelper.ActivityBase;
import com.example.mealhelper.R;
import com.example.mealhelper.data.MealDatabase;
import com.example.mealhelper.data.dao.MealPlanDao;
import com.example.mealhelper.data.dao.RecipeDao;
import com.example.mealhelper.data.entity.MealPlanEntity;
import com.example.mealhelper.data.entity.RecipeEntity;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class MealPlan extends ActivityBase implements MealPlanRecyclerViewInterface {

    MealDatabase mealDatabase;
    MealPlanDao mealPlanDao;
    RecipeDao recipeDao;
    MealPlanRecyclerAdapter adapter;
    ArrayList<MealPlanViewModel> mealPlanViewModels = new ArrayList<>();
    RecyclerView mealPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        View contentView = LayoutInflater.from(this)
                .inflate(R.layout.activity_meal_plan,
                        findViewById(R.id.content_frame), true);
        setupBottomNav(R.id.nav_planner);

        //This activity is where the Meal Planner will go

        //1. Recycler View for each day
        //2.

        mealDatabase = MealDatabase.getMealDatabase(getApplicationContext());
        mealPlanDao = mealDatabase.mealPlanDao();
        mealPlan = contentView.findViewById(R.id.mealPlanList);
        recipeDao = mealDatabase.recipeDao();

        adapter = new MealPlanRecyclerAdapter(this, mealPlanViewModels, this);
        mealPlan.setAdapter(adapter);
        mealPlan.setLayoutManager(new LinearLayoutManager(this));
        setupMealPlanViewModels();

    }

    public void setupMealPlanViewModels(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                //Query the database
                //1. Get the date, and the full row, so will return max 4. rows linked to that date
                //2. Get
                //Figure out how to do the date stuff
                //Add query to get recipe name by ID
                //Add recipe Name to the view model



                List<MealPlanEntity> existingPlans = mealPlanDao.getAll();

                ArrayList<MealPlanViewModel> list = new ArrayList<>();

                LocalDate today = LocalDate.now();

                //Loop through the next 7 days

                for (int i = 0; i < 7; i++){
                    String date = today.plusDays(i).toString();

                    String breakfast = null;
                    String lunch = null;
                    String dinner = null;
                    String supper = null;

                    for (MealPlanEntity meal : existingPlans){
                        if (meal.getDate().equals(date)){

                            //Get Recipe Name
                            String recipeName = recipeDao.getRecipeNameById(meal.getRecipeId());

                            switch (meal.getMealType()){
                                case "breakfast":
                                    breakfast = recipeName;
                                    break;
                                case "lunch":
                                    lunch = recipeName;
                                    break;
                                case "dinner":
                                    dinner = recipeName;
                                    break;
                                case "supper":
                                    supper = recipeName;
                                    break;
                            }
                        }
                    }

                    list.add(new MealPlanViewModel(
                            date,
                            breakfast,
                            lunch,
                            dinner,
                            supper
                    ));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mealPlanViewModels.clear();
                        mealPlanViewModels.addAll(list);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onItemClick(int position, String mealType){

        MealPlanViewModel day = mealPlanViewModels.get(position);
        String date = day.getDate();

        showAddRecipeDialog(date, mealType);
    }

    private void getRecipes(RecipesCallBack callback){

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<RecipeEntity> recipes = recipeDao.getAll();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResult(recipes);
                    }
                });
            }
        }).start();
    }

    public interface RecipesCallBack{
        void onResult(List<RecipeEntity> recipes);
    }

    private void showAddRecipeDialog(String date, String mealType){
        getRecipes(recipes ->{

            String[] recipeNames = new String[recipes.size()];

            for (int i = 0; i < recipes.size(); i++){
                recipeNames[i] = recipes.get(i).getRecipeTitle();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Recipe");

            builder.setItems(recipeNames, (dialog, which) ->{

                RecipeEntity selectedRecipe = recipes.get(which);

                insertRecipe(
                        selectedRecipe.getRecipeID(),
                        date,
                        mealType
                );
            });

            builder.setNegativeButton("Cancel", null);

            builder.show();


        });
    }

    private void insertRecipe(int recipeId, String date, String mealType){

        new Thread(new Runnable() {
            @Override
            public void run() {
                MealPlanEntity meal = new MealPlanEntity();

                meal.setRecipeId(recipeId);
                meal.setDate(date);
                meal.setMealType(mealType);

                mealPlanDao.addPlan(meal);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupMealPlanViewModels();
                    }
                });
            }
        }).start();
    }
}