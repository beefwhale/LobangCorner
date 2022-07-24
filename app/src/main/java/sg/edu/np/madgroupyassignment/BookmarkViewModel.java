package sg.edu.np.madgroupyassignment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BookmarkViewModel extends ViewModel {

    //Bookmarked recipelist transfer
    private final MutableLiveData<ArrayList<RecipeCorner>> rcplist = new MutableLiveData<ArrayList<RecipeCorner>>();
    public void RcpList(ArrayList list){rcplist.setValue(list);}
    public LiveData<ArrayList<RecipeCorner>> getrcplist(){return rcplist;}

//    private final MutableLiveData<RecyclerView> rv = new MutableLiveData<>();
//    public void Rv(RecyclerView recyclerView){rv.setValue(recyclerView);}
//    public MutableLiveData<RecyclerView> getRv() {
//        return rv;
//    }
//
//    private final MutableLiveData<RecyclerView> hcRv = new MutableLiveData<>();
//    public void HcRv(RecyclerView recyclerView){hcRv.setValue(recyclerView);}
//    public MutableLiveData<RecyclerView> getHcRv() {
//        return hcRv;
//    }

    //Bookmarked hawkerlist transfer
    private final MutableLiveData<ArrayList<HawkerCornerStalls>> hclist = new MutableLiveData<ArrayList<HawkerCornerStalls>>();
    public void hcList(ArrayList list){hclist.setValue(list);}
    public LiveData<ArrayList<HawkerCornerStalls>> gethclist(){return hclist;}


}
