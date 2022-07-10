package sg.edu.np.madgroupyassignment;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import org.parceler.Parcels;

import java.util.ArrayList;

public class HCChosenStall extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private HawkerCommentAdapter commentAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        return inflater.inflate(R.layout.chosenstall_comments, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Bundle to get info from Hawker Corner Main
        Bundle bundle = this.getArguments();
        //Make sure bundle
        assert bundle != null;
        int chosenstallno = (int) bundle.getInt("stallposition");
        int HomeDataCheck = (int) bundle.getInt("HomeDataCheck"); // check if coming from home. 1 if yes

        ArrayList<Comments> testlist = new ArrayList<>();

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

        if (HomeDataCheck == 1){
            HomeMixData chosenstall;
            ArrayList<HomeMixData> stallsList = Parcels.unwrap(bundle.getParcelable("list"));
            chosenstall = stallsList.get(chosenstallno);

            recyclerView = view.findViewById(R.id.ChosenStallCommentRecycler);
            linearLayoutManager = new LinearLayoutManager(getContext());
            commentAdapter = new HawkerCommentAdapter(getContext(), testlist, chosenstall, contentCheck);

            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(commentAdapter);
        }
        else{
            HawkerCornerStalls chosenstall;
            ArrayList<HawkerCornerStalls> stallsList = Parcels.unwrap(bundle.getParcelable("list"));
            chosenstall = stallsList.get(chosenstallno);

            HomeMixData commentSend = new HomeMixData();
            commentSend.hccoverimg = chosenstall.hccoverimg;
            commentSend.hccuserpfp = chosenstall.hccuserpfp;
            commentSend.hcstallname = chosenstall.hcstallname;
            commentSend.hcauthor = chosenstall.hcauthor;
            commentSend.hccaddress = chosenstall.hccaddress;
            commentSend.hccparagraph= chosenstall.hccparagraph;
            commentSend.hcstallname = chosenstall.hcstallname;
            commentSend.daysopen = chosenstall.daysopen;
            commentSend.hoursopen = chosenstall.hoursopen;

            recyclerView = view.findViewById(R.id.ChosenStallCommentRecycler);
            linearLayoutManager = new LinearLayoutManager(getContext());
            commentAdapter = new HawkerCommentAdapter(getContext(), testlist, commentSend, contentCheck);

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

    }

}