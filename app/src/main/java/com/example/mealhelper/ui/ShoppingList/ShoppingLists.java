package com.example.mealhelper.ui.ShoppingList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.mealhelper.ActivityBase;
import com.example.mealhelper.R;
import com.example.mealhelper.data.MealDatabase;
import com.example.mealhelper.data.dao.IngredientDao;
import com.example.mealhelper.data.dao.ShoppingListDao;
import com.example.mealhelper.data.dao.ShoppingListItemDao;
import com.example.mealhelper.data.entity.IngredientEntity;
import com.example.mealhelper.data.entity.ShoppingListEntity;
import com.example.mealhelper.ui.Ingredient.IngredientViewModel;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class ShoppingLists extends ActivityBase implements ShoppingListRecyclerViewInterface {

    MealDatabase mealDatabase;
    IngredientDao ingredientDao;
    ShoppingListDao shoppingListDao;
    ShoppingListItemDao shoppingListItemDao;
    ShoppingListRecyclerAdapter adapter;
    ArrayList<ShoppingListViewModel> shoppingListViewModels = new ArrayList<>();
    RecyclerView recyclerView;
    MaterialToolbar addShoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        View contentView = LayoutInflater.from(this)
                .inflate(R.layout.activity_shopping_lists,
                        findViewById(R.id.content_frame), true);

        setupBottomNav(R.id.nav_shopping);

        mealDatabase = MealDatabase.getMealDatabase(getApplicationContext());
        ingredientDao = mealDatabase.ingredientDao();
        shoppingListDao = mealDatabase.shoppingListDao();
        shoppingListItemDao = mealDatabase.shoppingListItemDao();

        recyclerView = contentView.findViewById(R.id.shoppingListRecyclerView);
        addShoppingList = contentView.findViewById(R.id.toolbarAddList);

        adapter = new ShoppingListRecyclerAdapter(this, shoppingListViewModels, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupShoppingListViewModels();

        //This activity is where all the shopping lists will be displayed
        //User can create a new shopping list, then after clicking into it (new activity), can add ingredients to that list.
        //View the shopping lists in a recycler view. Building a new recycler View for this purpose - done


        //Creating a new list - done
        //tool bar at the top with + symbol on top right - done
        //Opens the dialog box - done
        //User can name the list whatever they want - done
        //User clicks add, check for duplicate names, then refresh the view with the list added - done

        //Add Shopping List functionality here

        addShoppingList.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_add_list){
                showAddListDialog();
                return true;
            }
            return false;
        });
    }

    private void setupShoppingListViewModels(){
        //Query the shoppingList table, and build the recycler view
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Get the Shopping Lists currently in the database
                List<ShoppingListEntity> existingLists = shoppingListDao.getAll();

                List<ShoppingListViewModel> list = new ArrayList<>();

                //Creating new ingredient view models from the list of existing ingredients
                for (ShoppingListEntity shoppingList : existingLists){
                    list.add(new ShoppingListViewModel(
                            shoppingList.getShoppingListId(),
                            shoppingList.getShoppingListName()
                    ));
                }

                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        shoppingListViewModels.clear();
                        shoppingListViewModels.addAll(list);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
    private void showAddListDialog(){

        //Dialog box when user clicks add list
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create new shopping list");

        final AutoCompleteTextView input = new AutoCompleteTextView(this);
        input.setHint("Enter Shopping List Name");

        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {

            String newShoppingList = input.getText().toString().trim();

            if (newShoppingList.isEmpty()) {
                Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
                return;
            }

            insertShoppingList(newShoppingList);
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }
    private void insertShoppingList(String name){
        //Adding a new shopping list and refreshing the recycler view
        new Thread(new Runnable() {
            @Override
            public void run() {
                ShoppingListEntity existing = shoppingListDao.getShoppingListNames(name);

                if (existing != null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Shopping List aleady exists", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                ShoppingListEntity shoppingList = new ShoppingListEntity();
                shoppingList.setShoppingListName(name);
                shoppingListDao.addList(shoppingList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Shopping List Added", Toast.LENGTH_SHORT).show();
                        setupShoppingListViewModels();
                    }
                });
            }
        }).start();
    }
    public void onItemClick(int position){
        Intent i = new Intent(ShoppingLists.this, ShoppingListDetails.class);

        i.putExtra("shoppingListId", shoppingListViewModels.get(position).getShoppingListId());
        i.putExtra("shoppingListName", shoppingListViewModels.get(position).getShoppingListName());

        startActivity(i);
    }
}