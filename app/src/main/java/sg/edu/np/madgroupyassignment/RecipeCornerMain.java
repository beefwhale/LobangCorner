package sg.edu.np.madgroupyassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


public class RecipeCornerMain extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // creating variables for
    // our ui components.
    private RecyclerView recipeRV;

    // variable for our adapter
    // class and array list
    private RecipeAdapter adapter;
    private ArrayList<RecipeCorner> recipeModalArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_corner_main);

        //spinner for sorting/filtering
        Spinner sortSpinner = findViewById(R.id.spinner);
        ArrayAdapter sortAdapter = new ArrayAdapter<String>(RecipeCornerMain.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sortby));
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setOnItemSelectedListener(this);

        // initializing our variables.
        recipeRV = findViewById(R.id.idRVRecipe);

        // getting search view of our item.
        SearchView searchView = findViewById(R.id.SearchID);

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                return false;
            }
        });

        // calling method to
        // build recycler view.
        buildRecyclerView();
    }

    private void sortrating(int i){
        ArrayList<RecipeCorner> sortlist = new ArrayList<>();
        Collections.sort(recipeModalArrayList, new Comparator<RecipeCorner>() {
            @Override
            public int compare(RecipeCorner rc1, RecipeCorner rc2) {
                if (i==1)
                    return rc1.recipeRating.compareTo(rc2.recipeRating);
                else
                    return rc1.noOfRaters.compareTo(rc2.noOfRaters);
            }
        });
        Collections.reverse(recipeModalArrayList);
        sortlist = recipeModalArrayList;
        adapter.sort(sortlist);
    }

    private void sortAlphabet(){
        ArrayList<RecipeCorner> sortlist2 = new ArrayList<>();
        Collections.sort(recipeModalArrayList, new Comparator<RecipeCorner>() {
            @Override
            public int compare(RecipeCorner rc1, RecipeCorner rc2) {
                return rc1.recipeName.compareToIgnoreCase(rc2.recipeName);
            }
        });
        sortlist2 = recipeModalArrayList;
        adapter.sort(sortlist2);
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<RecipeCorner> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (RecipeCorner item : recipeModalArrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getRecipeName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }

    private void buildRecyclerView() {

        // below line we are creating a new array list
        recipeModalArrayList = new ArrayList<>();

        // below line is to add data to our array list.
        recipeModalArrayList.add(new RecipeCorner("Cream Cheese Rangoons", "Crispy Cream-Cheese filled Rangoons that are easy to make at home",
                3, 10, "Yongqing"));
        recipeModalArrayList.add(new RecipeCorner("Ondeh-Ondeh", "Traditional Gula-Melaka Filled Ondeh-Ondeh",
                3, 6, "Celcius"));
        recipeModalArrayList.add(new RecipeCorner("Dumpling", "Delicious and Juicy Pork and Chives Dumpling",
                2, 4, "Yong Chuen"));
        recipeModalArrayList.add(new RecipeCorner("Pineapple Tarts", "Best-Ever Pineapple Tarts",
                4, 26, "Zi Xian"));
        recipeModalArrayList.add(new RecipeCorner("Fried Crab Sticks", "Crispy and Tasty Fried Crab Sticks",
                5, 20, "Hasanah"));
        recipeModalArrayList.add(new RecipeCorner("Nine Layered Kueh", "Bright and Colourful Nine Layered Kueh",
                3, 9, "Wesley"));
        recipeModalArrayList.add(new RecipeCorner("Curry Path", "Chinese Curry Path", 0, 2,"Teo"));
        recipeModalArrayList.add(new RecipeCorner("Chiffon Cake", "Pandan Chiffon Cake", 4, 2,"Q"));


        // initializing our adapter class.
        adapter = new RecipeAdapter(recipeModalArrayList, RecipeCornerMain.this);

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recipeRV.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        recipeRV.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        recipeRV.setAdapter(adapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(i){
            case 1:
                sortrating(1);
                break;
            case 2:
                //Toast.makeText(this, "hello2", Toast.LENGTH_SHORT).show();
                sortrating(2);
                break;
            case 3:
                sortAlphabet();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

