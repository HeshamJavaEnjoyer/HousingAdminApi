package org.school.housing.adapters.user_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.school.housing.R;
import org.school.housing.interfaces.RVClickListener;
import org.school.housing.models.admin.User;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    private final List<User> userList;
    private RVClickListener rvClickListener;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.setData(user);

        if (rvClickListener!=null){
            holder.itemView.setOnClickListener(view -> rvClickListener.onItemClick(user));
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setRvClickListener(RVClickListener rvClickListener) {
        this.rvClickListener = rvClickListener;
    }
}
