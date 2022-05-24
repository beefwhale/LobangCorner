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
    private String UID;
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

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Registration.this, Login.class);
                startActivity(i);
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);
                String Username = username.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                String CnfPwd = cnfpassword.getText().toString();

                if (!Password.equals(CnfPwd)) {
                    loadingPB.setVisibility(View.GONE);
                    Toast.makeText(Registration.this, "Ensure both passwords are the same", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Username) && TextUtils.isEmpty(Email) && TextUtils.isEmpty(Password) && TextUtils.isEmpty(CnfPwd)) {
                    loadingPB.setVisibility(View.GONE);
                    Toast.makeText(Registration.this, "Please enter credentials", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UID = task.getResult().getUser().getUid();

                                HawkSeed = new HashMap<String, Object>();
                                HawkSeed.put("Seed", "http://www.xinhuanet.com/english/2020-08/03/139259771_15964101188651n.jpg");
                                RcpSeed = new HashMap<String, Object>();
                                RcpSeed.put("Seed", "TestRcpSeed");

                                CreateProfile(UID, Username, Email, HawkSeed, RcpSeed);

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


    private void CreateProfile(String UID, String Username, String Email, HashMap<String, Object> HawkSeed, HashMap<String, Object> RcpSeed) {
        userProfile = new UserProfile(UID, Username, Email, HawkSeed, RcpSeed);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loadingPB.setVisibility(View.GONE);
                databaseReference.child(UID).setValue(userProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Registration.this, "Error - " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}