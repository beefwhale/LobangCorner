package sg.edu.np.madgroupyassignment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends Fragment {
    //variable for context
    Context c;
    //constructor
    public BookmarkActivity(){this.c =c;};

    public static BookmarkViewModel viewModel;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private Button b;
    public PostsHolder2 postsHolder2;
    public RecipeAdapter adapter;
    public HCMainsAdapter hcadapter;
    public ArrayList<RecipeCorner> rcplist = new ArrayList<>();
    public ArrayList<RecipeCorner> dellist = new ArrayList<>();
    public ArrayList<HawkerCornerStalls> hwklist = new ArrayList<>();
    public ArrayList<HawkerCornerStalls> hdellist = new ArrayList<>();
    DatabaseReference reference;
    FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_bookmark, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.viewPager);
        b = view.findViewById(R.id.delete);
        b.setVisibility(View.GONE);     //delete button will not be displayed
        reference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //Connecting the 2 fragments through tabLayout
        BookmarkVPAdapter vpAdapter = new BookmarkVPAdapter(getChildFragmentManager(), getLifecycle());
        vpAdapter.addFragment(new BHCFragment(), "Hawker Corner");
        vpAdapter.addFragment(new BRCFragment(), "Recipe Corner");
        viewPager2.setAdapter(vpAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(vpAdapter.getPageTitle(position));
            }
        }).attach();

        viewModel = new ViewModelProvider(this).get(BookmarkViewModel.class);

        //getting the RC object from firebase and adding to arraylist
        rcplist.removeAll(rcplist);
        for (RecipeCorner obj : postsHolder2.getRecipePosts()) {
            rcplist.add(obj);
        }

        //getting the HC object from firebase and adding to arraylist
        hwklist.removeAll(hwklist);
        for (HawkerCornerStalls obj : postsHolder2.getHawkerPosts()) {
            hwklist.add(obj);
        }

        LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
        tabStrip.setEnabled(false);

        //actively checks and gets the bookmarked recipes from viewmodel
        viewModel.getrcplist().observe(getViewLifecycleOwner(), new Observer<ArrayList<RecipeCorner>>() {
            @Override
            public void onChanged(ArrayList<RecipeCorner> recipeCorners) {
                if (recipeCorners.size()==0){           //if there is no recipes bookmarked
                    b.setVisibility(View.GONE);         //dont display the delete button
                    viewPager2.setUserInputEnabled(true);   //enable user to swipe between the tabs
                    for(int i = 0; i < tabStrip.getChildCount(); i++) {
                        tabStrip.getChildAt(i).setClickable(true);      //able click on other tabs on tablayout
                    }
                }
                else{                                   //if there are recipes bookmarked
                    b.setVisibility(View.VISIBLE);              //display the delete button
                    viewPager2.setUserInputEnabled(false);      //user is unable to swipe between the tabs
                    for(int i = 0; i < tabStrip.getChildCount(); i++) {
                        tabStrip.getChildAt(i).setClickable(false);     //unable to click on other tabs on tablayout
                    }
                }
            }
        });

        //actively checks and gets the bookmarked stalls from viewmodel (same as rcp)
        viewModel.gethclist().observe(getViewLifecycleOwner(), new Observer<ArrayList<HawkerCornerStalls>>() {
            @Override
            public void onChanged(ArrayList<HawkerCornerStalls> hawkerCornerStalls) {
                if (hawkerCornerStalls.size()==0){
                    b.setVisibility(View.GONE);
                    for(int i = 0; i < tabStrip.getChildCount(); i++) {
                        tabStrip.getChildAt(i).setClickable(true);
                    }
                }
                else{
                    b.setVisibility(View.VISIBLE);
                    for(int i = 0; i < tabStrip.getChildCount(); i++) {
                        tabStrip.getChildAt(i).setClickable(false);
                    }
                }
            }
        });

        //getting the rc and hc adapters
        adapter = new RecipeAdapter(rcplist, c, 1, getParentFragment());
        hcadapter = new HCMainsAdapter(hwklist, false, getParentFragment());

        //when unsave button is clicked
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getrcplist().observe(getViewLifecycleOwner(), new Observer<ArrayList<RecipeCorner>>() {
                    @Override
                    public void onChanged(ArrayList<RecipeCorner> recipeCorners) {
                        if (recipeCorners.size()!=0){
                            dellist = recipeCorners;
                            Toast.makeText(getContext(), "Unsaved!", Toast.LENGTH_SHORT).show();    //display toast message
                            List<RecipeCorner> toRemove = new ArrayList<>();
                            for (RecipeCorner drcpObject : dellist)
                            {
                                toRemove.add(drcpObject);   //add the unsaved recipes to an empty list
                                reference.child("UserProfile").child(mAuth.getUid()).child("bmrcplist").child(drcpObject.postID).removeValue(); //remove every recipe from the deleted list from firebase
                            }
                            rcplist.removeAll(toRemove);    //remove the unsaved recipes from recipelist
                            dellist.removeAll(toRemove);    //remove the unsaved recipes from the deleted list
                            adapter.delete(rcplist);        //updates adapter
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, new Home(), "Home").commit();   //go back to home fragment once unsaved
                        }
                    }
                });
                //same as rc
                viewModel.gethclist().observe(getViewLifecycleOwner(), new Observer<ArrayList<HawkerCornerStalls>>() {
                    @Override
                    public void onChanged(ArrayList<HawkerCornerStalls> hawkerCornerStalls) {
                        if (hawkerCornerStalls.size()!=0){
                            hdellist = hawkerCornerStalls;
                            Toast.makeText(getContext(), "Unsaved!", Toast.LENGTH_SHORT).show();
                            List<HawkerCornerStalls> toRemove2 = new ArrayList<>();
                            for (HawkerCornerStalls dhwkObject : hdellist) {
                                toRemove2.add(dhwkObject);
                                reference.child("UserProfile").child(mAuth.getUid()).child("bmhawklist").child(dhwkObject.postid).removeValue();
                            }
                            hwklist.removeAll(toRemove2);
                            hdellist.removeAll(toRemove2);
                            hcadapter.delete(hwklist);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, new Home(), "Home").commit();
                        }
                    }
                });
            }
        });
        return view;
    }
}