package org.school.housing.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.school.housing.R;
import org.school.housing.adapters.cate_adapter.CategoryAdapter;
import org.school.housing.api.controllers.ContentApiController;
import org.school.housing.interfaces.ListCallback;
import org.school.housing.models.Category;
import org.school.housing.views.op_forms.NewOperationActivity;

import java.util.List;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView categories_recyclerView;
    private CategoryAdapter categoryAdapter;
    private Button btn_newOp;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setActBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //for Enabling the ->getting back to home Act Method
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setActBar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        inti();
    }

    private void inti() {
        findViews();
        setEveryClick();
        setupCategoryRecV();
    }

    private void findViews() {
        btn_newOp = findViewById(R.id.btn_newOp);
        categories_recyclerView = findViewById(R.id.recycler_categories);
    }

    private void setupCategoryRecV() {
        ContentApiController.getInstance().getCategories(new ListCallback<Category>() {
            @Override
            public void onSuccess(List<Category> list) {
                categoryAdapter = new CategoryAdapter(list);
                categories_recyclerView.setAdapter(categoryAdapter);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(DashboardActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setEveryClick() {
        btn_newOp.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_newOp:
                setIntent(NewOperationActivity.class);
                break;
            case R.id.adv_id:
                break;
            default:
                setIntent(MainActivity.class);
// 1           case R.id.btn_newOp:
//                setIntent(NewOperationActivity.class);
//                break;
        }
    }

    private void setIntent(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }


}