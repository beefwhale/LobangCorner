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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//Class is a fragment and will implement some methods for filtering
public class HawkerCornerMain extends Fragment implements AdapterView.OnItemSelectedListener {
    //Class for the Main Page of Hawker Corner.

    //Set list to static so accessible by all class.
    public static ArrayList<HawkerCornerStalls> stallsList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_hawker_corner_main, parent, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        //Populate hawker corner's stallList, temporary and will change to get data from firebase
        //Will create method to retrieve from db
        ArrayList<HawkerCornerStalls> hcDBList = new ArrayList<>();
        //If empty, means havent populate from firebase
        if (stallsList.isEmpty()){
            for (int i = 0; i < 10; i++) {
                HawkerCornerStalls newstall = new HawkerCornerStalls();
                newstall.hcstallname = "Stall " + i;
                newstall.hcauthor = "Author " + i;
                stallsList.add(newstall);
            }
        }
        //If not empty, check if size is same, if not same, means new stalls, repopulate
        else if (stallsList.size() != hcDBList.size()){

        }
        //Not empty and same size, no new stalls
        else{

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

        //Making of Spinner Filter
        Spinner hcmainSpinner = view.findViewById(R.id.hcmainspinner);

        //Getting spinner filters and making Array Adapter to configure the spinner
        String[] hcmainFilters = getResources().getStringArray(R.array.filters);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, hcmainFilters);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hcmainSpinner.setAdapter(adapter);
        hcmainSpinner.setOnItemSelectedListener(this);
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

    //Two methods to override from the superclass, methods for filtering
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                break;
            case 1:
                Collections.sort(stallsList, new Comparator<HawkerCornerStalls>() {
                    @Override
                    public int compare(HawkerCornerStalls stall1, HawkerCornerStalls stall2) {
                        return stall1.hcstallname.compareToIgnoreCase(stall2.hcstallname);
                    }
                });
            case 2:
                Collections.sort(stallsList, new Comparator<HawkerCornerStalls>() {
                    @Override
                    public int compare(HawkerCornerStalls stall1, HawkerCornerStalls stall2) {
                        return stall1.hcstallname.compareToIgnoreCase(stall2.hcstallname);
                    }
                });
                Collections.reverse(stallsList);
            case 3:
                Collections.sort(stallsList, new Comparator<HawkerCornerStalls>() {
                    @Override
                    public int compare(HawkerCornerStalls stall1, HawkerCornerStalls stall2) {
                        return stall1.hcauthor.compareToIgnoreCase(stall2.hcauthor);
                    }
                });
            case 4:
                Collections.sort(stallsList, new Comparator<HawkerCornerStalls>() {
                    @Override
                    public int compare(HawkerCornerStalls stall1, HawkerCornerStalls stall2) {
                        return stall1.hcauthor.compareToIgnoreCase(stall2.hcauthor);
                    }
                });
                Collections.reverse(stallsList);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}