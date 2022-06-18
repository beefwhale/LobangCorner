package sg.edu.np.madgroupyassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class HCChosenStall extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_hcchosen_stall, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //Variables or views needed to define whats in Chosen stall
        ImageView chosenstallimg = view.findViewById(R.id.chosenstallimg);
        ImageView hccuserpfp = view.findViewById(R.id.hccuserpfp);
        TextView chosenstallname = view.findViewById(R.id.chosenstallname);
        TextView hccusername = view.findViewById(R.id.hccusername);
        TextView hccaddress = view.findViewById(R.id.hccaddress);
        TextView hccparagraph = view.findViewById(R.id.hccparagraph);
        TextView descriptionheader = view.findViewById(R.id.descriptiontv);
        TextView hccopendays = view.findViewById(R.id.hccopendays);
        TextView hccopenhours = view.findViewById(R.id.hccopenhours);

        //Bundle to get info from Hawker Corner Main
        Bundle bundle = this.getArguments();

        assert bundle != null;
        int chosenstallno = (int) bundle.getInt("stallposition");
        int HomeDataCheck = (int) bundle.getInt("HomeDataCheck"); // check if coming from home. 1 if yes

        if (HomeDataCheck == 1){
            HomeMixData chosenstall;
            ArrayList<HomeMixData> stallsList = Parcels.unwrap(bundle.getParcelable("list"));
            chosenstall = stallsList.get(chosenstallno);

            Picasso.get().load(chosenstall.getHccoverimg()).into(chosenstallimg);
            Picasso.get().load(chosenstall.getHccuserpfp()).into(hccuserpfp);
            chosenstallname.setText(chosenstall.hcstallname);
            hccusername.setText(chosenstall.hcauthor);
            hccaddress.setText(chosenstall.hccaddress);
            hccparagraph.setText(chosenstall.hccparagraph);
            descriptionheader.setText("About " + chosenstall.hcstallname);
            hccopendays.setText(chosenstall.daysopen);
            hccopenhours.setText(chosenstall.hoursopen);
        }
        else{
            HawkerCornerStalls chosenstall;
            ArrayList<HawkerCornerStalls> stallsList = Parcels.unwrap(bundle.getParcelable("list"));
            chosenstall = stallsList.get(chosenstallno);

            Picasso.get().load(chosenstall.getHccoverimg()).into(chosenstallimg);
            Picasso.get().load(chosenstall.getHccuserpfp()).into(hccuserpfp);
            chosenstallname.setText(chosenstall.hcstallname);
            hccusername.setText(chosenstall.hcauthor);
            hccaddress.setText(chosenstall.hccaddress);
            hccparagraph.setText(chosenstall.hccparagraph);
            descriptionheader.setText("About " + chosenstall.hcstallname);
            hccopendays.setText(chosenstall.daysopen);
            hccopenhours.setText(chosenstall.hoursopen);
        }

    }
}