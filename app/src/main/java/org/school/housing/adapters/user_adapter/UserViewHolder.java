package org.school.housing.adapters.user_adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.school.housing.R;
import org.school.housing.models.admin.User;

public class UserViewHolder extends RecyclerView.ViewHolder {
    private TextView tv_name,tv_email,tv_mobile,tv_id;
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        findViews();
    }
    private void findViews(){
        tv_name = itemView.findViewById(R.id.user_name);
        tv_email = itemView.findViewById(R.id.user_email);
        tv_mobile = itemView.findViewById(R.id.user_mobile);
        tv_id = itemView.findViewById(R.id.user_id);
    }
    @SuppressLint("SetTextI18n")
    protected void setData(User data){
        tv_name.setText(data.name);
        tv_email.setText(data.email);
        tv_mobile.setText(data.mobile);
        tv_id.setText("USER_ID :"+data.id);
    }
}
