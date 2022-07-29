package sg.edu.np.madgroupyassignment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class ProfileSocials extends Fragment {
    Button socialsaveBtn;
    EditText social_insta_input, social_fb_input, social_twt_input;
    TextView social_insta_warning, social_fb_warning, social_twt_warning;
    String usernameID;
    String instagram, facebook, twitter;
    String insta, fb, twt;

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
        //Warning messages
        social_insta_warning = view.findViewById(R.id.instaWarning);
        social_fb_warning = view.findViewById(R.id.fbWarning);
        social_twt_warning = view.findViewById(R.id.twtWarning);
        //Set warnings
        social_insta_warning.setText("Please only enter alphabets, numbers, period and underscores");
        social_twt_warning.setText("Please only enter alphabets, numbers and underscores");
        //Set warning colour
        social_insta_warning.setTextColor(getResources().getColor(R.color.Main_Primary));
        social_twt_warning.setTextColor(getResources().getColor(R.color.Main_Primary));
        //Set Visibility
        social_insta_warning.setVisibility(View.GONE);
        social_fb_warning.setVisibility(View.GONE);
        social_twt_warning.setVisibility(View.GONE);


        if (insta.equals("") == false) {
            social_insta_input.setText(insta);
        }
        if (fb.equals("") == false) {
            social_fb_input.setText(fb);
        }
        if (twt.equals("") == false) {
            social_twt_input.setText(twt);
        }

        //Validate insta
        social_insta_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                socialsaveBtn.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (social_insta_input.getText().toString().matches("[a-zA-Z0-9._]*")){
                    socialsaveBtn.setEnabled(true);
                    social_insta_warning.setVisibility(View.GONE);
                }
                else{
                    //Toast.makeText(getContext(), "Invalid username", Toast.LENGTH_SHORT).show();
                    social_insta_warning.setVisibility(View.VISIBLE);
                    socialsaveBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //Validate twitter
        social_twt_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                socialsaveBtn.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (social_twt_input.getText().toString().matches("[a-zA-Z0-9_]*")){
                    socialsaveBtn.setEnabled(true);
                    social_twt_warning.setVisibility(View.GONE);
                }
                else{
                    //Toast.makeText(getContext(), "Invalid username", Toast.LENGTH_SHORT).show();
                    social_twt_warning.setVisibility(View.VISIBLE);
                    socialsaveBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


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

                if (instagram == null) {
                    instagram = "";
                }
                if (twitter == null) {
                    twitter = "";
                }
                if (facebook == null) {
                    facebook = "";
                }
                data.put("instagram", instagram);
                data.put("facebook", facebook);
                data.put("twitter", twitter);
                databaseReference.child("UserProfile").child(usernameID).updateChildren(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, new Profile(true), "Profile").commit();
                    }
                });


                Toast.makeText(getActivity(), "Changes Saved", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
