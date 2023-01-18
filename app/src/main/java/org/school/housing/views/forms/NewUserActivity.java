package org.school.housing.views.forms;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.school.housing.R;
import org.school.housing.api.controllers.UserApiController;
import org.school.housing.fragments.dialogs.ImagePickerDialog;
import org.school.housing.interfaces.ProcessCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class NewUserActivity extends AppCompatActivity implements View.OnClickListener, ImagePickerDialog.ImagePickerListener {
    private static final String TAG = "NewUserActivity";
    private TextInputEditText username_edt, email_edt, mobile_edt, national_number_edt, family_members_edt;
    private Button submit_button, btn_pick_image;
    private RadioGroup gender_radioGroup;
    private String name, email, mobile, national_number, family_members;
    private String gender;


    //    private ActivityResultLauncher<Void> galleryResultLauncher;
    private ActivityResultLauncher<String> permissionResultLauncher;
    private ActivityResultLauncher<Void> cameraResultLauncher;
    private Bitmap imageBitmap;
    private byte[] imagePart = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        setupResultsLauncher();

//        createDummyUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        inti();
    }

    private void inti() {
        findViews();
        setEveryOnClick();
        controlGenderSelection();

    }

    private void findViews() {
        username_edt = findViewById(R.id.username_edt);
        email_edt = findViewById(R.id.email_edt);
        mobile_edt = findViewById(R.id.mobile_edt);
        national_number_edt = findViewById(R.id.national_number_edt);
        family_members_edt = findViewById(R.id.family_members_edt);

        gender_radioGroup = findViewById(R.id.gender_radio_group);

        btn_pick_image = findViewById(R.id.btnPickImage);
        submit_button = findViewById(R.id.btn_submit_newUser);
    }

    private void setEveryOnClick() {
        btn_pick_image.setOnClickListener(this);
        submit_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == submit_button.getId()) {
            performCreation();
        } else if (view.getId() == btn_pick_image.getId()) {
            new ImagePickerDialog().show(getSupportFragmentManager(), "PickingImage");
        }
    }

    private boolean checkData() {
        return !Objects.requireNonNull(username_edt.getText()).toString().isEmpty() && !Objects.requireNonNull(email_edt.getText()).toString().isEmpty() && !Objects.requireNonNull(mobile_edt.getText()).toString().isEmpty() && !Objects.requireNonNull(national_number_edt.getText()).toString().isEmpty() && !Objects.requireNonNull(family_members_edt.getText()).toString().isEmpty()
                && imageBitmap != null;
    }

    private boolean saveUserData() {
        name = Objects.requireNonNull(username_edt.getText()).toString();
        email = Objects.requireNonNull(email_edt.getText()).toString();
        mobile = Objects.requireNonNull(mobile_edt.getText()).toString();
        national_number = Objects.requireNonNull(national_number_edt.getText()).toString();
        family_members = Objects.requireNonNull(family_members_edt.getText()).toString();
        gender = controlGenderSelection();
        imagePart = bitmapToBytes();

        Log.i(TAG, "saveUserData: name :=> " + name + " email " + email + " mobile " + mobile + " national_number " + national_number + " gender " + gender + " imagePart " + imagePart.length);
        return true;
    }

    private String controlGenderSelection() {
        gender_radioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> gender = checkedId == R.id.male_radio_button ? "M" : "F");
        return gender;
    }

    private void performCreation() {
        if (checkData()) {
            createUser();
        } else {
            Snackbar.make(findViewById(R.id.btn_submit_newUser), "Check empty  fields", Snackbar.LENGTH_LONG).show();
        }
    }

    private void createUser() {
        if (saveUserData()) {
            UserApiController.getInstance().create_user_map(name, email, mobile, national_number, family_members, gender, imagePart, new ProcessCallback() {
                @Override
                public void onSuccess(String message) {
                    Log.i(TAG, "onSuccess: Created =>" + message);
                    Snackbar.make(findViewById(R.id.btn_submit_newUser), "Done Congregates ", Snackbar.LENGTH_LONG).show();
                    onBackPressed();
                }

                @Override
                public void onFailure(String massage) {
                    Log.i(TAG, "onFailure: ");
                    Snackbar.make(findViewById(R.id.btn_submit_newUser), massage, Snackbar.LENGTH_LONG).show();
                }

            });
        }
    }


    private void pickCameraImage() {
        permissionResultLauncher.launch(Manifest.permission.CAMERA);
    }

    private void setupResultsLauncher() {
        permissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) {
                cameraResultLauncher.launch(null);
            } else {
                Toast.makeText(NewUserActivity.this, "False", Toast.LENGTH_SHORT).show();
            }
        });

        cameraResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), result -> {
            if (result != null) {
                imageBitmap = result;
                Log.i(TAG, "setupResultsLauncher: image =>" + imageBitmap);
                Toast.makeText(this, "Image Picked Successfully", Toast.LENGTH_SHORT).show();
                btn_pick_image.setBackgroundColor(getResources().getColor(R.color.special_green));
            } else {
                Toast.makeText(NewUserActivity.this, "No Image Got Picked", Toast.LENGTH_SHORT).show();
                btn_pick_image.setBackgroundColor(getResources().getColor(R.color.shiny_red));
            }
        });
    }

    private byte[] bitmapToBytes() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onCameraClicked() {
        pickCameraImage();
    }

    @Override
    public void onGalleryClicked() {pickImageFromGallery();}
    // ---Gallery Image Code
    private void pickImageFromGallery() {
        Intent intentPickImageFromGallery = new Intent();
        intentPickImageFromGallery.setType("image/*");
        intentPickImageFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        launchSomeActivity.launch(intentPickImageFromGallery);
    }
    private final ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap = null;
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                       //here use the bitmap object as you wish
                        if (selectedImageBitmap!= null){
                            imageBitmap = selectedImageBitmap;
                            Log.i(TAG, "setupResultsLauncher: image =>" + imageBitmap);
                            Toast.makeText(this, "Image Picked Successfully", Toast.LENGTH_SHORT).show();
                            btn_pick_image.setBackgroundColor(getResources().getColor(R.color.special_green));
                        }else{
                            Toast.makeText(NewUserActivity.this, "What! No Image!", Toast.LENGTH_SHORT).show();
                            btn_pick_image.setBackgroundColor(getResources().getColor(R.color.shiny_red));
                        }
                    }else{
                        Toast.makeText(NewUserActivity.this, "No Image Got Picked", Toast.LENGTH_SHORT).show();
                        btn_pick_image.setBackgroundColor(getResources().getColor(R.color.shiny_red));
                    }
                }
            });


    //    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE) {
//            //TODO: action
//            final Bundle extras = data.getExtras();
//            if (extras != null) {
//                //Get image
//                Bitmap newProfilePic = extras.getParcelable("data");
//            }
//        }
//    }
    /*
        private void createUserByObject() {
            if (saveUserData()) {
                UserApiController.getInstance().create_user_object(new User(name, email, mobile, national_number, family_members, gender, bitmapToBytes()), new ProcessCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Log.i(TAG, "onSuccess: Created =>" + message);
                        Snack bar.make(findViewById(R.id.btn_submit_newUser), "Done Congregates ", .LENGTH_LONG).show();
                        onBackPressed();
                    }

                    @Override
                    public void onFailure(String massage) {
                        .make(findViewById(R.id.btn_submit_newUser), massage, .LENGTH_LONG).show();
                        Log.i(TAG, "onFailure: massage" + massage + "WTF");
                    }

                });
            }
        }


       */
    /*-
    private void createDummyUser() {
        imagePart = bitmapToBytes();
        UserApiController.getInstance().create_user("Fares", "gehenu@finews.biz", "0599741554", "123123123", "7", "M", bitmapToBytes(), new ProcessCallback() {
            @Override
            public void onSuccess(String message) {
                Log.i(TAG, "onSuccess: Created =>" + message);
                Snackbar.make(findViewById(R.id.btn_submit_newUser), "Dummy Congregates ", Snackbar.LENGTH_LONG).show();
                onBackPressed();
            }

            @Override
            public void onFailure(String massage) {
                Snackbar.make(findViewById(R.id.btn_submit_newUser), massage, Snackbar.LENGTH_LONG).show();
                Log.i(TAG, "Dummy" + massage + "WTF");
            }

        });
    }
     */
}