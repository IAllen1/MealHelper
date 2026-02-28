package com.example.mealhelper.ui.ShoppingList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealhelper.R;
import com.example.mealhelper.ui.Ingredient.IngredientRecyclerAdapter;
import com.example.mealhelper.ui.Ingredient.IngredientRecyclerViewInterface;
import com.example.mealhelper.ui.Ingredient.IngredientViewModel;

import java.util.ArrayList;

public class ShoppingListRecyclerAdapter extends RecyclerView.Adapter<ShoppingListRecyclerAdapter.MyViewHolder>  {

    private final ShoppingListRecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<ShoppingListViewModel> shoppingListViewModels;

    public ShoppingListRecyclerAdapter (Context context, ArrayList<ShoppingListViewModel> shoppingListViewModels, ShoppingListRecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.shoppingListViewModels = shoppingListViewModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ShoppingListRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.shopping_lists_recycler_view_layout, parent, false);

        return new ShoppingListRecyclerAdapter.MyViewHolder(view, recyclerViewInterface);
    }
    @Override
    public void onBindViewHolder(@NonNull ShoppingListRecyclerAdapter.MyViewHolder holder, int position) {
        ShoppingListViewModel shoppingList = shoppingListViewModels.get(position);
        holder.shoppingListName.setText(shoppingList.getShoppingListName());
    }
    @Override
    public int getItemCount() {
        return shoppingListViewModels.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView shoppingListName;

        public MyViewHolder(@NonNull View itemView, ShoppingListRecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            shoppingListName = itemView.findViewById(R.id.txtShoppingListName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
