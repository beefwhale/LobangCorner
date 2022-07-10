package sg.edu.np.madgroupyassignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Glide.with(getApplicationContext()).load("https://c.tenor.com/x8v1oNUOmg4AAAAd/rickroll-roll.gif").into(fishGif);
                    fishGif.setVisibility(View.VISIBLE);
                }

                fishHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fishGif.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        Glide.with(getApplicationContext()).load("https://walfiegif.files.wordpress.com/2021/05/out-transparent-4.gif").into(fishGif);
                        fishHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fishGif.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                fishGif.setVisibility(View.GONE);
                            }
                        }, 2000);
                    }
                }, 8000);

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

//        loginBtn.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                LayoutInflater inflater = getLayoutInflater();
//                View test = inflater.inflate(R.layout.activity_splash_page, (ViewGroup) view.findViewById(R.id.splash));
//                Toast toast = new Toast(getApplicationContext());
//                toast.setView(test);
//                toast.setGravity(Gravity.FILL_VERTICAL|Gravity.FILL_HORIZONTAL, 0, 0);
//                toast.setDuration(Toast.LENGTH_LONG);
//                toast.show();
//                return true;
//            }
//        });
//
//        //
//        LayoutInflater inflater = getLayoutInflater();
//        View test = inflater.inflate(R.layout.activity_splash_page, (ViewGroup) this.findViewById(R.id.splash));
//        Toast toast = new Toast(getApplicationContext());
//        toast.setView(test);
//        toast.setGravity(Gravity.FILL_VERTICAL|Gravity.FILL_HORIZONTAL, 0, 0);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.show();

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


