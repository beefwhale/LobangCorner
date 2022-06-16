package sg.edu.np.madgroupyassignment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeChildAdapter extends RecyclerView.Adapter<HomeChildViewHolder> {
    PostsHolder postsHolder;
    ArrayList<HomeChildData> data = new ArrayList<>();
    Context c;

    public HomeChildAdapter(Context c, ArrayList<HomeChildData> data){
        this.c = c;
        this.data = data;
    }

    @NonNull
    @Override
    public HomeChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item;
        // Inflates child RV (LP) layout
        item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_home_latestposts, null, false);
        return new HomeChildViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeChildViewHolder holder, int position) {
        // gets HomeData object with given position in the dataset
        HomeChildData d = data.get(position);
        HomeMix homeMix = new HomeMix();
        ArrayList<HomeMixData> sortedMixList = homeMix.filterDT(); // List with LP info
        // Pass actual data into each ViewHolder
        if (sortedMixList.size() > 0){
            HomeMixData sortedMixListItem = sortedMixList.get(holder.getAdapterPosition());
            if (sortedMixList.get(holder.getAdapterPosition()).identifier == true) {// If hawker post
                Picasso.get().load(sortedMixListItem.hccoverimg).into(holder.post_img);
            }
            else{ //If recipe post
                Picasso.get().load(sortedMixListItem.foodImage).into(holder.post_img);
            }
        }

        // Dark Tint behind Text
        holder.post_header.setBackgroundColor(Color.argb(130, 0, 0, 0));
        holder.post_desc.setBackgroundColor(Color.argb(130, 0, 0, 0));
        holder.post_author.setBackgroundColor(Color.argb(130, 0, 0, 0));

        holder.post_header.setText(d.post_header);
        holder.post_desc.setText(d.post_desc);
        holder.post_author.setText("By: "+d.post_author);
        holder.post_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating activity context to for the view, starting new fragment when view is clicked
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                //Bundle to pass info to fragment as intent cannot
                if (sortedMixList.get(holder.getAdapterPosition()).identifier == true){ // If hawker post
                    Fragment chosenfragment = new HCChosenStall();
                    Bundle bundle = new Bundle();
                    bundle.putInt("stallposition", holder.getAdapterPosition());
                    bundle.putInt("HomeDataCheck", 1); // if from Home, number = 1
                    bundle.putParcelable("list", Parcels.wrap(sortedMixList));
                    chosenfragment.setArguments(bundle);

                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, chosenfragment).commit();
                }
                else{
                    Fragment rcpFragment = new RecipeCornerPosts();
                    Bundle bundle = new Bundle();
                    bundle.putInt("recipeNo", holder.getAdapterPosition());
                    bundle.putInt("HomeDataCheck", 1); // if from Home, number = 1
                    bundle.putParcelable("list", Parcels.wrap(sortedMixList));
                    rcpFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.MainFragment, rcpFragment).addToBackStack(null).commit();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size(); //array list size ;
    }

}
