package sg.edu.np.madgroupyassignment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class RecipeDraftsPage extends Fragment {

    RecyclerView recipeDraftRV;
    OnBackPressedCallback callback;

    public RecipeDraftsPage() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View recipeDraftPage = inflater.inflate(R.layout.fragment_recipe_drafts_page, container, false);
        recipeDraftRV = recipeDraftPage.findViewById(R.id.recipeDraftRV);
        recipeDraftRV.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recipeDraftRV.setAdapter(new RecipeDraftsPage.RecipeDraftsAdapter());
        return recipeDraftPage;
    }

    class RecipeDraftsAddViewHolder extends RecyclerView.ViewHolder{
        public RecipeDraftsAddViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class RecipeDraftsViewHolder extends RecyclerView.ViewHolder{
        public RecipeDraftsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class RecipeDraftsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == 0){
                View addDraftView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_draft, parent, false);
                return new RecipeDraftsPage.RecipeDraftsAddViewHolder(addDraftView);
            }
            else{
                View draftView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drafts, parent, false);
                return new RecipeDraftsPage.RecipeDraftsViewHolder(draftView);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (this.getItemViewType(position) == 0){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (MainActivity.checkFormsNum == 1){
                            MainActivity.checkFormsNum = 0; //changes to 0 when click into forms
                            MainActivity.whichForm = 2;
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            activity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.MainFragment, MainActivity.recipeForm).addToBackStack(null).commit();
                            MainActivity.mainFAB.hide();
                            MainActivity.rcFAB.hide();
                            MainActivity.hcFAB.hide();
                            MainActivity.rcFABText.setVisibility(View.GONE);
                            MainActivity.hcFABText.setVisibility(View.GONE);
                        }
                    }
                });
            }
            else{
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "rest", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }

    public void onResume() {
        //Alert shows up when back button is pressed.
        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getParentFragmentManager().popBackStack();
                MainActivity.mainFAB.show();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
        super.onResume();
    }

    @Override
    public void onPause() {
        callback.setEnabled(false);
        super.onPause();
    }
}