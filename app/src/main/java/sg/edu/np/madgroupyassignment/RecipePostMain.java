package sg.edu.np.madgroupyassignment;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.StringBufferInputStream;
import java.text.Normalizer;


public class RecipePostMain extends Fragment {

    EditText durInput;
    static String min;

    EditText titleInput;
    static String title;

    EditText descInput;
    static String desc;

    NumberPicker numberPicker;
    int newVal;

    ImageView displayPicButtonRecipe;
    Uri ImageUri;
    String downUrl;
    private StorageReference storageReference;
    ActivityResultLauncher<String> getPhoto;

    private FormsViewModel viewModel;




    public RecipePostMain() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View f1 = inflater.inflate(R.layout.fragment_recipe_post_main, container, false);
        durInput = f1.findViewById(R.id.minuteInput);
        titleInput = f1.findViewById(R.id.RecipeTitle);
        descInput = f1.findViewById(R.id.Desc);
        numberPicker = f1.findViewById(R.id.numPicker);
        displayPicButtonRecipe = f1.findViewById(R.id.displayPic2);
        newVal = 0;
        viewModel = new ViewModelProvider(requireParentFragment()).get(FormsViewModel.class);

        storageReference = FirebaseStorage.getInstance().getReference();

        //Button to get photo
        getPhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        ImageUri = result;
                        upPost();
                    }
                }
        );

        displayPicButtonRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhoto.launch("image/*");
            }
        });


        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(5);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                newVal = i1;
                viewModel.selectDifficulty(newVal);
            }
        });

        //TitleInput Data Transfer
        titleInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                title = titleInput.getText().toString(); //NAME parameter

            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.selectRecipeName(title);
            }
        });

        //DescInput Data Transfer
        descInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                desc = descInput.getText().toString(); //DESC parameter

            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.selectRecipeDesc(desc);
            }
        });

        //Duration Input Data Transfer
        durInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                min = durInput.getText().toString(); //DURATION parameter

            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.selectRecipeDuration(min);
            }
        });



        return f1;
    }

    //    Getting file extension
    private String getFileExt(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void upPost() {

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
                                    Picasso.get().load(downUrl).into(displayPicButtonRecipe);
                                    viewModel.selectImg(downUrl);
                                }
                            });
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}