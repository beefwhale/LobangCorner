package sg.edu.np.madgroupyassignment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        HomeMix homeMix = new HomeMix();
        if (viewType == 0){
            //Inflating child layout within Parent RV
            item = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_home_child, null, false);

            // Making Child RV for child layout within parent RV
            ArrayList<HomeChildData> data = new ArrayList<>();
            ArrayList<HomeMixData> sortedMixList = homeMix.filterDT();

            for (int i = 0; i < 5; i++) {
                //Adding the data for every ViewHolder
                HomeChildData d = new HomeChildData();
                if (sortedMixList.size() > 0){ // checking if list is not empty, app wont crash
                    if (sortedMixList.get(i).identifier == true){ // if its Hawker Corner post
                        d.post_header = sortedMixList.get(i).hcstallname;
                        d.post_desc = "LP Desc" + i;
//                    d.post_desc = mixData.get(i).shortdesc;
                        d.post_author = sortedMixList.get(i).hcauthor;
                        data.add(d);
                    }
                    else{// if its Recipe Corner post
                        d.post_header = sortedMixList.get(i).recipeName;
                        d.post_desc = sortedMixList.get(i).recipeDescription;
                        d.post_author = sortedMixList.get(i).owner;
                        data.add(d);
                    }
                }
                else{
                    d.post_header = "LP Title" + i;
                    d.post_desc = "LP Desc" + i;
//                    d.post_desc = mixData.get(i).shortdesc;
                    d.post_author = "LP Author" + i;
                    data.add(d);
                }

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
                    //MainActivity.bottomNavigationView.setSelectedItemId(R.id.rc);

                    AppCompatActivity activity = (AppCompatActivity) parent.getContext();
                    RecipeCornerMain rcmain = new RecipeCornerMain();
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.MainFragment, rcmain).addToBackStack(null).commit();
                    // making bottom nav bar appear checked
                    MainActivity.bottomNavigationView.getMenu().findItem(R.id.rc).setChecked(true);
                }
            });
            exploreHC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) parent.getContext();
                    HawkerCornerMain hcmain = new HawkerCornerMain();
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.MainFragment, hcmain).addToBackStack(null).commit();
                    // making bottom nav bar appear checked
                    MainActivity.bottomNavigationView.getMenu().findItem(R.id.hc).setChecked(true);
                }
            });
            // Child RV
            RecyclerView lp_rv = item.findViewById(R.id.lp_rv);
            LinearLayoutManager lp_layout = new LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false);
            HomeChildAdapter lp_adapter = new HomeChildAdapter(c,data);

            lp_rv.setAdapter(lp_adapter);
            lp_rv.setLayoutManager(lp_layout);

        }
        else{
            // If index != 0  means Parent RV layout file to be inflated
            item = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_home_feedposts, null, false);
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
            holder.post_header.setText(p.post_header);
            holder.post_desc.setText(p.post_desc);
            holder.post_author.setText(p.post_author);
        }

    }
    @Override
    public int getItemCount() {
        // return .size() +1 cus we used one viewholder in parent RV to put in child layout.
        // but we still want 10 latest posts therefore we need get this list to only stop when its +1 in size to make up for used VH
        return data.size()+1 ;
    }
}
