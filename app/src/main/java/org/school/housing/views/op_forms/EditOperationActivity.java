package org.school.housing.views.op_forms;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.school.housing.R;
import org.school.housing.api.controllers.ContentApiController;
import org.school.housing.api.controllers.OperationApiController;
import org.school.housing.enums.ActorType;
import org.school.housing.enums.Keys;
import org.school.housing.fragments.dialogs.DeleteConfirmationDialog;
import org.school.housing.interfaces.ListCallback;
import org.school.housing.interfaces.ProcessCallback;
import org.school.housing.interfaces.dialog.DialogListener;
import org.school.housing.models.Operation;
import org.school.housing.models.admin.Employee;
import org.school.housing.models.admin.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class EditOperationActivity extends AppCompatActivity implements View.OnClickListener, DialogListener {
    private static final String TAG = "EditOperationActivity";
    private Button btn_submit, btn_deleteOp;
    private TextInputEditText amount_edt, details_edt;

    private Spinner spinner_categoryId, spinner_actorId, spinner_actorType, spinner_opId;
    //----------------containers
    private String cateId, amount, details, actor_id, actor_type, date;

    //-----spinners
    private final List<String> opIds = new ArrayList<>();

    private final List<String> empIds = new ArrayList<>();
    private final List<String> userIds = new ArrayList<>();

    //For Update Purpose
    private int id;
    private Operation mIntentOperation;

    private boolean sp_ac_enabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_operation);
        setActBar();
    }

    private void setIntentOperation() {
        mIntentOperation = (Operation) getIntent().getSerializableExtra(Keys.OperaKey.name());
        if (mIntentOperation != null) {
            spinner_categoryId.setEnabled(false);
            //cateId, amount, details, actor_id, actor_type, date;
            id = mIntentOperation.id;
            details_edt.setText(mIntentOperation.details);
            amount_edt.setText(mIntentOperation.amount);
            cateId = mIntentOperation.categoryId;
            actor_id = String.valueOf(mIntentOperation.actorId);

            spinner_actorId.setVisibility(View.INVISIBLE);
            spinner_actorType.setVisibility(View.INVISIBLE);
        } else {
            controlSpinnerChoose();
            Log.d(TAG, "setIntentEmp() returned: object is " + false);
        }
    }

    //For ActionBar BackButton(Arrow)
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //For ActionBar BackButton(Arrow)
    private void setActBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //for Enabling the ->getting back to home Act Method
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        findViews();
        setEveryClicks();

        setIntentOperation();

    }

    private void findViews() {
        //update purposes
        spinner_opId = findViewById(R.id.spinner_opId);


        spinner_categoryId = findViewById(R.id.spinner_category_id);
        amount_edt = findViewById(R.id.amount_edt);
        details_edt = findViewById(R.id.details_edt);
        spinner_actorId = findViewById(R.id.spinner_actor_id);
        spinner_actorType = findViewById(R.id.spinner_actor_type);

        btn_submit = findViewById(R.id.btn_submit_newOperation);
        btn_deleteOp = findViewById(R.id.btn_deleteOperation);
    }

    private void setEveryClicks() {
        btn_submit.setOnClickListener(this);
        btn_deleteOp.setOnClickListener(this);
    }

    private void controlSpinnerChoose() {
        spinner_categoryId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        Toast.makeText(EditOperationActivity.this, "Resident SoBe It", Toast.LENGTH_SHORT).show();
                        setSpinner_actorIdResident();
                        spinner_actorId.setEnabled(true);
                        break;
                    case 1:
                        Toast.makeText(EditOperationActivity.this, "Employee SoBe It", Toast.LENGTH_SHORT).show();
                        setSpinner_actorIdEmp();
                        spinner_actorId.setEnabled(true);
                        break;
                    default:
                        sp_ac_enabled = false;
                        spinner_actorId.setEnabled(false);
                        spinner_actorType.setEnabled(false);

                        spinner_actorId.setVisibility(View.INVISIBLE);
                        spinner_actorType.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_deleteOperation:
                new DeleteConfirmationDialog().show(getSupportFragmentManager(), "DeleteOperation");
                break;
            case R.id.btn_submit_newOperation:
                performUpdateForOp();
                break;
        }
    }

    private int setOperationIdValue() {
        if (mIntentOperation != null) {
            id = mIntentOperation.id;
        } else {
            setupSpinnerOperationAdapter();
            opSelectedId();
        }
        return id;
    }

    private boolean saveUserEntryFromUi() {

        id = setOperationIdValue();

        cateId = setCateIdValue();

        final String user_amount = Objects.requireNonNull(amount_edt.getText()).toString().trim();
        if (!user_amount.isEmpty()) {
            amount = user_amount;
        } else {
            Log.d(TAG, "user_amount() null");
            return false;
        }

        final String user_details = Objects.requireNonNull(details_edt.getText()).toString().trim();
        if (!user_details.isEmpty()) {
            details = user_details;
        } else {
            Log.d(TAG, "user_details() null");
            return false;
        }

        actor_id = setActorId();

        if (sp_ac_enabled) {
            spinner_actorId.setVisibility(View.INVISIBLE);
            spinner_actorType.setVisibility(View.INVISIBLE);
        }

        setActorType();

        date = todayDateFormatToSt_YMD();

        return true;
    }

    private void setActorType() {
        final String user_actor_type = String.valueOf(spinner_actorType.getSelectedItemId());
        switch (user_actor_type) {
            case "0":
                actor_type = String.valueOf(ActorType.Employee);
                break;
            case "1":
                actor_type = String.valueOf(ActorType.Resident);
                break;
        }
    }

    private String setActorId() {
        if (mIntentOperation != null) {
            actor_id = String.valueOf(mIntentOperation.actorId);
        } else {
            final String user_actor_id = String.valueOf(spinner_actorId.getSelectedItem());
            if (!user_actor_id.isEmpty()) {
                actor_id = user_actor_id;
                Log.d(TAG, "user_actor_id() GotSelected: " + user_actor_id);
            } else {
                Toast.makeText(this, "Please pick a valid id", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "setActorId: \"Pick an valid id\"");
            }
        }
        return actor_id;
    }

    private String setCateIdValue() {
        Log.d(TAG, "setCateIdValue() returned: " + mIntentOperation.categoryId);
        cateId = mIntentOperation.categoryId;
        return cateId;
    }

    private boolean checkData() {
        return !Objects.requireNonNull(amount_edt.getText()).toString().isEmpty()
                && !Objects.requireNonNull(details_edt.getText()).toString().isEmpty();
    }

    private void performUpdateForOp() {
        if (checkData()) {
            updateOp();
        } else {
            Snackbar.make(findViewById(R.id.snackBar_action), "Check Empty fields", Snackbar.LENGTH_LONG).show();
        }
    }

    private void updateOp() {
        if (saveUserEntryFromUi()) {
            OperationApiController.getInstance().updateOperation(id, cateId, amount, details, actor_id, actor_type, date, new ProcessCallback() {
                @Override
                public void onSuccess(String message) {
                    Log.d(TAG, "onSuccess() called with: message = [" + message + "]");
                    Snackbar.make(findViewById(R.id.snackBar_action), message, Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(String massage) {
                    Log.d(TAG, "onFailure() called with: massage = [" + massage + "]");
                    Snackbar.make(findViewById(R.id.snackBar_action), massage, Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(this, "Check your data entered", Toast.LENGTH_SHORT).show();
        }
    }

    private void setSpinner_actorIdEmp() {
        ContentApiController.getInstance().getEmployees(new ListCallback<Employee>() {
            @Override
            public void onSuccess(List<Employee> list) {
                for (int i = 0; i < list.size(); i++) {
                    empIds.add(String.valueOf(list.get(i).id));
//                    empIdsNames.add( "=> " + list.get(i).name);
                }
                ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(EditOperationActivity.this, android.R.layout.simple_list_item_1, empIds);
                spinner_actorId.setAdapter(stringArrayAdapter);
                sp_ac_enabled = true;
            }

            @Override
            public void onFailure(String message) {
                Snackbar.make(findViewById(R.id.snackBar_action), message, Snackbar.LENGTH_LONG).setAction("TryAgain", view -> setSpinner_actorIdEmp()).show();
//                Toast.makeText(EditOperationActivity.this, message, Toast.LENGTH_SHORT).show();
                spinner_actorId.setEnabled(false);
            }
        });
    }

    private void setSpinner_actorIdResident() {
        ContentApiController.getInstance().getUsers(new ListCallback<User>() {
            @Override
            public void onSuccess(List<User> list) {
                for (int i = 0; i < list.size(); i++) {
                    userIds.add(String.valueOf(list.get(i).id));
                }
                ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(EditOperationActivity.this, android.R.layout.simple_list_item_1, userIds);
                spinner_actorId.setAdapter(stringArrayAdapter);
                sp_ac_enabled = true;
            }

            @Override
            public void onFailure(String message) {
                Snackbar.make(findViewById(R.id.snackBar_action), message, Snackbar.LENGTH_LONG).setAction("TryAgain", view -> setSpinner_actorIdResident()).show();
//                Toast.makeText(EditOperationActivity.this, message, Toast.LENGTH_SHORT).show();
                spinner_actorId.setEnabled(false);
            }
        });
    }

    //Converters
    private String todayDateFormatToSt_YMD() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }


    private void setupSpinnerOperationAdapter() {
        ContentApiController.getInstance().getOperation(new ListCallback<Operation>() {
            @Override
            public void onSuccess(List<Operation> list) {
                for (int i = 0; i < list.size(); i++) {
                    opIds.add(String.valueOf(list.get(i).id));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditOperationActivity.this, android.R.layout.simple_list_item_1, opIds);
                spinner_opId.setAdapter(adapter);
            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "onFailure() returned: " + message);
                Snackbar.make(findViewById(R.id.snackBar_action), message, Snackbar.LENGTH_LONG).setAction("Refresh Spinner", view -> setupSpinnerOperationAdapter()).show();
            }
        });

    }

    private void opSelectedId() {
        id = Integer.parseInt(String.valueOf(spinner_opId.getSelectedItem()));
    }

    @Override
    public void onConfirmClicked() {
        performDeleteOp();
    }

    private void performDeleteOp() {
        if (!String.valueOf(id).isEmpty()) {
            OperationApiController.getInstance().deleteOperation(id, new ProcessCallback() {
                @Override
                public void onSuccess(String message) {
                    btn_submit.setEnabled(false);
                    btn_deleteOp.setBackgroundColor(getResources().getColor(R.color.special_green));
                    btn_deleteOp.setEnabled(false);
                    Snackbar.make(findViewById(R.id.snackBar_action), "Deleted Successfully=> " + message, Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(String massage) {
                    Snackbar.make(findViewById(R.id.snackBar_action), massage, Snackbar.LENGTH_LONG).setAction("TryAgain", view-> performDeleteOp()).show();
                }
            });
        } else {
            Snackbar.make(findViewById(R.id.snackBar_action), "Please pick a valid id", Snackbar.LENGTH_LONG).show();
        }
    }
}