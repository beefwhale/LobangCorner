package sg.edu.np.madgroupyassignment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Top post RV ViewHolder for Home page
 */
public class ToppostViewHolder extends RecyclerView.ViewHolder {
    // Top post and Latest post elements
    TextView tp_header;
    ImageView tp_img;

    //Latest posts elements
    TextView lp_header;
    RecyclerView parent_rv;

    View viewItem;
    public ToppostViewHolder(View item){
        super(item);
        viewItem = item;
        parent_rv = item.findViewById(R.id.home_main_rv);

        lp_header= item.findViewById(R.id.lp_header);

        // LP and TP elements use the same id
        tp_header= item.findViewById(R.id.toppost_header);
        tp_img = item.findViewById(R.id.toppost_img);

    }
}
