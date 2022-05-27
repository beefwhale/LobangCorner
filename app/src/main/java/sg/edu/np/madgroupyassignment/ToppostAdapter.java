package sg.edu.np.madgroupyassignment;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Top post RV Adapter for Home page
 */
public class ToppostAdapter extends RecyclerView.Adapter<ToppostViewHolder> {
    ArrayList<ToppostData> data;
    Context c;
    //View chosen_view;
    Integer pos;

    public ToppostAdapter(Context c, ArrayList<ToppostData> data, Integer pos){
        this.data = data; //"this" refers to this class
        this.c = c;
        this.pos = pos;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0)?0:1;
    }

    @NonNull
    @Override
    public ToppostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //creating the layout here
        //parent is the parameter, provided. It is where we nest recycler view.

        //Setting layout file and adapter if viewposition ==0
        View item;
        if (viewType == 0){
            item = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_home_weekly, null, false);
            Integer pos;

            // Adding Inner(Toppost) RV
            ArrayList<HomeData> data = new ArrayList<>();
            for (int i = 1; i < 6; i++) {

                HomeData d = new HomeData();
                d.tp_header = "Insert Title" + i;
                //d.tp_img = "Description" + randdesc;
                data.add(d);
            }
            pos = data.size();
            RecyclerView tp_rv = item.findViewById(R.id.tp_rv);
            LinearLayoutManager tp_layout = new LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false);
            HomeAdapter tp_adapter = new HomeAdapter(c,data, pos);


            tp_rv.setAdapter(tp_adapter);
            tp_rv.setLayoutManager(tp_layout);

        }
        else{
            item = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_home_latestposts, null, false);
        }

        return new ToppostViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ToppostViewHolder holder, int position) {
        //onBindViewHolder populates viewholder with data
        //position is which index in arraylist is coming up now and has to be populated

        //Setting text for our name
        if (position != 0){
            ToppostData p = data.get(position-1);
            holder.tp_header.setText(p.tp_header);
        }


        //Creating an Instance of ParentItem class for given instance

    }


    @Override
    public int getItemCount() {
        return data.size()+1; //array list size ;
    }
}
