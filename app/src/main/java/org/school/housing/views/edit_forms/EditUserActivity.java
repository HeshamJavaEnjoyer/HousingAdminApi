package org.school.housing.views.edit_forms;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.school.housing.R;
import org.school.housing.api.controllers.ContentApiController;
import org.school.housing.api.controllers.UserApiController;
import org.school.housing.enums.Keys;
import org.school.housing.interfaces.ListCallback;
import org.school.housing.interfaces.ProcessCallback;
import org.school.housing.models.admin.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditUserActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EditUserActivity";
    private TextInputEditText username_edt, email_edt, mobile_edt, national_number_edt, family_members_edt;
    private Button submit_button, btn_pick_image, btn_deleteUser;

    private Spinner dropdown;
    private RadioGroup gender_radioGroup;
    private String name, email, mobile, national_number, family_members;
    private String gender;

    //    private ActivityResultLauncher<Void> galleryResultLauncher;
    private ActivityResultLauncher<String> permissionResultLauncher;
    private ActivityResultLauncher<Void> cameraResultLauncher;
    private Bitmap imageBitmap;
    private byte[] imagePart = null;

    private int id;
    private List<User> items = new ArrayList<>();
    private User mIntentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        setupResultsLauncher();

    }

    private void setIntentUser() {
       mIntentUser = (User) getIntent().getSerializableExtra(Keys.UserKey.name());
        if (mIntentUser != null) {
            Log.d(TAG, "setIntentUser() returned: " + mIntentUser);
            Log.d(TAG, "setIntentUser() returned: " + mIntentUser.id);
            Log.d(TAG, "setIntentUser() returned: " + mIntentUser.name);
            username_edt.setText(mIntentUser.name);
            email_edt.setText(mIntentUser.email);
            mobile_edt.setText(mIntentUser.mobile);
            national_number_edt.setText(mIntentUser.nationalNumber);
            family_members_edt.setText(mIntentUser.familyMembers);
        }else {
            Log.d(TAG, "setIntentUser() returned: object is " + false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        inti();
    }

    private void inti() {
        findViews();
        setEveryOnClick();
        setIntentUser();

        if (mIntentUser == null){
            setupSpinnerAdapter();
        }else {
            dropdown.setEnabled(false);
        }

        controlIdSelection();

        controlGenderSelection();

    }

    private void findViews() {
        dropdown = findViewById(R.id.dropdown_menu_item_id);
        btn_deleteUser = findViewById(R.id.btn_deleteUser);

        username_edt = findViewById(R.id.username_edt);
        email_edt = findViewById(R.id.email_edt);
        mobile_edt = findViewById(R.id.mobile_edt);
        national_number_edt = findViewById(R.id.national_number_edt);
        family_members_edt = findViewById(R.id.family_members_edt);

        gender_radioGroup = findViewById(R.id.gender_radio_group);

        btn_pick_image = findViewById(R.id.btnPickImage);
        submit_button = findViewById(R.id.btn_submit_newUser);
    }

    private void setupSpinnerAdapter() {
        ContentApiController.getInstance().getUsers(new ListCallback<User>() {
            @Override
            public void onSuccess(List<User> list) {
                items = list;
            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "onFailure() returned: " + message);
            }
        });
        ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

    private void setEveryOnClick() {
        btn_deleteUser.setOnClickListener(this);

        btn_pick_image.setOnClickListener(this);
        submit_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == submit_button.getId()) {
            performUpdate();
        } else if (view.getId() == btn_pick_image.getId()) {
            pickImage();
        } else if (view.getId() == btn_deleteUser.getId()) {
            performDeleteUser();
        }
    }

    private void performDeleteUser() {
        if (!String.valueOf(id).isEmpty())
            UserApiController.getInstance().delete_user(id, new ProcessCallback() {
                @Override
                public void onSuccess(String message) {
                    submit_button.setEnabled(false);
                    btn_deleteUser.setBackgroundColor(getResources().getColor(R.color.special_green));
                    Snackbar.make(findViewById(R.id.btn_submit_newUser), "Deleted Successfully=> " + message, Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(String massage) {
                    Snackbar.make(findViewById(R.id.btn_submit_newUser), "Failed to Delete => " + massage, Snackbar.LENGTH_LONG).show();
                }
            });
    }

    private boolean checkData() {
        return !Objects.requireNonNull(username_edt.getText()).toString().isEmpty() && !Objects.requireNonNull(email_edt.getText()).toString().isEmpty() && !Objects.requireNonNull(mobile_edt.getText()).toString().isEmpty() && !Objects.requireNonNull(national_number_edt.getText()).toString().isEmpty() && !Objects.requireNonNull(family_members_edt.getText()).toString().isEmpty()
                && id != -1;
    }

    private boolean saveUserData() {
        //UPDATE PURPOSE
        id = controlIdSelection();

        name = Objects.requireNonNull(username_edt.getText()).toString();
        email = Objects.requireNonNull(email_edt.getText()).toString();
        mobile = Objects.requireNonNull(mobile_edt.getText()).toString();
        national_number = Objects.requireNonNull(national_number_edt.getText()).toString();
        family_members = Objects.requireNonNull(family_members_edt.getText()).toString();
        gender = controlGenderSelection();

        if (imageBitmap != null) {
            imagePart = bitmapToBytes();
            Log.i(TAG, "saveUserData: name :=> " + name + " email " + email + " mobile " + mobile + " national_number " + national_number + " gender " + gender + " imagePart " + imagePart.length);
        } else {
            Log.i(TAG, "saveUserData: name :=> " + name + " email " + email + " mobile " + mobile + " national_number " + national_number + " gender " + gender);
            return false;
        }
        return true;
    }

    private int controlIdSelection() {
        if (mIntentUser != null){
            id = mIntentUser.id;
        }else {
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d(TAG, "onItemSelected() returned: " + i);
                    id = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    id = 1;
                }
            });
        }
        Log.d(TAG, "controlIdSelection() ID: " + id);
        return id;
    }

    private String controlGenderSelection() {
        gender_radioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> gender = checkedId == R.id.male_radio_button ? "M" : "F");
        return gender;
    }

    private void performUpdate() {
        if (checkData()) {
            updateUser();
        } else {
            Snackbar.make(findViewById(R.id.btn_submit_newUser), "Check empty  fields", Snackbar.LENGTH_LONG).show();
        }
    }

    private void updateUser() {
        if (saveUserData()) {
            //==Notice the id
            UserApiController.getInstance().update_user_map(id, name, email, mobile, national_number, family_members, gender, imagePart, new ProcessCallback() {
                @Override
                public void onSuccess(String message) {
                    Log.i(TAG, "onSuccess: Updated =>" + message);
                    Snackbar.make(findViewById(R.id.btn_submit_newUser), "Done Congregates ", Snackbar.LENGTH_LONG).show();
                    onBackPressed();
                }

                @Override
                public void onFailure(String massage) {
                    Log.i(TAG, "onFailure: ");
                    Snackbar.make(findViewById(R.id.btn_submit_newUser), massage, Snackbar.LENGTH_LONG).show();
                }

            });
        } else {
            //Here No Pic
            UserApiController.getInstance().update_user_no_pic_map(id, name, email, mobile, national_number, family_members, gender, new ProcessCallback() {
                @Override
                public void onSuccess(String message) {
                    Log.i(TAG, "onSuccess: Updated =>" + message);
                    Snackbar.make(findViewById(R.id.btn_submit_newUser), "Done Congregates ", Snackbar.LENGTH_LONG).show();
                    onBackPressed();
                }

                @Override
                public void onFailure(String massage) {
                    Log.i(TAG, "onFailure: " + massage);
                    Snackbar.make(findViewById(R.id.btn_submit_newUser), massage, Snackbar.LENGTH_LONG).show();
                }

            });
        }
    }


    //**Image Thing
    private void pickImage() {
        permissionResultLauncher.launch(Manifest.permission.CAMERA);
    }

    private void setupResultsLauncher() {
        permissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) {
                cameraResultLauncher.launch(null);
            } else {
                Toast.makeText(EditUserActivity.this, "False", Toast.LENGTH_SHORT).show();
            }
        });

        cameraResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), result -> {
            if (result != null) {
                imageBitmap = result;
                Log.i(TAG, "setupResultsLauncher: image =>" + imageBitmap);
                Toast.makeText(this, "Image Picked Successfully", Toast.LENGTH_SHORT).show();
                btn_pick_image.setBackgroundColor(getResources().getColor(R.color.special_green));
            } else {
                Toast.makeText(EditUserActivity.this, "No Image Got Picked", Toast.LENGTH_SHORT).show();
                btn_pick_image.setBackgroundColor(getResources().getColor(R.color.shiny_red));
            }
        });
    }

    private byte[] bitmapToBytes() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}