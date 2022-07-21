package sg.edu.np.madgroupyassignment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class HomeChildViewHolder extends RecyclerView.ViewHolder {
    ImageView post_img;
    TextView post_header;
    TextView post_desc;
    TextView post_author;
    View post_line;

    ImageView weekly_img;
    TextView weekly_title;
    TextView weekly_author;

    ImageView hc_btn;
    ImageView rc_btn;

    public HomeChildViewHolder(View item) {
        super(item);

        // Getting values of actual items in LP layout file
        post_img = item.findViewById(R.id.feed_img);
        post_header = item.findViewById(R.id.feed_header);
        post_desc = item.findViewById(R.id.feed_desc);
        post_author = item.findViewById(R.id.feed_author);
        post_line = item.findViewById(R.id.feed_line);

        // For Weekly Feature items
        weekly_img = item.findViewById(R.id.weekly_img);
        weekly_title = item.findViewById(R.id.weekly_title);
        weekly_author = item.findViewById(R.id.weekly_author);

        // Buttons for HC and RC
        hc_btn = item.findViewById(R.id.home_hc_btn);
        rc_btn = item.findViewById(R.id.home_rc_btn);
    }
}
