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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BHCFragment extends Fragment {

    RecyclerView recyclerView;
    ImageView deletebtn;
    HCMainsAdapter adapter;
    PostsHolder2 postsHolder;
    public ArrayList<HawkerCornerStalls> hwklist = new ArrayList<>();
    public ArrayList<HawkerCornerStalls> dellist = new ArrayList<>();
    DatabaseReference reference;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bhc, container, false);
        recyclerView = view.findViewById(R.id.bhc_rv);
        deletebtn = view.findViewById(R.id.imageView3);

        reference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        hwklist.removeAll(hwklist);
        for (HawkerCornerStalls obj : postsHolder.getHawkerPosts()) {
            hwklist.add(obj);
        }

        // initializing our adapter class.
        adapter = new HCMainsAdapter(hwklist, false);

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        recyclerView.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        recyclerView.setAdapter(adapter);

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dellist = adapter.getDel_hcslist();
                if (hwklist.isEmpty()) {
                    Toast.makeText(getContext(), "No stalls found", Toast.LENGTH_SHORT).show();
                }
                else if (dellist.isEmpty()){
                    Toast.makeText(getContext(), "No stalls selected", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), Integer.toString(dellist.size()) + " stall(s) deleted", Toast.LENGTH_SHORT).show();
                    for (HawkerCornerStalls dhwkObject : dellist)
                    {
                        hwklist.remove(dhwkObject);
                        dellist.remove(dhwkObject);
                        reference.child("UserProfile").child(mAuth.getUid()).child("bmhawklist").child(dhwkObject.postid).removeValue();
                    }
                    adapter.delete(hwklist);
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        return view;
    }
}
