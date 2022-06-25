package sg.edu.np.madgroupyassignment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;

public class Home extends Fragment {
    Context c;
    public Home() {
        this.c= c; // extending scope of Home cus u cant called Home.this anymore
    }

    @Nullable
    @Override // Use onCreate View for Fragments
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_parent, container, false);
        HomeMix homeMix = new HomeMix();

        // Parent (Latest) Post RV
        ArrayList<HomeParentData> feed_data = new ArrayList<>();
        ArrayList<HomeMixData> randomMixList = homeMix.RandomData();
        ArrayList<HomeMixData> sortedMixList = homeMix.filterDT();
//        for (HomeMixData i : randomMixList){
//            Log.e("lol2", sortedMixList.size() + "");
//            if (sortedMixList.contains(i) == true){
//                Log.e("lol", sortedMixList.size() + "");
//                randomMixList.remove(i);
//            }
//        }

        if (randomMixList.size() > 0){ // checking if list is not empty, app wont crash
            Integer limit = randomMixList.size();
            // Setting a Limit to no. of posts in discover more section
            if (randomMixList.size()>7){
                limit = 8;
            }

            for (int i = 0; i < limit; i++)  {
                //Adding the data for every ViewHolder
                HomeParentData d = new HomeParentData();
                if (randomMixList.get(i).identifier == true) { // if its Hawker Corner post
                    d.post_header = randomMixList.get(i).hcstallname;
                    d.post_desc = randomMixList.get(i).shortdesc;
                    d.post_author = randomMixList.get(i).hcauthor;
                    feed_data.add(d);
                }
                else{// if its Recipe Corner post
                    d.post_header = randomMixList.get(i).recipeName;
                    d.post_desc = randomMixList.get(i).recipeDescription;
                    d.post_author = randomMixList.get(i).userName;
                    feed_data.add(d);
                }
            }
        }
        else{
            for (int i = 1; i < 11; i++) {
                HomeParentData d = new HomeParentData();
                d.post_header = "Feed Title" + i;
                d.post_desc = "Feed Desc" + i;
                d.post_author = "by Feed Author" + i;

                feed_data.add(d);
            }
        }
        RecyclerView home_main_rv = view.findViewById(R.id.home_main_rv);
        LinearLayoutManager main_layout = new LinearLayoutManager(c, LinearLayoutManager.VERTICAL, false);
        HomeParentAdapter tp_adapter = new HomeParentAdapter(c,feed_data);

        // Giving RV adapter and layout
        home_main_rv.setAdapter(tp_adapter);
        home_main_rv.setLayoutManager(main_layout);
        return view;
    }
}
