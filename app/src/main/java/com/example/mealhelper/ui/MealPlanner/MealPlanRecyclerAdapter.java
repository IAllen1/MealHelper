package com.example.mealhelper.ui.MealPlanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealhelper.R;
import com.example.mealhelper.ui.ShoppingList.ShoppingListRecyclerAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;

public class MealPlanRecyclerAdapter extends RecyclerView.Adapter<MealPlanRecyclerAdapter.MyViewHolder>{

    private final MealPlanRecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<MealPlanViewModel> mealPlanViewModels;

    public MealPlanRecyclerAdapter (Context context, ArrayList<MealPlanViewModel> mealPlanViewModels, MealPlanRecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.mealPlanViewModels = mealPlanViewModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MealPlanRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.meal_plan_recycler_view_layout, parent, false);

        return new MealPlanRecyclerAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MealPlanRecyclerAdapter.MyViewHolder holder, int position){
        MealPlanViewModel planner = mealPlanViewModels.get(position);

        LocalDate date = LocalDate.parse(planner.getDate());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE d MMM");

        String formattedDate = date.format(formatter);

        holder.txtDate.setText(formattedDate);

        setMealText(holder.txtBreakfast, planner.getBreakfast());
        setMealText(holder.txtLunch, planner.getLunch());
        setMealText(holder.txtDinner, planner.getDinner());
        setMealText(holder.txtSupper, planner.getSupper());

        holder.txtBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerViewInterface != null) {
                    recyclerViewInterface.onItemClick(holder.getAdapterPosition(), "breakfast");
                }
            }
        });

        holder.txtLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerViewInterface != null) {
                    recyclerViewInterface.onItemClick(holder.getAdapterPosition(), "lunch");
                }
            }
        });

        holder.txtDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerViewInterface != null){
                    recyclerViewInterface.onItemClick(holder.getAdapterPosition(), "dinner");
                }
            }
        });

        holder.txtSupper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerViewInterface != null){
                    recyclerViewInterface.onItemClick(holder.getAdapterPosition(), "supper");
                }
            }
        });
    }

    @Override
    public int getItemCount(){
        return mealPlanViewModels.size();
    }

    private void setMealText(TextView textView, String meal){
        if (meal == null || meal.isEmpty()){
            textView.setText("+ Add");
        }
        else{
            textView.setText(meal);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtDate, txtBreakfast, txtLunch, txtDinner, txtSupper;

        public MyViewHolder(@NonNull View itemView, MealPlanRecyclerViewInterface recyclerViewInterface){
            super(itemView);

            txtDate = itemView.findViewById(R.id.txtMPDate);
            txtBreakfast = itemView.findViewById(R.id.txtMPBreakfast);
            txtLunch = itemView.findViewById(R.id.txtMPLunch);
            txtDinner = itemView.findViewById(R.id.txtMPDinner);
            txtSupper = itemView.findViewById(R.id.txtMPSupper);
        }
    }
}
