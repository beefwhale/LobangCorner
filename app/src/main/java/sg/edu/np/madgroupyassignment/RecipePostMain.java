package sg.edu.np.madgroupyassignment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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


public class RecipePostMain extends Fragment {

    NumberPicker numPicker;
    static int min;

    EditText titleInput;
    static String title;

    EditText descInput;
    static String desc;

    Button submitButton;

    public RecipePostMain() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View f1 = inflater.inflate(R.layout.fragment_recipe_post_main, container, false);
        numPicker = f1.findViewById(R.id.numberPicker);
        titleInput = f1.findViewById(R.id.RecipeTitle);
        descInput = f1.findViewById(R.id.Desc);


        numPicker.setMinValue(0);
        numPicker.setMaxValue(1440);


        numPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                min = i1; //DURATION value
            }
        });

        title = titleInput.getText().toString(); //NAME parameter
        desc = descInput.getText().toString(); //DESCRIPTION parameter
//        Bundle bundle = this.getArguments();
//        String test = (String) bundle.getString("test");
//
//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), test, Toast.LENGTH_SHORT).show();
//            }
//        });







        return f1;
    }
}