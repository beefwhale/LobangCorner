package sg.edu.np.madgroupyassignment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ProfileRecipeRV extends Fragment{

    private Context c;

    private PostsHolder postsHolder;
    private ArrayList<RecipeCorner> recipeCornersList = new ArrayList<>();
    RecyclerView recipeRV;
    RecipeAdapter adapter;

    public ProfileRecipeRV(){
        this.c = c;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_reciperv, container, false);

        recipeCornersList.removeAll(recipeCornersList);
        for (RecipeCorner obj : postsHolder.getUserRecipePosts()) {
            recipeCornersList.add(obj);
        }

        recipeRV = view.findViewById(R.id.idRVRecipe);
        adapter = new RecipeAdapter(recipeCornersList, c, false);
        LinearLayoutManager manager = new LinearLayoutManager(c);
        recipeRV.setHasFixedSize(true);

        recipeRV.setLayoutManager(manager);
        recipeRV.setAdapter(adapter);

        return view;
    }

}
