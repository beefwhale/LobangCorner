package sg.edu.np.madgroupyassignment;

import java.util.ArrayList;

public class PostsHolder {
    private static ArrayList<HawkerCornerStalls> hawkerPosts;
    private static ArrayList<RecipeCorner>  recipePosts;

    private static ArrayList<HawkerCornerStalls> userHawkerPosts;
    private static ArrayList<RecipeCorner> userRecipePosts;

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
}
