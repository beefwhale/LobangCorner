package sg.edu.np.madgroupyassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class HCChosenStall extends AppCompatActivity {
    //Class-xml for chosen stall from hawker corner. !!-Make appropriate changes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hcchosen_stall);

        TextView chosenstallname = findViewById(R.id.chosenstallname);
        ImageView chosenstallimg = findViewById(R.id.chosenstallimg);
        ImageView hccuserpfp = findViewById(R.id.hccuserpfp);
        TextView hccusername = findViewById(R.id.hccusername);
        Button hcclikebtn = findViewById(R.id.hcclikebtn);
        TextView hccparagraph = findViewById(R.id.hccparagraph);

        //Intent to retrieve info from hawker corner.
        Intent chosenstallno = getIntent();
        int positionofstall = chosenstallno.getIntExtra("stallposition", 0);
        HawkerCornerStalls chosenstall = HawkerCornerMain.stallsList.get(positionofstall);

        chosenstallname.setText(chosenstall.hcstallname);
        hccusername.setText(chosenstall.hcauthor);

    }
}