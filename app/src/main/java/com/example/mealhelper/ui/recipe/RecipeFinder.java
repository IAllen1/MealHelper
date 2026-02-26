package com.example.mealhelper.ui.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mealhelper.R;
import com.example.mealhelper.data.MealDatabase;
import com.example.mealhelper.data.dao.IngredientDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeFinder extends AppCompatActivity implements RecipeRecyclerViewInterface{

    MealDatabase mealDatabase;
    String apiKey = "2a695673a58445d98c130ecb7d1c8c98";
    RecyclerView recyclerView;

    RecipeRecyclerAdapter adapter;
    ArrayList<RecipeViewModel> recipeViewModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_finder);
        mealDatabase = MealDatabase.getMealDatabase(getApplicationContext());
        recyclerView = findViewById(R.id.recipeSearchResult);

        adapter = new RecipeRecyclerAdapter(this, recipeViewModels, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String ingredientBuilder = getIntent().getStringExtra("ingredientBuilder");

        callVolley(ingredientBuilder);

        //Query which ingredients have been selected
        //Needs to run whenever the user clicks generate recipes button on the ingredients list activity & the result of the api call is populated into the new activity
        //And taking the user into the recipeFinder activity
        //Tidy up the recyclerView row for recipe results, make it clickable etc.
        //Show 20 recipe results for now, adjust the url sorting by how many used ingredients first, then unused ingredients
        //In the future, store the unused ingredients for use for shopping list functionality
        //New Activity for detailed recipe info based on the selected recipe from search result - Image, Name, Ingredients Used, Ingredients Missing. View Recipe Button taking user into a WebView
        //Save button to add the recipe to the Recipe table for easy access in the future
        //Merge current into Master at end of day


        //https://api.spoonacular.com/recipes/findByIngredients?apiKey=2a695673a58445d98c130ecb7d1c8c98&ingredients=chicken+breast&ignorePantry=false

        /*btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Query the ingredients table to get the list of ingredients for now, will change this at a later date storing into a list, looping through the list
                //to build a string which will form the api call for the recipes

                //Should query to get only the ingredient name from the table



                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> ingredientsAvailable = ingredientDao.getIngredientNames();

                        String ingredientString = TextUtils.join(",", ingredientsAvailable);

                        ingredientString = Uri.encode(ingredientString);

                        String searchRecipeUrl = "https://api.spoonacular.com/recipes/findByIngredients?apiKey="
                                + apiKey +
                                "&ingredients=" + ingredientString +
                                "&ignorePantry=false";

                        Log.d("URL", searchRecipeUrl);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callVolley(searchRecipeUrl);
                                txtUrl.setText(searchRecipeUrl);
                            }
                        });
                    }
                }).start();
            }
        });*/
    }

    private void callVolley(String ingredientBuilder){

        //Build the URL here
        String recipeUrl = "https://api.spoonacular.com/recipes/findByIngredients"
                + "?apiKey=" + apiKey
                + "&ingredients=" + ingredientBuilder
                + "&number=20&ranking=1&ignorePantry=false";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, recipeUrl,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray recipeArray = new JSONArray(response);

                            recipeViewModels.clear();

                            for (int i = 0; i < recipeArray.length(); i++){
                                JSONObject recipeObj = recipeArray.getJSONObject(i);

                                int recipeId = recipeObj.getInt("id");
                                String title = recipeObj.getString("title");
                                String imageUrl = recipeObj.getString("image");

                                Log.d("RECIPE", title);

                                //Build recycler view here
                                recipeViewModels.add(new RecipeViewModel(recipeId, title));

                            }

                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onItemClick(int position){
        Intent i = new Intent();
        
        i.putExtra("recipeId", recipeViewModels.get(position).getRecipeId());
        i.putExtra("recipeName", recipeViewModels.get(position).getRecipeName());

        startActivity(i);
    }
}
