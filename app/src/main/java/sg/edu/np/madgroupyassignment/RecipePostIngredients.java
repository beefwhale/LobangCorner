package sg.edu.np.madgroupyassignment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipePostIngredients extends Fragment {

    static RecyclerView recyclerView;
    static ArrayList<Ingredient> ingredList;
    static RVAdapterIngred adapter;
    EditText ingredientName;
    EditText qty;
    EditText unit;
    ImageView add;
    String totalIngred;
    ArrayList<String> totalIngredList;
    FormsViewModel viewModel;

    public RecipePostIngredients() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View f3 = inflater.inflate(R.layout.fragment_recipe_post_ingredients, container, false);
        ingredList = new ArrayList<>();

        recyclerView = f3.findViewById(R.id.f3recyclerView); //Defining listView
        adapter = new RVAdapterIngred(getActivity().getApplicationContext(),ingredList,requireParentFragment());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));


        ingredientName = f3.findViewById(R.id.IngredInput); //Defining Ingred input
        qty = f3.findViewById(R.id.QtyInput); //Defining Qty input
        unit = f3.findViewById(R.id.UntInput); //Defining Unit input
        add = f3.findViewById(R.id.f3add); //Defining "plus sign" image
        //Make the plus sign add item
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ingred = ingredientName.getText().toString();

                String qtyString = qty.getText().toString();

                String unt = unit.getText().toString();

                if(ingred == null || ingred.length() == 0 || qtyString == null
                        || qtyString.length() == 0 || unt == null || unt.length() == 0){
                    //if input is empty, show a toast
                    Toast.makeText(getActivity(), "There is no input.", Toast.LENGTH_SHORT).show();
                }
                else{
                    int quantity = Integer.parseInt(qtyString);
                    Ingredient ingredient = new Ingredient(ingred,quantity,unt);
                    addItem(ingredient); //add input string to list
                    ingredientName.setText(""); //set text back to empty after entering
                    qty.setText(""); //set text back to empty after entering
                    unit.setText(""); //set text back to empty after entering

                    totalIngredList = new ArrayList<>();
                    totalIngred = "";

                    for (int i=0; i<ingredList.size(); i++){
                        String ingredString = ingredList.get(i).qty + " " + ingredList.get(i).unit + " " + ingredList.get(i).name;
                        totalIngredList.add(ingredString);

                    }

                    for (int i=0; i<totalIngredList.size(); i++){
                        if (i==(totalIngredList.size()-1)){
                            totalIngred = totalIngred + totalIngredList.get(i);
                        }
                        else if (i!=(totalIngredList.size()-1)){
                            totalIngred = totalIngred + totalIngredList.get(i) + "\n";
                        }
                    }

                    viewModel = new ViewModelProvider(requireParentFragment()).get(FormsViewModel.class);
                    viewModel.selectRecipeIngred(totalIngred);
                }
            }
        });


        return f3;
    }
    //function to add onto list
    public static void addItem(Ingredient item){
        ingredList.add(item);
        recyclerView.setAdapter(adapter); //update the listview
    }

    //function to remove from list
    public static void removeItem(int remove){
        ingredList.remove(remove);
        recyclerView.setAdapter(adapter); //update the listview
    }
}