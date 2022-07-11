package sg.edu.np.madgroupyassignment;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class RecipeCornerMain extends Fragment implements AdapterView.OnItemSelectedListener{

    //creating variables for context, recyclerview, adapter, postsholder and arraylist
    Context c;
    private RecyclerView recipeRV;
    public RecipeAdapter adapter;
    PostsHolder postsHolder;
    public ArrayList<RecipeCorner> recipeModalArrayList = new ArrayList<>();

    //create constructor
    public RecipeCornerMain(){
        this.c = c;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_recipe_corner_main, container, false);

        //getting the RC object from firebase and adding to arraylist
        recipeModalArrayList.removeAll(recipeModalArrayList);
        for (RecipeCorner obj : postsHolder.getRecipePosts()) {
            recipeModalArrayList.add(obj);
        }

        //spinner for sorting the items of the arraylist
        Spinner sortSpinner = view.findViewById(R.id.spinner);
        ArrayAdapter sortAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sortby));
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setOnItemSelectedListener(this);

        // getting the RC recyclerview
        recipeRV = view.findViewById(R.id.idRVRecipe);

        // getting search view of our item.
        SearchView searchView = view.findViewById(R.id.SearchID);

        // below line is to call set on query text listener method for search bar
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


        // initializing our adapter class.
        Collections.reverse(recipeModalArrayList); //default displays newest post first
        adapter = new RecipeAdapter(recipeModalArrayList, getActivity(),true);

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recipeRV.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        recipeRV.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        recipeRV.setAdapter(adapter);

        return view;
    }

    //method to sort by the ratings(level of difficulty)
    private void sortrating(boolean b1){
        ArrayList<RecipeCorner> sortlist = new ArrayList<>();
        for (RecipeCorner item : recipeModalArrayList) {
            sortlist.add(item);
        }
        Collections.sort(sortlist, new Comparator<RecipeCorner>() {
            @Override
            public int compare(RecipeCorner rc1, RecipeCorner rc2) {
                return rc1.recipeRating.compareTo(rc2.recipeRating);
            }
        });
        if (b1) //if true, reverse list to display hardest first
            Collections.reverse(sortlist);
        adapter.sort(sortlist);
    }

    //method to sort by stall name alphabetically
    private void sortAlphabet(boolean b2){
        ArrayList<RecipeCorner> sortlist2 = new ArrayList<>();
        for (RecipeCorner item : recipeModalArrayList) {
            sortlist2.add(item);
        }
        Collections.sort(sortlist2, new Comparator<RecipeCorner>() {
            @Override
            public int compare(RecipeCorner rc1, RecipeCorner rc2) {
                return rc1.recipeName.compareToIgnoreCase(rc2.recipeName);
            }
        });
        if (b2) //if true, reverse list to display from Z to A
            Collections.reverse(sortlist2);
        adapter.sort(sortlist2);
    }

    //method to sort by oldest or most recent posts
    private void sortdate(boolean b3){
        ArrayList<RecipeCorner> sortlist3 = new ArrayList<>();
        for (RecipeCorner item : recipeModalArrayList) {
            sortlist3.add(item);
        }
        if (b3) //if true, reverse list to display oldest post first
            Collections.reverse(sortlist3);
        adapter.sort(sortlist3);
    }

    //method to filter data when using the search bar
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
            Toast.makeText(getActivity(), "No Recipe Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }

    //method to select each item in the spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(i){
            case 0:
                adapter.unsort(recipeModalArrayList); //when clicked, goes back to default order of list
                break;
            case 1:
                sortrating(true); //when clicked, sort according to hardest recipe first
                break;
            case 2:
                sortrating(false); //when clicked, sort according to easiest recipe first
                break;
            case 3:
                sortAlphabet(false); //when clicked, sort according to A to Z
                break;
            case 4:
                sortAlphabet(true); //when clicked, sort according to Z to A
                break;
            case 5:
                sortdate(false); //when clicked, sort according to newest post first (default)
                break;
            case 6:
                sortdate(true); //when clicked, sort according to oldest post first
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}