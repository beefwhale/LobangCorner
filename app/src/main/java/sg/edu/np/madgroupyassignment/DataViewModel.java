package sg.edu.np.madgroupyassignment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class DataViewModel extends ViewModel {
    //User profile
    private final MutableLiveData<UserProfile> Profile = new MutableLiveData<UserProfile>();
    public void setProfile(UserProfile userProfile) {
        Profile.setValue(userProfile);
    }
    public LiveData<UserProfile> getProfile() {
        return Profile;
    }

    //List of recipe post
    private final MutableLiveData<ArrayList<RecipeCorner>> RcpList = new MutableLiveData<ArrayList<RecipeCorner>>();
    public void setRcpList(ArrayList<RecipeCorner> recipeCorners) {
        RcpList.setValue(recipeCorners);
    }
    public LiveData<ArrayList<RecipeCorner>> getRcpList() {
        return RcpList;
    }

    //List of hawker post
    private final MutableLiveData<ArrayList<HawkerCornerStalls>> HwkList = new MutableLiveData<ArrayList<HawkerCornerStalls>>();
    public void setHwkList(ArrayList<HawkerCornerStalls> hwkList) {
        HwkList.setValue(hwkList);
    }
    public LiveData<ArrayList<HawkerCornerStalls>> getHwkList() {
        return HwkList;
    }
}
