package org.school.housing.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText email_edit_text, password_edit_text;
    private Button login_button;
    private static final String TAG = "LoginActivity";
    //** i deleted the onCreate Method --there is no use of it

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeViews();
    }

    private void findViews() {
        login_button = findViewById(R.id.login_button);
        Log.d(TAG, "findViews() returned: " + login_button);
        email_edit_text = findViewById(R.id.email_edit_text);
        password_edit_text = findViewById(R.id.password_edit_text);
    }

    private void initializeViews() {
        findViews();
        Log.d(TAG, "initializeViews() returned: " + login_button);
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        login_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == login_button.getId()){
            performLogin();
        }
    }

    private void performLogin() {
        if (checkData()) {
            login();
        }
    }

    private boolean checkData() {
        if (!Objects.requireNonNull(email_edit_text.getText()).toString().isEmpty() &&
                !Objects.requireNonNull(password_edit_text.getText()).toString().isEmpty()) {
            return true;
        }
        Snackbar.make(findViewById(R.id.snackBar_action), "Enter Required Data", Snackbar.LENGTH_LONG).show();
        return false;
    }

    private void login(){
        String email = Objects.requireNonNull(email_edit_text.getText()).toString().trim();
        String password = Objects.requireNonNull(password_edit_text.getText()).toString().trim();
        AuthApiController.getInstance().login(email, password, new ProcessCallback() {
            @Override
            public void onSuccess(String message) {
                Log.d(TAG, "onSuccess() returned: " + message);
                //move if the login succeed
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }

            @Override
            public void onFailure(String massage) {
                Log.d(TAG, "onFailure() returned: " + massage);
                //show user the massage
                Snackbar.make(findViewById(R.id.snackBar_action),massage,Snackbar.LENGTH_LONG).show();
            }
        });
    }

}