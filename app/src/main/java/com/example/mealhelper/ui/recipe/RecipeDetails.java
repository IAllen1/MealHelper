package com.example.mealhelper.ui.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mealhelper.R;
import com.example.mealhelper.data.MealDatabase;
import com.example.mealhelper.data.dao.RecipeDao;
import com.example.mealhelper.data.entity.RecipeEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeDetails extends AppCompatActivity {

    MealDatabase mealDatabase;
    RecipeDao recipeDao;
    String apiKey = "2a695673a58445d98c130ecb7d1c8c98";
    TextView recipeName, recipeUsedIngredients, recipeUnusedIngredients;
    Button viewRecipe, saveRecipe;
    ImageView recipeImage;
    String sourceUrl = "";
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        mealDatabase = MealDatabase.getMealDatabase(getApplicationContext());
        recipeDao = mealDatabase.recipeDao();
        recipeName = findViewById(R.id.txtRecipeTitle);
        recipeUsedIngredients = findViewById(R.id.txtUsedIngredients);
        recipeUnusedIngredients = findViewById(R.id.txtUnusedIngredients);
        viewRecipe = findViewById(R.id.btnViewRecipe);
        saveRecipe = findViewById(R.id.btnSaveRecipe);
        recipeImage = findViewById(R.id.imgRecipeImage);

        int recipeId = getIntent().getIntExtra("recipeId", -1);
        String recipeName = getIntent().getStringExtra("recipeName");
        String usedIngredients = getIntent().getStringExtra("usedIngredients");
        String missedIngredients = getIntent().getStringExtra("missedIngredients");

        //Get Recipe information call: https://api.spoonacular.com/recipes/{id}/information?apiKey={apiKey}&addWinePairing=false&addTasteData=false
        //Get the recipe image, name, used and unused ingredients from the previous activity, source URL

        callVolley(recipeId);
        recipeUsedIngredients.setText(usedIngredients);
        recipeUnusedIngredients.setText(missedIngredients);

        //View Recipe Functionality Here
        viewRecipe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent i = new Intent(RecipeDetails.this, RecipeViewer.class);
                i.putExtra("sourceUrl", sourceUrl);
                startActivity(i);
            }
        });

        //Save Recipe Functionality Here
        //1. Set OnClickListener for save recipe button
        //2. Separate thread, access the table & dao
        //3. Check if the recipe is already in the table by checking against the recipeID
        //4. If recipe not in table, insert recipe into table
        //5. Toast user the output

        saveRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RecipeEntity recipe = new RecipeEntity();
                        recipe.setRecipeID(recipeId);
                        recipe.setRecipeTitle(recipeName);
                        recipe.setImageUrl(imageUrl);
                        recipe.setSourceUrl(sourceUrl);

                        //Check if the recipe is already saved in the table
                        RecipeEntity existing = recipeDao.getRecipeById(recipeId);

                        if (existing != null){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            recipeName + " already saved", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                        recipeDao.addRecipe(recipe);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        recipeName + " successfully added", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });

    }

    private void callVolley(int recipeId){

        //Build the URL here
        String recipeDetailsUrl = "https://api.spoonacular.com/recipes/"
                + recipeId + "/"
                + "information?apiKey=" + apiKey
                + "&addWinePairing=false&addTasteData=false";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, recipeDetailsUrl,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            //JSONArray recipeDetailsArray = new JSONArray(response);

                            JSONObject obj = new JSONObject(response);

                            recipeName.setText(obj.getString("title"));
                            imageUrl = obj.getString("image");
                            loadRecipeImage(imageUrl);
                            sourceUrl = obj.getString("sourceUrl");

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

    private void loadRecipeImage(String recipeURL){
        RequestQueue queue = Volley.newRequestQueue(this);

        ImageRequest imageRequest = new ImageRequest(recipeURL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                recipeImage.setImageBitmap(response);
            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Do Something with error
            }
        });
        queue.add(imageRequest);
    }
}