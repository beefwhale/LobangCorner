package sg.edu.np.madgroupyassignment;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;

public class Profile extends Fragment {

    private UserProfile userProfile;
    private ArrayList<UserProfile> userProfileList;
    private static String aboutMeInput;
    private static TextView username, hwkObj, rcpObj, fishRandom;
    private static ImageView profP, fishView;
    private static ImageButton socialBtn;
    private static ImageView social_insta, social_twt, social_fb;
    private Button logout, aboutbtn;
    private ProgressBar loadingPB;
    private EditText aboutme;
    private PostsHolder postsHolder;
    private LayoutInflater layoutInflater;
    private View fish;

    private String sAppLink,sPackage,sWebLink;
    private String insta_username,fb_username,twt_username;

    private Uri ImageUri;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    ActivityResultLauncher<String> getPhoto;
    public Boolean status = true;
    public String userID;

    public Profile(Boolean status){
        this.status = status; // true = the users own pfp, false = not the user's own pfp (clicked from a post)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, null);

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

        //Social Section
        socialBtn = view.findViewById(R.id.socialBtn);
        social_insta = view.findViewById(R.id.social_insta);
        social_twt = view.findViewById(R.id.social_twt);
        social_fb = view.findViewById(R.id.social_fb);


        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        // Users own profile page or not (true = user, false = author)
        if (status == false){
            // Getting Username ID (that needs to be shown)
            Bundle bundle = this.getArguments();
            if (bundle != null){
                userProfileList = PostsHolder.getAuthorProfileList(); //Getting entire profile List
                userID = bundle.getString("userID");
                for (UserProfile i: userProfileList) {
                    if (i.getUID().equals(userID)){
                        userProfile = i;
                    }
                }
            }
            socialBtn.setVisibility(View.GONE);
            aboutme.setFocusable(false);
            aboutbtn.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
        }
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
        if (status == true){
            profP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPhoto.launch("image/*");
                }
            });
        }

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
                    Fragment profileHcFragment = new ProfileHawkerRV(status);

                    //pass username
                    Bundle bundle = new Bundle();
                    bundle.putString("username", userProfile.getUsername());
                    bundle.putString("usernameID", userProfile.getUID());
                    bundle.putString("usernameImg", userProfile.getProfileImg());

                    profileHcFragment.setArguments(bundle);

                    //change fragment
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

                    Fragment profileRcpFragment = new ProfileRecipeRV(status);

                    //pass username
                    Bundle bundle = new Bundle();
                    bundle.putString("username", userProfile.getUsername());
                    bundle.putString("usernameID", userProfile.getUID());
                    bundle.putString("usernameImg", userProfile.getProfileImg());

                    profileRcpFragment.setArguments(bundle);

                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.MainFragment, profileRcpFragment).addToBackStack(null).commit();
                } else {
                    Toast.makeText(getActivity(), "No Recipe Corner Posts made", Toast.LENGTH_SHORT).show();
                }

            }
        });

//        Add Social Button
        socialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment socialsFragment = new ProfileSocials();

                //pass username
                Bundle bundle = new Bundle();
                bundle.putString("usernameID", userProfile.getUID());
                bundle.putString("insta", insta_username);
                bundle.putString("fb", fb_username);
                bundle.putString("twt", twt_username);
                socialsFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, socialsFragment).addToBackStack(null).commit();
            }
        });

//      Making Social Buttons GONE if no info added
        if (insta_username.equals("") || insta_username == null){
            social_insta.setVisibility(View.GONE);
        }
        if (fb_username.equals("") || fb_username == null){
            social_fb.setVisibility(View.GONE);
        }
        else{
            Log.e("fb",fb_username);
        }
        if (twt_username.equals("") || twt_username == null){
            social_twt.setVisibility(View.GONE);
        }

//        Instagram Button
        social_insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("Instagram", insta_username+"egg");
                String sAppLink = "https://www.instagram.com/"+insta_username+"/";
                String sPackage = "com.instagram.android";
                String sWebLink = "https://www.instagram.com/"+insta_username+"/";
                openLink(sAppLink, sPackage, sWebLink);
            }
        });

//        Facebook Button
        social_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sAppLink = fb_username;
                String sPackage = "com.facebook.katana";
                String sWebLink = fb_username;
                openLink(sAppLink, sPackage, sWebLink);
            }
        });

//        Twitter Button
        social_twt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sAppLink = "twitter://user?screen_name="+twt_username;
                String sPackage = "com.twitter.android";
                String sWebLink = "https://twitter.com/"+twt_username;
                openLink(sAppLink, sPackage, sWebLink);
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

    // Opening social media links
    private void openLink(String sAppLink, String sPackage, String sWebLink) {
        try{
            Uri uri = Uri.parse(sAppLink);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(sPackage);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        catch(ActivityNotFoundException activityNotFoundException){
            Uri uri = Uri.parse(sWebLink);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    //    Updating profile page
    public void updatePage() {
        MainActivity mainActivity = new MainActivity();
        mainActivity.profileFirstUpdate = false;

        if (status == true){ // users own profile
            userProfile = postsHolder.getUserProfile();
        }

        Picasso.get().load(userProfile.getProfileImg()).into(profP);
        username.setText(userProfile.getUsername());
        if (aboutme != null) {
            aboutme.setText(userProfile.getAboutMe());
        }
        if (userProfile.getHawkList()!= null && userProfile.getRcpList()!= null ){
            hwkObj.setText("" + (userProfile.getHawkList().size() - 1) + "\n\nHawker Posts");
            rcpObj.setText("" + (userProfile.getRcpList().size() - 1) + "\n\nRecipe Post");
        }


        //Socials
        //If there no accounts inside yet
        //INSTAGRAM
        if (userProfile.getInstagram() == null || userProfile.getInstagram().equals("")){
            insta_username = "";
        }
        else{
            insta_username = userProfile.getInstagram();
        }
        //FACEBOOK
        if (userProfile.getFacebook() == null || userProfile.getFacebook().equals("")){
            fb_username = "";
        }
        else{
            fb_username = userProfile.getFacebook();
        }
        if (userProfile.getTwitter() == null || userProfile.getTwitter().equals("")){
            twt_username = "";
        }
        else{
            twt_username = userProfile.getTwitter();
        }
        getUserPost();
    }

    //    Removes all non-user posts
    private void getUserPost() {
        ProfileHawkerRV profileHawkerRV = new ProfileHawkerRV(status);
        if (profileHawkerRV.hcadapter != null) {
            profileHawkerRV.hcadapter.notifyDataSetChanged();
        }

        ProfileRecipeRV profileRecipeRV = new ProfileRecipeRV(status);
        if (profileRecipeRV.rcadapter != null) {
            profileRecipeRV.rcadapter.notifyDataSetChanged();
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

}