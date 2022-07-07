package sg.edu.np.madgroupyassignment;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;


public class RecipeCornerPosts extends Fragment {
    //variable for context
    Context c;
    //constructor
    public RecipeCornerPosts(){this.c =c;};

    public ArrayList<RecipeCorner> rcbookmarklist = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_recipe_corner_posts, container, false);
        //find textview, ratingbar and imageview from view
        TextView n = view.findViewById(R.id.idRecipeName);
        TextView d = view.findViewById(R.id.idRecipeDescription);
        TextView id = view.findViewById(R.id.idUser);
        RatingBar rb = view.findViewById(R.id.ratingBar);
        ImageView i = view.findViewById(R.id.imageView);
        ImageView i2 = view.findViewById(R.id.rcbookmark);
        TextView duration = view.findViewById(R.id.idDuration);
        TextView steps = view.findViewById(R.id.idSteps);
        TextView ingred = view.findViewById(R.id.idIngreds);

        //use of bundle to get data from each item in recyclerview
        Bundle b = this.getArguments();
        int rcNo = (int) b.getInt("recipeNo");
        int HomeDataCheck = (int) b.getInt("HomeDataCheck");

        if (HomeDataCheck == 1){// If its Clicked from Home and Not Main Recipe Page
            ArrayList<HomeMixData> recipeList = new ArrayList<>();
            recipeList = Parcels.unwrap(b.getParcelable("list"));
            HomeMixData recipePost;
            recipePost = recipeList.get(rcNo);

            //set the textview, ratingbar and image view accordingly
            n.setText(recipePost.recipeName);
            d.setText(recipePost.recipeDescription);
            id.setText("By: " + recipePost.userName);
            rb.setRating(recipePost.recipeRating);
            duration.setText("Duration: " + recipePost.duration + " mins");
            steps.setText(recipePost.steps);
            ingred.setText(recipePost.ingredients);
            Picasso.get().load(recipePost.getFoodImage()).into(i);
        }
        else{ // Clicked from Main Recipe Page
            ArrayList<RecipeCorner> recipeList = new ArrayList<>();
            recipeList = Parcels.unwrap(b.getParcelable("list"));
            RecipeCorner recipePost;
            recipePost = recipeList.get(rcNo);

            //set the textview, ratingbar and image view accordingly
            n.setText(recipePost.recipeName);
            d.setText(recipePost.recipeDescription);
            id.setText("By: " + recipePost.userName);
            rb.setRating(recipePost.recipeRating);
            duration.setText("Duration: " + recipePost.duration + " mins");
            steps.setText(recipePost.steps);
            ingred.setText(recipePost.ingredients);
            Picasso.get().load(recipePost.getFoodImage()).into(i);
        }

        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Recipe saved!", Toast.LENGTH_SHORT).show();
                ArrayList<RecipeCorner> recipeList = new ArrayList<>();
                recipeList = Parcels.unwrap(b.getParcelable("list"));
                RecipeCorner recipePost;
                recipePost = recipeList.get(rcNo);
                rcbookmarklist.add(recipePost);
                //hvent add to firebase
            }
        });
        return view;
    }

    public ArrayList<RecipeCorner> getRcbookmarklist() {
        return rcbookmarklist;
    }
}