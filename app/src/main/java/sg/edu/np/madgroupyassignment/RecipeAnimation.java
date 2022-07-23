package sg.edu.np.madgroupyassignment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;

public class RecipeAnimation extends Fragment {

    LottieAnimationView lottie;

    public RecipeAnimation() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_animation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lottie = view.findViewById(R.id.recipelottie);
        lottie.animate().setStartDelay(3000);

        //Get bundle from Profile
        Bundle bundle = this.getArguments();
        Boolean status = bundle.getBoolean("status");

        //After Animation send the information received above to RcpFrag
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment profileRcpFragment = new ProfileRecipeRV(status);

                profileRcpFragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, profileRcpFragment).commit();
            }
        }, 3150);
    }
}