package sg.edu.np.madgroupyassignment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {
    //ArrayList<HomeAdapter> data2;
    ArrayList<HomeData> data = new ArrayList<>();
    Context c;
    public HomeAdapter(Context c, ArrayList<HomeData> data, Integer pos){
        //this.data2 = data2; //"this" refers to this class
        this.c = c;
        this.data = data;
    }
    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item;
        item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_home_topposts, null, false);
        return new HomeViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        //onBindViewHolder populates viewholder with data
        //position is which index in arraylist is coming up now and has to be populated
        HomeData d = data.get(position);
        holder.tp_header.setText(d.tp_header);
    }



    @Override
    public int getItemCount() {
        return data.size(); //array list size ;
    }
}
