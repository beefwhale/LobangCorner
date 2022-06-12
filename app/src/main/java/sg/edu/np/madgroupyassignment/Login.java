package sg.edu.np.madgroupyassignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.Calendar;

public class Login extends AppCompatActivity {

    private TextInputEditText email, password;
    private String UID;
    private Button loginBtn;
    private ProgressBar loadingPB;
    private TextView registerTV;
    private FirebaseAuth mAuth;
    private UserProfile userProfile;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ValueEventListener Listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.idUserName);
        password = findViewById(R.id.idPassword);
        loginBtn = findViewById(R.id.idBtnLog);
        loadingPB = findViewById(R.id.PBloading);
        registerTV = findViewById(R.id.idNUreg);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://lobang-corner-default-rtdb.asia-southeast1.firebasedatabase.app");

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Registration.class);
                startActivity(i);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                if (TextUtils.isEmpty(Email) && TextUtils.isEmpty(Password)) {
                    loadingPB.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "Please enter your login credentials", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(Login.this, "Login Successful...", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Login.this, MainActivity.class);

                                startActivity(i);
                                finish();
//                                UID = task.getResult().getUser().getUid();
//                                GetProfile(UID);
                            } else {
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(Login.this, "Failed to login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }
//
//    private void GetProfile(String UID) {
//        databaseReference = firebaseDatabase.getReference("UserProfile").child(UID);
//        Listener = databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                userProfile = snapshot.getValue(UserProfile.class);
//
//                loadingPB.setVisibility(View.GONE);
//                Toast.makeText(Login.this, "Login Successful...", Toast.LENGTH_SHORT).show();
//
//                Intent i = new Intent(Login.this, MainActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("UserProfile", Parcels.wrap(userProfile));
//                i.putExtras(bundle);
//
//                databaseReference.removeEventListener(Listener);
//                startActivity(i);
//                finish();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
            this.finish();
        }
    }

}




