package sg.edu.np.madgroupyassignment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Top post RV Adapter for Home page
 */
public class HomeParentAdapter extends RecyclerView.Adapter<HomeParentViewHolder> {
    ArrayList<HomeParentData> data;
    Context c;
    Integer random;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    HomeMixData weeklyPost = new HomeMixData();

    public HomeParentAdapter(Context c, ArrayList<HomeParentData> data){
        this.data = data;
        this.c = c;
    }

    @Override
    //getting position of viewholder, if its 0, return 0 else return 1
    public int getItemViewType(int position) {
        return (position == 0)?0:1;
    }


    @NonNull
    @Override
    public HomeParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Setting layout file and doing rv of nested layout within parent RV if posiiton of viewholder == 0
        View item;
        HomeMix homeMix = new HomeMix();
        if (viewType == 0){
            //Inflating child layout within Parent RV
            item = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_home_child, null, false);

            // Making Child RV for child layout within parent RV
            ArrayList<HomeChildData> data = new ArrayList<>();
            ArrayList<HomeMixData> sortedMixList = homeMix.filterDT();
            ArrayList<HomeMixData> randomMixList = homeMix.RandomData();

            ImageView weekly_img = item.findViewById(R.id.weekly_img);
            TextView weekly_title = item.findViewById(R.id.weekly_title);
            TextView weekly_author = item.findViewById(R.id.weekly_author);


            // Getting Weekly Date and UId Stored in Firebase
            MainActivity mainActivity = new MainActivity();
            Date storedDate = mainActivity.storedDate;
            String storedUID = mainActivity.storedUID;
            // IF stored post disappears
            if (randomMixList.size() > 0){
                // If data gets deleted from firebase
                if (storedUID== null || storedDate == null){
                    final int random = new Random().nextInt(randomMixList.size());
                    weeklyPost = randomMixList.get(random);
                    homeMix.setWeekly(weeklyPost);
                    // Immediately setting a post as a weekly feature
                    storedDate = new Date(System.currentTimeMillis());
                    storedUID = weeklyPost.postID;

                }
                //Getting Current Date Week
                Date currentDate = new Date(System.currentTimeMillis());
                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.setMinimalDaysInFirstWeek(1);
                calendar.setTime(currentDate);
                int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);

                //Getting Stored Date Week
                calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.setMinimalDaysInFirstWeek(1);
                calendar.setTime(storedDate);
                int storedWeek = calendar.get(Calendar.WEEK_OF_YEAR);

                if (currentWeek != storedWeek){ // setting post and date if post has not been updated this week
                    // post to retrieve for weekly feature determines randomly
                    final int random = new Random().nextInt(randomMixList.size());
                    weeklyPost = randomMixList.get(random);
                    homeMix.setWeekly(weeklyPost);
                }
                else{ // getting post from databse, if this week has been updated
                    for(HomeMixData i : randomMixList){
                        if (i.postID.equals(storedUID)){ // getting the object that matches the ID
                            weeklyPost = i;
                            break;
                        }
                        else{
                            if (i == randomMixList.get(randomMixList.size()-1)){ // If its already the last item and doesnt match
                                final int random = new Random().nextInt(randomMixList.size());
                                weeklyPost = randomMixList.get(random);
                                homeMix.setWeekly(weeklyPost);
                            }
                        }
                    }
                }

                ArrayList<HomeMixData> weekly_list = new ArrayList<>();
                weekly_list.add(weeklyPost); // can be passed to bundle
                //Setting items of things in Weekly post using sortedMixList
                if (weeklyPost.identifier != null) {
                    if (weeklyPost.identifier == true) { // if its Hawker Corner post
                        weekly_title.setText(weeklyPost.hcstallname);
                        weekly_author.setText("By: "+weeklyPost.hcauthor);
                        Picasso.get().load(weeklyPost.hccoverimg).resize(400,400).into(weekly_img);
                    }
                    else{// if its Recipe Corner post
                        weekly_title.setText(weeklyPost.recipeName);
                        weekly_author.setText(weeklyPost.userName);
                        Picasso.get().load(weeklyPost.foodImage).into(weekly_img);
                    }
                }

                weekly_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        //Bundle to pass info to fragment as intent cannot
                        if (weeklyPost.identifier == true){ // If hawker post
                            Fragment chosenfragment = new HCChosenStall();
                            Bundle bundle = new Bundle();
                            bundle.putInt("stallposition", 0);
                            bundle.putInt("HomeDataCheck", 1); // if from Home, number = 1
                            bundle.putParcelable("list", Parcels.wrap(weekly_list));
                            chosenfragment.setArguments(bundle);

                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, chosenfragment).commit();
                        }
                        else{
                            Fragment chosenfragment = new RecipeCornerPosts();
                            Bundle bundle = new Bundle();
                            bundle.putInt("recipeNo", 0);
                            bundle.putInt("HomeDataCheck", 1); // if from Home, number = 1
                            bundle.putParcelable("list", Parcels.wrap(weekly_list));
                            chosenfragment.setArguments(bundle);
                            activity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.MainFragment, chosenfragment).addToBackStack(null).commit();
                        }
                    }
                });
            }

            if (sortedMixList.size() > 0){ // checking if list is not empty, app wont crash
                //Setting items of things in LP RV using sortedMixList
                for (int i = 0; i < sortedMixList.size(); i++) {
                    //Adding the data for every ViewHolder
                    HomeChildData d = new HomeChildData();
                    if (sortedMixList.get(i).identifier == true) { // if its Hawker Corner post
                        d.post_header = sortedMixList.get(i).hcstallname;
                        d.post_desc = sortedMixList.get(i).shortdesc;
                        d.post_author = sortedMixList.get(i).hcauthor;
                        data.add(d);
                    }
                    else{// if its Recipe Corner post
                        d.post_header = sortedMixList.get(i).recipeName;
                        d.post_desc = sortedMixList.get(i).recipeDescription;
                        d.post_author = sortedMixList.get(i).userName;
                        data.add(d);
                    }
                }
            }
            else{
                for (int i = 0; i < 5; i++) {
                    HomeChildData d = new HomeChildData();
                    d.post_header = "LP Title" + i;
                    d.post_desc = "LP Desc" + i;
//                    d.post_desc = mixData.get(i).shortdesc;
                    d.post_author = "LP Author" + i;
                    data.add(d);
                }

            }

            // Child RV
            RecyclerView lp_rv = item.findViewById(R.id.lp_rv);
            LinearLayoutManager lp_layout = new LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false);
            HomeChildAdapter lp_adapter = new HomeChildAdapter(c,data);

            lp_rv.setAdapter(lp_adapter);
            lp_rv.setLayoutManager(lp_layout);

            // Explore ImageView "Buttons"
            ImageView exploreHC = item.findViewById(R.id.home_hc_btn);
            ImageView exploreRC = item.findViewById(R.id.home_rc_btn);
            // BLACK Tint to Images
            exploreHC.setColorFilter(Color.argb(130, 0, 0, 0));
            exploreRC.setColorFilter(Color.argb(130, 0, 0, 0));
            //Rounding corners of ImageView buttons
            exploreHC.setClipToOutline(true);
            exploreRC.setClipToOutline(true);

            //Making my Explore page image views clickable
            exploreRC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*View view = MainActivity.bottomNavigationView.findViewById(R.id.rc);
                    view.performClick();*/
                    //MainActivity.bottomNavigationView.setSelectedItemId(R.id.rc);

                    AppCompatActivity activity = (AppCompatActivity) parent.getContext();
                    RecipeCornerMain rcmain = new RecipeCornerMain();
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.MainFragment, rcmain).addToBackStack(null).commit();
                    // making bottom nav bar appear checked
                    MainActivity.bottomNavigationView.getMenu().findItem(R.id.rc).setChecked(true);
                }
            });
            exploreHC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) parent.getContext();
                    HawkerCornerMain hcmain = new HawkerCornerMain();
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.MainFragment, hcmain).addToBackStack(null).commit();
                    // making bottom nav bar appear checked
                    MainActivity.bottomNavigationView.getMenu().findItem(R.id.hc).setChecked(true);
                }
            });


        }
        else{
            // If index != 0  means Parent RV layout file to be inflated
            item = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_home_feedposts, null, false);
        }

        return new HomeParentViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeParentViewHolder holder, int position) {
        //onBindViewHolder populates viewholder with data
        //position is which index in arraylist is coming up now and has to be populated

        //Position has to be > 0 cus when position == 0, it is not the RV layout data thus null error
        if (position != 0){
            // First view holder, where child layout is in
            // But now position is 1, our data has to start from 0 for parent RV data thus -1
            HomeParentData p = data.get(position-1);
            //HomeMix homeMix = new HomeMix();
            ArrayList<HomeMixData> randomMixList = MainActivity.randomMixList; // List with Random Mixed HC and RC info
            if (randomMixList.size() > 0){
                HomeMixData randomMixListItem = randomMixList.get(holder.getAdapterPosition()-1);
                if (randomMixList.get(holder.getAdapterPosition()-1).identifier == true) {// If hawker post
                    Picasso.get().load(randomMixListItem.hccoverimg).into(holder.post_img);
                    // Use Line colour to shows diff between RC and HC
                    holder.post_line.setBackground(new ColorDrawable(Color.parseColor("#F6412D"))); // MAIN RED
                }
                else{ //If recipe post
                    Picasso.get().load(randomMixListItem.foodImage).into(holder.post_img);
                    // Use Line colour to shows diff between RC and HC
                    holder.post_line.setBackground(new ColorDrawable(Color.parseColor("#FFC100"))); // ACCENT YELLOW
                }
            }



            holder.post_header.setText(p.post_header);
            holder.post_desc.setText(p.post_desc);
            holder.post_author.setText("By: "+p.post_author);
            holder.post_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Creating activity context to for the view, starting new fragment when view is clicked
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    //Bundle to pass info to fragment as intent cannot
                    if (randomMixList.get((holder.getAdapterPosition())-1).identifier == true){ // If hawker post
                        Fragment chosenfragment = new HCChosenStall();
                        Bundle bundle = new Bundle();
                        bundle.putInt("stallposition", holder.getAdapterPosition() -1 );
                        bundle.putInt("HomeDataCheck", 1); // if from Home, number = 1
                        bundle.putParcelable("list", Parcels.wrap(randomMixList));
                        chosenfragment.setArguments(bundle);

                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, chosenfragment).commit();
                    }
                    else{ // If Recipe Post
                        Fragment rcpFragment = new RecipeCornerPosts();
                        Bundle bundle = new Bundle();
                        bundle.putInt("recipeNo", holder.getAdapterPosition() - 1);
                        bundle.putInt("HomeDataCheck", 1); // if from Home, number = 1
                        bundle.putParcelable("list", Parcels.wrap(randomMixList));
                        rcpFragment.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.MainFragment, rcpFragment).addToBackStack(null).commit();
                    }
                }
            });

        }

    }
    @Override
    public int getItemCount() {
        // return .size() +1 cus we used one viewholder in parent RV to put in child layout.
        // but we still want 10 latest posts therefore we need get this list to only stop when its +1 in size to make up for used VH
        return data.size()+1 ;
    }
}
