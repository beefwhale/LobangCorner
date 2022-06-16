package sg.edu.np.madgroupyassignment;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcel;
import org.w3c.dom.Text;

@Parcel
public class HawkerCornerStalls {
    //Data for the HawkerCornerStalls

    public String postid;              //Post id for the database to use
    public String hccoverimg;          //Cover image for display in hawker main, this and hccuserpfp are string as images passed as url
    public String hcstallname;         //Hawker stall name
    public String hcOwner;             //Owner of post, used in hawker form and profile
    public String hcauthor;            //Author who made hawker post, used in View Holder for recycler view in HCMains Adapter
    public String hccuserpfp;          //User profile pic who made the post, used in HCChosenStall
    public String hccparagraph;        //Full description of stall
    public String shortdesc;           //Short description of the stall
    public String hccaddress;          //Address of hawker stall
    public String daysopen;            //Days open
    public String hoursopen;           //Hours open
    public long postTimeStamp;         //Time stamp for home page

    //Constructors
    public HawkerCornerStalls (){

    };

    public HawkerCornerStalls(String postid, String hccoverimg,String hcOwner, String hcstallname, String hcauthor, String hccparagraph, String hccaddress,
                              String daysopen, String hoursopen, String hccuserpfp,String shortdesc, long postTimeStamp){
        this.postid = postid;
        this.hccoverimg = hccoverimg;
        this.hcOwner = hcOwner;
        this.hcstallname = hcstallname;
        this.hcauthor = hcauthor;
        this.hccparagraph = hccparagraph;
        this.hccaddress = hccaddress;
        this.daysopen = daysopen;
        this.hoursopen = hoursopen;
        this.hccuserpfp = hccuserpfp;
        this.shortdesc = shortdesc;
        this.postTimeStamp = postTimeStamp;
    }

    //Getters and setters

    public String getPostid() { return postid; }

    public void setPostid(String postid) { this.postid = postid; }

    public String getHccoverimg() { return hccoverimg; }

    public void setHccoverimg(String hccoverimg) { this.hccoverimg = hccoverimg; }

    public String getHcstallname() {
        return hcstallname;
    }

    public void setHcstallname(String hcstallname) {
        this.hcstallname = hcstallname;
    }

    public String getHcOwner() {
        return hcOwner;
    }

    public void setHcOwner(String hcOwner) {
        this.hcOwner = hcOwner;
    }

    public String getHcauthor() {
        return hcauthor;
    }

    public void setHcauthor(String hcauthor) {
        this.hcauthor = hcauthor;
    }

    public String getHccuserpfp() { return hccuserpfp; }

    public void setHccuserpfp(String hccuserpfp) { this.hccuserpfp = hccuserpfp; }

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

    public long getPostTimeStamp() {
        return postTimeStamp;
    }

    public void setPostTimeStamp(long postTimeStamp) {
        this.postTimeStamp = postTimeStamp;
    }
}
