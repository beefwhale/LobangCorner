package sg.edu.np.madgroupyassignment;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.HashMap;

public class Log_test extends Fragment {

    private UserProfile userProfile;
    private TextView Username;
    private String UID, profileImg;
    private Button logout;
    private ImageView profP;
    private ProgressBar loadingPB;

    private Uri ImageUri;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    ActivityResultLauncher<String> getPhoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_log_test, null);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("ImgUps");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        profP = view.findViewById(R.id.idProfP);
        Username = view.findViewById(R.id.TestTitle);
        logout = view.findViewById(R.id.idLogout);
        loadingPB = view.findViewById(R.id.PBloading);
        UID = mAuth.getCurrentUser().getUid();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile retrieved = snapshot.child("UserProfile").child(UID).getValue(UserProfile.class);
                userProfile = retrieved;
                ArrayList<RecipeCorner> rcpList = new ArrayList<>();

//                Recipes not added
//                for (DataSnapshot objectEntry : snapshot.child("Posts").child("Recipes").getChildren()) {
//                    RecipeCorner rcpObject = objectEntry.getValue(RecipeCorner.class);
//                    rcpList.add(rcpObject);
//                }

                //Load profile pic
                Picasso.get().load(retrieved.getProfileImg()).into(profP);
                Username.setText(retrieved.getUsername());
                profileImg = retrieved.getProfileImg();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("test", error.getMessage());
            }
        });

        getPhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        ImageUri = result;
                        //Picasso.get().load(result).into(ImgView);
                        upPost();
                    }
                }
        );

        //Getting image when profile pic is clicked
        profP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhoto.launch("image/*");
            }
        });

//        Sign up button
        logout.setOnClickListener(new View.OnClickListener() {
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

//    Upload chosen image to firebase and set download uri to user profile
    private void upPost() {
        if (ImageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(ImageUri));

            fileReference.putFile(ImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            loadingPB.setVisibility(View.GONE);

                            Toast.makeText(getActivity(), "Upload Successful", Toast.LENGTH_SHORT).show();

                            fileReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    HashMap<String, Object> profilePic = new HashMap<>();

                                    profilePic.put("profileImg", task.getResult().toString());
                                    databaseReference.child("UserProfile").child(UID).updateChildren(profilePic);
                                }
                            });

                        }
                    });
        } else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    //Register for new account
    private void LogOut() {
        Toast.makeText(getActivity(), "User Logged out...", Toast.LENGTH_SHORT).show();
        mAuth.signOut();
        Intent i = new Intent(getActivity(), Login.class);

        startActivity(i);
        getActivity().finish();
    }

}