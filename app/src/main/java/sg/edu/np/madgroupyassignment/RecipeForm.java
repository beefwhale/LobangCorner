package sg.edu.np.madgroupyassignment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipeForm extends Fragment {
    public TabLayout tabLayout;
    private ViewPager2 viewPager;
    private PostsHolder postsHolder;
    private UserProfile userProfile;
    public static FormsViewModel viewModel;
    private DatabaseReference databaseReferencetest;
    private FirebaseAuth mAuth;
    public static RecipeCorner recipeDrafts;
    public static String recipePostID;

    public static String ownerUID, username, totalIngred, recipeDesc, duration, steps, selectedImg, recipeName;
    public static Integer difficulty;
    RecipeCorner recipeCorner;
    HashMap<String, Object> userCurrentRcp;

    public Integer status;
    OnBackPressedCallback callback;

    public RecipeForm(Integer status) {
        // Required empty public constructor
        //Creating Post = 0, Editing Post = 1, Editing Draft  = 2
        this.status = status;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userProfile = postsHolder.getUserProfile();
        selectedImg = "";
        difficulty = 1;
        // Inflate the layout for this fragment
        databaseReferencetest = FirebaseDatabase.getInstance().getReference();
        View recipeform = inflater.inflate(R.layout.fragment_recipe_form, container, false);

        //Connecting the 3 fragments through tabLayout
        tabLayout = recipeform.findViewById(R.id.tabLayout);
        viewPager = recipeform.findViewById(R.id.viewPager);
        RecipePostVPAdapter vpAdapter = new RecipePostVPAdapter(getChildFragmentManager(), getLifecycle());
        vpAdapter.addFragment(new RecipePostMain(), "Recipe Post");
        vpAdapter.addFragment(new RecipePostIngredients(), "Recipe Ingredients");
        vpAdapter.addFragment(new RecipePostSteps(), "Recipe Steps");
        viewPager.setAdapter(vpAdapter);
        viewPager.setSaveEnabled(false);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(vpAdapter.getPageTitle(position));
            }
        }).attach();

        // making Tab layout not swipeable, only navigate using buttons below / using tabs
        viewPager.setUserInputEnabled(false);

        viewModel = new ViewModelProvider(this).get(FormsViewModel.class);
        recipeDrafts = new RecipeCorner();
        recipeName = "";
        recipeDesc = "";

        //Setting this so it can be referenced in the Main/Ingredients/steps fragment
        if (status.equals(0)) { // If Posting
            viewModel.status(0);
        } else if (status.equals(1)) { // If Editing Post
            viewModel.status(1);

            Bundle bundle = this.getArguments();
            assert bundle != null;
            int chosenrecipeno = (int) bundle.getInt("position");
            ArrayList<RecipeCorner> recipeList = Parcels.unwrap(bundle.getParcelable("list"));
            recipeDrafts = recipeList.get(chosenrecipeno);
            viewModel.recipePost(recipeDrafts);
        } else {
            viewModel.status(1);

            Bundle bundle = this.getArguments();
            assert bundle != null;
            recipeDrafts = Parcels.unwrap(bundle.getParcelable("recipe"));
            viewModel.recipePost(recipeDrafts);
            recipePostID = recipeDrafts.postID;
        }


        viewModel.getchangeFragment().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer i) {
                if (i == 1) { // Change to Ingredients page
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    tab.select();
                } else if (i == 2) { // Steps Page
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    tab.select();
                } else if (i == 0) { // Main Page
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    tab.select();
                } else if (i == 3) { // Submit Button is pressed
                    username = userProfile.getUsername(); //USERNAME parameter

                    viewModel.getSelectedRecipeName().observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            recipeName = s; //RECIPE TITLE parameter
                        }
                    });
                    viewModel.getSelectedRecipeDesc().observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            recipeDesc = s; //DESCRIPTION parameter
                        }
                    });
                    viewModel.getSelectedRecipeDuration().observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            duration = s; //DURATION parameter
                        }
                    });
                    viewModel.getSelectedRecipeSteps().observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            steps = s; //STEPS parameter
                        }
                    });
                    viewModel.getSelectedRecipeIngred().observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            totalIngred = s;//INGREDIENT parameter
                        }
                    });
                    viewModel.getSelectedDifficulty().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer s) {
                            difficulty = s;//difficulty parameter
                        }
                    });
                    viewModel.getSelectedImg().observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            selectedImg = s;//Image parameter
                        }
                    });

                    if (recipeName == null || recipeName.length() == 0 || recipeName.isEmpty() || recipeName == "" ||
                            recipeDesc == null || recipeDesc.length() == 0 || recipeDesc.isEmpty() ||
                            totalIngred == null || totalIngred.length() == 0 || totalIngred.isEmpty() ||
                            steps == null || steps.length() == 0 || steps.isEmpty() ||
                            selectedImg.isEmpty() || selectedImg.length() == 0 || selectedImg == "") {
                        Log.e("ingred", totalIngred);
                        Log.e("steps", steps);
                        Toast.makeText(getActivity(), "Please input recipe title, description, image, ingredients and steps", Toast.LENGTH_SHORT).show();
                    } else {
                        //POSTING:
                        MainActivity.checkFormsNum = 1;
                        MainActivity.mainFAB.show();
                        ownerUID = userProfile.getUID();
                        Long timeStamp;
                        String PostID = "";
                        if (status == 0) {
                            timeStamp = System.currentTimeMillis();
                            PostID = databaseReferencetest.push().getKey();
                            recipeCorner = new RecipeCorner(PostID, ownerUID, recipeName, recipeDesc, difficulty, username, duration, steps, totalIngred, timeStamp, selectedImg, false);
                            userCurrentRcp = userProfile.getRcpList();
                        }
                        //EDITING: POST
                        else if (status == 1) {
                            timeStamp = recipeDrafts.getPostTimeStamp();
                            PostID = recipeDrafts.getPostID();
                            recipeCorner = new RecipeCorner(PostID, ownerUID, recipeName, recipeDesc, difficulty, username, duration, steps, totalIngred, timeStamp, selectedImg, false);
                            userCurrentRcp = userProfile.getRcpList();
                        }
                        else if (status == 2){
                            timeStamp = System.currentTimeMillis();
                            PostID = recipeDrafts.getPostID();
                            recipeCorner = new RecipeCorner(PostID, ownerUID, recipeName, recipeDesc, difficulty, username, duration, steps, totalIngred, timeStamp, selectedImg, false);
                            userCurrentRcp = userProfile.getRcpList();
                        }
                        RcpUp(userCurrentRcp, recipeCorner, PostID, status);
                        //getActivity().recreate();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, new Home(), "Home").commit();
                        MainActivity.bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);

                    }
                    //Toast.makeText(getActivity(), totalIngred, Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.changeFragment(0); // Default Zero: forms main page
                }
            }

        });

        return recipeform;
    }

    private void RcpUp(HashMap<String, Object> userRcpList, RecipeCorner RcpObj, String PostID, Integer status) {
        mAuth = FirebaseAuth.getInstance();
        databaseReferencetest.child("Posts").child("Recipes").child(PostID).setValue(RcpObj);
        viewModel.changeFragment(0);
        //POSTING:
        if (status == 0) {
            userRcpList.put(PostID, PostID);
            databaseReferencetest.child("UserProfile").child(mAuth.getUid()).child("rcpList").updateChildren(userRcpList);
            Toast.makeText(getActivity(), "Recipe Uploaded Successfully!", Toast.LENGTH_SHORT).show();

        }
        //EDITING: POST
        else if (status == 1) {
            Toast.makeText(getActivity(), "Recipe Edited Successfully!", Toast.LENGTH_SHORT).show();
        } else if (status == 2) {
            userRcpList.put(PostID, PostID);
            databaseReferencetest.child("UserProfile").child(mAuth.getUid()).child("rcpList").updateChildren(userRcpList);
            Toast.makeText(getActivity(), "Recipe Uploaded Successfully!", Toast.LENGTH_SHORT).show();
            //delete drafts from drafts
            databaseReferencetest.child("Drafts").child("Recipes").child(mAuth.getUid()).child(recipeDrafts.getPostID()).removeValue();
        }

    }

    private void RcpDraftUp(/*HashMap<String, Object> userHwkDraftList,*/ RecipeCorner rcpDraftObj, String DraftID) {
        mAuth = FirebaseAuth.getInstance();
        databaseReferencetest.child("Drafts").child("Recipes").child(mAuth.getUid()).child(DraftID).setValue(rcpDraftObj);
//        userHwkDraftList.put(DraftID, DraftID);
//        databaseReferencetest.child("UserProfile").child(mAuth.getUid()).child("hawkList").updateChildren(userHwkDraftList);
        Toast.makeText(getActivity(), "Recipe saved to drafts", Toast.LENGTH_SHORT).show();
    }

    public void retrieveUserProfile() {
        this.userProfile = postsHolder.getUserProfile();
    }

    private void leaveAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity()); //Context is getActivity

        //Set title
        builder1.setTitle("Wait!");
        builder1.setMessage("Do you want to save this to drafts?");

        builder1.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                viewModel.getSelectedRecipeName().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        recipeName = s; //RECIPE TITLE parameter
                    }
                });
                viewModel.getSelectedRecipeDesc().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        recipeDesc = s; //DESCRIPTION parameter
                    }
                });
                viewModel.getSelectedRecipeDuration().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        duration = s; //DURATION parameter
                    }
                });
                viewModel.getSelectedRecipeSteps().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        steps = s; //STEPS parameter
                    }
                });
                viewModel.getSelectedRecipeIngred().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        totalIngred = s;//INGREDIENT parameter
                    }
                });
                viewModel.getSelectedDifficulty().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer s) {
                        difficulty = s;//difficulty parameter
                    }
                });
                viewModel.getSelectedImg().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        selectedImg = s;//Image parameter
                    }
                });

                if (status == 2) {
                    recipeDrafts.recipeName = recipeName;
                    recipeDrafts.owner = userProfile.getUID();
                    recipeDrafts.recipeDescription = recipeDesc;
                    recipeDrafts.duration = duration;
                    recipeDrafts.recipeRating = difficulty;
                    recipeDrafts.userName = userProfile.getUsername();
                    recipeDrafts.steps = steps;
                    recipeDrafts.ingredients = totalIngred;
                    recipeDrafts.foodImage = selectedImg;
                } else {
                    recipeDrafts.recipeName = recipeName;
                    recipeDrafts.postID = databaseReferencetest.push().getKey();
                    recipeDrafts.owner = userProfile.getUID();
                    recipeDrafts.recipeDescription = recipeDesc;
                    recipeDrafts.duration = duration;
                    recipeDrafts.recipeRating = difficulty;
                    recipeDrafts.userName = userProfile.getUsername();
                    recipeDrafts.steps = steps;
                    recipeDrafts.ingredients = totalIngred;
                    recipeDrafts.foodImage = selectedImg;
                }

                RcpDraftUp(recipeDrafts, recipeDrafts.postID);
//                Toast.makeText(getActivity(), recipeDrafts.steps, Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            }
        });

        builder1.setNeutralButton("Don't Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getParentFragmentManager().popBackStack();
            }
        });

        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dismiss Dialog
                dialogInterface.dismiss();
                callback.setEnabled(true);
                MainActivity.checkFormsNum = 0;
            }
        });

        builder1.show();
    }

    @Override
    public void onResume() {

        ownerUID = "";
        username = "";
        totalIngred = "";
        recipeDesc = "";
        duration = "";
        steps = "";
        selectedImg = "";
        recipeName = "";
        difficulty = 1;

        //Toast shows up when back button is pressed
        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                viewModel.getSelectedRecipeName().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        recipeName = s; //RECIPE TITLE parameter
                    }
                });
                if ((recipeName == "" || recipeName.isEmpty() || recipeName == null) &&
                        (recipeDesc == "" || recipeDesc.isEmpty() || recipeDesc == null) &&
                        (selectedImg == "" || selectedImg.isEmpty() || selectedImg == null)) {
                    getParentFragmentManager().popBackStack();
                } else {
                    leaveAlert();
                    //Ensure it doesnt affect when not in forms
                    setEnabled(false);
                }
            }
        };

        if (status == 1) {
            callback.setEnabled(false);
        } else {
            callback.setEnabled(true);
        }

        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
        super.onResume();
    }

    @Override
    public void onPause() {
        callback.setEnabled(false);
        super.onPause();
    }
}