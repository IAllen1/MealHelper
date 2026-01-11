package com.example.mealhelper;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RecipeFinder extends AppCompatActivity {

    MealDatabase mealDatabase;
    IngredientDao ingredientDao;
    String apiKey = "2a695673a58445d98c130ecb7d1c8c98";
    Button btnGenerate;
    TextView txtUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_finder);

        mealDatabase = MealDatabase.getMealDatabase(getApplicationContext());
        ingredientDao = mealDatabase.ingredientDao();

        btnGenerate = findViewById(R.id.btnGenerate);
        txtUrl = findViewById(R.id.txtUrl);

        //https://api.spoonacular.com/recipes/findByIngredients?apiKey=2a695673a58445d98c130ecb7d1c8c98&ingredients=chicken+breast&ignorePantry=false

        btnGenerate.setOnClickListener(new View.OnClickListener() {
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
        });
    }


    private void callVolley(String newUrl){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, newUrl,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray recipeArray = new JSONArray(response);

                            for (int i = 0; i < recipeArray.length(); i++){
                                JSONObject recipeObj = recipeArray.getJSONObject(i);

                                int recipeId = recipeObj.getInt("id");
                                String title = recipeObj.getString("title");
                                String imageUrl = recipeObj.getString("image");

                                Log.d("RECIPE", title);
                            }

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
}