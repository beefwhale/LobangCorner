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

        buildRecyclerView();

        recipeRV = view.findViewById(R.id.idRVRecipe);
        adapter = new RecipeAdapter(recipeCornersList, c);
        LinearLayoutManager manager = new LinearLayoutManager(c);
        recipeRV.setHasFixedSize(true);

        recipeRV.setLayoutManager(manager);
        recipeRV.setAdapter(adapter);

        return view;
    }

    private void buildRecyclerView() {
        //Populate user's recipe list, temporary and will change to get data from firebase
        if (recipeCornersList.isEmpty()){
            recipeCornersList.add(new RecipeCorner("4v8zoBfuGGPNY62hCSMT9UpyBAK2", "Cream Cheese Rangoons", "Crispy Cream-Cheese filled Rangoons that are easy to make at home",
                    3, 10, "Yongqing", "100", "step 1: ", "chikan", 1655028155253L));
            recipeCornersList.add(new RecipeCorner("4v8zoBfuGGPNY62hCSMT9UpyBAK2", "Ondeh-Ondeh", "Traditional Gula-Melaka Filled Ondeh-Ondeh",
                    3, 6, "Celsius", "30", "step 1: ", "chikan", 1655028155253L));
            recipeCornersList.add(new RecipeCorner("4v8zoBfuGGPNY62hCSMT9UpyBAK2", "Dumpling", "Delicious and Juicy Pork and Chives Dumpling",
                    2, 4, "Yong Chuen", "70", "step 1: ", "chikan", 1655028155253L));
            recipeCornersList.add(new RecipeCorner("4v8zoBfuGGPNY62hCSMT9UpyBAK2", "Pineapple Tarts", "Best-Ever Pineapple Tarts",
                    4, 26, "Zi Xian", "50", "step 1: ", "chikan", 1655028155253L));
            recipeCornersList.add(new RecipeCorner("4v8zoBfuGGPNY62hCSMT9UpyBAK2", "Fried Crab Sticks", "Crispy and Tasty Fried Crab Sticks",
                    5, 20, "Hasanah", "25",  "step 1: ", "chikan", 1655028187483L));
            recipeCornersList.add(new RecipeCorner("4v8zoBfuGGPNY62hCSMT9UpyBAK2", "Nine Layered Kueh", "Bright and Colourful Nine Layered Kueh",
                    3, 9, "Wesley", "45", "step 1: ", "chikan", 1655028187483L));
            recipeCornersList.add(new RecipeCorner("4v8zoBfuGGPNY62hCSMT9UpyBAK2", "Curry Path", "Chinese Curry Path",
                    0, 2,"Teo", "20", "step 1: ", "chikan", 1655028187483L));
            recipeCornersList.add(new RecipeCorner("4v8zoBfuGGPNY62hCSMT9UpyBAK2", "Chiffon Cake", "Pandan Chiffon Cake",
                    4, 2,"Q", "30", "step 1: ", "chikan", 1655028213219L));
        }
    }

}
