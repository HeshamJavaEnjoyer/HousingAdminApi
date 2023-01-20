package org.school.housing.adapters.operation_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.school.housing.R;
import org.school.housing.interfaces.RVClickListener;
import org.school.housing.models.Operation;
import java.util.List;

public class OperationAdapter extends RecyclerView.Adapter<OperationViewHolder>{
    private final List<Operation> operationList;
    private RVClickListener rvClickListener;

    public OperationAdapter(List<Operation> operationList) {
        this.operationList = operationList;
    }

    @NonNull
    @Override
    public OperationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_operation,parent,false);
        return new OperationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OperationViewHolder holder, int position) {
        Operation operation = operationList.get(position);
        holder.setData(operation);

        if (rvClickListener!=null){
            holder.itemView.setOnClickListener(view -> rvClickListener.onItemClick(operation));
        }
    }

    @Override
    public int getItemCount() {
        return operationList.size();
    }

    public void setRvClickListener(RVClickListener rvClickListener) {
        this.rvClickListener = rvClickListener;
    }
}
