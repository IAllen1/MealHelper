package com.example.mealhelper.ui.Ingredient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealhelper.R;

import java.util.ArrayList;

public class IngredientRecyclerAdapter extends RecyclerView.Adapter<IngredientRecyclerAdapter.MyViewHolder> {

    private final IngredientRecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<IngredientViewModel> ingredientViewModels;

    public IngredientRecyclerAdapter (Context context, ArrayList<IngredientViewModel> ingredientViewModels, IngredientRecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.ingredientViewModels = ingredientViewModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public IngredientRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ingredient_recycler_view_layout, parent, false);

        return new IngredientRecyclerAdapter.MyViewHolder(view, recyclerViewInterface);
    }
    @Override
    public void onBindViewHolder(@NonNull IngredientRecyclerAdapter.MyViewHolder holder, int position) {
        IngredientViewModel ingredient = ingredientViewModels.get(position);
        holder.ingredientName.setText(ingredient.getIngredientName());
        holder.itemView.setSelected(ingredient.isChecked());
    }
    @Override
    public int getItemCount() {
        return ingredientViewModels.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView ingredientName;

        public MyViewHolder(@NonNull View itemView, IngredientRecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            ingredientName = itemView.findViewById(R.id.txtIngredientName);

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
