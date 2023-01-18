package org.school.housing.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.school.housing.R;
import org.school.housing.adapters.adv_adapter.AdvertisementAdapter;
import org.school.housing.adapters.emp_adapter.EmployeeAdapter;
import org.school.housing.adapters.user_adapter.UserAdapter;
import org.school.housing.api.controllers.AuthApiController;
import org.school.housing.api.controllers.ContentApiController;
import org.school.housing.enums.Keys;
import org.school.housing.fragments.dialogs.LogoutDialog;
import org.school.housing.interfaces.ListCallback;
import org.school.housing.interfaces.ProcessCallback;
import org.school.housing.interfaces.dialog.DialogListener;
import org.school.housing.models.admin.Advertisement;
import org.school.housing.models.admin.Employee;
import org.school.housing.models.admin.User;
import org.school.housing.views.edit_forms.EditAdvActivity;
import org.school.housing.views.edit_forms.EditEmpActivity;
import org.school.housing.views.edit_forms.EditUserActivity;
import org.school.housing.views.forms.NewAdvActivity;
import org.school.housing.views.forms.NewEmpActivity;
import org.school.housing.views.forms.NewUserActivity;
import org.school.housing.views.password_form.PasswordActivity;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DialogListener {
    private static final String TAG = "MainActivity";
    private Button btn_newUser, btn_newEmp, btn_newAdv;
    private RecyclerView recyclerView_user,recyclerView_emp,recyclerView_adv;

    private UserAdapter userAdapter;
    private EmployeeAdapter employeeAdapter;
    private AdvertisementAdapter advertisementAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();
        inti();
        setupEveryRecyclerView();
    }
    private void setupEveryRecyclerView(){
        userRec();
        empRec();
        advRec();
    }
    private void userRec(){
        ContentApiController.getInstance().getUsers(new ListCallback<User>() {
            @Override
            public void onSuccess(List<User> list) {
                userAdapter = new UserAdapter(list);
                userAdapter.setRvClickListener(object -> {
                    Intent intent = new Intent(MainActivity.this,EditUserActivity.class);
                    intent.putExtra(Keys.UserKey.name(), (Serializable) object);
                    startActivity(intent);
                });
                recyclerView_user.setAdapter(userAdapter);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void empRec(){
        ContentApiController.getInstance().getEmployees(new ListCallback<Employee>() {
            @Override
            public void onSuccess(List<Employee> list) {
                employeeAdapter = new EmployeeAdapter(list);
                employeeAdapter.setRvClickListener(object -> {
                    Intent intent = new Intent(MainActivity.this,EditEmpActivity.class);
                    intent.putExtra(Keys.EmpKey.name(), (Serializable) object);
                    startActivity(intent);
                });
                recyclerView_emp.setAdapter(employeeAdapter);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void advRec(){
        ContentApiController.getInstance().getAdvertisements(new ListCallback<Advertisement>() {
            @Override
            public void onSuccess(List<Advertisement> list) {
                advertisementAdapter = new AdvertisementAdapter(list);
                advertisementAdapter.setRvClickListener(object -> {
                    Intent intent = new Intent(MainActivity.this,EditAdvActivity.class);
                    intent.putExtra(Keys.AdvKey.name(), (Serializable) object);
                    startActivity(intent);
                });
                recyclerView_adv.setAdapter(advertisementAdapter);
            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "onFailure() returned: " + message);
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inti() {
        findViews();
        setClick();
    }

    private void findViews() {
        recyclerView_user = findViewById(R.id.recycler_user);
        recyclerView_emp = findViewById(R.id.recycler_emp);
        recyclerView_adv = findViewById(R.id.recycler_adv);

        btn_newUser = findViewById(R.id.btn_newUser);
        btn_newEmp = findViewById(R.id.btn_newEmp);
        btn_newAdv = findViewById(R.id.btn_newAdv);
    }

    private void setClick() {
        btn_newUser.setOnClickListener(this);
        btn_newEmp.setOnClickListener(this);
        btn_newAdv.setOnClickListener(this);
    }

    //**THE logout Section----------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                new LogoutDialog().show(getSupportFragmentManager(),"LogOut");
                break;
            case R.id.menu_changePassword:
                setIntent(PasswordActivity.class);
                break;
            case R.id.menu_editUser:
                setIntent(EditUserActivity.class);
                break;
            case R.id.menu_editAdv:
                setIntent(EditAdvActivity.class);
                break;
            case R.id.menu_editEmp:
                setIntent(EditEmpActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        AuthApiController.getInstance().logout(new ProcessCallback() {
            @Override
            public void onSuccess(String message) {
                //performLogout and move to login
                Log.d(TAG, "onSuccess() returned: " + message);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }

            @Override
            public void onFailure(String massage) {
                Snackbar.make(findViewById(R.id.snackBar_action), massage, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_newUser:
                setIntent(NewUserActivity.class);
                break;
            case R.id.btn_newEmp:
                setIntent(NewEmpActivity.class);
                break;
            case R.id.btn_newAdv:
                setIntent(NewAdvActivity.class);
                break;
        }
    }

    private void setIntent(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    //this for okay btn in dialog
    @Override
    public void onConfirmClicked() {
        logout();
    }
}