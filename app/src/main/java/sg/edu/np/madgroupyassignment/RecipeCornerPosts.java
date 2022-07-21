package sg.edu.np.madgroupyassignment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import java.util.ArrayList;

public class RecipeCornerPosts extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecipeCommentAdapter commentAdapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //variable for context
    Context c;

    public RecipeCornerPosts() {
        this.c = c;
    }

    ;

    public ArrayList<RecipeCorner> rcbookmarklist = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.recipe_comment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        //use of bundle to get data from each item in recyclerview
        Bundle b = this.getArguments();
        int rcNo = (int) b.getInt("recipeNo");
        int HomeDataCheck = (int) b.getInt("HomeDataCheck");

        ArrayList<Comments> CommentList = new ArrayList<>();

        if (HomeDataCheck == 1) {// If its Clicked from Home and Not Main Recipe Page
            ArrayList<HomeMixData> recipeList;
            recipeList = Parcels.unwrap(b.getParcelable("list"));
            HomeMixData recipePost;
            recipePost = recipeList.get(rcNo);

            databaseReference = firebaseDatabase.getReference().child("Comments").child(recipePost.getPostID());
            databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        CommentList.removeAll(CommentList);
                        for (DataSnapshot objectEntry : task.getResult().getChildren()) {
                            Comments comment = objectEntry.getValue(Comments.class);
                            CommentList.add(comment);
                        }

                        //Check for existing comments
                        Boolean contentCheck = CommentList.isEmpty();
                        if (contentCheck) {
                            Comments comment = new Comments();
                            comment.setCommentUsername("Be the first to write a Comment!");
                            CommentList.add(comment);
                        }

                        recyclerView = view.findViewById(R.id.RecipeCommentRecycler);
                        linearLayoutManager = new LinearLayoutManager(getContext());
                        commentAdapter = new RecipeCommentAdapter(getContext(), CommentList, recipePost, contentCheck);

                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(commentAdapter);

                        touchEvent(recyclerView);

                    } else {
                        Log.e("Comment retrieval error", "" + task.getResult().getValue());
                    }
                }
            });

        } else { // Clicked from Main Recipe Page
            ArrayList<RecipeCorner> recipeList;
            recipeList = Parcels.unwrap(b.getParcelable("list"));
            RecipeCorner recipePost;
            recipePost = recipeList.get(rcNo);

            HomeMixData commentSend = new HomeMixData();
            commentSend.postID = recipePost.postID;
            commentSend.recipeName = recipePost.recipeName;
            commentSend.recipeDescription = recipePost.recipeDescription;
            commentSend.recipeRating = recipePost.recipeRating;
            commentSend.userName = recipePost.userName;
            commentSend.duration = recipePost.duration;
            commentSend.steps = recipePost.steps;
            commentSend.ingredients = recipePost.ingredients;
            commentSend.foodImage = recipePost.foodImage;
            commentSend.owner = recipePost.owner;

            databaseReference = firebaseDatabase.getReference().child("Comments").child(recipePost.getPostID());
            databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        CommentList.removeAll(CommentList);
                        for (DataSnapshot objectEntry : task.getResult().getChildren()) {
                            Comments comment = objectEntry.getValue(Comments.class);
                            CommentList.add(comment);
                        }

                        //Check for existing comments
                        Boolean contentCheck = CommentList.isEmpty();
                        if (contentCheck) {
                            Comments comment = new Comments();
                            comment.setCommentUsername("Be the first to write a Comment!");
                            CommentList.add(comment);
                        }

                        recyclerView = view.findViewById(R.id.RecipeCommentRecycler);
                        linearLayoutManager = new LinearLayoutManager(getContext());
                        commentAdapter = new RecipeCommentAdapter(getContext(), CommentList, commentSend, contentCheck);

                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(commentAdapter);

                        touchEvent(recyclerView);

                    } else {
                        Log.e("Comment retrieval error", "" + task.getResult().getValue());
                    }
                }
            });

        }
    }

    private void touchEvent(RecyclerView recyclerView) {
        if (recyclerView != null) {
            recyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    View current = getActivity().getCurrentFocus();
                    if (current != null) {
                        current.clearFocus();
                    }
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService((Context.INPUT_METHOD_SERVICE));
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    return false;
                }
            });
        }
    }

    public ArrayList<RecipeCorner> getRcbookmarklist() {
        return rcbookmarklist;
    }
}