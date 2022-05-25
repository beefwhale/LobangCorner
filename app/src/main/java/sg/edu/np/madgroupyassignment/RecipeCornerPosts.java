package sg.edu.np.madgroupyassignment;
//app crash cuz of fragment
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

public class RecipeCornerPosts extends Fragment {
    //Context c;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_recipe_corner_posts, container, false);

        //setContentView(R.layout.activity_recipe_corner_posts);

        Intent i = getActivity().getIntent();
        ((TextView)view.findViewById(R.id.idRecipeName))
                .setText(i.getStringExtra("name"));
        ((TextView)view.findViewById(R.id.idRecipeDescription))
                .setText(i.getStringExtra("desc"));
        ((TextView)view.findViewById(R.id.rating)).
                setText(i.getStringExtra("rating"));
        RatingBar r = view.findViewById(R.id.ratingBar);
        r.setRating(i.getIntExtra("ratingbar", 0));
        ((TextView)view.findViewById(R.id.idUser)).
                setText(i.getStringExtra("username"));
        return view;
    }
}