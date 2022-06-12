package sg.edu.np.madgroupyassignment;

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

    public static HawkerCornerStalls getRecentHawkerPosts(int i) {
        return recentHawkerPosts[i];
    }

    public static void setRecentHawkerPosts(HawkerCornerStalls[] recentHawkerPosts) {
        PostsHolder.recentHawkerPosts = recentHawkerPosts;
    }

    public static RecipeCorner getRecentRecipePosts(int i) {
        return recentRecipePosts[i];
    }

    public static void setRecentRecipePosts(RecipeCorner[] recentRecipePosts) {
        PostsHolder.recentRecipePosts = recentRecipePosts;
    }

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

        for (int i = 0 ; i < recentHawkerPosts.length ; i++) {
            if(recentHawkerPosts[i] == null || recentHawkerPosts[i].postTimeStamp < hawkerCornerStalls.postTimeStamp) {
                recentHawkerPosts[i] = hawkerCornerStalls;
                break;
            }
        }
    }

    public void updateRecentRecipePosts(RecipeCorner recipeCorner) {
        Arrays.sort(recentRecipePosts, new Comparator<RecipeCorner>() {
            @Override
            public int compare(RecipeCorner stall1, RecipeCorner stall2) {
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

        for (int i = 0 ; i < recentRecipePosts.length ; i++) {
            if(recentRecipePosts[i] == null || recentRecipePosts[i].postTimeStamp < recipeCorner.postTimeStamp) {
                recentRecipePosts[i] = recipeCorner;
                break;
            }
        }

    }

    public int[] getLatestPosts() {
        int[] typeList = new int[5];
        for (int i = 0 ; i < RECENT_POST_NUMBER; i++){
            int randomPostType = ThreadLocalRandom.current().nextInt(0, 2);
            typeList[i] = randomPostType;
        }
        return typeList;
    }

}
