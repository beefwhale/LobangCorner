package sg.edu.np.madgroupyassignment;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.widget.EditText;
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
import java.util.Arrays;
import java.util.HashMap;

public class Log_test extends Fragment {

    private static UserProfile userProfile;
    private static String aboutMeInput;
    private static TextView username,hwkObj, rcpObj;
    private static ImageView profP;
    private Button logout, testbtn, aboutbtn;
    private ProgressBar loadingPB;
    private EditText  aboutme;
    PostsHolder postsHolder;

    private Uri ImageUri;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    ActivityResultLauncher<String> getPhoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_log_test, null);

        profP = view.findViewById(R.id.idProfP);
        username = view.findViewById(R.id.TestTitle);
        aboutme = view.findViewById(R.id.idAbtme);
        aboutbtn = view.findViewById(R.id.about_btn);
        hwkObj = view.findViewById(R.id.idHawkObj);
        rcpObj = view.findViewById(R.id.idRcpObj);
        logout = view.findViewById(R.id.idLogout);
        loadingPB = view.findViewById(R.id.PBloading);
        testbtn = view.findViewById(R.id.idBtnTest);
//        input = view.findViewById(R.id.idAbtme);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

//        Updating page
        updatePage();

//        Getting image
        getPhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        ImageUri = result;
                        upPost();
                    }
                }
        );

//        Getting image when profile pic is clicked
        profP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhoto.launch("image/*");
            }
        });

//        Changes about me text on clicked

        // Changes line colour of edit Text
        aboutme.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
        aboutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutMeInput = aboutme.getText().toString();
                updateAboutMe(aboutMeInput);
                Toast.makeText(getActivity(),"Changes Saved",Toast.LENGTH_SHORT).show();
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("Change profile description");
//                input = new EditText(getActivity());
//                builder.setView(input);
//
//                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        aboutMeInput = input.getText().toString();
//                        updateAboutMe(aboutMeInput);
//                    }
//                });
//
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//
//                AlertDialog alert = builder.create();
//                alert.show();
            }
        });

//        User hawker posts
        hwkObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment profileHcFragment = new ProfileHawkerRV();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.MainFragment, profileHcFragment).addToBackStack(null).commit();
            }
        });

//        User recipe posts
        rcpObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment profileRcpFragment = new ProfileRecipeRV();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.MainFragment, profileRcpFragment).addToBackStack(null).commit();
            }
        });

//        Sign up button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOut();
            }
        });

//        Test button
        testbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (HawkerCornerStalls i : postsHolder.getRecentHawkerPosts()){
                    Log.d("duplicate2", i.getHcstallname());
                }
                Log.d("duplicate2", "" + postsHolder.getRecentHawkerPosts().length);
            }
        });

        return view;
    }

//    Updating profile page
    public void updatePage() {
        MainActivity mainActivity = new MainActivity();
        mainActivity.profileFirstUpdate = false;

        Picasso.get().load(userProfile.getProfileImg()).into(profP);
        username.setText(userProfile.getUsername());
        if (aboutme != null){
            aboutme.setText(userProfile.getAboutMe());
        }
        hwkObj.setText("" + (userProfile.getHawkList().size()-1) + "\n\nHawker Posts");
        rcpObj.setText("" + (userProfile.getRcpList().size()-1) + "\n\nRecipe Post");

        getUserPost();
    }

//    Removes all non-user posts
    private void getUserPost() {
        ProfileHawkerRV profileHawkerRV = new ProfileHawkerRV();
        if (profileHawkerRV.hcadapter != null){
            profileHawkerRV.hcadapter.notifyDataSetChanged();
        }

        ProfileRecipeRV profileRecipeRV = new ProfileRecipeRV();
        if (profileRecipeRV.adapter != null){
            profileRecipeRV.adapter.notifyDataSetChanged();
        }
    }

//    Getting file extension
    private String getFileExt(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

//    Setting new about me
    private void updateAboutMe(String newAboutMe){
        HashMap<String, Object> data = new HashMap<>();

        data.put("aboutMe", newAboutMe);
        databaseReference.child("UserProfile").child(userProfile.getUID()).updateChildren(data);
    }

//    Upload chosen image to firebase and changes profile picture in user profile
    private void upPost() {
        if (ImageUri != null) {
            StorageReference fileReference = storageReference.child("ImgUps").child(System.currentTimeMillis() + "." + getFileExt(ImageUri));

//            Putting new profile image into storage
            fileReference.putFile(ImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            loadingPB.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Upload Successful", Toast.LENGTH_SHORT).show();

//                            Getting new image url
                            fileReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String downUrl = task.getResult().toString();
                                    HashMap<String, Object> profilePic = new HashMap<>();
                                    profilePic.put("profileImg", downUrl);
                                    databaseReference.child("UserProfile").child(userProfile.getUID()).updateChildren(profilePic);

                                    HashMap<String, Object> hawkerPostProfileImgUpdate = new HashMap<>();
                                    hawkerPostProfileImgUpdate.put("hccuserpfp", downUrl);

//                                    Change hawker post profile picture
                                    for (String hawkerPostUpdate : userProfile.getHawkList().keySet()){
                                        if (!hawkerPostUpdate.equals("Seed")) {
                                            databaseReference.child("Posts").child("Hawkers").child(hawkerPostUpdate).updateChildren(hawkerPostProfileImgUpdate);
                                        }
                                    }
                                }
                            });

//                            Deleting old image from storage if not default
                            StorageReference storageLocationCheck = FirebaseStorage.getInstance().getReferenceFromUrl(userProfile.getProfileImg());
                            if (storageLocationCheck.getParent().getName().equals("ImgUps")){
                                storageLocationCheck.delete();
                            }

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


    //Update on database change
    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

}