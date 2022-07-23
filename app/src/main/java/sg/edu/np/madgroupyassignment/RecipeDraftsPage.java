package sg.edu.np.madgroupyassignment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

public class RecipeDraftsPage extends Fragment {

    RecyclerView recipeDraftRV;
    OnBackPressedCallback callback;
    PostsHolder postsHolder;
    ArrayList<RecipeCorner> draftsList = new ArrayList<RecipeCorner>();
    TextView header;

    ImageButton deleteBtn;
    RecipeDraftsAdapter rcdadapter;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    ArrayList<Integer> listPos;

    AlertDialog.Builder builder;
    private FirebaseAuth mAuth;

    public RecipeDraftsPage() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Defining items
        View recipeDraftPage = inflater.inflate(R.layout.fragment_recipe_drafts_page, container, false);
        header = recipeDraftPage.findViewById(R.id.recipe_drafts_header);
        header.setText("Create a Recipe Post");
        deleteBtn = recipeDraftPage.findViewById(R.id.deleteBtnDrafts);
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        //        Setting drafts list to list in firebase
        draftsList.removeAll(draftsList);
        for (RecipeCorner obj : postsHolder.getRecipeDrafts()) {
            draftsList.add(obj);
        }
        rcdadapter = new RecipeDraftsPage.RecipeDraftsAdapter(draftsList);
        //        Setting RecyclerView
        recipeDraftRV = recipeDraftPage.findViewById(R.id.recipeDraftRV);
        recipeDraftRV.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recipeDraftRV.setAdapter(rcdadapter);

//        Delete User Drafts
        rcdadapter.adapterListPos.clear();
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(getContext());
                //At least one post selected
                if (rcdadapter.cbCount > 0) {
                    //Setting message manually and performing action on button click
                    builder.setTitle("Confirm Delete ?")
                            .setMessage("You sure you want to permanently delete (" + rcdadapter.cbCount + ") drafts?")
                            .setCancelable(false)
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    //List of selected positions in RV (Checked)
                                    listPos = rcdadapter.adapterListPos;
                                    //Sort list in descending order to avoid Array Out of Bounds
                                    Collections.sort(listPos, Collections.reverseOrder());
                                    for (int i : listPos) {
                                        RecipeCorner deleteItem = draftsList.get(i - 1);
                                        //removing from database
                                        databaseReference.child("Drafts").child("Recipes").child(mAuth.getUid()).child(deleteItem.getPostID()).removeValue();
//                                        StorageReference storageLocationCheck = FirebaseStorage.getInstance().getReferenceFromUrl(deleteItem.foodImage);
//                                        storageLocationCheck.delete();

                                        //Updating List
                                        draftsList.remove(i - 1);
                                        rcdadapter.notifyItemRemoved(i - 1);
                                        rcdadapter.notifyItemRangeChanged(0, listPos.size() - 1);
                                    }
                                    rcdadapter.adapterListPos.clear();
                                    Toast.makeText(getActivity(), rcdadapter.cbCount + " Post(s) Deleted",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    Toast.makeText(getActivity(), "No Post Selected",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        return recipeDraftPage;
    }

    class RecipeDraftsAddViewHolder extends RecyclerView.ViewHolder {
        public RecipeDraftsAddViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class RecipeDraftsViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName, recipeDesc, userName;
        ImageView foodImage;
        RatingBar ratingBar;
        CheckBox checkbox;

        public RecipeDraftsViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeName = itemView.findViewById(R.id.idRecipeName);
            recipeDesc = itemView.findViewById(R.id.idRecipeDescription);
            userName = itemView.findViewById(R.id.idUser);
            foodImage = itemView.findViewById(R.id.imageView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            checkbox = itemView.findViewById(R.id.rccheckbox);

        }
    }

    class RecipeDraftsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<RecipeCorner> recipeDraftsList;
        public Integer cbCount = 0;
        public ArrayList<Integer> adapterListPos = new ArrayList<>();
        Integer toRemove;

        public RecipeDraftsAdapter(ArrayList<RecipeCorner> recipeDraftsList) {
            this.recipeDraftsList = recipeDraftsList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == 0) {
                View addDraftView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_draft, parent, false);
                return new RecipeDraftsPage.RecipeDraftsAddViewHolder(addDraftView);
            } else {
                View draftView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_profile_rc_card, parent, false);
                return new RecipeDraftsPage.RecipeDraftsViewHolder(draftView);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (this.getItemViewType(position) == 0) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (MainActivity.checkFormsNum == 1) {
                            MainActivity.recipeForm.status = 0;
                            MainActivity.checkFormsNum = 0; //changes to 0 when click into forms
                            MainActivity.whichForm = 2;
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            activity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.MainFragment, MainActivity.recipeForm).addToBackStack(null).commit();
                            MainActivity.mainFAB.hide();
                            MainActivity.rcFAB.hide();
                            MainActivity.hcFAB.hide();
                            MainActivity.rcFABText.setVisibility(View.GONE);
                            MainActivity.hcFABText.setVisibility(View.GONE);
                        }
                    }
                });
            } else {
                RecipeDraftsPage.RecipeDraftsViewHolder recipeDraftsViewHolder = (RecipeDraftsPage.RecipeDraftsViewHolder) holder;
                RecipeCorner item = recipeDraftsList.get(position - 1);
                //If no image, do nothing. If there's image, load image.
                if (item.getFoodImage() == "" | item.getFoodImage().isEmpty() | item.getFoodImage() == null) {
                } else {
                    Picasso.get().load(item.getFoodImage()).into(recipeDraftsViewHolder.foodImage); //load image from database to imageview
                }
                recipeDraftsViewHolder.recipeName.setText(item.recipeName);         //set textview to recipename
                recipeDraftsViewHolder.recipeDesc.setText(item.getRecipeDescription()); //set textview to recipedescription
                recipeDraftsViewHolder.ratingBar.setRating(item.getRecipeRating()); //set ratingbar to recipe difficulty level
                recipeDraftsViewHolder.userName.setText("By: " + item.getUserName());   //set textview to username

                //Setting all as default unselected
                item.setChecked(false);
                //DESELECTION : if removed from listPos, updating checkbox for every card
                if (adapterListPos.contains(holder.getAdapterPosition()) == false) {
                    recipeDraftsViewHolder.checkbox.setChecked(false);
                } else {
                    recipeDraftsViewHolder.checkbox.setChecked(true);
                }
                recipeDraftsViewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getChecked() == true) {
                            item.setChecked(false);
                            cbCount = cbCount - 1;
                            for (Integer i : adapterListPos) { //finding integer within list
                                if (i.equals(holder.getAdapterPosition())) {
                                    toRemove = i; // getting integer position
                                }
                            }
                            //Remove integer from list using integer position
                            adapterListPos.remove(toRemove);
                        } else {
                            item.setChecked(true);
                            cbCount = cbCount + 1;
                            //Add to list of checked using adapter position
                            adapterListPos.add(holder.getAdapterPosition());
                        }
                    }
                });
                // When click into drafts
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(getActivity(), "rest", Toast.LENGTH_SHORT).show();
                        MainActivity.recipeForm.status = 2;
                        MainActivity.checkFormsNum = 0; //changes to 0 when click into forms
                        MainActivity.whichForm = 2;

                        //pass username
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("recipe", Parcels.wrap(item));
                        MainActivity.recipeForm.setArguments(bundle);

                        //Creating activity context to for the view, starting new fragment when view is clicked
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, MainActivity.recipeForm).addToBackStack(null).commit();
                    }
                });
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return (1 + recipeDraftsList.size());
        }
    }

    public void onResume() {
        //Alert shows up when back button is pressed.
        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getParentFragmentManager().popBackStack();
                MainActivity.mainFAB.show();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
        super.onResume();
    }

    @Override
    public void onPause() {
        callback.setEnabled(false);
        super.onPause();
    }
}