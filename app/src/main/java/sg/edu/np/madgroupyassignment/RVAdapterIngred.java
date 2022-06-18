package sg.edu.np.madgroupyassignment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class RVAdapterIngred extends RecyclerView.Adapter<RVAdapterIngred.ViewHolder> {
    ArrayList<Ingredient> list;
    Context context;
    ViewModelStoreOwner VMSO;
    String totalIngred;
    FormsViewModel viewModel;
    String ingredString;
    ArrayList<String> totalIngredList;

    public RVAdapterIngred(Context context, ArrayList<Ingredient> ingreds, ViewModelStoreOwner vmso){
        this.context = context;
        list = ingreds;
        VMSO = vmso;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_row,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView ingredName = holder.ingredName;
        ingredName.setText(list.get(holder.getAdapterPosition()).name);

        TextView qty = holder.quantity;
        qty.setText(""+list.get(holder.getAdapterPosition()).qty);

        TextView unit = holder.unit;
        unit.setText(list.get(holder.getAdapterPosition()).unit);

        ImageView remove = holder.imageView;
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalIngredList = new ArrayList<>();
                totalIngred = "";
                RecipePostIngredients.removeItem(holder.getAdapterPosition());//allow remove of ingredient using imageview
                for (int i=0; i<list.size(); i++){
                    if (list.get(i).unit == "" || list.get(i).unit == null || list.get(i).unit.length() == 0){
                        ingredString = list.get(i).qty + " " + list.get(i).name;
                    }
                    else{
                        ingredString = list.get(i).qty + " " + list.get(i).unit + " " + list.get(i).name;
                    }
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

                viewModel = new ViewModelProvider(VMSO).get(FormsViewModel.class);
                viewModel.selectRecipeIngred(totalIngred);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size()  ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView ingredName;
        TextView quantity;
        TextView unit;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.remove2);
            ingredName = itemView.findViewById(R.id.ingredName);
            quantity = itemView.findViewById(R.id.quantity);
            unit = itemView.findViewById(R.id.unit);

        }
    }
}
