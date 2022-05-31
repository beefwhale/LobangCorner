package sg.edu.np.madgroupyassignment;

import java.util.HashMap;

public class RecipeCorner {
    // variables for recipe corner class
    String recipeName;
    String recipeDescription;
    Integer recipeRating;
    Integer noOfRaters;
    String userName;
    String duration;
    String steps;
    HashMap<String, Object> ingredients;
    String postTimeStamp;


    //Empty constructor
    public RecipeCorner(){

    }

    // creating constructor for our variables.
    public RecipeCorner(String recipeName, String recipeDescription, int recipeRating,
                       int noOfRaters, String userName, String duration,
                         String steps/*, HashMap<String, Object> ingredients, String postTimeStamp*/) {
        this.recipeName = recipeName;
        this.recipeDescription = recipeDescription;
        this.recipeRating = recipeRating;
        this.noOfRaters = noOfRaters;
        this.userName = userName;
        this.duration = duration;
        this.steps = steps;
        //this.ingredients = ingredients;
        //this.postTimeStamp = postTimeStamp;
    }



    // creating getter and setter methods.
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

    public Integer getRecipeRating(){
        return recipeRating;
    }

    public void setRecipeRating(int recipeRating){
        this.recipeRating = recipeRating;
    }

    public Integer getNoOfRaters(){
        return this.noOfRaters;
    }

    public void setNoOfRaters(int noOfRaters){
        this.noOfRaters = noOfRaters;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getDuration(){return duration;}

    public void setDuration(String duration){this.duration = duration; }

    /*public HashMap<String, Object> getIngredients(){return ingredients; }

    public void setIngredients(HashMap<String, Object> ingredients){this.ingredients = ingredients; } */

    public String getSteps(){return steps; }

    public void setSteps(String steps){this.steps = steps; }

    public String getPostTimeStamp() {
        return postTimeStamp;
    }

    public void setPostTimeStamp(String postTimeStamp) {
        this.postTimeStamp = postTimeStamp;
    }
}
