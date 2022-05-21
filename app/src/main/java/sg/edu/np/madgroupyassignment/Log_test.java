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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;
import org.w3c.dom.Text;

public class Log_test extends Fragment {

    private View view;
    private UserProfile userProfile;
    private Button BtnChoose, BtnUp, Logout;
    private ImageView ImgView;
    private TextView testtitle;
    private String Email;
    private ProgressBar loadingPB;

    private Uri ImageUri;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    ActivityResultLauncher<String> getPhoto;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_log_test, null);

        BtnChoose = view.findViewById(R.id.idBtnChoose);
        Logout = view.findViewById(R.id.idLogout);
        BtnUp = view.findViewById(R.id.idBtnUp);
        ImgView = view.findViewById(R.id.idImgPre);
        testtitle = view.findViewById(R.id.TestTitle);
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
        Log.i("Test_Check", userProfile.getUID());
        if (userProfile != null) {
            testtitle.setText(userProfile.getUsername().toString());
            Email = userProfile.getEmail().toString();
        }

        storageReference = FirebaseStorage.getInstance().getReference("ImgUps");
        databaseReference = FirebaseDatabase.getInstance().getReference("UserProfile").child(Email.substring(0, 7).toLowerCase() + "-LbcUID").child("hawkList");
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

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOut();
            }
        });

        return view;
    }

    private String getFileExt(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void upPost() {
        if (ImageUri!=null){
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
                            ImgUp imgUp = new ImgUp(Email.substring(0, 7).toLowerCase() + "-LbcUID", taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
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
        }else{
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