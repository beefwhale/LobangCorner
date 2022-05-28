package sg.edu.np.madgroupyassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

//    private String UID;
//    private UserProfile retrivedUserProfile;
//    private ArrayList<RecipeCorner> rcpList;
//    private ArrayList<HawkerCornerStalls> hawkList;
//    private FirebaseAuth mAuth;
//    private FirebaseDatabase firebaseDatabase;
//    private DatabaseReference databaseReference;
//    private ValueEventListener Listener;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        rcpList = new ArrayList<>();
//        mAuth = FirebaseAuth.getInstance();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference();
//        UID = mAuth.getCurrentUser().getUid();
//
//        //Updates fragments when database changes
//        Listener = databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                //Getting user profile
//                retrivedUserProfile = snapshot.child("UserProfile").child(UID).getValue(UserProfile.class);
//
//                //Getting recipe posts
//                for (DataSnapshot objectEntry : snapshot.child("Posts").child("Recipes").getChildren()) {
//                    RecipeCorner rcpObject = objectEntry.getValue(RecipeCorner.class);
//                    rcpList.add(rcpObject);
//                }
//
//                //Add update for hawker posts
//
//                //Sending data to profile page
////                Log_test log_test = new Log_test();
////                log_test.profileToProfilePage(retrivedUserProfile);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        //Calling classes to replace upon nav bar click
        Home homeFragment = new Home();
        RecipeForm recipeFragment = new RecipeForm(); //temp
        Log_test profile = new Log_test();
        HawkerCornerMain hcmain = new HawkerCornerMain();
        RecipeCornerMain rcmain = new RecipeCornerMain();

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.MainFragment, homeFragment)
                    .commit();
        }
        //Upon Bottom Nav Bar click
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, homeFragment, null).commit();
                        return true;
                }
                switch (item.getItemId()) {
                    case R.id.hc:
                        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, hcmain, null).commit();
                        return true;
                }
                switch (item.getItemId()) {
                    case R.id.rc:
                        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, rcmain, null).commit();
                        return true;
                }
                switch (item.getItemId()) {
                    case R.id.recipeform:
                        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, recipeFragment, null).commit(); //temp segment
                        return true;
                }
                switch (item.getItemId()) {
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, profile, null).commit();
                        return true;
                }
                return false;
            }
        });


    }
}