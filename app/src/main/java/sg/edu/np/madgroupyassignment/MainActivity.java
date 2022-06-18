package sg.edu.np.madgroupyassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity{

    private String UID;
    public static Boolean profileFirstUpdate;
    private UserProfile userProfile;
    private PostsHolder postsHolder;
    private Handler handler;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public static Date storedDate;
    public static String storedUID;



    Context c;
    Boolean FABVisible;
    public static BottomNavigationView bottomNavigationView;

    public MainActivity(){
        this.c =c;
        this.FABVisible = FABVisible;
    }

    // Method to make Status Bar Transparent
    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Makes Status Bar Transparent
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        Profile log_test = new Profile();
        RecipeForm recipeForm = new RecipeForm();
        HawkerForm hawkerForm = new HawkerForm();
        HawkerCornerMain hawkerCornerMain = new HawkerCornerMain();
        RecipeCornerMain recipeCornerMain = new RecipeCornerMain();

        //Floating action buttons
        FloatingActionButton mainFAB, rcFAB, hcFAB;
        TextView rcFABText, hcFABText;

        profileFirstUpdate = true;
        postsHolder = new PostsHolder();
        handler = new Handler();

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

//                Getting Weekly Post
                storedDate = new Date(snapshot.child("WeeklyDate").getValue(Long.class));
                storedUID = snapshot.child("WeeklyPost").getValue(String.class);

//                Getting recipe posts
                postsHolder.removeRecipePosts();
                postsHolder.removeUserRecipePosts();
                postsHolder.removeRecentRecipePosts();
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
                postsHolder.removeRecentHawkerPosts();
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
        SplashPage splashPage = new SplashPage();
        Home homeFragment = new Home();
        Profile profile = new Profile();
        HawkerCornerMain hcmain = new HawkerCornerMain();
        RecipeCornerMain rcmain = new RecipeCornerMain();

        if (savedInstanceState == null) {
            getSupportActionBar().hide();
            bottomNavigationView.setVisibility(View.GONE);
            findViewById(R.id.floating_main_nav_button).setVisibility(View.GONE);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.MainFragment, splashPage)
                    .commit();
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, homeFragment, null).commit();
                bottomNavigationView.setVisibility(View.VISIBLE);
                findViewById(R.id.floating_main_nav_button).setVisibility(View.VISIBLE);
            }
        }, 3000);


        /*public void hcClicked(){
            HawkerCornerMain hcmain2 = new HawkerCornerMain();
            getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, hcmain2, null).commit();
        }*/
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
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, profile, null).commit();
                        return true;
                }
                return false;
            }
        });

        //Floating Action Buttons
        mainFAB = findViewById(R.id.floating_main_nav_button);
        rcFAB = findViewById(R.id.floating_rc_nav_button);
        hcFAB = findViewById(R.id.floating_hc_nav_button);
        rcFABText = findViewById(R.id.floating_rc_nav_button_text);
        hcFABText = findViewById(R.id.floating_hc_nav_button_text);

        //set all as invisible when loaded
        rcFAB.setVisibility(View.GONE);
        hcFAB.setVisibility(View.GONE);
        rcFABText.setVisibility(View.GONE);
        hcFABText.setVisibility(View.GONE);
        FABVisible = false;

        //ANimations


        mainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!FABVisible){ // if false
                    rcFAB.show();
                    hcFAB.show();
                    rcFABText.setVisibility(View.VISIBLE);
                    hcFABText.setVisibility(View.VISIBLE);
                    FABVisible = true;
                }
                else{
                    rcFAB.hide();
                    hcFAB.hide();
                    rcFABText.setVisibility(View.GONE);
                    hcFABText.setVisibility(View.GONE);
                    FABVisible = false;
                }
            }
        });
        //FAB Hawker Corner Button
        hcFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.MainFragment, hawkerForm).addToBackStack(null).commit();
                rcFAB.hide();
                hcFAB.hide();
                rcFABText.setVisibility(View.GONE);
                hcFABText.setVisibility(View.GONE);
            }
        });
        //FAB RC Corner Button
        rcFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.MainFragment, recipeForm).addToBackStack(null).commit();
                rcFAB.hide();
                hcFAB.hide();
                rcFABText.setVisibility(View.GONE);
                hcFABText.setVisibility(View.GONE);
            }
        });


    }
}