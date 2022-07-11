package sg.edu.np.madgroupyassignment;

import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class HawkerForm extends Fragment {

    TextView openDayBtn;
    boolean[] selectedDay;
    ArrayList<Integer> dayList = new ArrayList<>();
    String daysOpen = "";
    String[] dayArray = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    int opHour, opMin, clHour, clMin;

    Button submit;
    EditText sNInput;
    EditText dInput;
    EditText aInput;
    EditText sdInput;
    Button opTInput;
    Button clTInput;

    public static String stallName;
    String desc;
    String shortDesc;
    String address;
    String openingTime;
    String closingTime;
    String userPfpUrl;
    String finalTime;

    private PostsHolder postsHolder;
    private DatabaseReference databaseReferencetest;
    private FirebaseAuth mAuth;
    private UserProfile userProfile;
    String ownerUID;
    String username;
    HashMap<String, Object> userCurrentHwk;
    OnBackPressedCallback callback;

    ImageView displayPicButtonHawker;
    Uri ImageUri;
    String downUrl;
    private StorageReference storageReference;
    ActivityResultLauncher<String> getPhoto;
    HawkerCornerStalls hCS;

    // For editing forms
    Integer check;
    HawkerCornerStalls chosenstall;


    public HawkerForm(Integer check) {
        //Checks if its creating or editing forms
        // Create = 0, Edit = 1, Edit Draft = 2
        this.check = check;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View hf;
        if (check == 0){
            hf = inflater.inflate(R.layout.fragment_hawker_form, container, false);
        }
        else{
            hf = inflater.inflate(R.layout.fragment_hawker_form_edit, container, false);
            Bundle bundle = this.getArguments();
            assert bundle != null;
            int chosenstallno = (int) bundle.getInt("stallposition");
            ArrayList<HawkerCornerStalls> stallsList = Parcels.unwrap(bundle.getParcelable("list"));
            chosenstall = stallsList.get(chosenstallno);
        }


        //Assign variable
        submit = hf.findViewById(R.id.submitBtn);
        displayPicButtonHawker = hf.findViewById(R.id.displayPic);
        sNInput = hf.findViewById(R.id.StallName);
        dInput = hf.findViewById(R.id.Desc);
        sdInput = hf.findViewById(R.id.shortDesc);
        aInput = hf.findViewById(R.id.addrInput);
        opTInput = hf.findViewById(R.id.openingTime);
        clTInput = hf.findViewById(R.id.closingTime);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReferencetest = FirebaseDatabase.getInstance().getReference();


        //Select Opening and Closing Time
        finalTime = "";

        //CREATING : Setting Default Values
        if (check == 0) {
            openingTime = "00:00";
            closingTime = "00:00";
            stallName = "";
            desc = "";
            shortDesc = "";
            address = "";
            downUrl = "";
            clHour = 00;
            clMin = 00;
            opHour = 00;
            opMin = 00;

        }
        // EDITING : setting values
        else if(check== 1){
            if (chosenstall.hoursopen.equals("") == false){
                openingTime = chosenstall.hoursopen.substring(0,5);
                closingTime = chosenstall.hoursopen.substring(8,13);

                clHour = Integer.parseInt(chosenstall.hoursopen.substring(8,10));
                clMin = Integer.parseInt(chosenstall.hoursopen.substring(11,13));
                opHour = Integer.parseInt(chosenstall.hoursopen.substring(0,2));
                opMin = Integer.parseInt(chosenstall.hoursopen.substring(3,5));

                opTInput.setText(openingTime);
                clTInput.setText(closingTime);
                finalTime = openingTime+" - "+closingTime;
            }
            if (chosenstall.daysopen.equals("") == false){
                daysOpen = chosenstall.daysopen;

            }
            stallName = chosenstall.hcstallname;
            desc = chosenstall.hccparagraph;
            shortDesc = chosenstall.shortdesc;
            address = chosenstall.hccaddress;
            downUrl = chosenstall.hccoverimg;


            displayPicButtonHawker = hf.findViewById(R.id.displayPic);
            sNInput.setText(stallName);
            dInput.setText(desc);
            sdInput.setText(shortDesc);
            aInput.setText(address);


            //Setting image
            downUrl = chosenstall.hccoverimg;
            Picasso.get().load(downUrl).into(displayPicButtonHawker);
        }


        //Button to get photo
        getPhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        ImageUri = result;
                        upPost(ImageUri);
                    }
                }
        );
        //When click button, launches get photo interface
        displayPicButtonHawker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhoto.launch("image/*");
            }
        });

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

        //Assigning Short Description to variable
        sdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                shortDesc = sdInput.getText().toString();
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




        opTInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener= new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHr, int selectedMin) {
                        opHour = selectedHr;
                        opMin = selectedMin;
                        opTInput.setText(String.format(Locale.getDefault(), "%02d:%02d", opHour, opMin));
                        //String formatting openingTime variable
                        openingTime = String.format(Locale.getDefault(), "%02d:%02d", opHour, opMin);
                        //Setting finalTime to openingTime to combine with closingTime later
                        finalTime = openingTime;
                    }
                };

                //Show timepicker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),TimePickerDialog.THEME_HOLO_LIGHT,
                        onTimeSetListener,opHour,opMin,true);

                //Setting title of timepicker
                timePickerDialog.setTitle("Select Time");
                //Showing timepicker when button clicked
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
                            //String formatting openingTime variable
                            closingTime = String.format(Locale.getDefault(), "%02d:%02d", clHour, clMin);
                            //Combining closingTime with openingTime
                            finalTime = finalTime +" - "+closingTime;
                        }

                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),TimePickerDialog.THEME_HOLO_LIGHT,
                        onTimeSetListener,clHour,clMin,true);

                //Setting title of timepicker
                timePickerDialog.setTitle("Select Time");
                //Showing timepicker when button clicked
                timePickerDialog.show();
            }
        });


        //*************************************Everything from here onwards is for Opening Days********************************\
        //Assign variable
        openDayBtn = hf.findViewById(R.id.openDaysBtn);
        //Initialize selected day array
        selectedDay = new boolean[dayArray.length];

        // EDIT FORMS : setting checked days
        if (check == 1 && chosenstall.daysopen != null){
            String daysOpen = chosenstall.daysopen;
            openDayBtn.setText(daysOpen);
            String[] daysSplit = daysOpen.split(",");
            // Matching Day eg;(Monday) to the list of days to get position
            for (String i : daysSplit) {
                //Removing spaces
                i = i.trim();
                for (int g = 0; g < dayArray.length; g++) {
                    if (i.equals(dayArray[g])) {
                        dayList.add(g);
                        Collections.sort(dayList);
                    }
                }
            }
            //Updating Checkbox
            for (int i : dayList) {
                selectedDay[i] = true;
            }
        }

        openDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); //Context is getActivity
                //Set title
                builder.setTitle("Select Day(s)");
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
                        daysOpen ="";
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If stallnames, description, shortdesc, address or no image, you cannot submit
                if (stallName.isEmpty() || stallName.length() == 0 || stallName == "" ||
                        desc.isEmpty() || desc.length() == 0 || desc == "" ||
                            shortDesc.isEmpty() || shortDesc.length() == 0 || shortDesc == "" ||
                                address.isEmpty() || address.length() == 0 || address == "" ||
                                    downUrl.isEmpty() || downUrl.length() == 0 || downUrl == ""){
                    Toast.makeText(getActivity(),"Please input Stall Name, Descriptions, Image and Address", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Posting Form
                    if (check == 0){
                        MainActivity.checkFormsNum = 1;
                        username = userProfile.getUsername(); //Getting username
                        userPfpUrl = userProfile.getProfileImg(); //Getting profile picture
                        ownerUID = userProfile.getUID(); //Getting profile uid
                        long timeStamp = System.currentTimeMillis(); //Getting post time
                        String PostID = databaseReferencetest.push().getKey(); //Getting Post id
                        hCS = new HawkerCornerStalls(PostID, downUrl,ownerUID, stallName,username,desc,address,daysOpen,finalTime,userPfpUrl, shortDesc, timeStamp, false);

                        userCurrentHwk = userProfile.getHawkList();
                        HwkUp(userCurrentHwk, hCS, PostID);
                    }
                    //Editing Form
                    else if (check == 1){
                        username = userProfile.getUsername(); //Getting username
                        userPfpUrl = userProfile.getProfileImg(); //Getting profile picture
                        ownerUID = userProfile.getUID(); //Getting profile uid

                        long timeStamp = chosenstall.postTimeStamp; //Getting post time
                        String PostID = chosenstall.postid; //Getting Post id
                        hCS = new HawkerCornerStalls(PostID, downUrl,ownerUID, stallName,username,desc,address,daysOpen,finalTime,userPfpUrl, shortDesc, timeStamp, false);

                        userCurrentHwk = userProfile.getHawkList();
                        HwkUp(userCurrentHwk, hCS, PostID);
                    }



                    //***********For input to reset when button submit***********
                    stallName = "";
                    desc = "";
                    shortDesc = "";
                    address = "";
                    downUrl = "";
                    daysOpen = "";
                    openingTime = "00:00";
                    closingTime ="00:00";
                    finalTime = "";


                    sNInput.setText("");
                    dInput.setText("");
                    sdInput.setText("");
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
//                    getActivity().recreate();
                    if (check == 0){
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, new HawkerForm(0)).commit();
                    }
                    else if (check == 1){
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, new Profile()).commit();
                    }

                }
                //Toast.makeText(getActivity(),finalTime, Toast.LENGTH_SHORT).show();

            }
        });
        return hf;
    }



    private void HwkUp(HashMap<String, Object> userHwkList, HawkerCornerStalls HwkObj, String PostID) {
//        databaseReferencetest = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //String PostID = databaseReferencetest.push().getKey();
        // Posting Forms
        if (check == 0){
            databaseReferencetest.child("Posts").child("Hawkers").child(PostID).setValue(HwkObj);
            userHwkList.put(PostID, PostID);
            databaseReferencetest.child("UserProfile").child(mAuth.getUid()).child("hawkList").updateChildren(userHwkList);
            Toast.makeText(getActivity(), "HawkerPost Uploaded", Toast.LENGTH_SHORT).show();
        }
        // Editing Forms
        else if (check == 1){
            databaseReferencetest.child("Posts").child("Hawkers").child(PostID).setValue(HwkObj);
            Toast.makeText(getActivity(), "HawkerPost Edited", Toast.LENGTH_SHORT).show();
        }

    }

    public void retrieveUserProfile(){
        this.userProfile = postsHolder.getUserProfile();
    }

    //    Getting file extension
    private String getFileExt(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void upPost(Uri ImageUri) {
        if (ImageUri != null) {
            StorageReference fileReference = storageReference.child("ImgUps").child(System.currentTimeMillis() + "." + getFileExt(ImageUri));

//            Putting new profile image into storage
            fileReference.putFile(ImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(), "Upload Successful", Toast.LENGTH_SHORT).show();

//                            Getting new image url
                            fileReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    downUrl = task.getResult().toString();
                                    Picasso.get().load(downUrl).into(displayPicButtonHawker);
                                }
                            });
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void leaveAlert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity()); //Context is getActivity

        //Set title
        builder1.setTitle("Wait!");
        builder1.setMessage("Do you want to save this to drafts?");

        builder1.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getParentFragmentManager().popBackStack();
                MainActivity.mainFAB.show();
            }
        });

        builder1.setNeutralButton("Don't Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getParentFragmentManager().popBackStack();
                MainActivity.mainFAB.show();
            }
        });

        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dismiss Dialog
                dialogInterface.dismiss();
                callback.setEnabled(true);
                MainActivity.checkFormsNum = 0;
            }
        });

        builder1.show();
    }
    @Override
    public void onResume() {
        // Clears input when reenter forms
        if (check == 0){
            stallName = "";
            desc = "";
            shortDesc = "";
            address = "";
            downUrl = "";
            daysOpen = "";
            openingTime = "00:00";
            closingTime ="00:00";
            finalTime = "";


            sNInput.setText("");
            dInput.setText("");
            sdInput.setText("");
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
        //Alert shows up when back button is pressed.
        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (stallName == "" || stallName.isEmpty() || stallName == null){
                    getParentFragmentManager().popBackStack();
                    MainActivity.mainFAB.show();
                }
                else{
                    leaveAlert();
                    //Ensure it doesnt affect when not in forms
                    setEnabled(false);
                }
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
        super.onResume();
    }

    @Override
    public void onPause() {
        callback.setEnabled(false);
        super.onPause();
    }
}
