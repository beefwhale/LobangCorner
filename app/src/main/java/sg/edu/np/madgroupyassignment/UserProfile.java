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
    HashMap<String, ImgUp> HawkList;

    public UserProfile() {

    }

    public UserProfile(String UID, String username, String email, HashMap<String, ImgUp> hawkList) {
        this.UID = UID;
        this.username = username;
        this.email = email;
        HawkList = hawkList;
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

    public HashMap<String, ImgUp> getHawkList() {
        return HawkList;
    }

    public void setHawkList(HashMap<String, ImgUp> hawkList) {
        HawkList = hawkList;
    }
}
