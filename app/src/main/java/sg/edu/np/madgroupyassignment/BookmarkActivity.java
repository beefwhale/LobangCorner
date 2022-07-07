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

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class BookmarkActivity extends Fragment {
    //variable for context
    Context c;
    //constructor
    public BookmarkActivity(){this.c =c;};

    //private RecipeCornerPosts rcpost;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_bookmark, container, false);
        //rcpost = new RecipeCornerPosts();
        //ArrayList<RecipeCorner> rclist = rcpost.getRcbookmarklist();

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.viewPager);

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

        return view;
    }
}