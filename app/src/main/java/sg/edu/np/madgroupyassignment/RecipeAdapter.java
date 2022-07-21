package sg.edu.np.madgroupyassignment;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Random;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder>{

    // creating a variable for array list and context.
    private ArrayList<RecipeCorner> recipeArrayList;
    private Context context;
    Integer status;
    ArrayList<RecipeCorner> del_rcplist = new ArrayList<>();
    Boolean aBoolean = false;

    //For editing
    public Integer cbCount = 0 ;
    public ArrayList<Integer> listPos = new ArrayList<>();
    Integer toRemove;

    // creating a constructor for our variables.
    // 0 = no check box, 1 = got checkbox, bookmark page , 2 = got checkbook, edit page
    public RecipeAdapter(ArrayList<RecipeCorner> recipeArrayList, Context context, Integer status) {
        this.recipeArrayList = recipeArrayList;
        this.context = context;
        this.status = status;
    }

    // method called from recipeCornerMain to sort list and update the data
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

    //method called from recipeCornerMain to 'unsort' list when "sort by" is clicked
    public void unsort(ArrayList<RecipeCorner> recipeList){
        recipeArrayList = recipeList;
        notifyDataSetChanged();
    }

    //oncreateviewholder method
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view;
        // 0 is without checkbox
        if (status == 0)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_corner_layout, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_profile_rc_card, parent, false);

        return new RecipeViewHolder(view);
    }

    //onbindviewholder method
    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        // setting data to our views of recycler view.
        RecipeCorner item = recipeArrayList.get(position);

        holder.recipeName.setText(item.recipeName);         //set textview to recipename
        holder.recipeDesc.setText(item.getRecipeDescription()); //set textview to recipedescription
        holder.ratingBar.setRating(item.getRecipeRating()); //set ratingbar to recipe difficulty level
        holder.userName.setText("By: " + item.getUserName());   //set textview to username
        Picasso.get().load(item.getFoodImage()).into(holder.foodImage); //load image from database to imageview
        holder.itemView.setOnClickListener(new View.OnClickListener() { //when item is clicked, lead user to recipeposts
            @Override
            public void onClick(View view) {
                //use bundle to send data from the item to the recipepost
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment rcpFragment = new RecipeCornerPosts();
                Bundle bundle = new Bundle();
                bundle.putInt("recipeNo", holder.getAdapterPosition());
                bundle.putParcelable("list", Parcels.wrap(recipeArrayList));
                rcpFragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.MainFragment, rcpFragment).addToBackStack(null).commit();
            }
        });

        // for bookmark page
        if (status==1){
            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (del_rcplist.contains(item)){
                        //holder.checkbox.setChecked(false);
                        aBoolean = false;
                        del_rcplist.remove(item);
                    }
                    else{
                        //holder.checkbox.setChecked(true);
                        aBoolean = true;
                        del_rcplist.add(item);
                    }
                }
            });
        }
        else if (status == 2){
            //Setting all as default unselected
            item.setChecked(false);
            //DESELECTION : if removed from listPos, updating checkbox for every card
            if (listPos.contains(holder.getAdapterPosition()) == false){
                holder.checkbox.setChecked(false);
            }
            else{
                holder.checkbox.setChecked(true);
            }
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getChecked() == true) {
                        item.setChecked(false);
                        cbCount = cbCount - 1;
                        for (Integer i:listPos){ //finding integer within list
                            if (i.equals(holder.getAdapterPosition())){
                                toRemove = i; // getting interger position
                            }
                        }
                        //Remove integer from list using integer position
                        listPos.remove(toRemove);
                    }
                    else {
                        item.setChecked(true);
                        cbCount = cbCount + 1;
                        //Add to list of checked using adapter position
                        listPos.add(holder.getAdapterPosition());
                    }
                }
            });
        }
    }

    public ArrayList<RecipeCorner> getDel_rcplist() {
        return del_rcplist;
    }

    public void delete(ArrayList<RecipeCorner> deletelist){
        recipeArrayList = deletelist;
        notifyDataSetChanged();
    }

    //display all the items of the array list in recyclerview
    @Override
    public int getItemCount() {
        // returning the size of array list.
        return recipeArrayList.size();
    }
}