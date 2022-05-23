package sg.edu.np.madgroupyassignment;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder>{

    // creating a variable for array list and context.
    private ArrayList<RecipeCorner> recipeArrayList;
    private Context context;

    // creating a constructor for our variables.
    public RecipeAdapter(ArrayList<RecipeCorner> recipeArrayList, Context context) {
        this.recipeArrayList = recipeArrayList;
        this.context = context;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_corner_layout, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        // setting data to our views of recycler view.
        RecipeCorner item = recipeArrayList.get(position);
        holder.recipeName.setText(item.getRecipeName());
        holder.recipeDesc.setText(item.getRecipeDescription());
        holder.ratingNo.setText(item.getNoOfRaters().toString());
        holder.ratingBar.setRating(item.getRecipeRating());
        holder.userName.setText("By: " + item.getUserName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, RecipeCornerPosts.class);
                i.putExtra("name", item.getRecipeName());
                i.putExtra("desc", item.getRecipeDescription());
                i.putExtra("rating", item.getNoOfRaters().toString());
                i.putExtra("ratingbar", item.getRecipeRating());
                i.putExtra("username", "By: " + item.getUserName());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return recipeArrayList.size();
    }

}