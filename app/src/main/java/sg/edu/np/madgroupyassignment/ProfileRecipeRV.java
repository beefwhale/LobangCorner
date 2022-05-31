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

    Context c;
    public static ArrayList<RecipeCorner> recipeCornersList = new ArrayList<>();

    // creating variables for
    // our ui components.
    private RecyclerView recipeRV;

    // variable for our adapter
    // class and array list
    private RecipeAdapter adapter;
    public ProfileRecipeRV(){
        this.c = c;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_reciperv, container, false);

        // calling method to
        // build recycler view.
        recipeRV = view.findViewById(R.id.idRVRecipe);
        buildRecyclerView();
        return view;
    }



    private void buildRecyclerView() {

        // below line we are creating a new array list

        /*HashMap<String, Object> testhash = new HashMap<String, Object>();
        testhash.put("testString1", "testobject1");
        testhash.put("testString2", "testobject2");*/

        // below line is to add data to our array list.
        recipeCornersList.add(new RecipeCorner("Cream Cheese Rangoons", "Crispy Cream-Cheese filled Rangoons that are easy to make at home",
                3, 10, "Yongqing", "100", "step 1: "));
        recipeCornersList.add(new RecipeCorner("Ondeh-Ondeh", "Traditional Gula-Melaka Filled Ondeh-Ondeh",
                3, 6, "Celsius", "30", "step 1: "));
        recipeCornersList.add(new RecipeCorner("Dumpling", "Delicious and Juicy Pork and Chives Dumpling",
                2, 4, "Yong Chuen", "70", "step 1: "));
        recipeCornersList.add(new RecipeCorner("Pineapple Tarts", "Best-Ever Pineapple Tarts",
                4, 26, "Zi Xian", "50", "step 1: "));
        recipeCornersList.add(new RecipeCorner("Fried Crab Sticks", "Crispy and Tasty Fried Crab Sticks",
                5, 20, "Hasanah", "25",  "step 1: "));
        recipeCornersList.add(new RecipeCorner("Nine Layered Kueh", "Bright and Colourful Nine Layered Kueh",
                3, 9, "Wesley", "45", "step 1: "));
        recipeCornersList.add(new RecipeCorner("Curry Path", "Chinese Curry Path",
                0, 2,"Teo", "20", "step 1: "));
        recipeCornersList.add(new RecipeCorner("Chiffon Cake", "Pandan Chiffon Cake",
                4, 2,"Q", "30", "step 1: "));


        // initializing our adapter class.
        adapter = new RecipeAdapter(recipeCornersList, c);

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(c);
        recipeRV.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        recipeRV.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        recipeRV.setAdapter(adapter);
    }

}
