package sg.edu.np.madgroupyassignment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private String UID;
    public static Boolean profileFirstUpdate;
    private UserProfile userProfile;
    private ArrayList<UserProfile> authorProfileList;
    private PostsHolder postsHolder;
    private PostsHolder2 postsHolder2;
    private Handler handler;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public static Date storedDate;
    public static String storedUID;
    public static ArrayList<HomeMixData> randomMixList;

    public static RecipeForm recipeForm;
    HawkerCornerStalls hawkerDrafts;
    RecipeCorner recipeDrafts;

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

    //List to store User Hawker Drafts
    public static ArrayList<HawkerCornerStalls> hawkerDraftsList;
    //List to store User Recipe Drafts
    public static ArrayList<RecipeCorner> recipeDraftsList;

    public static BottomNavigationView bottomNavigationView;

    public MainActivity() {
        this.c = c;
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

        Profile profilePage = new Profile(true);
        recipeForm = new RecipeForm(0);
        HawkerForm hawkerForm = new HawkerForm(0); //Posting = 0
        HawkerDraftsPage hawkerDraftsPage = new HawkerDraftsPage();
        RecipeDraftsPage recipeDraftsPage = new RecipeDraftsPage();
        HawkerCornerMain hawkerCornerMain = new HawkerCornerMain();
        RecipeCornerMain recipeCornerMain = new RecipeCornerMain();

        hawkerDrafts = new HawkerCornerStalls();
        recipeDrafts = new RecipeCorner();
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

//                Getting author profile
                postsHolder.removeAuthorProfileList();
                for (DataSnapshot objectEntry : snapshot.child("UserProfile").getChildren()) {
                    UserProfile userObj = objectEntry.getValue(UserProfile.class);
                    postsHolder.setAuthorProfileList(userObj);
                }

//                Getting Weekly Post
                storedDate = new Date(snapshot.child("WeeklyDate").getValue(Long.class));
                storedUID = snapshot.child("WeeklyPost").getValue(String.class);

//                Getting Hawker Drafts
                hawkerDraftsList = new ArrayList<HawkerCornerStalls>();
                postsHolder.removeHawkerDrafts();
                for (DataSnapshot objectEntry : snapshot.child("Drafts").child("Hawkers").child(mAuth.getUid()).getChildren()) {
                    HawkerCornerStalls eachHawkerObj = objectEntry.getValue(HawkerCornerStalls.class);
                    hawkerDraftsList.add(eachHawkerObj);
                    postsHolder.setHawkerDrafts(hawkerDraftsList);
                    //^ set into postsholder\
                }

//                Getting Recipe Drafts
                recipeDraftsList = new ArrayList<RecipeCorner>();
                postsHolder.removeRecipeDrafts();
                for (DataSnapshot objectEntry : snapshot.child("Drafts").child("Recipes").child(mAuth.getUid()).getChildren()) {
                    RecipeCorner eachRecipeObj = objectEntry.getValue(RecipeCorner.class);
                    recipeDraftsList.add(eachRecipeObj);
                    postsHolder.setRecipeDrafts(recipeDraftsList);
                    //^ set into postsholder\
                }

//                Getting recipe posts
                postsHolder.removeRecipePosts();
                postsHolder.removeUserRecipePosts();
                postsHolder.removeRecentRecipePosts();
                for (DataSnapshot objectEntry : snapshot.child("Posts").child("Recipes").getChildren()) {
                    RecipeCorner rcpObject = objectEntry.getValue(RecipeCorner.class);
                    postsHolder.setRecipePosts(rcpObject);
                    if (rcpObject.getOwner().equals(mAuth.getUid())) {
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
                    if (hwkObject.getHcOwner().equals(mAuth.getUid())) {
                        postsHolder.setUserHawkerPosts(hwkObject);
                    }
                    postsHolder.updateRecentHawkerPosts(hwkObject);
                }

                //                Getting bookmarked recipe posts
                postsHolder2.removeRecipePosts();
                for (DataSnapshot objectEntry : snapshot.child("UserProfile").child(mAuth.getUid()).child("bmrcplist").getChildren()) {
                    String PostID = objectEntry.getValue(String.class);
                    for (DataSnapshot objectEntry2 : snapshot.child("Posts").child("Recipes").getChildren()) {
                        RecipeCorner rcpObject2 = objectEntry2.getValue(RecipeCorner.class);
                        if (rcpObject2.getPostID().equals(PostID)) {
                            postsHolder2.setRecipePosts(rcpObject2);
                        }
                    }
                }

//                Getting bookmarked hawker posts
                postsHolder2.removeHawkerPosts();
                for (DataSnapshot objectEntry : snapshot.child("UserProfile").child(mAuth.getUid()).child("bmhawklist").getChildren()) {
                    String PostID = objectEntry.getValue(String.class);
                    for (DataSnapshot objectEntry2 : snapshot.child("Posts").child("Hawkers").getChildren()) {
                        HawkerCornerStalls hwkObject2 = objectEntry2.getValue(HawkerCornerStalls.class);
                        if (hwkObject2.getPostid().equals(PostID)) {
                            postsHolder2.setHawkerposts(hwkObject2);
                        }
                    }
                }


//                Updating profile page
                if (profileFirstUpdate != true) {
                    profilePage.updatePage();
                }

                //Updating hawker corner
                if (hawkerCornerMain.hcadapter != null) {
                    hawkerCornerMain.hcadapter.notifyDataSetChanged();
                }

                //Updating recipe corner
                if (recipeCornerMain.adapter != null) {
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
                    if(fm.isDestroyed() == false){
                        fm.beginTransaction()
                                .replace(R.id.MainFragment, new SplashPage(), null).commit();
                    }
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


//        Getting device registration token for notifications
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Test", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        //Get new FCM registration token
                        String token = task.getResult();

                        //Log and Toast
                        Log.d("Token", token);
                        databaseReference.child("Device Registration Tokens").child(mAuth.getUid()).setValue(token);
                    }
                });

        //Calling classes to replace upon nav bar click
//        SplashPage splashPage = new SplashPage();
        Home homeFragment = new Home();
        Profile profile = new Profile(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (prefs.getBoolean("onlyonce", false) != true) {
            //         Hiding Nav Bars and FAB and during splash page duration
            bottomNavigationView.setVisibility(View.GONE);
            findViewById(R.id.floating_main_nav_button).setVisibility(View.GONE);
            // Checking Internet Connection
            isNetworkAvailable(this);
        }

        // Upon Bottom Nav Bar click
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //When click on home
                switch (item.getItemId()) {
                    case R.id.home:
                        if (checkFormsNum == 1) { //If never enter forms
//                            getSupportFragmentManager().popBackStack();
                            mainFAB.show();
                            Fragment checkFragment = getSupportFragmentManager().findFragmentByTag("Home");
                            //If current Fragment is Home Page
                            if (checkFragment != null && checkFragment.isVisible()){ // Yes
                                getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, homeFragment, "Home")
                                        .commit();
                            }
                            else{ // No
                                getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, homeFragment, "Home")
                                        .addToBackStack(null).commit();
                            }
                        }
                        else if ((whichForm == 1 && HawkerForm.stallName == "" || whichForm == 1 && HawkerForm.stallName.isEmpty() || whichForm == 1 && HawkerForm.stallName == null) &&
                                (whichForm == 1 && HawkerForm.desc == "" || whichForm == 1 && HawkerForm.desc.isEmpty() || whichForm == 1 && HawkerForm.desc == null) &&
                                        (whichForm == 1 && HawkerForm.shortDesc == "" || whichForm == 1 && HawkerForm.shortDesc.isEmpty() || whichForm == 1 && HawkerForm.shortDesc == null) &&
                                                (whichForm == 1 && HawkerForm.address == "" || whichForm == 1 && HawkerForm.address.isEmpty() || whichForm == 1 && HawkerForm.address == null) &&
                                                        (whichForm == 1 && HawkerForm.downUrl == "" || whichForm == 1 && HawkerForm.downUrl.isEmpty() || whichForm == 1 && HawkerForm.downUrl == null)) {
                            mainFAB.show();
                            checkFormsNum = 1;
//                            getSupportFragmentManager().popBackStack();
                            getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, homeFragment, "Home")
                                    .addToBackStack(null).commit();
                        } else if (whichForm == 2) {
                            RecipeForm.viewModel.getSelectedRecipeName().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    RecipeForm.recipeName = s;
                                }
                            });
                            RecipeForm.viewModel.getSelectedRecipeDesc().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    RecipeForm.recipeDesc = s;
                                }
                            });
                            RecipeForm.viewModel.getSelectedImg().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    RecipeForm.selectedImg = s;
                                }
                            });
                            if ((RecipeForm.recipeName == "" || RecipeForm.recipeName.isEmpty() || RecipeForm.recipeName == null) &&
                                    (RecipeForm.recipeDesc == "" || RecipeForm.recipeDesc.isEmpty() || RecipeForm.recipeDesc == null) &&
                                    (RecipeForm.selectedImg == "" || RecipeForm.selectedImg.isEmpty() || RecipeForm.selectedImg == null)) {
                                mainFAB.show();
                                checkFormsNum = 1;
                                getSupportFragmentManager().popBackStack();
                                getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, homeFragment, "Home")
                                        .addToBackStack(null).commit();
                            } else {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this); //Context is getActivity

                                //Set title
                                builder1.setTitle("Wait!");
                                builder1.setMessage("Do you want to save this to drafts?");

                                builder1.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        saveToRecipeDrafts();
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
                        } else { //If enter forms
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this); //Context is getActivity

                            //Set title
                            builder1.setTitle("Wait!");
                            builder1.setMessage("Do you want to save this to drafts?");

                            builder1.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    saveToHawkerDrafts();
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
                        if (checkFormsNum == 1) { //If never enter forms
                            mainFAB.show();
                            //getSupportFragmentManager().popBackStack();
                            Fragment checkFragment = getSupportFragmentManager().findFragmentByTag("HC");
                            //If current Fragment is HC Page
                            if (checkFragment != null && checkFragment.isVisible()) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, hawkerCornerMain, "HC")
                                        .commit();
                            }
                            else{
                                getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, hawkerCornerMain, "HC")
                                        .addToBackStack(null).commit();
                            }
                        } else if ((whichForm == 1 && HawkerForm.stallName == "" || whichForm == 1 && HawkerForm.stallName.isEmpty() || whichForm == 1 && HawkerForm.stallName == null) &&
                                (whichForm == 1 && HawkerForm.desc == "" || whichForm == 1 && HawkerForm.desc.isEmpty() || whichForm == 1 && HawkerForm.desc == null) &&
                                (whichForm == 1 && HawkerForm.shortDesc == "" || whichForm == 1 && HawkerForm.shortDesc.isEmpty() || whichForm == 1 && HawkerForm.shortDesc == null) &&
                                (whichForm == 1 && HawkerForm.address == "" || whichForm == 1 && HawkerForm.address.isEmpty() || whichForm == 1 && HawkerForm.address == null) &&
                                (whichForm == 1 && HawkerForm.downUrl == "" || whichForm == 1 && HawkerForm.downUrl.isEmpty() || whichForm == 1 && HawkerForm.downUrl == null)) {
                            mainFAB.show();
                            checkFormsNum = 1;
                            getSupportFragmentManager().popBackStack();
                            getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, hawkerCornerMain, "HC")
                                    .addToBackStack(null).commit();
                        } else if (whichForm == 2) {
                            RecipeForm.viewModel.getSelectedRecipeName().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    RecipeForm.recipeName = s; //RECIPE TITLE parameter
                                }
                            });
                            RecipeForm.viewModel.getSelectedRecipeDesc().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    RecipeForm.recipeDesc = s;
                                }
                            });
                            RecipeForm.viewModel.getSelectedImg().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    RecipeForm.selectedImg = s;
                                }
                            });
                            if ((RecipeForm.recipeName == "" || RecipeForm.recipeName.isEmpty() || RecipeForm.recipeName == null) &&
                                    (RecipeForm.recipeDesc == "" || RecipeForm.recipeDesc.isEmpty() || RecipeForm.recipeDesc == null) &&
                                    (RecipeForm.selectedImg == "" || RecipeForm.selectedImg.isEmpty() || RecipeForm.selectedImg == null)) {
                                mainFAB.show();
                                checkFormsNum = 1;
                                getSupportFragmentManager().popBackStack();
                                getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, hawkerCornerMain, "HC")
                                        .addToBackStack(null).commit();
                            } else {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this); //Context is getActivity

                                //Set title
                                builder1.setTitle("Wait!");
                                builder1.setMessage("Do you want to save this to drafts?");

                                builder1.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        saveToRecipeDrafts();
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
                        } else { //If enter forms
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this); //Context is getActivity

                            //Set title
                            builder1.setTitle("Wait!");
                            builder1.setMessage("Do you want to save this to drafts?");

                            builder1.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    saveToHawkerDrafts();
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
                            Fragment checkFragment = getSupportFragmentManager().findFragmentByTag("RC");
                            //If current Fragment is RC Page
                            if (checkFragment != null && checkFragment.isVisible()) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, recipeCornerMain, "RC")
                                        .commit();
                            }
                            else{
                                getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, recipeCornerMain, "RC")
                                        .addToBackStack(null).commit();
                            }

                        } else if ((whichForm == 1 && HawkerForm.stallName == "" || whichForm == 1 && HawkerForm.stallName.isEmpty() || whichForm == 1 && HawkerForm.stallName == null) &&
                                (whichForm == 1 && HawkerForm.desc == "" || whichForm == 1 && HawkerForm.desc.isEmpty() || whichForm == 1 && HawkerForm.desc == null) &&
                                (whichForm == 1 && HawkerForm.shortDesc == "" || whichForm == 1 && HawkerForm.shortDesc.isEmpty() || whichForm == 1 && HawkerForm.shortDesc == null) &&
                                (whichForm == 1 && HawkerForm.address == "" || whichForm == 1 && HawkerForm.address.isEmpty() || whichForm == 1 && HawkerForm.address == null) &&
                                (whichForm == 1 && HawkerForm.downUrl == "" || whichForm == 1 && HawkerForm.downUrl.isEmpty() || whichForm == 1 && HawkerForm.downUrl == null)) {
                            mainFAB.show();
                            checkFormsNum = 1;
                            getSupportFragmentManager().popBackStack();
                            getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, recipeCornerMain, "RC")
                                    .addToBackStack(null).commit();
                        } else if (whichForm == 2) {
                            RecipeForm.viewModel.getSelectedRecipeName().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    RecipeForm.recipeName = s; //RECIPE TITLE parameter
                                }
                            });
                            RecipeForm.viewModel.getSelectedRecipeDesc().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    RecipeForm.recipeDesc = s;
                                }
                            });
                            RecipeForm.viewModel.getSelectedImg().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    RecipeForm.selectedImg = s;
                                }
                            });
                            if ((RecipeForm.recipeName == "" || RecipeForm.recipeName.isEmpty() || RecipeForm.recipeName == null) &&
                                    (RecipeForm.recipeDesc == "" || RecipeForm.recipeDesc.isEmpty() || RecipeForm.recipeDesc == null) &&
                                    (RecipeForm.selectedImg == "" || RecipeForm.selectedImg.isEmpty() || RecipeForm.selectedImg == null)) {
                                mainFAB.show();
                                checkFormsNum = 1;
                                getSupportFragmentManager().popBackStack();
                                getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, recipeCornerMain, "RC")
                                        .addToBackStack(null).commit();
                            } else {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this); //Context is getActivity

                                //Set title
                                builder1.setTitle("Wait!");
                                builder1.setMessage("Do you want to save this to drafts?");

                                builder1.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        saveToRecipeDrafts();
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
                        } else { //If enter forms
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this); //Context is getActivity

                            //Set title
                            builder1.setTitle("Wait!");
                            builder1.setMessage("Do you want to save this to drafts?");

                            builder1.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    saveToHawkerDrafts();
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
                        if (checkFormsNum == 1) { //If never enter forms
                            mainFAB.show();
//                            getSupportFragmentManager().popBackStack();
                            Fragment checkFragment = getSupportFragmentManager().findFragmentByTag("Profile");
                            //If current Fragment is Profile Page
                            if (checkFragment != null && checkFragment.isVisible()) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, profile, "Profile")
                                        .commit();
                            }
                            else{
                                getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, profile, "Profile")
                                        .addToBackStack(null).commit();
                            }
                        } else if ((whichForm == 1 && HawkerForm.stallName == "" || whichForm == 1 && HawkerForm.stallName.isEmpty() || whichForm == 1 && HawkerForm.stallName == null) &&
                                (whichForm == 1 && HawkerForm.desc == "" || whichForm == 1 && HawkerForm.desc.isEmpty() || whichForm == 1 && HawkerForm.desc == null) &&
                                (whichForm == 1 && HawkerForm.shortDesc == "" || whichForm == 1 && HawkerForm.shortDesc.isEmpty() || whichForm == 1 && HawkerForm.shortDesc == null) &&
                                (whichForm == 1 && HawkerForm.address == "" || whichForm == 1 && HawkerForm.address.isEmpty() || whichForm == 1 && HawkerForm.address == null) &&
                                (whichForm == 1 && HawkerForm.downUrl == "" || whichForm == 1 && HawkerForm.downUrl.isEmpty() || whichForm == 1 && HawkerForm.downUrl == null)) {
                            mainFAB.show();
                            checkFormsNum = 1;
                            getSupportFragmentManager().popBackStack();
                            getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, profile, "Profile")
                                    .addToBackStack(null).commit();
                        } else if (whichForm == 2) {
                            RecipeForm.viewModel.getSelectedRecipeName().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    RecipeForm.recipeName = s; //RECIPE TITLE parameter
                                }
                            });
                            RecipeForm.viewModel.getSelectedRecipeDesc().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    RecipeForm.recipeDesc = s;
                                }
                            });
                            RecipeForm.viewModel.getSelectedImg().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    RecipeForm.selectedImg = s;
                                }
                            });
                            if ((RecipeForm.recipeName == "" || RecipeForm.recipeName.isEmpty() || RecipeForm.recipeName == null) &&
                                    (RecipeForm.recipeDesc == "" || RecipeForm.recipeDesc.isEmpty() || RecipeForm.recipeDesc == null) &&
                                    (RecipeForm.selectedImg == "" || RecipeForm.selectedImg.isEmpty() || RecipeForm.selectedImg == null)) {
                                mainFAB.show();
                                checkFormsNum = 1;
                                getSupportFragmentManager().popBackStack();
                                getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, profile, "Profile")
                                        .addToBackStack(null).commit();
                            } else {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this); //Context is getActivity

                                //Set title
                                builder1.setTitle("Wait!");
                                builder1.setMessage("Do you want to save this to drafts?");

                                builder1.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        saveToRecipeDrafts();
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
                        } else { //If enter forms
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this); //Context is getActivity

                            //Set title
                            builder1.setTitle("Wait!");
                            builder1.setMessage("Do you want to save this to drafts?");

                            builder1.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    saveToHawkerDrafts();
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
                if (!FABVisible) { // if false
                    rcFAB.show();
                    hcFAB.show();
                    rcFABText.setVisibility(View.VISIBLE);
                    hcFABText.setVisibility(View.VISIBLE);
                    FABVisible = true;
                } else {
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
                if (hawkerDraftsList.size() == 0){
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.MainFragment, hawkerForm).addToBackStack(null).commit();
                }
                else{
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.MainFragment, hawkerDraftsPage).addToBackStack(null).commit();
                }
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
                if (recipeDraftsList.size() == 0){
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.MainFragment, recipeForm).addToBackStack(null).commit();
                }
                else{
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.MainFragment, recipeDraftsPage).addToBackStack(null).commit();
                }
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
        Fragment homeFragment = getSupportFragmentManager().findFragmentByTag("Home");
        if (homeFragment != null && homeFragment.isVisible()) {
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
        }
        Fragment hcFragment = getSupportFragmentManager().findFragmentByTag("HC");
        if (hcFragment != null && hcFragment.isVisible()) {
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.hc).setChecked(true);
        }
        Fragment rcFragment = getSupportFragmentManager().findFragmentByTag("RC");
        if (rcFragment != null && rcFragment.isVisible()) {
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.rc).setChecked(true);
        }
        Fragment profileFragment = getSupportFragmentManager().findFragmentByTag("Profile");
        if (profileFragment != null && profileFragment.isVisible()) {
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.profile).setChecked(true);
        }
    }

    public void saveToHawkerDrafts() {

        //Set postid as postid if already exist
        if (HawkerForm.chosenstall != null) {
            hawkerDrafts.hcauthor = userProfile.getUsername();
            hawkerDrafts.hccuserpfp = userProfile.getProfileImg();
            hawkerDrafts.hcOwner = userProfile.getUID();

            hawkerDrafts.hcstallname = HawkerForm.stallName;
            hawkerDrafts.shortdesc = HawkerForm.shortDesc;
            hawkerDrafts.hccoverimg = HawkerForm.downUrl;
            hawkerDrafts.hccparagraph = HawkerForm.desc;
            hawkerDrafts.hccaddress = HawkerForm.address;
            hawkerDrafts.postid = HawkerForm.chosenstall.postid;
            hawkerDrafts.daysopen = HawkerForm.daysOpen;
            hawkerDrafts.hoursopen = HawkerForm.finalTime;
        }
        //If postid doesnt exist, get new key
        else {
            hawkerDrafts.hcauthor = userProfile.getUsername();
            hawkerDrafts.hccuserpfp = userProfile.getProfileImg();
            hawkerDrafts.hcOwner = userProfile.getUID();

            hawkerDrafts.hcstallname = HawkerForm.stallName;
            hawkerDrafts.shortdesc = HawkerForm.shortDesc;
            hawkerDrafts.hccoverimg = HawkerForm.downUrl;
            hawkerDrafts.hccparagraph = HawkerForm.desc;
            hawkerDrafts.hccaddress = HawkerForm.address;
            hawkerDrafts.postid = databaseReference.push().getKey();
            hawkerDrafts.daysopen = HawkerForm.daysOpen;
            hawkerDrafts.hoursopen = HawkerForm.finalTime;
        }

//        Toast.makeText(this, HawkerForm.chosenstall.postid, Toast.LENGTH_SHORT).show();
        HwkDraftUp(hawkerDrafts, hawkerDrafts.postid);
    }

    public void saveToRecipeDrafts() {
        RecipeForm.viewModel.getSelectedRecipeName().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                RecipeForm.recipeName = s; //RECIPE TITLE parameter
            }
        });
        RecipeForm.viewModel.getSelectedRecipeDesc().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                RecipeForm.recipeDesc = s; //DESCRIPTION parameter
            }
        });
        RecipeForm.viewModel.getSelectedRecipeDuration().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                RecipeForm.duration = s; //DURATION parameter
            }
        });
        RecipeForm.viewModel.getSelectedRecipeSteps().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                RecipeForm.steps = s; //STEPS parameter
            }
        });
        RecipeForm.viewModel.getSelectedRecipeIngred().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                RecipeForm.totalIngred = s;//INGREDIENT parameter
            }
        });
        RecipeForm.viewModel.getSelectedDifficulty().observe(recipeForm.getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer s) {
                RecipeForm.difficulty = s;//difficulty parameter
            }
        });
        RecipeForm.viewModel.getSelectedImg().observe(recipeForm.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                RecipeForm.selectedImg = s;//Image parameter
            }
        });
        //Set postid as postid if already exist
        if (recipeForm.status == 2) {
            recipeDrafts.recipeName = RecipeForm.recipeName;
            recipeDrafts.postID = RecipeForm.recipePostID;
            recipeDrafts.owner = userProfile.getUID();
            recipeDrafts.recipeDescription = RecipeForm.recipeDesc;
            recipeDrafts.duration = RecipeForm.duration;
            recipeDrafts.recipeRating = RecipeForm.difficulty;
            recipeDrafts.userName = userProfile.getUsername();
            recipeDrafts.steps = RecipeForm.steps;
            recipeDrafts.ingredients = RecipeForm.totalIngred;
            recipeDrafts.foodImage = RecipeForm.selectedImg;
        }
        //If postid doesnt exist, get new key
        else {
            recipeDrafts.recipeName = RecipeForm.recipeName;
            recipeDrafts.postID = databaseReference.push().getKey();
            recipeDrafts.owner = userProfile.getUID();
            recipeDrafts.recipeDescription = RecipeForm.recipeDesc;
            recipeDrafts.duration = RecipeForm.duration;
            recipeDrafts.recipeRating = RecipeForm.difficulty;
            recipeDrafts.userName = userProfile.getUsername();
            recipeDrafts.steps = RecipeForm.steps;
            recipeDrafts.ingredients = RecipeForm.totalIngred;
            recipeDrafts.foodImage = RecipeForm.selectedImg;
        }

//        Toast.makeText(this, RecipeForm.recipePostID, Toast.LENGTH_SHORT).show();
        RcpDraftUp(recipeDrafts, recipeDrafts.postID);
    }

    public void HwkDraftUp(HawkerCornerStalls HwkDraftObj, String DraftID) {
        mAuth = FirebaseAuth.getInstance();
        databaseReference.child("Drafts").child("Hawkers").child(mAuth.getUid()).child(DraftID).setValue(HwkDraftObj);
        Toast.makeText(this, "HawkerPost saved to drafts", Toast.LENGTH_SHORT).show();
    }

    private void RcpDraftUp(RecipeCorner rcpDraftObj, String DraftID) {
        mAuth = FirebaseAuth.getInstance();
        databaseReference.child("Drafts").child("Recipes").child(mAuth.getUid()).child(DraftID).setValue(rcpDraftObj);
        Toast.makeText(this, "Recipe saved to drafts", Toast.LENGTH_SHORT).show();
    }
    /**
     * This method check mobile is connected to network.
     * @param context
     * @return true if connected otherwise false.
     */
    public void isNetworkAvailable(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected()){
            Home homeFragment = new Home();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, homeFragment, "Home").commit();
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    findViewById(R.id.floating_main_nav_button).setVisibility(View.VISIBLE);
                }
            }, 2500);
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Oh No!")
                    .setMessage("Network Error! Make sure you are connected to the internet.")
                    .setCancelable(false)
                    .setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();
                            finish();
                        }
                    })
                    .setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            isNetworkAvailable(context);
                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            alert.show();
        }

    }
}