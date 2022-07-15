package sg.edu.np.madgroupyassignment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;

public class HCChosenStall extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    HawkerCommentAdapter commentAdapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        return inflater.inflate(R.layout.chosenstall_comments, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        //Bundle to get info from Hawker Corner Main
        Bundle bundle = this.getArguments();

        //Make sure bundle
        assert bundle != null;
        int chosenstallno = (int) bundle.getInt("stallposition");
        int HomeDataCheck = (int) bundle.getInt("HomeDataCheck"); // check if coming from home. 1 if yes

        ArrayList<Comments> CommentList = new ArrayList<>();

        if (HomeDataCheck == 1){
            HomeMixData chosenstall;
            ArrayList<HomeMixData> stallsList = Parcels.unwrap(bundle.getParcelable("list"));
            chosenstall = stallsList.get(chosenstallno);

            databaseReference = firebaseDatabase.getReference().child("Comments").child(chosenstall.getPostID());
            databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()){
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

                        recyclerView = view.findViewById(R.id.ChosenStallCommentRecycler);
                        linearLayoutManager = new LinearLayoutManager(getContext());
                        commentAdapter = new HawkerCommentAdapter(getContext(), CommentList, chosenstall, contentCheck);

                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(commentAdapter);

                        touchEvent(recyclerView);

                    } else {
                        Log.e("Comment retrieval error", "" + task.getResult().getValue());
                    }
                }
            });

        } else {
            HawkerCornerStalls chosenstall;
            ArrayList<HawkerCornerStalls> stallsList = Parcels.unwrap(bundle.getParcelable("list"));
            chosenstall = stallsList.get(chosenstallno);

            HomeMixData commentSend = new HomeMixData();
            commentSend.postID = chosenstall.postid;
            commentSend.hccoverimg = chosenstall.hccoverimg;
            commentSend.hccuserpfp = chosenstall.hccuserpfp;
            commentSend.hcstallname = chosenstall.hcstallname;
            commentSend.hcauthor = chosenstall.hcauthor;
            commentSend.hccaddress = chosenstall.hccaddress;
            commentSend.hccparagraph= chosenstall.hccparagraph;
            commentSend.hcstallname = chosenstall.hcstallname;
            commentSend.daysopen = chosenstall.daysopen;
            commentSend.hoursopen = chosenstall.hoursopen;
            commentSend.hcOwner = chosenstall.hcOwner;

            databaseReference = firebaseDatabase.getReference().child("Comments").child(commentSend.getPostID());
            databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()){
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

                        recyclerView = view.findViewById(R.id.ChosenStallCommentRecycler);
                        linearLayoutManager = new LinearLayoutManager(getContext());
                        commentAdapter = new HawkerCommentAdapter(getContext(), CommentList, commentSend, contentCheck);

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
                    if (current != null) { current.clearFocus();}
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService((Context.INPUT_METHOD_SERVICE));
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    return false;
                }
            });
        }
    }

}