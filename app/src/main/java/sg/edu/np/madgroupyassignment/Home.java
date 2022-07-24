package sg.edu.np.madgroupyassignment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


public class Home extends Fragment {
    public static ArrayList<HomeMixData> randomMixList;
    Context c;

    public Home() {
        this.c = c; // extending scope of Home cus u cant called Home.this anymore
    }

    @Nullable
    @Override // Use onCreate View for Fragments
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_parent, container, false);
        HomeMix homeMix = new HomeMix();
        ArrayList<HomeParentData> feed_data = new ArrayList<>();

        // Parent (Latest) Post RV
        ArrayList<HomeMixData> sortedMixList = homeMix.filterDT();

        // Getting from Main activity if shuffled or not
        randomMixList = MainActivity.randomMixList;
        if (randomMixList != null) {
            if (randomMixList.size() > 0) { // checking if list is not empty, app wont crash
                Integer limit = randomMixList.size();
                // Setting a Limit to no. of posts in discover more section
                if (randomMixList.size() > 7) {
                    limit = 8;
                }

                for (int i = 0; i < limit; i++) {
                    //Adding the data for every ViewHolder
                    HomeParentData d = new HomeParentData();
                    if (randomMixList.get(i).identifier == true) { // if its Hawker Corner post
                        d.post_header = randomMixList.get(i).hcstallname;
                        d.post_desc = randomMixList.get(i).shortdesc;
                        d.post_author = randomMixList.get(i).hcauthor;
                        feed_data.add(d);
                    } else {// if its Recipe Corner post
                        d.post_header = randomMixList.get(i).recipeName;
                        d.post_desc = randomMixList.get(i).recipeDescription;
                        d.post_author = randomMixList.get(i).userName;
                        feed_data.add(d);
                    }
                }
            } else {
                for (int i = 1; i < 11; i++) {
                    HomeParentData d = new HomeParentData();
                    d.post_header = "Feed Title" + i;
                    d.post_desc = "Feed Desc" + i;
                    d.post_author = "by Feed Author" + i;

                    feed_data.add(d);
                }
            }
        }

        RecyclerView home_main_rv = view.findViewById(R.id.home_main_rv);
        LinearLayoutManager main_layout = new LinearLayoutManager(c, LinearLayoutManager.VERTICAL, false);
        HomeParentAdapter tp_adapter = new HomeParentAdapter(c, feed_data);

        // Giving RV adapter and layout
        home_main_rv.setAdapter(tp_adapter);
        home_main_rv.setLayoutManager(main_layout);

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.accent));   //set refresh color to accent yellow
        //swipe to refresh to refresh data
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ConnectivityManager conMan = ((ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE));
                if (conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected()) {  //if there is internet connection, update feed
                    HomeParentAdapter tp_adapter = new HomeParentAdapter(c, feed_data);
                    home_main_rv.setAdapter(tp_adapter);
                    swipeRefreshLayout.setRefreshing(false);
                }
                else{       //if no internet connection, display toast message
                    Toast.makeText(getContext(), "Please check your internet and try again", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        return view;
    }
}
