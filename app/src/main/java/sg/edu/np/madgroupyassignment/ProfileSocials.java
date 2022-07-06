package sg.edu.np.madgroupyassignment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class ProfileSocials extends Fragment {
    Button socialsaveBtn;
    EditText social_insta_input, social_fb_input, social_twt_input;
    String usernameID;
    String instagram, facebook, twitter;
    String insta,fb,twt;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_socials, null);
        Bundle bundle = this.getArguments();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        usernameID = (String) bundle.getString("usernameID");
        insta = (String) bundle.getString("insta");
        twt = (String) bundle.getString("twt");
        fb = (String) bundle.getString("fb");

        // Save changes Button
        socialsaveBtn = view.findViewById(R.id.social_save);

        // Social edit texts
        social_insta_input = view.findViewById(R.id.social_insta_input);
        social_fb_input = view.findViewById(R.id.social_fb_input);
        social_twt_input = view.findViewById(R.id.social_twt_input);

        if (insta.equals("") == false){
            social_insta_input.setText(insta);
        }
        if (fb.equals("") == false){
            social_fb_input.setText(fb);
        }
        if (twt.equals("") == false){
            social_twt_input.setText(twt);
        }

        //When Changes are saved
        socialsaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Updating Database
                HashMap<String, Object> data = new HashMap<>();

                //Getting from edit text
                instagram = social_insta_input.getText().toString();
                facebook = social_fb_input.getText().toString();
                twitter = social_twt_input.getText().toString();

                data.put("instagram", instagram);
                data.put("facebook", facebook);
                data.put("twitter", twitter);
                databaseReference.child("UserProfile").child(usernameID).updateChildren(data);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, new Profile()).addToBackStack(null).commit();

                Toast.makeText(getActivity(), "Changes Saved", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
