package sg.edu.np.madgroupyassignment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

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
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity{

    private String UID;
    public static Boolean profileFirstUpdate;
    private UserProfile userProfile;
    private PostsHolder postsHolder;
    private  PostsHolder2 postsHolder2;
    private Handler handler;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public static Date storedDate;
    public static String storedUID;
    public static ArrayList<HomeMixData> randomMixList;

    //Floating Action Buttons
    public static FloatingActionButton mainFAB, rcFAB, hcFAB;
    public static TextView rcFABText, hcFABText;

    Context c;
    Boolean FABVisible;
    HomeMix homeMix = new HomeMix();

    //This is to check when user is in forms
    public static Integer checkFormsNum;
    //This is to check which form user is in
    public static Integer whichForm;

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

        checkFormsNum = 1; //default for checkFormsNum is 1\
        whichForm = 0; //default for checking which form
        // Hiding Title Bar thing
        getSupportActionBar().hide();

        // Make Fully Transparent Status bar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        Profile profilePage = new Profile();
        RecipeForm recipeForm = new RecipeForm();
        HawkerForm hawkerForm = new HawkerForm(0); //Posting = 0
        HawkerDraftsPage hawkerDraftsPage = new HawkerDraftsPage();
        RecipeDraftsPage recipeDraftsPage = new RecipeDraftsPage();
        HawkerCornerMain hawkerCornerMain = new HawkerCornerMain();
        RecipeCornerMain recipeCornerMain = new RecipeCornerMain();


        profileFirstUpdate = true;
        postsHolder = new PostsHolder();
        postsHolder2 = new PostsHolder2();
        handler = new Handler();

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        UID = mAuth.getCurrentUser().getUid();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        //Defining Floating Button
        mainFAB = findViewById(R.id.floating_main_nav_button);
        rcFAB = findViewById(R.id.floating_rc_nav_button);
        hcFAB = findViewById(R.id.floating_hc_nav_button);
        rcFABText = findViewById(R.id.floating_rc_nav_button_text);
        hcFABText = findViewById(R.id.floating_hc_nav_button_text);



        //Makes boolean false again on app startup
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        preferences.edit().remove("onlyonce").commit();

//        Getting data from database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Getting user profile
                userProfile = snapshot.child("UserProfile").child(UID).getValue(UserProfile.class);
                postsHolder.setUserProfile(userProfile);

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

                //                Getting bookmarked recipe posts
                postsHolder2.removeRecipePosts();
                for (DataSnapshot objectEntry : snapshot.child("UserProfile").child(mAuth.getUid()).child("bmrcplist").getChildren()){
                    String PostID = objectEntry.getValue(String.class);
                    for (DataSnapshot objectEntry2 : snapshot.child("Posts").child("Recipes").getChildren()) {
                        RecipeCorner rcpObject2 = objectEntry2.getValue(RecipeCorner.class);
                        if (rcpObject2.getPostID().equals(PostID)){
                            postsHolder2.setRecipePosts(rcpObject2);
                        }
                    }
                }

//                Getting bookmarked hawker posts
                postsHolder2.removeHawkerPosts();
                for (DataSnapshot objectEntry : snapshot.child("UserProfile").child(mAuth.getUid()).child("bmhawklist").getChildren()){
                    String PostID = objectEntry.getValue(String.class);
                    for (DataSnapshot objectEntry2 : snapshot.child("Posts").child("Hawkers").getChildren()) {
                        HawkerCornerStalls hwkObject2 = objectEntry2.getValue(HawkerCornerStalls.class);
                        if (hwkObject2.getPostid().equals(PostID)){
                            postsHolder2.setHawkerposts(hwkObject2);
                        }
                    }
                }


//                Updating profile page
                if (profileFirstUpdate != true) {
                    profilePage.updatePage();
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
                recipeForm.retrieveUserProfile();
                hawkerForm.retrieveUserProfile();

                // Default fragment when app is started, only runs once per app startup
                randomMixList = homeMix.RandomData();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                // Only runs when app is first opened
                if (prefs.getBoolean("onlyonce", false) != true) {
                    // Default home fragment
                    FragmentManager fm = getSupportFragmentManager();
                    fm.beginTransaction()
                            .replace(R.id.MainFragment, new SplashPage(), null).commit();
                    // Shuffles Discover More Section everytime
                    Collections.shuffle(randomMixList);

                    // mark once runned.
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("onlyonce", true);
                    editor.commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Calling classes to replace upon nav bar click
//        SplashPage splashPage = new SplashPage();
        Home homeFragment = new Home();
        Profile profile = new Profile();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (prefs.getBoolean("onlyonce", false) != true){
            //         Hiding Nav Bars and FAB and during splash page duration
            bottomNavigationView.setVisibility(View.GONE);
            findViewById(R.id.floating_main_nav_button).setVisibility(View.GONE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, homeFragment, "Home").commit();
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    findViewById(R.id.floating_main_nav_button).setVisibility(View.VISIBLE);
                }
            }, 2500);
        }

        // Upon Bottom Nav Bar click
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //When click on home
                switch (item.getItemId()) {
                    case R.id.home:
                        if (checkFormsNum == 1){ //If never enter forms
//                            getSupportFragmentManager().popBackStack();
                            mainFAB.show();
                            getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, homeFragment, "Home")
                                    .addToBackStack(null).commit();

                        }
                        else if ( whichForm == 1 && HawkerForm.stallName == "" || whichForm == 1 && HawkerForm.stallName.isEmpty() || whichForm == 1 && HawkerForm.stallName == null){
                            mainFAB.show();
                            checkFormsNum = 1;
//                            getSupportFragmentManager().popBackStack();
                            getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, homeFragment, "Home")
                                    .addToBackStack(null).commit();
                        }
                        else if ( whichForm == 2){
                            RecipeForm.viewModel.getSelectedRecipeName().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    RecipeForm.recipeName = s; //RECIPE TITLE parameter
                                }
                            });
                            if (RecipeForm.recipeName == "" || RecipeForm.recipeName.isEmpty() || RecipeForm.recipeName == null){
                                mainFAB.show();
                                checkFormsNum = 1;
                                getSupportFragmentManager().popBackStack();
                                getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, homeFragment, "Home")
                                        .addToBackStack(null).commit();
                            }
                            else{
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this); //Context is getActivity

                                //Set title
                                builder1.setTitle("Wait!");
                                builder1.setMessage("Do you want to save this to drafts?");

                                builder1.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        checkFormsNum = 1; //changes to 1 when click back to the pages that are not forms
                                        mainFAB.show();
                                        getSupportFragmentManager().popBackStack();
                                        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, homeFragment, "Home")
                                                .addToBackStack(null).commit();
                                    }
                                });

                                builder1.setNeutralButton("Don't Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        checkFormsNum = 1; //changes to 1 when click back to the pages that are not forms
                                        mainFAB.show();
                                        getSupportFragmentManager().popBackStack();
                                        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, homeFragment, "Home")
                                                .addToBackStack(null).commit();
                                    }
                                });

                                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //dismiss Dialog
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder1.show();
                            }
                        }
                        else{ //If enter forms
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this); //Context is getActivity

                            //Set title
                            builder1.setTitle("Wait!");
                            builder1.setMessage("Do you want to save this to drafts?");

                            builder1.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    checkFormsNum = 1; //changes to 1 when click back to the pages that are not forms
                                    mainFAB.show();
                                    getSupportFragmentManager().popBackStack();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, homeFragment, "Home")
                                            .addToBackStack(null).commit();
                                }
                            });

                            builder1.setNeutralButton("Don't Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    checkFormsNum = 1; //changes to 1 when click back to the pages that are not forms
                                    mainFAB.show();
                                    getSupportFragmentManager().popBackStack();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, homeFragment, "Home")
                                            .addToBackStack(null).commit();
                                }
                            });

                            builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //dismiss Dialog
                                    dialogInterface.dismiss();
                                }
                            });

                            builder1.show();
                        }
                        return true;
                }




                //When click on hc
                switch (item.getItemId()) {
                    case R.id.hc:
                        if (checkFormsNum == 1){ //If never enter forms
                            mainFAB.show();
                            //getSupportFragmentManager().popBackStack();
                            getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, hawkerCornerMain, "HC")
                                    .addToBackStack(null).commit();
                        }
                        else if ( whichForm == 1 && HawkerForm.stallName == "" || whichForm == 1 && HawkerForm.stallName.isEmpty() || whichForm == 1 && HawkerForm.stallName == null){
                            mainFAB.show();
                            checkFormsNum = 1;
                            getSupportFragmentManager().popBackStack();
                            getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, hawkerCornerMain, "HC")
                                    .addToBackStack(null).commit();
                        }
                        else if ( whichForm == 2){
                            RecipeForm.viewModel.getSelectedRecipeName().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    RecipeForm.recipeName = s; //RECIPE TITLE parameter
                                }
                            });
                            if (RecipeForm.recipeName == "" || RecipeForm.recipeName.isEmpty() || RecipeForm.recipeName == null){
                                mainFAB.show();
                                checkFormsNum = 1;
                                getSupportFragmentManager().popBackStack();
                                getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, hawkerCornerMain, "HC")
                                        .addToBackStack(null).commit();
                            }
                            else{
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this); //Context is getActivity

                                //Set title
                                builder1.setTitle("Wait!");
                                builder1.setMessage("Do you want to save this to drafts?");

                                builder1.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        checkFormsNum = 1; //changes to 1 when click back to the pages that are not forms
                                        mainFAB.show();
                                        getSupportFragmentManager().popBackStack();
                                        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, hawkerCornerMain, "HC")
                                                .addToBackStack(null).commit();
                                    }
                                });

                                builder1.setNeutralButton("Don't Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        checkFormsNum = 1; //changes to 1 when click back to the pages that are not forms
                                        mainFAB.show();
                                        getSupportFragmentManager().popBackStack();
                                        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, hawkerCornerMain, "HC")
                                                .addToBackStack(null).commit();
                                    }
                                });

                                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //dismiss Dialog
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder1.show();
                            }
                        }
                        else { //If enter forms
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this); //Context is getActivity

                            //Set title
                            builder1.setTitle("Wait!");
                            builder1.setMessage("Do you want to save this to drafts?");

                            builder1.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mainFAB.show();
                                    checkFormsNum = 1; //changes to 1 when click back to the pages that are not forms
                                    getSupportFragmentManager().popBackStack();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, hawkerCornerMain, "HC")
                                            .addToBackStack(null).commit();
                                }
                            });

                            builder1.setNeutralButton("Don't Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mainFAB.show();
                                    checkFormsNum = 1; //changes to 1 when click back to the pages that are not forms
                                    getSupportFragmentManager().popBackStack();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, hawkerCornerMain, "HC")
                                            .addToBackStack(null).commit();
                                }
                            });

                            builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //dismiss Dialog
                                    dialogInterface.dismiss();
                                }
                            });

                            builder1.show();
                        }


                        return true;
                }




                //When click on rc
                switch (item.getItemId()) {
                    case R.id.rc:
                        if (checkFormsNum == 1) { //If never enter forms
                            mainFAB.show();
//                            getSupportFragmentManager().popBackStack();
                            getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, recipeCornerMain, "RC")
                                    .addToBackStack(null).commit();
                        }
                        else if ( whichForm == 1 && HawkerForm.stallName == "" || whichForm == 1 && HawkerForm.stallName.isEmpty() || whichForm == 1 && HawkerForm.stallName == null){
                            mainFAB.show();
                            checkFormsNum = 1;
                            getSupportFragmentManager().popBackStack();
                            getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, recipeCornerMain, "RC")
                                    .addToBackStack(null).commit();
                        }
                        else if ( whichForm == 2){
                            RecipeForm.viewModel.getSelectedRecipeName().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    RecipeForm.recipeName = s; //RECIPE TITLE parameter
                                }
                            });
                            if (RecipeForm.recipeName == "" || RecipeForm.recipeName.isEmpty() || RecipeForm.recipeName == null){
                                mainFAB.show();
                                checkFormsNum = 1;
                                getSupportFragmentManager().popBackStack();
                                getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, recipeCornerMain, "RC")
                                        .addToBackStack(null).commit();
                            }
                            else{
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this); //Context is getActivity

                                //Set title
                                builder1.setTitle("Wait!");
                                builder1.setMessage("Do you want to save this to drafts?");

                                builder1.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        checkFormsNum = 1; //changes to 1 when click back to the pages that are not forms
                                        mainFAB.show();
                                        getSupportFragmentManager().popBackStack();
                                        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, recipeCornerMain, "RC")
                                                .addToBackStack(null).commit();
                                    }
                                });

                                builder1.setNeutralButton("Don't Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        checkFormsNum = 1; //changes to 1 when click back to the pages that are not forms
                                        mainFAB.show();
                                        getSupportFragmentManager().popBackStack();
                                        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, recipeCornerMain, "RC")
                                                .addToBackStack(null).commit();
                                    }
                                });

                                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //dismiss Dialog
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder1.show();
                            }
                        }
                        else{ //If enter forms
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this); //Context is getActivity

                            //Set title
                            builder1.setTitle("Wait!");
                            builder1.setMessage("Do you want to save this to drafts?");

                            builder1.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    checkFormsNum = 1; //changes to 1 when click back to the pages that are not forms
                                    mainFAB.show();
                                    getSupportFragmentManager().popBackStack();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, recipeCornerMain, "RC")
                                            .addToBackStack(null).commit();
                                }
                            });

                            builder1.setNeutralButton("Don't Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    checkFormsNum = 1; //changes to 1 when click back to the pages that are not forms
                                    mainFAB.show();
                                    getSupportFragmentManager().popBackStack();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, recipeCornerMain, "RC")
                                            .addToBackStack(null).commit();
                                }
                            });


                            builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //dismiss Dialog
                                    dialogInterface.dismiss();
                                }
                            });

                            builder1.show();
                        }
                        return true;
                }




                //When click on profile
                switch (item.getItemId()) {
                    case R.id.profile:
                        if (checkFormsNum == 1){ //If never enter forms
                            mainFAB.show();
//                            getSupportFragmentManager().popBackStack();
                            getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, profile, "Profile")
                                    .addToBackStack(null).commit();
                        }
                        else if ( whichForm == 1 && HawkerForm.stallName == "" || whichForm == 1 &&  HawkerForm.stallName.isEmpty() || whichForm == 1 && HawkerForm.stallName == null){
                            mainFAB.show();
                            checkFormsNum = 1;
                            getSupportFragmentManager().popBackStack();
                            getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, profile, "Profile")
                                    .addToBackStack(null).commit();
                        }
                        else if ( whichForm == 2){
                            RecipeForm.viewModel.getSelectedRecipeName().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    RecipeForm.recipeName = s; //RECIPE TITLE parameter
                                }
                            });
                            if (RecipeForm.recipeName == "" || RecipeForm.recipeName.isEmpty() || RecipeForm.recipeName == null){
                                mainFAB.show();
                                checkFormsNum = 1;
                                getSupportFragmentManager().popBackStack();
                                getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, profile, "Profile")
                                        .addToBackStack(null).commit();
                            }
                            else{
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this); //Context is getActivity

                                //Set title
                                builder1.setTitle("Wait!");
                                builder1.setMessage("Do you want to save this to drafts?");

                                builder1.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        checkFormsNum = 1; //changes to 1 when click back to the pages that are not forms
                                        mainFAB.show();
                                        getSupportFragmentManager().popBackStack();
                                        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, profile, "Profile")
                                                .addToBackStack(null).commit();
                                    }
                                });

                                builder1.setNeutralButton("Don't Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        checkFormsNum = 1; //changes to 1 when click back to the pages that are not forms
                                        mainFAB.show();
                                        getSupportFragmentManager().popBackStack();
                                        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, profile, "Profile")
                                                .addToBackStack(null).commit();
                                    }
                                });

                                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //dismiss Dialog
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder1.show();
                            }
                        }
                        else{ //If enter forms
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this); //Context is getActivity

                            //Set title
                            builder1.setTitle("Wait!");
                            builder1.setMessage("Do you want to save this to drafts?");

                            builder1.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    checkFormsNum = 1; //changes to 1 when click back to the pages that are not forms\
                                    mainFAB.show();
                                    getSupportFragmentManager().popBackStack();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, profile, "Profile")
                                            .addToBackStack(null).commit();
                                }
                            });

                            builder1.setNeutralButton("Don't Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    checkFormsNum = 1; //changes to 1 when click back to the pages that are not forms
                                    mainFAB.show();
                                    getSupportFragmentManager().popBackStack();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, profile, "Profile")
                                            .addToBackStack(null).commit();
                                }
                            });

                            builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //dismiss Dialog
                                    dialogInterface.dismiss();
                                }
                            });

                            builder1.show();
                        }
                        return true;
                }
                return false;
            }
        });




        //set all as invisible when loaded
        rcFAB.setVisibility(View.GONE);
        hcFAB.setVisibility(View.GONE);
        rcFABText.setVisibility(View.GONE);
        hcFABText.setVisibility(View.GONE);
        FABVisible = false;

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
//                if (checkFormsNum == 1){
//                    checkFormsNum = 0; //changes to 0 when click the floating button
                    whichForm = 1;
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.MainFragment, hawkerDraftsPage).addToBackStack(null).commit();
                    mainFAB.hide();
                    rcFAB.hide();
                    hcFAB.hide();
                    rcFABText.setVisibility(View.GONE);
                    hcFABText.setVisibility(View.GONE);
//                }
            }
        });
        //FAB Recipe Corner Button
        rcFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (checkFormsNum == 1){
//                    checkFormsNum = 0; //changes to 0 when click the floating button
                    whichForm = 2;
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.MainFragment, recipeDraftsPage).addToBackStack(null).commit();
                    mainFAB.hide();
                    rcFAB.hide();
                    hcFAB.hide();
                    rcFABText.setVisibility(View.GONE);
                    hcFABText.setVisibility(View.GONE);
//                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        checkFormsNum = 1;
        super.onBackPressed();
        Home homeFragment = (Home)getSupportFragmentManager().findFragmentByTag("Home");
        if (homeFragment != null && homeFragment.isVisible()) {
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
        }
        HawkerCornerMain hcFragment = (HawkerCornerMain)getSupportFragmentManager().findFragmentByTag("HC");
        if (hcFragment != null && hcFragment.isVisible()) {
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.hc).setChecked(true);
        }
        RecipeCornerMain rcFragment = (RecipeCornerMain)getSupportFragmentManager().findFragmentByTag("RC");
        if (rcFragment != null && rcFragment.isVisible()) {
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.rc).setChecked(true);
        }
        Profile profileFragment = (Profile)getSupportFragmentManager().findFragmentByTag("Profil");
        if (profileFragment != null && profileFragment.isVisible()) {
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.profile).setChecked(true);
        }
    }

}