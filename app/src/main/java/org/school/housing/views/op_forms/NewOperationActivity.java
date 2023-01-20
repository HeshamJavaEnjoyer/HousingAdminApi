package org.school.housing.views.op_forms;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.school.housing.R;
import org.school.housing.api.controllers.OperationApiController;
import org.school.housing.enums.ActorType;
import org.school.housing.enums.CategoryId;
import org.school.housing.interfaces.ProcessCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class NewOperationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NewOperationActivity";
    private Button btn_submit;
    private TextInputEditText amount_edt, details_edt;
    //,date_edt;
    private Spinner spinner_categoryId, spinner_actorId, spinner_actorType;

    //----------------containers
    private String cateId;
    //            = String.valueOf(CategoryId.ResidentService.id);
    private String amount;
    //= doubleAmountToSt(45.18);
    private String details;
//                = "This is a Details for this Transaction";

    //Related Var {
    private String actor_id;
    //            = actorIdToSt(167);
    //guide with if()
    private String actor_type;
    //        = ActorType.Employee.name();
    // }
    private String date;
//        = todayDateFormatToSt_YMD();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_operation);
    }

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

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        findViews();
        setEveryClicks();
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

    @Override
    public void onClick(View view) {
        if (view.getId() == btn_submit.getId()) {
            performStoreForOp();
        }
    }


    private boolean saveUserEntryFromUi() {
        final String user_cateId = String.valueOf(spinner_categoryId.getSelectedItemId());
        switch (user_cateId) {
            case "1":
                cateId = String.valueOf(CategoryId.ResidentService.id);
                break;
            case "2":
                cateId = String.valueOf(CategoryId.Salary.id);
                break;
            case "3":
                cateId = String.valueOf(CategoryId.Purchases.id);
                break;
            case "4":
                cateId = String.valueOf(CategoryId.Maintenance.id);
                break;
            default:
                return false;
        }// TODO: 1/20/2023 check here
//
        final String user_amount = Objects.requireNonNull(amount_edt.getText()).toString().trim();
        if (!user_amount.isEmpty()) {
            amount = user_amount;
        } else {
            return false;
        }
//                = doubleAmountToSt(45.18);
        final String user_details = Objects.requireNonNull(details_edt.getText()).toString().trim();
        if (!user_details.isEmpty()) {
            details = user_details;
        } else {
            return false;
        }
//                = "This is a Details for this Transaction";

        //Related Var {
        final String user_actor_id = String.valueOf(spinner_actorId.getSelectedItemId());
        if (!user_actor_id.isEmpty()) {// TODO: 1/20/2023 check here
            actor_id = user_actor_id;
        } else {
            return false;
        }
//                = actorIdToSt(167);
        //guide with if()
        final String user_actor_type = String.valueOf(spinner_actorType.getSelectedItemId());
        switch (user_actor_type) {
            case "1":
                actor_type = String.valueOf(ActorType.Employee);
                break;
            case "2":
                actor_type = String.valueOf(ActorType.Resident);
                break;
            default:
                return false;
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

    //from double to St
    private String doubleAmountToSt(double amount) {
        return String.valueOf(amount);
    }

    //from int to st
    private String actorIdToSt(int actor_id) {
        return String.valueOf(actor_id);
    }


}