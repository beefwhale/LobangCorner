package sg.edu.np.madgroupyassignment;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class HawkerCornerStalls {
    //Data for the HawkerCornerStalls

    public ImageView hccoverimg;    //cover image for display in main
    public String hcstallname;      //stallname of hawker
    public String hcauthor;         //author who made hawker post
    public ImageView chosenstallimg;       //image in chosen stall post
    public ImageView hccuserpfp;           //user profile pic who made the post
    public TextView hccusername;           //user's name
    public Button hcclikebtn;              //liked on the post
    public TextView hccparagraph;          //description of stall

    //Constructors
    public HawkerCornerStalls (){

    };

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

    public ImageView getChosenstallimg() {
        return chosenstallimg;
    }

    public void setChosenstallimg(ImageView chosenstallimg) {
        this.chosenstallimg = chosenstallimg;
    }

    public ImageView getHccuserpfp() {
        return hccuserpfp;
    }

    public void setHccuserpfp(ImageView hccuserpfp) {
        this.hccuserpfp = hccuserpfp;
    }

    public TextView getHccusername() {
        return hccusername;
    }

    public void setHccusername(TextView hccusername) {
        this.hccusername = hccusername;
    }

    public Button getHcclikebtn() {
        return hcclikebtn;
    }

    public void setHcclikebtn(Button hcclikebtn) {
        this.hcclikebtn = hcclikebtn;
    }

    public TextView getHccparagraph() {
        return hccparagraph;
    }

    public void setHccparagraph(TextView hccparagraph) {
        this.hccparagraph = hccparagraph;
    }
}
