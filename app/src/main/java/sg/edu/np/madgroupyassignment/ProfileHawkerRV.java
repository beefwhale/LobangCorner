package sg.edu.np.madgroupyassignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//Class for hawker corner main page is a fragment
public class ProfileHawkerRV extends Fragment {

    private PostsHolder postsHolder;
    private ArrayList<HawkerCornerStalls> hawkerCornersList = new ArrayList<>();

    //Recyclerview & Adapter so different functions can access
    private static TextView hcheader;
    RecyclerView hcmainrv;
    HCMainsAdapter hcadapter;
    String username;
    String usernameID;
    String usernameImg;
    ImageButton deleteBtn;
    ImageButton editBtn;

    private static UserProfile userProfile;

    CheckBox checkBox;
    AlertDialog.Builder builder;
    ArrayList<Integer> listPos;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    Context c;

    public ProfileHawkerRV(){
        this.c = c;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_profile_hawkerrv, parent, false);
        Bundle bundle = this.getArguments();

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        //Make sure bundle
        assert bundle != null;
        username = (String) bundle.getString("username");
        usernameID = (String) bundle.getString("usernameID");
        usernameImg = (String) bundle.getString("usernameImg");
        hcheader = view.findViewById(R.id.profile_hc_header);
        hcheader.setText(username + "'s Hawker Corner");

        //Upon Delete button click
        hcadapter = new HCMainsAdapter(hawkerCornersList, false);

//        Deleting Users own post
        deleteBtn = view.findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(getContext());
                //At least one post selected
                if (hcadapter.cbCount > 0){
                    //Setting message manually and performing action on button click
                    builder.setTitle("Confirm Delete ?")
                            .setMessage("You sure you want to permanently delete ("+hcadapter.cbCount+") posts?")
                            .setCancelable(false)
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
//                                Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
//                                        Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    //List of selected positions in RV (Checked)
                                    listPos = hcadapter.listPos;
                                    for (int i: listPos) {
                                        HawkerCornerStalls deleteItem = hawkerCornersList.get(i);


                                        //removing from database
                                        databaseReference.child("UserProfile").child(usernameID).child("hawkList").child(deleteItem.getPostid()).removeValue();
                                        databaseReference.child("Posts").child("Hawkers").child(deleteItem.getPostid()).removeValue();
                                        StorageReference storageLocationCheck = FirebaseStorage.getInstance().getReferenceFromUrl(deleteItem.hccoverimg);
                                        storageLocationCheck.delete();

                                        //Updating List
                                        hawkerCornersList.remove(i);
                                        hcadapter.notifyItemRemoved(i);
                                    }
                                    Toast.makeText(getActivity(),hcadapter.cbCount+" Post(s) Deleted",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else{
                    Toast.makeText(getActivity(),"No Post Selected",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
//        Editing Users own Post
        editBtn = view.findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(getContext());
                //Only one post selected
                if (hcadapter.cbCount == 1 && hcadapter.listPos.size() == 1){
                    //Getting Chosen Post to Edit
                    Integer pos = hcadapter.listPos.get(0);

                    Fragment HawkerForm = new HawkerForm(false);

                    //pass username
                    Bundle bundle = new Bundle();
                    bundle.putInt("stallposition", pos);
                    bundle.putParcelable("list", Parcels.wrap(hawkerCornersList));
                    HawkerForm.setArguments(bundle);

                    //Creating activity context to for the view, starting new fragment when view is clicked
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, HawkerForm).addToBackStack(null).commit();
                }
                // No post selected
                else if(hcadapter.cbCount==0){
                    Toast.makeText(getActivity(),"Please select a post to edit",
                            Toast.LENGTH_SHORT).show();
                }
                // More than 1 post selected
                else{
                    //Setting message manually and performing action on button click
                    builder.setTitle("Deselect ALL posts?")
                            .setMessage("You can only edit 1 post at a time.")
                            .setCancelable(false)
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
//                                Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
//                                        Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    //Deselect all Posts
                                    for (HawkerCornerStalls i: hawkerCornersList) {
                                        i.setChecked(false);

                                    }
                                    hcadapter.cbCount = 0;
                                    hcadapter.listPos.clear();
                                    hcadapter.notifyDataSetChanged();

                                    Toast.makeText(getActivity(),"All posts unselected.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
        hawkerCornersList.removeAll(hawkerCornersList);
        for (HawkerCornerStalls obj : postsHolder.getUserHawkerPosts()) {
            hawkerCornersList.add(obj);
        }
        hcadapter = new HCMainsAdapter(hawkerCornersList, false);
        hcmainrv = view.findViewById(R.id.hawkercornerrv);
        // If you want the checkbox layout, set as false
        LinearLayoutManager hclayout = new LinearLayoutManager(view.getContext());

        hcmainrv.setAdapter(hcadapter);
        hcmainrv.setLayoutManager(hclayout);

        return view;
    }

}
