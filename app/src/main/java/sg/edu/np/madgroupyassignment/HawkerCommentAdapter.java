package sg.edu.np.madgroupyassignment;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HawkerCommentAdapter extends RecyclerView.Adapter<CommentViewholder> {

    private Context c;
    private ArrayList<Comments> commentData;
    private HomeMixData CommentRetrieve;
    private Boolean contentCheck;

    public HawkerCommentAdapter(Context c, ArrayList<Comments> commentData, HomeMixData CommentRetrieve, Boolean contentCheck) {
        this.c = c;
        this.commentData = commentData;
        this.CommentRetrieve = CommentRetrieve;
        this.contentCheck = contentCheck;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == commentData.size() + 1){
            return 2;
        }
        else if (position == 0){
            return 0;
        }
        else{
            return 1;
        }
    }

    @NonNull
    @Override
    public CommentViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item;
        if (viewType == 0) {
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_hcchosen_stall, null, false);

            //Variables or views needed to define whats in Chosen stall
            ImageView chosenstallimg = item.findViewById(R.id.chosenstallimg);
            ImageView hccuserpfp = item.findViewById(R.id.hccuserpfp);
            TextView chosenstallname = item.findViewById(R.id.chosenstallname);
            TextView hccusername = item.findViewById(R.id.hccusername);
            TextView hccaddress = item.findViewById(R.id.hccaddress);
            TextView hccparagraph = item.findViewById(R.id.hccparagraph);
            TextView descriptionheader = item.findViewById(R.id.descriptiontv);
            TextView hccopendays = item.findViewById(R.id.hccopendays);
            TextView hccopenhours = item.findViewById(R.id.hccopenhours);

            //Adding Stall data
            Picasso.get().load(CommentRetrieve.getHccoverimg()).into(chosenstallimg);
            Picasso.get().load(CommentRetrieve.getHccuserpfp()).into(hccuserpfp);
            chosenstallname.setText(CommentRetrieve.hcstallname);
            hccusername.setText(CommentRetrieve.hcauthor);
            hccaddress.setText(CommentRetrieve.hccaddress);
            hccparagraph.setText(CommentRetrieve.hccparagraph);
            descriptionheader.setText("About " + CommentRetrieve.hcstallname);
            hccopendays.setText(CommentRetrieve.daysopen);
            hccopenhours.setText(CommentRetrieve.hoursopen);
        }else if (viewType == 1) {
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
        } else {
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_post, parent, false);
        }

        return new CommentViewholder(item);
}

    @Override
    public void onBindViewHolder(@NonNull CommentViewholder holder, int position) {
        if (position <= commentData.size() && position != 0){
            ImageView commentPic = holder.itemView.findViewById(R.id.CommentPFP);
            TextView commentUser = holder.itemView.findViewById(R.id.CommentUsername);
            TextView commentText = holder.itemView.findViewById(R.id.CommentText);

            Comments recyclerCurrentComment = commentData.get(position - 1);
            if (contentCheck){
                commentPic.setVisibility(View.INVISIBLE);
            }
            commentUser.setText(recyclerCurrentComment.getCommentUsername());
            commentText.setText(recyclerCurrentComment.getCommentText());
        }

        //Comment input
        if (position == commentData.size() + 1) {
            TextInputEditText commentInput = holder.itemView.findViewById(R.id.commentInput);
            Button commentPost = holder.itemView.findViewById(R.id.commentPost);

            //Submit Comments
            commentPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(c, "Posted...", Toast.LENGTH_SHORT).show();
                    commentInput.getText().clear();
                    commentPost.setVisibility(View.GONE);
                }
            });


            //Show button when text edit is in focus
            commentInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                        commentPost.setVisibility(View.VISIBLE);
                    }
                }
            });

            //Enable button when comment text has been entered
            commentInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    commentPost.setEnabled(charSequence.toString().trim().length() == 0 ? false : true);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return commentData.size() + 2;
    }
}
