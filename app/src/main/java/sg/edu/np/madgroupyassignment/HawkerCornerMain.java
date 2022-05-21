package sg.edu.np.madgroupyassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

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

        //Making of search bar
        SearchView hcmainsearch = findViewById(R.id.hawkercornersearch);
        hcmainsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }

    //A method to filter the list of hawker stalls
    public void filterList(String text) {
        ArrayList<HawkerCornerStalls> filteredList = new ArrayList<>();
        //Checks if the user enters anything that matches stall name or user name, match will be added to filter
        for (HawkerCornerStalls hcs : stallsList){
            if (hcs.hcstallname.toLowerCase().contains(text.toLowerCase()) || hcs.hcauthor.toLowerCase().contains(text.toLowerCase())){
                filteredList.add(hcs);
            }
        }

        //If the filtered list is empty, meaning no matches, show toast
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No Matching Stalls or Users", Toast.LENGTH_LONG).show();
        }
        //Else, set the recyclerview's adapter to filteredlist
        else{
            RecyclerView hcmrvforfilter = findViewById(R.id.hawkercornerrv);
            HCMainsAdapter filteredstalls = new HCMainsAdapter(filteredList);
            hcmrvforfilter.setAdapter(filteredstalls);
        }
    }
}