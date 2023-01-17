package org.school.housing.adapters.emp_adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.school.housing.R;
import org.school.housing.models.admin.Employee;

public class EmpViewHolder extends RecyclerView.ViewHolder {
    private TextView tv_name,tv_mobile,tv_id;
    public EmpViewHolder(@NonNull View itemView) {
        super(itemView);
        findViews();
    }
    private void findViews(){
        tv_name = itemView.findViewById(R.id.emp_name);
        tv_mobile = itemView.findViewById(R.id.emp_mobile);
        tv_id = itemView.findViewById(R.id.emp_id);
    }
    @SuppressLint("SetTextI18n")
    protected void setData(Employee data){
        tv_name.setText(data.name);
        tv_mobile.setText(data.mobile);
        tv_id.setText("EMP_ID :"+data.id);
    }
}
