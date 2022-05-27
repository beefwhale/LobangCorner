package sg.edu.np.madgroupyassignment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class HomeChildViewHolder extends RecyclerView.ViewHolder {
    TextView tp_header;
    ImageView tp_img;

    public HomeChildViewHolder(View item){
        super(item);

        // Getting values of actual items in TOPPOST layout file
        tp_header= item.findViewById(R.id.toppost_header);
        tp_img = item.findViewById(R.id.toppost_img);
    }
}
