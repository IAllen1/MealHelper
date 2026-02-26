package com.example.mealhelper.ui.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealhelper.R;

import java.util.ArrayList;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeRecyclerAdapter.MyViewHolder>{

    private final RecipeRecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<RecipeViewModel> recipeViewModels;

    public RecipeRecyclerAdapter (Context context, ArrayList<RecipeViewModel> recipeViewModels, RecipeRecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.recipeViewModels = recipeViewModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public RecipeRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_recycler_view_layout, parent, false);

        return new RecipeRecyclerAdapter.MyViewHolder(view, recyclerViewInterface);
    }
    @Override
    public void onBindViewHolder(@NonNull RecipeRecyclerAdapter.MyViewHolder holder, int position) {
        RecipeViewModel recipe = recipeViewModels.get(position);
        holder.recipeName.setText(recipe.getRecipeName());
    }
    @Override
    public int getItemCount() {
        return recipeViewModels.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView recipeName;

        public MyViewHolder(@NonNull View itemView, RecipeRecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            recipeName = itemView.findViewById(R.id.txtRecipeName);

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
