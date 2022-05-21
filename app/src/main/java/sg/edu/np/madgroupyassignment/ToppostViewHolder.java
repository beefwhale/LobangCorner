package sg.edu.np.madgroupyassignment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Top post RV ViewHolder for Home page
 */
public class ToppostViewHolder extends RecyclerView.ViewHolder {
    TextView tp_header;
    ImageView tp_img;
    View viewItem;
    public ToppostViewHolder(View item){
        super(item);
        viewItem = item;
        tp_header= item.findViewById(R.id.toppost_header);
        tp_img = item.findViewById(R.id.toppost_img);

    }
}
