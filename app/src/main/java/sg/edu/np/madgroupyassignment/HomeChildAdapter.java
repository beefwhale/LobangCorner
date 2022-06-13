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

    int[] postType = postsHolder.getLatestPostsType();

    public HomeChildAdapter(Context c, ArrayList<HomeChildData> data){
        this.c = c;
        this.data = data;
        Log.i("test", Arrays.toString(postType));
    }

    @NonNull
    @Override
    public HomeChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item;
        // Inflates child RV (Toppost) layout
        item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_home_latestposts, null, false);
        return new HomeChildViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeChildViewHolder holder, int position) {
        // gets HomeData object with given position in the dataset
        HomeChildData d = data.get(position);
        // Pass actual data into each ViewHolder
        holder.tp_header.setText(d.tp_header);

    }

    @Override
    public int getItemCount() {
        return data.size(); //array list size ;
    }

}
