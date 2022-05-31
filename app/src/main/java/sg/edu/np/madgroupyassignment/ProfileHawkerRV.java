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

//Class for hawker corner main page is a fragment
public class ProfileHawkerRV extends Fragment {

    //Set list to static so accessible by all class & create list to get from Database.
    //hawker corner DBList will be used as a copy by filter to use no filter and get original order
    //stallsList will take up the database list in the codes
    public static ArrayList<HawkerCornerStalls> hawkerCornersList = new ArrayList<>();
    ArrayList<HawkerCornerStalls> hcDBList = new ArrayList<>();

    //Recyclerview & Adapter so different functions can access
    RecyclerView hcmainrv;
    HCMainsAdapter hcadapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_profile_hawkerrv, parent, false);

        //Populate hawker corner's stallList, temporary and will change to get data from firebase
        //Will create method to retrieve from db
        //If empty, means havent populate from firebase
        if (hawkerCornersList.isEmpty()){
            for (int i = 0; i < 10; i++) {
                HawkerCornerStalls newstall = new HawkerCornerStalls();
                newstall.hcstallname = "Stall " + i;
                newstall.hcauthor = "Author " + i;
                hawkerCornersList.add(newstall);
            }
        }
        //If not empty, check if size is same, if not same, means new stalls, repopulate
        else if (hawkerCornersList.size() != hcDBList.size()){

        }
        //Not empty and same size, no new stalls
        else{

        }

        //Defining Recycler View info & Setting Layout and Adapter.
        hcmainrv = view.findViewById(R.id.hawkercornerrv);
        hcadapter = new HCMainsAdapter(hawkerCornersList);
        LinearLayoutManager hclayout = new LinearLayoutManager(view.getContext());

        hcmainrv.setAdapter(hcadapter);
        hcmainrv.setLayoutManager(hclayout);


        return view;
    }
}
