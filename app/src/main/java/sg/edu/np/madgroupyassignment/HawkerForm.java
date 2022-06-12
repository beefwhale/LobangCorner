package sg.edu.np.madgroupyassignment;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class HawkerForm extends Fragment {

    TextView openDayBtn;
    boolean[] selectedDay;
    ArrayList<Integer> dayList = new ArrayList<>();
    String daysOpen;
    String[] dayArray = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    int opHour, opMin, clHour, clMin;

    Button test;
    Button submit;
    EditText sNInput;
    EditText dInput;
    EditText aInput;
    Button opTInput;
    Button clTInput;

    String stallName;
    String desc;
    String address;
    String openingTime;
    String closingTime;
    String userPfpUrl;
    String finalTime;

    private DatabaseReference databaseReferencetest;
    private FirebaseAuth mAuth;
    private static UserProfile userProfile;
    String ownerUID;
    String username;
    HashMap<String, Object> userCurrentHwk;


    HawkerCornerStalls hCS;

    public HawkerForm() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View hf = inflater.inflate(R.layout.fragment_hawker_form, container, false);

        test = hf.findViewById(R.id.hbutton2);//Cover image button
        submit = hf.findViewById(R.id.submitBtn);

        openingTime = "00:00";
        closingTime = "00:00";

        clHour = 00;
        clMin = 00;
        opHour = 00;
        opMin = 00;

        //Assign variable
        sNInput = hf.findViewById(R.id.StallName);
        dInput = hf.findViewById(R.id.Desc);
        aInput = hf.findViewById(R.id.addrInput);
        opTInput = hf.findViewById(R.id.openingTime);
        clTInput = hf.findViewById(R.id.closingTime);

        //Assigning Stall Name to variable
        sNInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                stallName = sNInput.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        //Assigning Description to variable
        dInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                desc = dInput.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        //Assigning Address to variable
        aInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                address = aInput.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        //Select Opening and Closing Time
        finalTime = "";

        opTInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener= new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHr, int selectedMin) {
                        opHour = selectedHr;
                        opMin = selectedMin;
                        opTInput.setText(String.format(Locale.getDefault(), "%02d:%02d", opHour, opMin));
                        openingTime = String.format(Locale.getDefault(), "%02d:%02d", opHour, opMin);
                        finalTime = openingTime;
                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),TimePickerDialog.THEME_HOLO_LIGHT,
                        onTimeSetListener,opHour,opMin,true);


                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });

        clTInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener= new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHr, int selectedMin) {
                        if (clTInput.getText() == "") {
                            clHour = 00;
                            clMin = 00;
                        }
                        else{
                            clHour = selectedHr;
                            clMin = selectedMin;
                            clTInput.setText(String.format(Locale.getDefault(), "%02d:%02d", clHour, clMin));
                            closingTime = String.format(Locale.getDefault(), "%02d:%02d", clHour, clMin);
                            finalTime = finalTime +" - "+closingTime;
                        }

                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),TimePickerDialog.THEME_HOLO_LIGHT,
                        onTimeSetListener,clHour,clMin,true);


                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });








        //*************************************Everything from here onwards is for Opening Days********************************
        //Assign variable
        openDayBtn = hf.findViewById(R.id.openDaysBtn);

        //Initialize selected day array
        selectedDay = new boolean[dayArray.length];

        openDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); //Context is getActivity

                //Set title
                builder.setTitle("Select Day");
                //Set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(dayArray, selectedDay, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        //Check condition
                        if (b) {
                            //when checkbox selected
                            //add position in day list
                            dayList.add(i);
                            //Sort day list
                            Collections.sort(dayList);
                        }
                        else{
                            //When checkbox unselected
                            //Remove position from day list
                            dayList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        //use for loop
                        for (int j = 0; j<dayList.size(); j++){
                            //Concat array value
                            stringBuilder.append(dayArray[dayList.get(j)]);
                            //Check condition
                            if (j != dayList.size()-1){
                                //When j value not equal to day list size -1
                                //Add comma
                                stringBuilder.append(", ");
                            }
                        }
                        //to set text of list
                        openDayBtn.setText(stringBuilder.toString());
                        daysOpen = ""; //String with all the stuff
                        for (int j =0;j<dayList.size();j++){
                            if (j == dayList.size()-1){
                                daysOpen = daysOpen + dayArray[dayList.get(j)];
                            }
                            else{
                                daysOpen = daysOpen + dayArray[dayList.get(j)] +", ";
                            }
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dismiss Dialog
                        dialogInterface.dismiss();
                    }
                });

                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Use for loop
                        for (int j=0; j<selectedDay.length; j++){
                            //Remove all selection
                            selectedDay[j] = false;
                            //Clear day list
                            dayList.clear();
                            //Clear text view values
                            openDayBtn.setText("");
                            daysOpen = "";
                        }
                    }
                });
                //Show dialog
                builder.show();

            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stallName.isEmpty() || stallName.length() == 0 || stallName == "" ||
                        desc.isEmpty() || desc.length() == 0 || desc == ""){
                    Toast.makeText(getActivity(),"Please include Stall Name and Description", Toast.LENGTH_SHORT).show();
                }
                else{
                    username = userProfile.getUsername(); //USERNAME parameter
                    userPfpUrl = userProfile.getProfileImg();
                    ownerUID = userProfile.getUID();
                    long timeStamp = System.currentTimeMillis();
                    hCS = new HawkerCornerStalls(ownerUID, stallName,username/*,false*/,desc,address,daysOpen,finalTime,userPfpUrl, timeStamp);

                    userCurrentHwk = userProfile.getHawkList();
                    //HwkUp(userCurrentHwk, hCS);


                    //***********For input to reset when button submit***********
                    stallName = "";
                    desc = "";
                    address = "";
                    daysOpen = "";
                    openingTime = "00:00";
                    closingTime ="00:00";
                    finalTime = "";


                    sNInput.setText("");
                    dInput.setText("");
                    aInput.setText("");
                    opTInput.setText("");
                    clTInput.setText("");
                    for (int j=0; j<selectedDay.length; j++) {
                        //Remove all selection
                        selectedDay[j] = false;
                    }
                    //Clear day list
                    dayList.clear();
                    //Clear text view values
                    openDayBtn.setText("");
                    daysOpen = "";
                }
                //Toast.makeText(getActivity(),finalTime, Toast.LENGTH_SHORT).show();

            }
        });



        return hf;
    }

    private void HwkUp(HashMap<String, Object> userHwkList, HawkerCornerStalls HwkObj) {
        databaseReferencetest = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        String PostID = databaseReferencetest.push().getKey();
        databaseReferencetest.child("Posts").child("Hawkers").child(PostID).setValue(HwkObj);

        userHwkList.put(PostID, PostID);
        databaseReferencetest.child("UserProfile").child(mAuth.getUid()).child("hawkList").updateChildren(userHwkList);
        Toast.makeText(getActivity(), "HawkerPost Uploaded", Toast.LENGTH_SHORT).show();
    }

    public void retrieveUserProfile(UserProfile userProfile){
        this.userProfile = userProfile;
    }
}