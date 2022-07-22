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

    //private RecipeCornerPosts rcpost;
    public static BookmarkViewModel viewModel;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private Button b;
    public PostsHolder2 postsHolder2;
    public RecipeAdapter adapter;
    public ArrayList<RecipeCorner> rcplist = new ArrayList<>();
    public ArrayList<RecipeCorner> dellist = new ArrayList<>();
    DatabaseReference reference;
    FirebaseAuth mAuth;
    Integer int2=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_bookmark, container, false);
        //rcpost = new RecipeCornerPosts();
        //ArrayList<RecipeCorner> rclist = rcpost.getRcbookmarklist();

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.viewPager);
        b = view.findViewById(R.id.delete);
        b.setVisibility(View.GONE);

        BookmarkVPAdapter vpAdapter = new BookmarkVPAdapter(getChildFragmentManager(), getLifecycle());
        vpAdapter.addFragment(new BHCFragment(), "Hawker Corner");
        vpAdapter.addFragment(new BRCFragment(), "Recipe Corner");
        viewPager2.setAdapter(vpAdapter);
        viewPager2.setUserInputEnabled(false);
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(vpAdapter.getPageTitle(position));
            }
        }).attach();
        viewModel = new ViewModelProvider(this).get(BookmarkViewModel.class);
        rcplist.removeAll(rcplist);
        for (RecipeCorner obj : postsHolder2.getRecipePosts()) {
            rcplist.add(obj);
        }
        reference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        viewModel.getcheckedBox().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer==0){
                    b.setVisibility(View.GONE);
                    LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
                    tabStrip.setEnabled(false);
                    for(int i = 0; i < tabStrip.getChildCount(); i++) {
                        tabStrip.getChildAt(i).setClickable(true);
                    }
                    Log.d("Button", "gone");
                }
                else{
                    b.setVisibility(View.VISIBLE);
                    LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
                    tabStrip.setEnabled(false);
                    for(int i = 0; i < tabStrip.getChildCount(); i++) {
                        tabStrip.getChildAt(i).setClickable(false);
                    }
                    Log.d("Button", integer.toString());
                }
            }
        });
        adapter = new RecipeAdapter(rcplist, c, 1, getParentFragment());
        //viewModel.RcpList(adapter.getDel_rcplist());
//        viewModel.getrcplist().observe(getViewLifecycleOwner(), new Observer<List<RecipeCorner>>() {
//            @Override
//            public void onChanged(List<RecipeCorner> recipeCorners) {
//                if (recipeCorners.isEmpty()){
//                    b.setVisibility(View.INVISIBLE);
//                }
//                else{
//                    b.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//
//        adapter = new RecipeAdapter(rcplist, getActivity(), 1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getrcplist().observe(getViewLifecycleOwner(), new Observer<ArrayList<RecipeCorner>>() {
                    @Override
                    public void onChanged(ArrayList<RecipeCorner> recipeCorners) {
                        if (recipeCorners.size()!=0){
                            dellist = recipeCorners;
                            Toast.makeText(getContext(), Integer.toString(recipeCorners.size()) + " recipe(s) deleted", Toast.LENGTH_SHORT).show();
                            List<RecipeCorner> toRemove = new ArrayList<>();
                            for (RecipeCorner drcpObject : dellist)
                            {
                                toRemove.add(drcpObject);
                                //rcplist.remove(drcpObject);
                                //dellist.remove(drcpObject);
                                reference.child("UserProfile").child(mAuth.getUid()).child("bmrcplist").child(drcpObject.postID).removeValue();
                            }
                            rcplist.removeAll(toRemove);
                            dellist.removeAll(toRemove);
                            adapter.delete(rcplist);
                            viewModel.getRv().observe(getViewLifecycleOwner(), new Observer<RecyclerView>() {
                                @Override
                                public void onChanged(RecyclerView recyclerView) {
                                    recyclerView.setAdapter(adapter);
                                }
                            });
                            b.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });
//        RecipeAdapter recipeAdapter = new RecipeAdapter(rcplist, c, 1);
//

//        if (recipeAdapter.aBoolean == false){
//            b.setVisibility(View.VISIBLE);
//        }
//        else{
//            b.setVisibility(View.INVISIBLE);
//        }

        return view;
    }
}