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

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class HCChosenStall extends Fragment {
    //Class-xml for chosen stall from hawker corner. !!-Make appropriate changes
//    private ArrayList<HawkerCornerStalls> stallsList = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_hcchosen_stall, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        TextView chosenstallname = view.findViewById(R.id.chosenstallname);
        ImageView chosenstallimg = view.findViewById(R.id.chosenstallimg);
        ImageView hccuserpfp = view.findViewById(R.id.hccuserpfp);
        TextView hccusername = view.findViewById(R.id.hccusername);
        /*Button hcclikebtn = view.findViewById(R.id.hcclikebtn);*/
        TextView hccaddress = view.findViewById(R.id.hccaddress);
        TextView hccparagraph = view.findViewById(R.id.hccparagraph);
        TextView descriptionheader = view.findViewById(R.id.descriptiontv);
        TextView hccopendays = view.findViewById(R.id.hccopendays);
        TextView hccopenhours = view.findViewById(R.id.hccopenhours);

        //Bundle to get info from Hawker Corner Main
        Bundle bundle = this.getArguments();

        int chosenstallno = (int) bundle.getInt("stallposition");
        int HomeDataCheck = (int) bundle.getInt("HomeDataCheck"); // check if coming from home. 1 if yes

        if (HomeDataCheck == 1){
            HomeMixData chosenstall;
            ArrayList<HomeMixData> stallsList = new ArrayList<>();
            stallsList = Parcels.unwrap(bundle.getParcelable("list"));

            chosenstall = stallsList.get(chosenstallno);
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
            ArrayList<HawkerCornerStalls> stallsList = new ArrayList<>();
            stallsList = Parcels.unwrap(bundle.getParcelable("list"));
            chosenstall = stallsList.get(chosenstallno);


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