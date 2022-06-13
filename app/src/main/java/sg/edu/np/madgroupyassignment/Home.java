package sg.edu.np.madgroupyassignment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class Home extends Fragment {
    Context c;

    public Home() {
        this.c= c; // extending scope of Home cus u cant called Home.this anymore
    }

    @Nullable
    @Override // Use onCreate View for Fragments
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_parent, container, false);

        // Parent (Latest) Post RV
        ArrayList<HomeParentData> feed_data = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            HomeParentData d = new HomeParentData();
            d.post_header = "Feed Title" + i;
            d.post_desc = "Feed Desc" + i;
            d.post_author = "by Feed Author" + i;

            feed_data.add(d);
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
