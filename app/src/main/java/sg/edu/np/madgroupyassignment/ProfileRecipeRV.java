package sg.edu.np.madgroupyassignment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

public class ProfileRecipeRV extends Fragment{

    private Context c;

    private PostsHolder postsHolder;
    private ArrayList<RecipeCorner> recipeCornersList = new ArrayList<>();

    //Recyclerview & Adapter so different functions can access
    private static TextView rcheader;
    RecyclerView recipeRV;
    RecipeAdapter rcadapter;
    String username;
    String usernameID;
    String usernameImg;
    ImageButton deleteBtn;
    ImageButton editBtn;

    CheckBox checkBox;
    AlertDialog.Builder builder;
    ArrayList<Integer> listPos;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    Boolean status;
    public ProfileRecipeRV(Boolean status){// User;s own profile = true, Author's profile = false

        this.c = c;
        this.status = status;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_reciperv, container, false);

        Bundle bundle = this.getArguments();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        //Make sure bundle
        assert bundle != null;
        username = (String) bundle.getString("username");
        usernameID = (String) bundle.getString("usernameID");
        usernameImg = (String) bundle.getString("usernameImg");
        rcheader = view.findViewById(R.id.profile_rc_header);
        //Setting personalised header
        rcheader.setText(username + "'s Recipe Corner");

        deleteBtn = view.findViewById(R.id.deleteBtn);
        editBtn = view.findViewById(R.id.editBtn);
        rcadapter = new RecipeAdapter(recipeCornersList, c, 2);
        if (status == false){
            deleteBtn.setVisibility(View.GONE);
            editBtn.setVisibility(View.GONE);
        }

//        Deleting Users own post
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(getContext());
                //At least one post selected
                if (rcadapter.cbCount > 0){
                    //Setting message manually and performing action on button click
                    builder.setTitle("Confirm Delete ?")
                            .setMessage("You sure you want to permanently delete ("+rcadapter.cbCount+") posts?")
                            .setCancelable(false)
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
//                                Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
//                                        Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    //List of selected positions in RV (Checked)
                                    listPos = rcadapter.listPos;
                                    //Sort list in descending order to avoid Array Out of Bounds
                                    Collections.sort(listPos, Collections.reverseOrder());
                                    for (int i: listPos) {
                                        RecipeCorner deleteItem = recipeCornersList.get(i);


                                        //removing from database
                                        databaseReference.child("UserProfile").child(usernameID).child("rcpList").child(deleteItem.getPostID()).removeValue();
                                        databaseReference.child("Posts").child("Recipes").child(deleteItem.getPostID()).removeValue();
                                        databaseReference.child("Comments").child(deleteItem.getPostID()).removeValue();
                                        StorageReference storageLocationCheck = FirebaseStorage.getInstance().getReferenceFromUrl(deleteItem.foodImage);
                                        storageLocationCheck.delete();

                                        //Updating List
                                        recipeCornersList.remove(i);
                                        rcadapter.notifyItemRemoved(i);
                                        rcadapter.notifyItemRangeChanged(0,listPos.size());
                                    }
                                    listPos.clear();
                                    Toast.makeText(getActivity(),rcadapter.cbCount+" Post(s) Deleted",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else{
                    Toast.makeText(getActivity(),"No Post Selected",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
//        Editing Users own Post
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(getContext());
                //Only one post selected
                if (rcadapter.cbCount == 1 && rcadapter.listPos.size() == 1){
                    //Getting Chosen Post to Edit
                    Integer pos = rcadapter.listPos.get(0);

                    Fragment RecipeForm = new RecipeForm(1); //Editing = 1

                    //pass username
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", pos);
                    bundle.putParcelable("list", Parcels.wrap(recipeCornersList));
                    RecipeForm.setArguments(bundle);

                    //Creating activity context to for the view, starting new fragment when view is clicked
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, RecipeForm).addToBackStack(null).commit();
                }
                // No post selected
                else if(rcadapter.cbCount.equals(0)){
                    Toast.makeText(getActivity(),"Please select a post to edit",
                            Toast.LENGTH_SHORT).show();
                }
                // More than 1 post selected
                else{
                    //Setting message manually and performing action on button click
                    builder.setTitle("Deselect ALL posts?")
                            .setMessage("You can only edit 1 post at a time.")
                            .setCancelable(false)
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
//                                Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
//                                        Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    //Deselect all Posts
                                    for (RecipeCorner i: recipeCornersList) {
                                        i.setChecked(false);

                                    }
                                    rcadapter.cbCount = 0;
                                    rcadapter.listPos.clear();
                                    rcadapter.notifyDataSetChanged();

                                    Toast.makeText(getActivity(),"All posts unselected.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        recipeCornersList.removeAll(recipeCornersList);
        if (status == true){
            for (RecipeCorner obj : postsHolder.getUserRecipePosts()) {
                recipeCornersList.add(obj);
            }
            rcadapter = new RecipeAdapter(recipeCornersList, c, 2);
        }
        else{
            for (RecipeCorner obj : postsHolder.getRecipePosts()){
                if (obj.owner.equals(usernameID)){
                    recipeCornersList.add(obj);
                }
            }
            rcadapter = new RecipeAdapter(recipeCornersList, c, 0);
        }
        recipeRV = view.findViewById(R.id.idRVRecipe);
        LinearLayoutManager manager = new LinearLayoutManager(c);
        recipeRV.setHasFixedSize(true);
        recipeRV.setLayoutManager(manager);
        recipeRV.setAdapter(rcadapter);

        return view;
    }

}
