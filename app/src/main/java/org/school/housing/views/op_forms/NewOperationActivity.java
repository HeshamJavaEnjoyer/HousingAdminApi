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
import org.school.housing.enums.CategoryId;
import org.school.housing.interfaces.ListCallback;
import org.school.housing.interfaces.ProcessCallback;
import org.school.housing.models.admin.Employee;
import org.school.housing.models.admin.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class NewOperationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NewOperationActivity";
    private Button btn_submit;
    private TextInputEditText amount_edt, details_edt;
    //,date_edt;
    private Spinner spinner_categoryId, spinner_actorId, spinner_actorType;
    //----------------containers
    private String cateId, amount, details, actor_id, actor_type, date;
    //-----spinners
    private final List<String> empIds = new ArrayList<>();
//    private final List<String> empIdsNames = new ArrayList<>();
    private final List<String> userIds = new ArrayList<>();
//    private final List<String> userIdsNames = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_operation);
        setActBar();
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
        controlSpinnerChoose();
    }

    private void findViews() {
        spinner_categoryId = findViewById(R.id.spinner_category_id);
        amount_edt = findViewById(R.id.amount_edt);
        details_edt = findViewById(R.id.details_edt);
        spinner_actorId = findViewById(R.id.spinner_actor_id);
        spinner_actorType = findViewById(R.id.spinner_actor_type);

        btn_submit = findViewById(R.id.btn_submit_newOperation);
    }

    private void setEveryClicks() {
        btn_submit.setOnClickListener(this);
    }

    private void controlSpinnerChoose() {
        spinner_categoryId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Toast.makeText(NewOperationActivity.this, "Resident SoBe It", Toast.LENGTH_SHORT).show();
                        setSpinner_actorIdResident();
                        spinner_actorId.setEnabled(true);
                        break;
                    case 1:
                        Toast.makeText(NewOperationActivity.this, "Employee SoBe It", Toast.LENGTH_SHORT).show();
                        setSpinner_actorIdEmp();
                        spinner_actorId.setEnabled(true);
                        break;
                    case 3:
                    case 4:
                        spinner_actorId.setEnabled(false);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btn_submit.getId()) {
            performStoreForOp();
        }
    }


    private boolean saveUserEntryFromUi() {
        final String user_cateId = String.valueOf(spinner_categoryId.getSelectedItemId());
        switch (user_cateId) {
            case "0":
                cateId = String.valueOf(CategoryId.ResidentService.id);
//                setSpinner_actorIdResident();
                break;
            case "1":
                cateId = String.valueOf(CategoryId.Salary.id);
//                setSpinner_actorIdEmp();
                break;
            case "2":
                cateId = String.valueOf(CategoryId.Purchases.id);
                break;
            case "3":
                cateId = String.valueOf(CategoryId.Maintenance.id);
                break;
        }// TODO: 1/20/2023 check here
//
        final String user_amount = Objects.requireNonNull(amount_edt.getText()).toString().trim();
        if (!user_amount.isEmpty()) {
            amount = user_amount;
        } else {
            Log.d(TAG, "user_amount() null");
            return false;
        }
//                = doubleAmountToSt(45.18);
        final String user_details = Objects.requireNonNull(details_edt.getText()).toString().trim();
        if (!user_details.isEmpty()) {
            details = user_details;
        } else {
            Log.d(TAG, "user_details() null");
            return false;
        }
//                = "This is a Details for this Transaction";

        //Related Var {
        final String user_actor_id = String.valueOf(spinner_actorId.getSelectedItem());
        if (!user_actor_id.isEmpty()) {// TODO: 1/20/2023 check here
            actor_id = user_actor_id;
            Log.d(TAG, "user_actor_id() returned: " + user_actor_id);
        } else {
            Log.d(TAG, "user_actor_id() returned: null");
            Log.d(TAG, "user_actor_id() returned: " + user_actor_id);
            return false;
        }
//                = actorIdToSt(167);
        //guide with if()
        final String user_actor_type = String.valueOf(spinner_actorType.getSelectedItemId());
        switch (user_actor_type) {
            case "0":
                actor_type = String.valueOf(ActorType.Employee);
                break;
            case "1":
                actor_type = String.valueOf(ActorType.Resident);
                break;
        }// TODO: 1/20/2023 check here

//                = ActorType.Employee.name();
        // }

        //and finally today date
        date = todayDateFormatToSt_YMD();
//                = todayDateFormatToSt_YMD();
        return true;
    }

    private boolean checkData() {
        return !Objects.requireNonNull(amount_edt.getText()).toString().isEmpty()
                && !Objects.requireNonNull(details_edt.getText()).toString().isEmpty();
    }

    private void performStoreForOp() {
        if (checkData()) {
            storeOp();
        } else {
            Snackbar.make(findViewById(R.id.snackBar_action), "Check Empty fields", Snackbar.LENGTH_LONG).show();
        }
    }

    private void storeOp() {
        if (saveUserEntryFromUi()) {
            OperationApiController.getInstance().storeOperation(cateId, amount, details, actor_id, actor_type, date, new ProcessCallback() {
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
                ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(NewOperationActivity.this, android.R.layout.simple_list_item_1, empIds);
                spinner_actorId.setAdapter(stringArrayAdapter);
            }

            @Override
            public void onFailure(String message) {
                Snackbar.make(findViewById(R.id.snackBar_action), message, Snackbar.LENGTH_LONG).setAction("TryAgain", view -> setSpinner_actorIdEmp()).show();
//                Toast.makeText(NewOperationActivity.this, message, Toast.LENGTH_SHORT).show();
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
                ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(NewOperationActivity.this, android.R.layout.simple_list_item_1, userIds);
                spinner_actorId.setAdapter(stringArrayAdapter);
            }

            @Override
            public void onFailure(String message) {
                Snackbar.make(findViewById(R.id.snackBar_action), message, Snackbar.LENGTH_LONG).setAction("TryAgain", view -> setSpinner_actorIdResident()).show();
//                Toast.makeText(NewOperationActivity.this, message, Toast.LENGTH_SHORT).show();
                spinner_actorId.setEnabled(false);
            }
        });
    }

    //Converters
    /*
    date Method Gives TodayDate Always
    @todayDateFormatToSt_YMD();
    */
    private String todayDateFormatToSt_YMD() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }


}

/*
  @deprecated {@code @testForStoreOp}
//WorksFine./
private void testForStoreOp() {
//"category_id": "1",
//"amount": "100",
//"details": "Details",
//"date": "2023-01-01",
//"actor_id": 167,
//"id": 306,

//"category_id":[1,2,3,4] -dep on FixedCategory
//"amount": =99.9,
//"details": "bullshit",
//"date": "2023-01-01", Y - M - D
//"actor_type": [Employee/Resident], Must Pick one Type either Emp or Resident
//"actor_id": 167, Must be a valid id for Emp or Resident And belong to Token
//"id": 306, I GUESS THIS just the op id


OperationApiController.getInstance().storeOperation(cateId, amount, details, actor_id, actor_type, date, new ProcessCallback() {
@Override
public void onSuccess(String message) {
Log.d(TAG, "onSuccess() called with: message = [" + message + "]");
Toast.makeText(NewOperationActivity.this, message, Toast.LENGTH_SHORT).show();
}

@Override
public void onFailure(String massage) {
Log.d(TAG, "onSuccess() called with: message = [" + massage + "]");
Toast.makeText(NewOperationActivity.this, massage, Toast.LENGTH_SHORT).show();
}
});
}

 */

/*
    //from double to St
    private String doubleAmountToSt(double amount) {
        return String.valueOf(amount);
    }
    */
/*
    //from int to st
    private String actorIdToSt(int actor_id) {
        return String.valueOf(actor_id);
    }

 */

