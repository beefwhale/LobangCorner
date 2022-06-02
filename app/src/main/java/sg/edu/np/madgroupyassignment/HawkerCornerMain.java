package sg.edu.np.madgroupyassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//Class for hawker corner main page is a fragment
public class HawkerCornerMain extends Fragment implements AdapterView.OnItemSelectedListener {

//    private FirebaseDatabase firebaseDatabase;
//    private DatabaseReference databaseReference;

    //Set list to static so accessible by all class & create list to get from Database.
    //hawker corner DBList will be used as a copy by filter to use no filter and get original order
    //stallsList will take up the database list in the codes
    public static ArrayList<HawkerCornerStalls> stallsList = new ArrayList<>();
    ArrayList<HawkerCornerStalls> hcDBList = new ArrayList<>();


    //Recyclerview & Adapter so different functions can access
    RecyclerView hcmainrv;
    HCMainsAdapter hcadapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_hawker_corner_main, parent, false);

//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference().child("Posts").child("Hawkers");
//
////        Getting hawker posts
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot objectEntry : snapshot.getChildren()) {
//                    HawkerCornerStalls hwkObject = objectEntry.getValue(HawkerCornerStalls.class);
//                    stallsList.add(hwkObject);
//                    hcadapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        //Populate hawker corner's stallList, temporary and will change to get data from firebase
        //Will create method to retrieve from db
        //If empty, means havent populate from firebase

        //Commented out to test database grabbing.
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
        hcmainrv = view.findViewById(R.id.hawkercornerrv);
        hcadapter = new HCMainsAdapter(stallsList);
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

        //Getting spinner filters and making Array Adapter to configure the spinner's drop down
        String[] hcmainFilters = getResources().getStringArray(R.array.filters);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, hcmainFilters);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hcmainSpinner.setAdapter(adapter);
        hcmainSpinner.setOnItemSelectedListener(this);

        return view;
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
            hcmainrv = view.findViewById(R.id.hawkercornerrv);
            hcadapter = new HCMainsAdapter(filteredList);
            hcmainrv.setAdapter(hcadapter);
        }
    }

    //Two methods to override from the superclass, methods for filtering in spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0: //No filter
                hcadapter.sortChange(stallsList);
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

    //Method to be used in spinner switch-case
    public ArrayList<HawkerCornerStalls> sortListByStall(){
        Collections.sort(stallsList, new Comparator<HawkerCornerStalls>() {
            @Override
            public int compare(HawkerCornerStalls stall1, HawkerCornerStalls stall2) {
                return stall1.hcstallname.compareToIgnoreCase(stall2.hcstallname);
            }
        });
        return stallsList;
    }

    public ArrayList<HawkerCornerStalls> sortListByAuthor(){
        Collections.sort(stallsList, new Comparator<HawkerCornerStalls>() {
            @Override
            public int compare(HawkerCornerStalls stall1, HawkerCornerStalls stall2) {
                return stall1.hcauthor.compareToIgnoreCase(stall2.hcauthor);
            }
        });
        return stallsList;
    }

    public void getHawkerList(ArrayList<HawkerCornerStalls> stallsList) {
        this.stallsList = stallsList;
    }
}