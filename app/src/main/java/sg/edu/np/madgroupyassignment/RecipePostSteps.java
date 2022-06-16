package sg.edu.np.madgroupyassignment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;


public class RecipePostSteps extends Fragment {

    static RecyclerView recyclerView;
    static ArrayList<String> items;
    static RVAdapterSteps adapter;
    EditText input;
    ImageView add;
    static String finalSteps;
    private FormsViewModel viewModel;

    public RecipePostSteps() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View f2 = inflater.inflate(R.layout.fragment_recipe_post_steps, container, false);

        items = new ArrayList<>();

        recyclerView = f2.findViewById(R.id.f2recyclerView);
        adapter = new RVAdapterSteps(getActivity().getApplicationContext(),items,requireParentFragment());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));



        input = (EditText) f2.findViewById(R.id.input); //Defining input
        add = f2.findViewById(R.id.f2add); //Defining "plus sign" image

        //Make the plus sign add item

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = input.getText().toString();
                if(text == null || text.length() == 0){//if input is empty, show a toast
                    Toast.makeText(getActivity(), "There is no input.", Toast.LENGTH_SHORT).show();
                }
                else{
                    addItem(text); //add input string to list
                    input.setText(""); //set text back to empty after entering
                    finalSteps = ""; //STEPS parameter
                    for (int i=0; i<items.size(); i++){
                        if (i==(items.size()-1)){
                            finalSteps = finalSteps + "Step " + (i+1) + ": " +
                                    items.get(i);
                        }
                        else if (i!=(items.size()-1)){
                            finalSteps = finalSteps + "Step " + (i+1) + ": " +
                                    items.get(i) + "\n" + "\n";
                        }
                    }

                    viewModel = new ViewModelProvider(requireParentFragment()).get(FormsViewModel.class);
                    viewModel.selectRecipeSteps(finalSteps);
                }
            }
        });



        return f2;
    }
    //function to add onto list
    public static void addItem(String item){
        items.add(item);
        recyclerView.setAdapter(adapter); //update the listview
    }

    //function to remove from list
    public static void removeItem(int remove){
        items.remove(remove);
        recyclerView.setAdapter(adapter); //update the listview
    }
}