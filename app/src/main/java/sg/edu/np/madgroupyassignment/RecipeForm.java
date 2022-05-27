package sg.edu.np.madgroupyassignment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipeForm extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    Button submitButton;
    UserProfile userProfile;


    String username;
    HashMap<String, Object> totalIngred;
    String recipeName;
    String recipeDesc;
    Integer duration;
    String steps;
    RecipeCorner recipeCorner;

    public RecipeForm() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View recipeform = inflater.inflate(R.layout.fragment_recipe_form, container, false);
        // Inflate the layout for this fragment
        //Connecting the 3 fragments through tabLayout

        tabLayout = recipeform.findViewById(R.id.tabLayout);
        viewPager = recipeform.findViewById(R.id.viewPager);
        submitButton = recipeform.findViewById(R.id.submitbutton);


        RecipePostVPAdapter vpAdapter = new RecipePostVPAdapter(getChildFragmentManager(), getLifecycle());
        vpAdapter.addFragment(new RecipePostMain(),"Recipe Post");
        vpAdapter.addFragment(new RecipePostSteps(),"Recipe Steps");
        vpAdapter.addFragment(new RecipePostIngredients(),"Recipe Ingredients");
        viewPager.setAdapter(vpAdapter);
        viewPager.setSaveEnabled(false);

        new TabLayoutMediator(tabLayout, viewPager,new TabLayoutMediator.TabConfigurationStrategy() {
            @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(vpAdapter.getPageTitle(position));
            }
        }).attach();


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userProfile = Parcels.unwrap(getActivity().getIntent().getParcelableExtra("UserProfile"));
                username = userProfile.getUsername(); //USERNAME parameter
//                totalIngred = RecipePostIngredients.totalIngred; //INGREDIENT parameter


//                recipeName = RecipePostMain.title; //RECIPE TITLE parameter
//                recipeDesc = RecipePostMain.desc; //DESCRIPTION parameter
//                duration = RecipePostMain.min; //DURATION parameter
//                steps = RecipePostSteps.finalSteps; //STEPS parameter\

                recipeCorner = new RecipeCorner(recipeName,recipeDesc,0,0,username);
                //Toast.makeText(getActivity(), steps, Toast.LENGTH_SHORT).show();


            }
        });



        return recipeform;
    }



}