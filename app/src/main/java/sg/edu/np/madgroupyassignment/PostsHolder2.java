package sg.edu.np.madgroupyassignment;

import java.util.ArrayList;

//postholder for bookmarked posts
public class PostsHolder2 {
    public static ArrayList<HawkerCornerStalls> hawkerPosts;
    public static ArrayList<RecipeCorner> recipePosts;

    public PostsHolder2() {
        this.hawkerPosts = new ArrayList<>();
        this.recipePosts = new ArrayList<>();
    }

    //getters setters for bookmarked hawker and recipe posts
    public static ArrayList<HawkerCornerStalls> getHawkerPosts() {
        return hawkerPosts;
    }

    public static void setHawkerposts(HawkerCornerStalls hawkerPosts) {
        PostsHolder2.hawkerPosts.add(hawkerPosts);
    }

    public static ArrayList<RecipeCorner> getRecipePosts() {
        return recipePosts;
    }

    public static void setRecipePosts(RecipeCorner recipePosts) {
        PostsHolder2.recipePosts.add(recipePosts);
    }

    //methods remove the bookmarked hawker and recipe posts
    public void removeHawkerPosts() {
        this.hawkerPosts.removeAll(hawkerPosts);
    }

    public void removeRecipePosts() {
        this.recipePosts.removeAll(recipePosts);
    }
}

