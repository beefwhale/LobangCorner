package sg.edu.np.madgroupyassignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class RecipePostVPAdapter extends FragmentStateAdapter {
    private final ArrayList<Fragment> fArrayList = new ArrayList<>();
    private final ArrayList<String> fTitle = new ArrayList<>();

    public RecipePostVPAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return fArrayList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fArrayList.add(fragment);
        fTitle.add(title);

    }

    @Nullable
    public CharSequence getPageTitle(int position) {
        return fTitle.get(position);
    }


}
