package org.school.housing.views.password_form;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.school.housing.R;
import org.school.housing.api.controllers.AuthApiController;
import org.school.housing.interfaces.ProcessCallback;

import java.util.Objects;

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "PasswordActivity";
    private TextInputEditText mobile_edit_text;
    private Button btn_forgetPass_mobileSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeViews();
    }

    private void findViews() {
        mobile_edit_text = findViewById(R.id.mobile_edit_text);
        btn_forgetPass_mobileSubmit = findViewById(R.id.btn_forgetPass_mobileSubmit);
    }

    private void initializeViews() {
        findViews();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        btn_forgetPass_mobileSubmit.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_forgetPass_mobileSubmit) {
            performRequest();
        }
    }
    private void performRequest(){
        if (checkData()){
            forgetPasswordRequest();
        }else {
            Snackbar.make(findViewById(R.id.snackBar_action),"Fill Empty Field",Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean checkData() {
        return !Objects.requireNonNull(mobile_edit_text.getText()).toString().trim().isEmpty();
    }

    private void forgetPasswordRequest() {
       String mobile = Objects.requireNonNull(mobile_edit_text.getText()).toString().trim();
        AuthApiController.getInstance().forget_password(mobile, new ProcessCallback() {
            @Override
            public void onSuccess(String message) {
                Log.d(TAG, "onSuccess() returned: " + message);
                Snackbar.make(findViewById(R.id.snackBar_action),message,Snackbar.LENGTH_LONG).setAction("Back", view -> onBackPressed()).show();
            }

            @Override
            public void onFailure(String massage) {
                Snackbar.make(findViewById(R.id.snackBar_action),massage,Snackbar.LENGTH_LONG).setAction("Try Again", view -> performRequest()).show();
            }
        });
    }

}