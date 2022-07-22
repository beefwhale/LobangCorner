package sg.edu.np.madgroupyassignment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class BookmarkViewModel extends ViewModel {

    //changing fragment
    private final MutableLiveData<Integer> checkedBox = new MutableLiveData<Integer>();
    public void checkedBox(Integer integer){checkedBox.setValue(integer);}
    public LiveData<Integer> getcheckedBox(){return checkedBox;}

    private final MutableLiveData<List<RecipeCorner>> rcplist = new MutableLiveData<List<RecipeCorner>>();
    public void RcpList(List list){rcplist.setValue(list);}
    public LiveData<List<RecipeCorner>> getrcplist(){return rcplist;}


}
