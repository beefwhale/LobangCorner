package sg.edu.np.madgroupyassignment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Top post RV ViewHolder for Home page
 */
public class HomeParentViewHolder extends RecyclerView.ViewHolder {
    // Feed and Latest post elements
    ImageView post_img;
    TextView post_header;
    TextView post_desc;
    TextView post_author;
    View post_line;
    CardView post_card;

    //Feed posts elements - Main RV
    RecyclerView parent_rv;

    View viewItem;

    public HomeParentViewHolder(View item) {
        super(item);
        viewItem = item;
        parent_rv = item.findViewById(R.id.home_main_rv);

        // LP and Feed elements use the same id
        post_img = item.findViewById(R.id.feed_img);
        post_header = item.findViewById(R.id.feed_header);
        post_desc = item.findViewById(R.id.feed_desc);
        post_author = item.findViewById(R.id.feed_author);
        post_line = item.findViewById(R.id.feed_line);
        post_card = item.findViewById(R.id.feed_card);

    }
}
