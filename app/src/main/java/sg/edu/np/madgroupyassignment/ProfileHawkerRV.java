package sg.edu.np.madgroupyassignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//Class for hawker corner main page is a fragment
public class ProfileHawkerRV extends Fragment {

    private PostsHolder postsHolder;
    private ArrayList<HawkerCornerStalls> hawkerCornersList = new ArrayList<>();

    //Recyclerview & Adapter so different functions can access
    RecyclerView hcmainrv;
    HCMainsAdapter hcadapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_profile_hawkerrv, parent, false);

        hawkerCornersList.removeAll(hawkerCornersList);
        for (HawkerCornerStalls obj : postsHolder.getUserHawkerPosts()) {
            hawkerCornersList.add(obj);
        }


        //Populate user's stall List, temporary and will change to get data from firebase
        if (hawkerCornersList.isEmpty()){
            for (int i = 0; i < 10; i++) {
                HawkerCornerStalls newstall = new HawkerCornerStalls();
                newstall.hcstallname = "Stall " + i;
                newstall.hcauthor = "Author " + i;
                hawkerCornersList.add(newstall);
            }
        }

        hcmainrv = view.findViewById(R.id.hawkercornerrv);
        hcadapter = new HCMainsAdapter(hawkerCornersList);
        LinearLayoutManager hclayout = new LinearLayoutManager(view.getContext());

        hcmainrv.setAdapter(hcadapter);
        hcmainrv.setLayoutManager(hclayout);

        return view;
    }

}
