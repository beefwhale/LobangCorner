package sg.edu.np.madgroupyassignment;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

public class PostsHolder {
    private static ArrayList<HawkerCornerStalls> hawkerPosts;
    private static ArrayList<RecipeCorner>  recipePosts;

    private static ArrayList<HawkerCornerStalls> userHawkerPosts;
    private static ArrayList<RecipeCorner> userRecipePosts;

    private static final int RECENT_POST_NUMBER = 5;
    private static HawkerCornerStalls[] recentHawkerPosts = new HawkerCornerStalls[5];
    private static RecipeCorner[] recentRecipePosts = new RecipeCorner[5];

    public PostsHolder() {
        this.hawkerPosts = new ArrayList<>();
        this.recipePosts = new ArrayList<>();
        this.userHawkerPosts = new ArrayList<>();
        this.userRecipePosts = new ArrayList<>();

    }

    public static ArrayList<HawkerCornerStalls> getHawkerPosts() {
        return hawkerPosts;
    }

    public static void setHawkerPosts(HawkerCornerStalls hawkerPosts) {
        PostsHolder.hawkerPosts.add(hawkerPosts);
    }
    public static ArrayList<RecipeCorner> getRecipePosts() {
        return recipePosts;
    }

    public static void setRecipePosts(RecipeCorner recipePosts) {
        PostsHolder.recipePosts.add(recipePosts);
    }

    public static ArrayList<HawkerCornerStalls> getUserHawkerPosts() {
        return userHawkerPosts;
    }

    public static void setUserHawkerPosts(HawkerCornerStalls userHakerPosts) {
        PostsHolder.userHawkerPosts.add(userHakerPosts);
    }

    public static ArrayList<RecipeCorner> getUserRecipePosts() {
        return userRecipePosts;
    }

    public static void setUserRecipePosts(RecipeCorner userRecipePosts) {
        PostsHolder.userRecipePosts.add(userRecipePosts);
    }

    public static HawkerCornerStalls[] getRecentHawkerPosts() {
        return recentHawkerPosts;
    }

    public static void setRecentHawkerPosts(HawkerCornerStalls[] recentHawkerPosts) {
        PostsHolder.recentHawkerPosts = recentHawkerPosts;
    }

    public static RecipeCorner[] getRecentRecipePosts() {
        return recentRecipePosts;
    }

    public static void setRecentRecipePosts(RecipeCorner[] recentRecipePosts) {
        PostsHolder.recentRecipePosts = recentRecipePosts;
    }

    public static HawkerCornerStalls[] getTest(){
        return recentHawkerPosts;}

    public void removeHawkerPosts(){
        this.hawkerPosts.removeAll(hawkerPosts);
    }

    public void removeRecipePosts(){
        this.recipePosts.removeAll(recipePosts);
    }

    public void removeUserHawkerPosts(){
        this.userHawkerPosts.removeAll(userHawkerPosts);
    }

    public void removeUserRecipePosts(){
        this.userRecipePosts.removeAll(userRecipePosts);
    }

    public void updateRecentHawkerPosts(HawkerCornerStalls hawkerCornerStalls) {
        sortEntries();

        for (int i = 0 ; i < recentHawkerPosts.length ; i++) {
            if(recentHawkerPosts[i] == null || recentHawkerPosts[i].postTimeStamp < hawkerCornerStalls.postTimeStamp) {
                recentHawkerPosts[i] = hawkerCornerStalls;
                break;
            }
        }
        sortEntries();
    }

    public void updateRecentRecipePosts(RecipeCorner recipeCorner) {
        sortEntries();

        for (int i = 0 ; i < recentRecipePosts.length ; i++) {
            if(recentRecipePosts[i] == null || recentRecipePosts[i].postTimeStamp < recipeCorner.postTimeStamp) {
                recentRecipePosts[i] = recipeCorner;
                break;
            }
        }
        sortEntries();
    }

    private void sortEntries(){
        Arrays.sort(recentHawkerPosts, new Comparator<HawkerCornerStalls>() {
            @Override
            public int compare(HawkerCornerStalls stall1, HawkerCornerStalls stall2) {
                if (stall1 == null && stall2 == null){
                    return 0;
                }

                if (stall1 == null) {
                    return -1;
                }

                if (stall2 == null) {
                    return 1;
                }
                return (int)(stall1.postTimeStamp - stall2.postTimeStamp);
            }
        });
    }


    //Not used
    public static int[] getLatestPostsType() {
        int[] typeList = new int[5];

        int numberOfRecentRecipe = 0;
        int numberOfRecentHawker = 0;
        for (int i = 0 ; i < RECENT_POST_NUMBER; i++){
            if (recentRecipePosts[i] != null){
                ++numberOfRecentRecipe;
            }
            if (recentHawkerPosts[i] != null){
                ++numberOfRecentHawker;
            }
        }

        int numberOfRecipe = 0;
        int numberOfHawker = 0;

        int bottomBoundsForRecipe = 1;
        int upperBoundsForHawker = 3;

        for (int i = 0 ; i < RECENT_POST_NUMBER; i++){
            int randomPostType = ThreadLocalRandom.current().nextInt(bottomBoundsForRecipe, upperBoundsForHawker);
            if (randomPostType == 1){
                numberOfRecipe++;
            }else{
                numberOfHawker++;
            }

            if (numberOfRecipe > numberOfRecentRecipe && randomPostType == 1){
                randomPostType = 2;
                typeList[i] = randomPostType;
                numberOfRecipe--;
                numberOfHawker++;
            }

            if (numberOfHawker > numberOfRecentHawker && randomPostType == 2){
                randomPostType = 1;
                typeList[i] = randomPostType;
                numberOfHawker--;
                numberOfRecipe++;
            }

            typeList[i] = randomPostType;

            if (numberOfRecipe >= numberOfRecentRecipe && numberOfHawker >= numberOfRecentHawker) {
                break;
            }
        }

        return typeList;
    }

}
