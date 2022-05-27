package sg.edu.np.madgroupyassignment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class HomeViewHolder extends RecyclerView.ViewHolder {
    //elements
    ImageView wklyfeat_img;
    TextView lp_header;
    RecyclerView tp_rv;

    TextView tp_header;
    ImageView tp_img;

    View viewItem;
    public HomeViewHolder(View item){
        super(item);
        viewItem = item;

        lp_header = item.findViewById(R.id.lp_header);
        wklyfeat_img= item.findViewById(R.id.wklyfeat_img);
        tp_rv = item.findViewById(R.id.tp_rv);

        tp_header= item.findViewById(R.id.toppost_header);
        tp_img = item.findViewById(R.id.toppost_img);


    }
}
