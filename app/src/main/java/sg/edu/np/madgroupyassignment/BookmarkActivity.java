package sg.edu.np.madgroupyassignment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends Fragment {
    //variable for context
    Context c;
    //constructor
    public BookmarkActivity(){this.c =c;};

    //private RecipeCornerPosts rcpost;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private Button b;
    public PostsHolder2 postsHolder2;
    public RecipeAdapter adapter;
    public ArrayList<RecipeCorner> rcplist = new ArrayList<>();
    //    public ArrayList<RecipeCorner> dellist = new ArrayList<>();
//    DatabaseReference reference;
//    FirebaseAuth mAuth;
    Boolean aBoolean = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_bookmark, container, false);
        //rcpost = new RecipeCornerPosts();
        //ArrayList<RecipeCorner> rclist = rcpost.getRcbookmarklist();

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.viewPager);
        b = view.findViewById(R.id.delete);


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

        rcplist.removeAll(rcplist);
        for (RecipeCorner obj : postsHolder2.getRecipePosts()) {
            rcplist.add(obj);
        }
//
//        adapter = new RecipeAdapter(rcplist, getActivity(), 1);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dellist = adapter.getDel_rcplist();
//                if (rcplist.isEmpty()) {
//                    Toast.makeText(getContext(), "No recipes found", Toast.LENGTH_SHORT).show();
//                }
//                else if (dellist.isEmpty()){
//                    Toast.makeText(getContext(), "No recipes selected", Toast.LENGTH_SHORT).show();
//
//                }
//                else{
//                    Toast.makeText(getContext(), Integer.toString(dellist.size()) + " recipe(s) deleted", Toast.LENGTH_SHORT).show();
//                    List<RecipeCorner> toRemove = new ArrayList<>();
//                    for (RecipeCorner drcpObject : dellist)
//                    {
//                        toRemove.add(drcpObject);
//                        //rcplist.remove(drcpObject);
//                        //dellist.remove(drcpObject);
//                        reference.child("UserProfile").child(mAuth.getUid()).child("bmrcplist").child(drcpObject.postID).removeValue();
//                    }
//                    rcplist.removeAll(toRemove);
//                    dellist.removeAll(toRemove);
//                    adapter.delete(rcplist);
//                }
//            }
//        });
        RecipeAdapter recipeAdapter = new RecipeAdapter(rcplist, c, 1);
//

        if (recipeAdapter.aBoolean == false){
            b.setVisibility(View.VISIBLE);
        }
        else{
            b.setVisibility(View.INVISIBLE);
        }

        return view;
    }
}