package sg.edu.np.madgroupyassignment;

import java.io.Serializable;

public class ImgUp implements Serializable {
    private String uOwn;
    private String ImgUrl;

    public ImgUp() {

    }

    public ImgUp(String upr, String imgUrl) {
        uOwn = upr;
        ImgUrl = imgUrl;
    }

    public String getuOwn() {
        return uOwn;
    }

    public void setuOwn(String uOwn) {
        this.uOwn = uOwn;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }
}
