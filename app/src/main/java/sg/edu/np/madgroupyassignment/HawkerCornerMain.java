package sg.edu.np.madgroupyassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class HawkerCornerMain extends AppCompatActivity {
    //Class for the Main Page of Hawker Corner.

    //Set list to static so accessible by all class.
    public static ArrayList<HawkerCornerStalls> stallsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hawker_corner_main);

        //Creating List of hawker corner stalls for display !!- Need to change to actual stalls, how to change pic + user's?
        for (int i = 0; i < 10; i++) {
            HawkerCornerStalls newstall = new HawkerCornerStalls();
            newstall.hcstallname = "Stall " + i;
            newstall.hcauthor = "Author " + i;
            stallsList.add(newstall);
        }

        //Defining Recycler View info & Setting Layout and Adapter.
        RecyclerView hcmainrv = findViewById(R.id.hawkercornerrv);
        HCMainsAdapter hcadapter = new HCMainsAdapter(stallsList);
        LinearLayoutManager hclayout = new LinearLayoutManager(this);

        hcmainrv.setAdapter(hcadapter);
        hcmainrv.setLayoutManager(hclayout);


    }
}