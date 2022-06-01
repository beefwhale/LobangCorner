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

import org.w3c.dom.Text;

public class HCChosenStall extends Fragment {
    //Class-xml for chosen stall from hawker corner. !!-Make appropriate changes

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_hcchosen_stall, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        TextView chosenstallname = view.findViewById(R.id.chosenstallname);
        ImageView chosenstallimg = view.findViewById(R.id.chosenstallimg);
        ImageView hccuserpfp = view.findViewById(R.id.hccuserpfp);
        TextView hccusername = view.findViewById(R.id.hccusername);
        Button hcclikebtn = view.findViewById(R.id.hcclikebtn);
        TextView hccparagraph = view.findViewById(R.id.hccparagraph);

        //Bundle to get info from Hawker Corner Main
        Bundle bundle = this.getArguments();

        int chosenstallno = (int) bundle.getInt("stallposition");
        HawkerCornerStalls chosenstall;
        HawkerCornerMain hawkerCornerMain = new HawkerCornerMain();
        if (hawkerCornerMain.stallsList .size() != 0){
            // list for normal HC
             chosenstall = hawkerCornerMain.stallsList.get(chosenstallno);
        }
        else{
            // list for Profile linked HC
             chosenstall = ProfileHawkerRV.hawkerCornersList.get(chosenstallno);
        }


        chosenstallname.setText(chosenstall.hcstallname);
        hccusername.setText(chosenstall.hcauthor);

    }
}