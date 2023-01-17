package org.school.housing.adapters.emp_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.school.housing.R;
import org.school.housing.interfaces.RVClickListener;
import org.school.housing.models.admin.Employee;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmpViewHolder> {
    private final List<Employee> employeeList;
    private RVClickListener rvClickListener;

    public EmployeeAdapter(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    @NonNull
    @Override
    public EmpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee,parent,false);
        return new EmpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpViewHolder holder, int position) {
        Employee employee = employeeList.get(position);
        holder.setData(employee);

        if (rvClickListener!=null){
            holder.itemView.setOnClickListener(view -> rvClickListener.onItemClick(employee));
        }

    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public void setRvClickListener(RVClickListener rvClickListener) {
        this.rvClickListener = rvClickListener;
    }
}
