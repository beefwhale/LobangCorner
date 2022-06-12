package sg.edu.np.madgroupyassignment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Top post RV Adapter for Home page
 */
public class HomeParentAdapter extends RecyclerView.Adapter<HomeParentViewHolder> {
    ArrayList<HomeParentData> data;
    Context c;
    //View chosen_view;

    public HomeParentAdapter(Context c, ArrayList<HomeParentData> data){
        this.data = data;
        this.c = c;
    }

    @Override
    //getting position of viewholder, if its 0, return 0 else return 1
    public int getItemViewType(int position) {
        return (position == 0)?0:1;
    }


    @NonNull
    @Override
    public HomeParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Setting layout file and doing rv of nested layout within parent RV if posiiton of viewholder == 0
        View item;
        if (viewType == 0){
            //Inflating child layout within Parent RV
            item = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_home_child, null, false);

            // Making Child RV (TOPPOST) for child layout within parent RV
            ArrayList<HomeChildData> data = new ArrayList<>();
            for (int i = 1; i < 6; i++) {

                //Adding the data for every ViewHolder
                HomeChildData d = new HomeChildData();
                d.tp_header = "Insert Title" + i;
                data.add(d);
            }
            //Rounding corners of ImageView buttons
            ImageView exploreHC = item.findViewById(R.id.home_hc_btn);
            ImageView exploreRC = item.findViewById(R.id.home_rc_btn);
            exploreHC.setClipToOutline(true);
            exploreRC.setClipToOutline(true);

            //Making my Explore page image views clickable
            exploreRC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*View view = MainActivity.bottomNavigationView.findViewById(R.id.rc);
                    view.performClick();*/
                    AppCompatActivity activity = (AppCompatActivity) parent.getContext();
                    RecipeCornerMain rcmain = new RecipeCornerMain();
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.MainFragment, rcmain).addToBackStack(null).commit();
                    MainActivity.bottomNavigationView.getMenu().findItem(R.id.rc).setChecked(true);
                    //MainActivity.bottomNavigationView.setSelectedItemId(R.id.rc);
                }
            });
            exploreHC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) parent.getContext();
                    HawkerCornerMain hcmain = new HawkerCornerMain();
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.MainFragment, hcmain).addToBackStack(null).commit();
                    MainActivity.bottomNavigationView.getMenu().findItem(R.id.hc).setChecked(true);
                }
            });

            RecyclerView tp_rv = item.findViewById(R.id.tp_rv);
            LinearLayoutManager tp_layout = new LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false);
            HomeChildAdapter tp_adapter = new HomeChildAdapter(c,data);

            tp_rv.setAdapter(tp_adapter);
            tp_rv.setLayoutManager(tp_layout);

        }
        else{
            // If index != 0  means Parent RV layout file to be inflated
            item = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_home_latestposts, null, false);
        }

        return new HomeParentViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeParentViewHolder holder, int position) {
        //onBindViewHolder populates viewholder with data
        //position is which index in arraylist is coming up now and has to be populated

        //Position has to be > 0 cus when position == 0, it is not the RV layout data thus null error
        if (position != 0){
            // First view holder, where child layout is in
            // But now position is 1, our data has to start from 0 for parent RV data thus -1
            HomeParentData p = data.get(position-1);
            holder.tp_header.setText(p.tp_header);
        }

    }
    @Override
    public int getItemCount() {
        // return .size() +1 cus we used one viewholder in parent RV to put in child layout.
        // but we still want 10 latest posts therefore we need get this list to only stop when its +1 in size to make up for used VH
        return data.size()+1 ;
    }
}
