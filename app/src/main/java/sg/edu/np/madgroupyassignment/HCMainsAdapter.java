package sg.edu.np.madgroupyassignment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

public class HCMainsAdapter extends RecyclerView.Adapter<HCMainViewHolder> {
    //Adapter for Recycler View in Hawker Corner Main Page.

    ArrayList<HawkerCornerStalls> stallsList;
    Boolean status;
    public Integer cbCount = 0;
    public ArrayList<Integer> listPos = new ArrayList<>();
    Integer toRemove;
    View stall;
    public ArrayList<HawkerCornerStalls> del_hcslist = new ArrayList<>();

    //Constructor for Adapter.
    public HCMainsAdapter(ArrayList<HawkerCornerStalls> stallsList, Boolean status) {
        this.stallsList = stallsList;
        this.status = status;
    }

    //Create View holder, parent as Stalls are centered.
    @NonNull
    @Override
    public HCMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Checks which layout u want (checkbox = false, no checkbox = true)
        if (status == true) {
            stall = LayoutInflater.from(parent.getContext()).inflate(R.layout.hawker_corner_stalls, parent, false);
        } else {
            stall = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_profile_hc_card, parent, false);
        }

        return new HCMainViewHolder(stall);
    }

    @Override
    public void onBindViewHolder(@NonNull HCMainViewHolder holder, int position) {
        HawkerCornerStalls newstall = stallsList.get(position);
        Picasso.get().load(newstall.getHccoverimg()).into(holder.hccoverimg);
        holder.hcstallname.setText(newstall.hcstallname);
        holder.hcshortdesc.setText(newstall.shortdesc);
        holder.hcauthor.setText("By: " + newstall.hcauthor);

        //When user click the stall, start fragment displaying chosen stall.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating activity context to for the view, starting new fragment when view is clicked
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment chosenfragment = new HCChosenStall();

                //Bundle to pass info to fragment as intent cannot
                Bundle bundle = new Bundle();
                bundle.putInt("stallposition", holder.getAdapterPosition());
                bundle.putParcelable("list", Parcels.wrap(stallsList));
                chosenfragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, chosenfragment, "HCpost").addToBackStack(null).commit();
            }
        });
        // If layout has checkbox
        if (status == false) {
            //Setting all as default unselected
            newstall.setChecked(false);
            //DESELECTION : if removed from listPos, updating checkbox for every card
            if (listPos.contains(holder.getAdapterPosition()) == false) {
                holder.hcCheckbox.setChecked(false);
            } else {
                holder.hcCheckbox.setChecked(true);
            }
            holder.hcCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (newstall.getChecked() == true) {
                        newstall.setChecked(false);
                        cbCount = cbCount - 1;
                        for (Integer i : listPos) { //finding integer within list
                            if (i.equals(holder.getAdapterPosition())) {
                                toRemove = i; // getting integer position
                            }
                        }
                        //Remove integer from list using integer position
                        listPos.remove(toRemove);
                    } else {
                        newstall.setChecked(true);
                        cbCount = cbCount + 1;
                        //Add to list of checked using adapter position
                        listPos.add(holder.getAdapterPosition());
                    }
                    if (cbCount == 0) {
                        listPos.clear();
                    }
                }
            });

            holder.hcCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (del_hcslist.contains(newstall)) {
                        del_hcslist.remove(newstall);
                    } else {
                        del_hcslist.add(newstall);
                    }
                }
            });

        }
    }

    public ArrayList<HawkerCornerStalls> getDel_hcslist() {
        return del_hcslist;
    }

    public void delete(ArrayList<HawkerCornerStalls> deletelist) {
        stallsList = deletelist;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return stallsList.size();
    }

    //Method to notify change from filter
    public void sortChange(ArrayList<HawkerCornerStalls> newList) {
        this.stallsList = newList;
        notifyDataSetChanged();
    }
}
