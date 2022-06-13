package sg.edu.np.madgroupyassignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.ArrayList;

public class HCMainsAdapter extends RecyclerView.Adapter<HCMainViewHolder> {
    //Adapter for Recycler View in Hawker Corner Main Page.

    ArrayList<HawkerCornerStalls> stallsList;
    //Constructor for Adapter.
    public HCMainsAdapter (ArrayList<HawkerCornerStalls> stallsList){
        this.stallsList = stallsList;
    }

    //Create View holder, parent as Stalls are centered.
    @NonNull
    @Override
    public HCMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View stall = LayoutInflater.from(parent.getContext()).inflate(R.layout.hawker_corner_stalls, parent, false);
        return new HCMainViewHolder(stall);
    }

    @Override
    public void onBindViewHolder(@NonNull HCMainViewHolder holder, int position) {
        HawkerCornerStalls newstall = stallsList.get(position);
        //holder.hccoverimg. how to set image?
        holder.hcstallname.setText(newstall.hcstallname);
        /*holder.shortdesc.setText(newstall.shortdesc);*/
        holder.hcauthor.setText("By: " + newstall.hcauthor);

        //When user click their chosen image, start fragment displaying chosen stall.
        holder.hccoverimg.setOnClickListener(new View.OnClickListener() {
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

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, chosenfragment).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return stallsList.size();
    }

    //Method to notify change from filter
    public void sortChange(ArrayList<HawkerCornerStalls> newList){
        this.stallsList = newList;
        notifyDataSetChanged();
    }
}
