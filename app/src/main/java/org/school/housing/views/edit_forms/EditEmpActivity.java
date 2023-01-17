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
import org.school.housing.api.controllers.ContentApiController;
import org.school.housing.api.controllers.EmpApiController;
import org.school.housing.enums.Keys;
import org.school.housing.interfaces.ListCallback;
import org.school.housing.interfaces.ProcessCallback;
import org.school.housing.models.admin.Employee;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditEmpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EditEmpActivity";

    private TextInputEditText username_edt, mobile_edt, national_number_edt;
    private Button submit_button, btn_pick_image, btn_deleteEmp;
    private String name, mobile, national_number;

    private Spinner dropdown;
    private int id;
    private List<Employee> items = new ArrayList<>();


    //name mobile nationalNum -Image
    private ActivityResultLauncher<String> permissionResultLauncher;
    private ActivityResultLauncher<Void> cameraResultLauncher;
    private Bitmap imageBitmap;
    private byte[] imagePart = null;

    private Employee mIntentEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_emp);
        setupResultsLauncher();
    }

    private void setIntentEmployee() {
        mIntentEmployee = (Employee) getIntent().getSerializableExtra(Keys.EmpKey.name());
        if (mIntentEmployee != null) {
            Log.d(TAG, "setIntentEmp() Objects " + mIntentEmployee);
            Log.d(TAG, "setIntentEmp() Objects Id: " + mIntentEmployee.id);
            Log.d(TAG, "setIntentEmp() Objects Name: " + mIntentEmployee.name);
            username_edt.setText(mIntentEmployee.name);
            mobile_edt.setText(mIntentEmployee.mobile);
            national_number_edt.setText(mIntentEmployee.nationalNumber);
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
        setIntentEmployee();

        if (mIntentEmployee == null) {
            setupSpinnerAdapter();
        } else {
            dropdown.setEnabled(false);
        }

        controlIdSelection();

    }

    private void findViews() {
        dropdown = findViewById(R.id.dropdown_menu_item_id);
        btn_deleteEmp = findViewById(R.id.btn_deleteEmp);

        username_edt = findViewById(R.id.username_edt);
        mobile_edt = findViewById(R.id.mobile_edt);
        national_number_edt = findViewById(R.id.national_number_edt);
        btn_pick_image = findViewById(R.id.btnPickImage);
        submit_button = findViewById(R.id.btn_submit_newEmp);
    }

    private void setupSpinnerAdapter() {
        ContentApiController.getInstance().getEmployees(new ListCallback<Employee>() {
            @Override
            public void onSuccess(List<Employee> list) {
                items = list;
            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "onFailure() returned: " + message);
            }
        });
        ArrayAdapter<Employee> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

    private void setClicks() {
        btn_deleteEmp.setOnClickListener(this);
        btn_pick_image.setOnClickListener(this);
        submit_button.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPickImage:
                Toast.makeText(this, "To The Image", Toast.LENGTH_SHORT).show();
                pickImage();
                break;
            case R.id.btn_submit_newEmp:
                performUpdate();
                break;
            case R.id.btn_deleteEmp:
                performDeleteEmp();
                break;
        }
    }


    //Update Thing
    private void performUpdate() {
        if (checkData()) {
            updateEmployee();
        } else {
            Snackbar.make(findViewById(R.id.snackBar_action), "Check Entry Data!", Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean checkData() {
        return !Objects.requireNonNull(username_edt.getText()).toString().isEmpty() &&
                !Objects.requireNonNull(mobile_edt.getText()).toString().isEmpty() &&
                !Objects.requireNonNull(national_number_edt.getText()).toString().isEmpty()
                && imageBitmap != null
                && id != -1;
    }

    private void updateEmployee() {
        if (saveEntryData()) {
            EmpApiController.getInstance().updateEmployeeMap(id, name, mobile, national_number, imagePart, new ProcessCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(EditEmpActivity.this, "DoneSuccessfully =>" + message, Toast.LENGTH_SHORT).show();
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
        /*No Request of Empty Pic if so unCommit
        else {
            EmpApiController.getInstance().updateEmployeeMap(id, name, mobile, national_number, null, new ProcessCallback() {
                @Override
                public void onSuccess(String message) {

                }

                @Override
                public void onFailure(String massage) {

                }
            });
        }*/
    }

    private void performDeleteEmp() {
        if (!String.valueOf(id).isEmpty()){
            EmpApiController.getInstance().deleteEmployee(id, new ProcessCallback() {
                @Override
                public void onSuccess(String message) {
                    submit_button.setEnabled(false);
                    btn_deleteEmp.setBackgroundColor(getResources().getColor(R.color.special_green));
                    Snackbar.make(findViewById(R.id.btn_submit_newEmp), "Deleted Successfully=> " + message, Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(String massage) {
                    Snackbar.make(findViewById(R.id.btn_submit_newEmp), "failed to => " + massage, Snackbar.LENGTH_LONG).show();
                }
            });
        }else {
            Snackbar.make(findViewById(R.id.btn_submit_newEmp), "Select a valid Id !", Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean saveEntryData() {
        id = controlIdSelection();

        name = Objects.requireNonNull(username_edt.getText()).toString();
        mobile = Objects.requireNonNull(mobile_edt.getText()).toString();
        national_number = Objects.requireNonNull(national_number_edt.getText()).toString();

        if (imageBitmap != null) {
            imagePart = bitmapToBytes();
            Log.i(TAG, "saveEmpData: name :=> " + name + " mobile " + mobile + " national_number " + national_number + " imagePart " + imagePart.length);
        } else {
            Log.i(TAG, "saveEmpData: name :=> " + name + " mobile " + mobile + " national_number " + national_number);
            return false;
        }

        return true;
    }

    private int controlIdSelection() {
        if (mIntentEmployee != null){
            id = mIntentEmployee.id;
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


    //IMAGE
    private void pickImage() {
        permissionResultLauncher.launch(Manifest.permission.CAMERA);
    }

    private void setupResultsLauncher() {
        permissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) {
                cameraResultLauncher.launch(null);
            } else {
                Toast.makeText(EditEmpActivity.this, "False", Toast.LENGTH_SHORT).show();
            }
        });

        cameraResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), result -> {
            if (result != null) {
                imageBitmap = result;
                Log.i(TAG, "setupResultsLauncher: image =>" + imageBitmap);
                Toast.makeText(this, "Image Picked Successfully", Toast.LENGTH_SHORT).show();
                btn_pick_image.setBackgroundColor(getResources().getColor(R.color.special_green));
            } else {
                Toast.makeText(EditEmpActivity.this, "No Image Got Picked", Toast.LENGTH_SHORT).show();
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