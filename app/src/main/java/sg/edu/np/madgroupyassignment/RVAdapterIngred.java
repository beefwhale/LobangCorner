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

import java.util.ArrayList;
import java.util.HashMap;

public class RVAdapterIngred extends RecyclerView.Adapter<RVAdapterIngred.ViewHolder> {
    ArrayList<Ingredient> list;
    Context context;
    ViewModelStoreOwner VMSO;
    HashMap<String, Object> totalIngred;
    FormsViewModel viewModel;

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
                RecipePostIngredients.removeItem(holder.getAdapterPosition());//allow remove of ingredient using imageview
                totalIngred = new HashMap<String, Object>(); //INGREDIENT parameter
                for (int i=0; i<list.size(); i++){
                    String iName = list.get(i).name;
                    String qtyAndUnit =list.get(i).qty + " " + list.get(i).unit;
                    totalIngred.put(iName, qtyAndUnit);
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
