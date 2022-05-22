package sg.edu.np.madgroupyassignment;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Top post RV Adapter for Home page
 */
public class ToppostAdapter extends RecyclerView.Adapter<ToppostViewHolder> {
    ArrayList<ToppostData> data;
    Context c;

    public ToppostAdapter(Context c, ArrayList<ToppostData> data){
        this.data = data; //"this" refers to this class
        this.c = c;
    }

    @NonNull
    @Override
    public ToppostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //creating the layout here
        //parent is the parameter, provided. It is where we nest recycler view.
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_home_topposts, null, false);
        View item2 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_home_latestposts, null, false);
        return new ToppostViewHolder(item, item2);
    }

    @Override
    public void onBindViewHolder(@NonNull ToppostViewHolder holder, int position) {
        //onBindViewHolder populates viewholder with data
        //position is which index in arraylist is coming up now and has to be populated
        ToppostData p = data.get(position);
        //Setting text for our name
        holder.tp_header.setText(p.tp_header);
    }

    @Override
    public int getItemCount() {
        return data.size(); //array list size ;
    }
}
