package sg.edu.np.madgroupyassignment;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RecipeViewHolder extends RecyclerView.ViewHolder {

    // creating variables for our views.
    TextView recipeName, recipeDesc, ratingNo, userName;
    TextView ingreds, steps, duration;
    ImageView foodImage;
    RatingBar ratingBar;

    public RecipeViewHolder(View item) {
        super(item);
        // initializing our views with their ids.
        recipeName = itemView.findViewById(R.id.idRecipeName);
        recipeDesc = itemView.findViewById(R.id.idRecipeDescription);
        ratingNo = itemView.findViewById(R.id.rating);
        userName = itemView.findViewById(R.id.idUser);
        foodImage = itemView.findViewById(R.id.imageView);
        ratingBar = itemView.findViewById(R.id.ratingBar);
        //ingreds = itemView.findViewById(R.id.idIngreds);
        //steps = itemView.findViewById(R.id.idSteps);
        //duration = itemView.findViewById(R.id.idDuration);

    }
}
