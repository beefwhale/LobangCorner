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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

    public ProfileRecipeRV(){
        this.c = c;
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

        rcadapter = new RecipeAdapter(recipeCornersList, c, 2);
        deleteBtn = view.findViewById(R.id.deleteBtn);
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

        recipeCornersList.removeAll(recipeCornersList);
        for (RecipeCorner obj : postsHolder.getUserRecipePosts()) {
            recipeCornersList.add(obj);
        }

        recipeRV = view.findViewById(R.id.idRVRecipe);
        rcadapter = new RecipeAdapter(recipeCornersList, c, 2);
        LinearLayoutManager manager = new LinearLayoutManager(c);
        recipeRV.setHasFixedSize(true);

        recipeRV.setLayoutManager(manager);
        recipeRV.setAdapter(rcadapter);

        return view;
    }

}
