package sg.edu.np.madgroupyassignment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        // Pass actual data into each ViewHolder
        holder.post_header.setText(d.post_header);
        holder.post_desc.setText(d.post_desc);
        holder.post_author.setText(d.post_author);

    }

    @Override
    public int getItemCount() {
        return data.size(); //array list size ;
    }

}
