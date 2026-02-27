package com.example.mealhelper.ui.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mealhelper.R;
import com.google.android.material.appbar.MaterialToolbar;

public class RecipeViewer extends AppCompatActivity {

    WebView recipeViewer;
    MaterialToolbar backToRecipeDetails;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_viewer);

        recipeViewer = findViewById(R.id.webView);
        backToRecipeDetails = findViewById(R.id.toolbar);
        setSupportActionBar(backToRecipeDetails);

        backToRecipeDetails.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String url = getIntent().getStringExtra("sourceUrl");



        recipeViewer.getSettings().setJavaScriptEnabled(true);

        recipeViewer.setWebViewClient(new WebViewClient());

        recipeViewer.loadUrl(url);
    }
}