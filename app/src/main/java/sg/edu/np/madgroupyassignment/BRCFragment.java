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

public class BRCFragment extends Fragment {

    RecyclerView recyclerView;
    ImageView delete2;
    private RecipeCornerPosts rcpost;
    public RecipeAdapter adapter;
    PostsHolder postsHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_brc, container, false);
        recyclerView = view.findViewById(R.id.brc_rv);
        delete2 = view.findViewById(R.id.imageView3);

//       for (RecipeCorner obj : rcpost.getRcbookmarklist()) {
//           recipeModalArrayList.add(obj);
//       }

        rcpost = new RecipeCornerPosts();
        //brclist = rcpost.getRcbookmarklist();

        // initializing our adapter class.
        //adapter = new RecipeAdapter(brclist, getActivity());

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        recyclerView.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        recyclerView.setAdapter(adapter);

        delete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rcpost.getRcbookmarklist().isEmpty())
                    Toast.makeText(getContext(), "list empty", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}