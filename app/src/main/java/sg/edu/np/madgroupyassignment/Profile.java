package sg.edu.np.madgroupyassignment;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends Fragment {

    private static UserProfile userProfile;
    private static String aboutMeInput;
    private static TextView username, hwkObj, rcpObj, fishRandom;
    private static ImageView profP, fishView;
    private Button logout, aboutbtn;
    private ProgressBar loadingPB;
    private EditText aboutme;
    private PostsHolder postsHolder;
    private LayoutInflater layoutInflater;
    private View fish;

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
        hwkObj = view.findViewById(R.id.idHawkObj);
        rcpObj = view.findViewById(R.id.idRcpObj);
        logout = view.findViewById(R.id.idLogout);
        loadingPB = view.findViewById(R.id.PBloading);
        layoutInflater = getLayoutInflater();
        fish = inflater.inflate(R.layout.fish, (ViewGroup) view.findViewById(R.id.fish));
        fishRandom = fish.findViewById(R.id.fishText);
        fishView = fish.findViewById(R.id.fishView);
        aboutbtn = view.findViewById(R.id.about_btn);

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
        aboutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutMeInput = aboutme.getText().toString();
                updateAboutMe(aboutMeInput);
                Toast.makeText(getActivity(), "Changes Saved", Toast.LENGTH_SHORT).show();
            }
        });

//        User hawker posts
        hwkObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (postsHolder.getUserHawkerPosts().size() > 0) {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment profileHcFragment = new ProfileHawkerRV();
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.MainFragment, profileHcFragment).addToBackStack(null).commit();
                } else {
                    Toast.makeText(getActivity(), "No Hawker Corner Posts made", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        User recipe posts
        rcpObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (postsHolder.getUserRecipePosts().size() > 0) {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment profileRcpFragment = new ProfileRecipeRV();
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.MainFragment, profileRcpFragment).addToBackStack(null).commit();
                } else {
                    Toast.makeText(getActivity(), "No Recipe Corner Posts made", Toast.LENGTH_SHORT).show();
                }

            }
        });

//        Sign up button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOut();
            }
        });

        logout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/lobang-corner.appspot.com/o/DefaultProfilePic%2FFish.png?alt=media&token=095f452e-f158-4de3-a533-a5db64eb5e38").into(fishView);
                fishRandom.setText("" + postsHolder.getFishRandom());
                Toast toast = new Toast(getContext());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setView(fish);
                toast.show();
                return true;
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
        if (aboutme != null) {
            aboutme.setText("\n" + userProfile.getAboutMe());
        }
        hwkObj.setText("" + (userProfile.getHawkList().size() - 1) + "\n\nHawker Posts");
        rcpObj.setText("" + (userProfile.getRcpList().size() - 1) + "\n\nRecipe Post");

        getUserPost();
    }

    //    Removes all non-user posts
    private void getUserPost() {
        ProfileHawkerRV profileHawkerRV = new ProfileHawkerRV();
        if (profileHawkerRV.hcadapter != null) {
            profileHawkerRV.hcadapter.notifyDataSetChanged();
        }

        ProfileRecipeRV profileRecipeRV = new ProfileRecipeRV();
        if (profileRecipeRV.adapter != null) {
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
    private void updateAboutMe(String newAboutMe) {
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
                                    for (String hawkerPostUpdate : userProfile.getHawkList().keySet()) {
                                        if (!hawkerPostUpdate.equals("Seed")) {
                                            databaseReference.child("Posts").child("Hawkers").child(hawkerPostUpdate).updateChildren(hawkerPostProfileImgUpdate);
                                        }
                                    }
                                }
                            });

//                            Deleting old image from storage if not default
                            StorageReference storageLocationCheck = FirebaseStorage.getInstance().getReferenceFromUrl(userProfile.getProfileImg());
                            if (storageLocationCheck.getParent().getName().equals("ImgUps")) {
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