package sg.edu.np.madgroupyassignment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BRCFragment extends Fragment {

    RecyclerView recyclerView;
    ImageView delete;
    public RecipeAdapter adapter;
    PostsHolder2 postsHolder2;
    public ArrayList<RecipeCorner> rcplist = new ArrayList<>();
    public ArrayList<RecipeCorner> dellist = new ArrayList<>();
    DatabaseReference reference;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_brc, container, false);
        recyclerView = view.findViewById(R.id.brc_rv);
        delete = view.findViewById(R.id.imageView3);

        reference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        rcplist.removeAll(rcplist);
        for (RecipeCorner obj : postsHolder2.getRecipePosts()) {
            rcplist.add(obj);
        }

        // initializing our adapter class.
        adapter = new RecipeAdapter(rcplist, getActivity(), false);

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        recyclerView.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        recyclerView.setAdapter(adapter);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dellist = adapter.getDel_rcplist();
                if (rcplist.isEmpty()) {
                    Toast.makeText(getContext(), "No recipes found", Toast.LENGTH_SHORT).show();
                }
                else if (dellist.isEmpty()){
                    Toast.makeText(getContext(), "No recipes selected", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), Integer.toString(dellist.size()) + " recipe(s) deleted", Toast.LENGTH_SHORT).show();
                    for (RecipeCorner drcpObject : dellist)
                    {
                        rcplist.remove(drcpObject);
                        dellist.remove(drcpObject);
                        reference.child("UserProfile").child(mAuth.getUid()).child("bmrcplist").child(drcpObject.postID).removeValue();
                    }
                    adapter.delete(rcplist);
                    recyclerView.setAdapter(adapter);
                }

            }
        });

        return view;
    }
}