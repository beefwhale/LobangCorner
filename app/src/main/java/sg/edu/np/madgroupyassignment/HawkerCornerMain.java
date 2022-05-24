package sg.edu.np.madgroupyassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class HawkerCornerMain extends Fragment {
    //Class for the Main Page of Hawker Corner.

    //Set list to static so accessible by all class.
    public static ArrayList<HawkerCornerStalls> stallsList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_hawker_corner_main, parent, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        //Creating List of hawker corner stalls for display !!- Need to change to actual stalls, how to change pic + user's?
        for (int i = 0; i < 10; i++) {
            HawkerCornerStalls newstall = new HawkerCornerStalls();
            newstall.hcstallname = "Stall " + i;
            newstall.hcauthor = "Author " + i;
            stallsList.add(newstall);
        }

        //Defining Recycler View info & Setting Layout and Adapter.
        RecyclerView hcmainrv = view.findViewById(R.id.hawkercornerrv);
        HCMainsAdapter hcadapter = new HCMainsAdapter(stallsList);
        LinearLayoutManager hclayout = new LinearLayoutManager(view.getContext());

        hcmainrv.setAdapter(hcadapter);
        hcmainrv.setLayoutManager(hclayout);

        //Making of search bar
        SearchView hcmainsearch = view.findViewById(R.id.hawkercornersearch);
        hcmainsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText,view);
                return true;
            }
        });
    }

    //A method to filter the list of hawker stalls
    public void filterList(String text, View view) {
        ArrayList<HawkerCornerStalls> filteredList = new ArrayList<>();
        //Checks if the user enters anything that matches stall name or user name, match will be added to filter
        for (HawkerCornerStalls hcs : stallsList){
            if (hcs.hcstallname.toLowerCase().contains(text.toLowerCase()) || hcs.hcauthor.toLowerCase().contains(text.toLowerCase())){
                filteredList.add(hcs);
            }
        }

        //If the filtered list is empty, meaning no matches, show toast
        if (filteredList.isEmpty()){
            Toast.makeText(view.getContext(), "No Matching Stalls or Users", Toast.LENGTH_SHORT).show();
        }
        //Else, set the recyclerview's adapter to filteredlist
        else{
            RecyclerView hcmrvforfilter = view.findViewById(R.id.hawkercornerrv);
            HCMainsAdapter filteredstalls = new HCMainsAdapter(filteredList);
            hcmrvforfilter.setAdapter(filteredstalls);
        }
    }
}