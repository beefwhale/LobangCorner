package sg.edu.np.madgroupyassignment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

public class FormsViewModel extends ViewModel {
    //RecipeName Transfer
    private final MutableLiveData<String> selectedRecipeName = new MutableLiveData<String>();
    public void selectRecipeName(String string){
        selectedRecipeName.setValue(string);
    }
    public LiveData<String> getSelectedRecipeName(){
        return selectedRecipeName;
    }

    //RecipeDesc Transfer
    private final MutableLiveData<String> selectedRecipeDesc = new MutableLiveData<String>();
    public void selectRecipeDesc(String string){
        selectedRecipeDesc.setValue(string);
    }
    public LiveData<String> getSelectedRecipeDesc(){
        return selectedRecipeDesc;
    }

    //RecipeDuration Transfer
    private final MutableLiveData<String> selectedRecipeDuration = new MutableLiveData<String>();
    public void selectRecipeDuration(String string){
        selectedRecipeDuration.setValue(string);
    }
    public LiveData<String> getSelectedRecipeDuration(){
        return selectedRecipeDuration;
    }

    //RecipeSteps Transfer
    private final MutableLiveData<String> selectedRecipeSteps = new MutableLiveData<String>();
    public void selectRecipeSteps(String string){
        selectedRecipeSteps.setValue(string);
    }
    public LiveData<String> getSelectedRecipeSteps(){
        return selectedRecipeSteps;
    }

    //RecipeIngred Transfer
    private final MutableLiveData<String> selectedRecipeIngred = new MutableLiveData<String>();
    public void selectRecipeIngred(String string){selectedRecipeIngred.setValue(string);}
    public LiveData<String> getSelectedRecipeIngred(){return selectedRecipeIngred;}

    //RecipeDifficulty Transfer
    private final MutableLiveData<Integer> selectedDifficulty = new MutableLiveData<Integer>();
    public void selectDifficulty(Integer integer){selectedDifficulty.setValue(integer);}
    public LiveData<Integer> getSelectedDifficulty(){return selectedDifficulty;}

    //RecipeImg Transfer
    private final MutableLiveData<String> selectedImg = new MutableLiveData<String>();
    public void selectImg(String string){selectedImg.setValue(string);}
    public LiveData<String> getSelectedImg(){return selectedImg;}

    //changing fragment
    private final MutableLiveData<Integer> changeFragment = new MutableLiveData<Integer>();
    public void changeFragment(Integer integer){changeFragment.setValue(integer);}
    public LiveData<Integer> getchangeFragment(){return changeFragment;}

    //checking if Posting / Editing
    private final MutableLiveData<Integer> status = new MutableLiveData<Integer>();
    public void status(Integer integer){status.setValue(integer);}
    public LiveData<Integer> getStatus(){return status;}

    //Passing recipe objects to child fragments
    private final MutableLiveData<RecipeCorner> recipePost = new MutableLiveData<RecipeCorner>();
    public void recipePost(RecipeCorner RecipeCorner){recipePost.setValue(RecipeCorner);}
    public LiveData<RecipeCorner> getRecipePost(){return recipePost;}
}
