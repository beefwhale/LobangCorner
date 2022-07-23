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

public class BookmarkSplash extends Fragment {

    LottieAnimationView lottie;

    public BookmarkSplash() {
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
        return inflater.inflate(R.layout.fragment_bookmark_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lottie = view.findViewById(R.id.bookmarksplashlottie);

        //Animation for the text and lottie animation
        lottie.animate().translationX(2000).setStartDelay(3000);

        //After Animation send the information received above to MapFrag
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                BookmarkActivity ba = new BookmarkActivity();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, ba).commit();
            }
        }, 3200);
    }
}