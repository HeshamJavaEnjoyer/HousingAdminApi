package org.school.housing.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.school.housing.R;
import org.school.housing.api.controllers.AuthApiController;
import org.school.housing.interfaces.ProcessCallback;
import org.school.housing.views.password_form.PasswordActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText email_edit_text, password_edit_text;
    private Button login_button;
    private TextView tv_forgetPassword, tv_createAccount;
    private View view_ic_password_eye;
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
        view_ic_password_eye = findViewById(R.id.view_ic_password_eye);

        tv_createAccount = findViewById(R.id.tv_create_account);
        tv_forgetPassword = findViewById(R.id.tv_forget_password);

        login_button = findViewById(R.id.login_button);

        email_edit_text = findViewById(R.id.email_edit_text);
        password_edit_text = findViewById(R.id.password_edit_text);
    }

    private void initializeViews() {
        findViews();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        view_ic_password_eye.setOnClickListener(this);

        tv_createAccount.setOnClickListener(this);
        tv_forgetPassword.setOnClickListener(this);

        login_button.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_ic_password_eye:
                changePasswordInputType();
                break;
            case R.id.login_button:
                performLogin();
                break;
            case R.id.tv_create_account:
                goToRegisterUrl();
                break;
            case R.id.tv_forget_password:
                goToPasswordAct();
                break;
        }
    }

    private void changePasswordInputType() {
        //Dose Not work properly
        password_edit_text.setInputType(
                password_edit_text.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                ? (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                : (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD));
    }

    private void goToRegisterUrl() {
        Uri uriUrl = Uri.parse("https://towers.mr-dev.tech/admin/register");
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    private void goToPasswordAct() {
        startActivity(new Intent(this, PasswordActivity.class));
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

    private void login() {
        String email = Objects.requireNonNull(email_edit_text.getText()).toString().trim();
        String password = Objects.requireNonNull(password_edit_text.getText()).toString().trim();
        AuthApiController.getInstance().login(email, password, new ProcessCallback() {
            @Override
            public void onSuccess(String message) {
                Log.d(TAG, "onSuccess() returned: " + message);
                //move if the login succeed
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(String massage) {
                Log.d(TAG, "onFailure() returned: " + massage);
                //show user the massage
                Snackbar.make(findViewById(R.id.snackBar_action), massage, Snackbar.LENGTH_LONG).show();
            }
        });
    }

}