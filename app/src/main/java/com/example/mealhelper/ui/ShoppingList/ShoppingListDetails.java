package com.example.mealhelper.ui.ShoppingList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Paint;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.mealhelper.ActivityBase;
import com.example.mealhelper.R;
import com.example.mealhelper.data.MealDatabase;
import com.example.mealhelper.data.dao.IngredientDao;
import com.example.mealhelper.data.dao.ShoppingListDao;
import com.example.mealhelper.data.dao.ShoppingListItemDao;
import com.example.mealhelper.data.entity.IngredientEntity;
import com.example.mealhelper.data.entity.ShoppingListEntity;
import com.example.mealhelper.data.entity.ShoppingListItemEntity;
import com.example.mealhelper.ui.Ingredient.IngredientRecyclerAdapter;
import com.example.mealhelper.ui.Ingredient.IngredientRecyclerViewInterface;
import com.example.mealhelper.ui.Ingredient.IngredientViewModel;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListDetails extends ActivityBase implements IngredientRecyclerViewInterface {

    MealDatabase mealDatabase;
    IngredientDao ingredientDao;
    ShoppingListDao shoppingListDao;
    ShoppingListItemDao shoppingListItemDao;

    IngredientRecyclerAdapter adapter;

    ArrayList<IngredientViewModel> listIngredientViewModels = new ArrayList<>();

    RecyclerView recyclerView;
    MaterialToolbar addIngredient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        View contentView = LayoutInflater.from(this)
                .inflate(R.layout.activity_shopping_list_details,
                        findViewById(R.id.content_frame), true);

        setupBottomNav(-1);

        String shoppingListTitle = getIntent().getStringExtra("shoppingListName");
        int shoppingListId = getIntent().getIntExtra("shoppingListId", -1);

        //List existing ingredients, should be able to reuse the ingredients recycler adapter

        //Same as shopping lists, material tool bar with the + symbol to add new list
        //When + is clicked, open dialog box, list the existing ingredients, let the user select multiple
        //When user clicks add, run insert query and refresh the list (recycler view)


        mealDatabase = MealDatabase.getMealDatabase(getApplicationContext());
        ingredientDao = mealDatabase.ingredientDao();
        shoppingListDao = mealDatabase.shoppingListDao();
        shoppingListItemDao = mealDatabase.shoppingListItemDao();

        recyclerView = contentView.findViewById(R.id.shoppingIngredientRecyclerView);
        addIngredient = contentView.findViewById(R.id.toolbarAddIngredientToList);

        addIngredient.setTitle(shoppingListTitle);

        adapter = new IngredientRecyclerAdapter(this, listIngredientViewModels, this, R.layout.shopping_details_recycler_view_layout, true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupIngredientListViewModels(shoppingListId);

        addIngredient.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_add_list){
                showAddIngredientDialog(shoppingListId);
                return true;
            }
            return false;
        });
    }

    public void onItemClick(int position){
        //When user clicks an ingredient, it will cross out (strikethrough should be a thing)
        //Table will update the ingredientListItemID to isChecked = true
        //When user taps again, it will un-cross out
        //Table will update the ingredientListItemID to isChecked = false

        IngredientViewModel ingredient = listIngredientViewModels.get(position);
        ingredient.setChecked(!ingredient.isChecked());
        adapter.notifyItemChanged(position);
    }

    private void setupIngredientListViewModels(int listId){
        //Populate the contents of the shopping list

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Loads all the shopping list items, so this is a no no
                //Needs to load all the shopping list items based on a shopping list ID

                List<ShoppingListItemEntity> existingIngredientsinList = shoppingListItemDao.getIngredientsById(listId);

                List<IngredientViewModel> list = new ArrayList<>();

                for (ShoppingListItemEntity ingredientList : existingIngredientsinList){
                    list.add(new IngredientViewModel(
                            ingredientList.getIngredientId(),
                            ingredientDao.getIngredientNameById(ingredientList.getIngredientId()),
                            false
                    ));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listIngredientViewModels.clear();
                        listIngredientViewModels.addAll(list);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
    private void getIngredients(IngredientsCallBack callBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<IngredientEntity> ingredients = ingredientDao.getAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onResult(ingredients);
                    }
                });
            }
        }).start();
    }
    public interface IngredientsCallBack{
        void onResult(List<IngredientEntity> ingredients);
    }
    private void showAddIngredientDialog(int shoppingListId){
        //When user clicks + Symbol
        //Open Dialog Box
        //List all existing ingredients from Ingredients table

        getIngredients(ingredients ->{

            String[] ingredientNames = new String[ingredients.size()];

            for (int i = 0; i < ingredients.size(); i++) {
                ingredientNames[i] = ingredients.get(i).getIngredientName();
            }

            boolean[] checkedItems = new boolean[ingredientNames.length];
            ArrayList<Integer> selected = new ArrayList();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Add Ingredients");

            builder.setMultiChoiceItems(ingredientNames, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                    if (isChecked){
                        selected.add(which);
                    }
                    else if(selected.contains(which)){
                        selected.remove(Integer.valueOf(which));
                    }
                }
            });

            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    insertIngredientToList(ingredients, selected, shoppingListId);
                }
            });

            builder.setNegativeButton("Cancel", null);

            builder.show();
        });
    }
    private void insertIngredientToList(List<IngredientEntity> ingredients, ArrayList<Integer> selected, int shoppingListId){
        //Inserting the selected ingredients to the list
        //Insert query
        //Check if the ingredient is already in the list
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (Integer index : selected){

                    IngredientEntity ingredient = ingredients.get(index);

                    ShoppingListItemEntity shoppingListIngredient = new ShoppingListItemEntity();
                    shoppingListIngredient.setShoppingListId(shoppingListId);
                    shoppingListIngredient.setIngredientId(ingredient.getIngredientId());
                    shoppingListIngredient.setChecked(false);

                    shoppingListItemDao.addItem(shoppingListIngredient);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupIngredientListViewModels(shoppingListId);
                        Toast.makeText(getApplicationContext(), "Ingredients Added", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }).start();
    }
}