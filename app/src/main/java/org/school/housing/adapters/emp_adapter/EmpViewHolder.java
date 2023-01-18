package org.school.housing.adapters.emp_adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.school.housing.R;
import org.school.housing.models.admin.Employee;

public class EmpViewHolder extends RecyclerView.ViewHolder {
    private TextView tv_name,tv_mobile,tv_id;
    private ImageView emp_imageView;
    public EmpViewHolder(@NonNull View itemView) {
        super(itemView);
        findViews();
    }
    private void findViews(){
        emp_imageView = itemView.findViewById(R.id.emp_image);

        tv_name = itemView.findViewById(R.id.emp_name);
        tv_mobile = itemView.findViewById(R.id.emp_mobile);
        tv_id = itemView.findViewById(R.id.emp_id);
    }
    @SuppressLint("SetTextI18n")
    protected void setData(Employee data){
        if (data.imageUrl!= null){
            Picasso.get().load(data.imageUrl).into(emp_imageView);
        }else {
            emp_imageView.setImageResource(R.drawable.image_no_image);
        }

        tv_name.setText(data.name);
        tv_mobile.setText(data.mobile);
        tv_id.setText("EMP_ID :"+data.id);
    }
}
