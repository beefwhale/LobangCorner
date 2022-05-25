package sg.edu.np.madgroupyassignment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class RecipeForm extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

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


        //tabLayout.setupWithViewPager(viewPager);
        RecipePostVPAdapter vpAdapter = new RecipePostVPAdapter(getActivity().getSupportFragmentManager(), getLifecycle());
        vpAdapter.addFragment(new RecipePostMain(),"Recipe Post");
        vpAdapter.addFragment(new RecipePostSteps(),"Recipe Steps");
        vpAdapter.addFragment(new RecipePostIngredients(),"Recipe Ingredients");
        viewPager.setAdapter(vpAdapter);

        new TabLayoutMediator(tabLayout, viewPager,new TabLayoutMediator.TabConfigurationStrategy() {
            @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(vpAdapter.getPageTitle(position));
            }
        }).attach();
        return recipeform;
    }
}