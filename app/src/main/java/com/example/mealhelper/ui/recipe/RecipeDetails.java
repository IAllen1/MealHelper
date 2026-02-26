package com.example.mealhelper.ui.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
        String usedIngredients = getIntent().getStringExtra("usedIngredients");
        String missedIngredients = getIntent().getStringExtra("missedIngredients");

        //Get Recipe information call: https://api.spoonacular.com/recipes/{id}/information?apiKey={apiKey}&addWinePairing=false&addTasteData=false
        //Get the recipe image, name, used and unused ingredients from the previous activity, source URL

        callVolley(recipeId);
        recipeUsedIngredients.setText(usedIngredients);
        recipeUnusedIngredients.setText(missedIngredients);

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

                            String recipeTitle = obj.getString("title");
                            String recipeImageURL = obj.getString("image");
                            String sourceURL = obj.getString("sourceUrl");

                            recipeName.setText(recipeTitle);
                            sourceUrl = sourceURL;

                            //Parse the Image URL to fill the image view here
                            loadRecipeImage(recipeImageURL);

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