package sg.edu.np.madgroupyassignment;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class HCMainViewHolder extends RecyclerView.ViewHolder {
    //Hawker Corner View Holder class.

    ImageView hccoverimg;
    TextView hcstallname;
    TextView hcauthor;
    TextView hcshortdesc;
    CheckBox hcCheckbox;

    public HCMainViewHolder(View item) {
        super(item);

        hccoverimg = item.findViewById(R.id.hccoverimg);
        hcstallname = item.findViewById(R.id.hcstallname);
        hcauthor = item.findViewById(R.id.hccoverauthor);
        hcshortdesc = item.findViewById(R.id.hcshortdesc);
        hcCheckbox = item.findViewById(R.id.hccheckbox);

    }

}
