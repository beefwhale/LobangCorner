package sg.edu.np.madgroupyassignment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RecipeCommentAdapter extends RecyclerView.Adapter<CommentViewholder> {

    private Context c;
    private ArrayList<Comments> commentData;
    private HomeMixData CommentRetrieve;
    private Boolean contentCheck;
    private String postID, userID, token;
    private PostsHolder postsHolder;
    private UserProfile userProfile;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference, databaseReference2, deviceTokenReference;
    private FirebaseDatabase firebaseDatabase;
    private HashMap<String, Object> rcplist = new HashMap<String, Object>();
    View item;

    public RecipeCommentAdapter(Context c, ArrayList<Comments> commentData, HomeMixData CommentRetrieve, Boolean contentCheck) {
        this.c = c;
        this.commentData = commentData;
        this.CommentRetrieve = CommentRetrieve;
        this.contentCheck = contentCheck;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        postID = CommentRetrieve.getPostID();
        userID = firebaseAuth.getUid();
        userProfile = postsHolder.getUserProfile();
        databaseReference = firebaseDatabase.getReference().child("Comments").child(postID);
        databaseReference2 = firebaseDatabase.getReference();
        deviceTokenReference = firebaseDatabase.getReference().child("Device Registration Tokens").child(CommentRetrieve.getOwner());

        deviceTokenReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        token = task.getResult().getValue().toString();
                    }
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position == commentData.size() + 1) {
            return 2;
        } else if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public CommentViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
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

            // Making Username Clickable
            id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayAuthorProfile();
                }
            });
            //Cleaning Comments
            String ingredients = CommentRetrieve.ingredients;
            ingredients = ingredients.replace("#=#", " ");
            ingredients = ingredients.replace("#-#", "\n");
            ingred.setText(ingredients);
            Picasso.get().load(CommentRetrieve.getFoodImage()).into(i);

            DatabaseReference dr = databaseReference2.child("UserProfile").child(firebaseAuth.getUid()).child("bmrcplist");
            dr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(postID)) {                             //if post alr exist in bookmark rcp list,
                        i2.setImageResource(R.drawable.ic_bookmark_filled);     // replace image with a filled icon
                        i2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(c, "Recipe already saved", Toast.LENGTH_SHORT).show();   //when icon is clicked, toast message displayed
                            }
                        });
                    } else {                                                       //if post do not exist, when icon is clicked
                        i2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                i2.setImageResource(R.drawable.ic_bookmark_filled);                     //replace image with a filled icon
                                Toast.makeText(c, "Recipe saved!", Toast.LENGTH_SHORT).show();      //display toast message
                                rcplist.put(postID, postID);                                             //save in firebase
                                databaseReference2.child("UserProfile").child(firebaseAuth.getUid()).child("bmrcplist").updateChildren(rcplist);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else if (viewType == 1) {
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
        } else {
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
            ImageView commentMore = holder.itemView.findViewById(R.id.CommentMore);
            TextView commentSince = holder.itemView.findViewById(R.id.CommentSince);

            //Getting current view comment
            Comments recyclerCurrentComment = commentData.get(position - 1);

            //Comment check
            if (contentCheck) {
                commentPic.setVisibility(View.GONE);
                commentMore.setVisibility(View.GONE);
                commentSince.setVisibility(View.GONE);
            } else {
                commentPic.setVisibility(View.VISIBLE);
                commentMore.setVisibility(View.VISIBLE);
                commentSince.setVisibility(View.VISIBLE);
            }

            //Load comment data
            if (!contentCheck) {
                Picasso.get().load(recyclerCurrentComment.getCommentPFP()).into(commentPic);
            }
            commentUser.setText(recyclerCurrentComment.getCommentUsername());
            commentText.setText(recyclerCurrentComment.getCommentText());

            //Populating comment date
            if (recyclerCurrentComment.getTimestamp() != null) {
                Date commentDate = new Date(recyclerCurrentComment.getTimestamp());
                Date currentDate = new Date(System.currentTimeMillis());
                Long differenceInTime = currentDate.getTime() - commentDate.getTime();
                Long differenceInSeconds = (differenceInTime / 1000 % 60);
                Long differenceInMinutes = (differenceInTime / (1000 * 60) % 60);
                Long differenceInHours = (differenceInTime / (1000 * 60 * 60) % 60);
                Long differenceInDays = (differenceInTime / (1000 * 60 * 60 * 24)) % 365;
                Long differenceInYears = (differenceInTime / (1000l * 60 * 60 * 24 * 365));

                if (differenceInYears != 0) {
                    commentSince.setText(differenceInYears + " years ago");
                } else if (differenceInDays != 0) {
                    commentSince.setText(differenceInDays + " days ago");
                } else if (differenceInHours != 0) {
                    commentSince.setText(differenceInHours + " hours ago");
                } else if (differenceInMinutes != 0) {
                    commentSince.setText(differenceInMinutes + " minutes ago");
                } else {
                    commentSince.setText(differenceInSeconds + " seconds ago");
                }
            }

            //Comment more options
            commentMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(c, commentMore);
                    popupMenu.getMenuInflater().inflate(R.menu.comment_more_options, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch ("" + menuItem.getTitle()) {
                                case "Copy comment text":
                                    Toast.makeText(c, "Copied... ", Toast.LENGTH_SHORT).show();
                                    ClipboardManager clipboard = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clipData = ClipData.newPlainText("Comment Text", recyclerCurrentComment.getCommentText());
                                    clipboard.setPrimaryClip(clipData);
                                    break;

                                case "Delete":
                                    databaseReference.child(recyclerCurrentComment.getCommentUID()).setValue(null);
                                    Toast.makeText(c, "Deleted...", Toast.LENGTH_SHORT).show();
                                    commentData.remove(holder.getAdapterPosition() - 1);
                                    Log.e("commentdelete", false + "");
                                    if (commentData.size() == 0) {
                                        Log.e("commentdelete", true + "");
                                        Comments comment = new Comments();
                                        commentPic.setVisibility(View.GONE);
                                        commentMore.setVisibility(View.GONE);
                                        commentSince.setVisibility(View.GONE);

                                        comment.setCommentUsername("Be the first to write a Comment!");
                                        commentData.add(comment);
                                        contentCheck = true;
                                    }
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    notifyItemRangeChanged(holder.getAdapterPosition(), commentData.size() + 2);
                                    break;
                            }
                            return true;
                        }
                    });

                    if (!recyclerCurrentComment.getCommentOwner().equals(userID)) {
                        Menu m = popupMenu.getMenu();
//                        m.removeItem(m.findItem(R.id.Edit).getItemId());
                        m.removeItem(m.findItem(R.id.Delete).getItemId());
                    }

                    popupMenu.show();
                }
            });
        }

        //Comment posts
        if (position == commentData.size() + 1) {
            TextInputEditText commentInput = holder.itemView.findViewById(R.id.commentInput);
            Button commentPost = holder.itemView.findViewById(R.id.commentPost);

            //Submit Comments
            commentPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(c, "Posted...", Toast.LENGTH_SHORT).show();
                    String commentUID = databaseReference.push().getKey();
                    Comments newComment = new Comments(postID, commentUID, userID, userProfile.getProfileImg(), userProfile.getUsername(), commentInput.getText().toString(), System.currentTimeMillis());
                    databaseReference.child(commentUID).setValue(newComment);
                    for (Comments comments : commentData) {
                        if (comments.getCommentUsername() == "Be the first to write a Comment!") {
                            commentData.removeAll(commentData);
                            contentCheck = false;
                        }
                    }
                    commentData.add(newComment);
                    notifyItemInserted(commentData.size() + 1);
                    notifyItemRangeChanged(commentData.size(), commentData.size() + 2);

//                    Sending notification to post owner
                    if (!userID.equals(CommentRetrieve.getOwner()) && token != null) {
                        FirebaseMessagingSender.pushNotification(
                                c,
                                token,
                                CommentRetrieve.recipeName,
                                userProfile.getUsername() + " has commented on your post!"
                        );
                    }

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


    public void displayAuthorProfile() {
        AppCompatActivity activity = (AppCompatActivity) item.getContext();
        if (CommentRetrieve.owner != null) {
            if (CommentRetrieve.owner.equals(userProfile.UID)) { // if its the user's own account
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, new Profile(true), "Profile")
                        .addToBackStack(null).commit();
                MainActivity.bottomNavigationView.getMenu().findItem(R.id.profile).setChecked(true);
            } else { // Not the user's wn account = author's account
                Fragment profileFragment = new Profile(false);
                Bundle bundle = new Bundle();
                bundle.putString("userID", CommentRetrieve.owner); // Passing username inside
                profileFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, profileFragment)
                        .addToBackStack(null).commit();
            }
        } else {
            Toast.makeText(c, "An Error Occured", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return commentData.size() + 2;
    }
}
