package sg.edu.np.madgroupyassignment;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Log_test extends Fragment {

    private HashMap<String, Object> UptV;

    private View view;
    private UserProfile userProfile;
    private Button BtnChoose, BtnUp, Logout, Test;
    private ImageView ImgView;
    private TextView Username;
    private String UID;
    private ProgressBar loadingPB;

    private Uri ImageUri;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference, databaseReferencetest;

    ActivityResultLauncher<String> getPhoto;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_log_test, null);

        BtnChoose = view.findViewById(R.id.idBtnChoose);
        Logout = view.findViewById(R.id.idLogout);
        BtnUp = view.findViewById(R.id.idBtnUp);
        Test = view.findViewById(R.id.idBtnTest);
        ImgView = view.findViewById(R.id.idImgPre);
        Username = view.findViewById(R.id.TestTitle);
        loadingPB = view.findViewById(R.id.PBloading);
        mAuth = FirebaseAuth.getInstance();

        getPhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        ImageUri = result;
                        Picasso.get().load(result).into(ImgView);
                    }
                }
        );

        userProfile = Parcels.unwrap(getActivity().getIntent().getParcelableExtra("UserProfile"));
        if (userProfile != null) {
            Username.setText(userProfile.getUsername().toString());
            UID = userProfile.getUID();
            UptV = userProfile.getRcpList();
        }

        storageReference = FirebaseStorage.getInstance().getReference("ImgUps");
        databaseReference = FirebaseDatabase.getInstance().getReference("UserProfile").child(UID).child("hawkList");
        //^to be changed

        BtnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhoto.launch("image/*");
            }
        });

        BtnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upPost();
            }
        });

        Test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RcpUp(UptV);
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOut();
            }
        });

        return view;
    }

    private void RcpUp(HashMap<String, Object> UptV) {
        databaseReferencetest = FirebaseDatabase.getInstance().getReference();

        RecipeCorner RCP = new RecipeCorner("Testname", "TestDescription", 4, 5, "TestUser");
        String PostID = databaseReferencetest.push().getKey();
        databaseReferencetest.child("Posts").child("Recipes").child(PostID).setValue(RCP);

        UptV.put(PostID, PostID);
        databaseReferencetest.child("UserProfile").child(mAuth.getUid()).child("rcpList").updateChildren(UptV);
        Toast.makeText(getActivity(), "Recipe Uploaded", Toast.LENGTH_SHORT).show();
    }



    private String getFileExt(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void upPost() {
        if (ImageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(ImageUri));

            fileReference.putFile(ImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadingPB.setVisibility(View.GONE);
                                }
                            }, 500);

                            Toast.makeText(getActivity(), "Upload Succesful", Toast.LENGTH_SHORT).show();
                            ImgUp imgUp = new ImgUp(UID, taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                            String UpID = databaseReference.push().getKey();
                            databaseReference.child(UpID).setValue(imgUp);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            loadingPB.setVisibility(View.VISIBLE);
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void LogOut() {
        Toast.makeText(getActivity(), "User Logged out...", Toast.LENGTH_SHORT).show();
        mAuth.signOut();
        Intent i = new Intent(getActivity(), Login.class);
        startActivity(i);
        getActivity().finish();
    }

}