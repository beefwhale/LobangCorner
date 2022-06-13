package sg.edu.np.madgroupyassignment;

public class HomeMixData {
    public long postTimeStamp;
    public Boolean identifier; // Identifier for HC (true) and RC (false) and

    //HC values
    public String hcstallname;      //stallname of hawker
    public String hcOwner;          //owner of post
    public String hcauthor;         //author who made hawker post
    public String hccuserpfp;        //user profile pic who made the post
    public Boolean hccliked;          //liked on the post
    public String hccparagraph;    //description of stall
    public String hccaddress;     //Address of hawker stall
    public String daysopen;      //How to make it so that user can enter different days and appear in two records
    public String hoursopen;    //Same as ^
    public String shortdesc;

    // RC VALUES
    String owner;
    String recipeName;
    String recipeDescription;
    Integer recipeRating;
    Integer noOfRaters;
    String userName;
    String duration;
    String steps;
    String ingredients;
    String foodImage;

//    public HomeMixData(){
//
//    }
    public HomeMixData(){
        this.identifier = identifier;
//        HC SIDE
        //this.hccoverimg = hccoverimg;
        this.hcOwner = hcOwner;
        this.hcstallname = hcstallname;
        this.hcauthor = hcauthor;
        /*this.hccliked = hccliked;*/
        this.hccparagraph = hccparagraph;
        this.hccaddress = hccaddress;
        this.daysopen = daysopen;
        this.hoursopen = hoursopen;
        this.hccuserpfp = hccuserpfp;
        /*this.shortdesc = shortdesc;*/
        this.postTimeStamp = postTimeStamp;

//        RC SIDE
        this.owner = owner;
        this.recipeName = recipeName;
        this.recipeDescription = recipeDescription;
        this.recipeRating = recipeRating;
        this.noOfRaters = noOfRaters;
        this.userName = userName;
        this.duration = duration;
        this.steps = steps;
        this.ingredients = ingredients;
        this.postTimeStamp = postTimeStamp;
        this.foodImage = foodImage;

    }
    public long getPostTimeStamp() {
        return postTimeStamp;
    }
    public void setPostTimeStamp(long postTimeStamp) {
        this.postTimeStamp = postTimeStamp;
    }
    public boolean getIdentifier() {
        return identifier;
    }
    public void setIdentifier(boolean identifier) {
        this.identifier = identifier;
    }

    //HC Getters and setters
    public String getHcOwner() {
        return hcOwner;
    }
    public void setHcOwner(String hcOwner) {
        this.hcOwner = hcOwner;
    }
//    public ImageView getHccoverimg() {
//        return hccoverimg;
//    }
//
//    public void setHccoverimg(ImageView hccoverimg) {
//        this.hccoverimg = hccoverimg;
//    }
    public String getHcstallname() {
        return hcstallname;
    }
    public void setHcstallname(String hcstallname) {
        this.hcstallname = hcstallname;
    }
    public String getHcauthor() {
        return hcauthor;
    }
    public void setHcauthor(String hcauthor) {
        this.hcauthor = hcauthor;
    }
    public String getHccuserpfp() {
        return hccuserpfp;
    }
    public void setHccuserpfp(String hccuserpfp) {
        this.hccuserpfp = hccuserpfp;
    }
    /*public Boolean getHccliked() {
        return hccliked;
    }

    public void setHccliked(Boolean hccliked) {
        this.hccliked = hccliked;
    }*/
    public String getHccparagraph() {
        return hccparagraph;
    }
    public void setHccparagraph(String hccparagraph) {
        this.hccparagraph = hccparagraph;
    }
    public String getHccaddress() {
        return hccaddress;
    }
    public void setHccaddress(String hccaddress) {
        this.hccaddress = hccaddress;
    }
    public String getDaysopen() {
        return daysopen;
    }
    public void setDaysopen(String daysopen) {
        this.daysopen = daysopen;
    }
    public String getHoursopen() {
        return hoursopen;
    }
    public void setHoursopen(String hoursopen) {
        this.hoursopen = hoursopen;
    }
    public String getShortdesc() {
        return shortdesc;
    }

    public void setShortdesc(String shortdesc) {
        this.shortdesc = shortdesc;
    }

    // RC Getter Setters
    // creating getter and setter methods.
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

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps(){return steps; }

    public void setSteps(String steps){this.steps = steps; }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }
}
