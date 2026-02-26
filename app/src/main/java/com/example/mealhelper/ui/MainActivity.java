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
import com.example.mealhelper.ui.recipe.RecipeFinder;
import com.example.mealhelper.data.MealDatabase;
import com.example.mealhelper.data.dao.IngredientDao;
import com.example.mealhelper.data.entity.IngredientEntity;
import com.example.mealhelper.ui.Ingredient.IngredientList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    MealDatabase mealDatabase;
    IngredientDao ingredientDao;
    TextView txtResult;
    EditText editText1;
    Button btnSearch, btnSave, btnGoToGenerator;
    String apiKey = "2a695673a58445d98c130ecb7d1c8c98";

    String lastIngredientId, lastIngredientName, lastIngredientImageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mealDatabase = MealDatabase.getMealDatabase(getApplicationContext());
        ingredientDao = mealDatabase.ingredientDao();

        txtResult = findViewById(R.id.txtSearchResult);
        editText1 = findViewById(R.id.editTxtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnSave = findViewById(R.id.B);
        btnGoToGenerator = findViewById(R.id.btnRecipeGenerate);

        btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String ingredientName = editText1.getText().toString().trim();

                String ingredientURL = "https://api.spoonacular.com/food/ingredients/search?apiKey="+ apiKey + "&query=" + ingredientName + "&number=1";
                //txt1.setText(ingredientURL);
                //https://api.spoonacular.com/recipes/findByIngredients?apiKey=2a695673a58445d98c130ecb7d1c8c98&ingredients=apples,+flour,+sugar&number=2

                //https://api.spoonacular.com/food/ingredients/search?apiKey=2a695673a58445d98c130ecb7d1c8c98&query=chicken&number=1

                callVolley(ingredientURL);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        IngredientEntity ingredient = new IngredientEntity();
                        ingredient.setIngredientId(Integer.parseInt(lastIngredientId));
                        ingredient.setIngredientName(lastIngredientName);
                       // ingredient.setImageUrl(lastIngredientImageUrl);

                        IngredientEntity existing = ingredientDao.getIngredientById(Integer.parseInt(lastIngredientId));

                        if (existing != null){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), lastIngredientName + " already in database", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                        ingredientDao.addIngredient(ingredient);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        ingredient.getIngredientName() + " successfully added", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });
    }

    public void goToRecipeGenerator(View view) {
        Intent intent = new Intent(MainActivity.this, RecipeFinder.class);
        startActivity(intent);
    }

    public void goToIngredientList(View view) {
        Intent intent = new Intent(MainActivity.this, IngredientList.class);
        startActivity(intent);
    }

    private void callVolley(String newUrl){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, newUrl,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONObject obj = new JSONObject(response);
                            JSONArray resultsArray = obj.getJSONArray("results");

                            if (resultsArray.length() > 0) {

                                JSONObject ingredientObj = resultsArray.getJSONObject(0);

                                String ingredientId = ingredientObj.getString("id");
                                String ingredientName = ingredientObj.getString("name");
                                String ingredientImageName = ingredientObj.getString("image");

                                String imageUrl = "https://spoonacular.com/cdn/ingredients_100x100/" + ingredientImageName;

                                String resultText = "ID: " + ingredientId + "\n" +
                                        "Name: " + ingredientName + "\n" +
                                        "ImageUrl: " + imageUrl;

                                txtResult.setText(resultText);
                                btnSave.setEnabled(true);

                                lastIngredientId = ingredientId;
                                lastIngredientName = ingredientName;
                                lastIngredientImageUrl = imageUrl;

                            } else {
                                txtResult.setText("No ingredient found.");
                                btnSave.setEnabled(false);
                            }

                        } catch (JSONException e) {
                            txtResult.setText("JSON Parsing Error");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                txtResult.setText("Network Error: " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }
}