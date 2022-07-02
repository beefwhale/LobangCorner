package sg.edu.np.madgroupyassignment;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;


public class HomeMix {
    PostsHolder postHolder;
    Context c;
    Integer shuffle;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String storedUID;
    Date storedDate;
    private DatabaseReference databaseReferencetest;
    public void HomeMixData(Context c){
        this.c=c;
    }

    // getting filtered list
    ArrayList<HomeMixData> mixData = new ArrayList<>();
    ArrayList<HomeMixData> sortedData = new ArrayList<>();

    // Method to Mix HC and RC data only for Latest Post RV (filterDT method)
    public ArrayList<HomeMixData> setMixData(HawkerCornerStalls[] recentHCArray, RecipeCorner[] recentRCArray){
        mixData = new ArrayList<>(); // resetting the mixed list just in case
        // Making new Mixed List
        for (HawkerCornerStalls i : recentHCArray) {
            HomeMixData HomeMixData = new HomeMixData();
            if (i != null){
                HomeMixData.hcstallname = i.hcstallname;
                HomeMixData.hcOwner = i.hcOwner;
                HomeMixData.hcauthor = i.hcauthor;
                HomeMixData.hccuserpfp = i.hccuserpfp;
                HomeMixData.hccparagraph = i.hccparagraph;
                HomeMixData.hccaddress = i.hccaddress;
                HomeMixData.daysopen = i.daysopen;
                HomeMixData.hoursopen = i.hoursopen;
                HomeMixData.postTimeStamp = i.postTimeStamp;
                HomeMixData.identifier = true; // Identify its a hc post
                HomeMixData.shortdesc = i.shortdesc;
                HomeMixData.hccoverimg= i.hccoverimg;
                HomeMixData.postID = i.postid;
                mixData.add(HomeMixData);
            }
        }
        for (RecipeCorner i : recentRCArray) {
            HomeMixData HomeMixData = new HomeMixData();
            if (i != null){
                HomeMixData.owner = i.owner;
                HomeMixData.recipeName = i.recipeName;
                HomeMixData.recipeDescription = i.recipeDescription;
                HomeMixData.recipeRating = i.recipeRating;
                HomeMixData.userName = i.userName;
                HomeMixData.duration = i.duration;
                HomeMixData.steps = i.steps;
                HomeMixData.ingredients = i.ingredients;
                HomeMixData.foodImage = i.foodImage;
                HomeMixData.postTimeStamp = i.postTimeStamp;
                HomeMixData.identifier = false; // Identify its a rc post
                HomeMixData.postID = i.postID;
                mixData.add(HomeMixData);
            }
        }

        return mixData;
    }
    public ArrayList<HomeMixData> filterDT(){
        HawkerCornerStalls[] recentHCArray = postHolder.getRecentHawkerPosts();
        RecipeCorner[] recentRCArray = postHolder.getRecentRecipePosts();

        // Getting mixed Data List
        mixData = setMixData(recentHCArray, recentRCArray);
        // Sorting the List according to Date Time
        if (mixData.size() > 0){
            while (mixData.size() != 0){
                long timestamp = mixData.get(0).postTimeStamp;
                HomeMixData smallestData = mixData.get(0);
                for(HomeMixData i : mixData){
                    // finding smaller date time
                    if (i.postTimeStamp >= timestamp){
                        timestamp = i.postTimeStamp;
                        smallestData = i;
                    }
                }
                // putting object into sorted list
                sortedData.add(smallestData);
                mixData.remove(smallestData);
            }
        }
        return sortedData;

    }

    // Method to Mix HC and RC data only for Discover More Post RV (RandomData method)
    public ArrayList<HomeMixData> setMixData2(ArrayList<HawkerCornerStalls> recentHCArray, ArrayList<RecipeCorner> recentRCArray){
        mixData = new ArrayList<>(); // resetting the list just incase
        // Making new Mixed List
        for (HawkerCornerStalls i : recentHCArray) {
            HomeMixData HomeMixData = new HomeMixData();
            if (i != null){
                HomeMixData.hcstallname = i.hcstallname;
                HomeMixData.hcOwner = i.hcOwner;
                HomeMixData.hcauthor = i.hcauthor;
                HomeMixData.hccuserpfp = i.hccuserpfp;
                HomeMixData.hccparagraph = i.hccparagraph;
                HomeMixData.hccaddress = i.hccaddress;
                HomeMixData.daysopen = i.daysopen;
                HomeMixData.hoursopen = i.hoursopen;
                HomeMixData.postTimeStamp = i.postTimeStamp;
                HomeMixData.identifier = true; // Identify its a hc post
                HomeMixData.shortdesc = i.shortdesc;
                HomeMixData.hccoverimg = i.hccoverimg;
                HomeMixData.postID = i.postid;
                mixData.add(HomeMixData);
            }
        }
        for (RecipeCorner i : recentRCArray) {
            HomeMixData HomeMixData = new HomeMixData();
            if (i != null){
                HomeMixData.owner = i.owner;
                HomeMixData.recipeName = i.recipeName;
                HomeMixData.recipeDescription = i.recipeDescription;
                HomeMixData.recipeRating = i.recipeRating;
                HomeMixData.userName = i.userName;
                HomeMixData.duration = i.duration;
                HomeMixData.steps = i.steps;
                HomeMixData.ingredients = i.ingredients;
                HomeMixData.foodImage = i.foodImage;
                HomeMixData.postTimeStamp = i.postTimeStamp;
                HomeMixData.identifier = false; // Identify its a rc post
                HomeMixData.postID = i.postID;
                mixData.add(HomeMixData);
            }
        }

        return mixData;
    }
    //Mixing Random Data in from HC and RC
    public ArrayList<HomeMixData> RandomData(){
        // Getting all existing Hawker Corner Posts
        ArrayList<HawkerCornerStalls> stallsList = new ArrayList<>();
        for (HawkerCornerStalls obj : postHolder.getHawkerPosts()){
            stallsList.add(obj);
        }

        // Getting all existing Recipe Corner Posts
        ArrayList<RecipeCorner> recipeModalArrayList = new ArrayList<>();
        for (RecipeCorner obj : postHolder.getRecipePosts()) {
            recipeModalArrayList.add(obj);
        }
        mixData = setMixData2(stallsList, recipeModalArrayList);
//        Collections.shuffle(mixData);
//        Log.e("Shuffle", mixData.size()+"");
        return mixData;
    }

    // Used to set Permanent Date in Firebase as 13 June 2022 (Monday)
    public void setWeekly(HomeMixData data) {
        Long weeklyDate = System.currentTimeMillis();
        String weeklyPostUID = data.postID;

        // Put object into Firebase
        databaseReferencetest = FirebaseDatabase.getInstance().getReference();
        databaseReferencetest.child("WeeklyDate").setValue(weeklyDate);
        databaseReferencetest.child("WeeklyPost").setValue(weeklyPostUID);
    }

}
