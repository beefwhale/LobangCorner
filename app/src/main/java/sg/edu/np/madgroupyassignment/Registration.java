package sg.edu.np.madgroupyassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
                                aboutMe = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. ";
                                HawkSeed = new HashMap<String, Object>();
                                HawkSeed.put("Seed", "HawkSeed");
                                RcpSeed = new HashMap<String, Object>();
                                RcpSeed.put("Seed", "RcpSeed");

//                                Creating new user profile
                                CreateProfile(UID, Username, Email, profP,aboutMe, HawkSeed, RcpSeed);

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

    private void CreateProfile(String UID, String Username, String Email, String profP, String aboutMe, HashMap<String, Object> HawkSeed, HashMap<String, Object> RcpSeed) {
//        Creating new userProfile object
        userProfile = new UserProfile(UID, Username, Email, profP, aboutMe, HawkSeed, RcpSeed);
//        Adding user profile to database
        databaseReference.child(UID).setValue(userProfile);

        loadingPB.setVisibility(View.GONE);
    }
}