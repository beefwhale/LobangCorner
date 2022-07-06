package sg.edu.np.madgroupyassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;

public class Registration extends AppCompatActivity {

    private TextInputEditText username, email, password, cnfpassword;
    private String UID, profP, aboutMe;
    private String instagram, facebook, twitter;
    private Button regBtn;
    private ProgressBar loadingPB;
    private TextView loginTV;
    private HashMap<String, Object> HawkSeed, RcpSeed;
    private FirebaseAuth mAuth;
    private UserProfile userProfile;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        // Makes Status Bar Transparent
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_registration);

        username = findViewById(R.id.idUserName);
        email = findViewById(R.id.idEmail);
        password = findViewById(R.id.idPassword);
        cnfpassword = findViewById(R.id.idCnfPwd);
        regBtn = findViewById(R.id.idBtnReg);
        loadingPB = findViewById(R.id.PBloading);
        loginTV = findViewById(R.id.idEUlog);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://lobang-corner-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference("UserProfile");

//        Move to login page
        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Registration.this, Login.class);
                startActivity(i);
            }
        });

//        Registering new user
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);

//                Getting registration information
                String Username = username.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                String CnfPwd = cnfpassword.getText().toString();

//                Checking that information is correct
                if (!Password.equals(CnfPwd)) {
                    loadingPB.setVisibility(View.GONE);
                    Toast.makeText(Registration.this, "Ensure both passwords are the same", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Username) && TextUtils.isEmpty(Email) && TextUtils.isEmpty(Password) && TextUtils.isEmpty(CnfPwd)) {
                    loadingPB.setVisibility(View.GONE);
                    Toast.makeText(Registration.this, "Please enter credentials", Toast.LENGTH_SHORT).show();
                } else {

//                    Creating user
                    mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UID = task.getResult().getUser().getUid();

//                                Setting default data
                                profP = "https://firebasestorage.googleapis.com/v0/b/lobang-corner.appspot.com/o/DefaultProfilePic%2FBlobus.PNG?alt=media&token=2f89a9bb-8292-4578-8d0e-b2fa7e37676d";
                                aboutMe = "Add something about yourself here... ";
                                HawkSeed = new HashMap<String, Object>();
                                HawkSeed.put("Seed", "HawkSeed");
                                RcpSeed = new HashMap<String, Object>();
                                RcpSeed.put("Seed", "RcpSeed");
                                instagram = "";
                                facebook = "";
                                twitter = "";

//                                Creating new user profile
                                CreateProfile(UID, Username, Email, profP, aboutMe, HawkSeed, RcpSeed, instagram, facebook, twitter);

//                                Sending user to login page after registration
                                Toast.makeText(Registration.this, "User Registered...", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Registration.this, Login.class);
                                startActivity(i);
                                finish();

                            } else {
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(Registration.this, "Failed to register user...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });

    }

    private void CreateProfile(String UID, String Username, String Email, String profP, String aboutMe, HashMap<String, Object> HawkSeed, HashMap<String, Object> RcpSeed,
                               String Instagram, String Facebook, String Twitter) {
//        Creating new userProfile object
        userProfile = new UserProfile(UID, Username, Email, profP, aboutMe, HawkSeed, RcpSeed, Instagram, Facebook, Twitter);
//        Adding user profile to database
        databaseReference.child(UID).setValue(userProfile);

        loadingPB.setVisibility(View.GONE);
    }

    // Method to make Status Bar Transparent
    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}