package sg.edu.np.madgroupyassignment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecipePostIngredients extends Fragment {

    static RecyclerView recyclerView;
    static ArrayList<Ingredient> ingredList;
    static RVAdapterIngred adapter;
    EditText ingredientName;
    EditText qty;
    EditText unit;
    ImageView add;
    String totalIngred;
    String ingredString;
    ArrayList<String> totalIngredList;
    FormsViewModel viewModel;
    String unt;
    Button nextBtn;
    TextView prevBtn;

    RecipeCorner recipeStall = new RecipeCorner();

    public RecipePostIngredients() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View f2 = inflater.inflate(R.layout.fragment_recipe_post_ingredients, container, false);
        ingredList = new ArrayList<>();

        recyclerView = f2.findViewById(R.id.f3recyclerView); //Defining listView
        adapter = new RVAdapterIngred(getActivity().getApplicationContext(), ingredList, requireParentFragment());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));


        ingredientName = f2.findViewById(R.id.IngredInput); //Defining Ingred input
        qty = f2.findViewById(R.id.QtyInput); //Defining Qty input
        unit = f2.findViewById(R.id.UntInput); //Defining Unit input
        add = f2.findViewById(R.id.f3add); //Defining "plus sign" image
        nextBtn = f2.findViewById(R.id.f2nextbutton);
        prevBtn = f2.findViewById(R.id.f2prevbutton);

        viewModel = new ViewModelProvider(requireParentFragment()).get(FormsViewModel.class);
        totalIngredList = new ArrayList<>();

        //If EDITING
        viewModel.getRecipePost().observe(getViewLifecycleOwner(), new Observer<RecipeCorner>() {
            public void onChanged(RecipeCorner i) {
                if (i.recipeName != null || i.recipeName != "") {
                    recipeStall = i;
                    String symbol1 = (String) "#-#";
                    String symbol2 = (String) "#=#";
                    totalIngred = recipeStall.getIngredients();
                    String[] ingredSplit1 = recipeStall.getIngredients().split(symbol1); // Splitting every set of ingredients listed
                    for (String s : ingredSplit1) {
                        String[] ingredSplit2 = {}; //Resetting Array
                        ingredSplit2 = s.split(symbol2); //Splitting every set of ingredient by its unit, qty and ingredient name
                        if (ingredSplit2.length == 2) { //Doesn't have unit within
                            // Name, Qty, Empty Unit
                            ingredList.add(new Ingredient(ingredSplit2[1], Integer.parseInt(ingredSplit2[0]), ""));
                        } else if ((ingredSplit2.length == 3)) { // Has a Unit within
                            // Name, Qty,  Unit
                            ingredList.add(new Ingredient(ingredSplit2[2], Integer.parseInt(ingredSplit2[0]), ingredSplit2[1]));
                        }
                    }
                    viewModel.selectRecipeIngred(totalIngred);

                }

            }
        });
        //Make the plus sign add item
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ingred = ingredientName.getText().toString();
                totalIngred = "";
                String qtyString = qty.getText().toString();
                if (unit == null || unit.length() == 0 || unit.getText().toString() == "") {
                    unt = "";
                } else {
                    unt = unit.getText().toString();
                }

                if (ingred == null || ingred.length() == 0 || qtyString == null
                        || qtyString.length() == 0) {
                    //if input is empty, show a toast
                    Toast.makeText(getActivity(), "There is no input.", Toast.LENGTH_SHORT).show();
                } else {
                    int quantity = Integer.parseInt(qtyString);
                    Ingredient ingredient = new Ingredient(ingred, quantity, unt);
                    addItem(ingredient); //add input string to list
                    ingredientName.setText(""); //set text back to empty after entering
                    qty.setText(""); //set text back to empty after entering
                    unit.setText(""); //set text back to empty after entering

                    for (int i = 0; i < ingredList.size(); i++) {
                        if (ingredList.get(i).unit == "" || ingredList.get(i).unit == null || ingredList.get(i).unit.length() == 0) {
                            ingredString = ingredList.get(i).qty + "#=#" + ingredList.get(i).name; //String formatting for ingredients (w/o unit)
                        } else {
                            ingredString = ingredList.get(i).qty + "#=#" + ingredList.get(i).unit + "#=#" + ingredList.get(i).name; //String formatting for ingredients
                        }
                        totalIngredList.add(ingredString);

                    }

                    for (int i = 0; i < totalIngredList.size(); i++) {
                        if (i == (totalIngredList.size() - 1)) { // If last in List
                            totalIngred = totalIngred + totalIngredList.get(i);
                        } else if (i != (totalIngredList.size() - 1)) { // If not the last in list
                            totalIngred = totalIngred + totalIngredList.get(i) + "#-#";
                        }
                    }

                    viewModel.selectRecipeIngred(totalIngred);
                }
            }
        });
        //Moving to next page (Steps)
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.changeFragment(2);
            }
        });
        //Moving to prev page (Main)
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.changeFragment(0);
            }
        });
        return f2;
    }

    //function to add onto list
    public static void addItem(Ingredient item) {
        ingredList.add(item);
        recyclerView.setAdapter(adapter); //update the listview
    }

    //function to remove from list
    public static void removeItem(int remove) {
        ingredList.remove(remove);
        recyclerView.setAdapter(adapter); //update the listview
    }
}