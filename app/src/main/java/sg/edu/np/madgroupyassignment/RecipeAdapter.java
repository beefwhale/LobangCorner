package sg.edu.np.madgroupyassignment;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder>{

    // creating a variable for array list and context.
    private ArrayList<RecipeCorner> recipeArrayList;
    private Context context;
    private Integer count;

    // creating a constructor for our variables.
    public RecipeAdapter(ArrayList<RecipeCorner> recipeArrayList, Context context) {
        this.recipeArrayList = recipeArrayList;
        this.context = context;
        this.count = count;
    }

    public void sort(ArrayList<RecipeCorner> sortllist){
        recipeArrayList = sortllist;
        notifyDataSetChanged();
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<RecipeCorner> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        recipeArrayList = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_corner_layout, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        // setting data to our views of recycler view.
        RecipeCorner item = recipeArrayList.get(position);
        //Log.d("list name", item.recipeName);

        holder.recipeName.setText(item.recipeName);
        holder.recipeDesc.setText(item.getRecipeDescription());
        holder.ratingNo.setText(item.getNoOfRaters().toString());
        holder.ratingBar.setRating(item.getRecipeRating());
        holder.userName.setText("By: " + item.getUserName());
        //idk how to pass the image to the posts activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment rcpFragment = new RecipeCornerPosts();
                Bundle bundle = new Bundle();
                bundle.putInt("recipeNo", holder.getAdapterPosition());
                rcpFragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.MainFragment, rcpFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return recipeArrayList.size();
    }
}