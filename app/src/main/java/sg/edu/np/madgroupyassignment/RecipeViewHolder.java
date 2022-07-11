package sg.edu.np.madgroupyassignment;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RecipeViewHolder extends RecyclerView.ViewHolder {

    // creating variables for our views.
    TextView recipeName, recipeDesc, userName;
    ImageView foodImage;
    RatingBar ratingBar;
    CheckBox checkbox;

    public RecipeViewHolder(View item) {
        super(item);
        // initializing our views with their ids.
        recipeName = itemView.findViewById(R.id.idRecipeName);
        recipeDesc = itemView.findViewById(R.id.idRecipeDescription);
        userName = itemView.findViewById(R.id.idUser);
        foodImage = itemView.findViewById(R.id.imageView);
        ratingBar = itemView.findViewById(R.id.ratingBar);
        checkbox = itemView.findViewById(R.id.rccheckbox);
    }
}
