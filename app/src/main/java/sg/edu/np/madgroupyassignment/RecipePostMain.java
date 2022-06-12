package sg.edu.np.madgroupyassignment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.StringBufferInputStream;
import java.text.Normalizer;


public class RecipePostMain extends Fragment {

    EditText durInput;
    static String min;

    EditText titleInput;
    static String title;

    EditText descInput;
    static String desc;

    private FormsViewModel viewModel;

    String test;

    public RecipePostMain() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View f1 = inflater.inflate(R.layout.fragment_recipe_post_main, container, false);
        durInput = f1.findViewById(R.id.minuteInput);
        titleInput = f1.findViewById(R.id.RecipeTitle);
        descInput = f1.findViewById(R.id.Desc);
        viewModel = new ViewModelProvider(requireParentFragment()).get(FormsViewModel.class);


        //TitleInput Data Transfer
        titleInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                title = titleInput.getText().toString(); //NAME parameter

            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.selectRecipeName(title);
            }
        });

        //DescInput Data Transfer
        descInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                desc = descInput.getText().toString(); //DESC parameter

            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.selectRecipeDesc(desc);
            }
        });

        //Duration Input Data Transfer
        durInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                min = durInput.getText().toString(); //DURATION parameter

            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.selectRecipeDuration(min);
            }
        });



        return f1;
    }
}