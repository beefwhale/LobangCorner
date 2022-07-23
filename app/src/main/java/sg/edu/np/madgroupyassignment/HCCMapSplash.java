package sg.edu.np.madgroupyassignment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class HCCMapSplash extends Fragment {
    //Splash page between Hawker Corner Chosen Stall and Hawker Map

    TextView splashtxt;
    LottieAnimationView lottie;

    public HCCMapSplash() {
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
        return inflater.inflate(R.layout.fragment_hcc_map_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        splashtxt = view.findViewById(R.id.hawkermapsplashtxt);
        lottie = view.findViewById(R.id.hawkersplashlottie);

        Bundle bundle = this.getArguments();
        //Get the hawker address and name from HCChosenStall to pass to HCCMapFrag
        String hccaddr = bundle.getString("stalladdr");
        String hccname = bundle.getString("stallname");

        //Animation for the text and lottie animation
        splashtxt.animate().translationY(-1400).setDuration(2400).setStartDelay(0);
        lottie.animate().translationX(2000).setStartDelay(3550);

        //After Animation send the information received above to MapFrag
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment stallmap = new HCCMapFrag();
                Bundle bundle2 = new Bundle();
                bundle2.putString("stalladdr", hccaddr);
                bundle2.putString("stallname", hccname);

                stallmap.setArguments(bundle2);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, stallmap).commit();
            }
        }, 3950);
    }
}