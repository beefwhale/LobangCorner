package sg.edu.np.madgroupyassignment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class Home extends Fragment {
    Context c;
    public Home(){
        this.c = c; // extending scope of Home cus u cant called Home.this anymore
    }
    @Nullable
    @Override // Use onCreate View for Fragments
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);


        // Top Post RV
        ArrayList<ToppostData> data = new ArrayList<>();
        for (int i=0; i<100; i++){

            ToppostData d = new ToppostData();
            d.tp_header = "Insert Title";
            //d.tp_img = "Description" + randdesc;
            data.add(d);
        }
        RecyclerView rv  = view.findViewById(R.id.toppost_rv);
        LinearLayoutManager layout = new LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false);
        ToppostAdapter adapter = new ToppostAdapter(c, data);

        // Giving RV adapter and layout
        rv.setAdapter(adapter);
        rv.setLayoutManager(layout);
        return view;
    }
}
