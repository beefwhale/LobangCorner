package sg.edu.np.madgroupyassignment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class BHCFragment extends Fragment {

    RecyclerView recyclerView;
    ImageView delete;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bhc, container, false);

        recyclerView = view.findViewById(R.id.bhc_rv);
        delete = view.findViewById(R.id.imageView3);


        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        recyclerView.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        //recyclerView.setAdapter(adapter);


        return view;
    }
}
