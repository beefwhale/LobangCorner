package sg.edu.np.madgroupyassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class Registration extends AppCompatActivity {

    private TextInputEditText username, password, cnfpassword;
    private Button regBtn;
    private ProgressBar loadingPB;
    private TextView loginTV;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        username = findViewById(R.id.idUserName);
        password = findViewById(R.id.idPassword);
        cnfpassword = findViewById(R.id.idCnfPwd);
        regBtn = findViewById(R.id.idBtnReg);
        loadingPB = findViewById(R.id.PBloading);
        loginTV = findViewById(R.id.idEUlog);
        mAuth = FirebaseAuth.getInstance();

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
                String Password = password.getText().toString();
                String CnfPwd = cnfpassword.getText().toString();
                if(!Password.equals(CnfPwd)){
                    loadingPB.setVisibility(View.GONE);
                    Toast.makeText(Registration.this, "Ensure both passwords are the same", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(Username) && TextUtils.isEmpty(Password) && TextUtils.isEmpty(CnfPwd)){
                    loadingPB.setVisibility(View.GONE);
                    Toast.makeText(Registration.this, "Please enter credentials", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(Username, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(Registration.this, "User Registered...", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Registration.this, Login.class);
                                startActivity(i);
                                finish();

                            }else{
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(Registration.this, "Failed to register user...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });

    }
}