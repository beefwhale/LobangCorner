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

public class HawkerDraftsPage extends Fragment {

    RecyclerView hawkerDraftRV;
    OnBackPressedCallback callback;
    PostsHolder postsHolder;
    ArrayList<HawkerCornerStalls> draftsList = new ArrayList<HawkerCornerStalls>();

    ImageButton deleteBtn;
    HawkerDraftsAdapter hcdadapter;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    ArrayList<Integer> listPos;
    AlertDialog.Builder builder;


    public HawkerDraftsPage() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Defining Items
        View hawkerDraftPage = inflater.inflate(R.layout.fragment_hawker_drafts_page, container, false);
        deleteBtn = hawkerDraftPage.findViewById(R.id.deleteBtnDrafts);
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

//        Setting drafts list to list in firebase
        draftsList.removeAll(draftsList);
        for (HawkerCornerStalls obj : postsHolder.getHawkerDrafts()) {
            draftsList.add(obj);
        }
        hcdadapter = new HawkerDraftsAdapter(draftsList);
//        Setting RecyclerView
        hawkerDraftRV = hawkerDraftPage.findViewById(R.id.hawkerDraftRV);
        hawkerDraftRV.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        hawkerDraftRV.setAdapter(hcdadapter);

//        Delete User Drafts
        hcdadapter.adapterListPos.clear();
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(getContext());
                //At least one post selected
                if (hcdadapter.cbCount > 0) {
                    //Setting message manually and performing action on button click
                    builder.setTitle("Confirm Delete ?")
                            .setMessage("You sure you want to permanently delete (" + hcdadapter.cbCount + ") posts?")
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
                                    listPos = hcdadapter.adapterListPos;
                                    //Sort list in descending order to avoid Array Out of Bounds
                                    Collections.sort(listPos, Collections.reverseOrder());
                                    for (int i : listPos) {
                                        HawkerCornerStalls deleteItem = draftsList.get(i - 1);
                                        //removing from database
                                        databaseReference.child("Drafts").child("Hawkers").child(mAuth.getUid()).child(deleteItem.getPostid()).removeValue();
//                                        StorageReference storageLocationCheck = FirebaseStorage.getInstance().getReferenceFromUrl(deleteItem.hccoverimg);
//                                        storageLocationCheck.delete();

                                        //Updating List
                                        draftsList.remove(i - 1);
                                        hcdadapter.notifyItemRemoved(i - 1);
                                        hcdadapter.notifyItemRangeChanged(0, listPos.size() - 1);
                                    }
                                    hcdadapter.adapterListPos.clear();
                                    Toast.makeText(getActivity(), hcdadapter.cbCount + " Post(s) Deleted",
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
        return hawkerDraftPage;
    }

    class HawkerDraftsAddViewHolder extends RecyclerView.ViewHolder {
        public HawkerDraftsAddViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class HawkerDraftsViewHolder extends RecyclerView.ViewHolder {
        ImageView hccoverimg;
        TextView hcstallname;
        TextView hcauthor;
        TextView hcshortdesc;
        CheckBox hcCheckbox;

        public HawkerDraftsViewHolder(@NonNull View item) {
            super(item);
            hccoverimg = item.findViewById(R.id.hccoverimg);
            hcstallname = item.findViewById(R.id.hcstallname);
            hcauthor = item.findViewById(R.id.hccoverauthor);
            hcshortdesc = item.findViewById(R.id.hcshortdesc);
            hcCheckbox = item.findViewById(R.id.hccheckbox);
        }
    }

    class HawkerDraftsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<HawkerCornerStalls> stallsList;
        public ArrayList<Integer> adapterListPos = new ArrayList<>();
        public Integer cbCount = 0;
        Integer toRemove;
        public Fragment HawkerForm;

        public HawkerDraftsAdapter(ArrayList<HawkerCornerStalls> stallsList) {
            this.stallsList = stallsList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == 0) {
                View addDraftView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_draft, parent, false);
                return new HawkerDraftsPage.HawkerDraftsAddViewHolder(addDraftView);
            } else {
                View draftView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_profile_hc_card, parent, false);
                return new HawkerDraftsPage.HawkerDraftsViewHolder(draftView);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            HawkerForm hawkerForm = new HawkerForm(0); //Posting = 0
            //When click into first item (plus) go into forms
            if (this.getItemViewType(position) == 0) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (MainActivity.checkFormsNum == 1) {
                            MainActivity.checkFormsNum = 0; //changes to 0 when click into forms
                            MainActivity.whichForm = 1;
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            activity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.MainFragment, hawkerForm).addToBackStack(null).commit();
                            MainActivity.mainFAB.hide();
                            MainActivity.rcFAB.hide();
                            MainActivity.hcFAB.hide();
                            MainActivity.rcFABText.setVisibility(View.GONE);
                            MainActivity.hcFABText.setVisibility(View.GONE);
                        }
                    }
                });
            }
            //When click into something else (drafts) go into forms with data
            else {
                HawkerDraftsViewHolder hawkerDraftsViewHolder = (HawkerDraftsViewHolder) holder;
                HawkerCornerStalls newstall = stallsList.get(position - 1);
                //If no image, do nothing. If there's image, load image.
                if (newstall.getHccoverimg() == "" | newstall.getHccoverimg().isEmpty() | newstall.getHccoverimg() == null) {
                } else {
                    Picasso.get().load(newstall.getHccoverimg()).into(hawkerDraftsViewHolder.hccoverimg);
                }
                hawkerDraftsViewHolder.hcstallname.setText(newstall.hcstallname);
                hawkerDraftsViewHolder.hcshortdesc.setText(newstall.shortdesc);
                hawkerDraftsViewHolder.hcauthor.setText("By: " + newstall.hcauthor);
                //Setting all as default unselected
                newstall.setChecked(false);
                //DESELECTION : if removed from listPos, updating checkbox for every card
                if (adapterListPos.contains(holder.getAdapterPosition()) == false) {
                    hawkerDraftsViewHolder.hcCheckbox.setChecked(false);
                } else {
                    hawkerDraftsViewHolder.hcCheckbox.setChecked(true);
                }
                hawkerDraftsViewHolder.hcCheckbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (newstall.getChecked() == true) {
                            newstall.setChecked(false);
                            cbCount = cbCount - 1;
                            for (Integer i : adapterListPos) { //finding integer within list
                                if (i.equals(holder.getAdapterPosition())) {
                                    toRemove = i; // getting integer position
                                }
                            }
                            //Remove integer from list using integer position
                            adapterListPos.remove(toRemove);
                        } else {
                            newstall.setChecked(true);
                            cbCount = cbCount + 1;
                            //Add to list of checked using adapter position
                            adapterListPos.add(holder.getAdapterPosition());
                        }
                        if (cbCount == 0) {
                            listPos.clear();
                        }
                    }
                });
                //When click into drafts
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(getActivity(), "rest", Toast.LENGTH_SHORT).show();
                        HawkerForm = new HawkerForm(2); //Editing draft = 2
                        MainActivity.checkFormsNum = 0; //changes to 0 when click into forms
                        MainActivity.whichForm = 1;

                        //pass username
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("stall", Parcels.wrap(newstall));
                        HawkerForm.setArguments(bundle);

                        //Creating activity context to for the view, starting new fragment when view is clicked
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, HawkerForm).addToBackStack(null).commit();
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
            return (1 + stallsList.size());
        }

        public void sortChange(ArrayList<HawkerCornerStalls> newList) {
            this.stallsList = newList;
            notifyDataSetChanged();
        }
    }

    @Override
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