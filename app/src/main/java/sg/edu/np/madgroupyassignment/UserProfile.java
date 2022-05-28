package sg.edu.np.madgroupyassignment;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

@org.parceler.Parcel
public class UserProfile{
    String UID;
    String username;
    String email;
    String profileImg;
    HashMap<String, Object> HawkList;
    HashMap<String, Object> RcpList;

    public UserProfile() {

    }

    public UserProfile(String UID, String username, String email,String profileImg, HashMap<String, Object> hawkList, HashMap<String, Object> rcpList) {
        this.UID = UID;
        this.username = username;
        this.email = email;
        this.profileImg = profileImg;
        HawkList = hawkList;
        RcpList = rcpList;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImg() {return profileImg;}

    public void setProfileImg(String profileImg) {this.profileImg = profileImg; }

    public HashMap<String, Object> getHawkList() {
        return HawkList;
    }

    public void setHawkList(HashMap<String, Object> hawkList) {
        HawkList = hawkList;
    }

    public  HashMap<String, Object> getRcpList() { return RcpList;}

    public void setRcpList(HashMap<String, Object> rcpList) {
        RcpList = rcpList;
    }
}
