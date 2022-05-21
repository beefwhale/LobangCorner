package sg.edu.np.madgroupyassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ImageView;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ArrayList<ToppostData> data = new ArrayList<>();
        for (int i=0; i<100; i++){

            ToppostData d = new ToppostData();
            d.tp_header = "Insert Title";
            //d.tp_img = "Description" + randdesc;
            data.add(d);
        }
        RecyclerView rv  = findViewById(R.id.toppost_rv);
        LinearLayoutManager layout = new LinearLayoutManager(Home.this, LinearLayoutManager.HORIZONTAL, false);
        ToppostAdapter adapter = new ToppostAdapter(Home.this, data);

        // Giving RV adapter and layout finally
        rv.setAdapter(adapter);
        rv.setLayoutManager(layout);
    }
}
