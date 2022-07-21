package sg.edu.np.madgroupyassignment;

import org.parceler.Parcel;

import java.util.HashMap;

@Parcel
public class UserProfile {
    String UID;
    String username;
    String email;
    String profileImg;
    String aboutMe;
    HashMap<String, Object> HawkList;
    HashMap<String, Object> RcpList;

    String instagram;
    String facebook;
    String twitter;

    public UserProfile() {

    }

    public UserProfile(String UID, String username, String email, String profileImg, String aboutMe, HashMap<String, Object> hawkList, HashMap<String, Object> rcpList,
                       String instagram, String facebook, String twitter) {
        this.UID = UID;
        this.username = username;
        this.email = email;
        this.profileImg = profileImg;
        this.aboutMe = aboutMe;
        HawkList = hawkList;
        RcpList = rcpList;

        //Socials
        this.instagram = instagram;
        this.facebook = facebook;
        this.twitter = twitter;
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

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public HashMap<String, Object> getHawkList() {
        return HawkList;
    }

    public void setHawkList(HashMap<String, Object> hawkList) {
        HawkList = hawkList;
    }

    public HashMap<String, Object> getRcpList() {
        return RcpList;
    }

    public void setRcpList(HashMap<String, Object> rcpList) {
        RcpList = rcpList;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}
