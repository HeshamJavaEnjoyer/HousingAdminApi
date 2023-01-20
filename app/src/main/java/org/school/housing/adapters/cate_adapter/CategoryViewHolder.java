package org.school.housing.adapters.cate_adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.school.housing.R;
import org.school.housing.models.Category;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    private TextView tv_name,tv_actionCount,tv_id,tv_total;
    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        findViews();
    }
    private void findViews(){
        tv_id = itemView.findViewById(R.id.category_id);
        tv_name = itemView.findViewById(R.id.category_name);
        tv_actionCount = itemView.findViewById(R.id.category_actionCount);
        tv_total = itemView.findViewById(R.id.category_total);
    }
    @SuppressLint("SetTextI18n")
    protected void setData(Category data){
        tv_id.setText("CATEGORY_ID :"+data.id);
        tv_name.setText(data.name);
        tv_actionCount.setText("ActionCount : "+data.actionsCount);
        tv_total.setText("Total :"+data.total);
    }
}
