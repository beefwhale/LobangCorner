package sg.edu.np.madgroupyassignment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    Button submitButton;
    private static UserProfile userProfile;
    FormsViewModel viewModel;
    private DatabaseReference databaseReferencetest;
    private FirebaseAuth mAuth;

    String ownerUID;
    String username;
    String totalIngred;
    String recipeName;
    String recipeDesc;
    String duration;
    String steps;
    String selectedImg;
    Integer difficulty;
    RecipeCorner recipeCorner;
    HashMap<String, Object> userCurrentRcp;

    public RecipeForm() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        selectedImg = "";
        difficulty = 1;
        // Inflate the layout for this fragment
        databaseReferencetest = FirebaseDatabase.getInstance().getReference();
        View recipeform = inflater.inflate(R.layout.fragment_recipe_form, container, false);


        //Connecting the 3 fragments through tabLayout
        tabLayout = recipeform.findViewById(R.id.tabLayout);
        viewPager = recipeform.findViewById(R.id.viewPager);
        submitButton = recipeform.findViewById(R.id.submitbutton);
        RecipePostVPAdapter vpAdapter = new RecipePostVPAdapter(getChildFragmentManager(), getLifecycle());
        vpAdapter.addFragment(new RecipePostMain(), "Recipe Post");
        vpAdapter.addFragment(new RecipePostSteps(), "Recipe Steps");
        vpAdapter.addFragment(new RecipePostIngredients(), "Recipe Ingredients");
        viewPager.setAdapter(vpAdapter);
        viewPager.setSaveEnabled(false);



        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(vpAdapter.getPageTitle(position));
            }
        }).attach();

        viewModel = new ViewModelProvider(this).get(FormsViewModel.class);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                if(recipeName == null || recipeName.length() == 0 || recipeName.isEmpty() ||
                        recipeDesc == null || recipeDesc.length() == 0 || recipeDesc.isEmpty() ||
                            totalIngred == null || totalIngred.length() == 0 || totalIngred.isEmpty() ||
                                steps == null || steps.length() == 0 || steps.isEmpty() ||
                                    selectedImg.isEmpty() || selectedImg.length() == 0 || selectedImg == ""){
                    Toast.makeText(getActivity(), "Please input recipe title, description, image, ingredients and steps", Toast.LENGTH_SHORT).show();
                }
                else{
                    ownerUID = userProfile.getUID();
                    Long timeStamp = System.currentTimeMillis();
                    String PostID = databaseReferencetest.push().getKey();
                    recipeCorner = new RecipeCorner(PostID, ownerUID, recipeName, recipeDesc, difficulty, username, duration, steps,totalIngred, timeStamp, selectedImg);
                    userCurrentRcp = userProfile.getRcpList();
                    RcpUp(userCurrentRcp, recipeCorner, PostID);
                    //getActivity().recreate();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, new Home()).commit();
                }
                //Toast.makeText(getActivity(), totalIngred, Toast.LENGTH_SHORT).show();

            }
        });

        return recipeform;
    }

    private void RcpUp(HashMap<String, Object> userRcpList, RecipeCorner RcpObj, String PostID) {
        mAuth = FirebaseAuth.getInstance();

        databaseReferencetest.child("Posts").child("Recipes").child(PostID).setValue(RcpObj);

        userRcpList.put(PostID, PostID);
        databaseReferencetest.child("UserProfile").child(mAuth.getUid()).child("rcpList").updateChildren(userRcpList);
        Toast.makeText(getActivity(), "Recipe Uploaded", Toast.LENGTH_SHORT).show();
    }

    public void retrieveUserProfile(UserProfile userProfile){
        this.userProfile = userProfile;
    }


}