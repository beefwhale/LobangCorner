package sg.edu.np.madgroupyassignment;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

public class RecipeCommentAdapter extends RecyclerView.Adapter<CommentViewholder> {
    private Context c;
    private ArrayList<Comments> commentData;
    private HomeMixData CommentRetrieve;
    private Boolean contentCheck;

    public RecipeCommentAdapter(Context c, ArrayList<Comments> commentData, HomeMixData CommentRetrieve, Boolean contentCheck) {
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
        if (viewType == 0){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recipe_corner_posts, null, false);

            //find textview, ratingbar and imageview from view
            TextView n = item.findViewById(R.id.idRecipeName);
            TextView d = item.findViewById(R.id.idRecipeDescription);
            TextView id = item.findViewById(R.id.idUser);
            RatingBar rb = item.findViewById(R.id.ratingBar);
            ImageView i = item.findViewById(R.id.imageView);
            ImageView i2 = item.findViewById(R.id.rcbookmark);
            TextView duration = item.findViewById(R.id.idDuration);
            TextView steps = item.findViewById(R.id.idSteps);
            TextView ingred = item.findViewById(R.id.idIngreds);

            //set the textview, ratingbar and image view accordingly
            n.setText(CommentRetrieve.recipeName);
            d.setText(CommentRetrieve.recipeDescription);
            id.setText("By: " + CommentRetrieve.userName);
            rb.setRating(CommentRetrieve.recipeRating);
            duration.setText("Duration: " + CommentRetrieve.duration + " mins");
            steps.setText(CommentRetrieve.steps);
            ingred.setText(CommentRetrieve.ingredients);
            Picasso.get().load(CommentRetrieve.getFoodImage()).into(i);
            //Work on the bookmark here if you want --Celsius

        } else if (viewType == 1) {
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
        }  else {
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_post, parent, false);
        }

        return new CommentViewholder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewholder holder, int position) {
        if (position <= commentData.size() && position != 0) {
            ImageView commentPic = holder.itemView.findViewById(R.id.CommentPFP);
            TextView commentUser = holder.itemView.findViewById(R.id.CommentUsername);
            TextView commentText = holder.itemView.findViewById(R.id.CommentText);

            Comments recyclerCurrentComment = commentData.get(position - 1);
            if (contentCheck) {
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
