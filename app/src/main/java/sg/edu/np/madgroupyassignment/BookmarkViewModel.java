package sg.edu.np.madgroupyassignment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BookmarkViewModel extends ViewModel {

    //changing fragment
    private final MutableLiveData<Integer> checkedBox = new MutableLiveData<Integer>();
    public void checkedBox(Integer integer){checkedBox.setValue(integer);}
    public LiveData<Integer> getcheckedBox(){return checkedBox;}

    private final MutableLiveData<ArrayList<RecipeCorner>> rcplist = new MutableLiveData<ArrayList<RecipeCorner>>();
    public void RcpList(ArrayList list){rcplist.setValue(list);}
    public LiveData<ArrayList<RecipeCorner>> getrcplist(){return rcplist;}

    private final MutableLiveData<RecyclerView> rv = new MutableLiveData<>();
    public void Rv(RecyclerView recyclerView){rv.setValue(recyclerView);}

    public MutableLiveData<RecyclerView> getRv() {
        return rv;
    }
}
