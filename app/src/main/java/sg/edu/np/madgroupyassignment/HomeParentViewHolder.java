package sg.edu.np.madgroupyassignment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Top post RV ViewHolder for Home page
 */
public class HomeParentViewHolder extends RecyclerView.ViewHolder {
    // Top post and Latest post elements
    TextView tp_header;
    ImageView tp_img;

    //Latest posts elements
    TextView feed_header;
    RecyclerView parent_rv;

    View viewItem;
    public HomeParentViewHolder(View item){
        super(item);
        viewItem = item;
        parent_rv = item.findViewById(R.id.home_main_rv);

        feed_header= item.findViewById(R.id.feed_header);

        // LP and TP elements use the same id
        tp_header= item.findViewById(R.id.feed_title);
        tp_img = item.findViewById(R.id.feed_img);

    }
}
