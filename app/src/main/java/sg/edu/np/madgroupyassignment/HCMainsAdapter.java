package sg.edu.np.madgroupyassignment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.hcauthor.setText("By: " + newstall.hcauthor);

        //When user click their chosen image, start new Activity displaying chosen stall.
        holder.hccoverimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tohcchosen = new Intent(view.getContext(), HCChosenStall.class);
                tohcchosen.putExtra("stallposition", holder.getAdapterPosition());
                view.getContext().startActivity(tohcchosen);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stallsList.size();
    }

}
