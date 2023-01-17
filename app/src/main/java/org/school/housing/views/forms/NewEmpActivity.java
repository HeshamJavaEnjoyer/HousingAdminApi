package org.school.housing.views.forms;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.school.housing.R;
import org.school.housing.api.controllers.EmpApiController;
import org.school.housing.interfaces.ProcessCallback;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class NewEmpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NewEmpActivity";
    private TextInputEditText username_edt, mobile_edt, national_number_edt;
    private Button submit_button, btn_pick_image;
    private String name, mobile, national_number;
    //name mobile nationalNum -Image
    private ActivityResultLauncher<String> permissionResultLauncher;
    private ActivityResultLauncher<Void> cameraResultLauncher;
    private Bitmap imageBitmap;
    private byte[] imagePart = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_emp);
        setupResultsLauncher();
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        findViews();
        setClicks();
    }

    private void findViews() {
        username_edt = findViewById(R.id.username_edt);
        mobile_edt = findViewById(R.id.mobile_edt);
        national_number_edt = findViewById(R.id.national_number_edt);
        btn_pick_image = findViewById(R.id.btnPickImage);
        submit_button = findViewById(R.id.btn_submit_newEmp);
    }

    private void setClicks() {
        btn_pick_image.setOnClickListener(this);
        submit_button.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPickImage:
                Toast.makeText(this, "Oi", Toast.LENGTH_SHORT).show();
                pickImage();
                break;
            case R.id.btn_submit_newEmp:
                performCreate();
                break;
        }
    }

    //CREATININE
    private void performCreate() {
        if (checkData()) {
            createEmployee();
        }else {
            Snackbar.make(findViewById(R.id.snackBar_action),"Check Entry Data!",Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean checkData() {
        return !Objects.requireNonNull(username_edt.getText()).toString().isEmpty() &&
                !Objects.requireNonNull(mobile_edt.getText()).toString().isEmpty() &&
                !Objects.requireNonNull(national_number_edt.getText()).toString().isEmpty()
                && imageBitmap != null;
    }

    private void createEmployee() {
        if (saveEntryData()) {
            EmpApiController.getInstance().createEmployeeMap(name, mobile, national_number, imagePart, new ProcessCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(NewEmpActivity.this, "DoneSuccessfully =>" + message, Toast.LENGTH_SHORT).show();
                    username_edt.setText("");
                    mobile_edt.setText("");
                    national_number_edt.setText("");
                }

                @Override
                public void onFailure(String massage) {
                    Snackbar.make(findViewById(R.id.snackBar_action), massage, Snackbar.LENGTH_LONG).show();
                    Log.d(TAG, "onFailure() returned: " + massage);
                }
            });
        }
    }

    private boolean saveEntryData() {
        name = Objects.requireNonNull(username_edt.getText()).toString();
        mobile = Objects.requireNonNull(mobile_edt.getText()).toString();
        national_number = Objects.requireNonNull(national_number_edt.getText()).toString();
        imagePart = bitmapToBytes();

        Log.i(TAG, "saveEmpData: name :=> " + name + " mobile " + mobile + " national_number " + national_number +" imagePart " + imagePart.length);

        return true;
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
                Toast.makeText(NewEmpActivity.this, "False", Toast.LENGTH_SHORT).show();
            }
        });

        cameraResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), result -> {
            if (result != null) {
                imageBitmap = result;
                Log.i(TAG, "setupResultsLauncher: image =>" + imageBitmap);
                Toast.makeText(this, "Image Picked Successfully", Toast.LENGTH_SHORT).show();
                btn_pick_image.setBackgroundColor(getResources().getColor(R.color.special_green));
            } else {
                Toast.makeText(NewEmpActivity.this, "No Image Got Picked", Toast.LENGTH_SHORT).show();
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