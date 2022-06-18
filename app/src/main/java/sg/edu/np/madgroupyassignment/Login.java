package sg.edu.np.madgroupyassignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

    private ImageView fishInput, fishGif;
    private TextInputEditText email, password;
    private Button loginBtn;
    private ProgressBar loadingPB;
    private TextView registerTV;
    private PostsHolder postsHolder;
    private FirebaseAuth mAuth;
    private Handler fishHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        fishInput = findViewById(R.id.idRegTitle);
        fishGif = findViewById(R.id.fishGif);
        fishHandler = new Handler();
        email = findViewById(R.id.idUserName);
        password = findViewById(R.id.idPassword);
        loginBtn = findViewById(R.id.idBtnLog);
        loadingPB = findViewById(R.id.PBloading);
        registerTV = findViewById(R.id.idNUreg);
        mAuth = FirebaseAuth.getInstance();

        fishInput.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String fishInput = email.getText().toString();
                String fishInputSecondary = password.getText().toString();
                String fishCheck = String.valueOf(postsHolder.getFishRandom());

                if (fishInput.equals(fishCheck) || fishInputSecondary.equals(fishCheck)){
                    fishGif.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext()).load("https://c.tenor.com/x8v1oNUOmg4AAAAd/rickroll-roll.gif").into(fishGif);
                }

                fishHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fishGif.setVisibility(View.GONE);
                    }
                }, 10000);

                return true;
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)) {
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
                            } else {
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(Login.this, "Failed to login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Registration.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
            this.finish();
        }
    }
}


