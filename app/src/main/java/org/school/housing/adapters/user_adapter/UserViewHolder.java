package org.school.housing.adapters.user_adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.school.housing.R;
import org.school.housing.models.admin.User;

public class UserViewHolder extends RecyclerView.ViewHolder {
    private TextView tv_name,tv_email,tv_mobile,tv_id;
    private ImageView user_imageView;
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        findViews();
    }
    private void findViews(){
        user_imageView = itemView.findViewById(R.id.user_image);

        tv_name = itemView.findViewById(R.id.user_name);
        tv_email = itemView.findViewById(R.id.user_email);
        tv_mobile = itemView.findViewById(R.id.user_mobile);
        tv_id = itemView.findViewById(R.id.user_id);
    }
    @SuppressLint("SetTextI18n")
    protected void setData(User data){

        if (data.imageUrl!= null){
            Picasso.get().load(data.imageUrl).into(user_imageView);
        }else {
            user_imageView.setImageResource(R.drawable.ic_image_not_supported);
        }

        tv_name.setText(data.name);
        tv_email.setText(data.email);
        tv_mobile.setText(data.mobile);
        tv_id.setText("USER_ID :"+data.id);
    }
}
