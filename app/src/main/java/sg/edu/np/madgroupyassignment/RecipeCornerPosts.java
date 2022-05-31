package sg.edu.np.madgroupyassignment;
//unsure of how to put the data(duration,ingreds, steps) in from recipecorner
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


public class RecipeCornerPosts extends Fragment {
    Context c;

    public RecipeCornerPosts(){this.c =c;};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_recipe_corner_posts, container, false);


        TextView n = view.findViewById(R.id.idRecipeName);
        TextView d = view.findViewById(R.id.idRecipeDescription);
        TextView id = view.findViewById(R.id.idUser);
        TextView r = view.findViewById(R.id.rating);
        RatingBar rb = view.findViewById(R.id.ratingBar);
        ImageView i = view.findViewById(R.id.imageView);

        TextView duration = view.findViewById(R.id.idDuration);
        TextView steps = view.findViewById(R.id.idSteps);

        Bundle b = this.getArguments();
        int rcNo = (int) b.getInt("recipeNo");
        RecipeCorner recipePost = RecipeCornerMain.recipeModalArrayList.get(rcNo);

        n.setText(recipePost.recipeName);
        d.setText(recipePost.recipeDescription);
        id.setText("By: " + recipePost.userName);
        r.setText(recipePost.noOfRaters.toString());
        rb.setRating(recipePost.recipeRating);
        //i.setImage
        duration.setText("Duration: " + recipePost.duration + " mins");
        steps.setText(recipePost.steps);


        return view;
    }
}