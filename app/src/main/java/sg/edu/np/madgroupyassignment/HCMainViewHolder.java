package sg.edu.np.madgroupyassignment;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class HCMainViewHolder extends RecyclerView.ViewHolder {
    //Hawker Corner View Holder class.

    ImageView hccoverimg;
    TextView hcstallname;
    TextView hcauthor;
    TextView hcshortdesc;

    public HCMainViewHolder(View item){
        super(item);

        hccoverimg = item.findViewById(R.id.hccoverimg);
        hcstallname = (TextView) item.findViewById(R.id.hcstallname);
        hcauthor = (TextView) item.findViewById(R.id.hccoverauthor);
        hcshortdesc = (TextView) item.findViewById(R.id.hcshortdesc);

    }

}
