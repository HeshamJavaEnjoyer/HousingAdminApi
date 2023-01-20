package org.school.housing.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import org.school.housing.R;
import org.school.housing.adapters.cate_adapter.CategoryAdapter;
import org.school.housing.adapters.operation_adapter.OperationAdapter;
import org.school.housing.api.controllers.ContentApiController;
import org.school.housing.enums.Keys;
import org.school.housing.interfaces.ListCallback;
import org.school.housing.models.Category;
import org.school.housing.models.Operation;
import org.school.housing.views.op_forms.EditOperationActivity;
import org.school.housing.views.op_forms.NewOperationActivity;

import java.io.Serializable;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView categories_recyclerView, operation_recyclerView;
    private CategoryAdapter categoryAdapter;
    private OperationAdapter operationAdapter;
    private Button btn_newOp;
    private View view_badInternet;

    private CircularProgressIndicator progressBar_loading;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_editOperation) {
            setIntent(EditOperationActivity.class);
        }
        return super.onOptionsItemSelected(item);
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
        setupOperationRecV();
    }

    private void findViews() {
        btn_newOp = findViewById(R.id.btn_newOp);
        categories_recyclerView = findViewById(R.id.recycler_categories);
        operation_recyclerView = findViewById(R.id.recycler_operation);

        view_badInternet = findViewById(R.id.view_badInternet);
        progressBar_loading = findViewById(R.id.progressBar_loading);
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

    private void setupOperationRecV() {
        ContentApiController.getInstance().getOperation(new ListCallback<Operation>() {
            @Override
            public void onSuccess(List<Operation> list) {
                view_badInternet.setVisibility(View.INVISIBLE);
                progressBar_loading.setVisibility(View.INVISIBLE);
                operationAdapter = new OperationAdapter(list);
                operationAdapter.setRvClickListener(object -> {
                    Intent intent = new Intent(DashboardActivity.this, EditOperationActivity.class);
                    intent.putExtra(Keys.OperaKey.name(), (Serializable) object);
                    startActivity(intent);
                });
                operation_recyclerView.setAdapter(operationAdapter);
            }

            @Override
            public void onFailure(String message) {
                view_badInternet.setVisibility(View.VISIBLE);
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
        if (view.getId() == R.id.btn_newOp) {
            setIntent(NewOperationActivity.class);
        }
    }

    private void setIntent(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }


}