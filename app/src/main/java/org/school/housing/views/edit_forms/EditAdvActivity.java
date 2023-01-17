package org.school.housing.views.edit_forms;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.school.housing.R;
import org.school.housing.api.controllers.AdvApiController;
import org.school.housing.api.controllers.ContentApiController;
import org.school.housing.enums.Keys;
import org.school.housing.interfaces.ListCallback;
import org.school.housing.interfaces.ProcessCallback;
import org.school.housing.models.admin.Advertisement;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditAdvActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EditAdvActivity";
    private TextInputEditText title_edt, info_edt;
    private Button submit_button, btn_pick_image, btn_deleteAdv;
    private String title, info;

    private Spinner dropdown;
    private int id;
    private List<Advertisement> items = new ArrayList<>();

    //-Image
    private ActivityResultLauncher<String> permissionResultLauncher;
    private ActivityResultLauncher<Void> cameraResultLauncher;
    private Bitmap imageBitmap;
    private byte[] imagePart = null;

    private Advertisement mIntentAdv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_adv);
        setupResultsLauncher();
    }

    private void setIntentAdv() {
        mIntentAdv = (Advertisement) getIntent().getSerializableExtra(Keys.EmpKey.name());
        if (mIntentAdv != null) {
            Log.d(TAG, "setIntentEmp() Objects " + mIntentAdv);
            Log.d(TAG, "setIntentEmp() Objects Id: " + mIntentAdv.id);
            Log.d(TAG, "setIntentEmp() Objects Title: " + mIntentAdv.title);
            title_edt.setText(mIntentAdv.title);
            info_edt.setText(mIntentAdv.info);
        } else {
            Log.d(TAG, "setIntentEmp() returned: object is " + false);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        findViews();
        setClicks();
        setIntentAdv();

        if (mIntentAdv == null) {
            setupSpinnerAdapter();
        } else {
            dropdown.setEnabled(false);
        }

        controlIdSelection();

    }

    private void setupSpinnerAdapter() {
        ContentApiController.getInstance().getAdvertisements(new ListCallback<Advertisement>() {
            @Override
            public void onSuccess(List<Advertisement> list) {
                items = list;
                ArrayAdapter<Advertisement> adapter = new ArrayAdapter<>(EditAdvActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
                dropdown.setAdapter(adapter);
            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "onFailure() returned: " + message);
            }
        });

    }

    private void findViews() {
        dropdown = findViewById(R.id.dropdown_menu_item_id);
        btn_deleteAdv = findViewById(R.id.btn_deleteAdv);

        title_edt = findViewById(R.id.title_edt);
        info_edt = findViewById(R.id.info_edt);
        btn_pick_image = findViewById(R.id.btnPickImage);
        submit_button = findViewById(R.id.btn_submit_newAdv);
    }

    private void setClicks() {
        btn_deleteAdv.setOnClickListener(this);

        btn_pick_image.setOnClickListener(this);
        submit_button.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPickImage:
                Toast.makeText(this, "Pick An Image if you want", Toast.LENGTH_SHORT).show();
                pickImage();
                break;
            case R.id.btn_submit_newAdv:
                performUpdate();
                break;
            case R.id.btn_deleteAdv:
                performDelete();
                break;
        }
    }

    //CREATININE
    private void performUpdate() {
        if (checkData()) {
            updateAdv();
        } else {
            Snackbar.make(findViewById(R.id.snackBar_action), "Check Entry Data!", Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean checkData() {
        return !Objects.requireNonNull(title_edt.getText()).toString().isEmpty() &&
                !Objects.requireNonNull(info_edt.getText()).toString().isEmpty();
    }

    private void updateAdv() {
        if (saveEntryData()) {
            AdvApiController.getInstance().updateAdv(id, title, info, imagePart, new ProcessCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(EditAdvActivity.this, "DoneSuccessfully =>" + message, Toast.LENGTH_SHORT).show();
                    title_edt.setText("");
                    info_edt.setText("");
                }

                @Override
                public void onFailure(String massage) {
                    Snackbar.make(findViewById(R.id.snackBar_action), massage, Snackbar.LENGTH_LONG).show();
                    Log.d(TAG, "onFailure() returned: " + massage);
                }
            });
        } else {
            AdvApiController.getInstance().updateAdv_noImage(id, title, info, new ProcessCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(EditAdvActivity.this, "DoneSuccessfully =>" + message, Toast.LENGTH_SHORT).show();
                    title_edt.setText("");
                    info_edt.setText("");
                }

                @Override
                public void onFailure(String massage) {
                    Snackbar.make(findViewById(R.id.snackBar_action), massage, Snackbar.LENGTH_LONG).show();
                    Log.d(TAG, "onFailure() returned: " + massage);
                }
            });
        }
    }

    private void performDelete() {
        if (!String.valueOf(id).isEmpty()){
            AdvApiController.getInstance().deleteAdv(id, new ProcessCallback() {
                @Override
                public void onSuccess(String message) {
                    submit_button.setEnabled(false);
                    btn_deleteAdv.setBackgroundColor(getResources().getColor(R.color.special_green));
                    Snackbar.make(findViewById(R.id.btn_submit_newAdv), "Deleted Successfully=> " + message, Snackbar.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(String massage) {
                    Snackbar.make(findViewById(R.id.btn_submit_newAdv), "failed to => " + massage, Snackbar.LENGTH_LONG).show();

                }
            });
        }else {
            Snackbar.make(findViewById(R.id.btn_submit_newEmp), "Select a valid Id !", Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean saveEntryData() {
        id = controlIdSelection();

        title = Objects.requireNonNull(title_edt.getText()).toString();
        info = Objects.requireNonNull(info_edt.getText()).toString();
        if (imageBitmap != null) {
            imagePart = bitmapToBytes();
            Log.i(TAG, "saveAdvData: title :=> " + title + " info " + info + " imagePart " + imagePart.length);
        } else {
            Log.i(TAG, "saveAdvData: title :=> " + title + " info " + info);
            return false;
        }

        return true;
    }

    private int controlIdSelection() {
        if (mIntentAdv != null) {
            id = mIntentAdv.id;
        } else {
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


    //IMAGE
    private void pickImage() {
        permissionResultLauncher.launch(Manifest.permission.CAMERA);
    }

    private void setupResultsLauncher() {
        permissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) {
                cameraResultLauncher.launch(null);
            } else {
                Toast.makeText(EditAdvActivity.this, "False", Toast.LENGTH_SHORT).show();
            }
        });

        cameraResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), result -> {
            if (result != null) {
                imageBitmap = result;
                Log.i(TAG, "setupResultsLauncher: image =>" + imageBitmap);
                Toast.makeText(this, "Image Picked Successfully", Toast.LENGTH_SHORT).show();
                btn_pick_image.setBackgroundColor(getResources().getColor(R.color.special_green));
            } else {
                Toast.makeText(EditAdvActivity.this, "No Image Got Picked", Toast.LENGTH_SHORT).show();
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