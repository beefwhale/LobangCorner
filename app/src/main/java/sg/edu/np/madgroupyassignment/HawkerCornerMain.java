package sg.edu.np.madgroupyassignment;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//Class for hawker corner main page is a fragment
public class HawkerCornerMain extends Fragment implements AdapterView.OnItemSelectedListener {

    //Variables or views needed
    PostsHolder postsHolder;
    public ArrayList<HawkerCornerStalls> stallsList = new ArrayList<>();

    //Recyclerview & Adapter so different functions can access
    RecyclerView hcmainrv;
    HCMainsAdapter hcadapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_hawker_corner_main, parent, false);

        stallsList.removeAll(stallsList);
        for (HawkerCornerStalls obj : postsHolder.getHawkerPosts()) {
            stallsList.add(obj);
        }

        //Reverse list to display newest
        Collections.reverse(stallsList);

        //Defining Recycler View info & Setting Layout and Adapter.
        hcmainrv = view.findViewById(R.id.hawkercornerrv);
        hcadapter = new HCMainsAdapter(stallsList, true, getActivity());
        LinearLayoutManager hclayout = new LinearLayoutManager(view.getContext());

        hcmainrv.setAdapter(hcadapter);
        hcmainrv.setLayoutManager(hclayout);

        //Making of search bar
        SearchView hcmainsearch = view.findViewById(R.id.hawkercornersearch);
        hcmainsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (filterList(query, view).isEmpty()) {
                    Toast.makeText(view.getContext(), "No Matching Stalls or Users", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText, view);
                return true;
            }
        });

        //Making of Spinner Filter
        Spinner hcmainSpinner = view.findViewById(R.id.hcmainspinner);

        //Getting spinner filters and making Array Adapter to configure the spinner's drop down
        String[] hcmainFilters = getResources().getStringArray(R.array.filters);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, hcmainFilters);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hcmainSpinner.setAdapter(adapter);
        hcmainSpinner.setOnItemSelectedListener(this);

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        //swipe to refresh to refresh data
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                stallsList.removeAll(stallsList);
                for (HawkerCornerStalls obj : postsHolder.getHawkerPosts()) {
                    stallsList.add(obj);
                }
                Collections.reverse(stallsList);
                hcadapter = new HCMainsAdapter(stallsList, true, getActivity());
                hcmainrv.setAdapter(hcadapter);
                hcmainsearch.setQuery("", false);
                hcmainSpinner.setSelection(0);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    //A method to filter the list of hawker stalls
    public ArrayList<HawkerCornerStalls> filterList(String text, View view) {
        ArrayList<HawkerCornerStalls> filteredList = new ArrayList<>();
        //Checks if the user enters anything that matches stall name or user name, match will be added to filter
        for (HawkerCornerStalls hcs : stallsList) {
            if (hcs.hcstallname.toLowerCase().contains(text.toLowerCase()) || hcs.hcauthor.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(hcs);
            }
        }

        hcmainrv = view.findViewById(R.id.hawkercornerrv);
        hcadapter = new HCMainsAdapter(filteredList, true, getActivity());
        hcmainrv.setAdapter(hcadapter);

        return filteredList;
    }

    //Two methods to override from the superclass, methods for filtering in spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0: //Default - Sort By, does nothing
                break;
            case 1: //Filter based on stall name ascending
                stallsList = sortListByStall();
                hcadapter.sortChange(stallsList);
                break;
            case 2: //Filter based on stall name descending
                stallsList = sortListByStall();
                Collections.reverse(stallsList);
                hcadapter.sortChange(stallsList);
                break;
            case 3: //Filter based on author name ascending
                stallsList = sortListByAuthor();
                hcadapter.sortChange(stallsList);
                break;
            case 4: //Filter based on author name descending
                stallsList = sortListByAuthor();
                Collections.reverse(stallsList);
                hcadapter.sortChange(stallsList);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //Methods to be used in spinner switch-case, can be reused if adding more filters
    public ArrayList<HawkerCornerStalls> sortListByStall() {
        Collections.sort(stallsList, new Comparator<HawkerCornerStalls>() {
            @Override
            public int compare(HawkerCornerStalls stall1, HawkerCornerStalls stall2) {
                return stall1.hcstallname.compareToIgnoreCase(stall2.hcstallname);
            }
        });
        return stallsList;
    }

    public ArrayList<HawkerCornerStalls> sortListByAuthor() {
        Collections.sort(stallsList, new Comparator<HawkerCornerStalls>() {
            @Override
            public int compare(HawkerCornerStalls stall1, HawkerCornerStalls stall2) {
                return stall1.hcauthor.compareToIgnoreCase(stall2.hcauthor);
            }
        });
        return stallsList;
    }

}