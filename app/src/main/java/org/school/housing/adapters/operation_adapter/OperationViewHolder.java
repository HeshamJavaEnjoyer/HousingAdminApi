package org.school.housing.adapters.operation_adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.school.housing.R;
import org.school.housing.models.Operation;

public class OperationViewHolder extends RecyclerView.ViewHolder {
    private TextView tv_opId, tv_categoryName, tv_opAmount, tv_opDetails, tv_opDate, tv_opActorId;
    public OperationViewHolder(@NonNull View itemView) {
        super(itemView);
        findViews();
    }
    private void findViews() {
        tv_opId = itemView.findViewById(R.id.operation_id);
        tv_categoryName = itemView.findViewById(R.id.category_name);
        tv_opAmount = itemView.findViewById(R.id.operation_amount);
        tv_opDetails = itemView.findViewById(R.id.operation_details);
        tv_opDate = itemView.findViewById(R.id.operation_date);
        tv_opActorId = itemView.findViewById(R.id.operation_actor_id);
    }
    @SuppressLint("SetTextI18n")
    protected void setData(Operation data) {
        tv_opId.setText("Op_ID :"+data.id);
        tv_categoryName.setText("Cate_name :"+data.categoryName);
        tv_opAmount.setText("Op_amount :"+data.amount);
        tv_opDetails.setText("Op_details :"+data.details);
        tv_opDate.setText("Op_date : "+data.date);
        tv_opActorId.setText("Actor_ID : "+data.actorId);
    }
}
