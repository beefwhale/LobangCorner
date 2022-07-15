package sg.edu.np.madgroupyassignment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class HawkerDraftsPage extends Fragment {

    RecyclerView hawkerDraftRV;
    OnBackPressedCallback callback;
    PostsHolder postsHolder;

    public HawkerDraftsPage() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View hawkerDraftPage = inflater.inflate(R.layout.fragment_hawker_drafts_page, container, false);
        hawkerDraftRV = hawkerDraftPage.findViewById(R.id.hawkerDraftRV);
        hawkerDraftRV.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        hawkerDraftRV.setAdapter(new HawkerDraftsAdapter());
        return hawkerDraftPage;
    }

    class HawkerDraftsAddViewHolder extends RecyclerView.ViewHolder{
        public HawkerDraftsAddViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class HawkerDraftsViewHolder extends RecyclerView.ViewHolder{
        public HawkerDraftsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class HawkerDraftsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == 0){
                View addDraftView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_draft, parent, false);
                return new HawkerDraftsPage.HawkerDraftsAddViewHolder(addDraftView);
            }
            else{
                View draftView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drafts, parent, false);
                return new HawkerDraftsPage.HawkerDraftsViewHolder(draftView);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            HawkerForm hawkerForm = new HawkerForm(0); //Posting = 0
            if (this.getItemViewType(position) == 0){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (MainActivity.checkFormsNum == 1){
                            MainActivity.checkFormsNum = 0; //changes to 0 when click into forms
                            MainActivity.whichForm = 1;
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            activity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.MainFragment, hawkerForm).addToBackStack(null).commit();
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
            return (1+postsHolder.getHawkerDrafts().size());
        }
    }

    @Override
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