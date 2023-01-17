package org.school.housing.views.forms;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.school.housing.R;
import org.school.housing.api.controllers.AdvApiController;
import org.school.housing.interfaces.ProcessCallback;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class NewAdvActivity extends AppCompatActivity implements View.OnClickListener {
    // Title Info -=Image=-
    private static final String TAG = "NewEmpActivity";
    private TextInputEditText title_edt, info_edt;
    private Button submit_button, btn_pick_image;
    private String title, info;
    //-Image
    private ActivityResultLauncher<String> permissionResultLauncher;
    private ActivityResultLauncher<Void> cameraResultLauncher;
    private Bitmap imageBitmap;
    private byte[] imagePart = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_adv);
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
        title_edt = findViewById(R.id.title_edt);
        info_edt = findViewById(R.id.info_edt);
        btn_pick_image = findViewById(R.id.btnPickImage);
        submit_button = findViewById(R.id.btn_submit_newAdv);
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
                Toast.makeText(this, "Pick An Image if you want", Toast.LENGTH_SHORT).show();
                pickImage();
                break;
            case R.id.btn_submit_newAdv:
                performCreate();
                break;
        }
    }

    //CREATININE
    private void performCreate() {
        if (checkData()) {
            createAdv();
        }else {
            Snackbar.make(findViewById(R.id.snackBar_action),"Check Entry Data!",Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean checkData() {
        return !Objects.requireNonNull(title_edt.getText()).toString().isEmpty() &&
                !Objects.requireNonNull(info_edt.getText()).toString().isEmpty() ;
    }

    private void createAdv() {
        if (saveEntryData()) {
            AdvApiController.getInstance().storeAdv(title, info,imagePart, new ProcessCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(NewAdvActivity.this, "DoneSuccessfully =>" + message, Toast.LENGTH_SHORT).show();
                    title_edt.setText("");
                    info_edt.setText("");
                }

                @Override
                public void onFailure(String massage) {
                    Snackbar.make(findViewById(R.id.snackBar_action), massage, Snackbar.LENGTH_LONG).show();
                    Log.d(TAG, "onFailure() returned: " + massage);
                }
            });
        }else {
            AdvApiController.getInstance().storeAdv_noImage(title, info, new ProcessCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(NewAdvActivity.this, "DoneSuccessfully =>" + message, Toast.LENGTH_SHORT).show();
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

    private boolean saveEntryData() {
        title = Objects.requireNonNull(title_edt.getText()).toString();
        info = Objects.requireNonNull(info_edt.getText()).toString();
        if (imageBitmap != null){
            imagePart = bitmapToBytes();
            Log.i(TAG, "saveAdvData: title :=> " + title + " info " + info +" imagePart " + imagePart.length);
        }else {
            Log.i(TAG, "saveAdvData: title :=> " + title + " info " + info );
            return false;
        }

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
                Toast.makeText(NewAdvActivity.this, "False", Toast.LENGTH_SHORT).show();
            }
        });

        cameraResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), result -> {
            if (result != null) {
                imageBitmap = result;
                Log.i(TAG, "setupResultsLauncher: image =>" + imageBitmap);
                Toast.makeText(this, "Image Picked Successfully", Toast.LENGTH_SHORT).show();
                btn_pick_image.setBackgroundColor(getResources().getColor(R.color.special_green));
            } else {
                Toast.makeText(NewAdvActivity.this, "No Image Got Picked", Toast.LENGTH_SHORT).show();
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