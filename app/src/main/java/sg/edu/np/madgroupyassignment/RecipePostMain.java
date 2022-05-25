package sg.edu.np.madgroupyassignment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;


public class RecipePostMain extends Fragment {

    NumberPicker np1;
    NumberPicker np2;
    int hrs;
    int min;

    public RecipePostMain() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View f1 = inflater.inflate(R.layout.fragment_recipe_post_main, container, false);
        np1 = f1.findViewById(R.id.numberPicker);
        np2 = f1.findViewById(R.id.numberPicker2);

        np1.setMinValue(0);
        np1.setMaxValue(99);

        np2.setMinValue(0);
        np2.setMaxValue(59);

        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                hrs = i1;
            }
        });

        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                min = i1;
            }
        });


        return f1;
    }
}