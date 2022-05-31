package sg.edu.np.madgroupyassignment;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class HawkerCornerStalls {
    //Data for the HawkerCornerStalls

    public ImageView hccoverimg;    //cover image for display in main
    public String hcstallname;      //stallname of hawker
    public String hcauthor;         //author who made hawker post
    public String hccuserpfp;        //user profile pic who made the post
    public Boolean hccliked;          //liked on the post
    public String hccparagraph;    //description of stall
    public String hccaddress;     //Address of hawker stall
    public String daysopen;      //How to make it so that user can enter different days and appear in two records
    public String hoursopen;    //Same as ^

    //Constructors
    public HawkerCornerStalls (){

    };

    public HawkerCornerStalls(/*ImageView hccoverimg,*/ String hcstallname, String hcauthor,
                              Boolean hccliked, String hccparagraph, String hccaddress,
                              String daysopen, String hoursopen, String hccuserpfp){
        //this.hccoverimg = hccoverimg;
        this.hcstallname = hcstallname;
        this.hcauthor = hcauthor;
        this.hccliked = hccliked;
        this.hccparagraph = hccparagraph;
        this.hccaddress = hccaddress;
        this.daysopen = daysopen;
        this.hoursopen = hoursopen;
        this.hccuserpfp = hccuserpfp;
    }

    //Getters and setters
    public ImageView getHccoverimg() {
        return hccoverimg;
    }

    public void setHccoverimg(ImageView hccoverimg) {
        this.hccoverimg = hccoverimg;
    }

    public String getHcstallname() {
        return hcstallname;
    }

    public void setHcstallname(String hcstallname) {
        this.hcstallname = hcstallname;
    }

    public String getHcauthor() {
        return hcauthor;
    }

    public void setHcauthor(String hcauthor) {
        this.hcauthor = hcauthor;
    }

    public String getHccuserpfp() {
        return hccuserpfp;
    }

    public void setHccuserpfp(String hccuserpfp) {
        this.hccuserpfp = hccuserpfp;
    }

    public Boolean getHccliked() {
        return hccliked;
    }

    public void setHccliked(Boolean hccliked) {
        this.hccliked = hccliked;
    }

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
}
