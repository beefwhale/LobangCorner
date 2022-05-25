package sg.edu.np.madgroupyassignment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
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
        View view = inflater.inflate(R.layout.activity_home, container, false);
        //*View lp_view = view.findViewById(R.id.lp_layout);

        Integer pos;

        // Top Post RV
        ArrayList<ToppostData> data = new ArrayList<>();
        for (int i = 1; i < 6; i++) {

            ToppostData d = new ToppostData();
            d.tp_header = "Insert Title" + i;
            //d.tp_img = "Description" + randdesc;
            data.add(d);
        }
        pos = data.size();
        RecyclerView rv = view.findViewById(R.id.tp_rv);
        LinearLayoutManager layout = new LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false);
        ToppostAdapter adapter = new ToppostAdapter(c, data, pos);

        // Giving Top Post RV adapter and layout
        rv.setAdapter(adapter);
        rv.setLayoutManager(layout);

        // Latest Post RV
        ArrayList<ToppostData> lp_data = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            ToppostData d = new ToppostData();
            d.tp_header = "egg" + i;
            lp_data.add(d);
        }

        pos = lp_data.size();
        RecyclerView lp_rv = view.findViewById(R.id.lp_rv);
        LinearLayoutManager lp_layout = new LinearLayoutManager(c, LinearLayoutManager.VERTICAL, false);
        ToppostAdapter lp_adapter = new ToppostAdapter(c,lp_data, pos);

        // Giving RV adapter and layout
        lp_rv.setLayoutManager(lp_layout);
        lp_rv.setAdapter(lp_adapter);

        return view;
    }
}
