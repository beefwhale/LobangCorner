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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAdapterSteps extends RecyclerView.Adapter<RVAdapterSteps.ViewHolder>{
    ArrayList<String> list;
    Context context;

    public RVAdapterSteps(Context context, ArrayList<String> items){
        this.context = context;
        list = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_list_row,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView number =holder.stepNo;
        number.setText("Step "+ (holder.getAdapterPosition()+1)); //set text in custom layout to according to position in list

        TextView name = holder.stepName;
        name.setText(list.get(holder.getAdapterPosition()));//set text in custom layout to name

        ImageView remove = holder.imageView;
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipePostSteps.removeItem(holder.getAdapterPosition());//allow remove of step using imageview
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView stepNo;
        TextView stepName;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.remove);
            stepName = itemView.findViewById(R.id.name);
            stepNo = itemView.findViewById(R.id.number);

        }
    }
}
