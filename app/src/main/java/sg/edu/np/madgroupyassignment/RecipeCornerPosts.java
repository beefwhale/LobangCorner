package sg.edu.np.madgroupyassignment;
//unsure of how to put the data(duration,ingreds, steps) in from recipecorner
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

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;


public class RecipeCornerPosts extends Fragment {
    Context c;
    private static RecipeCorner recipeCorner;
    public RecipeCornerPosts(){this.c =c;};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_recipe_corner_posts, container, false);


        TextView n = view.findViewById(R.id.idRecipeName);
        TextView d = view.findViewById(R.id.idRecipeDescription);
        TextView id = view.findViewById(R.id.idUser);
        RatingBar rb = view.findViewById(R.id.ratingBar);
        ImageView i = view.findViewById(R.id.imageView);

        TextView duration = view.findViewById(R.id.idDuration);
        TextView steps = view.findViewById(R.id.idSteps);
        TextView ingred = view.findViewById(R.id.idIngreds);

        Bundle b = this.getArguments();
        int rcNo = (int) b.getInt("recipeNo");
        int HomeDataCheck = (int) b.getInt("HomeDataCheck");

        if (HomeDataCheck == 1){// If its Clicked from Home and Not Main Recipe Page
            ArrayList<HomeMixData> recipeList = new ArrayList<>();
            recipeList = Parcels.unwrap(b.getParcelable("list"));
            HomeMixData recipePost;
            recipePost = recipeList.get(rcNo);

            n.setText(recipePost.recipeName);
            d.setText(recipePost.recipeDescription);
            id.setText("By: " + recipePost.userName);
            rb.setRating(recipePost.recipeRating);
            //i.setImage
            duration.setText("Duration: " + recipePost.duration + " mins");
            steps.setText(recipePost.steps);
            ingred.setText(recipePost.ingredients);
        }
        else{ // Clicked from Main Recipe Page
            ArrayList<RecipeCorner> recipeList = new ArrayList<>();
            recipeList = Parcels.unwrap(b.getParcelable("list"));
            RecipeCorner recipePost;
            recipePost = recipeList.get(rcNo);


            n.setText(recipePost.recipeName);
            d.setText(recipePost.recipeDescription);
            id.setText("By: " + recipePost.userName);
            rb.setRating(recipePost.recipeRating);
            //i.setImage
            duration.setText("Duration: " + recipePost.duration + " mins");
            steps.setText(recipePost.steps);
            ingred.setText(recipePost.ingredients);
        }


        //Picasso.get().load(recipeCorner.getFoodImage()).into(i);

        return view;
    }


}