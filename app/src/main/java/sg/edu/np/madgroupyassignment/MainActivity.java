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
import com.squareup.picasso.Picasso;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private String UID;
    public static Boolean profileFirstUpdate;
    private UserProfile userProfile;
    private PostsHolder postsHolder;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log_test log_test = new Log_test();
        RecipeForm recipeForm = new RecipeForm();
        HawkerForm hawkerForm = new HawkerForm();
        HawkerCornerMain hawkerCornerMain = new HawkerCornerMain();
        RecipeCornerMain recipeCornerMain = new RecipeCornerMain();
        profileFirstUpdate = true;

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        UID = mAuth.getCurrentUser().getUid();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

//        Getting data from database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Getting user profile
                userProfile = snapshot.child("UserProfile").child(UID).getValue(UserProfile.class);

//                Getting recipe posts
                postsHolder.removeRecipePosts();
                postsHolder.removeUserRecipePosts();
                for (DataSnapshot objectEntry : snapshot.child("Posts").child("Recipes").getChildren()) {
                    RecipeCorner rcpObject = objectEntry.getValue(RecipeCorner.class);
                    postsHolder.setRecipePosts(rcpObject);
                    if (rcpObject.getOwner().equals(mAuth.getUid())){
                        postsHolder.setUserRecipePosts(rcpObject);
                    }
                    postsHolder.updateRecentRecipePosts(rcpObject);
                }

//                Getting hawker posts
                postsHolder.removeHawkerPosts();
                postsHolder.removeUserHawkerPosts();
                for (DataSnapshot objectEntry : snapshot.child("Posts").child("Hawkers").getChildren()) {
                    HawkerCornerStalls hwkObject = objectEntry.getValue(HawkerCornerStalls.class);
                    postsHolder.setHawkerPosts(hwkObject);
                    if (hwkObject.getHcOwner().equals(mAuth.getUid())){
                        postsHolder.setUserHawkerPosts(hwkObject);
                    }
                    postsHolder.updateRecentHawkerPosts(hwkObject);
                }

//                Updating profile page
                log_test.setUserProfile(userProfile);
                if (profileFirstUpdate != true) {
                    log_test.updatePage();
                }

                //Updating hawker corner
                if (hawkerCornerMain.hcadapter != null){
                    hawkerCornerMain.hcadapter.notifyDataSetChanged();
                }

                //Updating recipe corner
                if (recipeCornerMain.adapter != null){
                    recipeCornerMain.adapter.notifyDataSetChanged();
                }

                //Sending user profile to forms
                recipeForm.retrieveUserProfile(userProfile);
                hawkerForm.retrieveUserProfile(userProfile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("test", error.getMessage());
            }
        });

        //Calling classes to replace upon nav bar click
        Home homeFragment = new Home();
        RecipeForm recipeFragment = new RecipeForm(); //temp
        HawkerForm hawkerFragment = new HawkerForm(); //temp
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
                        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, hawkerFragment, null).commit(); //temp segment
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