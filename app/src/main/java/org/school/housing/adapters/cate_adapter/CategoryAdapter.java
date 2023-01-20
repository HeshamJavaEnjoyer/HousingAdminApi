package org.school.housing.adapters.cate_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.school.housing.R;
//import org.school.housing.interfaces.RVClickListener;
import org.school.housing.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private final List<Category> categoryList;
//    private RVClickListener rvClickListener;

    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.setData(category);

//  1      if (rvClickListener!=null){
//            holder.itemView.setOnClickListener(view -> rvClickListener.onItemClick(category));
//        }

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

//  1  public void setRvClickListener(RVClickListener rvClickListener) {
//        this.rvClickListener = rvClickListener;
//    }
}
