package sg.edu.np.madgroupyassignment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import org.parceler.Parcels;

import java.util.ArrayList;


public class RecipeCornerPosts extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecipeCommentAdapter commentAdapter;

    //variable for context
    Context c;
    //constructor
    public RecipeCornerPosts(){this.c =c;};

    public ArrayList<RecipeCorner> rcbookmarklist = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.recipe_comment, container, false);

        //use of bundle to get data from each item in recyclerview
        Bundle b = this.getArguments();
        int rcNo = (int) b.getInt("recipeNo");
        int HomeDataCheck = (int) b.getInt("HomeDataCheck");

        ArrayList<Comments> testlist = new ArrayList<>();

        //For testing purposes
        for (int i = 0; i < 10; i++) {
            Comments comments = new Comments("", "", "", "","TestUser" + i, "Test" + i , 0L);
            testlist.add(comments);
        }

        //Check for existing comments
        Boolean contentCheck = testlist.isEmpty();
        if (contentCheck) {
            Comments comment = new Comments("", "", "", "","Be the first to write a Comment!", "" , 0L);
            testlist.add(comment);
        }

        if (HomeDataCheck == 1){// If its Clicked from Home and Not Main Recipe Page
            ArrayList<HomeMixData> recipeList = new ArrayList<>();
            recipeList = Parcels.unwrap(b.getParcelable("list"));
            HomeMixData recipePost;
            recipePost = recipeList.get(rcNo);

            recyclerView = view.findViewById(R.id.RecipeCommentRecycler);
            linearLayoutManager = new LinearLayoutManager(getContext());
            commentAdapter = new RecipeCommentAdapter(getContext(), testlist, recipePost, contentCheck);

            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(commentAdapter);

        }
        else{ // Clicked from Main Recipe Page
            ArrayList<RecipeCorner> recipeList = new ArrayList<>();
            recipeList = Parcels.unwrap(b.getParcelable("list"));
            RecipeCorner recipePost;
            recipePost = recipeList.get(rcNo);

            HomeMixData commentSend = new HomeMixData();
            commentSend.recipeName = recipePost.recipeName;
            commentSend.recipeDescription = recipePost.recipeDescription;
            commentSend.recipeRating = recipePost.recipeRating;
            commentSend.userName = recipePost.userName;
            commentSend.duration = recipePost.duration;
            commentSend.steps = recipePost.steps;
            commentSend.ingredients = recipePost.ingredients;
            commentSend.foodImage = recipePost.foodImage;

            recyclerView = view.findViewById(R.id.RecipeCommentRecycler);
            linearLayoutManager = new LinearLayoutManager(getContext());
            commentAdapter = new RecipeCommentAdapter(getContext(), testlist, commentSend, contentCheck);

            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(commentAdapter);

        }

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                View current = getActivity().getCurrentFocus();
                if (current != null) { current.clearFocus();}
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService((Context.INPUT_METHOD_SERVICE));
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                return false;
            }
        });


//        Commented out because its not working? if you wanna work on it please either inform me once its done and i'll put
//        it where it should be or you can go to RecipeCommentAdapter and work on it there. --Celsius

//        i2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "Recipe saved!", Toast.LENGTH_SHORT).show();
//                ArrayList<RecipeCorner> recipeList = new ArrayList<>();
//                recipeList = Parcels.unwrap(b.getParcelable("list"));
//                RecipeCorner recipePost;
//                recipePost = recipeList.get(rcNo);
//                rcbookmarklist.add(recipePost);
//                //hvent add to firebase
//            }
//        });

        return view;
    }

    public ArrayList<RecipeCorner> getRcbookmarklist() {
        return rcbookmarklist;
    }
}