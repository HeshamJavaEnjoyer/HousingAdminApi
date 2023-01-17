package org.school.housing.adapters.adv_adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.school.housing.R;
import org.school.housing.models.admin.Advertisement;

public class AdvViewHolder extends RecyclerView.ViewHolder {
    private TextView tv_title,tv_info,tv_id;
    private ImageView adv_imageView;

    public AdvViewHolder(@NonNull View itemView) {
        super(itemView);
        findViews();
    }
    private void findViews(){
        adv_imageView = itemView.findViewById(R.id.adv_image);

        tv_title = itemView.findViewById(R.id.adv_title);
        tv_info = itemView.findViewById(R.id.adv_info);
        tv_id = itemView.findViewById(R.id.adv_id);

    }
    @SuppressLint("SetTextI18n")
    protected void setData(Advertisement data){
//        adv_imageView.set(Uri.parse(data.imageUrl));
        Picasso.get().load(data.imageUrl).into(adv_imageView);
        tv_id.setText("Adv_ID :"+data.id);
        tv_title.setText(data.title);
        tv_info.setText(data.info);
    }
}
