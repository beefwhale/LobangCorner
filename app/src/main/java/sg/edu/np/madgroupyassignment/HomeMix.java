package sg.edu.np.madgroupyassignment;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;

public class HomeMix {
    PostsHolder postHolder;
    Context c;

    public void HomeMixData(Context c){
        this.c=c;
    }

    // getting filtered list
    ArrayList<HomeMixData> mixData = new ArrayList<>();
    ArrayList<HomeMixData> sortedData = new ArrayList<>();


    public ArrayList<HomeMixData> filterDT(){
        HawkerCornerStalls[] recentHCArray = postHolder.getRecentHawkerPosts();
        RecipeCorner[] recentRCArray = postHolder.getRecentRecipePosts();
        // Making new Mixed List
        for (HawkerCornerStalls i : recentHCArray) {
            HomeMixData HomeMixData = new HomeMixData();
            Log.e("hc length", ""+recentHCArray.length);
            if (i != null){
                Log.e("hc", "not null "+i.hcstallname);
                HomeMixData.hcstallname = i.hcstallname;
                HomeMixData.hcOwner = i.hcOwner;
                HomeMixData.hcauthor = i.hcauthor;
                HomeMixData.hccuserpfp = i.hccuserpfp;
                HomeMixData.hccliked = i.hccliked;
                HomeMixData.hccparagraph = i.hccparagraph;
                HomeMixData.hccaddress = i.hccaddress;
                HomeMixData.daysopen = i.daysopen;
                HomeMixData.hoursopen = i.hoursopen;
                HomeMixData.postTimeStamp = i.postTimeStamp;
                HomeMixData.identifier = true; // Identify its a hc post
                HomeMixData.shortdesc = i.shortdesc;
                mixData.add(HomeMixData);
            }
            else{
                Log.e("hc null checker", "null");
            }
        }
        for (RecipeCorner i : recentRCArray) {
            HomeMixData HomeMixData = new HomeMixData();
            Log.e("rc length", ""+ recentRCArray.length);
            if (i != null){
                Log.e("rc", "not null "+i.owner);
                HomeMixData.owner = i.owner;
                HomeMixData.recipeName = i.recipeName;
                HomeMixData.recipeDescription = i.recipeDescription;
                HomeMixData.recipeRating = i.recipeRating;
                HomeMixData.noOfRaters = i.noOfRaters;
                HomeMixData.userName = i.userName;
                HomeMixData.duration = i.duration;
                HomeMixData.steps = i.steps;
                HomeMixData.ingredients = i.ingredients;
                HomeMixData.foodImage = i.foodImage;
                HomeMixData.postTimeStamp = i.postTimeStamp;
                HomeMixData.identifier = false; // Identify its a rc post
                mixData.add(HomeMixData);
            }
            else{
                Log.e("rc null checker", "null");
            }
        }
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
            Log.e("sort test", ""+sortedData.size());
        }
        return sortedData;

    }
}
