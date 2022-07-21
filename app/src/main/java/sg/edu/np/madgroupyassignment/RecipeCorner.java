package sg.edu.np.madgroupyassignment;

import org.parceler.Parcel;

@Parcel
public class RecipeCorner {
    //variables for recipe corner class
    String owner;
    String recipeName;
    String recipeDescription;
    Integer recipeRating;
    String userName;
    String duration;
    String steps;
    String ingredients;
    Long postTimeStamp;
    String foodImage;
    String postID;

    public Boolean isChecked;          // Checks checkbox

    //Empty constructor
    public RecipeCorner() {

    }

    // creating constructor for our variables.
    public RecipeCorner(String postID, String owner, String recipeName, String recipeDescription, int recipeRating,
                        String userName, String duration,
                        String steps, String ingredients, Long postTimeStamp, String foodImage, Boolean isChecked) {
        this.postID = postID;                       //postID for database use
        this.owner = owner;                         //post owner for rc form and profile
        this.recipeName = recipeName;               //recipe name
        this.recipeDescription = recipeDescription; //recipe description
        this.recipeRating = recipeRating;           //recipe difficulty level displayed in the form of ratings
        this.userName = userName;                   //user's username
        this.duration = duration;                   //duration to make the recipe
        this.steps = steps;                         //steps of the recipe
        this.ingredients = ingredients;             //ingredients of the recipe
        this.postTimeStamp = postTimeStamp;         //time stamp for home page
        this.foodImage = foodImage;                 //image of the recipe displayed

        this.isChecked = isChecked; // for Checkbox
    }

    // creating getter and setter methods.
    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public Integer getRecipeRating() {
        return recipeRating;
    }

    public void setRecipeRating(int recipeRating) {
        this.recipeRating = recipeRating;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public Long getPostTimeStamp() {
        return postTimeStamp;
    }

    public void setPostTimeStamp(Long postTimeStamp) {
        this.postTimeStamp = postTimeStamp;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }
}
