package sg.edu.np.madgroupyassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

public class RecipeCornerPosts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_corner_posts);

        Intent i = getIntent();
        ((TextView)findViewById(R.id.idRecipeName))
                .setText(i.getStringExtra("name"));
        ((TextView)findViewById(R.id.idRecipeDescription))
                .setText(i.getStringExtra("desc"));
        ((TextView)findViewById(R.id.rating)).
                setText(i.getStringExtra("rating"));
        RatingBar r = findViewById(R.id.ratingBar);
        r.setRating(i.getIntExtra("ratingbar", 0));
        ((TextView)findViewById(R.id.idUser)).
                setText(i.getStringExtra("username"));
    }
}